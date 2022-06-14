package me.mangorage.nethermelt.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TickCommand implements Command<CommandSourceStack> {

    private static final TickCommand CMD = new TickCommand();

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return Commands.literal("tickblock")
                .requires(cs -> cs.hasPermission(0))
                .then(Commands.argument("pos", BlockPosArgument.blockPos())
                        .executes(CMD))
                .executes((CMD));
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
        Level level = context.getSource().getLevel();
        BlockState state = level.getBlockState(pos);

        if (level.isClientSide) {
            return 0;
        }

        if (state.isRandomlyTicking()) {
            state.randomTick((ServerLevel) level, pos, level.random);
        } else {
            state.tick((ServerLevel) level, pos, level.random);
        }

        level.scheduleTick(pos, level.getBlockState(pos).getBlock(), 1);



        return 1;
    }
}
