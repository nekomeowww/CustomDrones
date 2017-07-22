/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.Container;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.inventory.Slot;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import williamle.drones.drone.InventoryDrone;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ContainerDrone
/*     */   extends Container
/*     */ {
/*     */   public InventoryBasic module;
/*     */   public InventoryDrone invDrone;
/*     */   public int moduleSlotID;
/*     */   
/*     */   public ContainerDrone(IInventory invp, InventoryDrone invd)
/*     */   {
/*  23 */     func_75146_a(new Slot(this.module = new InventoryBasic("Mod apply", true, 1), 0, 82, 47));
/*  24 */     this.invDrone = invd;
/*  25 */     this.moduleSlotID = (this.field_75151_b.size() - 1);
/*     */     
/*  27 */     for (int y = 0; y < 3; y++)
/*     */     {
/*  29 */       for (int x = 0; x < 9; x++)
/*     */       {
/*  31 */         func_75146_a(new Slot(invp, x + y * 9 + 9, 116 + x * 18, 101 + y * 18));
/*     */       }
/*     */     }
/*     */     
/*  35 */     for (int x = 0; x < 9; x++)
/*     */     {
/*  37 */       func_75146_a(new Slot(invp, x, 116 + x * 18, 159));
/*     */     }
/*     */     
/*  40 */     for (int y = 0; y < 4; y++)
/*     */     {
/*  42 */       for (int x = 0; x < 9; x++)
/*     */       {
/*  44 */         int index = x + y * 9;
/*  45 */         if (index < invd.func_70302_i_())
/*     */         {
/*  47 */           func_75146_a(new Slot(invd, index, 116 + x * 18, 11 + y * 18));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public ItemStack func_82846_b(EntityPlayer playerIn, int index)
/*     */   {
/*  55 */     ItemStack itemstack = null;
/*  56 */     Slot slot = (Slot)this.field_75151_b.get(index);
/*     */     
/*  58 */     if ((slot != null) && (slot.func_75216_d()))
/*     */     {
/*  60 */       ItemStack itemstack1 = slot.func_75211_c();
/*  61 */       itemstack = itemstack1.func_77946_l();
/*     */       
/*  63 */       if (index == 0)
/*     */       {
/*  65 */         if (!func_75135_a(itemstack1, 1, 36, false))
/*     */         {
/*  67 */           return null;
/*     */         }
/*     */         
/*  70 */         slot.func_75220_a(itemstack1, itemstack);
/*     */       }
/*  72 */       else if (index <= 36)
/*     */       {
/*  74 */         if (!func_75135_a(itemstack1, 37, 38 + this.invDrone.func_70302_i_() - 1, false))
/*     */         {
/*  76 */           return null;
/*     */         }
/*  78 */         slot.func_75220_a(itemstack1, itemstack);
/*     */       }
/*  80 */       else if (!func_75135_a(itemstack1, 1, 37, false))
/*     */       {
/*  82 */         return null;
/*     */       }
/*     */       
/*  85 */       if (itemstack1.field_77994_a == 0)
/*     */       {
/*  87 */         slot.func_75215_d((ItemStack)null);
/*     */       }
/*     */       else
/*     */       {
/*  91 */         slot.func_75218_e();
/*     */       }
/*     */       
/*  94 */       if (itemstack1.field_77994_a == itemstack.field_77994_a)
/*     */       {
/*  96 */         return null;
/*     */       }
/*     */       
/*  99 */       slot.func_82870_a(playerIn, itemstack1);
/*     */     }
/*     */     
/* 102 */     return itemstack;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_75145_c(EntityPlayer playerIn)
/*     */   {
/* 108 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_75134_a(EntityPlayer playerIn)
/*     */   {
/* 114 */     if (func_75147_a(this.module, 0).func_75216_d())
/*     */     {
/* 116 */       playerIn.func_71019_a(func_75147_a(this.module, 0).func_75211_c(), false);
/* 117 */       func_75147_a(this.module, 0).func_75215_d(null);
/*     */     }
/* 119 */     super.func_75134_a(playerIn);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\ContainerDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */