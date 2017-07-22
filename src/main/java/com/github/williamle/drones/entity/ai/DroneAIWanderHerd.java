/*    */ package williamle.drones.entity.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.Filters.FilterAccepts;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ public class DroneAIWanderHerd extends DroneAIWander
/*    */ {
/*    */   public Filters.FilterAccepts herdClass;
/* 13 */   public double herdRange = 1.0D;
/* 14 */   public int tryCount = 1;
/*    */   
/*    */ 
/*    */   public DroneAIWanderHerd(EntityDroneMob m, double r, double s, double herdArea, int herdTry, Class<? extends Entity>... clazz)
/*    */   {
/* 19 */     super(m, r, s);
/* 20 */     this.herdClass = new Filters.FilterAccepts((Object[])clazz);
/* 21 */     this.herdRange = herdArea;
/* 22 */     this.tryCount = herdTry;
/*    */   }
/*    */   
/*    */   public Vec3d getWanderPos(double range)
/*    */   {
/* 27 */     double weight = 0.0D;
/* 28 */     Vec3d thisWander = null;
/* 29 */     for (int a = 0; a < this.tryCount; a++)
/*    */     {
/* 31 */       double xWander = this.drone.field_70165_t + (this.rnd.nextDouble() - 0.5D) * range;
/* 32 */       double zWander = this.drone.field_70161_v + (this.rnd.nextDouble() - 0.5D) * range;
/* 33 */       double yWander = this.drone.field_70163_u + (this.rnd.nextDouble() - 0.5D) * range;
/* 34 */       yWander = Math.min(Math.max(yWander, 2.0D), this.drone.getBelowSurfaceY() + 8.0D);
/* 35 */       Vec3d vec = new Vec3d(xWander, yWander, zWander);
/* 36 */       List<Entity> herdFriends = this.drone.field_70170_p.func_175674_a(this.drone, this.drone
/* 37 */         .func_174813_aQ().func_186662_g(this.herdRange), this.herdClass);
/* 38 */       double thisWeight = getWeight(herdFriends);
/* 39 */       if ((thisWander == null) || (weight == 0.0D) || (thisWeight > weight))
/*    */       {
/* 41 */         thisWander = vec;
/* 42 */         weight = thisWeight;
/*    */       }
/*    */     }
/* 45 */     return thisWander;
/*    */   }
/*    */   
/*    */   public double getWeight(List<Entity> herd)
/*    */   {
/* 50 */     double d = 0.0D;
/* 51 */     for (Entity e : herd)
/*    */     {
/* 53 */       d += getEntityWeight(e);
/*    */     }
/* 55 */     return d;
/*    */   }
/*    */   
/*    */   public double getEntityWeight(Entity e)
/*    */   {
/* 60 */     if ((e instanceof EntityDroneMob)) return ((EntityDroneMob)e).getHerdIndividualWeight();
/* 61 */     return 1.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\ai\DroneAIWanderHerd.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */