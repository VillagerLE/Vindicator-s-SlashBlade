package com.vindicators.slashblade.mixin;

import mods.flammpfeil.slashblade.item.ItemSlashBlade;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemSlashBlade.class)
public abstract class MixinItemSlashBlade {
    @Inject(method = "hurtEnemy(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void vsb$noDurabilityForMobs(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        if (!(attacker instanceof Player)) {
            cir.setReturnValue(true);
        }
    }
}
