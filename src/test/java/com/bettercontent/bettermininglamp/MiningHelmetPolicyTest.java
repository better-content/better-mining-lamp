package com.bettercontent.bettermininglamp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiningHelmetPolicyTest {
    @Test
    void optionalLightingAdmissionDoesNotLoadAbsentProvider() {
        var registrations = new java.util.concurrent.atomic.AtomicInteger();
        org.junit.jupiter.api.Assertions.assertFalse(MiningLampLightPolicy.registerIfAvailable(id -> false,
                () -> { throw new AssertionError("absent provider was invoked"); }));
        org.junit.jupiter.api.Assertions.assertTrue(MiningLampLightPolicy.registerIfAvailable(id -> {
            assertEquals("sodiumdynamiclights", id); return true;
        }, registrations::incrementAndGet));
        assertEquals(1, registrations.get());
    }

    @Test
    void onlyArmorHeadOrHeadCurioEmitsMaximumLight() {
        assertEquals(15, MiningLampLightPolicy.luminance(MiningLampLightPolicy.isWorn(true, java.util.stream.Stream.empty())));
        assertEquals(15, MiningLampLightPolicy.luminance(MiningLampLightPolicy.isWorn(false, java.util.stream.Stream.of("head"))));
        for (String slot : new String[]{"body", "hands", "belt", "inventory", "offhand"}) {
            assertEquals(0, MiningLampLightPolicy.luminance(MiningLampLightPolicy.isWorn(false, java.util.stream.Stream.of(slot))));
        }
        assertEquals(0, MiningLampLightPolicy.luminance(MiningLampLightPolicy.isWorn(false, java.util.stream.Stream.empty())));
    }

    @Test
    void bundledDynamicLightResourceMatchesItemAndEmission() throws Exception {
        try (var input = getClass().getResourceAsStream("/assets/better_mining_lamp/dynamiclights/item/better_mining_lamp.json")) {
            org.junit.jupiter.api.Assertions.assertNotNull(input);
            var json = com.google.gson.JsonParser.parseReader(new java.io.InputStreamReader(input, java.nio.charset.StandardCharsets.UTF_8)).getAsJsonObject();
            assertEquals("better_mining_lamp:better_mining_lamp", json.get("item").getAsString());
            assertEquals(15, json.get("luminance").getAsInt());
            org.junit.jupiter.api.Assertions.assertTrue(json.get("water_sensitive").getAsBoolean());
        }
    }

    @Test
    void copperCapHasAuthoredDurabilityAndDefense() {
        assertEquals(165, CopperUtilityArmorMaterial.INSTANCE.getDurabilityForType(
                net.minecraft.world.item.ArmorItem.Type.HELMET));
        assertEquals(1, CopperUtilityArmorMaterial.INSTANCE.getDefenseForType(
                net.minecraft.world.item.ArmorItem.Type.HELMET));
    }

    @Test
    void curioWearUsesFinalDamageQuarterWithMinimumOne() {
        assertEquals(1, MiningHelmetEvents.curioWear(0.25F));
        assertEquals(1, MiningHelmetEvents.curioWear(7.99F));
        assertEquals(2, MiningHelmetEvents.curioWear(8.0F));
        assertEquals(5, MiningHelmetEvents.curioWear(20.0F));
    }
}
