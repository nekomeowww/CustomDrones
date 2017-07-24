package com.github.nekomeowww.customdrones.api.helpers;

import com.google.common.base.Predicate;
import java.util.List;
import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.api.geometry.Line3d;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class WorldHelper
{
    public static BlockPos getLowestAir(World world, BlockPos bp)
    {
        for (int a = 0; a < world.getActualHeight(); a++)
        {
            BlockPos bp0 = new BlockPos(bp.getX(), a, bp.getZ());
            if (world.isAirBlock(bp0)) {
                return bp0;
            }
        }
        return bp.toImmutable();
    }

    public static RayTraceResult fullRayTrace(World world, Vec3d start, Vec3d end, Predicate entityFilter)
    {
        return fullRayTrace(world, start, end, false, false, entityFilter);
    }

    public static RayTraceResult fullRayTrace(World world, Vec3d start, Vec3d end, boolean liquid, boolean ignoreNonBound, Predicate entityFilter)
    {
        RayTraceResult mop = world.rayTraceBlocks(start, end, liquid, ignoreNonBound, false);
        Vec3d destination = end;
        if (mop != null) {
            destination = mop.hitVec;
        }
        List<Entity> list = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(start.xCoord, start.yCoord, start.zCoord, destination.xCoord, destination.yCoord, destination.zCoord), entityFilter == null ? EntitySelectors.IS_ALIVE : entityFilter);
        double nearestIntercept;
        if ((list != null) && (!list.isEmpty()))
        {
            nearestIntercept = -1.0D;
            for (Entity e : list)
            {
                RayTraceResult mop1 = e.getEntityBoundingBox().calculateIntercept(start, destination);
                if ((mop1 != null) && (
                        (mop1.hitVec.subtract(start).lengthVector() < nearestIntercept) || (nearestIntercept < 0.0D))) {
                    mop = new RayTraceResult(e, mop1.hitVec);
                }
            }
        }
        return mop;
    }

    public static Entity getEntityBestInAngle(World world, Vec3d ori, Vec3d dir, AxisAlignedBB area, double maxAngle, Entity exclude, Predicate filter)
    {
        Vec3d[] cuts = GeometryHelper.lineCutAABB(area, new Line3d(ori, dir), true);
        if ((cuts[1] != null) || (cuts[0] != null))
        {
            RayTraceResult firstRtr = fullRayTrace(world, ori, cuts[1] != null ? cuts[1] : cuts[0], false, true, filter);
            if ((firstRtr != null) && (firstRtr.entityHit != null)) {
                return firstRtr.entityHit;
            }
        }
        Entity target = null;
        double angle = maxAngle;
        List<Entity> entities = world.getEntitiesInAABBexcluding(exclude, area, filter);
        for (Entity e : entities)
        {
            Vec3d eEye = EntityHelper.getEyeVec(e);
            RayTraceResult rtr = fullRayTrace(world, ori, eEye, false, true, filter);
            if ((rtr == null) || (rtr.typeOfHit == RayTraceResult.Type.MISS) || (rtr.entityHit == e))
            {
                Vec3d toEye = VecHelper.fromTo(ori, eEye);
                double thisAngle = VecHelper.getAngleBetween(toEye, dir) / 3.141592653589793D * 180.0D;
                if (thisAngle <= angle)
                {
                    angle = thisAngle;
                    target = e;
                }
            }
        }
        return target;
    }

    public static Entity getEntityByPersistentUUID(World world, UUID uuid)
    {
        List<Entity> list = world.getLoadedEntityList();
        for (Entity e : list) {
            if (uuid.equals(e.getPersistentID())) {
                return e;
            }
        }
        return null;
    }

    public static Predicate filterDrone = new Predicate()
    {
        public boolean apply(Object input)
        {
            return input instanceof EntityDrone;
        }
    };
}

