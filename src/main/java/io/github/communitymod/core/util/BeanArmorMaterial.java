package io.github.communitymod.core.util;

import io.github.communitymod.core.init.ItemInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum BeanArmorMaterial implements ArmorMaterial {
    BEAN_ARMOR("bean", 10, new int[] { 1, 1, 1, 1 }, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0f, 0f,
            () -> Ingredient.of(ItemInit.BEANS.get()));

    private static final int[] baseDurability = { 100, 100, 100, 100 };

    private final int[] defense;
    private final int durabilityMultiplier;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float knockbackResistance;
    private final String name;
    private final Ingredient repairIngredient;
    private final float toughness;

    BeanArmorMaterial(final String name, final int durabilityMultiplier, final int[] defense,
            final int enchantmentValue, final SoundEvent equipSound, final float toughness,
            final float knockbackResistance, final Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.defense = defense;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = repairIngredient.get();
    }

    @Override
    public int getDefenseForSlot(final EquipmentSlot slot) {
        return this.defense[slot.getIndex()];
    }

    @Override
    public int getDurabilityForSlot(final EquipmentSlot slot) {
        return baseDurability[slot.getIndex()] * this.durabilityMultiplier;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }

    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient;
    }

    @Override
    public float getToughness() {
        return this.toughness;
    }
}
