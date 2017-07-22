/*    */ package williamle.drones.api.geometry;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class Line3d
/*    */ {
/*    */   public final Vec3d unit;
/*    */   public final Vec3d unitNorm;
/*    */   public final Vec3d aPoint;
/*    */   
/*    */   public Line3d(Vec3d point, Vec3d vec)
/*    */   {
/* 13 */     this.unit = vec;
/* 14 */     this.unitNorm = vec.func_72432_b();
/* 15 */     this.aPoint = point;
/*    */   }
/*    */   
/*    */   public Vec3d getPointFromX(double x)
/*    */   {
/* 20 */     double y = this.aPoint.field_72448_b + (x - this.aPoint.field_72450_a) * this.unit.field_72448_b / this.unit.field_72450_a;
/* 21 */     double z = this.aPoint.field_72449_c + (x - this.aPoint.field_72450_a) * this.unit.field_72449_c / this.unit.field_72450_a;
/* 22 */     return new Vec3d(x, y, z);
/*    */   }
/*    */   
/*    */   public Vec3d getPointFromY(double y)
/*    */   {
/* 27 */     double x = this.aPoint.field_72450_a + (y - this.aPoint.field_72448_b) * this.unit.field_72450_a / this.unit.field_72448_b;
/* 28 */     double z = this.aPoint.field_72449_c + (y - this.aPoint.field_72448_b) * this.unit.field_72449_c / this.unit.field_72448_b;
/* 29 */     return new Vec3d(x, y, z);
/*    */   }
/*    */   
/*    */   public Vec3d getPointFromZ(double z)
/*    */   {
/* 34 */     double x = this.aPoint.field_72450_a + (z - this.aPoint.field_72449_c) * this.unit.field_72450_a / this.unit.field_72449_c;
/* 35 */     double y = this.aPoint.field_72448_b + (z - this.aPoint.field_72449_c) * this.unit.field_72448_b / this.unit.field_72449_c;
/* 36 */     return new Vec3d(x, y, z);
/*    */   }
/*    */   
/*    */   public Vec3d intersect(Line3d line)
/*    */   {
/* 41 */     if (this.unit.func_72431_c(line.unit).func_72433_c() == 0.0D) return null;
/* 42 */     Vec3d leftSide = this.unit.func_72431_c(line.unit);
/* 43 */     Vec3d rightSide = line.aPoint.func_178788_d(this.aPoint).func_72431_c(line.unit);
/* 44 */     if (Double.compare(leftSide.func_72431_c(rightSide).func_72433_c(), 0.0D) == 0)
/*    */     {
/* 46 */       double a = leftSide.field_72449_c != 0.0D ? rightSide.field_72449_c / leftSide.field_72449_c : leftSide.field_72448_b != 0.0D ? rightSide.field_72448_b / leftSide.field_72448_b : leftSide.field_72450_a != 0.0D ? rightSide.field_72450_a / leftSide.field_72450_a : 0.0D;
/*    */       
/*    */ 
/* 49 */       return this.aPoint.func_178787_e(williamle.drones.api.helpers.VecHelper.scale(this.unit, a));
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 57 */     return "Vec: " + this.aPoint.toString() + " ; Dir: " + this.unit.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\geometry\Line3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */