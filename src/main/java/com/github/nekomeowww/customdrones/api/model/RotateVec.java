package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class RotateVec
{
    public Vec3d vec;
    public double rotation;

    public RotateVec(double rot, Vec3d v)
    {
        this.rotation = rot;
        this.vec = v;
    }

    public RotateVec(double rot, double x, double y, double z)
    {
        this(rot, new Vec3d(x, y, z));
    }
}