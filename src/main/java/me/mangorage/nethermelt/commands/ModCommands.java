package me.mangorage.nethermelt.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.mangorage.nethermelt.NetherMelt;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> CMD = dispatcher.register(
                Commands.literal(NetherMelt.MOD_ID)
                        .then(TickCommand.register(dispatcher))

        );

        dispatcher.register(Commands.literal("nethermelt").redirect(CMD));
    }

}
