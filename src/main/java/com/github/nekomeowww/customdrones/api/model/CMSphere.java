package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class CMSphere
        extends CMBase
{
    public Vec3d origin;
    public double radius;
    public double verDiv;
    public double horDiv;

    public CMSphere(Vec3d ori, double r, double div)
    {
        this(ori, r, div, div * 2.0D);
    }

    public CMSphere(Vec3d ori, double r, double div1, double div2)
    {
        this.origin = ori;
        if (ori == null) {
            this.origin = vec(0.0D, 0.0D, 0.0D);
        }
        this.radius = r;
        this.verDiv = div1;
        this.horDiv = div2;
    }

    public void render()
    {
        Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
        Vec3d yUnit = vec(0.0D, 1.0D, 0.0D);
        Vec3d yRad = vec(0.0D, this.radius, 0.0D);
        double verDivAngle = 3.141592653589793D / this.verDiv;
        double horDivAngle = 6.283185307179586D / this.horDiv;
        for (int a = 0; a < this.verDiv; a++)
        {
            Vec3d currentVerVec = rotateAround(yRad, zUnit, verDivAngle * a);
            Vec3d nextVerVec = rotateAround(yRad, zUnit, verDivAngle * (a + 1));
            begin(8);
            for (int b = 0; b < this.horDiv + 1.0D; b++)
            {
                Vec3d currentVec = rotateAround(currentVerVec, yUnit, -horDivAngle * b);
                Vec3d nextVec = rotateAround(nextVerVec, yUnit, -horDivAngle * b);
                double[] currentUV = calculateUVFromLocalSphere(currentVec);
                double[] nextUV = calculateUVFromLocalSphere(nextVec);
                Vec3d currentFinal = this.origin.func_178787_e(currentVec);
                Vec3d nextFinal = this.origin.func_178787_e(nextVec);
                Vec3d currentNorm = currentVec.func_72432_b();
                Vec3d nextNorm = nextVec.func_72432_b();
                normal(currentNorm.field_72450_a, currentNorm.field_72448_b, currentNorm.field_72449_c);
                vertex(currentFinal.field_72450_a, currentFinal.field_72448_b, currentFinal.field_72449_c, currentUV[0], currentUV[1]);
                normal(nextNorm.field_72450_a, nextNorm.field_72448_b, nextNorm.field_72449_c);
                vertex(nextFinal.field_72450_a, nextFinal.field_72448_b, nextFinal.field_72449_c, nextUV[0], nextUV[1]);
            }
            end();
        }
    }

    public double[] calculateUVFromLocalSphere(Vec3d position)
    {
        Vec3d needPos = scale(position, -1.0D);
        double[] uvs = new double[2];
        if (shouldApplyTexture())
        {
            double umid = (this.textureUV.u1 + this.textureUV.u2) / 2.0D;
            double ulength = this.textureUV.u2 - this.textureUV.u1;
            double vmid = (this.textureUV.v1 + this.textureUV.v2) / 2.0D;
            double vlength = this.textureUV.v1 - this.textureUV.v2;
            uvs[0] = (umid - ulength * (Math.atan2(needPos.field_72449_c, needPos.field_72450_a) / 2.0D / 3.141592653589793D));
            uvs[1] = (vmid - vlength * (Math.sin(needPos.field_72448_b) / 3.141592653589793D));
        }
        return uvs;
    }
}
