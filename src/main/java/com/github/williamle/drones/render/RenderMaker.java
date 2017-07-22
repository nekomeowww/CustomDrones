/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraftforge.fml.client.registry.IRenderFactory;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.entity.EntityDroneMob;
/*    */ import williamle.drones.entity.EntityHomingBox;
/*    */ import williamle.drones.entity.EntityPlasmaShot;
/*    */ 
/*    */ public class RenderMaker<B extends Entity> implements IRenderFactory<B>
/*    */ {
/*    */   Class clazz;
/*    */   
/*    */   public RenderMaker(Class<? super Entity> entityClass)
/*    */   {
/* 18 */     this.clazz = entityClass;
/*    */   }
/*    */   
/*    */ 
/*    */   public Render<? super B> createRenderFor(RenderManager manager)
/*    */   {
/* 24 */     DroneModels.init(manager);
/* 25 */     if (EntityHomingBox.class == this.clazz)
/*    */     {
/* 27 */       return new RenderHomingBox(manager);
/*    */     }
/* 29 */     if (EntityPlasmaShot.class == this.clazz)
/*    */     {
/* 31 */       return new RenderPlasmaBullet(manager);
/*    */     }
/* 33 */     if (EntityDroneMob.class.isAssignableFrom(this.clazz))
/*    */     {
/* 35 */       return new RenderDroneMob(manager);
/*    */     }
/* 37 */     if (EntityDrone.class == this.clazz)
/*    */     {
/* 39 */       return new RenderDrone(manager);
/*    */     }
/* 41 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\RenderMaker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */