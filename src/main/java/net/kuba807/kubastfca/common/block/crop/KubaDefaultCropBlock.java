package net.kuba807.kubastfca.common.block.crop;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.DefaultCropBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.climate.ClimateRanges;
import net.kuba807.kubastfca.common.block.KubaBlocks;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.function.Supplier;

public abstract class KubaDefaultCropBlock extends DefaultCropBlock {

    public static KubaDefaultCropBlock create(ExtendedProperties properties, int stages, Crop crop) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages - 1);
        return new KubaDefaultCropBlock(properties, stages - 1, (Supplier) KubaBlocks.DEAD_CROPS.get(crop), (Supplier) KubastfcaItems.CROP_SEEDS.get(crop), crop.getNitrogen(),crop.getPhosphorous(),crop.getPotassium(), (Supplier) ClimateRanges.CROPS.get(crop)) {
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };}



    protected KubaDefaultCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
        super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
    }



}
