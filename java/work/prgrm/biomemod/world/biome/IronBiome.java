package work.prgrm.biomemod.world.biome;


import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class IronBiome extends Biome {
    public IronBiome() {
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.DEFAULT,
                new SurfaceBuilderConfig(Blocks.IRON_BLOCK.getDefaultState(),
                        Blocks.REDSTONE_BLOCK.getDefaultState(),
                        Blocks.DIAMOND_ORE.getDefaultState()))
                .precipitation(RainType.RAIN)
                .category(Category.PLAINS)
                .depth(0.125f)
                .scale(0.05f)
                .temperature(0.0f)
                .downfall(0.5f)
                .waterColor(0x7eb377)
                .waterFogColor(0x364a31)
                );

        this.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/taiga/town_centers", 6)));

        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addExtraEmeraldOre(this);
        this.addSpawn(EntityClassification.MONSTER,new SpawnListEntry(EntityType.SKELETON,100,5,5));
        this.addSpawn(EntityClassification.MONSTER,new SpawnListEntry(EntityType.ZOMBIE,100,5,5));

    }
    public static void generate(IronBiome biome,EntityClassification classification, SpawnListEntry entry){
        biome.addSpawn(classification,entry);
    }
}