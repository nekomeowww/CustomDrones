/*    */ package williamle.drones.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ public class ContainerCrafter
/*    */   extends Container
/*    */ {
/*    */   public ContainerCrafter(InventoryPlayer playerInventory)
/*    */   {
/* 16 */     for (int i = 0; i < 3; i++)
/*    */     {
/* 18 */       for (int j = 0; j < 9; j++)
/*    */       {
/* 20 */         func_75146_a(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 101 + i * 18));
/*    */       }
/*    */     }
/*    */     
/* 24 */     for (int k = 0; k < 9; k++)
/*    */     {
/* 26 */       func_75146_a(new Slot(playerInventory, k, 8 + k * 18, 159));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_75145_c(EntityPlayer playerIn)
/*    */   {
/* 33 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_82846_b(EntityPlayer playerIn, int index)
/*    */   {
/* 39 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
/*    */   {
/* 45 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94530_a(ItemStack stack, Slot p_94530_2_)
/*    */   {
/* 51 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean func_75135_a(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94531_b(Slot p_94531_1_)
/*    */   {
/* 63 */     return false;
/*    */   }
/*    */   
/*    */   public void func_75141_a(int slotID, ItemStack stack) {}
/*    */   
/*    */   public void func_75131_a(ItemStack[] p_75131_1_) {}
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\ContainerCrafter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */