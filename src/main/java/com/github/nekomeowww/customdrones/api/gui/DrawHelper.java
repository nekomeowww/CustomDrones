package com.github.nekomeowww.customdrones.api.gui;

import java.util.List;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.model.Color;

public class DrawHelper
{
    public static void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font, int width, int height)
    {
        if (!textLines.isEmpty())
        {
            GL11.glPushMatrix();
            GL11.glDisable(2896);
            GL11.glTranslated(0.0D, 0.0D, 200.0D);
            int i = 0;
            for (String s : textLines)
            {
                int j = font.func_78256_a(s);
                if (j > i) {
                    i = j;
                }
            }
            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;
            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }
            if (l1 + i > width) {
                l1 -= 28 + i;
            }
            if (i2 + k + 6 > height) {
                i2 = height - k - 6;
            }
            int l = -267386864;
            drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 0xFEFEFE) >> 1 | i1 & 0xFF000000;
            drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);
            for (int k1 = 0; k1 < textLines.size(); k1++)
            {
                String s1 = (String)textLines.get(k1);
                font.func_175063_a(s1, l1, i2, -1);
                if (k1 == 0) {
                    i2 += 2;
                }
                i2 += 10;
            }
            GL11.glColor3d(1.0D, 1.0D, 1.0D);
            GL11.glPopMatrix();
        }
    }

    public static Color nextColor(Color c)
    {
        if (c == null) {
            return null;
        }
        return c.getNextColor();
    }

    public static void drawLine(double x0, double y0, double x1, double y1, Color c, float width)
    {
        drawLine(x0, y0, 0.0D, x1, y1, 0.0D, c, width);
    }

    public static void drawLine(double x0, double y0, double z0, double x1, double y1, double z1, Color c, float width)
    {
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(width);
        GL11.glBegin(1);
        GL11.glColor4d(c.red, c.green, c.blue, c.alpha);
        GL11.glVertex3d(x0, y0, z0);
        if (c.isGradient()) {
            GL11.glColor4d(c.getNextColor().red, c.getNextColor().green, c.getNextColor().blue, c.getNextColor().alpha);
        }
        GL11.glVertex3d(x1, y1, z1);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
        GL11.glPopMatrix();
    }

    public static void drawRectMargin(double x0, double y0, double x1, double y1, double mg, Color c)
    {
        drawRectMargin(x0, y0, x1, y1, mg, c == null ? 0L : c.toLong());
    }

    public static void drawRectMargin(double x0, double y0, double x1, double y1, double mg, long color)
    {
        drawRectMargin(x0, y0, x1, y1, mg, mg, color);
    }

    public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mg, Color c1, Color c2)
    {
        drawGradientRectMargin(x0, y0, x1, y1, mg, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
    }

    public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mg, long color1, long color2)
    {
        drawGradientRectMargin(x0, y0, x1, y1, mg, mg, color1, color2);
    }

    public static void drawRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, Color c)
    {
        drawRectMargin(x0, y0, x1, y1, mgx, mgy, c == null ? 0L : c.toLong());
    }

    public static void drawRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, long color)
    {
        drawRect(x0, y0, x0 + mgx, y1, color);
        drawRect(x0, y0, x1, y0 + mgy, color);
        drawRect(x1 - mgx, y0, x1, y1, color);
        drawRect(x0, y1 - mgy, x1, y1, color);
    }

    public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, Color c1, Color c2)
    {
        drawGradientRectMargin(x0, y0, x1, y1, mgx, mgy, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
    }

    public static void drawGradientRectMargin(double x0, double y0, double x1, double y1, double mgx, double mgy, long color1, long color2)
    {
        drawGradientRect(x0, y0, x0 + mgx, y1, color1, color2);
        drawGradientRect(x0, y0, x1, y0 + mgy, color1, color2);
        drawGradientRect(x1 - mgx, y0, x1, y1, color1, color2);
        drawGradientRect(x0, y1 - mgy, x1, y1, color1, color2);
    }

    public static void drawRect(double left, double top, double right, double bottom, Color c)
    {
        drawRect(left, top, right, bottom, c == null ? 0L : c.toLong());
    }

    public static void drawRect(double left, double top, double right, double bottom, long color)
    {
        if (left < right)
        {
            double i = left;
            left = right;
            right = i;
        }
        if (top < bottom)
        {
            double j = top;
            top = bottom;
            bottom = j;
        }
        float f3 = (float)(color >> 24 & 0xFF) / 255.0F;
        float f = (float)(color >> 16 & 0xFF) / 255.0F;
        float f1 = (float)(color >> 8 & 0xFF) / 255.0F;
        float f2 = (float)(color & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glBegin(7);
        GL11.glColor4f(f, f1, f2, f3);
        GL11.glVertex2d(left, bottom);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glVertex2d(left, top);
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
        GL11.glPopMatrix();
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, Color c1, Color c2)
    {
        drawGradientRect(left, top, right, bottom, c1 == null ? 0L : c1.toLong(), c2 == null ? 0L : c2.toLong());
    }

    public static void drawGradientRect(double left, double top, double right, double bottom, long startColor, long endColor)
    {
        float f = (float)(startColor >> 24 & 0xFF) / 255.0F;
        float f1 = (float)(startColor >> 16 & 0xFF) / 255.0F;
        float f2 = (float)(startColor >> 8 & 0xFF) / 255.0F;
        float f3 = (float)(startColor & 0xFF) / 255.0F;
        float f4 = (float)(endColor >> 24 & 0xFF) / 255.0F;
        float f5 = (float)(endColor >> 16 & 0xFF) / 255.0F;
        float f6 = (float)(endColor >> 8 & 0xFF) / 255.0F;
        float f7 = (float)(endColor & 0xFF) / 255.0F;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glShadeModel(7425);
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(right, top);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glEnd();
        GL11.glShadeModel(7424);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
        GL11.glPopMatrix();
    }
}
