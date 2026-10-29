package com.vindicators.slashblade.mixin;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.MathHelper;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.mrqx.sbr_core.animation.VanillaConvertedVmdAnimation;
import net.mrqx.sbr_core.client.model.ISlashBladeEntityModel;
import net.mrqx.sbr_core.entity.ISlashBladeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(HumanoidModel.class)
public abstract class MixinHumanoidModel implements ISlashBladeEntityModel {
    @Unique
    private final Map<ModelPart, String> vsb$partMap = new HashMap<>();
    @Unique
    private ModelPart vsb$body;
    @Unique
    private float vsb$bodyRotX = 0;
    @Unique
    private float vsb$bodyRotY = 0;
    @Unique
    private float vsb$bodyRotZ = 0;

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void vsb$resetPose(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        HumanoidModel<?> model = (HumanoidModel<?>) (Object) this;
        if (this.vsb$body == null) {
            this.vsb$body = model.body;
            this.vsb$partMap.put(model.head, "head");
            this.vsb$partMap.put(model.leftArm, "left arm");
            this.vsb$partMap.put(model.rightArm, "right arm");
            this.vsb$partMap.put(model.leftLeg, "left leg");
            this.vsb$partMap.put(model.rightLeg, "right leg");
            this.vsb$partMap.put(model.body, "torso");
        }
        model.head.getAllParts().forEach(ModelPart::resetPose);
        model.body.getAllParts().forEach(ModelPart::resetPose);
        model.leftArm.getAllParts().forEach(ModelPart::resetPose);
        model.rightArm.getAllParts().forEach(ModelPart::resetPose);
        model.leftLeg.getAllParts().forEach(ModelPart::resetPose);
        model.rightLeg.getAllParts().forEach(ModelPart::resetPose);
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void vsb$applyAnimation(LivingEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (entity instanceof ISlashBladeEntity slashBladeEntity) {
            VanillaConvertedVmdAnimation animation = slashBladeEntity.getCurrentAnimation();
            if (animation != null) {
                for (Map.Entry<ModelPart, String> entry : this.vsb$partMap.entrySet()) {
                    animation.updatePart(entry.getValue(), entry.getKey());
                }
                Vec3f rot = animation.get3DTransform("body", TransformType.ROTATION, new Vec3f(
                        MathHelper.clampToRadian(this.vsb$body.xRot),
                        MathHelper.clampToRadian(this.vsb$body.yRot),
                        MathHelper.clampToRadian(this.vsb$body.zRot)));
                this.vsb$bodyRotX = rot.getX();
                this.vsb$bodyRotY = rot.getY();
                this.vsb$bodyRotZ = rot.getZ();
                return;
            }
        }
        this.vsb$bodyRotX = 0;
        this.vsb$bodyRotY = 0;
        this.vsb$bodyRotZ = 0;
    }

    @Override
    public ModelPart getBody() {
        return this.vsb$body;
    }

    @Override
    public float getBodyRotX() {
        return this.vsb$bodyRotX;
    }

    @Override
    public float getBodyRotY() {
        return this.vsb$bodyRotY;
    }

    @Override
    public float getBodyRotZ() {
        return this.vsb$bodyRotZ;
    }

    @Override
    public void setBodyRotX(float bodyRotX) {
        this.vsb$bodyRotX = bodyRotX;
    }

    @Override
    public void setBodyRotY(float bodyRotY) {
        this.vsb$bodyRotY = bodyRotY;
    }

    @Override
    public void setBodyRotZ(float bodyRotZ) {
        this.vsb$bodyRotZ = bodyRotZ;
    }

    @Override
    public Map<ModelPart, String> getPartMap() {
        return this.vsb$partMap;
    }
}
