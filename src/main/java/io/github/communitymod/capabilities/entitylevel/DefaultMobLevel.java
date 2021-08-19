package io.github.communitymod.capabilities.entitylevel;

public class DefaultMobLevel implements IMobLevel {

    private int mobLevel;

    @Override
    public int getMobLevel() {
        return this.mobLevel;
    }

    @Override
    public void setMobLevel(int mobLevel) {
        this.mobLevel = mobLevel;
    }
}
