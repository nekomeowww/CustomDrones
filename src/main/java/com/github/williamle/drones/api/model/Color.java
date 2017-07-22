/*     */ package williamle.drones.api.model;
/*     */ 
/*     */ public class Color
/*     */ {
/*     */   public double alpha;
/*     */   public double red;
/*     */   public double green;
/*     */   public double blue;
/*     */   public Color nextColor;
/*     */   
/*     */   public Color(long color) {
/*  12 */     this.alpha = ((color >> 24 & 0xFF) / 255.0D);
/*  13 */     this.red = ((color >> 16 & 0xFF) / 255.0D);
/*  14 */     this.green = ((color >> 8 & 0xFF) / 255.0D);
/*  15 */     this.blue = ((color & 0xFF) / 255.0D);
/*     */   }
/*     */   
/*     */   public Color(double r, double g, double b)
/*     */   {
/*  20 */     this(r, g, b, 1.0D);
/*     */   }
/*     */   
/*     */   public Color(double r, double g, double b, double a)
/*     */   {
/*  25 */     this.red = Math.max(Math.min(r, 1.0D), 0.0D);
/*  26 */     this.green = Math.max(Math.min(g, 1.0D), 0.0D);
/*  27 */     this.blue = Math.max(Math.min(b, 1.0D), 0.0D);
/*  28 */     this.alpha = Math.max(Math.min(a, 1.0D), 0.0D);
/*     */   }
/*     */   
/*     */   public Color(int r, int g, int b, int a)
/*     */   {
/*  33 */     this(r / 255.0D, g / 255.0D, b / 255.0D, a / 255.0D);
/*     */   }
/*     */   
/*     */   public Color add(double r, double g, double b, double a)
/*     */   {
/*  38 */     return new Color(this.red + r, this.green + g, this.blue + b, this.alpha + a);
/*     */   }
/*     */   
/*     */   public Color multiplyRGBA(double d)
/*     */   {
/*  43 */     return multiply(d, d, d, d);
/*     */   }
/*     */   
/*     */   public Color multiplyRGB(double d)
/*     */   {
/*  48 */     return multiply(d, d, d, 1.0D);
/*     */   }
/*     */   
/*     */   public Color multiply(double r, double g, double b, double a)
/*     */   {
/*  53 */     return new Color(this.red * r, this.green * g, this.blue * b, this.alpha * a);
/*     */   }
/*     */   
/*     */   public Color blendNormal(Color c1, double blendRate)
/*     */   {
/*  58 */     return new Color(this.red + (c1.red - this.red) * blendRate, this.green + (c1.green - this.green) * blendRate, this.blue + (c1.blue - this.blue) * blendRate, this.alpha + (c1.alpha - this.alpha) * blendRate);
/*     */   }
/*     */   
/*     */ 
/*     */   public Color setNextColor(Color c)
/*     */   {
/*  64 */     this.nextColor = c;
/*  65 */     return this;
/*     */   }
/*     */   
/*     */   public Color getNextColor()
/*     */   {
/*  70 */     if (this.nextColor == null) return this;
/*  71 */     return this.nextColor;
/*     */   }
/*     */   
/*     */   public int redI()
/*     */   {
/*  76 */     return (int)(this.red * 255.0D);
/*     */   }
/*     */   
/*     */   public int greenI()
/*     */   {
/*  81 */     return (int)(this.green * 255.0D);
/*     */   }
/*     */   
/*     */   public int blueI()
/*     */   {
/*  86 */     return (int)(this.blue * 255.0D);
/*     */   }
/*     */   
/*     */   public int alphaI()
/*     */   {
/*  91 */     return (int)(this.alpha * 255.0D);
/*     */   }
/*     */   
/*     */   public boolean isGradient()
/*     */   {
/*  96 */     return this.nextColor != null;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 102 */     return "ARGB [ " + (int)(this.alpha * 255.0D) + " ; " + (int)(this.red * 255.0D) + " ; " + (int)(this.green * 255.0D) + " ; " + (int)(this.blue * 255.0D) + " ]";
/*     */   }
/*     */   
/*     */ 
/*     */   public long toLong()
/*     */   {
/* 108 */     return (this.alpha * 255.0D) << 24 | (this.red * 255.0D) << 16 | (this.green * 255.0D) << 8 | (this.blue * 255.0D);
/*     */   }
/*     */   
/*     */   public int toInt()
/*     */   {
/* 113 */     return (int)(this.alpha * 255.0D) << 24 | (int)(this.red * 255.0D) << 16 | (int)(this.green * 255.0D) << 8 | (int)(this.blue * 255.0D);
/*     */   }
/*     */   
/*     */   public Color copy()
/*     */   {
/* 118 */     return new Color(this.red, this.green, this.blue, this.alpha);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\Color.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */