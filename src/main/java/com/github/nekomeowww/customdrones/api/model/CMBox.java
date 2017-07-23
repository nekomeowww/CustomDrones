package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class CMBox
        extends CMBase
{
    double x1;
    double y1;
    double z1;
    double x2;
    double y2;
    double z2;

    public CMBox(double size)
    {
        this(size, size, size, null);
    }

    public CMBox(double size, Vec3d mid)
    {
        this(size, size, size, mid);
    }

    public CMBox(double xw, double yw, double zw)
    {
        this(xw, yw, zw, null);
    }

    public CMBox(double xw, double yw, double zw, Vec3d mid)
    {
        this(-xw / 2.0D + (mid == null ? 0.0D : mid.field_72450_a), -yw / 2.0D + (mid == null ? 0.0D : mid.field_72448_b), -zw / 2.0D + (mid == null ? 0.0D : mid.field_72449_c), xw / 2.0D + (mid == null ? 0.0D : mid.field_72450_a), yw / 2.0D + (mid == null ? 0.0D : mid.field_72448_b), zw / 2.0D + (mid == null ? 0.0D : mid.field_72449_c));
    }

    public CMBox(double x, double y, double z, double xx, double yy, double zz)
    {
        this.x1 = x;
        this.x2 = xx;
        this.y1 = y;
        this.y2 = yy;
        this.z1 = z;
        this.z2 = zz;
    }

    public void render()
    {
        double xw = Math.abs(this.x2 - this.x1);
        double yw = Math.abs(this.y2 - this.y1);
        double zw = Math.abs(this.z2 - this.z1);
        double tWidth = xw * 2.0D + zw * 2.0D;
        double tHeight = zw + yw;

        double u1 = this.textureUV.u1;
        double u5 = this.textureUV.u2;
        double u2 = u1 + zw / tWidth * (u5 - u1);
        double u3 = u2 + xw / tWidth * (u5 - u1);
        double u4 = u3 + zw / tWidth * (u5 - u1);
        double v1 = this.textureUV.v1;
        double v4 = this.textureUV.v2;
        double v2 = v1 + zw / tHeight * (v4 - v1);

        begin(8);
        normal(-1.0D, 0.0D, 0.0D);
        vertex(this.x1, this.y2, this.z1, u1, v2);
        vertex(this.x1, this.y1, this.z1, u1, v4);
        normal(0.0D, 0.0D, 1.0D);
        vertex(this.x1, this.y2, this.z2, u2, v2);
        vertex(this.x1, this.y1, this.z2, u2, v4);
        normal(1.0D, 0.0D, 0.0D);
        vertex(this.x2, this.y2, this.z2, u3, v2);
        vertex(this.x2, this.y1, this.z2, u3, v4);
        normal(0.0D, 0.0D, -1.0D);
        vertex(this.x2, this.y2, this.z1, u4, v2);
        vertex(this.x2, this.y1, this.z1, u4, v4);
        normal(-1.0D, 0.0D, 0.0D);
        vertex(this.x1, this.y2, this.z1, u5, v2);
        vertex(this.x1, this.y1, this.z1, u5, v4);
        end();

        begin(7);
        normal(0.0D, 1.0D, 0.0D);
        vertex(this.x1, this.y2, this.z1, u2, v1);
        vertex(this.x1, this.y2, this.z2, u2, v2);
        vertex(this.x2, this.y2, this.z2, u3, v2);
        vertex(this.x2, this.y2, this.z1, u3, v1);
        normal(0.0D, -1.0D, 0.0D);
        vertex(this.x2, this.y1, this.z1, u5, v2);
        vertex(this.x2, this.y1, this.z2, u5, v1);
        vertex(this.x1, this.y1, this.z2, u4, v1);
        vertex(this.x1, this.y1, this.z1, u4, v2);
        end();
    }
}
