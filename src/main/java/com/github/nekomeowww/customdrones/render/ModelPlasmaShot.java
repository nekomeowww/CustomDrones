package com.github.nekomeowww.customdrones.render;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.math.Vec3d;
import com.github.nekomeowww.customdrones.api.model.CMPipe;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;

public class ModelPlasmaShot
        extends ModelBaseMod<EntityPlasmaShot>
{
    public ModelPlasmaShot(RenderManager rm)
    {
        super(rm);
    }

    public void doRender(EntityPlasmaShot bullet, float yaw, float partialTicks, Object... names)
    {
        if (bullet != null)
        {
            double d = 0.35D;
            Vec3d dir = vec(bullet.motionX * d, bullet.motionY * d, bullet.motionZ * d);
            double width = Math.sqrt(Math.min(8.0D, Math.max(1.0D, bullet.damage))) / 50.0D;
            CMPipe pipe = new CMPipe(vec(0.0D, 0.0D, 0.0D), dir, width / 5.0D, width, 10);
            pipe.setColor(bullet.color);
            pipe.fullRender();
        }
    }
}
