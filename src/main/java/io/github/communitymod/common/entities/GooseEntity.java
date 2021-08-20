package io.github.communitymod.common.entities;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.Tags;

public class GooseEntity extends Animal {

    private static final ResourceLocation LOOT_TABLE = new ResourceLocation(CommunityMod.MODID,
            "entities/goose");

    private int jumpTicks;

    private int jumpDuration;

    private boolean wasOnGround;

    private int jumpDelayTicks;

    public GooseEntity(final EntityType<GooseEntity> type, final Level level) {
        super(type, level);

        this.jumpControl = new GooseEntity.JumpHelperController(this);
        this.moveControl = new GooseEntity.MoveHelperController(this);
    }

    public GooseEntity(final Level level, final double x, final double y, final double z) {
        this(EntityInit.GOOSE_ENTITY.get(), level);
        setPos(x, y, z);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 200).add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(Attributes.FOLLOW_RANGE, 16).add(Attributes.ATTACK_DAMAGE, 7)
                .add(Attributes.ATTACK_SPEED, 5).add(ForgeMod.ENTITY_GRAVITY.get(), 0.2);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.jumpTicks != this.jumpDuration) {
            ++this.jumpTicks;
        } else if (this.jumpDuration != 0) {
            this.jumpTicks = 0;
            this.jumpDuration = 0;
            setJumping(false);
        }
    }

    @Override
    public AgeableMob getBreedOffspring(final ServerLevel level, final AgeableMob partner) {
        final var goose = new GooseEntity(level, partner.getX(), partner.getY(), partner.getZ());
        goose.finalizeSpawn(level, level.getCurrentDifficultyAt(goose.blockPosition()), MobSpawnType.BREEDING,
                null, null);
        return goose;
    }

    public float getJumpCompletion(final float value) {
        return this.jumpDuration == 0 ? 0.0F : (this.jumpTicks + value) / this.jumpDuration;
    }

    @Override
    public void handleEntityEvent(final byte event) {
        if (event == 1) {
            spawnSprintParticle();
            this.jumpDuration = 10;
            this.jumpTicks = 0;
        } else {
            super.handleEntityEvent(event);
        }
    }

    @Override
    public boolean isFood(final ItemStack pStack) {
        return pStack.is(Tags.Items.SEEDS);
    }

    @Override
    public void setJumping(final boolean isJumping) {
        super.setJumping(isJumping);
        if (isJumping) {
            playSound(getJumpSound(), getSoundVolume(),
                    ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }
    }

    public void setSpeedModifier(final double speed) {
        getNavigation().setSpeedModifier(speed);
        this.moveControl.setWantedPosition(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
                this.moveControl.getWantedZ(), speed);
    }

    public void startJumping() {
        setJumping(true);
        this.jumpDuration = 10;
        this.jumpTicks = 0;
    }

    @Override
    protected int calculateFallDamage(final float x, final float z) {
        return 0; // goose no take dmg. goose pro.
    }

    @Override
    protected void customServerAiStep() {
        if (this.jumpDelayTicks > 0) {
            --this.jumpDelayTicks;
        }

        final var delta = getDeltaMovement();
        if (!this.onGround && delta.y < 0.0D) {
            this.setDeltaMovement(delta.multiply(1.0D, 0.3D, 1.0D));
        }

        if (!isNoAi() && GoalUtils.hasGroundPathNavigation(this)) {
            final var flag = ((ServerLevel) this.level).isRaided(blockPosition());
            ((GroundPathNavigation) getNavigation()).setCanOpenDoors(flag);
        }

        if (this.onGround) {
            if (!this.wasOnGround) {
                setJumping(false);
                checkLandingDelay();
            }

            if (this.jumpDelayTicks == 0) {
                final var livingentity = getTarget();
                if (livingentity != null && this.distanceToSqr(livingentity) < 16.0D) {
                    facePoint(livingentity.getX(), livingentity.getZ());
                    this.moveControl.setWantedPosition(livingentity.getX(), livingentity.getY(),
                            livingentity.getZ(), this.moveControl.getSpeedModifier());
                    startJumping();
                    this.wasOnGround = true;
                }
            }

            final var jumpController = (GooseEntity.JumpHelperController) this.jumpControl;
            if (!jumpController.wantJump()) {
                if (this.moveControl.hasWanted() && this.jumpDelayTicks == 0) {
                    final var path = this.navigation.getPath();
                    var vector3d = new Vec3(this.moveControl.getWantedX(), this.moveControl.getWantedY(),
                            this.moveControl.getWantedZ());
                    if (path != null && !path.isDone()) {
                        vector3d = path.getNextEntityPos(this);
                    }

                    facePoint(vector3d.x, vector3d.z);
                    startJumping();
                }
            } else if (!jumpController.canJump()) {
                enableJumpControl();
            }
        }

        this.wasOnGround = this.onGround;
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected float getJumpPower() {
        return this.level.random.nextFloat() * 1.5f + 0.3f;
    }

    protected SoundEvent getJumpSound() {
        return SoundEvents.RABBIT_JUMP;
    }

    @Override
    protected void jumpFromGround() {
        super.jumpFromGround();
        final var speed = this.moveControl.getSpeedModifier();
        if (speed > 0.0D) {
            final var distance = distanceToSqr(getDeltaMovement());
            if (distance < 0.01D) {
                moveRelative(0.1F, new Vec3(0.0D, 0.0D, 1.0D));
            }
        }

        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte) 1);
        }
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

    private void checkLandingDelay() {
        setLandingDelay();
        disableJumpControl();
    }

    private void disableJumpControl() {
        ((GooseEntity.JumpHelperController) this.jumpControl).setCanJump(false);
    }

    private void enableJumpControl() {
        ((GooseEntity.JumpHelperController) this.jumpControl).setCanJump(true);
    }

    private void facePoint(final double x, final double z) {
        this.yBodyRot = (float) (Math.atan2(z - this.getZ(), x - this.getX()) * (180F / (float) Math.PI))
                - 90.0F;
    }

    private void setLandingDelay() {
        if (this.moveControl.getSpeedModifier() < 2.2D) {
            this.jumpDelayTicks = 10;
        } else {
            this.jumpDelayTicks = 1;
        }
    }

    public class JumpHelperController extends JumpControl {
        private final GooseEntity goose;
        private boolean canJump;

        public JumpHelperController(final GooseEntity entity) {
            super(entity);
            this.goose = entity;
        }

        public boolean canJump() {
            return this.canJump;
        }

        public void setCanJump(final boolean canJump) {
            this.canJump = canJump;
        }

        @Override
        public void tick() {
            if (this.jump) {
                this.goose.startJumping();
                this.jump = false;
            }
        }

        public boolean wantJump() {
            return this.jump;
        }
    }

    static class MoveHelperController extends MoveControl {
        private final GooseEntity goose;
        private double nextJumpSpeed;

        public MoveHelperController(final GooseEntity goose) {
            super(goose);
            this.goose = goose;
        }

        @Override
        public void setWantedPosition(final double x, final double y, final double z, double speed) {
            if (this.goose.isInWater()) {
                speed = 1.5D;
            }

            super.setWantedPosition(z, y, z, speed);
            if (speed > 0.0D) {
                this.nextJumpSpeed = speed;
            }
        }

        @Override
        public void tick() {
            if (this.goose.onGround && !this.goose.jumping
                    && !((GooseEntity.JumpHelperController) this.goose.jumpControl).wantJump()) {
                this.goose.setSpeedModifier(0.0D);
            } else if (hasWanted()) {
                this.goose.setSpeedModifier(this.nextJumpSpeed);
            }

            super.tick();
        }
    }
}
