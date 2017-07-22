/*     */ package williamle.drones.api.helpers;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.util.math.MathHelper;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.geometry.Plane3d;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class VecHelper
/*     */ {
/*     */   public static Vec3d[] flipAndLoop(Vec3d[] ori, boolean x, boolean y, boolean z)
/*     */   {
/*  16 */     return removeDuplicate(combine(new Vec3d[][] { ori, reverse(mirror(ori, x, y, z)) }));
/*     */   }
/*     */   
/*     */   public static Vec3d[] removeDuplicate(Vec3d[] ori)
/*     */   {
/*  21 */     List<Vec3d> positions = new ArrayList();
/*  22 */     for (int a = 0; a < ori.length; a++)
/*     */     {
/*  24 */       Vec3d thisPos = ori[a];
/*  25 */       Vec3d toCompare = null;
/*  26 */       if ((a > 0) && (!positions.isEmpty()))
/*     */       {
/*  28 */         if (a == ori.length - 1) toCompare = (Vec3d)positions.get(0); else
/*  29 */           toCompare = (Vec3d)positions.get(positions.size() - 1);
/*     */       }
/*  31 */       if (!thisPos.equals(toCompare))
/*     */       {
/*  33 */         positions.add(thisPos);
/*     */       }
/*     */     }
/*  36 */     return (Vec3d[])positions.toArray(new Vec3d[0]);
/*     */   }
/*     */   
/*     */   public static Vec3d[] translate(Vec3d[] ori, Vec3d translate)
/*     */   {
/*  41 */     Vec3d[] newA = new Vec3d[ori.length];
/*  42 */     for (int a = 0; a < ori.length; a++)
/*     */     {
/*  44 */       Vec3d oriVec = ori[a];
/*  45 */       newA[a] = oriVec.func_178787_e(translate);
/*     */     }
/*  47 */     return newA;
/*     */   }
/*     */   
/*     */   public static Vec3d[] scale(Vec3d[] ori, Vec3d center, double d)
/*     */   {
/*  52 */     Vec3d[] newA = new Vec3d[ori.length];
/*  53 */     for (int a = 0; a < ori.length; a++)
/*     */     {
/*  55 */       Vec3d oriVec = ori[a];
/*  56 */       newA[a] = oriVec.func_178788_d(center).func_186678_a(d).func_178787_e(center);
/*     */     }
/*  58 */     return newA;
/*     */   }
/*     */   
/*     */   public static Vec3d[] scaleAbsolute(Vec3d[] ori, Vec3d center, double d)
/*     */   {
/*  63 */     Vec3d[] newA = new Vec3d[ori.length];
/*  64 */     for (int a = 0; a < ori.length; a++)
/*     */     {
/*  66 */       Vec3d oriVec = ori[a];
/*  67 */       Vec3d oriDelta = oriVec.func_178788_d(center);
/*  68 */       double deltaLength = oriDelta.func_72433_c();
/*  69 */       newA[a] = oriDelta.func_186678_a((deltaLength - d) / deltaLength).func_178787_e(center);
/*     */     }
/*  71 */     return newA;
/*     */   }
/*     */   
/*     */   public static Vec3d[] combine(Vec3d[]... vecs)
/*     */   {
/*  76 */     int a = 0;
/*  77 */     for (Vec3d[] vecss : vecs)
/*     */     {
/*  79 */       a += vecss.length;
/*     */     }
/*  81 */     Vec3d[] total = new Vec3d[a];
/*  82 */     int index = 0;
/*  83 */     for (int b = 0; b < vecs.length; b++)
/*     */     {
/*  85 */       Vec3d[] vecss = vecs[b];
/*  86 */       for (int c = 0; c < vecss.length; c++)
/*     */       {
/*  88 */         total[index] = vecss[c];
/*  89 */         index++;
/*     */       }
/*     */     }
/*  92 */     return total;
/*     */   }
/*     */   
/*     */   public static Vec3d[] mirror(Vec3d[] ori, boolean x, boolean y, boolean z)
/*     */   {
/*  97 */     Vec3d[] newA = new Vec3d[ori.length];
/*  98 */     for (int a = 0; a < newA.length; a++)
/*     */     {
/* 100 */       Vec3d oriVec = ori[a];
/* 101 */       Vec3d newVec = new Vec3d(oriVec.field_72450_a * (x ? -1 : 1), oriVec.field_72448_b * (y ? -1 : 1), oriVec.field_72449_c * (z ? -1 : 1));
/*     */       
/* 103 */       newA[a] = newVec;
/*     */     }
/* 105 */     return newA;
/*     */   }
/*     */   
/*     */   public static Vec3d[] reverse(Vec3d[] ori)
/*     */   {
/* 110 */     Vec3d[] newA = new Vec3d[ori.length];
/* 111 */     for (int a = 0; a < ori.length; a++)
/*     */     {
/* 113 */       newA[(ori.length - a - 1)] = ori[a];
/*     */     }
/* 115 */     return newA;
/*     */   }
/*     */   
/*     */   public static Vec3d getClosest(Vec3d ori, Vec3d[] vecs)
/*     */   {
/* 120 */     if (vecs.length > 0)
/*     */     {
/* 122 */       Vec3d v0 = vecs[0];
/* 123 */       double distSqr = v0 != null ? ori.func_72436_e(v0) : Double.MAX_VALUE;
/* 124 */       for (int a = 0; a < vecs.length; a++)
/*     */       {
/* 126 */         Vec3d v1 = vecs[a];
/* 127 */         if (v1 != null)
/*     */         {
/* 129 */           double thisDistSqr = ori.func_72436_e(v1);
/* 130 */           if (thisDistSqr <= distSqr)
/*     */           {
/* 132 */             v0 = v1;
/* 133 */             distSqr = thisDistSqr;
/*     */           }
/*     */         }
/*     */       }
/* 137 */       return v0;
/*     */     }
/* 139 */     return null;
/*     */   }
/*     */   
/*     */   public static Vec3d getFarthest(Vec3d ori, Vec3d[] vecs)
/*     */   {
/* 144 */     if (vecs.length > 0)
/*     */     {
/* 146 */       Vec3d v0 = vecs[0];
/* 147 */       double distSqr = v0 != null ? ori.func_72436_e(v0) : 0.0D;
/* 148 */       for (int a = 0; a < vecs.length; a++)
/*     */       {
/* 150 */         Vec3d v1 = vecs[a];
/* 151 */         if (v1 != null)
/*     */         {
/* 153 */           double thisDistSqr = ori.func_72436_e(v1);
/* 154 */           if (thisDistSqr >= distSqr)
/*     */           {
/* 156 */             v0 = v1;
/* 157 */             distSqr = thisDistSqr;
/*     */           }
/*     */         }
/*     */       }
/* 161 */       return v0;
/*     */     }
/* 163 */     return null;
/*     */   }
/*     */   
/*     */   public static Vec3d getMidList(List<Vec3d> vecs)
/*     */   {
/* 168 */     Vec3d total = new Vec3d(0.0D, 0.0D, 0.0D);
/* 169 */     if (vecs.size() == 0) return total;
/* 170 */     for (Vec3d vec : vecs)
/*     */     {
/* 172 */       total = total.func_178787_e(vec);
/*     */     }
/* 174 */     return scale(total, 1.0D / vecs.size());
/*     */   }
/*     */   
/*     */   public static Vec3d getMid(Vec3d... vecs)
/*     */   {
/* 179 */     Vec3d total = new Vec3d(0.0D, 0.0D, 0.0D);
/* 180 */     if (vecs.length == 0) return total;
/* 181 */     for (Vec3d vec : vecs)
/*     */     {
/* 183 */       total = total.func_178787_e(vec);
/*     */     }
/* 185 */     return scale(total, 1.0D / vecs.length);
/*     */   }
/*     */   
/*     */   public static Vec3d getPerpendicularVec(Vec3d vec1, Vec3d vec2)
/*     */   {
/* 190 */     Vec3d perp = vec1.func_72431_c(vec2);
/* 191 */     if (isZeroVec(perp))
/*     */     {
/* 193 */       if ((vec1.field_72450_a == 0.0D) && (vec1.field_72449_c == 0.0D))
/*     */       {
/* 195 */         perp = vec1.func_72431_c(vec(1.0D, 0.0D, 0.0D));
/*     */       }
/*     */       else
/*     */       {
/* 199 */         perp = vec1.func_72431_c(vec(0.0D, 1.0D, 0.0D));
/*     */       }
/*     */     }
/* 202 */     return perp;
/*     */   }
/*     */   
/*     */ 
/*     */   public static double getAngleBetween(Vec3d vec1, Vec3d vec2)
/*     */   {
/* 208 */     if ((isZeroVec(vec1)) || (isZeroVec(vec2))) return 0.0D;
/* 209 */     return Math.acos(Math.min(1.0D, vec1.func_72430_b(vec2) / (vec1.func_72433_c() * vec2.func_72433_c())));
/*     */   }
/*     */   
/*     */   public static boolean isParallel(Vec3d vec1, Vec3d vec2)
/*     */   {
/* 214 */     double angle = getAngleBetween(vec1, vec2);
/* 215 */     return (Math.abs(angle) < 1.0E-8D) || (Math.abs(angle - 3.141592653589793D) < 1.0E-15D);
/*     */   }
/*     */   
/*     */   public static boolean isPerpendicular(Vec3d vec1, Vec3d vec2)
/*     */   {
/* 220 */     double angle = getAngleBetween(vec1, vec2);
/* 221 */     return Math.abs(angle) < 1.0E-8D;
/*     */   }
/*     */   
/*     */   public static Vec3d fromTo(Vec3d vec1, Vec3d vec2)
/*     */   {
/* 226 */     return vec2.func_178788_d(vec1);
/*     */   }
/*     */   
/*     */   public static Vec3d rotateAround(Vec3d init, Vec3d axis, double angleRad)
/*     */   {
/* 231 */     Vec3d axisN = axis.func_72432_b();
/* 232 */     Vec3d initN = init;
/* 233 */     Vec3d v1 = initN.func_72431_c(axisN);
/* 234 */     Vec3d v2 = axisN.func_72431_c(v1);
/* 235 */     v1 = scale(v1, Math.sin(angleRad));
/* 236 */     v2 = scale(v2, Math.cos(angleRad));
/* 237 */     Vec3d shadowRotated = v1.func_178787_e(v2);
/* 238 */     double dotProduct = axisN.func_72430_b(initN);
/* 239 */     return setLength(scale(axisN, dotProduct).func_178787_e(shadowRotated), init.func_72433_c());
/*     */   }
/*     */   
/*     */   public static Vec3d getVectorForRotation(float pitch, float yaw)
/*     */   {
/* 244 */     float f = MathHelper.func_76134_b(-yaw * 0.017453292F - 3.1415927F);
/* 245 */     float f1 = MathHelper.func_76126_a(-yaw * 0.017453292F - 3.1415927F);
/* 246 */     float f2 = -MathHelper.func_76134_b(-pitch * 0.017453292F);
/* 247 */     float f3 = MathHelper.func_76126_a(-pitch * 0.017453292F);
/* 248 */     return new Vec3d(f1 * f2, f3, f * f2);
/*     */   }
/*     */   
/*     */   public static Vec3d vec(double x, double y, double z)
/*     */   {
/* 253 */     return new Vec3d(x, y, z);
/*     */   }
/*     */   
/*     */   public static Vec3d scale(Vec3d vec, double d)
/*     */   {
/* 258 */     return vec(vec.field_72450_a * d, vec.field_72448_b * d, vec.field_72449_c * d);
/*     */   }
/*     */   
/*     */   public static Vec3d setLength(Vec3d vec, double l)
/*     */   {
/* 263 */     return scale(vec, l / vec.func_72433_c());
/*     */   }
/*     */   
/*     */   public static boolean isZeroVec(Vec3d vec)
/*     */   {
/* 268 */     return (vec.field_72450_a == 0.0D) && (vec.field_72448_b == 0.0D) && (vec.field_72449_c == 0.0D);
/*     */   }
/*     */   
/*     */   public static Vec3d jitter(Vec3d ori, double angleRad)
/*     */   {
/* 273 */     Random rnd = new Random();
/* 274 */     double oriLength = ori.func_72433_c();
/*     */     
/* 276 */     Plane3d plane = new Plane3d(ori, ori);
/* 277 */     double planeVecLength = Math.tan(angleRad) * oriLength * rnd.nextDouble();
/*     */     
/* 279 */     Vec3d fixPlaneVec = plane.getAVecOnPlane().func_72432_b().func_186678_a(planeVecLength);
/*     */     
/* 281 */     Vec3d varPlaneVec = isZeroVec(fixPlaneVec) ? Vec3d.field_186680_a : rotateAround(fixPlaneVec, ori, rnd.nextDouble() * 3.141592653589793D * 2.0D);
/*     */     
/* 283 */     Vec3d newDir = ori.func_178787_e(varPlaneVec);
/* 284 */     return newDir.func_72432_b().func_186678_a(oriLength);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\VecHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */