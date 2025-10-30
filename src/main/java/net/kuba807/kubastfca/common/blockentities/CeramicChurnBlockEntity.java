/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.kuba807.kubastfca.common.blockentities;

import net.dries007.tfc.common.blockentities.TickableInventoryBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;

import net.dries007.tfc.client.TFCSounds;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.rotation.RotationSinkBlockEntity;
import net.dries007.tfc.common.blocks.devices.QuernBlock;
import net.dries007.tfc.common.capabilities.PartialItemHandler;
import net.dries007.tfc.common.items.TFCItems;
import net.dries007.tfc.common.recipes.QuernRecipe;
import net.dries007.tfc.config.TFCConfig;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.rotation.NetworkAction;
import net.dries007.tfc.util.rotation.Node;
import net.dries007.tfc.util.rotation.SinkNode;

public class CeramicChurnBlockEntity extends TickableInventoryBlockEntity<ItemStackHandler> implements RotationSinkBlockEntity
{
    public static final int SLOT_INPUT = 1;
    public static final int SLOT_OUTPUT = 2;

    public static final int MANUAL_TICKS = 90;
    public static final float MANUAL_SPEED = Mth.TWO_PI / MANUAL_TICKS; // In radians / tick

    private static final float MANUAL_RECIPE_PER_TICK = 1f; // Exactly 90 ticks at 1/tick
    private static final float NETWORK_RECIPE_PER_SPEED = MANUAL_TICKS / Mth.TWO_PI; // progress / radian

    public static void serverTick(Level level, BlockPos pos, BlockState state, CeramicChurnBlockEntity ceramic_butter_churn)
    {
        final ServerLevel serverLevel = (ServerLevel) level;

        ceramic_butter_churn.checkForLastTickSync();


        final boolean wasGrinding = ceramic_butter_churn.recipeTimer > 0;

        clientTick(level, pos, state, ceramic_butter_churn);

        if (wasGrinding)
        {
            final ItemStack inputStack = ceramic_butter_churn.inventory.getStackInSlot(SLOT_INPUT);
            if (!inputStack.isEmpty())
            {
                sendParticle(serverLevel, pos, inputStack, 1);
            }
        }

        if (wasGrinding && ceramic_butter_churn.recipeTimer <= 0)
        {
            ceramic_butter_churn.finishGrinding();
            Helpers.playSound(level, pos, SoundEvents.ARMOR_STAND_FALL);

            if (ceramic_butter_churn.isConnectedToNetwork())
            {
                // If possible, immediately restart
                ceramic_butter_churn.startGrinding();
            }
        }

        if (ceramic_butter_churn.isConnectedToNetwork() && !ceramic_butter_churn.isGrinding() && level.getGameTime() % 10 == 0)
        {
            ceramic_butter_churn.startGrinding();
        }
    }

    public static void clientTick(Level level, BlockPos pos, BlockState state, CeramicChurnBlockEntity ceramic_butter_churn)
    {
        if (ceramic_butter_churn.recipeTimer > 0)
        {
            if (ceramic_butter_churn.node.rotation() != null)
            {
                ceramic_butter_churn.previousRotationDirection = ceramic_butter_churn.node.rotation().direction() == Direction.UP ? 1 : -1;
                ceramic_butter_churn.previousRotationSpeed = ceramic_butter_churn.getRotationSpeed();
            }
            ceramic_butter_churn.recipeTimer -= ceramic_butter_churn.isConnectedToNetwork()
                ? ceramic_butter_churn.getRotationSpeed() * NETWORK_RECIPE_PER_SPEED
                : MANUAL_RECIPE_PER_TICK;
        }
    }

    private static void sendParticle(ServerLevel level, BlockPos pos, ItemStack item, int count)
    {
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, item), pos.getX() + 0.5D, pos.getY() + 0.875D, pos.getZ() + 0.5D, count, Helpers.triangle(level.random) / 2.0D, level.random.nextDouble() / 4.0D, Helpers.triangle(level.random) / 2.0D, 0.15f);
    }

    private final SinkNode node;

    private float recipeTimer;
    private boolean needsStateUpdate = false;
    private float previousRotationDirection = 1;
    private float previousRotationSpeed = MANUAL_SPEED;

    public CeramicChurnBlockEntity(BlockPos pos, BlockState state)
    {
        super(KubastfcaBlockEntities.CERAMIC_BUTTER_CHURN.get(), pos, state, defaultInventory(3));

        this.recipeTimer = 0;
        this.node = new SinkNode(pos, Direction.UP) {
            @Override
            public String toString()
            {
                return "Quern[pos=%s]".formatted(pos());
            }
        };

    }


    @Override
    public void setAndUpdateSlots(int slot)
    {
        super.setAndUpdateSlots(slot);
        needsStateUpdate = true;
        markForSync();
    }

    @Override
    public int getSlotStackLimit(int slot)
    {
        return  64;
    }


    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        recipeTimer = nbt.getFloat("recipeTimer");
        super.loadAdditional(nbt, provider);
        needsStateUpdate = true;
    }

    @Override
    public void saveAdditional(CompoundTag nbt, HolderLookup.Provider provider)
    {
        nbt.putFloat("recipeTimer", recipeTimer);
        super.saveAdditional(nbt, provider);
    }

    @Override
    public boolean canInteractWith(Player player)
    {
        return super.canInteractWith(player) && recipeTimer <= 0 && !isConnectedToNetwork();
    }

    /**
     * @return if a recipe is being executed
     */
    public boolean isGrinding()
    {
        return recipeTimer > 0;
    }


    /**
     * Attempts to start grinding. Returns {@code true} if it did.
     */
    public boolean startGrinding()
    {
        assert level != null;
        final ItemStack inputStack = inventory.getStackInSlot(SLOT_INPUT);

        if (!inputStack.isEmpty())
        {
            final QuernRecipe recipe = QuernRecipe.getRecipe(inputStack);
            if (recipe != null && recipe.matches(inputStack))
            {
                recipeTimer = MANUAL_TICKS;
                level.playSound(null, worldPosition, TFCSounds.QUERN_DRAG.get(), SoundSource.BLOCKS, 1, 1 + ((level.random.nextFloat() - level.random.nextFloat()) / 16));
                markForSync();
                previousRotationDirection = 1;
                previousRotationSpeed = MANUAL_SPEED;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onLoadAdditional()
    {
        performNetworkAction(NetworkAction.ADD);
    }

    @Override
    protected void onUnloadAdditional()
    {
        performNetworkAction(NetworkAction.REMOVE);
    }

    @Override
    public Node getRotationNode()
    {
        return node;
    }

    public float getRotationSpeed()
    {
        return node.rotation() != null ? Mth.abs(node.rotation().speed()) : (isGrinding() ? previousRotationSpeed : 0f);
    }

    @Override
    public float getRotationAngle(float partialTick)
    {
        return isConnectedToNetwork()
            ? RotationSinkBlockEntity.super.getRotationAngle(partialTick)
            : -recipeTimer * previousRotationSpeed * previousRotationDirection;
    }

    public boolean isConnectedToNetwork()
    {
        return node.rotation() != null ;
    }

    private void finishGrinding()
    {
        assert level != null;
        final ItemStack inputStack = inventory.getStackInSlot(SLOT_INPUT);
        if (!inputStack.isEmpty())
        {
            final QuernRecipe recipe = QuernRecipe.getRecipe(inputStack);
            if (recipe != null && recipe.matches(inputStack))
            {
                ItemStack outputStack = recipe.assemble(inputStack);
                outputStack = Helpers.mergeInsertStack(inventory, SLOT_OUTPUT, outputStack);
                if (!outputStack.isEmpty() && !level.isClientSide)
                {
                    Helpers.spawnItem(level, worldPosition, outputStack);
                }

                // Shrink the input stack after the recipe is done assembling
                inputStack.shrink(1);
                markForSync();
            }
        }

        recipeTimer = 0f;
    }
}
