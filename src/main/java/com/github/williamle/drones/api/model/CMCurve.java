/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ public class CMCurve extends CMBase
/*     */ {
/*     */   public int segments;
/*     */   public Vec3d[] vecs;
/*   9 */   public double pullMult = 0.2D;
/*     */   public CMArch[] arches;
/*     */   
/*     */   public CMCurve(boolean autoPull, int segmentsPerArch, Vec3d[] vec3s)
/*     */   {
/*  14 */     this.segments = segmentsPerArch;
/*  15 */     this.vecs = (autoPull ? autoCalculatePull(vec3s) : vec3s);
/*  16 */     this.arches = calculateArches();
/*     */   }
/*     */   
/*     */ 
/*     */   public Vec3d[] autoCalculatePull(Vec3d[] vec3s)
/*     */   {
/*  22 */     if ((vec3s == null) || (vec3s.length == 0)) { return new Vec3d[0];
/*     */     }
/*  24 */     Vec3d[] vecs = new Vec3d[vec3s.length * 2];
/*  25 */     if (vec3s.length == 1) { vecs[1] = vec3s[0];
/*  26 */     } else if (vec3s.length == 2)
/*     */     {
/*  28 */       vecs[1] = fromTo(vec3s[0], vec3s[1]);
/*  29 */       vecs[3] = fromTo(vec3s[1], vec3s[0]);
/*     */     }
/*     */     else
/*     */     {
/*  33 */       for (int a = 1; a < vec3s.length - 1; a++)
/*     */       {
/*  35 */         Vec3d thisVec = vec3s[a];
/*  36 */         Vec3d prevVec = vec3s[(a - 1)];
/*  37 */         Vec3d nextVec = vec3s[(a + 1)];
/*  38 */         Vec3d pull = scale(fromTo(prevVec, thisVec).func_178787_e(fromTo(nextVec, thisVec)), this.pullMult);
/*  39 */         vecs[(a * 2)] = thisVec;
/*  40 */         vecs[(a * 2 + 1)] = pull;
/*     */       }
/*  42 */       Vec3d firstVec = vec3s[0];
/*  43 */       vecs[0] = firstVec;
/*  44 */       Vec3d secondVec = vec3s[1];
/*  45 */       Vec3d secondPull = vecs[3];
/*  46 */       Vec3d firstMid = scale(firstVec.func_178787_e(secondVec), 0.5D);
/*  47 */       Vec3d firstToSecond = fromTo(firstVec, secondVec);
/*  48 */       if (secondPull.func_72430_b(firstToSecond) == 0.0D)
/*     */       {
/*  50 */         vecs[1] = secondPull;
/*     */       }
/*     */       else
/*     */       {
/*  54 */         double d = fromTo(secondVec, firstMid).func_72430_b(firstToSecond) / firstToSecond.func_72430_b(secondPull);
/*  55 */         Vec3d intersect = secondVec.func_178787_e(scale(secondPull, d));
/*  56 */         Vec3d firstPull = setLength(fromTo(firstVec, intersect), secondPull.func_72433_c());
/*     */         
/*  58 */         if (firstPull.func_72430_b(fromTo(firstVec, intersect)) * secondPull.func_72430_b(fromTo(secondVec, intersect)) < 0.0D)
/*  59 */           firstPull = scale(firstPull, -1.0D);
/*  60 */         vecs[1] = firstPull;
/*     */       }
/*     */       
/*  63 */       Vec3d lastVec = vec3s[(vec3s.length - 1)];
/*  64 */       vecs[(vecs.length - 2)] = lastVec;
/*  65 */       Vec3d secondlastVec = vec3s[(vec3s.length - 2)];
/*  66 */       Vec3d secondLastPull = vecs[(vec3s.length * 2 - 3)];
/*  67 */       Vec3d lastMid = scale(lastVec.func_178787_e(secondlastVec), 0.5D);
/*  68 */       Vec3d lastToSecond = fromTo(firstVec, secondVec);
/*  69 */       if (secondLastPull.func_72430_b(lastToSecond) == 0.0D)
/*     */       {
/*  71 */         vecs[(vecs.length - 1)] = secondLastPull;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*  76 */         double d = fromTo(secondlastVec, lastMid).func_72430_b(lastToSecond) / lastToSecond.func_72430_b(secondLastPull);
/*  77 */         Vec3d intersect = secondlastVec.func_178787_e(scale(secondLastPull, d));
/*  78 */         Vec3d lastPull = setLength(fromTo(lastVec, intersect), secondLastPull.func_72433_c());
/*     */         
/*  80 */         if (lastPull.func_72430_b(fromTo(lastVec, intersect)) * secondLastPull.func_72430_b(fromTo(secondlastVec, intersect)) > 0.0D)
/*  81 */           lastPull = scale(lastPull, -1.0D);
/*  82 */         vecs[(vecs.length - 1)] = lastPull;
/*     */       }
/*     */     }
/*  85 */     return vecs;
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/*  91 */     for (CMArch arch : this.arches)
/*     */     {
/*  93 */       arch.render();
/*     */     }
/*     */   }
/*     */   
/*     */   public CMArch[] calculateArches()
/*     */   {
/*  99 */     int archCount = (this.vecs.length - 2) / 2;
/* 100 */     CMArch[] arch = new CMArch[archCount];
/* 101 */     for (int a = 0; a < archCount; a++)
/*     */     {
/* 103 */       Vec3d end1 = this.vecs[(a * 2)];
/* 104 */       Vec3d end1Pull = scale(this.vecs[(a * 2 + 1)], a == 0 ? 1.0D : -1.0D);
/* 105 */       Vec3d end2 = this.vecs[((a + 1) * 2)];
/* 106 */       Vec3d end2Pull = this.vecs[((a + 1) * 2 + 1)];
/* 107 */       CMArch anarch = new CMArch(end1, end2, end1Pull, end2Pull, this.segments);
/* 108 */       arch[a] = anarch;
/*     */     }
/* 110 */     return arch;
/*     */   }
/*     */   
/*     */   public double getTotalLength(boolean arched)
/*     */   {
/* 115 */     double length = 0.0D;
/* 116 */     int a; Vec3d thisVec; Vec3d nextVec; if (!arched)
/*     */     {
/* 118 */       for (a = 0; a < this.vecs.length - 3; a += 2)
/*     */       {
/* 120 */         thisVec = this.vecs[a];
/* 121 */         nextVec = this.vecs[(a + 2)];
/* 122 */         length += fromTo(thisVec, nextVec).func_72433_c();
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 127 */       a = this.arches;thisVec = a.length; for (nextVec = 0; nextVec < thisVec; nextVec++) { CMArch arch = a[nextVec];
/*     */         
/* 129 */         length += arch.totalSegmentLength();
/*     */       }
/*     */     }
/* 132 */     return length;
/*     */   }
/*     */   
/*     */   public Vec3d getPointAtBaseRate(double d)
/*     */   {
/* 137 */     double totalLength = getTotalLength(false);
/* 138 */     double neededLength = totalLength * d;
/* 139 */     double currentLength = 0.0D;
/* 140 */     for (int a = 0; a < this.vecs.length - 3; a += 2)
/*     */     {
/* 142 */       Vec3d thisVec = this.vecs[a];
/* 143 */       Vec3d nextVec = this.vecs[(a + 2)];
/* 144 */       Vec3d thisToNextVec = fromTo(thisVec, nextVec);
/* 145 */       double nextLength = thisToNextVec.func_72433_c();
/* 146 */       if (currentLength == neededLength)
/*     */       {
/* 148 */         return thisVec;
/*     */       }
/* 150 */       if (currentLength + nextLength == neededLength)
/*     */       {
/* 152 */         return nextVec;
/*     */       }
/* 154 */       if (currentLength + nextLength > neededLength)
/*     */       {
/* 156 */         double exceedLength = neededLength - currentLength;
/* 157 */         double exceedRatio = exceedLength / nextLength;
/* 158 */         CMArch thisArch = this.arches[(a / 2)];
/* 159 */         return thisArch.getPointAtBaseRate(exceedRatio);
/*     */       }
/* 161 */       currentLength += nextLength;
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public Vec3d getPointAtLengthRate(double d)
/*     */   {
/* 168 */     double totalLength = getTotalLength(true);
/* 169 */     double neededLength = totalLength * d;
/* 170 */     double currentLength = 0.0D;
/* 171 */     for (int a = 0; a < this.arches.length; a++)
/*     */     {
/* 173 */       CMArch thisArch = this.arches[a];
/* 174 */       double thisLength = thisArch.totalSegmentLength();
/* 175 */       if (currentLength == neededLength)
/*     */       {
/* 177 */         return thisArch.end1;
/*     */       }
/* 179 */       if (currentLength + thisLength == neededLength)
/*     */       {
/* 181 */         return thisArch.end2;
/*     */       }
/* 183 */       if (currentLength + thisLength > neededLength)
/*     */       {
/* 185 */         double exceedLength = neededLength - currentLength;
/* 186 */         double exceedRatio = exceedLength / thisLength;
/* 187 */         return thisArch.getPointAtLengthRate(exceedRatio);
/*     */       }
/* 189 */       currentLength += thisLength;
/*     */     }
/* 191 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMCurve.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */