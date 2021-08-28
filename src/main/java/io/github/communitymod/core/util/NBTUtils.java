package io.github.communitymod.core.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

public class NBTUtils {
    
    public static boolean getBoolean(ItemStack stack, String key) {
        return stack.hasTag() && getTagCompound(stack).getBoolean(key);
    }
    
    public static void setBoolean(ItemStack stack, String key, boolean value) {
        getTagCompound(stack).putBoolean(key, value);
    }

    public static void flipBoolean(ItemStack stack, String key) {
        setBoolean(stack, key, !getBoolean(stack, key));
    }
    
    public static CompoundTag getTagCompound(ItemStack stack) {
        validateCompound(stack);
        return stack.getTag();
    }
    
    public static void validateCompound(ItemStack stack) {
        if (!stack.hasTag()) {
            CompoundTag tag = new CompoundTag();
            stack.setTag(tag);
        }
    }

}
