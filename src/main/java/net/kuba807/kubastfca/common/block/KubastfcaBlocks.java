package net.kuba807.kubastfca.common.block;


import net.kuba807.kubastfca.common.block.crop.Crop;


import net.dries007.tfc.util.Helpers;
import net.kuba807.kubastfca.common.fluids.KubastfcaFluids;
import net.kuba807.kubastfca.common.fluids.DefaultFluids;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.Nullable;

import net.dries007.tfc.util.registry.RegistrationHelpers;
import net.dries007.tfc.util.registry.RegistryHolder;

import static net.kuba807.kubastfca.kubastfca.MODID;

public class KubastfcaBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Registries.BLOCK, MODID);



    public static final Map<DefaultFluids, Id<LiquidBlock>> DEFAULT_FLUIDS = Helpers.mapOf(DefaultFluids.class, fluid ->
            registerNoItem("fluid/" + fluid.getId(), () -> new LiquidBlock(KubastfcaFluids.DEFAULT_FLUIDS.get(fluid).getSource(), BlockBehaviour.Properties.ofFullCopy(Blocks.WATER).noLootTable()))
    );




    public static final Map<Crop, Id<Block>> CROPS = Helpers.mapOf(Crop.class, crop ->
            registerNoItem("crop/" + crop.name(), crop::create)
    );

    public static final Map<Crop, Id<Block>> DEAD_CROPS = Helpers.mapOf(Crop.class, crop ->
            registerNoItem("dead_crop/" + crop.name(), crop::createDead)
    );

    public static final Map<Crop, Id<Block>> WILD_CROPS = Helpers.mapOf(Crop.class, crop ->
            register("wild_crop/" + crop.name(), crop::createWild)
    );


  // public static boolean always(BlockState state, BlockGetter level, BlockPos pos)
  // {
  //     return true;
  // }

  // public static boolean never(BlockState state, BlockGetter level, BlockPos pos)
  // {
  //     return false;
  // }

  // public static boolean neverEntity(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> type)
  // {
  //     return false;
  // }


  // public static ToIntFunction<BlockState> alwaysLit()
  // {
  //     return s -> 15;
  // }

  // public static ToIntFunction<BlockState> lavaLoggedBlockEmission()
  // {
    //    return state -> state.getValue(((IFluidLoggable) state.getBlock()).getFluidProperty()).is(Fluids.LAVA) ? 15 : 0;
    //}

   // public static ToIntFunction<BlockState> litBlockEmission(int lightValue)
   // {
   //     return state -> state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
   // }
    private static <T extends Block> Id<T> registerNoItem(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, (Function<T, ? extends BlockItem>) null);
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, new Item.Properties()));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, Item.Properties blockItemProperties)
    {
        return register(name, blockSupplier, block -> new BlockItem(block, blockItemProperties));
    }

    private static <T extends Block> Id<T> register(String name, Supplier<T> blockSupplier, @Nullable Function<T, ? extends BlockItem> blockItemFactory)
    {
        return new Id<>(RegistrationHelpers.registerBlock(KubastfcaBlocks.BLOCKS, KubastfcaItems.ITEMS, name, blockSupplier, blockItemFactory));
    }


    public record Id<T extends Block>(DeferredHolder<Block, T> holder) implements RegistryHolder<Block, T>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get().asItem();
        }
    }
}
