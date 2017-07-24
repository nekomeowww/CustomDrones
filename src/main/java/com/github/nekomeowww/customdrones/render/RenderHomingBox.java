package com.github.nekomeowww.customdrones.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.CMBox;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.entity.EntityHomingBox;

public class RenderHomingBox<T extends EntityHomingBox>
        extends Render<T>
{
    public RenderHomingBox(RenderManager renderManager)
    {
        super(renderManager);
    }

    public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        if ((entity != null) && (entity.target != null))
        {
            AxisAlignedBB aabb = entity.target.getEntityBoundingBox();
            if (aabb == null)
            {
                Vec3d vec = EntityHelper.getCenterVec(entity.target);
                aabb = new AxisAlignedBB(vec, vec);
                aabb.expand(entity.target.width / 2.0F, entity.target.height / 2.0F, entity.target.width / 2.0F);
            }
            double expand = aabb.getAverageEdgeLength() / 4.0D;
            aabb = aabb.expandXyz(expand);

            double thickness = Math.sqrt(x * x + y * y + z * z) / 100.0D;
            double xWidth = aabb.maxX - aabb.minX;
            double yWidth = aabb.maxY - aabb.minY;
            double zWidth = aabb.maxZ - aabb.minZ;
            Color c = new Color(1.0D, 0.3D, 0.3D, 1.0D);
            CMBase bX = new CMBox(xWidth, thickness, thickness).setColor(c);
            CMBase bY = new CMBox(thickness, yWidth, thickness).setColor(c);
            CMBase bZ = new CMBox(thickness, thickness, zWidth).setColor(c);
            GL11.glPushMatrix();
            GL11.glTranslated(entity.target.posX - entity.posX, entity.target.posY - entity.posY - expand, entity.target.posZ - entity.posZ);

            GL11.glPushMatrix();
            GL11.glTranslated(xWidth / 2.0D, 0.0D, 0.0D);
            bZ.fullRender();
            GL11.glTranslated(0.0D, yWidth, 0.0D);
            bZ.fullRender();
            GL11.glTranslated(-xWidth, 0.0D, 0.0D);
            bZ.fullRender();
            GL11.glTranslated(0.0D, -yWidth, 0.0D);
            bZ.fullRender();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, 0.0D, zWidth / 2.0D);
            bX.fullRender();
            GL11.glTranslated(0.0D, yWidth, 0.0D);
            bX.fullRender();
            GL11.glTranslated(0.0D, 0.0D, -zWidth);
            bX.fullRender();
            GL11.glTranslated(0.0D, -yWidth, 0.0D);
            bX.fullRender();
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslated(0.0D, yWidth / 2.0D, 0.0D);
            GL11.glTranslated(xWidth / 2.0D, 0.0D, zWidth / 2.0D);
            bY.fullRender();
            GL11.glTranslated(0.0D, 0.0D, -zWidth);
            bY.fullRender();
            GL11.glTranslated(-xWidth, 0.0D, 0.0D);
            bY.fullRender();
            GL11.glTranslated(0.0D, 0.0D, zWidth);
            bY.fullRender();
            GL11.glPopMatrix();

            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

    protected ResourceLocation getEntityTexture(T entity)
    {
        return null;
    }
}
