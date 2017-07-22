/*     */ package williamle.drones.api.path;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ 
/*     */ 
/*     */ public class Path
/*     */ {
/*  11 */   public List<Node> nodes = new ArrayList();
/*     */   
/*     */ 
/*     */   public int pathIndex;
/*     */   
/*     */ 
/*     */ 
/*     */   public Path simplifiedPath()
/*     */   {
/*  20 */     Path p = new Path();
/*  21 */     if (!this.nodes.isEmpty())
/*     */     {
/*  23 */       p.addNode((Node)this.nodes.get(0));
/*  24 */       for (int a = 1; a < this.nodes.size(); a++)
/*     */       {
/*  26 */         Node prevNode = (Node)this.nodes.get(a - 1);
/*  27 */         Node nowNode = (Node)this.nodes.get(a);
/*  28 */         if (a == this.nodes.size() - 1)
/*     */         {
/*  30 */           p.addNode(nowNode);
/*  31 */           break;
/*     */         }
/*  33 */         Vec3d travelled = nowNode.toVec().func_178788_d(prevNode.toVec());
/*  34 */         double travelledLength = travelled.func_72433_c();
/*  35 */         for (int b = a + 1; b < this.nodes.size(); b++)
/*     */         {
/*  37 */           Node comingNode = (Node)this.nodes.get(b);
/*  38 */           Vec3d toTravel = comingNode.toVec().func_178788_d(nowNode.toVec());
/*  39 */           double angleBetween = VecHelper.getAngleBetween(travelled, toTravel);
/*  40 */           if ((b == this.nodes.size() - 1) || (angleBetween > 0.17453292519943295D) || 
/*  41 */             (toTravel.func_72433_c() - travelledLength * (b - a) > 8.0E-4D))
/*     */           {
/*  43 */             a = b;
/*  44 */             p.addNode(comingNode);
/*  45 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  50 */     return p;
/*     */   }
/*     */   
/*     */   public void clearPath()
/*     */   {
/*  55 */     this.nodes.clear();
/*  56 */     resetPathIndex();
/*     */   }
/*     */   
/*     */   public void resetPathIndex()
/*     */   {
/*  61 */     this.pathIndex = 0;
/*     */   }
/*     */   
/*     */   public boolean hasPath()
/*     */   {
/*  66 */     return !this.nodes.isEmpty();
/*     */   }
/*     */   
/*     */   public Node goToNextNode()
/*     */   {
/*  71 */     if (this.pathIndex < this.nodes.size() - 1)
/*     */     {
/*  73 */       return (Node)this.nodes.get(this.pathIndex++);
/*     */     }
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   public Node getNextNode()
/*     */   {
/*  80 */     if (this.pathIndex < this.nodes.size() - 1)
/*     */     {
/*  82 */       return (Node)this.nodes.get(this.pathIndex + 1);
/*     */     }
/*  84 */     return null;
/*     */   }
/*     */   
/*     */   public Node getCurrentNode()
/*     */   {
/*  89 */     if (this.pathIndex < this.nodes.size())
/*     */     {
/*  91 */       return (Node)this.nodes.get(this.pathIndex);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */   
/*     */   public Node goToPrevNode()
/*     */   {
/*  98 */     if (this.pathIndex > 0)
/*     */     {
/* 100 */       return (Node)this.nodes.get(this.pathIndex--);
/*     */     }
/* 102 */     return null;
/*     */   }
/*     */   
/*     */   public Node getPrevNode()
/*     */   {
/* 107 */     if (this.pathIndex > 0)
/*     */     {
/* 109 */       return (Node)this.nodes.get(this.pathIndex - 1);
/*     */     }
/* 111 */     return null;
/*     */   }
/*     */   
/*     */   public Path addNode(Node p)
/*     */   {
/* 116 */     this.nodes.add(p);
/* 117 */     return this;
/*     */   }
/*     */   
/*     */   public Path addNodes(Node... pathNodes)
/*     */   {
/* 122 */     for (Node p : pathNodes)
/*     */     {
/* 124 */       addNode(p);
/*     */     }
/* 126 */     return this;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\path\Path.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */