
package com.oreoredstone.orefish.entity.renderer;

import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.util.ResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.client.renderer.entity.model.CodModel;
import net.minecraft.client.renderer.entity.MobRenderer;

import com.oreoredstone.orefish.entity.StoneFishEntity;

@OnlyIn(Dist.CLIENT)
public class StoneFishRenderer {
	public static class ModelRegisterHandler {
		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void registerModels(ModelRegistryEvent event) {
			RenderingRegistry.registerEntityRenderingHandler(StoneFishEntity.entity,
					renderManager -> new MobRenderer(renderManager, new CodModel(), 0.5f) {

						@Override
						public ResourceLocation getEntityTexture(Entity entity) {
							return new ResourceLocation("ore_fish:textures/entities/stone_fish.png");
						}
					});
		}
	}
}
