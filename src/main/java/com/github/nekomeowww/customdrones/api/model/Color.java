package com.github.nekomeowww.customdrones.api.model;

public class Color
{
    public double alpha;
    public double red;
    public double green;
    public double blue;
    public Color nextColor;

    public Color(long color)
    {
        this.alpha = ((color >> 24 & 0xFF) / 255.0D);
        this.red = ((color >> 16 & 0xFF) / 255.0D);
        this.green = ((color >> 8 & 0xFF) / 255.0D);
        this.blue = ((color & 0xFF) / 255.0D);
    }

    public Color(double r, double g, double b)
    {
        this(r, g, b, 1.0D);
    }

    public Color(double r, double g, double b, double a)
    {
        this.red = Math.max(Math.min(r, 1.0D), 0.0D);
        this.green = Math.max(Math.min(g, 1.0D), 0.0D);
        this.blue = Math.max(Math.min(b, 1.0D), 0.0D);
        this.alpha = Math.max(Math.min(a, 1.0D), 0.0D);
    }

    public Color(int r, int g, int b, int a)
    {
        this(r / 255.0D, g / 255.0D, b / 255.0D, a / 255.0D);
    }

    public Color add(double r, double g, double b, double a)
    {
        return new Color(this.red + r, this.green + g, this.blue + b, this.alpha + a);
    }

    public Color multiplyRGBA(double d)
    {
        return multiply(d, d, d, d);
    }

    public Color multiplyRGB(double d)
    {
        return multiply(d, d, d, 1.0D);
    }

    public Color multiply(double r, double g, double b, double a)
    {
        return new Color(this.red * r, this.green * g, this.blue * b, this.alpha * a);
    }

    public Color blendNormal(Color c1, double blendRate)
    {
        return new Color(this.red + (c1.red - this.red) * blendRate, this.green + (c1.green - this.green) * blendRate, this.blue + (c1.blue - this.blue) * blendRate, this.alpha + (c1.alpha - this.alpha) * blendRate);
    }

    public Color setNextColor(Color c)
    {
        this.nextColor = c;
        return this;
    }

    public Color getNextColor()
    {
        if (this.nextColor == null) {
            return this;
        }
        return this.nextColor;
    }

    public int redI()
    {
        return (int)(this.red * 255.0D);
    }

    public int greenI()
    {
        return (int)(this.green * 255.0D);
    }

    public int blueI()
    {
        return (int)(this.blue * 255.0D);
    }

    public int alphaI()
    {
        return (int)(this.alpha * 255.0D);
    }

    public boolean isGradient()
    {
        return this.nextColor != null;
    }

    public String toString()
    {
        return "ARGB [ " + (int)(this.alpha * 255.0D) + " ; " + (int)(this.red * 255.0D) + " ; " + (int)(this.green * 255.0D) + " ; " + (int)(this.blue * 255.0D) + " ]";
    }

    public long toLong()
    {
        return (long)(this.alpha * 255.0D) << 24 | (long)(this.red * 255.0D) << 16 | (long)(this.green * 255.0D) << 8 | (long)(this.blue * 255.0D);
    }

    public int toInt()
    {
        return (int)(this.alpha * 255.0D) << 24 | (int)(this.red * 255.0D) << 16 | (int)(this.green * 255.0D) << 8 | (int)(this.blue * 255.0D);
    }

    public Color copy()
    {
        return new Color(this.red, this.green, this.blue, this.alpha);
    }
}
