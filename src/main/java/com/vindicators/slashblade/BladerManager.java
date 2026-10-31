package com.vindicators.slashblade;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class BladerManager {
    private static volatile Map<ResourceLocation, Integer> bladerWhitelist = Map.of();
    private static volatile Set<ResourceLocation> bornWithBladeWhitelist = Set.of();

    private BladerManager() {
    }

    public static void reload() {
        bladerWhitelist = parseBladerEntries(Config.BLADER_WHITELIST.get());
        bornWithBladeWhitelist = parseIds(Config.BORN_WITH_BLADE_WHITELIST.get());
    }

    public static boolean isBlader(Mob mob) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(mob.getType());
        return id != null && bladerWhitelist.containsKey(id);
    }

    public static boolean isBornWithBlade(EntityType<?> type) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        return id != null && bornWithBladeWhitelist.contains(id);
    }

    public static int getLevel(EntityType<?> type) {
        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if (id != null) {
            Integer level = bladerWhitelist.get(id);
            if (level != null) {
                return level;
            }
        }
        return 1;
    }

    public static boolean isFriendly(LivingEntity attacker, Entity entity) {
        if (entity == attacker) {
            return true;
        }
        if (attacker.isAlliedTo(entity)) {
            return true;
        }
        if (entity instanceof Player && !(attacker instanceof Enemy || (attacker instanceof Mob mob && mob.getTarget() instanceof Player))) {
            return true;
        }
        return false;
    }

    private static Map<ResourceLocation, Integer> parseBladerEntries(java.util.List<? extends String> list) {
        Map<ResourceLocation, Integer> result = new HashMap<>();
        for (String entry : list) {
            String[] parts = entry.split(",");
            ResourceLocation id;
            int level = 1;
            try {
                id = ResourceLocation.parse(parts[0].trim());
            } catch (Exception ignored) {
                continue;
            }
            if (parts.length >= 2) {
                try {
                    level = Integer.parseInt(parts[1].trim());
                } catch (Exception ignored) {
                    level = 1;
                }
            }
            if (level < 1 || level > 6) {
                level = 1;
            }
            result.put(id, level);
        }
        return Map.copyOf(result);
    }

    private static Set<ResourceLocation> parseIds(java.util.List<? extends String> list) {
        Set<ResourceLocation> result = new HashSet<>();
        for (String s : list) {
            try {
                result.add(ResourceLocation.parse(s.trim()));
            } catch (Exception ignored) {
            }
        }
        return Set.copyOf(result);
    }
}
