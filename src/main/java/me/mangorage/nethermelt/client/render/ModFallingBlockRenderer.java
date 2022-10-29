package me.mangorage.nethermelt.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mangorage.nethermelt.common.entities.ModFallingBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

public class ModFallingBlockRenderer extends EntityRenderer<ModFallingBlockEntity> {
    public static class Provider implements EntityRendererProvider {
        @Override
        public EntityRenderer create(Context pContext) {
            return new ModFallingBlockRenderer(pContext);
        }
    }

    public ModFallingBlockRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public boolean shouldRender(ModFallingBlockEntity pLivingEntity, Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public void render(ModFallingBlockEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        BlockState blockstate = pEntity.getBlockState();
        if (blockstate.getRenderShape() == RenderShape.MODEL) {
            Level level = pEntity.getLevel();
            if (blockstate != level.getBlockState(pEntity.blockPosition()) && blockstate.getRenderShape() != RenderShape.INVISIBLE) {
                pMatrixStack.pushPose();
                BlockPos blockpos = new BlockPos(pEntity.getX(), pEntity.getBoundingBox().maxY, pEntity.getZ());
                pMatrixStack.translate(-0.5D, 0.0D, -0.5D);
                BlockRenderDispatcher blockrenderdispatcher = Minecraft.getInstance().getBlockRenderer();
                for (net.minecraft.client.renderer.RenderType type : net.minecraft.client.renderer.RenderType.chunkBufferLayers()) {
                    if (ItemBlockRenderTypes.canRenderInLayer(blockstate, type)) {
                        net.minecraftforge.client.ForgeHooksClient.setRenderType(type);
                        blockrenderdispatcher.getModelRenderer().tesselateBlock(level, blockrenderdispatcher.getBlockModel(blockstate), blockstate, blockpos, pMatrixStack, pBuffer.getBuffer(type), false, new Random(), blockstate.getSeed(pEntity.getStartPos()), OverlayTexture.NO_OVERLAY);
                    }
                }
                net.minecraftforge.client.ForgeHooksClient.setRenderType(null);
                pMatrixStack.popPose();
                super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
            }
        }
    }

    @Override
    protected boolean shouldShowName(ModFallingBlockEntity pEntity) {
        return false;
    }



    @Override
    public ResourceLocation getTextureLocation(ModFallingBlockEntity pEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
