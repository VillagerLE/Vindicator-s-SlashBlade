package com.vindicators.slashblade.mixin;

import com.vindicators.slashblade.BladerLevelSettings;
import com.vindicators.slashblade.BladerManager;
import com.vindicators.slashblade.Config;
import mods.flammpfeil.slashblade.capability.slashblade.BladeStateAccess;
import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import mods.flammpfeil.slashblade.util.TargetSelector;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.mrqx.sbr_core.animation.VanillaConvertedVmdAnimation;
import net.mrqx.sbr_core.entity.ISlashBladeEntity;
import net.mrqx.sbr_core.utils.JustSlashArtManager;
import net.mrqx.sbr_core.utils.SlashBladeAttackUtils;
import net.mrqx.sbr_core.utils.SlashBladeMovementUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

@Mixin(Mob.class)
public abstract class MixinMob implements ISlashBladeEntity {

    @Unique
    @Nullable
    private VanillaConvertedVmdAnimation vsb$currentAnimation;

    @Unique
    private Mob vsb$self() {
        return (Mob) (Object) this;
    }

    @Inject(method = "createMobAttributes", at = @At("RETURN"))
    private static void vsb$addSweepingAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir) {
        cir.getReturnValue().add(Attributes.SWEEPING_DAMAGE_RATIO);
    }

    @Override
    public void setCurrentAnimation(@Nullable VanillaConvertedVmdAnimation animation) {
        this.vsb$currentAnimation = animation;
        if (this.vsb$currentAnimation != null) {
            this.vsb$currentAnimation.play();
        }
    }

    @Override
    public @Nullable VanillaConvertedVmdAnimation getCurrentAnimation() {
        return this.vsb$currentAnimation;
    }

    @Override
    public boolean canProgressCombo(LivingEntity target, ResourceLocation current, ResourceLocation next) {
        return this.canUseCombo(next);
    }

    @Override
    public boolean canUseCombo(ResourceLocation combo) {
        return true;
    }

    @Override
    public Set<Class<? extends Entity>> getAttackableEntities() {
        return Set.of(LivingEntity.class);
    }

    @Override
    public boolean useUpperSlashJump() {
        return BladerLevelSettings.byLevel(BladerManager.getLevel(this.vsb$self().getType())).canUseUpperSlashJump();
    }

    @Override
    public List<Entity> processTargetList(Level world, LivingEntity attacker, AABB aabb, double reach, List<Entity> originalTargetList) {
        List<Entity> targetList = ISlashBladeEntity.super.processTargetList(world, attacker, aabb, reach, originalTargetList);
        targetList.removeIf(entity -> this.vsb$isFriendly(attacker, entity));

        BladeStateAccess.of(attacker.getMainHandItem()).ifPresent(state -> {
            Entity target = state.getTargetEntity(world);
            if (target != null) {
                targetList.add(target);
            }
        });
        if (attacker instanceof Mob mob && mob.getTarget() != null) {
            targetList.add(mob.getTarget());
        }
        return targetList;
    }

    @Unique
    private boolean vsb$isFriendly(LivingEntity attacker, Entity entity) {
        if (entity == attacker) {
            return true;
        }

        if (attacker.isAlliedTo(entity)) {
            return true;
        }

        if (entity instanceof Player && !this.vsb$isHostileToPlayer(attacker)) {
            return true;
        }
        return false;
    }

    @Unique
    private boolean vsb$isHostileToPlayer(LivingEntity attacker) {
        if (attacker instanceof Enemy) {
            return true;
        }
        return attacker instanceof Mob mob && mob.getTarget() instanceof Player;
    }

    @Inject(method = "isWithinMeleeAttackRange(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void vsb$bladeReach(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        Mob mob = this.vsb$self();
        if (BladeStateAccess.of(mob.getMainHandItem()).isPresent()) {
            double reach = TargetSelector.getResolvedReach(mob);
            cir.setReturnValue(mob.distanceTo(entity) < reach * reach);
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void vsb$tick(CallbackInfo ci) {
        Mob mob = this.vsb$self();

        if (mob.level().isClientSide()) {
            if (this.vsb$currentAnimation != null) {
                this.vsb$currentAnimation.tick();
                if (!this.vsb$currentAnimation.isActive() && this.vsb$currentAnimation.getCurrentTick() > 0) {
                    this.vsb$currentAnimation = null;
                }
            }
            return;
        }

        if (!BladerManager.isBlader(mob)) {
            return;
        }

        if (mob.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemSlashBlade) {
            mob.getItemInHand(InteractionHand.MAIN_HAND).inventoryTick(mob.level(), mob, 0, true);
        }

        CompoundTag data = mob.getPersistentData();
        int voidSlashCounter = data.getInt(SlashBladeAttackUtils.VOID_SLASH_COUNTER_KEY);
        if (voidSlashCounter > 0) {
            data.putInt(SlashBladeAttackUtils.VOID_SLASH_COUNTER_KEY, voidSlashCounter - 1);
        }

        BladerLevelSettings settings = BladerLevelSettings.byLevel(BladerManager.getLevel(mob.getType()));
        long justCooldown = JustSlashArtManager.getJustCooldown(mob);
        if (justCooldown > 0) {
            justCooldown -= settings.powerful() ? 2 : 1;
            JustSlashArtManager.setJustCooldown(mob, justCooldown);
            if (justCooldown <= 0) {
                JustSlashArtManager.resetJustCount(mob);
            }
        }

        LivingEntity target = mob.getTarget();
        if (target != null && target.isAlive()
                && mob.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof ItemSlashBlade
                && Config.SHOW_ENDER_PARTICLES.get()) {
            SlashBladeMovementUtils.tickSlashBladeTrick(mob, target,
                    settings.canAirTrick(), settings.canTrickDown(), settings.canTrickDodge(), settings.powerful());
        }
    }
}
