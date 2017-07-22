/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ public class CMPipeLine extends CMBase
/*     */ {
/*     */   public int sides;
/*     */   public double[] radi;
/*     */   public Vec3d[] positions;
/*  11 */   public Vec3d[] endCapsDirection = new Vec3d[2];
/*     */   
/*     */   public CMPipe[] pipes;
/*     */   
/*     */ 
/*     */   public CMPipeLine() {}
/*     */   
/*     */   public CMPipeLine(int s, double radius, Vec3d[] vecs, Vec3d[] endCapsDir)
/*     */   {
/*  20 */     this(s, filledDouble(radius, vecs.length), vecs, endCapsDir);
/*     */   }
/*     */   
/*     */   public CMPipeLine(int s, double[] radius, Vec3d[] vecs, Vec3d[] endCapsDir)
/*     */   {
/*  25 */     this.sides = s;
/*  26 */     this.radi = radius;
/*  27 */     this.positions = vecs;
/*  28 */     if (endCapsDir == null)
/*     */     {
/*  30 */       this.endCapsDirection[0] = fromTo(this.positions[1], this.positions[0]);
/*  31 */       this.endCapsDirection[1] = fromTo(this.positions[(this.positions.length - 1)], this.positions[(this.positions.length - 2)]);
/*     */     }
/*     */     else
/*     */     {
/*  35 */       this.endCapsDirection = endCapsDir;
/*     */     }
/*  37 */     this.pipes = calculatePipes();
/*     */   }
/*     */   
/*     */   public CMPipe[] calculatePipes()
/*     */   {
/*  42 */     CMPipe[] pip = new CMPipe[this.positions.length - 1];
/*  43 */     for (int a = 0; a < this.positions.length - 1; a++)
/*     */     {
/*  45 */       Vec3d thisVec = this.positions[a];
/*  46 */       Vec3d nextVec = this.positions[(a + 1)];
/*  47 */       Vec3d thisPerp = this.endCapsDirection[0];
/*  48 */       Vec3d nextPerp = this.endCapsDirection[1];
/*  49 */       if (a > 0)
/*     */       {
/*  51 */         Vec3d prevVec = this.positions[(a - 1)];
/*  52 */         Vec3d prevToThis = fromTo(prevVec, thisVec).func_72432_b();
/*  53 */         Vec3d nextToThis = fromTo(nextVec, thisVec).func_72432_b();
/*  54 */         Vec3d paraToPlane = prevToThis.func_178787_e(nextToThis);
/*  55 */         Vec3d perpHelper = getPerpendicularVec(prevToThis, nextToThis);
/*  56 */         thisPerp = getPerpendicularVec(paraToPlane, perpHelper);
/*     */       }
/*  58 */       if (a < this.positions.length - 2)
/*     */       {
/*  60 */         Vec3d nextNextVec = this.positions[(a + 2)];
/*  61 */         Vec3d thisToNext = fromTo(thisVec, nextVec).func_72432_b();
/*  62 */         Vec3d next2ToNext = fromTo(nextNextVec, nextVec).func_72432_b();
/*  63 */         Vec3d paraToPlane = thisToNext.func_178787_e(next2ToNext);
/*  64 */         Vec3d perpHelper = getPerpendicularVec(thisToNext, next2ToNext);
/*  65 */         nextPerp = getPerpendicularVec(paraToPlane, perpHelper);
/*     */       }
/*  67 */       CMPipe pipe = new CMPipe(thisVec, nextVec, thisPerp, nextPerp, this.radi[a], this.radi[(a + 1)], this.sides);
/*  68 */       pipe.renderCaps = renderCaps(pipe, a);
/*  69 */       pip[a] = pipe;
/*     */     }
/*  71 */     return pip;
/*     */   }
/*     */   
/*     */   public CapType renderCaps(CMPipe pipe, int pipeIndex)
/*     */   {
/*  76 */     if (pipeIndex == 0) return CapType.BOTTOM;
/*  77 */     if (pipeIndex == this.positions.length - 1) return CapType.TOP;
/*  78 */     return CapType.NONE;
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/*  84 */     for (CMPipe pipe : this.pipes)
/*     */     {
/*  86 */       pipe.render();
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPipesColor(Color color)
/*     */   {
/*  92 */     for (CMPipe pipe : this.pipes)
/*     */     {
/*  94 */       pipe.color = color;
/*  95 */       pipe.setUseTexture(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPipesColors(Color[] colors)
/*     */   {
/* 101 */     for (int a = 0; a < this.pipes.length; a++)
/*     */     {
/* 103 */       this.pipes[a].color = colors[a];
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPipesTexture(ResourceLocation res)
/*     */   {
/* 109 */     for (CMPipe pipe : this.pipes)
/*     */     {
/* 111 */       pipe.texture = res;
/* 112 */       pipe.setTextureUV(new TextureUV());
/* 113 */       if (res != null) pipe.setUseTexture(true);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setPipesTextureUV(TextureUV uv)
/*     */   {
/* 119 */     for (CMPipe pipe : this.pipes)
/*     */     {
/* 121 */       pipe.textureUV = uv;
/*     */     }
/*     */   }
/*     */   
/*     */   public static double[] filledDouble(double item, int count)
/*     */   {
/* 127 */     double[] array = new double[count];
/* 128 */     for (int a = 0; a < array.length; a++)
/*     */     {
/* 130 */       array[a] = item;
/*     */     }
/* 132 */     return array;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMPipeLine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */