package work.prgrm.biomemod.init;


import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeEntry;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ObjectHolder;
import net.minecraftforge.registries.RegistryManager;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.world.biome.FragileBiome;
import work.prgrm.biomemod.world.biome.IronBiome;

import static work.prgrm.biomemod.util.InjectionUtil.Null;

@ObjectHolder(BiomeMod.MOD_ID)
public class BiomeRegister {
    public static final IronBiome IRON_BIOME = Null();
    public static final FragileBiome FRAGILE_BIOME = Null();

//    @Mod.EventBusSubscriber(modid = BiomeMod.MOD_ID,bus = Bus.MOD)
//    public static class Register{
//        @SubscribeEvent
//        public static void registerBiomes(final RegistryEvent.Register<Biome> event){
//            final Biome[] biomes = {
//                new AddBiome().setRegistryName(BiomeMod.MOD_ID,"add_biome")
//            };
//            event.getRegistry().registerAll(biomes);
//        }
//    }
    public static void addBiomes(Biome biome,BiomeType type,int weight){
        BiomeManager.addBiome(type,new BiomeEntry(biome,weight));
        BiomeManager.addSpawnBiome(biome);
    }
    public static void removeBiome(Biome biome){
        BiomeManager.removeSpawnBiome(biome);
    }
}
