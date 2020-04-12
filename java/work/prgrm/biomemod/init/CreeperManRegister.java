package work.prgrm.biomemod.init;


import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import work.prgrm.biomemod.BiomeMod;
import work.prgrm.biomemod.entity.mob.CreeperManEntity;
import work.prgrm.biomemod.entity.renderer.CreeperManRenderer;

import static work.prgrm.biomemod.util.InjectionUtil.Null;

@ObjectHolder(BiomeMod.MOD_ID)
public class CreeperManRegister {

//    public static final EntityType<CreeperManEntity> CREEPER_MAN =
//            EntityType.Builder.<CreeperManEntity>create(CreeperManEntity::new,EntityClassification.MONSTER)
//            .setCustomClientFactory(CreeperManEntity::new)
//            .setTrackingRange(64)
//            .size(0.6f,2.9f)
//            .build(BiomeMod.MOD_ID + ":creeper_man");

//    private void render(){
//        RenderingRegistry.registerEntityRenderingHandler(CREEPER_MAN, new IRenderFactory<CreeperManEntity>() {
//            @Override
//            public EntityRenderer<? super CreeperManEntity> createRenderFor(EntityRendererManager manager) {
//                return new CreeperManRenderer(manager);
//            }
//        });
//    }
//    @Mod.EventBusSubscriber(modid = BiomeMod.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
//    public static class Register{
//
//        @SubscribeEvent
//                public static void registerEntities(final RegistryEvent.Register<EntityType<?>> event){
//            final EntityType<?>[] entities = {
//                    CREEPER_MAN.setRegistryName("creeper_man")
//            };
//            event.getRegistry().registerAll(entities);
//        }
//
//    }


}
