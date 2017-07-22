/*     */ package williamle.drones.render;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.model.CMBase;
/*     */ import williamle.drones.api.model.CMPipe;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.drone.DroneAppearance;
/*     */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ public class ModelDrone extends ModelBaseMod<EntityDrone>
/*     */ {
/*  19 */   public static Color white = new Color(1.0D, 1.0D, 1.0D, 1.0D);
/*  20 */   public static Color iron = new Color(0.8D, 0.8D, 0.8D, 1.0D);
/*  21 */   public static Color gold = new Color(1.0D, 1.0D, 0.5D, 1.0D);
/*  22 */   public static Color diamond = new Color(0.6D, 1.0D, 0.9D, 1.0D);
/*  23 */   public static Color emerald = new Color(0.6D, 1.0D, 0.6D, 1.0D);
/*     */   public CMBase fullDrone;
/*     */   public CMBase wingsFull;
/*     */   
/*     */   public ModelDrone(RenderManager rm) {
/*  28 */     super(rm);
/*  29 */     this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Drone", white.copy(), iron.copy(), iron.copy(), iron.copy(), iron
/*  30 */       .copy(), iron.multiplyRGB(0.6D), iron.multiplyRGB(0.4D), iron.copy());
/*  31 */     setup();
/*     */   }
/*     */   
/*     */ 
/*     */   public ModelDrone setName(String s)
/*     */   {
/*  37 */     super.setName(s);
/*  38 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setup() {}
/*     */   
/*     */ 
/*     */   public void setupWing(CMBase model, double wingLength, double wingThick)
/*     */   {
/*  48 */     model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, wingLength), 0.005D, wingThick, 4)
/*  49 */       .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller1").setPaletteIndexes(new String[] { "Wing" }));
/*  50 */     model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4)
/*  51 */       .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller2").setPaletteIndexes(new String[] { "Wing" }));
/*  52 */     model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4)
/*  53 */       .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller3").setPaletteIndexes(new String[] { "Wing" }));
/*  54 */     model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, wingLength), 0.005D, wingThick, 4)
/*  55 */       .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller4").setPaletteIndexes(new String[] { "Wing" }));
/*     */   }
/*     */   
/*     */ 
/*     */   public void doRender(EntityDrone drone, float yaw, float partialTicks, Object... params)
/*     */   {
/*  61 */     applyAppearances(drone, yaw, partialTicks, params);
/*  62 */     applyRotation(drone, yaw, partialTicks);
/*  63 */     for (CMBase cm : this.models.values())
/*     */     {
/*  65 */       cm.fullRender();
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyRotation(EntityDrone drone, float rotationYaw, float partialTick)
/*     */   {
/*  71 */     Set<CMBase> wingLikes = getWingLikeModels();
/*  72 */     DroneInfo di; if (drone != null)
/*     */     {
/*  74 */       di = drone.droneInfo;
/*  75 */       double mx = drone.field_70159_w;
/*  76 */       double my = drone.field_70181_x;
/*  77 */       double mz = drone.field_70179_y;
/*  78 */       Vec3d look = drone.func_70676_i(partialTick);
/*  79 */       double horzSpeed = Math.sqrt(mx * mx + mz * mz);
/*  80 */       double angleDif = Math.atan2(mx, mz) - Math.atan2(look.field_72450_a, look.field_72449_c);
/*  81 */       double vertTakeover = Math.cos(Math.atan2(my, horzSpeed));
/*  82 */       double rotatePercentage = Math.min(horzSpeed / di.getMaxSpeed() * 8.0D * vertTakeover, 2.0D);
/*  83 */       double pitch = getLeanAngle() * Math.cos(angleDif) * rotatePercentage;
/*  84 */       double roll = -getLeanAngle() * Math.sin(angleDif) * rotatePercentage;
/*  85 */       if (this.fullDrone != null)
/*     */       {
/*  87 */         this.fullDrone.resetRotation(0.0D, 0.0D, 1.0D, roll);
/*  88 */         this.fullDrone.setRotation(1.0D, 0.0D, 0.0D, pitch);
/*  89 */         this.fullDrone.setRotation(0.0D, 1.0D, 0.0D, -rotationYaw);
/*     */       }
/*  91 */       if (!wingLikes.isEmpty())
/*     */       {
/*  93 */         for (CMBase cmbase : wingLikes)
/*     */         {
/*  95 */           if (cmbase != null) {
/*  96 */             cmbase.resetRotation(0.0D, 1.0D, 0.0D, drone.getWingRotation(partialTick) * wingRotationRate());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 102 */       this.fullDrone.centerRots.clear();
/* 103 */       if (!wingLikes.isEmpty())
/*     */       {
/* 105 */         for (CMBase cmbase : wingLikes)
/*     */         {
/* 107 */           if (cmbase != null) cmbase.centerRots.clear();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double wingRotationRate()
/*     */   {
/* 115 */     return 1.0D;
/*     */   }
/*     */   
/*     */   public double getLeanAngle()
/*     */   {
/* 120 */     return 45.0D;
/*     */   }
/*     */   
/*     */   public Set<CMBase> getWingLikeModels()
/*     */   {
/* 125 */     Set<CMBase> set = new HashSet();
/* 126 */     set.add(this.wingsFull);
/* 127 */     return set;
/*     */   }
/*     */   
/*     */   public void applyAppearances(EntityDrone drone, float yaw, float partialTicks, Object... params)
/*     */   {
/* 132 */     applyDefaultAppearance();
/* 133 */     if (drone != null)
/*     */     {
/* 135 */       DroneInfo di = drone.droneInfo;
/* 136 */       if (di != null)
/*     */       {
/* 138 */         applyDIAppearance(di);
/* 139 */         applyAppearance(di.appearance);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 144 */       applyDIAppearance(new DroneInfo());
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyDIAppearance(DroneInfo di)
/*     */   {
/* 150 */     if (di != null)
/*     */     {
/* 152 */       Color engine = iron;
/* 153 */       switch (di.engine)
/*     */       {
/*     */       case 2: 
/* 156 */         engine = gold;
/* 157 */         break;
/*     */       case 3: 
/* 159 */         engine = diamond;
/* 160 */         break;
/*     */       case 4: 
/* 162 */         engine = emerald;
/*     */       }
/*     */       
/* 165 */       Color core = iron;
/* 166 */       switch (di.core)
/*     */       {
/*     */       case 2: 
/* 169 */         core = gold;
/* 170 */         break;
/*     */       case 3: 
/* 172 */         core = diamond;
/* 173 */         break;
/*     */       case 4: 
/* 175 */         core = emerald;
/*     */       }
/*     */       
/* 178 */       Color casing = iron;
/* 179 */       switch (di.casing)
/*     */       {
/*     */       case 2: 
/* 182 */         casing = gold;
/* 183 */         break;
/*     */       case 3: 
/* 185 */         casing = diamond;
/* 186 */         break;
/*     */       case 4: 
/* 188 */         casing = emerald;
/*     */       }
/*     */       
/* 191 */       DroneAppearance.ColorPalette cp = DroneAppearance.ColorPalette.fastMake("", white.copy(), engine.copy(), casing.copy(), core.copy(), casing
/* 192 */         .multiplyRGB(0.8D), casing.multiplyRGB(0.6D), casing.multiplyRGB(0.4D), iron.copy());
/* 193 */       applyPalette(cp);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyAppearance(DroneAppearance appearance)
/*     */   {
/* 199 */     if (appearance != null) applyPalette(appearance.palette);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */