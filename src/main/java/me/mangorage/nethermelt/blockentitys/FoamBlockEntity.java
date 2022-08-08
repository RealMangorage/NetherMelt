package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.core.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Random;

import static me.mangorage.nethermelt.core.Constants.BlockStateProperties.STAGE;
import static me.mangorage.nethermelt.core.Constants.MAX_STAGES;

public class FoamBlockEntity extends BlockEntity {
    private float[] chance = new float[]{0.1f, 0f, 0f, 0f, 0.2f, 0.0f, 0f, 0.3f, 0f, 0f, 0f, 0.4f, 0.5f};
    private Random random = new Random();
    private BlockPos root; // NBT
    private BlockState Absorbing = Blocks.NETHERRACK.defaultBlockState();
    public boolean interupted = false;
    private int ticks = 0;
    public FoamBlockEntity(BlockPos pos, BlockState state) {
        super(Registration.BLOCKENTITY_FOAM.get(), pos, state);
    }

    @Override
    public void onLoad() {
        if (getLevel().isClientSide) return;

        if (root != null) {
            if (getLevel().getBlockEntity(root) instanceof RootBlockEntity RBE)
                RBE.loadFoam(this);
        }

        super.onLoad();
    }

    public void setRoot(BlockPos root) {this.root = root;}
    public BlockState getAbsorbing() {
        return Absorbing;
    }

    public RootBlockEntity getRoot() {
        if (root == null)
            return null;

        BlockEntity entity = getLevel().getBlockEntity(root);

        if (entity instanceof RootBlockEntity RBE) return RBE;

        return null;
    }

    private void markUpdated() {
        this.setChanged();

        if (level != null) {
            level.sendBlockUpdated(
                    getBlockPos(),
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );

            level.updateNeighborsAt(getBlockPos(), getBlockState().getBlock());
            getBlockState().updateNeighbourShapes(level, worldPosition, 2);
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }


    public void setAbsorbing(BlockState State) {
        if (State.getBlock() instanceof FoamBlock)
            return;

        Absorbing = State;
        markUpdated();
    }

    private void updateClient() {
        if (level != null && level.isClientSide) {
            level.sendBlockUpdated(
                    getBlockPos(),
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
    }

    public void serverTick() {
        if (getRoot() == null || getRoot().getCore() == null) {
            return;
        }
        ticks++;

        if (ticks % 20 == 0) {
            if (getBlockState().getValue(STAGE) != MAX_STAGES) {
                getLevel().setBlock(getBlockPos(), getBlockState().cycle(STAGE), Block.UPDATE_ALL);
            } else {
                ArrayList<BlockPos> growto = new ArrayList<>();

                growto.add(getBlockPos().above(1));
                growto.add(getBlockPos().below(1));
                growto.add(getBlockPos().north(1));
                growto.add(getBlockPos().south(1));
                growto.add(getBlockPos().east(1));
                growto.add(getBlockPos().west(1));

                growto.forEach(blockPos -> {
                    getRoot().getCore().Grow(blockPos);
                });

                getRoot().getCore().Die(this);
            }
        }
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        updateClient();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        if (Absorbing != null) {
            tag.put("Absorbing", NbtUtils.writeBlockState(Absorbing));
        }

        return tag;
    }


    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    @Override
    protected void saveAdditional(CompoundTag Tag) {
        if (root != null)
            Tag.put("Source", NbtUtils.writeBlockPos(root));
        if (Absorbing != null)
            Tag.put("Absorbing", NbtUtils.writeBlockState(Absorbing));
    }

    @Override
    public void load(CompoundTag Tag) {
        if (Tag.contains("Source"))
            root = NbtUtils.readBlockPos(Tag.getCompound("Source"));
        if (Tag.contains("Absorbing"))
            Absorbing = NbtUtils.readBlockState(Tag.getCompound("Absorbing"));
    }
}
