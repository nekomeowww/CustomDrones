/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.api.helpers.WorldHelper;
/*     */ import williamle.drones.api.path.Node;
/*     */ import williamle.drones.api.path.Path;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ import williamle.drones.item.ItemDroneFlyer;
/*     */ 
/*     */ public class ModuleMovement extends Module
/*     */ {
/*     */   public ModuleMovement(int l, String s)
/*     */   {
/*  26 */     super(l, Module.ModuleType.Scout, s);
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/*  33 */     return new ModuleMovementGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void overrideDroneMovement(EntityDrone drone)
/*     */   {
/*  39 */     int mode = drone.getFlyingMode();
/*  40 */     if ((mode == 2) || ((mode == 3) && (drone.recordingPath != null)))
/*     */     {
/*  42 */       EntityPlayer p = drone.field_70170_p.func_72890_a(drone, 64 * drone.droneInfo.chip);
/*  43 */       if ((p != null) && (p.func_184614_ca() != null) && 
/*  44 */         (DronesMod.droneFlyer.isControllingDrone(p.func_184614_ca(), drone)))
/*     */       {
/*  46 */         drone.idle = false;
/*  47 */         int flyMode = DronesMod.droneFlyer.getFlyMode(p.func_184614_ca());
/*  48 */         if (flyMode == 1)
/*     */         {
/*  50 */           Vec3d maxTarget = EntityHelper.getEyeVec(p).func_178787_e(VecHelper.setLength(p.func_70040_Z(), 32.0D));
/*  51 */           RayTraceResult mop = WorldHelper.fullRayTrace(drone.field_70170_p, EntityHelper.getEyeVec(p), maxTarget, 
/*  52 */             !p.func_70090_H(), false, new williamle.drones.api.Filters.FilterExcepts(new Object[] { drone, p, drone.getRider(), EntityXPOrb.class }));
/*  53 */           Vec3d dest = maxTarget;
/*  54 */           if (mop != null)
/*     */           {
/*  56 */             if (mop.field_72308_g != null)
/*     */             {
/*  58 */               dest = EntityHelper.getEyeVec(mop.field_72308_g);
/*     */             }
/*  60 */             else if (mop.field_72307_f != null)
/*     */             {
/*  62 */               dest = mop.field_72307_f;
/*     */             }
/*     */           }
/*  65 */           dest = dest.func_72441_c(0.0D, drone.getRiderHeight(), 0.0D);
/*  66 */           Vec3d dirNorm = VecHelper.fromTo(EntityHelper.getEyeVec(drone), dest);
/*  67 */           drone.flyNormalAlong(dirNorm, 0.6D, 1.0D);
/*     */         }
/*  69 */         else if (flyMode == 2)
/*     */         {
/*  71 */           drone.flyNormalAlong(p.func_70040_Z(), 0.0D, 1.0D);
/*     */         }
/*  73 */         if (drone.recordingPath != null)
/*     */         {
/*  75 */           if ((drone.prevMotionX != drone.field_70159_w) || (drone.prevMotionY != drone.field_70181_x) || (drone.prevMotionZ != drone.field_70179_y))
/*     */           {
/*     */ 
/*  78 */             Node n = new Node(drone.field_70165_t, drone.field_70163_u, drone.field_70161_v);
/*  79 */             drone.recordingPath.addNode(n);
/*     */           }
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  85 */         drone.idle = true;
/*     */       }
/*     */     }
/*  88 */     else if (mode == 3)
/*     */     {
/*  90 */       drone.idle = false;
/*  91 */       if ((drone.automatedPath != null) && (drone.automatedPath.hasPath()))
/*     */       {
/*  93 */         Node nowNode = drone.automatedPath.getCurrentNode();
/*  94 */         Vec3d nowNodeVec = nowNode.toVec();
/*  95 */         Vec3d pos = drone.func_174791_d();
/*  96 */         Vec3d flyVec = null;
/*  97 */         flyVec = VecHelper.fromTo(pos, nowNodeVec);
/*  98 */         if (flyVec != null)
/*     */         {
/* 100 */           double speedMult = drone.getSpeedMultiplication();
/* 101 */           double minDist = speedMult * 20.0D;
/* 102 */           flyVec = flyVec.func_72432_b();
/* 103 */           drone.field_70159_w += flyVec.field_72450_a * speedMult;
/* 104 */           drone.field_70181_x += flyVec.field_72448_b * speedMult;
/* 105 */           drone.field_70179_y += flyVec.field_72449_c * speedMult;
/*     */           
/* 107 */           if (pos.func_72441_c(drone.field_70159_w, drone.field_70181_x, drone.field_70179_y).func_72436_e(nowNodeVec) < minDist * minDist)
/*     */           {
/* 109 */             if (drone.automatedPath.goToNextNode() == null)
/*     */             {
/* 111 */               drone.automatedPath.resetPathIndex();
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 117 */     else if (mode == 4)
/*     */     {
/* 119 */       drone.idle = false;
/* 120 */       EntityPlayer toFollow = drone.getControllingPlayer();
/* 121 */       if (toFollow != null)
/*     */       {
/* 123 */         Vec3d vecTo = new Vec3d(toFollow.field_70165_t, toFollow.field_70163_u + toFollow.func_70047_e(), toFollow.field_70161_v);
/* 124 */         Vec3d vecFrom = drone.func_174791_d();
/* 125 */         double dist = vecFrom.func_72438_d(vecTo);
/* 126 */         double minDist = Math.pow(drone.droneInfo.engine, 0.35D) * 2.5D;
/* 127 */         if (dist > minDist)
/*     */         {
/* 129 */           Vec3d followDir = VecHelper.setLength(VecHelper.fromTo(vecFrom, vecTo), dist - minDist);
/* 130 */           drone.flyNormalAlong(followDir, 0.6D, 1.0D);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 135 */         drone.idle = true;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int overridePriority()
/*     */   {
/* 143 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canOverrideDroneMovement(EntityDrone drone)
/*     */   {
/* 149 */     return drone.getFlyingMode() > 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDisabled(EntityDrone drone)
/*     */   {
/* 155 */     super.onDisabled(drone);
/* 156 */     if (!drone.canDroneHaveFlyMode(drone.getFlyingMode())) { drone.setNextFlyingMode();
/*     */     }
/*     */   }
/*     */   
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 162 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 163 */     if (this == controlMovement) { list.add("Allow manual control movement");
/* 164 */     } else if (this == pathMovement) { list.add("Allow preset path movement");
/* 165 */     } else if (this == followMovement) { list.add("Allow following movement");
/* 166 */     } else if (this == multiMovement) list.add("Allow manual control, preset path, and following movement");
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleMovementGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     public ModuleMovementGui(T gui)
/*     */     {
/* 174 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 180 */       super.addDescText(l);
/* 181 */       if (this.parent.drone.getControllingPlayer() != null)
/*     */       {
/* 183 */         l.add("Controlled by " + net.minecraft.util.text.TextFormatting.GREEN + this.parent.drone.getControllingPlayer().func_70005_c_());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleMovement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */