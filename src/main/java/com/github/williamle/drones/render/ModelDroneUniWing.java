/*    */ package williamle.drones.render;
/*    */ 
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMBox;
/*    */ import williamle.drones.api.model.CMPipe;
/*    */ 
/*    */ public class ModelDroneUniWing extends ModelDrone
/*    */ {
/*    */   public CMBox body;
/*    */   public CMBox core;
/*    */   public CMBase leg1;
/*    */   public CMBase leg2;
/*    */   public CMBase leg3;
/*    */   public CMBase leg4;
/*    */   public CMBase arms1;
/*    */   public CMBase arms2;
/*    */   public CMBase engine;
/*    */   public CMBase propl1;
/*    */   public CMBase propl2;
/*    */   public CMBase propl3;
/*    */   public CMBase propl4;
/*    */   
/*    */   public ModelDroneUniWing(net.minecraft.client.renderer.entity.RenderManager rm)
/*    */   {
/* 25 */     super(rm);
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 31 */     this.models.clear();
/* 32 */     double bodySize = 0.3D;
/* 33 */     double bodyThick = 0.2D;
/* 34 */     double coreThick = 0.05D;
/* 35 */     double armThick = 0.06D;
/* 36 */     double armLength = 0.55D;
/* 37 */     double legThick = 0.03D;
/* 38 */     double legHeight = 0.15D;
/* 39 */     double wingLength = 0.5D;
/* 40 */     double engineSize = 0.04D;
/* 41 */     double engineThick = 0.1D;
/* 42 */     double wingThick = 0.06D;
/*    */     
/* 44 */     this.fullDrone = new CMBase().setName("Full Drone");
/* 45 */     this.fullDrone.addChild(
/* 46 */       this.body = (CMBox)new CMBox(bodySize, bodyThick, bodySize, vec(0.0D, legHeight + coreThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
/* 47 */     this.fullDrone.addChild(
/* 48 */       this.core = (CMBox)new CMBox(bodySize, coreThick, bodySize, vec(0.0D, legHeight - bodyThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));
/*    */     
/* 50 */     this.fullDrone.addChild(
/*    */     
/* 52 */       this.leg1 = new CMBox(legThick, legHeight, legThick, vec(0.0D, legHeight / 2.0D, armLength - legThick / 2.0D)).setName("Leg1").setPaletteIndexes(new String[] { "Leg" }));
/* 53 */     this.fullDrone.addChild(
/*    */     
/* 55 */       this.leg2 = new CMBox(legThick, legHeight, legThick, vec(0.0D, legHeight / 2.0D, -armLength + legThick / 2.0D)).setName("Leg2").setPaletteIndexes(new String[] { "Leg" }));
/* 56 */     this.fullDrone.addChild(
/*    */     
/* 58 */       this.leg3 = new CMBox(legThick, legHeight, legThick, vec(armLength - legThick / 2.0D, legHeight / 2.0D, 0.0D)).setName("Leg3").setPaletteIndexes(new String[] { "Leg" }));
/* 59 */     this.fullDrone.addChild(
/*    */     
/* 61 */       this.leg4 = new CMBox(legThick, legHeight, legThick, vec(-armLength + legThick / 2.0D, legHeight / 2.0D, 0.0D)).setName("Leg4").setPaletteIndexes(new String[] { "Leg" }));
/*    */     
/* 63 */     this.fullDrone.addChild(
/* 64 */       this.arms1 = new CMBox(armLength * 2.0D, armThick, armThick).setTranslate(0.0D, legHeight + armThick / 2.0D, 0.0D).setName("Arms1").setPaletteIndexes(new String[] { "Arm" }));
/* 65 */     this.fullDrone.addChild(
/* 66 */       this.arms2 = new CMBox(armThick, armThick, armLength * 2.0D).setTranslate(0.0D, legHeight + armThick / 2.0D, 0.0D).setName("Arms2").setPaletteIndexes(new String[] { "Arm" }));
/*    */     
/* 68 */     this.fullDrone.addChild(
/*    */     
/* 70 */       this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, legHeight + coreThick / 2.0D + bodyThick / 2.0D + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));
/*    */     
/* 72 */     this.wingsFull = new CMBase().setName("Wing Set");
/* 73 */     this.wingsFull.addChild(
/* 74 */       this.propl1 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller1"));
/* 75 */     this.wingsFull.addChild(
/* 76 */       this.propl2 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller2"));
/* 77 */     this.wingsFull.addChild(
/* 78 */       this.propl3 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller3"));
/* 79 */     this.wingsFull.addChild(
/* 80 */       this.propl4 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller4"));
/* 81 */     this.fullDrone.addChild(new CMBase().addChild(this.wingsFull)
/* 82 */       .setTranslate(0.0D, legHeight + coreThick / 2.0D + bodyThick / 2.0D + engineThick, 0.0D).setName("Wings")
/* 83 */       .setPaletteIndexes(new String[] { "Wing" }));
/*    */     
/* 85 */     this.fullDrone.setCenter(0.0D, legHeight, 0.0D);
/* 86 */     addModel(this.fullDrone);
/*    */   }
/*    */   
/*    */ 
/*    */   public double getLeanAngle()
/*    */   {
/* 92 */     return 60.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneUniWing.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */