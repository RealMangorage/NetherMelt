package me.mangorage.nethermelt.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingBlockBakedModel extends BakedModelWrapper<BakedModel> {
    public static ModelProperty<BlockState> FALLING_STATE = new ModelProperty<>();


    public FallingBlockBakedModel(BakedModel originalModel) {
        super(originalModel);
    }

    @Nonnull
    @Override
    @SuppressWarnings("deprecation")
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> result = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderType();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        BlockState FALLING = extraData.getData(FALLING_STATE);
        if (FALLING != null) {
            if (layer == RenderType.cutoutMipped()) {
                BakedModel model = dispatcher.getBlockModel(FALLING);
                result.addAll(model.getQuads(FALLING, side, rand));
            }
            return result;
        }

        if (layer == RenderType.solid() || layer == null)
            result.addAll(originalModel.getQuads(state, side, rand, extraData));

        return result;

    }
}
