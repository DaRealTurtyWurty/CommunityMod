package io.github.communitymod.common.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import io.github.communitymod.common.block_entities.MeatballChaliceBlockEntity;
import io.github.communitymod.core.init.BlockEntityInit;
import io.github.communitymod.core.init.ItemInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MeatballChalice extends Block implements EntityBlock {

    public static final BooleanProperty IS_GLOWING = BooleanProperty.create("glowing");

    public MeatballChalice(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(IS_GLOWING, Boolean.FALSE));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> pBuilder) {
        pBuilder.add(IS_GLOWING);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(1, 13, 1, 15, 16, 15), Block.box(2, 5, 2, 14, 13, 14),
                Block.box(0, 0, 0, 16, 5, 16));
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, Random pRand) {

        if (pState.getValue(IS_GLOWING)) {
            for (int j = 0; j < 10; ++j) {
                
                double d0 = pLevel.random.nextDouble();
                float f = (pLevel.random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (pLevel.random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (pLevel.random.nextFloat() - 0.5F) * 0.2F;
                double d1 = Mth.lerp(d0, (double) pPos.getX(), (double) pPos.getX())
                        + (pLevel.random.nextDouble() - 0.5D) + 0.5D;
                double d2 = Mth.lerp(d0, (double) pPos.getY(), (double) pPos.getY()) + pLevel.random.nextDouble()
                        + 0.3D;
                double d3 = Mth.lerp(d0, (double) pPos.getZ(), (double) pPos.getZ())
                        + (pLevel.random.nextDouble() - 0.5D) + 0.5D;
                
                pLevel.addParticle(ParticleTypes.END_ROD, d1, d2, d3, (double) f, (double) f1, (double) f2);
            }
        }
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand,
            BlockHitResult pHit) {

        if (!pPlayer.isCrouching()) {

            MeatballChaliceBlockEntity blockEntity = ((MeatballChaliceBlockEntity) (pLevel.getBlockEntity(pPos)));

            if (pPlayer.getItemInHand(pHand).isEmpty()) {

                pPlayer.setItemInHand(pHand, blockEntity.getItem().copy());

                ((MeatballChaliceBlockEntity) (pLevel.getBlockEntity(pPos))).removeItem(0, 1);

                pLevel.setBlock(pPos, pState.setValue(IS_GLOWING, false), 2);

                return InteractionResult.SUCCESS;

            } else if (pPlayer.getItemInHand(pHand).getItem().equals(ItemInit.MEATBALL_WAND.get())
                    && blockEntity.isEmpty()) {

                blockEntity.setItem(pPlayer.getItemInHand(pHand).copy());

                blockEntity.setTimer(200);

                pPlayer.getItemInHand(pHand).shrink(1);

                return InteractionResult.CONSUME;

            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new MeatballChaliceBlockEntity(pPos, pState);
    }

    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level p_152694_, BlockState p_152695_,
            BlockEntityType<T> p_152696_) {
        return p_152694_.isClientSide ? null
                : createTickerHelper(p_152696_, BlockEntityInit.MEATBALL_CHALICE_BE.get(),
                        MeatballChaliceBlockEntity::serverTick);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    protected static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> p_152133_, BlockEntityType<E> p_152134_, BlockEntityTicker<? super E> p_152135_) {
        return p_152134_ == p_152133_ ? (BlockEntityTicker<A>) p_152135_ : null;
    }
}
