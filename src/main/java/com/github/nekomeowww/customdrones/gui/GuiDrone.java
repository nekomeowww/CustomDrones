package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.DroneInfo.ApplyResult;
import com.github.nekomeowww.customdrones.drone.DroneInfo.ApplyType;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneGuiApplyItem;
import com.github.nekomeowww.customdrones.network.server.PacketDroneItemize;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRename;

public class GuiDrone
        extends GuiContainerModule
{
    public EntityPlayer player;
    public GuiTextField renameField;
    public GuiButton itemApplyButton;
    public String itemApplyStatus = "";

    public GuiDrone(EntityPlayer p, EntityDrone d)
    {
        super(new ContainerDrone(p.inventory, d.droneInfo.inventory), d);
        this.player = p;
        this.drone = d;
    }

    public void initGui()
    {
        super.initGui();
        this.renameField = new GuiTextField(0, this.fontRendererObj, this.width / 2 + 81, this.height / 2 - 95, 107, 20);
        this.renameField.setText(this.drone.droneInfo.getDisplayName());
        this.renameField.setMaxStringLength(20);
        this.buttonList.add(new GuiButton(0, this.width / 2 + 27, this.height / 2 - 95, 50, 20, "Rename"));
        this.buttonList.add(this.itemApplyButton = new GuiButton(1, this.width / 2 - 75, this.height / 2 - 38, 65, 20, "Put item ->"));
        this.buttonList.add(new GuiButton(2, this.width / 2 - 200, this.height / 2 + 80, 50, 20, "Itemize"));
        this.itemApplyButton.enabled = false;
        for (int a = 0; a < this.drone.droneInfo.getMaxModSlots(); a++) {
            this.moduleSlots.add(new SlotModule(a, -46 + a % 4 * 36, 122 + (int)Math.floor(a / 4.0D) * 28, 24, this.drone, a));
        }
    }

    protected void keyTyped(char typedChar, int keyCode)
            throws IOException
    {
        if (!this.renameField.textboxKeyTyped(typedChar, keyCode)) {
            super.keyTyped(typedChar, keyCode);
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.renameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        super.actionPerformed(button);
        if (button == this.itemApplyButton)
        {
            this.itemApplyStatus = "";
            Slot applySlot = this.inventorySlots.getSlotFromInventory(((ContainerDrone)this.inventorySlots).module, 0);
            if (applySlot.getHasStack())
            {
                ItemStack is = applySlot.getStack();
                Item item = is.getItem();
                DroneInfo.ApplyResult addItemEntry = this.drone.droneInfo.canApplyStack(is);
                if (addItemEntry.type != DroneInfo.ApplyType.NONE)
                {
                    this.itemApplyStatus = addItemEntry.displayString;
                    boolean addable = addItemEntry.successful;
                    if (addable)
                    {
                        PacketDispatcher.sendToServer(new PacketDroneGuiApplyItem(this.drone));
                        applySlot.putStack(null);
                    }
                }
            }
        }
        else if ((button.id == 0) && (!this.renameField.getText().isEmpty()))
        {
            PacketDispatcher.sendToServer(new PacketDroneRename(this.drone, this.renameField.getText()));
        }
        else if (button.id == 2)
        {
            PacketDispatcher.sendToServer(new PacketDroneItemize(this.drone));
            this.mc.displayGuiScreen(null);
        }
    }

    public void updateScreen()
    {
        Slot applySlot = this.inventorySlots.getSlotFromInventory(((ContainerDrone)this.inventorySlots).module, 0);
        if (!applySlot.getHasStack())
        {
            this.itemApplyButton.enabled = false;
            this.itemApplyButton.displayString = "Put item ->";
        }
        else
        {
            ItemStack is = applySlot.getStack();
            Item item = is.getItem();
            if (item == CustomDrones.droneModule)
            {
                this.itemApplyButton.enabled = true;
                this.itemApplyButton.displayString = "Install mod";
            }
            else if (this.drone.droneInfo.canApplyStack(is).type != DroneInfo.ApplyType.NONE)
            {
                this.itemApplyButton.enabled = true;
                this.itemApplyButton.displayString = "Apply item";
            }
            else
            {
                this.itemApplyButton.enabled = false;
                this.itemApplyButton.displayString = "No use";
            }
        }
        for (int a = 0; a < this.moduleSlots.size(); a++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(a);
            slot.overlayColor = -1;
            if (applySlot.getHasStack()) {
                if (applySlot.getStack().getItem() == CustomDrones.droneModule)
                {
                    Module installingModule = ItemDroneModule.getModule(applySlot.getStack());
                    Module thisModule = ((SlotModule)this.moduleSlots.get(a)).module;
                    if (thisModule != null) {
                        if (thisModule.canFunctionAs(installingModule)) {
                            slot.overlayColor = 1157562368;
                        } else if (installingModule.canReplace(thisModule)) {
                            slot.overlayColor = 1140915968;
                        }
                    }
                }
            }
        }
        super.updateScreen();
    }

    public void onGuiClosed()
    {
        super.onGuiClosed();
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/drone.png");

    public void drawBackground(int tint) {}

    public void drawDefaultBackground() {}

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {}

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int sclW = sr.getScaledWidth();
        int sclH = sr.getScaledHeight();
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(sclW / 2 - 200, sclH / 2 - 100, 0.0F, 0.0F, 400, 200, 400.0F, 200.0F);
        this.renameField.drawTextBox();

        DroneInfo di = this.drone.droneInfo;

        drawRect(sclW / 2 + 27, sclH / 2 - 73 + di.getInvSize() * 2, sclW / 2 + 189, sclH / 2 - 1, -3750202);

        this.fontRendererObj.drawString("Drone " + TextFormatting.BOLD + di.getDisplayName(), sclW / 2 - 185, sclH / 2 - 90, 0);

        this.fontRendererObj.drawString("Drone's", sclW / 2 + 27, sclH / 2 - 71 + di.getInvSize() * 2, 4473924);
        this.fontRendererObj.drawString("Player's", sclW / 2 + 147, sclH / 2 + 8, 4473924);

        String line1 = TextFormatting.AQUA + "Casing: " + DroneInfo.greekNumber[di.casing];
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.AQUA + "Chip: " + DroneInfo.greekNumber[di.chip];
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.AQUA + "Core: " + DroneInfo.greekNumber[di.core];
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.AQUA + "Engine: " + DroneInfo.greekNumber[di.engine];
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.YELLOW + "Freq: " + (di.droneFreq >= 0 ? di.droneFreq + "GHz" : "None");
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.YELLOW + "Mode: " + this.drone.getFlyingModeString();
        line1 = line1 + TextFormatting.WHITE + " - ";
        line1 = line1 + TextFormatting.YELLOW + "Est. fly time: " + di.getEstimatedFlyTimeString(this.drone);
        List<String> splitLine1 = this.fontRendererObj.listFormattedStringToWidth(line1, 200);
        for (int a = 0; a < splitLine1.size(); a++) {
            this.fontRendererObj.drawString((String)splitLine1.get(a), sclW / 2 - 186, sclH / 2 - 77 + a * 10, 16777215);
        }
        String line2 = "Mods: " + TextFormatting.GREEN + di.mods.size() + "/" + di.getMaxModSlots();
        this.fontRendererObj.drawString(line2, sclW / 2 - 185, sclH / 2 - 35, 16777215);
        line2 = "Max mod rank: " + TextFormatting.GREEN + di.getMaxModLevel();
        this.fontRendererObj.drawString(line2, sclW / 2 - 185, sclH / 2 - 25, 16777215);
        line2 = "Max speed: " + TextFormatting.GREEN + Math.round(di.getMaxSpeed() * di.getEngineLevel()) + "m/s";
        this.fontRendererObj.drawString(line2, sclW / 2 - 185, sclH / 2 - 15, 16777215);
        line2 = "Health: " + TextFormatting.GREEN + di.getDamage(true) + "/" + di.getMaxDamage(this.drone);
        this.fontRendererObj.drawString(line2, sclW / 2 - 185, sclH / 2 - 5, 16777215);
        line2 = "Battery: " + TextFormatting.GREEN + di.getBattery(true) + "/" + di.getMaxBattery();
        this.fontRendererObj.drawSplitString(line2, sclW / 2 - 185, sclH / 2 + 5, 105, 16777215);

        this.fontRendererObj.drawString("Modules", sclW / 2 - 180, sclH / 2 + 60, 0);
        if (!this.itemApplyStatus.isEmpty()) {
            this.fontRendererObj.drawSplitString(this.itemApplyStatus, sclW / 2 - 73, sclH / 2 - 8, 80, 4473924);
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
