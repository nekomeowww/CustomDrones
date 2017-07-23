package com.github.nekomeowww.customdrones.render;

import java.util.Map;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMBox;
import com.github.nekomeowww.customdrones.api.model.CMPipe;

public class ModelDroneOriginal
        extends ModelDrone
{
    public CMPipe body1;
    public CMPipe body2;
    public CMPipe body3;
    public CMPipe core;
    public CMBase leg1;
    public CMBase leg2;
    public CMBase leg3;
    public CMBase leg4;
    public CMBase arms1;
    public CMBase arms2;
    public CMBase piv1;
    public CMBase piv2;
    public CMBase piv3;
    public CMBase piv4;
    public CMBase propl1;
    public CMBase propl2;
    public CMBase propl3;
    public CMBase propl4;

    public ModelDroneOriginal(RenderManager rm)
    {
        super(rm);
    }

    public void setup()
    {
        this.models.clear();
        Vec3d unitY = vec(0.0D, 1.0D, 0.0D);
        double sin45 = Math.sqrt(0.5D);
        double legEnd = 0.2025D;
        double armBaseLength = 0.5D;
        double pivotPos = armBaseLength * sin45;
        double armThick = 0.07D;
        double pivotRad = armThick * sin45;

        this.fullDrone = new CMBase().setName("Full Drone");
        this.fullDrone.setTranslate(0.0D, -0.05D, 0.0D);
        this.fullDrone.addChild(
                this.body1 = (CMPipe)new CMPipe(vec(0.0D, 0.1D, 0.0D), vec(0.0D, 0.15D, 0.0D), 0.25D, 8).setInitSpin(0.39269908169872414D).setName("Body1").setPaletteIndexes(new String[] { "Body bottom" }));
        this.fullDrone.addChild(
                this.body2 = (CMPipe)new CMPipe(vec(0.0D, 0.15D, 0.0D), vec(0.0D, 0.25D, 0.0D), 0.4D, 8).setInitSpin(0.39269908169872414D).setName("Body2").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(
                this.body3 = (CMPipe)new CMPipe(vec(0.0D, 0.25D, 0.0D), vec(0.0D, 0.28D, 0.0D), 0.25D, 8).setInitSpin(0.39269908169872414D).setName("Body3").setPaletteIndexes(new String[] { "Body top" }));
        this.fullDrone.addChild(
                this.core = (CMPipe)new CMPipe(vec(0.0D, 0.28D, 0.0D), vec(0.0D, 0.3D, 0.0D), 0.1D, 16).setInitSpin(0.39269908169872414D).setName("Core").setPaletteIndexes(new String[] { "Core" }));

        this.fullDrone.addChild(
                this.leg1 = new CMPipe(vec(0.0D, 0.25D, 0.0D), vec(legEnd, 0.05D, legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg1").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(
                this.leg2 = new CMPipe(vec(0.0D, 0.25D, 0.0D), vec(legEnd, 0.05D, -legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg2").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(
                this.leg3 = new CMPipe(vec(0.0D, 0.25D, 0.0D), vec(-legEnd, 0.05D, -legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg3").setPaletteIndexes(new String[] { "Leg" }));
        this.fullDrone.addChild(
                this.leg4 = new CMPipe(vec(0.0D, 0.25D, 0.0D), vec(-legEnd, 0.05D, legEnd), unitY, 0.0D, 0.03D, 4).setInitSpin(0.7853981633974483D).setName("Leg4").setPaletteIndexes(new String[] { "Leg" }));

        this.fullDrone.addChild(

                this.arms1 = new CMBox(armThick, armBaseLength * 2.0D + armThick, 0.03D).setTranslate(0.0D, 0.2D, 0.0D).setRotation(0.0D, 1.0D, 0.0D, 90.0D).setRotation(0.0D, 0.0D, 1.0D, 90.0D).setRotation(0.0D, 1.0D, 0.0D, 45.0D).setName("Arms1").setPaletteIndexes(new String[] { "Arm" }));
        this.fullDrone.addChild(

                this.arms2 = new CMBox(armThick, armBaseLength * 2.0D + armThick, 0.03D).setTranslate(0.0D, 0.2D, 0.0D).setRotation(0.0D, 1.0D, 0.0D, 90.0D).setRotation(0.0D, 0.0D, 1.0D, 90.0D).setRotation(0.0D, 1.0D, 0.0D, -45.0D).setName("Arms2").setPaletteIndexes(new String[] { "Arm" }));

        this.fullDrone.addChild(

                this.piv1 = new CMPipe(vec(pivotPos, 0.215D, pivotPos), vec(pivotPos, 0.32D, pivotPos), pivotRad, 0.0D, 4).setName("Pivot1").setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(

                this.piv2 = new CMPipe(vec(pivotPos, 0.215D, -pivotPos), vec(pivotPos, 0.32D, -pivotPos), pivotRad, 0.0D, 4).setName("Pivot2").setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(

                this.piv3 = new CMPipe(vec(-pivotPos, 0.215D, -pivotPos), vec(-pivotPos, 0.32D, -pivotPos), pivotRad, 0.0D, 4).setName("Pivot3").setPaletteIndexes(new String[] { "Engine" }));
        this.fullDrone.addChild(

                this.piv4 = new CMPipe(vec(-pivotPos, 0.215D, pivotPos), vec(-pivotPos, 0.32D, pivotPos), pivotRad, 0.0D, 4).setName("Pivot4").setPaletteIndexes(new String[] { "Engine" }));

        this.wingsFull = new CMBase().setName("Wing Set").setPaletteIndexes(new String[] { "Wing" });
        setupWing(this.wingsFull, armBaseLength / 2.1D, 0.03D);
        this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(pivotPos, 0.32D, pivotPos).setName("Wings1")
                .setPaletteIndexes(new String[] { "Wing" }));
        this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(pivotPos, 0.32D, -pivotPos).setName("Wings2")
                .setPaletteIndexes(new String[] { "Wing" }));
        this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(-pivotPos, 0.32D, -pivotPos).setName("Wings3")
                .setPaletteIndexes(new String[] { "Wing" }));
        this.fullDrone.addChild(new CMBase().addChild(this.wingsFull).setTranslate(-pivotPos, 0.32D, pivotPos).setName("Wings4")
                .setPaletteIndexes(new String[] { "Wing" }));

        this.fullDrone.setCenter(0.0D, 0.15D, 0.0D);
        addModel(this.fullDrone);
    }
}
