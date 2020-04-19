package work.prgrm.biomemod.init;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.entity.mob.CreeperManEntity;
import work.prgrm.biomemod.entity.mob.SleeperEntity;

public class ModEntityType{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BiomeMod.MOD_ID);
    public static final RegistryObject<EntityType<CreeperManEntity>> CREEPER_MAN = ENTITY_TYPES.register("creeper_man",() ->
            EntityType.Builder.<CreeperManEntity>create(CreeperManEntity::new,EntityClassification.MONSTER)
                    .setTrackingRange(64)
                    .setCustomClientFactory(CreeperManEntity::new)
                    .size(0.6f,2.9f)
                    .build(new ResourceLocation(BiomeMod.MOD_ID,"creeper_man").toString()));
    public static final RegistryObject<EntityType<SleeperEntity>> SLEEPER = ENTITY_TYPES.register("sleeper",() ->
            EntityType.Builder.<SleeperEntity>create(SleeperEntity::new,EntityClassification.MONSTER)
                    .setTrackingRange(64)
                    .setCustomClientFactory(SleeperEntity::new)
                    .size(2.04f,2.04f)
                    .build(new ResourceLocation(BiomeMod.MOD_ID,"sleeper").toString()));
}
