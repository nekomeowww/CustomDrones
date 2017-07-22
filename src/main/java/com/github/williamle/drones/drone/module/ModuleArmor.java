/*    */ package williamle.drones.drone.module;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.gui.GuiDroneStatus;
/*    */ 
/*    */ 
/*    */ public class ModuleArmor
/*    */   extends Module
/*    */ {
/*    */   public ModuleArmor(int l, String s)
/*    */   {
/* 17 */     super(l, Module.ModuleType.Battle, s);
/*    */   }
/*    */   
/*    */ 
/*    */   @SideOnly(Side.CLIENT)
/*    */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*    */   {
/* 24 */     return new ModuleArmorGui(gui, this);
/*    */   }
/*    */   
/*    */   public double getDamageReduction(EntityDrone e)
/*    */   {
/* 29 */     return this.level * 0.1D;
/*    */   }
/*    */   
/*    */ 
/*    */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*    */   {
/* 35 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 36 */     if (!forGuiDroneStatus)
/*    */     {
/* 38 */       list.add("Damage reduction: " + TextFormatting.RED + this.level * 10 + "%");
/*    */     }
/*    */   }
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public class ModuleArmorGui<T extends Module> extends Module.ModuleGui<T>
/*    */   {
/*    */     public ModuleArmorGui(T gui)
/*    */     {
/* 47 */       super(mod);
/*    */     }
/*    */     
/*    */ 
/*    */     public void addDescText(List<String> l)
/*    */     {
/* 53 */       super.addDescText(l);
/* 54 */       double damage = this.parent.drone.droneInfo.getDamage(true);
/* 55 */       int heart = (int)Math.round(damage / 2.0D);
/* 56 */       l.add("Health: " + TextFormatting.RED + damage + TextFormatting.RESET + " (" + heart + " heart" + (heart > 1 ? "s" : "") + ")");
/*    */       
/* 58 */       l.add("Damage reduction: " + TextFormatting.RED + this.parent.drone.droneInfo
/* 59 */         .getDamageReduction(this.parent.drone) * 100.0D + "%");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleArmor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */