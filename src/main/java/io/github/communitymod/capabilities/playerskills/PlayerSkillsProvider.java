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
    private final LazyOptional<IPlayerSkills> playerSkillsOptional = LazyOptional.of(() -> skills);

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
            compoundNBT.putInt("combatLvl", skills.getCombatLvl());
            compoundNBT.putInt("combatXp", skills.getCombatXp());

            compoundNBT.putInt("miningLvl", skills.getMiningLvl());
            compoundNBT.putInt("miningXp", skills.getMiningXp());

            compoundNBT.putInt("farmingLvl", skills.getFarmingLvl());
            compoundNBT.putInt("farmingXp", skills.getFarmingXp());

            compoundNBT.putInt("foragingLvl", skills.getForagingLvl());
            compoundNBT.putInt("foragingXp", skills.getForagingXp());

            compoundNBT.putInt("strength", skills.getStrength());

            compoundNBT.putFloat("health", skills.getHealth());
            compoundNBT.putFloat("maxHealth", skills.getMaxHealth());

            compoundNBT.putInt("defense", skills.getDefense());
            return compoundNBT;
        }
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        if (CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY != null) {
            int combatLvl = nbt.getInt("combatLvl");
            int combatXp = nbt.getInt("combatXp");
            skills.setCombatLvl(combatLvl);
            skills.setCombatXp(combatXp);

            int miningLvl = nbt.getInt("miningLvl");
            int miningXp = nbt.getInt("miningXp");
            skills.setMiningLvl(miningLvl);
            skills.setMiningXp(miningXp);

            int farmingLvl = nbt.getInt("farmingLvl");
            int farmingXp = nbt.getInt("farmingXp");
            skills.setFarmingLvl(farmingLvl);
            skills.setFarmingXp(farmingXp);

            int foragingLvl = nbt.getInt("foragingLvl");
            int foragingXp = nbt.getInt("foragingXp");
            skills.setForagingLvl(foragingLvl);
            skills.setForagingXp(foragingXp);

            int strength = nbt.getInt("strength");
            int defense = nbt.getInt("defense");
            skills.setStrength(strength);
            skills.setDefense(defense);

            float health = nbt.getFloat("health");
            float maxHealth = nbt.getFloat("maxHealth");
            skills.setHealth(health);
            skills.setMaxHealth(maxHealth);
        }
    }
}
