/*    */ package williamle.drones.api.gui;
/*    */ 
/*    */ import williamle.drones.api.model.Color;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PIObjectColor
/*    */   extends PI
/*    */ {
/*    */   public Color defaultColor;
/*    */   public Color color;
/*    */   
/*    */   public PIObjectColor(Panel p, Color c)
/*    */   {
/* 23 */     super(p);
/* 24 */     this.color = c;
/* 25 */     this.defaultColor = (this.color != null ? this.color.copy() : null);
/*    */   }
/*    */   
/*    */ 
/*    */   public void drawItemContent()
/*    */   {
/* 31 */     super.drawItemContent();
/* 32 */     double ymin = this.yw - additionalContentY() * 1.5D + 4.0D;
/* 33 */     double ymax = this.yw - 4.0D;
/* 34 */     double xMargin = 12.0D;
/* 35 */     if (this.color != null)
/*    */     {
/* 37 */       DrawHelper.drawRect(xMargin, ymin, this.xw - xMargin, ymax, this.color);
/*    */     }
/*    */     else
/*    */     {
/* 41 */       Color c = new Color(1.0D, 0.0D, 0.0D);
/* 42 */       DrawHelper.drawLine(xMargin, ymin, this.xw - xMargin, ymax, c, 2.0F);
/* 43 */       DrawHelper.drawLine(this.xw - xMargin, ymin, xMargin, ymax, c, 2.0F);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int additionalContentY()
/*    */   {
/* 50 */     return 15;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\PIObjectColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */