/*    */ package williamle.drones.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.InventoryBasic;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import williamle.drones.drone.InventoryDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContainerDroneStatus
/*    */   extends Container
/*    */ {
/*    */   public InventoryBasic module;
/*    */   public int moduleSlotID;
/*    */   
/*    */   public ContainerDroneStatus(InventoryDrone invd)
/*    */   {
/* 22 */     for (int y = 0; y < 4; y++)
/*    */     {
/* 24 */       for (int x = 0; x < 9; x++)
/*    */       {
/* 26 */         int index = x + y * 9;
/* 27 */         if (index < invd.func_70302_i_())
/*    */         {
/* 29 */           func_75146_a(new Slot(invd, index, 69 + x * 18, -8 + y * 18));
/*    */         }
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75145_c(EntityPlayer playerIn)
/*    */   {
/* 38 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_82846_b(EntityPlayer playerIn, int index)
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
/*    */   {
/* 50 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94530_a(ItemStack stack, Slot p_94530_2_)
/*    */   {
/* 56 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean func_75135_a(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
/*    */   {
/* 62 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94531_b(Slot p_94531_1_)
/*    */   {
/* 68 */     return false;
/*    */   }
/*    */   
/*    */   public void func_75141_a(int slotID, ItemStack stack) {}
/*    */   
/*    */   public void func_75131_a(ItemStack[] p_75131_1_) {}
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\ContainerDroneStatus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */