package com.vindicators.slashblade;

import mods.flammpfeil.slashblade.SlashBlade;
import mods.flammpfeil.slashblade.registry.slashblade.SlashBladeDefinition;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public final class BladeEquipmentService {
    private BladeEquipmentService() {
    }

    public static void equipRandomBlade(Mob mob) {
        double chance = Config.BORN_WITH_BLADE_CHANCE.get();
        if (chance < 1.0 && mob.getRandom().nextDouble() >= chance) {
            return;
        }
        ItemStack blade = pickRandomBlade(mob);
        if (blade.isEmpty()) {
            return;
        }
        mob.setDropChance(EquipmentSlot.MAINHAND, Config.BLADE_DROP_CHANCE.get().floatValue());
        mob.setItemInHand(InteractionHand.MAIN_HAND, blade);
    }

    private static ItemStack pickRandomBlade(Mob mob) {
        Registry<SlashBladeDefinition> registry = SlashBlade.getSlashBladeDefinitionRegistry(mob.level());
        List<SlashBladeDefinition> definitions = new ArrayList<>();
        for (ResourceLocation key : registry.keySet()) {
            SlashBladeDefinition definition = registry.get(key);
            if (definition != null) {
                definitions.add(definition);
            }
        }
        if (definitions.isEmpty()) {
            return ItemStack.EMPTY;
        }
        for (int attempt = 0; attempt < 20; attempt++) {
            SlashBladeDefinition definition = definitions.get(mob.getRandom().nextInt(definitions.size()));
            ItemStack blade = definition.getBlade(mob.registryAccess());
            if (blade != null && !blade.isEmpty()) {
                return blade;
            }
        }
        return ItemStack.EMPTY;
    }
}
