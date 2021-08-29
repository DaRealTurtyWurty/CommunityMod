package io.github.communitymod.core.util;

public enum MeatballTypes {

    DAMAGE, FIRE, FREEZE, EXPLOSIVE, AMOUNT, MAXSIZE, POISON, SEASONING, CAUSTICITY, SPEED, WEIGHT, RICOCHET, GENTLE,
    FASTRELOAD, HOLY, ELDRITCH;

    public MeatballTypes get(int index) {
        return MeatballTypes.values()[index];
    }

    public int getIndex(MeatballTypes type) {
        return type.ordinal();
    }

}
