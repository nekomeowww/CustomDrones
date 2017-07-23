package com.github.nekomeowww.customdrones.api.gui;

import java.util.LinkedList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.model.Color;

public class Panel
{
    public GuiContainerPanel parent;
    public Minecraft mc;
    public boolean scrollerAlwaysOn = false;
    public int scrollerSize = 4;
    public int px;
    public int py;
    public int pw;
    public int ph;
    public int scrollMax;
    public int scroll;
    public Color bgColor;
    public LinkedList<PI> items = new LinkedList();
    public boolean multipleChoice = false;

    public Panel(GuiContainerPanel g, int x, int y, int width, int height)
    {
        this.parent = g;
        this.mc = g.field_146297_k;
        this.px = x;
        this.py = y;
        this.pw = width;
        this.ph = height;
    }

    public void setScrollerSize(int i)
    {
        this.scrollerSize = i;
        if (this.scrollerAlwaysOn) {
            for (PI pi : this.items) {
                pi.xw = Math.min(pi.xw, this.pw - this.scrollerSize);
            }
        }
    }

    public void addItem(PI pi)
    {
        this.items.add(pi);
    }

    public void mouseClicked(int mx, int my, int mb)
    {
        if ((mx >= this.px) && (mx <= this.px + this.pw) && (my >= this.py) && (my <= this.py + this.ph)) {
            mouseClickedLocal(mx - this.px, my - this.py, mb);
        }
    }

    public void mouseClickedLocal(int mx, int my, int mb)
    {
        if (mx >= this.pw - this.scrollerSize)
        {
            double clickedScrollLevel = my / this.ph;
            double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
            this.scroll = ((int)Math.min(this.scrollMax, clickedScrollLevel * this.scrollMax - scrollerHeight / 2.0D * this.scrollMax / this.ph));
        }
        else
        {
            int myScroll = my + this.scroll;
            int throughY = 0;
            for (int a = 0; a < this.items.size(); a++)
            {
                PI pi = (PI)this.items.get(a);
                throughY = (int)(throughY + pi.yw);
                if (throughY >= myScroll)
                {
                    if (!this.multipleChoice) {
                        unselectAll();
                    }
                    itemClicked(pi, mb);
                    break;
                }
            }
        }
    }

    public void mouseClickMove(int mx, int my, int clickedMouseButton, long timeSinceLastClick)
    {
        if ((mx >= this.px) && (mx <= this.px + this.pw) && (my >= this.py) && (my <= this.py + this.ph)) {
            mouseClickMoveLocal(mx - this.px, my - this.py, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void mouseClickMoveLocal(int mx, int my, int clickedMouseButton, long timeSinceLastClick)
    {
        if (mx >= this.pw - this.scrollerSize)
        {
            double clickedScrollLevel = my / this.ph;
            double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
            this.scroll = ((int)Math.min(this.scrollMax, clickedScrollLevel * this.scrollMax - scrollerHeight / 2.0D * this.scrollMax / this.ph));
        }
    }

    public void unselectAll()
    {
        for (int a = 0; a < this.items.size(); a++) {
            ((PI)this.items.get(a)).unselect();
        }
    }

    public PI getFirstSelectedItem()
    {
        for (int a = 0; a < this.items.size(); a++)
        {
            PI pi = (PI)this.items.get(a);
            if (pi.selected) {
                return pi;
            }
        }
        return null;
    }

    public void itemClicked(PI pi, int mb)
    {
        pi.select();
    }

    public void itemSelected(PI pi)
    {
        this.parent.itemSelected(this, pi);
    }

    public void itemUnselected(PI pi)
    {
        this.parent.itemUnselected(this, pi);
    }

    public void itemDisabled(PI pi)
    {
        this.parent.itemDisabled(this, pi);
    }

    public void itemEnabled(PI pi)
    {
        this.parent.itemEnabled(this, pi);
    }

    public void keyTyped(char cha, int code) {}

    public void updatePanel()
    {
        this.scrollMax = 0;
        for (int a = 0; a < this.items.size(); a++)
        {
            this.scrollMax = ((int)(this.scrollMax + ((PI)this.items.get(a)).yw));
            PI pi = (PI)this.items.get(a);
            pi.updateItem();
        }
        if (isMouseOnPanel())
        {
            int mouseD = Mouse.getDWheel();
            this.scroll = ((int)(this.scroll - Math.signum(mouseD) * Math.pow(Math.abs(mouseD / 3.0D), 0.7D)));
            mouseHoverItem(false);
        }
        this.scroll = Math.max(0, Math.min(this.scroll, this.scrollMax - this.ph));
    }

    public boolean isMouseOnPanel()
    {
        if (Mouse.isCreated())
        {
            ScaledResolution sr = new ScaledResolution(this.mc);
            int w = this.mc.field_71443_c;
            int h = this.mc.field_71440_d;
            int mxlocal = Mouse.getX() * sr.func_78326_a() / w;
            int mylocal = (h - Mouse.getY()) * sr.func_78328_b() / h;
            return (mxlocal >= this.px) && (mxlocal <= this.px + this.pw) && (mylocal >= this.py) && (mylocal <= this.py + this.ph);
        }
        return false;
    }

    public PI mouseHoverItem(boolean drawing)
    {
        if ((Mouse.isCreated()) && (isMouseOnPanel()))
        {
            ScaledResolution sr = new ScaledResolution(this.mc);
            int w = this.mc.field_71443_c;
            int h = this.mc.field_71440_d;
            int mxlocal = Mouse.getX() * sr.func_78326_a() / w - this.px;
            int mylocal = (h - Mouse.getY()) * sr.func_78328_b() / h - this.py + this.scroll;
            int curScrollItem = 0;
            for (int a = 0; a < this.items.size(); a++)
            {
                PI pi = (PI)this.items.get(a);
                if ((mxlocal >= 0) && (mxlocal <= pi.xw) && (mylocal >= curScrollItem) && (mylocal <= curScrollItem + pi.yw))
                {
                    pi.mouseOverItem(mxlocal, mylocal, drawing);
                    return pi;
                }
                curScrollItem = (int)(curScrollItem + pi.yw);
            }
        }
        return null;
    }

    public void drawPanel()
    {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int sclh = sr.func_78328_b();
        int sclw = sr.func_78326_a();
        DrawHelper.drawRect(this.px, this.py, this.px + this.pw, this.py + this.ph, this.bgColor == null ? 0L : this.bgColor.toLong());
        GL11.glPushMatrix();
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        GL11.glEnable(3089);
        GL11.glScissor(this.px * this.mc.field_71443_c / sclw, (sclh - this.py - this.ph) * this.mc.field_71440_d / sclh, this.pw * this.mc.field_71443_c / sclw, this.ph * this.mc.field_71440_d / sclh);

        GL11.glTranslated(this.px, this.py - this.scroll, 0.0D);
        drawInPanel();
        GL11.glDisable(3089);
        mouseHoverItem(true);
        GL11.glPopMatrix();
        if ((!this.items.isEmpty()) && ((isMouseOnPanel()) || (this.scrollerAlwaysOn)))
        {
            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, 0.0D, 150.0D);
            double d0 = 0.2D;
            DrawHelper.drawRect(this.px + this.pw - this.scrollerSize, this.py, this.px + this.pw, this.py + this.ph, new Color(d0, d0, d0));
            double scrollerStart = this.py + this.scroll / this.scrollMax * this.ph;
            double scrollerHeight = Math.min(this.ph, this.ph / this.scrollMax * this.ph);
            double d = 0.5D;
            double d1 = 0.75D;
            DrawHelper.drawGradientRect(this.px + this.pw - this.scrollerSize, scrollerStart, this.px + this.pw, scrollerStart + scrollerHeight, new Color(d, d, d), new Color(d1, d1, d1));

            GL11.glPopMatrix();
        }
    }

    public void drawInPanel()
    {
        int drawScrolled = 0;
        GL11.glPushMatrix();
        for (int a = 0; a < this.items.size(); a++)
        {
            PI pi = (PI)this.items.get(a);
            if (((drawScrolled >= this.scroll) && (drawScrolled <= this.scroll + this.ph)) || ((drawScrolled + pi.yw >= this.scroll) && (drawScrolled + pi.yw <= this.scroll + this.ph)) || ((this.scroll >= drawScrolled) && (this.scroll <= drawScrolled + pi.yw)) || ((this.scroll + this.ph >= drawScrolled) && (this.scroll + this.ph <= drawScrolled + pi.yw))) {
                pi.drawItem();
            }
            GL11.glTranslated(0.0D, pi.yw, 0.0D);
            drawScrolled = (int)(drawScrolled + pi.yw);
        }
        GL11.glPopMatrix();
    }
}
