/*    */ package williamle.drones.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.creativetab.CreativeTabs;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.drone.module.Module;
/*    */ 
/*    */ public class ItemDroneModule
/*    */   extends Item
/*    */ {
/*    */   public ItemDroneModule()
/*    */   {
/* 17 */     func_77625_d(16);
/* 18 */     func_77627_a(true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*    */   {
/* 24 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/* 25 */     Module mod = getModule(stack);
/* 26 */     if (mod != null)
/*    */     {
/* 28 */       String name = (String)tooltip.get(tooltip.size() - 1);
/* 29 */       List<String> moduleTooltips = mod.getTooltip();
/* 30 */       tooltip.set(tooltip.size() - 1, name + " " + (String)moduleTooltips.get(0));
/* 31 */       tooltip.addAll(moduleTooltips.subList(1, moduleTooltips.size()));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public int getMetadata(ItemStack stack)
/*    */   {
/* 38 */     Module mod = getModule(stack);
/* 39 */     if (mod != null)
/*    */     {
/* 41 */       return Math.max(0, mod.level - 1);
/*    */     }
/* 43 */     return super.getMetadata(stack);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*    */   {
/* 49 */     super.func_150895_a(itemIn, tab, subItems);
/* 50 */     if ((tab == DronesMod.droneTab) || (tab == null))
/*    */     {
/* 52 */       for (int a = 1; a < Module.modules.size(); a++)
/*    */       {
/* 54 */         subItems.add(itemModule((Module)Module.modules.get(a)));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public static void setModule(ItemStack stack, Module mod)
/*    */   {
/* 61 */     if (!stack.func_77942_o())
/*    */     {
/* 63 */       stack.func_77982_d(new NBTTagCompound());
/*    */     }
/* 65 */     stack.func_77978_p().func_74778_a("Drone Module", mod.getID());
/*    */   }
/*    */   
/*    */   public static Module getModule(ItemStack stack)
/*    */   {
/* 70 */     if (!stack.func_77942_o())
/*    */     {
/* 72 */       stack.func_77982_d(new NBTTagCompound());
/*    */     }
/* 74 */     Module m = Module.getModuleByID(stack.func_77978_p().func_74779_i("Drone Module"));
/* 75 */     return m == null ? Module.useless1 : m;
/*    */   }
/*    */   
/*    */   public static ItemStack itemModule(Module m)
/*    */   {
/* 80 */     ItemStack is = new ItemStack(DronesMod.droneModule);
/* 81 */     setModule(is, m);
/* 82 */     return is;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDroneModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */