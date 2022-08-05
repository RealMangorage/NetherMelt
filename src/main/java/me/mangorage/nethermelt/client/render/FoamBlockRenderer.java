package me.mangorage.nethermelt.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import me.mangorage.nethermelt.blockentitys.FoamBlockEntity;
import me.mangorage.nethermelt.core.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;

import java.awt.*;

public class FoamBlockRenderer implements BlockEntityRenderer<FoamBlockEntity> {
    private static BooleanProperty VISIBLE = Constants.BlockStateProperties.VISIBLE;

    public static class Provider implements BlockEntityRendererProvider {
        @Override
        public BlockEntityRenderer create(Context pContext) {
            return new FoamBlockRenderer(pContext);
        }
    }

    private final BlockEntityRendererProvider.Context context;
    public FoamBlockRenderer(BlockEntityRendererProvider.Context context) {
        this.context = context;
    }



    @Override
    public void render(FoamBlockEntity FBE, float partialtick, PoseStack stack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        final BlockRenderDispatcher dispatcher = this.context.getBlockRenderDispatcher();
        var state = FBE.getAbsorbing();

        if (state.getFluidState() != Fluids.EMPTY.defaultFluidState()) {
            FluidState fluidState = state.getFluidState();
            FluidStack fluidStack = new FluidStack(fluidState.getType(), fluidState.getAmount());
            stack.pushPose();
            //stack.translate(0.01f, 0.01f, 0.01f);
            stack.scale(1f, 1f, 1f);
            renderCubeUsingQuads(fluidStack, partialtick, stack, buffer, packedLight, packedOverlay);
            stack.popPose();
        } else {
            stack.pushPose();
            //stack.translate(0.01f, 0.01f, 0.01f);
            stack.scale(1f, 1f, 1f);
            dispatcher.renderSingleBlock(state, stack, buffer, packedLight, packedOverlay, EmptyModelData.INSTANCE);
            stack.popPose();
        }

        stack.pushPose();
        stack.scale(1.02f,1.02f,1.02f);
        stack.translate(-0.01f, -0.01f, -0.01f);
        dispatcher.renderSingleBlock(FBE.getBlockState().setValue(VISIBLE, true), stack, buffer, packedLight, packedOverlay, EmptyModelData.INSTANCE);
        stack.popPose();
    }


    public static void renderCubeUsingQuads(FluidStack fluid, float partialTicks, PoseStack matrixStack, MultiBufferSource renderBuffers, int combinedLight, int combinedOverlay) {
        // draw the object as a cube, using quads
        // When render method is called, the origin [0,0,0] is at the current [x,y,z] of the block.

        // The cube-drawing method draws the cube in the region from [0,0,0] to [1,1,1] but we want it
        //   to be in the block one above this, i.e. from [0,1,0] to [1,2,1],
        //   so we need to translate up by one block, i.e. by [0,1,0]
        final Vector3d TRANSLATION_OFFSET = new Vector3d(.5, 0, .5);

        matrixStack.pushPose(); // push the current transformation matrix + normals matrix
        matrixStack.translate(TRANSLATION_OFFSET.x,TRANSLATION_OFFSET.y,TRANSLATION_OFFSET.z); // translate
        Color gemColour = Color.white;

        drawCubeQuads(fluid, matrixStack, renderBuffers, gemColour, combinedLight);
        matrixStack.popPose(); // restore the original transformation matrix + normals matrix
    }

