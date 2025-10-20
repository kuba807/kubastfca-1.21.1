/*
 * Licensed under the EUPL, Version 1.2.
 * You may obtain a copy of the Licence at:
 * https://joinup.ec.europa.eu/collection/eupl/eupl-text-eupl-12
 */

package net.kuba807.kubastfca.util.registry;

import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;


public interface IdHolder<T> extends Supplier<T>
{
    DeferredHolder<? super T, T> holder();

    default ResourceLocation getId()
    {
        return holder().getId();
    }
}
