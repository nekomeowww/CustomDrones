/*    */ package williamle.drones.api.geometry;
/*    */ 
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class Plane3d
/*    */ {
/*    */   public final Vec3d aPoint;
/*    */   public final Vec3d normal;
/*    */   public final Vec3d normalNorm;
/*    */   
/*    */   public Plane3d(Vec3d p, Vec3d n)
/*    */   {
/* 14 */     this.aPoint = p;
/* 15 */     this.normal = n;
/* 16 */     this.normalNorm = this.normal.func_72432_b();
/*    */   }
/*    */   
/*    */   public Vec3d intersectInAABB(Line3d line, AxisAlignedBB aabb)
/*    */   {
/* 21 */     Vec3d vec = intersect(line);
/* 22 */     if (vec != null)
/*    */     {
/* 24 */       if (aabb.func_72318_a(vec)) return vec;
/*    */     }
/* 26 */     return null;
/*    */   }
/*    */   
/*    */   public Vec3d intersect(Line3d line)
/*    */   {
/* 31 */     if (line.unitNorm.func_72430_b(this.normal) == 0.0D) return null;
/* 32 */     Vec3d wVec = line.aPoint.func_178788_d(this.aPoint);
/* 33 */     double unitScale = -this.normalNorm.func_72430_b(wVec) / this.normalNorm.func_72430_b(line.unitNorm);
/* 34 */     return line.aPoint.func_178787_e(line.unitNorm.func_186678_a(unitScale));
/*    */   }
/*    */   
/*    */ 
/*    */   public Vec3d getAVecOnPlane()
/*    */   {
/* 40 */     Vec3d secondVector = this.normalNorm.field_72448_b == 1.0D ? this.normalNorm.func_178789_a(1.5707964F) : this.normalNorm.func_178785_b(1.5707964F);
/* 41 */     return williamle.drones.api.helpers.VecHelper.getPerpendicularVec(this.normalNorm, secondVector);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\geometry\Plane3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */