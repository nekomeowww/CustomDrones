/*    */ package williamle.drones.item;
/*    */ 
/*    */ import net.minecraft.item.EnumRarity;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemDroneBit
/*    */   extends Item
/*    */ {
/*    */   public boolean func_77636_d(ItemStack stack)
/*    */   {
/* 17 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_77614_k()
/*    */   {
/* 23 */     return true;
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumRarity func_77613_e(ItemStack stack)
/*    */   {
/* 29 */     if (stack.func_77952_i() == 1) return EnumRarity.RARE;
/* 30 */     return super.func_77613_e(stack);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDroneBit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */