/*    */ package williamle.drones.api.helpers;
/*    */ 
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.geometry.Line3d;
/*    */ import williamle.drones.api.geometry.Plane3d;
/*    */ 
/*    */ public class GeometryHelper
/*    */ {
/*    */   public static Vec3d[] lineCutAABB(AxisAlignedBB aabb, Line3d l, boolean sameSideWithDir)
/*    */   {
/* 12 */     Plane3d pminX = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(1.0D, 0.0D, 0.0D));
/* 13 */     Plane3d pminY = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(0.0D, 1.0D, 0.0D));
/* 14 */     Plane3d pminZ = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(0.0D, 0.0D, 1.0D));
/* 15 */     Plane3d pmaxX = new Plane3d(vec(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c), vec(-1.0D, 0.0D, 0.0D));
/* 16 */     Plane3d pmaxY = new Plane3d(vec(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c), vec(0.0D, -1.0D, 0.0D));
/* 17 */     Plane3d pmaxZ = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f), vec(0.0D, 0.0D, -1.0D));
/* 18 */     Vec3d iminX = pminX.intersectInAABB(l, aabb);
/* 19 */     Vec3d imaxX = pmaxX.intersectInAABB(l, aabb);
/* 20 */     Vec3d iminY = pminY.intersectInAABB(l, aabb);
/* 21 */     Vec3d imaxY = pmaxY.intersectInAABB(l, aabb);
/* 22 */     Vec3d iminZ = pminZ.intersectInAABB(l, aabb);
/* 23 */     Vec3d imaxZ = pmaxZ.intersectInAABB(l, aabb);
/* 24 */     Vec3d[] allIntersects = { iminX, imaxX, iminY, imaxY, iminZ, imaxZ };
/* 25 */     Vec3d closest = VecHelper.getClosest(l.aPoint, allIntersects);
/* 26 */     Vec3d farthest = VecHelper.getFarthest(l.aPoint, allIntersects);
/* 27 */     return new Vec3d[] { closest, farthest };
/*    */   }
/*    */   
/*    */   public static Vec3d vec(double x, double y, double z)
/*    */   {
/* 32 */     return new Vec3d(x, y, z);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\GeometryHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */