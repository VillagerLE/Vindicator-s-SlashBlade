package com.vindicators.slashblade.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vindicators.slashblade.Config;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.animation.VanillaConvertedVmdAnimation;
import net.mrqx.sbr_core.client.model.ISlashBladeEntityModel;
import net.mrqx.sbr_core.entity.ISlashBladeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class MixinLivingEntityRenderer {
    @Unique
    private boolean vsb$bodyPosePushed = false;

    @Accessor("model")
    abstract EntityModel<?> vsb$getModel();

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"))
    private void vsb$preRender(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!Config.USE_PLAYER_ANIMATOR.get()) {
            return;
        }
        if (entity instanceof ISlashBladeEntity slashBladeEntity) {
            VanillaConvertedVmdAnimation animation = slashBladeEntity.getCurrentAnimation();
            if (animation != null) {
                animation.setTickDelta(partialTicks);
                if (this.vsb$getModel() instanceof ISlashBladeEntityModel slashModel) {
                    poseStack.pushPose();
                    poseStack.mulPose(slashModel.getBodyRot());
                    this.vsb$bodyPosePushed = true;
                }
            }
        }
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("RETURN"))
    private void vsb$postRender(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (this.vsb$bodyPosePushed) {
            this.vsb$bodyPosePushed = false;
            poseStack.popPose();
        }
    }
}
