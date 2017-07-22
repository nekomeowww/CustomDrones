/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMPipe
/*     */   extends CMBase
/*     */ {
/*  12 */   public static boolean NEWRENDERMODE = true;
/*     */   public Vec3d end1;
/*     */   public Vec3d end2;
/*     */   public Vec3d axis1;
/*     */   public Vec3d axis2;
/*     */   public double ra1;
/*     */   public double ra2;
/*  19 */   public int sides; public double initSpin1; public double initSpin2; public CapType renderCaps = CapType.BOTH;
/*  20 */   public boolean textureHasCaps = true;
/*     */   
/*  22 */   public List<CMVertex> end1Vers = new ArrayList();
/*  23 */   public List<CMVertex> end2Vers = new ArrayList();
/*  24 */   public List<CMVertex> sideVers = new ArrayList();
/*     */   
/*     */   public CMPipe(Vec3d e1, Vec3d e2, double radius, int sides)
/*     */   {
/*  28 */     this(e1, e2, e1.func_178788_d(e2), e1.func_178788_d(e2), radius, radius, sides);
/*     */   }
/*     */   
/*     */   public CMPipe(Vec3d e1, Vec3d e2, double radius1, double radius2, int sides)
/*     */   {
/*  33 */     this(e1, e2, e1.func_178788_d(e2), e1.func_178788_d(e2), radius1, radius2, sides);
/*     */   }
/*     */   
/*     */   public CMPipe(Vec3d e1, Vec3d e2, Vec3d tiltEnds, double radius1, double radius2, int sides)
/*     */   {
/*  38 */     this(e1, e2, tiltEnds, tiltEnds, radius1, radius2, sides);
/*     */   }
/*     */   
/*     */   public CMPipe(Vec3d e1, Vec3d e2, Vec3d p1, Vec3d p2, double end1r, double end2r, int s)
/*     */   {
/*  43 */     this.end1 = e1;
/*  44 */     this.end2 = e2;
/*  45 */     Vec3d end12 = fromTo(this.end1, this.end2);
/*  46 */     this.axis1 = p1;
/*  47 */     this.axis2 = p2;
/*  48 */     if (this.axis1.func_72430_b(end12) > 0.0D) this.axis1 = scale(this.axis1, -1.0D);
/*  49 */     if (this.axis2.func_72430_b(end12) > 0.0D) this.axis2 = scale(this.axis2, -1.0D);
/*  50 */     this.ra1 = end1r;
/*  51 */     this.ra2 = end2r;
/*  52 */     this.sides = s;
/*     */   }
/*     */   
/*     */   public CMPipe setInitSpin(double d)
/*     */   {
/*  57 */     this.initSpin1 = (this.initSpin2 = d);
/*  58 */     return this;
/*     */   }
/*     */   
/*     */   public CMPipe setInitSpin(double d1, double d2)
/*     */   {
/*  63 */     this.initSpin1 = d1;
/*  64 */     this.initSpin2 = d2;
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public CMPipe setRenderCaps(CapType type)
/*     */   {
/*  70 */     this.renderCaps = type;
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public CMPipe autoUVfromU(double u1, double v1, double u2)
/*     */   {
/*  76 */     Vec3d end12 = fromTo(this.end1, this.end2);
/*  77 */     double angleRad = 6.283185307179586D / this.sides;
/*  78 */     double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
/*  79 */     double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
/*  80 */     double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
/*  81 */     double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();
/*     */     
/*  83 */     double texHeight = totalHeight / totalWidth * (u2 - u1);
/*  84 */     double v2 = v1 + texHeight;
/*  85 */     setTextureUV(new TextureUV(u1, v1, u2, v2));
/*  86 */     return this;
/*     */   }
/*     */   
/*     */   public CMPipe autoUVfromV(double u1, double v1, double v2)
/*     */   {
/*  91 */     Vec3d end12 = fromTo(this.end1, this.end2);
/*  92 */     double angleRad = 6.283185307179586D / this.sides;
/*  93 */     double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
/*  94 */     double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
/*  95 */     double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
/*  96 */     double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();
/*     */     
/*  98 */     double texWidth = totalWidth / totalHeight * (v2 - v1);
/*  99 */     double u2 = u1 + texWidth;
/* 100 */     setTextureUV(new TextureUV(u1, v1, u2, v2));
/* 101 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public void render()
/*     */   {
/* 107 */     NEWRENDERMODE = true;
/* 108 */     if (NEWRENDERMODE)
/*     */     {
/* 110 */       newRender();
/*     */     }
/*     */     else
/*     */     {
/* 114 */       this.end1Vers.clear();
/* 115 */       this.end2Vers.clear();
/* 116 */       this.sideVers.clear();
/* 117 */       oldRender();
/*     */     }
/*     */   }
/*     */   
/*     */   public void oldRender()
/*     */   {
/* 123 */     Vec3d end12 = fromTo(this.end1, this.end2);
/* 124 */     Vec3d mid12 = scale(this.end1.func_178787_e(this.end2), 0.5D);
/* 125 */     double angleRad = 6.283185307179586D / this.sides;
/* 126 */     double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
/* 127 */     double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
/* 128 */     double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
/* 129 */     double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();
/*     */     
/* 131 */     double v1 = this.textureUV.v1;
/* 132 */     double v4 = this.textureUV.v2;
/* 133 */     double v2 = v1 + Math.max(this.ra1, this.ra2) * 2.0D / totalHeight * (v4 - v1);
/* 134 */     if (!this.textureHasCaps)
/*     */     {
/* 136 */       v2 = v1;
/* 137 */       totalHeight = end12.func_72433_c();
/*     */     }
/* 139 */     double u1 = this.textureUV.u1;
/* 140 */     double uend = this.textureUV.u2;
/* 141 */     double ustep = (sideW1 + sideW2) / 2.0D / totalWidth * (uend - u1);
/* 142 */     Vec3d spin1 = getPerpendicularVec(this.axis1, end12);
/* 143 */     Vec3d spin2 = getPerpendicularVec(this.axis2, end12);
/* 144 */     if (spin1.func_72430_b(spin2) < 0.0D)
/*     */     {
/* 146 */       spin2 = scale(spin2, -1.0D);
/*     */     }
/* 148 */     if (this.renderCaps != CapType.BOTH) { enableCull(false);
/*     */     }
/* 150 */     begin(8);
/* 151 */     for (int a = 0; a < this.sides + 1; a++)
/*     */     {
/* 153 */       double unow = u1 + ustep * a;
/* 154 */       Vec3d final2 = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
/* 155 */       Vec3d final2ToLater = fromTo(final2, 
/* 156 */         setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * (a + 1)), this.ra2).func_178787_e(this.end2));
/* 157 */       Vec3d final1 = setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
/* 158 */       Vec3d normal = vec(0.0D, 1.0D, 0.0D);
/* 159 */       if (final2ToLater.func_72433_c() == 0.0D)
/*     */       {
/* 161 */         Vec3d final1ToLater = fromTo(final1, 
/* 162 */           setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * (a + 1)), this.ra1)
/* 163 */           .func_178787_e(this.end1));
/* 164 */         normal = getPerpendicularVec(final1ToLater, fromTo(final1, final2)).func_72432_b();
/*     */       }
/*     */       else
/*     */       {
/* 168 */         normal = getPerpendicularVec(fromTo(final2, final1), final2ToLater).func_72432_b();
/*     */       }
/* 170 */       normal(normal.field_72450_a, normal.field_72448_b, normal.field_72449_c);
/* 171 */       vertex(final2.field_72450_a, final2.field_72448_b, final2.field_72449_c, unow, v2);
/* 172 */       vertex(final1.field_72450_a, final1.field_72448_b, final1.field_72449_c, unow, v4);
/*     */     }
/* 174 */     end();
/* 175 */     if (this.renderCaps != CapType.NONE)
/*     */     {
/* 177 */       Vec3d texMid1 = vec(u1 + (uend - u1) / 4.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
/* 178 */       Vec3d texMid2 = vec(u1 + (uend - u1) / 4.0D * 3.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
/* 179 */       Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
/* 180 */       Vec3d texSpin = vec(0.0D, (v2 - v1) / 2.0D, 0.0D);
/* 181 */       if (this.renderCaps != CapType.TOP)
/*     */       {
/* 183 */         begin(9);
/* 184 */         normal(this.axis1.field_72450_a, this.axis1.field_72448_b, this.axis1.field_72449_c);
/* 185 */         for (int a = 0; a < this.sides; a++)
/*     */         {
/* 187 */           Vec3d botTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);
/*     */           
/* 189 */           botTex = vec(botTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), botTex.field_72448_b, botTex.field_72449_c).func_178787_e(texMid1);
/*     */           
/*     */ 
/* 192 */           Vec3d botVertex = setLength(rotateAround(spin1, this.axis1, this.initSpin1 - 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
/* 193 */           vertex(botVertex.field_72450_a, botVertex.field_72448_b, botVertex.field_72449_c, botTex.field_72450_a, botTex.field_72448_b);
/*     */         }
/* 195 */         end();
/*     */       }
/* 197 */       if (this.renderCaps != CapType.BOTTOM)
/*     */       {
/* 199 */         texSpin = vec(0.0D, (v1 - v2) / 2.0D, 0.0D);
/* 200 */         begin(9);
/* 201 */         normal(this.axis2.field_72450_a, this.axis2.field_72448_b, this.axis2.field_72449_c);
/* 202 */         for (int a = 0; a < this.sides; a++)
/*     */         {
/* 204 */           Vec3d topTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);
/*     */           
/* 206 */           topTex = vec(topTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), topTex.field_72448_b, topTex.field_72449_c).func_178787_e(texMid2);
/*     */           
/* 208 */           Vec3d topVertex = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
/* 209 */           vertex(topVertex.field_72450_a, topVertex.field_72448_b, topVertex.field_72449_c, topTex.field_72450_a, topTex.field_72448_b);
/*     */         }
/* 211 */         end();
/*     */       }
/*     */     }
/* 214 */     if (this.renderCaps != CapType.BOTH) enableCull(true);
/*     */   }
/*     */   
/*     */   public void newRender()
/*     */   {
/* 219 */     if ((this.end1Vers.isEmpty()) || (this.end2Vers.isEmpty()) || (this.sideVers.isEmpty()))
/*     */     {
/* 221 */       this.sideVers.clear();
/* 222 */       this.end1Vers.clear();
/* 223 */       this.end2Vers.clear();
/* 224 */       Vec3d end12 = fromTo(this.end1, this.end2);
/* 225 */       Vec3d mid12 = scale(this.end1.func_178787_e(this.end2), 0.5D);
/* 226 */       double angleRad = 6.283185307179586D / this.sides;
/* 227 */       double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
/* 228 */       double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
/* 229 */       double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
/* 230 */       double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();
/* 231 */       double v1 = this.textureUV.v1;
/* 232 */       double v4 = this.textureUV.v2;
/* 233 */       double v2 = v1 + Math.max(this.ra1, this.ra2) * 2.0D / totalHeight * (v4 - v1);
/* 234 */       if (!this.textureHasCaps)
/*     */       {
/* 236 */         v2 = v1;
/* 237 */         totalHeight = end12.func_72433_c();
/*     */       }
/* 239 */       double u1 = this.textureUV.u1;
/* 240 */       double uend = this.textureUV.u2;
/* 241 */       double ustep = (sideW1 + sideW2) / 2.0D / totalWidth * (uend - u1);
/* 242 */       Vec3d spin1 = getPerpendicularVec(this.axis1, end12);
/* 243 */       Vec3d spin2 = getPerpendicularVec(this.axis2, end12);
/* 244 */       if (spin1.func_72430_b(spin2) < 0.0D)
/*     */       {
/* 246 */         spin2 = scale(spin2, -1.0D);
/*     */       }
/* 248 */       for (int a = 0; a < this.sides + 1; a++)
/*     */       {
/* 250 */         double unow = u1 + ustep * a;
/*     */         
/* 252 */         Vec3d final2 = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
/* 253 */         Vec3d final2ToLater = fromTo(final2, 
/* 254 */           setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * (a + 1)), this.ra2)
/* 255 */           .func_178787_e(this.end2));
/*     */         
/* 257 */         Vec3d final1 = setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
/* 258 */         Vec3d normal = vec(0.0D, 1.0D, 0.0D);
/* 259 */         if (final2ToLater.func_72433_c() == 0.0D)
/*     */         {
/* 261 */           Vec3d final1ToLater = fromTo(final1, 
/* 262 */             setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * (a + 1)), this.ra1)
/* 263 */             .func_178787_e(this.end1));
/* 264 */           normal = getPerpendicularVec(final1ToLater, fromTo(final1, final2)).func_72432_b();
/*     */         }
/*     */         else
/*     */         {
/* 268 */           normal = getPerpendicularVec(fromTo(final2, final1), final2ToLater).func_72432_b();
/*     */         }
/* 270 */         this.sideVers.add(new CMVertex(final2, vec(unow, v2, 0.0D), normal));
/* 271 */         this.sideVers.add(new CMVertex(final1, vec(unow, v4, 0.0D), normal));
/*     */       }
/* 273 */       Vec3d texMid1 = vec(u1 + (uend - u1) / 4.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
/* 274 */       Vec3d texMid2 = vec(u1 + (uend - u1) / 4.0D * 3.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
/* 275 */       Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
/* 276 */       Vec3d texSpin = vec(0.0D, (v2 - v1) / 2.0D, 0.0D);
/* 277 */       boolean flipNormal1 = this.axis1.func_72430_b(end12) > 0.0D;
/* 278 */       boolean flipNormal2 = this.axis2.func_72430_b(end12) < 0.0D;
/* 279 */       for (int a = 0; a < this.sides; a++)
/*     */       {
/* 281 */         Vec3d botTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);
/*     */         
/* 283 */         botTex = vec(botTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), botTex.field_72448_b, botTex.field_72449_c).func_178787_e(texMid1);
/*     */         
/* 285 */         Vec3d botVertex = setLength(rotateAround(spin1, this.axis1, this.initSpin1 - 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
/* 286 */         this.end1Vers.add(new CMVertex(botVertex, botTex, scale(this.axis1, flipNormal1 ? -1.0D : 1.0D)));
/*     */       }
/* 288 */       texSpin = vec(0.0D, (v1 - v2) / 2.0D, 0.0D);
/* 289 */       for (int a = 0; a < this.sides; a++)
/*     */       {
/* 291 */         Vec3d topTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);
/*     */         
/* 293 */         topTex = vec(topTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), topTex.field_72448_b, topTex.field_72449_c).func_178787_e(texMid2);
/*     */         
/* 295 */         Vec3d topVertex = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
/* 296 */         this.end2Vers.add(new CMVertex(topVertex, topTex, scale(this.axis2, flipNormal2 ? -1.0D : 1.0D)));
/*     */       }
/*     */     }
/*     */     
/* 300 */     if (this.renderCaps != CapType.BOTH) enableCull(false);
/* 301 */     if (!this.sideVers.isEmpty())
/*     */     {
/* 303 */       begin(8);
/* 304 */       for (int a = 0; a < this.sideVers.size(); a++)
/*     */       {
/* 306 */         CMVertex vert = (CMVertex)this.sideVers.get(a);
/* 307 */         vert.addToDrawing();
/*     */       }
/* 309 */       end();
/*     */     }
/* 311 */     if (this.renderCaps != CapType.NONE)
/*     */     {
/* 313 */       if ((!this.end1Vers.isEmpty()) && (this.renderCaps != CapType.TOP))
/*     */       {
/* 315 */         begin(9);
/* 316 */         for (int a = 0; a < this.end1Vers.size(); a++)
/*     */         {
/* 318 */           CMVertex vert = (CMVertex)this.end1Vers.get(a);
/* 319 */           vert.addToDrawing();
/*     */         }
/* 321 */         end();
/*     */       }
/* 323 */       if ((!this.end2Vers.isEmpty()) && (this.renderCaps != CapType.BOTTOM))
/*     */       {
/* 325 */         begin(9);
/* 326 */         for (int a = 0; a < this.end2Vers.size(); a++)
/*     */         {
/* 328 */           CMVertex vert = (CMVertex)this.end2Vers.get(a);
/* 329 */           vert.addToDrawing();
/*     */         }
/* 331 */         end();
/*     */       }
/*     */     }
/* 334 */     if (this.renderCaps != CapType.BOTH) enableCull(true);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMPipe.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */