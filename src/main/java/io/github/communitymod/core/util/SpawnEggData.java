package io.github.communitymod.core.util;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.DispenserBlock;

import java.awt.Color;

public final class SpawnEggData {
    private DispenseItemBehavior dispenseBehaviour = new DefaultDispenseItemBehavior() {
        @Override
        protected ItemStack execute(final BlockSource source, final ItemStack stack) {
            final var direction = source.getBlockState().getValue(DispenserBlock.FACING);
            final EntityType<?> type = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
            type.spawn(source.getLevel(), stack, null, source.getPos().relative(direction),
                    MobSpawnType.DISPENSER, direction != Direction.UP, false);
            stack.shrink(1);
            return stack;
        }
    };
    public final int primaryColor;
    public final int secondaryColor;

    public SpawnEggData(final Color primaryColor, final Color secondaryColor) {
        this(primaryColor.getRGB(), secondaryColor.getRGB());
    }

    public SpawnEggData(final int primaryColor, final int secondaryColor) {
        this.primaryColor = primaryColor;
        this.secondaryColor = secondaryColor;
    }

    public DispenseItemBehavior getDispenseBehaviour() {
        return this.dispenseBehaviour;
    }

    public SpawnEggData setDispenseBehaviour(final DispenseItemBehavior dispenseBehaviour) {
        this.dispenseBehaviour = dispenseBehaviour;
        return this;
    }
}
