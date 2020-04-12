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
import org.graalvm.compiler.core.common.type.ArithmeticOpTable;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.entity.mob.CreeperManEntity;

public class ModEntityType{
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, BiomeMod.MOD_ID);
    public static final RegistryObject<EntityType<CreeperManEntity>> CREEPER_MAN = ENTITY_TYPES.register("creeper_man",() ->
            EntityType.Builder.<CreeperManEntity>create(CreeperManEntity::new,EntityClassification.MONSTER)
                    .setTrackingRange(64)
                    .setCustomClientFactory(CreeperManEntity::new)
                    .size(0.6f,2.9f)
                    .build(new ResourceLocation(BiomeMod.MOD_ID,"creeper_man").toString()));
    public static final EntitySpawnPlacementRegistry SPAWN_PLACEMENTS = new EntitySpawnPlacementRegistry();
}
