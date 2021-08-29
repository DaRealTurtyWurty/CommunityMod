package io.github.communitymod.client.blockentity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;

import io.github.communitymod.common.block_entities.MeatballChaliceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class MeatballChaliceRenderer implements BlockEntityRenderer<MeatballChaliceBlockEntity> {

    public MeatballChaliceRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(MeatballChaliceBlockEntity pBlockEntity, float pPartialTicks, PoseStack stack,
            MultiBufferSource pBuffer, int pCombinedLight, int pCombinedOverlay) {

        ItemStack item = pBlockEntity.getItem();
       
        if (!item.isEmpty()) {
            stack.pushPose();

            float f = 1.5f;
            stack.scale(f, f, f);
            stack.translate(0.41, 0.75f, 0.3333);
            stack.mulPose(new Quaternion(0, 0, 45, true));

            Minecraft.getInstance().getItemRenderer().render(item, TransformType.GROUND, false, stack, pBuffer,
                    pCombinedLight, pCombinedOverlay,
                    Minecraft.getInstance().getItemRenderer().getModel(item, pBlockEntity.getLevel(), null, 0));
            stack.popPose();
        }

    }

}
