package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;

public class CMPipeLine
        extends CMBase
{
    public int sides;
    public double[] radi;
    public Vec3d[] positions;
    public Vec3d[] endCapsDirection = new Vec3d[2];
    public CMPipe[] pipes;

    public CMPipeLine() {}

    public CMPipeLine(int s, double radius, Vec3d[] vecs, Vec3d[] endCapsDir)
    {
        this(s, filledDouble(radius, vecs.length), vecs, endCapsDir);
    }

    public CMPipeLine(int s, double[] radius, Vec3d[] vecs, Vec3d[] endCapsDir)
    {
        this.sides = s;
        this.radi = radius;
        this.positions = vecs;
        if (endCapsDir == null)
        {
            this.endCapsDirection[0] = fromTo(this.positions[1], this.positions[0]);
            this.endCapsDirection[1] = fromTo(this.positions[(this.positions.length - 1)], this.positions[(this.positions.length - 2)]);
        }
        else
        {
            this.endCapsDirection = endCapsDir;
        }
        this.pipes = calculatePipes();
    }

    public CMPipe[] calculatePipes()
    {
        CMPipe[] pip = new CMPipe[this.positions.length - 1];
        for (int a = 0; a < this.positions.length - 1; a++)
        {
            Vec3d thisVec = this.positions[a];
            Vec3d nextVec = this.positions[(a + 1)];
            Vec3d thisPerp = this.endCapsDirection[0];
            Vec3d nextPerp = this.endCapsDirection[1];
            if (a > 0)
            {
                Vec3d prevVec = this.positions[(a - 1)];
                Vec3d prevToThis = fromTo(prevVec, thisVec).func_72432_b();
                Vec3d nextToThis = fromTo(nextVec, thisVec).func_72432_b();
                Vec3d paraToPlane = prevToThis.func_178787_e(nextToThis);
                Vec3d perpHelper = getPerpendicularVec(prevToThis, nextToThis);
                thisPerp = getPerpendicularVec(paraToPlane, perpHelper);
            }
            if (a < this.positions.length - 2)
            {
                Vec3d nextNextVec = this.positions[(a + 2)];
                Vec3d thisToNext = fromTo(thisVec, nextVec).func_72432_b();
                Vec3d next2ToNext = fromTo(nextNextVec, nextVec).func_72432_b();
                Vec3d paraToPlane = thisToNext.func_178787_e(next2ToNext);
                Vec3d perpHelper = getPerpendicularVec(thisToNext, next2ToNext);
                nextPerp = getPerpendicularVec(paraToPlane, perpHelper);
            }
            CMPipe pipe = new CMPipe(thisVec, nextVec, thisPerp, nextPerp, this.radi[a], this.radi[(a + 1)], this.sides);
            pipe.renderCaps = renderCaps(pipe, a);
            pip[a] = pipe;
        }
        return pip;
    }

    public CapType renderCaps(CMPipe pipe, int pipeIndex)
    {
        if (pipeIndex == 0) {
            return CapType.BOTTOM;
        }
        if (pipeIndex == this.positions.length - 1) {
            return CapType.TOP;
        }
        return CapType.NONE;
    }

    public void render()
    {
        for (CMPipe pipe : this.pipes) {
            pipe.render();
        }
    }

    public void setPipesColor(Color color)
    {
        for (CMPipe pipe : this.pipes)
        {
            pipe.color = color;
            pipe.setUseTexture(false);
        }
    }

    public void setPipesColors(Color[] colors)
    {
        for (int a = 0; a < this.pipes.length; a++) {
            this.pipes[a].color = colors[a];
        }
    }

    public void setPipesTexture(ResourceLocation res)
    {
        for (CMPipe pipe : this.pipes)
        {
            pipe.texture = res;
            pipe.setTextureUV(new TextureUV());
            if (res != null) {
                pipe.setUseTexture(true);
            }
        }
    }

    public void setPipesTextureUV(TextureUV uv)
    {
        for (CMPipe pipe : this.pipes) {
            pipe.textureUV = uv;
        }
    }

    public static double[] filledDouble(double item, int count)
    {
        double[] array = new double[count];
        for (int a = 0; a < array.length; a++) {
            array[a] = item;
        }
        return array;
    }
}
