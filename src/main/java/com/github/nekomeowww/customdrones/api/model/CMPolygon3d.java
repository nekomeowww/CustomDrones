package com.github.nekomeowww.customdrones.api.model;

import java.util.List;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class CMPolygon3d
        extends CMPolygon
{
    public Vec3d skew = vec0;
    public List<Vec3d> frontFaceUVs;
    public List<Vec3d> backFaceUVs;
    public List<Vec3d> sideUVs;
    public Color frontFaceColor;
    public Color backFaceColor;

    public CMPolygon3d(Vec3d[] pos, Vec3d nor, Vec3d ske, List<Vec3d>... faceuvs)
    {
        super(pos, null, nor);
        this.skew = ske;
        if (faceuvs.length > 0) {
            this.frontFaceUVs = faceuvs[0];
        }
        if (faceuvs.length > 1) {
            this.backFaceUVs = faceuvs[1];
        }
        if (faceuvs.length > 2) {
            this.sideUVs = faceuvs[2];
        }
    }

    public CMPolygon3d setColors(Color c1, Color c2, Color c3)
    {
        this.frontFaceColor = c1;
        this.color = c2;
        this.backFaceColor = c3;
        return this;
    }

    public void render()
    {
        this.renderFront = true;
        this.renderBack = false;
        this.uvs = this.frontFaceUVs;
        if (this.frontFaceColor != null) {
            applyColorOrTexture(this.texture, this.frontFaceColor);
        }
        super.render();

        Vec3d polyCenter = VecHelper.getMid((Vec3d[])this.positions.toArray(new Vec3d[0]));
        this.uvs = this.sideUVs;
        applyColorOrTexture(this.texture, this.color);
        if (this.positions.size() > 0)
        {
            begin(8);
            for (int a = 0; a <= this.positions.size(); a++)
            {
                Vec3d pos = (Vec3d)this.positions.get(a == this.positions.size() ? 0 : a);
                Vec3d uv1 = (this.uvs == null) || (!shouldApplyTexture()) ? vec0 : (Vec3d)this.uvs.get(Math.min(a * 2, this.uvs.size() - 1));
                Vec3d uv2 = (this.uvs == null) || (!shouldApplyTexture()) ? vec0 : (Vec3d)this.uvs.get(Math.min(a * 2 + 1, this.uvs.size() - 1));

                Vec3d sideNormal = VecHelper.fromTo(polyCenter, pos).normalize();
                double skewAffectNormal = sideNormal.dotProduct(this.skew);
                if (skewAffectNormal != 0.0D) {
                    skewAffectNormal /= Math.abs(skewAffectNormal);
                }
                sideNormal = sideNormal.add(VecHelper.scale(this.skew, -skewAffectNormal)).normalize();

                normal(sideNormal.xCoord, sideNormal.yCoord, sideNormal.zCoord);
                vertex(pos.xCoord, pos.yCoord, pos.zCoord, uv1.xCoord, uv1.yCoord);
                pos = pos.add(this.skew);
                vertex(pos.xCoord, pos.yCoord, pos.zCoord, uv2.xCoord, uv2.yCoord);
            }
            end();
        }
        translate(this.skew.xCoord, this.skew.yCoord, this.skew.zCoord);
        this.renderFront = false;
        this.renderBack = true;
        this.uvs = this.backFaceUVs;
        if (this.backFaceColor != null) {
            applyColorOrTexture(this.texture, this.backFaceColor);
        }
        super.render();
    }
}
