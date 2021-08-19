package io.github.communitymod.common.commands.impl;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.client.util.ColorText;
import io.github.communitymod.common.commands.BaseCommand;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class StatsCommand extends BaseCommand {
    public StatsCommand(String name, int permissionLevel, boolean enabled) {
        super(name, permissionLevel, enabled);
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> setExecution() {
        return builder.then(Commands.argument("player", EntityArgument.player()).executes(
                source -> execute((CommandSource) source.getSource(), EntityArgument.getPlayer(source, "player"))));
    }

    private int execute(CommandSource source, ServerPlayer player) {
        player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
            //@todo still having command errors, if you know the fix feel free to apply it.
            player.displayClientMessage(Component.nullToEmpty(ColorText.BOLD.toString() + ColorText.AQUA + "Stats for" + player.getGameProfile().getName()
                    + ColorText.RESET + ColorText.YELLOW +
                    "Combat: " + skills.getCombatLvl() + " " + skills.getCombatXp() + " " +
                    "Mining: " + skills.getMiningLvl() + " " + skills.getMiningXp() + " " +
                    "Foraging: " + skills.getForagingLvl() + " " + skills.getForagingXp() + " " +
                    "Farming: " + skills.getFarmingLvl() + " " + skills.getFarmingXp() + " " +
                    ColorText.RED + "Health: " + skills.getHealth() + " " + skills.getMaxHealth() + " " +
                    ColorText.GREEN + "Defense: " + skills.getDefense() + " " +
                    ColorText.RED + "Strength: " + skills.getStrength() + " " +
                    ColorText.WHITE + "Speed: " + skills.getSpeed() + " " + skills.getBaseSpeed() + " "), false);
        });
        return Command.SINGLE_SUCCESS;
    }
}
