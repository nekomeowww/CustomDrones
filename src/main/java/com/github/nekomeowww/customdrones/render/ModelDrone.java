package com.github.nekomeowww.customdrones.render;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMPipe;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class ModelDrone
        extends ModelBaseMod<EntityDrone>
{
    public static Color white = new Color(1.0D, 1.0D, 1.0D, 1.0D);
    public static Color iron = new Color(0.8D, 0.8D, 0.8D, 1.0D);
    public static Color gold = new Color(1.0D, 1.0D, 0.5D, 1.0D);
    public static Color diamond = new Color(0.6D, 1.0D, 0.9D, 1.0D);
    public static Color emerald = new Color(0.6D, 1.0D, 0.6D, 1.0D);
    public CMBase fullDrone;
    public CMBase wingsFull;

    public ModelDrone(RenderManager rm)
    {
        super(rm);
        this.defaultPalette = DroneAppearance.ColorPalette.fastMake("Default Drone", white.copy(), iron.copy(), iron.copy(), iron.copy(), iron
                .copy(), iron.multiplyRGB(0.6D), iron.multiplyRGB(0.4D), iron.copy());
        setup();
    }

    public ModelDrone setName(String s)
    {
        super.setName(s);
        return this;
    }

    public void setup() {}

    public void setupWing(CMBase model, double wingLength, double wingThick)
    {
        model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, wingLength), 0.005D, wingThick, 4)
                .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller1").setPaletteIndexes(new String[] { "Wing" }));
        model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4)
                .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller2").setPaletteIndexes(new String[] { "Wing" }));
        model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, -wingLength), 0.005D, wingThick, 4)
                .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller3").setPaletteIndexes(new String[] { "Wing" }));
        model.addChild(new CMPipe(vec(0.0D, 0.0D, 0.0D), vec(-wingLength, 0.0D, wingLength), 0.005D, wingThick, 4)
                .setInitSpin(0.7853981633974483D).setScale(1.0D, 0.2D, 1.0D).setName("Propeller4").setPaletteIndexes(new String[] { "Wing" }));
    }

    public void doRender(EntityDrone drone, float yaw, float partialTicks, Object... params)
    {
        applyAppearances(drone, yaw, partialTicks, params);
        applyRotation(drone, yaw, partialTicks);
        for (CMBase cm : this.models.values()) {
            cm.fullRender();
        }
    }

    public void applyRotation(EntityDrone drone, float rotationYaw, float partialTick)
    {
        Set<CMBase> wingLikes = getWingLikeModels();
        DroneInfo di;
        if (drone != null)
        {
            di = drone.droneInfo;
            double mx = drone.field_70159_w;
            double my = drone.field_70181_x;
            double mz = drone.field_70179_y;
            Vec3d look = drone.func_70676_i(partialTick);
            double horzSpeed = Math.sqrt(mx * mx + mz * mz);
            double angleDif = Math.atan2(mx, mz) - Math.atan2(look.field_72450_a, look.field_72449_c);
            double vertTakeover = Math.cos(Math.atan2(my, horzSpeed));
            double rotatePercentage = Math.min(horzSpeed / di.getMaxSpeed() * 8.0D * vertTakeover, 2.0D);
            double pitch = getLeanAngle() * Math.cos(angleDif) * rotatePercentage;
            double roll = -getLeanAngle() * Math.sin(angleDif) * rotatePercentage;
            if (this.fullDrone != null)
            {
                this.fullDrone.resetRotation(0.0D, 0.0D, 1.0D, roll);
                this.fullDrone.setRotation(1.0D, 0.0D, 0.0D, pitch);
                this.fullDrone.setRotation(0.0D, 1.0D, 0.0D, -rotationYaw);
            }
            if (!wingLikes.isEmpty()) {
                for (CMBase cmbase : wingLikes) {
                    if (cmbase != null) {
                        cmbase.resetRotation(0.0D, 1.0D, 0.0D, drone.getWingRotation(partialTick) * wingRotationRate());
                    }
                }
            }
        }
        else
        {
            this.fullDrone.centerRots.clear();
            if (!wingLikes.isEmpty()) {
                for (CMBase cmbase : wingLikes) {
                    if (cmbase != null) {
                        cmbase.centerRots.clear();
                    }
                }
            }
        }
    }

    public double wingRotationRate()
    {
        return 1.0D;
    }

    public double getLeanAngle()
    {
        return 45.0D;
    }

    public Set<CMBase> getWingLikeModels()
    {
        Set<CMBase> set = new HashSet();
        set.add(this.wingsFull);
        return set;
    }

    public void applyAppearances(EntityDrone drone, float yaw, float partialTicks, Object... params)
    {
        applyDefaultAppearance();
        if (drone != null)
        {
            DroneInfo di = drone.droneInfo;
            if (di != null)
            {
                applyDIAppearance(di);
                applyAppearance(di.appearance);
            }
        }
        else
        {
            applyDIAppearance(new DroneInfo());
        }
    }

    public void applyDIAppearance(DroneInfo di)
    {
        if (di != null)
        {
            Color engine = iron;
            switch (di.engine)
            {
                case 2:
                    engine = gold;
                    break;
                case 3:
                    engine = diamond;
                    break;
                case 4:
                    engine = emerald;
            }
            Color core = iron;
            switch (di.core)
            {
                case 2:
                    core = gold;
                    break;
                case 3:
                    core = diamond;
                    break;
                case 4:
                    core = emerald;
            }
            Color casing = iron;
            switch (di.casing)
            {
                case 2:
                    casing = gold;
                    break;
                case 3:
                    casing = diamond;
                    break;
                case 4:
                    casing = emerald;
            }
            DroneAppearance.ColorPalette cp = DroneAppearance.ColorPalette.fastMake("", white.copy(), engine.copy(), casing.copy(), core.copy(), casing
                    .multiplyRGB(0.8D), casing.multiplyRGB(0.6D), casing.multiplyRGB(0.4D), iron.copy());
            applyPalette(cp);
        }
    }

    public void applyAppearance(DroneAppearance appearance)
    {
        if (appearance != null) {
            applyPalette(appearance.palette);
        }
    }
}
