package com.bettercontent.mininghelmet.client;

import com.bettercontent.mininghelmet.MiningHelmetMod;
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
            if (ModList.get().isLoaded("sodiumdynamiclights")) DynamicLightsCompat.register();
        });
    }
}
