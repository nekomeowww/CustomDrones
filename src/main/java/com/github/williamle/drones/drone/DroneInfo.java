/*     */ package williamle.drones.drone;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.drone.module.ModuleArmor;
/*     */ import williamle.drones.drone.module.ModuleBatterySave;
/*     */ import williamle.drones.drone.module.ModulePlaceHolder;
/*     */ import williamle.drones.drone.module.ModuleRecharge;
/*     */ import williamle.drones.drone.module.ModuleWeapon;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.item.ItemDroneModule;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.client.PacketDroneInfo;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DroneInfo
/*     */ {
/*  35 */   public static final String[] greekNumber = { "Nan", "I", "II", "III", "IV" };
/*  36 */   public static final Map<Object, Double> batteryFuel = new HashMap();
/*  37 */   public static final Map<Object, Double> damageRecover = new HashMap();
/*     */   
/*     */   static
/*     */   {
/*  41 */     batteryFuel.put(Items.field_151044_h, Double.valueOf(10.0D));
/*  42 */     batteryFuel.put(Items.field_151042_j, Double.valueOf(100.0D));
/*  43 */     batteryFuel.put(Items.field_151043_k, Double.valueOf(200.0D));
/*  44 */     batteryFuel.put(Items.field_151045_i, Double.valueOf(1000.0D));
/*  45 */     batteryFuel.put(Items.field_151166_bC, Double.valueOf(2000.0D));
/*  46 */     damageRecover.put(Blocks.field_150339_S, Double.valueOf(10.0D));
/*  47 */     damageRecover.put(Blocks.field_150340_R, Double.valueOf(20.0D));
/*  48 */     damageRecover.put(Blocks.field_150484_ah, Double.valueOf(40.0D));
/*  49 */     damageRecover.put(Blocks.field_150475_bE, Double.valueOf(60.0D));
/*     */   }
/*     */   
/*  52 */   public static int nextID = 1;
/*     */   
/*  54 */   public String name = "#$Drone";
/*     */   public int id;
/*  56 */   public int chip = 1; public int core = 1; public int casing = 1; public int engine = 1;
/*     */   private double battery;
/*  58 */   private double prevBattery; private double damage; private double engineLevel; public boolean isChanged = false;
/*  59 */   public int droneFreq = -1;
/*  60 */   public List<Module> mods = new ArrayList();
/*  61 */   public List<Module> disabledMods = new ArrayList();
/*  62 */   public NBTTagCompound modsNBT = new NBTTagCompound();
/*  63 */   public InventoryDrone inventory = new InventoryDrone(this);
/*  64 */   public final DroneAppearance appearance = new DroneAppearance();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DroneInfo()
/*     */   {
/*  71 */     this(null);
/*     */   }
/*     */   
/*     */   public DroneInfo(EntityDrone e)
/*     */   {
/*  76 */     this(e, 1, 1, 1, 1);
/*     */   }
/*     */   
/*     */   public DroneInfo(int ch, int co, int ca, int en)
/*     */   {
/*  81 */     this(null, ch, co, ca, en);
/*     */   }
/*     */   
/*     */   public DroneInfo(EntityDrone e, int ch, int co, int ca, int en)
/*     */   {
/*  86 */     this.chip = ch;
/*  87 */     this.core = co;
/*  88 */     this.casing = ca;
/*  89 */     this.engine = en;
/*  90 */     this.battery = getMaxBattery();
/*  91 */     this.damage = getMaxDamage(e);
/*  92 */     this.engineLevel = 1.0D;
/*     */   }
/*     */   
/*     */   public DroneInfo newID()
/*     */   {
/*  97 */     this.id = (nextID++);
/*  98 */     this.isChanged = true;
/*  99 */     return this;
/*     */   }
/*     */   
/*     */   public EntityDrone getDrone(World world)
/*     */   {
/* 104 */     return EntityDrone.getDroneFromID(world, this.id);
/*     */   }
/*     */   
/*     */   public void updateDroneInfo(EntityDrone drone)
/*     */   {
/* 109 */     this.prevBattery = this.battery;
/* 110 */     if ((this.battery == 0.0D) && (hasInventory()))
/*     */     {
/* 112 */       for (int a = 0; a < this.inventory.func_70302_i_(); a++)
/*     */       {
/* 114 */         ItemStack is = this.inventory.func_70301_a(a);
/* 115 */         ApplyResult applyResult = canApplyStack(is);
/* 116 */         if ((applyResult.type == ApplyType.BATTERY) && (applyResult.successful))
/*     */         {
/* 118 */           is.field_77994_a -= 1;
/* 119 */           is = applyItem(drone, is);
/* 120 */           this.inventory.func_70299_a(a, is);
/*     */         }
/*     */       }
/*     */     }
/* 124 */     int mode = drone.getFlyingMode();
/* 125 */     if (mode > 0)
/*     */     {
/* 127 */       List<Integer> toRemove = new ArrayList();
/* 128 */       for (int a = 0; a < this.mods.size(); a++)
/*     */       {
/* 130 */         if (this.mods.get(a) == null) { toRemove.add(Integer.valueOf(a));
/*     */         }
/*     */         else {
/* 133 */           Module m = (Module)this.mods.get(a);
/* 134 */           if (isEnabled(m)) m.updateModule(drone);
/*     */         }
/*     */       }
/* 137 */       for (Integer i : toRemove)
/*     */       {
/* 139 */         this.mods.remove(i.intValue());
/*     */       }
/*     */       
/* 142 */       double consumption = getMovementBatteryConsumption(drone);
/* 143 */       reduceBattery(consumption);
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateDroneInfoToClient(EntityPlayer p)
/*     */   {
/* 149 */     if ((p instanceof EntityPlayerMP))
/*     */     {
/* 151 */       this.isChanged = false;
/* 152 */       PacketDispatcher.sendTo(new PacketDroneInfo(this), (EntityPlayerMP)p);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 158 */         throw new Exception(p + " is not EntityPlayerMP. " + this + " not updated.");
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 162 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateDroneModulesToClient(EntityPlayer p)
/*     */   {
/* 169 */     if ((p instanceof EntityPlayerMP))
/*     */     {
/* 171 */       this.isChanged = false;
/* 172 */       PacketDispatcher.sendTo(new PacketDroneInfo(this), (EntityPlayerMP)p);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 178 */         throw new Exception(p + " is not EntityPlayerMP. " + this + " not updated.");
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 182 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setDisplayName(String n)
/*     */   {
/* 189 */     this.name = n;
/* 190 */     this.isChanged = true;
/*     */   }
/*     */   
/*     */   public String getDisplayName()
/*     */   {
/* 195 */     if (this.name.equals("#$Drone")) return "#" + this.id;
/* 196 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean hasInventory()
/*     */   {
/* 201 */     for (Module m : this.mods)
/*     */     {
/* 203 */       if ((m != null) && (m.canFunctionAs(Module.itemInventory)))
/*     */       {
/* 205 */         return true;
/*     */       }
/*     */     }
/* 208 */     return false;
/*     */   }
/*     */   
/*     */   public int getInvSize()
/*     */   {
/* 213 */     switch (this.casing)
/*     */     {
/*     */     case 2: 
/* 216 */       return 18;
/*     */     case 3: 
/* 218 */       return 27;
/*     */     case 4: 
/* 220 */       return 36;
/*     */     }
/* 222 */     return 9;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getItemStackObject(ItemStack is)
/*     */   {
/* 229 */     return (is.func_77973_b() instanceof ItemBlock) ? ((ItemBlock)is.func_77973_b()).func_179223_d() : is == null ? null : is.func_77973_b();
/*     */   }
/*     */   
/*     */   public static class ApplyResult
/*     */   {
/*     */     public DroneInfo.ApplyType type;
/*     */     public boolean successful;
/*     */     public int consume;
/*     */     public double effect;
/*     */     public String displayString;
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b)
/*     */     {
/* 242 */       this(t, b, 1, 0.0D, "");
/*     */     }
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b, String s)
/*     */     {
/* 247 */       this(t, b, 1, 0.0D, s);
/*     */     }
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b, double d)
/*     */     {
/* 252 */       this(t, b, 1, d, "");
/*     */     }
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, double d)
/*     */     {
/* 257 */       this(t, b, i, d, "");
/*     */     }
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, String s)
/*     */     {
/* 262 */       this(t, b, i, 0.0D, s);
/*     */     }
/*     */     
/*     */     public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, double d, String s)
/*     */     {
/* 267 */       this.type = t;
/* 268 */       this.successful = b;
/* 269 */       this.consume = i;
/* 270 */       this.effect = d;
/* 271 */       this.displayString = s;
/*     */     }
/*     */   }
/*     */   
/*     */   public static enum ApplyType
/*     */   {
/* 277 */     NONE,  DAMAGE,  BATTERY,  MODULE;
/*     */     
/*     */     private ApplyType() {}
/*     */   }
/*     */   
/* 282 */   public ApplyResult canApplyStack(ItemStack is) { if (is == null) return new ApplyResult(ApplyType.NONE, false);
/* 283 */     Object isO = getItemStackObject(is);
/* 284 */     if (isO == DronesMod.droneModule)
/*     */     {
/* 286 */       return canAddModule(ItemDroneModule.getModule(is));
/*     */     }
/* 288 */     if (batteryFuel.containsKey(isO))
/*     */     {
/* 290 */       return recoverBatteryAnalyze(is);
/*     */     }
/* 292 */     if (damageRecover.containsKey(isO))
/*     */     {
/* 294 */       return recoverDamageAnalyze(is);
/*     */     }
/* 296 */     return new ApplyResult(ApplyType.NONE, false);
/*     */   }
/*     */   
/*     */   public List<Integer> installedModulesToOverride(Module m)
/*     */   {
/* 301 */     List<Integer> l = new ArrayList();
/* 302 */     for (int a = 0; a < this.mods.size(); a++)
/*     */     {
/* 304 */       if (m.canReplace((Module)this.mods.get(a))) l.add(Integer.valueOf(a));
/*     */     }
/* 306 */     return l;
/*     */   }
/*     */   
/*     */   public ApplyResult canAddModule(Module m)
/*     */   {
/* 311 */     if ((m instanceof ModulePlaceHolder))
/*     */     {
/* 313 */       return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Module empty");
/*     */     }
/* 315 */     if (this.mods.contains(m))
/*     */     {
/* 317 */       return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + m.displayName + " already installed");
/*     */     }
/*     */     
/* 320 */     for (Module m1 : this.mods)
/*     */     {
/* 322 */       if (m1.canReplace(m))
/*     */       {
/* 324 */         return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Better module (" + m1.displayName + ")" + " already installed");
/*     */       }
/*     */     }
/*     */     
/* 328 */     if (m.level > getMaxModLevel())
/*     */     {
/* 330 */       return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Module level too high");
/*     */     }
/* 332 */     if (this.mods.size() - installedModulesToOverride(m).size() + 1 <= getMaxModSlots())
/*     */     {
/* 334 */       return new ApplyResult(ApplyType.MODULE, true, TextFormatting.GREEN + m.displayName + " installed");
/*     */     }
/*     */     
/*     */ 
/* 338 */     return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "No more module slot");
/*     */   }
/*     */   
/*     */ 
/*     */   public void applyModule(Module m)
/*     */   {
/* 344 */     if (m == null)
/*     */     {
/* 346 */       return;
/*     */     }
/* 348 */     List<Integer> toRemoveIndex = installedModulesToOverride(m);
/* 349 */     List toRemove = new ArrayList();
/* 350 */     for (Integer i : toRemoveIndex)
/*     */     {
/* 352 */       toRemove.add(this.mods.get(i.intValue()));
/*     */     }
/* 354 */     this.mods.removeAll(toRemove);
/* 355 */     this.mods.add(m);
/* 356 */     this.isChanged = true;
/*     */   }
/*     */   
/*     */   public ItemStack applyItem(EntityDrone e, ItemStack is)
/*     */   {
/* 361 */     if (batteryFuel.containsKey(getItemStackObject(is)))
/*     */     {
/* 363 */       ApplyResult recov = recoverBatteryAnalyze(is);
/* 364 */       if (recov.successful)
/*     */       {
/* 366 */         reduceBattery(-recov.effect);
/* 367 */         is.field_77994_a -= recov.consume;
/* 368 */         if (is.field_77994_a == 0) is = null;
/*     */       }
/*     */     }
/* 371 */     if (damageRecover.containsKey(getItemStackObject(is)))
/*     */     {
/* 373 */       ApplyResult recov = recoverDamageAnalyze(is);
/* 374 */       if (recov.successful)
/*     */       {
/* 376 */         reduceDamage(e, -recov.effect);
/* 377 */         is.field_77994_a -= recov.consume;
/* 378 */         if (is.field_77994_a == 0) is = null;
/*     */       }
/*     */     }
/* 381 */     return is;
/*     */   }
/*     */   
/*     */   public ApplyResult recoverBatteryAnalyze(ItemStack is)
/*     */   {
/* 386 */     double maxNeedRecover = getMaxBattery() - getBattery(false);
/* 387 */     double eachItemRecover = ((Double)batteryFuel.getOrDefault(getItemStackObject(is), Double.valueOf(0.0D))).doubleValue();
/* 388 */     double maxCanRecover = eachItemRecover * is.field_77994_a;
/* 389 */     double maxToRecover = Math.min(maxNeedRecover, maxCanRecover);
/* 390 */     int stackUse = eachItemRecover > 0.0D ? (int)Math.ceil(maxToRecover / eachItemRecover) : 0;
/* 391 */     return new ApplyResult(ApplyType.BATTERY, stackUse > 0, stackUse, maxToRecover, TextFormatting.GREEN + "Recovered " + (int)maxToRecover + " battery");
/*     */   }
/*     */   
/*     */ 
/*     */   public ApplyResult recoverDamageAnalyze(ItemStack is)
/*     */   {
/* 397 */     double maxNeedRecover = getMaxDamage(null) - getDamage(false);
/* 398 */     double eachItemRecover = ((Double)damageRecover.getOrDefault(getItemStackObject(is), Double.valueOf(0.0D))).doubleValue();
/* 399 */     double maxCanRecover = eachItemRecover * is.field_77994_a;
/* 400 */     double maxToRecover = Math.min(maxNeedRecover, maxCanRecover);
/* 401 */     int stackUse = eachItemRecover > 0.0D ? (int)Math.ceil(maxToRecover / eachItemRecover) : 0;
/* 402 */     return new ApplyResult(ApplyType.DAMAGE, stackUse > 0, stackUse, maxToRecover, TextFormatting.GREEN + "Recovered " + (int)maxToRecover + " health");
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxModSlots()
/*     */   {
/* 408 */     switch (this.chip)
/*     */     {
/*     */     case 2: 
/* 411 */       return 4;
/*     */     case 3: 
/* 413 */       return 6;
/*     */     case 4: 
/* 415 */       return 8;
/*     */     }
/* 417 */     return 3;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getMaxModLevel()
/*     */   {
/* 423 */     return this.chip;
/*     */   }
/*     */   
/*     */   public boolean hasEnabled(Module m)
/*     */   {
/* 428 */     return isEnabled(getModuleWithFunctionOf(m));
/*     */   }
/*     */   
/*     */   public boolean isEnabled(Module m)
/*     */   {
/* 433 */     return (m != null) && ((this.battery > 0.0D) || ((m instanceof ModuleRecharge))) && (!this.disabledMods.contains(m));
/*     */   }
/*     */   
/*     */   public boolean isEnabled(int modIndex)
/*     */   {
/* 438 */     return isEnabled((Module)this.mods.get(modIndex));
/*     */   }
/*     */   
/*     */   public void switchAllModule(EntityDrone drone, boolean enable)
/*     */   {
/* 443 */     if (((enable) && (this.disabledMods.isEmpty())) || ((!enable) && (this.disabledMods.size() == this.mods.size()))) return;
/* 444 */     for (int a = 0; a < this.mods.size(); a++)
/*     */     {
/* 446 */       switchModule(drone, (Module)this.mods.get(a), enable);
/*     */     }
/*     */   }
/*     */   
/*     */   public void switchModule(EntityDrone drone, Module m, boolean enable)
/*     */   {
/* 452 */     if (this.mods.contains(m))
/*     */     {
/* 454 */       if (enable)
/*     */       {
/* 456 */         if (this.disabledMods.contains(m))
/*     */         {
/* 458 */           this.disabledMods.remove(m);
/* 459 */           m.onReenabled(drone);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 464 */       else if (!this.disabledMods.contains(m))
/*     */       {
/* 466 */         this.disabledMods.add(m);
/* 467 */         m.onDisabled(drone);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void switchModule(EntityDrone drone, Integer index, boolean enable)
/*     */   {
/* 475 */     Module m = (Module)this.mods.get(index.intValue());
/* 476 */     switchModule(drone, m, enable);
/*     */   }
/*     */   
/*     */   public Module getModuleWithFunctionOf(Module mod)
/*     */   {
/* 481 */     for (int a = 0; a < this.mods.size(); a++)
/*     */     {
/* 483 */       Module m = (Module)this.mods.get(a);
/* 484 */       if ((m != null) && (m.canFunctionAs(mod))) return m;
/*     */     }
/* 486 */     return null;
/*     */   }
/*     */   
/*     */   public Module getFirstModuleWithEitherFunctionOf(Module... mods)
/*     */   {
/* 491 */     for (Module m : mods)
/*     */     {
/* 493 */       Module m0 = getModuleWithFunctionOf(m);
/* 494 */       if (m0 != null) return m0;
/*     */     }
/* 496 */     return null;
/*     */   }
/*     */   
/*     */   public double getBatteryConsumptionRate(EntityDrone e)
/*     */   {
/* 501 */     for (Module m : this.mods)
/*     */     {
/* 503 */       if (isEnabled(m))
/*     */       {
/* 505 */         if ((m instanceof ModuleBatterySave)) return ((ModuleBatterySave)m).consumptionRate(e);
/*     */       }
/*     */     }
/* 508 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public double getMovementBatteryConsumption(EntityDrone e)
/*     */   {
/* 513 */     double d = 0.0D;
/* 514 */     if (e.getFlyingMode() > 0)
/*     */     {
/* 516 */       d += getFlyingBatteryConsumption(e, e.idle);
/* 517 */       for (Module m : this.mods)
/*     */       {
/* 519 */         if (isEnabled(m))
/*     */         {
/* 521 */           d += m.costBatRawPerSec(e) / 20.0D * getBatteryConsumptionRate(e);
/*     */         }
/*     */       }
/*     */     }
/* 525 */     return d;
/*     */   }
/*     */   
/*     */   public double getFlyingBatteryConsumption(EntityDrone e, boolean idle)
/*     */   {
/* 530 */     double d = this.chip * 0.5D + this.engine * this.engineLevel + (this.core + this.casing) * 0.25D;
/* 531 */     for (int a = 0; a < this.inventory.func_70302_i_(); a++)
/*     */     {
/* 533 */       ItemStack is = this.inventory.func_70301_a(a);
/* 534 */       if (is != null)
/*     */       {
/* 536 */         d += 0.2D;
/*     */       }
/*     */     }
/* 539 */     if (idle) d /= 60.0D;
/* 540 */     d = d * getBatteryConsumptionRate(e) / 20.0D;
/* 541 */     return d;
/*     */   }
/*     */   
/*     */   public double getMaxBattery()
/*     */   {
/* 546 */     switch (this.core)
/*     */     {
/*     */     case 2: 
/* 549 */       return 1200.0D;
/*     */     case 3: 
/* 551 */       return 5200.0D;
/*     */     case 4: 
/* 553 */       return 13200.0D;
/*     */     }
/* 555 */     return 400.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getBattery(boolean forDisplay)
/*     */   {
/* 561 */     return forDisplay ? Math.round(this.battery * 100.0D) / 100.0D : this.battery;
/*     */   }
/*     */   
/*     */   public void setBattery(double battery)
/*     */   {
/* 566 */     this.battery = Math.max(Math.min(battery, getMaxBattery()), 0.0D);
/* 567 */     this.isChanged = true;
/*     */   }
/*     */   
/*     */   public void reduceBattery(double cons)
/*     */   {
/* 572 */     setBattery(this.battery - cons);
/*     */   }
/*     */   
/*     */   public int getEstimatedFlyTimeTick(EntityDrone e)
/*     */   {
/* 577 */     if (this.battery == 0.0D) return 0;
/* 578 */     double consumedBattery = this.prevBattery - this.battery;
/* 579 */     if (e != null) consumedBattery = getMovementBatteryConsumption(e);
/* 580 */     if (consumedBattery <= 0.0D) return -1;
/* 581 */     int tickTotal = (int)Math.round(getBattery(false) / consumedBattery);
/* 582 */     return tickTotal;
/*     */   }
/*     */   
/*     */   public Integer[] getEstimatedFlyTime(EntityDrone e)
/*     */   {
/* 587 */     if (this.battery == 0.0D) return new Integer[] { Integer.valueOf(0) };
/* 588 */     List<Integer> times = new ArrayList();
/* 589 */     double consumedBattery = this.prevBattery - this.battery;
/* 590 */     if (e != null) consumedBattery = getMovementBatteryConsumption(e);
/* 591 */     if (consumedBattery <= 0.0D) return new Integer[] { Integer.valueOf(-1) };
/* 592 */     int secTotal = (int)Math.round(getBattery(false) / 20.0D / consumedBattery);
/* 593 */     int sec = secTotal % 60;
/* 594 */     secTotal -= sec;
/* 595 */     times.add(Integer.valueOf(sec));
/* 596 */     if (secTotal > 0)
/*     */     {
/* 598 */       int min = secTotal / 60 % 60;
/* 599 */       secTotal -= min * 60;
/* 600 */       times.add(Integer.valueOf(min));
/*     */     }
/* 602 */     if (secTotal > 0)
/*     */     {
/* 604 */       int hour = secTotal / 3600 % 24;
/* 605 */       secTotal -= hour * 3600;
/* 606 */       times.add(Integer.valueOf(hour));
/*     */     }
/* 608 */     if (secTotal > 0)
/*     */     {
/* 610 */       int day = secTotal / 86400 % 7;
/* 611 */       secTotal -= day * 86400;
/* 612 */       times.add(Integer.valueOf(day));
/*     */     }
/* 614 */     if (secTotal > 0)
/*     */     {
/* 616 */       int month = secTotal / 604800;
/* 617 */       times.add(Integer.valueOf(month));
/*     */     }
/* 619 */     if (times.isEmpty())
/*     */     {
/* 621 */       times.add(Integer.valueOf(0));
/*     */     }
/* 623 */     Integer[] timesArray = new Integer[times.size()];
/* 624 */     times.toArray(timesArray);
/* 625 */     return timesArray;
/*     */   }
/*     */   
/*     */   public String getEstimatedFlyTimeString(EntityDrone e)
/*     */   {
/* 630 */     String s = "";
/* 631 */     Integer[] times = getEstimatedFlyTime(e);
/* 632 */     if ((times.length == 1) && (times[0].intValue() == -1)) return "infinity";
/* 633 */     for (int a = times.length - 1; a >= 0; a--)
/*     */     {
/* 635 */       if (a != times.length - 1) s = s + " ";
/* 636 */       s = s + times[a];
/* 637 */       switch (a)
/*     */       {
/*     */       case 1: 
/* 640 */         s = s + "m";
/* 641 */         break;
/*     */       case 2: 
/* 643 */         s = s + "h";
/* 644 */         break;
/*     */       case 3: 
/* 646 */         s = s + "d";
/* 647 */         break;
/*     */       case 4: 
/* 649 */         s = s + "w";
/* 650 */         break;
/*     */       default: 
/* 652 */         s = s + "s";
/*     */       }
/*     */     }
/* 655 */     return s;
/*     */   }
/*     */   
/*     */   public double getAttackPower(EntityDrone e)
/*     */   {
/* 660 */     double atk = this.core + e.getBaseAttack();
/* 661 */     for (Module m : this.mods)
/*     */     {
/* 663 */       if (((m instanceof ModuleWeapon)) && (isEnabled(m)))
/*     */       {
/* 665 */         atk += ((ModuleWeapon)m).getAttackPower(e);
/*     */       }
/*     */     }
/* 668 */     return atk;
/*     */   }
/*     */   
/*     */   public double getMaxDamage(EntityDrone e)
/*     */   {
/* 673 */     double i = e == null ? 0.0D : e.getBaseHealth();
/* 674 */     switch (this.casing)
/*     */     {
/*     */     case 2: 
/* 677 */       i += 20.0D;
/* 678 */       break;
/*     */     case 3: 
/* 680 */       i += 40.0D;
/* 681 */       break;
/*     */     case 4: 
/* 683 */       i += 60.0D;
/* 684 */       break;
/*     */     default: 
/* 686 */       i += 10.0D;
/*     */     }
/*     */     
/* 689 */     return i;
/*     */   }
/*     */   
/*     */   public double getDamage(boolean forDisplay)
/*     */   {
/* 694 */     return forDisplay ? Math.round(this.damage * 100.0D) / 100.0D : this.damage;
/*     */   }
/*     */   
/*     */   public void setDamage(EntityDrone e, double damage)
/*     */   {
/* 699 */     this.damage = Math.max(Math.min(damage, getMaxDamage(e)), 0.0D);
/* 700 */     this.isChanged = true;
/* 701 */     if (e != null) e.func_70606_j((float)damage);
/*     */   }
/*     */   
/*     */   public void reduceDamage(EntityDrone e, double dam)
/*     */   {
/* 706 */     setDamage(e, this.damage - dam);
/*     */   }
/*     */   
/*     */   public double getDamageReduction(EntityDrone e)
/*     */   {
/* 711 */     double rate = 0.0D;
/* 712 */     for (Module m : this.mods)
/*     */     {
/* 714 */       if ((m instanceof ModuleArmor))
/*     */       {
/* 716 */         rate = Math.max(((ModuleArmor)m).getDamageReduction(e), rate);
/* 717 */         break;
/*     */       }
/*     */     }
/* 720 */     return rate;
/*     */   }
/*     */   
/*     */   public void damageDrone(EntityDrone e, double dam)
/*     */   {
/* 725 */     reduceDamage(e, dam * (1.0D - getDamageReduction(e)));
/* 726 */     if (getDamage(false) == 0.0D)
/*     */     {
/* 728 */       setBattery(0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   public double getEngineLevel()
/*     */   {
/* 734 */     if ((getDamage(false) == 0.0D) || (getBattery(false) == 0.0D)) return 0.0D;
/* 735 */     return this.engineLevel;
/*     */   }
/*     */   
/*     */   public void setEngineLevel(double d)
/*     */   {
/* 740 */     this.engineLevel = d;
/* 741 */     this.isChanged = true;
/*     */   }
/*     */   
/*     */   public double getMaxSpeed()
/*     */   {
/* 746 */     switch (this.engine)
/*     */     {
/*     */     case 2: 
/* 749 */       return 10.0D;
/*     */     case 3: 
/* 751 */       return 20.0D;
/*     */     case 4: 
/* 753 */       return 30.0D;
/*     */     }
/* 755 */     return 5.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeToNBT(NBTTagCompound tag)
/*     */   {
/* 761 */     NBTTagCompound info = new NBTTagCompound();
/*     */     
/* 763 */     info.func_74778_a("Name", this.name);
/* 764 */     info.func_74768_a("ID", this.id);
/* 765 */     info.func_74768_a("Chip", this.chip);
/* 766 */     info.func_74768_a("Core", this.core);
/* 767 */     info.func_74768_a("Casing", this.casing);
/* 768 */     info.func_74768_a("Engine", this.engine);
/* 769 */     info.func_74780_a("Battery", this.battery);
/* 770 */     info.func_74780_a("PrevBattery", this.prevBattery);
/* 771 */     info.func_74780_a("Damage", this.damage);
/* 772 */     info.func_74780_a("EngineLevel", this.engineLevel);
/* 773 */     info.func_74768_a("Controller Frequency", this.droneFreq);
/*     */     
/*     */ 
/* 776 */     int count = 0;
/* 777 */     for (int a = 0; a < this.mods.size(); a++)
/*     */     {
/* 779 */       Module m = (Module)this.mods.get(a);
/* 780 */       if (m != null)
/*     */       {
/*     */ 
/*     */ 
/* 784 */         count++;
/* 785 */         this.modsNBT.func_74778_a(String.valueOf(a), m.getID());
/*     */       } }
/* 787 */     this.modsNBT.func_74768_a("Count", count);
/* 788 */     info.func_74782_a("Modules", this.modsNBT);
/*     */     
/*     */ 
/* 791 */     NBTTagCompound disabledModTag = new NBTTagCompound();
/* 792 */     count = 0;
/* 793 */     for (int a = 0; a < this.disabledMods.size(); a++)
/*     */     {
/* 795 */       Module m = (Module)this.disabledMods.get(a);
/* 796 */       if (m != null)
/*     */       {
/*     */ 
/*     */ 
/* 800 */         count++;
/* 801 */         disabledModTag.func_74778_a(String.valueOf(a), m.getID());
/*     */       } }
/* 803 */     disabledModTag.func_74768_a("Count", count);
/* 804 */     info.func_74782_a("Disabled Modules", disabledModTag);
/*     */     
/*     */ 
/* 807 */     NBTTagCompound invTag = new NBTTagCompound();
/* 808 */     for (int a = 0; a < 36; a++)
/*     */     {
/* 810 */       ItemStack is = this.inventory.func_70301_a(a);
/* 811 */       if (is != null)
/*     */       {
/* 813 */         NBTTagCompound istag = new NBTTagCompound();
/* 814 */         is.func_77955_b(istag);
/* 815 */         invTag.func_74782_a("IS " + a, istag);
/*     */       }
/*     */     }
/* 818 */     info.func_74782_a("Inv", invTag);
/*     */     
/*     */ 
/* 821 */     NBTTagCompound appearanceTag = new NBTTagCompound();
/* 822 */     this.appearance.writeToNBT(appearanceTag);
/* 823 */     info.func_74782_a("Appearance", appearanceTag);
/*     */     
/*     */ 
/* 826 */     tag.func_74782_a("Drone Info", info);
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound tag)
/*     */   {
/* 831 */     this.mods.clear();
/* 832 */     NBTTagCompound info = tag.func_74775_l("Drone Info");
/*     */     
/* 834 */     this.name = (info.func_74764_b("Name") ? info.func_74779_i("Name") : "#$Drone");
/* 835 */     this.id = (info.func_74764_b("ID") ? info.func_74762_e("ID") : 0);
/* 836 */     nextID = Math.max(nextID, this.id + 1);
/* 837 */     this.chip = (info.func_74764_b("Chip") ? info.func_74762_e("Chip") : 1);
/* 838 */     this.core = (info.func_74764_b("Core") ? info.func_74762_e("Core") : 1);
/* 839 */     this.casing = (info.func_74764_b("Casing") ? info.func_74762_e("Casing") : 1);
/* 840 */     this.engine = (info.func_74764_b("Engine") ? info.func_74762_e("Engine") : 1);
/* 841 */     setBattery(info.func_74764_b("Battery") ? info.func_74769_h("Battery") : getMaxBattery());
/* 842 */     this.prevBattery = (info.func_74764_b("PrevBattery") ? info.func_74769_h("PrevBattery") : this.battery);
/* 843 */     setDamage(null, info.func_74764_b("Damage") ? info.func_74769_h("Damage") : getMaxDamage(null));
/* 844 */     this.engineLevel = (info.func_74764_b("EngineLevel") ? info.func_74769_h("EngineLevel") : 1.0D);
/* 845 */     this.droneFreq = (info.func_74764_b("Controller Frequency") ? info.func_74762_e("Controller Frequency") : -1);
/*     */     
/*     */ 
/* 848 */     readModulesNBT(info.func_74775_l("Modules"));
/*     */     
/*     */ 
/* 851 */     NBTTagCompound disabledModTag = info.func_74775_l("Disabled Modules");
/* 852 */     int disModCount = disabledModTag.func_74762_e("Count");
/* 853 */     for (int a = 0; a < disModCount; a++)
/*     */     {
/* 855 */       this.disabledMods.add(Module.getModuleByID(disabledModTag.func_74779_i(String.valueOf(a))));
/*     */     }
/*     */     
/*     */ 
/* 859 */     NBTTagCompound invTag = info.func_74775_l("Inv");
/* 860 */     for (int a = 0; a < 36; a++)
/*     */     {
/* 862 */       if (invTag.func_74764_b("IS " + a))
/*     */       {
/* 864 */         NBTTagCompound istag = invTag.func_74775_l("IS " + a);
/* 865 */         this.inventory.func_70299_a(a, ItemStack.func_77949_a(istag));
/*     */       }
/*     */       else
/*     */       {
/* 869 */         this.inventory.func_70299_a(a, null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 874 */     this.appearance.readFromNBT(info.func_74775_l("Appearance"));
/*     */   }
/*     */   
/*     */   public void readModulesNBT(NBTTagCompound tag)
/*     */   {
/* 879 */     this.modsNBT = tag;
/* 880 */     int modCount = this.modsNBT.func_74762_e("Count");
/* 881 */     for (int a = 0; a < modCount; a++)
/*     */     {
/* 883 */       this.mods.add(Module.getModuleByID(this.modsNBT.func_74779_i(String.valueOf(a))));
/*     */     }
/*     */   }
/*     */   
/*     */   public DroneInfo copy()
/*     */   {
/* 889 */     NBTTagCompound tag = new NBTTagCompound();
/* 890 */     writeToNBT(tag);
/* 891 */     return fromNBT(tag);
/*     */   }
/*     */   
/*     */   public static DroneInfo fromNBT(NBTTagCompound tag)
/*     */   {
/* 896 */     DroneInfo di = new DroneInfo();
/* 897 */     di.readFromNBT(tag);
/* 898 */     return di;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\DroneInfo.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */