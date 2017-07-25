package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class CMCurve
        extends CMBase
{
    public int segments;
    public Vec3d[] vecs;
    public double pullMult = 0.2D;
    public CMArch[] arches;

    public CMCurve(boolean autoPull, int segmentsPerArch, Vec3d[] vec3s)
    {
        this.segments = segmentsPerArch;
        this.vecs = (autoPull ? autoCalculatePull(vec3s) : vec3s);
        this.arches = calculateArches();
    }

    public Vec3d[] autoCalculatePull(Vec3d[] vec3s)
    {
        if ((vec3s == null) || (vec3s.length == 0)) {
            return new Vec3d[0];
        }
        Vec3d[] vecs = new Vec3d[vec3s.length * 2];
        if (vec3s.length == 1)
        {
            vecs[1] = vec3s[0];
        }
        else if (vec3s.length == 2)
        {
            vecs[1] = fromTo(vec3s[0], vec3s[1]);
            vecs[3] = fromTo(vec3s[1], vec3s[0]);
        }
        else
        {
            for (int a = 1; a < vec3s.length - 1; a++)
            {
                Vec3d thisVec = vec3s[a];
                Vec3d prevVec = vec3s[(a - 1)];
                Vec3d nextVec = vec3s[(a + 1)];
                Vec3d pull = scale(fromTo(prevVec, thisVec).add(fromTo(nextVec, thisVec)), this.pullMult);
                vecs[(a * 2)] = thisVec;
                vecs[(a * 2 + 1)] = pull;
            }
            Vec3d firstVec = vec3s[0];
            vecs[0] = firstVec;
            Vec3d secondVec = vec3s[1];
            Vec3d secondPull = vecs[3];
            Vec3d firstMid = scale(firstVec.add(secondVec), 0.5D);
            Vec3d firstToSecond = fromTo(firstVec, secondVec);
            if (secondPull.dotProduct(firstToSecond) == 0.0D)
            {
                vecs[1] = secondPull;
            }
            else
            {
                double d = fromTo(secondVec, firstMid).dotProduct(firstToSecond) / firstToSecond.dotProduct(secondPull);
                Vec3d intersect = secondVec.add(scale(secondPull, d));
                Vec3d firstPull = setLength(fromTo(firstVec, intersect), secondPull.lengthVector());
                if (firstPull.dotProduct(fromTo(firstVec, intersect)) * secondPull.dotProduct(fromTo(secondVec, intersect)) < 0.0D) {
                    firstPull = scale(firstPull, -1.0D);
                }
                vecs[1] = firstPull;
            }
            Vec3d lastVec = vec3s[(vec3s.length - 1)];
            vecs[(vecs.length - 2)] = lastVec;
            Vec3d secondlastVec = vec3s[(vec3s.length - 2)];
            Vec3d secondLastPull = vecs[(vec3s.length * 2 - 3)];
            Vec3d lastMid = scale(lastVec.add(secondlastVec), 0.5D);
            Vec3d lastToSecond = fromTo(firstVec, secondVec);
            if (secondLastPull.dotProduct(lastToSecond) == 0.0D)
            {
                vecs[(vecs.length - 1)] = secondLastPull;
            }
            else
            {
                double d = fromTo(secondlastVec, lastMid).dotProduct(lastToSecond) / lastToSecond.dotProduct(secondLastPull);
                Vec3d intersect = secondlastVec.add(scale(secondLastPull, d));
                Vec3d lastPull = setLength(fromTo(lastVec, intersect), secondLastPull.lengthVector());
                if (lastPull.dotProduct(fromTo(lastVec, intersect)) * secondLastPull.dotProduct(fromTo(secondlastVec, intersect)) > 0.0D) {
                    lastPull = scale(lastPull, -1.0D);
                }
                vecs[(vecs.length - 1)] = lastPull;
            }
        }
        return vecs;
    }

    public void render()
    {
        for (CMArch arch : this.arches) {
            arch.render();
        }
    }

    public CMArch[] calculateArches()
    {
        int archCount = (this.vecs.length - 2) / 2;
        CMArch[] arch = new CMArch[archCount];
        for (int a = 0; a < archCount; a++)
        {
            Vec3d end1 = this.vecs[(a * 2)];
            Vec3d end1Pull = scale(this.vecs[(a * 2 + 1)], a == 0 ? 1.0D : -1.0D);
            Vec3d end2 = this.vecs[((a + 1) * 2)];
            Vec3d end2Pull = this.vecs[((a + 1) * 2 + 1)];
            CMArch anarch = new CMArch(end1, end2, end1Pull, end2Pull, this.segments);
            arch[a] = anarch;
        }
        return arch;
    }

    public double getTotalLength(boolean arched)
    {
        double length = 0.0D;
        //
        int a;
        CMArch[] b;
        Vec3d thisVec;
        Vec3d nextVec;
        if (!arched)
        {
            for (a = 0; a < this.vecs.length - 3; a += 2)
            {
                thisVec = this.vecs[a];
                nextVec = this.vecs[(a + 2)];
                length += fromTo(thisVec, nextVec).lengthVector();
            }
        }
        /*
        else
        {
            b = this.arches;thisVec = b.length;
            for (nextVec = 0; nextVec < thisVec; nextVec++)
            {
                CMArch arch = b[nextVec];

                length += arch.totalSegmentLength();
            }
        }
        */
        return length;
    }

    public Vec3d getPointAtBaseRate(double d)
    {
        double totalLength = getTotalLength(false);
        double neededLength = totalLength * d;
        double currentLength = 0.0D;
        for (int a = 0; a < this.vecs.length - 3; a += 2)
        {
            Vec3d thisVec = this.vecs[a];
            Vec3d nextVec = this.vecs[(a + 2)];
            Vec3d thisToNextVec = fromTo(thisVec, nextVec);
            double nextLength = thisToNextVec.lengthVector();
            if (currentLength == neededLength) {
                return thisVec;
            }
            if (currentLength + nextLength == neededLength) {
                return nextVec;
            }
            if (currentLength + nextLength > neededLength)
            {
                double exceedLength = neededLength - currentLength;
                double exceedRatio = exceedLength / nextLength;
                CMArch thisArch = this.arches[(a / 2)];
                return thisArch.getPointAtBaseRate(exceedRatio);
            }
            currentLength += nextLength;
        }
        return null;
    }

    public Vec3d getPointAtLengthRate(double d)
    {
        double totalLength = getTotalLength(true);
        double neededLength = totalLength * d;
        double currentLength = 0.0D;
        for (int a = 0; a < this.arches.length; a++)
        {
            CMArch thisArch = this.arches[a];
            double thisLength = thisArch.totalSegmentLength();
            if (currentLength == neededLength) {
                return thisArch.end1;
            }
            if (currentLength + thisLength == neededLength) {
                return thisArch.end2;
            }
            if (currentLength + thisLength > neededLength)
            {
                double exceedLength = neededLength - currentLength;
                double exceedRatio = exceedLength / thisLength;
                return thisArch.getPointAtLengthRate(exceedRatio);
            }
            currentLength += thisLength;
        }
        return null;
    }
}
