package net.kuba807.kubastfca;

import net.kuba807.kubastfca.client.ClientEventHandler;
import net.kuba807.kubastfca.common.fluids.KubastfcaFluids;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.kuba807.kubastfca.common.recipes.KubaRecipeSerializers;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.kuba807.kubastfca.common.block.KubastfcaBlocks;


// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(kubastfca.MODID)
public class kubastfca {

    // Define mod id in a common place for everything to reference
    public static final String MODID = "kubastfca";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "kubastfca" namespace

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "kubastfca" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);



    // Creates a new food item with the id "kubastfca:example_id", nutrition 1 and saturation 2

    // Creates a creative tab with the id "kubastfca:example_tab" for the example item, that is placed after the combat tab
 public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("kubastfca", () -> CreativeModeTab.builder()
         .title(Component.translatable("itemGroup.kubastfca")) //The language key for the title of your CreativeModeTab
         .withTabsBefore(CreativeModeTabs.COMBAT)
         .icon(() -> KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance())
         .displayItems((parameters, output) -> {
             output.accept(KubastfcaItems.COOKED_DUMPLING.get().getDefaultInstance());
             output.accept(KubastfcaItems.RAW_DUMPLING.get().getDefaultInstance());
             output.accept(KubastfcaItems.COOKED_POPPY_ROLL.get().getDefaultInstance());
             output.accept(KubastfcaItems.RAW_POPPY_ROLL.get().getDefaultInstance());
             output.accept(KubastfcaItems.COOKED_PASTA.get().getDefaultInstance());
             output.accept(KubastfcaItems.RAW_PASTA.get().getDefaultInstance());
             output.accept(KubastfcaItems.PEMMICAN.get().getDefaultInstance());
             output.accept(KubastfcaItems.MEAT_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.UNSEALED_MEAT_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.MIX_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.UNSEALED_MIX_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.VEGGIE_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.UNSEALED_VEGGIE_WEK.get().getDefaultInstance());
             output.accept(KubastfcaItems.DIRTY_JAR.get().getDefaultInstance());
             output.accept(KubastfcaItems.COTTAGE_CHEESE.get().getDefaultInstance());




             // Add the example item to the tab. For your own tabs, this method is preferred over the event
         }).build());

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public kubastfca(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        //BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        KubastfcaItems.ITEMS.register(modEventBus);

        KubastfcaBlocks.BLOCKS.register(modEventBus);
        KubastfcaFluids.FLUIDS.register(modEventBus);
        KubastfcaFluids.FLUID_TYPES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        KubaRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);


        if (FMLEnvironment.dist == Dist.CLIENT)
        {
            ClientEventHandler.init(modContainer, modEventBus);

        }

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (Kubastfcadditions) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
      //  modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (Config.LOG_DIRT_BLOCK.getAsBoolean()) {
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));
        }

        LOGGER.info("{}{}", Config.MAGIC_NUMBER_INTRODUCTION.get(), Config.MAGIC_NUMBER.getAsInt());

        Config.ITEM_STRINGS.get().forEach((item) -> LOGGER.info("ITEM >> {}", item));
    }

    // Add the example block item to the building blocks tab
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
