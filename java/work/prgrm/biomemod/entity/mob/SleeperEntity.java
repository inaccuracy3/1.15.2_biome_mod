package work.prgrm.biomemod.entity.mob;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraftforge.fml.network.FMLPlayMessages;
import work.prgrm.biomemod.init.ModEntityType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SleeperEntity extends MonsterEntity implements IMob {
    private static final DataParameter<Integer> SLEEPER_SIZE = EntityDataManager.createKey(SleeperEntity.class, DataSerializers.VARINT);
    private static final DataParameter<Integer> STATE = EntityDataManager.createKey(SleeperEntity.class,DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.createKey(SleeperEntity.class,DataSerializers.BOOLEAN);
    public float squishAmount;
    public float squishFactor;
    public float prevSquishFactor;
    private boolean wasOnGround;
    private int timeSinceIgnited;
    private int fuseTime = 30;

    public SleeperEntity(final EntityType<? extends SleeperEntity> type, final World worldIn){
        super(type,worldIn);
        this.moveController = new SleeperEntity.MoveHelperController(this);
    }

    public SleeperEntity(FMLPlayMessages.SpawnEntity spawnEntity,World world){
        super(ModEntityType.SLEEPER.get(),world);
    }

    protected void registerGoals(){
        this.goalSelector.addGoal(1,new SleeperGoals.Float(this));
        this.goalSelector.addGoal(2,new SleeperGoals.Swell(this));
        this.goalSelector.addGoal(2,new SleeperGoals.Attack(this));
        this.goalSelector.addGoal(3,new SleeperGoals.FaceRandom(this));
        this.goalSelector.addGoal(5,new SleeperGoals.Hop(this));
        this.targetSelector.addGoal(1,new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this, PigEntity.class,10,true,false,(p_213811_1_) -> Math.abs(p_213811_1_.getPosY() - this.getPosY()) <= 4.0d));
        this.targetSelector.addGoal(2,new NearestAttackableTargetGoal<>(this, PlayerEntity.class,10,true,false,(p_213811_1_) -> Math.abs(p_213811_1_.getPosY() - this.getPosY()) <= 4.0d));
        this.targetSelector.addGoal(3,new NearestAttackableTargetGoal<>(this, IronGolemEntity.class,true));
    }

    protected void registerData(){
        super.registerData();
        this.dataManager.register(SLEEPER_SIZE,1);
        this.dataManager.register(STATE,-1);
        this.dataManager.register(IGNITED,false);
    }

    protected void setSleeperSize(int size,boolean resetHealth){
        this.dataManager.set(SLEEPER_SIZE,size);
        this.recenterBoundingBox();
        this.recalculateSize();
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue((size * size));
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue((0.2F + 0.1F * (float)size));
        if(resetHealth){
            this.setHealth(this.getMaxHealth());
        }
        this.experienceValue = size + 1;
    }

    public int getSleeperSize(){return this.dataManager.get(SLEEPER_SIZE);}

    public void writeAdditional(CompoundNBT compound){
        super.writeAdditional(compound);
        compound.putInt("Size", this.getSleeperSize() - 1);
        compound.putBoolean("wasOnGround",this.wasOnGround);
    }


    public void readAdditional(CompoundNBT compound){
        int i = compound.getInt("Size");
        if(i < 0){
            i = 0;
        }
        this.setSleeperSize(i + 1,false);
        super.readAdditional(compound);
        this.wasOnGround = compound.getBoolean("wasOnGround");
    }

    public boolean isSmallSleeper(){ return this.getSleeperSize() <= 1;}

    protected IParticleData getSquishParticle(){ return ParticleTypes.ITEM_SLIME;}

    protected boolean isDespawnPeaceful(){ return this.getSleeperSize() > 0;}

    public void tick(){
        this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5f;
        this.prevSquishFactor = this.squishFactor;
        super.tick();
        if(this.onGround && !this.wasOnGround){
            int i = this.getSleeperSize();

            if(spawnCustomParticles()) i = 0;
            for(int j = 0; j < i; ++j){
                float f = this.rand.nextFloat() * ((float)Math.PI * 2f);
                float f1 = this.rand.nextFloat() * 0.5f + 0.5f;
                float f2 = MathHelper.sin(f) * (float)i * 0.5f * f1;
                float f3 = MathHelper.cos(f) * (float)i * 0.5f * f1;
                this.world.addParticle(this.getSquishParticle(),this.getPosX() + (double)f2,this.getPosY(),this.getPosZ() + (double)f3,0.0d,0.0d,0.0d);
            }
            this.playSound(this.getSquishSound(),this.getSoundVolume(),((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2f + 0.1f) / 0.8f);
            this.squishAmount = -0.5f;
        }else if(!this.onGround && this.wasOnGround){
            this.squishAmount = 1.0f;
        }
        this.wasOnGround = this.onGround;
        this.alterSquishAmount();
        if(this.isAlive()){
            if(this.getAttackTarget() != null && this.canEntityBeSeen(this.getAttackTarget())&&this.getAttackTarget().getDistanceSq(this)<9.0D&&this.onGround){
                this.hasIgnited();
            }
            if(this.hasIgnited()){
                this.setState(1);
            }
            int i = this.getState();
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
    }

    protected void alterSquishAmount(){ this.squishAmount *= 0.6f;}

    protected int getJumpDelay(){ return this.rand.nextInt(20) + 30;}

    public void recalculateSize(){
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        super.recalculateSize();
        this.setPosition(d0,d1,d2);
    }

    public void notifyDataManagerChange(@Nonnull DataParameter<?> key){
        if(SLEEPER_SIZE.equals(key)){
            this.recalculateSize();
            this.rotationYaw = this.rotationYawHead;
            this.renderYawOffset = this.rotationYawHead;
            if(this.isInWater() && this.rand.nextInt(20) == 0){
                this.doWaterSplashEffect();
            }
        }
        super.notifyDataManagerChange(key);
    }
    @Nonnull
    public EntityType<?> getType() {
        return super.getType();
    }



    @Override
    public void remove(boolean keepData){
        int i = this.getSleeperSize();
        if(!this.world.isRemote && i > 1 && this.getHealth() <= 0.0f){
            int j = 2 + this.rand.nextInt(3);
            for(int k = 0;k < j; ++k){
                float f = ((float)(k % 2) - 0.5f) * (float)i / 4.0f;
                float f1 = ((float)(k / 2) - 0.5f) * (float)i / 4.0f;
                SleeperEntity entity = (SleeperEntity)this.getType().create(this.world);
                if(this.hasCustomName()){
                    assert entity != null;
                    entity.setCustomName(this.getCustomName());
                }

                if(this.isNoDespawnRequired()){
                    assert entity != null;
                    entity.enablePersistence();
                }
                assert entity != null;
                entity.setInvulnerable(this.isInvulnerable());
                entity.setSleeperSize( i / 2,true);
                entity.setLocationAndAngles(this.getPosX() + (double)f,this.getPosY() + 0.5d,this.getPosZ()+(double)f1,this.rand.nextFloat() * 360f,0.0f);
                this.world.addEntity(entity);
            }
        }
        super.remove(keepData);
    }
    private void explode(){
        if(!this.world.isRemote){
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world,this) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            this.world.createExplosion(this,this.getPosX(),this.getPosY(),this.getPosZ(),(float)this.getSleeperSize(),explosion$mode);
            this.setHealth(0.0f);
        }
    }
    protected boolean canDamagePlayer(){
        return !this.isSmallSleeper() && this.isServerWorld();
    }

    protected SoundEvent getHurtSound(DamageSource damageSource){
        return this.isSmallSleeper() ? SoundEvents.ENTITY_CREEPER_HURT : SoundEvents.ENTITY_SLIME_HURT;
    }

    protected SoundEvent getDeathSound(){
        return this.isSmallSleeper() ? SoundEvents.ENTITY_CREEPER_DEATH : SoundEvents.ENTITY_SLIME_DEATH;
    }

    protected SoundEvent getSquishSound(){
        return this.isSmallSleeper() ? SoundEvents.ENTITY_SLIME_SQUISH_SMALL: SoundEvents.ENTITY_SLIME_SQUISH;
    }

    @Nonnull
    protected ResourceLocation getLootTable(){
        return this.getSleeperSize() == 1 ? this.getType().getLootTable(): LootTables.EMPTY;
    }

    protected float getSoundVolume(){ return 0.4f * (float)this.getSleeperSize();}

    public int getVerticalFaceSpeed(){ return 0;}

    protected boolean makesSoundOnJump(){
        return this.getSleeperSize() > 0;
    }

    protected void jump(){
        Vec3d vec3d = this.getMotion();
        this.setMotion(vec3d.x,this.getJumpUpwardsMotion(),vec3d.z);
        this.isAirBorne = true;
    }

    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance diff, SpawnReason reason,@Nullable ILivingEntityData spawnDataIn,@Nullable CompoundNBT dataTag){
        int i = this.rand.nextInt(3);
        if(i < 2&& this.rand.nextFloat() < 0.5f * diff.getClampedAdditionalDifficulty()){
            ++i;
        }
        int j = 1 << i;
        this.setSleeperSize(j,true);
        return super.onInitialSpawn(worldIn,diff,reason,spawnDataIn,dataTag);
    }

    protected SoundEvent getJumpSound(){
        return this.isSmallSleeper() ? SoundEvents.ENTITY_SLIME_JUMP_SMALL : SoundEvents.ENTITY_SLIME_JUMP;
    }

    @Nonnull
    public EntitySize getSize(Pose poseIn){ return super.getSize(poseIn).scale(0.25f * (float)this.getSleeperSize());}

    protected boolean spawnCustomParticles(){ return false;}

    public int getState(){ return this.dataManager.get(STATE);}

    public void setState(int state){this.dataManager.set(STATE,state);}

    public boolean hasIgnited(){return this.dataManager.get(IGNITED);}

    public void ignite(){this.dataManager.set(IGNITED,true);}

    static class MoveHelperController extends MovementController{
        private float yRot;
        private int jumpDelay;
        private final SleeperEntity sleeper;
        private boolean isAggressive;

        public MoveHelperController(SleeperEntity entity){
            super(entity);
            this.sleeper = entity;
            this.yRot = 180.0f * entity.rotationYaw / (float)Math.PI;
        }
        public void setDirection(float yRotIn, boolean aggressive){
            this.yRot = yRotIn;
            this.isAggressive = aggressive;
        }

        public void setSpeed(double speedIn){
            this.speed = speedIn;
            this.action = Action.MOVE_TO;
        }

        public void tick(){
            this.mob.rotationYaw = this.limitAngle(this.mob.rotationYaw,this.yRot,90f);
            this.mob.rotationYawHead = this.mob.rotationYaw;
            this.mob.renderYawOffset = this.mob.rotationYaw;
            if(this.action != Action.MOVE_TO){
                this.mob.setMoveForward(0.0f);
            }else{
                this.action = Action.WAIT;
                if(this.mob.onGround){
                    this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                    if(this.jumpDelay-- <= 0){
                        this.jumpDelay = this.sleeper.getJumpDelay();
                        if(this.isAggressive){
                            this.jumpDelay /= 3;
                        }

                        this.sleeper.getJumpController().setJumping();
                        if(this.sleeper.makesSoundOnJump()){
                            this.sleeper.playSound(this.sleeper.getJumpSound(),this.sleeper.getSoundVolume(),((this.sleeper.getRNG().nextFloat() - this.sleeper.getRNG().nextFloat()) * 0.2f + 1.0f) *0.8f);
                        }
                    } else {
                        this.sleeper.moveStrafing = 0.0f;
                        this.sleeper.moveForward = 0.0f;
                        this.mob.setAIMoveSpeed(0.0f);
                    }
                } else {
                    this.mob.setAIMoveSpeed((float)(this.speed * this.mob.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getValue()));
                }
            }
        }
    }

}
