package io.github.communitymod.capabilities.playerskills;

import io.github.communitymod.CommunityMod;
import io.github.communitymod.capabilities.entitylevel.CapabilityMobLevel;
import io.github.communitymod.client.util.ColorText;
import io.github.communitymod.core.CommonEvents;
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
    public static void AwardCombatStats(final LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player & event.getEntityLiving() instanceof Mob) {
            Player player = (Player) event.getSource().getEntity();
            Mob target = (Mob) event.getEntityLiving();
            player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills ->
                    target.getCapability(CapabilityMobLevel.MOB_LEVEL_CAPABILITY).ifPresent(mobLevel -> {
                        skills.AwardCombatXp(Math.round(Math.round(mobLevel.getMobLevel() + (event.getEntityLiving().getMaxHealth() * 3))));
                        player.level.playSound(null, player, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                        CommonEvents.ForgeEvents.skillShowText = ColorText.DARK_AQUA + "+" + (Math.round(mobLevel.getMobLevel() + (event.getEntityLiving().getMaxHealth() * 3))) + " Combat (" + skills.getCombatXp() + "/" + ((Math.pow((skills.getCombatLvl() + 3), 3) - Math.pow((skills.getCombatLvl() + 1), 2)) * 2) + ")";
                        CommonEvents.ForgeEvents.skillShowTimer = 60;
                    }));
            //ClientUtils.SendPrivateMessage("calc: " + "(((" + PlayerSkills.combatLvl + " + 3) ^ 2) - ((" + PlayerSkills.combatLvl + " + 1) ^ 2))");
        }
    }

    @SubscribeEvent
    public static void AwardMiningStats(final BlockEvent.BreakEvent event) {
        int harvestLevel = 0;
        if (!event.getPlayer().isCreative()) {
            event.getPlayer().getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
                if (event.getState().is(BlockTags.CROPS)) {
                    skills.AwardFarmingXp(6);
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    CommonEvents.ForgeEvents.skillShowText = ColorText.DARK_AQUA + "+6 Farming (" + skills.getFarmingXp() + "/" + ((Math.pow((skills.getFarmingLvl() + 3), 3) - Math.pow((skills.getFarmingLvl() + 1), 2)) * 2) + ")";
                    CommonEvents.ForgeEvents.skillShowTimer = 60;
                } else if (event.getState().is(BlockTags.LOGS)) {
                    skills.AwardForagingXp(10);
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    CommonEvents.ForgeEvents.skillShowText = ColorText.DARK_AQUA + "+10 Foraging (" + skills.getForagingXp() + "/" + ((Math.pow((skills.getForagingLvl() + 3), 3) - Math.pow((skills.getForagingLvl() + 1), 2)) * 2) + ")";
                    CommonEvents.ForgeEvents.skillShowTimer = 60;
                } else {
                    skills.AwardMiningXp(((harvestLevel + 1) * 5) + (event.getExpToDrop() * 2));
                    event.getPlayer().level.playSound(null, event.getPlayer(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.PLAYERS, 1f, 1f);
                    CommonEvents.ForgeEvents.skillShowText = ColorText.DARK_AQUA + "+" + (((harvestLevel + 1) * 5) + (event.getExpToDrop() * 2)) + " Mining (" + skills.getMiningXp() + "/" + ((Math.pow((skills.getMiningLvl() + 3), 3) - Math.pow((skills.getMiningLvl() + 1), 2)) * 2) + ")";
                    CommonEvents.ForgeEvents.skillShowTimer = 60;
                }
            });
        }
    }

    @SubscribeEvent
    public static void PersistCapabilityData(final PlayerEvent.Clone event) {
        Player player = event.getPlayer();
        player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(newskills -> {
            if (event.isWasDeath()) {
                event.getOriginal().getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(oldskills -> {
                    newskills.setCombatXp(0);
                    newskills.setCombatLvl(oldskills.getCombatLvl() - 1);

                    newskills.setMiningXp(0);
                    newskills.setMiningLvl(oldskills.getMiningLvl() - 1);

                    newskills.setFarmingXp(0);
                    newskills.setFarmingLvl(oldskills.getFarmingLvl() - 1);

                    newskills.setForagingXp(0);
                    newskills.setForagingLvl(oldskills.getForagingLvl() - 1);

                    newskills.setStrength(oldskills.getStrength());

                    newskills.setDefense(oldskills.getDefense());

                    newskills.setHealth(oldskills.getHealth());
                    newskills.setMaxHealth(oldskills.getMaxHealth());
                });
            }
        });
    }
}
