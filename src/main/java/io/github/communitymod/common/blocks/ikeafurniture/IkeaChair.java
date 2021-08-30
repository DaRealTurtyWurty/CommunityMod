package io.github.communitymod.common.blocks.ikeafurniture;

import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IkeaChair extends Block implements IkeaFurniture {

    final MeatballTypes type;

    public IkeaChair(Properties properties, MeatballTypes type) {
        super(properties);
        this.type = type;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(11, 0, 2, 13, 8, 4), Block.box(3, 0, 2, 5, 8, 4), Block.box(11, 0, 12, 13, 8, 14),
                Block.box(3, 0, 12, 5, 8, 14), Block.box(3, 8, 1, 13, 10, 12), Block.box(3, 8, 12, 13, 22, 14));
    }

    @Override
    public MeatballTypes getAttribute() {
        return type;
    }

}
