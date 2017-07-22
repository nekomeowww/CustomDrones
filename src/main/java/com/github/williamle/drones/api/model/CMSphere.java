/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class CMSphere
/*    */   extends CMBase
/*    */ {
/*    */   public Vec3d origin;
/*    */   public double radius;
/*    */   public double verDiv;
/*    */   public double horDiv;
/*    */   
/*    */   public CMSphere(Vec3d ori, double r, double div)
/*    */   {
/* 15 */     this(ori, r, div, div * 2.0D);
/*    */   }
/*    */   
/*    */   public CMSphere(Vec3d ori, double r, double div1, double div2)
/*    */   {
/* 20 */     this.origin = ori;
/* 21 */     if (ori == null) this.origin = vec(0.0D, 0.0D, 0.0D);
/* 22 */     this.radius = r;
/* 23 */     this.verDiv = div1;
/* 24 */     this.horDiv = div2;
/*    */   }
/*    */   
/*    */ 
/*    */   public void render()
/*    */   {
/* 30 */     Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
/* 31 */     Vec3d yUnit = vec(0.0D, 1.0D, 0.0D);
/* 32 */     Vec3d yRad = vec(0.0D, this.radius, 0.0D);
/* 33 */     double verDivAngle = 3.141592653589793D / this.verDiv;
/* 34 */     double horDivAngle = 6.283185307179586D / this.horDiv;
/* 35 */     for (int a = 0; a < this.verDiv; a++)
/*    */     {
/* 37 */       Vec3d currentVerVec = rotateAround(yRad, zUnit, verDivAngle * a);
/* 38 */       Vec3d nextVerVec = rotateAround(yRad, zUnit, verDivAngle * (a + 1));
/* 39 */       begin(8);
/* 40 */       for (int b = 0; b < this.horDiv + 1.0D; b++)
/*    */       {
/* 42 */         Vec3d currentVec = rotateAround(currentVerVec, yUnit, -horDivAngle * b);
/* 43 */         Vec3d nextVec = rotateAround(nextVerVec, yUnit, -horDivAngle * b);
/* 44 */         double[] currentUV = calculateUVFromLocalSphere(currentVec);
/* 45 */         double[] nextUV = calculateUVFromLocalSphere(nextVec);
/* 46 */         Vec3d currentFinal = this.origin.func_178787_e(currentVec);
/* 47 */         Vec3d nextFinal = this.origin.func_178787_e(nextVec);
/* 48 */         Vec3d currentNorm = currentVec.func_72432_b();
/* 49 */         Vec3d nextNorm = nextVec.func_72432_b();
/* 50 */         normal(currentNorm.field_72450_a, currentNorm.field_72448_b, currentNorm.field_72449_c);
/* 51 */         vertex(currentFinal.field_72450_a, currentFinal.field_72448_b, currentFinal.field_72449_c, currentUV[0], currentUV[1]);
/* 52 */         normal(nextNorm.field_72450_a, nextNorm.field_72448_b, nextNorm.field_72449_c);
/* 53 */         vertex(nextFinal.field_72450_a, nextFinal.field_72448_b, nextFinal.field_72449_c, nextUV[0], nextUV[1]);
/*    */       }
/* 55 */       end();
/*    */     }
/*    */   }
/*    */   
/*    */   public double[] calculateUVFromLocalSphere(Vec3d position)
/*    */   {
/* 61 */     Vec3d needPos = scale(position, -1.0D);
/* 62 */     double[] uvs = new double[2];
/* 63 */     if (shouldApplyTexture())
/*    */     {
/* 65 */       double umid = (this.textureUV.u1 + this.textureUV.u2) / 2.0D;
/* 66 */       double ulength = this.textureUV.u2 - this.textureUV.u1;
/* 67 */       double vmid = (this.textureUV.v1 + this.textureUV.v2) / 2.0D;
/* 68 */       double vlength = this.textureUV.v1 - this.textureUV.v2;
/* 69 */       uvs[0] = (umid - ulength * (Math.atan2(needPos.field_72449_c, needPos.field_72450_a) / 2.0D / 3.141592653589793D));
/* 70 */       uvs[1] = (vmid - vlength * (Math.sin(needPos.field_72448_b) / 3.141592653589793D));
/*    */     }
/* 72 */     return uvs;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMSphere.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */