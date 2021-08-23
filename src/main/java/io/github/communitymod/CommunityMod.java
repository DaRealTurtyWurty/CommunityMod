package io.github.communitymod;

import io.github.communitymod.core.config.Config;
import io.github.communitymod.core.init.*;
import io.github.communitymod.core.network.PacketHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

@Mod(CommunityMod.MODID)
public class CommunityMod {

    public static final String MODID = "communitymod";

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {

        @Override
        public ItemStack makeIcon() {
            final var resourceLocation = new ResourceLocation(Config.CLIENT.tabIcon.get());
            return (ForgeRegistries.ITEMS.containsKey(resourceLocation)
                    ? ForgeRegistries.ITEMS.getValue(resourceLocation)
                    : ItemInit.BEANS.get()).getDefaultInstance();
        }
    };

    public CommunityMod() {
        final var bus = FMLJavaModLoadingContext.get().getModEventBus();

        EnchantmentInit.ENCHANTMENTS.register(bus);
        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        StructureInit.STRUCTURES.register(bus);
        BlockInit.toBlacklist();
        BlockEntityInit.BLOCK_ENTITIES.register(bus);
        EntityInit.ENTITIES.register(bus);
        EntityInit.registerSpawnEggs();
        SoundsInit.SOUNDS.register(bus);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.CLIENT_SPEC);

        PacketHandler.init();
    }
}
