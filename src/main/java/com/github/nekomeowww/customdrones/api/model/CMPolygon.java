package com.github.nekomeowww.customdrones.api.model;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.geometry.Line3d;
import com.github.nekomeowww.customdrones.api.geometry.Segment3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class CMPolygon
        extends CMBase
{
    public List<Vec3d> positions;
    public List<Vec3d> uvs;
    public Vec3d normal;
    public boolean renderFront = true;
    public boolean renderBack = true;
    public List<CMTriangle> triangles = new ArrayList();

    public CMPolygon(Vec3d[] pos, Vec3d[] uv, Vec3d nor)
    {
        this.positions = new ArrayList();
        this.uvs = new ArrayList();
        for (int a = 0; a < pos.length; a++)
        {
            Vec3d thisPos = pos[a];
            Vec3d toCompare = null;
            if ((a > 0) && (!this.positions.isEmpty())) {
                if (a == pos.length - 1) {
                    toCompare = (Vec3d)this.positions.get(0);
                } else {
                    toCompare = (Vec3d)this.positions.get(this.positions.size() - 1);
                }
            }
            if (!thisPos.equals(toCompare))
            {
                this.positions.add(thisPos);
                if ((uv != null) && (a < uv.length)) {
                    this.uvs.add(uv[a]);
                }
            }
        }
        this.normal = nor;
        triangulate();
    }

    public CMVertex generateVertex(Vec3d v1)
    {
        int index = this.positions.indexOf(v1);
        Vec3d uv1 = (this.uvs != null) && (index < this.uvs.size()) ? (Vec3d)this.uvs.get(index) : null;
        CMVertex vert1 = new CMVertex(v1, uv1, this.normal);
        return vert1;
    }

    public void triangulate()
    {
        List<Vec3d> posLeft = new ArrayList(this.positions);
        Vec3d startVec = (Vec3d)posLeft.get(0);
        while (posLeft.size() >= 3)
        {
            CMTriangle triangle = firstTriangle(posLeft, startVec, 0);
            if (triangle == null) {
                break;
            }
            this.triangles.add(triangle);
        }
    }

    public CMTriangle firstTriangle(List<Vec3d> poses, Vec3d startFrom, int loopcount)
    {
        if ((poses.size() < 3) || (loopcount > Math.pow(poses.size(), 2.0D))) {
            return null;
        }
        if (poses.size() == 3)
        {
            CMTriangle triangle = new CMTriangle(generateVertex((Vec3d)poses.get(0)), generateVertex((Vec3d)poses.get(1)), generateVertex((Vec3d)poses.get(2))).orderAndNormal(VecHelper.getMid(new Vec3d[] { (Vec3d)poses.get(0), (Vec3d)poses.get(1), (Vec3d)poses.get(2) }));
            poses.clear();
            return triangle;
        }
        int index = poses.indexOf(startFrom);
        Vec3d next1 = (Vec3d)poses.get((index + 1) % poses.size());
        Vec3d next2 = (Vec3d)poses.get((index + 2) % poses.size());

        CMTriangle triangle = new CMTriangle(generateVertex(startFrom), generateVertex(next1), generateVertex(next2));
        if (!trianglePartOutsidePoly(triangle, this.positions))
        {
            poses.remove(next1);
            return triangle;
        }
        return firstTriangle(poses, next1, loopcount + 1);
    }

    public boolean trianglePartOutsidePoly(CMTriangle triangle, List<Vec3d> poses)
    {
        List<Segment3d> triangleSegs = triangle.segs();
        List<Segment3d> segs = toSegments(poses);
        for (int a = 0; a < triangleSegs.size(); a++)
        {
            Segment3d tseg = (Segment3d)triangleSegs.get(a);
            int cutCount = 0;
            boolean isTriSegOnPoly = false;
            for (Segment3d seg : segs)
            {
                if (!tseg.connected(seg))
                {
                    Vec3d intersect = tseg.intersectSegment(seg);
                    if (intersect != null) {
                        cutCount++;
                    }
                }
                if (tseg.equals(seg)) {
                    isTriSegOnPoly = true;
                }
            }
            if (cutCount > 0) {
                return true;
            }
            if (!isTriSegOnPoly)
            {
                Vec3d testPoint = VecHelper.getMid(new Vec3d[] { tseg.aPoint, tseg.bPoint });
                if (isPointOutSide(testPoint, segs)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<Segment3d> toSegments(List<Vec3d> poses)
    {
        List<Segment3d> segs = new ArrayList();
        for (int a = 0; a < poses.size(); a++)
        {
            Vec3d v1 = (Vec3d)poses.get(a);
            Vec3d v2 = (Vec3d)poses.get((a + 1) % poses.size());
            segs.add(new Segment3d(v1, v2));
        }
        return segs;
    }

    public boolean isPointOutSide(Vec3d p, List<Segment3d> sides)
    {
        int outsideCount = 0;
        for (int a = 0; a < sides.size(); a++)
        {
            int cut = 0;
            Segment3d testSeg = (Segment3d)sides.get(a);
            Vec3d testEnd = VecHelper.getMid(new Vec3d[] { testSeg.aPoint, testSeg.bPoint });
            Line3d testLine = new Line3d(p, testEnd.func_178788_d(p));
            for (Segment3d side : sides)
            {
                if (side.onLine(p)) {
                    return false;
                }
                Vec3d intersect = side.intersect(testLine);
                if ((intersect != null) && (!intersect.equals(p)) && (intersect.func_178788_d(p).func_72430_b(testLine.unit) > 0.0D)) {
                    cut++;
                }
            }
            outsideCount += (cut % 2 == 0 ? 1 : -1);
        }
        return outsideCount > 0;
    }

    public void render()
    {
        if (this.renderFront) {
            for (CMTriangle triangle : this.triangles) {
                triangle.render();
            }
        }
        if (this.renderBack) {
            for (CMTriangle triangle : this.triangles) {
                triangle.renderReverse();
            }
        }
    }
}
