package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class RootBlockEntity extends BlockEntity {
    private int ticks = 1;
    private boolean init = false;

    private boolean activated = false;
    private ArrayList<BlockPos> foams = new ArrayList<>();

    public RootBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.BLOCKENTITY_ROOT.get(), pos, state);
    }


    public void tick() {
        if (NetherMelt.getCore() == null)
            return;
        if (getLevel().isClientSide)
            return;

        if (getBlockState().getValue(RootBlock.ACTIVATED) && !init) {
            ticks +=1;
            if (ticks % 20 == 0) {
                init = true;

                ArrayList<BlockPos> growto = new ArrayList<>();

                growto.add(getBlockPos().above(1));
                growto.add(getBlockPos().below(1));
                growto.add(getBlockPos().north(1));
                growto.add(getBlockPos().south(1));
                growto.add(getBlockPos().east(1));
                growto.add(getBlockPos().west(1));

                growto.forEach(Blockpos -> {
                    NetherMelt.getCore().Grow(this, (ServerLevel) getLevel(), Blockpos, getLevel().getBlockState(Blockpos));
                });

                activated();
            }
        }

        if (init) {
            ticks +=1;
        }

        if (init && ticks % 20 == 0) {
            LogManager.getLogger().info("Foam: " + foams.size());
            if (foams.size() == 0 && activated) {
                // Die
                getLevel().setBlock(getBlockPos(), Registry.BLOCK_DEAD_ROOT.get().defaultBlockState(), Block.UPDATE_ALL); // Temp, replace with Dead Root!
            }
        }
    }

    public void addFoam(BlockPos pos) {
        if (!foams.contains(pos)) {
            foams.add(pos);
        }
    }

    public void activated() {
        activated = true;
    }

    public void removeFoam(BlockPos pos) {
        foams.remove(pos);
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }


    @Override
    public void handleUpdateTag(CompoundTag tag) {

    }

    @Override
    public void onLoad() {

    }


}
