package io.github.communitymod.common.entities;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;

public class GooseEntity extends Animal {

	private int jumpTicks;
	private int jumpDuration;
	private boolean wasOnGround;
	private int jumpDelayTicks;

	private static final ResourceLocation LOOT_TABLE = new ResourceLocation(CommunityMod.MODID,
			"entities/goose");

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.MOVEMENT_SPEED, 0.4)
				.add(Attributes.FOLLOW_RANGE, 16).add(Attributes.ATTACK_DAMAGE, 7)
				.add(Attributes.ATTACK_SPEED, 5).add(ForgeMod.ENTITY_GRAVITY.get(), 0.2);
	}

	public GooseEntity(EntityType<GooseEntity> type, Level level) {
		super(type, level);

		this.jumpControl = new GooseEntity.JumpHelperController(this);
		this.moveControl = new GooseEntity.MoveHelperController(this);
	}

	public GooseEntity(Level level, double x, double y, double z) {
		this(EntityInit.GOOSE_ENTITY.get(), level);
		setPos(x, y, z);
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
	protected int calculateFallDamage(float p_225508_1_, float p_225508_2_)
	{
		return 0; // goose no take dmg. goose pro.
	}

	@Override
	protected float getJumpPower()
	{
		return (this.level.random.nextFloat() * 1.5f) + 0.3f;
	}

	@Override
	protected void jumpFromGround() {
		super.jumpFromGround();
		double d0 = this.moveControl.getSpeedModifier();
		if (d0 > 0.0D) {
			double d1 = distanceToSqr(this.getDeltaMovement());
			if (d1 < 0.01D) {
				this.moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
			}
		}

		if (!this.level.isClientSide) {
			this.level.broadcastEntityEvent(this, (byte)1);
		}

	}

	@OnlyIn(Dist.CLIENT)
	public float getJumpCompletion(float p_175521_1_) {
		return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + p_175521_1_) / (float)this.jumpDuration;
	}

	public void setSpeedModifier(double p_175515_1_) {
		this.getNavigation().setSpeedModifier(p_175515_1_);
		this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ(), p_175515_1_);
	}

	public void startJumping() {
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}

	@Override
	public void setJumping(boolean p_70637_1_) {
		super.setJumping(p_70637_1_);
		if (p_70637_1_) {
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}

	@Override
	protected void customServerAiStep() {
		if (this.jumpDelayTicks > 0) {
			--this.jumpDelayTicks;
		}

		Vec3 var1 = this.getDeltaMovement();
		if (!this.onGround && var1.y < 0.0D) {
			this.setDeltaMovement(var1.multiply(1.0D, 0.75D, 1.0D));
		}

		if (!this.isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
			boolean flag = ((ServerLevel)this.level).isRaided(this.blockPosition());
			((GroundPathNavigation)this.getNavigation()).setCanOpenDoors(flag);
		}
		if (this.onGround) {
			if (!this.wasOnGround) {
				this.setJumping(false);
				this.checkLandingDelay();
			}

			if (this.jumpDelayTicks == 0) {
				LivingEntity livingentity = this.getTarget();
				if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
					this.facePoint(livingentity.getX(), livingentity.getZ());
					this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(), livingentity.getZ(), this.moveControl.getSpeedModifier());
					this.startJumping();
					this.wasOnGround = true;
				}
			}

			GooseEntity.JumpHelperController Disguisagerentity$jumphelpercontroller = (GooseEntity.JumpHelperController)this.jumpControl;
			if (!Disguisagerentity$jumphelpercontroller.wantJump()) {
				if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
					Path path = this.navigation.getPath();
					Vec3 vector3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(), this.moveControl.getWantedZ());
					if (path != null && !path.isDone()) {
						vector3d = path.getNextEntityPos(this);
					}

					this.facePoint(vector3d.x, vector3d.z);
					this.startJumping();
				}
			} else if (!Disguisagerentity$jumphelpercontroller.canJump()) {
				this.enableJumpControl();
			}
		}

		this.wasOnGround = this.onGround;
	}

	private void facePoint(double p_175533_1_, double p_175533_3_) {
		this.yBodyRot = (float)(Math.atan2(p_175533_3_ - this.getZ(), p_175533_1_ - this.getX()) * (double)(180F / (float)Math.PI)) - 90.0F;
	}

	private void enableJumpControl() {
		((GooseEntity.JumpHelperController)this.jumpControl).setCanJump(true);
	}

	private void disableJumpControl() {
		((GooseEntity.JumpHelperController)this.jumpControl).setCanJump(false);
	}

	private void setLandingDelay() {
		if (this.moveControl.getSpeedModifier() < 2.2D) {
			this.jumpDelayTicks = 10;
		} else {
			this.jumpDelayTicks = 1;
		}

	}

	private void checkLandingDelay() {
		this.setLandingDelay();
		this.disableJumpControl();
	}

	@Override
	public void aiStep() {
		super.aiStep();
		if (this.jumpTicks != this.jumpDuration) {
			++this.jumpTicks;
		} else if (this.jumpDuration != 0) {
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}

	}

	protected SoundEvent getJumpSound() {
		return SoundEvents.RABBIT_JUMP;
	}

	@OnlyIn(Dist.CLIENT)
	public void handleEntityEvent(byte p_70103_1_) {
		if (p_70103_1_ == 1) {
			this.spawnSprintParticle();
			this.jumpDuration = 10;
			this.jumpTicks = 0;
		} else {
			super.handleEntityEvent(p_70103_1_);
		}

	}

	public class JumpHelperController extends JumpControl
	{
		private final GooseEntity Goose;
		private boolean canJump;

		public JumpHelperController(GooseEntity p_i45863_2_) {
			super(p_i45863_2_);
			this.Goose = p_i45863_2_;
		}

		public boolean wantJump() {
			return this.jump;
		}

		public boolean canJump() {
			return this.canJump;
		}

		public void setCanJump(boolean p_180066_1_) {
			this.canJump = p_180066_1_;
		}

		public void tick() {
			if (this.jump) {
				this.Goose.startJumping();
				this.jump = false;
			}

		}
	}

	static class MoveHelperController extends MoveControl
	{
		private final GooseEntity Goose;
		private double nextJumpSpeed;

		public MoveHelperController(GooseEntity p_i45862_1_) {
			super(p_i45862_1_);
			this.Goose = p_i45862_1_;
		}

		public void tick() {
			if (this.Goose.onGround && !this.Goose.jumping && !((GooseEntity.JumpHelperController)this.Goose.jumpControl).wantJump()) {
				this.Goose.setSpeedModifier(0.0D);
			} else if (this.hasWanted()) {
				this.Goose.setSpeedModifier(this.nextJumpSpeed);
			}

			super.tick();
		}

		public void setWantedPosition(double p_75642_1_, double p_75642_3_, double p_75642_5_, double p_75642_7_) {
			if (this.Goose.isInWater()) {
				p_75642_7_ = 1.5D;
			}

			super.setWantedPosition(p_75642_1_, p_75642_3_, p_75642_5_, p_75642_7_);
			if (p_75642_7_ > 0.0D) {
				this.nextJumpSpeed = p_75642_7_;
			}

		}
	}
}
