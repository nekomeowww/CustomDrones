/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ public class TextureUV
/*    */ {
/*    */   public double u1;
/*    */   public double v1;
/*    */   public double u2;
/*    */   public double v2;
/*    */   
/*    */   public TextureUV(double u, double v, double uu, double vv)
/*    */   {
/* 12 */     this.u1 = u;
/* 13 */     this.v1 = v;
/* 14 */     this.u2 = uu;
/* 15 */     this.v2 = vv;
/*    */   }
/*    */   
/*    */   public TextureUV()
/*    */   {
/* 20 */     this(0.0D, 0.0D, 1.0D, 1.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\TextureUV.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */