/*    */ package williamle.drones.render;
/*    */ 
/*    */ import williamle.drones.api.model.CMBase;
/*    */ 
/*    */ public class ModelDroneOriginal extends ModelDrone {
/*    */   public williamle.drones.api.model.CMPipe body1;
/*    */   public williamle.drones.api.model.CMPipe body2;
/*    */   public williamle.drones.api.model.CMPipe body3;
/*    */   public williamle.drones.api.model.CMPipe core;
/*    */   public CMBase leg1;
/*    */   public CMBase leg2;
/*    */   public CMBase leg3;
/*    */   public CMBase leg4;
/*    */   public CMBase arms1;
/*    */   public CMBase arms2;
/*    */   public CMBase piv1;
/*    */   public CMBase piv2;
/*    */   public CMBase piv3;
/*    */   public CMBase piv4;
/*    */   public CMBase propl1;
/*    */   public CMBase propl2;
/*    */   public CMBase propl3;
/*    */   public CMBase propl4;
/*    */   
/* 25 */   public ModelDroneOriginal(net.minecraft.client.renderer.entity.RenderManager rm) { super(rm); }
/*    */   
/*    */ 
/*    */ 
/*    */   public void setup()
/*    */   {
/* 31 */     this.models.clear();
/* 32 */     net.minecraft.util.math.Vec3d unitY = vec(0.0D, 1.0D, 0.0D);
/* 33 */     double sin45 = Math.sqrt(0.5D);
/* 34 */     double legEnd = 0.2025D;
/* 35 */     double armBaseLength = 0.5D;
/* 36 */     double pivotPos = armBaseLength * sin45;
/* 37 */     double armThick = 0.07D;
/* 38 */     double pivotRad = armThick * sin45;
/*    */     
/* 40 */     this.fullDrone = new CMBase().setName("Full Drone");
/* 41 */     this.fullDrone.setTranslate(0.0D, -0.05D, 0.0D);
/* 42 */     this.fullDrone.addChild(
/* 43 */       this.body1 = (williamle.drones.api.model.CMPipe)new williamle.drones.api.model.CMPipe(vec(0.0D, 0.1D, 0.0D), vec(0.0D, 0.15D, 0.0D), 0.25D, 8).setInitSpin(0.39269908169872414D).setName("Body1").setPaletteIndexes(new String[] { "Body bottom" }));
/* 44 */     this.fullDrone.addChild(
/* 45 */       this.body2 = (williamle.drones.api.model.CMPipe)new williamle.drones.api.model.CMPipe(vec(0.0D, 0.15D, 0.0D), vec(0.0D, 0.25D, 0.0D), 0.4D, 8).setInitSpin(0.39269908169872414D).setName("Body2").setPaletteIndexes(new String[] { "Body" }));
/* 46 */     this.fullDrone.addChild(
/* 47 */       this.body3 = (williamle.drones.api.model.CMPipe)new williamle.drones.api.model.CMPipe(vec(0.0D, 0.25D, 0.0D), vec(0.0D, 0.28D, 0.0D), 0.25D, 8).setInitSpin(0.39269908169872414D).setName("Body3").setPaletteIndexes(new String[] { "Body top" }));
/* 48 */     this.fullDrone.addChild(
/* 49 */       this.core = (williamle.drones.api.model.CMPipe)new williamle.drones.api.model.CMPipe(vec(0.0D, 0.28D, 0.0D), vec(0.0D, 0.3D, 0.0D), 0.1D, 16).setInitSpin(0.39269908169872414D).setName("Core").setPaletteIndexes(new String[] { "Core" }));
/*    */     
/* 51 */     this.fullDrone.addChild(
/* 52 */       this.leg1 = new williamle.drones.api.model.CMPipe(vec(0.0D, 0.25D, 0.0D), vec(legEnd, 0.05D, legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg1").setPaletteIndexes(new String[] { "Leg" }));
/* 53 */     this.fullDrone.addChild(
/* 54 */       this.leg2 = new williamle.drones.api.model.CMPipe(vec(0.0D, 0.25D, 0.0D), vec(legEnd, 0.05D, -legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg2").setPaletteIndexes(new String[] { "Leg" }));
/* 55 */     this.fullDrone.addChild(
/* 56 */       this.leg3 = new williamle.drones.api.model.CMPipe(vec(0.0D, 0.25D, 0.0D), vec(-legEnd, 0.05D, -legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg3").setPaletteIndexes(new String[] { "Leg" }));
/* 57 */     this.fullDrone.addChild(
/* 58 */       this.leg4 = new williamle.drones.api.model.CMPipe(vec(0.0D, 0.25D, 0.0D), vec(-legEnd, 0.05D, legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg4").setPaletteIndexes(new String[] { "Leg" }));
/*    */     
/* 60 */     this.fullDrone.addChild(
/*    */     
/* 62 */       this.arms1 = new williamle.drones.api.model.CMBox(armThick, armBaseLength * 2.0D + armThick, 0.03D).setTranslate(0.0D, 0.2D, 0.0D).setRotation(0.0D, 1.0D, 0.0D, 90.0D).setRotation(0.0D, 0.0D, 1.0D, 90.0D).setRotation(0.0D, 1.0D, 0.0D, 45.0D).setName("Arms1").setPaletteIndexes(new String[] { "Arm" }));
/* 63 */     this.fullDrone.addChild(
/*    */     
/* 65 */       this.arms2 = new williamle.drones.api.model.CMBox(armThick, armBaseLength * 2.0D + armThick, 0.03D).setTranslate(0.0D, 0.2D, 0.0D).setRotation(0.0D, 1.0D, 0.0D, 90.0D).setRotation(0.0D, 0.0D, 1.0D, 90.0D).setRotation(0.0D, 1.0D, 0.0D, -45.0D).setName("Arms2").setPaletteIndexes(new String[] { "Arm" }));
/*    */     
/* 67 */     this.fullDrone.addChild(
/*    */     
/* 69 */       this.piv1 = new williamle.drones.api.model.CMPipe(vec(pivotPos, 0.215D, pivotPos), vec(pivotPos, 0.32D, pivotPos), pivotRad, 0.0D, 4).setName("Pivot1").setPaletteIndexes(new String[] { "Engine" }));
/* 70 */     this.fullDrone.addChild(
/*    */     
/* 72 */       this.piv2 = new williamle.drones.api.model.CMPipe(vec(pivotPos, 0.215D, -pivotPos), vec(pivotPos, 0.32D, -pivotPos), pivotRad, 0.0D, 4).setName("Pivot2").setPaletteIndexes(new String[] { "Engine" }));
/* 73 */     this.fullDrone.addChild(
/*    */     
/* 75 */       this.piv3 = new williamle.drones.api.model.CMPipe(vec(-pivotPos, 0.215D, -pivotPos), vec(-pivotPos, 0.32D, -pivotPos), pivotRad, 0.0D, 4).setName("Pivot3").setPaletteIndexes(new String[] { "Engine" }));
/* 76 */     this.fullDrone.addChild(
/*    */     
/* 78 */       this.piv4 = new williamle.drones.api.model.CMPipe(vec(-pivotPos, 0.215D, pivotPos), vec(-pivotPos, 0.32D, pivotPos), pivotRad, 0.0D, 4).setName("Pivot4").setPaletteIndexes(new String[] { "Engine" }));
/*    */     
/* 80 */     this.wingsFull = new CMBase().setName("Wing Set").setPaletteIndexes(new String[] { "Wing" });
/* 81 */     setupWing(this.wingsFull, armBaseLength / 2.1D, 0.03D);
/* 82 */     this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(pivotPos, 0.32D, pivotPos).setName("Wings1")
/* 83 */       .setPaletteIndexes(new String[] { "Wing" }));
/* 84 */     this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(pivotPos, 0.32D, -pivotPos).setName("Wings2")
/* 85 */       .setPaletteIndexes(new String[] { "Wing" }));
/* 86 */     this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(-pivotPos, 0.32D, -pivotPos).setName("Wings3")
/* 87 */       .setPaletteIndexes(new String[] { "Wing" }));
/* 88 */     this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(-pivotPos, 0.32D, pivotPos).setName("Wings4")
/* 89 */       .setPaletteIndexes(new String[] { "Wing" }));
/*    */     
/* 91 */     this.fullDrone.setCenter(0.0D, 0.15D, 0.0D);
/* 92 */     addModel(this.fullDrone);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneOriginal.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */