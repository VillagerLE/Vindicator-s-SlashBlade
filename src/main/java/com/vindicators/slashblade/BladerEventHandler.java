package com.vindicators.slashblade;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.mrqx.sbr_core.entity.ai.goal.SimpleSlashGoal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = VindicatorsSlashBlade.MODID)
public final class BladerEventHandler {
    private static final String BORN_WITH_BLADE_GRANTED = "vindicators_slash_blade:born_with_blade_granted";

    private BladerEventHandler() {
    }

    @SubscribeEvent
    public static void onEntityJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }
        Entity entity = event.getEntity();
        if (!(entity instanceof PathfinderMob mob)) {
            return;
        }

        EntityType<?> type = mob.getType();
        boolean blader = BladerManager.isBlader(mob);
        boolean bornWithBlade = BladerManager.isBornWithBlade(type);

        if (blader || bornWithBlade) {
            addSlashGoal(mob);
        }
        if (bornWithBlade && !event.loadedFromDisk() && !mob.getPersistentData().getBoolean(BORN_WITH_BLADE_GRANTED)) {
            BladeEquipmentService.equipRandomBlade(mob);
            mob.getPersistentData().putBoolean(BORN_WITH_BLADE_GRANTED, true);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addSlashGoal(PathfinderMob mob) {
        BladerLevelSettings settings = BladerLevelSettings.byLevel(BladerManager.getLevel(mob.getType()));
        SimpleSlashGoal goal = new SimpleSlashGoal(mob, 1.0D, settings.attackCooldown(), true,
                settings.canRapidSlash(), settings.preferAirAttack(), settings.canVoidSlash(),
                settings.canDoSlashArts(), settings.canDoJustSlashArts(), settings.powerful());
        mob.goalSelector.addGoal(0, goal);
    }
}
