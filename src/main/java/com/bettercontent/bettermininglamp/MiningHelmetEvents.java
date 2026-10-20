package com.bettercontent.bettermininglamp;

import com.bettercontent.bettermininglamp.diagnostics.PlaytestDiagnostics;
import com.bettercontent.bettermininglamp.diagnostics.PlaytestLog;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICurio;

public final class MiningHelmetEvents {
    private MiningHelmetEvents() {}

    @SubscribeEvent
    public static void livingDamaged(LivingDamageEvent event) {
        if (event.getEntity().level().isClientSide || event.getAmount() <= 0.0F
                || event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) return;
        LivingEntity wearer = event.getEntity();
        CuriosApi.getCuriosInventory(wearer).resolve().flatMap(handler ->
                handler.findFirstCurio(stack -> stack.is(MiningHelmetMod.MINING_HELMET.get())))
                .filter(result -> "head".equals(result.slotContext().identifier()))
                .ifPresent(result -> {
                    int wear = curioWear(event.getAmount());
                    boolean observing = PlaytestDiagnostics.LOG.enabled("durability");
                    int before = observing ? result.stack().getDamageValue() : -1;
                    result.stack().hurtAndBreak(wear, wearer,
                            brokenWearer -> ICurio.playBreakAnimation(result.stack(), brokenWearer));
                    if (observing) {
                        PlaytestDiagnostics.LOG.event("durability", "lamp.damage_processed", "server", "processed", "native_hurt_and_break_returned",
                                () -> PlaytestLog.fields("requested_wear", wear, "damage_before", before,
                                        "damage_after", result.stack().getDamageValue(), "stack_empty", result.stack().isEmpty()));
                    }
                });
    }

    static int curioWear(float finalDamage) {
        return Math.max(1, (int) Math.floor(finalDamage / 4.0F));
    }
}
