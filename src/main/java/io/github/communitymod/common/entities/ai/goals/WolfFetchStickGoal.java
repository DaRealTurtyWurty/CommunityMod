package io.github.communitymod.common.entities.ai.goals;

import io.github.communitymod.common.entities.ThrownStickEntity;
import io.github.communitymod.core.init.EntityInit;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.CapabilityItemHandler;

import java.util.List;
import java.util.Optional;

public class WolfFetchStickGoal extends Goal {

    private final Wolf wolf;
    private ThrownStickEntity targetStick;

    public WolfFetchStickGoal(Wolf wolf) {
        this.wolf = wolf;
    }

    @Override
    public boolean canUse() {
        Level level = wolf.level;

        List<ThrownStickEntity> nearSticks = level.getEntities(EntityInit.THROWN_STICK.get(), this.wolf.getBoundingBox().inflate(10.0f),
                thrownStickEntity -> thrownStickEntity.getOwner() == this.wolf.getOwner());

        Optional<ThrownStickEntity> stickEntity = nearSticks.stream().reduce(this::findClosest);

        if (stickEntity.isPresent()) {
            this.targetStick = stickEntity.get();
            return true;
        }

        return false;
    }

    @Override
    public void tick() {
        boolean stableDestination = this.wolf.getNavigation().isStableDestination(this.targetStick.getOnPos());
        if (stableDestination) {
            this.wolf.getNavigation().moveTo(this.targetStick, this.wolf.getSpeed() * 1.2d);
        }
    }

    @Override
    public void stop() {
        if (this.targetStick.distanceTo(this.wolf) <= 0.8f) {
            this.targetStick.discard();

            this.wolf.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                    .ifPresent(iItemHandler ->
                            iItemHandler.insertItem(0, Items.STICK.getDefaultInstance(), false));
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.targetStick.isOnGround() && this.wolf.distanceTo(this.targetStick) <= 1f;
    }

    private ThrownStickEntity findClosest(ThrownStickEntity thrownStickEntity, ThrownStickEntity thrownStickEntity2) {
        float distance1 = this.wolf.distanceTo(thrownStickEntity);
        float distance2 = this.wolf.distanceTo(thrownStickEntity2);

        return distance1 <= distance2 ? thrownStickEntity : thrownStickEntity2;
    }

}
