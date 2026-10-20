package com.bettercontent.bettermininglamp.diagnostics;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStoppedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/** Native lifecycle bridge only; the bounded sink retains no player, menu or world references. */
@Mod.EventBusSubscriber(modid = "better_mining_lamp")
public final class PlaytestDiagnostics {
    public static final PlaytestLog LOG = new PlaytestLog("better_mining_lamp");
    private PlaytestDiagnostics() {}
    private static String version(String id) {
        return ModList.get().getModContainerById(id)
                .map(mod -> mod.getModInfo().getVersion().toString()).orElse("absent");
    }
    @SubscribeEvent public static void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && LOG.enabled("summary")) LOG.summary("server");
    }
    @SubscribeEvent public static void stop(ServerStoppedEvent event) { LOG.reset("server"); }

    private static String source() {
        var file = ModList.get().getModFileById("better_mining_lamp");
        return LOG.sourceIdentity(file == null || file.getFile() == null ? (java.nio.file.Path) null
                : file.getFile().findResource("META-INF", "better-content-source.properties"));
    }
    @Mod.EventBusSubscriber(modid = "better_mining_lamp", bus = Mod.EventBusSubscriber.Bus.MOD)
    public static final class Startup {
        @SubscribeEvent public static void setup(FMLCommonSetupEvent event) {
            LOG.status("common", version("better_mining_lamp"), source(), () -> PlaytestLog.fields("enabled", true, "curios_version", version("curios"), "sodiumdynamiclights_version", version("sodiumdynamiclights")));
        }
    }
    @Mod.EventBusSubscriber(modid = "better_mining_lamp", value = Dist.CLIENT)
    public static final class Client {
        @SubscribeEvent public static void tick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END && LOG.enabled("summary")) LOG.summary("client");
        }
        @SubscribeEvent public static void logout(ClientPlayerNetworkEvent.LoggingOut event) { LOG.reset("client"); }
    }
}
