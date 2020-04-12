package work.prgrm.biomemod.entity.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.entity.mob.CreeperManEntity;
import work.prgrm.biomemod.entity.model.CreeperManModel;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CreeperManRenderer extends MobRenderer<CreeperManEntity, CreeperManModel<CreeperManEntity>>{
    private static final ResourceLocation CREEPERMAN_TEXTURES = new ResourceLocation(BiomeMod.MOD_ID,"textures/entity/creeper_man.png");
    private final Random rnd = new Random();

    public CreeperManRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CreeperManModel<>(0.0F), 0.5F);
    }

    public static class RenderFactory implements IRenderFactory<CreeperManEntity>{
        @Override
        public EntityRenderer<? super CreeperManEntity> createRenderFor(EntityRendererManager manager)
        {
            try{
                return new CreeperManRenderer(manager);
            }catch(Exception e){
                return null;
        }
        }
    }

    /**
     * Returns the location of an entity's texture.
     */
    @Nonnull
    @ParametersAreNonnullByDefault
    @Override
    public ResourceLocation getEntityTexture( final CreeperManEntity entity) {
        return CREEPERMAN_TEXTURES;
    }

}
