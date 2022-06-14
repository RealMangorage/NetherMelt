package me.mangorage.nethermelt.util;

import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.blockentitys.RootBlockEntity;
import me.mangorage.nethermelt.setup.Registry;
import net.minecraft.core.BlockPos;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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

        // Startup Logic

        CorrosiveResistantTags.add(ForgeRegistries.BLOCKS.tags().createTagKey(new ResourceLocation("forge", "ores")));

        CorrosiveResistant.add(Blocks.AIR.defaultBlockState());
        CorrosiveResistant.add(Registry.BLOCK_FOAM.get().defaultBlockState());
        CorrosiveResistant.add(Registry.BLOCK_ROOT.get().defaultBlockState());

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
                level.setBlock(pos, Registry.BLOCK_FOAM.get().defaultBlockState(), Block.UPDATE_ALL);

                FoamBlockEntity entity = (FoamBlockEntity) level.getBlockEntity(pos);

                entity.setRoot(root);
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


}
