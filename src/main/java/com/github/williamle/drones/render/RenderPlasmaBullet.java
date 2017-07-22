/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import williamle.drones.entity.EntityPlasmaShot;
/*    */ 
/*    */ public class RenderPlasmaBullet<T extends EntityPlasmaShot>
/*    */   extends Render<T>
/*    */ {
/*    */   public static ModelPlasmaShot model;
/*    */   
/*    */   public RenderPlasmaBullet(RenderManager renderManager)
/*    */   {
/* 16 */     super(renderManager);
/* 17 */     model = new ModelPlasmaShot(renderManager);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 23 */     super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
/* 24 */     GL11.glPushMatrix();
/* 25 */     GL11.glTranslated(x, y, z);
/* 26 */     model.doRender(entity, entityYaw, partialTicks, new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) });
/* 27 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(T entity)
/*    */   {
/* 33 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\RenderPlasmaBullet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */