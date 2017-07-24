package com.github.nekomeowww.customdrones.render;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.api.model.UniqueName;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class ModelBaseMod<T extends Entity>
{
    public Map<UniqueName, CMBase> models = new HashMap();
    public RenderManager renderManager;
    public String name;
    //public DroneAppearance.ColorPalette defaultPalette;
    public ColorPalette defaultPalette;

    public ModelBaseMod(RenderManager rm)
    {
        this.renderManager = rm;
    }

    public ModelBaseMod setName(String s)
    {
        this.name = s;
        return this;
    }

    public void doRender(T entity, float yaw, float partialTicks, Object... names)
    {
        for (CMBase cm : this.models.values()) {
            cm.fullRender();
        }
    }

    public void addModel(CMBase model)
    {
        this.models.put(model.cmName, model);
    }

    public Vec3d vec(double x, double y, double z)
    {
        return new Vec3d(x, y, z);
    }

    public Vec3d vec(double x, double z)
    {
        return new Vec3d(x, 0.0D, z);
    }

    public Vec3d vecY(double y)
    {
        return new Vec3d(0.0D, y, 0.0D);
    }

    public void bindTexture(ResourceLocation location)
    {
        this.renderManager.renderEngine.bindTexture(location);
    }

    public void applyAppearances(EntityDrone drone, float yaw, float partialTicks, Object... names)
    {
        applyDefaultAppearance();
    }

    public void applyDefaultAppearance()
    {
        if (this.defaultPalette != null) {
            applyPalette(this.defaultPalette);
        }
    }

    public void applyPalette(DroneAppearance.ColorPalette p)
    {
        if (p != null) {
            for (CMBase cm : this.models.values()) {
                setColorSingle(p, cm);
            }
        }
    }

    public void setColorSingle(DroneAppearance.ColorPalette palette, CMBase cm0)
    {
        List<String> indexes = cm0.getPaletteIndexes();
        for (String paletteIndex : indexes)
        {
            Color color = palette.getPaletteColorFor(paletteIndex);
            if (color != null) {
                cm0.setPaletteIndexColor(paletteIndex, color, true);
            }
        }
        for (CMBase cm : cm0.childModels) {
            setColorSingle(palette, cm);
        }
    }
}
