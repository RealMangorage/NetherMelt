package me.mangorage.nethermelt.util;

import me.mangorage.nethermelt.blockentitys.FallingBlockEntity;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.FallingBlock;
import me.mangorage.nethermelt.config.NetherMeltConfig;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Core {

    private final LockableBoolean Init = new LockableBoolean(false);

    private final ArrayList<BlockState> CorrosiveResistant = new ArrayList<>(); // Blocks that are Resistant to the Corrosive nature of NetherMelt's Foam
    private final ArrayList<TagKey<Block>> CorrosiveResistantTags = new ArrayList<>();
    private final int Range = 28;

    public void init() {
        if (Init.isLocked())
            new IllegalStateException("Core has been Initilized already! Cant re init");
        Init.set(true);
        Init.lock();

        // Startup Logic & Default Hard Coded Settings!
        CorrosiveResistant.add(Registry.BLOCK_FOAM.get().defaultBlockState());
        CorrosiveResistant.add(Registry.BLOCK_ROOT.get().defaultBlockState());
        CorrosiveResistant.add(Registry.BLOCK_FALLING.get().defaultBlockState());

        NetherMeltConfig.RESISTANT.get().forEach(property -> {
            String[] split = property.split(":");

            if (split.length == 3) {
                String Type = split[0];
                String namespace = split[1];
                String path = split[2];;
                ResourceLocation rl = new ResourceLocation(namespace, path);

                if (Type.equals("block")) {
                    if (ForgeRegistries.BLOCKS.containsKey(rl)) {
                        CorrosiveResistant.add(ForgeRegistries.BLOCKS.getValue(rl).defaultBlockState());
                    }
                } else if (Type.equals("tag")) {
                    CorrosiveResistantTags.add(ForgeRegistries.BLOCKS.tags().createTagKey(rl));
                }

            }

        });

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

    private boolean canCorrode(BlockState state) {
        AtomicBoolean result = new AtomicBoolean(true);
        Block block = state.getBlock();

        CorrosiveResistantTags.forEach(Tag -> {
            if (ForgeRegistries.BLOCKS.tags().getTag(Tag).contains(state.getBlock())) {
                result.set(false);
            }
        });

        if (CorrosiveResistant.contains(block.defaultBlockState())) {
            result.set(false);
        }

        return result.get();
    }

    public void Grow(RootBlockEntity root, ServerLevel level, BlockPos pos, BlockState state) {
        if (canCorrode(state)) {
            if (isInRange(root.getBlockPos(), pos)) {
                BlockState oldState = level.getBlockState(pos);
                level.setBlock(pos, Registry.BLOCK_FOAM.get().defaultBlockState(), Block.UPDATE_ALL);

                FoamBlockEntity entity = (FoamBlockEntity) level.getBlockEntity(pos);

                entity.setRoot(root);
                entity.setAbsorbing(oldState);
                root.addFoam(pos);
                return;
            }
        }

        if (!isInRange(root.getBlockPos(), pos)) {
            // Walls && Ceiling, not Floor! Maybe not floor
            level.setBlock(pos, Blocks.NETHERRACK.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    public void Die(FoamBlockEntity foam, FoamDeathType DeathType) {
        if (DeathType == FoamDeathType.INTERUPTED) {
            foam.getLevel().setBlock(foam.getBlockPos(), Registry.BLOCK_DEAD_FOAM.get().defaultBlockState(), Block.UPDATE_ALL);
        } else {
            foam.getRoot().removeFoam(foam.getBlockPos());
            foam.getLevel().setBlock(foam.getBlockPos(), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
        }
    }

    public void Die(RootBlockEntity root) {
        // Die root

        Level level = root.getLevel();
        BlockPos rootPos = root.getBlockPos();
        Stream<BlockPos> posStream = BlockPos.betweenClosedStream(new BlockPos(rootPos.getX() - Range, rootPos.getY() - Range, rootPos.getZ() - Range), new BlockPos(rootPos.getX() + Range, rootPos.getY() + Range, rootPos.getZ() + Range));

        posStream.forEach(blockPos -> {

            if (isInRange(rootPos, blockPos)) {
                if (!(level.getBlockState(blockPos).getBlock() instanceof AirBlock)) {
                    BlockState state = level.getBlockState(blockPos);

                    if (state == Registry.BLOCK_FALLING.get().defaultBlockState())
                        return;
                    if (canCorrode(state))
                        return;

                    level.setBlock(blockPos, Registry.BLOCK_FALLING.get().defaultBlockState(), Block.UPDATE_ALL);

                    if (level.getBlockEntity(blockPos) instanceof FallingBlockEntity BE) {
                        BE.setState(state);
                    }
                }
            }

        });


        level.setBlock(rootPos, Registry.BLOCK_FALLING.get().defaultBlockState(), Block.UPDATE_ALL);

        if (level.getBlockEntity(rootPos) instanceof FallingBlockEntity BE) {
            BE.setState(Registry.BLOCK_DEAD_ROOT.get().defaultBlockState());
        }

    }


}
