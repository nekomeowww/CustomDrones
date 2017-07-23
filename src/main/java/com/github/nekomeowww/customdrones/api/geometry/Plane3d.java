package com.github.nekomeowww.customdrones.api.geometry

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

public class Plane3d
{
    public final Vec3d aPoint;
    public final Vec3d normal;
    public final Vec3d normalNorm;

    public Plane3d(Vec3d p, Vec3d n)
    {
        this.aPoint = p;
        this.normal = n;
        this.normalNorm = this.normal.func_72432_b();
    }

    public Vec3d intersectInAABB(Line3d line, AxisAlignedBB aabb)
    {
        Vec3d vec = intersect(line);
        if (vec != null)
        {
            if (aabb.func_72318_a(vec)) return vec;
        }
        return null;
    }

    public Vec3d intersect(Line3d line)
    {
        if (line.unitNorm.func_72430_b(this.normal) == 0.0D) return null;
        Vec3d wVec = line.aPoint.func_178788_d(this.aPoint);
        double unitScale = -this.normalNorm.func_72430_b(wVec) / this.normalNorm.func_72430_b(line.unitNorm);
        return line.aPoint.func_178787_e(line.unitNorm.func_186678_a(unitScale));
    }


    public Vec3d getAVecOnPlane()
    {
        Vec3d secondVector = this.normalNorm.field_72448_b == 1.0D ? this.normalNorm.func_178789_a(1.5707964F) : this.normalNorm.func_178785_b(1.5707964F);
        return williamle.drones.api.helpers.VecHelper.getPerpendicularVec(this.normalNorm, secondVector);
    }
}