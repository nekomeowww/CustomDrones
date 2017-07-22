/*    */ package williamle.drones.render;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMPipe;
/*    */ import williamle.drones.api.model.CMSphere;
/*    */ import williamle.drones.api.model.CapType;
/*    */ 
/*    */ public class ModelDroneUFO extends ModelDrone
/*    */ {
/*    */   public CMBase body;
/*    */   public CMBase bodyTop;
/*    */   public CMBase bodyBottom;
/*    */   public CMBase core;
/*    */   public CMBase engine;
/*    */   
/*    */   public ModelDroneUFO(net.minecraft.client.renderer.entity.RenderManager rm)
/*    */   {
/* 19 */     super(rm);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 25 */     this.models.clear();
/* 26 */     int bodySegs = 32;
/* 27 */     int engineSegs = 16;
/* 28 */     int coreDiv = 12;
/* 29 */     int ballDiv = 3;
/* 30 */     int ballCount = 4;
/* 31 */     double engineThick = 0.05D;
/* 32 */     double engineRa1 = 0.15D;
/* 33 */     double engineRa2 = 0.2D;
/* 34 */     double bottomThick = 0.1D;
/* 35 */     double bottomRa1 = engineRa2;
/* 36 */     double bottomRa2 = 0.6D;
/* 37 */     double bodyThick = 0.05D;
/* 38 */     double bodyRa = bottomRa2;
/* 39 */     double topThick = 0.1D;
/* 40 */     double topRa1 = bodyRa;
/* 41 */     double topRa2 = 0.25D;
/* 42 */     double coreRa = topRa2;
/* 43 */     double ballRa = 0.04D;
/*    */     
/* 45 */     this.fullDrone = new CMBase().setName("Full Drone");
/* 46 */     this.fullDrone.addChild(
/* 47 */       this.engine = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(0.0D, engineThick, 0.0D), engineRa1, engineRa2, engineSegs).setRenderCaps(CapType.BOTTOM).setPaletteIndexes(new String[] { "Engine" }).setName("Engine"));
/* 48 */     this.fullDrone.addChild(
/*    */     
/* 50 */       this.bodyBottom = new CMPipe(vec(0.0D, engineThick, 0.0D), vec(0.0D, engineThick + bottomThick, 0.0D), bottomRa1, bottomRa2, bodySegs).setRenderCaps(CapType.BOTTOM).setPaletteIndexes(new String[] { "Body bottom" }).setName("Body bottom"));
/* 51 */     this.fullDrone.addChild(
/*    */     
/* 53 */       this.body = new CMPipe(vec(0.0D, engineThick + bottomThick, 0.0D), vec(0.0D, engineThick + bottomThick + bodyThick, 0.0D), bodyRa, bodySegs).setRenderCaps(CapType.NONE).setPaletteIndexes(new String[] { "Body" }).setName("Body"));
/* 54 */     this.fullDrone.addChild(
/*    */     
/* 56 */       this.bodyTop = new CMPipe(vec(0.0D, engineThick + bottomThick + bodyThick, 0.0D), vec(0.0D, engineThick + bottomThick + bodyThick + topThick, 0.0D), topRa1, topRa2, bodySegs).setRenderCaps(CapType.TOP).setPaletteIndexes(new String[] { "Body top" }).setName("Body top"));
/* 57 */     this.fullDrone.addChild(
/* 58 */       this.wingsFull = new CMBase().setTranslate(0.0D, engineThick + bottomThick + bodyThick + topThick / 2.0D, 0.0D).setName("Wings"));
/* 59 */     for (int a = 0; a < ballCount; a++)
/*    */     {
/* 61 */       Vec3d vecCoreBall = vec((topRa1 + topRa2) / 2.0D, 0.0D, 0.0D);
/* 62 */       this.wingsFull.addChild(new CMSphere(vecCoreBall
/* 63 */         .func_178785_b((float)(a * 3.141592653589793D * 2.0D / ballCount)), ballRa, ballDiv)
/* 64 */         .setPaletteIndexes(new String[] { "Wing" }));
/*    */     }
/* 66 */     this.wingsFull.addChild(
/* 67 */       this.core = new CMSphere(vec(0.0D, 0.0D, 0.0D), coreRa, coreDiv).setTranslate(0.0D, topThick / 2.0D, 0.0D).setScale(1.0D, 0.5D, 1.0D).setPaletteIndexes(new String[] { "Core" }).setName("Core"));
/* 68 */     addModel(this.fullDrone);
/*    */   }
/*    */   
/*    */ 
/*    */   public double wingRotationRate()
/*    */   {
/* 74 */     return 0.1D;
/*    */   }
/*    */   
/*    */ 
/*    */   public double getLeanAngle()
/*    */   {
/* 80 */     return 80.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneUFO.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */