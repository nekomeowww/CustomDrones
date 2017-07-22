/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RenderDrone<T extends EntityDrone>
/*    */   extends Render<T>
/*    */ {
/*    */   public RenderDrone(RenderManager renderManager)
/*    */   {
/* 17 */     super(renderManager);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 23 */     super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
/* 24 */     GL11.glPushMatrix();
/* 25 */     GL11.glTranslated(x, y, z);
/* 26 */     double d = 1.0D;
/* 27 */     GL11.glScaled(d, d, d);
/* 28 */     ModelDrone model = DroneModels.instance.getModelOrDefault(entity);
/* 29 */     model.doRender(entity, entityYaw, partialTicks, new Object[0]);
/* 30 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(T entity)
/*    */   {
/* 36 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\RenderDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */