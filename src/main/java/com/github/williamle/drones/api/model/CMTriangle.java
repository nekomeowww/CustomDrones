/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.geometry.Segment3d;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ 
/*     */ public class CMTriangle extends CMBase
/*     */ {
/*     */   public CMVertex v1;
/*     */   public CMVertex v2;
/*     */   public CMVertex v3;
/*     */   public Color outline;
/*     */   
/*     */   public CMTriangle(Vec3d ve1, Vec3d ve2, Vec3d ve3, Vec3d inner)
/*     */   {
/*  19 */     this.v1 = new CMVertex(ve1);
/*  20 */     this.v2 = new CMVertex(ve2);
/*  21 */     this.v3 = new CMVertex(ve3);
/*  22 */     orderAndNormal(inner);
/*     */   }
/*     */   
/*     */   public CMTriangle(CMVertex ve1, CMVertex ve2, CMVertex ve3)
/*     */   {
/*  27 */     this.v1 = ve1;
/*  28 */     this.v2 = ve2;
/*  29 */     this.v3 = ve3;
/*     */   }
/*     */   
/*     */   public CMTriangle orderAndNormal(Vec3d innerPoint)
/*     */   {
/*  34 */     Vec3d mid = VecHelper.getMid(new Vec3d[] { this.v1.pos, this.v2.pos, this.v3.pos });
/*  35 */     Vec3d currentNormal = getNormal();
/*  36 */     if (VecHelper.fromTo(innerPoint, mid).func_72430_b(currentNormal) < 0.0D)
/*     */     {
/*  38 */       CMVertex v4 = this.v2;
/*  39 */       this.v2 = this.v3;
/*  40 */       this.v3 = v4;
/*  41 */       setNormal(VecHelper.scale(currentNormal, -1.0D));
/*     */     }
/*     */     else
/*     */     {
/*  45 */       setNormal(currentNormal);
/*     */     }
/*  47 */     return this;
/*     */   }
/*     */   
/*     */   public CMTriangle setNormal(Vec3d norm)
/*     */   {
/*  52 */     this.v1.normal = norm;
/*  53 */     this.v2.normal = norm;
/*  54 */     this.v3.normal = norm;
/*  55 */     return this;
/*     */   }
/*     */   
/*     */   public List<Segment3d> segs()
/*     */   {
/*  60 */     List<Segment3d> list = new ArrayList();
/*  61 */     list.add(new Segment3d(this.v1.pos, this.v2.pos));
/*  62 */     list.add(new Segment3d(this.v2.pos, this.v3.pos));
/*  63 */     list.add(new Segment3d(this.v3.pos, this.v1.pos));
/*  64 */     return list;
/*     */   }
/*     */   
/*     */   public Vec3d getNormal()
/*     */   {
/*  69 */     return VecHelper.getPerpendicularVec(this.v1.pos.func_178788_d(this.v2.pos), this.v1.pos.func_178788_d(this.v3.pos));
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/*  75 */     super.render();
/*  76 */     begin(4);
/*  77 */     this.v1.addToDrawing();
/*  78 */     this.v2.addToDrawing();
/*  79 */     this.v3.addToDrawing();
/*  80 */     end();
/*  81 */     if (this.outline != null)
/*     */     {
/*  83 */       GL11.glLineWidth(1.0F);
/*  84 */       color(this.outline.red, this.outline.green, this.outline.blue, this.outline.alpha);
/*  85 */       begin(3);
/*  86 */       this.v1.addToDrawing();
/*  87 */       this.v2.addToDrawing();
/*  88 */       this.v3.addToDrawing();
/*  89 */       end();
/*  90 */       resetColor();
/*     */     }
/*     */   }
/*     */   
/*     */   public void renderReverse()
/*     */   {
/*  96 */     begin(4);
/*  97 */     this.v1.addToDrawing();
/*  98 */     this.v3.addToDrawing();
/*  99 */     this.v2.addToDrawing();
/* 100 */     end();
/* 101 */     if (this.outline != null)
/*     */     {
/* 103 */       GL11.glLineWidth(1.0F);
/* 104 */       color(this.outline.red, this.outline.green, this.outline.blue, this.outline.alpha);
/* 105 */       begin(3);
/* 106 */       this.v1.addToDrawing();
/* 107 */       this.v3.addToDrawing();
/* 108 */       this.v2.addToDrawing();
/* 109 */       end();
/* 110 */       resetColor();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 117 */     return "{1: " + this.v1 + "}-{2: " + this.v2 + "}-{3: " + this.v3 + "}";
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMTriangle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */