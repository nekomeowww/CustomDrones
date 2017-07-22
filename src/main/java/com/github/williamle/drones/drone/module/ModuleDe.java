/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ 
/*     */ 
/*     */ public class ModuleDe
/*     */   extends Module
/*     */ {
/*     */   public ModuleDe(int l, String s)
/*     */   {
/*  30 */     super(l, Module.ModuleType.Battle, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  36 */     boolean projectile = canFunctionAs(deflect);
/*  37 */     boolean fire = canFunctionAs(deflame);
/*  38 */     boolean explosion = canFunctionAs(deexplosion);
/*  39 */     super.updateModule(drone);
/*  40 */     int count = 0;
/*  41 */     double range = getRange(drone);
/*  42 */     Vec3d deflectMid; double strength; if (projectile)
/*     */     {
/*  44 */       List<Entity> projectiles = drone.field_70170_p.func_175647_a(Entity.class, drone
/*  45 */         .func_174813_aQ().func_186662_g(range), new Predicate()
/*     */         {
/*     */ 
/*     */           public boolean apply(Entity input)
/*     */           {
/*  50 */             return input instanceof IProjectile;
/*     */           }
/*     */           
/*  53 */         });
/*  54 */       deflectMid = EntityHelper.getCenterVec(drone.getControllingPlayer() != null ? drone.getControllingPlayer() : drone);
/*  55 */       strength = getStrength(drone);
/*  56 */       for (Entity e : projectiles)
/*     */       {
/*  58 */         double dist = Math.pow(e.func_70032_d(drone), 1.4D);
/*  59 */         Vec3d deflectVec = EntityHelper.getCenterVec(e).func_178788_d(deflectMid).func_72432_b();
/*  60 */         deflectVec = VecHelper.scale(deflectVec, strength / dist);
/*  61 */         e.func_70024_g(deflectVec.field_72450_a, deflectVec.field_72448_b, deflectVec.field_72449_c);
/*  62 */         count++;
/*     */       }
/*     */     }
/*  65 */     if ((fire) && (drone.field_70173_aa % 20 == 0))
/*     */     {
/*  67 */       int maxFire = getFirePerSec(drone);
/*  68 */       int fireCount = 0;
/*  69 */       EntityPlayer player = drone.getControllingPlayer() != null ? drone.getControllingPlayer() : null;
/*  70 */       for (int i = 0; i < range; i++)
/*     */       {
/*  72 */         int xmin = (int)Math.floor(drone.field_70165_t - i);
/*  73 */         int xmax = (int)Math.floor(drone.field_70165_t + i);
/*  74 */         int ymin = (int)Math.floor(drone.field_70163_u - i);
/*  75 */         int ymax = (int)Math.floor(drone.field_70163_u + i);
/*  76 */         int zmin = (int)Math.floor(drone.field_70161_v - i);
/*  77 */         int zmax = (int)Math.floor(drone.field_70161_v + i);
/*  78 */         for (int x = xmin; x <= xmax; x++)
/*     */         {
/*  80 */           if (fireCount == maxFire) break;
/*  81 */           for (int y = ymin; y <= ymax; y++)
/*     */           {
/*  83 */             if (fireCount == maxFire) break;
/*  84 */             for (int z = zmin; z <= zmax; z++)
/*     */             {
/*  86 */               if (fireCount == maxFire) break;
/*  87 */               if ((x == xmin) || (x == xmax) || (z == zmin) || (z == zmax) || (y == ymin) || (y == ymax))
/*     */               {
/*  89 */                 BlockPos bp = new BlockPos(x, y, z);
/*  90 */                 IBlockState bs = drone.field_70170_p.func_180495_p(bp);
/*  91 */                 Block b = bs.func_177230_c();
/*  92 */                 if (b == Blocks.field_150480_ab)
/*     */                 {
/*  94 */                   fireCount++;
/*  95 */                   count++;
/*  96 */                   drone.field_70170_p.func_180498_a(player, 1009, bp, 0);
/*  97 */                   drone.field_70170_p.func_175698_g(bp);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 105 */     if (explosion) {}
/*     */     
/*     */ 
/* 108 */     drone.droneInfo.reduceBattery(count * 5 * drone.droneInfo.getBatteryConsumptionRate(drone));
/*     */   }
/*     */   
/*     */   public double getRange(EntityDrone drone)
/*     */   {
/* 113 */     return drone.droneInfo.chip * 2;
/*     */   }
/*     */   
/*     */   public static double getMaxPossibleRange()
/*     */   {
/* 118 */     return 8.0D;
/*     */   }
/*     */   
/*     */   public double getStrength(EntityDrone drone)
/*     */   {
/* 123 */     return drone.droneInfo.core * 0.5D;
/*     */   }
/*     */   
/*     */   public int getFirePerSec(EntityDrone drone)
/*     */   {
/* 128 */     return drone.droneInfo.core * 4;
/*     */   }
/*     */   
/*     */   public double getMaxExplosionSize(EntityDrone drone)
/*     */   {
/* 133 */     return drone.droneInfo.core * 4;
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 140 */     return new ModuleDeGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 146 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 147 */     boolean projectile = canFunctionAs(deflect);
/* 148 */     boolean fire = canFunctionAs(deflame);
/* 149 */     boolean explosion = canFunctionAs(deexplosion);
/* 150 */     if (projectile) list.add("Deflect incoming projectiles");
/* 151 */     if (fire) list.add("Extinguish nearby fire");
/* 152 */     if (explosion) list.add("Negate nearby explosions");
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleDeGui<T extends Module> extends Module.ModuleGui<T> {
/*     */     boolean projectile;
/*     */     boolean fire;
/*     */     boolean explosion;
/*     */     
/*     */     public ModuleDeGui(T gui) {
/* 162 */       super(mod);
/* 163 */       this.projectile = mod.canFunctionAs(Module.deflect);
/* 164 */       this.fire = mod.canFunctionAs(Module.deflame);
/* 165 */       this.explosion = mod.canFunctionAs(Module.deexplosion);
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 171 */       super.addDescText(l);
/* 172 */       int diameter = (int)(ModuleDe.this.getRange(this.parent.drone) * 2.0D);
/* 173 */       l.add("Effective range: " + TextFormatting.YELLOW + diameter + "x" + diameter + "x" + diameter + TextFormatting.RESET + " blocks");
/*     */       
/* 175 */       if (this.projectile)
/*     */       {
/* 177 */         l.add("Deflecting power multiplier: " + TextFormatting.RED + "x" + this.parent.drone.droneInfo.core);
/*     */       }
/* 179 */       if (this.fire)
/*     */       {
/* 181 */         l.add("Fire extinguish per second: " + TextFormatting.RED + ModuleDe.this.getFirePerSec(this.parent.drone));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleDe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */