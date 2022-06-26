package me.mangorage.nethermelt.util;

import me.mangorage.nethermelt.NetherMelt;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.blocks.RootBlock;
import me.mangorage.nethermelt.config.NetherMeltConfig;
import me.mangorage.nethermelt.entities.ModFallingBlockEntity;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

public class Core {


    /**
     TODO Change Type to Block.class for CorrosiveResistant and FallingBlacklist
     */

    private Logger logger = LogManager.getLogger(NetherMelt.MOD_ID + "/" + this.getClass());
    private boolean loaded = false;
    private final ArrayList<BlockState> CorrosiveResistant = new ArrayList<>(); // Blocks that are Resistant to the Corrosive nature of NetherMelt's Foam
    private final ArrayList<BlockState> FallingBlacklist = new ArrayList<>(); // Blocks that cant be affected by Gravity!
    private int Range = 28;


    public boolean isLoaded() {
        return loaded;
    }

    public void unLoad() {
        loaded = false;

        CorrosiveResistant.clear();
        FallingBlacklist.clear();
    }
    public void init() {

        // Startup Logic & Default Hard Coded Settings!

        NetherMeltConfig.RESISTANT.get().forEach(property -> {
            loadConfig(ConfigType.RESISTANT, property);
        });

        NetherMeltConfig.FALLING_BLOCKS.get().forEach(property -> {
            loadConfig(ConfigType.FALLING, property);
        });

        Range = NetherMeltConfig.RANGE.get();

        loadConfig(ConfigType.RESISTANT, Registry.BLOCK_FOAM.get().defaultBlockState());
        loadConfig(ConfigType.RESISTANT, Registry.BLOCK_ROOT.get().defaultBlockState());
        loadConfig(ConfigType.RESISTANT, Blocks.CHEST.defaultBlockState());

        loadConfig(ConfigType.FALLING, Registry.BLOCK_ROOT.get().defaultBlockState().setValue(RootBlock.ACTIVATED, true));
    }

    private void loadConfig(ConfigType type, String property) {
        String[] split = property.split(":");

        if (split.length == 3) {
            String Type = split[0];
            String namespace = split[1];
            String path = split[2];
            ResourceLocation rl = new ResourceLocation(namespace, path);

            if (Type.equals("block")) {
                if (ForgeRegistries.BLOCKS.containsKey(rl))
                    loadConfig(type, ForgeRegistries.BLOCKS.getValue(rl).defaultBlockState());
            }

            if (Type.equals("tag")) {
                var tagKey = ForgeRegistries.BLOCKS.tags().createTagKey(rl);

                var known = ForgeRegistries.BLOCKS.tags().isKnownTagName(tagKey);

                if (known) {
                    ForgeRegistries.BLOCKS.tags().getTag(tagKey).forEach(Block -> {
                        loadConfig(type, Block.defaultBlockState());
                    });
                }


            }

        }

    }

    private void loadConfig(ConfigType type, BlockState state) {
        logger.info("Loading Block: Config Type: " + type.name() + " Block: " + state.getBlock().getRegistryName());

        switch (type) {
            case RESISTANT -> {
                if (!CorrosiveResistant.contains(state))
                    CorrosiveResistant.add(state);
            }
            case FALLING -> {
                if (!FallingBlacklist.contains(state))
                    FallingBlacklist.add(state);
            }
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

    private boolean canCorrode(BlockState state) {
        return !CorrosiveResistant.contains(state.getBlock().defaultBlockState());
    }

    private boolean canFall(BlockState state) {
        return !FallingBlacklist.contains(state);
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
        Level level = root.getLevel();
        BlockPos rootPos = root.getBlockPos();
        Stream<BlockPos> posStream = BlockPos.betweenClosedStream(new BlockPos(rootPos.getX() - Range, rootPos.getY() - Range, rootPos.getZ() - Range), new BlockPos(rootPos.getX() + Range, rootPos.getY() + Range, rootPos.getZ() + Range));

        posStream.forEach(blockPos -> {
            if (isInRange(rootPos, blockPos)) {
                BlockState state = level.getBlockState(blockPos);

                if (canFall(state)) {
                    ModFallingBlockEntity.fall(level, blockPos, state);
                }
            }
        });

        int Charges = root.getCharges();
        if (Charges <= 0) {
            level.setBlock(rootPos, Registry.BLOCK_DEAD_ROOT.get().defaultBlockState(), Block.UPDATE_ALL);
        } else {
            level.setBlock(rootPos, root.getBlockState().setValue(RootBlock.ACTIVATED, false), Block.UPDATE_ALL); // Turn off the root! :D
            if (level.getBlockEntity(rootPos) instanceof  RootBlockEntity BE)
                BE.setCharges(Charges);
        }

        ModFallingBlockEntity.fall(level, rootPos, level.getBlockState(rootPos));
    }

    public enum FoamDeathType {
        DEFAULT(),
        INTERUPTED();
    }

    public enum ConfigType {
        RESISTANT(),
        FALLING();
    }
}
