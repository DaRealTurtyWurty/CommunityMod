package io.github.communitymod.common.entities;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.ai.goals.WolfFetchStickGoal;
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
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;

public class BeanEntity extends Animal {

    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(CommunityMod.MODID,
            "entities/bean");

    public BeanEntity(final EntityType<BeanEntity> type, final Level level) {
        super(type, level);
    }

    public BeanEntity(final Level level, final double x, final double y, final double z) {
        this(EntityInit.BEAN_ENTITY.get(), level);
        setPos(x, y, z);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 1)
                .add(Attributes.FOLLOW_RANGE, 16).add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.ATTACK_SPEED, 5).add(Attributes.ARMOR, 3).add(Attributes.ARMOR_TOUGHNESS, 1)
                .add(Attributes.JUMP_STRENGTH, 16).add(ForgeMod.ENTITY_GRAVITY.get(), 0.5)
                .add(ForgeMod.REACH_DISTANCE.get(), 16).add(ForgeMod.SWIM_SPEED.get(), 2);
    }

    @Override
    public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
        final var bean = new BeanEntity(level, partner.getX(), partner.getY(), partner.getZ());
        bean.finalizeSpawn(level, level.getCurrentDifficultyAt(bean.blockPosition()), MobSpawnType.BREEDING,
                null, null);
        return bean;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(ItemInit.BEANS.get()), false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
    }
}
