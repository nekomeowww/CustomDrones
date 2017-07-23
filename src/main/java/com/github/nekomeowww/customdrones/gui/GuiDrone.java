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
        super(new ContainerDrone(p.field_71071_by, d.droneInfo.inventory), d);
        this.player = p;
        this.drone = d;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.renameField = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 + 81, this.field_146295_m / 2 - 95, 107, 20);
        this.renameField.func_146180_a(this.drone.droneInfo.getDisplayName());
        this.renameField.func_146203_f(20);
        this.field_146292_n.add(new GuiButton(0, this.field_146294_l / 2 + 27, this.field_146295_m / 2 - 95, 50, 20, "Rename"));
        this.field_146292_n.add(this.itemApplyButton = new GuiButton(1, this.field_146294_l / 2 - 75, this.field_146295_m / 2 - 38, 65, 20, "Put item ->"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 - 200, this.field_146295_m / 2 + 80, 50, 20, "Itemize"));
        this.itemApplyButton.field_146124_l = false;
        for (int a = 0; a < this.drone.droneInfo.getMaxModSlots(); a++) {
            this.moduleSlots.add(new SlotModule(a, -46 + a % 4 * 36, 122 + (int)Math.floor(a / 4.0D) * 28, 24, this.drone, a));
        }
    }

    protected void func_73869_a(char typedChar, int keyCode)
            throws IOException
    {
        if (!this.renameField.func_146201_a(typedChar, keyCode)) {
            super.func_73869_a(typedChar, keyCode);
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.renameField.func_146192_a(mouseX, mouseY, mouseButton);
    }

    protected void func_146284_a(GuiButton button)
            throws IOException
    {
        super.func_146284_a(button);
        if (button == this.itemApplyButton)
        {
            this.itemApplyStatus = "";
            Slot applySlot = this.field_147002_h.func_75147_a(((ContainerDrone)this.field_147002_h).module, 0);
            if (applySlot.func_75216_d())
            {
                ItemStack is = applySlot.func_75211_c();
                Item item = is.func_77973_b();
                DroneInfo.ApplyResult addItemEntry = this.drone.droneInfo.canApplyStack(is);
                if (addItemEntry.type != DroneInfo.ApplyType.NONE)
                {
                    this.itemApplyStatus = addItemEntry.displayString;
                    boolean addable = addItemEntry.successful;
                    if (addable)
                    {
                        PacketDispatcher.sendToServer(new PacketDroneGuiApplyItem(this.drone));
                        applySlot.func_75215_d(null);
                    }
                }
            }
        }
        else if ((button.field_146127_k == 0) && (!this.renameField.func_146179_b().isEmpty()))
        {
            PacketDispatcher.sendToServer(new PacketDroneRename(this.drone, this.renameField.func_146179_b()));
        }
        else if (button.field_146127_k == 2)
        {
            PacketDispatcher.sendToServer(new PacketDroneItemize(this.drone));
            this.field_146297_k.func_147108_a(null);
        }
    }

    public void func_73876_c()
    {
        Slot applySlot = this.field_147002_h.func_75147_a(((ContainerDrone)this.field_147002_h).module, 0);
        if (!applySlot.func_75216_d())
        {
            this.itemApplyButton.field_146124_l = false;
            this.itemApplyButton.field_146126_j = "Put item ->";
        }
        else
        {
            ItemStack is = applySlot.func_75211_c();
            Item item = is.func_77973_b();
            if (item == DronesMod.droneModule)
            {
                this.itemApplyButton.field_146124_l = true;
                this.itemApplyButton.field_146126_j = "Install mod";
            }
            else if (this.drone.droneInfo.canApplyStack(is).type != DroneInfo.ApplyType.NONE)
            {
                this.itemApplyButton.field_146124_l = true;
                this.itemApplyButton.field_146126_j = "Apply item";
            }
            else
            {
                this.itemApplyButton.field_146124_l = false;
                this.itemApplyButton.field_146126_j = "No use";
            }
        }
        for (int a = 0; a < this.moduleSlots.size(); a++)
        {
            SlotModule slot = (SlotModule)this.moduleSlots.get(a);
            slot.overlayColor = -1;
            if (applySlot.func_75216_d()) {
                if (applySlot.func_75211_c().func_77973_b() == DronesMod.droneModule)
                {
                    Module installingModule = ItemDroneModule.getModule(applySlot.func_75211_c());
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
        super.func_73876_c();
    }

    public void func_146281_b()
    {
        super.func_146281_b();
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/drone.png");

    public void func_146278_c(int tint) {}

    public void func_146276_q_() {}

    protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 200, sclH / 2 - 100, 0.0F, 0.0F, 400, 200, 400.0F, 200.0F);
        this.renameField.func_146194_f();

        DroneInfo di = this.drone.droneInfo;

        func_73734_a(sclW / 2 + 27, sclH / 2 - 73 + di.getInvSize() * 2, sclW / 2 + 189, sclH / 2 - 1, -3750202);

        this.field_146289_q.func_78276_b("Drone " + TextFormatting.BOLD + di.getDisplayName(), sclW / 2 - 185, sclH / 2 - 90, 0);

        this.field_146289_q.func_78276_b("Drone's", sclW / 2 + 27, sclH / 2 - 71 + di.getInvSize() * 2, 4473924);
        this.field_146289_q.func_78276_b("Player's", sclW / 2 + 147, sclH / 2 + 8, 4473924);

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
        List<String> splitLine1 = this.field_146289_q.func_78271_c(line1, 200);
        for (int a = 0; a < splitLine1.size(); a++) {
            this.field_146289_q.func_78276_b((String)splitLine1.get(a), sclW / 2 - 186, sclH / 2 - 77 + a * 10, 16777215);
        }
        String line2 = "Mods: " + TextFormatting.GREEN + di.mods.size() + "/" + di.getMaxModSlots();
        this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 35, 16777215);
        line2 = "Max mod rank: " + TextFormatting.GREEN + di.getMaxModLevel();
        this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 25, 16777215);
        line2 = "Max speed: " + TextFormatting.GREEN + Math.round(di.getMaxSpeed() * di.getEngineLevel()) + "m/s";
        this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 15, 16777215);
        line2 = "Health: " + TextFormatting.GREEN + di.getDamage(true) + "/" + di.getMaxDamage(this.drone);
        this.field_146289_q.func_78276_b(line2, sclW / 2 - 185, sclH / 2 - 5, 16777215);
        line2 = "Battery: " + TextFormatting.GREEN + di.getBattery(true) + "/" + di.getMaxBattery();
        this.field_146289_q.func_78279_b(line2, sclW / 2 - 185, sclH / 2 + 5, 105, 16777215);

        this.field_146289_q.func_78276_b("Modules", sclW / 2 - 180, sclH / 2 + 60, 0);
        if (!this.itemApplyStatus.isEmpty()) {
            this.field_146289_q.func_78279_b(this.itemApplyStatus, sclW / 2 - 73, sclH / 2 - 8, 80, 4473924);
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}
