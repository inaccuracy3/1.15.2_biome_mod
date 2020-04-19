package work.prgrm.biomemod.entity.model;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@OnlyIn(Dist.CLIENT)
public class SleeperModel<T extends Entity> extends SegmentedModel<T> {
    private final ModelRenderer sleeperBodies;
    private final ModelRenderer sleeperRightEye;
    private final ModelRenderer sleeperLeftEye;
    private final ModelRenderer sleeperMouth;

    public SleeperModel(int slimeBodyTexOffY) {
        this.sleeperBodies = new ModelRenderer(this, 0, slimeBodyTexOffY);
        this.sleeperRightEye = new ModelRenderer(this, 32, 0);
        this.sleeperLeftEye = new ModelRenderer(this, 32, 4);
        this.sleeperMouth = new ModelRenderer(this, 32, 8);
        if (slimeBodyTexOffY > 0) {
            this.sleeperBodies.addBox(-3.0F, 17.0F, -3.0F, 6.0F, 6.0F, 6.0F);
            this.sleeperRightEye.addBox(-3.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
            this.sleeperLeftEye.addBox(1.25F, 18.0F, -3.5F, 2.0F, 2.0F, 2.0F);
            this.sleeperMouth.addBox(0.0F, 21.0F, -3.5F, 1.0F, 1.0F, 1.0F);
        } else {
            this.sleeperBodies.addBox(-4.0F, 16.0F, -4.0F, 8.0F, 8.0F, 8.0F);
        }

    }

    /**
     * Sets this entity's model rotation angles
     */
    @ParametersAreNonnullByDefault
    public void setRotationAngles(T entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Nonnull
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(this.sleeperBodies, this.sleeperRightEye, this.sleeperLeftEye, this.sleeperMouth);
    }
}