    /**
     * Draw a cube from [0,0,0] to [1,1,1], same texture on all sides, using a supplied texture
     */
    private static void drawCubeQuads(FluidStack Fluid, PoseStack matrixStack, MultiBufferSource renderBuffer, Color color, int combinedLight) {
        FluidStack fluid = Fluid;

        Fluid renderFluid = fluid.getFluid();

        FluidAttributes attributes = renderFluid.getAttributes();
        ResourceLocation fluidStill = attributes.getStillTexture(fluid);
        if(fluidStill != null) {
            TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
            ResourceLocation spriteLocation = sprite.getName();

            int color1 = renderFluid.getAttributes().getColor();

            float a = 1.0F;
            float r = (color1>> 16 & 0xFF) / 255.0F;
            float g = (color1>> 8 & 0xFF) / 255.0F;
            float b = (color1& 0xFF) / 255.0F;

            color = new Color(r, g, b, a);

            VertexConsumer vertexBuilderBlockQuads = renderBuffer.getBuffer(RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS));
            // other typical RenderTypes used by TER are:
            // getEntityCutout, getBeaconBeam (which has translucency),

            Matrix4f matrixPos = matrixStack.last().pose();     // retrieves the current transformation matrix
            Matrix3f matrixNormal = matrixStack.last().normal();  // retrieves the current transformation matrix for the normal vector

            // we use the whole texture
            Vec2 bottomLeftUV = new Vec2(sprite.getU0(), sprite.getV0());
            float UVwidth = sprite.getU1() - sprite.getU0();
            float UVheight = sprite.getV1() - sprite.getV0();

            // all faces have the same height and width
            final float WIDTH = 1.0F;
            final float HEIGHT = 1F;

            float scale = (HEIGHT) * fluid.getAmount() / 8;

            matrixStack.scale(1f, Math.abs(scale), 1f);
            //if(scale <= 0) { matrixStack.scale(.5f, Math.abs(scale) + .21f, .5f); }
            matrixStack.translate(-.5, 0.0, -.5);

            final Vector3d EAST_FACE_MIDPOINT = new Vector3d(1.0, 0.5, 0.5);
            final Vector3d WEST_FACE_MIDPOINT = new Vector3d(0.0, 0.5, 0.5);
            final Vector3d NORTH_FACE_MIDPOINT = new Vector3d(0.5, 0.5, 0.0);
            final Vector3d SOUTH_FACE_MIDPOINT = new Vector3d(0.5, 0.5, 1.0);
            final Vector3d UP_FACE_MIDPOINT = new Vector3d(0.5, 1.0, 0.5);
            final Vector3d DOWN_FACE_MIDPOINT = new Vector3d(0.5, 0.0, 0.5);

            addFace(Direction.EAST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, EAST_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
            addFace(Direction.WEST, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, WEST_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
            addFace(Direction.NORTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, NORTH_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
            addFace(Direction.SOUTH, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, SOUTH_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
            addFace(Direction.UP, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, UP_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
            addFace(Direction.DOWN, matrixPos, matrixNormal, vertexBuilderBlockQuads,
                    color, DOWN_FACE_MIDPOINT, WIDTH, HEIGHT, bottomLeftUV, UVwidth, UVheight, combinedLight);
        }
    }

    private static void addFace(Direction whichFace, Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer renderBuffer, Color color, Vector3d centrePos, float width, float height, Vec2 bottomLeftUV, float texUwidth, float texVheight, int lightmapValue) {
        // the Direction class has a bunch of methods which can help you rotate quads
        //  I've written the calculations out long hand, and based them on a centre position, to make it clearer what
        //   is going on.
        // Beware that the Direction class is based on which direction the face is pointing, which is opposite to
        //   the direction that the viewer is facing when looking at the face.
        // Eg when drawing the NORTH face, the face points north, but when we're looking at the face, we are facing south,
        //   so that the bottom left corner is the eastern-most, not the western-most!


        // calculate the bottom left, bottom right, top right, top left vertices from the VIEWER's point of view (not the
        //  face's point of view)

        Vector3f leftToRightDirection, bottomToTopDirection;

        switch (whichFace) {
            case NORTH: { // bottom left is east
                leftToRightDirection = new Vector3f(-1, 0, 0);  // or alternatively Vector3f.XN
                bottomToTopDirection = new Vector3f(0, 1, 0);  // or alternatively Vector3f.YP
                break;
            }
            case SOUTH: {  // bottom left is west
                leftToRightDirection = new Vector3f(1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case EAST: {  // bottom left is south
                leftToRightDirection = new Vector3f(0, 0, -1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case WEST: { // bottom left is north
                leftToRightDirection = new Vector3f(0, 0, 1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
            case UP: { // bottom left is southwest by minecraft block convention
                leftToRightDirection = new Vector3f(-1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 0, 1);
                break;
            }
            case DOWN: { // bottom left is northwest by minecraft block convention
                leftToRightDirection = new Vector3f(1, 0, 0);
                bottomToTopDirection = new Vector3f(0, 0, 1);
                break;
            }
            default: {  // should never get here, but just in case;
                leftToRightDirection = new Vector3f(0, 0, 1);
                bottomToTopDirection = new Vector3f(0, 1, 0);
                break;
            }
        }
        leftToRightDirection.mul(0.5F * width);  // convert to half width
        bottomToTopDirection.mul(0.5F * height);  // convert to half height

        // calculate the four vertices based on the centre of the face

        Vector3f bottomLeftPos = new Vector3f((float)centrePos.x, (float)centrePos.y, (float)centrePos.z);
        bottomLeftPos.sub(leftToRightDirection);
        bottomLeftPos.sub(bottomToTopDirection);

        Vector3f bottomRightPos = new Vector3f((float)centrePos.x, (float)centrePos.y, (float)centrePos.z);
        bottomRightPos.add(leftToRightDirection);
        bottomRightPos.sub(bottomToTopDirection);

        Vector3f topRightPos = new Vector3f((float)centrePos.x, (float)centrePos.y, (float)centrePos.z);
        topRightPos.add(leftToRightDirection);
        topRightPos.add(bottomToTopDirection);

        Vector3f topLeftPos = new Vector3f((float)centrePos.x, (float)centrePos.y, (float)centrePos.z);
        topLeftPos.sub(leftToRightDirection);
        topLeftPos.add(bottomToTopDirection);

        // texture coordinates are "upside down" relative to the face
        // eg bottom left = [U min, V max]
        Vec2 bottomLeftUVpos = new Vec2(bottomLeftUV.x, bottomLeftUV.y);
        Vec2 bottomRightUVpos = new Vec2(bottomLeftUV.x + texUwidth, bottomLeftUV.y);
        Vec2 topLeftUVpos = new Vec2(bottomLeftUV.x + texUwidth, bottomLeftUV.y + texVheight);
        Vec2 topRightUVpos = new Vec2(bottomLeftUV.x, bottomLeftUV.y + texVheight);

        Vector3f normalVector = whichFace.step();  // gives us the normal to the face

        addQuad(matrixPos, matrixNormal, renderBuffer,
                bottomLeftPos, bottomRightPos, topRightPos, topLeftPos,
                bottomLeftUVpos, bottomRightUVpos, topLeftUVpos, topRightUVpos,
                normalVector, color, lightmapValue);
    }

    /**
     * Add a quad.
     * The vertices are added in anti-clockwise order from the VIEWER's  point of view, i.e.
     * bottom left; bottom right, top right, top left
     * If you add the vertices in clockwise order, the quad will face in the opposite direction; i.e. the viewer will be
     *   looking at the back face, which is usually culled (not visible)
     * See
     * http://greyminecraftcoder.blogspot.com/2014/12/the-tessellator-and-worldrenderer-18.html
     * http://greyminecraftcoder.blogspot.com/2014/12/block-models-texturing-quads-faces.html
     */
    private static void addQuad(Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer renderBuffer, Vector3f blpos, Vector3f brpos, Vector3f trpos, Vector3f tlpos, Vec2 blUVpos, Vec2 brUVpos, Vec2 trUVpos, Vec2 tlUVpos, Vector3f normalVector, Color color, int lightmapValue) {
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, blpos, blUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, brpos, brUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, trpos, trUVpos, normalVector, color, lightmapValue);
        addQuadVertex(matrixPos, matrixNormal, renderBuffer, tlpos, tlUVpos, normalVector, color, lightmapValue);
    }

    // suitable for vertexbuilders using the DefaultVertexFormats.ENTITY format
    private static void addQuadVertex(Matrix4f matrixPos, Matrix3f matrixNormal, VertexConsumer renderBuffer, Vector3f pos, Vec2 texUV, Vector3f normalVector, Color color, int lightmapValue) {
        renderBuffer.vertex(matrixPos, pos.x(), pos.y(), pos.z()) // position coordinate
                .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha())        // color
                .uv(texUV.x, texUV.y)                     // texel coordinate
                .overlayCoords(OverlayTexture.NO_OVERLAY)  // only relevant for rendering Entities (Living)
                .uv2(lightmapValue)             // lightmap with full brightness
                .normal(matrixNormal, normalVector.x(), normalVector.y(), normalVector.z())
                .endVertex();
    }

}
