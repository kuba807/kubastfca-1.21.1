package net.kuba807.kubastfca.common.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;


import static net.kuba807.kubastfca.kubastfca.MODID;

public class KubastfcaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Creates a new Block with the id "kubastfca:example_block", combining the namespace and path
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "kubastfca:example_block", combining the namespace and path
    public static final DeferredItem<Item> PEMMICAN= ITEMS.registerSimpleItem(
            "pemmican", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 100),0.03f).build()));
    // The properties to use.
    public static final DeferredItem<Item> COOKED_PASTA= ITEMS.registerSimpleItem(
            "cooked_pasta", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<Item> RAW_PASTA= ITEMS.registerSimpleItem(
            "raw_pasta", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.POISON, 100),0.8f).build()));

}
