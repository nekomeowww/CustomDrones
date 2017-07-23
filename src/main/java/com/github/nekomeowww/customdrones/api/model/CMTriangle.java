package com.github.nekomeowww.customdrones.api.model;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.geometry.Segment3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class CMTriangle
        extends CMBase
{
    public CMVertex v1;
    public CMVertex v2;
    public CMVertex v3;
    public Color outline;

    public CMTriangle(Vec3d ve1, Vec3d ve2, Vec3d ve3, Vec3d inner)
    {
        this.v1 = new CMVertex(ve1);
        this.v2 = new CMVertex(ve2);
        this.v3 = new CMVertex(ve3);
        orderAndNormal(inner);
    }

    public CMTriangle(CMVertex ve1, CMVertex ve2, CMVertex ve3)
    {
        this.v1 = ve1;
        this.v2 = ve2;
        this.v3 = ve3;
    }

    public CMTriangle orderAndNormal(Vec3d innerPoint)
    {
        Vec3d mid = VecHelper.getMid(new Vec3d[] { this.v1.pos, this.v2.pos, this.v3.pos });
        Vec3d currentNormal = getNormal();
        if (VecHelper.fromTo(innerPoint, mid).func_72430_b(currentNormal) < 0.0D)
        {
            CMVertex v4 = this.v2;
            this.v2 = this.v3;
            this.v3 = v4;
            setNormal(VecHelper.scale(currentNormal, -1.0D));
        }
        else
        {
            setNormal(currentNormal);
        }
        return this;
    }

    public CMTriangle setNormal(Vec3d norm)
    {
        this.v1.normal = norm;
        this.v2.normal = norm;
        this.v3.normal = norm;
        return this;
    }

    public List<Segment3d> segs()
    {
        List<Segment3d> list = new ArrayList();
        list.add(new Segment3d(this.v1.pos, this.v2.pos));
        list.add(new Segment3d(this.v2.pos, this.v3.pos));
        list.add(new Segment3d(this.v3.pos, this.v1.pos));
        return list;
    }

    public Vec3d getNormal()
    {
        return VecHelper.getPerpendicularVec(this.v1.pos.func_178788_d(this.v2.pos), this.v1.pos.func_178788_d(this.v3.pos));
    }

    public void render()
    {
        super.render();
        begin(4);
        this.v1.addToDrawing();
        this.v2.addToDrawing();
        this.v3.addToDrawing();
        end();
        if (this.outline != null)
        {
            GL11.glLineWidth(1.0F);
            color(this.outline.red, this.outline.green, this.outline.blue, this.outline.alpha);
            begin(3);
            this.v1.addToDrawing();
            this.v2.addToDrawing();
            this.v3.addToDrawing();
            end();
            resetColor();
        }
    }

    public void renderReverse()
    {
        begin(4);
        this.v1.addToDrawing();
        this.v3.addToDrawing();
        this.v2.addToDrawing();
        end();
        if (this.outline != null)
        {
            GL11.glLineWidth(1.0F);
            color(this.outline.red, this.outline.green, this.outline.blue, this.outline.alpha);
            begin(3);
            this.v1.addToDrawing();
            this.v3.addToDrawing();
            this.v2.addToDrawing();
            end();
            resetColor();
        }
    }

    public String toString()
    {
        return "{1: " + this.v1 + "}-{2: " + this.v2 + "}-{3: " + this.v3 + "}";
    }
}
