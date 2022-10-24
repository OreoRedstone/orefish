package com.oreoredstone.orefish.procedures;

import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.fml.loading.FMLPaths;

import net.minecraft.potion.EffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.Collection;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import com.oreoredstone.orefish.potion.GrowingPotionEffect;
import com.oreoredstone.orefish.item.ArditeScaleItem;
import com.oreoredstone.orefish.OreFishMod;

import com.google.gson.Gson;

public class ArditeFishRightClickProcedure {

	public static void executeProcedure(Map<String, Object> dependencies) {
		if (dependencies.get("entity") == null) {
			if (!dependencies.containsKey("entity"))
				OreFishMod.LOGGER.warn("Failed to load dependency entity for procedure FishRightClick!");
			return;
		}
		if (dependencies.get("sourceentity") == null) {
			if (!dependencies.containsKey("sourceentity"))
				OreFishMod.LOGGER.warn("Failed to load dependency sourceentity for procedure FishRightClick!");
			return;
		}
		Entity entity = (Entity) dependencies.get("entity");
		Entity sourceentity = (Entity) dependencies.get("sourceentity");
		File configfile = new File("");
		com.google.gson.JsonObject jsonobj = new com.google.gson.JsonObject();
		configfile = (File) new File((FMLPaths.GAMEDIR.get().toString() + "/config/"), File.separator + "orefishconfig.json");
		{
			try {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(configfile));
				StringBuilder jsonstringbuilder = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					jsonstringbuilder.append(line);
				}
				bufferedReader.close();
				jsonobj = new Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				if (!(new Object() {
					boolean check(Entity _entity) {
						if (_entity instanceof LivingEntity) {
							Collection<EffectInstance> effects = ((LivingEntity) _entity).getActivePotionEffects();
							for (EffectInstance effect : effects) {
								if (effect.getPotion() == GrowingPotionEffect.potion)
									return true;
							}
						}
						return false;
					}
				}.check(entity))) {
					if (sourceentity instanceof PlayerEntity) {
						ItemStack _setstack = new ItemStack(ArditeScaleItem.block);
						_setstack.setCount((int) 1);
						ItemHandlerHelper.giveItemToPlayer(((PlayerEntity) sourceentity), _setstack);
					}
					if (entity instanceof LivingEntity)
						((LivingEntity) entity).addPotionEffect(new EffectInstance(GrowingPotionEffect.potion,
								(int) jsonobj.get("base_growth_time_ticks").getAsDouble(), (int) 1, (false), (false)));
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
