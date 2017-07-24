package com.github.nekomeowww.customdrones.api.geometry;

import java.util.Random;
import net.minecraft.util.math.Vec3d;

public class Segment3d
        extends Line3d
{
    public Vec3d bPoint;

    public Segment3d(Vec3d vec1, Vec3d vec2)
    {
        super(vec1, vec2.subtract(vec1));
        this.bPoint = vec2;
    }

    public Vec3d getRandomPointOnSeg()
    {
        Random rnd = new Random();
        double rndRate = rnd.nextDouble();
        double x = (this.bPoint.xCoord - this.aPoint.xCoord) * rndRate + this.aPoint.xCoord;
        double y = (this.bPoint.yCoord - this.aPoint.yCoord) * rndRate + this.aPoint.yCoord;
        double z = (this.bPoint.zCoord - this.aPoint.zCoord) * rndRate + this.aPoint.zCoord;
        Vec3d vec = new Vec3d(x, y, z);
        return vec;
    }

    public Vec3d intersectWithoutBothEnds(Segment3d seg)
    {
        Vec3d intersect = intersectSegment(seg);
        if ((intersect != null) && (!intersect.equals(this.aPoint)) && (!intersect.equals(this.bPoint)) && (!intersect.equals(seg.aPoint)) &&
                (!intersect.equals(seg.bPoint)))
            return intersect;
        return null;
    }

    public Vec3d intersectWithoutEnds(Line3d line)
    {
        Vec3d intersect = intersect(line);
        if ((intersect != null) && (!intersect.equals(this.aPoint)) && (!intersect.equals(this.bPoint))) return intersect;
        return null;
    }


    public Vec3d intersect(Line3d line)
    {
        Vec3d intersect = super.intersect(line);
        return inBoundBox(intersect) ? intersect : null;
    }

    public Vec3d intersectSegment(Segment3d seg)
    {
        Vec3d intersect = intersect(seg);
        return seg.inBoundBox(intersect) ? intersect : null;
    }

    public boolean onLine(Vec3d vec)
    {
        if ((vec.equals(this.aPoint)) || (vec.equals(this.bPoint))) return true;
        if (inBoundBox(vec))
        {
            return vec.subtract(this.aPoint).crossProduct(this.unit).lengthVector() < 1.0E-18D;
        }
        return false;
    }

    public boolean inBoundBox(Vec3d vec)
    {
        if (vec == null) return false;
        double xmin = Math.min(this.aPoint.xCoord, this.bPoint.xCoord);
        double xmax = Math.max(this.aPoint.xCoord, this.bPoint.xCoord);
        double ymin = Math.min(this.aPoint.yCoord, this.bPoint.yCoord);
        double ymax = Math.max(this.aPoint.yCoord, this.bPoint.yCoord);
        double zmin = Math.min(this.aPoint.zCoord, this.bPoint.zCoord);
        double zmax = Math.max(this.aPoint.zCoord, this.bPoint.zCoord);
        return (vec.xCoord >= xmin) && (vec.xCoord <= xmax) && (vec.yCoord >= ymin) && (vec.yCoord <= ymax) && (vec.zCoord >= zmin) && (vec.zCoord <= zmax);
    }



    public boolean connected(Segment3d seg)
    {
        return (this.aPoint.equals(seg.aPoint)) || (this.aPoint.equals(seg.bPoint)) || (this.bPoint.equals(seg.aPoint)) || (this.bPoint.equals(seg.bPoint));
    }


    public String toString()
    {
        return "A: " + this.aPoint + " - B: " + this.bPoint;
    }


    public boolean equals(Object obj)
    {
        if ((obj instanceof Segment3d))
        {
            Segment3d seg = (Segment3d)obj;

            return ((seg.aPoint.equals(this.aPoint)) && (seg.bPoint.equals(this.bPoint))) || ((seg.aPoint.equals(this.bPoint)) && (seg.bPoint.equals(this.aPoint)));
        }
        return false;
    }
}
