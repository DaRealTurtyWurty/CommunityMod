package io.github.communitymod.capabilities.playerskills;

import io.github.communitymod.core.GameplayEvents;

public class DefaultPlayerSkills implements PlayerSkills {

    public int combatLvl;
    public int combatXp;

    public int miningLvl;
    public int miningXp;

    public int farmingLvl;
    public int farmingXp;

    public int foragingLvl;
    public int foragingXp;

    public int strength = 100;
    public int baseStrength = 100;

    public int defense;
    public int baseDefense;

    public float health = 100f;
    public float maxHealth = 100f;
    public float baseMaxHealth = 100f;

    public float speed = 100f;
    public float baseSpeed = 100f;

    public int soulCount;

    public void awardCombatXp(int xp) {
        //the while loops cannot be removed, as i cannot find another way to do this besides using recursion.
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

    public void awardMiningXp(int xp) {
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

    public void awardFarmingXp(int xp) {
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

    public void awardForagingXp(int xp) {
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
