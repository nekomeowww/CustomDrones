/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.DroneInfo.ApplyResult;
/*     */ import williamle.drones.drone.DroneInfo.ApplyType;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.item.ItemDroneModule;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneGuiApplyItem;
/*     */ import williamle.drones.network.server.PacketDroneItemize;
/*     */ import williamle.drones.network.server.PacketDroneRename;
/*     */ 
/*     */ public class GuiDrone extends GuiContainerModule
/*     */ {
/*     */   public EntityPlayer player;
/*     */   public GuiTextField renameField;
/*     */   public GuiButton itemApplyButton;
/*  35 */   public String itemApplyStatus = "";
/*     */   
/*     */   public GuiDrone(EntityPlayer p, EntityDrone d)
/*     */   {
/*  39 */     super(new ContainerDrone(p.field_71071_by, d.droneInfo.inventory), d);
/*  40 */     this.player = p;
/*  41 */     this.drone = d;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  47 */     super.func_73866_w_();
/*  48 */     this.renameField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 + 81, this.field_146295_m / 2 - 95, 107, 20);
/*  49 */     this.renameField.func_146180_a(this.drone.droneInfo.getDisplayName());
/*  50 */     this.renameField.func_146203_f(20);
/*  51 */     this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 27, this.field_146295_m / 2 - 95, 50, 20, "Rename"));
/*  52 */     this.field_146292_n.add(this.itemApplyButton = new GuiButton(1, this.field_146294_l / 2 - 75, this.field_146295_m / 2 - 38, 65, 20, "Put item ->"));
/*  53 */     this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 200, this.field_146295_m / 2 + 80, 50, 20, "Itemize"));
/*  54 */     this.itemApplyButton.field_146124_l = false;
/*  55 */     for (int a = 0; a < this.drone.droneInfo.getMaxModSlots(); a++)
/*     */     {
/*  57 */       this.moduleSlots.add(new SlotModule(a, -46 + a % 4 * 36, 122 + (int)Math.floor(a / 4.0D) * 28, 24, this.drone, a));
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  64 */     if (!this.renameField.func_146201_a(typedChar, keyCode))
/*     */     {
/*  66 */       super.func_73869_a(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  73 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*  74 */     this.renameField.func_146192_a(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/*  80 */     super.func_146284_a(button);
/*  81 */     if (button == this.itemApplyButton)
/*     */     {
/*  83 */       this.itemApplyStatus = "";
/*  84 */       Slot applySlot = this.field_147002_h.func_75147_a(((ContainerDrone)this.field_147002_h).module, 0);
/*  85 */       if (applySlot.func_75216_d())
/*     */       {
/*  87 */         ItemStack is = applySlot.func_75211_c();
/*  88 */         Item item = is.func_77973_b();
/*  89 */         DroneInfo.ApplyResult addItemEntry = this.drone.droneInfo.canApplyStack(is);
/*  90 */         if (addItemEntry.type != DroneInfo.ApplyType.NONE)
/*     */         {
/*  92 */           this.itemApplyStatus = addItemEntry.displayString;
/*  93 */           boolean addable = addItemEntry.successful;
/*  94 */           if (addable)
/*     */           {
/*  96 */             PacketDispatcher.sendToServer(new PacketDroneGuiApplyItem(this.drone));
/*  97 */             applySlot.func_75215_d(null);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 102 */     else if ((button.field_146127_k == 0) && (!this.renameField.func_146179_b().isEmpty()))
/*     */     {
/* 104 */       PacketDispatcher.sendToServer(new PacketDroneRename(this.drone, this.renameField.func_146179_b()));
/*     */     }
/* 106 */     else if (button.field_146127_k == 2)
/*     */     {
/* 108 */       PacketDispatcher.sendToServer(new PacketDroneItemize(this.drone));
/* 109 */       this.field_146297_k.func_147108_a(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73876_c()
/*     */   {
/* 116 */     Slot applySlot = this.field_147002_h.func_75147_a(((ContainerDrone)this.field_147002_h).module, 0);
/* 117 */     if (!applySlot.func_75216_d())
/*     */     {
/* 119 */       this.itemApplyButton.field_146124_l = false;
/* 120 */       this.itemApplyButton.field_146126_j = "Put item ->";
/*     */     }
/*     */     else
/*     */     {
/* 124 */       ItemStack is = applySlot.func_75211_c();
/* 125 */       Item item = is.func_77973_b();
/* 126 */       if (item == DronesMod.droneModule)
/*     */       {
/* 128 */         this.itemApplyButton.field_146124_l = true;
/* 129 */         this.itemApplyButton.field_146126_j = "Install mod";
/*     */       }
/* 131 */       else if (this.drone.droneInfo.canApplyStack(is).type != DroneInfo.ApplyType.NONE)
/*     */       {
/* 133 */         this.itemApplyButton.field_146124_l = true;
/* 134 */         this.itemApplyButton.field_146126_j = "Apply item";
/*     */       }
/*     */       else
/*     */       {
/* 138 */         this.itemApplyButton.field_146124_l = false;
/* 139 */         this.itemApplyButton.field_146126_j = "No use";
/*     */       }
/*     */     }
/* 142 */     for (int a = 0; a < this.moduleSlots.size(); a++)
/*     */     {
/* 144 */       SlotModule slot = (SlotModule)this.moduleSlots.get(a);
/* 145 */       slot.overlayColor = -1;
/* 146 */       if (applySlot.func_75216_d())
/*     */       {
/* 148 */         if (applySlot.func_75211_c().func_77973_b() == DronesMod.droneModule)
/*     */         {
/* 150 */           Module installingModule = ItemDroneModule.getModule(applySlot.func_75211_c());
/* 151 */           Module thisModule = ((SlotModule)this.moduleSlots.get(a)).module;
/* 152 */           if (thisModule != null)
/*     */           {
/*     */ 
/*     */ 
/* 156 */             if (thisModule.canFunctionAs(installingModule))
/*     */             {
/* 158 */               slot.overlayColor = 1157562368;
/*     */             }
/* 160 */             else if (installingModule.canReplace(thisModule))
/*     */             {
/* 162 */               slot.overlayColor = 1140915968; }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 167 */     super.func_73876_c();
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_146281_b()
/*     */   {
/* 173 */     super.func_146281_b();
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
/*     */ 
/* 192 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/drone.png");
/*     */   
/*     */   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}
/*     */   
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
/* 197 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 198 */     int sclW = sr.func_78326_a();
/* 199 */     int sclH = sr.func_78328_b();
/* 200 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/* 201 */     func_146110_a(sclW / 2 - 200, sclH / 2 - 100, 0.0F, 0.0F, 400, 200, 400.0F, 200.0F);
/* 202 */     this.renameField.func_146194_f();
/*     */     
/* 204 */     DroneInfo di = this.drone.droneInfo;
/*     */     
/* 206 */     func_73734_a(sclW / 2 + 27, sclH / 2 - 73 + di.getInvSize() * 2, sclW / 2 + 189, sclH / 2 - 1, -3750202);
/*     */     
/* 208 */     this.field_146289_q.func_78276_b("Drone " + TextFormatting.BOLD + di.getDisplayName(), sclW / 2 - 185, sclH / 2 - 90, 0);
/*     */     
/* 210 */     this.field_146289_q.func_78276_b("Drone's", sclW / 2 + 27, sclH / 2 - 71 + di.getInvSize() * 2, 4473924);
/* 211 */     this.field_146289_q.func_78276_b("Player's", sclW / 2 + 147, sclH / 2 + 8, 4473924);
/*     */     
/*     */ 
/* 214 */     String line1 = TextFormatting.AQUA + "Casing: " + DroneInfo.greekNumber[di.casing];
/* 215 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 216 */     line1 = line1 + TextFormatting.AQUA + "Chip: " + DroneInfo.greekNumber[di.chip];
/* 217 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 218 */     line1 = line1 + TextFormatting.AQUA + "Core: " + DroneInfo.greekNumber[di.core];
/* 219 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 220 */     line1 = line1 + TextFormatting.AQUA + "Engine: " + DroneInfo.greekNumber[di.engine];
/* 221 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 222 */     line1 = line1 + TextFormatting.YELLOW + "Freq: " + (di.droneFreq >= 0 ? di.droneFreq + "GHz" : "None");
/* 223 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 224 */     line1 = line1 + TextFormatting.YELLOW + "Mode: " + this.drone.getFlyingModeString();
/* 225 */     line1 = line1 + TextFormatting.WHITE + " - ";
/* 226 */     line1 = line1 + TextFormatting.YELLOW + "Est. fly time: " + di.getEstimatedFlyTimeString(this.drone);
/* 227 */     List<String> splitLine1 = this.field_146289_q.func_78271_c(line1, 200);
/* 228 */     for (int a = 0; a < splitLine1.size(); a++)
/*     */     {
/* 230 */       this.field_146289_q.func_78276_b((String)splitLine1.get(a), sclW / 2 - 186, sclH / 2 - 77 + a * 10, 16777215);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 235 */     String line2 = "Mods: " + TextFormatting.GREEN + di.mods.size() + "/" + di.getMaxModSlots();
/* 236 */     this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 35, 16777215);
/* 237 */     line2 = "Max mod rank: " + TextFormatting.GREEN + di.getMaxModLevel();
/* 238 */     this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 25, 16777215);
/* 239 */     line2 = "Max speed: " + TextFormatting.GREEN + Math.round(di.getMaxSpeed() * di.getEngineLevel()) + "m/s";
/* 240 */     this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 15, 16777215);
/* 241 */     line2 = "Health: " + TextFormatting.GREEN + di.getDamage(true) + "/" + di.getMaxDamage(this.drone);
/* 242 */     this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 5, 16777215);
/* 243 */     line2 = "Battery: " + TextFormatting.GREEN + di.getBattery(true) + "/" + di.getMaxBattery();
/* 244 */     this.field_146289_q.func_78279_b(line2, sclW / 2 - 185, sclH / 2 + 5, 105, 16777215);
/*     */     
/*     */ 
/* 247 */     this.field_146289_q.func_78276_b("Modules", sclW / 2 - 180, sclH / 2 + 60, 0);
/*     */     
/*     */ 
/* 250 */     if (!this.itemApplyStatus.isEmpty())
/*     */     {
/* 252 */       this.field_146289_q.func_78279_b(this.itemApplyStatus, sclW / 2 - 73, sclH / 2 - 8, 80, 4473924);
/*     */     }
/* 254 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */