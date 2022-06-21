package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;

public class RootBlockEntity extends BlockEntity {
    private int ticks = 1;
    private boolean init = false; // NBT
    private boolean activated = false; // NBT
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
            if (foams.size() == 0 && activated) {
                // Die
                NetherMelt.getCore().Die(this);
                // getLevel().setBlock(getBlockPos(), Registry.BLOCK_DEAD_ROOT.get().defaultBlockState(), Block.UPDATE_ALL);
                // getLevel().scheduleTick(getBlockPos(), getLevel().getBlockState(getBlockPos()).getBlock(), 1);
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
    protected void saveAdditional(CompoundTag Tag) {
        Tag.putBoolean("init", init);
        Tag.putBoolean("activated", activated);
    }

    @Override
    public void load(CompoundTag Tag) {
        if (Tag.contains("init")) {
            init = Tag.getBoolean("init");
        }

        if (Tag.contains("activated")) {
            activated = Tag.getBoolean("activated");
        }
    }
}

