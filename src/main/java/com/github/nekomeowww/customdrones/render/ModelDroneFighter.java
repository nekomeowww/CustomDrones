package com.github.nekomeowww.customdrones.render;

import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMPipe;
import com.github.nekomeowww.customdrones.api.model.CMTriangleMountain;
import com.github.nekomeowww.customdrones.api.model.CapType;

public class ModelDroneFighter
        extends ModelDrone
{
    public CMBase body;
    public CMBase bodyTop;
    public CMBase bodyBottom;
    public CMBase core;
    public CMBase engine;

    public ModelDroneFighter(RenderManager rm)
    {
        super(rm);
    }

    public void setup()
    {
        this.models.clear();
        this.fullDrone = new CMBase().setName("Full Drone");

        Vec3d[] centerVs = VecHelper.flipAndLoop(new Vec3d[] {
                vec(0.0D, 0.5D), vec(0.6D, -0.2D), vec(0.5D, -0.4D), vec(0.0D, -0.3D) }, true, false, false);
        Vec3d[] bottomMidVs = VecHelper.translate(VecHelper.scale(centerVs, vec(0.0D, 0.05D), 0.9D), vecY(-0.025D));
        Vec3d[] bottomVs = VecHelper.translate(VecHelper.scale(centerVs, vec(0.0D, 0.05D), 0.8D), vecY(-0.04D));
        Vec3d[] topMidVs = VecHelper.mirror(bottomMidVs, false, true, false);
        Vec3d[] topVs = VecHelper.mirror(bottomVs, false, true, false);
        this.fullDrone.addChild(new CMTriangleMountain(CapType.BOTH, new Vec3d[][] { bottomVs, bottomMidVs, centerVs, topMidVs, topVs })
                .setPaletteIndexes(new String[] { "Body bottom", "Body", "Body", "Body top" }));

        this.fullDrone.addChild(new CMPipe(vec(-0.15D, 0.0D), vec(-0.15D, -0.37D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(new CMPipe(vec(-0.05D, 0.0D), vec(-0.05D, -0.35D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(new CMPipe(vec(0.05D, 0.0D), vec(0.05D, -0.35D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(new CMPipe(vec(0.15D, 0.0D), vec(0.15D, -0.37D), 0.0275D, 6).setPaletteIndexes(new String[] { "Engine" }));
        addModel(this.fullDrone.setTranslate(0.0D, 0.15D, 0.0D));
    }

    public double getLeanAngle()
    {
        return 15.0D;
    }
}
