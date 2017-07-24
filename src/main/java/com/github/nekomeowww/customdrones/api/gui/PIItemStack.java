package com.github.nekomeowww.customdrones.api.gui;

import java.util.List;

import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
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
        int stringLength = this.fontRenderer.getStringWidth(desc);
        int totalLength = 16 + stringLength + 4;
        int maxStringLength = (int)Math.floor(this.xw - this.margin) - 20;
        totalLength = 0;
        List<String> strings = this.fontRenderer.listFormattedStringToWidth(desc, maxStringLength);
        for (String s : strings)
        {
            int slength = this.fontRenderer.getStringWidth(s.trim());
            totalLength = Math.max(totalLength, 16 + slength + 4);
        }
        this.yw = Math.max(this.yw, strings.size() * 10 + 20);

        GL11.glPushMatrix();
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        renderItem(this.is, (int)(this.xw - totalLength) / 2, (int)this.yw / 2 - 8);
        for (int a = 0; a < strings.size(); a++)
        {
            String s = (String)strings.get(a);
            this.fontRenderer.drawString(s, (int)(this.xw - totalLength) / 2 + 18, (int)this.yw / 2 - 5 * strings.size() + 10 * a,
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
        if (is.getItem() == CustomDrones.droneModule) {
            return ItemDroneModule.getModule(is).displayName;
        }
        if (is.getItem() == CustomDrones.gunUpgrade)
        {
            //((ItemGunUpgrade)CustomDrones.gunUpgrade);
            return ItemGunUpgrade.getGunUpgrade(is).name;
        }
        if (is.getItem() == CustomDrones.droneSpawn)
        {
            DroneInfo di = CustomDrones.droneSpawn.getDroneInfo(is);
            return DroneInfo.greekNumber[di.casing] + " " + DroneInfo.greekNumber[di.chip] + " " + DroneInfo.greekNumber[di.core] + " " + DroneInfo.greekNumber[di.engine];
        }
        return is.getDisplayName();
    }

    public void renderItem(ItemStack is, int i, int j)
    {
        if ((is != null) && (is.getItem() != null))
        {
            GL11.glPopMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            renderItem().renderItemIntoGUI(is, i, j);
            renderItem().renderItemOverlayIntoGUI(this.panel.mc.fontRendererObj, is, i, j, null);
            RenderHelper.disableStandardItemLighting();
            GL11.glPushMatrix();
        }
    }

    public RenderItem renderItem()
    {
        return this.panel.mc.getRenderItem();
    }

    public void renderTooltip(ItemStack stack, int x, int y)
    {
        List<String> list = stack.getTooltip(this.panel.mc.thePlayer, this.panel.mc.gameSettings.advancedItemTooltips);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + (String)list.get(i));
            } else {
                list.set(i, TextFormatting.GRAY + (String)list.get(i));
            }
        }
        FontRenderer font = stack.getItem().getFontRenderer(stack);
        DrawHelper.drawHoveringText(list, x, y, font == null ? this.panel.mc.fontRendererObj : font, this.panel.mc.displayWidth, y);
    }
}