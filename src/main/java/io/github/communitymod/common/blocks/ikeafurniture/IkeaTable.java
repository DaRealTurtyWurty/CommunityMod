package io.github.communitymod.common.blocks.ikeafurniture;

import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IkeaTable extends Block implements IkeaFurniture {

    final MeatballTypes type;

    public IkeaTable(Properties p_49795_, MeatballTypes type) {
        super(p_49795_);
        this.type = type;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(2, 0, 2, 4, 11, 4), Block.box(2, 0, 12, 4, 11, 14), Block.box(12, 0, 12, 14, 11, 14),
                Block.box(12, 0, 2, 14, 11, 4), Block.box(1, 11, 1, 15, 14, 15));
    }

    @Override
    public MeatballTypes getAttribute() {
        return type;
    }

}
