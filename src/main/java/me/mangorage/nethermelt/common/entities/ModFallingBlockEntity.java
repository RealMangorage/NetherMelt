package me.mangorage.nethermelt.common.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ModFallingBlockEntity extends Entity {

    private BlockState blockState; //nbt
    private CompoundTag BlockEntityData; //nbt
    public int time; // nbt
    protected static final EntityDataAccessor<BlockPos> DATA_START_POS = SynchedEntityData.defineId(ModFallingBlockEntity.class, EntityDataSerializers.BLOCK_POS);

    public ModFallingBlockEntity(EntityType<?> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private ModFallingBlockEntity(Level pLevel, double pX, double pY, double pZ, BlockState pState, @Nullable CompoundTag Blockdata) {
        this(EntityType.FALLING_BLOCK, pLevel);
        this.blockState = pState;
        this.BlockEntityData = Blockdata;
        this.blocksBuilding = true;
        this.setPos(pX, pY, pZ);
        this.setDeltaMovement(Vec3.ZERO);
        this.xo = pX;
        this.yo = pY;
        this.zo = pZ;
        this.setStartPos(this.blockPosition());
    }

    public static ModFallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
        CompoundTag tag = level.getBlockEntity(pos) != null ? level.getBlockEntity(pos).saveWithFullMetadata() : null;

        ModFallingBlockEntity fallingblockentity = new ModFallingBlockEntity(level, (double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, state.hasProperty(BlockStateProperties.WATERLOGGED) ? state.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(false)) : state, tag);
        level.setBlock(pos, state.getFluidState().createLegacyBlock(), 3);
        level.addFreshEntity(fallingblockentity);
        return fallingblockentity;
    }



    public BlockState getBlockState() {
        return blockState;
    }

    public void setStartPos(BlockPos pStartPos) {
        this.entityData.set(DATA_START_POS, pStartPos);
    }

    public BlockPos getStartPos() {
        return this.entityData.get(DATA_START_POS);
    }



    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.NONE;
    }

    @Override
    public void tick() {

        ++this.time;
        if (!this.isNoGravity()) {
            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));
        }

        this.move(MoverType.SELF, this.getDeltaMovement());

        if (!level.isClientSide) {
            if (this.onGround) {
                // land
                this.discard();

                level.setBlock(this.blockPosition(), this.getBlockState(), Block.UPDATE_ALL);
                if (level.getBlockEntity(this.blockPosition()) != null && this.BlockEntityData != null)
                    level.getBlockEntity(this.blockPosition()).load(this.BlockEntityData);

            } else if (!level.getBlockState(this.blockPosition()).getMaterial().isSolid() && !level.getBlockState(this.blockPosition()).isAir()) {
                level.destroyBlock(this.blockPosition(), true);
            }
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_START_POS, BlockPos.ZERO);
    }


    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {
        pCompound.put("BlockState", NbtUtils.writeBlockState(this.blockState));
        pCompound.putInt("Time", this.time);
        if (this.BlockEntityData != null) {
            pCompound.put("TileEntityData", this.BlockEntityData);
        }

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {
        this.blockState = NbtUtils.readBlockState(pCompound.getCompound("BlockState"));
        this.time = pCompound.getInt("Time");

        if (pCompound.contains("TileEntityData", 10)) {
            this.BlockEntityData = pCompound.getCompound("TileEntityData");
        }
    }


    @Override
    public Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this, Block.getId(this.blockState));
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        this.blockState = Block.stateById(pPacket.getData());
        this.blocksBuilding = true;
        double d0 = pPacket.getX();
        double d1 = pPacket.getY();
        double d2 = pPacket.getZ();
        this.setPos(d0, d1, d2);
        this.setStartPos(this.blockPosition());
    }
}
