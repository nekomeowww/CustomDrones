package com.github.nekomeowww.customdrones.api.gui;

import com.github.nekomeowww.customdrones.api.model.Color;

public class PIObjectColor
        extends PI
{
    public Color defaultColor;
    public Color color;

    public PIObjectColor(Panel p, Color c)
    {
        super(p);
        this.color = c;
        this.defaultColor = (this.color != null ? this.color.copy() : null);
    }

    public void drawItemContent()
    {
        super.drawItemContent();
        double ymin = this.yw - additionalContentY() * 1.5D + 4.0D;
        double ymax = this.yw - 4.0D;
        double xMargin = 12.0D;
        if (this.color != null)
        {
            DrawHelper.drawRect(xMargin, ymin, this.xw - xMargin, ymax, this.color);
        }
        else
        {
            Color c = new Color(1.0D, 0.0D, 0.0D);
            DrawHelper.drawLine(xMargin, ymin, this.xw - xMargin, ymax, c, 2.0F);
            DrawHelper.drawLine(this.xw - xMargin, ymin, xMargin, ymax, c, 2.0F);
        }
    }

    public int additionalContentY()
    {
        return 15;
    }
}
