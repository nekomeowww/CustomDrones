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

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 115, 90, 20, "Flyer screen"));
        GuiButton b = new GuiButton(1, this.field_146294_l / 2 - 0, this.field_146295_m / 2 - 115, 90, 20, "Remote screen");
        b.field_146124_l = false;
        this.field_146292_n.add(b);

        int mms = this.drone.droneInfo.getMaxModSlots();
        for (int a = 0; a < mms; a++) {
            this.moduleSlots.add(new SlotModule(a, 90 + (a - mms / 2) * 30, 67, 24, this.drone, a));
        }
    }

    protected void func_146284_a(GuiButton button)
            throws IOException
    {
        if ((this.player == null) || (this.player.field_70128_L) || (this.player.func_184614_ca() == null))
        {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        super.func_146284_a(button);
        if (button.field_146127_k == 0)
        {
            EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.player.field_70170_p, this.player.func_184614_ca());
            if (drone != null) {
                this.player.openGui(DronesMod.instance, 0, this.player.field_70170_p, drone.droneInfo.id, 0, 0);
            }
        }
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        if (this.selectedModGui != null) {
            this.selectedModGui.func_73876_c();
        }
    }

    public void func_146274_d()
            throws IOException
    {
        super.func_146274_d();
        if (this.selectedModGui != null) {
            this.selectedModGui.func_146274_d();
        }
    }

    public void func_146282_l()
            throws IOException
    {
        super.func_146282_l();
        if (this.selectedModGui != null) {
            this.selectedModGui.func_146282_l();
        }
    }

    public void modClicked(SlotModule slot, int mX, int mY, int mB)
    {
        super.modClicked(slot, mX, mY, mB);
        if ((slot != null) && (slot.module != null) && (this.selectedModSlot != slot))
        {
            ScaledResolution sr = new ScaledResolution(this.field_146297_k);
            if (this.selectedModSlot != null) {
                this.selectedModSlot.overlayColor = -1;
            }
            this.selectedModSlot = slot;
            this.selectedModGui = slot.module.getModuleGui(this);
            this.selectedModGui.modSlot = slot;
            this.selectedModGui.func_73866_w_();
            this.selectedModGui.func_146280_a(this.field_146297_k, sr.func_78326_a(), sr.func_78328_b());
            if (this.selectedModSlot != null) {
                this.selectedModSlot.overlayColor = -2013265920;
            }
        }
    }

    public void func_146280_a(Minecraft mc, int width, int height)
    {
        super.func_146280_a(mc, width, height);
        if (this.selectedModGui != null) {
            this.selectedModGui.func_146280_a(mc, width, height);
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneStatus.png");

    public void func_146278_c(int tint) {}

    public void func_146276_q_() {}

    protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 150, sclH / 2 - 120, 0.0F, 0.0F, 300, 220, 300.0F, 220.0F);

        DroneInfo di = this.drone.droneInfo;

        String line1 = TextFormatting.BOLD + di.getDisplayName() + "\n";
        line1 = line1 + TextFormatting.AQUA + "Parts: " + DroneInfo.greekNumber[di.casing] + " , " + DroneInfo.greekNumber[di.chip] + " , " + DroneInfo.greekNumber[di.core] + " , " + DroneInfo.greekNumber[di.engine] + "\n";

        line1 = line1 + TextFormatting.LIGHT_PURPLE + "Pos: " + Math.round(this.drone.field_70165_t) + " , " + Math.round(this.drone.field_70163_u) + " , " + Math.round(this.drone.field_70161_v) + "\n";
        line1 = line1 + TextFormatting.GREEN + "Battery: " + di.getBattery(true) + "\n";
        line1 = line1 + TextFormatting.YELLOW + "EFT: " + di.getEstimatedFlyTimeString(this.drone);
        List<String> splitLine1 = this.field_146289_q.func_78271_c(line1, 115);
        for (int a = 0; a < splitLine1.size(); a++) {
            this.field_146289_q.func_78276_b((String)splitLine1.get(a), sclW / 2 - 140, sclH / 2 - 88 + a * 10, 16777215);
        }
        if (this.selectedModGui != null) {
            this.selectedModGui.func_73863_a(mouseX, mouseY, partialTicks);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}
