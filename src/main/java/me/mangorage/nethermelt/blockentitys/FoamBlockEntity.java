package me.mangorage.nethermelt.blockentitys;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blocks.FoamBlock;
import me.mangorage.nethermelt.render.FoamBlockBakedModel;
import me.mangorage.nethermelt.setup.Registry;
import me.mangorage.nethermelt.util.FoamDeathType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Random;

public class FoamBlockEntity extends BlockEntity {


    private float[] chance = new float[]{0.1f, 0f, 0f, 0f, 0.2f, 0.0f, 0f, 0.3f, 0f, 0f, 0f, 0.4f, 0.5f};
    private Random random = new Random();
    private RootBlockEntity root; // NBT
    private BlockPos rootdata; // Used for Loading purposes
    private BlockState Absorbing = Blocks.NETHERRACK.defaultBlockState(); // Default: Netherrack
    private int ticks = 0;

    private Runnable run = null;


    public FoamBlockEntity(BlockPos pos, BlockState state) {
        super(Registry.BLOCKENTITY_FOAM.get(), pos, state);
    }

    public void setRoot(RootBlockEntity root) {
        this.root = root;
    }
    public RootBlockEntity getRoot() { return this.root; }

    public void playSound() {
        if (!getLevel().isClientSide) {
            float volume = 0f;

            if (random.nextInt(100) <= 5) {
                volume = 0.5f;
            }

            ServerLevel level = (ServerLevel) getLevel();
            level.sendParticles(ParticleTypes.LARGE_SMOKE, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10, -0.5, -0.5, -0.5, 0.1F);
            getLevel().playSound(null, getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), SoundEvents.LAVA_EXTINGUISH, SoundSource.BLOCKS, volume, 0F);

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


    public void setAbsorbing(BlockState State) {
        if (State.getBlock() instanceof FoamBlock)
            return;

        Absorbing = State;
        markUpdated();
    }

    public BlockState getAbsorbing() {
        return Absorbing;
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

    public void tick() {
        if (getLevel().isClientSide) {
            return;
        }

        if (rootdata != null) {
            if (getLevel().getBlockEntity(rootdata) instanceof RootBlockEntity) {
                root = (RootBlockEntity) getLevel().getBlockEntity(rootdata);
                root.addFoam(getBlockPos());
            }
            rootdata = null; // clean up!
        }

        if (root == null)
            return;
        if (!(getLevel().getBlockEntity(root.getBlockPos()) instanceof RootBlockEntity)) {
            root = null;
            NetherMelt.getCore().Die(this, FoamDeathType.INTERUPTED);
            return;
        }



        if (getBlockState().getValue(FoamBlock.STAGE) < FoamBlock.MAX_STAGES) {
            ticks++;
            if (ticks % 20 == 0) {
                ticks = 0;

                getLevel().setBlock(getBlockPos(), getBlockState().cycle(FoamBlock.STAGE), Block.UPDATE_ALL);
            }
        }

        if (getBlockState().getValue(FoamBlock.STAGE) >= FoamBlock.MAX_STAGES && NetherMelt.getCore() != null) {
            ticks++;
            if (ticks % 20 == 0) {

                if (run != null) {
                    run.run();
                    run = null;
                }

                ArrayList<BlockPos> growto = new ArrayList<>();

                growto.add(getBlockPos().above(1));
                growto.add(getBlockPos().below(1));
                growto.add(getBlockPos().north(1));
                growto.add(getBlockPos().south(1));
                growto.add(getBlockPos().east(1));
                growto.add(getBlockPos().west(1));

                growto.forEach(blockPos -> {
                    NetherMelt.getCore().Grow(root, (ServerLevel) getLevel(), blockPos, getLevel().getBlockState(blockPos));
                });

                NetherMelt.getCore().Die((FoamBlockEntity) getLevel().getBlockEntity(getBlockPos()), FoamDeathType.DEFAULT);

                /**
                run = new Runnable() {
                    @Override
                    public void run() {
                        NetherMelt.getCore().Die((FoamBlockEntity) getLevel().getBlockEntity(getBlockPos()), FoamDeathType.DEFAULT);
                    }
                };
                 **/

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

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }



    @Nonnull
    @Override
    public IModelData getModelData() {
        ModelDataMap.Builder builder = new ModelDataMap.Builder();

        if (Absorbing != null) {
            builder.withInitial(FoamBlockBakedModel.ABSORBING_STATE, Absorbing);
        }

        return builder.build();
    }



    @Override
    protected void saveAdditional(CompoundTag Tag) {
        if (root != null)
            Tag.put("Source", NbtUtils.writeBlockPos(root.getBlockPos()));
        if (Absorbing != null)
            Tag.put("Absorbing", NbtUtils.writeBlockState(Absorbing));
    }

    @Override
    public void load(CompoundTag Tag) {
        if (Tag.contains("Source"))
            rootdata = NbtUtils.readBlockPos(Tag.getCompound("Source"));
        if (Tag.contains("Absorbing"))
            Absorbing = NbtUtils.readBlockState(Tag.getCompound("Absorbing"));
    }
}
