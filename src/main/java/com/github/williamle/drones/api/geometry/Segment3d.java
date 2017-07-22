/*     */ package williamle.drones.api.geometry;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ 
/*     */ public class Segment3d
/*     */   extends Line3d
/*     */ {
/*     */   public Vec3d bPoint;
/*     */   
/*     */   public Segment3d(Vec3d vec1, Vec3d vec2)
/*     */   {
/*  14 */     super(vec1, vec2.func_178788_d(vec1));
/*  15 */     this.bPoint = vec2;
/*     */   }
/*     */   
/*     */   public Vec3d getRandomPointOnSeg()
/*     */   {
/*  20 */     Random rnd = new Random();
/*  21 */     double rndRate = rnd.nextDouble();
/*  22 */     double x = (this.bPoint.field_72450_a - this.aPoint.field_72450_a) * rndRate + this.aPoint.field_72450_a;
/*  23 */     double y = (this.bPoint.field_72448_b - this.aPoint.field_72448_b) * rndRate + this.aPoint.field_72448_b;
/*  24 */     double z = (this.bPoint.field_72449_c - this.aPoint.field_72449_c) * rndRate + this.aPoint.field_72449_c;
/*  25 */     Vec3d vec = new Vec3d(x, y, z);
/*  26 */     return vec;
/*     */   }
/*     */   
/*     */   public Vec3d intersectWithoutBothEnds(Segment3d seg)
/*     */   {
/*  31 */     Vec3d intersect = intersectSegment(seg);
/*  32 */     if ((intersect != null) && (!intersect.equals(this.aPoint)) && (!intersect.equals(this.bPoint)) && (!intersect.equals(seg.aPoint)) && 
/*  33 */       (!intersect.equals(seg.bPoint)))
/*  34 */       return intersect;
/*  35 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3d intersectWithoutEnds(Line3d line)
/*     */   {
/*  40 */     Vec3d intersect = intersect(line);
/*  41 */     if ((intersect != null) && (!intersect.equals(this.aPoint)) && (!intersect.equals(this.bPoint))) return intersect;
/*  42 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Vec3d intersect(Line3d line)
/*     */   {
/*  48 */     Vec3d intersect = super.intersect(line);
/*  49 */     return inBoundBox(intersect) ? intersect : null;
/*     */   }
/*     */   
/*     */   public Vec3d intersectSegment(Segment3d seg)
/*     */   {
/*  54 */     Vec3d intersect = intersect(seg);
/*  55 */     return seg.inBoundBox(intersect) ? intersect : null;
/*     */   }
/*     */   
/*     */   public boolean onLine(Vec3d vec)
/*     */   {
/*  60 */     if ((vec.equals(this.aPoint)) || (vec.equals(this.bPoint))) return true;
/*  61 */     if (inBoundBox(vec))
/*     */     {
/*  63 */       return vec.func_178788_d(this.aPoint).func_72431_c(this.unit).func_72433_c() < 1.0E-18D;
/*     */     }
/*  65 */     return false;
/*     */   }
/*     */   
/*     */   public boolean inBoundBox(Vec3d vec)
/*     */   {
/*  70 */     if (vec == null) return false;
/*  71 */     double xmin = Math.min(this.aPoint.field_72450_a, this.bPoint.field_72450_a);
/*  72 */     double xmax = Math.max(this.aPoint.field_72450_a, this.bPoint.field_72450_a);
/*  73 */     double ymin = Math.min(this.aPoint.field_72448_b, this.bPoint.field_72448_b);
/*  74 */     double ymax = Math.max(this.aPoint.field_72448_b, this.bPoint.field_72448_b);
/*  75 */     double zmin = Math.min(this.aPoint.field_72449_c, this.bPoint.field_72449_c);
/*  76 */     double zmax = Math.max(this.aPoint.field_72449_c, this.bPoint.field_72449_c);
/*  77 */     return (vec.field_72450_a >= xmin) && (vec.field_72450_a <= xmax) && (vec.field_72448_b >= ymin) && (vec.field_72448_b <= ymax) && (vec.field_72449_c >= zmin) && (vec.field_72449_c <= zmax);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean connected(Segment3d seg)
/*     */   {
/*  84 */     return (this.aPoint.equals(seg.aPoint)) || (this.aPoint.equals(seg.bPoint)) || (this.bPoint.equals(seg.aPoint)) || (this.bPoint.equals(seg.bPoint));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  90 */     return "A: " + this.aPoint + " - B: " + this.bPoint;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  96 */     if ((obj instanceof Segment3d))
/*     */     {
/*  98 */       Segment3d seg = (Segment3d)obj;
/*     */       
/* 100 */       return ((seg.aPoint.equals(this.aPoint)) && (seg.bPoint.equals(this.bPoint))) || ((seg.aPoint.equals(this.bPoint)) && (seg.bPoint.equals(this.aPoint)));
/*     */     }
/* 102 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\geometry\Segment3d.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */