package com.vindicators.slashblade;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public final class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLADER_WHITELIST = BUILDER
            .comment("Entities that can fight with a SlashBlade, with their level.",
                    "Format per entry: \"<entity_id>, <level>\" (level 1-6).",
                    "Example: [\"minecraft:vindicator, 6\", \"minecraft:pillager, 2\"]")
            .defineListAllowEmpty("bladerWhitelist", List.of("minecraft:vindicator, 6"), () -> "", Config::validateEntry);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BORN_WITH_BLADE_WHITELIST = BUILDER
            .comment("Entities that spawn holding a random SlashBlade.")
            .defineListAllowEmpty("bornWithBladeWhitelist", List.of("minecraft:vindicator"), () -> "", Config::validateEntityId);

    public static final ModConfigSpec.DoubleValue BORN_WITH_BLADE_CHANCE = BUILDER
            .comment("Chance (0.0 - 1.0) that a born_with_blade mob actually spawns with a blade.")
            .defineInRange("bornWithBladeChance", 1.0, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BLADE_DROP_CHANCE = BUILDER
            .comment("Chance (0.0 - 1.0) that a blader drops its blade on death.")
            .defineInRange("bladeDropChance", 0.0, 0.0, 1.0);

    public static final ModConfigSpec.BooleanValue SHOW_ENDER_PARTICLES = BUILDER
            .comment("Whether bladers emit ender particles when using step tricks.")
            .define("showEnderParticles", true);

    public static final ModConfigSpec.BooleanValue USE_PLAYER_ANIMATOR = BUILDER
            .comment("Whether bladers play slash animations via Player Animator. Disable for lower-end machines.")
            .define("usePlayerAnimator", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    private Config() {
    }

    private static boolean validateEntityId(Object obj) {
        if (!(obj instanceof String s) || s.isBlank()) {
            return false;
        }
        try {
            ResourceLocation.parse(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean validateEntry(Object obj) {
        return obj instanceof String s && !s.isBlank();
    }
}
