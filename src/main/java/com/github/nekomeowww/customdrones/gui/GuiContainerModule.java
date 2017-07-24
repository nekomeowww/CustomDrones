package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.inventory.Container;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.gui.GuiContainerPanel;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRequireUpdate;

public class GuiContainerModule
        extends GuiContainerPanel
{
    public List<SlotModule> moduleSlots = new ArrayList();
    public EntityDrone drone;

    public GuiContainerModule(Container inventorySlotsIn, EntityDrone drone)
    {
        super(inventorySlotsIn);
        this.drone = drone;
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (int a = 0; a < this.moduleSlots.size(); a++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(a);
            if (isMouseOverSlot(slot, mouseX, mouseY))
            {
                slot.slotClicked(mouseX, mouseY, mouseButton);
                modClicked(slot, mouseX, mouseY, mouseButton);
            }
        }
    }

    public void modClicked(SlotModule slot, int mX, int mY, int mB) {}

    public void updateScreen()
    {
        super.updateScreen();
        if (this.drone.getWatchedDIChanged()) {
            PacketDispatcher.sendToServer(new PacketDroneRequireUpdate(this.drone));
        }
        for (int a = 0; a < this.moduleSlots.size(); a++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(a);
            slot.updateSlot();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(this.guiLeft, this.guiTop, 0.0D);
        GlStateManager.enableRescaleNormal();
        for (int i1 = 0; i1 < this.moduleSlots.size(); i1++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(i1);
            slot.drawModule();
        }
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.enableDepth();
        RenderHelper.enableStandardItemLighting();
        super.drawScreen(mouseX, mouseY, partialTicks);
        for (int i1 = 0; i1 < this.moduleSlots.size(); i1++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(i1);
            if ((isMouseOverSlot(slot, mouseX, mouseY)) && (slot.module != null))
            {
                slot.hovering = true;
                renderToolTip(slot.module, mouseX, mouseY);
            }
            else
            {
                slot.hovering = false;
            }
        }
    }

    public boolean isMouseOverSlot(SlotModule slotIn, int mouseX, int mouseY)
    {
        return isPointInRegion(slotIn.posX, slotIn.posY, slotIn.sizeX, slotIn.sizeY, mouseX, mouseY);
    }

    protected void renderToolTip(Module mod, int x, int y)
    {
        drawHoveringText(mod.getTooltip(), x, y, this.fontRendererObj);
    }
}