package net.kuba807.kubastfca.util.climate;

/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */


import java.util.Locale;
import java.util.Map;


import net.kuba807.kubastfca.common.block.crop.Crop;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.DataManager;
import net.minecraft.resources.ResourceLocation;

import static net.dries007.tfc.util.Helpers.resourceLocation;
import static net.kuba807.kubastfca.kubastfca.MODID;

public final class ClimateRanges
{

    //public static final Map<FruitBlocks.Tree, DataManager.Reference<ClimateRange>> FRUIT_TREES = Helpers.mapOf(FruitBlocks.Tree.class, tree -> register("plant/" + tree.name() + "_tree"));

    public static final Map<Crop, DataManager.Reference<ClimateRange>> CROPS = Helpers.mapOf(Crop.class, crop -> register("crop/" + crop.getSerializedName()));

    private static DataManager.Reference<ClimateRange> register(String name)
    {
        return ClimateRange.MANAGER.getReference(resourceLocation(MODID, name));
    }
}