package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.blocks.ExtremeTnt;
import io.github.communitymod.common.blocks.MeatballChalice;
import io.github.communitymod.common.blocks.ikeafurniture.IkeaChair;
import io.github.communitymod.common.blocks.ikeafurniture.IkeaLamp;
import io.github.communitymod.common.blocks.ikeafurniture.IkeaShelf;
import io.github.communitymod.common.blocks.ikeafurniture.IkeaTable;
import io.github.communitymod.core.util.MeatballTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
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

    public static final RegistryObject<Block> EXTREME_TNT = BLOCKS.register("extreme_tnt",
            () -> new ExtremeTnt(BlockBehaviour.Properties.copy(Blocks.TNT)));
    
    public static final RegistryObject<Block> MEATBALL_CHALICE = BLOCKS.register("meatball_chalice",
            () -> new MeatballChalice(BlockBehaviour.Properties.copy(Blocks.QUARTZ_BLOCK).lightLevel((x) -> {
                return x.getValue(MeatballChalice.IS_GLOWING) ? 10 : 0;
            })));

    public static final RegistryObject<Block> OAK_CHAIR = BLOCKS.register("oak_chair",
            () -> new IkeaChair(Properties.copy(Blocks.OAK_PLANKS), MeatballTypes.FASTRELOAD));

    public static final RegistryObject<Block> WOOL_CHAIR = BLOCKS.register("wool_chair",
            () -> new IkeaChair(Properties.copy(Blocks.WHITE_WOOL), MeatballTypes.WEIGHT));

    public static final RegistryObject<Block> WARPED_CHAIR = BLOCKS.register("warped_chair",
            () -> new IkeaChair(Properties.copy(Blocks.WARPED_STEM), MeatballTypes.POISON));

    public static final RegistryObject<Block> STONE_BRICK_CHAIR = BLOCKS.register("stone_brick_chair",
            () -> new IkeaChair(Properties.copy(Blocks.STONE_BRICKS), MeatballTypes.DAMAGE));

    public static final RegistryObject<Block> OBSIDIAN_TABLE = BLOCKS.register("obsidian_table",
            () -> new IkeaTable(Properties.copy(Blocks.OBSIDIAN), MeatballTypes.MAXSIZE));

    public static final RegistryObject<Block> OAK_TABLE = BLOCKS.register("oak_table",
            () -> new IkeaTable(Properties.copy(Blocks.OAK_WOOD), MeatballTypes.SPEED));

    public static final RegistryObject<Block> GRANITE_TABLE = BLOCKS.register("granite_table",
            () -> new IkeaTable(Properties.copy(Blocks.GRANITE), MeatballTypes.RICOCHET));

    public static final RegistryObject<Block> GLASS_TABLE = BLOCKS.register("glass_table",
            () -> new IkeaTable(Properties.copy(Blocks.QUARTZ_BLOCK), MeatballTypes.GENTILE));

    public static final RegistryObject<Block> PRISMARINE_LAMP = BLOCKS.register("prismarine_lamp",
            () -> new IkeaLamp(Properties.copy(Blocks.PRISMARINE), MeatballTypes.FREEZE));

    public static final RegistryObject<Block> OAK_LAMP = BLOCKS.register("oak_lamp",
            () -> new IkeaLamp(Properties.copy(Blocks.OAK_WOOD), MeatballTypes.FIRE));

    public static final RegistryObject<Block> BIRCH_LAMP = BLOCKS.register("birch_lamp",
            () -> new IkeaLamp(Properties.copy(Blocks.BIRCH_WOOD), MeatballTypes.HOLY));

    public static final RegistryObject<Block> BLACKSTONE_LAMP = BLOCKS.register("blackstone_lamp",
            () -> new IkeaLamp(Properties.copy(Blocks.BLACKSTONE), MeatballTypes.ELDRITCH));

    public static final RegistryObject<Block> SANDSTONE_SHELF = BLOCKS.register("sandstone_shelf",
            () -> new IkeaShelf(Properties.copy(Blocks.SANDSTONE), MeatballTypes.AMOUNT));

    public static final RegistryObject<Block> OAK_SHELF = BLOCKS.register("oak_shelf",
            () -> new IkeaShelf(Properties.copy(Blocks.OAK_PLANKS), MeatballTypes.SEASONING));

    public static final RegistryObject<Block> IRON_SHELF = BLOCKS.register("iron_shelf",
            () -> new IkeaShelf(Properties.copy(Blocks.IRON_BLOCK), MeatballTypes.EXPLOSIVE));

    public static final RegistryObject<Block> BRICK_SHELF = BLOCKS.register("brick_shelf",
            () -> new IkeaShelf(Properties.copy(Blocks.BRICKS), MeatballTypes.CAUSTICITY));



    @SuppressWarnings("unchecked")
    public static <T extends Block> void blacklistBlockItem(final RegistryObject<T> block) {
        ItemInit.BLOCK_ITEM_BLACKLIST.add((RegistryObject<Block>) block);
    }

    public static void toBlacklist() {
        // blacklistBlockItem(BlockInit.MY_BLOCK);
    }
}
