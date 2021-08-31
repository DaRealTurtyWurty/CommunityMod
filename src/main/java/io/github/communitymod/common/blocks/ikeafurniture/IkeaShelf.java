package io.github.communitymod.common.blocks.ikeafurniture;

import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IkeaShelf extends Block implements IkeaFurniture {
    
    final MeatballTypes type;

    public IkeaShelf(Properties p_49795_, MeatballTypes type) {
        super(p_49795_);
        this.type = type;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(1, 0, 0, 15, 1, 15), Block.box(1, 15, 0, 15, 16, 15), Block.box(1, 7, 0, 15, 9, 15),
                Block.box(0, 0, 0, 1, 16, 15), Block.box(15, 0, 0, 16, 16, 15), Block.box(0, 0, 15, 16, 16, 16));
    }
    
    @Override
    public MeatballTypes getAttribute() {
        return type;
    }

}
