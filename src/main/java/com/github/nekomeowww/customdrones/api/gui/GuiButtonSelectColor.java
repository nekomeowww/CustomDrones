package com.github.nekomeowww.customdrones.api.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.model.Color;

public class GuiButtonSelectColor
extends GuiButton
{
    public Color color1;
    public Color color2;
    public double selectedIndex = 0.0D;
    public double pointerOuter = 2.0D;
    public float pointWidth = 2.0F;

    public GuiButtonSelectColor(int buttonId, int x, int y, int w, int h, Color c1, Color c2)
    {
        super(buttonId, x, y, w, h, "");
        this.color1 = c1;
        this.color2 = c2;
    }

    public boolean func_146116_c(Minecraft mc, int mouseX, int mouseY)
    {
        if ((this.field_146124_l) && (this.field_146125_m) && (mouseX >= this.field_146128_h) && (mouseY >= this.field_146129_i) && (mouseX <= this.field_146128_h + this.field_146120_f) && (mouseY <= this.field_146129_i + this.field_146121_g))
        {
            this.selectedIndex = ((mouseX - this.field_146128_h) / this.field_146120_f);
            return true;
        }
        return false;
    }

    protected void func_146119_b(Minecraft mc, int mouseX, int mouseY)
    {
        super.func_146119_b(mc, mouseX, mouseY);
        if ((this.field_146124_l) && (this.field_146125_m) && (mouseX >= this.field_146128_h) && (mouseY >= this.field_146129_i) && (mouseX <= this.field_146128_h + this.field_146120_f) && (mouseY <= this.field_146129_i + this.field_146121_g)) {
            this.selectedIndex = ((mouseX - this.field_146128_h) / this.field_146120_f);
        }
    }

    public Color getOutputColor()
    {
        double alpha = (this.color2.alpha - this.color1.alpha) * this.selectedIndex + this.color1.alpha;
        double red = (this.color2.red - this.color1.red) * this.selectedIndex + this.color1.red;
        double green = (this.color2.green - this.color1.green) * this.selectedIndex + this.color1.green;
        double blue = (this.color2.blue - this.color1.blue) * this.selectedIndex + this.color1.blue;
        Color c = new Color(red, green, blue, alpha);
        return c;
    }

    public Color getReverseOutputColor()
    {
        double s = this.selectedIndex;
        this.selectedIndex = (1.0D - this.selectedIndex);
        Color c = getOutputColor();
        this.selectedIndex = s;
        return c;
    }

    public void func_146112_a(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.field_146125_m)
        {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(7);
            GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
            GL11.glVertex2d(this.field_146128_h, this.field_146129_i);
            GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
            GL11.glVertex2d(this.field_146128_h, this.field_146129_i + this.field_146121_g);
            GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
            GL11.glVertex2d(this.field_146128_h + this.field_146120_f, this.field_146129_i + this.field_146121_g);
            GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
            GL11.glVertex2d(this.field_146128_h + this.field_146120_f, this.field_146129_i);
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glPopMatrix();
            if ((Mouse.isCreated()) && (Mouse.isButtonDown(0))) {
                func_146119_b(mc, mouseX, mouseY);
            }
            double lineX = this.field_146128_h + this.field_146120_f * this.selectedIndex;
            DrawHelper.drawLine(lineX, this.field_146129_i - this.pointerOuter, lineX, this.field_146129_i + this.field_146121_g + this.pointerOuter,
                    getReverseOutputColor(), this.pointWidth);
        }
    }
}
