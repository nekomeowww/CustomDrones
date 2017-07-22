/*    */ package williamle.drones.drone.module;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiButton;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.relauncher.Side;
/*    */ import net.minecraftforge.fml.relauncher.SideOnly;
/*    */ import williamle.drones.gui.GuiDroneStatus;
/*    */ 
/*    */ public class ModuleCamera extends Module
/*    */ {
/*    */   public static Entity prevRenderView;
/*    */   public static boolean prevHideGui;
/*    */   
/*    */   public ModuleCamera(int l, String s)
/*    */   {
/* 18 */     super(l, Module.ModuleType.Scout, s);
/*    */   }
/*    */   
/*    */ 
/*    */   @SideOnly(Side.CLIENT)
/*    */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*    */   {
/* 25 */     return new ModuleCameraGui(gui, this);
/*    */   }
/*    */   
/*    */   @SideOnly(Side.CLIENT)
/*    */   public class ModuleCameraGui<T extends Module>
/*    */     extends Module.ModuleGui<T>
/*    */   {
/*    */     public ModuleCameraGui(T gui)
/*    */     {
/* 34 */       super(mod);
/*    */     }
/*    */     
/*    */ 
/*    */     public void func_73866_w_()
/*    */     {
/* 40 */       super.func_73866_w_();
/* 41 */       this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 40, this.field_146295_m / 2 + 40, 80, 20, "Go to camera"));
/*    */     }
/*    */     
/*    */ 
/*    */     public void addDescText(List<String> l)
/*    */     {
/* 47 */       super.addDescText(l);
/*    */     }
/*    */     
/*    */ 
/*    */     public void buttonClickedOnEnabledGui(GuiButton button)
/*    */     {
/* 53 */       super.buttonClickedOnEnabledGui(button);
/* 54 */       if (button.field_146127_k == 1)
/*    */       {
/* 56 */         this.field_146297_k.func_147108_a(null);
/* 57 */         ModuleCamera.prevRenderView = this.field_146297_k.func_175606_aa();
/* 58 */         ModuleCamera.prevHideGui = this.field_146297_k.field_71474_y.field_74319_N;
/* 59 */         this.field_146297_k.func_175607_a(this.parent.drone);
/* 60 */         this.field_146297_k.field_71438_f.func_174979_m();
/* 61 */         this.field_146297_k.field_71460_t.func_175066_a((Entity)null);
/* 62 */         this.parent.drone.setCameraMode(true);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleCamera.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */