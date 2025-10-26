package net.kuba807.kubastfca.client;

import net.dries007.tfc.client.extensions.FluidRendererExtension;
import net.dries007.tfc.util.Helpers;
import net.kuba807.kubastfca.common.fluids.KubastfcaFluids;

import net.kuba807.kubastfca.kubastfca;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.model.DynamicFluidContainerModel;

import java.util.Objects;


public class ClientEventHandler {
    public static final ResourceLocation WATER_STILL = Helpers.identifierMC("block/water_still");
    public static final ResourceLocation WATER_FLOW = Helpers.identifierMC("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY = Helpers.identifierMC("block/water_overlay");
    public static final ResourceLocation UNDERWATER_LOCATION = Helpers.identifierMC("textures/misc/underwater.png");



    public static void init(ModContainer mod, IEventBus bus)
    {

        bus.addListener(ClientEventHandler::registerColorHandlerItems);
        bus.addListener(ClientEventHandler::registerExtensions);


    }




    public static void registerColorHandlerItems(RegisterColorHandlersEvent.Item event)
    {


        for (Fluid fluid : BuiltInRegistries.FLUID)
        {
            if (Objects.requireNonNull(BuiltInRegistries.FLUID.getKey(fluid)).getNamespace().equals(kubastfca.MODID))
            {
                event.register(new DynamicFluidContainerModel.Colors(), fluid.getBucket());
            }
        }


    }

    public static void registerExtensions(RegisterClientExtensionsEvent event)
    {
        KubastfcaFluids.DEFAULT_FLUIDS.forEach((fluid, holder) -> event.registerFluidType(
                new FluidRendererExtension(fluid.isTransparent() ? KubastfcaFluids.ALPHA_MASK | fluid.getColor() : fluid.getColor(), WATER_STILL, WATER_FLOW, WATER_OVERLAY, UNDERWATER_LOCATION),
                holder.getType()
        ));
    }



}
