/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.client.config.GuiSlider;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.path.Path;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.item.ItemDroneFlyer;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneSetEngineLevel;
/*     */ 
/*     */ public class GuiDroneFlyer extends GuiScreen
/*     */ {
/*     */   public World world;
/*     */   public EntityPlayer player;
/*     */   public int frequency;
/*     */   public GuiTextField textNewFrequency;
/*     */   public GuiButton recordButton;
/*     */   public GuiButton statusScreenButton;
/*     */   public GuiSlider engineLevelSlider;
/*     */   
/*     */   public GuiDroneFlyer(World w, EntityPlayer p)
/*     */   {
/*  37 */     this.player = p;
/*  38 */     this.world = this.player.field_70170_p;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_73868_f()
/*     */   {
/*  44 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  50 */     super.func_73866_w_();
/*  51 */     EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/*     */     
/*  53 */     this.textNewFrequency = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 88, 44, 20);
/*  54 */     this.textNewFrequency.func_146180_a(String.valueOf(DronesMod.droneFlyer.getControllerFreq(this.player.func_184614_ca())));
/*  55 */     this.textNewFrequency.func_146195_b(true);
/*  56 */     this.textNewFrequency.func_146203_f(6);
/*  57 */     this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 45, this.field_146295_m / 2 - 88, 55, 20, "Set freq"));
/*  58 */     this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 + 16, this.field_146295_m / 2 - 88, 75, 20, "Switch mode"));
/*     */     
/*  60 */     this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 90, this.field_146295_m / 2 + 68, 75, 20, "Unbind drone"));
/*  61 */     this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 68, 100, 20, "Switch drone mode"));
/*     */     
/*  63 */     this.field_146292_n.add(this.recordButton = new GuiButton(5, this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 90, 100, 20, "Record path"));
/*     */     
/*  65 */     this.field_146292_n.add(this.statusScreenButton = new GuiButton(6, this.field_146294_l / 2 + 0, this.field_146295_m / 2 - 110, 90, 20, "Remote screen"));
/*     */     
/*  67 */     this.field_146292_n.add(
/*  68 */       this.engineLevelSlider = new GuiSlider(7, this.field_146294_l / 2 - 90, this.field_146295_m / 2 + 90, 74, 20, "Engine ", "", 0.0D, 1.0D, drone != null ? Math.round(drone.droneInfo.getEngineLevel() * 100.0D) / 100.0D : 1.0D, true, true));
/*  69 */     this.engineLevelSlider.precision = 2;
/*     */     
/*  71 */     GuiButton b = new GuiButton(8, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 110, 90, 20, "Flyer screen");
/*  72 */     b.field_146124_l = false;
/*  73 */     this.field_146292_n.add(b);
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  79 */     super.func_73869_a(typedChar, keyCode);
/*  80 */     if (this.textNewFrequency.func_146206_l())
/*     */     {
/*  82 */       if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211))
/*     */       {
/*  84 */         this.textNewFrequency.func_146201_a(typedChar, keyCode);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  92 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*  93 */     EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/*  94 */     if (drone != null)
/*     */     {
/*  96 */       if (drone.droneInfo.getEngineLevel() != this.engineLevelSlider.getValue()) { updateEngineLevel(drone);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_146286_b(int mouseX, int mouseY, int state)
/*     */   {
/* 103 */     super.func_146286_b(mouseX, mouseY, state);
/* 104 */     EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/* 105 */     if (drone != null)
/*     */     {
/* 107 */       if (drone.droneInfo.getEngineLevel() != this.engineLevelSlider.getValue()) updateEngineLevel(drone);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateEngineLevel(EntityDrone drone)
/*     */   {
/* 113 */     PacketDispatcher.sendToServer(new PacketDroneSetEngineLevel(drone, this.engineLevelSlider.getValue()));
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 119 */     if ((this.player == null) || (this.player.field_70128_L) || (this.player.func_184614_ca() == null))
/*     */     {
/* 121 */       this.field_146297_k.func_147108_a(null);
/* 122 */       return;
/*     */     }
/* 124 */     super.func_146284_a(button);
/*     */     
/* 126 */     int encode = button.field_146127_k == 1 ? Integer.valueOf(this.textNewFrequency.func_146179_b()).intValue() * -1 : button.field_146127_k;
/* 127 */     PacketDispatcher.sendToServer(new williamle.drones.network.server.PacketDroneControllerChange(encode));
/* 128 */     if (button == this.recordButton)
/*     */     {
/* 130 */       EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/* 131 */       if (drone != null)
/*     */       {
/* 133 */         drone.recordingPath = new Path();
/* 134 */         drone.automatedPath = null;
/*     */       }
/* 136 */       EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Right click" + TextFormatting.RESET + " this controller to add back-and-forth path.");
/*     */       
/* 138 */       EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Left click" + TextFormatting.RESET + " to add loop path.");
/*     */       
/* 140 */       this.field_146297_k.func_147108_a(null);
/*     */     }
/* 142 */     else if (button == this.statusScreenButton)
/*     */     {
/* 144 */       EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/* 145 */       if (drone != null)
/*     */       {
/* 147 */         this.player.openGui(DronesMod.instance, 2, this.world, drone.droneInfo.id, 0, 0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73876_c()
/*     */   {
/* 155 */     super.func_73876_c();
/* 156 */     if ((this.player == null) || (this.player.field_70128_L) || (this.player.func_184614_ca() == null))
/*     */     {
/* 158 */       this.field_146297_k.func_147108_a(null);
/* 159 */       return;
/*     */     }
/* 161 */     EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
/* 162 */     if (drone != null)
/*     */     {
/* 164 */       int droneModeI = drone.getFlyingMode();
/* 165 */       this.recordButton.field_146124_l = ((droneModeI == 3) || (droneModeI == 2));
/* 166 */       this.statusScreenButton.field_146124_l = true;
/*     */     }
/*     */     else
/*     */     {
/* 170 */       this.recordButton.field_146124_l = false;
/* 171 */       this.statusScreenButton.field_146124_l = false;
/*     */     }
/*     */   }
/*     */   
/* 175 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneFlyer.png");
/*     */   
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 180 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 181 */     int sclW = sr.func_78326_a();
/* 182 */     int sclH = sr.func_78328_b();
/* 183 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/* 184 */     func_146110_a(sclW / 2 - 100, sclH / 2 - 115, 0.0F, 0.0F, 200, 230, 200.0F, 230.0F);
/*     */     
/* 186 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */     
/* 188 */     this.textNewFrequency.func_146194_f();
/*     */     
/* 190 */     net.minecraft.item.ItemStack flyerIS = this.player.func_184614_ca();
/* 191 */     int frequency = DronesMod.droneFlyer.getControllerFreq(flyerIS);
/* 192 */     String frequencyS = TextFormatting.RESET + "Frequency: " + TextFormatting.BOLD + frequency + "GHz";
/* 193 */     int flyModeI = DronesMod.droneFlyer.getFlyMode(flyerIS);
/* 194 */     String flyMode = TextFormatting.RESET + "Controller mode: " + TextFormatting.BOLD;
/* 195 */     if (flyModeI == 0) { flyMode = flyMode + "off";
/* 196 */     } else if (flyModeI == 1) { flyMode = flyMode + "targeting";
/* 197 */     } else if (flyModeI == 2) { flyMode = flyMode + "directing";
/* 198 */     } else if (flyModeI == 3) flyMode = flyMode + "buttons"; else
/* 199 */       flyMode = flyMode + "unknown";
/* 200 */     String flyModeDesc = TextFormatting.ITALIC + "";
/* 201 */     if (flyModeI == 0) { flyModeDesc = flyModeDesc + "";
/* 202 */     } else if (flyModeI == 1) { flyModeDesc = flyModeDesc + "Drone flies to crosshair";
/* 203 */     } else if (flyModeI == 2) { flyModeDesc = flyModeDesc + "Drone flies to looking direction";
/* 204 */     } else if (flyModeI == 3) flyModeDesc = flyModeDesc + "Control with WASD, Jump, Sneak"; else
/* 205 */       flyModeDesc = flyModeDesc + "";
/* 206 */     func_73731_b(this.field_146289_q, frequencyS, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 62, 16777215);
/* 207 */     func_73731_b(this.field_146289_q, flyMode, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 51, 16777215);
/* 208 */     func_73731_b(this.field_146289_q, flyModeDesc, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 40, 16777215);
/*     */     
/* 210 */     func_73732_a(this.field_146289_q, TextFormatting.BOLD + "Controller switches", sr.func_78326_a() / 2, sr
/* 211 */       .func_78328_b() / 2 - 17, 16777215);
/* 212 */     func_73732_a(this.field_146289_q, TextFormatting.BOLD + "Drone switches", sr.func_78326_a() / 2, sr
/* 213 */       .func_78328_b() / 2 + 9, 16777215);
/*     */     
/* 215 */     EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, flyerIS);
/* 216 */     String controllingDrone = "";
/* 217 */     if (drone == null) controllingDrone = TextFormatting.RED + "Control not bound to drone"; else
/* 218 */       controllingDrone = TextFormatting.RESET + "Controlling drone " + drone.droneInfo.getDisplayName();
/* 219 */     func_73731_b(this.field_146289_q, controllingDrone, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 28, 16777215);
/*     */     
/* 221 */     if (drone != null)
/*     */     {
/* 223 */       BlockPos pos = new BlockPos(drone);
/* 224 */       String posStr = "At [" + pos.func_177958_n() + " , " + pos.func_177956_o() + " , " + pos.func_177952_p() + "]";
/* 225 */       posStr = posStr + " - " + Math.floor(drone.func_70032_d(this.player) * 10.0D) / 10.0D + "m away";
/* 226 */       String droneMode = "Drone mode: " + drone.getFlyingModeString();
/* 227 */       func_73731_b(this.field_146289_q, posStr, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 39, 16777215);
/* 228 */       func_73731_b(this.field_146289_q, droneMode, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 50, 16777215);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiDroneFlyer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */