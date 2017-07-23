package com.github.nekomeowww.customdrones.api.model;

public class TextureUV
{
    public double u1;
    public double v1;
    public double u2;
    public double v2;

    public TextureUV(double u, double v, double uu, double vv)
    {
        this.u1 = u;
        this.v1 = v;
        this.u2 = uu;
        this.v2 = vv;
    }

    public TextureUV()
    {
        this(0.0D, 0.0D, 1.0D, 1.0D);
    }
}
