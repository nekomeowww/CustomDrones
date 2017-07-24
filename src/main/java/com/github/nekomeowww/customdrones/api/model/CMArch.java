package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class CMArch
        extends CMBase
{
    public Vec3d end1;
    public Vec3d end2;
    public Vec3d pull1;
    public Vec3d pull2;
    public int segments;
    public double pullMultiply = 0.5D;
    public double rounding = 1.0D;
    public double endMagnetivity = 0.5D;
    public Vec3d[] points;

    public CMArch(Vec3d e1, Vec3d e2, Vec3d p1, Vec3d p2, int segs)
    {
        this.end1 = e1;
        this.end2 = e2;
        this.pull1 = p1;
        this.pull2 = p2;
        this.segments = segs;
        this.points = calculatePoints();
    }

    public void render()
    {
        enableLighting(false);
        lineWidth(3.0D);
        begin(3);
        for (Vec3d v : this.points) {
            vertex(v.xCoord, v.yCoord, v.zCoord, 0.0D, 0.0D);
        }
        end();
    }

    public Vec3d[] calculatePoints()
    {
        Vec3d[] list = new Vec3d[this.segments + 1];
        double pull1Length = this.pull1.lengthVector();
        double pull2Length = this.pull2.lengthVector();
        double pull1DifRatio = Math.pow((pull1Length + pull2Length) / Math.pow(pull2Length, this.rounding), 1.0D);
        double pull2DifRatio = Math.pow((pull1Length + pull2Length) / Math.pow(pull1Length, this.rounding), 1.0D);
        Vec3d baseLine = fromTo(this.end1, this.end2);
        double baseLineLength = baseLine.lengthVector();
        Vec3d baseLineStep = setLength(baseLine, baseLineLength / this.segments);
        for (int a = 0; a <= this.segments; a++)
        {
            Vec3d thisBasePoint = this.end1.add(scale(baseLineStep, a));
            double thisStep = a / this.segments;

            double pull1Strength = 1.0D - thisStep * (pull1DifRatio + 1.0D);
            if (pull1Strength <= 0.0D) {
                pull1Strength /= pull1DifRatio;
            }
            pull1Strength *= 1.5707963267948966D;
            double pull2Strength = 1.0D - (1.0D - thisStep) * (pull2DifRatio + 1.0D);
            if (pull2Strength < 0.0D) {
                pull2Strength /= pull2DifRatio;
            }
            pull2Strength *= 1.5707963267948966D;
            double pull1Mult = this.pullMultiply * Math.pow(Math.cos(pull1Strength), this.endMagnetivity);
            double pull2Mult = this.pullMultiply * Math.pow(Math.cos(pull2Strength), this.endMagnetivity);
            Vec3d thisArchPoint = thisBasePoint.add(scale(this.pull1, pull1Mult)).add(scale(this.pull2, pull2Mult));
            list[a] = thisArchPoint;
        }
        return list;
    }

    public double totalSegmentLength()
    {
        double l = 0.0D;
        for (int a = 0; a < this.points.length - 1; a++) {
            l += fromTo(this.points[a], this.points[(a + 1)]).lengthVector();
        }
        return l;
    }

    public Vec3d getPointAtBaseRate(double d)
    {
        if ((d < 0.0D) || (d > 1.0D)) {
            return null;
        }
        double segmentPosition = d * this.segments;
        int prevSegment = (int)Math.floor(segmentPosition);
        double segMod = segmentPosition - prevSegment;
        Vec3d prevVec = this.points[prevSegment];
        Vec3d nextVec = this.points[(prevSegment + 1)];
        Vec3d prevToNext = fromTo(prevVec, nextVec);
        return prevVec.add(scale(prevToNext, segMod));
    }

    public Vec3d getPointAtLengthRate(double d)
    {
        double totalLength = totalSegmentLength();
        double neededLength = d * totalLength;

        double currentLength = 0.0D;
        for (int a = 0; a < this.points.length - 1; a++)
        {
            Vec3d thisVec = this.points[a];
            Vec3d nextVec = this.points[(a + 1)];
            Vec3d thisToNext = fromTo(thisVec, nextVec);
            double thisLength = thisToNext.lengthVector();
            if (currentLength == neededLength) {
                return thisVec;
            }
            if (currentLength + thisLength == neededLength) {
                return nextVec;
            }
            if (currentLength + thisLength > neededLength)
            {
                double exceedLength = neededLength - currentLength;
                double exceedRatio = exceedLength / thisLength;
                return thisVec.add(scale(thisToNext, exceedRatio));
            }
            currentLength += thisLength;
        }
        return null;
    }
}
