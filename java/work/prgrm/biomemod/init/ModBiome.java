package work.prgrm.biomemod.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.world.biome.FragileBiome;
import work.prgrm.biomemod.world.biome.IronBiome;

public class ModBiome {
    public static final DeferredRegister<Biome> BIOMES = new DeferredRegister<>(ForgeRegistries.BIOMES, BiomeMod.MOD_ID);
    public static final RegistryObject<Biome> IRON_BIOME = BIOMES.register("iron_biome", IronBiome::new);
    public static final RegistryObject<Biome> FRAGILE_BIOME = BIOMES.register("fragile_biome", FragileBiome::new);


}
