package com.github.nekomeowww.customdrones.api.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.item.ItemDroneSpawn;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade.GunUpgrade;

public class PIItemStack
        extends PI
{
    public ItemStack is;

    public PIItemStack(Panel p, ItemStack item)
    {
        super(p);
        this.is = item;
    }

    public void mouseOverItem(int mxlocal, int mylocal, boolean drawing)
    {
        super.mouseOverItem(mxlocal, mylocal, drawing);
        if ((drawing) && (this.is != null)) {
            renderTooltip(this.is, mxlocal, mylocal);
        }
    }

    public void drawItemContent()
    {
        String desc = getDescStringForIS(this.is);
        int stringLength = this.fontRenderer.func_78256_a(desc);
        int totalLength = 16 + stringLength + 4;
        int maxStringLength = (int)Math.floor(this.xw - this.margin) - 20;
        totalLength = 0;
        List<String> strings = this.fontRenderer.func_78271_c(desc, maxStringLength);
        for (String s : strings)
        {
            int slength = this.fontRenderer.func_78256_a(s.trim());
            totalLength = Math.max(totalLength, 16 + slength + 4);
        }
        this.yw = Math.max(this.yw, strings.size() * 10 + 20);

        GL11.glPushMatrix();
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        renderItem(this.is, (int)(this.xw - totalLength) / 2, (int)this.yw / 2 - 8);
        for (int a = 0; a < strings.size(); a++)
        {
            String s = (String)strings.get(a);
            this.fontRenderer.func_175065_a(s, (int)(this.xw - totalLength) / 2 + 18, (int)this.yw / 2 - 5 * strings.size() + 10 * a,
                    (int)this.strColor.toLong(), this.stringShadow);
        }
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        GL11.glPopMatrix();
    }

    public String getDescStringForIS(ItemStack is)
    {
        if (is == null) {
            return "";
        }
        if (is.func_77973_b() == DronesMod.droneModule) {
            return ItemDroneModule.getModule(is).displayName;
        }
        if (is.func_77973_b() == DronesMod.gunUpgrade)
        {
            ((ItemGunUpgrade)DronesMod.gunUpgrade);return ItemGunUpgrade.getGunUpgrade(is).name;
        }
        if (is.func_77973_b() == DronesMod.droneSpawn)
        {
            DroneInfo di = DronesMod.droneSpawn.getDroneInfo(is);
            return DroneInfo.greekNumber[di.casing] + " " + DroneInfo.greekNumber[di.chip] + " " + DroneInfo.greekNumber[di.core] + " " + DroneInfo.greekNumber[di.engine];
        }
        return is.func_82833_r();
    }

    public void renderItem(ItemStack is, int i, int j)
    {
        if ((is != null) && (is.func_77973_b() != null))
        {
            GL11.glPopMatrix();
            RenderHelper.func_74520_c();
            renderItem().func_175042_a(is, i, j);
            renderItem().func_180453_a(this.panel.mc.field_71466_p, is, i, j, null);
            RenderHelper.func_74518_a();
            GL11.glPushMatrix();
        }
    }

    public RenderItem renderItem()
    {
        return this.panel.mc.func_175599_af();
    }

    public void renderTooltip(ItemStack stack, int x, int y)
    {
        List<String> list = stack.func_82840_a(this.panel.mc.field_71439_g, this.panel.mc.field_71474_y.field_82882_x);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.set(i, stack.func_77953_t().field_77937_e + (String)list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + (String)list.get(i));
            }
        }
        FontRenderer font = stack.func_77973_b().getFontRenderer(stack);
        DrawHelper.drawHoveringText(list, x, y, font == null ? this.panel.mc.field_71466_p : font, this.panel.mc.field_71443_c, y);
    }
}