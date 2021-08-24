package io.github.communitymod.common.commands.impl;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.communitymod.capabilities.playerskills.CapabilityPlayerSkills;
import io.github.communitymod.capabilities.playerskills.DefaultPlayerSkills;
import io.github.communitymod.common.commands.BaseCommand;
import io.github.communitymod.core.util.ColorConstants;
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
        return builder.then(Commands.argument("player", EntityArgument.player()).executes(StatsCommand::execute));
    }

    static int execute(CommandContext<CommandSourceStack> commandContext) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(commandContext, "player");
        player.getCapability(CapabilityPlayerSkills.PLAYER_STATS_CAPABILITY).ifPresent(skills -> {
            DefaultPlayerSkills actualSkills = (DefaultPlayerSkills) skills;
            player.displayClientMessage(Component.nullToEmpty(ColorConstants.AQUA + ColorConstants.BOLD + "Stats for" + player.getGameProfile().getName()
                    + ColorConstants.RESET + ColorConstants.YELLOW +
                    " Combat: " + actualSkills.combatLvl + " " + actualSkills.combatXp + " " +
                    "Mining: " + actualSkills.miningLvl + " " + actualSkills.miningXp + " " +
                    "Foraging: " + actualSkills.foragingLvl + " " + actualSkills.foragingXp + " " +
                    "Farming: " + actualSkills.farmingLvl + " " + actualSkills.farmingXp + " " +
                    ColorConstants.RED + "Health: " + actualSkills.health + " " + actualSkills.maxHealth + " " +
                    ColorConstants.GREEN + "Defense: " + actualSkills.defense + " " + actualSkills.baseDefense + " " +
                    ColorConstants.RED + "Strength: " + actualSkills.strength + " " + actualSkills.baseStrength + " " +
                    ColorConstants.WHITE + "Speed: " + actualSkills.speed + " " + actualSkills.baseSpeed + " "), false);
        });
        return Command.SINGLE_SUCCESS;
    }
}