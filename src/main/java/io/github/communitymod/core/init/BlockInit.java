package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockInit {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            CommunityMod.MODID);

    public static final RegistryObject<Block> BEAN_BLOCK = BLOCKS.register("bean_block", () -> new Block(
            BlockBehaviour.Properties.of(Material.VEGETABLE).speedFactor(0.5f).sound(SoundType.CORAL_BLOCK)));

    public static final RegistryObject<Block> GRANMOND = BLOCKS.register("granmond",
            () -> new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.DIAMOND)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)));


    @SuppressWarnings("unchecked")
    public static <T extends Block> void blacklistBlockItem(final RegistryObject<T> block) {
        ItemInit.BLOCK_ITEM_BLACKLIST.add((RegistryObject<Block>) block);
    }

    public static void toBlacklist() {
        // blacklistBlockItem(BlockInit.MY_BLOCK);
    }
}
