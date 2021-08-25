package io.github.communitymod.common.entities;

import io.github.communitymod.core.init.EntityInit;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ExtremeTntEntity extends PrimedTnt {

    private LivingEntity igniter;

    public ExtremeTntEntity(EntityType<? extends ExtremeTntEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public ExtremeTntEntity(Level worldIn, double x, double y, double z, @Nullable LivingEntity igniter) {
        this(EntityInit.EXTREME_TNT_ENTITY.get(), worldIn);
        this.setPos(x, y, z);
        double d0 = worldIn.random.nextDouble() * (double)((float)Math.PI * 2F);
        this.lerpMotion(-Math.sin(d0) * 0.02D, 0.2F, -Math.cos(d0) * 0.02D);
        this.setFuse(80);
        this.xOld = x;
        this.yOld = y;
        this.zOld = z;
        this.igniter = igniter;
    }

    @Override
    protected void explode() {
        this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 25.0F, true, Explosion.BlockInteraction.BREAK);
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Nullable
    @Override
    public LivingEntity getOwner() {
        return igniter;
    }
}
