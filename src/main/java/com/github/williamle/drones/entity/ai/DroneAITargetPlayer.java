/*    */ package williamle.drones.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.world.World;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DroneAITargetPlayer
/*    */   extends EntityAIBase
/*    */ {
/*    */   public EntityDroneMob drone;
/*    */   public double range;
/*    */   
/*    */   public DroneAITargetPlayer(EntityDroneMob m, double range)
/*    */   {
/* 19 */     this.drone = m;
/* 20 */     this.range = range;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75250_a()
/*    */   {
/* 26 */     return (this.drone.hostile) && (this.drone.getDroneAttackTarget() == null);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75246_d()
/*    */   {
/* 32 */     super.func_75246_d();
/* 33 */     EntityPlayer p = this.drone.field_70170_p.func_184136_b(this.drone, this.range);
/* 34 */     if (p != null)
/*    */     {
/* 36 */       this.drone.setDroneAttackTarget(p, true);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\ai\DroneAITargetPlayer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */