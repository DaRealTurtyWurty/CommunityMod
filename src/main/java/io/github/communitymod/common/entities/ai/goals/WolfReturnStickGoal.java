package io.github.communitymod.common.entities.ai.goals;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class WolfReturnStickGoal extends Goal {

    private final Wolf wolf;
    private final double speed;

    public WolfReturnStickGoal(Wolf wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.getOwner().isPresent() &&
                this.wolf.isAlive() &&
                this.hasStick() &&
                !this.wolf.isBaby();
    }

    @Override
    public void tick() {
        this.getOwner().ifPresent(owner -> {
            Vec3 ownerPos = owner.position();
            this.wolf.getNavigation().moveTo(ownerPos.x, ownerPos.y, ownerPos.z, this.speed);
            this.wolf.getLookControl().setLookAt(owner);
        });
    }

    @Override
    public boolean canContinueToUse() {
        final AtomicBoolean isNear = new AtomicBoolean(false);
        this.getOwner().ifPresent((owner) -> isNear.set(this.wolf.distanceTo(owner) < 1f));

        return !isNear.get() && this.wolf.isAlive() && this.hasStick();
    }

    @Override
    public void stop() {
        this.getOwner().ifPresent(owner -> {
            if (this.wolf.distanceTo(owner) < 1.2f) {
                if (owner instanceof ServerPlayer playerOwner) {
                    ItemStack stickStack = new ItemStack(Items.STICK);
                    if (!playerOwner.addItem(stickStack)) {
                        Vec3 wolfPos = this.wolf.position();
                        ItemEntity itemEntity =
                                new ItemEntity(this.wolf.level, wolfPos.x, wolfPos.y, wolfPos.z, stickStack);
                        itemEntity.setDefaultPickUpDelay();
                        this.wolf.level.addFreshEntity(itemEntity);
                    }

                    this.wolf.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    playerOwner.getLevel().sendParticles(ParticleTypes.HEART, this.wolf.getX(), this.wolf.getY(),
                            this.wolf.getZ(), 6, 1, 0, 1, 0.3f);
                }
            }
        });
    }

    private boolean hasStick() {
        return this.wolf.getMainHandItem().is(Items.STICK);
    }

    private Optional<LivingEntity> getOwner() {
        return Optional.ofNullable(this.wolf.getOwner());
    }

}
