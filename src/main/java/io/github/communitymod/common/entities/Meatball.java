package io.github.communitymod.common.entities;

import static io.github.communitymod.core.util.MeatballTypes.CAUSTICITY;
import static io.github.communitymod.core.util.MeatballTypes.DAMAGE;
import static io.github.communitymod.core.util.MeatballTypes.ELDRITCH;
import static io.github.communitymod.core.util.MeatballTypes.EXPLOSIVE;
import static io.github.communitymod.core.util.MeatballTypes.FIRE;
import static io.github.communitymod.core.util.MeatballTypes.FREEZE;
import static io.github.communitymod.core.util.MeatballTypes.HOLY;
import static io.github.communitymod.core.util.MeatballTypes.MAXSIZE;
import static io.github.communitymod.core.util.MeatballTypes.POISON;
import static io.github.communitymod.core.util.MeatballTypes.RICOCHET;
import static io.github.communitymod.core.util.MeatballTypes.SEASONING;
import static io.github.communitymod.core.util.MeatballTypes.WEIGHT;
import static io.github.communitymod.core.util.MeatballTypes.getIndex;

import io.github.communitymod.core.init.EntityInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class Meatball extends Projectile {

    public static final EntityDataAccessor<String> ID_FLAGS = SynchedEntityData.defineId(Meatball.class,
            EntityDataSerializers.STRING);

    private int bouncetimes = 7;

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

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("bouncetimes", bouncetimes);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("bouncetimes"))
            bouncetimes = pCompound.getInt("bouncetimes");
    }

    public char[] getIDFlags() {

        return this.entityData.get(ID_FLAGS).toCharArray();

    }

    public void setIDFlags(String id) {

        if (id.length() != 16) {
            return;
        }

        this.entityData.set(ID_FLAGS, id);

    }

    public boolean isLarge() {
        char[] data = this.getIDFlags();

        return data[getIndex(MAXSIZE)] != '0';
    }

    @Override
    public EntityDimensions getDimensions(Pose pPose) {
        char[] data = this.getIDFlags();
        if (data[getIndex(MAXSIZE)] != '0') {
            return super.getDimensions(pPose).scale(4);
        }
        return super.getDimensions(pPose);
    }

    public void tick() {

        HitResult hitresult = ProjectileUtil.getHitResult(this, this::canHitEntity);
        boolean flag = false;
        char[] data = this.getIDFlags();

        if (data[getIndex(FIRE)] != '0')
            this.setSecondsOnFire(1);

        if (data[getIndex(FREEZE)] != '0'
                && level.getBlockState(this.getOnPos()).equals(Blocks.WATER.defaultBlockState())) {
            level.setBlockAndUpdate(this.getOnPos(), Blocks.FROSTED_ICE.defaultBlockState());
        }

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
            double gravity = data[getIndex(WEIGHT)] != '0' ? 0.01 : .03;
            gravity += data[getIndex(MAXSIZE)] != '0' ? 0.01 : 0;
            this.setDeltaMovement(vec31.x, vec31.y - (double) gravity, vec31.z);
        }

        this.setPos(d2, d0, d1);

    }

    @Override
    protected void onHitEntity(EntityHitResult r) {
        Entity entity = r.getEntity();
        char[] data = this.getIDFlags();
        BlockPos pos = entity.getOnPos();

        float damage = 0;

        damage += (data[getIndex(DAMAGE)] == '0') ? 0 : 3;
        damage += (data[getIndex(MAXSIZE)] == '0') ? 0 : 2;
        if (data[getIndex(HOLY)] != '0' && (entity instanceof AbstractSkeleton || entity instanceof Zombie)) {
            damage += 3;
        } else if (data[getIndex(ELDRITCH)] != '0' && (entity instanceof Animal || entity instanceof AbstractGolem)) {
            damage += 3;
        }

        entity.hurt(DamageSource.mobAttack((Player) getOwner()), damage);

        if (data[getIndex(FIRE)] != '0') {
            entity.setSecondsOnFire(10);
        }
        if (data[getIndex(FREEZE)] != '0') {
            entity.clearFire();
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2));
            }
        }
        if (data[getIndex(POISON)] != '0') {
            if (entity instanceof LivingEntity) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1));
            }
        }
        if (data[getIndex(SEASONING)] != '0') {
            if (entity instanceof Player) {
                ((Player) entity).getFoodData().eat(6, 3);
            } else if (entity instanceof Wolf) {
                ((Wolf) entity).heal(6);
            }
        }
        if (data[getIndex(EXPLOSIVE)] != '0') {

            this.level.explode(getOwner(), pos.getX(), pos.getY(), pos.getZ(), 3, BlockInteraction.NONE);
        }
        super.onHitEntity(r);
        this.kill();
    }

    @Override
    protected void onHitBlock(BlockHitResult r) {

        BlockPos pos = r.getBlockPos();

        char[] data = this.getIDFlags();

        if (data[getIndex(RICOCHET)] != '0' && bouncetimes > 0) {

            this.setDeltaMovement(new Vec3(this.getDeltaMovement().x() / 2, 0.6, this.getDeltaMovement().z() / 2));
            bouncetimes--;
            return;
        }

        if (data[getIndex(EXPLOSIVE)] != '0') {

            this.level.explode(getOwner(), pos.getX(), pos.getY(), pos.getZ(), 3, BlockInteraction.NONE);
        }
        if (data[getIndex(FIRE)] != '0') {
            setFire(pos);
        }

        if (data[getIndex(CAUSTICITY)] != '0') {

            for (int x = pos.getX() - 1; x < pos.getX() + 1; x++) {
                for (int y = pos.getY() - 1; y < pos.getY() + 1; y++) {
                    for (int z = pos.getZ() - 1; z < pos.getZ() + 1; z++) {

                        if (Math.random() > 0.75
                                && !level.getBlockState(new BlockPos(x, y, z)).equals(Blocks.WATER.defaultBlockState())
                                && !level.getBlockState(new BlockPos(x, y, z))
                                        .equals(Blocks.LAVA.defaultBlockState())) {

                            level.setBlockAndUpdate(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState());

                            level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.1f,
                                    (float) Math.random());
                        }
                    }
                }
            }
        }
        this.kill();
    }

    private void setFire(BlockPos pos) {
        BlockState blockstate = level.getBlockState(pos);
        if (!CampfireBlock.canLight(blockstate) && !CandleBlock.canLight(blockstate)
                && !CandleCakeBlock.canLight(blockstate)) {
            BlockPos blockpos1 = pos.relative(Direction.UP);
            if (BaseFireBlock.canBePlacedAt(level, blockpos1, Direction.UP)) {
                level.playSound(null, blockpos1, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F,
                        level.getRandom().nextFloat() * 0.4F + 0.8F);
                BlockState blockstate1 = BaseFireBlock.getState(level, blockpos1);
                level.setBlock(blockpos1, blockstate1, 11);
            }
        } else {
            level.playSound(null, pos, SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS, 1.0F,
                    level.getRandom().nextFloat() * 0.4F + 0.8F);
            level.setBlock(pos, blockstate.setValue(BlockStateProperties.LIT, Boolean.valueOf(true)), 11);

        }
    }

}
