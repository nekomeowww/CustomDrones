package com.github.nekomeowww.customdrones.api.geometry;

import java.util.Random;
import net.minecraft.util.math.Vec3d;

public class Segment3d
        extends Line3d
{
    public Vec3d bPoint;

    public Segment3d(Vec3d vec1, Vec3d vec2)
    {
        super(vec1, vec2.func_178788_d(vec1));
        this.bPoint = vec2;
    }

    public Vec3d getRandomPointOnSeg()
    {
        Random rnd = new Random();
        double rndRate = rnd.nextDouble();
        double x = (this.bPoint.field_72450_a - this.aPoint.field_72450_a) * rndRate + this.aPoint.field_72450_a;
        double y = (this.bPoint.field_72448_b - this.aPoint.field_72448_b) * rndRate + this.aPoint.field_72448_b;
        double z = (this.bPoint.field_72449_c - this.aPoint.field_72449_c) * rndRate + this.aPoint.field_72449_c;
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
            return vec.func_178788_d(this.aPoint).func_72431_c(this.unit).func_72433_c() < 1.0E-18D;
        }
        return false;
    }

    public boolean inBoundBox(Vec3d vec)
    {
        if (vec == null) return false;
        double xmin = Math.min(this.aPoint.field_72450_a, this.bPoint.field_72450_a);
        double xmax = Math.max(this.aPoint.field_72450_a, this.bPoint.field_72450_a);
        double ymin = Math.min(this.aPoint.field_72448_b, this.bPoint.field_72448_b);
        double ymax = Math.max(this.aPoint.field_72448_b, this.bPoint.field_72448_b);
        double zmin = Math.min(this.aPoint.field_72449_c, this.bPoint.field_72449_c);
        double zmax = Math.max(this.aPoint.field_72449_c, this.bPoint.field_72449_c);
        return (vec.field_72450_a >= xmin) && (vec.field_72450_a <= xmax) && (vec.field_72448_b >= ymin) && (vec.field_72448_b <= ymax) && (vec.field_72449_c >= zmin) && (vec.field_72449_c <= zmax);
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
