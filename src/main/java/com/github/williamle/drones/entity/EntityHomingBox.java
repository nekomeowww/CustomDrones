/*    */ package williamle.drones.entity;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.nbt.NBTTagCompound;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ public class EntityHomingBox extends Entity
/*    */ {
/*    */   public Entity target;
/*    */   
/*    */   public EntityHomingBox(World worldIn)
/*    */   {
/* 13 */     super(worldIn);
/* 14 */     this.field_98038_p = true;
/* 15 */     func_70105_a(1.0F, 1.0F);
/* 16 */     this.field_70145_X = true;
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_70071_h_()
/*    */   {
/* 22 */     super.func_70071_h_();
/* 23 */     if (this.target != null)
/*    */     {
/* 25 */       func_70105_a(this.target.field_70130_N, this.target.field_70131_O);
/* 26 */       func_70107_b(this.target.field_70165_t, this.target.field_70163_u, this.target.field_70161_v);
/*    */     } else {
/* 28 */       func_70105_a(1.0F, 1.0F);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected void func_70088_a() {}
/*    */   
/*    */ 
/*    */   public boolean func_70067_L()
/*    */   {
/* 39 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean func_70104_M()
/*    */   {
/* 45 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   protected boolean func_70041_e_()
/*    */   {
/* 51 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean canRiderInteract()
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */   
/*    */   protected void func_70037_a(NBTTagCompound compound) {}
/*    */   
/*    */   protected void func_70014_b(NBTTagCompound compound) {}
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityHomingBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */