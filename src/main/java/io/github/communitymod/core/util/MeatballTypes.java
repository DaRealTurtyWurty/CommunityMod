package io.github.communitymod.core.util;

import java.util.List;

public enum MeatballTypes {

    DAMAGE, FIRE, FREEZE, EXPLOSIVE, AMOUNT, MAXSIZE, POISON, SEASONING, CAUSTICITY, SPEED, WEIGHT, RICOCHET, GENTILE,
    FASTRELOAD, HOLY, ELDRITCH;

    public static MeatballTypes get(int index) {
        return MeatballTypes.values()[index];
    }

    public static int getIndex(MeatballTypes type) {
        return type.ordinal();
    }

    public static int getIndex(String type) {

        if (containsValue(type)) {
            for (MeatballTypes t : getListValues()) {
                if (t.toString() == type) {
                    return getIndex(t);
                }
            }
        }
        return 0;
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
