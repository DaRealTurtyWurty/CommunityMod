package io.github.communitymod.common.entities;

import io.github.communitymod.core.init.EntityInit;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownStickEntity extends ThrowableItemProjectile {

    public static final String FIRED_ROTATION_KEY = "FiredRot";
    private static final EntityDataAccessor<Float> FIRED_ROTATION =
            SynchedEntityData.defineId(ThrownStickEntity.class, EntityDataSerializers.FLOAT);

    public ThrownStickEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownStickEntity(double x, double eyeY, double z, Level level) {
        super(EntityInit.THROWN_STICK.get(), x, eyeY, z, level);
    }

    public ThrownStickEntity(LivingEntity user, Level level) {
        super(EntityInit.THROWN_STICK.get(), user, level);
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!pEntity.level.isClientSide() && this.isOnGround()) {
            pEntity.addItem(this.getDefaultItem().getDefaultInstance());
            this.discard();
        }
    }

    public float getFiredRotation() {
        return this.entityData.get(FIRED_ROTATION);
    }

    public void setFiredRotation(float firedRotation) {
        this.entityData.set(FIRED_ROTATION, firedRotation);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putFloat(FIRED_ROTATION_KEY, this.getFiredRotation());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setFiredRotation(pCompound.getFloat(FIRED_ROTATION_KEY));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FIRED_ROTATION, 0.0f);
    }

    @MethodsReturnNonnullByDefault
    @Override
    protected Item getDefaultItem() {
        return Items.STICK;
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);

        Vec3 vec3 = p_37258_.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(vec3);
        Vec3 vec31 = vec3.normalize().scale(0.05d);
        this.setPosRaw(this.getX() - vec31.x, this.getY() - vec31.y, this.getZ() - vec31.z);
        this.onGround = true;
    }

}
