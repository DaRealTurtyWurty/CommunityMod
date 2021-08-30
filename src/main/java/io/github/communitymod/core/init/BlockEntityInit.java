package io.github.communitymod.core.init;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.block_entities.MeatballChaliceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BlockEntityInit {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
            .create(ForgeRegistries.BLOCK_ENTITIES, CommunityMod.MODID);

    public static final RegistryObject<BlockEntityType<MeatballChaliceBlockEntity>> MEATBALL_CHALICE_BE = BLOCK_ENTITIES
            .register("meatball_chalice", () -> BlockEntityType.Builder
                    .of(MeatballChaliceBlockEntity::new, BlockInit.MEATBALL_CHALICE.get()).build(null));
}
