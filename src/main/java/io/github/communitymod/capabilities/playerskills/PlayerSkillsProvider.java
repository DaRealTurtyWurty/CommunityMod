package io.github.communitymod.capabilities.playerskills;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlayerSkillsProvider implements ICapabilitySerializable<CompoundTag> {
    private final DefaultPlayerSkills skills = new DefaultPlayerSkills();
    private final LazyOptional<PlayerSkills> playerSkillsOptional = LazyOptional.of(() -> skills);

    public void invalidate() {
        playerSkillsOptional.invalidate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return playerSkillsOptional.cast();
    }

    @Override
    public CompoundTag serializeNBT() {
        if (CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY == null) {
            return new CompoundTag();
        } else {
            CompoundTag compoundNBT = new CompoundTag();
            compoundNBT.putInt("combatLvl", skills.combatLvl);
            compoundNBT.putInt("combatXp", skills.combatXp);

            compoundNBT.putInt("miningLvl", skills.miningLvl);
            compoundNBT.putInt("miningXp", skills.miningXp);

            compoundNBT.putInt("farmingLvl", skills.farmingLvl);
            compoundNBT.putInt("farmingXp", skills.farmingXp);

            compoundNBT.putInt("foragingLvl", skills.foragingLvl);
            compoundNBT.putInt("foragingXp", skills.foragingXp);

            compoundNBT.putInt("strength", skills.strength);

            compoundNBT.putFloat("health", skills.health);
            compoundNBT.putFloat("maxHealth", skills.maxHealth);

            compoundNBT.putInt("defense", skills.defense);
            return compoundNBT;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY != null) {
            int combatLvl = nbt.getInt("combatLvl");
            int combatXp = nbt.getInt("combatXp");
            skills.combatLvl = (combatLvl);
            skills.combatXp = (combatXp);

            int miningLvl = nbt.getInt("miningLvl");
            int miningXp = nbt.getInt("miningXp");
            skills.miningLvl = (miningLvl);
            skills.miningXp = (miningXp);

            int farmingLvl = nbt.getInt("farmingLvl");
            int farmingXp = nbt.getInt("farmingXp");
            skills.farmingLvl = (farmingLvl);
            skills.farmingXp = (farmingXp);

            int foragingLvl = nbt.getInt("foragingLvl");
            int foragingXp = nbt.getInt("foragingXp");
            skills.foragingLvl = (foragingLvl);
            skills.foragingXp = (foragingXp);

            int strength = nbt.getInt("strength");
            int defense = nbt.getInt("defense");
            skills.strength = (strength);
            skills.defense = (defense);

            float health = nbt.getFloat("health");
            float maxHealth = nbt.getFloat("maxHealth");
            skills.health = (health);
            skills.maxHealth = (maxHealth);
        }
    }
}
