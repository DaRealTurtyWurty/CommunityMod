package io.github.communitymod.core.util;

import java.util.List;

public enum MeatballTypes {

    DAMAGE, FIRE, FREEZE, EXPLOSIVE, AMOUNT, MAXSIZE, POISON, SEASONING, CAUSTICITY, SPEED, WEIGHT, RICOCHET, GENTILE,
    FASTRELOAD, HOLY, ELDRITCH;

    public MeatballTypes get(int index) {
        return MeatballTypes.values()[index];
    }

    public int getIndex(MeatballTypes type) {
        return type.ordinal();
    }

    public static List<MeatballTypes> getListValues() {

        List<MeatballTypes> l = List.of(MeatballTypes.values());
        return l;

    }

    public static boolean containsValue(String name) {
        for (MeatballTypes type : getListValues()) {
            if (type.toString() == name) {
                return true;
            }
        }
        return false;
    }

}
