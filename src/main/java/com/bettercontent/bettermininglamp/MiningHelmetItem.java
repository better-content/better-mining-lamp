package com.bettercontent.bettermininglamp;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public final class MiningHelmetItem extends ArmorItem implements ICurioItem {
    public MiningHelmetItem() {
        super(CopperUtilityArmorMaterial.INSTANCE, Type.HELMET, new Properties().stacksTo(1));
    }

    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        if (!"head".equals(slotContext.identifier())) return false;
        LivingEntity wearer = slotContext.entity();
        if (wearer.getItemBySlot(EquipmentSlot.HEAD).is(this)) return false;
        return CuriosApi.getCuriosInventory(wearer)
                .map(handler -> handler.findCurios(this).isEmpty())
                .orElse(false);
    }

    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack ingredient) {
        return ingredient.is(Items.COPPER_INGOT) || super.isValidRepairItem(stack, ingredient);
    }

    @Override
    public String getArmorTexture(ItemStack stack, net.minecraft.world.entity.Entity entity,
                                  EquipmentSlot slot, String type) {
        return MiningHelmetMod.MOD_ID + ":textures/models/armor/better_mining_lamp_layer_1.png";
    }

    public static boolean isWorn(LivingEntity entity) {
        if (entity.getItemBySlot(EquipmentSlot.HEAD).is(MiningHelmetMod.MINING_HELMET.get())) return true;
        return CuriosApi.getCuriosInventory(entity)
                .map(handler -> handler.findCurios(MiningHelmetMod.MINING_HELMET.get()).stream()
                        .anyMatch(result -> "head".equals(result.slotContext().identifier())))
                .orElse(false);
    }
}
