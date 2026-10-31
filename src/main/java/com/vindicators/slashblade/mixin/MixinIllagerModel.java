package com.vindicators.slashblade.mixin;

import com.vindicators.slashblade.Config;
import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.MathHelper;
import dev.kosmx.playerAnim.core.util.Vec3f;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.monster.AbstractIllager;
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

@Mixin(IllagerModel.class)
public abstract class MixinIllagerModel implements ISlashBladeEntityModel {
    @Unique
    private final Map<ModelPart, String> vsb$partMap = new HashMap<>();
    @Unique
    private ModelPart vsb$root;
    @Unique
    private ModelPart vsb$body;
    @Unique
    private float vsb$bodyRotX = 0;
    @Unique
    private float vsb$bodyRotY = 0;
    @Unique
    private float vsb$bodyRotZ = 0;

    @Inject(method = "<init>(Lnet/minecraft/client/model/geom/ModelPart;)V", at = @At("TAIL"))
    private void vsb$init(ModelPart root, CallbackInfo ci) {
        this.vsb$root = root;
        ModelPart head = root.getChild("head");
        ModelPart hat = head.getChild("hat");
        ModelPart body = root.getChild("body");
        ModelPart arms = root.getChild("arms");
        ModelPart leftLeg = root.getChild("left_leg");
        ModelPart rightLeg = root.getChild("right_leg");
        ModelPart leftArm = root.getChild("left_arm");
        ModelPart rightArm = root.getChild("right_arm");

        this.vsb$body = body;
        this.vsb$partMap.put(head, "head");
        this.vsb$partMap.put(hat, "head");
        this.vsb$partMap.put(leftArm, "left arm");
        this.vsb$partMap.put(rightArm, "right arm");
        this.vsb$partMap.put(arms, "body");
        this.vsb$partMap.put(leftLeg, "left leg");
        this.vsb$partMap.put(rightLeg, "right leg");
        this.vsb$partMap.put(body, "torso");
    }

    @Inject(method = "setupAnim", at = @At("HEAD"))
    private void vsb$resetPose(AbstractIllager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (this.vsb$root != null) {
            this.vsb$root.getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @Inject(method = "setupAnim", at = @At("TAIL"))
    private void vsb$applyAnimation(AbstractIllager entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (!Config.USE_PLAYER_ANIMATOR.get()) {
            return;
        }
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
