package net.kuba807.kubastfca.common.recipes;


import java.util.function.Supplier;

import com.mojang.serialization.MapCodec;
import net.dries007.tfc.common.recipes.RecipeSerializerImpl;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import net.dries007.tfc.util.registry.RegistryHolder;

import static net.kuba807.kubastfca.kubastfca.MODID;
public class KubaRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);

    public static final Id<DumplingPotRecipe> DUMPLING_POT = register("dumpling_pot", DumplingPotRecipe.CODEC, DumplingPotRecipe.STREAM_CODEC);

    private static <R extends Recipe<?>> Id<R> register(String name, MapCodec<R> codec, StreamCodec<RegistryFriendlyByteBuf, R> stream)
    {
        return register(name, new RecipeSerializerImpl<>(codec, stream));
    }

    private static <R extends Recipe<?>> Id<R> register(String name, RecipeSerializer<R> serializer)
    {
        return new Id<>(RECIPE_SERIALIZERS.register(name, () -> serializer));
    }

    public record Id<T extends Recipe<?>>(DeferredHolder<RecipeSerializer<?>, RecipeSerializer<T>> holder)
            implements RegistryHolder<RecipeSerializer<?>, RecipeSerializer<T>> {}

}
