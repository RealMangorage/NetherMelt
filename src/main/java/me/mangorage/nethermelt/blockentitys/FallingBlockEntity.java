package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.blocks.FallingBlock;
import me.mangorage.nethermelt.render.FallingBlockBakedModel;
import me.mangorage.nethermelt.render.FoamBlockBakedModel;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public class FallingBlockEntity extends BlockEntity {
    private BlockState FALLING_STATE;
    private boolean loading = false;
    private int ticks = 0;

    public FallingBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registry.BLOCKENTITY_FALLING.get(), pWorldPosition, pBlockState);
    }

    public void setState(BlockState state) {
        FALLING_STATE = state;
        markUpdated();
    }

    private void updateClient() {
        if (level != null && level.isClientSide) {
            requestModelDataUpdate();
            level.sendBlockUpdated(
                    getBlockPos(),
                    getBlockState(),
                    getBlockState(),
                    Block.UPDATE_ALL
            );
            level.getChunkSource().getLightEngine().checkBlock(getBlockPos());
        }
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

    private void setBlock() {
        getLevel().setBlock(getBlockPos(), FALLING_STATE, Block.UPDATE_ALL);
    }

    public void tick() {
        if (getLevel().isClientSide)
            return;
        if (FALLING_STATE == null)
            return;

        ticks++;

        if (ticks % 20 == 0) {
            BlockPos pos = getBlockPos().below(1);
            Block block = getLevel().getBlockState(pos).getBlock();
            if (block instanceof AirBlock) {
                // Move Down
                getLevel().setBlock(pos, getBlockState(), Block.UPDATE_ALL);

                if (getLevel().getBlockEntity(pos) instanceof FallingBlockEntity BE) {
                    BE.setState(FALLING_STATE);
                }

                getLevel().setBlock(getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            } else if (!(block instanceof FallingBlock)) {
                setBlock();
            }
        }

    }

    @NotNull
    @Override
    public IModelData getModelData() {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();

        if (FALLING_STATE != null) {
            builder.withInitial(FallingBlockBakedModel.FALLING_STATE, FALLING_STATE);
            LogManager.getLogger().info("Woop Woop! Falling State!! " + FALLING_STATE.toString());
        }

        return builder.build();
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        handleUpdateTag(pkt.getTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        super.handleUpdateTag(tag);
        updateClient();
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();

        if (FALLING_STATE != null) {
            tag.put("FALLING_STATE", NbtUtils.writeBlockState(FALLING_STATE));
        }

        return tag;
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        if (FALLING_STATE != null) {
            tag.put("FALLING_STATE", NbtUtils.writeBlockState(FALLING_STATE));
        }
    }

    @Override
    public void load(CompoundTag tag) {
        if (tag.contains("FALLING_STATE")) {
            FALLING_STATE = NbtUtils.readBlockState(tag.getCompound("FALLING_STATE"));
        }

        loading = true;
    }
}
