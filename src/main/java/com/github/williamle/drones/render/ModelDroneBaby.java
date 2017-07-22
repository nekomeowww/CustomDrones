/*    */ package williamle.drones.render;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMBox;
/*    */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModelDroneBaby
/*    */   extends ModelDrone
/*    */ {
/*    */   public CMBase body;
/*    */   public CMBase bodyTop;
/*    */   public CMBase bodyBottom;
/*    */   public CMBase core;
/*    */   public CMBase engine;
/*    */   
/*    */   public ModelDroneBaby(RenderManager rm)
/*    */   {
/* 23 */     super(rm);
/* 24 */     this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Baby", Integer.valueOf(-10066330));
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 30 */     this.models.clear();
/* 31 */     double coreThick = 0.1D;
/* 32 */     double bodyTopThick = 0.1D;
/* 33 */     double bodyThick = 0.4D;
/* 34 */     double bodyBottomThick = 0.1D;
/* 35 */     double coreSize = 0.4D;
/* 36 */     double bodyTopSize = 0.5D;
/* 37 */     double bodySize = 0.5D;
/* 38 */     double bodyBottomSize = 0.4D;
/* 39 */     double engineSize = 0.05D;
/* 40 */     double engineThick = 0.1D;
/* 41 */     double wingLength = 0.5D;
/* 42 */     double wingThick = 0.1D;
/*    */     
/* 44 */     this.fullDrone = new CMBase().setName("Full Drone");
/* 45 */     this.fullDrone.addChild(
/*    */     
/* 47 */       this.bodyBottom = new CMBox(bodyBottomSize, bodyBottomThick, bodyBottomSize, vec(0.0D, bodyBottomThick / 2.0D, 0.0D)).setName("Body bottom").setPaletteIndexes(new String[] { "Body bottom" }));
/* 48 */     this.fullDrone.addChild(
/* 49 */       this.body = new CMBox(bodySize, bodyThick, bodySize, vec(0.0D, bodyBottomThick + bodyThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
/* 50 */     this.fullDrone.addChild(
/*    */     
/* 52 */       this.bodyTop = new CMBox(bodyTopSize, bodyTopThick, bodyTopSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick / 2.0D, 0.0D)).setName("Body top").setPaletteIndexes(new String[] { "Body top" }));
/* 53 */     this.fullDrone.addChild(
/*    */     
/* 55 */       this.core = new CMBox(coreSize, coreThick, coreSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));
/*    */     
/* 57 */     this.fullDrone.addChild(
/*    */     
/* 59 */       this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));
/*    */     
/* 61 */     this.fullDrone.addChild(
/*    */     
/* 63 */       this.wingsFull = new CMBase().setTranslate(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick + engineThick, 0.0D).setName("Wings").setPaletteIndexes(new String[] { "Wing" }));
/* 64 */     setupWing(this.wingsFull, wingLength, wingThick);
/*    */     
/* 66 */     this.fullDrone.setCenter(0.0D, (bodyBottomThick + bodyThick + bodyTopThick + coreThick) / 2.0D, 0.0D);
/* 67 */     addModel(this.fullDrone);
/*    */   }
/*    */   
/*    */ 
/*    */   public double getLeanAngle()
/*    */   {
/* 73 */     return 80.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneBaby.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */