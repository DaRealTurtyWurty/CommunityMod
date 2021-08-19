package io.github.communitymod.common.capability.provider;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class WolfItemHandlerProvider implements ICapabilityProvider, INBTSerializable<Tag> {

    private final ItemStackHandler items = new ItemStackHandler(1);
    private final LazyOptional<IItemHandler> handler = LazyOptional.of(() -> items);

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return handler.cast();
        }

        return LazyOptional.empty().cast();
    }

    @Override
    public Tag serializeNBT() {
        if (CapabilityItemHandler.ITEM_HANDLER_CAPABILITY == null) {
            return new CompoundTag();
        }

        return this.items.serializeNBT();
    }

    @Override
    public void deserializeNBT(Tag nbt) {
        this.items.deserializeNBT((CompoundTag) nbt);
        System.out.println(nbt.toString());
    }

    public void invalidate() {
        this.handler.invalidate();
    }

}
