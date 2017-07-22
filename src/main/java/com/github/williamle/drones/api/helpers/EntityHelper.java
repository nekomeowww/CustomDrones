/*    */ package williamle.drones.api.helpers;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.entity.player.EntityPlayerMP;
/*    */ import net.minecraft.inventory.IInventory;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.util.text.TextComponentString;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EntityHelper
/*    */ {
/*    */   public static void addChat(EntityPlayer p, int worldSide, String chat)
/*    */   {
/* 23 */     if ((worldSide == 0) || ((worldSide == 1) && ((p instanceof EntityPlayerMP))) || ((worldSide == 2) && ((p instanceof EntityPlayerMP))))
/*    */     {
/*    */ 
/* 26 */       p.func_146105_b(new TextComponentString(chat));
/*    */     }
/*    */   }
/*    */   
/*    */   public static ItemStack addItemStackToInv(IInventory inv, ItemStack is)
/*    */   {
/* 32 */     int remaining = is.field_77994_a;
/* 33 */     for (int a = 0; a < inv.func_70302_i_(); a++)
/*    */     {
/* 35 */       ItemStack is0 = inv.func_70301_a(a);
/* 36 */       int toAdd = 0;
/* 37 */       if (is0 == null)
/*    */       {
/* 39 */         toAdd = Math.min(Math.min(is.field_77994_a, is.func_77976_d()), inv.func_70297_j_());
/* 40 */         ItemStack copy = is.func_77946_l();
/* 41 */         copy.field_77994_a = toAdd;
/* 42 */         inv.func_70299_a(a, copy);
/*    */       }
/* 44 */       else if ((ItemStack.func_179545_c(is0, is)) && (ItemStack.func_77970_a(is0, is)))
/*    */       {
/* 46 */         int isAllow = is0.func_77976_d() - is0.field_77994_a;
/* 47 */         int invAllow = inv.func_70297_j_() - is0.field_77994_a;
/* 48 */         toAdd = Math.min(Math.min(is.field_77994_a, isAllow), invAllow);
/* 49 */         is0.field_77994_a += toAdd;
/*    */       }
/* 51 */       is.field_77994_a -= toAdd;
/* 52 */       if (is.field_77994_a <= 0)
/*    */       {
/* 54 */         is = null;
/* 55 */         break;
/*    */       }
/*    */     }
/* 58 */     inv.func_70296_d();
/* 59 */     return is;
/*    */   }
/*    */   
/*    */   public static Vec3d getCenterVec(Entity e)
/*    */   {
/* 64 */     if (e != null)
/*    */     {
/* 66 */       AxisAlignedBB aabb = e.func_174813_aQ();
/* 67 */       if (aabb != null)
/*    */       {
/* 69 */         return new Vec3d((aabb.field_72336_d + aabb.field_72340_a) / 2.0D, (aabb.field_72337_e + aabb.field_72338_b) / 2.0D, (aabb.field_72334_f + aabb.field_72339_c) / 2.0D);
/*    */       }
/*    */       
/*    */ 
/* 73 */       return e.func_174791_d().func_72441_c(0.0D, e.field_70131_O / 2.0F, 0.0D);
/*    */     }
/*    */     
/* 76 */     return null;
/*    */   }
/*    */   
/*    */   public static Vec3d getEyeVec(Entity e)
/*    */   {
/* 81 */     return e.func_174791_d().func_72441_c(0.0D, e.func_70047_e(), 0.0D);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\EntityHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */