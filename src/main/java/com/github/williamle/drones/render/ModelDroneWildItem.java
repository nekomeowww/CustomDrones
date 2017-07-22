/*    */ package williamle.drones.render;
/*    */ 
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import williamle.drones.api.model.CMBase;
/*    */ import williamle.drones.api.model.CMBox;
/*    */ import williamle.drones.api.model.CMItemStack;
/*    */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ import williamle.drones.entity.EntityDroneWildItem;
/*    */ 
/*    */ public class ModelDroneWildItem
/*    */   extends ModelDrone
/*    */ {
/*    */   public CMItemStack itemModel;
/*    */   public CMBase body;
/*    */   public CMBase bodyLeft;
/*    */   public CMBase bodyRight;
/*    */   public CMBase bodyTop;
/*    */   public CMBase bodyBottom;
/*    */   public CMBase core;
/*    */   public CMBase engine;
/*    */   
/*    */   public ModelDroneWildItem(RenderManager rm)
/*    */   {
/* 26 */     super(rm);
/* 27 */     this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Wild", Integer.valueOf(-10066330));
/*    */   }
/*    */   
/*    */ 
/*    */   public void setup()
/*    */   {
/* 33 */     this.models.clear();
/* 34 */     double itemHeight = 1.0D;
/* 35 */     double bodyThick = 0.15D;
/* 36 */     double bodyHeight = 0.7D;
/* 37 */     double bodyWidth = 0.8D;
/* 38 */     double coreThick = 0.1D;
/* 39 */     double coreWidth = 0.6D;
/* 40 */     double engineSize = 0.05D;
/* 41 */     double engineThick = 0.1D;
/* 42 */     double wingLength = 0.5D;
/* 43 */     double wingThick = 0.1D;
/*    */     
/* 45 */     this.fullDrone = new CMBase().setName("Full Drone");
/*    */     
/* 47 */     this.fullDrone.addChild(this.itemModel = (CMItemStack)new CMItemStack(vec(0.0D, itemHeight / 2.0D, 0.0D)).setName("Itemstack"));
/*    */     
/* 49 */     this.fullDrone.addChild(
/* 50 */       this.body = new CMBox(bodyWidth, bodyThick, bodyThick, vec(0.0D, itemHeight + bodyThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
/* 51 */     this.fullDrone.addChild(
/*    */     
/* 53 */       this.bodyLeft = new CMBox(bodyThick, bodyHeight, bodyThick, vec(-bodyWidth / 2.0D - bodyThick / 2.0D, itemHeight + bodyThick - bodyHeight / 2.0D, 0.0D)).setName("Body left").setPaletteIndexes(new String[] { "Body" }));
/* 54 */     this.fullDrone.addChild(
/*    */     
/* 56 */       this.bodyRight = new CMBox(bodyThick, bodyHeight, bodyThick, vec(bodyWidth / 2.0D + bodyThick / 2.0D, itemHeight + bodyThick - bodyHeight / 2.0D, 0.0D)).setName("Body right").setPaletteIndexes(new String[] { "Body" }));
/* 57 */     this.fullDrone.addChild(
/*    */     
/* 59 */       this.core = new CMBox(coreWidth, coreThick, coreThick, vec(0.0D, itemHeight + bodyThick + coreThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));
/*    */     
/* 61 */     this.fullDrone.addChild(
/*    */     
/* 63 */       this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, itemHeight + bodyThick + coreThick + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));
/*    */     
/* 65 */     this.fullDrone.addChild(
/* 66 */       this.wingsFull = new CMBase().setTranslate(0.0D, itemHeight + bodyThick + coreThick + engineThick, 0.0D).setName("Wings").setPaletteIndexes(new String[] { "Wing" }));
/* 67 */     setupWing(this.wingsFull, wingLength, wingThick);
/*    */     
/* 69 */     this.fullDrone.setCenter(0.0D, itemHeight + bodyThick / 2.0D, 0.0D);
/* 70 */     addModel(this.fullDrone);
/*    */   }
/*    */   
/*    */ 
/*    */   public void doRender(EntityDrone d, float yaw, float partialTicks, Object... params)
/*    */   {
/* 76 */     if (this.itemModel != null)
/*    */     {
/* 78 */       if (((d instanceof EntityDroneWildItem)) && (((EntityDroneWildItem)d).holding != null))
/*    */       {
/* 80 */         this.itemModel.is = ((EntityDroneWildItem)d).holding;
/* 81 */         this.itemModel.world = d.field_70170_p;
/*    */       }
/*    */       else
/*    */       {
/* 85 */         this.itemModel.is = null;
/* 86 */         this.itemModel.world = null;
/*    */       }
/*    */     }
/* 89 */     super.doRender(d, yaw, partialTicks, params);
/*    */   }
/*    */   
/*    */ 
/*    */   public double getLeanAngle()
/*    */   {
/* 95 */     return 80.0D;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelDroneWildItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */