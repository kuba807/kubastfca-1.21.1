package net.kuba807.kubastfca.common.block.crop;

import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DefaultCropBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.kuba807.kubastfca.common.block.KubastfcaBlockStateProperties;
import net.kuba807.kubastfca.common.block.KubastfcaBlocks;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.kuba807.kubastfca.util.climate.ClimateRanges;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.function.Supplier;

public abstract class KubaDefaultCropBlock extends DefaultCropBlock {

    public static KubaDefaultCropBlock create(ExtendedProperties properties, int stages, Crop crop) {
        final IntegerProperty property = KubastfcaBlockStateProperties.getAgeProperty(stages - 1);
        return new KubaDefaultCropBlock(properties, stages - 1, KubastfcaBlocks.DEAD_CROPS.get(crop), (Supplier) KubastfcaItems.CROP_SEEDS.get(crop), crop.getNitrogen(),crop.getPhosphorous(),crop.getPotassium(), (Supplier) ClimateRanges.CROPS.get(crop)) {
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };}



    protected KubaDefaultCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
        super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
    }



}
