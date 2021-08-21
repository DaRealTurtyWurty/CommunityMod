package io.github.communitymod.capabilities.playerskills;

public interface PlayerSkills {
    void awardCombatXp(int xp);

    void awardMiningXp(int xp);

    void awardFarmingXp(int xp);

    void awardForagingXp(int xp);
}
