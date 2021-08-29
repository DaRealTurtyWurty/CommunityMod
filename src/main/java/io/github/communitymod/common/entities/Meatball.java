package io.github.communitymod.common.entities;

import io.github.communitymod.core.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Meatball extends Projectile {

    public static final EntityDataAccessor<String> ID_FLAGS = SynchedEntityData.defineId(Meatball.class,
            EntityDataSerializers.STRING);

    public Meatball(EntityType<? extends Meatball> type, Level world) {
        super(type, world);
    }

    public Meatball(Level world, Player owner) {
        super(EntityInit.MEATBALL.get(), world);
        this.setOwner(owner);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(ID_FLAGS, "0000000000000000");
    }

    public String getIDFlags() {

        return this.entityData.get(ID_FLAGS);

    }

    public void setIDFlags(String id) {

        if (id.length() != 16) {
            return;
        }

        this.entityData.set(ID_FLAGS, id);

    }

    public void tick() {

        activeTick();

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        boolean flag = false;
        if (hitresult.getType() == HitResult.Type.BLOCK) {
            BlockPos blockpos = ((BlockHitResult) hitresult).getBlockPos();
            BlockState blockstate = this.level.getBlockState(blockpos);
            if (blockstate.is(Blocks.NETHER_PORTAL)) {
                this.handleInsidePortal(blockpos);
                flag = true;
            } else if (blockstate.is(Blocks.END_GATEWAY)) {
                BlockEntity blockentity = this.level.getBlockEntity(blockpos);
                if (blockentity instanceof TheEndGatewayBlockEntity
                        && TheEndGatewayBlockEntity.canEntityTeleport(this)) {
                    TheEndGatewayBlockEntity.teleportEntity(this.level, blockpos, blockstate, this,
                            (TheEndGatewayBlockEntity) blockentity);
                }

                flag = true;
            }
            this.onHitBlock((BlockHitResult) hitresult);
        }

        if (hitresult.getType() == HitResult.Type.ENTITY) {

            onHitEntity((EntityHitResult) hitresult);

        }

        if (hitresult.getType() != HitResult.Type.MISS && !flag
                && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, hitresult)) {
            this.onHit(hitresult);
        }

        this.checkInsideBlocks();
        Vec3 vec3 = this.getDeltaMovement();
        double d2 = this.getX() + vec3.x;
        double d0 = this.getY() + vec3.y;
        double d1 = this.getZ() + vec3.z;
        this.updateRotation();
        float f;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.level.addParticle(ParticleTypes.BUBBLE, d2 - vec3.x * 0.25D, d0 - vec3.y * 0.25D,
                        d1 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
            }

            f = 0.8F;
        } else {
            f = 0.99F;
        }

        this.setDeltaMovement(vec3.scale((double) f));
        if (!this.isNoGravity()) {
            Vec3 vec31 = this.getDeltaMovement();
            /*
             * .03 is gravity
             */
            this.setDeltaMovement(vec31.x, vec31.y - (double) .03, vec31.z);
        }

        this.setPos(d2, d0, d1);

    }

    private void activeTick() {

    }

    @Override
    protected void onHitEntity(EntityHitResult r) {
        Entity entity = r.getEntity();
        this.kill();
    }

    @Override
    protected void onHitBlock(BlockHitResult r) {
        BlockPos pos = r.getBlockPos();
        this.level.explode(getOwner(), r.getBlockPos().getX(), r.getBlockPos().getY(), r.getBlockPos().getZ(), 3,
                BlockInteraction.NONE);
        this.kill();
    }

}
