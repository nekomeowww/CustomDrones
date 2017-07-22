/*     */ package williamle.drones.api.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.client.renderer.RenderItem;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.item.ItemGunUpgrade;
/*     */ 
/*     */ public class PIItemStack extends PI
/*     */ {
/*     */   public ItemStack is;
/*     */   
/*     */   public PIItemStack(Panel p, ItemStack item)
/*     */   {
/*  22 */     super(p);
/*  23 */     this.is = item;
/*     */   }
/*     */   
/*     */ 
/*     */   public void mouseOverItem(int mxlocal, int mylocal, boolean drawing)
/*     */   {
/*  29 */     super.mouseOverItem(mxlocal, mylocal, drawing);
/*  30 */     if ((drawing) && (this.is != null))
/*     */     {
/*  32 */       renderTooltip(this.is, mxlocal, mylocal);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void drawItemContent()
/*     */   {
/*  39 */     String desc = getDescStringForIS(this.is);
/*  40 */     int stringLength = this.fontRenderer.func_78256_a(desc);
/*  41 */     int totalLength = 16 + stringLength + 4;
/*  42 */     int maxStringLength = (int)Math.floor(this.xw - this.margin) - 20;
/*  43 */     totalLength = 0;
/*  44 */     List<String> strings = this.fontRenderer.func_78271_c(desc, maxStringLength);
/*  45 */     for (String s : strings)
/*     */     {
/*  47 */       int slength = this.fontRenderer.func_78256_a(s.trim());
/*  48 */       totalLength = Math.max(totalLength, 16 + slength + 4);
/*     */     }
/*  50 */     this.yw = Math.max(this.yw, strings.size() * 10 + 20);
/*     */     
/*  52 */     GL11.glPushMatrix();
/*  53 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/*  54 */     renderItem(this.is, (int)(this.xw - totalLength) / 2, (int)this.yw / 2 - 8);
/*  55 */     for (int a = 0; a < strings.size(); a++)
/*     */     {
/*  57 */       String s = (String)strings.get(a);
/*  58 */       this.fontRenderer.func_175065_a(s, (int)(this.xw - totalLength) / 2 + 18, (int)this.yw / 2 - 5 * strings.size() + 10 * a, 
/*  59 */         (int)this.strColor.toLong(), this.stringShadow);
/*     */     }
/*  61 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/*  62 */     GL11.glPopMatrix();
/*     */   }
/*     */   
/*     */   public String getDescStringForIS(ItemStack is)
/*     */   {
/*  67 */     if (is == null) return "";
/*  68 */     if (is.func_77973_b() == DronesMod.droneModule)
/*     */     {
/*  70 */       return williamle.drones.item.ItemDroneModule.getModule(is).displayName;
/*     */     }
/*  72 */     if (is.func_77973_b() == DronesMod.gunUpgrade)
/*     */     {
/*  74 */       ((ItemGunUpgrade)DronesMod.gunUpgrade);return ItemGunUpgrade.getGunUpgrade(is).name;
/*     */     }
/*  76 */     if (is.func_77973_b() == DronesMod.droneSpawn)
/*     */     {
/*  78 */       williamle.drones.drone.DroneInfo di = DronesMod.droneSpawn.getDroneInfo(is);
/*  79 */       return williamle.drones.drone.DroneInfo.greekNumber[di.casing] + " " + williamle.drones.drone.DroneInfo.greekNumber[di.chip] + " " + williamle.drones.drone.DroneInfo.greekNumber[di.core] + " " + williamle.drones.drone.DroneInfo.greekNumber[di.engine];
/*     */     }
/*     */     
/*  82 */     return is.func_82833_r();
/*     */   }
/*     */   
/*     */   public void renderItem(ItemStack is, int i, int j)
/*     */   {
/*  87 */     if ((is != null) && (is.func_77973_b() != null))
/*     */     {
/*  89 */       GL11.glPopMatrix();
/*  90 */       RenderHelper.func_74520_c();
/*  91 */       renderItem().func_175042_a(is, i, j);
/*  92 */       renderItem().func_180453_a(this.panel.mc.field_71466_p, is, i, j, null);
/*  93 */       RenderHelper.func_74518_a();
/*  94 */       GL11.glPushMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public RenderItem renderItem()
/*     */   {
/* 100 */     return this.panel.mc.func_175599_af();
/*     */   }
/*     */   
/*     */   public void renderTooltip(ItemStack stack, int x, int y)
/*     */   {
/* 105 */     List<String> list = stack.func_82840_a(this.panel.mc.field_71439_g, this.panel.mc.field_71474_y.field_82882_x);
/*     */     
/* 107 */     for (int i = 0; i < list.size(); i++)
/*     */     {
/* 109 */       if (i == 0)
/*     */       {
/* 111 */         list.set(i, stack.func_77953_t().field_77937_e + (String)list.get(i));
/*     */       }
/*     */       else
/*     */       {
/* 115 */         list.set(i, net.minecraft.util.text.TextFormatting.GRAY + (String)list.get(i));
/*     */       }
/*     */     }
/*     */     
/* 119 */     FontRenderer font = stack.func_77973_b().getFontRenderer(stack);
/* 120 */     DrawHelper.drawHoveringText(list, x, y, font == null ? this.panel.mc.field_71466_p : font, this.panel.mc.field_71443_c, y);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\PIItemStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */