/*     */ package williamle.drones.render;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.renderer.entity.RenderManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.model.CMBase;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.api.model.UniqueName;
/*     */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ public class ModelBaseMod<T extends Entity>
/*     */ {
/*  19 */   public Map<UniqueName, CMBase> models = new HashMap();
/*     */   public RenderManager renderManager;
/*     */   public String name;
/*     */   public DroneAppearance.ColorPalette defaultPalette;
/*     */   
/*     */   public ModelBaseMod(RenderManager rm)
/*     */   {
/*  26 */     this.renderManager = rm;
/*     */   }
/*     */   
/*     */   public ModelBaseMod setName(String s)
/*     */   {
/*  31 */     this.name = s;
/*  32 */     return this;
/*     */   }
/*     */   
/*     */   public void doRender(T entity, float yaw, float partialTicks, Object... params)
/*     */   {
/*  37 */     for (CMBase cm : this.models.values())
/*     */     {
/*  39 */       cm.fullRender();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addModel(CMBase model)
/*     */   {
/*  45 */     this.models.put(model.cmName, model);
/*     */   }
/*     */   
/*     */   public Vec3d vec(double x, double y, double z)
/*     */   {
/*  50 */     return new Vec3d(x, y, z);
/*     */   }
/*     */   
/*     */   public Vec3d vec(double x, double z)
/*     */   {
/*  55 */     return new Vec3d(x, 0.0D, z);
/*     */   }
/*     */   
/*     */   public Vec3d vecY(double y)
/*     */   {
/*  60 */     return new Vec3d(0.0D, y, 0.0D);
/*     */   }
/*     */   
/*     */   public void bindTexture(ResourceLocation location)
/*     */   {
/*  65 */     this.renderManager.field_78724_e.func_110577_a(location);
/*     */   }
/*     */   
/*     */   public void applyAppearances(EntityDrone drone, float yaw, float partialTicks, Object... params)
/*     */   {
/*  70 */     applyDefaultAppearance();
/*     */   }
/*     */   
/*     */   public void applyDefaultAppearance()
/*     */   {
/*  75 */     if (this.defaultPalette != null)
/*     */     {
/*  77 */       applyPalette(this.defaultPalette);
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyPalette(DroneAppearance.ColorPalette p)
/*     */   {
/*  83 */     if (p != null)
/*     */     {
/*  85 */       for (CMBase cm : this.models.values())
/*     */       {
/*  87 */         setColorSingle(p, cm);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setColorSingle(DroneAppearance.ColorPalette palette, CMBase cm0)
/*     */   {
/*  94 */     List<String> indexes = cm0.getPaletteIndexes();
/*  95 */     for (String paletteIndex : indexes)
/*     */     {
/*  97 */       Color color = palette.getPaletteColorFor(paletteIndex);
/*  98 */       if (color != null)
/*     */       {
/* 100 */         cm0.setPaletteIndexColor(paletteIndex, color, true);
/*     */       }
/*     */     }
/* 103 */     for (CMBase cm : cm0.childModels)
/*     */     {
/* 105 */       setColorSingle(palette, cm);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\render\ModelBaseMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */