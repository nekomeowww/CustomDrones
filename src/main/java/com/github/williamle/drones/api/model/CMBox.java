/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class CMBox extends CMBase {
/*    */   double x1;
/*    */   double y1;
/*    */   double z1;
/*    */   double x2;
/*    */   double y2;
/*    */   double z2;
/*    */   
/* 13 */   public CMBox(double size) { this(size, size, size, null); }
/*    */   
/*    */ 
/*    */   public CMBox(double size, Vec3d mid)
/*    */   {
/* 18 */     this(size, size, size, mid);
/*    */   }
/*    */   
/*    */   public CMBox(double xw, double yw, double zw)
/*    */   {
/* 23 */     this(xw, yw, zw, null);
/*    */   }
/*    */   
/*    */   public CMBox(double xw, double yw, double zw, Vec3d mid)
/*    */   {
/* 28 */     this(-xw / 2.0D + (mid == null ? 0.0D : mid.field_72450_a), -yw / 2.0D + (mid == null ? 0.0D : mid.field_72448_b), -zw / 2.0D + (mid == null ? 0.0D : mid.field_72449_c), xw / 2.0D + (mid == null ? 0.0D : mid.field_72450_a), yw / 2.0D + (mid == null ? 0.0D : mid.field_72448_b), zw / 2.0D + (mid == null ? 0.0D : mid.field_72449_c));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public CMBox(double x, double y, double z, double xx, double yy, double zz)
/*    */   {
/* 35 */     this.x1 = x;
/* 36 */     this.x2 = xx;
/* 37 */     this.y1 = y;
/* 38 */     this.y2 = yy;
/* 39 */     this.z1 = z;
/* 40 */     this.z2 = zz;
/*    */   }
/*    */   
/*    */ 
/*    */   public void render()
/*    */   {
/* 46 */     double xw = Math.abs(this.x2 - this.x1);
/* 47 */     double yw = Math.abs(this.y2 - this.y1);
/* 48 */     double zw = Math.abs(this.z2 - this.z1);
/* 49 */     double tWidth = xw * 2.0D + zw * 2.0D;
/* 50 */     double tHeight = zw + yw;
/*    */     
/* 52 */     double u1 = this.textureUV.u1;
/* 53 */     double u5 = this.textureUV.u2;
/* 54 */     double u2 = u1 + zw / tWidth * (u5 - u1);
/* 55 */     double u3 = u2 + xw / tWidth * (u5 - u1);
/* 56 */     double u4 = u3 + zw / tWidth * (u5 - u1);
/* 57 */     double v1 = this.textureUV.v1;
/* 58 */     double v4 = this.textureUV.v2;
/* 59 */     double v2 = v1 + zw / tHeight * (v4 - v1);
/*    */     
/* 61 */     begin(8);
/* 62 */     normal(-1.0D, 0.0D, 0.0D);
/* 63 */     vertex(this.x1, this.y2, this.z1, u1, v2);
/* 64 */     vertex(this.x1, this.y1, this.z1, u1, v4);
/* 65 */     normal(0.0D, 0.0D, 1.0D);
/* 66 */     vertex(this.x1, this.y2, this.z2, u2, v2);
/* 67 */     vertex(this.x1, this.y1, this.z2, u2, v4);
/* 68 */     normal(1.0D, 0.0D, 0.0D);
/* 69 */     vertex(this.x2, this.y2, this.z2, u3, v2);
/* 70 */     vertex(this.x2, this.y1, this.z2, u3, v4);
/* 71 */     normal(0.0D, 0.0D, -1.0D);
/* 72 */     vertex(this.x2, this.y2, this.z1, u4, v2);
/* 73 */     vertex(this.x2, this.y1, this.z1, u4, v4);
/* 74 */     normal(-1.0D, 0.0D, 0.0D);
/* 75 */     vertex(this.x1, this.y2, this.z1, u5, v2);
/* 76 */     vertex(this.x1, this.y1, this.z1, u5, v4);
/* 77 */     end();
/*    */     
/* 79 */     begin(7);
/* 80 */     normal(0.0D, 1.0D, 0.0D);
/* 81 */     vertex(this.x1, this.y2, this.z1, u2, v1);
/* 82 */     vertex(this.x1, this.y2, this.z2, u2, v2);
/* 83 */     vertex(this.x2, this.y2, this.z2, u3, v2);
/* 84 */     vertex(this.x2, this.y2, this.z1, u3, v1);
/* 85 */     normal(0.0D, -1.0D, 0.0D);
/* 86 */     vertex(this.x2, this.y1, this.z1, u5, v2);
/* 87 */     vertex(this.x2, this.y1, this.z2, u5, v1);
/* 88 */     vertex(this.x1, this.y1, this.z2, u4, v1);
/* 89 */     vertex(this.x1, this.y1, this.z1, u4, v2);
/* 90 */     end();
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */