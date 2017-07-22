/*    */ package williamle.drones.api.gui;
/*    */ 
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.InventoryPlayer;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ 
/*    */ public class PIItemStackRequirement extends PIItemStack
/*    */ {
/*    */   public IInventory invp;
/*    */   public int require;
/*    */   public int has;
/*    */   
/*    */   public PIItemStackRequirement(Panel p, IInventory inv, ItemStack item, int r)
/*    */   {
/* 17 */     super(p, item);
/* 18 */     this.invp = inv;
/* 19 */     this.is = item.func_77946_l();
/* 20 */     this.is.field_77994_a = 1;
/* 21 */     this.require = item.field_77994_a;
/*    */   }
/*    */   
/*    */ 
/*    */   public void updateItem()
/*    */   {
/* 27 */     super.updateItem();
/* 28 */     if (this.is != null)
/*    */     {
/* 30 */       this.has = 0;
/* 31 */       for (int a = 0; a < this.invp.func_70302_i_(); a++)
/*    */       {
/* 33 */         ItemStack is0 = this.invp.func_70301_a(a);
/* 34 */         if ((ItemStack.func_179545_c(is0, this.is)) && (ItemStack.func_77970_a(is0, this.is)))
/*    */         {
/* 36 */           this.has += is0.field_77994_a;
/*    */         }
/*    */       }
/*    */     }
/* 40 */     if ((this.has < this.require) && ((this.invp instanceof InventoryPlayer)) && (((InventoryPlayer)this.invp).field_70458_d.func_184812_l_()))
/*    */     {
/* 42 */       this.has = this.require;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   public String getDescStringForIS(ItemStack is)
/*    */   {
/* 49 */     return (this.has >= this.require ? TextFormatting.GREEN + "" : "") + this.has + "/" + this.require;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\PIItemStackRequirement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */