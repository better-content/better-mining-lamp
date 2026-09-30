package com.bettercontent.bettermininglamp;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MiningHelmetPolicyTest {
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
