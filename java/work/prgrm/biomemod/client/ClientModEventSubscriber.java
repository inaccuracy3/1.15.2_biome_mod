package work.prgrm.biomemod.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.entity.mob.CreeperManEntity;
import work.prgrm.biomemod.entity.renderer.CreeperManRenderer;
import work.prgrm.biomemod.entity.renderer.SleeperRenderer;
import work.prgrm.biomemod.init.ModEntityType;

@Mod.EventBusSubscriber(modid = BiomeMod.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD,value = Dist.CLIENT)
public final class ClientModEventSubscriber {
    @SubscribeEvent
    public static void FMLClientSetupEvent(final FMLClientSetupEvent event){
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.CREEPER_MAN.get(), CreeperManRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntityType.SLEEPER.get(), SleeperRenderer::new);
    }
}
