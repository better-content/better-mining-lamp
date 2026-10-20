package com.bettercontent.bettermininglamp.client;

import com.bettercontent.bettermininglamp.MiningHelmetItem;
import com.bettercontent.bettermininglamp.diagnostics.PlaytestDiagnostics;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandler;
import dev.lambdaurora.lambdynlights.api.DynamicLightHandlers;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;

final class DynamicLightsCompat {
    private DynamicLightsCompat() {}

    static void register() {
        DynamicLightHandlers.registerDynamicLightHandler(EntityType.PLAYER,
                DynamicLightHandler.makeLivingEntityHandler(new DynamicLightHandler<Player>() {
                    @Override public int getLuminance(Player player) {
                        return MiningHelmetItem.isWorn(player) ? 15 : 0;
                    }

                    @Override public boolean isWaterSensitive(Player player) { return true; }
                }));
        if (PlaytestDiagnostics.LOG.enabled("integration")) {
            PlaytestDiagnostics.LOG.event("integration", "lamp.light_handler", "client", "registered", "native_registration_returned", null);
        }
    }
}
