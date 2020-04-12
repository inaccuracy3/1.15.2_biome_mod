package work.prgrm.biomemod.entity.mob;

import jdk.internal.jline.internal.Nullable;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.PandaEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;
import work.prgrm.biomemod.init.ModEntityType;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Predicate;

@OnlyIn(
        value = Dist.CLIENT,
        _interface = IChargeableMob.class
)
public class CreeperManEntity extends MonsterEntity implements IChargeableMob{
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(CreeperManEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> POWERED = EntityDataManager.createKey(CreeperManEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(CreeperManEntity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> field_226535_bx_ = EntityDataManager.createKey(CreeperManEntity.class, DataSerializers.BOOLEAN);
    private int explosionRadius = 6;
    private int lastActiveTime;
    private int timeSinceIgnited = 0;
    private int fuseTime = 30;
    private int teleportTime;

    public CreeperManEntity(final EntityType<? extends CreeperManEntity> type,final World worldIn) {
        super(type, worldIn);
        this.experienceValue = 114514;
        this.entityDropItem(Items.DIAMOND);
    }

    public CreeperManEntity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        super(ModEntityType.CREEPER_MAN.get(),world);
    }





    protected void registerGoals(){
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new CreeperManSwellGoal(this));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, PandaEntity.class,6.0f,1.0D,1.2D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this,1.0D,false));
        this.goalSelector.addGoal(5, new LookAtGoal(this, PlayerEntity.class,6.0f));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(5, new RandomWalkingGoal(this,0.3));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PigEntity.class,true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    protected void registerAttributes(){
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(60D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3f);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(64.00);
    }

    protected void registerData(){
        super.registerData();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(POWERED, false);
        this.dataManager.register(IGNITED, false);
    }

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        if(this.dataManager.get(POWERED)){
            compound.putBoolean("powered",true);
        }
        compound.putShort("Fuse", (short)this.fuseTime);
        compound.putByte("ExplosionRadius", (byte)this.explosionRadius);
        compound.putBoolean("ignited", this.hasIgnited());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(POWERED, compound.getBoolean("powered"));

        if (compound.contains("ExplosionRadius", 99)) {
            this.explosionRadius = compound.getByte("ExplosionRadius");
        }

        if (compound.getBoolean("ignited")) {
            this.ignite();
        }
    }


    protected void dropSpecialItems(DamageSource source,int looting,boolean recentlyHit) {
        super.dropSpecialItems(source,looting,recentlyHit);
        if(!this.hasIgnited()) {
            this.entityDropItem(Items.DIAMOND);
        }
    }


    @Nullable
    @ParametersAreNonnullByDefault
    public ItemEntity entityDropItem(IItemProvider itemIn){
        return this.entityDropItem(itemIn,0);
    }

    @OnlyIn(Dist.CLIENT)
    public float getCreeperFlashIntensity(float partialTicks) {
        return MathHelper.lerp(partialTicks, (float)this.lastActiveTime, (float)this.timeSinceIgnited) / (float)(this.fuseTime - 2);
    }

    protected void updateAITasks(){
        if(this.getAttackTarget() == null){
            float f = this.getBrightness();
            if(f > 0.5f && this.world.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F){
                this.teleportRandomly();
            }
        }
        super.updateAITasks();
    }

    public void tick(){
        if(this.isAlive()){
            if(this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())&&this.getAttackTarget().getDistanceSq(this)>9.0D){
                this.teleportToEntity(this.getAttackTarget());
                this.hasIgnited();
            }
            this.lastActiveTime = this.timeSinceIgnited;


            if(this.hasIgnited()){
                this.setCreeperManState(1);
            }

            int i = this.getCreeperManState();
            if(i > 0 && this.timeSinceIgnited == 0){
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED,1.0f,0.5f);
            }
            this.timeSinceIgnited += i;
            if(this.timeSinceIgnited < 0){
                this.timeSinceIgnited = 0;
            }

            if(this.timeSinceIgnited >= this.fuseTime){
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }
        super.tick();
    }

    protected boolean teleportRandomly(){
        if(!this.world.isRemote() && this.isAlive()){
            double d0 = getPosX() + (this.rand.nextDouble() - 0.5D) * 32.0D;
            double d1 = this.getPosY() + (double)(this.rand.nextInt(64) - 32);
            double d2 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 32.0D;
            return this.teleportTo(d0, d1, d2);
        }else{
            return false;
        }
    }

    private void teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.getPosX() - p_70816_1_.getPosX(), this.getPosYHeight(0.5D) - p_70816_1_.getPosYEye(), this.getPosZ() - p_70816_1_.getPosZ());
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.getPosY() + (double)(this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x,double y, double z) {
        BlockPos.Mutable blockPos$mutable = new BlockPos.Mutable(x, y, z);

        while (blockPos$mutable.getY() > 0 && !this.world.getBlockState(blockPos$mutable).getMaterial().blocksMovement()) {
            blockPos$mutable.move(Direction.DOWN);
        }
        BlockState blockState = this.world.getBlockState(blockPos$mutable);
        boolean flag = blockState.getMaterial().blocksMovement();
        boolean flag1 = blockState.getFluidState().isTagged(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }
            return flag2;
        }else{
            return false;
        }
    }



    protected SoundEvent getHurtSound(DamageSource damageIn){return SoundEvents.ENTITY_ENDERMAN_HURT;}

    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_CREEPER_DEATH;
    }


    @ParametersAreNonnullByDefault
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!(source instanceof IndirectEntityDamageSource) && source != DamageSource.FIREWORKS) {
            boolean flag = super.attackEntityFrom(source, amount);
            if (!this.world.isRemote() && source.isUnblockable() && this.rand.nextInt(10) != 0) {
                this.teleportRandomly();
            }
            return flag;
        } else {
            for(int i = 0; i < 64; ++i) {
                if (this.teleportRandomly()) {
                    return true;
                }
            }
            return false;
        }
    }
    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }


    public boolean func_225509_J__() {
        return this.dataManager.get(POWERED);
    }

    public boolean func_226537_et_() {
        return this.dataManager.get(field_226535_bx_);
    }

    public void func_226538_eu_() {
        this.dataManager.set(field_226535_bx_, true);
    }


    protected boolean canDropLoot(){return true;}

    public int getCreeperManState(){return this.dataManager.get(STATE);}

    public void setCreeperManState(int state){this.dataManager.set(STATE,state);}

    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket()
    {
        return NetworkHooks.getEntitySpawningPacket(this);
    }


    protected boolean processInteract(PlayerEntity player, Hand hand) {
        ItemStack itemstack = player.getHeldItem(hand);
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            this.world.playSound(player, this.getPosX(), this.getPosY(), this.getPosZ(), SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
            if (!this.world.isRemote) {
                this.ignite();
                itemstack.damageItem(1, player, (p_213625_1_) -> p_213625_1_.sendBreakAnimation(hand));
            }

            return true;
        } else {
            return super.processInteract(player, hand);
        }
    }

    private void explode(){
        if(!this.world.isRemote){
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            float f = this.func_225509_J__() ? 2.0F : 1.0F;
            this.dead = true;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), (float)this.explosionRadius * f, explosion$mode);
            this.remove();
        }
    }

    public boolean hasIgnited(){return this.dataManager.get(IGNITED);}
    public void ignite(){this.dataManager.set(IGNITED,true);}

}


