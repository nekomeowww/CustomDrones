package com.github.nekomeowww.customdrones.api.helpers;

import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.geometry.Line3d;
import com.github.nekomeowww.customdrones.api.geometry.Plane3d;

public class GeometryHelper
{
    public static Vec3d[] lineCutAABB(AxisAlignedBB aabb, Line3d l, boolean sameSideWithDir)
    {
        Plane3d pminX = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(1.0D, 0.0D, 0.0D));
        Plane3d pminY = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(0.0D, 1.0D, 0.0D));
        Plane3d pminZ = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72339_c), vec(0.0D, 0.0D, 1.0D));
        Plane3d pmaxX = new Plane3d(vec(aabb.field_72336_d, aabb.field_72338_b, aabb.field_72339_c), vec(-1.0D, 0.0D, 0.0D));
        Plane3d pmaxY = new Plane3d(vec(aabb.field_72340_a, aabb.field_72337_e, aabb.field_72339_c), vec(0.0D, -1.0D, 0.0D));
        Plane3d pmaxZ = new Plane3d(vec(aabb.field_72340_a, aabb.field_72338_b, aabb.field_72334_f), vec(0.0D, 0.0D, -1.0D));
        Vec3d iminX = pminX.intersectInAABB(l, aabb);
        Vec3d imaxX = pmaxX.intersectInAABB(l, aabb);
        Vec3d iminY = pminY.intersectInAABB(l, aabb);
        Vec3d imaxY = pmaxY.intersectInAABB(l, aabb);
        Vec3d iminZ = pminZ.intersectInAABB(l, aabb);
        Vec3d imaxZ = pmaxZ.intersectInAABB(l, aabb);
        Vec3d[] allIntersects = { iminX, imaxX, iminY, imaxY, iminZ, imaxZ };
        Vec3d closest = VecHelper.getClosest(l.aPoint, allIntersects);
        Vec3d farthest = VecHelper.getFarthest(l.aPoint, allIntersects);
        return new Vec3d[] { closest, farthest };
    }

    public static Vec3d vec(double x, double y, double z)
    {
        return new Vec3d(x, y, z);
    }
}
