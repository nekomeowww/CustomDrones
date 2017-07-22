/*    */ package williamle.drones.item;
/*    */ 
/*    */ import net.minecraft.item.Item;
/*    */ 
/*    */ public class ItemDronePart
/*    */   extends Item
/*    */ {
/*    */   public int rank;
/*    */   
/*    */   public ItemDronePart(int i, int rank)
/*    */   {
/* 12 */     func_77625_d(i);
/* 13 */     this.rank = rank;
/*    */   }
/*    */   
/*    */ 
/*    */   public ItemDronePart(int rank)
/*    */   {
/* 19 */     this.rank = rank;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDronePart.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */