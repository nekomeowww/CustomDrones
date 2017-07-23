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

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.func_73864_a(mouseX, mouseY, mouseButton);
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

    public void func_73876_c()
    {
        super.func_73876_c();
        if (this.drone.getWatchedDIChanged()) {
            PacketDispatcher.sendToServer(new PacketDroneRequireUpdate(this.drone));
        }
        for (int a = 0; a < this.moduleSlots.size(); a++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(a);
            slot.updateSlot();
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        super.func_73863_a(mouseX, mouseY, partialTicks);
        GlStateManager.func_179094_E();
        GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslated(this.field_147003_i, this.field_147009_r, 0.0D);
        GlStateManager.func_179091_B();
        for (int i1 = 0; i1 < this.moduleSlots.size(); i1++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(i1);
            slot.drawModule();
        }
        GlStateManager.func_179121_F();
        GlStateManager.func_179145_e();
        GlStateManager.func_179126_j();
        RenderHelper.func_74519_b();
        super.func_73863_a(mouseX, mouseY, partialTicks);
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
        return func_146978_c(slotIn.posX, slotIn.posY, slotIn.sizeX, slotIn.sizeY, mouseX, mouseY);
    }

    protected void renderToolTip(Module mod, int x, int y)
    {
        drawHoveringText(mod.getTooltip(), x, y, this.field_146289_q);
    }
}