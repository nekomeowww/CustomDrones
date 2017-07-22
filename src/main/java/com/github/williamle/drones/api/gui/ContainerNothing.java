/*    */ package williamle.drones.api.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.inventory.ClickType;
/*    */ import net.minecraft.inventory.Container;
/*    */ import net.minecraft.inventory.Slot;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ContainerNothing
/*    */   extends Container
/*    */ {
/*    */   public boolean func_75145_c(EntityPlayer playerIn)
/*    */   {
/* 20 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_82846_b(EntityPlayer playerIn, int index)
/*    */   {
/* 26 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
/*    */   {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94530_a(ItemStack stack, Slot p_94530_2_)
/*    */   {
/* 38 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean func_75135_a(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
/*    */   {
/* 44 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_94531_b(Slot p_94531_1_)
/*    */   {
/* 50 */     return false;
/*    */   }
/*    */   
/*    */   public void func_75141_a(int slotID, ItemStack stack) {}
/*    */   
/*    */   public void func_75131_a(ItemStack[] p_75131_1_) {}
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\ContainerNothing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */