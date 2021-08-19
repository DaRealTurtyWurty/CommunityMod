package io.github.communitymod.common.entities;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.ItemInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;

public class GooseEntity extends Animal {

	private static final ResourceLocation LOOT_TABLE = new ResourceLocation(CommunityMod.MODID,
			"entities/goose");

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 16).add(Attributes.ATTACK_DAMAGE, 7)
				.add(Attributes.ATTACK_SPEED, 5).add(ForgeMod.ENTITY_GRAVITY.get(), 0.2);
	}

	public GooseEntity(EntityType<GooseEntity> type, Level level) {
		super(type, level);
	}

	public GooseEntity(Level level, double x, double y, double z) {
		this(EntityInit.GOOSE_ENTITY.get(), level);
		setPos(x, y, z);
	}

	@Override
	public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
		final var goose = new GooseEntity(level, partner.getX(), partner.getY(), partner.getZ());
		goose.finalizeSpawn(level, level.getCurrentDifficultyAt(goose.blockPosition()), MobSpawnType.BREEDING,
				null, null);
		return goose;
	}

	@Override
	protected ResourceLocation getDefaultLootTable() {
		return LOOT_TABLE;
	}

	@Override
	public boolean isFood(ItemStack pStack)
	{
		return pStack.is(Tags.Items.SEEDS);
	}

	@Override
	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
		this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Tags.Items.SEEDS), false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	}
}
