package com.github.nekomeowww.customdrones.render;

import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMPipe;
import com.github.nekomeowww.customdrones.api.model.CMSphere;
import com.github.nekomeowww.customdrones.api.model.CapType;

public class ModelDroneUFO
        extends ModelDrone
{
    public CMBase body;
    public CMBase bodyTop;
    public CMBase bodyBottom;
    public CMBase core;
    public CMBase engine;

    public ModelDroneUFO(RenderManager rm)
    {
        super(rm);
    }

    public void setup()
    {
        this.models.clear();
        int bodySegs = 32;
        int engineSegs = 16;
        int coreDiv = 12;
        int ballDiv = 3;
        int ballCount = 4;
        double engineThick = 0.05D;
        double engineRa1 = 0.15D;
        double engineRa2 = 0.2D;
        double bottomThick = 0.1D;
        double bottomRa1 = engineRa2;
        double bottomRa2 = 0.6D;
        double bodyThick = 0.05D;
        double bodyRa = bottomRa2;
        double topThick = 0.1D;
        double topRa1 = bodyRa;
        double topRa2 = 0.25D;
        double coreRa = topRa2;
        double ballRa = 0.04D;

        this.fullDrone = new CMBase().setName("Full Drone");
        this.fullDrone.addChild(
                this.engine = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(0.0D, engineThick, 0.0D), engineRa1, engineRa2, engineSegs).setRenderCaps(CapType.BOTTOM).setPaletteIndexes(new String[] { "Engine" }).setName("Engine"));
        this.fullDrone.addChild(

                this.bodyBottom = new CMPipe(vec(0.0D, engineThick, 0.0D), vec(0.0D, engineThick + bottomThick, 0.0D), bottomRa1, bottomRa2, bodySegs).setRenderCaps(CapType.BOTTOM).setPaletteIndexes(new String[] { "Body bottom" }).setName("Body bottom"));
        this.fullDrone.addChild(

                this.body = new CMPipe(vec(0.0D, engineThick + bottomThick, 0.0D), vec(0.0D, engineThick + bottomThick + bodyThick, 0.0D), bodyRa, bodySegs).setRenderCaps(CapType.NONE).setPaletteIndexes(new String[] { "Body" }).setName("Body"));
        this.fullDrone.addChild(

                this.bodyTop = new CMPipe(vec(0.0D, engineThick + bottomThick + bodyThick, 0.0D), vec(0.0D, engineThick + bottomThick + bodyThick + topThick, 0.0D), topRa1, topRa2, bodySegs).setRenderCaps(CapType.TOP).setPaletteIndexes(new String[] { "Body top" }).setName("Body top"));
        this.fullDrone.addChild(
                this.wingsFull = new CMBase().setTranslate(0.0D, engineThick + bottomThick + bodyThick + topThick / 2.0D, 0.0D).setName("Wings"));
        for (int a = 0; a < ballCount; a++)
        {
            Vec3d vecCoreBall = vec((topRa1 + topRa2) / 2.0D, 0.0D, 0.0D);
            this.wingsFull.addChild(new CMSphere(vecCoreBall
                    .func_178785_b((float)(a * 3.141592653589793D * 2.0D / ballCount)), ballRa, ballDiv)
                    .setPaletteIndexes(new String[] { "Wing" }));
        }
        this.wingsFull.addChild(
                this.core = new CMSphere(vec(0.0D, 0.0D, 0.0D), coreRa, coreDiv).setTranslate(0.0D, topThick / 2.0D, 0.0D).setScale(1.0D, 0.5D, 1.0D).setPaletteIndexes(new String[] { "Core" }).setName("Core"));
        addModel(this.fullDrone);
    }

    public double wingRotationRate()
    {
        return 0.1D;
    }

    public double getLeanAngle()
    {
        return 80.0D;
    }
}
