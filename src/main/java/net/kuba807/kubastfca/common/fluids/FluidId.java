package net.kuba807.kubastfca.common.fluids;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.world.level.material.Fluid;




public record FluidId(String name, OptionalInt color, Supplier<? extends Fluid> fluid)
{

    private static final Map<Enum<?>, FluidId> IDENTITY = new HashMap<>();
    private static final List<FluidId> VALUES = Stream.of(
                    Arrays.stream(DefaultFluids.values()).map(fluid -> fromEnum(fluid, fluid.getColor(), fluid.getId(), KubastfcaFluids.DEFAULT_FLUIDS.get(fluid).source()))

            )
            .flatMap(Function.identity())
            .toList();

    public static <R> Map<FluidId, R> mapOf(Function<? super FluidId, ? extends R> map)
    {
        return VALUES.stream().collect(Collectors.toMap(Function.identity(), map));
    }

    public static FluidId asType(Enum<?> identity)
    {
        return IDENTITY.get(identity);
    }

    private static FluidId fromEnum(Enum<?> identity, int color, String name, Supplier<? extends Fluid> fluid)
    {
        final FluidId type = new FluidId(name, OptionalInt.of(KubastfcaFluids.ALPHA_MASK | color), fluid);
        IDENTITY.put(identity, type);
        return type;
    }
}