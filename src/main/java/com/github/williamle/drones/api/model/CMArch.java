/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ 
/*     */ public class CMArch
/*     */   extends CMBase
/*     */ {
/*     */   public Vec3d end1;
/*     */   public Vec3d end2;
/*     */   public Vec3d pull1;
/*     */   public Vec3d pull2;
/*     */   public int segments;
/*  14 */   public double pullMultiply = 0.5D;
/*     */   
/*  16 */   public double rounding = 1.0D;
/*     */   
/*  18 */   public double endMagnetivity = 0.5D;
/*     */   
/*     */   public Vec3d[] points;
/*     */   
/*     */   public CMArch(Vec3d e1, Vec3d e2, Vec3d p1, Vec3d p2, int segs)
/*     */   {
/*  24 */     this.end1 = e1;
/*  25 */     this.end2 = e2;
/*  26 */     this.pull1 = p1;
/*  27 */     this.pull2 = p2;
/*  28 */     this.segments = segs;
/*  29 */     this.points = calculatePoints();
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/*  35 */     enableLighting(false);
/*  36 */     lineWidth(3.0D);
/*  37 */     begin(3);
/*  38 */     for (Vec3d v : this.points)
/*     */     {
/*  40 */       vertex(v.field_72450_a, v.field_72448_b, v.field_72449_c, 0.0D, 0.0D);
/*     */     }
/*  42 */     end();
/*     */   }
/*     */   
/*     */   public Vec3d[] calculatePoints()
/*     */   {
/*  47 */     Vec3d[] list = new Vec3d[this.segments + 1];
/*  48 */     double pull1Length = this.pull1.func_72433_c();
/*  49 */     double pull2Length = this.pull2.func_72433_c();
/*  50 */     double pull1DifRatio = Math.pow((pull1Length + pull2Length) / Math.pow(pull2Length, this.rounding), 1.0D);
/*  51 */     double pull2DifRatio = Math.pow((pull1Length + pull2Length) / Math.pow(pull1Length, this.rounding), 1.0D);
/*  52 */     Vec3d baseLine = fromTo(this.end1, this.end2);
/*  53 */     double baseLineLength = baseLine.func_72433_c();
/*  54 */     Vec3d baseLineStep = setLength(baseLine, baseLineLength / this.segments);
/*     */     
/*  56 */     for (int a = 0; a <= this.segments; a++)
/*     */     {
/*  58 */       Vec3d thisBasePoint = this.end1.func_178787_e(scale(baseLineStep, a));
/*  59 */       double thisStep = a / this.segments;
/*     */       
/*     */ 
/*     */ 
/*  63 */       double pull1Strength = 1.0D - thisStep * (pull1DifRatio + 1.0D);
/*  64 */       if (pull1Strength <= 0.0D) pull1Strength /= pull1DifRatio;
/*  65 */       pull1Strength *= 1.5707963267948966D;
/*  66 */       double pull2Strength = 1.0D - (1.0D - thisStep) * (pull2DifRatio + 1.0D);
/*  67 */       if (pull2Strength < 0.0D) pull2Strength /= pull2DifRatio;
/*  68 */       pull2Strength *= 1.5707963267948966D;
/*  69 */       double pull1Mult = this.pullMultiply * Math.pow(Math.cos(pull1Strength), this.endMagnetivity);
/*  70 */       double pull2Mult = this.pullMultiply * Math.pow(Math.cos(pull2Strength), this.endMagnetivity);
/*  71 */       Vec3d thisArchPoint = thisBasePoint.func_178787_e(scale(this.pull1, pull1Mult)).func_178787_e(scale(this.pull2, pull2Mult));
/*  72 */       list[a] = thisArchPoint;
/*     */     }
/*  74 */     return list;
/*     */   }
/*     */   
/*     */   public double totalSegmentLength()
/*     */   {
/*  79 */     double l = 0.0D;
/*  80 */     for (int a = 0; a < this.points.length - 1; a++)
/*     */     {
/*  82 */       l += fromTo(this.points[a], this.points[(a + 1)]).func_72433_c();
/*     */     }
/*  84 */     return l;
/*     */   }
/*     */   
/*     */   public Vec3d getPointAtBaseRate(double d)
/*     */   {
/*  89 */     if ((d < 0.0D) || (d > 1.0D)) return null;
/*  90 */     double segmentPosition = d * this.segments;
/*  91 */     int prevSegment = (int)Math.floor(segmentPosition);
/*  92 */     double segMod = segmentPosition - prevSegment;
/*  93 */     Vec3d prevVec = this.points[prevSegment];
/*  94 */     Vec3d nextVec = this.points[(prevSegment + 1)];
/*  95 */     Vec3d prevToNext = fromTo(prevVec, nextVec);
/*  96 */     return prevVec.func_178787_e(scale(prevToNext, segMod));
/*     */   }
/*     */   
/*     */   public Vec3d getPointAtLengthRate(double d)
/*     */   {
/* 101 */     double totalLength = totalSegmentLength();
/* 102 */     double neededLength = d * totalLength;
/*     */     
/* 104 */     double currentLength = 0.0D;
/* 105 */     for (int a = 0; a < this.points.length - 1; a++)
/*     */     {
/* 107 */       Vec3d thisVec = this.points[a];
/* 108 */       Vec3d nextVec = this.points[(a + 1)];
/* 109 */       Vec3d thisToNext = fromTo(thisVec, nextVec);
/* 110 */       double thisLength = thisToNext.func_72433_c();
/* 111 */       if (currentLength == neededLength)
/*     */       {
/* 113 */         return thisVec;
/*     */       }
/* 115 */       if (currentLength + thisLength == neededLength)
/*     */       {
/* 117 */         return nextVec;
/*     */       }
/* 119 */       if (currentLength + thisLength > neededLength)
/*     */       {
/* 121 */         double exceedLength = neededLength - currentLength;
/* 122 */         double exceedRatio = exceedLength / thisLength;
/* 123 */         return thisVec.func_178787_e(scale(thisToNext, exceedRatio));
/*     */       }
/*     */       
/*     */ 
/* 127 */       currentLength += thisLength;
/*     */     }
/*     */     
/* 130 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMArch.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */