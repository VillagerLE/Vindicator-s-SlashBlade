package com.vindicators.slashblade;

public record BladerLevelSettings(
        boolean canRapidSlash,
        boolean preferAirAttack,
        boolean canUseUpperSlashJump,
        boolean canVoidSlash,
        boolean canDoSlashArts,
        boolean canDoJustSlashArts,
        boolean powerful,
        boolean canAirTrick,
        boolean canTrickDown,
        boolean canTrickDodge,
        boolean canUseMirageBlade,
        int attackCooldown) {

    private static final BladerLevelSettings[] LEVELS = new BladerLevelSettings[7];

    static {
        LEVELS[1] = new BladerLevelSettings(false, false, false, false, false, false, false, false, false, false, false, 5);
        LEVELS[2] = new BladerLevelSettings(false, false, false, false, false, false, true, false, false, false, false, 4);
        LEVELS[3] = new BladerLevelSettings(true, false, true, false, false, false, true, false, false, false, false, 3);
        LEVELS[4] = new BladerLevelSettings(true, true, true, true, true, false, true, true, true, true, false, 2);
        LEVELS[5] = new BladerLevelSettings(true, true, true, true, true, true, true, true, true, true, false, 2);
        LEVELS[6] = new BladerLevelSettings(true, true, true, true, true, true, true, true, true, true, true, 2);
    }

    public static BladerLevelSettings byLevel(int level) {
        return LEVELS[Math.max(1, Math.min(6, level))];
    }
}
