package com.github.nekomeowww.customdrones.api.gui;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.model.Color;

public class PI
{
    public Panel panel;
    public int id;
    public double xw;
    public double yw;
    public double margin;
    public Color bgColor = new Color(0.7D, 0.7D, 0.7D);
    public Color bgMargin = new Color(0.2D, 0.2D, 0.2D).setNextColor(new Color(0.0D, 0.0D, 0.0D));
    public Color fgColor;
    public Color fgMargin;
    public Color strColor = new Color(1.0D, 1.0D, 1.0D);
    public boolean stringShadow = true;
    public String displayString;
    public boolean fitHorizontal = false;
    public boolean selected;
    public boolean negated;
    public FontRenderer fontRenderer;

    public PI(Panel p)
    {
        this.panel = p;
        this.fontRenderer = p.parent.field_146297_k.field_71466_p;
        this.xw = p.pw;
        if (p.scrollerAlwaysOn) {
            this.xw = (p.pw - p.scrollerSize);
        }
        this.yw = 30.0D;
        this.margin = 1.0D;
    }

    public PI setSize(double x, double y)
    {
        this.xw = x;
        this.yw = y;
        return this;
    }

    public void disable()
    {
        this.negated = true;
        this.panel.itemDisabled(this);
    }

    public void enable()
    {
        this.negated = false;
        this.panel.itemEnabled(this);
    }

    public void unselect()
    {
        this.selected = false;
        this.panel.itemUnselected(this);
    }

    public void select()
    {
        this.selected = true;
        this.panel.itemSelected(this);
    }

    public int midColor()
    {
        if (this.selected) {
            return -16711936;
        }
        if (this.negated) {
            return -65536;
        }
        return 0;
    }

    public void mouseOverItem(int mxlocal, int mylocal, boolean drawing) {}

    public void updateItem() {}

    public void drawItem()
    {
        GL11.glPushMatrix();
        Minecraft mc = this.panel.mc;
        ScaledResolution sr = new ScaledResolution(mc);
        int sclh = sr.func_78328_b();
        int sclw = sr.func_78326_a();
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        DrawHelper.drawGradientRect(0.0D, 0.0D, this.xw, this.yw, this.bgColor, DrawHelper.nextColor(this.bgColor));
        DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, this.bgMargin, DrawHelper.nextColor(this.bgMargin));
        drawItemContent();
        DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, midColor(), midColor());
        DrawHelper.drawGradientRect(0.0D, 0.0D, this.xw, this.yw, this.fgColor, DrawHelper.nextColor(this.fgColor));
        DrawHelper.drawGradientRectMargin(0.0D, 0.0D, this.xw, this.yw, this.margin, this.fgMargin, DrawHelper.nextColor(this.fgMargin));
        GL11.glPopMatrix();
    }

    public void drawItemContent()
    {
        int stringLength = this.fontRenderer.func_78256_a(this.displayString);
        int textMargin = (int)(this.margin * 2.0D + 8.0D);
        int totalLength = stringLength + textMargin;
        int maxStringLength = (int)Math.floor(this.xw - this.margin) - textMargin;
        if ((this.fitHorizontal) && (stringLength > maxStringLength))
        {
            GL11.glPushMatrix();
            GL11.glTranslated((this.xw - maxStringLength) / 2.0D, 0.0D, 0.0D);
            GL11.glScaled(maxStringLength / stringLength, 1.0D, 1.0D);
            this.fontRenderer.func_175065_a(this.displayString, 0.0F, (int)(this.yw - additionalContentY()) / 2 - 5,
                    (int)this.strColor.toLong(), this.stringShadow);
            GL11.glPopMatrix();
        }
        else
        {
            totalLength = 0;
            List<String> strings = this.fontRenderer.func_78271_c(this.displayString, maxStringLength);
            for (String s : strings)
            {
                int slength = this.fontRenderer.func_78256_a(s.trim());
                totalLength = Math.max(totalLength, slength + textMargin);
            }
            this.yw = Math.max(this.yw, strings.size() * 10 + textMargin + additionalContentY());
            for (int a = 0; a < strings.size(); a++)
            {
                String s = (String)strings.get(a);
                int thisLength = this.fontRenderer.func_78256_a(s);
                this.fontRenderer.func_175065_a(s, (int)(this.xw - thisLength) / 2,
                        (int)(this.yw - additionalContentY()) / 2 - 5 * strings.size() + 10 * a, (int)this.strColor.toLong(), this.stringShadow);
            }
        }
    }

    public int additionalContentY()
    {
        return 0;
    }
}
