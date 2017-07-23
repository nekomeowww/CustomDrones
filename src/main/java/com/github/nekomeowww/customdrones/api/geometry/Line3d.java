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
        this.unitNorm = vec.func_72432_b();
        this.aPoint = point;
    }

    public Vec3d getPointFromX(double x)
    {
        double y = this.aPoint.field_72448_b + (x - this.aPoint.field_72450_a) * this.unit.field_72448_b / this.unit.field_72450_a;
        double z = this.aPoint.field_72449_c + (x - this.aPoint.field_72450_a) * this.unit.field_72449_c / this.unit.field_72450_a;
        return new Vec3d(x, y, z);
    }

    public Vec3d getPointFromY(double y)
    {
        double x = this.aPoint.field_72450_a + (y - this.aPoint.field_72448_b) * this.unit.field_72450_a / this.unit.field_72448_b;
        double z = this.aPoint.field_72449_c + (y - this.aPoint.field_72448_b) * this.unit.field_72449_c / this.unit.field_72448_b;
        return new Vec3d(x, y, z);
    }

    public Vec3d getPointFromZ(double z)
    {
        double x = this.aPoint.field_72450_a + (z - this.aPoint.field_72449_c) * this.unit.field_72450_a / this.unit.field_72449_c;
        double y = this.aPoint.field_72448_b + (z - this.aPoint.field_72449_c) * this.unit.field_72448_b / this.unit.field_72449_c;
        return new Vec3d(x, y, z);
    }

    public Vec3d intersect(Line3d line)
    {
        if (this.unit.func_72431_c(line.unit).func_72433_c() == 0.0D) return null;
        Vec3d leftSide = this.unit.func_72431_c(line.unit);
        Vec3d rightSide = line.aPoint.func_178788_d(this.aPoint).func_72431_c(line.unit);
        if (Double.compare(leftSide.func_72431_c(rightSide).func_72433_c(), 0.0D) == 0)
        {
            double a = leftSide.field_72449_c != 0.0D ? rightSide.field_72449_c / leftSide.field_72449_c : leftSide.field_72448_b != 0.0D ? rightSide.field_72448_b / leftSide.field_72448_b : leftSide.field_72450_a != 0.0D ? rightSide.field_72450_a / leftSide.field_72450_a : 0.0D;


            return this.aPoint.func_178787_e(williamle.drones.api.helpers.VecHelper.scale(this.unit, a));
        }
        return null;
    }


    public String toString()
    {
        return "Vec: " + this.aPoint.toString() + " ; Dir: " + this.unit.toString();
    }
}