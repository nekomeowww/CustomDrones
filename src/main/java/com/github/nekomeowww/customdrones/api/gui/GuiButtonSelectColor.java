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

    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if ((this.enabled) && (this.visible) && (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX <= this.xPosition + this.width) && (mouseY <= this.yPosition + this.height))
        {
            this.selectedIndex = ((mouseX - this.xPosition) / this.width);
            return true;
        }
        return false;
    }

    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        super.mouseDragged(mc, mouseX, mouseY);
        if ((this.enabled) && (this.visible) && (mouseX >= this.xPosition) && (mouseY >= this.yPosition) && (mouseX <= this.xPosition + this.width) && (mouseY <= this.yPosition + this.height)) {
            this.selectedIndex = ((mouseX - this.xPosition) / this.width);
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

    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            GL11.glPushMatrix();
            GL11.glDisable(3553);
            GL11.glShadeModel(7425);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glBegin(7);
            GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
            GL11.glVertex2d(this.xPosition, this.yPosition);
            GL11.glColor4d(this.color1.red, this.color1.green, this.color1.blue, this.color1.alpha);
            GL11.glVertex2d(this.xPosition, this.yPosition + this.height);
            GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
            GL11.glVertex2d(this.xPosition + this.width, this.yPosition + this.height);
            GL11.glColor4d(this.color2.red, this.color2.green, this.color2.blue, this.color2.alpha);
            GL11.glVertex2d(this.xPosition + this.width, this.yPosition);
            GL11.glEnd();
            GL11.glEnable(3553);
            GL11.glShadeModel(7424);
            GL11.glPopMatrix();
            if ((Mouse.isCreated()) && (Mouse.isButtonDown(0))) {
                mouseDragged(mc, mouseX, mouseY);
            }
            double lineX = this.xPosition + this.width * this.selectedIndex;
            DrawHelper.drawLine(lineX, this.yPosition - this.pointerOuter, lineX, this.yPosition + this.height + this.pointerOuter,
                    getReverseOutputColor(), this.pointWidth);
        }
    }
}
