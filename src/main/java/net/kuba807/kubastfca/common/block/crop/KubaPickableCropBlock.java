package net.kuba807.kubastfca.common.block.crop;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.PickableCropBlock;
import net.dries007.tfc.util.climate.ClimateRange;
import net.kuba807.kubastfca.common.block.KubastfcaBlockStateProperties;
import net.kuba807.kubastfca.common.block.KubastfcaBlocks;
import net.kuba807.kubastfca.common.item.KubastfcaItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import net.kuba807.kubastfca.util.climate.ClimateRanges;

import java.util.function.Supplier;

public abstract class KubaPickableCropBlock extends PickableCropBlock{

    public static KubaPickableCropBlock create(ExtendedProperties properties, int stages, Crop crop, @Nullable Supplier<Supplier<? extends Item>> fruit, Supplier<Supplier<? extends Item>> matureFruit) {
        final IntegerProperty property = KubastfcaBlockStateProperties.getAgeProperty(stages - 1);
        return new KubaPickableCropBlock(properties, stages - 1, KubastfcaBlocks.DEAD_CROPS.get(crop), (Supplier)  KubastfcaItems.CROP_SEEDS.get(crop), crop.getNitrogen(), crop.getPhosphorous(), crop.getPotassium(), ClimateRanges.CROPS.get(crop), fruit, matureFruit) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }
    protected KubaPickableCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange, @Nullable Supplier<Supplier<? extends Item>> fruit, Supplier<Supplier<? extends Item>> matureFruit) {
        super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange, fruit, matureFruit);
    }
}
