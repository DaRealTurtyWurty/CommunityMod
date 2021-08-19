package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.common.entities.ThrownStickEntity;
import io.github.communitymod.core.init.DimensionInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.StructureInit;
import io.github.communitymod.core.util.ModDataGeneration;
import io.github.communitymod.core.world.structures.ConfiguredStructures;
import io.github.communitymod.core.world.structures.bean.BeanPieces;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class CommonEvents {

    @EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.FORGE)
    public static final class ForgeEvents {

        @SubscribeEvent(receiveCanceled = true)
        public static void onItemUse(final PlayerInteractEvent.RightClickItem event) {
            ItemStack stack = event.getItemStack();
            Entity user = event.getEntity();
            Level level = user.level;

            if (stack.getItem() == Items.STICK && user instanceof ServerPlayer player) {
                if (!player.getAbilities().instabuild) stack.shrink(1);

                ThrownStickEntity stickEntity = new ThrownStickEntity(player, level);
                stickEntity.setItem(stack);
                stickEntity.shootFromRotation(player, user.getXRot(), user.getYRot(), 0.0f, 1.5f, 0.0f);
                stickEntity.setFiredRotation(player.getYRot());
                level.addFreshEntity(stickEntity);

                player.awardStat(Stats.ITEM_USED.get(stack.getItem()), 1);

                event.setCancellationResult(InteractionResult.sidedSuccess(!level.isClientSide()));
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void biomeModification(final BiomeLoadingEvent event) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURE_TEST_STRUCTURE);
        }

        @SubscribeEvent
        public static void addDimensionalSpacing(final WorldEvent.Load event) {
            if (event.getWorld() instanceof ServerLevel serverLevel) {
                Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(serverLevel.getChunkSource().generator.getSettings().structureConfig());
                tempMap.putIfAbsent(StructureInit.BEAN_STRUCTURE.get(), StructureSettings.DEFAULTS.get(StructureInit.BEAN_STRUCTURE.get()));
                serverLevel.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
        }

    }

    @EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
    public static final class ModEvents {

        @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event) {
            DimensionInit.setup();

            //Structures
            register(BeanPieces.BEAN_TYPE, "bean_structure");
            StructureInit.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();
        }

        @SubscribeEvent
        public static void gatherData(final GatherDataEvent event) {
            DataGenerator generator = event.getGenerator();
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

            if (event.includeServer()) {
                //TODO: recipes, loot tables, tags
            }
            if (event.includeClient()) {
                generator.addProvider(new ModDataGeneration.LanguageGen(generator, "en_us"));
				/* TODO: change textures path to /item and /block etc. else it won't work!
				generator.addProvider(
						new ModDataGeneration.ItemModelGen(generator, existingFileHelper));
				generator.addProvider(
						new ModDataGeneration.BlockStateGen(generator, existingFileHelper));
				 */
            }
        }

        @SubscribeEvent
        public static void entityAttributes(final EntityAttributeCreationEvent event) {
            event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
        }

        private static StructurePieceType register(StructurePieceType structurePiece, String key) {
            return Registry.register(Registry.STRUCTURE_PIECE, key.toLowerCase(Locale.ROOT), structurePiece);
        }

    }

}
