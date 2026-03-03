/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.kuba807.kubastfca.common.item;

import java.util.function.Supplier;

import net.dries007.tfc.common.items.PlantableInfo;
import net.dries007.tfc.util.climate.ClimateRange;
import net.kuba807.kubastfca.util.climate.ClimateRanges;
//import net.kuba807.kubastfca.common.block.crop.Crop;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;


public class SeedItem extends ItemNameBlockItem implements PlantableInfo
{
    private final PlantNutrients nutrients;
    private final Supplier<ClimateRange> climateRange;

    public SeedItem(Crop crop, Block block, Properties properties)
    {
        super(block, properties);
        nutrients = new PlantNutrients(crop.getNitrogen(), crop.getPhosphorous(), crop.getPotassium());
        climateRange = crop.getClimateRange();
    }

    @Override
    public @Nullable PlantableInfo.PlantNutrients getNutrientsInfo()
    {
        return nutrients;
    }

    @Override
    public @Nullable ClimateRange getClimateRangeInfo()
    {
        return climateRange.get();
    }
}
