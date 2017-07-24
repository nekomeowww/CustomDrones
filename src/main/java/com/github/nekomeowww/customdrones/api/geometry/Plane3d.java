package com.github.nekomeowww.customdrones.api.geometry;

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
        this.normalNorm = this.normal.normalize();
    }

    public Vec3d intersectInAABB(Line3d line, AxisAlignedBB aabb)
    {
        Vec3d vec = intersect(line);
        if (vec != null)
        {
            if (aabb.isVecInside(vec)) return vec;
        }
        return null;
    }

    public Vec3d intersect(Line3d line)
    {
        if (line.unitNorm.dotProduct(this.normal) == 0.0D) return null;
        Vec3d wVec = line.aPoint.subtract(this.aPoint);
        double unitScale = -this.normalNorm.dotProduct(wVec) / this.normalNorm.dotProduct(line.unitNorm);
        return line.aPoint.add(line.unitNorm.scale(unitScale));
    }


    public Vec3d getAVecOnPlane()
    {
        Vec3d secondVector = this.normalNorm.yCoord == 1.0D ? this.normalNorm.rotatePitch(1.5707964F) : this.normalNorm.rotateYaw(1.5707964F);
        return com.github.nekomeowww.customdrones.api.helpers.VecHelper.getPerpendicularVec(this.normalNorm, secondVector);
    }
}