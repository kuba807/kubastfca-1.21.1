package net.kuba807.kubastfca.common.item;

import net.dries007.tfc.common.Lore;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HangingSignItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SignItem;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
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

    public static final DeferredItem<Item> RAW_POPPY_ROLL= ITEMS.registerSimpleItem(
            "raw_poppy_roll", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.POISON, 100),0.8f).build()));
    public static final DeferredItem<Item> COOKED_POPPY_ROLL= ITEMS.registerSimpleItem(
            "cooked_poppy_roll", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).build()));

  public static final DeferredItem<Item> MEAT_WEK= ITEMS.register("jar/meat", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));

  public static final DeferredItem<Item> UNSEALED_MEAT_WEK= ITEMS.register("jar/meat_unsealed", () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(TFCItems.EMPTY_JAR.asItem())));

 //  public static final DeferredItem<Item> MIX_WEK= ITEMS.register("jar/mix", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));
 //  public static final DeferredItem<Item> UNSEALED_MIX_WEK= ITEMS.register("jar/mix_unsealed", () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(TFCItems.EMPTY_JAR.asItem())));

 //  public static final DeferredItem<Item> VEGGIE_WEK= ITEMS.register("jar/veggie", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));
 //  public static final DeferredItem<Item> UNSEALED_VEGGIE_WEK= ITEMS.register("jar/veggie_unsealed", () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(TFCItems.EMPTY_JAR.asItem())));


}
