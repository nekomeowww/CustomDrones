/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class CMVertex
/*    */   extends CMBase
/*    */ {
/*    */   public Vec3d pos;
/*    */   public Vec3d uv;
/*    */   public Vec3d normal;
/*    */   
/*    */   public CMVertex(Vec3d v1)
/*    */   {
/* 15 */     this.pos = v1;
/*    */   }
/*    */   
/*    */   public CMVertex(Vec3d v1, Vec3d v2)
/*    */   {
/* 20 */     this.pos = v1;
/* 21 */     this.uv = v2;
/*    */   }
/*    */   
/*    */   public CMVertex(Vec3d v1, Vec3d v2, Vec3d v3)
/*    */   {
/* 26 */     this.pos = v1;
/* 27 */     this.uv = v2;
/* 28 */     this.normal = v3;
/*    */   }
/*    */   
/*    */   public static Vec3d getMid(List<CMVertex> verts)
/*    */   {
/* 33 */     Vec3d total = new Vec3d(0.0D, 0.0D, 0.0D);
/* 34 */     if (verts.isEmpty()) return total;
/* 35 */     for (CMVertex vec : verts)
/*    */     {
/* 37 */       total = total.func_178787_e(vec.pos);
/*    */     }
/* 39 */     return scale(total, 1.0D / verts.size());
/*    */   }
/*    */   
/*    */   public CMVertex getClosest(List<CMVertex> verts)
/*    */   {
/* 44 */     if (verts.size() > 0)
/*    */     {
/* 46 */       CMVertex v0 = (CMVertex)verts.get(0);
/* 47 */       double distSqr = this.pos.func_72436_e(v0.pos);
/* 48 */       for (int a = 0; a < verts.size(); a++)
/*    */       {
/* 50 */         CMVertex v1 = (CMVertex)verts.get(a);
/* 51 */         double thisDistSqr = this.pos.func_72436_e(v1.pos);
/* 52 */         if (thisDistSqr < distSqr)
/*    */         {
/* 54 */           v0 = v1;
/* 55 */           distSqr = thisDistSqr;
/*    */         }
/*    */       }
/* 58 */       return v0;
/*    */     }
/* 60 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public void render()
/*    */   {
/* 66 */     super.render();
/* 67 */     begin(0);
/* 68 */     addToDrawing();
/* 69 */     end();
/*    */   }
/*    */   
/*    */   public void addToDrawing()
/*    */   {
/* 74 */     if (this.normal != null) normal(this.normal.field_72450_a, this.normal.field_72448_b, this.normal.field_72449_c);
/* 75 */     vertex(this.pos.field_72450_a, this.pos.field_72448_b, this.pos.field_72449_c, this.uv != null ? this.uv.field_72450_a : 0.0D, this.uv != null ? this.uv.field_72448_b : 0.0D);
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 81 */     String s = "Vert: " + this.pos + " UV: " + this.uv;
/* 82 */     if (this.normal != null) s = s + " Normal: " + this.normal;
/* 83 */     return s;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 89 */     if ((obj instanceof CMVertex))
/*    */     {
/*    */ 
/* 92 */       return (this.pos.equals(((CMVertex)obj).pos)) && ((this.normal == ((CMVertex)obj).normal) || (this.normal.equals(((CMVertex)obj).normal)));
/*    */     }
/* 94 */     return super.equals(obj);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMVertex.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */