/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.Tessellator;
/*     */ import net.minecraft.client.renderer.VertexBuffer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ public class SlotModule
/*     */ {
/*  19 */   public static Minecraft mc = ;
/*     */   
/*     */   public int id;
/*     */   
/*     */   public int posX;
/*     */   public int posY;
/*     */   public int sizeX;
/*     */   public int sizeY;
/*     */   public Module module;
/*     */   public EntityDrone drone;
/*  29 */   public int modIndex = -1;
/*     */   
/*  31 */   public int overlayColor = -1;
/*  32 */   public boolean hovering = false;
/*  33 */   public int hoveringColor = -1996488705;
/*     */   
/*     */   public SlotModule(int i, int x, int y, int size, EntityDrone d, int index)
/*     */   {
/*  37 */     this.id = i;
/*  38 */     this.posX = x;
/*  39 */     this.posY = y;
/*  40 */     this.sizeX = (this.sizeY = size);
/*  41 */     this.drone = d;
/*  42 */     this.modIndex = index;
/*     */   }
/*     */   
/*     */   public SlotModule(int i, int x, int y, int xw, int yw, Module mod)
/*     */   {
/*  47 */     this.id = i;
/*  48 */     this.posX = x;
/*  49 */     this.posY = y;
/*  50 */     this.sizeX = xw;
/*  51 */     this.sizeY = yw;
/*  52 */     this.module = mod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void slotClicked(int mX, int mY, int mBut) {}
/*     */   
/*     */ 
/*     */   public void updateSlot()
/*     */   {
/*  62 */     if ((this.drone != null) && (this.modIndex != -1))
/*     */     {
/*  64 */       if (this.modIndex < this.drone.droneInfo.mods.size())
/*     */       {
/*  66 */         if (this.module != this.drone.droneInfo.mods.get(this.modIndex)) { this.module = ((Module)this.drone.droneInfo.mods.get(this.modIndex));
/*     */         }
/*     */       }
/*     */       else {
/*  70 */         this.module = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void drawModule()
/*     */   {
/*  78 */     drawModule(this.posX, this.posY, this.posX + this.sizeX, this.posY + this.sizeY);
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public void drawModule(double x0, double y0, double x1, double y1)
/*     */   {
/*  84 */     if (mc == null)
/*     */     {
/*  86 */       mc = Minecraft.func_71410_x();
/*     */     }
/*  88 */     if (mc != null)
/*     */     {
/*  90 */       mc.field_71446_o.func_110577_a((this.module == null ? Module.useless1 : this.module).texture);
/*  91 */       GL11.glPushMatrix();
/*  92 */       GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
/*  93 */       GL11.glEnable(3553);
/*  94 */       GL11.glDisable(2896);
/*  95 */       GL11.glBegin(7);
/*  96 */       GL11.glTexCoord2d(0.0D, 0.0D);
/*  97 */       GL11.glVertex2d(x0, y0);
/*  98 */       GL11.glTexCoord2d(0.0D, 1.0D);
/*  99 */       GL11.glVertex2d(x0, y1);
/* 100 */       GL11.glTexCoord2d(1.0D, 1.0D);
/* 101 */       GL11.glVertex2d(x1, y1);
/* 102 */       GL11.glTexCoord2d(1.0D, 0.0D);
/* 103 */       GL11.glVertex2d(x1, y0);
/* 104 */       GL11.glEnd();
/* 105 */       GL11.glDisable(2896);
/* 106 */       GL11.glPopMatrix();
/* 107 */       int marginX = (int)(0.125D * this.sizeX);
/* 108 */       int marginY = (int)(0.125D * this.sizeY);
/* 109 */       if (this.overlayColor != -1)
/*     */       {
/* 111 */         GL11.glPushMatrix();
/* 112 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 113 */         drawRect(this.posX + marginX, this.posY + marginY, this.posX + this.sizeX - marginX, this.posY + this.sizeY - marginY, this.overlayColor);
/* 114 */         GL11.glPopMatrix();
/*     */       }
/* 116 */       if (this.hovering)
/*     */       {
/* 118 */         GL11.glPushMatrix();
/* 119 */         GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/* 120 */         drawRect(this.posX + marginX, this.posY + marginY, this.posX + this.sizeX - marginX, this.posY + this.sizeY - marginY, this.hoveringColor);
/* 121 */         GL11.glPopMatrix();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void drawRect(int left, int top, int right, int bottom, int color)
/*     */   {
/* 128 */     if (left < right)
/*     */     {
/* 130 */       int i = left;
/* 131 */       left = right;
/* 132 */       right = i;
/*     */     }
/*     */     
/* 135 */     if (top < bottom)
/*     */     {
/* 137 */       int j = top;
/* 138 */       top = bottom;
/* 139 */       bottom = j;
/*     */     }
/*     */     
/* 142 */     float f3 = (color >> 24 & 0xFF) / 255.0F;
/* 143 */     float f = (color >> 16 & 0xFF) / 255.0F;
/* 144 */     float f1 = (color >> 8 & 0xFF) / 255.0F;
/* 145 */     float f2 = (color & 0xFF) / 255.0F;
/* 146 */     Tessellator tessellator = Tessellator.func_178181_a();
/* 147 */     VertexBuffer worldrenderer = tessellator.func_178180_c();
/* 148 */     GlStateManager.func_179147_l();
/* 149 */     GlStateManager.func_179090_x();
/* 150 */     GlStateManager.func_179120_a(770, 771, 1, 0);
/* 151 */     GlStateManager.func_179131_c(f, f1, f2, f3);
/* 152 */     worldrenderer.func_181668_a(7, DefaultVertexFormats.field_181705_e);
/* 153 */     worldrenderer.func_181662_b(left, bottom, 0.0D).func_181675_d();
/* 154 */     worldrenderer.func_181662_b(right, bottom, 0.0D).func_181675_d();
/* 155 */     worldrenderer.func_181662_b(right, top, 0.0D).func_181675_d();
/* 156 */     worldrenderer.func_181662_b(left, top, 0.0D).func_181675_d();
/* 157 */     tessellator.func_78381_a();
/* 158 */     GlStateManager.func_179098_w();
/* 159 */     GlStateManager.func_179084_k();
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\SlotModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */