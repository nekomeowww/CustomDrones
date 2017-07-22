/*     */ package williamle.drones.api.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.model.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PI
/*     */ {
/*     */   public Panel panel;
/*     */   public int id;
/*     */   public double xw;
/*     */   public double yw;
/*     */   public double margin;
/*  20 */   public Color bgColor = new Color(0.7D, 0.7D, 0.7D);
/*  21 */   public Color bgMargin = new Color(0.2D, 0.2D, 0.2D).setNextColor(new Color(0.0D, 0.0D, 0.0D));
/*     */   public Color fgColor;
/*     */   public Color fgMargin;
/*  24 */   public Color strColor = new Color(1.0D, 1.0D, 1.0D);
/*  25 */   public boolean stringShadow = true;
/*     */   public String displayString;
/*  27 */   public boolean fitHorizontal = false;
/*     */   
/*     */   public boolean selected;
/*     */   
/*     */   public boolean negated;
/*     */   public FontRenderer fontRenderer;
/*     */   
/*     */   public PI(Panel p)
/*     */   {
/*  36 */     this.panel = p;
/*  37 */     this.fontRenderer = p.parent.field_146297_k.field_71466_p;
/*  38 */     this.xw = p.pw;
/*  39 */     if (p.scrollerAlwaysOn) this.xw = (p.pw - p.scrollerSize);
/*  40 */     this.yw = 30.0D;
/*  41 */     this.margin = 1.0D;
/*     */   }
/*     */   
/*     */   public PI setSize(double x, double y)
/*     */   {
/*  46 */     this.xw = x;
/*  47 */     this.yw = y;
/*  48 */     return this;
/*     */   }
/*     */   
/*     */   public void disable()
/*     */   {
/*  53 */     this.negated = true;
/*  54 */     this.panel.itemDisabled(this);
/*     */   }
/*     */   
/*     */   public void enable()
/*     */   {
/*  59 */     this.negated = false;
/*  60 */     this.panel.itemEnabled(this);
/*     */   }
/*     */   
/*     */   public void unselect()
/*     */   {
/*  65 */     this.selected = false;
/*  66 */     this.panel.itemUnselected(this);
/*     */   }
/*     */   
/*     */   public void select()
/*     */   {
/*  71 */     this.selected = true;
/*  72 */     this.panel.itemSelected(this);
/*     */   }
/*     */   
/*     */   public int midColor()
/*     */   {
/*  77 */     if (this.selected) return -16711936;
/*  78 */     if (this.negated) return -65536;
/*  79 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void mouseOverItem(int mxlocal, int mylocal, boolean drawing) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void updateItem() {}
/*     */   
/*     */ 
/*     */   public void drawItem()
/*     */   {
/*  93 */     GL11.glPushMatrix();
/*  94 */     Minecraft mc = this.panel.mc;
/*  95 */     ScaledResolution sr = new ScaledResolution(mc);
/*  96 */     int sclh = sr.func_78328_b();
/*  97 */     int sclw = sr.func_78326_a();
/*  98 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/*  99 */     DrawHelper.drawGradientRect(0.0D, 0.0D, this.xw, this.yw, this.bgColor, DrawHelper.nextColor(this.bgColor));
/* 100 */     DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, this.bgMargin, DrawHelper.nextColor(this.bgMargin));
/* 101 */     drawItemContent();
/* 102 */     DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, midColor(), midColor());
/* 103 */     DrawHelper.drawGradientRect(0.0D, 0.0D, this.xw, this.yw, this.fgColor, DrawHelper.nextColor(this.fgColor));
/* 104 */     DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, this.fgMargin, DrawHelper.nextColor(this.fgMargin));
/* 105 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public void drawItemContent()
/*     */   {
/* 110 */     int stringLength = this.fontRenderer.func_78256_a(this.displayString);
/* 111 */     int textMargin = (int)(this.margin * 2.0D + 8.0D);
/* 112 */     int totalLength = stringLength + textMargin;
/* 113 */     int maxStringLength = (int)Math.floor(this.xw - this.margin) - textMargin;
/* 114 */     if ((this.fitHorizontal) && (stringLength > maxStringLength))
/*     */     {
/* 116 */       GL11.glPushMatrix();
/* 117 */       GL11.glTranslated((this.xw - maxStringLength) / 2.0D, 0.0D, 0.0D);
/* 118 */       GL11.glScaled(maxStringLength / stringLength, 1.0D, 1.0D);
/* 119 */       this.fontRenderer.func_175065_a(this.displayString, 0.0F, (int)(this.yw - additionalContentY()) / 2 - 5, 
/* 120 */         (int)this.strColor.toLong(), this.stringShadow);
/* 121 */       GL11.glPopMatrix();
/*     */     }
/*     */     else
/*     */     {
/* 125 */       totalLength = 0;
/* 126 */       List<String> strings = this.fontRenderer.func_78271_c(this.displayString, maxStringLength);
/* 127 */       for (String s : strings)
/*     */       {
/* 129 */         int slength = this.fontRenderer.func_78256_a(s.trim());
/* 130 */         totalLength = Math.max(totalLength, slength + textMargin);
/*     */       }
/* 132 */       this.yw = Math.max(this.yw, strings.size() * 10 + textMargin + additionalContentY());
/* 133 */       for (int a = 0; a < strings.size(); a++)
/*     */       {
/* 135 */         String s = (String)strings.get(a);
/* 136 */         int thisLength = this.fontRenderer.func_78256_a(s);
/* 137 */         this.fontRenderer.func_175065_a(s, (int)(this.xw - thisLength) / 2, 
/* 138 */           (int)(this.yw - additionalContentY()) / 2 - 5 * strings.size() + 10 * a, (int)this.strColor.toLong(), this.stringShadow);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int additionalContentY()
/*     */   {
/* 146 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\PI.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */