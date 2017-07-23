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
            newA[a] = oriVec.func_178787_e(translate);
        }
        return newA;
    }

    public static Vec3d[] scale(Vec3d[] ori, Vec3d center, double d)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d oriVec = ori[a];
            newA[a] = oriVec.func_178788_d(center).func_186678_a(d).func_178787_e(center);
        }
        return newA;
    }

    public static Vec3d[] scaleAbsolute(Vec3d[] ori, Vec3d center, double d)
    {
        Vec3d[] newA = new Vec3d[ori.length];
        for (int a = 0; a < ori.length; a++)
        {
            Vec3d oriVec = ori[a];
            Vec3d oriDelta = oriVec.func_178788_d(center);
            double deltaLength = oriDelta.func_72433_c();
            newA[a] = oriDelta.func_186678_a((deltaLength - d) / deltaLength).func_178787_e(center);
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
            Vec3d newVec = new Vec3d(oriVec.field_72450_a * (x ? -1 : 1), oriVec.field_72448_b * (y ? -1 : 1), oriVec.field_72449_c * (z ? -1 : 1));

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
            double distSqr = v0 != null ? ori.func_72436_e(v0) : Double.MAX_VALUE;
            for (int a = 0; a < vecs.length; a++)
            {
                Vec3d v1 = vecs[a];
                if (v1 != null)
                {
                    double thisDistSqr = ori.func_72436_e(v1);
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
            double distSqr = v0 != null ? ori.func_72436_e(v0) : 0.0D;
            for (int a = 0; a < vecs.length; a++)
            {
                Vec3d v1 = vecs[a];
                if (v1 != null)
                {
                    double thisDistSqr = ori.func_72436_e(v1);
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
            total = total.func_178787_e(vec);
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
            total = total.func_178787_e(vec);
        }
        return scale(total, 1.0D / vecs.length);
    }

    public static Vec3d getPerpendicularVec(Vec3d vec1, Vec3d vec2)
    {
        Vec3d perp = vec1.func_72431_c(vec2);
        if (isZeroVec(perp)) {
            if ((vec1.field_72450_a == 0.0D) && (vec1.field_72449_c == 0.0D)) {
                perp = vec1.func_72431_c(vec(1.0D, 0.0D, 0.0D));
            } else {
                perp = vec1.func_72431_c(vec(0.0D, 1.0D, 0.0D));
            }
        }
        return perp;
    }

    public static double getAngleBetween(Vec3d vec1, Vec3d vec2)
    {
        if ((isZeroVec(vec1)) || (isZeroVec(vec2))) {
            return 0.0D;
        }
        return Math.acos(Math.min(1.0D, vec1.func_72430_b(vec2) / (vec1.func_72433_c() * vec2.func_72433_c())));
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
        return vec2.func_178788_d(vec1);
    }

    public static Vec3d rotateAround(Vec3d init, Vec3d axis, double angleRad)
    {
        Vec3d axisN = axis.func_72432_b();
        Vec3d initN = init;
        Vec3d v1 = initN.func_72431_c(axisN);
        Vec3d v2 = axisN.func_72431_c(v1);
        v1 = scale(v1, Math.sin(angleRad));
        v2 = scale(v2, Math.cos(angleRad));
        Vec3d shadowRotated = v1.func_178787_e(v2);
        double dotProduct = axisN.func_72430_b(initN);
        return setLength(scale(axisN, dotProduct).func_178787_e(shadowRotated), init.func_72433_c());
    }

    public static Vec3d getVectorForRotation(float pitch, float yaw)
    {
        float f = MathHelper.func_76134_b(-yaw * 0.017453292F - 3.1415927F);
        float f1 = MathHelper.func_76126_a(-yaw * 0.017453292F - 3.1415927F);
        float f2 = -MathHelper.func_76134_b(-pitch * 0.017453292F);
        float f3 = MathHelper.func_76126_a(-pitch * 0.017453292F);
        return new Vec3d(f1 * f2, f3, f * f2);
    }

    public static Vec3d vec(double x, double y, double z)
    {
        return new Vec3d(x, y, z);
    }

    public static Vec3d scale(Vec3d vec, double d)
    {
        return vec(vec.field_72450_a * d, vec.field_72448_b * d, vec.field_72449_c * d);
    }

    public static Vec3d setLength(Vec3d vec, double l)
    {
        return scale(vec, l / vec.func_72433_c());
    }

    public static boolean isZeroVec(Vec3d vec)
    {
        return (vec.field_72450_a == 0.0D) && (vec.field_72448_b == 0.0D) && (vec.field_72449_c == 0.0D);
    }

    public static Vec3d jitter(Vec3d ori, double angleRad)
    {
        Random rnd = new Random();
        double oriLength = ori.func_72433_c();

        Plane3d plane = new Plane3d(ori, ori);
        double planeVecLength = Math.tan(angleRad) * oriLength * rnd.nextDouble();

        Vec3d fixPlaneVec = plane.getAVecOnPlane().func_72432_b().func_186678_a(planeVecLength);

        Vec3d varPlaneVec = isZeroVec(fixPlaneVec) ? Vec3d.field_186680_a : rotateAround(fixPlaneVec, ori, rnd.nextDouble() * 3.141592653589793D * 2.0D);

        Vec3d newDir = ori.func_178787_e(varPlaneVec);
        return newDir.func_72432_b().func_186678_a(oriLength);
    }
}
