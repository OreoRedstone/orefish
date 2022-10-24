package com.oreoredstone.orefish.entity;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.ForgeMod;

import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.World;
import net.minecraft.world.IWorldReader;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Hand;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ActionResultType;
import net.minecraft.pathfinding.SwimmerPathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.network.IPacket;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Item;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.entity.ai.goal.PanicGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.FollowMobGoal;
import net.minecraft.entity.ai.goal.BreedGoal;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.Entity;
import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.BlockState;

import java.util.stream.Stream;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap;

import com.oreoredstone.orefish.procedures.ManyllyumFishTickProcedure;
import com.oreoredstone.orefish.procedures.ManyllyumFishRightClickProcedure;
import com.oreoredstone.orefish.item.ManyllyumScaleItem;
import com.oreoredstone.orefish.entity.renderer.ManyllyumFishRenderer;
import com.oreoredstone.orefish.OreFishModElements;

@OreFishModElements.ModElement.Tag
public class ManyllyumFishEntity extends OreFishModElements.ModElement
{

    public static EntityType entity = (EntityType.Builder.< CustomEntity > create(CustomEntity::new, EntityClassification.WATER_CREATURE)
            .setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(CustomEntity::new)
            .size(0.5f, 0.3f)).build("manyllyum_fish").setRegistryName("manyllyum_fish");

public ManyllyumFishEntity(OreFishModElements instance)
{
    super(instance, 15);
    FMLJavaModLoadingContext.get().getModEventBus().register(new ManyllyumFishRenderer.ModelRegisterHandler());
    FMLJavaModLoadingContext.get().getModEventBus().register(new EntityAttributesRegisterHandler());
    MinecraftForge.EVENT_BUS.register(this);
}
@Override
    public void initElements()
{
    elements.entities.add(()->entity);
    elements.items.add(()-> new SpawnEggItem(entity, -1, -1, new Item.Properties().group(ItemGroup.MISC)).setRegistryName("manyllyum_fish_spawn_egg"));
}

@SubscribeEvent
    public void addFeatureToBiomes(BiomeLoadingEvent event)
{
		event.getSpawns().getSpawner(EntityClassification.WATER_CREATURE).add(new MobSpawnInfo.Spawners(entity, 20, 4, 4));
}

@Override
    public void init(FMLCommonSetupEvent event)
{
    EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
            SquidEntity::func_223365_b);
}

private static class EntityAttributesRegisterHandler
{
    @SubscribeEvent
    public void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        AttributeModifierMap.MutableAttribute ammma = MobEntity.func_233666_p_();
        ammma = ammma.createMutableAttribute(Attributes.MOVEMENT_SPEED, 2);
        ammma = ammma.createMutableAttribute(Attributes.MAX_HEALTH, 10);
        ammma = ammma.createMutableAttribute(Attributes.ARMOR, 0);
        ammma = ammma.createMutableAttribute(Attributes.ATTACK_DAMAGE, 3);
        ammma = ammma.createMutableAttribute(Attributes.FOLLOW_RANGE, 16);
        ammma = ammma.createMutableAttribute(ForgeMod.SWIM_SPEED.get(), 2);

            event.put(entity, ammma.create());
		}
	}

	public static class CustomEntity extends AnimalEntity
{

        public CustomEntity(FMLPlayMessages.SpawnEntity packet, World world)
{
    this(entity, world);
}

public CustomEntity(EntityType<CustomEntity> type, World world)
{
    super(type, world);
    experienceValue = 0;
    setNoAI(false);
    enablePersistence();
    this.setPathPriority(PathNodeType.WATER, 0);
    this.moveController = new MovementController(this) {
                @Override

                public void tick()
    {
        if (CustomEntity.this.isInWater())
						CustomEntity.this.setMotion(CustomEntity.this.getMotion().add(0, 0.005, 0));
if (this.action == MovementController.Action.MOVE_TO && !CustomEntity.this.getNavigator().noPath()) {
    double dx = this.posX - CustomEntity.this.getPosX();
    double dy = this.posY - CustomEntity.this.getPosY();
    double dz = this.posZ - CustomEntity.this.getPosZ();
    float f = (float)(MathHelper.atan2(dz, dx) * (double)(180 / Math.PI)) - 90;
    float f1 = (float)(this.speed * CustomEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
    CustomEntity.this.rotationYaw = this.limitAngle(CustomEntity.this.rotationYaw, f, 10);
    CustomEntity.this.renderYawOffset = CustomEntity.this.rotationYaw;
    CustomEntity.this.rotationYawHead = CustomEntity.this.rotationYaw;
    if (CustomEntity.this.isInWater()) {
        CustomEntity.this.setAIMoveSpeed((float)CustomEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
        float f2 = -(float)(MathHelper.atan2(dy, MathHelper.sqrt(dx * dx + dz * dz)) * (180F / Math.PI));
        f2 = MathHelper.clamp(MathHelper.wrapDegrees(f2), -85, 85);
        CustomEntity.this.rotationPitch = this.limitAngle(CustomEntity.this.rotationPitch, f2, 5);
        float f3 = MathHelper.cos(CustomEntity.this.rotationPitch * (float)(Math.PI / 180.0));
        CustomEntity.this.setMoveForward(f3 * f1);
        CustomEntity.this.setMoveVertical((float)(f1 * dy));
    } else
    {
        CustomEntity.this.setAIMoveSpeed(f1 * 0.05F);
    }
} else
{
    CustomEntity.this.setAIMoveSpeed(0);
    CustomEntity.this.setMoveVertical(0);
    CustomEntity.this.setMoveForward(0);
}
				}
			};
this.navigator = new SwimmerPathNavigator(this, this.world);
		}

		@Override
        public IPacket<?> createSpawnPacket()
{
    return NetworkHooks.getEntitySpawningPacket(this);
}

@Override
        protected void registerGoals()
{
    super.registerGoals();
    this.goalSelector.addGoal(1, new BreedGoal(this, 1));
    this.goalSelector.addGoal(2, new TemptGoal(this, 1, Ingredient.fromItems(Blocks.KELP.asItem()), false));
    this.goalSelector.addGoal(3, new PanicGoal(this, 2));
    this.goalSelector.addGoal(4, new FollowMobGoal(this, (float)1, 10, 5));
    this.goalSelector.addGoal(5, new RandomSwimmingGoal(this, 1, 40));
    this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
}

@Override
        public CreatureAttribute getCreatureAttribute()
{
    return CreatureAttribute.WATER;
}
@Override
        public boolean canDespawn(double distanceToClosestPlayer)
{
    return false;
}

protected void dropSpecialItems(DamageSource source, int looting, boolean recentlyHitIn)
{
    super.dropSpecialItems(source, looting, recentlyHitIn);
    this.entityDropItem(new ItemStack(ManyllyumScaleItem.block));
}

@Override
        public net.minecraft.util.SoundEvent getAmbientSound()
{
    return (net.minecraft.util.SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.cod.ambient"));
}

@Override
        public void playStepSound(BlockPos pos, BlockState blockIn)
{
    this.playSound((net.minecraft.util.SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.fish.swim")), 0.15f, 1);
}

@Override
        public net.minecraft.util.SoundEvent getHurtSound(DamageSource ds)
{
    return (net.minecraft.util.SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.hurt"));
}

@Override
        public net.minecraft.util.SoundEvent getDeathSound()
{
    return (net.minecraft.util.SoundEvent)ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.generic.death"));
}

@Override
        public boolean attackEntityFrom(DamageSource source, float amount)
{
    if (source == DamageSource.DROWN)
        return false;
    return super.attackEntityFrom(source, amount);
}

@Override
        public ActionResultType func_230254_b_(PlayerEntity sourceentity, Hand hand)
{
    ItemStack itemstack = sourceentity.getHeldItem(hand);
    ActionResultType retval = ActionResultType.func_233537_a_(this.world.isRemote());
    super.func_230254_b_(sourceentity, hand);
    double x = this.getPosX();
    double y = this.getPosY();
    double z = this.getPosZ();
    Entity entity = this;

    ManyllyumFishRightClickProcedure.executeProcedure(
            Stream.of(new AbstractMap.SimpleEntry<>("entity", entity), new AbstractMap.SimpleEntry<>("sourceentity", sourceentity))
                    .collect(HashMap::new, (_m, _e)->_m.put(_e.getKey(), _e.getValue()), Map::putAll));
    return retval;
}

@Override
        public void baseTick()
{
    super.baseTick();
    double x = this.getPosX();
    double y = this.getPosY();
    double z = this.getPosZ();
    Entity entity = this;

    ManyllyumFishTickProcedure.executeProcedure(Stream
            .of(new AbstractMap.SimpleEntry<>("world", world), new AbstractMap.SimpleEntry<>("x", x), new AbstractMap.SimpleEntry<>("y", y),
                    new AbstractMap.SimpleEntry<>("z", z), new AbstractMap.SimpleEntry<>("entity", entity))
            .collect(HashMap::new, (_m, _e)->_m.put(_e.getKey(), _e.getValue()), Map::putAll));
}

@Override
        public AgeableEntity func_241840_a(ServerWorld serverWorld, AgeableEntity ageable)
{
    CustomEntity retval = (CustomEntity)entity.create(serverWorld);
    retval.onInitialSpawn(serverWorld, serverWorld.getDifficultyForLocation(new BlockPos(retval.getPosition())), SpawnReason.BREEDING,
            (ILivingEntityData)null, (CompoundNBT)null);
    return retval;
}

@Override
        public boolean isBreedingItem(ItemStack stack)
{
    if (stack == null)
        return false;
    if (Blocks.KELP.asItem() == stack.getItem())
        return true;
    return false;
}
@Override
        public boolean canBreatheUnderwater()
{
    return true;
}

@Override
        public boolean isNotColliding(IWorldReader world)
{
    return world.checkNoEntityCollision(this);
}

@Override
        public boolean isPushedByWater()
{
    return false;
}
	}
}
