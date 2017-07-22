/*     */ package williamle.drones.drone;
/*     */ 
/*     */ import net.minecraft.inventory.InventoryBasic;
/*     */ import net.minecraft.item.ItemStack;
/*     */ 
/*     */ public class InventoryDrone extends InventoryBasic
/*     */ {
/*     */   public DroneInfo drone;
/*     */   
/*     */   public InventoryDrone(DroneInfo di)
/*     */   {
/*  12 */     super("Drone Inventory", false, 64);
/*  13 */     this.drone = di;
/*     */   }
/*     */   
/*     */   public boolean canAddToInv(ItemStack stack0, boolean mustAddAll)
/*     */   {
/*  18 */     if (stack0 == null) return false;
/*  19 */     ItemStack stack = stack0.func_77946_l();
/*  20 */     if (func_70302_i_() <= 0) return false;
/*  21 */     for (int a = 0; a < func_70302_i_(); a++)
/*     */     {
/*  23 */       ItemStack is2 = func_70301_a(a);
/*  24 */       if (is2 == null)
/*     */       {
/*  26 */         return true;
/*     */       }
/*  28 */       if (ItemStack.func_179545_c(stack, is2))
/*     */       {
/*  30 */         int maxAdd = is2.func_77976_d() - is2.field_77994_a;
/*  31 */         int canAdd = Math.min(maxAdd, stack.field_77994_a);
/*  32 */         stack.field_77994_a -= canAdd;
/*  33 */         if ((!mustAddAll) && (canAdd > 0)) return true;
/*  34 */         if (stack.field_77994_a <= 0) {
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  40 */     return stack.field_77994_a == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack addToInv(ItemStack stack0)
/*     */   {
/*  46 */     if ((stack0 == null) || (func_70302_i_() <= 0)) return stack0;
/*  47 */     ItemStack stack = stack0.func_77946_l();
/*  48 */     for (int a = 0; a < func_70302_i_(); a++)
/*     */     {
/*  50 */       ItemStack is2 = func_70301_a(a);
/*  51 */       if (ItemStack.func_179545_c(stack, is2))
/*     */       {
/*  53 */         int maxAdd = is2.func_77976_d() - is2.field_77994_a;
/*  54 */         int canAdd = Math.min(maxAdd, stack.field_77994_a);
/*  55 */         is2.field_77994_a += canAdd;
/*  56 */         func_70299_a(a, is2);
/*  57 */         stack.field_77994_a -= canAdd;
/*  58 */         if (stack.field_77994_a <= 0)
/*     */         {
/*  60 */           stack = null;
/*  61 */           break;
/*     */         }
/*     */       }
/*     */     }
/*  65 */     if ((stack != null) && (stack.field_77994_a > 0))
/*     */     {
/*  67 */       for (int a = 0; a < func_70302_i_(); a++)
/*     */       {
/*  69 */         ItemStack is2 = func_70301_a(a);
/*  70 */         if (is2 == null)
/*     */         {
/*  72 */           func_70299_a(a, stack.func_77946_l());
/*  73 */           stack = null;
/*  74 */           break;
/*     */         }
/*     */       }
/*     */     }
/*  78 */     this.drone.isChanged = true;
/*  79 */     return stack;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70299_a(int index, ItemStack stack)
/*     */   {
/*  85 */     if (index >= func_70302_i_()) return;
/*  86 */     this.drone.isChanged = true;
/*  87 */     super.func_70299_a(index, stack);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_70301_a(int index)
/*     */   {
/*  93 */     if (index >= func_70302_i_()) return null;
/*  94 */     return super.func_70301_a(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_70298_a(int index, int count)
/*     */   {
/* 100 */     if (index >= func_70302_i_()) return null;
/* 101 */     this.drone.isChanged = true;
/* 102 */     return super.func_70298_a(index, count);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_70304_b(int index)
/*     */   {
/* 108 */     if (index >= func_70302_i_()) return null;
/* 109 */     this.drone.isChanged = true;
/* 110 */     return super.func_70304_b(index);
/*     */   }
/*     */   
/*     */ 
/*     */   public String func_70005_c_()
/*     */   {
/* 116 */     return this.drone.name;
/*     */   }
/*     */   
/*     */ 
/*     */   public int func_70302_i_()
/*     */   {
/* 122 */     if (!this.drone.hasInventory()) return 0;
/* 123 */     return this.drone.getInvSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\InventoryDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */