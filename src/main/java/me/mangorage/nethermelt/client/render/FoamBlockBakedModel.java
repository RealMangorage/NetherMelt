package me.mangorage.nethermelt.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.BakedModelWrapper;
import net.minecraftforge.client.model.QuadTransformer;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FoamBlockBakedModel extends BakedModelWrapper<BakedModel> {

    public static ModelProperty<BlockState> ABSORBING_STATE = new ModelProperty<>();

    public FoamBlockBakedModel(BakedModel originalModel) {
        super(originalModel);
    }


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        List<BakedQuad> result = new ArrayList<>();
        RenderType layer = MinecraftForgeClient.getRenderType();
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();

        BlockState FoamBlock = state;
        BlockState Absorbing = extraData.getData(ABSORBING_STATE);

        if (Absorbing != null) {
            if (layer == RenderType.cutoutMipped()) {
                BakedModel model = dispatcher.getBlockModel(Absorbing);

                result.addAll(model.getQuads(Absorbing, side, rand, extraData));
            }
        }

        if (layer == RenderType.cutout()) {
            BakedModel overlayModel = dispatcher.getBlockModel(state);
            BlockModelRotation rot = BlockModelRotation.by(0, (int) 0);
            QuadTransformer transformer = new QuadTransformer(rot.getRotation().blockCenterToCorner());

            result.addAll(transformer.processMany(overlayModel.getQuads(state, side, rand)));
            return result;
        }




        return result;

    }
}
