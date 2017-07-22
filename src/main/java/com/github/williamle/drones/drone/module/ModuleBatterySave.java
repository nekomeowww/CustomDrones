/*    */ package williamle.drones.drone.module;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ import williamle.drones.gui.GuiDroneStatus;
/*    */ 
/*    */ 
/*    */ public class ModuleBatterySave
/*    */   extends Module
/*    */ {
/*    */   public ModuleBatterySave(int l, String s)
/*    */   {
/* 18 */     super(l, Module.ModuleType.Recover, s);
/*    */   }
/*    */   
/*    */ 
/*    */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*    */   {
/* 24 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 25 */     if (!forGuiDroneStatus) { list.add("Save " + Math.round((1.0D - consumptionRate(null)) * 100.0D) + "% battery");
/*    */     }
/*    */   }
/*    */   
/*    */   public double costBatRawPerSec(EntityDrone drone)
/*    */   {
/* 31 */     return 0.0D;
/*    */   }
/*    */   
/*    */   public double consumptionRate(EntityDrone drone)
/*    */   {
/* 36 */     if ((drone instanceof EntityDroneMob)) return 0.0D;
/* 37 */     return 1.0D - 0.1D * this.level;
/*    */   }
/*    */   
/*    */ 
/*    */   @SideOnly(Side.CLIENT)
/*    */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*    */   {
/* 44 */     return new ModuleBatterySaveGui(gui, this);
/*    */   }
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public class ModuleBatterySaveGui<T extends Module> extends Module.ModuleGui<T>
/*    */   {
/*    */     double prevConsumed;
/*    */     double prevCost;
/*    */     double saved;
/*    */     
/*    */     public ModuleBatterySaveGui(T gui)
/*    */     {
/* 56 */       super(mod);
/*    */     }
/*    */     
/*    */ 
/*    */     public void addDescText(List<String> l)
/*    */     {
/* 62 */       super.addDescText(l);
/* 63 */       double rate = this.parent.drone.droneInfo.getBatteryConsumptionRate(this.parent.drone);
/* 64 */       double prevConsumedNew = this.parent.drone.droneInfo.getMovementBatteryConsumption(this.parent.drone);
/* 65 */       double prevCostNew = prevConsumedNew / rate;
/* 66 */       Module mre1 = this.parent.drone.droneInfo.getModuleWithFunctionOf(Module.heatPower);
/* 67 */       Module mre2 = this.parent.drone.droneInfo.getModuleWithFunctionOf(Module.solarPower);
/* 68 */       if ((mre1 != null) && (this.parent.drone.droneInfo.isEnabled(mre1)))
/*    */       {
/* 70 */         prevConsumedNew -= ((ModuleRecharge)mre1).getRechargeAmount(this.parent.drone);
/*    */       }
/* 72 */       if ((mre2 != null) && (mre2 != mre1) && (this.parent.drone.droneInfo.isEnabled(mre2)))
/*    */       {
/* 74 */         prevConsumedNew -= ((ModuleRecharge)mre2).getRechargeAmount(this.parent.drone);
/*    */       }
/* 76 */       if (prevConsumedNew < 0.0D) prevConsumedNew = 0.0D;
/* 77 */       double savedNew = prevCostNew - prevConsumedNew;
/* 78 */       if ((this.parent.drone.field_70173_aa % 7 == 0) || (Math.abs(prevConsumedNew - this.prevConsumed) >= 0.05D))
/* 79 */         this.prevConsumed = prevConsumedNew;
/* 80 */       if ((this.parent.drone.field_70173_aa % 7 == 0) || (Math.abs(prevCostNew - this.prevCost) >= 0.05D)) this.prevCost = prevCostNew;
/* 81 */       if ((this.parent.drone.field_70173_aa % 7 == 0) || (Math.abs(savedNew - this.saved) >= 0.05D)) { this.saved = savedNew;
/*    */       }
/* 83 */       double pcnDisplay = Math.round(this.prevConsumed * 20.0D * 100.0D) / 100.0D;
/* 84 */       double pcinDisplay = Math.round(this.prevCost * 20.0D * 100.0D) / 100.0D;
/* 85 */       double sDisplay = Math.round(this.saved * 20.0D * 100.0D) / 100.0D;
/* 86 */       l.add("Battery: " + TextFormatting.GREEN + this.parent.drone.droneInfo.getBattery(true));
/* 87 */       l.add("Consumption rate: " + TextFormatting.GREEN + rate * 100.0D + "%");
/* 88 */       l.add("Battery consumption: " + TextFormatting.RED + pcinDisplay + "/sec");
/* 89 */       l.add("Battery saved: " + TextFormatting.YELLOW + sDisplay + "/sec");
/* 90 */       l.add("Actual battery consumed: " + TextFormatting.GREEN + pcnDisplay + "/sec");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleBatterySave.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */