package com.oreoredstone.orefish.procedures;

import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.common.MinecraftForge;

import net.minecraft.world.World;
import net.minecraft.world.IWorld;
import net.minecraft.util.DamageSource;
import net.minecraft.potion.EffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import java.util.Map;
import java.util.Collection;

import java.io.IOException;
import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;

import com.oreoredstone.orefish.potion.GrowingPotionEffect;
import com.oreoredstone.orefish.item.DirtScaleItem;
import com.oreoredstone.orefish.OreFishMod;

import com.google.gson.Gson;

public class DirtFishTickProcedure
{

    public static void executeProcedure(Map<String, Object> dependencies)
    {
        if (dependencies.get("world") == null)
        {
            if (!dependencies.containsKey("world"))
                OreFishMod.LOGGER.warn("Failed to load dependency world for procedure DirtFishTick!");
            return;
        }
        if (dependencies.get("entity") == null)
        {
            if (!dependencies.containsKey("entity"))
                OreFishMod.LOGGER.warn("Failed to load dependency entity for procedure DirtFishTick!");
            return;
        }
        IWorld world = (IWorld)dependencies.get("world");
        Entity entity = (Entity)dependencies.get("entity");
        File configfile = new File("");
        com.google.gson.JsonObject jsonobj = new com.google.gson.JsonObject();
        ItemStack item = ItemStack.EMPTY;
        configfile = (File)new File((FMLPaths.GAMEDIR.get().toString() + "/config/"), File.separator + "orefishconfig.json");
        {
            try
            {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(configfile));
                StringBuilder jsonstringbuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    jsonstringbuilder.append(line);
                }
                bufferedReader.close();
                jsonobj = new Gson().fromJson(jsonstringbuilder.toString(), com.google.gson.JsonObject.class);
				//OreFishMod.LOGGER.warn("Loading times...");
				int growtime = (int)jsonobj.get("base_growth_time_ticks").getAsDouble();
    //OreFishMod.LOGGER.warn("Loaded growth times...");
    int droptime = (int)jsonobj.get("base_drop_time_ticks").getAsDouble();
				//OreFishMod.LOGGER.warn("Loaded times...");
				if (!(new Object()
    {
        boolean check(Entity _entity)
        {
            if (_entity instanceof LivingEntity) {
            Collection<EffectInstance> effects = ((LivingEntity)_entity).getActivePotionEffects();
            for (EffectInstance effect : effects)
            {
                if (effect.getPotion() == GrowingPotionEffect.potion)
                    return true;
            }
        }
        return false;
    }
}.check(entity))) {
    new Object()
    {

                        private int ticks = 0;
private float waitTicks;
private IWorld world;

public void start(IWorld world, int waitTicks)
{
    this.waitTicks = waitTicks;
    MinecraftForge.EVENT_BUS.register(this);
    this.world = world;
}
@SubscribeEvent
                        public void tick(TickEvent.ServerTickEvent event)
{
    if (event.phase == TickEvent.Phase.END) {
    this.ticks += 1;
    if (this.ticks >= this.waitTicks)
        run();
}
						}

						private void run()
{
    if (!(new Object() {
                                boolean check(Entity _entity) {

                                    if (_entity instanceof LivingEntity) {
    Collection<EffectInstance> effects = ((LivingEntity)_entity).getActivePotionEffects();
    for (EffectInstance effect : effects)
    {
        if (effect.getPotion() == GrowingPotionEffect.potion)
            return true;
    }
}
return false;
								}
							}.check(entity))) {
    if (entity instanceof LivingEntity)
									((LivingEntity)entity).addPotionEffect(new EffectInstance(GrowingPotionEffect.potion,
                                            growtime, (int)1, (false), (false)));
    if (world instanceof World && !world.isRemote()) {
        ItemEntity entityToSpawn = new ItemEntity((World)world, entity.lastTickPosX, entity.lastTickPosY, entity.lastTickPosZ, new ItemStack(DirtScaleItem.block));
        entityToSpawn.setPickupDelay((int)0);
        world.addEntity(entityToSpawn);
    }
}
MinecraftForge.EVENT_BUS.unregister(this);
						}
					}.start(world, droptime);
				}
				if (!entity.isInWaterRainOrBubbleColumn())
{
    new Object()
    {

                        private int ticks = 0;
private float waitTicks;
private IWorld world;

public void start(IWorld world, int waitTicks)
{
    this.waitTicks = waitTicks;
    MinecraftForge.EVENT_BUS.register(this);
    this.world = world;
}
@SubscribeEvent
                        public void tick(TickEvent.ServerTickEvent event)
{
    if (event.phase == TickEvent.Phase.END) {
    this.ticks += 1;
    if (this.ticks >= this.waitTicks)
        run();
}
						}

						private void run()
{
    if (!entity.isInWaterRainOrBubbleColumn())
    {
        entity.attackEntityFrom(DamageSource.DRYOUT, (float)1);
    }
    MinecraftForge.EVENT_BUS.unregister(this);
}
					}.start(world, (int)140);
				}

			} catch (IOException e)
{
    e.printStackTrace();
}
		}
	}
}
