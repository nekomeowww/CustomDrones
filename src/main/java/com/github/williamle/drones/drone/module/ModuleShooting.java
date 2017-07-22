/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.monster.IMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.entity.EntityPlasmaShot;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ 
/*     */ public class ModuleShooting extends Module
/*     */ {
/*     */   public ModuleShooting(int l, String s)
/*     */   {
/*  29 */     super(l, Module.ModuleType.Battle, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  35 */     super.updateModule(drone);
/*  36 */     double range = getShootingRange(drone);
/*  37 */     EntityPlayer p = drone.getControllingPlayer();
/*  38 */     List<EntityLivingBase> targets; if (p != null)
/*     */     {
/*  40 */       Entity lae = p.func_110144_aD();
/*  41 */       if ((lae != null) && (lae.func_70089_S()))
/*     */       {
/*  43 */         drone.setDroneAttackTarget(lae, false);
/*     */       }
/*     */       else
/*     */       {
/*  47 */         targets = drone.field_70170_p.func_72872_a(EntityLivingBase.class, p
/*  48 */           .func_174813_aQ().func_72314_b(range, range, range));
/*  49 */         targets.addAll(drone.field_70170_p.func_72872_a(EntityLivingBase.class, drone
/*  50 */           .func_174813_aQ().func_72314_b(range, range, range)));
/*  51 */         for (int a = 0; a < targets.size(); a++)
/*     */         {
/*  53 */           EntityLivingBase e = (EntityLivingBase)targets.get(a);
/*  54 */           if ((e != null) && (e.func_70089_S()))
/*     */           {
/*  56 */             Entity targetee = null;
/*  57 */             if ((e instanceof EntityLiving)) { targetee = ((EntityLiving)e).func_70638_az();
/*     */ 
/*     */             }
/*  60 */             else if (e.func_110144_aD() != null) { targetee = e.func_110144_aD();
/*  61 */             } else if (e.func_70643_av() != null) { targetee = e.func_70643_av();
/*     */             }
/*  63 */             if (targetee != null)
/*     */             {
/*  65 */               if ((targetee == p) || (targetee == drone)) { drone.setDroneAttackTarget(targetee, false);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  72 */     if (drone.getDroneAttackTarget() == null)
/*     */     {
/*  74 */       List<Entity> targets = drone.field_70170_p.func_175674_a(drone.getRider(), drone
/*  75 */         .func_174813_aQ().func_72314_b(range, range, range), IMob.field_82192_a);
/*  76 */       for (Entity e : targets)
/*     */       {
/*  78 */         drone.setDroneAttackTarget(e, false);
/*     */       }
/*     */     }
/*     */     
/*  82 */     if (drone.getDroneAttackTarget() != null)
/*     */     {
/*  84 */       if (drone.func_70032_d(drone.getDroneAttackTarget()) > getShootingRange(drone) * 2.0D)
/*     */       {
/*  86 */         drone.setDroneAttackTarget(null, true);
/*     */       }
/*     */       else
/*     */       {
/*  90 */         shootAt(drone, drone.getDroneAttackTarget());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void shootAt(EntityDrone drone, Entity e)
/*     */   {
/*  97 */     double range = getShootingRange(drone);
/*  98 */     double speed = getShootingSpeed(drone);
/*  99 */     double rate = getShootingRate(drone);
/* 100 */     Vec3d mid = EntityHelper.getCenterVec(drone);
/* 101 */     Vec3d eMid = EntityHelper.getCenterVec(e);
/* 102 */     double dist = mid.func_72438_d(eMid);
/* 103 */     if (dist <= range)
/*     */     {
/* 105 */       if (drone.attackDelay <= 0)
/*     */       {
/* 107 */         drone.attackDelay = ((int)(20.0D / rate));
/* 108 */         if (!drone.field_70170_p.field_72995_K)
/*     */         {
/* 110 */           Vec3d startShootVec = VecHelper.fromTo(mid, eMid).func_72432_b();
/* 111 */           EntityPlasmaShot bullet = new EntityPlasmaShot(drone.field_70170_p);
/* 112 */           if (canFunctionAs(shootingHoming))
/*     */           {
/* 114 */             bullet.homing = true;
/* 115 */             bullet.homingTarget = e;
/*     */           }
/* 117 */           bullet.shooter = drone;
/* 118 */           bullet.damage = drone.droneInfo.getAttackPower(drone);
/* 119 */           bullet.func_70107_b(mid.field_72450_a, mid.field_72448_b, mid.field_72449_c);
/* 120 */           Vec3d dir = VecHelper.setLength(VecHelper.fromTo(bullet.func_174791_d(), eMid), speed);
/* 121 */           bullet.field_70159_w = dir.field_72450_a;
/* 122 */           bullet.field_70181_x = dir.field_72448_b;
/* 123 */           bullet.field_70179_y = dir.field_72449_c;
/*     */           
/* 125 */           Color c = new Color(1, 1, 1, 1);
/* 126 */           switch (drone.droneInfo.core)
/*     */           {
/*     */           case 2: 
/* 129 */             c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
/* 130 */             break;
/*     */           case 3: 
/* 132 */             c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
/* 133 */             break;
/*     */           case 4: 
/* 135 */             c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
/*     */           }
/*     */           
/* 138 */           bullet.color = c;
/* 139 */           drone.field_70170_p.func_72838_d(bullet);
/* 140 */           drone.func_184185_a(SoundEvents.field_187853_gC, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
/* 141 */           drone.droneInfo.reduceBattery(getShootingCost(drone));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int overridePriority()
/*     */   {
/* 150 */     return 50;
/*     */   }
/*     */   
/*     */ 
/*     */   public void overrideDroneMovement(EntityDrone drone)
/*     */   {
/* 156 */     super.overrideDroneMovement(drone);
/* 157 */     int mode = drone.getFlyingMode();
/* 158 */     double speedMult = drone.getSpeedMultiplication();
/* 159 */     Vec3d mid = EntityHelper.getCenterVec(drone);
/* 160 */     Entity e = drone.getDroneAttackTarget();
/* 161 */     Vec3d eMid = EntityHelper.getCenterVec(e);
/* 162 */     double speed = getShootingSpeed(drone);
/* 163 */     double rate = getShootingRate(drone);
/* 164 */     double dist = mid.func_72438_d(eMid);
/* 165 */     double range = getShootingRange(drone);
/* 166 */     if (dist > range)
/*     */     {
/* 168 */       if ((mode != 2) && (mode != 3))
/*     */       {
/* 170 */         Vec3d followDir = VecHelper.setLength(VecHelper.fromTo(drone.func_174791_d(), eMid), dist - range);
/* 171 */         if (followDir.func_72433_c() > 0.6D)
/*     */         {
/* 173 */           if (followDir.func_72433_c() > 1.0D) followDir = followDir.func_72432_b();
/* 174 */           drone.field_70181_x += followDir.field_72448_b * speedMult;
/* 175 */           drone.field_70159_w += followDir.field_72450_a * speedMult;
/* 176 */           drone.field_70179_y += followDir.field_72449_c * speedMult;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canOverrideDroneMovement(EntityDrone drone)
/*     */   {
/* 185 */     int mode = drone.getFlyingMode();
/*     */     
/* 187 */     return (drone.getDroneAttackTarget() != null) && (mode != 2) && (drone.func_70068_e(drone.getDroneAttackTarget()) > Math.pow(getShootingRange(drone), 2.0D));
/*     */   }
/*     */   
/*     */   public double getShootingSpeed(EntityDrone e)
/*     */   {
/* 192 */     return 10.0D * e.droneInfo.core / 20.0D;
/*     */   }
/*     */   
/*     */   public double getShootingRange(EntityDrone e)
/*     */   {
/* 197 */     return 8.0D * e.droneInfo.chip;
/*     */   }
/*     */   
/*     */   public double getShootingRate(EntityDrone e)
/*     */   {
/* 202 */     return e.droneInfo.chip / 2.0D;
/*     */   }
/*     */   
/*     */   public double getShootingCost(EntityDrone e)
/*     */   {
/* 207 */     return e.droneInfo.getAttackPower(e) * 4.0D * e.droneInfo.getBatteryConsumptionRate(e);
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 214 */     return new ModuleShootingGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 220 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 221 */     list.add("Ranged attack. Target nearby monsters and any entity that attacks the controller.");
/* 222 */     if (canFunctionAs(shootingHoming))
/*     */     {
/* 224 */       list.add(TextFormatting.YELLOW + "Homing shot.");
/*     */     }
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleShootingGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     public ModuleShootingGui(T gui)
/*     */     {
/* 233 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 239 */       super.addDescText(l);
/* 240 */       double damage = this.parent.drone.droneInfo.getAttackPower(this.parent.drone);
/* 241 */       double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
/* 242 */       l.add("Attack power: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
/* 243 */       double speed = ((ModuleShooting)this.mod).getShootingSpeed(this.parent.drone) * 20.0D;
/* 244 */       double range = ((ModuleShooting)this.mod).getShootingRange(this.parent.drone);
/* 245 */       double rate = ((ModuleShooting)this.mod).getShootingRate(this.parent.drone);
/* 246 */       double cost = ((ModuleShooting)this.mod).getShootingCost(this.parent.drone);
/* 247 */       l.add("Speed: " + TextFormatting.GREEN + Math.round(speed * 10.0D) / 10.0D + "m/sec");
/* 248 */       l.add("Range: " + TextFormatting.GREEN + Math.round(range * 10.0D) / 10.0D + "m");
/* 249 */       l.add("Rate: " + TextFormatting.GREEN + Math.round(rate * 10.0D) / 10.0D + " shots/sec");
/* 250 */       l.add("Cost: " + TextFormatting.GREEN + Math.round(cost * 10.0D) / 10.0D + " bat/shot");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleShooting.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */