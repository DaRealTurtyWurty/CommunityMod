package io.github.communitymod.core.init;

import com.mojang.brigadier.CommandDispatcher;
import io.github.communitymod.CommunityMod;
import io.github.communitymod.common.commands.BaseCommand;
import io.github.communitymod.common.commands.impl.StatsCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = CommunityMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CommandInit {
    private static final ArrayList<BaseCommand> COMMANDS = new ArrayList<>();

    @SubscribeEvent
    public static void registerCommands(final RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        COMMANDS.add(new StatsCommand("stats", 0, true));

        COMMANDS.forEach(command -> {
            if (command.isEnabled() && command.setExecution() != null) {
                dispatcher.register(command.getBuilder());
            }
        });
    }
}
