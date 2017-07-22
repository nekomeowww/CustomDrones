/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.helpers.VecHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMPolygon3d
/*    */   extends CMPolygon
/*    */ {
/* 12 */   public Vec3d skew = vec0;
/*    */   public List<Vec3d> frontFaceUVs;
/*    */   public List<Vec3d> backFaceUVs;
/*    */   
/*    */   public CMPolygon3d(Vec3d[] pos, Vec3d nor, Vec3d ske, List<Vec3d>... faceuvs)
/*    */   {
/* 18 */     super(pos, null, nor);
/* 19 */     this.skew = ske;
/* 20 */     if (faceuvs.length > 0) this.frontFaceUVs = faceuvs[0];
/* 21 */     if (faceuvs.length > 1) this.backFaceUVs = faceuvs[1];
/* 22 */     if (faceuvs.length > 2) this.sideUVs = faceuvs[2];
/*    */   }
/*    */   
/*    */   public CMPolygon3d setColors(Color c1, Color c2, Color c3)
/*    */   {
/* 27 */     this.frontFaceColor = c1;
/* 28 */     this.color = c2;
/* 29 */     this.backFaceColor = c3;
/* 30 */     return this;
/*    */   }
/*    */   
/*    */   public List<Vec3d> sideUVs;
/*    */   public Color frontFaceColor;
/*    */   public Color backFaceColor;
/* 36 */   public void render() { this.renderFront = true;
/* 37 */     this.renderBack = false;
/* 38 */     this.uvs = this.frontFaceUVs;
/* 39 */     if (this.frontFaceColor != null) applyColorOrTexture(this.texture, this.frontFaceColor);
/* 40 */     super.render();
/*    */     
/* 42 */     Vec3d polyCenter = VecHelper.getMid((Vec3d[])this.positions.toArray(new Vec3d[0]));
/* 43 */     this.uvs = this.sideUVs;
/* 44 */     applyColorOrTexture(this.texture, this.color);
/* 45 */     if (this.positions.size() > 0)
/*    */     {
/* 47 */       begin(8);
/* 48 */       for (int a = 0; a <= this.positions.size(); a++)
/*    */       {
/* 50 */         Vec3d pos = (Vec3d)this.positions.get(a == this.positions.size() ? 0 : a);
/* 51 */         Vec3d uv1 = (this.uvs == null) || (!shouldApplyTexture()) ? vec0 : (Vec3d)this.uvs.get(Math.min(a * 2, this.uvs.size() - 1));
/* 52 */         Vec3d uv2 = (this.uvs == null) || (!shouldApplyTexture()) ? vec0 : (Vec3d)this.uvs.get(Math.min(a * 2 + 1, this.uvs.size() - 1));
/*    */         
/* 54 */         Vec3d sideNormal = VecHelper.fromTo(polyCenter, pos).func_72432_b();
/* 55 */         double skewAffectNormal = sideNormal.func_72430_b(this.skew);
/* 56 */         if (skewAffectNormal != 0.0D) skewAffectNormal /= Math.abs(skewAffectNormal);
/* 57 */         sideNormal = sideNormal.func_178787_e(VecHelper.scale(this.skew, -skewAffectNormal)).func_72432_b();
/*    */         
/* 59 */         normal(sideNormal.field_72450_a, sideNormal.field_72448_b, sideNormal.field_72449_c);
/* 60 */         vertex(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c, uv1.field_72450_a, uv1.field_72448_b);
/* 61 */         pos = pos.func_178787_e(this.skew);
/* 62 */         vertex(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c, uv2.field_72450_a, uv2.field_72448_b);
/*    */       }
/* 64 */       end();
/*    */     }
/*    */     
/* 67 */     translate(this.skew.field_72450_a, this.skew.field_72448_b, this.skew.field_72449_c);
/* 68 */     this.renderFront = false;
/* 69 */     this.renderBack = true;
/* 70 */     this.uvs = this.backFaceUVs;
/* 71 */     if (this.backFaceColor != null) applyColorOrTexture(this.texture, this.backFaceColor);
/* 72 */     super.render();
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMPolygon3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */