package me.mangorage.nethermelt.common.blockentitys;

import me.mangorage.nethermelt.common.core.ITickable;
import me.mangorage.nethermelt.common.blocks.RootBlock;
import me.mangorage.nethermelt.common.core.ModBlockTags;
import me.mangorage.nethermelt.common.core.Registration;
import me.mangorage.nethermelt.common.core.RegistryCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static me.mangorage.nethermelt.common.core.Constants.BlockStateProperties.ACTIVATED;

public class RootBlockEntity extends BlockEntity implements ITickable.Server {
    private int ticks = 1;
    private int CHARGES = -1;
    private Core core;
    private boolean grown = false;

    public RootBlockEntity(BlockPos pWorldPosition, BlockState pBlockState) {
        super(Registration.BLOCKENTITY_ROOT.get(), pWorldPosition, pBlockState);
    }

    @Override
    public void onLoad() {
        if (!(getBlockState().getBlock() instanceof RootBlock)) {
            getLevel().removeBlockEntity(getBlockPos());
            return;
        }

        super.onLoad();
        if (!getLevel().isClientSide) {
            if (core == null)
                core = new Core();

            if (CHARGES == -1)
                CHARGES = ((RootBlock) getBlockState().getBlock()).getConfig().getDefaultCharges();

        }

    }

    public void deactivate() {
        if (CHARGES > 0) {
            getLevel().setBlock(getBlockPos(), getBlockState().setValue(ACTIVATED, false), Block.UPDATE_ALL);
            grown = false;
        } else {
            getLevel().setBlock(getBlockPos(), RegistryCollection.getVariant(getBlock().getVariantID()).BLOCK_DEAD_ROOT.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    @Override
    public void serverTick() {
        ticks++;

        if (ticks % 20 == 0) {
            if (getBlockState().getValue(ACTIVATED)) {
                if (!grown) {
                    BlockPos pos = getBlockPos();
                    AtomicBoolean Spreaded = new AtomicBoolean(false);

                    for (Direction direction : Direction.values()) {
                        boolean result = getCore().Grow(pos.relative(direction, 1));
                        if (result)
                            Spreaded.set(true);
                    }

                    if (!Spreaded.get()) {
                        deactivate();
                    } else {
                        grown = true;
                    }
                    
                } else if (getCore().FOAM.isEmpty()) {
                    deactivate();
                }
            }
        }
    }

    public Core getCore() {
        if (core == null)
            core = new Core();

        return core;
    }

    @Override
    public void saveAdditional(CompoundTag Tag) {
        Tag.putInt("charges", CHARGES);
    }

    @Override
    public void load(CompoundTag Tag) {
        if (Tag.contains("charges"))
            CHARGES = Tag.getInt("charges");
    }

    public void loadFoam(FoamBlockEntity foam) {getCore().add(foam);}
    public int getCharges() {return CHARGES;}
    public void setCharges(int amount) {CHARGES = amount;}

    public void addCharge(int amount, boolean preventExplosion) {
        setCharges(CHARGES + amount);
        if (CHARGES > getBlock().getConfig().getMaxCharges() && !preventExplosion && !level.isClientSide()) {
            getLevel().explode(EntityType.TNT.create(level), getBlockPos().getX(), getBlockPos().relative(Direction.DOWN, 1).getY(), getBlockPos().getZ(), 4.0F, Explosion.BlockInteraction.BREAK);
            getLevel().setBlock(getBlockPos(), RegistryCollection.getVariant(getBlock().getVariantID()).BLOCK_DEAD_ROOT.get().defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    public void addCharge(int amount) {
        addCharge(amount, false);
    }

    public RootBlock getBlock() {
        return (RootBlock) getBlockState().getBlock();
    }

    public class Core  {
        private List<FoamBlockEntity> FOAM = new ArrayList<>();
        private int Range = 0;

        public Core() {
            Block block = getBlockState().getBlock();
            if (block instanceof RootBlock RB) {
                Range = RB.getConfig().getDefaultRange();
            } else {
                getLevel().removeBlockEntity(getBlockPos()); // Block has a BE that is invalid!
            }
        }
        private double getDistanceXZ(BlockPos source, BlockPos foam) {
            double x = source.getX() - foam.getX();
            double z = source.getZ() - foam.getZ();

            return Math.sqrt((x*x) + (z*z));
        }

        private double getDistanceY(BlockPos source, BlockPos foam) {
            double y = source.getY() - foam.getY();

            return Math.sqrt((y*y));
        }

        private boolean isInRange(BlockPos RootPos, BlockPos pos) {
            AtomicBoolean result = new AtomicBoolean(true);

            if (pos.getY() <= 1 || pos.getY() >= 127) {
                return false;
            }

            if (getDistanceXZ(RootPos, pos) >= Range/2) {
                result.set(false);
            }

            if (getDistanceY(RootPos, pos) >= Range/2) {
                result.set(false);
            }

            return result.get();
        }

        private boolean canCorrode(Block block) {
            return ForgeRegistries.BLOCKS.getHolder(block).get().containsTag(ModBlockTags.CAN_CORRODE);
        }

        private boolean canFall(Block block) {
            return ForgeRegistries.BLOCKS.getHolder(block).get().containsTag(ModBlockTags.CAN_FALL);
        }

        public void killAllFoam() {
            List<BlockPos> posList = new ArrayList<>();
            FOAM.forEach(foam -> posList.add(foam.getBlockPos()));
            posList.forEach(pos -> {
                if (getLevel().getBlockEntity(pos) instanceof FoamBlockEntity FBE)
                    FBE.interupted = true;
                getLevel().setBlock(pos, RegistryCollection.getVariant(getBlock().getVariantID()).BLOCK_DEAD_FOAM.get().defaultBlockState(), Block.UPDATE_ALL);
            });

            FOAM.clear();
        }

        public void add(FoamBlockEntity foamBlockEntity) {
            if (!FOAM.contains(foamBlockEntity))
                FOAM.add(foamBlockEntity);
        }

        public void Die(FoamBlockEntity foam) {
            FOAM.remove(foam);
            getLevel().setBlock(foam.getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
            if (FOAM.size() == 0)
                deactivate();
        }

        public boolean Grow(BlockPos pos) {
            if (isInRange(getBlockPos(), pos) && canCorrode(getLevel().getBlockState(pos).getBlock())) {
                BlockState oldState = getLevel().getBlockState(pos);
                BlockState newState = RegistryCollection.getVariant(getBlock().getVariantID()).BLOCK_FOAM.get().defaultBlockState();

                getLevel().setBlock(pos, newState, Block.UPDATE_ALL);

                if (getLevel().getBlockEntity(pos) instanceof FoamBlockEntity FBE) {
                    FBE.setAbsorbing(oldState);
                    FBE.setRoot(getBlockPos());
                    add(FBE);
                }
                return true;
            }
            return false;
        }

        public Integer getFoamCount() {
            return FOAM.size();
        }

        public Integer getRange() {
            return Range;
        }
    }
}

