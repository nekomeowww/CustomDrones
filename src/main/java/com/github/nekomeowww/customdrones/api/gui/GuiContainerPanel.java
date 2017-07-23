package com.github.nekomeowww.customdrones.api.gui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

public class GuiContainerPanel
extends GuiContainer
{
    public List<Panel> panels = new ArrayList();

    public GuiContainerPanel(Container inventorySlotsIn)
    {
        super(inventorySlotsIn);
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.panels.clear();
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    protected void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        super.func_146273_a(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    protected void func_73869_a(char typedChar, int keyCode)
            throws IOException
    {
        super.func_73869_a(typedChar, keyCode);
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.keyTyped(typedChar, keyCode);
        }
    }

    public void itemSelected(Panel panel, PI pi) {}

    public void itemUnselected(Panel panel, PI pi) {}

    public void itemDisabled(Panel panel, PI pi) {}

    public void itemEnabled(Panel panel, PI pi) {}

    public void func_146276_q_() {}

    protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}

    public void func_73876_c()
    {
        super.func_73876_c();
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.updatePanel();
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.drawPanel();
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}