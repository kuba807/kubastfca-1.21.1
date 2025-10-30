package net.kuba807.kubastfca.common.blockentities;

import net.kuba807.kubastfca.common.block.KubastfcaBlocks;
import net.kuba807.kubastfca.util.registry.RegistrationHelpers;
import net.kuba807.kubastfca.util.registry.RegistryHolder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static net.kuba807.kubastfca.kubastfca.MODID;

public final class KubastfcaBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);




    public static final Id<CeramicChurnBlockEntity> CERAMIC_BUTTER_CHURN = register("ceramic_butter_churn", CeramicChurnBlockEntity::new, KubastfcaBlocks.CERAMIC_BUTTER_CHURN);


    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Supplier<? extends Block> block)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, block));
    }

    private static <T extends BlockEntity> Id<T> register(String name, BlockEntityType.BlockEntitySupplier<T> factory, Stream<? extends Supplier<? extends Block>> blocks)
    {
        return new Id<>(RegistrationHelpers.register(BLOCK_ENTITIES, name, factory, blocks));
    }



    public record Id<T extends BlockEntity>(DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> holder)
            implements RegistryHolder<BlockEntityType<?>, BlockEntityType<T>> {}
}