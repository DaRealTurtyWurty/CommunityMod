package io.github.communitymod.capabilities.entitylevel;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MobLevelProvider implements ICapabilitySerializable<CompoundTag> {
    private final DefaultMobLevel mobLevel = new DefaultMobLevel();
    private final LazyOptional<IMobLevel> mobLevelOptional = LazyOptional.of(() -> mobLevel);

    public void invalidate() {
        mobLevelOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return mobLevelOptional.cast();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (CapabilityMobLevel.MOB_LEVEL_CAPABILITY == null) {
            return new CompoundTag();
        } else {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putInt("mobLevel", mobLevel.getMobLevel());
            return compoundNBT;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CapabilityMobLevel.MOB_LEVEL_CAPABILITY != null) {
            int mobLevel1 = nbt.getInt("mobLevel");
            mobLevel.setMobLevel(mobLevel1);
        }
    }
}
