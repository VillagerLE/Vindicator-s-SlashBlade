package com.vindicators.slashblade;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import org.slf4j.Logger;

@Mod(VindicatorsSlashBlade.MODID)
public class VindicatorsSlashBlade {
    public static final String MODID = "vindicators_slash_blade";
    public static final Logger LOGGER = LogUtils.getLogger();

    public VindicatorsSlashBlade(IEventBus modEventBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        modEventBus.addListener((ModConfigEvent.Loading event) -> BladerManager.reload());
        modEventBus.addListener((ModConfigEvent.Reloading event) -> BladerManager.reload());
    }
}
