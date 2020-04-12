package work.prgrm.biomemod.world.biome;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;

public class FragileBiome extends Biome {
    public FragileBiome(){
        super((new Biome.Builder()).surfaceBuilder(SurfaceBuilder.SWAMP,
                new SurfaceBuilderConfig(Blocks.SLIME_BLOCK.getDefaultState(), Blocks.SLIME_BLOCK.getDefaultState(), Blocks.SLIME_BLOCK.getDefaultState()))
        .precipitation(RainType.RAIN)
        .category(Category.RIVER)
        .depth(0.1f)
        .downfall(0.3f)
        .temperature(0.4f)
        .scale(0.05f)
        .waterColor(0xaeeb34)
        .waterFogColor(0xaeeb34)
        );
        this.addStructure(Feature.VILLAGE.withConfiguration(new VillageConfig("village/desert/town_centers", 6)));

        DefaultBiomeFeatures.addLakes(this);
        DefaultBiomeFeatures.addMonsterRooms(this);
        DefaultBiomeFeatures.addExtraGoldOre(this);
        DefaultBiomeFeatures.addOres(this);
        DefaultBiomeFeatures.addCarvers(this);
        DefaultBiomeFeatures.addStructures(this);

        this.addSpawn(EntityClassification.MONSTER,new SpawnListEntry(EntityType.SLIME,100,4,4));
    }
    public static void generate(FragileBiome biome,EntityClassification classification,SpawnListEntry entry){
        biome.addSpawn(classification,entry);
    }
}
