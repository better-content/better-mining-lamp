package com.bettercontent.mininghelmet.client;

import com.bettercontent.mininghelmet.MiningHelmetMod;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public final class MiningHelmetAttachmentRenderer implements ICurioRenderer {
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(
            new ResourceLocation(MiningHelmetMod.MOD_ID, "mining_helmet_attachment"), "main");
    public static final ResourceLocation TEXTURE = new ResourceLocation(
            MiningHelmetMod.MOD_ID, "textures/entity/mining_helmet_curio.png");
    public static final ResourceLocation EMISSIVE = new ResourceLocation(
            MiningHelmetMod.MOD_ID, "textures/entity/mining_helmet_curio_emissive.png");
    private final HumanoidModel<LivingEntity> model;

    public MiningHelmetAttachmentRenderer() {
        this.model = new HumanoidModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(LAYER));
        this.model.setAllVisible(false);
        this.model.head.visible = true;
    }

    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(LAYER, MiningHelmetAttachmentRenderer::layerDefinition);
    }

    private static LayerDefinition layerDefinition() {
        MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
        PartDefinition head = mesh.getRoot().getChild("head");
        head.addOrReplaceChild("bracket", CubeListBuilder.create().texOffs(0, 0)
                .addBox(-2.0F, -7.5F, -5.5F, 4.0F, 2.0F, 2.0F), PartPose.ZERO);
        head.addOrReplaceChild("lamp", CubeListBuilder.create().texOffs(0, 8)
                .addBox(-2.5F, -8.0F, -6.5F, 5.0F, 3.0F, 1.0F), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 32);
    }

    @Override
    public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext,
            PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffers,
            int packedLight, float limbSwing, float limbSwingAmount, float partialTicks,
            float ageInTicks, float netHeadYaw, float headPitch) {
        if (slotContext.entity().getItemBySlot(EquipmentSlot.HEAD).is(MiningHelmetMod.MINING_HELMET.get())) return;
        copyHead(renderLayerParent.getModel());
        renderAttachment(poseStack, buffers, packedLight);
    }

    @SubscribeEvent
    public static void renderArmorLamp(RenderLivingEvent.Post<?, ?> event) {
        if (!event.getEntity().getItemBySlot(EquipmentSlot.HEAD).is(MiningHelmetMod.MINING_HELMET.get())) return;
        MiningHelmetAttachmentRenderer renderer = Holder.INSTANCE;
        renderer.copyHead(event.getRenderer().getModel());
        renderer.renderAttachment(event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight());
    }

    private void copyHead(EntityModel<?> parent) {
        if (parent instanceof HumanoidModel<?> humanoid) model.head.copyFrom(humanoid.head);
    }

    private void renderAttachment(PoseStack poseStack, MultiBufferSource buffers, int packedLight) {
        VertexConsumer base = buffers.getBuffer(RenderType.entityCutoutNoCull(TEXTURE));
        model.renderToBuffer(poseStack, base, packedLight, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
        VertexConsumer glow = buffers.getBuffer(RenderType.eyes(EMISSIVE));
        model.renderToBuffer(poseStack, glow, 0xF000F0, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static final class Holder {
        private static final MiningHelmetAttachmentRenderer INSTANCE = new MiningHelmetAttachmentRenderer();
    }
}
