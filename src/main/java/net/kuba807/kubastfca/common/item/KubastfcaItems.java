package net.kuba807.kubastfca.common.item;

import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.Lore;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryHolder;
import net.kuba807.kubastfca.common.block.KubastfcaBlocks;
import net.kuba807.kubastfca.common.block.crop.Crop;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredHolder;


import static net.kuba807.kubastfca.kubastfca.MODID;
import static net.dries007.tfc.common.items.TFCItems.EMPTY_JAR;

public class KubastfcaItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    // Creates a new Block with the id "kubastfca:example_block", combining the namespace and path
    //public static final DeferredBlock<Block> EXAMPLE_BLOCK = BLOCKS.registerSimpleBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE));
    // Creates a new BlockItem with the id "kubastfca:example_block", combining the namespace and path
    public static final DeferredItem<Item> PEMMICAN= ITEMS.registerSimpleItem(
            "pemmican", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 100),0.03f).build()));
    // The properties to use.
    public static final DeferredItem<Item> DIRTY_JAR= ITEMS.registerSimpleItem(
            "jar/dirty_jar", new Item.Properties());

    public static final DeferredItem<Item> COOKED_PASTA= ITEMS.registerSimpleItem(
            "cooked_pasta", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<Item> RAW_PASTA= ITEMS.registerSimpleItem(
            "raw_pasta", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 100),0.1f).build()));

    public static final DeferredItem<Item> COOKED_DUMPLING= ITEMS.registerSimpleItem(
            "cooked_dumpling", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).build()));
    public static final DeferredItem<Item> RAW_DUMPLING= ITEMS.registerSimpleItem(
            "raw_dumpling", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 100),0.1f).build()));

    public static final DeferredItem<Item> RAW_POPPY_ROLL= ITEMS.registerSimpleItem(
            "raw_poppy_roll", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).effect(() -> new MobEffectInstance(MobEffects.HUNGER, 100),0.1f).build()));
    public static final DeferredItem<Item> COOKED_POPPY_ROLL= ITEMS.registerSimpleItem(
            "cooked_poppy_roll", new Item.Properties().food(new FoodProperties.Builder()
                    .nutrition(1).saturationModifier(2f).build()));

  public static final DeferredItem<Item> MEAT_WEK= ITEMS.register("jar/meat", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));
  public static final DeferredItem<Item> UNSEALED_MEAT_WEK= ITEMS.register("jar/meat_unsealed",  () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(DIRTY_JAR.asItem()).stacksTo(4)));

  public static final DeferredItem<Item> MIX_WEK= ITEMS.register("jar/mix", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));
  public static final DeferredItem<Item> UNSEALED_MIX_WEK= ITEMS.register("jar/mix_unsealed", () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(KubastfcaItems.DIRTY_JAR.asItem()).stacksTo(4)));

  public static final DeferredItem<Item> VEGGIE_WEK= ITEMS.register("jar/veggie", () -> new Item(new Properties().component(Lore.TYPE, Lore.SEALED)));
  public static final DeferredItem<Item> UNSEALED_VEGGIE_WEK= ITEMS.register("jar/veggie_unsealed", () -> new Item(new Properties().component(Lore.TYPE, Lore.UNSEALED).craftRemainder(DIRTY_JAR.asItem()).stacksTo(4)));


    public static final Map<Crop, Object> CROP_SEEDS = Helpers.mapOf(Crop.class, crop ->
            register("seeds/" + crop.name(), () -> new ItemNameBlockItem(KubastfcaBlocks.CROPS.get(crop).get(), new Properties()))
    );

    private static ItemId register(String name)
    {
        return register(name, () -> new Item(new Properties()));
    }

    private static ItemId register(String name, Properties properties)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), () -> new Item(properties)));
    }

    private static ItemId register(String name, Supplier<Item> item)
    {
        return new ItemId(ITEMS.register(name.toLowerCase(Locale.ROOT), item));
    }

    public record ItemId(DeferredHolder<Item, Item> holder) implements RegistryHolder<Item, Item>, ItemLike
    {
        @Override
        public Item asItem()
        {
            return get();
        }
    }

}
