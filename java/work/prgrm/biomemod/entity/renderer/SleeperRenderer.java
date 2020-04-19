package work.prgrm.biomemod.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import work.prgrm.biomemod.entity.layer.SleeperGelLayer;
import work.prgrm.biomemod.entity.mob.SleeperEntity;
import work.prgrm.biomemod.entity.model.SleeperModel;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class SleeperRenderer extends MobRenderer<SleeperEntity, SleeperModel<SleeperEntity>> {
    private static final ResourceLocation SLIME_TEXTURES = new ResourceLocation("textures/entity/slime/slime.png");

    public SleeperRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new SleeperModel<>(16), 0.25F);
        this.addLayer(new SleeperGelLayer<>(this));
    }

    public void render(SleeperEntity entityIn, float entityYaw, float partialTicks, @Nonnull MatrixStack matrixStackIn,@Nonnull IRenderTypeBuffer bufferIn, int packedLightIn) {
        this.shadowSize = 0.25F * (float)entityIn.getSleeperSize();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    protected void preRenderCallback(SleeperEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        float f = 0.999F;
        matrixStackIn.scale(0.999F, 0.999F, 0.999F);
        matrixStackIn.translate(0.0D, 0.001F, 0.0D);
        float f1 = (float)entitylivingbaseIn.getSleeperSize();
        float f2 = MathHelper.lerp(partialTickTime, entitylivingbaseIn.prevSquishFactor, entitylivingbaseIn.squishFactor) / (f1 * 0.5F + 1.0F);
        float f3 = 1.0F / (f2 + 1.0F);
        matrixStackIn.scale(f3 * f1, 1.0F / f3 * f1, f3 * f1);
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Nonnull
    @Override
    @ParametersAreNonnullByDefault
    public ResourceLocation getEntityTexture(final SleeperEntity entity) {
        return SLIME_TEXTURES;
    }
}
