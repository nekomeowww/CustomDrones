package com.github.nekomeowww.customdrones.api.helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.geometry.Plane3d;

public class VecHelper
{
    public static Vec3d[] flipAndLoop(Vec3d[] ori, boolean x, boolean y, boolean z)
    {
        return removeDuplicate(combine(new Vec3d[][] { ori, reverse(mirror(ori, x, y, z)) }));
    }

    public static Vec3d[] removeDuplicate(Vec3d[] ori)
    {
        List<Vec3d> positions = new ArrayList();
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d thisPos = ori[a];
            Vec3d toCompare = null;
            if ((a > 0) && (!positions.isEmpty())) {
                if (a == ori.length - 1) {
                    toCompare = (Vec3d)positions.get(0);
                } else {
                    toCompare = (Vec3d)positions.get(positions.size() - 1);
                }
            }
            if (!thisPos.equals(toCompare)) {
                positions.add(thisPos);
            }
        }
        return (Vec3d[])positions.toArray(new Vec3d[0]);
    }

    public static Vec3d[] translate(Vec3d[] ori, Vec3d translate)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d oriVec = ori[a];
            newA[a] = oriVec.add(translate);
        }
        return newA;
    }

    public static Vec3d[] scale(Vec3d[] ori, Vec3d center, double d)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d oriVec = ori[a];
            newA[a] = oriVec.subtract(center).scale(d).add(center);
        }
        return newA;
    }

    public static Vec3d[] scaleAbsolute(Vec3d[] ori, Vec3d center, double d)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d oriVec = ori[a];
            Vec3d oriDelta = oriVec.subtract(center);
            double deltaLength = oriDelta.lengthVector();
            newA[a] = oriDelta.scale((deltaLength - d) / deltaLength).add(center);
        }
        return newA;
    }

    public static Vec3d[] combine(Vec3d[]... vecs)
    {
        int a = 0;
        for (Vec3d[] vecss : vecs) {
            a += vecss.length;
        }
        Vec3d[] total = new Vec3d[a];
        int index = 0;
        for (int b = 0; b < vecs.length; b++)
        {
            Vec3d[] vecss = vecs[b];
            for (int c = 0; c < vecss.length; c++)
            {
                total[index] = vecss[c];
                index++;
            }
        }
        return total;
    }

    public static Vec3d[] mirror(Vec3d[] ori, boolean x, boolean y, boolean z)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < newA.length; a++)
        {
            Vec3d oriVec = ori[a];
            Vec3d newVec = new Vec3d(oriVec.xCoord * (x ? -1 : 1), oriVec.yCoord * (y ? -1 : 1), oriVec.zCoord * (z ? -1 : 1));

            newA[a] = newVec;
        }
        return newA;
    }

    public static Vec3d[] reverse(Vec3d[] ori)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++) {
            newA[(ori.length - a - 1)] = ori[a];
        }
        return newA;
    }

    public static Vec3d getClosest(Vec3d ori, Vec3d[] vecs)
    {
        if (vecs.length > 0)
        {
            Vec3d v0 = vecs[0];
            double distSqr = v0 != null ? ori.squareDistanceTo(v0) : Double.MAX_VALUE;
            for (int a = 0; a < vecs.length; a++)
            {
                Vec3d v1 = vecs[a];
                if (v1 != null)
                {
                    double thisDistSqr = ori.squareDistanceTo(v1);
                    if (thisDistSqr <= distSqr)
                    {
                        v0 = v1;
                        distSqr = thisDistSqr;
                    }
                }
            }
            return v0;
        }
        return null;
    }

    public static Vec3d getFarthest(Vec3d ori, Vec3d[] vecs)
    {
        if (vecs.length > 0)
        {
            Vec3d v0 = vecs[0];
            double distSqr = v0 != null ? ori.squareDistanceTo(v0) : 0.0D;
            for (int a = 0; a < vecs.length; a++)
            {
                Vec3d v1 = vecs[a];
                if (v1 != null)
                {
                    double thisDistSqr = ori.squareDistanceTo(v1);
                    if (thisDistSqr >= distSqr)
                    {
                        v0 = v1;
                        distSqr = thisDistSqr;
                    }
                }
            }
            return v0;
        }
        return null;
    }

    public static Vec3d getMidList(List<Vec3d> vecs)
    {
        Vec3d total = new Vec3d(0.0D, 0.0D, 0.0D);
        if (vecs.size() == 0) {
            return total;
        }
        for (Vec3d vec : vecs) {
            total = total.add(vec);
        }
        return scale(total, 1.0D / vecs.size());
    }

    public static Vec3d getMid(Vec3d... vecs)
    {
        Vec3d total = new Vec3d(0.0D, 0.0D, 0.0D);
        if (vecs.length == 0) {
            return total;
        }
        for (Vec3d vec : vecs) {
            total = total.add(vec);
        }
        return scale(total, 1.0D / vecs.length);
    }

    public static Vec3d getPerpendicularVec(Vec3d vec1, Vec3d vec2)
    {
        Vec3d perp = vec1.crossProduct(vec2);
        if (isZeroVec(perp)) {
            if ((vec1.xCoord == 0.0D) && (vec1.zCoord == 0.0D)) {
                perp = vec1.crossProduct(vec(1.0D, 0.0D, 0.0D));
            } else {
                perp = vec1.crossProduct(vec(0.0D, 1.0D, 0.0D));
            }
        }
        return perp;
    }

    public static double getAngleBetween(Vec3d vec1, Vec3d vec2)
    {
        if ((isZeroVec(vec1)) || (isZeroVec(vec2))) {
            return 0.0D;
        }
        return Math.acos(Math.min(1.0D, vec1.dotProduct(vec2) / (vec1.lengthVector() * vec2.lengthVector())));
    }

    public static boolean isParallel(Vec3d vec1, Vec3d vec2)
    {
        double angle = getAngleBetween(vec1, vec2);
        return (Math.abs(angle) < 1.0E-8D) || (Math.abs(angle - 3.141592653589793D) < 1.0E-15D);
    }

    public static boolean isPerpendicular(Vec3d vec1, Vec3d vec2)
    {
        double angle = getAngleBetween(vec1, vec2);
        return Math.abs(angle) < 1.0E-8D;
    }

    public static Vec3d fromTo(Vec3d vec1, Vec3d vec2)
    {
        return vec2.subtract(vec1);
    }

    public static Vec3d rotateAround(Vec3d init, Vec3d axis, double angleRad)
    {
        Vec3d axisN = axis.normalize();
        Vec3d initN = init;
        Vec3d v1 = initN.crossProduct(axisN);
        Vec3d v2 = axisN.crossProduct(v1);
        v1 = scale(v1, Math.sin(angleRad));
        v2 = scale(v2, Math.cos(angleRad));
        Vec3d shadowRotated = v1.add(v2);
        double dotProduct = axisN.dotProduct(initN);
        return setLength(scale(axisN, dotProduct).add(shadowRotated), init.lengthVector());
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.cos(-yaw * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.sin(-yaw * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.cos(-pitch * 0.017453292F);
        float f3 = MathHelper.sin(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static Vec3d vec(double x, double y, double z)
    {
        return new Vec3d(x, y, z);
    }

    public static Vec3d scale(Vec3d vec, double d)
    {
        return vec(vec.xCoord * d, vec.yCoord * d, vec.zCoord * d);
    }

    public static Vec3d setLength(Vec3d vec, double l)
    {
        return scale(vec, l / vec.lengthVector());
    }

    public static boolean isZeroVec(Vec3d vec)
    {
        return (vec.xCoord == 0.0D) && (vec.yCoord == 0.0D) && (vec.zCoord == 0.0D);
    }

    public static Vec3d jitter(Vec3d ori, double angleRad)
    {
        Random rnd = new Random();
        double oriLength = ori.lengthVector();

        Plane3d plane = new Plane3d(ori, ori);
        double planeVecLength = Math.tan(angleRad) * oriLength * rnd.nextDouble();

        Vec3d fixPlaneVec = plane.getAVecOnPlane().normalize().scale(planeVecLength);

        Vec3d varPlaneVec = isZeroVec(fixPlaneVec) ? Vec3d.ZERO : rotateAround(fixPlaneVec, ori, rnd.nextDouble() * 3.141592653589793D * 2.0D);

        Vec3d newDir = ori.add(varPlaneVec);
        return newDir.normalize().scale(oriLength);
    }
}
