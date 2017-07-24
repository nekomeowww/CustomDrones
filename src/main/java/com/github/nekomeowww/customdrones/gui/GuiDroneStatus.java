package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.drone.module.Module.ModuleGui;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;

public class GuiDroneStatus
        extends GuiContainerModule
{
    public EntityPlayer player;
    public EntityDrone drone;
    public SlotModule selectedModSlot;
    public Module.ModuleGui selectedModGui;

    public GuiDroneStatus(EntityPlayer p, EntityDrone d)
    {
        super(new ContainerDroneStatus(d.droneInfo.inventory), d);
        this.player = p;
        this.drone = d;
    }

    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(0, this.width / 2 - 90, this.height / 2 - 115, 90, 20, "Flyer screen"));
        GuiButton b = new GuiButton(1, this.width / 2 - 0, this.height / 2 - 115, 90, 20, "Remote screen");
        b.enabled = false;
        this.buttonList.add(b);

        int mms = this.drone.droneInfo.getMaxModSlots();
        for (int a = 0; a < mms; a++) {
            this.moduleSlots.add(new SlotModule(a, 90 + (a - mms / 2) * 30, 67, 24, this.drone, a));
        }
    }

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        if ((this.player == null) || (this.player.isDead) || (this.player.getHeldItemMainhand() == null))
        {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.actionPerformed(button);
        if (button.id == 0)
        {
            EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.player.getEntityWorld(), this.player.getHeldItemMainhand());
            if (drone != null) {
                this.player.openGui(CustomDrones.instance, 0, this.player.getEntityWorld(), drone.droneInfo.id, 0, 0);
            }
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        if (this.selectedModGui != null) {
            this.selectedModGui.updateScreen();
        }
    }

    public void handleMouseInput()
            throws IOException
    {
        super.handleMouseInput();
        if (this.selectedModGui != null) {
            this.selectedModGui.handleMouseInput();
        }
    }

    public void handleKeyboardInput()
            throws IOException
    {
        super.handleKeyboardInput();
        if (this.selectedModGui != null) {
            this.selectedModGui.handleKeyboardInput();
        }
    }

    public void modClicked(SlotModule slot, int mX, int mY, int mB)
    {
        super.modClicked(slot, mX, mY, mB);
        if ((slot != null) && (slot.module != null) && (this.selectedModSlot != slot))
        {
            ScaledResolution sr = new ScaledResolution(this.mc);
            if (this.selectedModSlot != null) {
                this.selectedModSlot.overlayColor = -1;
            }
            this.selectedModSlot = slot;
            this.selectedModGui = slot.module.getModuleGui(this);
            this.selectedModGui.modSlot = slot;
            this.selectedModGui.initGui();
            this.selectedModGui.setWorldAndResolution(this.mc, sr.getScaledWidth(), sr.getScaledHeight());
            if (this.selectedModSlot != null) {
                this.selectedModSlot.overlayColor = -2013265920;
            }
        }
    }

    public void setWorldAndResolution(Minecraft mc, int width, int height)
    {
        super.setWorldAndResolution(mc, width, height);
        if (this.selectedModGui != null) {
            this.selectedModGui.setWorldAndResolution(mc, width, height);
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneStatus.png");

    public void drawBackground(int tint) {}

    public void drawDefaultBackground() {}

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int sclW = sr.getScaledWidth();
        int sclH = sr.getScaledHeight();
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(sclW / 2 - 150, sclH / 2 - 120, 0.0F, 0.0F, 300, 220, 300.0F, 220.0F);

        DroneInfo di = this.drone.droneInfo;

        String line1 = TextFormatting.BOLD + di.getDisplayName() + "\n";
        line1 = line1 + TextFormatting.AQUA + "Parts: " + DroneInfo.greekNumber[di.casing] + " , " + DroneInfo.greekNumber[di.chip] + " , " + DroneInfo.greekNumber[di.core] + " , " + DroneInfo.greekNumber[di.engine] + "\n";

        line1 = line1 + TextFormatting.LIGHT_PURPLE + "Pos: " + Math.round(this.drone.posX) + " , " + Math.round(this.drone.posY) + " , " + Math.round(this.drone.posZ) + "\n";
        line1 = line1 + TextFormatting.GREEN + "Battery: " + di.getBattery(true) + "\n";
        line1 = line1 + TextFormatting.YELLOW + "EFT: " + di.getEstimatedFlyTimeString(this.drone);
        List<String> splitLine1 = this.fontRendererObj.listFormattedStringToWidth(line1, 115);
        for (int a = 0; a < splitLine1.size(); a++) {
            this.fontRendererObj.drawString((String)splitLine1.get(a), sclW / 2 - 140, sclH / 2 - 88 + a * 10, 16777215);
        }
        if (this.selectedModGui != null) {
            this.selectedModGui.drawScreen(mouseX, mouseY, partialTicks);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
