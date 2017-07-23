package com.github.nekomeowww.customdrones.api.path;

import net.minecraft.util.math.Vec3d;

public class Node
{
    public final double x;
    public final double y;
    public final double z;

    public Node(double i, double j, double k)
    {
        this.x = i;
        this.y = j;
        this.z = k;
    }

    public Vec3d toVec()
    {
        return new Vec3d(this.x, this.y, this.z);
    }

    public Vec3d toIVec()
    {
        return new Vec3d(intX(), intY(), intZ());
    }

    public int intX()
    {
        return (int)Math.floor(this.x);
    }

    public int intY()
    {
        return (int)Math.floor(this.y);
    }

    public int intZ()
    {
        return (int)Math.floor(this.z);
    }

    public double distTo(Node p2)
    {
        return Math.sqrt(distSqrTo(p2));
    }

    public double distTo(double i, double j, double k)
    {
        return Math.sqrt(distSqrTo(i, j, k));
    }

    public double distSqrTo(Node p2)
    {
        return distSqrTo(p2.x, p2.y, p2.z);
    }

    public double distSqrTo(double i, double j, double k)
    {
        return (this.x - i) * (this.x - i) + (this.y - j) * (this.y - j) + (this.z - k) * (this.z - k);
    }

    public boolean equals(Object obj)
    {
        return ((obj instanceof Node)) && (((Node)obj).x == this.x) && (((Node)obj).y == this.y) && (((Node)obj).z == this.z);
    }
}
