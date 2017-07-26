package com.github.nekomeowww.customdrones.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class DroneModels
{
    public int index = 0;
    public RenderManager rm;
    public List<ModelProp> models = new ArrayList();
    public Map<Integer, ModelDrone> modelIndex = new HashMap();
    public ModelProp original;
    public ModelProp uniWing;
    public ModelProp UFO;
    public ModelProp fighter;
    public ModelProp baby;
    public ModelProp wildItem;
    public static DroneModels instance;

    private DroneModels(RenderManager rm)
    {
        addModel(this.original = new ModelProp(new ModelDroneOriginal(rm).setName("Original"), this.index++, false));
        addModel(this.uniWing = new ModelProp(new ModelDroneUniWing(rm).setName("Uni-Wing"), this.index++, false));
        addModel(this.UFO = new ModelProp(new ModelDroneUFO(rm).setName("UFO"), this.index++, false));
        addModel(this.fighter = new ModelProp(new ModelDroneFighter(rm).setName("Fighter"), this.index++, false));
        addModel(this.baby = new ModelProp(new ModelDroneBaby(rm).setName("Baby"), this.index++, true));
        addModel(this.wildItem = new ModelProp(new ModelDroneWildItem(rm).setName("WildItem"), this.index++, true));
    }

    public void addModel(ModelProp prop)
    {
        this.models.add(prop);
        this.modelIndex.put(Integer.valueOf(prop.id), prop.model);
    }

    public ModelDrone getModelOrDefault(int id)
    {
        ModelDrone md = (ModelDrone)this.modelIndex.get(Integer.valueOf(id));
        return md == null ? this.original.model : md;
    }

    public ModelDrone getModelOrDefault(EntityDrone drone)
    {
        if (drone == null) {
            return this.original.model;
        }
        return getModelOrDefault(drone.droneInfo.appearance.modelID);
    }

    public static void init(RenderManager rm)
    {
        if ((instance == null) || (instance.rm != rm)) {
            instance = new DroneModels(rm);
        }
    }


    static
    {
        init(Minecraft.getMinecraft().getRenderManager());
    }


    public static class ModelProp
    {
        public ModelDrone model;
        public int id;
        public boolean isMobModel;

        public ModelProp(ModelDrone mod, int i, boolean mob)
        {
            this.model = mod;
            this.id = i;
            this.isMobModel = mob;
        }

        public boolean equals(Object obj)
        {
            if ((obj instanceof ModelProp)) {
                return (((ModelProp)obj).model == this.model) && (((ModelProp)obj).id == this.id) && (((ModelProp)obj).isMobModel == this.isMobModel);
            }
            return false;
        }
    }
}
