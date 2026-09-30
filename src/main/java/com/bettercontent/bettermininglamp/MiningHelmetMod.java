package com.bettercontent.bettermininglamp;

import com.bettercontent.bettermininglamp.client.MiningHelmetClient;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.RegisterGameTestsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(MiningHelmetMod.MOD_ID)
public final class MiningHelmetMod {
    public static final String MOD_ID = "better_mining_lamp";
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> MINING_HELMET = ITEMS.register("better_mining_lamp", MiningHelmetItem::new);

    public MiningHelmetMod() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ITEMS.register(modBus);
        modBus.addListener(this::creativeTab);
        modBus.addListener(this::registerGameTests);
        MinecraftForge.EVENT_BUS.register(MiningHelmetEvents.class);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> MiningHelmetClient::register);
    }

    private void creativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) event.accept(MINING_HELMET);
    }

    private void registerGameTests(RegisterGameTestsEvent event) {
        event.register(MiningHelmetGameTests.class);
    }
}
