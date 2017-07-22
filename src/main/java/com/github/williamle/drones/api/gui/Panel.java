/*     */ package williamle.drones.api.gui;
/*     */ 
/*     */ import java.util.LinkedList;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import org.lwjgl.input.Mouse;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.model.Color;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Panel
/*     */ {
/*     */   public GuiContainerPanel parent;
/*     */   public Minecraft mc;
/*  16 */   public boolean scrollerAlwaysOn = false;
/*  17 */   public int scrollerSize = 4;
/*     */   public int px;
/*     */   public int py;
/*     */   public int pw;
/*     */   public int ph;
/*     */   public int scrollMax;
/*  23 */   public int scroll; public Color bgColor; public LinkedList<PI> items = new LinkedList();
/*  24 */   public boolean multipleChoice = false;
/*     */   
/*     */   public Panel(GuiContainerPanel g, int x, int y, int width, int height)
/*     */   {
/*  28 */     this.parent = g;
/*  29 */     this.mc = g.field_146297_k;
/*  30 */     this.px = x;
/*  31 */     this.py = y;
/*  32 */     this.pw = width;
/*  33 */     this.ph = height;
/*     */   }
/*     */   
/*     */   public void setScrollerSize(int i)
/*     */   {
/*  38 */     this.scrollerSize = i;
/*  39 */     if (this.scrollerAlwaysOn)
/*     */     {
/*  41 */       for (PI pi : this.items)
/*     */       {
/*  43 */         pi.xw = Math.min(pi.xw, this.pw - this.scrollerSize);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addItem(PI pi)
/*     */   {
/*  50 */     this.items.add(pi);
/*     */   }
/*     */   
/*     */   public void mouseClicked(int mx, int my, int mb)
/*     */   {
/*  55 */     if ((mx >= this.px) && (mx <= this.px + this.pw) && (my >= this.py) && (my <= this.py + this.ph))
/*     */     {
/*  57 */       mouseClickedLocal(mx - this.px, my - this.py, mb);
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseClickedLocal(int mx, int my, int mb)
/*     */   {
/*  63 */     if (mx >= this.pw - this.scrollerSize)
/*     */     {
/*  65 */       double clickedScrollLevel = my / this.ph;
/*  66 */       double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
/*  67 */       this.scroll = ((int)Math.min(this.scrollMax, clickedScrollLevel * this.scrollMax - scrollerHeight / 2.0D * this.scrollMax / this.ph));
/*     */     }
/*     */     else
/*     */     {
/*  71 */       int myScroll = my + this.scroll;
/*  72 */       int throughY = 0;
/*  73 */       for (int a = 0; a < this.items.size(); a++)
/*     */       {
/*  75 */         PI pi = (PI)this.items.get(a);
/*  76 */         throughY = (int)(throughY + pi.yw);
/*  77 */         if (throughY >= myScroll)
/*     */         {
/*  79 */           if (!this.multipleChoice) unselectAll();
/*  80 */           itemClicked(pi, mb);
/*  81 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseClickMove(int mx, int my, int clickedMouseButton, long timeSinceLastClick)
/*     */   {
/*  89 */     if ((mx >= this.px) && (mx <= this.px + this.pw) && (my >= this.py) && (my <= this.py + this.ph))
/*     */     {
/*  91 */       mouseClickMoveLocal(mx - this.px, my - this.py, clickedMouseButton, timeSinceLastClick);
/*     */     }
/*     */   }
/*     */   
/*     */   public void mouseClickMoveLocal(int mx, int my, int clickedMouseButton, long timeSinceLastClick)
/*     */   {
/*  97 */     if (mx >= this.pw - this.scrollerSize)
/*     */     {
/*  99 */       double clickedScrollLevel = my / this.ph;
/* 100 */       double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
/* 101 */       this.scroll = ((int)Math.min(this.scrollMax, clickedScrollLevel * this.scrollMax - scrollerHeight / 2.0D * this.scrollMax / this.ph));
/*     */     }
/*     */   }
/*     */   
/*     */   public void unselectAll()
/*     */   {
/* 107 */     for (int a = 0; a < this.items.size(); a++)
/*     */     {
/* 109 */       ((PI)this.items.get(a)).unselect();
/*     */     }
/*     */   }
/*     */   
/*     */   public PI getFirstSelectedItem()
/*     */   {
/* 115 */     for (int a = 0; a < this.items.size(); a++)
/*     */     {
/* 117 */       PI pi = (PI)this.items.get(a);
/* 118 */       if (pi.selected) return pi;
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */   
/*     */   public void itemClicked(PI pi, int mb)
/*     */   {
/* 125 */     pi.select();
/*     */   }
/*     */   
/*     */   public void itemSelected(PI pi)
/*     */   {
/* 130 */     this.parent.itemSelected(this, pi);
/*     */   }
/*     */   
/*     */   public void itemUnselected(PI pi)
/*     */   {
/* 135 */     this.parent.itemUnselected(this, pi);
/*     */   }
/*     */   
/*     */   public void itemDisabled(PI pi)
/*     */   {
/* 140 */     this.parent.itemDisabled(this, pi);
/*     */   }
/*     */   
/*     */   public void itemEnabled(PI pi)
/*     */   {
/* 145 */     this.parent.itemEnabled(this, pi);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void keyTyped(char cha, int code) {}
/*     */   
/*     */ 
/*     */   public void updatePanel()
/*     */   {
/* 155 */     this.scrollMax = 0;
/* 156 */     for (int a = 0; a < this.items.size(); a++)
/*     */     {
/* 158 */       this.scrollMax = ((int)(this.scrollMax + ((PI)this.items.get(a)).yw));
/* 159 */       PI pi = (PI)this.items.get(a);
/* 160 */       pi.updateItem();
/*     */     }
/* 162 */     if (isMouseOnPanel())
/*     */     {
/* 164 */       int mouseD = Mouse.getDWheel();
/* 165 */       this.scroll = ((int)(this.scroll - Math.signum(mouseD) * Math.pow(Math.abs(mouseD / 3.0D), 0.7D)));
/* 166 */       mouseHoverItem(false);
/*     */     }
/* 168 */     this.scroll = Math.max(0, Math.min(this.scroll, this.scrollMax - this.ph));
/*     */   }
/*     */   
/*     */   public boolean isMouseOnPanel()
/*     */   {
/* 173 */     if (Mouse.isCreated())
/*     */     {
/* 175 */       ScaledResolution sr = new ScaledResolution(this.mc);
/* 176 */       int w = this.mc.field_71443_c;
/* 177 */       int h = this.mc.field_71440_d;
/* 178 */       int mxlocal = Mouse.getX() * sr.func_78326_a() / w;
/* 179 */       int mylocal = (h - Mouse.getY()) * sr.func_78328_b() / h;
/* 180 */       return (mxlocal >= this.px) && (mxlocal <= this.px + this.pw) && (mylocal >= this.py) && (mylocal <= this.py + this.ph);
/*     */     }
/* 182 */     return false;
/*     */   }
/*     */   
/*     */   public PI mouseHoverItem(boolean drawing)
/*     */   {
/* 187 */     if ((Mouse.isCreated()) && (isMouseOnPanel()))
/*     */     {
/* 189 */       ScaledResolution sr = new ScaledResolution(this.mc);
/* 190 */       int w = this.mc.field_71443_c;
/* 191 */       int h = this.mc.field_71440_d;
/* 192 */       int mxlocal = Mouse.getX() * sr.func_78326_a() / w - this.px;
/* 193 */       int mylocal = (h - Mouse.getY()) * sr.func_78328_b() / h - this.py + this.scroll;
/* 194 */       int curScrollItem = 0;
/* 195 */       for (int a = 0; a < this.items.size(); a++)
/*     */       {
/* 197 */         PI pi = (PI)this.items.get(a);
/* 198 */         if ((mxlocal >= 0) && (mxlocal <= pi.xw) && (mylocal >= curScrollItem) && (mylocal <= curScrollItem + pi.yw))
/*     */         {
/* 200 */           pi.mouseOverItem(mxlocal, mylocal, drawing);
/* 201 */           return pi;
/*     */         }
/* 203 */         curScrollItem = (int)(curScrollItem + pi.yw);
/*     */       }
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */   
/*     */   public void drawPanel()
/*     */   {
/* 211 */     ScaledResolution sr = new ScaledResolution(this.mc);
/* 212 */     int sclh = sr.func_78328_b();
/* 213 */     int sclw = sr.func_78326_a();
/* 214 */     DrawHelper.drawRect(this.px, this.py, this.px + this.pw, this.py + this.ph, this.bgColor == null ? 0L : this.bgColor.toLong());
/* 215 */     GL11.glPushMatrix();
/* 216 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/* 217 */     GL11.glEnable(3089);
/* 218 */     GL11.glScissor(this.px * this.mc.field_71443_c / sclw, (sclh - this.py - this.ph) * this.mc.field_71440_d / sclh, this.pw * this.mc.field_71443_c / sclw, this.ph * this.mc.field_71440_d / sclh);
/*     */     
/* 220 */     GL11.glTranslated(this.px, this.py - this.scroll, 0.0D);
/* 221 */     drawInPanel();
/* 222 */     GL11.glDisable(3089);
/* 223 */     mouseHoverItem(true);
/* 224 */     GL11.glPopMatrix();
/*     */     
/* 226 */     if ((!this.items.isEmpty()) && ((isMouseOnPanel()) || (this.scrollerAlwaysOn)))
/*     */     {
/* 228 */       GL11.glPushMatrix();
/* 229 */       GL11.glTranslated(0.0D, 0.0D, 150.0D);
/* 230 */       double d0 = 0.2D;
/* 231 */       DrawHelper.drawRect(this.px + this.pw - this.scrollerSize, this.py, this.px + this.pw, this.py + this.ph, new Color(d0, d0, d0));
/* 232 */       double scrollerStart = this.py + this.scroll / this.scrollMax * this.ph;
/* 233 */       double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
/* 234 */       double d = 0.5D;
/* 235 */       double d1 = 0.75D;
/* 236 */       DrawHelper.drawGradientRect(this.px + this.pw - this.scrollerSize, scrollerStart, this.px + this.pw, scrollerStart + scrollerHeight, new Color(d, d, d), new Color(d1, d1, d1));
/*     */       
/* 238 */       GL11.glPopMatrix();
/*     */     }
/*     */   }
/*     */   
/*     */   public void drawInPanel()
/*     */   {
/* 244 */     int drawScrolled = 0;
/* 245 */     GL11.glPushMatrix();
/* 246 */     for (int a = 0; a < this.items.size(); a++)
/*     */     {
/* 248 */       PI pi = (PI)this.items.get(a);
/* 249 */       if (((drawScrolled >= this.scroll) && (drawScrolled <= this.scroll + this.ph)) || ((drawScrolled + pi.yw >= this.scroll) && (drawScrolled + pi.yw <= this.scroll + this.ph)) || ((this.scroll >= drawScrolled) && (this.scroll <= drawScrolled + pi.yw)) || ((this.scroll + this.ph >= drawScrolled) && (this.scroll + this.ph <= drawScrolled + pi.yw)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 254 */         pi.drawItem();
/*     */       }
/* 256 */       GL11.glTranslated(0.0D, pi.yw, 0.0D);
/* 257 */       drawScrolled = (int)(drawScrolled + pi.yw);
/*     */     }
/* 259 */     GL11.glPopMatrix();
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\Panel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */