/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiScreen;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ import williamle.drones.gui.SlotModule;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneSwitchMod;
/*     */ import williamle.drones.network.server.PacketDroneUninstallMod;
/*     */ 
/*     */ 
/*     */ public class Module
/*     */ {
/*  32 */   public static int texturesPerRow = 12;
/*     */   
/*  34 */   public static final List<Module> modules = new ArrayList();
/*  35 */   public static final Module useless1 = new ModulePlaceHolder(1, "Place holder 1").setID("usl1");
/*  36 */   public static final Module useless2 = new ModulePlaceHolder(2, "Place holder 2").setID("usl2");
/*  37 */   public static final Module useless3 = new ModulePlaceHolder(3, "Place holder 3").setID("usl3");
/*  38 */   public static final Module useless4 = new ModulePlaceHolder(4, "Place holder 4").setID("usl4");
/*  39 */   public static final Module itemInventory = new ModuleTransport(1, "Items Inventory").setTexture("itemInventory")
/*  40 */     .setID("tra1");
/*  41 */   public static final Module nplayerTransport = new ModuleTransport(2, "Non-player Transporting")
/*  42 */     .setTexture("nplayerTransport").setID("tra2");
/*  43 */   public static final Module playerTransport = new ModuleTransport(3, "Player Transporting")
/*  44 */     .setTexture("playerTransport").setID("tra3");
/*  45 */   public static final Module multiTransport = new ModuleTransport(4, "Multi Transporting").setID("tra4")
/*  46 */     .setUndergrade(new Module[] { itemInventory, nplayerTransport, playerTransport }).setTexture("multiTransport");
/*  47 */   public static final Module itemCollect = new ModuleCollect(1, "Items Collecting").setTexture("itemCollect")
/*  48 */     .setID("col1");
/*  49 */   public static final Module xpCollect = new ModuleCollect(2, "XP Collecting").setTexture("xpCollect").setID("col2");
/*  50 */   public static final Module multiCollect = new ModuleCollect(3, "Multi Collecting").setID("col3")
/*  51 */     .setUndergrade(new Module[] { itemCollect, xpCollect }).setTexture("multiCollect");
/*  52 */   public static final Module chestDeposit = new ModuleChestDeposit(2, "Chest Depositing").setTexture("chestDeposit")
/*  53 */     .setID("dep1");
/*  54 */   public static final Module mobScan1 = new ModuleScan(1, "Mobs Scanning I").setTexture("mobScan1").setID("sca1");
/*  55 */   public static final Module mobScan2 = new ModuleScan(2, "Mobs Scanning II").setUndergrade(new Module[] { mobScan1 }).setID("sca2")
/*  56 */     .setTexture("mobScan2");
/*  57 */   public static final Module oreScan = new ModuleScan(1, "Ore Scanning").setTexture("oreScan").setID("sca3");
/*  58 */   public static final Module multiScan = new ModuleScan(2, "Multi Scanning").setUndergrade(new Module[] { mobScan2, oreScan })
/*  59 */     .setTexture("multiScan").setID("sca4");
/*  60 */   public static final Module controlMovement = new ModuleMovement(1, "Manual Control Movement")
/*  61 */     .setTexture("controlMovement").setID("mov1");
/*  62 */   public static final Module pathMovement = new ModuleMovement(1, "Preset Path Movement").setTexture("pathMovement")
/*  63 */     .setID("mov2");
/*  64 */   public static final Module followMovement = new ModuleMovement(1, "Follow Movement").setTexture("followMovement")
/*  65 */     .setID("mov3");
/*  66 */   public static final Module multiMovement = new ModuleMovement(2, "Multi Movement").setID("mov4")
/*  67 */     .setUndergrade(new Module[] { pathMovement, followMovement, controlMovement }).setTexture("multiMovement");
/*  68 */   public static final Module camera = new ModuleCamera(3, "Camera").setTexture("camera").setID("cam1");
/*  69 */   public static final Module weapon1 = new ModuleWeapon(1, "Weapon I").setTexture("weapon1").setID("wea1");
/*  70 */   public static final Module weapon2 = new ModuleWeapon(2, "Weapon II").setUndergrade(new Module[] { weapon1 }).setTexture("weapon2")
/*  71 */     .setID("wea2");
/*  72 */   public static final Module weapon3 = new ModuleWeapon(3, "Weapon III").setUndergrade(new Module[] { weapon2 }).setTexture("weapon3")
/*  73 */     .setID("wea3");
/*  74 */   public static final Module weapon4 = new ModuleWeapon(4, "Weapon IV").setUndergrade(new Module[] { weapon3 }).setTexture("weapon4")
/*  75 */     .setID("wea4");
/*  76 */   public static final Module armor1 = new ModuleArmor(1, "Armor I").setTexture("armor1").setID("arm1");
/*  77 */   public static final Module armor2 = new ModuleArmor(2, "Armor II").setUndergrade(new Module[] { armor1 }).setTexture("armor2")
/*  78 */     .setID("arm2");
/*  79 */   public static final Module armor3 = new ModuleArmor(3, "Armor III").setUndergrade(new Module[] { armor2 }).setTexture("armor3")
/*  80 */     .setID("arm3");
/*  81 */   public static final Module armor4 = new ModuleArmor(4, "Armor IV").setUndergrade(new Module[] { armor3 }).setTexture("armor4")
/*  82 */     .setID("arm4");
/*  83 */   public static final Module shooting = new ModuleShooting(1, "Shooting").setTexture("shooting").setID("sho1");
/*  84 */   public static final Module shootingHoming = new ModuleShooting(2, "Shooting Homing").setTexture("shootingHoming")
/*  85 */     .setID("sho2");
/*  86 */   public static final Module batterySave1 = new ModuleBatterySave(1, "Battery Saving I").setTexture("batterySave1")
/*  87 */     .setID("bas1");
/*  88 */   public static final Module batterySave2 = new ModuleBatterySave(2, "Battery Saving II").setUndergrade(new Module[] { batterySave1 })
/*  89 */     .setTexture("batterySave2").setID("bas2");
/*  90 */   public static final Module batterySave3 = new ModuleBatterySave(3, "Battery Saving III").setUndergrade(new Module[] { batterySave2 })
/*  91 */     .setTexture("batterySave3").setID("bas3");
/*  92 */   public static final Module batterySave4 = new ModuleBatterySave(4, "Battery Saving IV").setUndergrade(new Module[] { batterySave3 })
/*  93 */     .setTexture("batterySave4").setID("bas4");
/*  94 */   public static final Module heatPower = new ModuleRecharge(1, "Heat Powered").setTexture("heatPower").setID("pow1");
/*  95 */   public static final Module solarPower = new ModuleRecharge(2, "Solar Powered").setTexture("solarPower")
/*  96 */     .setID("pow2");
/*  97 */   public static final Module multiPower = new ModuleRecharge(2, "Multi Powered").setTexture("multiPower")
/*  98 */     .setID("pow3").setUndergrade(new Module[] { heatPower, solarPower });
/*  99 */   public static final Module autoReturn = new ModuleReturn(2, "Return on Low Battery").setTexture("autoReturn")
/* 100 */     .setID("aur1");
/* 101 */   public static final Module deflect = new ModuleDe(1, "Projectile Deflecting").setTexture("deflect").setID("def1");
/* 102 */   public static final Module deflame = new ModuleDe(2, "Fire Extinguishing").setTexture("deflame").setID("def2");
/* 103 */   public static final Module deexplosion = new ModuleDe(3, "Explosion Negating").setTexture("deexplosion")
/* 104 */     .setID("def3");
/* 105 */   public static final Module multiDe = new ModuleDe(4, "Multi Protection").setID("def4")
/* 106 */     .setUndergrade(new Module[] { deflect, deflame, deexplosion }).setTexture("multiDe");
/* 107 */   public static final Module mine1 = new ModuleMine(1, "Mining I").setTexture("mine1").setID("min1");
/* 108 */   public static final Module mine2 = new ModuleMine(2, "Mining II").setUndergrade(new Module[] { mine1 }).setTexture("mine2")
/* 109 */     .setID("min2");
/* 110 */   public static final Module mine3 = new ModuleMine(3, "Mining III").setUndergrade(new Module[] { mine2 }).setTexture("mine3")
/* 111 */     .setID("min3");
/* 112 */   public static final Module mine4 = new ModuleMine(4, "Mining IV").setUndergrade(new Module[] { mine3 }).setTexture("mine4")
/* 113 */     .setID("min4");
/*     */   
/*     */   private String id;
/*     */   public int level;
/*     */   public String displayName;
/*     */   public ModuleType type;
/* 119 */   public List<Module> undergrade = new ArrayList();
/* 120 */   public ResourceLocation texture = new ResourceLocation("drones", "/textures/modules/placeholder.png");
/*     */   
/*     */   public Module(int l, ModuleType t, String s)
/*     */   {
/* 124 */     this.level = l;
/* 125 */     this.type = t;
/* 126 */     this.displayName = s;
/* 127 */     modules.add(this);
/*     */   }
/*     */   
/*     */   public Module setID(String s)
/*     */   {
/* 132 */     this.id = s;
/* 133 */     return this;
/*     */   }
/*     */   
/*     */   public Module setUndergrade(Module... ug)
/*     */   {
/* 138 */     for (int a = 0; a < ug.length; a++)
/*     */     {
/* 140 */       this.undergrade.add(ug[a]);
/* 141 */       this.undergrade.addAll(ug[a].undergrade);
/*     */     }
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public Module setTexture(String s)
/*     */   {
/* 148 */     this.texture = new ResourceLocation("drones", "textures/modules/" + s + ".png");
/* 149 */     return this;
/*     */   }
/*     */   
/*     */   public List<String> getTooltip()
/*     */   {
/* 154 */     List<String> list = new ArrayList();
/* 155 */     TextFormatting color = TextFormatting.WHITE;
/* 156 */     switch (this.level)
/*     */     {
/*     */     case 2: 
/* 159 */       color = TextFormatting.YELLOW;
/* 160 */       break;
/*     */     case 3: 
/* 162 */       color = TextFormatting.AQUA;
/* 163 */       break;
/*     */     case 4: 
/* 165 */       color = TextFormatting.GREEN;
/*     */     }
/*     */     
/* 168 */     list.add(color + this.displayName);
/* 169 */     list.add("Rank: " + color + this.level);
/* 170 */     list.add("Category: " + TextFormatting.WHITE + this.type);
/* 171 */     additionalTooltip(list, false);
/* 172 */     return list;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void updateModule(EntityDrone drone) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void overrideDroneMovement(EntityDrone drone) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean collideWithEntity(EntityDrone drone, Entity e)
/*     */   {
/* 188 */     return false;
/*     */   }
/*     */   
/*     */   public boolean collideWithBlock(EntityDrone drone, BlockPos pos, IBlockState state)
/*     */   {
/* 193 */     return false;
/*     */   }
/*     */   
/*     */   public int overridePriority()
/*     */   {
/* 198 */     return 0;
/*     */   }
/*     */   
/*     */   public boolean canOverrideDroneMovement(EntityDrone drone)
/*     */   {
/* 203 */     return false;
/*     */   }
/*     */   
/*     */   public double costBatRawPerSec(EntityDrone drone)
/*     */   {
/* 208 */     return this.level;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void onDisabled(EntityDrone drone) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void onReenabled(EntityDrone drone) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public NBTTagCompound getModNBT(DroneInfo di)
/*     */   {
/* 228 */     if ((di != null) && (di.modsNBT != null))
/*     */     {
/* 230 */       if (!di.modsNBT.func_74764_b("MNBT")) di.modsNBT.func_74782_a("MNBT", new NBTTagCompound());
/* 231 */       return di.modsNBT.func_74775_l("MNBT");
/*     */     }
/* 233 */     return null;
/*     */   }
/*     */   
/*     */   public void setModNBT(DroneInfo di, NBTTagCompound tag)
/*     */   {
/* 238 */     if ((di != null) && (di.modsNBT != null))
/*     */     {
/* 240 */       di.modsNBT.func_74782_a("MNBT", tag);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canReplace(Module m)
/*     */   {
/* 246 */     return this.undergrade.contains(m);
/*     */   }
/*     */   
/*     */   public boolean canFunctionAs(Module m)
/*     */   {
/* 251 */     return (this == m) || (canReplace(m));
/*     */   }
/*     */   
/*     */   public String getID()
/*     */   {
/* 256 */     return this.id;
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 262 */     return new ModuleGui(gui, this);
/*     */   }
/*     */   
/*     */   public static Module getModuleByID(String id)
/*     */   {
/* 267 */     for (Module m : modules)
/*     */     {
/* 269 */       if (m.getID().equals(id)) return m;
/*     */     }
/* 271 */     return null;
/*     */   }
/*     */   
/*     */   public static Module getModule(String name)
/*     */   {
/* 276 */     for (Module m : modules)
/*     */     {
/* 278 */       if (m.displayName.equalsIgnoreCase(name)) return m;
/*     */     }
/* 280 */     return null;
/*     */   }
/*     */   
/*     */   public static enum ModuleType
/*     */   {
/* 285 */     Transport,  Collect,  Scout,  Battle,  Recover,  Misc;
/*     */     
/*     */     private ModuleType() {}
/*     */   }
/*     */   
/*     */   public String toString() {
/* 291 */     return "Mod-" + this.displayName;
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public static class ModuleGui<T extends Module>
/*     */     extends GuiScreen
/*     */   {
/*     */     public GuiDroneStatus parent;
/*     */     public T mod;
/*     */     public GuiButton disableButton;
/*     */     public GuiButton uninstallButton;
/*     */     public SlotModule modSlot;
/*     */     public int scrollHeight;
/*     */     public int maxScrollHeight;
/* 305 */     public List<String> splittedString = new ArrayList();
/*     */     
/*     */     public ModuleGui(GuiDroneStatus gui, T m)
/*     */     {
/* 309 */       this.parent = gui;
/* 310 */       this.mod = m;
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/* 316 */       super.func_73866_w_();
/* 317 */       this.field_146292_n.add(this.disableButton = new GuiButton(0, this.field_146294_l / 2 + 48, this.field_146295_m / 2 + 70, 40, 20, "Disable"));
/* 318 */       this.field_146292_n.add(this.uninstallButton = new GuiButton(99, this.field_146294_l / 2 + 90, this.field_146295_m / 2 + 70, 50, 20, "Uninstall"));
/* 319 */       if (!this.parent.drone.droneInfo.isEnabled(this.mod)) this.disableButton.field_146126_j = "Enable";
/*     */     }
/*     */     
/*     */     protected void func_146284_a(GuiButton button)
/*     */       throws IOException
/*     */     {
/* 325 */       super.func_146284_a(button);
/* 326 */       boolean enabled = this.parent.drone.droneInfo.isEnabled(this.mod);
/* 327 */       if (button == this.disableButton)
/*     */       {
/* 329 */         PacketDispatcher.sendToServer(new PacketDroneSwitchMod(this.parent.drone, this.mod, !enabled));
/* 330 */         this.disableButton.field_146126_j = (enabled ? "Enable" : "Disable");
/*     */       }
/* 332 */       if (button == this.uninstallButton)
/*     */       {
/* 334 */         PacketDispatcher.sendToServer(new PacketDroneUninstallMod(this.parent.drone, this.mod));
/* 335 */         this.parent.selectedModSlot.overlayColor = -1;
/* 336 */         this.parent.selectedModSlot = null;
/* 337 */         this.parent.selectedModGui = null;
/*     */       }
/* 339 */       else if (!enabled)
/*     */       {
/* 341 */         return;
/*     */       }
/* 343 */       buttonClickedOnEnabledGui(button);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_146278_c(int tint) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_146276_q_() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_146270_b(int tint) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_73876_c()
/*     */     {
/* 367 */       super.func_73876_c();
/* 368 */       if (Mouse.isCreated())
/*     */       {
/* 370 */         int mouseD = Mouse.getDWheel();
/* 371 */         this.scrollHeight = ((int)(this.scrollHeight - Math.signum(mouseD) * Math.pow(Math.abs(mouseD / 12.0D), 0.7D)));
/*     */       }
/* 373 */       this.scrollHeight = Math.max(0, Math.min(this.scrollHeight, this.maxScrollHeight - 60));
/*     */     }
/*     */     
/*     */     public List<String> getDescText()
/*     */     {
/* 378 */       List<String> l = new ArrayList();
/* 379 */       TextFormatting color = TextFormatting.WHITE;
/* 380 */       switch (this.mod.level)
/*     */       {
/*     */       case 2: 
/* 383 */         color = TextFormatting.YELLOW;
/* 384 */         break;
/*     */       case 3: 
/* 386 */         color = TextFormatting.AQUA;
/* 387 */         break;
/*     */       case 4: 
/* 389 */         color = TextFormatting.GREEN;
/*     */       }
/*     */       
/* 392 */       String init = color + "" + TextFormatting.BOLD + this.mod.displayName + TextFormatting.WHITE + "" + TextFormatting.BOLD + " - " + this.mod.type + " mod rank " + color + "" + TextFormatting.BOLD + this.mod.level;
/*     */       
/*     */ 
/* 395 */       if (!this.parent.drone.droneInfo.isEnabled(this.mod))
/*     */       {
/* 397 */         init = color + "" + TextFormatting.BOLD + this.mod.displayName + TextFormatting.RED + "" + TextFormatting.BOLD + " - disabled";
/*     */       }
/*     */       
/* 400 */       l.add(init);
/* 401 */       this.mod.additionalTooltip(l, true);
/* 402 */       addDescText(l);
/* 403 */       return l;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void addDescText(List<String> l) {}
/*     */     
/*     */ 
/*     */ 
/*     */     public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */     {
/* 414 */       List<String> desc = getDescText();
/* 415 */       if (!desc.isEmpty())
/*     */       {
/* 417 */         ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 418 */         int sclh = sr.func_78328_b();
/* 419 */         int sclw = sr.func_78326_a();
/* 420 */         this.splittedString.clear();
/* 421 */         for (int a = 0; a < desc.size(); a++)
/*     */         {
/* 423 */           this.splittedString.addAll(this.field_146289_q.func_78271_c((String)desc.get(a), 280));
/*     */         }
/* 425 */         this.maxScrollHeight = (this.splittedString.size() * 10 + 8);
/* 426 */         GL11.glPushMatrix();
/* 427 */         GL11.glEnable(3089);
/* 428 */         GL11.glScissor((sclw / 2 - 143) * this.field_146297_k.field_71443_c / sclw, (sclh - (sclh / 2 + 91)) * this.field_146297_k.field_71440_d / sclh, 284 * this.field_146297_k.field_71443_c / sclw, 80 * this.field_146297_k.field_71440_d / sclh);
/*     */         
/*     */ 
/* 431 */         GL11.glTranslated(this.field_146294_l / 2 - 140, this.field_146295_m / 2 + 15 - this.scrollHeight, 0.0D);
/* 432 */         for (int a = 0; a < this.splittedString.size(); a++)
/*     */         {
/* 434 */           String s = (String)this.splittedString.get(a);
/* 435 */           this.field_146289_q.func_78276_b(s, 0, a * 10, 16777215);
/*     */         }
/* 437 */         GL11.glDisable(3089);
/* 438 */         GL11.glPopMatrix();
/*     */       }
/* 440 */       super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\Module.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */