package com.vindicators.slashblade.mixin;

import com.vindicators.slashblade.BladerManager;
import mods.flammpfeil.slashblade.entity.EntityAbstractSummonedSword;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityAbstractSummonedSword.class)
public abstract class MixinSummonedSword {
    @Inject(method = "onHitEntity", at = @At("HEAD"), cancellable = true)
    private void vsb$noFriendlyFire(EntityHitResult result, CallbackInfo ci) {
        Entity hit = result.getEntity();
        Entity owner = ((EntityAbstractSummonedSword) (Object) this).getShooter();
        if (owner instanceof Mob mob && hit != null && BladerManager.isBlader(mob) && BladerManager.isFriendly(mob, hit)) {
            ci.cancel();
        }
    }
}
