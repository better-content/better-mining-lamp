package com.bettercontent.bettermininglamp;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

public enum CopperUtilityArmorMaterial implements ArmorMaterial {
    INSTANCE;

    @Override public int getDurabilityForType(ArmorItem.Type type) { return type == ArmorItem.Type.HELMET ? 165 : 0; }
    @Override public int getDefenseForType(ArmorItem.Type type) { return type == ArmorItem.Type.HELMET ? 1 : 0; }
    @Override public int getEnchantmentValue() { return 8; }
    @Override public SoundEvent getEquipSound() { return SoundEvents.ARMOR_EQUIP_CHAIN; }
    @Override public Ingredient getRepairIngredient() { return Ingredient.of(Items.COPPER_INGOT); }
    @Override public String getName() { return MiningHelmetMod.MOD_ID + ":copper_utility"; }
    @Override public float getToughness() { return 0.0F; }
    @Override public float getKnockbackResistance() { return 0.0F; }
}
