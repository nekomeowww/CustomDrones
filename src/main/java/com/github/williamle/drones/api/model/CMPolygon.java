/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.geometry.Line3d;
/*     */ import williamle.drones.api.geometry.Segment3d;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMPolygon
/*     */   extends CMBase
/*     */ {
/*     */   public List<Vec3d> positions;
/*     */   public List<Vec3d> uvs;
/*     */   public Vec3d normal;
/*  21 */   public boolean renderFront = true;
/*  22 */   public boolean renderBack = true;
/*  23 */   public List<CMTriangle> triangles = new ArrayList();
/*     */   
/*     */   public CMPolygon(Vec3d[] pos, Vec3d[] uv, Vec3d nor)
/*     */   {
/*  27 */     this.positions = new ArrayList();
/*  28 */     this.uvs = new ArrayList();
/*  29 */     for (int a = 0; a < pos.length; a++)
/*     */     {
/*  31 */       Vec3d thisPos = pos[a];
/*  32 */       Vec3d toCompare = null;
/*  33 */       if ((a > 0) && (!this.positions.isEmpty()))
/*     */       {
/*  35 */         if (a == pos.length - 1) toCompare = (Vec3d)this.positions.get(0); else
/*  36 */           toCompare = (Vec3d)this.positions.get(this.positions.size() - 1);
/*     */       }
/*  38 */       if (!thisPos.equals(toCompare))
/*     */       {
/*  40 */         this.positions.add(thisPos);
/*  41 */         if ((uv != null) && (a < uv.length))
/*     */         {
/*  43 */           this.uvs.add(uv[a]);
/*     */         }
/*     */       }
/*     */     }
/*  47 */     this.normal = nor;
/*  48 */     triangulate();
/*     */   }
/*     */   
/*     */   public CMVertex generateVertex(Vec3d v1)
/*     */   {
/*  53 */     int index = this.positions.indexOf(v1);
/*  54 */     Vec3d uv1 = (this.uvs != null) && (index < this.uvs.size()) ? (Vec3d)this.uvs.get(index) : null;
/*  55 */     CMVertex vert1 = new CMVertex(v1, uv1, this.normal);
/*  56 */     return vert1;
/*     */   }
/*     */   
/*     */   public void triangulate()
/*     */   {
/*  61 */     List<Vec3d> posLeft = new ArrayList(this.positions);
/*  62 */     Vec3d startVec = (Vec3d)posLeft.get(0);
/*  63 */     while (posLeft.size() >= 3)
/*     */     {
/*  65 */       CMTriangle triangle = firstTriangle(posLeft, startVec, 0);
/*  66 */       if (triangle == null) break;
/*  67 */       this.triangles.add(triangle);
/*     */     }
/*     */   }
/*     */   
/*     */   public CMTriangle firstTriangle(List<Vec3d> poses, Vec3d startFrom, int loopcount)
/*     */   {
/*  73 */     if ((poses.size() < 3) || (loopcount > Math.pow(poses.size(), 2.0D)))
/*     */     {
/*  75 */       return null;
/*     */     }
/*  77 */     if (poses.size() == 3)
/*     */     {
/*     */ 
/*     */ 
/*  81 */       CMTriangle triangle = new CMTriangle(generateVertex((Vec3d)poses.get(0)), generateVertex((Vec3d)poses.get(1)), generateVertex((Vec3d)poses.get(2))).orderAndNormal(VecHelper.getMid(new Vec3d[] { (Vec3d)poses.get(0), (Vec3d)poses.get(1), (Vec3d)poses.get(2) }));
/*  82 */       poses.clear();
/*  83 */       return triangle;
/*     */     }
/*     */     
/*     */ 
/*  87 */     int index = poses.indexOf(startFrom);
/*  88 */     Vec3d next1 = (Vec3d)poses.get((index + 1) % poses.size());
/*  89 */     Vec3d next2 = (Vec3d)poses.get((index + 2) % poses.size());
/*     */     
/*  91 */     CMTriangle triangle = new CMTriangle(generateVertex(startFrom), generateVertex(next1), generateVertex(next2));
/*  92 */     if (!trianglePartOutsidePoly(triangle, this.positions))
/*     */     {
/*  94 */       poses.remove(next1);
/*  95 */       return triangle;
/*     */     }
/*  97 */     return firstTriangle(poses, next1, loopcount + 1);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean trianglePartOutsidePoly(CMTriangle triangle, List<Vec3d> poses)
/*     */   {
/* 103 */     List<Segment3d> triangleSegs = triangle.segs();
/* 104 */     List<Segment3d> segs = toSegments(poses);
/* 105 */     for (int a = 0; a < triangleSegs.size(); a++)
/*     */     {
/* 107 */       Segment3d tseg = (Segment3d)triangleSegs.get(a);
/* 108 */       int cutCount = 0;
/* 109 */       boolean isTriSegOnPoly = false;
/* 110 */       for (Segment3d seg : segs)
/*     */       {
/* 112 */         if (!tseg.connected(seg))
/*     */         {
/* 114 */           Vec3d intersect = tseg.intersectSegment(seg);
/* 115 */           if (intersect != null) cutCount++;
/*     */         }
/* 117 */         if (tseg.equals(seg)) isTriSegOnPoly = true;
/*     */       }
/* 119 */       if (cutCount > 0) return true;
/* 120 */       if (!isTriSegOnPoly)
/*     */       {
/* 122 */         Vec3d testPoint = VecHelper.getMid(new Vec3d[] { tseg.aPoint, tseg.bPoint });
/* 123 */         if (isPointOutSide(testPoint, segs)) return true;
/*     */       }
/*     */     }
/* 126 */     return false;
/*     */   }
/*     */   
/*     */   public List<Segment3d> toSegments(List<Vec3d> poses)
/*     */   {
/* 131 */     List<Segment3d> segs = new ArrayList();
/* 132 */     for (int a = 0; a < poses.size(); a++)
/*     */     {
/* 134 */       Vec3d v1 = (Vec3d)poses.get(a);
/* 135 */       Vec3d v2 = (Vec3d)poses.get((a + 1) % poses.size());
/* 136 */       segs.add(new Segment3d(v1, v2));
/*     */     }
/* 138 */     return segs;
/*     */   }
/*     */   
/*     */   public boolean isPointOutSide(Vec3d p, List<Segment3d> sides)
/*     */   {
/* 143 */     int outsideCount = 0;
/* 144 */     for (int a = 0; a < sides.size(); a++)
/*     */     {
/* 146 */       int cut = 0;
/* 147 */       Segment3d testSeg = (Segment3d)sides.get(a);
/* 148 */       Vec3d testEnd = VecHelper.getMid(new Vec3d[] { testSeg.aPoint, testSeg.bPoint });
/* 149 */       Line3d testLine = new Line3d(p, testEnd.func_178788_d(p));
/* 150 */       for (Segment3d side : sides)
/*     */       {
/* 152 */         if (side.onLine(p)) return false;
/* 153 */         Vec3d intersect = side.intersect(testLine);
/* 154 */         if ((intersect != null) && (!intersect.equals(p)) && (intersect.func_178788_d(p).func_72430_b(testLine.unit) > 0.0D))
/* 155 */           cut++;
/*     */       }
/* 157 */       outsideCount += (cut % 2 == 0 ? 1 : -1);
/*     */     }
/* 159 */     return outsideCount > 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/* 165 */     if (this.renderFront)
/*     */     {
/* 167 */       for (CMTriangle triangle : this.triangles)
/*     */       {
/* 169 */         triangle.render();
/*     */       }
/*     */     }
/* 172 */     if (this.renderBack)
/*     */     {
/* 174 */       for (CMTriangle triangle : this.triangles)
/*     */       {
/* 176 */         triangle.renderReverse();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMPolygon.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */