/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.kuba807.kubastfca.common.fluids;

import java.util.Map;

import net.dries007.tfc.common.fluids.FluidHelpers;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;


import net.dries007.tfc.mixin.accessor.FlowingFluidAccessor;
import net.dries007.tfc.util.Helpers;

public abstract class MixingFluid extends BaseFlowingFluid
{
    protected MixingFluid(Properties properties)
    {
        super(properties);
    }


    public int sourceNeighborCount(LevelReader worldIn, BlockPos pos)
    {
        int adjacentSources = 0;
        for (Direction direction : Direction.Plane.HORIZONTAL)
        {
            BlockPos adjacentPos = pos.relative(direction);
            FluidState adjacentFluid = worldIn.getFluidState(adjacentPos);
            if (isSourceBlockOfThisType(adjacentFluid))
            {
                adjacentSources++;
            }
        }
        return adjacentSources;
    }


    public boolean isSourceBlockOfThisType(FluidState stateIn)
    {
        return stateIn.getType().isSame(this) && stateIn.isSource();
    }

  // @Override
  // public boolean isSame(Fluid fluid) {
  //     return super.isSame(fluid) || Helpers.isFluid(fluid, TFCTags.Fluids.ANY_INFINITE_WATER);
  // }


    public void spreadToSides(Level level, BlockPos pos, FluidState sourceFluid, BlockState sourceState)
    {
        int adjacentAmount = sourceFluid.getAmount() - getDropOff(level);
        if (sourceFluid.getValue(FALLING))
        {
            // Falling indicates this fluid is being fed from above - this is then going to spread like a source block (8 - drop off)
            adjacentAmount = 7;
        }
        if (adjacentAmount > 0)
        {
            // Calculate where the fluid should spread based on each direction
            Map<Direction, FluidState> map = getSpread(level, pos, sourceState);
            for (Map.Entry<Direction, FluidState> entry : map.entrySet())
            {
                final Direction direction = entry.getKey();
                final FluidState spreadFluid = entry.getValue();
                final BlockPos destPos = pos.relative(direction);
                final BlockState destState = level.getBlockState(destPos);
                if (canSpreadTo(level, pos, sourceState, direction, destPos, destState, level.getFluidState(destPos), spreadFluid.getType()))
                {
                    spreadTo(level, destPos, destState, direction, spreadFluid);
                }
            }
        }
    }

    @Override
    protected void spread(Level level, BlockPos pos, FluidState stateIn)
    {
        // Only spread if the current state has actual fluid
        if (!stateIn.isEmpty())
        {
            BlockState blockStateAt = level.getBlockState(pos);
            BlockPos posBelow = pos.below();
            BlockState blockStateBelow = level.getBlockState(posBelow);

            // First, try and flow downwards. Calculate a fluid state directly below this one
            FluidState fluidstate = getNewLiquid(level, posBelow, blockStateBelow);

            // This checks if the block border is passable, and that the below fluid state returns true to being replaced with this fluid state
            if (canSpreadTo(level, pos, blockStateAt, Direction.DOWN, posBelow, blockStateBelow, level.getFluidState(posBelow), fluidstate.getType()))
            {
                // Try and spread directly below
                spreadTo(level, posBelow, blockStateBelow, Direction.DOWN, fluidstate);

                // Count the number of adjacent blocks horizontally
                // A fluid (regardless of source vs. flowing) will always spread to the sides when there are three or more neighboring source blocks
                // This, notably, happens regardless of if the source blocks are passable
                if (this.sourceNeighborCount(level, pos) >= 3)
                {
                    spreadToSides(level, pos, stateIn, blockStateAt);
                }
            }
            else if (stateIn.isSource() || !((FlowingFluidAccessor) this).invoke$isWaterHole(level, fluidstate.getType(), pos, blockStateAt, posBelow, blockStateBelow))
            {
                // Source blocks, if they can't spread downwards, will always spread sideways (this happens one tick after they spread downwards)
                // Flowing blocks will only spread sideways if they can't fall downwards, either to a water hole, or directly falling down (the above if chain)
                spreadToSides(level, pos, stateIn, blockStateAt);
            }
        }
    }

    /**
     * Modified to use mixing mechanics, in such a way that can be exposed to other subclasses
     */
    @Override
    protected FluidState getNewLiquid(Level level, BlockPos pos, BlockState blockStateIn)
    {
        return FluidHelpers.getNewFluidWithMixing(this, level, pos, blockStateIn, getDropOff(level));
    }

    @Override
    public void tick(Level level, BlockPos pos, FluidState state)
    {
        if (!state.isSource())
        {
            // Flowing fluid ticks
            // Inside this statement, we know we're in a non-waterlogged block, as flowing fluids cannot be waterlogged.
            FluidState fluidAt = getNewLiquid(level, pos, level.getBlockState(pos));
            int spreadDelay = getSpreadDelay(level, pos, state, fluidAt);
            if (fluidAt.isEmpty())
            {
                // The current state should have no fluid in it - set the block to empty, and then call spread with an empty fluid (mojang why?)
                state = fluidAt;
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            }
            else if (!fluidAt.equals(state)) // equals() is not overridden, so this is just a state object equality check
            {
                // The new fluid state is not the same as the existing one. In vanilla, this could happen if the state height was changed, or if it was converted into a source block.
                // In tfc, this may also happen when a fluid with a larger amount replaces this one.
                state = fluidAt;
                BlockState blockstate = fluidAt.createLegacyBlock();
                level.setBlock(pos, blockstate, 2);
                level.scheduleTick(pos, fluidAt.getType(), spreadDelay);
                level.updateNeighborsAt(pos, blockstate.getBlock());
            }
        }
        spread(level, pos, state);
    }

    /**
     * From {@link net.minecraft.world.level.material.WaterFluid#animateTick}
     */
    @Override
    public void animateTick(Level level, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                level.playLocalSound(
                    (double)pos.getX() + 0.5,
                    (double)pos.getY() + 0.5,
                    (double)pos.getZ() + 0.5,
                    SoundEvents.WATER_AMBIENT,
                    SoundSource.BLOCKS,
                    random.nextFloat() * 0.25F + 0.75F,
                    random.nextFloat() + 0.5F,
                    false
                );
            }
        } else if (random.nextInt(10) == 0) {
            level.addParticle(
                ParticleTypes.UNDERWATER,
                (double)pos.getX() + random.nextDouble(),
                (double)pos.getY() + random.nextDouble(),
                (double)pos.getZ() + random.nextDouble(),
                0.0,
                0.0,
                0.0
            );
        }
    }

    @Nullable
    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.DRIPPING_WATER;
    }

    public static class Flowing extends MixingFluid
    {
        public Flowing(Properties properties)
        {
            super(properties);
        }

        public boolean isSource(FluidState state)
        {
            return false;
        }

        public int getAmount(FluidState state)
        {
            return state.getValue(LEVEL);
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder)
        {
            super.createFluidStateDefinition(builder.add(LEVEL));
        }
    }

    public static class Source extends MixingFluid
    {
        public Source(Properties properties)
        {
            super(properties);
        }

        public boolean isSource(FluidState state)
        {
            return true;
        }

        public int getAmount(FluidState state)
        {
            return 8;
        }
    }
}
