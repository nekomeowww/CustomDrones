/*    */ package williamle.drones.render;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.helpers.VecHelper;
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMPipe;
/*    */ import williamle.drones.api.model.CMTriangleMountain;
/*    */ import williamle.drones.api.model.CapType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelDroneFighter
/*    */   extends ModelDrone
/*    */ {
/*    */   public CMBase body;
/*    */   public CMBase bodyTop;
/*    */   public CMBase bodyBottom;
/*    */   public CMBase core;
/*    */   public CMBase engine;
/*    */   
/*    */   public ModelDroneFighter(RenderManager rm)
/*    */   {
/* 28 */     super(rm);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 34 */     this.models.clear();
/* 35 */     this.fullDrone = new CMBase().setName("Full Drone");
/*    */     
/* 37 */     Vec3d[] centerVs = VecHelper.flipAndLoop(new Vec3d[] {
/* 38 */       vec(0.0D, 0.5D), vec(0.6D, -0.2D), vec(0.5D, -0.4D), vec(0.0D, -0.3D) }, true, false, false);
/* 39 */     Vec3d[] bottomMidVs = VecHelper.translate(VecHelper.scale(centerVs, vec(0.0D, 0.05D), 0.9D), vecY(-0.025D));
/* 40 */     Vec3d[] bottomVs = VecHelper.translate(VecHelper.scale(centerVs, vec(0.0D, 0.05D), 0.8D), vecY(-0.04D));
/* 41 */     Vec3d[] topMidVs = VecHelper.mirror(bottomMidVs, false, true, false);
/* 42 */     Vec3d[] topVs = VecHelper.mirror(bottomVs, false, true, false);
/* 43 */     this.fullDrone.addChild(new CMTriangleMountain(CapType.BOTH, new Vec3d[][] { bottomVs, bottomMidVs, centerVs, topMidVs, topVs })
/* 44 */       .setPaletteIndexes(new String[] { "Body bottom", "Body", "Body", "Body top" }));
/*    */     
/* 46 */     this.fullDrone.addChild(new CMPipe(vec(-0.15D, 0.0D), vec(-0.15D, -0.37D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
/* 47 */     this.fullDrone.addChild(new CMPipe(vec(-0.05D, 0.0D), vec(-0.05D, -0.35D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
/* 48 */     this.fullDrone.addChild(new CMPipe(vec(0.05D, 0.0D), vec(0.05D, -0.35D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
/* 49 */     this.fullDrone.addChild(new CMPipe(vec(0.15D, 0.0D), vec(0.15D, -0.37D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
/* 50 */     addModel(this.fullDrone.setTranslate(0.0D, 0.15D, 0.0D));
/*    */   }
/*    */   
/*    */ 
/*    */   public double getLeanAngle()
/*    */   {
/* 56 */     return 15.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneFighter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */