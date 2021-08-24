package io.github.communitymod.core;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.common.entities.BeanEntity;
import io.github.communitymod.common.entities.GooseEntity;
import io.github.communitymod.common.entities.ThrownStickEntity;
import io.github.communitymod.common.entities.ai.goals.WolfFetchStickGoal;
import io.github.communitymod.common.entities.ai.goals.WolfReturnStickGoal;
import io.github.communitymod.core.init.DimensionInit;
import io.github.communitymod.core.init.EntityInit;
import io.github.communitymod.core.init.StructureInit;
import io.github.communitymod.core.world.structures.ConfiguredStructures;
import io.github.communitymod.core.world.structures.tent.TentPieces;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.StructureSettings;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import java.util.HashMap;
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
        public static void onEntityJoined(final LivingSpawnEvent event) {
            if (event.getEntity() instanceof Wolf wolf) {
                wolf.goalSelector.addGoal(1, new WolfReturnStickGoal(wolf, 1.2d));
                wolf.goalSelector.addGoal(4, new WolfFetchStickGoal(wolf, 1.2d));
            }
        }

        @SuppressWarnings("resource")
        @SubscribeEvent
        public static void addDimensionalSpacing(final WorldEvent.Load event) {
            if (event.getWorld() instanceof final ServerLevel serverLevel) {
                final Map<StructureFeature<?>, StructureFeatureConfiguration> tempMap = new HashMap<>(
                        serverLevel.getChunkSource().generator.getSettings().structureConfig());
                tempMap.putIfAbsent(StructureInit.TENT_STRUCTURE.get(),
                        StructureSettings.DEFAULTS.get(StructureInit.TENT_STRUCTURE.get()));
                serverLevel.getChunkSource().generator.getSettings().structureConfig = tempMap;
            }
        }

        @SubscribeEvent
        public static void biomeModification(final BiomeLoadingEvent event) {
            event.getGeneration().getStructures().add(() -> ConfiguredStructures.CONFIGURE_TENT_STRUCTURE);
        }
    }

    @EventBusSubscriber(modid = CommunityMod.MODID, bus = Bus.MOD)
    public static final class ModEvents {

        @SubscribeEvent
        public static void commonSetup(final FMLCommonSetupEvent event) {
            DimensionInit.setup();

            // Structures
            StructureInit.register(TentPieces.TENT_TYPE, "tent_structure");
            StructureInit.setupStructures();
            ConfiguredStructures.registerConfiguredStructures();

            //Capabilities
            CapabilityPlayerSkills.register();
            CapabilityMobLevel.register();
        }

        @SubscribeEvent
        public static void entityAttributes(final EntityAttributeCreationEvent event) {
            event.put(EntityInit.BEAN_ENTITY.get(), BeanEntity.createAttributes().build());
            event.put(EntityInit.GOOSE_ENTITY.get(), GooseEntity.createAttributes().build());
        }
    }
}
