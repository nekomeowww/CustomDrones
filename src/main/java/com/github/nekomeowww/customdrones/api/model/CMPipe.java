package com.github.nekomeowww.customdrones.api.model;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.math.Vec3d;

public class CMPipe
        extends CMBase
{
    public static boolean NEWRENDERMODE = true;
    public Vec3d end1;
    public Vec3d end2;
    public Vec3d axis1;
    public Vec3d axis2;
    public double ra1;
    public double ra2;
    public int sides;
    public double initSpin1;
    public double initSpin2;
    public CapType renderCaps = CapType.BOTH;
    public boolean textureHasCaps = true;
    public List<CMVertex> end1Vers = new ArrayList();
    public List<CMVertex> end2Vers = new ArrayList();
    public List<CMVertex> sideVers = new ArrayList();

    public CMPipe(Vec3d e1, Vec3d e2, double radius, int sides)
    {
        this(e1, e2, e1.func_178788_d(e2), e1.func_178788_d(e2), radius, radius, sides);
    }

    public CMPipe(Vec3d e1, Vec3d e2, double radius1, double radius2, int sides)
    {
        this(e1, e2, e1.func_178788_d(e2), e1.func_178788_d(e2), radius1, radius2, sides);
    }

    public CMPipe(Vec3d e1, Vec3d e2, Vec3d tiltEnds, double radius1, double radius2, int sides)
    {
        this(e1, e2, tiltEnds, tiltEnds, radius1, radius2, sides);
    }

    public CMPipe(Vec3d e1, Vec3d e2, Vec3d p1, Vec3d p2, double end1r, double end2r, int s)
    {
        this.end1 = e1;
        this.end2 = e2;
        Vec3d end12 = fromTo(this.end1, this.end2);
        this.axis1 = p1;
        this.axis2 = p2;
        if (this.axis1.func_72430_b(end12) > 0.0D) {
            this.axis1 = scale(this.axis1, -1.0D);
        }
        if (this.axis2.func_72430_b(end12) > 0.0D) {
            this.axis2 = scale(this.axis2, -1.0D);
        }
        this.ra1 = end1r;
        this.ra2 = end2r;
        this.sides = s;
    }

    public CMPipe setInitSpin(double d)
    {
        this.initSpin1 = (this.initSpin2 = d);
        return this;
    }

    public CMPipe setInitSpin(double d1, double d2)
    {
        this.initSpin1 = d1;
        this.initSpin2 = d2;
        return this;
    }

    public CMPipe setRenderCaps(CapType type)
    {
        this.renderCaps = type;
        return this;
    }

    public CMPipe autoUVfromU(double u1, double v1, double u2)
    {
        Vec3d end12 = fromTo(this.end1, this.end2);
        double angleRad = 6.283185307179586D / this.sides;
        double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
        double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
        double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
        double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();

        double texHeight = totalHeight / totalWidth * (u2 - u1);
        double v2 = v1 + texHeight;
        setTextureUV(new TextureUV(u1, v1, u2, v2));
        return this;
    }

    public CMPipe autoUVfromV(double u1, double v1, double v2)
    {
        Vec3d end12 = fromTo(this.end1, this.end2);
        double angleRad = 6.283185307179586D / this.sides;
        double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
        double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
        double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
        double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();

        double texWidth = totalWidth / totalHeight * (v2 - v1);
        double u2 = u1 + texWidth;
        setTextureUV(new TextureUV(u1, v1, u2, v2));
        return this;
    }

    public void render()
    {
        NEWRENDERMODE = true;
        if (NEWRENDERMODE)
        {
            newRender();
        }
        else
        {
            this.end1Vers.clear();
            this.end2Vers.clear();
            this.sideVers.clear();
            oldRender();
        }
    }

    public void oldRender()
    {
        Vec3d end12 = fromTo(this.end1, this.end2);
        Vec3d mid12 = scale(this.end1.func_178787_e(this.end2), 0.5D);
        double angleRad = 6.283185307179586D / this.sides;
        double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
        double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
        double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
        double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();

        double v1 = this.textureUV.v1;
        double v4 = this.textureUV.v2;
        double v2 = v1 + Math.max(this.ra1, this.ra2) * 2.0D / totalHeight * (v4 - v1);
        if (!this.textureHasCaps)
        {
            v2 = v1;
            totalHeight = end12.func_72433_c();
        }
        double u1 = this.textureUV.u1;
        double uend = this.textureUV.u2;
        double ustep = (sideW1 + sideW2) / 2.0D / totalWidth * (uend - u1);
        Vec3d spin1 = getPerpendicularVec(this.axis1, end12);
        Vec3d spin2 = getPerpendicularVec(this.axis2, end12);
        if (spin1.func_72430_b(spin2) < 0.0D) {
            spin2 = scale(spin2, -1.0D);
        }
        if (this.renderCaps != CapType.BOTH) {
            enableCull(false);
        }
        begin(8);
        for (int a = 0; a < this.sides + 1; a++)
        {
            double unow = u1 + ustep * a;
            Vec3d final2 = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
            Vec3d final2ToLater = fromTo(final2,
                    setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * (a + 1)), this.ra2).func_178787_e(this.end2));
            Vec3d final1 = setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
            Vec3d normal = vec(0.0D, 1.0D, 0.0D);
            if (final2ToLater.func_72433_c() == 0.0D)
            {
                Vec3d final1ToLater = fromTo(final1,
                        setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * (a + 1)), this.ra1)
                                .func_178787_e(this.end1));
                normal = getPerpendicularVec(final1ToLater, fromTo(final1, final2)).func_72432_b();
            }
            else
            {
                normal = getPerpendicularVec(fromTo(final2, final1), final2ToLater).func_72432_b();
            }
            normal(normal.field_72450_a, normal.field_72448_b, normal.field_72449_c);
            vertex(final2.field_72450_a, final2.field_72448_b, final2.field_72449_c, unow, v2);
            vertex(final1.field_72450_a, final1.field_72448_b, final1.field_72449_c, unow, v4);
        }
        end();
        if (this.renderCaps != CapType.NONE)
        {
            Vec3d texMid1 = vec(u1 + (uend - u1) / 4.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
            Vec3d texMid2 = vec(u1 + (uend - u1) / 4.0D * 3.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
            Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
            Vec3d texSpin = vec(0.0D, (v2 - v1) / 2.0D, 0.0D);
            if (this.renderCaps != CapType.TOP)
            {
                begin(9);
                normal(this.axis1.field_72450_a, this.axis1.field_72448_b, this.axis1.field_72449_c);
                for (int a = 0; a < this.sides; a++)
                {
                    Vec3d botTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);

                    botTex = vec(botTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), botTex.field_72448_b, botTex.field_72449_c).func_178787_e(texMid1);

                    Vec3d botVertex = setLength(rotateAround(spin1, this.axis1, this.initSpin1 - 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
                    vertex(botVertex.field_72450_a, botVertex.field_72448_b, botVertex.field_72449_c, botTex.field_72450_a, botTex.field_72448_b);
                }
                end();
            }
            if (this.renderCaps != CapType.BOTTOM)
            {
                texSpin = vec(0.0D, (v1 - v2) / 2.0D, 0.0D);
                begin(9);
                normal(this.axis2.field_72450_a, this.axis2.field_72448_b, this.axis2.field_72449_c);
                for (int a = 0; a < this.sides; a++)
                {
                    Vec3d topTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);

                    topTex = vec(topTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), topTex.field_72448_b, topTex.field_72449_c).func_178787_e(texMid2);

                    Vec3d topVertex = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
                    vertex(topVertex.field_72450_a, topVertex.field_72448_b, topVertex.field_72449_c, topTex.field_72450_a, topTex.field_72448_b);
                }
                end();
            }
        }
        if (this.renderCaps != CapType.BOTH) {
            enableCull(true);
        }
    }

    public void newRender()
    {
        if ((this.end1Vers.isEmpty()) || (this.end2Vers.isEmpty()) || (this.sideVers.isEmpty()))
        {
            this.sideVers.clear();
            this.end1Vers.clear();
            this.end2Vers.clear();
            Vec3d end12 = fromTo(this.end1, this.end2);
            Vec3d mid12 = scale(this.end1.func_178787_e(this.end2), 0.5D);
            double angleRad = 6.283185307179586D / this.sides;
            double sideW1 = Math.tan(angleRad / 2.0D) * this.ra1 * 2.0D;
            double sideW2 = Math.tan(angleRad / 2.0D) * this.ra2 * 2.0D;
            double totalWidth = (sideW1 + sideW2) / 2.0D * this.sides;
            double totalHeight = Math.max(this.ra1, this.ra2) * 2.0D + end12.func_72433_c();
            double v1 = this.textureUV.v1;
            double v4 = this.textureUV.v2;
            double v2 = v1 + Math.max(this.ra1, this.ra2) * 2.0D / totalHeight * (v4 - v1);
            if (!this.textureHasCaps)
            {
                v2 = v1;
                totalHeight = end12.func_72433_c();
            }
            double u1 = this.textureUV.u1;
            double uend = this.textureUV.u2;
            double ustep = (sideW1 + sideW2) / 2.0D / totalWidth * (uend - u1);
            Vec3d spin1 = getPerpendicularVec(this.axis1, end12);
            Vec3d spin2 = getPerpendicularVec(this.axis2, end12);
            if (spin1.func_72430_b(spin2) < 0.0D) {
                spin2 = scale(spin2, -1.0D);
            }
            for (int a = 0; a < this.sides + 1; a++)
            {
                double unow = u1 + ustep * a;

                Vec3d final2 = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
                Vec3d final2ToLater = fromTo(final2,
                        setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * (a + 1)), this.ra2)
                                .func_178787_e(this.end2));

                Vec3d final1 = setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
                Vec3d normal = vec(0.0D, 1.0D, 0.0D);
                if (final2ToLater.func_72433_c() == 0.0D)
                {
                    Vec3d final1ToLater = fromTo(final1,
                            setLength(rotateAround(spin1, this.axis1, this.initSpin1 + 6.283185307179586D / this.sides * (a + 1)), this.ra1)
                                    .func_178787_e(this.end1));
                    normal = getPerpendicularVec(final1ToLater, fromTo(final1, final2)).func_72432_b();
                }
                else
                {
                    normal = getPerpendicularVec(fromTo(final2, final1), final2ToLater).func_72432_b();
                }
                this.sideVers.add(new CMVertex(final2, vec(unow, v2, 0.0D), normal));
                this.sideVers.add(new CMVertex(final1, vec(unow, v4, 0.0D), normal));
            }
            Vec3d texMid1 = vec(u1 + (uend - u1) / 4.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
            Vec3d texMid2 = vec(u1 + (uend - u1) / 4.0D * 3.0D, v1 + (v2 - v1) / 2.0D, 0.0D);
            Vec3d zUnit = vec(0.0D, 0.0D, 1.0D);
            Vec3d texSpin = vec(0.0D, (v2 - v1) / 2.0D, 0.0D);
            boolean flipNormal1 = this.axis1.func_72430_b(end12) > 0.0D;
            boolean flipNormal2 = this.axis2.func_72430_b(end12) < 0.0D;
            for (int a = 0; a < this.sides; a++)
            {
                Vec3d botTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);

                botTex = vec(botTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), botTex.field_72448_b, botTex.field_72449_c).func_178787_e(texMid1);

                Vec3d botVertex = setLength(rotateAround(spin1, this.axis1, this.initSpin1 - 6.283185307179586D / this.sides * a), this.ra1).func_178787_e(this.end1);
                this.end1Vers.add(new CMVertex(botVertex, botTex, scale(this.axis1, flipNormal1 ? -1.0D : 1.0D)));
            }
            texSpin = vec(0.0D, (v1 - v2) / 2.0D, 0.0D);
            for (int a = 0; a < this.sides; a++)
            {
                Vec3d topTex = rotateAround(texSpin, zUnit, 6.283185307179586D / this.sides * a);

                topTex = vec(topTex.field_72450_a * (totalWidth / totalHeight * (v4 - v1) / (uend - u1)), topTex.field_72448_b, topTex.field_72449_c).func_178787_e(texMid2);

                Vec3d topVertex = setLength(rotateAround(spin2, this.axis2, this.initSpin2 + 6.283185307179586D / this.sides * a), this.ra2).func_178787_e(this.end2);
                this.end2Vers.add(new CMVertex(topVertex, topTex, scale(this.axis2, flipNormal2 ? -1.0D : 1.0D)));
            }
        }
        if (this.renderCaps != CapType.BOTH) {
            enableCull(false);
        }
        if (!this.sideVers.isEmpty())
        {
            begin(8);
            for (int a = 0; a < this.sideVers.size(); a++)
            {
                CMVertex vert = (CMVertex)this.sideVers.get(a);
                vert.addToDrawing();
            }
            end();
        }
        if (this.renderCaps != CapType.NONE)
        {
            if ((!this.end1Vers.isEmpty()) && (this.renderCaps != CapType.TOP))
            {
                begin(9);
                for (int a = 0; a < this.end1Vers.size(); a++)
                {
                    CMVertex vert = (CMVertex)this.end1Vers.get(a);
                    vert.addToDrawing();
                }
                end();
            }
            if ((!this.end2Vers.isEmpty()) && (this.renderCaps != CapType.BOTTOM))
            {
                begin(9);
                for (int a = 0; a < this.end2Vers.size(); a++)
                {
                    CMVertex vert = (CMVertex)this.end2Vers.get(a);
                    vert.addToDrawing();
                }
                end();
            }
        }
        if (this.renderCaps != CapType.BOTH) {
            enableCull(true);
        }
    }
}
