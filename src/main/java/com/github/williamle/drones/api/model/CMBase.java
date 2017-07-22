/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CMBase
/*     */   extends VecHelper
/*     */ {
/*  21 */   public static Minecraft mc = ;
/*  22 */   public static boolean USEGLSM = true;
/*  23 */   public static boolean USECOLORSPRITE = true;
/*     */   public static final int COLORWIDTH = 24;
/*  25 */   public static final ResourceLocation COLORSPITE = new ResourceLocation("drones", "textures/colors.png");
/*  26 */   public static final Vec3d vec0 = vec(0.0D, 0.0D, 0.0D);
/*     */   
/*  28 */   public UniqueName cmName = new UniqueName(String.valueOf(hashCode()));
/*  29 */   public Color color = new Color(-1L);
/*  30 */   private List<String> paletteIndexes = new ArrayList();
/*  31 */   private boolean useTexture = false;
/*     */   public ResourceLocation texture;
/*  33 */   public TextureUV textureUV = new TextureUV();
/*  34 */   public Set<CMBase> childModels = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  39 */   public int visible = 3;
/*     */   
/*  41 */   public Vec3d localScale = vec(1.0D, 1.0D, 1.0D);
/*  42 */   public Vec3d localTranslate = vec(0.0D, 0.0D, 0.0D);
/*  43 */   public Vec3d localCenter = vec(0.0D, 0.0D, 0.0D);
/*  44 */   public List<RotateVec> centerRots = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CMBase setName(String s)
/*     */   {
/*  52 */     this.cmName = new UniqueName(s);
/*  53 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase addChild(CMBase cm)
/*     */   {
/*  58 */     this.childModels.add(cm);
/*  59 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setScale(double x, double y, double z)
/*     */   {
/*  64 */     this.localScale = vec(x, y, z);
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setTranslate(double x, double y, double z)
/*     */   {
/*  70 */     this.localTranslate = vec(x, y, z);
/*  71 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setCenter(double x, double y, double z)
/*     */   {
/*  76 */     this.localCenter = vec(x, y, z);
/*  77 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase resetRotation(double x, double y, double z, double a)
/*     */   {
/*  82 */     this.centerRots.clear();
/*  83 */     return setRotation(x, y, z, a);
/*     */   }
/*     */   
/*     */   public CMBase setRotation(double x, double y, double z, double a)
/*     */   {
/*  88 */     this.centerRots.add(new RotateVec(a, x, y, z));
/*  89 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setColor(Color c)
/*     */   {
/*  94 */     this.color = c;
/*  95 */     this.useTexture = false;
/*  96 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setColor(double r, double g, double b, double a)
/*     */   {
/* 101 */     this.color = new Color(r, g, b, a);
/* 102 */     this.useTexture = false;
/* 103 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setPaletteIndexes(String... s)
/*     */   {
/* 108 */     this.paletteIndexes.clear();
/* 109 */     for (int a = 0; a < s.length; a++)
/*     */     {
/* 111 */       String pal = s[a];
/* 112 */       addPaletteIndex(pal);
/*     */     }
/* 114 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase addPaletteIndex(String s)
/*     */   {
/* 119 */     if (s != null) this.paletteIndexes.add(s); else
/* 120 */       this.paletteIndexes.add("");
/* 121 */     return this;
/*     */   }
/*     */   
/*     */   public List<String> getPaletteIndexes()
/*     */   {
/* 126 */     return this.paletteIndexes;
/*     */   }
/*     */   
/*     */   public boolean hasPaletteIndex(String s)
/*     */   {
/* 131 */     return getPaletteIndexes().contains(s);
/*     */   }
/*     */   
/*     */   public void setPaletteIndexColor(String s, Color c, boolean setFull)
/*     */   {
/* 136 */     if (hasPaletteIndex(s))
/*     */     {
/* 138 */       if (setFull) setFullColor(c); else {
/* 139 */         setColor(c);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Color getPaletteIndexColor(String s) {
/* 145 */     if (hasPaletteIndex(s)) return this.color;
/* 146 */     return null;
/*     */   }
/*     */   
/*     */   public CMBase setFullColor(Color c)
/*     */   {
/* 151 */     setColor(c);
/* 152 */     for (CMBase cm : this.childModels)
/*     */     {
/* 154 */       cm.setFullColor(c);
/*     */     }
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setFullColor(double r, double g, double b, double a)
/*     */   {
/* 161 */     setColor(r, g, b, a);
/* 162 */     for (CMBase cm : this.childModels)
/*     */     {
/* 164 */       cm.setFullColor(r, g, b, a);
/*     */     }
/* 166 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setFullTexture(ResourceLocation text)
/*     */   {
/* 171 */     setTexture(text);
/* 172 */     for (CMBase cm : this.childModels)
/*     */     {
/* 174 */       cm.setFullTexture(text);
/*     */     }
/* 176 */     return this;
/*     */   }
/*     */   
/*     */   public void setUseTexture(boolean b)
/*     */   {
/* 181 */     this.useTexture = b;
/*     */   }
/*     */   
/*     */   public CMBase setTexture(ResourceLocation text)
/*     */   {
/* 186 */     this.texture = text;
/* 187 */     this.useTexture = (text != null);
/* 188 */     return this;
/*     */   }
/*     */   
/*     */   public CMBase setTextureUV(TextureUV uv)
/*     */   {
/* 193 */     this.textureUV = uv;
/* 194 */     return this;
/*     */   }
/*     */   
/*     */   public void fullRender()
/*     */   {
/* 199 */     push();
/* 200 */     resetColor();
/* 201 */     enableNormalize(true);
/* 202 */     enableBlend(true);
/* 203 */     blendFunc(770, 771);
/* 204 */     applyColorOrTexture(this.texture, this.color);
/* 205 */     translate(this.localTranslate.field_72450_a, this.localTranslate.field_72448_b, this.localTranslate.field_72449_c);
/* 206 */     scale(this.localScale.field_72450_a, this.localScale.field_72448_b, this.localScale.field_72449_c);
/* 207 */     if (!this.centerRots.isEmpty())
/*     */     {
/* 209 */       translate(this.localCenter.field_72450_a, this.localCenter.field_72448_b, this.localCenter.field_72449_c);
/* 210 */       for (int a = this.centerRots.size() - 1; a >= 0; a--)
/*     */       {
/* 212 */         RotateVec rot = (RotateVec)this.centerRots.get(a);
/* 213 */         rotate(rot.rotation, rot.vec.field_72450_a, rot.vec.field_72448_b, rot.vec.field_72449_c);
/*     */       }
/* 215 */       translate(-this.localCenter.field_72450_a, -this.localCenter.field_72448_b, -this.localCenter.field_72449_c);
/*     */     }
/* 217 */     if ((this.visible == 2) || (this.visible == 3)) render();
/* 218 */     if ((this.visible == 1) || (this.visible == 3)) renderChilds();
/* 219 */     enableNormalize(false);
/* 220 */     enableTexture(true);
/* 221 */     pop();
/*     */   }
/*     */   
/*     */ 
/*     */   public void render() {}
/*     */   
/*     */ 
/*     */   public void renderChilds()
/*     */   {
/* 230 */     for (CMBase cm : this.childModels)
/*     */     {
/* 232 */       cm.fullRender();
/*     */     }
/*     */   }
/*     */   
/*     */   public void applyColorOrTexture(ResourceLocation text, Color col)
/*     */   {
/* 238 */     if (shouldApplyTexture())
/*     */     {
/* 240 */       enableTexture(true);
/* 241 */       bindTexture(text);
/*     */     }
/*     */     else
/*     */     {
/* 245 */       enableTexture(false);
/* 246 */       if (col != null) color(col.red, col.green, col.blue, col.alpha);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void bindTexture(ResourceLocation location)
/*     */   {
/* 252 */     if (mc == null) mc = Minecraft.func_71410_x();
/* 253 */     if ((mc != null) && (location != null))
/*     */     {
/* 255 */       TextureManager texturemanager = mc.field_71446_o;
/*     */       
/* 257 */       if (texturemanager != null)
/*     */       {
/* 259 */         texturemanager.func_110577_a(location);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean shouldApplyTexture()
/*     */   {
/* 266 */     return (this.texture != null) && ((this.useTexture) || (this.color == null));
/*     */   }
/*     */   
/*     */   public static void push()
/*     */   {
/* 271 */     if (USEGLSM) GlStateManager.func_179094_E(); else {
/* 272 */       GL11.glPushMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void pop() {
/* 277 */     if (USEGLSM) GlStateManager.func_179121_F(); else {
/* 278 */       GL11.glPopMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void color(double r, double g, double b) {
/* 283 */     color(r, g, b, 1.0D);
/*     */   }
/*     */   
/*     */   public static void color(double r, double g, double b, double a)
/*     */   {
/* 288 */     if (USECOLORSPRITE)
/*     */     {
/* 290 */       double pixelHalfWidth = 8.680555555555555E-4D;
/* 291 */       double x = Math.floor(b * 23.0D) * 24.0D + Math.floor(r * 23.0D);
/* 292 */       double y = Math.floor(a * 23.0D) * 24.0D + Math.floor(g * 23.0D);
/* 293 */       x = x / 24.0D / 24.0D + pixelHalfWidth;
/* 294 */       y = y / 24.0D / 24.0D + pixelHalfWidth;
/* 295 */       texCoord(x, y);
/*     */     }
/* 297 */     else if (USEGLSM) { GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)a);
/* 298 */     } else { GL11.glColor4d(r, g, b, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void resetColor() {
/* 303 */     color(1.0D, 1.0D, 1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public static void clearColor(double r, double g, double b, double a)
/*     */   {
/* 308 */     if (USEGLSM) GlStateManager.func_179082_a((float)r, (float)g, (float)b, (float)a); else {
/* 309 */       GL11.glClearColor((float)r, (float)g, (float)b, (float)a);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableNormalize(boolean enable) {
/* 314 */     if (enable)
/*     */     {
/* 316 */       if (USEGLSM) GlStateManager.func_179108_z(); else {
/* 317 */         GL11.glEnable(2977);
/*     */       }
/*     */       
/*     */     }
/* 321 */     else if (USEGLSM) GlStateManager.func_179133_A(); else {
/* 322 */       GL11.glDisable(2977);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableLighting(boolean enable)
/*     */   {
/* 328 */     if (enable)
/*     */     {
/* 330 */       if (USEGLSM) GlStateManager.func_179145_e(); else {
/* 331 */         GL11.glEnable(2896);
/*     */       }
/*     */       
/*     */     }
/* 335 */     else if (USEGLSM) GlStateManager.func_179140_f(); else {
/* 336 */       GL11.glDisable(2896);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableFog(boolean enable)
/*     */   {
/* 342 */     if (enable)
/*     */     {
/* 344 */       if (USEGLSM) GlStateManager.func_179127_m(); else {
/* 345 */         GL11.glEnable(2912);
/*     */       }
/*     */       
/*     */     }
/* 349 */     else if (USEGLSM) GlStateManager.func_179106_n(); else {
/* 350 */       GL11.glDisable(2912);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableTexture(boolean enable)
/*     */   {
/* 356 */     if ((enable) || ((USECOLORSPRITE) && (!enable)))
/*     */     {
/* 358 */       if (USEGLSM) GlStateManager.func_179098_w(); else
/* 359 */         GL11.glEnable(3553);
/* 360 */       if (USECOLORSPRITE)
/*     */       {
/* 362 */         bindTexture(COLORSPITE);
/*     */       }
/*     */       
/*     */ 
/*     */     }
/* 367 */     else if (USEGLSM) { GlStateManager.func_179090_x();
/* 368 */     } else { GL11.glDisable(3553);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableColorMaterial(boolean enable)
/*     */   {
/* 374 */     if (enable)
/*     */     {
/* 376 */       if (USEGLSM) GlStateManager.func_179142_g(); else {
/* 377 */         GL11.glEnable(2903);
/*     */       }
/*     */       
/*     */     }
/* 381 */     else if (USEGLSM) GlStateManager.func_179119_h(); else {
/* 382 */       GL11.glDisable(2903);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableAlpha(boolean enable)
/*     */   {
/* 388 */     if (enable)
/*     */     {
/* 390 */       if (USEGLSM) GlStateManager.func_179141_d(); else {
/* 391 */         GL11.glEnable(3008);
/*     */       }
/*     */       
/*     */     }
/* 395 */     else if (USEGLSM) GlStateManager.func_179118_c(); else {
/* 396 */       GL11.glDisable(3008);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableBlend(boolean enable)
/*     */   {
/* 402 */     if (enable)
/*     */     {
/* 404 */       if (USEGLSM) GlStateManager.func_179147_l(); else {
/* 405 */         GL11.glEnable(3042);
/*     */       }
/*     */       
/*     */     }
/* 409 */     else if (USEGLSM) GlStateManager.func_179084_k(); else {
/* 410 */       GL11.glDisable(3042);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enableCull(boolean enable)
/*     */   {
/* 416 */     if (enable)
/*     */     {
/* 418 */       if (USEGLSM) GlStateManager.func_179089_o(); else {
/* 419 */         GL11.glEnable(2884);
/*     */       }
/*     */       
/*     */     }
/* 423 */     else if (USEGLSM) GlStateManager.func_179129_p(); else {
/* 424 */       GL11.glDisable(2884);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void blendFunc(int i1, int i2)
/*     */   {
/* 430 */     if (USEGLSM) GlStateManager.func_179112_b(i1, i2); else {
/* 431 */       GL11.glBlendFunc(i1, i2);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void translate(double x, double y, double z) {
/* 436 */     if (USEGLSM) GlStateManager.func_179137_b(x, y, z); else {
/* 437 */       GL11.glTranslated(x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void scale(double x, double y, double z) {
/* 442 */     if (USEGLSM) GlStateManager.func_179139_a(x, y, z); else {
/* 443 */       GL11.glScaled(x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void rotate(double angle, double x, double y, double z) {
/* 448 */     if (USEGLSM) GlStateManager.func_179114_b((float)angle, (float)x, (float)y, (float)z); else {
/* 449 */       GL11.glRotated(angle, x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void lineWidth(double w) {
/* 454 */     if (USEGLSM) GlStateManager.func_187441_d((float)w); else {
/* 455 */       GL11.glLineWidth((float)w);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void begin(int mode) {
/* 460 */     if (USEGLSM) GlStateManager.func_187447_r(mode); else {
/* 461 */       GL11.glBegin(mode);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void enable(int enable) {
/* 466 */     if (USEGLSM) GlStateManager.func_187410_q(enable); else {
/* 467 */       GL11.glEnable(enable);
/*     */     }
/*     */   }
/*     */   
/*     */   public void vertex(double x, double y, double z, double u, double v) {
/* 472 */     if (shouldApplyTexture()) texCoord(u, v);
/* 473 */     if (USEGLSM)
/*     */     {
/* 475 */       GlStateManager.func_187435_e((float)x, (float)y, (float)z);
/*     */     }
/*     */     else
/*     */     {
/* 479 */       GL11.glVertex3d(x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void texCoord(double u, double v)
/*     */   {
/* 485 */     if (USEGLSM)
/*     */     {
/* 487 */       GlStateManager.func_187426_b((float)u, (float)v);
/*     */     }
/*     */     else
/*     */     {
/* 491 */       GL11.glTexCoord2d(u, v);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void normal(double x, double y, double z)
/*     */   {
/* 497 */     if (USEGLSM) GlStateManager.func_187432_a((float)x, (float)y, (float)z); else {
/* 498 */       GL11.glNormal3d(x, y, z);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void end() {
/* 503 */     if (USEGLSM) GlStateManager.func_187437_J(); else {
/* 504 */       GL11.glEnd();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */