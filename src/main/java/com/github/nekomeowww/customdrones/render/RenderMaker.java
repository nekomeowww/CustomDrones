package com.github.nekomeowww.customdrones.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;
import com.github.nekomeowww.customdrones.entity.EntityHomingBox;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;

public class RenderMaker<B extends Entity>
        implements IRenderFactory<B>
{
    Class clazz;

    public RenderMaker(Class<? super Entity> entityClass)
    {
        this.clazz = entityClass;
    }

    public Render<? super B> createRenderFor(RenderManager manager)
    {
        DroneModels.init(manager);
        if (EntityHomingBox.class == this.clazz) {
            return new RenderHomingBox(manager);
        }
        if (EntityPlasmaShot.class == this.clazz) {
            return new RenderPlasmaBullet(manager);
        }
        if (EntityDroneMob.class.isAssignableFrom(this.clazz)) {
            return new RenderDroneMob(manager);
        }
        if (EntityDrone.class == this.clazz) {
            return new RenderDrone(manager);
        }
        return null;
    }
}
