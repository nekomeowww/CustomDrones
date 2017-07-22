/*    */ package williamle.drones.render;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.renderer.entity.RenderManager;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ public class DroneModels
/*    */ {
/* 13 */   public int index = 0;
/*    */   public RenderManager rm;
/* 15 */   public List<ModelProp> models = new ArrayList();
/* 16 */   public Map<Integer, ModelDrone> modelIndex = new HashMap();
/*    */   public ModelProp original;
/*    */   public ModelProp uniWing;
/*    */   public ModelProp UFO;
/*    */   public ModelProp fighter;
/*    */   public ModelProp baby;
/*    */   public ModelProp wildItem;
/*    */   public static DroneModels instance;
/*    */   
/*    */   private DroneModels(RenderManager rm)
/*    */   {
/* 27 */     addModel(this.original = new ModelProp(new ModelDroneOriginal(rm).setName("Original"), this.index++, false));
/* 28 */     addModel(this.uniWing = new ModelProp(new ModelDroneUniWing(rm).setName("Uni-Wing"), this.index++, false));
/* 29 */     addModel(this.UFO = new ModelProp(new ModelDroneUFO(rm).setName("UFO"), this.index++, false));
/* 30 */     addModel(this.fighter = new ModelProp(new ModelDroneFighter(rm).setName("Fighter"), this.index++, false));
/* 31 */     addModel(this.baby = new ModelProp(new ModelDroneBaby(rm).setName("Baby"), this.index++, true));
/* 32 */     addModel(this.wildItem = new ModelProp(new ModelDroneWildItem(rm).setName("WildItem"), this.index++, true));
/*    */   }
/*    */   
/*    */   public void addModel(ModelProp prop)
/*    */   {
/* 37 */     this.models.add(prop);
/* 38 */     this.modelIndex.put(Integer.valueOf(prop.id), prop.model);
/*    */   }
/*    */   
/*    */   public ModelDrone getModelOrDefault(int id)
/*    */   {
/* 43 */     ModelDrone md = (ModelDrone)this.modelIndex.get(Integer.valueOf(id));
/* 44 */     return md == null ? this.original.model : md;
/*    */   }
/*    */   
/*    */   public ModelDrone getModelOrDefault(EntityDrone drone)
/*    */   {
/* 49 */     if (drone == null) return this.original.model;
/* 50 */     return getModelOrDefault(drone.droneInfo.appearance.modelID);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void init(RenderManager rm)
/*    */   {
/* 57 */     if ((instance == null) || (instance.rm != rm))
/*    */     {
/* 59 */       instance = new DroneModels(rm);
/*    */     }
/*    */   }
/*    */   
/*    */   public static class ModelProp
/*    */   {
/*    */     public ModelDrone model;
/*    */     public int id;
/*    */     public boolean isMobModel;
/*    */     
/*    */     public ModelProp(ModelDrone mod, int i, boolean mob)
/*    */     {
/* 71 */       this.model = mod;
/* 72 */       this.id = i;
/* 73 */       this.isMobModel = mob;
/*    */     }
/*    */     
/*    */ 
/*    */     public boolean equals(Object obj)
/*    */     {
/* 79 */       if ((obj instanceof ModelProp))
/*    */       {
/* 81 */         return (((ModelProp)obj).model == this.model) && (((ModelProp)obj).id == this.id) && (((ModelProp)obj).isMobModel == this.isMobModel);
/*    */       }
/*    */       
/* 84 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\DroneModels.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */