/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ 
/*    */ public class RotateVec
/*    */ {
/*    */   public Vec3d vec;
/*    */   public double rotation;
/*    */   
/*    */   public RotateVec(double rot, Vec3d v)
/*    */   {
/* 13 */     this.rotation = rot;
/* 14 */     this.vec = v;
/*    */   }
/*    */   
/*    */   public RotateVec(double rot, double x, double y, double z)
/*    */   {
/* 19 */     this(rot, new Vec3d(x, y, z));
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\RotateVec.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */