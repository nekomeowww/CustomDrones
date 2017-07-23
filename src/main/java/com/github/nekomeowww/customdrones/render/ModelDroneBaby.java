package com.github.nekomeowww.customdrones.render;

import java.util.Map;

import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import net.minecraft.client.renderer.entity.RenderManager;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMBox;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;

public class ModelDroneBaby
        extends ModelDrone
{
    public CMBase body;
    public CMBase bodyTop;
    public CMBase bodyBottom;
    public CMBase core;
    public CMBase engine;

    public ModelDroneBaby(RenderManager rm)
    {
        super(rm);
        this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Baby", Integer.valueOf(-10066330));
    }

    public void setup()
    {
        this.models.clear();
        double coreThick = 0.1D;
        double bodyTopThick = 0.1D;
        double bodyThick = 0.4D;
        double bodyBottomThick = 0.1D;
        double coreSize = 0.4D;
        double bodyTopSize = 0.5D;
        double bodySize = 0.5D;
        double bodyBottomSize = 0.4D;
        double engineSize = 0.05D;
        double engineThick = 0.1D;
        double wingLength = 0.5D;
        double wingThick = 0.1D;

        this.fullDrone = new CMBase().setName("Full Drone");
        this.fullDrone.addChild(

                this.bodyBottom = new CMBox(bodyBottomSize, bodyBottomThick, bodyBottomSize, vec(0.0D, bodyBottomThick / 2.0D, 0.0D)).setName("Body bottom").setPaletteIndexes(new String[] { "Body bottom" }));
        this.fullDrone.addChild(
                this.body = new CMBox(bodySize, bodyThick, bodySize, vec(0.0D, bodyBottomThick + bodyThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(

                this.bodyTop = new CMBox(bodyTopSize, bodyTopThick, bodyTopSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick / 2.0D, 0.0D)).setName("Body top").setPaletteIndexes(new String[] { "Body top" }));
        this.fullDrone.addChild(

                this.core = new CMBox(coreSize, coreThick, coreSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));

        this.fullDrone.addChild(

                this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));

        this.fullDrone.addChild(

                this.wingsFull = new CMBase().setTranslate(0.0D, bodyBottomThick + bodyThick + bodyTopThick + coreThick + engineThick, 0.0D).setName("Wings").setPaletteIndexes(new String[] { "Wing" }));
        setupWing(this.wingsFull, wingLength, wingThick);

        this.fullDrone.setCenter(0.0D, (bodyBottomThick + bodyThick + bodyTopThick + coreThick) / 2.0D, 0.0D);
        addModel(this.fullDrone);
    }

    public double getLeanAngle()
    {
        return 80.0D;
    }
}
