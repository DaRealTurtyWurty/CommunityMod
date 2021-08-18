package io.github.communitymod.core.util.enums;

import java.util.function.Supplier;

import com.google.common.base.Suppliers;
import io.github.communitymod.core.init.ItemInit;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;

public enum ModToolMaterials implements Tier {
	   BEAN(0, 118, 2.0F, 0.0F, 15, () -> Ingredient.of(ItemInit.BEANS.get()));

	   private final int level;
	   private final int uses;
	   private final float speed;
	   private final float damage;
	   private final int enchantmentValue;
	   private final Supplier<Ingredient> repairIngredient;

	   private ModToolMaterials(int harvestLevel, int uses, float attackSpeed, float damage, int enchantValue, Supplier<Ingredient> repairIngredient) {
	      this.level = harvestLevel;
	      this.uses = uses;
	      this.speed = attackSpeed;
	      this.damage = damage;
	      this.enchantmentValue = enchantValue;
	      this.repairIngredient = Suppliers.memoize(repairIngredient::get);
	   }
	   
	   public int getUses() {
	      return this.uses;
	   }

	   public float getSpeed() {
	      return this.speed;
	   }

	   public float getAttackDamageBonus() {
	      return this.damage;
	   }

	   public int getLevel() {
	      return this.level;
	   }

	   public int getEnchantmentValue() {
	      return this.enchantmentValue;
	   }

	   public Ingredient getRepairIngredient() {
	      return this.repairIngredient.get();
	   }
}
