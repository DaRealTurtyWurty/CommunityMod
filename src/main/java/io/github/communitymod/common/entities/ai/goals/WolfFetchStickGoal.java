package io.github.communitymod.common.entities.ai.goals;

import io.github.communitymod.common.entities.ThrownStickEntity;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

public class WolfFetchStickGoal extends Goal {

    private final Wolf wolf;
    private final double speed;
    private ThrownStickEntity targetStick;

    public WolfFetchStickGoal(Wolf wolf, double speed) {
        this.wolf = wolf;
        this.speed = speed;
        this.setFlags(EnumSet.of(Flag.LOOK, Flag.MOVE, Flag.JUMP));
    }

    @Override
    public boolean canUse() {
        if (this.wolf.isBaby()) {
            return false;
        }

        Level level = wolf.level;

        List<ThrownStickEntity> nearSticks = level.getEntities(EntityInit.THROWN_STICK.get(), this.wolf.getBoundingBox().inflate(10.0f),
                thrownStickEntity -> thrownStickEntity.getOwner() == this.wolf.getOwner());

        Optional<ThrownStickEntity> stickEntity = nearSticks.stream().reduce(this::findClosest);

        if (stickEntity.isPresent()) {
            this.targetStick = stickEntity.get();
            return !(this.wolf.getMainHandItem().getItem() == Items.STICK);
        }

        return false;
    }

    @Override
    public void tick() {
        Vec3 stickPos = this.targetStick.position();
        this.wolf.getNavigation().moveTo(stickPos.x, stickPos.y, stickPos.z, this.speed);
        this.wolf.getLookControl().setLookAt(this.targetStick);
    }

    @Override
    public void stop() {
        if (this.targetStick.distanceTo(this.wolf) <= 1.5f) {
            this.wolf.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.STICK));
            this.targetStick.discard();
        }
    }

    @Override
    public boolean canContinueToUse() {
        float distance = this.wolf.distanceTo(this.targetStick);

        return this.targetStick.isAlive() &&
                !this.targetStick.isOnGround() &&
                distance < 1f &&
                this.wolf.getNavigation().isStableDestination(this.targetStick.getOnPos());
    }

    private ThrownStickEntity findClosest(ThrownStickEntity thrownStickEntity, ThrownStickEntity thrownStickEntity2) {
        float distance1 = this.wolf.distanceTo(thrownStickEntity);
        float distance2 = this.wolf.distanceTo(thrownStickEntity2);

        return distance1 <= distance2 ? thrownStickEntity : thrownStickEntity2;
    }

}
