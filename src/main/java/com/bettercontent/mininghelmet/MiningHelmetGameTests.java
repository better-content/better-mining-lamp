package com.bettercontent.mininghelmet;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.gametest.PrefixGameTestTemplate;
import net.minecraftforge.registries.ForgeRegistries;

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
}
