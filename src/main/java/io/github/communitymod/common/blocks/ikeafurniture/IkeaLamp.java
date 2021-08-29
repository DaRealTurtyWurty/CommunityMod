package io.github.communitymod.common.blocks.ikeafurniture;

import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IkeaLamp extends Block implements IkeaFurniture {

    final MeatballTypes type;

    public IkeaLamp(Properties p_49795_, MeatballTypes type) {
        super(p_49795_.lightLevel((x) -> {
            return 12;
        }));
        this.type = type;

    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return Shapes.or(Block.box(5, 0, 5, 11, 6, 11), Block.box(6, 6, 6, 10, 9, 10), Block.box(4, 9, 4, 12, 17, 12));
    }

    @Override
    public MeatballTypes getAttribute() {
        return type;
    }

}
