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
/*    */ public class ModuleWeapon
/*    */   extends Module
/*    */ {
/*    */   public ModuleWeapon(int l, String s)
/*    */   {
/* 17 */     super(l, Module.ModuleType.Battle, s);
/*    */   }
/*    */   
/*    */ 
/*    */   @SideOnly(Side.CLIENT)
/*    */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*    */   {
/* 24 */     return new ModuleWeaponGui(gui, this);
/*    */   }
/*    */   
/*    */   public double getAttackPower(EntityDrone drone)
/*    */   {
/* 29 */     return this.level * 1.5D;
/*    */   }
/*    */   
/*    */ 
/*    */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*    */   {
/* 35 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 36 */     if (!forGuiDroneStatus)
/*    */     {
/* 38 */       double damage = this.level * 1.5D;
/* 39 */       double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
/* 40 */       list.add("Additional attack: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
/*    */     }
/*    */   }
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public class ModuleWeaponGui<T extends Module> extends Module.ModuleGui<T>
/*    */   {
/*    */     public ModuleWeaponGui(T gui)
/*    */     {
/* 49 */       super(mod);
/*    */     }
/*    */     
/*    */ 
/*    */     public void addDescText(List<String> l)
/*    */     {
/* 55 */       super.addDescText(l);
/* 56 */       double damage = this.parent.drone.droneInfo.getAttackPower(this.parent.drone);
/* 57 */       double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
/* 58 */       l.add("Attack power: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
/* 59 */       l.add("Damage entities on colliding with");
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleWeapon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */