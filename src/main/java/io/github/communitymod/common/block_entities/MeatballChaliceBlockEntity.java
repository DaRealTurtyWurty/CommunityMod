package io.github.communitymod.common.block_entities;

import java.util.HashMap;

import javax.annotation.Nullable;

import io.github.communitymod.common.blocks.MeatballChalice;
import io.github.communitymod.common.blocks.ikeafurniture.IkeaFurniture;
import io.github.communitymod.core.init.BlockEntityInit;
import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class MeatballChaliceBlockEntity extends BaseContainerBlockEntity {

    protected int timer = 0;

    protected NonNullList<ItemStack> item = NonNullList.withSize(1, ItemStack.EMPTY);

    public MeatballChaliceBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(BlockEntityInit.MEATBALL_CHALICE_BE.get(), pWorldPosition, pBlockState);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, MeatballChaliceBlockEntity bE) {

        if (bE.getTimer() > 0) {
            bE.timer--;
            bE.setChanged();
        }

        if (bE.isEmpty())
            bE.timer = 0;

        if (bE.getBlockState().getValue(MeatballChalice.IS_GLOWING) != bE.getTimer() > 0)
            pLevel.setBlock(pPos, pState.setValue(MeatballChalice.IS_GLOWING, bE.getTimer() > 0), 2);

        if (bE.timer == 1) {
            if (bE.selectTargetBlock(pLevel, pPos) != null) {
                bE.getItem().getOrCreateTag().putBoolean(bE.selectTargetBlock(pLevel, pPos).toString(), true);

            }
        }
    }

    @Nullable
    private MeatballTypes selectTargetBlock(Level pLevel, BlockPos pPos) {

        HashMap<BlockPos, IkeaFurniture> validBlocks = new HashMap<>();

        for (int x = pPos.getX() - 4; x < pPos.getX() + 4; x++) {
            for (int y = pPos.getY() - 3; y < pPos.getY() + 3; y++) {
                for (int z = pPos.getZ() - 4; z < pPos.getZ() + 4; z++) {

                    BlockPos pos = new BlockPos(x, y, z);
                    Block block = pLevel.getBlockState(pos).getBlock();

                    if (block instanceof IkeaFurniture) {
                        validBlocks.put(pos, (IkeaFurniture) block);
                    }

                }
            }
        }

        validBlocks.entrySet().stream()
                .sorted((x, y) -> Double.compare(x.getKey().distSqr(pPos, true), y.getKey().distSqr(pPos, true)));

        if (!validBlocks.isEmpty()) {
            return validBlocks.entrySet().stream().findFirst().get().getValue().getAttribute();
        } else
            return null;
    }

    public ItemStack getItem() {
        return item.get(0);
    }

    public int getTimer() {
        return timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public CompoundTag save(CompoundTag pCompound) {
        super.save(pCompound);
        ContainerHelper.saveAllItems(pCompound, this.item);
        pCompound.putInt("timer", timer);

        return pCompound;
    }

    public void load(CompoundTag pTag) {

        super.load(pTag);
        this.item = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);

        this.timer = pTag.getInt("timer");

        ContainerHelper.loadAllItems(pTag, this.item);
    }

    public void setItem(ItemStack stack) {
        item.set(0, stack);
        this.setChanged();
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(worldPosition, -1, getUpdateTag());
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.save(new CompoundTag());
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    protected Component getDefaultName() {
        return new TextComponent("block.communitymod.meatball_chalice");
    }

    @Override
    protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return getItem(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return getItem();
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        this.setChanged();

        getItem(pIndex).shrink(pCount);

        return getItem();
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {
        item.set(pIndex, pStack);
        this.setChanged();
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;
    }

    @Override
    public void clearContent() {
        this.item.clear();
        this.setChanged();
    }

}
