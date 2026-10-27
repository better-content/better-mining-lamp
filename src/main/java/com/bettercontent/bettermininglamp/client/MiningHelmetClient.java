package com.bettercontent.bettermininglamp.client;

import com.bettercontent.bettermininglamp.MiningHelmetMod;
import com.bettercontent.bettermininglamp.diagnostics.PlaytestDiagnostics;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

public final class MiningHelmetClient {
    private MiningHelmetClient() {}

    public static void register() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MiningHelmetClient::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(MiningHelmetAttachmentRenderer::registerLayer);
        MinecraftForge.EVENT_BUS.register(MiningHelmetAttachmentRenderer.class);
    }

    private static void setup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            CuriosRendererRegistry.register(MiningHelmetMod.MINING_HELMET.get(), MiningHelmetAttachmentRenderer::new);
            if (!com.bettercontent.bettermininglamp.MiningLampLightPolicy.registerIfAvailable(
                    id -> ModList.get().isLoaded(id), () -> DynamicLightsCompat.register())
                    && PlaytestDiagnostics.LOG.enabled("integration")) {
                PlaytestDiagnostics.LOG.event("integration", "lamp.light_handler", "client", "unavailable", "optional_provider_absent", null);
            }
        });
    }
}
