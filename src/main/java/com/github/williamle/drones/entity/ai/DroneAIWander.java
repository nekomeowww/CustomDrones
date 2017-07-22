/*    */ package williamle.drones.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.world.World;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ public class DroneAIWander
/*    */   extends EntityAIBase
/*    */ {
/*    */   public Random rnd;
/*    */   public EntityDroneMob drone;
/*    */   public Vec3d wanderPos;
/*    */   public double range;
/*    */   public double speed;
/*    */   
/*    */   public DroneAIWander(EntityDroneMob m, double r, double s)
/*    */   {
/* 20 */     this.drone = m;
/* 21 */     this.rnd = new Random();
/* 22 */     this.range = r;
/* 23 */     this.speed = s;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75250_a()
/*    */   {
/* 29 */     return (this.drone.getDroneAttackTarget() == null) && (!this.drone.field_70170_p.field_72995_K);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75251_c()
/*    */   {
/* 35 */     super.func_75251_c();
/* 36 */     this.wanderPos = null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_75246_d()
/*    */   {
/* 42 */     super.func_75246_d();
/* 43 */     int baseTickMod = 38 + this.rnd.nextInt(4);
/* 44 */     boolean newPos = (this.wanderPos == null) && (this.drone.field_70173_aa % baseTickMod == 0);
/* 45 */     if ((!newPos) && (this.wanderPos != null))
/*    */     {
/* 47 */       newPos = ((this.drone.func_70092_e(this.wanderPos.field_72450_a, this.wanderPos.field_72448_b, this.wanderPos.field_72449_c) < 0.2D) && (this.drone.field_70173_aa % (baseTickMod * 2) == 0)) || (this.drone.field_70173_aa % (baseTickMod * 5) == 0);
/*    */     }
/*    */     
/* 50 */     if (newPos)
/*    */     {
/* 52 */       this.wanderPos = getWanderPos(this.range);
/*    */     }
/* 54 */     if (this.wanderPos != null)
/*    */     {
/* 56 */       this.drone.flyTo(this.wanderPos, 0.4D, this.speed);
/*    */     }
/*    */   }
/*    */   
/*    */   public Vec3d getWanderPos(double range)
/*    */   {
/* 62 */     double yWander = this.drone.field_70163_u + (this.rnd.nextDouble() - 0.5D) * range;
/* 63 */     yWander = Math.min(Math.max(yWander, 2.0D), this.drone.getBelowSurfaceY() + range);
/*    */     
/* 65 */     return new Vec3d(this.drone.field_70165_t + (this.rnd.nextDouble() - 0.5D) * range, yWander, this.drone.field_70161_v + (this.rnd.nextDouble() - 0.5D) * range);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\ai\DroneAIWander.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */