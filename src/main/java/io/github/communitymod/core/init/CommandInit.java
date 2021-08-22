package io.github.communitymod.core.init;

import com.mojang.brigadier.CommandDispatcher;
import io.github.communitymod.common.commands.BaseCommand;
import io.github.communitymod.common.commands.impl.StatsCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.ArrayList;

public class CommandInit {
    private static final ArrayList<BaseCommand> commands = new ArrayList<>();

    public static void registerCommands(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        commands.add(new StatsCommand("stats", 0, true));

        commands.forEach(command -> {
            if (command.isEnabled() && command.setExecution() != null) {
                dispatcher.register(command.getBuilder());
            }
        });
    }
}
