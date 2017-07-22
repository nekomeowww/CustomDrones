/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ 
/*     */ public class ModuleRecharge
/*     */   extends Module
/*     */ {
/*     */   public ModuleRecharge(int l, String s)
/*     */   {
/*  21 */     super(l, Module.ModuleType.Recover, s);
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/*  28 */     return new ModuleRechargeGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public double costBatRawPerSec(EntityDrone drone)
/*     */   {
/*  34 */     return 0.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  40 */     super.updateModule(drone);
/*  41 */     drone.droneInfo.reduceBattery(-getRechargeAmount(drone));
/*     */   }
/*     */   
/*     */   public double getRechargeAmount(EntityDrone d)
/*     */   {
/*  46 */     if (!d.droneInfo.isEnabled(this)) { return 0.0D;
/*     */     }
/*  48 */     int range = 2 + d.droneInfo.chip;
/*  49 */     double recharge = 0.0D;
/*  50 */     if ((canFunctionAs(solarPower)) && (d.field_70170_p.func_175710_j(d.func_180425_c())))
/*     */     {
/*  52 */       double lightClearness = d.field_70170_p.func_175724_o(d.func_180425_c());
/*  53 */       double sunnyness = d.field_70170_p.func_72820_D() % 24000L;
/*  54 */       if (sunnyness < 6000.0D) { sunnyness += 0.0D;
/*  55 */       } else if ((sunnyness > 6000.0D) && (sunnyness <= 18000.0D)) { sunnyness = 12000.0D - sunnyness;
/*  56 */       } else if (sunnyness < 24000.0D) sunnyness -= 24000.0D;
/*  57 */       sunnyness += 1000.0D;
/*  58 */       double daytimeLightness = Math.max(0.0D, sunnyness / 7000.0D);
/*  59 */       recharge += lightClearness * daytimeLightness * 0.375D * d.droneInfo.chip;
/*     */     }
/*  61 */     if (canFunctionAs(heatPower))
/*     */     {
/*  63 */       for (int x = (int)(d.field_70165_t - range); x <= d.field_70165_t + range; x++)
/*     */       {
/*  65 */         for (int y = (int)(d.field_70163_u - range); y <= d.field_70163_u + range; y++)
/*     */         {
/*  67 */           for (int z = (int)(d.field_70161_v - range); z <= d.field_70161_v + range; z++)
/*     */           {
/*  69 */             double distSqr = d.func_70092_e(x + 0.5D, y + 0.5D, z + 0.5D);
/*  70 */             if (distSqr <= range * range)
/*     */             {
/*  72 */               Block b = d.field_70170_p.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
/*  73 */               double heatPower = 0.0D;
/*  74 */               if (b == Blocks.field_150480_ab) { heatPower = 1.0D;
/*  75 */               } else if (b == Blocks.field_150356_k) { heatPower = 2.0D;
/*  76 */               } else if (b == Blocks.field_150353_l) heatPower = 3.0D;
/*  77 */               if (heatPower > 0.0D)
/*     */               {
/*  79 */                 double distHeatRate = 1.0D - distSqr / range / range;
/*  80 */                 recharge += heatPower * distHeatRate / 120.0D;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  87 */     return recharge;
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/*  93 */     super.additionalTooltip(list, forGuiDroneStatus);
/*  94 */     if (this == heatPower) { list.add("Recharge battery from heat");
/*  95 */     } else if (this == solarPower) { list.add("Recharge battery from sunlight");
/*  96 */     } else if (this == multiPower) list.add("Recharge battery from heat and sunlight");
/*  97 */     if (canFunctionAs(heatPower)) list.add("Drone immune to fire");
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleRechargeGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     public ModuleRechargeGui(T gui)
/*     */     {
/* 105 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 111 */       super.addDescText(l);
/* 112 */       if (ModuleRecharge.this.canFunctionAs(Module.heatPower))
/*     */       {
/* 114 */         int range = 2 + this.parent.drone.droneInfo.chip;
/* 115 */         l.add("Heat range: " + TextFormatting.YELLOW + range * 2 + "x" + range * 2 + "x" + range * 2 + TextFormatting.RESET + " blocks");
/*     */       }
/*     */       
/* 118 */       double recharge = ((ModuleRecharge)this.mod).getRechargeAmount(this.parent.drone);
/* 119 */       l.add("Recharge: " + TextFormatting.GREEN + Math.round(recharge * 20.0D * 100.0D) / 100.0D + "/sec");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleRecharge.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */