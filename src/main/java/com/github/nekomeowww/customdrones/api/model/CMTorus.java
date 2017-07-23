package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.math.Vec3d;

public class CMTorus
        extends CMPipeLine
{
    public int segments;
    public double initRot;
    public double torusRa;
    public Vec3d origin;
    public Vec3d normal;

    public CMTorus(int s, int segs, double ra, double thick, double rot, Vec3d ori, Vec3d nor)
    {
        this.sides = s;
        this.segments = segs;
        this.torusRa = ra;
        this.radi = filledDouble(thick, segs + 1);
        this.initRot = rot;
        this.origin = (ori == null ? vec(0.0D, 0.0D, 0.0D) : ori);
        this.normal = ((nor == null) || (isZeroVec(nor)) ? vec(0.0D, 1.0D, 0.0D) : nor.func_72432_b());
        fullCalculate();
    }

    public void fullCalculate()
    {
        this.positions = calculatePositions();
        this.endCapsDirection[0] = fromTo(this.positions[0], this.positions[1])
                .func_178787_e(fromTo(this.positions[(this.positions.length - 2)], this.positions[(this.positions.length - 1)]));
        this.endCapsDirection[1] = this.endCapsDirection[0];
        this.pipes = calculatePipes();
        for (CMPipe pipe : this.pipes) {
            pipe.textureHasCaps = false;
        }
    }

    public Vec3d[] calculatePositions()
    {
        Vec3d[] pos = new Vec3d[this.segments + 1];
        Vec3d baseRot = setLength(rotateAround(
                getPerpendicularVec(this.normal, (this.normal.field_72450_a == 0.0D) && (this.normal.field_72449_c == 0.0D) ? vec(0.0D, 0.0D, 1.0D) : vec(0.0D, 1.0D, 0.0D)), this.normal, this.initRot), this.torusRa);

        double rotAngle = 6.283185307179586D / this.segments;
        for (int a = 0; a < pos.length; a++) {
            pos[a] = rotateAround(baseRot, this.normal, rotAngle * a).func_178787_e(this.origin);
        }
        return pos;
    }

    public CapType renderCaps(CMPipe pipe, int pipeIndex)
    {
        return CapType.NONE;
    }
}
