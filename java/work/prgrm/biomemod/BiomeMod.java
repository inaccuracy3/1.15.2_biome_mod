package work.prgrm.biomemod;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.*;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import work.prgrm.biomemod.init.BiomeRegister;
import work.prgrm.biomemod.init.ModBiome;
import work.prgrm.biomemod.init.ModEntityType;
import work.prgrm.biomemod.world.biome.FragileBiome;
import work.prgrm.biomemod.world.biome.IronBiome;

@Mod(BiomeMod.MOD_ID)
@Mod.EventBusSubscriber(modid = BiomeMod.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class BiomeMod {
    public static final String MOD_ID = "biomemod";
    public BiomeMod(){
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::loadComplete);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBiome.BIOMES.register(modEventBus);
        ModEntityType.ENTITY_TYPES.register(modEventBus);
    }
    private void setup(final FMLCommonSetupEvent event){
        BiomeRegister.addBiomes(BiomeRegister.IRON_BIOME, BiomeManager.BiomeType.COOL,10);
        BiomeRegister.addBiomes(BiomeRegister.FRAGILE_BIOME, BiomeManager.BiomeType.DESERT,10);

    }

    private void clientSetup(final FMLClientSetupEvent event){
    }

    private void loadComplete(final FMLLoadCompleteEvent event){
        IronBiome.generate((IronBiome) ModBiome.IRON_BIOME.get(),EntityClassification.MONSTER,new Biome.SpawnListEntry(ModEntityType.CREEPER_MAN.get(),100,1,2));
        FragileBiome.generate((FragileBiome) ModBiome.FRAGILE_BIOME.get(),EntityClassification.MONSTER,new Biome.SpawnListEntry(ModEntityType.SLEEPER.get(),100,3,3));
    }


}
