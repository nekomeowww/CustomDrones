/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.drone.module.Module.ModuleGui;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ public class GuiDroneStatus extends GuiContainerModule
/*     */ {
/*     */   public EntityPlayer player;
/*     */   public EntityDrone drone;
/*     */   public SlotModule selectedModSlot;
/*     */   public Module.ModuleGui selectedModGui;
/*     */   
/*     */   public GuiDroneStatus(EntityPlayer p, EntityDrone d)
/*     */   {
/*  27 */     super(new ContainerDroneStatus(d.droneInfo.inventory), d);
/*  28 */     this.player = p;
/*  29 */     this.drone = d;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  35 */     super.func_73866_w_();
/*  36 */     this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 115, 90, 20, "Flyer screen"));
/*  37 */     GuiButton b = new GuiButton(1, this.field_146294_l / 2 - 0, this.field_146295_m / 2 - 115, 90, 20, "Remote screen");
/*  38 */     b.field_146124_l = false;
/*  39 */     this.field_146292_n.add(b);
/*     */     
/*  41 */     int mms = this.drone.droneInfo.getMaxModSlots();
/*  42 */     for (int a = 0; a < mms; a++)
/*     */     {
/*  44 */       this.moduleSlots.add(new SlotModule(a, 90 + (a - mms / 2) * 30, 67, 24, this.drone, a));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  51 */     if ((this.player == null) || (this.player.field_70128_L) || (this.player.func_184614_ca() == null))
/*     */     {
/*  53 */       this.field_146297_k.func_147108_a(null);
/*  54 */       return;
/*     */     }
/*  56 */     super.func_146284_a(button);
/*  57 */     if (button.field_146127_k == 0)
/*     */     {
/*  59 */       EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.player.field_70170_p, this.player.func_184614_ca());
/*  60 */       if (drone != null)
/*     */       {
/*  62 */         this.player.openGui(DronesMod.instance, 0, this.player.field_70170_p, drone.droneInfo.id, 0, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73876_c()
/*     */   {
/*  70 */     super.func_73876_c();
/*  71 */     if (this.selectedModGui != null)
/*     */     {
/*  73 */       this.selectedModGui.func_73876_c();
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146274_d()
/*     */     throws IOException
/*     */   {
/*  80 */     super.func_146274_d();
/*  81 */     if (this.selectedModGui != null)
/*     */     {
/*  83 */       this.selectedModGui.func_146274_d();
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146282_l()
/*     */     throws IOException
/*     */   {
/*  90 */     super.func_146282_l();
/*  91 */     if (this.selectedModGui != null)
/*     */     {
/*  93 */       this.selectedModGui.func_146282_l();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void modClicked(SlotModule slot, int mX, int mY, int mB)
/*     */   {
/* 100 */     super.modClicked(slot, mX, mY, mB);
/* 101 */     if ((slot != null) && (slot.module != null) && (this.selectedModSlot != slot))
/*     */     {
/* 103 */       ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 104 */       if (this.selectedModSlot != null) this.selectedModSlot.overlayColor = -1;
/* 105 */       this.selectedModSlot = slot;
/* 106 */       this.selectedModGui = slot.module.getModuleGui(this);
/* 107 */       this.selectedModGui.modSlot = slot;
/* 108 */       this.selectedModGui.func_73866_w_();
/* 109 */       this.selectedModGui.func_146280_a(this.field_146297_k, sr.func_78326_a(), sr.func_78328_b());
/* 110 */       if (this.selectedModSlot != null) { this.selectedModSlot.overlayColor = -2013265920;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_146280_a(Minecraft mc, int width, int height)
/*     */   {
/* 117 */     super.func_146280_a(mc, width, height);
/* 118 */     if (this.selectedModGui != null)
/*     */     {
/* 120 */       this.selectedModGui.func_146280_a(mc, width, height);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_146278_c(int tint) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_146276_q_() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 139 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneStatus.png");
/*     */   
/*     */   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/* 144 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 145 */     int sclW = sr.func_78326_a();
/* 146 */     int sclH = sr.func_78328_b();
/* 147 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/* 148 */     func_146110_a(sclW / 2 - 150, sclH / 2 - 120, 0.0F, 0.0F, 300, 220, 300.0F, 220.0F);
/*     */     
/* 150 */     DroneInfo di = this.drone.droneInfo;
/*     */     
/*     */ 
/* 153 */     String line1 = TextFormatting.BOLD + di.getDisplayName() + "\n";
/* 154 */     line1 = line1 + TextFormatting.AQUA + "Parts: " + DroneInfo.greekNumber[di.casing] + " , " + DroneInfo.greekNumber[di.chip] + " , " + DroneInfo.greekNumber[di.core] + " , " + DroneInfo.greekNumber[di.engine] + "\n";
/*     */     
/*     */ 
/* 157 */     line1 = line1 + TextFormatting.LIGHT_PURPLE + "Pos: " + Math.round(this.drone.field_70165_t) + " , " + Math.round(this.drone.field_70163_u) + " , " + Math.round(this.drone.field_70161_v) + "\n";
/* 158 */     line1 = line1 + TextFormatting.GREEN + "Battery: " + di.getBattery(true) + "\n";
/* 159 */     line1 = line1 + TextFormatting.YELLOW + "EFT: " + di.getEstimatedFlyTimeString(this.drone);
/* 160 */     List<String> splitLine1 = this.field_146289_q.func_78271_c(line1, 115);
/* 161 */     for (int a = 0; a < splitLine1.size(); a++)
/*     */     {
/* 163 */       this.field_146289_q.func_78276_b((String)splitLine1.get(a), sclW / 2 - 140, sclH / 2 - 88 + a * 10, 16777215);
/*     */     }
/* 165 */     if (this.selectedModGui != null)
/*     */     {
/* 167 */       this.selectedModGui.func_73863_a(mouseX, mouseY, partialTicks);
/*     */     }
/* 169 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiDroneStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */