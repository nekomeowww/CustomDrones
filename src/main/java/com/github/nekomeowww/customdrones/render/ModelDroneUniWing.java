package com.github.nekomeowww.customdrones.render;

import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMBox;
import com.github.nekomeowww.customdrones.api.model.CMPipe;

public class ModelDroneUniWing
        extends ModelDrone
{
    public CMBox body;
    public CMBox core;
    public CMBase leg1;
    public CMBase leg2;
    public CMBase leg3;
    public CMBase leg4;
    public CMBase arms1;
    public CMBase arms2;
    public CMBase engine;
    public CMBase propl1;
    public CMBase propl2;
    public CMBase propl3;
    public CMBase propl4;

    public ModelDroneUniWing(RenderManager rm)
    {
        super(rm);
    }

    public void setup()
    {
        this.models.clear();
        double bodySize = 0.3D;
        double bodyThick = 0.2D;
        double coreThick = 0.05D;
        double armThick = 0.06D;
        double armLength = 0.55D;
        double legThick = 0.03D;
        double legHeight = 0.15D;
        double wingLength = 0.5D;
        double engineSize = 0.04D;
        double engineThick = 0.1D;
        double wingThick = 0.06D;

        this.fullDrone = new CMBase().setName("Full Drone");
        this.fullDrone.addChild(
                this.body = (CMBox)new CMBox(bodySize, bodyThick, bodySize, vec(0.0D, legHeight + coreThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(
                this.core = (CMBox)new CMBox(bodySize, coreThick, bodySize, vec(0.0D, legHeight - bodyThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));

        this.fullDrone.addChild(

                this.leg1 = new CMBox(legThick, legHeight, legThick, vec(0.0D, legHeight / 2.0D, armLength - legThick / 2.0D)).setName("Leg1").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(

                this.leg2 = new CMBox(legThick, legHeight, legThick, vec(0.0D, legHeight / 2.0D, -armLength + legThick / 2.0D)).setName("Leg2").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(

                this.leg3 = new CMBox(legThick, legHeight, legThick, vec(armLength - legThick / 2.0D, legHeight / 2.0D, 0.0D)).setName("Leg3").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(

                this.leg4 = new CMBox(legThick, legHeight, legThick, vec(-armLength + legThick / 2.0D, legHeight / 2.0D, 0.0D)).setName("Leg4").setPaletteIndexes(new String[] { "Leg" }));

        this.fullDrone.addChild(
                this.arms1 = new CMBox(armLength * 2.0D, armThick, armThick).setTranslate(0.0D, legHeight + armThick / 2.0D, 0.0D).setName("Arms1").setPaletteIndexes(new String[] { "Arm" }));
        this.fullDrone.addChild(
                this.arms2 = new CMBox(armThick, armThick, armLength * 2.0D).setTranslate(0.0D, legHeight + armThick / 2.0D, 0.0D).setName("Arms2").setPaletteIndexes(new String[] { "Arm" }));

        this.fullDrone.addChild(

                this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, legHeight + coreThick / 2.0D + bodyThick / 2.0D + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));

        this.wingsFull = new CMBase().setName("Wing Set");
        this.wingsFull.addChild(
                this.propl1 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller1"));
        this.wingsFull.addChild(
                this.propl2 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller2"));
        this.wingsFull.addChild(
                this.propl3 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller3"));
        this.wingsFull.addChild(
                this.propl4 = new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, wingLength), 0.005D, wingThick, 4).setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller4"));
        this.fullDrone.addChild(new CMBase().addChild(this.wingsFull)
                .setTranslate(0.0D, legHeight + coreThick / 2.0D + bodyThick / 2.0D + engineThick, 0.0D).setName("Wings")
                .setPaletteIndexes(new String[] { "Wing" }));

        this.fullDrone.setCenter(0.0D, legHeight, 0.0D);
        addModel(this.fullDrone);
    }

    public double getLeanAngle()
    {
        return 60.0D;
    }
}
