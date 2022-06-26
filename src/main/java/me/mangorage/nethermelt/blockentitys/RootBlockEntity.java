package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.config.NetherMeltConfig;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.logging.log4j.LogManager;
import java.util.ArrayList;

public class RootBlockEntity extends BlockEntity {
    private int ticks = 1;
    private boolean activated = false; // NBT
    private int CHARGES = NetherMeltConfig.DEFAULT_CHARGES.get();
    private ArrayList<BlockPos> foams = new ArrayList<>();

    public RootBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.BLOCKENTITY_ROOT.get(), pos, state);
    }


    public void tick() {
        if (NetherMelt.getCore() == null)
            return;
        if (getLevel().isClientSide)
            return;
        if (!getBlockState().getValue(RootBlock.ACTIVATED))
            activated = false;
        if (CHARGES <= 0 && !getBlockState().getValue(RootBlock.ACTIVATED)) {
            LogManager.getLogger().info("Dieing! " + CHARGES);
            NetherMelt.getCore().Die(this);
            return;
        }

        if (getBlockState().getValue(RootBlock.ACTIVATED) && !activated) {
            ticks +=1;
            if (ticks % 20 == 0) {
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

        if (activated) {
            ticks +=1;
        }

        if (activated && ticks % 20 == 0) {
            if (foams.size() == 0 && getBlockState().getValue(RootBlock.ACTIVATED)) {
                // Die
                NetherMelt.getCore().Die(this);
            }
        }
    }

    public void addFoam(BlockPos pos) {
        if (!foams.contains(pos)) {
            foams.add(pos);
        }
    }

    public void removeFoam(BlockPos pos) {
        foams.remove(pos);
    }

    public int getCharges() {
        return CHARGES;
    }

    public void setCharges(int amount) {
        CHARGES = amount;
    }

    private void activated() {
        activated = true;
    }



    @Override
    public void saveAdditional(CompoundTag Tag) {
        Tag.putBoolean("activated", activated);
        Tag.putInt("charges", CHARGES);
    }

    @Override
    public void load(CompoundTag Tag) {
        if (Tag.contains("activated")) {
            activated = Tag.getBoolean("activated");
        }

        if (Tag.contains("charges")) {
            CHARGES = Tag.getInt("charges");
        }
    }
}

