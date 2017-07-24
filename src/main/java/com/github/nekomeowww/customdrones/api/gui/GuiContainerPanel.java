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

    public void initGui()
    {
        super.initGui();
        this.panels.clear();
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    protected void keyTyped(char typedChar, int keyCode)
            throws IOException
    {
        super.keyTyped(typedChar, keyCode);
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

    public void drawDefaultBackground() {}

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    public void updateScreen()
    {
        super.updateScreen();
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.updatePanel();
        }
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        for (int a = 0; a < this.panels.size(); a++)
        {
            Panel p = (Panel)this.panels.get(a);
            p.drawPanel();
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}