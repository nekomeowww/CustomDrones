/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.model.CMPipe;
/*    */ import williamle.drones.entity.EntityPlasmaShot;
/*    */ 
/*    */ public class ModelPlasmaShot extends ModelBaseMod<EntityPlasmaShot>
/*    */ {
/*    */   public ModelPlasmaShot(RenderManager rm)
/*    */   {
/* 12 */     super(rm);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(EntityPlasmaShot bullet, float yaw, float partialTicks, Object... params)
/*    */   {
/* 18 */     if (bullet != null)
/*    */     {
/* 20 */       double d = 0.35D;
/* 21 */       Vec3d dir = vec(bullet.field_70159_w * d, bullet.field_70181_x * d, bullet.field_70179_y * d);
/* 22 */       double width = Math.sqrt(Math.min(8.0D, Math.max(1.0D, bullet.damage))) / 50.0D;
/* 23 */       CMPipe pipe = new CMPipe(vec(0.0D, 0.0D, 0.0D), dir, width / 5.0D, width, 10);
/* 24 */       pipe.setColor(bullet.color);
/* 25 */       pipe.fullRender();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelPlasmaShot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */