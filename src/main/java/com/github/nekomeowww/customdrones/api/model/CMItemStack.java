package com.github.nekomeowww.customdrones.api.model;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CMItemStack
        extends CMBase
{
    public ItemStack is;
    public World world;
    public Vec3d origin;
    public double scale;

    public CMItemStack(Vec3d ori)
    {
        this.origin = ori;
        if (ori == null) {
            this.origin = vec(0.0D, 0.0D, 0.0D);
        }
        this.scale = 1.0D;
        setUseTexture(true);
    }

    public void render()
    {
        if ((this.is != null) && (this.world != null))
        {
            push();
            translate(this.origin.field_72450_a, this.origin.field_72448_b, this.origin.field_72449_c);
            scale(this.scale, this.scale, this.scale);
            enableTexture(true);
            RenderItem itemRenderer = Minecraft.func_71410_x().func_175599_af();
            if (itemRenderer != null) {
                itemRenderer.func_181564_a(this.is, ItemCameraTransforms.TransformType.NONE);
            }
            pop();
        }
    }
}
