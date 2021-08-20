package io.github.communitymod.capabilities.playerskills;

public interface PlayerSkills {
    int getCombatXp();

    void setCombatXp(int xp);

    int getCombatLvl();

    void setCombatLvl(int lvl);

    int getMiningXp();

    void setMiningXp(int xp);

    int getMiningLvl();

    void setMiningLvl(int lvl);

    int getFarmingXp();

    void setFarmingXp(int xp);

    int getFarmingLvl();

    void setFarmingLvl(int lvl);

    int getForagingXp();

    void setForagingXp(int xp);

    int getForagingLvl();

    void setForagingLvl(int lvl);

    int getStrength();

    void setStrength(int strength);

    int getDefense();

    void setDefense(int defense);

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    float getSpeed();

    void setSpeed(float speed);

    float getBaseSpeed();

    void setBaseSpeed(float baseSpeed);

    void AwardCombatXp(int xp);

    void AwardMiningXp(int xp);

    void AwardFarmingXp(int xp);

    void AwardForagingXp(int xp);
}
