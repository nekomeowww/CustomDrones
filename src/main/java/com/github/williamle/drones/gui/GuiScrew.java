/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.gui.ContainerNothing;
/*     */ import williamle.drones.api.gui.DrawHelper;
/*     */ import williamle.drones.api.gui.GuiContainerPanel;
/*     */ import williamle.drones.api.gui.PI;
/*     */ import williamle.drones.api.gui.Panel;
/*     */ import williamle.drones.drone.DroneAppearance;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneScrew;
/*     */ import williamle.drones.render.DroneModels;
/*     */ import williamle.drones.render.DroneModels.ModelProp;
/*     */ import williamle.drones.render.ModelDrone;
/*     */ 
/*     */ 
/*     */ public class GuiScrew
/*     */   extends GuiContainerPanel
/*     */ {
/*  30 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/screw.png");
/*     */   public EntityDrone drone;
/*     */   public Panel panelModels;
/*     */   public GuiButton buttonApply;
/*     */   public GuiButton buttonReset;
/*     */   
/*     */   public GuiScrew(EntityDrone drone) {
/*  37 */     super(new ContainerNothing());
/*  38 */     this.drone = drone;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  44 */     super.func_73866_w_();
/*  45 */     this.field_146292_n.add(this.buttonApply = new GuiButton(0, this.field_146294_l / 2 + 70, this.field_146295_m / 2 + 70, 70, 20, "Apply"));
/*  46 */     this.field_146292_n.add(this.buttonReset = new GuiButton(1, this.field_146294_l / 2 + 10, this.field_146295_m / 2 + 70, 55, 20, "Reset"));
/*     */     
/*  48 */     this.panelModels = new Panel(this, this.field_146294_l / 2 - 137, this.field_146295_m / 2 - 63, 60, 150);
/*  49 */     for (int a = 0; a < DroneModels.instance.models.size(); a++)
/*     */     {
/*  51 */       DroneModels.ModelProp mp = (DroneModels.ModelProp)DroneModels.instance.models.get(a);
/*  52 */       if (!mp.isMobModel)
/*     */       {
/*  54 */         PI pi = new PI(this.panelModels);
/*  55 */         pi.displayString = mp.model.name;
/*  56 */         pi.id = mp.id;
/*  57 */         this.panelModels.addItem(pi);
/*     */       }
/*     */     }
/*  60 */     this.panels.add(this.panelModels);
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  66 */     super.func_146284_a(button);
/*  67 */     int newModel = -1;
/*  68 */     if (button.field_146127_k == 0)
/*     */     {
/*  70 */       if (this.panelModels.getFirstSelectedItem() != null)
/*     */       {
/*  72 */         newModel = this.panelModels.getFirstSelectedItem().id;
/*     */       }
/*     */     }
/*  75 */     else if (button.field_146127_k == 1)
/*     */     {
/*  77 */       newModel = 0;
/*     */     }
/*  79 */     if ((newModel != -1) && (newModel != this.drone.droneInfo.appearance.modelID))
/*     */     {
/*  81 */       this.drone.droneInfo.appearance.modelID = newModel;
/*  82 */       PacketDispatcher.sendToServer(new PacketDroneScrew(this.drone, newModel));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  90 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/*  91 */     int sclW = sr.func_78326_a();
/*  92 */     int sclH = sr.func_78328_b();
/*  93 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/*  94 */     func_146110_a(sclW / 2 - 150, sclH / 2 - 100, 0.0F, 0.0F, 300, 200, 300.0F, 200.0F);
/*     */     
/*  96 */     func_73732_a(this.field_146289_q, TextFormatting.RESET + "Remodel drone " + TextFormatting.BOLD + this.drone.droneInfo
/*  97 */       .getDisplayName(), sclW / 2, sclH / 2 - 90, 16777215);
/*     */     
/*  99 */     func_73732_a(this.field_146289_q, "Models", sclW / 2 - 108, sclH / 2 - 75, 16777215);
/* 100 */     func_73732_a(this.field_146289_q, "Current", sclW / 2 - 30, sclH / 2 - 50, 16777215);
/* 101 */     func_73732_a(this.field_146289_q, "model", sclW / 2 - 30, sclH / 2 - 40, 16777215);
/* 102 */     func_73732_a(this.field_146289_q, "Selected", sclW / 2 - 30, sclH / 2 + 20, 16777215);
/* 103 */     func_73732_a(this.field_146289_q, "model", sclW / 2 - 30, sclH / 2 + 30, 16777215);
/* 104 */     DrawHelper.drawRect(sclW / 2 - 0, sclH / 2 - 70, sclW / 2 + 140, sclH / 2 - 10, -12237499L);
/* 105 */     DrawHelper.drawRect(sclW / 2 - 0, sclH / 2 - 0, sclW / 2 + 140, sclH / 2 + 60, -12237499L);
/*     */     
/* 107 */     ModelDrone cm = DroneModels.instance.getModelOrDefault(this.drone);
/* 108 */     if (cm != null)
/*     */     {
/* 110 */       renderDrone(cm, sclW / 2 + 70, sclH / 2 - 30);
/*     */     }
/* 112 */     if (this.panelModels.getFirstSelectedItem() != null)
/*     */     {
/* 114 */       ModelDrone cm1 = DroneModels.instance.getModelOrDefault(this.panelModels.getFirstSelectedItem().id);
/* 115 */       if (cm1 != null)
/*     */       {
/* 117 */         renderDrone(cm1, sclW / 2 + 70, sclH / 2 + 40);
/*     */       }
/*     */     }
/* 120 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */   public void renderDrone(ModelDrone cm, double x, double y)
/*     */   {
/* 125 */     GL11.glPushMatrix();
/* 126 */     GL11.glTranslated(x, y, 100.0D);
/* 127 */     GL11.glScaled(60.0D, -60.0D, 60.0D);
/* 128 */     GL11.glRotated(15.0D, 1.0D, 0.0D, 0.0D);
/* 129 */     GL11.glRotated(System.currentTimeMillis() / 40L % 14400L, 0.0D, 1.0D, 0.0D);
/* 130 */     cm.doRender(null, 0.0F, 1.0F, new Object[0]);
/* 131 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiScrew.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */