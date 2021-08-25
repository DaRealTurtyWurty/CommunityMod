package io.github.communitymod.common.blocks;

import io.github.communitymod.common.entities.ExtremeTntEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.gameevent.GameEvent;

import javax.annotation.Nullable;

public class ExtremeTnt extends TntBlock {

    public static final BooleanProperty UNSABLE = BlockStateProperties.UNSTABLE;

    public ExtremeTnt(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(UNSABLE, Boolean.TRUE));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_57464_) {
        p_57464_.add(UNSABLE);
    }

    @Override
    public void catchFire(BlockState state, Level pLevel, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
        if (!pLevel.isClientSide) {
            ExtremeTntEntity primedtnt = new ExtremeTntEntity(pLevel, pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, igniter);
            pLevel.addFreshEntity(primedtnt);
            pLevel.playSound(null, primedtnt.getX(), primedtnt.getY(), primedtnt.getZ(), SoundEvents.TNT_PRIMED, SoundSource.BLOCKS, 1.0F, 1.0F);
            pLevel.gameEvent(igniter, GameEvent.PRIME_FUSE, pos);
        }
    }
}