/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractMap.SimpleEntry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TreeSet;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.gui.ContainerNothing;
/*     */ import williamle.drones.api.gui.DrawHelper;
/*     */ import williamle.drones.api.gui.GuiButtonSelectColor;
/*     */ import williamle.drones.api.gui.GuiContainerPanel;
/*     */ import williamle.drones.api.gui.PI;
/*     */ import williamle.drones.api.gui.PIObjectColor;
/*     */ import williamle.drones.api.gui.Panel;
/*     */ import williamle.drones.api.model.CMBase;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.drone.DroneAppearance;
/*     */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDronePaint;
/*     */ import williamle.drones.render.DroneModels;
/*     */ import williamle.drones.render.ModelDrone;
/*     */ 
/*     */ public class GuiPainter extends GuiContainerPanel
/*     */ {
/*  38 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/painter.png");
/*     */   public EntityDrone drone;
/*     */   public Panel panelParts;
/*     */   public Panel panelPresetPalettes;
/*     */   public GuiButton buttonApply;
/*     */   
/*     */   public GuiPainter(EntityDrone drone)
/*     */   {
/*  46 */     super(new ContainerNothing());
/*  47 */     this.drone = drone;
/*     */   }
/*     */   
/*     */   public GuiButton buttonReset;
/*     */   public GuiButton buttonModel;
/*     */   
/*  53 */   public void func_73866_w_() { super.func_73866_w_();
/*  54 */     this.field_146292_n.add(this.buttonApply = new GuiButton(0, this.field_146294_l / 2 + 95, this.field_146295_m / 2 + 70, 70, 20, "Apply Color"));
/*  55 */     this.field_146292_n.add(this.buttonSelectRed = new GuiButtonSelectColor(1, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 61, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(1.0D, 0.0D, 0.0D)));
/*     */     
/*  57 */     this.field_146292_n.add(this.buttonSelectGreen = new GuiButtonSelectColor(2, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 41, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(0.0D, 1.0D, 0.0D)));
/*     */     
/*  59 */     this.field_146292_n.add(this.buttonSelectBlue = new GuiButtonSelectColor(3, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 21, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(0.0D, 0.0D, 1.0D)));
/*     */     
/*  61 */     this.field_146292_n.add(this.buttonReset = new GuiButton(4, this.field_146294_l / 2 + 35, this.field_146295_m / 2 + 70, 55, 20, "Reset"));
/*  62 */     this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 167, this.field_146295_m / 2 + 74, 75, 20, "Apply palette"));
/*  63 */     this.field_146292_n.add(this.buttonModel = new GuiButton(6, this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 70, 60, 20, "View model"));
/*     */     
/*  65 */     this.panelParts = new Panel(this, this.field_146294_l / 2 - 102, this.field_146295_m / 2 - 56, 55, 126);
/*  66 */     ModelDrone model = DroneModels.instance.getModelOrDefault(this.drone);
/*  67 */     TreeSet<String> sortedNames = new TreeSet();
/*  68 */     searchPaletteIndexIn(model.models.values(), sortedNames);
/*  69 */     for (String s : sortedNames)
/*     */     {
/*  71 */       Color c = this.drone.droneInfo.appearance.palette.getPaletteColorFor(s);
/*  72 */       PIObjectColor pi = new PIObjectColor(this.panelParts, c);
/*  73 */       pi.defaultColor = ((DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(0)).getPaletteColorFor(s);
/*  74 */       pi.yw = 25.0D;
/*  75 */       pi.displayString = s;
/*  76 */       this.panelParts.addItem(pi);
/*     */     }
/*  78 */     this.panels.add(this.panelParts);
/*     */     
/*  80 */     this.panelPresetPalettes = new Panel(this, this.field_146294_l / 2 - 167, this.field_146295_m / 2 - 56, 55, 126);
/*  81 */     for (int a = 0; a < DroneAppearance.presetPalettes.size(); a++)
/*     */     {
/*  83 */       DroneAppearance.ColorPalette palette = (DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(a);
/*  84 */       PI pi = new PI(this.panelPresetPalettes);
/*  85 */       pi.displayString = palette.paletteName;
/*  86 */       pi.id = a;
/*  87 */       this.panelPresetPalettes.addItem(pi);
/*     */     }
/*  89 */     this.panels.add(this.panelPresetPalettes);
/*     */   }
/*     */   
/*     */   public void searchPaletteIndexIn(Collection<CMBase> cm0, TreeSet<String> sortedNames)
/*     */   {
/*  94 */     for (CMBase cm1 : cm0)
/*     */     {
/*  96 */       List<String> paletteIndexes = new ArrayList(cm1.getPaletteIndexes());
/*  97 */       if ((paletteIndexes != null) && (!sortedNames.containsAll(paletteIndexes)))
/*     */       {
/*  99 */         paletteIndexes.removeAll(sortedNames);
/* 100 */         sortedNames.addAll(paletteIndexes);
/*     */       }
/* 102 */       searchPaletteIndexIn(cm1.childModels, sortedNames);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 109 */     super.func_146284_a(button);
/* 110 */     if (button.field_146127_k == 0)
/*     */     {
/* 112 */       if (this.panelParts.getFirstSelectedItem() != null)
/*     */       {
/* 114 */         PIObjectColor pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
/* 115 */         String toChange = pi.displayString;
/* 116 */         Color ored = this.buttonSelectRed.getOutputColor();
/* 117 */         Color ogreen = this.buttonSelectGreen.getOutputColor();
/* 118 */         Color oblue = this.buttonSelectBlue.getOutputColor();
/* 119 */         Color newColor = new Color(ored.red, ogreen.green, oblue.blue);
/* 120 */         pi.color = newColor;
/* 121 */         PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, toChange, newColor));
/*     */       } }
/*     */     PIObjectColor pi;
/* 124 */     if ((button.field_146127_k == 4) || (button.field_146127_k == 5))
/*     */     {
/* 126 */       if (this.panelParts.getFirstSelectedItem() != null)
/*     */       {
/* 128 */         pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
/* 129 */         String toChange = pi.displayString;
/* 130 */         pi.color = pi.defaultColor;
/* 131 */         PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, toChange, null));
/*     */       }
/*     */     }
/* 134 */     if (button.field_146127_k == 5)
/*     */     {
/* 136 */       if (this.panelPresetPalettes.getFirstSelectedItem() != null)
/*     */       {
/* 138 */         for (PI part : this.panelParts.items)
/*     */         {
/* 140 */           List<Map.Entry<String, Color>> entries = new ArrayList();
/* 141 */           if ((part instanceof PIObjectColor))
/*     */           {
/* 143 */             String name = part.displayString;
/* 144 */             Color color = ((PIObjectColor)part).color;
/* 145 */             entries.add(new AbstractMap.SimpleEntry(name, color));
/*     */           }
/* 147 */           PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, entries));
/*     */         }
/*     */       }
/* 150 */       this.panelPresetPalettes.unselectAll();
/*     */     }
/* 152 */     if (button.field_146127_k == 6)
/*     */     {
/* 154 */       this.panelParts.unselectAll(); }
/*     */   }
/*     */   
/*     */   public GuiButtonSelectColor buttonSelectRed;
/*     */   public GuiButtonSelectColor buttonSelectGreen;
/*     */   public GuiButtonSelectColor buttonSelectBlue;
/*     */   public void itemSelected(Panel panel, PI pi) {
/* 161 */     super.itemSelected(panel, pi);
/* 162 */     if ((panel == this.panelParts) && ((pi instanceof PIObjectColor)))
/*     */     {
/* 164 */       Color c = ((PIObjectColor)pi).color;
/* 165 */       if (c != null)
/*     */       {
/* 167 */         this.buttonSelectRed.selectedIndex = c.red;
/* 168 */         this.buttonSelectGreen.selectedIndex = c.green;
/* 169 */         this.buttonSelectBlue.selectedIndex = c.blue;
/*     */       }
/*     */       else
/*     */       {
/* 173 */         this.buttonSelectRed.selectedIndex = 0.0D;
/* 174 */         this.buttonSelectGreen.selectedIndex = 0.0D;
/* 175 */         this.buttonSelectBlue.selectedIndex = 0.0D;
/*     */       } }
/*     */     DroneAppearance.ColorPalette palette;
/* 178 */     if (panel == this.panelPresetPalettes)
/*     */     {
/* 180 */       int id = pi.id;
/* 181 */       palette = (DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(id);
/* 182 */       for (PI part : this.panelParts.items)
/*     */       {
/* 184 */         if ((part instanceof PIObjectColor))
/*     */         {
/* 186 */           ((PIObjectColor)part).color = palette.getPaletteColorFor(part.displayString);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 195 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 196 */     int sclW = sr.func_78326_a();
/* 197 */     int sclH = sr.func_78328_b();
/* 198 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/* 199 */     func_146110_a(sclW / 2 - 175, sclH / 2 - 100, 0.0F, 0.0F, 350, 200, 350.0F, 200.0F);
/*     */     
/* 201 */     func_73732_a(this.field_146289_q, "Recolor drone", sclW / 2 - 105, sclH / 2 - 93, 16777215);
/* 202 */     func_73732_a(this.field_146289_q, TextFormatting.BOLD + this.drone.droneInfo.getDisplayName(), sclW / 2 - 105, sclH / 2 - 83, 16777215);
/*     */     
/* 204 */     func_73732_a(this.field_146289_q, "Palettes", sclW / 2 - 140, sclH / 2 - 70, 16777215);
/* 205 */     func_73732_a(this.field_146289_q, "Parts", sclW / 2 - 75, sclH / 2 - 70, 16777215);
/*     */     
/* 207 */     if (this.panelParts.getFirstSelectedItem() != null)
/*     */     {
/* 209 */       PIObjectColor pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
/* 210 */       Color currentColor = pi.color;
/* 211 */       this.buttonModel.field_146125_m = (this.buttonReset.field_146125_m = this.buttonApply.field_146125_m = this.buttonSelectBlue.field_146125_m = this.buttonSelectGreen.field_146125_m = this.buttonSelectRed.field_146125_m = 1);
/* 212 */       func_73732_a(this.field_146289_q, TextFormatting.RESET + "Pick color for " + TextFormatting.BOLD + pi.displayString, sclW / 2 + 65, sclH / 2 - 90, 16777215);
/*     */       
/*     */ 
/* 215 */       if (currentColor == null)
/*     */       {
/* 217 */         String s = TextFormatting.ITALIC + "(Using default)";
/* 218 */         this.field_146289_q.func_78276_b(s, sclW / 2 + 65 - this.field_146289_q.func_78256_a(s) / 2, sclH / 2 - 78, -5636096);
/*     */       }
/*     */       
/* 221 */       func_73732_a(this.field_146289_q, "Red", sclW / 2 - 15, sclH / 2 - 60, 16777215);
/* 222 */       func_73732_a(this.field_146289_q, "Green", sclW / 2 - 15, sclH / 2 - 40, 16777215);
/* 223 */       func_73732_a(this.field_146289_q, "Blue", sclW / 2 - 15, sclH / 2 - 20, 16777215);
/*     */       
/* 225 */       Color ored = this.buttonSelectRed.getOutputColor();
/* 226 */       Color ogreen = this.buttonSelectGreen.getOutputColor();
/* 227 */       Color oblue = this.buttonSelectBlue.getOutputColor();
/* 228 */       Color output = new Color(ored.red, ogreen.green, oblue.blue);
/* 229 */       func_73731_b(this.field_146289_q, "" + (int)Math.round(ored.red * 255.0D), sclW / 2 + 135, sclH / 2 - 60, 16777215);
/* 230 */       func_73731_b(this.field_146289_q, "" + (int)Math.round(ogreen.green * 255.0D), sclW / 2 + 135, sclH / 2 - 40, 16777215);
/*     */       
/* 232 */       func_73731_b(this.field_146289_q, "" + (int)Math.round(oblue.blue * 255.0D), sclW / 2 + 135, sclH / 2 - 20, 16777215);
/*     */       
/*     */ 
/* 235 */       func_73732_a(this.field_146289_q, "Old", sclW / 2 + 20, sclH / 2 + 5, 16777215);
/* 236 */       func_73732_a(this.field_146289_q, "->", sclW / 2 + 60, sclH / 2 + 36, 16777215);
/* 237 */       func_73732_a(this.field_146289_q, "New", sclW / 2 + 100, sclH / 2 + 5, 16777215);
/*     */       
/* 239 */       int xo0 = sclW / 2 - 5;
/* 240 */       int xo1 = sclW / 2 + 45;
/* 241 */       int xn0 = sclW / 2 + 75;
/* 242 */       int xn1 = sclW / 2 + 125;
/* 243 */       int y0 = sclH / 2 + 15;
/* 244 */       int y1 = sclH / 2 + 65;
/* 245 */       Color cblack = new Color(0.0D, 0.0D, 0.0D);
/* 246 */       if (currentColor != null) { func_73734_a(xo0, y0, xo1, y1, (int)currentColor.toLong());
/*     */       }
/*     */       else {
/* 249 */         Color cred = new Color(1.0D, 0.0D, 0.0D);
/* 250 */         DrawHelper.drawLine(xo0 + 1, y0 + 1, xo1 - 1, y1 - 1, cred, 4.0F);
/* 251 */         DrawHelper.drawLine(xo1 - 1, y0 + 1, xo0 + 1, y1 - 1, cred, 4.0F);
/*     */       }
/* 253 */       DrawHelper.drawRectMargin(xo0, y0, xo1, y1, 1.0D, (int)cblack.toLong());
/* 254 */       DrawHelper.drawRect(xn0, y0, xn1, y1, (int)output.toLong());
/* 255 */       DrawHelper.drawRectMargin(xn0, y0, xn1, y1, 1.0D, (int)cblack.toLong());
/*     */     }
/*     */     else
/*     */     {
/* 259 */       this.buttonModel.field_146125_m = (this.buttonReset.field_146125_m = this.buttonApply.field_146125_m = this.buttonSelectBlue.field_146125_m = this.buttonSelectGreen.field_146125_m = this.buttonSelectRed.field_146125_m = 0);
/* 260 */       ModelDrone cm = DroneModels.instance.getModelOrDefault(this.drone);
/* 261 */       if (cm != null)
/*     */       {
/* 263 */         GL11.glPushMatrix();
/* 264 */         GL11.glTranslated(sclW / 2 + 65, sclH / 2 + 10, 100.0D);
/* 265 */         GL11.glScaled(100.0D, -100.0D, 100.0D);
/* 266 */         GL11.glRotated(15.0D, 1.0D, 0.0D, 0.0D);
/* 267 */         GL11.glRotated(System.currentTimeMillis() / 40L % 14400L, 0.0D, 1.0D, 0.0D);
/* 268 */         cm.doRender(this.drone, 0.0F, 0.0F, new Object[0]);
/* 269 */         GL11.glPopMatrix();
/*     */       }
/*     */     }
/* 272 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiPainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */