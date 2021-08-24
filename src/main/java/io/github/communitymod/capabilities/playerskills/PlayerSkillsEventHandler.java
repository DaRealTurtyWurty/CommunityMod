package io.github.communitymod.capabilities.playerskills;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.capabilities.entitylevel.DefaultMobLevel;
import io.github.communitymod.core.GameplayEvents;
import io.github.communitymod.core.util.ColorConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CommunityMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSkillsEventHandler {
    @SubscribeEvent
    public static void onAttachCapabilitiesEvent(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player) {
            PlayerSkillsProvider providerskills = new PlayerSkillsProvider();
            event.addCapability(new ResourceLocation(CommunityMod.MODID, "skills"), providerskills);
            event.addListener(providerskills::invalidate);
        }
    }

    @SubscribeEvent
    public static void awardCombatStats(final LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player & event.getEntityLiving() instanceof Mob) {
            Player player = (Player) event.getSource().getEntity();
            Mob target = (Mob) event.getEntityLiving();
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills ->
                    target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                        DefaultPlayerSkills skills1 = (DefaultPlayerSkills) skills;
                        DefaultMobLevel actualMobLevel = (DefaultMobLevel) mobLevel;
                        skills1.awardCombatXp(Math.round(Math.round(actualMobLevel.mobLevel + (event.getEntityLiving().getMaxHealth() * 3))));
                        player.level.playSound(null, player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                        GameplayEvents.skillShowText = ColorConstants.DARK_AQUA + "+" + (Math.round(actualMobLevel.mobLevel + (event.getEntityLiving().getMaxHealth() * 3))) + " Combat (" + skills1.combatLvl + ": " + skills1.combatXp + "/" + ((Math.pow((skills1.combatLvl + 3), 3) - Math.pow((skills1.combatLvl + 1), 2)) * 2) + ")";
                        GameplayEvents.skillShowTimer = 60;
                    }));
            //ClientUtils.SendPrivateMessage("calc: " + "(((" + PlayerSkills.combatLvl + " + 3) ^ 2) - ((" + PlayerSkills.combatLvl + " + 1) ^ 2))");
        }
    }

    @SubscribeEvent
    public static void awardMiningStats(final BlockEvent.BreakEvent event) {
        int harvestLevel = 0;
        if (!event.getPlayer().isCreative()) {
            event.getPlayer().getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                DefaultPlayerSkills skills1 = (DefaultPlayerSkills) skills;
                if (event.getState().is(BlockTags.CROPS)) {
                    skills.awardFarmingXp(6);
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    GameplayEvents.skillShowText = ColorConstants.DARK_AQUA + "+6 Farming (" + skills1.farmingLvl + ": " + skills1.farmingXp + "/" + ((Math.pow((skills1.farmingLvl + 3), 3) - Math.pow((skills1.farmingLvl + 1), 2)) * 2) + ")";
                    GameplayEvents.skillShowTimer = 60;
                } else if (event.getState().is(BlockTags.LOGS)) {
                    skills.awardForagingXp(10);
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    GameplayEvents.skillShowText = ColorConstants.DARK_AQUA + "+10 Foraging (" + skills1.foragingLvl + ": " + skills1.foragingXp + "/" + ((Math.pow((skills1.foragingLvl + 3), 3) - Math.pow((skills1.foragingLvl + 1), 2)) * 2) + ")";
                    GameplayEvents.skillShowTimer = 60;
                } else {
                    skills.awardMiningXp(((harvestLevel + 1) * 5) + (event.getExpToDrop() * 2));
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    GameplayEvents.skillShowText = ColorConstants.DARK_AQUA + "+" + (((harvestLevel + 1) * 5) + (event.getExpToDrop() * 2)) + " Mining (" + skills1.miningLvl + ": " + skills1.miningXp + "/" + ((Math.pow((skills1.miningLvl + 3), 3) - Math.pow((skills1.miningLvl + 1), 2)) * 2) + ")";
                    GameplayEvents.skillShowTimer = 60;
                }
            });
        }
    }

    @SubscribeEvent
    public static void persistCapabilityData(final PlayerEvent.Clone event) {
        System.out.println("step 1");
        Player player = event.getPlayer();
        GameplayEvents.miningBonus = 0;
        GameplayEvents.farmingBonus = 0;
        GameplayEvents.foragingBonus = 0;
        player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(newskills -> {
            System.out.println("step 2");
            if (event.isWasDeath()) {
                System.out.println("step 3");
                event.getOriginal().getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(oldskills -> {
                    System.out.println("step 4");
                    DefaultPlayerSkills newskills1 = (DefaultPlayerSkills) newskills;
                    DefaultPlayerSkills oldskills1 = (DefaultPlayerSkills) oldskills;

                    newskills1.combatXp = (0);
                    newskills1.combatLvl = (oldskills1.combatLvl - 1);

                    newskills1.miningXp = (0);
                    newskills1.miningLvl = (oldskills1.miningLvl - 1);

                    newskills1.farmingXp = (0);
                    newskills1.farmingLvl = (oldskills1.farmingLvl - 1);

                    newskills1.foragingXp = (0);
                    newskills1.foragingLvl = (oldskills1.foragingLvl - 1);

                    newskills1.strength = (oldskills1.strength);

                    newskills1.defense = (oldskills1.defense);

                    newskills1.health = (oldskills1.health);
                    newskills1.maxHealth = (oldskills1.maxHealth);

                });
            }
        });
    }
}
