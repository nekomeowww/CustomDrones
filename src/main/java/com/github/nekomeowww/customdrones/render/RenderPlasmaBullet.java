package com.github.nekomeowww.customdrones.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;

public class RenderPlasmaBullet<T extends EntityPlasmaShot>
        extends Render<T>
{
    public static ModelPlasmaShot model;

    public RenderPlasmaBullet(RenderManager renderManager)
    {
        super(renderManager);
        model = new ModelPlasmaShot(renderManager);
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        model.doRender(entity, entityYaw, partialTicks, new Object[] { Double.valueOf(x), Double.valueOf(y), Double.valueOf(z) });
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(T entity)
    {
        return null;
    }
}
