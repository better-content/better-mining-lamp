package com.bettercontent.mininghelmet;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import top.theillusivec4.curios.api.CuriosApi;
import java.util.UUID;

@PrefixGameTestTemplate(false)
public final class MiningHelmetGameTests {
    private MiningHelmetGameTests() {}

    @GameTest(templateNamespace = MiningHelmetMod.MOD_ID, template = "blank", timeoutTicks = 40)
    public static void registryDurabilityRepairAndRecipeAreLive(GameTestHelper helper) {
        ItemStack helmet = new ItemStack(MiningHelmetMod.MINING_HELMET.get());
        helper.assertTrue(helmet.getMaxDamage() == 165, "Mining Helmet durability must be 165");
        helper.assertTrue(helmet.getItem().isValidRepairItem(helmet, new ItemStack(Items.COPPER_INGOT)),
                "Copper ingots must repair the Mining Helmet");
        helper.assertTrue(ForgeRegistries.ITEMS.getKey(helmet.getItem()).equals(
                new ResourceLocation(MiningHelmetMod.MOD_ID, "mining_helmet")), "Registry ID changed");
        helper.assertTrue(helper.getLevel().getRecipeManager().byKey(
                new ResourceLocation(MiningHelmetMod.MOD_ID, "mining_helmet")).isPresent(),
                "Survival recipe is missing");
        helper.succeed();
    }

    @GameTest(templateNamespace = MiningHelmetMod.MOD_ID, template = "blank", timeoutTicks = 40)
    public static void damageEventWearsEquippedHeadCurio(GameTestHelper helper) {
        ServerPlayer player = wearer(helper);
        ItemStack helmet = equip(player, "head");
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), 8.0F));
        helper.assertTrue(helmet.getDamageValue() == 2, "Eight damage must wear the equipped helmet by two");
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), 1.0F));
        helper.assertTrue(helmet.getDamageValue() == 3, "Positive damage must cause at least one wear");
        helper.succeed();
    }

    @GameTest(templateNamespace = MiningHelmetMod.MOD_ID, template = "blank", timeoutTicks = 40)
    public static void bypassAndNonpositiveDamageDoNotWearCurio(GameTestHelper helper) {
        ServerPlayer player = wearer(helper);
        ItemStack helmet = equip(player, "head");
        var bypass = player.damageSources().fellOutOfWorld();
        helper.assertTrue(bypass.is(DamageTypeTags.BYPASSES_ARMOR), "Fixture must use armor-bypassing damage");
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, bypass, 12.0F));
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), 0.0F));
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), -4.0F));
        helper.assertTrue(helmet.getDamageValue() == 0, "Excluded damage must preserve durability");
        helper.succeed();
    }

    @GameTest(templateNamespace = MiningHelmetMod.MOD_ID, template = "blank", timeoutTicks = 40)
    public static void damageEventBreaksExhaustedHeadCurio(GameTestHelper helper) {
        ServerPlayer player = wearer(helper);
        ItemStack helmet = equip(player, "head");
        helmet.setDamageValue(helmet.getMaxDamage() - 1);
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), 4.0F));
        helper.assertTrue(helmet.isEmpty(), "Last durability point must break the equipped stack");
        helper.assertTrue(CuriosApi.getCuriosInventory(player).resolve().orElseThrow()
            .findFirstCurio(MiningHelmetMod.MINING_HELMET.get()).isEmpty(), "Broken helmet must no longer be equipped");
        helper.succeed();
    }

    @GameTest(templateNamespace = MiningHelmetMod.MOD_ID, template = "blank", timeoutTicks = 40)
    public static void otherCurioSlotsAndInventoryAreNotWorn(GameTestHelper helper) {
        ServerPlayer player = wearer(helper);
        ItemStack elsewhere = equip(player, "curio");
        ItemStack carried = new ItemStack(MiningHelmetMod.MINING_HELMET.get());
        player.getInventory().setItem(0, carried);
        MinecraftForge.EVENT_BUS.post(new LivingDamageEvent(player, player.damageSources().playerAttack(player), 12.0F));
        helper.assertTrue(elsewhere.getDamageValue() == 0 && carried.getDamageValue() == 0,
            "Only a helmet equipped in the Curios head slot should wear");
        helper.succeed();
    }

    private static ServerPlayer wearer(GameTestHelper helper) {
        var player = FakePlayerFactory.get(helper.getLevel(), new GameProfile(UUID.randomUUID(), "helmet-test"));
        player.getAbilities().instabuild = false;
        return player;
    }

    private static ItemStack equip(ServerPlayer player, String slot) {
        var handler = CuriosApi.getCuriosInventory(player).resolve().orElseThrow();
        // Slots come from the GameTest-only datapack, never the production JAR.
        var stacks = handler.getStacksHandler(slot).orElseThrow();
        ItemStack helmet = new ItemStack(MiningHelmetMod.MINING_HELMET.get());
        stacks.getStacks().setStackInSlot(0, helmet);
        return helmet;
    }
}
