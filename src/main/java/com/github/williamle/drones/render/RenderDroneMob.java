/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderDroneMob<T extends EntityDroneMob>
/*    */   extends Render<T>
/*    */ {
/*    */   public RenderDroneMob(RenderManager renderManager)
/*    */   {
/* 16 */     super(renderManager);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 22 */     super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
/* 23 */     GL11.glPushMatrix();
/* 24 */     GL11.glTranslated(x, y, z);
/* 25 */     double d = entity.modelScale;
/* 26 */     GL11.glScaled(d, d, d);
/* 27 */     ModelDrone model = DroneModels.instance.getModelOrDefault(entity);
/* 28 */     model.doRender(entity, entityYaw, partialTicks, new Object[0]);
/* 29 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(T entity)
/*    */   {
/* 35 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\RenderDroneMob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */