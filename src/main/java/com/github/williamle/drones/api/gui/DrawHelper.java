/*     */ package williamle.drones.api.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.model.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DrawHelper
/*     */ {
/*     */   public static void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font, int width, int height)
/*     */   {
/*  14 */     if (!textLines.isEmpty())
/*     */     {
/*  16 */       GL11.glPushMatrix();
/*  17 */       GL11.glDisable(2896);
/*  18 */       GL11.glTranslated(0.0D, 0.0D, 200.0D);
/*  19 */       int i = 0;
/*     */       
/*  21 */       for (String s : textLines)
/*     */       {
/*  23 */         int j = font.func_78256_a(s);
/*     */         
/*  25 */         if (j > i)
/*     */         {
/*  27 */           i = j;
/*     */         }
/*     */       }
/*     */       
/*  31 */       int l1 = x + 12;
/*  32 */       int i2 = y - 12;
/*  33 */       int k = 8;
/*     */       
/*  35 */       if (textLines.size() > 1)
/*     */       {
/*  37 */         k += 2 + (textLines.size() - 1) * 10;
/*     */       }
/*     */       
/*  40 */       if (l1 + i > width)
/*     */       {
/*  42 */         l1 -= 28 + i;
/*     */       }
/*     */       
/*  45 */       if (i2 + k + 6 > height)
/*     */       {
/*  47 */         i2 = height - k - 6;
/*     */       }
/*     */       
/*  50 */       int l = -267386864;
/*  51 */       drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
/*  52 */       drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
/*  53 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
/*  54 */       drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
/*  55 */       drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
/*  56 */       int i1 = 1347420415;
/*  57 */       int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
/*  58 */       drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
/*  59 */       drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
/*  60 */       drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
/*  61 */       drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
/*     */       
/*  63 */       for (int k1 = 0; k1 < textLines.size(); k1++)
/*     */       {
/*  65 */         String s1 = (String)textLines.get(k1);
/*  66 */         font.func_175063_a(s1, l1, i2, -1);
/*     */         
/*  68 */         if (k1 == 0)
/*     */         {
/*  70 */           i2 += 2;
/*     */         }
/*     */         
/*  73 */         i2 += 10;
/*     */       }
/*     */       
/*  76 */       GL11.glColor3d(1.0D, 1.0D, 1.0D);
/*  77 */       GL11.glPopMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public static Color nextColor(Color c)
/*     */   {
/*  83 */     if (c == null) return null;
/*  84 */     return c.getNextColor();
/*     */   }
/*     */   
/*     */   public static void drawLine(double x0, double y0, double x1, double y1, Color c, float width)
/*     */   {
/*  89 */     drawLine(x0, y0, 0.0D, x1, y1, 0.0D, c, width);
/*     */   }
/*     */   
/*     */   public static void drawLine(double x0, double y0, double z0, double x1, double y1, double z1, Color c, float width)
/*     */   {
/*  94 */     GL11.glPushMatrix();
/*  95 */     GL11.glEnable(3042);
/*  96 */     GL11.glBlendFunc(770, 771);
/*  97 */     GL11.glDisable(3553);
/*  98 */     GL11.glShadeModel(7425);
/*  99 */     GL11.glLineWidth(width);
/* 100 */     GL11.glBegin(1);
/* 101 */     GL11.glColor4d(c.red, c.green, c.blue, c.alpha);
/* 102 */     GL11.glVertex3d(x0, y0, z0);
/* 103 */     if (c.isGradient())
/*     */     {
/* 105 */       GL11.glColor4d(c.getNextColor().red, c.getNextColor().green, c.getNextColor().blue, c.getNextColor().alpha);
/*     */     }
/* 107 */     GL11.glVertex3d(x1, y1, z1);
/* 108 */     GL11.glEnd();
/* 109 */     GL11.glShadeModel(7424);
/* 110 */     GL11.glEnable(3553);
/* 111 */     GL11.glDisable(3042);
/* 112 */     GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
/* 113 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void drawRectMargin(double x0, double y0, double x1, double y1, double mg, Color c)
/*     */   {
/* 118 */     drawRectMargin(x0, y0, x1, y1, mg, c == null ? 0L : c.toLong());
/*     */   }
/*     */   
/*     */   public static void drawRectMargin(double x0, double y0, double x1, double y1, double mg, long color)
/*     */   {
/* 123 */     drawRectMargin(x0, y0, x1, y1, mg, mg, color);
/*     */   }
/*     */   
/*     */   public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mg, Color c1, Color c2)
/*     */   {
/* 128 */     drawGradientRectMargin(x0, y0, x1, y1, mg, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
/*     */   }
/*     */   
/*     */ 
/*     */   public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mg, long color1, long color2)
/*     */   {
/* 134 */     drawGradientRectMargin(x0, y0, x1, y1, mg, mg, color1, color2);
/*     */   }
/*     */   
/*     */   public static void drawRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, Color c)
/*     */   {
/* 139 */     drawRectMargin(x0, y0, x1, y1, mgx, mgy, c == null ? 0L : c.toLong());
/*     */   }
/*     */   
/*     */   public static void drawRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, long color)
/*     */   {
/* 144 */     drawRect(x0, y0, x0 + mgx, y1, color);
/* 145 */     drawRect(x0, y0, x1, y0 + mgy, color);
/* 146 */     drawRect(x1 - mgx, y0, x1, y1, color);
/* 147 */     drawRect(x0, y1 - mgy, x1, y1, color);
/*     */   }
/*     */   
/*     */ 
/*     */   public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, Color c1, Color c2)
/*     */   {
/* 153 */     drawGradientRectMargin(x0, y0, x1, y1, mgx, mgy, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
/*     */   }
/*     */   
/*     */ 
/*     */   public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, long color1, long color2)
/*     */   {
/* 159 */     drawGradientRect(x0, y0, x0 + mgx, y1, color1, color2);
/* 160 */     drawGradientRect(x0, y0, x1, y0 + mgy, color1, color2);
/* 161 */     drawGradientRect(x1 - mgx, y0, x1, y1, color1, color2);
/* 162 */     drawGradientRect(x0, y1 - mgy, x1, y1, color1, color2);
/*     */   }
/*     */   
/*     */   public static void drawRect(double left, double top, double right, double bottom, Color c)
/*     */   {
/* 167 */     drawRect(left, top, right, bottom, c == null ? 0L : c.toLong());
/*     */   }
/*     */   
/*     */   public static void drawRect(double left, double top, double right, double bottom, long color)
/*     */   {
/* 172 */     if (left < right)
/*     */     {
/* 174 */       double i = left;
/* 175 */       left = right;
/* 176 */       right = i;
/*     */     }
/*     */     
/* 179 */     if (top < bottom)
/*     */     {
/* 181 */       double j = top;
/* 182 */       top = bottom;
/* 183 */       bottom = j;
/*     */     }
/*     */     
/* 186 */     float f3 = (float)(color >> 24 & 0xFF) / 255.0F;
/* 187 */     float f = (float)(color >> 16 & 0xFF) / 255.0F;
/* 188 */     float f1 = (float)(color >> 8 & 0xFF) / 255.0F;
/* 189 */     float f2 = (float)(color & 0xFF) / 255.0F;
/* 190 */     GL11.glPushMatrix();
/* 191 */     GL11.glEnable(3042);
/* 192 */     GL11.glBlendFunc(770, 771);
/* 193 */     GL11.glDisable(3553);
/* 194 */     GL11.glBegin(7);
/* 195 */     GL11.glColor4f(f, f1, f2, f3);
/* 196 */     GL11.glVertex2d(left, bottom);
/* 197 */     GL11.glVertex2d(right, bottom);
/* 198 */     GL11.glVertex2d(right, top);
/* 199 */     GL11.glVertex2d(left, top);
/* 200 */     GL11.glEnd();
/* 201 */     GL11.glEnable(3553);
/* 202 */     GL11.glDisable(3042);
/* 203 */     GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
/* 204 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public static void drawGradientRect(double left, double top, double right, double bottom, Color c1, Color c2)
/*     */   {
/* 209 */     drawGradientRect(left, top, right, bottom, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
/*     */   }
/*     */   
/*     */ 
/*     */   public static void drawGradientRect(double left, double top, double right, double bottom, long startColor, long endColor)
/*     */   {
/* 215 */     float f = (float)(startColor >> 24 & 0xFF) / 255.0F;
/* 216 */     float f1 = (float)(startColor >> 16 & 0xFF) / 255.0F;
/* 217 */     float f2 = (float)(startColor >> 8 & 0xFF) / 255.0F;
/* 218 */     float f3 = (float)(startColor & 0xFF) / 255.0F;
/* 219 */     float f4 = (float)(endColor >> 24 & 0xFF) / 255.0F;
/* 220 */     float f5 = (float)(endColor >> 16 & 0xFF) / 255.0F;
/* 221 */     float f6 = (float)(endColor >> 8 & 0xFF) / 255.0F;
/* 222 */     float f7 = (float)(endColor & 0xFF) / 255.0F;
/* 223 */     GL11.glPushMatrix();
/* 224 */     GL11.glEnable(3042);
/* 225 */     GL11.glBlendFunc(770, 771);
/* 226 */     GL11.glDisable(3553);
/* 227 */     GL11.glShadeModel(7425);
/* 228 */     GL11.glBegin(7);
/* 229 */     GL11.glColor4f(f1, f2, f3, f);
/* 230 */     GL11.glVertex2d(right, top);
/* 231 */     GL11.glColor4f(f1, f2, f3, f);
/* 232 */     GL11.glVertex2d(left, top);
/* 233 */     GL11.glColor4f(f5, f6, f7, f4);
/* 234 */     GL11.glVertex2d(left, bottom);
/* 235 */     GL11.glColor4f(f5, f6, f7, f4);
/* 236 */     GL11.glVertex2d(right, bottom);
/* 237 */     GL11.glEnd();
/* 238 */     GL11.glShadeModel(7424);
/* 239 */     GL11.glEnable(3553);
/* 240 */     GL11.glDisable(3042);
/* 241 */     GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
/* 242 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\DrawHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */