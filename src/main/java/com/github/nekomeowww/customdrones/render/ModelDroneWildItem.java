package com.github.nekomeowww.customdrones.render;

import java.util.Map;

import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import net.minecraft.client.renderer.entity.RenderManager;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMBox;
import com.github.nekomeowww.customdrones.api.model.CMItemStack;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneWildItem;

public class ModelDroneWildItem
        extends ModelDrone
{
    public CMItemStack itemModel;
    public CMBase body;
    public CMBase bodyLeft;
    public CMBase bodyRight;
    public CMBase bodyTop;
    public CMBase bodyBottom;
    public CMBase core;
    public CMBase engine;

    public ModelDroneWildItem(RenderManager rm)
    {
        super(rm);
        this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Wild", Integer.valueOf(-10066330));
    }

    public void setup()
    {
        this.models.clear();
        double itemHeight = 1.0D;
        double bodyThick = 0.15D;
        double bodyHeight = 0.7D;
        double bodyWidth = 0.8D;
        double coreThick = 0.1D;
        double coreWidth = 0.6D;
        double engineSize = 0.05D;
        double engineThick = 0.1D;
        double wingLength = 0.5D;
        double wingThick = 0.1D;

        this.fullDrone = new CMBase().setName("Full Drone");

        this.fullDrone.addChild(this.itemModel = (CMItemStack)new CMItemStack(vec(0.0D, itemHeight / 2.0D, 0.0D)).setName("Itemstack"));

        this.fullDrone.addChild(
                this.body = new CMBox(bodyWidth, bodyThick, bodyThick, vec(0.0D, itemHeight + bodyThick / 2.0D, 0.0D)).setName("Body").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(

                this.bodyLeft = new CMBox(bodyThick, bodyHeight, bodyThick, vec(-bodyWidth / 2.0D - bodyThick / 2.0D, itemHeight + bodyThick - bodyHeight / 2.0D, 0.0D)).setName("Body left").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(

                this.bodyRight = new CMBox(bodyThick, bodyHeight, bodyThick, vec(bodyWidth / 2.0D + bodyThick / 2.0D, itemHeight + bodyThick - bodyHeight / 2.0D, 0.0D)).setName("Body right").setPaletteIndexes(new String[] { "Body" }));
        this.fullDrone.addChild(

                this.core = new CMBox(coreWidth, coreThick, coreThick, vec(0.0D, itemHeight + bodyThick + coreThick / 2.0D, 0.0D)).setName("Core").setPaletteIndexes(new String[] { "Core" }));

        this.fullDrone.addChild(

                this.engine = new CMBox(engineSize, engineThick, engineSize, vec(0.0D, itemHeight + bodyThick + coreThick + engineThick / 2.0D, 0.0D)).setName("Engine").setPaletteIndexes(new String[] { "Engine" }));

        this.fullDrone.addChild(
                this.wingsFull = new CMBase().setTranslate(0.0D, itemHeight + bodyThick + coreThick + engineThick, 0.0D).setName("Wings").setPaletteIndexes(new String[] { "Wing" }));
        setupWing(this.wingsFull, wingLength, wingThick);

        this.fullDrone.setCenter(0.0D, itemHeight + bodyThick / 2.0D, 0.0D);
        addModel(this.fullDrone);
    }

    public void doRender(EntityDrone d, float yaw, float partialTicks, Object... params)
    {
        if (this.itemModel != null) {
            if (((d instanceof EntityDroneWildItem)) && (((EntityDroneWildItem)d).holding != null))
            {
                this.itemModel.is = ((EntityDroneWildItem)d).holding;
                this.itemModel.world = d.field_70170_p;
            }
            else
            {
                this.itemModel.is = null;
                this.itemModel.world = null;
            }
        }
        super.doRender(d, yaw, partialTicks, params);
    }

    public double getLeanAngle()
    {
        return 80.0D;
    }
}
