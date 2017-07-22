/*    */ package williamle.drones.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.PlayerCapabilities;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ public class DroneAIAttackMelee extends EntityAIBase
/*    */ {
/*    */   public Random rnd;
/*    */   public EntityDroneMob drone;
/*    */   
/*    */   public DroneAIAttackMelee(EntityDroneMob m)
/*    */   {
/* 19 */     this.drone = m;
/* 20 */     this.rnd = new Random();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75250_a()
/*    */   {
/* 26 */     return this.drone.getDroneAttackTarget() != null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75249_e()
/*    */   {
/* 32 */     super.func_75249_e();
/* 33 */     this.drone.droneInfo.switchModule(this.drone, this.drone.droneInfo.getModuleWithFunctionOf(Module.weapon1), true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75251_c()
/*    */   {
/* 39 */     super.func_75251_c();
/* 40 */     this.drone.droneInfo.switchModule(this.drone, this.drone.droneInfo.getModuleWithFunctionOf(Module.weapon1), false);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75246_d()
/*    */   {
/* 46 */     super.func_75246_d();
/* 47 */     Entity target = this.drone.getDroneAttackTarget();
/* 48 */     this.drone.flyTo(williamle.drones.api.helpers.EntityHelper.getCenterVec(target), 0.0D, 1.0D);
/* 49 */     if (((target instanceof EntityPlayer)) && (((EntityPlayer)target).field_71075_bZ.field_75102_a))
/*    */     {
/* 51 */       this.drone.setDroneAttackTarget(null, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\ai\DroneAIAttackMelee.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */