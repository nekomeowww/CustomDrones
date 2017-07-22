/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.RenderItem;
/*    */ import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.world.World;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CMItemStack
/*    */   extends CMBase
/*    */ {
/*    */   public ItemStack is;
/*    */   public World world;
/*    */   public Vec3d origin;
/*    */   public double scale;
/*    */   
/*    */   public CMItemStack(Vec3d ori)
/*    */   {
/* 27 */     this.origin = ori;
/* 28 */     if (ori == null) this.origin = vec(0.0D, 0.0D, 0.0D);
/* 29 */     this.scale = 1.0D;
/* 30 */     setUseTexture(true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void render()
/*    */   {
/* 36 */     if ((this.is != null) && (this.world != null))
/*    */     {
/* 38 */       push();
/* 39 */       translate(this.origin.field_72450_a, this.origin.field_72448_b, this.origin.field_72449_c);
/* 40 */       scale(this.scale, this.scale, this.scale);
/* 41 */       enableTexture(true);
/* 42 */       RenderItem itemRenderer = Minecraft.func_71410_x().func_175599_af();
/* 43 */       if (itemRenderer != null)
/*    */       {
/* 45 */         itemRenderer.func_181564_a(this.is, ItemCameraTransforms.TransformType.NONE);
/*    */       }
/* 47 */       pop();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMItemStack.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */