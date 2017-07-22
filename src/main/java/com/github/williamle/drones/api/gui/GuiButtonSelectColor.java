/*    */ package williamle.drones.api.gui;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import org.lwjgl.input.Mouse;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import williamle.drones.api.model.Color;
/*    */ 
/*    */ public class GuiButtonSelectColor
/*    */   extends GuiButton
/*    */ {
/*    */   public Color color1;
/*    */   public Color color2;
/* 14 */   public double selectedIndex = 0.0D;
/* 15 */   public double pointerOuter = 2.0D;
/* 16 */   public float pointWidth = 2.0F;
/*    */   
/*    */   public GuiButtonSelectColor(int buttonId, int x, int y, int w, int h, Color c1, Color c2)
/*    */   {
/* 20 */     super(buttonId, x, y, w, h, "");
/* 21 */     this.color1 = c1;
/* 22 */     this.color2 = c2;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 28 */     if ((this.field_146124_l) && (this.field_146125_m) && (mouseX >= this.field_146128_h) && (mouseY >= this.field_146129_i) && (mouseX <= this.field_146128_h + this.field_146120_f) && (mouseY <= this.field_146129_i + this.field_146121_g))
/*    */     {
/*    */ 
/* 31 */       this.selectedIndex = ((mouseX - this.field_146128_h) / this.field_146120_f);
/* 32 */       return true;
/*    */     }
/* 34 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected void func_146119_b(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 40 */     super.func_146119_b(mc, mouseX, mouseY);
/* 41 */     if ((this.field_146124_l) && (this.field_146125_m) && (mouseX >= this.field_146128_h) && (mouseY >= this.field_146129_i) && (mouseX <= this.field_146128_h + this.field_146120_f) && (mouseY <= this.field_146129_i + this.field_146121_g))
/*    */     {
/*    */ 
/* 44 */       this.selectedIndex = ((mouseX - this.field_146128_h) / this.field_146120_f);
/*    */     }
/*    */   }
/*    */   
/*    */   public Color getOutputColor()
/*    */   {
/* 50 */     double alpha = (this.color2.alpha - this.color1.alpha) * this.selectedIndex + this.color1.alpha;
/* 51 */     double red = (this.color2.red - this.color1.red) * this.selectedIndex + this.color1.red;
/* 52 */     double green = (this.color2.green - this.color1.green) * this.selectedIndex + this.color1.green;
/* 53 */     double blue = (this.color2.blue - this.color1.blue) * this.selectedIndex + this.color1.blue;
/* 54 */     Color c = new Color(red, green, blue, alpha);
/* 55 */     return c;
/*    */   }
/*    */   
/*    */   public Color getReverseOutputColor()
/*    */   {
/* 60 */     double s = this.selectedIndex;
/* 61 */     this.selectedIndex = (1.0D - this.selectedIndex);
/* 62 */     Color c = getOutputColor();
/* 63 */     this.selectedIndex = s;
/* 64 */     return c;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_146112_a(Minecraft mc, int mouseX, int mouseY)
/*    */   {
/* 70 */     if (this.field_146125_m)
/*    */     {
/* 72 */       GL11.glPushMatrix();
/* 73 */       GL11.glDisable(3553);
/* 74 */       GL11.glShadeModel(7425);
/* 75 */       GL11.glEnable(3042);
/* 76 */       GL11.glBlendFunc(770, 771);
/* 77 */       GL11.glBegin(7);
/* 78 */       GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
/* 79 */       GL11.glVertex2d(this.field_146128_h, this.field_146129_i);
/* 80 */       GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
/* 81 */       GL11.glVertex2d(this.field_146128_h, this.field_146129_i + this.field_146121_g);
/* 82 */       GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
/* 83 */       GL11.glVertex2d(this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g);
/* 84 */       GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
/* 85 */       GL11.glVertex2d(this.field_146128_h + this.field_146120_f, this.field_146129_i);
/* 86 */       GL11.glEnd();
/* 87 */       GL11.glEnable(3553);
/* 88 */       GL11.glShadeModel(7424);
/* 89 */       GL11.glPopMatrix();
/*    */       
/* 91 */       if ((Mouse.isCreated()) && (Mouse.isButtonDown(0)))
/*    */       {
/* 93 */         func_146119_b(mc, mouseX, mouseY);
/*    */       }
/* 95 */       double lineX = this.field_146128_h + this.field_146120_f * this.selectedIndex;
/* 96 */       DrawHelper.drawLine(lineX, this.field_146129_i - this.pointerOuter, lineX, this.field_146129_i + this.field_146121_g + this.pointerOuter, 
/* 97 */         getReverseOutputColor(), this.pointWidth);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\GuiButtonSelectColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */