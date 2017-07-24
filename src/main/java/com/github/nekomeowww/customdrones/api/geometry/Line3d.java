package com.github.nekomeowww.customdrones.api.geometry;

import net.minecraft.util.math.Vec3d;

public class Line3d
{
    public final Vec3d unit;
    public final Vec3d unitNorm;
    public final Vec3d aPoint;

    public Line3d(Vec3d point, Vec3d vec)
    {
        this.unit = vec;
        this.unitNorm = vec.normalize();
        this.aPoint = point;
    }

    public Vec3d getPointFromX(double x)
    {
        double y = this.aPoint.yCoord + (x - this.aPoint.xCoord) * this.unit.yCoord / this.unit.xCoord;
        double z = this.aPoint.zCoord + (x - this.aPoint.xCoord) * this.unit.zCoord / this.unit.xCoord;
        return new Vec3d(x, y, z);
    }

    public Vec3d getPointFromY(double y)
    {
        double x = this.aPoint.xCoord + (y - this.aPoint.yCoord) * this.unit.xCoord / this.unit.yCoord;
        double z = this.aPoint.zCoord + (y - this.aPoint.yCoord) * this.unit.zCoord / this.unit.yCoord;
        return new Vec3d(x, y, z);
    }

    public Vec3d getPointFromZ(double z)
    {
        double x = this.aPoint.xCoord + (z - this.aPoint.zCoord) * this.unit.xCoord / this.unit.zCoord;
        double y = this.aPoint.yCoord + (z - this.aPoint.zCoord) * this.unit.yCoord / this.unit.zCoord;
        return new Vec3d(x, y, z);
    }

    public Vec3d intersect(Line3d line)
    {
        if (this.unit.crossProduct(line.unit).lengthVector() == 0.0D) return null;
        Vec3d leftSide = this.unit.crossProduct(line.unit);
        Vec3d rightSide = line.aPoint.subtract(this.aPoint).crossProduct(line.unit);
        if (Double.compare(leftSide.crossProduct(rightSide).lengthVector(), 0.0D) == 0)
        {
            double a = leftSide.zCoord != 0.0D ? rightSide.zCoord / leftSide.zCoord : leftSide.yCoord != 0.0D ? rightSide.yCoord / leftSide.yCoord : leftSide.xCoord != 0.0D ? rightSide.xCoord / leftSide.xCoord : 0.0D;


            return this.aPoint.add(com.github.nekomeowww.customdrones.api.helpers.VecHelper.scale(this.unit, a));
        }
        return null;
    }


    public String toString()
    {
        return "Vec: " + this.aPoint.toString() + " ; Dir: " + this.unit.toString();
    }
}