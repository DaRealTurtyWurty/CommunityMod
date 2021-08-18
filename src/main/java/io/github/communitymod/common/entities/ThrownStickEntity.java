package io.github.communitymod.common.entities;

import io.github.communitymod.core.init.EntityInit;
import net.minecraft.MethodsReturnNonnullByDefault;
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

    public ThrownStickEntity(EntityType<? extends ThrowableItemProjectile> type, Level level) {
        super(type, level);
    }

    public ThrownStickEntity(double x, double eyeY, double z, Level level) {
        super(EntityInit.THROWN_STICK.get(), x, eyeY, z, level);
    }

    public ThrownStickEntity(LivingEntity user, Level level) {
        super(EntityInit.THROWN_STICK.get(), user, level);
    }

    @MethodsReturnNonnullByDefault
    @Override
    protected Item getDefaultItem() {
        return Items.STICK;
    }

    @Override
    protected void onHitBlock(BlockHitResult p_37258_) {
        super.onHitBlock(p_37258_);

        this.setDeltaMovement(Vec3.ZERO);
    }

    @Override
    public void playerTouch(Player pEntity) {
        if (!pEntity.level.isClientSide()) {
            pEntity.addItem(this.getDefaultItem().getDefaultInstance());
            this.discard();
        }
    }

}
