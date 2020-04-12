package work.prgrm.biomemod.entity.mob;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;

import java.util.EnumSet;

public class CreeperManSwellGoal extends Goal {
    private final CreeperManEntity swellingCreeperMan;
    private LivingEntity attackTarget;
    public CreeperManSwellGoal(CreeperManEntity entity){
        this.swellingCreeperMan = entity;
        this.setMutexFlags(EnumSet.of(Flag.MOVE));
    }

    public boolean shouldExecute(){
        LivingEntity livingEntity = this.swellingCreeperMan.getAttackTarget();
        return this.swellingCreeperMan.getCreeperManState() > 0 || livingEntity != null && this.swellingCreeperMan.getDistanceSq(livingEntity) < 9.0D;
    }

    public void startExecuting(){
        this.swellingCreeperMan.getNavigator().clearPath();
        this.attackTarget = this.swellingCreeperMan.getAttackTarget();
    }

    public void resetTask(){this.attackTarget = null;}

    public void tick(){
        if(this.attackTarget == null){
            this.swellingCreeperMan.setCreeperManState(-1);
        }else if(this.swellingCreeperMan.getDistanceSq(this.attackTarget) > 49.0D){
            this.swellingCreeperMan.setCreeperManState(-1);
        }else if(!this.swellingCreeperMan.getEntitySenses().canSee(this.attackTarget)){
            this.swellingCreeperMan.setCreeperManState(-1);
        }else{
            this.swellingCreeperMan.setCreeperManState(1);
        }
    }
}
