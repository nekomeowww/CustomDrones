/*    */ package williamle.drones.entity.ai;
/*    */ 
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.ai.EntityAIBase;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.world.World;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ public class DroneAIFlyToNearest extends EntityAIBase
/*    */ {
/*    */   public Random rnd;
/*    */   public EntityDroneMob drone;
/*    */   public double range;
/*    */   public double speed;
/*    */   public Class<? extends Entity> clazz;
/*    */   
/*    */   public DroneAIFlyToNearest(EntityDroneMob m, double r, double s, Class<? extends Entity> clazz)
/*    */   {
/* 20 */     this.drone = m;
/* 21 */     this.rnd = new Random();
/* 22 */     this.range = r;
/* 23 */     this.speed = s;
/* 24 */     this.clazz = clazz;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75250_a()
/*    */   {
/* 30 */     return this.drone.field_70170_p.func_72857_a(this.clazz, this.drone.func_174813_aQ().func_186662_g(this.range), this.drone) != null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void func_75246_d()
/*    */   {
/* 37 */     super.func_75246_d();
/* 38 */     Entity e = this.drone.field_70170_p.func_72857_a(this.clazz, this.drone.func_174813_aQ().func_186662_g(this.range), this.drone);
/*    */     
/* 40 */     this.drone.flyTo(williamle.drones.api.helpers.EntityHelper.getEyeVec(e), 0.2D, this.speed);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\ai\DroneAIFlyToNearest.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */