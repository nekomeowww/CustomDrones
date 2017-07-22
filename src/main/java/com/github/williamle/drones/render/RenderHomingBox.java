/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.client.renderer.entity.Render;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.ResourceLocation;
/*    */ import net.minecraft.util.math.AxisAlignedBB;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ import williamle.drones.api.helpers.EntityHelper;
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMBox;
/*    */ import williamle.drones.api.model.Color;
/*    */ import williamle.drones.entity.EntityHomingBox;
/*    */ 
/*    */ public class RenderHomingBox<T extends EntityHomingBox> extends Render<T>
/*    */ {
/*    */   public RenderHomingBox(RenderManager renderManager)
/*    */   {
/* 20 */     super(renderManager);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
/*    */   {
/* 26 */     super.func_76986_a(entity, x, y, z, entityYaw, partialTicks);
/* 27 */     GL11.glPushMatrix();
/* 28 */     GL11.glTranslated(x, y, z);
/* 29 */     if ((entity != null) && (entity.target != null))
/*    */     {
/* 31 */       AxisAlignedBB aabb = entity.target.func_174813_aQ();
/* 32 */       if (aabb == null)
/*    */       {
/* 34 */         Vec3d vec = EntityHelper.getCenterVec(entity.target);
/* 35 */         aabb = new AxisAlignedBB(vec, vec);
/* 36 */         aabb.func_72314_b(entity.target.field_70130_N / 2.0F, entity.target.field_70131_O / 2.0F, entity.target.field_70130_N / 2.0F);
/*    */       }
/* 38 */       double expand = aabb.func_72320_b() / 4.0D;
/* 39 */       aabb = aabb.func_186662_g(expand);
/*    */       
/* 41 */       double thickness = Math.sqrt(x * x + y * y + z * z) / 100.0D;
/* 42 */       double xWidth = aabb.field_72336_d - aabb.field_72340_a;
/* 43 */       double yWidth = aabb.field_72337_e - aabb.field_72338_b;
/* 44 */       double zWidth = aabb.field_72334_f - aabb.field_72339_c;
/* 45 */       Color c = new Color(1.0D, 0.3D, 0.3D, 1.0D);
/* 46 */       CMBase bX = new CMBox(xWidth, thickness, thickness).setColor(c);
/* 47 */       CMBase bY = new CMBox(thickness, yWidth, thickness).setColor(c);
/* 48 */       CMBase bZ = new CMBox(thickness, thickness, zWidth).setColor(c);
/* 49 */       GL11.glPushMatrix();
/* 50 */       GL11.glTranslated(entity.target.field_70165_t - entity.field_70165_t, entity.target.field_70163_u - entity.field_70163_u - expand, entity.target.field_70161_v - entity.field_70161_v);
/*    */       
/*    */ 
/* 53 */       GL11.glPushMatrix();
/* 54 */       GL11.glTranslated(xWidth / 2.0D, 0.0D, 0.0D);
/* 55 */       bZ.fullRender();
/* 56 */       GL11.glTranslated(0.0D, yWidth, 0.0D);
/* 57 */       bZ.fullRender();
/* 58 */       GL11.glTranslated(-xWidth, 0.0D, 0.0D);
/* 59 */       bZ.fullRender();
/* 60 */       GL11.glTranslated(0.0D, -yWidth, 0.0D);
/* 61 */       bZ.fullRender();
/* 62 */       GL11.glPopMatrix();
/*    */       
/*    */ 
/* 65 */       GL11.glPushMatrix();
/* 66 */       GL11.glTranslated(0.0D, 0.0D, zWidth / 2.0D);
/* 67 */       bX.fullRender();
/* 68 */       GL11.glTranslated(0.0D, yWidth, 0.0D);
/* 69 */       bX.fullRender();
/* 70 */       GL11.glTranslated(0.0D, 0.0D, -zWidth);
/* 71 */       bX.fullRender();
/* 72 */       GL11.glTranslated(0.0D, -yWidth, 0.0D);
/* 73 */       bX.fullRender();
/* 74 */       GL11.glPopMatrix();
/*    */       
/*    */ 
/* 77 */       GL11.glPushMatrix();
/* 78 */       GL11.glTranslated(0.0D, yWidth / 2.0D, 0.0D);
/* 79 */       GL11.glTranslated(xWidth / 2.0D, 0.0D, zWidth / 2.0D);
/* 80 */       bY.fullRender();
/* 81 */       GL11.glTranslated(0.0D, 0.0D, -zWidth);
/* 82 */       bY.fullRender();
/* 83 */       GL11.glTranslated(-xWidth, 0.0D, 0.0D);
/* 84 */       bY.fullRender();
/* 85 */       GL11.glTranslated(0.0D, 0.0D, zWidth);
/* 86 */       bY.fullRender();
/* 87 */       GL11.glPopMatrix();
/*    */       
/* 89 */       GL11.glPopMatrix();
/*    */     }
/* 91 */     GL11.glPopMatrix();
/*    */   }
/*    */   
/*    */ 
/*    */   protected ResourceLocation getEntityTexture(T entity)
/*    */   {
/* 97 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\RenderHomingBox.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */