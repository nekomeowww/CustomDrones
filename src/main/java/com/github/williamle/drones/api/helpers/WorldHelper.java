/*     */ package williamle.drones.api.helpers;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.RayTraceResult.Type;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.api.geometry.Line3d;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WorldHelper
/*     */ {
/*     */   public static BlockPos getLowestAir(World world, BlockPos bp)
/*     */   {
/*  23 */     for (int a = 0; a < world.func_72940_L(); a++)
/*     */     {
/*  25 */       BlockPos bp0 = new BlockPos(bp.func_177958_n(), a, bp.func_177952_p());
/*  26 */       if (world.func_175623_d(bp0)) return bp0;
/*     */     }
/*  28 */     return bp.func_185334_h();
/*     */   }
/*     */   
/*     */   public static RayTraceResult fullRayTrace(World world, Vec3d start, Vec3d end, Predicate entityFilter)
/*     */   {
/*  33 */     return fullRayTrace(world, start, end, false, false, entityFilter);
/*     */   }
/*     */   
/*     */ 
/*     */   public static RayTraceResult fullRayTrace(World world, Vec3d start, Vec3d end, boolean liquid, boolean ignoreNonBound, Predicate entityFilter)
/*     */   {
/*  39 */     RayTraceResult mop = world.func_147447_a(start, end, liquid, ignoreNonBound, false);
/*  40 */     Vec3d destination = end;
/*  41 */     if (mop != null) destination = mop.field_72307_f;
/*  42 */     List<Entity> list = world.func_175647_a(Entity.class, new AxisAlignedBB(start.field_72450_a, start.field_72448_b, start.field_72449_c, destination.field_72450_a, destination.field_72448_b, destination.field_72449_c), entityFilter == null ? EntitySelectors.field_94557_a : entityFilter);
/*     */     
/*     */     double nearestIntercept;
/*     */     
/*  46 */     if ((list != null) && (!list.isEmpty()))
/*     */     {
/*  48 */       nearestIntercept = -1.0D;
/*  49 */       for (Entity e : list)
/*     */       {
/*  51 */         RayTraceResult mop1 = e.func_174813_aQ().func_72327_a(start, destination);
/*  52 */         if ((mop1 != null) && (
/*  53 */           (mop1.field_72307_f.func_178788_d(start).func_72433_c() < nearestIntercept) || (nearestIntercept < 0.0D)))
/*     */         {
/*  55 */           mop = new RayTraceResult(e, mop1.field_72307_f);
/*     */         }
/*     */       }
/*     */     }
/*  59 */     return mop;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Entity getEntityBestInAngle(World world, Vec3d ori, Vec3d dir, AxisAlignedBB area, double maxAngle, Entity exclude, Predicate filter)
/*     */   {
/*  65 */     Vec3d[] cuts = GeometryHelper.lineCutAABB(area, new Line3d(ori, dir), true);
/*  66 */     if ((cuts[1] != null) || (cuts[0] != null))
/*     */     {
/*  68 */       RayTraceResult firstRtr = fullRayTrace(world, ori, cuts[1] != null ? cuts[1] : cuts[0], false, true, filter);
/*     */       
/*  70 */       if ((firstRtr != null) && (firstRtr.field_72308_g != null)) { return firstRtr.field_72308_g;
/*     */       }
/*     */     }
/*  73 */     Entity target = null;
/*  74 */     double angle = maxAngle;
/*  75 */     List<Entity> entities = world.func_175674_a(exclude, area, filter);
/*  76 */     for (Entity e : entities)
/*     */     {
/*  78 */       Vec3d eEye = EntityHelper.getEyeVec(e);
/*  79 */       RayTraceResult rtr = fullRayTrace(world, ori, eEye, false, true, filter);
/*  80 */       if ((rtr == null) || (rtr.field_72313_a == RayTraceResult.Type.MISS) || (rtr.field_72308_g == e))
/*     */       {
/*  82 */         Vec3d toEye = VecHelper.fromTo(ori, eEye);
/*  83 */         double thisAngle = VecHelper.getAngleBetween(toEye, dir) / 3.141592653589793D * 180.0D;
/*  84 */         if (thisAngle <= angle)
/*     */         {
/*  86 */           angle = thisAngle;
/*  87 */           target = e;
/*     */         }
/*     */       }
/*     */     }
/*  91 */     return target;
/*     */   }
/*     */   
/*     */   public static Entity getEntityByPersistentUUID(World world, UUID uuid)
/*     */   {
/*  96 */     List<Entity> list = world.func_72910_y();
/*  97 */     for (Entity e : list)
/*     */     {
/*  99 */       if (uuid.equals(e.getPersistentID())) return e;
/*     */     }
/* 101 */     return null;
/*     */   }
/*     */   
/* 104 */   public static Predicate filterDrone = new Predicate()
/*     */   {
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/* 109 */       return input instanceof EntityDrone;
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\WorldHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */