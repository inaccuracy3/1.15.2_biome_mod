package work.prgrm.biomemod.entity.mob;

import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effects;

import java.util.EnumSet;

public class SleeperGoals {
    static class Attack extends Goal {
        private final SleeperEntity sleeper;
        private int growTieredTimer;

        public Attack(SleeperEntity entity){
            this.sleeper = entity;
            this.setMutexFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean shouldExecute(){
            LivingEntity livingEntity = this.sleeper.getAttackTarget();
            if(livingEntity == null){
                return false;
            }else if(!livingEntity.isAlive()){
                return false;
            }else{
                return (!(livingEntity instanceof PlayerEntity) || !((PlayerEntity) livingEntity).abilities.disableDamage) && this.sleeper.getMoveHelper() instanceof SleeperEntity.MoveHelperController;
            }
        }

        public void startExecuting(){
            this.growTieredTimer = 300;
            super.startExecuting();
        }

        public void tick(){
            this.sleeper.faceEntity(this.sleeper.getAttackTarget(),10.0f,10.0f);
            ((SleeperEntity.MoveHelperController)this.sleeper.getMoveHelper()).setDirection(this.sleeper.rotationYaw,this.sleeper.canDamagePlayer());
        }
    }

    static class FaceRandom extends Goal{
        private final SleeperEntity sleeper;
        private float chosenDegress;
        private int nextRandomizeTime;

        public FaceRandom(SleeperEntity entity){
            this.sleeper = entity;
            this.setMutexFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean shouldExecute(){
            return this.sleeper.getAttackTarget() == null && (this.sleeper.onGround || this.sleeper.isInLava() || this.sleeper.isPotionActive(Effects.LEVITATION)) && this.sleeper.getMoveHelper() instanceof  SleeperEntity.MoveHelperController;
        }

        public void tick(){
            if(--this.nextRandomizeTime <= 0){
                this.nextRandomizeTime = 40 + this.sleeper.getRNG().nextInt(60);
                this.chosenDegress = (float)this.sleeper.getRNG().nextInt(360);
            }
            ((SleeperEntity.MoveHelperController)this.sleeper.getMoveHelper()).setDirection(this.chosenDegress,false);
        }
    }

    static class Float extends Goal{
        private final SleeperEntity sleeper;

        public Float(SleeperEntity entity){
            this.sleeper = entity;
            this.setMutexFlags(EnumSet.of(Flag.JUMP,Flag.MOVE));
            entity.getNavigator().setCanSwim(true);
        }

        public boolean shouldExecute(){
            return (this.sleeper.isInWater() || this.sleeper.isInLava()) && this.sleeper.getMoveHelper() instanceof SleeperEntity.MoveHelperController;
        }
        public void tick(){
            if(this.sleeper.getRNG().nextFloat() < 0.8f){
                this.sleeper.getJumpController().setJumping();
            }
            ((SleeperEntity.MoveHelperController)this.sleeper.getMoveHelper()).setSpeed(1.2D);
        }
    }

    static class Hop extends Goal{
        private final SleeperEntity sleeper;

        public Hop(SleeperEntity entity){
            this.sleeper = entity;
            this.setMutexFlags(EnumSet.of(Flag.JUMP,Flag.MOVE));
        }

        public boolean shouldExecute(){return !this.sleeper.isPassenger();}

        public void tick(){((SleeperEntity.MoveHelperController)this.sleeper.getMoveHelper()).setSpeed(1.0d);}
    }

    static class Swell extends Goal{
        private final SleeperEntity swellingSleeper;
        private LivingEntity attackTarget;
        public Swell(SleeperEntity entity){
            this.swellingSleeper = entity;
            this.setMutexFlags(EnumSet.of(Flag.MOVE));
        }
        public boolean shouldExecute(){
            LivingEntity livingEntity = this.swellingSleeper.getAttackTarget();
            return this.swellingSleeper.getState() > 0 || livingEntity != null && this.swellingSleeper.getDistanceSq(livingEntity) <= 9.0D;
        }

        public void startExecuting(){
            this.swellingSleeper.getNavigator().clearPath();
            this.attackTarget = this.swellingSleeper.getAttackTarget();
        }

        public void resetTask(){this.attackTarget = null;}

        public void tick(){
            if(this.attackTarget == null){
                this.swellingSleeper.setState(-1);
            }else if(this.swellingSleeper.getDistanceSq(this.attackTarget) > 9.0D){
                this.swellingSleeper.setState(-1);
            }else if(!this.swellingSleeper.getEntitySenses().canSee(this.attackTarget)){
                this.swellingSleeper.setState(-1);
            }else{
                this.swellingSleeper.setState(1);
            }
        }
    }

}
