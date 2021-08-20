package io.github.communitymod.capabilities.playerskills;

import io.github.communitymod.core.GameplayEvents;

public class DefaultPlayerSkills implements PlayerSkills {

    private int combatLvl;
    private int combatXp;

    private int miningLvl;
    private int miningXp;

    private int farmingLvl;
    private int farmingXp;

    private int foragingLvl;
    private int foragingXp;

    private int strength = 100;

    private int defense;

    private float health = 100f;
    private float maxHealth = 100f;

    private float speed = 100f;
    private float baseSpeed = 100f;

    @Override
    public int getCombatXp() {
        return this.combatXp;
    }

    @Override
    public void setCombatXp(int xp) {
        this.combatXp = xp;
    }

    @Override
    public int getCombatLvl() {
        return this.combatLvl;
    }

    @Override
    public void setCombatLvl(int lvl) {
        this.combatLvl = lvl;
    }

    @Override
    public int getMiningXp() {
        return this.miningXp;
    }

    @Override
    public void setMiningXp(int xp) {
        this.miningXp = xp;
    }

    @Override
    public int getMiningLvl() {
        return this.miningLvl;
    }

    @Override
    public void setMiningLvl(int lvl) {
        this.miningLvl = lvl;
    }

    @Override
    public int getFarmingXp() {
        return this.farmingXp;
    }

    @Override
    public void setFarmingXp(int xp) {
        this.farmingXp = xp;
    }

    @Override
    public int getFarmingLvl() {
        return this.farmingLvl;
    }

    @Override
    public void setFarmingLvl(int lvl) {
        this.farmingLvl = lvl;
    }

    @Override
    public int getForagingXp() {
        return this.foragingXp;
    }

    @Override
    public void setForagingXp(int xp) {
        this.foragingXp = xp;
    }

    @Override
    public int getForagingLvl() {
        return this.foragingLvl;
    }

    @Override
    public void setForagingLvl(int lvl) {
        this.foragingLvl = lvl;
    }

    @Override
    public int getStrength() {
        return this.strength;
    }

    @Override
    public void setStrength(int strength) {
        this.strength = strength;
    }

    @Override
    public int getDefense() {
        return this.defense;
    }

    @Override
    public void setDefense(int defense) {
        this.defense = defense;
    }

    @Override
    public float getHealth() {
        return this.health;
    }

    @Override
    public void setHealth(float health) {
        this.health = health;
    }

    @Override
    public float getMaxHealth() {
        return this.maxHealth;
    }

    @Override
    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public float getBaseSpeed() {
        return this.baseSpeed;
    }

    @Override
    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public void AwardCombatXp(int xp) {
        int qxp = xp;
        while (qxp > 0) {
            ++combatXp;
            if (combatXp > (Math.pow((combatLvl + 3), 3) - Math.pow((combatLvl + 1), 2)) * 2) {
                ++combatLvl;
                combatXp = 0;
                GameplayEvents.displayLevelCombat = combatLvl;
                GameplayEvents.showLevelCombat = true;
            }
            qxp -= 1;
        }
    }

    public void AwardMiningXp(int xp) {
        int qxp = xp;
        while (qxp > 0) {
            ++miningXp;
            if (miningXp > (Math.pow((miningLvl + 3), 3) - Math.pow((miningLvl + 1), 2)) * 2) {
                ++miningLvl;
                miningXp = 0;
                GameplayEvents.displayLevelMining = miningLvl;
                GameplayEvents.showLevelMining = true;
            }
            qxp -= 1;
        }
    }

    public void AwardFarmingXp(int xp) {
        int qxp = xp;
        while (qxp > 0) {
            ++farmingXp;
            if (farmingXp > (Math.pow((farmingLvl + 3), 3) - Math.pow((farmingLvl + 1), 2)) * 2) {
                ++farmingLvl;
                farmingXp = 0;
                GameplayEvents.displayLevelFarming = farmingLvl;
                GameplayEvents.showLevelFarming = true;
            }
            qxp -= 1;
        }
    }

    public void AwardForagingXp(int xp) {
        int qxp = xp;
        while (qxp > 0) {
            ++foragingXp;
            if (foragingXp > (Math.pow((foragingLvl + 3), 3) - Math.pow((foragingLvl + 1), 2)) * 2) {
                ++foragingLvl;
                foragingXp = 0;
                GameplayEvents.displayLevelForaging = foragingLvl;
                GameplayEvents.showLevelForaging = true;
            }
            qxp -= 1;
        }
    }
}
