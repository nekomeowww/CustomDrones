/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ 
/*     */ public class CMTriangleMountain extends CMBase
/*     */ {
/*  14 */   public List<List<CMTriangle>> trianglesLayers = new ArrayList();
/*     */   public CMPolygon topCap;
/*     */   public CMPolygon bottomCap;
/*     */   
/*     */   public CMTriangleMountain(CapType cap, List<Vec3d>... layers) {
/*  19 */     for (int a = 0; a < layers.length - 1; a++)
/*     */     {
/*  21 */       List<Vec3d> thisLayer = layers[a];
/*  22 */       List<Vec3d> nextLayer = layers[(a + 1)];
/*  23 */       boolean nextHasMore = nextLayer.size() > thisLayer.size();
/*  24 */       this.trianglesLayers
/*  25 */         .add(makeTriangles(nextHasMore ? nextLayer : thisLayer, nextHasMore ? thisLayer : nextLayer));
/*     */     }
/*  27 */     if (cap != CapType.NONE)
/*     */     {
/*  29 */       List<Vec3d> bottomLayer = layers[0];
/*  30 */       List<Vec3d> topLayer = layers[(layers.length - 1)];
/*  31 */       Vec3d bottomMid = VecHelper.getMidList(bottomLayer);
/*  32 */       Vec3d topMid = VecHelper.getMidList(topLayer);
/*  33 */       if (cap != CapType.BOTTOM)
/*     */       {
/*  35 */         this.topCap = new CMPolygon((Vec3d[])topLayer.toArray(new Vec3d[0]), null, topMid.func_178788_d(bottomMid).func_72432_b());
/*  36 */         addChild(this.topCap);
/*     */       }
/*  38 */       if (cap != CapType.TOP)
/*     */       {
/*     */ 
/*  41 */         this.bottomCap = new CMPolygon(VecHelper.reverse((Vec3d[])bottomLayer.toArray(new Vec3d[0])), null, bottomMid.func_178788_d(topMid).func_72432_b());
/*  42 */         addChild(this.bottomCap);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CMTriangleMountain(CapType cap, Vec3d[]... layers)
/*     */   {
/*  49 */     this(cap, toListList(layers));
/*     */   }
/*     */   
/*     */   public static List<Vec3d>[] toListList(Vec3d[]... layers)
/*     */   {
/*  54 */     List<Vec3d>[] listList = new List[layers.length];
/*  55 */     for (int a = 0; a < layers.length; a++)
/*     */     {
/*  57 */       List<Vec3d> thisList = Arrays.asList(layers[a]);
/*  58 */       listList[a] = thisList;
/*     */     }
/*  60 */     return listList;
/*     */   }
/*     */   
/*     */   public List<CMTriangle> makeTriangles(List<Vec3d> moreLayer, List<Vec3d> lessLayer)
/*     */   {
/*  65 */     List<CMTriangle> triangles = new ArrayList();
/*  66 */     Vec3d innerPoint = VecHelper.getMidList(moreLayer);
/*  67 */     LinkedHashMap<Integer, List<Integer>> moreToLess = new LinkedHashMap();
/*  68 */     LinkedHashMap<Integer, List<Integer>> lessToMore = new LinkedHashMap();
/*     */     
/*     */ 
/*  71 */     for (int a = 0; a < lessLayer.size(); a++)
/*     */     {
/*     */ 
/*  74 */       Vec3d v1InLess = (Vec3d)lessLayer.get(a);
/*     */       
/*  76 */       Vec3d closestMore = VecHelper.getClosest(v1InLess, (Vec3d[])moreLayer.toArray(new Vec3d[0]));
/*  77 */       Integer closestMoreIndex = Integer.valueOf(moreLayer.indexOf(closestMore));
/*     */       
/*     */ 
/*  80 */       List<Integer> closestToLessv1 = !moreToLess.containsKey(closestMoreIndex) ? new ArrayList() : (List)moreToLess.get(closestMoreIndex);
/*  81 */       closestToLessv1.add(Integer.valueOf(a));
/*  82 */       moreToLess.put(closestMoreIndex, closestToLessv1);
/*     */       
/*  84 */       List<Integer> lessv1ToMore = !lessToMore.containsKey(Integer.valueOf(a)) ? new ArrayList() : (List)lessToMore.get(Integer.valueOf(a));
/*  85 */       lessv1ToMore.add(closestMoreIndex);
/*  86 */       lessToMore.put(Integer.valueOf(a), lessv1ToMore);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  91 */     List<Integer> firstMoresConnectedToLess = new ArrayList(moreToLess.keySet());
/*  92 */     List<Integer> firstLessConnectedToMore = new ArrayList(lessToMore.keySet());
/*  93 */     for (int a = 0; a < firstMoresConnectedToLess.size(); a++)
/*     */     {
/*  95 */       Integer thisMoreHasConnection = (Integer)firstMoresConnectedToLess.get(a);
/*  96 */       List<Integer> thisMoreConnections = (List)moreToLess.get(thisMoreHasConnection);
/*  97 */       Integer nextMoreHasConnection = (Integer)firstMoresConnectedToLess.get((a + 1) % firstMoresConnectedToLess.size());
/*  98 */       List<Integer> nextMoreConnections = (List)moreToLess.get(nextMoreHasConnection);
/*     */       
/*     */ 
/*     */ 
/* 102 */       if ((nextMoreHasConnection.intValue() - thisMoreHasConnection.intValue() <= 1) || 
/* 103 */         (thisMoreHasConnection.intValue() - nextMoreHasConnection.intValue() == moreLayer.size() - 1))
/*     */       {
/* 105 */         boolean adjacentMoreSameLess = false;
/* 106 */         for (Integer connectionOfThisMore : thisMoreConnections)
/*     */         {
/* 108 */           if (nextMoreConnections.contains(connectionOfThisMore))
/*     */           {
/* 110 */             adjacentMoreSameLess = true;
/* 111 */             break;
/*     */           }
/*     */         }
/* 114 */         if (adjacentMoreSameLess) {}
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 119 */         Integer lessConnectedToThisMore = null;
/* 120 */         Integer lessConnectedToNextMore = null;
/* 121 */         for (int b = 0; 
/* 122 */             b < firstLessConnectedToMore.size(); b++)
/*     */         {
/* 124 */           Integer thisLessHasConnection = (Integer)firstLessConnectedToMore.get(b);
/* 125 */           List<Integer> thisLessConnections = (List)lessToMore.get(thisLessHasConnection);
/* 126 */           Integer nextLessHasConnection = (Integer)firstLessConnectedToMore.get((b + 1) % firstLessConnectedToMore.size());
/* 127 */           List<Integer> nextLessConnections = (List)lessToMore.get(nextLessHasConnection);
/* 128 */           if ((thisLessConnections.contains(thisMoreHasConnection)) && 
/* 129 */             (nextLessConnections.contains(nextMoreHasConnection)))
/*     */           {
/* 131 */             lessConnectedToThisMore = thisLessHasConnection;
/* 132 */             lessConnectedToNextMore = nextLessHasConnection;
/* 133 */             break;
/*     */           }
/*     */         }
/* 136 */         if (lessConnectedToThisMore != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */           int amidUnlocalMax = nextMoreHasConnection.intValue() > thisMoreHasConnection.intValue() ? nextMoreHasConnection.intValue() : moreLayer.size() + nextMoreHasConnection.intValue();
/* 143 */           int amidMaxIndex = amidUnlocalMax - thisMoreHasConnection.intValue();
/* 144 */           int amidIndex = 0;
/* 145 */           for (int amidMoreUnlocal = thisMoreHasConnection.intValue(); amidMoreUnlocal <= amidUnlocalMax; amidMoreUnlocal++)
/*     */           {
/*     */ 
/* 148 */             int amidMore = amidMoreUnlocal >= moreLayer.size() ? amidMoreUnlocal % moreLayer.size() : amidMoreUnlocal;
/*     */             
/*     */ 
/*     */ 
/* 152 */             if ((amidIndex > 0) && (amidIndex < amidMaxIndex))
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */               List<Integer> newSet = new ArrayList();
/* 160 */               if (amidIndex <= amidMaxIndex / 2)
/*     */               {
/* 162 */                 newSet.add(lessConnectedToThisMore);
/* 163 */                 List<Integer> theList = (List)lessToMore.get(lessConnectedToThisMore);
/* 164 */                 theList.add(Integer.valueOf(amidMore));
/*     */               }
/* 166 */               if (amidIndex >= amidMaxIndex / 2)
/*     */               {
/* 168 */                 newSet.add(lessConnectedToNextMore);
/* 169 */                 List<Integer> theList = (List)lessToMore.get(lessConnectedToNextMore);
/* 170 */                 theList.add(Integer.valueOf(amidMore));
/*     */               }
/* 172 */               moreToLess.put(Integer.valueOf(amidMore), newSet);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/* 177 */             if ((amidIndex * 2 - 1 == amidMaxIndex) && (amidMaxIndex % 2 == 1))
/*     */             {
/* 179 */               List<Integer> theSet = moreToLess.containsKey(Integer.valueOf(amidMore)) ? (List)moreToLess.get(Integer.valueOf(amidMore)) : new ArrayList();
/*     */               
/* 181 */               theSet.add(lessConnectedToThisMore);
/* 182 */               moreToLess.put(Integer.valueOf(amidMore), theSet);
/* 183 */               List<Integer> theList = (List)lessToMore.get(lessConnectedToThisMore);
/* 184 */               theList.add(Integer.valueOf(amidMore));
/*     */             }
/*     */             
/* 187 */             amidIndex++;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 192 */     for (Map.Entry<Integer, List<Integer>> moreToLessEntry : moreToLess.entrySet())
/*     */     {
/* 194 */       Integer moreIndex = (Integer)moreToLessEntry.getKey();
/* 195 */       Vec3d moreVertex = (Vec3d)moreLayer.get(moreIndex.intValue());
/* 196 */       List<Integer> lessList = (List)moreToLessEntry.getValue();
/* 197 */       for (int a = 0; a < lessList.size() - 1; a++)
/*     */       {
/* 199 */         Vec3d lessVertex1 = (Vec3d)lessLayer.get(((Integer)lessList.get(a)).intValue());
/* 200 */         Vec3d lessVertex2 = (Vec3d)lessLayer.get(((Integer)lessList.get(a + 1)).intValue());
/* 201 */         CMTriangle triangle = new CMTriangle(moreVertex, lessVertex1, lessVertex2, innerPoint);
/* 202 */         triangles.add(triangle);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 207 */     for (Map.Entry<Integer, List<Integer>> lessToMoreEntry : lessToMore.entrySet())
/*     */     {
/* 209 */       Integer lessIndex = (Integer)lessToMoreEntry.getKey();
/* 210 */       Vec3d lessVertex = (Vec3d)lessLayer.get(lessIndex.intValue());
/* 211 */       List<Integer> moreList = (List)lessToMoreEntry.getValue();
/* 212 */       for (int a = 0; a < moreList.size() - 1; a++)
/*     */       {
/* 214 */         Vec3d moreVertex1 = (Vec3d)moreLayer.get(((Integer)moreList.get(a)).intValue());
/* 215 */         Vec3d moreVertex2 = (Vec3d)moreLayer.get(((Integer)moreList.get(a + 1)).intValue());
/* 216 */         CMTriangle triangle = new CMTriangle(lessVertex, moreVertex1, moreVertex2, innerPoint);
/* 217 */         triangles.add(triangle);
/*     */       }
/*     */     }
/* 220 */     return triangles;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPaletteIndexColor(String s, Color c, boolean setFull)
/*     */   {
/* 226 */     List<String> pIndexes = getPaletteIndexes();
/* 227 */     for (int a = 0; a < pIndexes.size(); a++)
/*     */     {
/* 229 */       String pNow = (String)pIndexes.get(a);
/* 230 */       if (pNow.equals(s))
/*     */       {
/* 232 */         if ((a == 0) && (this.bottomCap != null)) this.bottomCap.setColor(c);
/* 233 */         Iterator localIterator; if (a < this.trianglesLayers.size())
/*     */         {
/* 235 */           List<CMTriangle> triangles = (List)this.trianglesLayers.get(a);
/* 236 */           for (localIterator = triangles.iterator(); localIterator.hasNext();) { triangle = (CMTriangle)localIterator.next();
/*     */             
/* 238 */             triangle.setColor(c);
/*     */           } }
/*     */         CMTriangle triangle;
/* 241 */         if (a == pIndexes.size() - 1)
/*     */         {
/* 243 */           if (this.topCap != null) this.topCap.setColor(c);
/* 244 */           for (int b = a + 1; b < this.trianglesLayers.size(); b++)
/*     */           {
/* 246 */             Object triangles = (List)this.trianglesLayers.get(b);
/* 247 */             for (CMTriangle triangle : (List)triangles)
/*     */             {
/* 249 */               triangle.setColor(c);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/* 260 */     super.render();
/* 261 */     for (int a = 0; a < this.trianglesLayers.size(); a++)
/*     */     {
/* 263 */       List<CMTriangle> triangles = (List)this.trianglesLayers.get(a);
/* 264 */       for (CMTriangle triangle : triangles)
/*     */       {
/* 266 */         triangle.fullRender();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMTriangleMountain.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */