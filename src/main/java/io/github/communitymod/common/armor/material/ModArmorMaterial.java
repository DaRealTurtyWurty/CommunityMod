package io.github.communitymod.common.armor.material;

import io.github.communitymod.core.init.ItemInit;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.function.Supplier;

public enum ModArmorMaterial implements ArmorMaterial {
    BEAN_ARMOR("bean", new int[]{1000, 1000, 1000, 1000}, new int[]{1, 1, 1, 1}, 20, SoundEvents.ARMOR_EQUIP_CHAIN, 0f, 0f,
            () -> Ingredient.of(ItemInit.BEANS.get())),
    //@todo Ingredient.of(ItemInit.SOUL.get()) gives an init error, its empty for now
    SOULSTEALER("soulstealer", new int[]{700, 1300, 1200, 800}, new int[]{5, 10, 8, 5}, 30, SoundEvents.ARMOR_EQUIP_NETHERITE, 4f, 1f,
            () -> /*Ingredient.of(ItemInit.SOUL.get())*/ Ingredient.EMPTY);

    private final int[] durability;
    private final int[] defense;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float knockbackResistance;
    private final String name;
    private final Ingredient repairIngredient;
    private final float toughness;

    ModArmorMaterial(final String name, final int[] durability, final int[] defense,
                     final int enchantmentValue, final SoundEvent equipSound, final float toughness,
                     final float knockbackResistance, final Supplier<Ingredient> repairIngredient) {
        this.name = name;
        this.durability = durability;
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
        return durability[slot.getIndex()];
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
