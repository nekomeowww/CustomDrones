package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiSlider;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.path.Path;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneControllerChange;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetEngineLevel;

public class GuiDroneFlyer
        extends GuiScreen
{
    public World world;
    public EntityPlayer player;
    public int frequency;
    public GuiTextField textNewFrequency;
    public GuiButton recordButton;
    public GuiButton statusScreenButton;
    public GuiSlider engineLevelSlider;

    public GuiDroneFlyer(World w, EntityPlayer p)
    {
        this.player = p;
        this.world = this.player.field_70170_p;
    }

    public boolean func_73868_f()
    {
        return false;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());

        this.textNewFrequency = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 88, 44, 20);
        this.textNewFrequency.func_146180_a(String.valueOf(DronesMod.droneFlyer.getControllerFreq(this.player.func_184614_ca())));
        this.textNewFrequency.func_146195_b(true);
        this.textNewFrequency.func_146203_f(6);
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 45, this.field_146295_m / 2 - 88, 55, 20, "Set freq"));
        this.field_146292_n.add(new GuiButton(2, this.field_146294_l / 2 + 16, this.field_146295_m / 2 - 88, 75, 20, "Switch mode"));

        this.field_146292_n.add(new GuiButton(3, this.field_146294_l / 2 - 90, this.field_146295_m / 2 + 68, 75, 20, "Unbind drone"));
        this.field_146292_n.add(new GuiButton(4, this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 68, 100, 20, "Switch drone mode"));

        this.field_146292_n.add(this.recordButton = new GuiButton(5, this.field_146294_l / 2 - 10, this.field_146295_m / 2 + 90, 100, 20, "Record path"));

        this.field_146292_n.add(this.statusScreenButton = new GuiButton(6, this.field_146294_l / 2 + 0, this.field_146295_m / 2 - 110, 90, 20, "Remote screen"));

        this.field_146292_n.add(
                this.engineLevelSlider = new GuiSlider(7, this.field_146294_l / 2 - 90, this.field_146295_m / 2 + 90, 74, 20, "Engine ", "", 0.0D, 1.0D, drone != null ? Math.round(drone.droneInfo.getEngineLevel() * 100.0D) / 100.0D : 1.0D, true, true));
        this.engineLevelSlider.precision = 2;

        GuiButton b = new GuiButton(8, this.field_146294_l / 2 - 90, this.field_146295_m / 2 - 110, 90, 20, "Flyer screen");
        b.field_146124_l = false;
        this.field_146292_n.add(b);
    }

    protected void func_73869_a(char typedChar, int keyCode)
            throws IOException
    {
        super.func_73869_a(typedChar, keyCode);
        if (this.textNewFrequency.func_146206_l()) {
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211)) {
                this.textNewFrequency.func_146201_a(typedChar, keyCode);
            }
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
        if (drone != null) {
            if (drone.droneInfo.getEngineLevel() != this.engineLevelSlider.getValue()) {
                updateEngineLevel(drone);
            }
        }
    }

    protected void func_146286_b(int mouseX, int mouseY, int state)
    {
        super.func_146286_b(mouseX, mouseY, state);
        EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
        if (drone != null) {
            if (drone.droneInfo.getEngineLevel() != this.engineLevelSlider.getValue()) {
                updateEngineLevel(drone);
            }
        }
    }

    public void updateEngineLevel(EntityDrone drone)
    {
        PacketDispatcher.sendToServer(new PacketDroneSetEngineLevel(drone, this.engineLevelSlider.getValue()));
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

        int encode = button.field_146127_k == 1 ? Integer.valueOf(this.textNewFrequency.func_146179_b()).intValue() * -1 : button.field_146127_k;
        PacketDispatcher.sendToServer(new PacketDroneControllerChange(encode));
        if (button == this.recordButton)
        {
            EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
            if (drone != null)
            {
                drone.recordingPath = new Path();
                drone.automatedPath = null;
            }
            EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Right click" + TextFormatting.RESET + " this controller to add back-and-forth path.");

            EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Left click" + TextFormatting.RESET + " to add loop path.");

            this.field_146297_k.func_147108_a(null);
        }
        else if (button == this.statusScreenButton)
        {
            EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
            if (drone != null) {
                this.player.openGui(DronesMod.instance, 2, this.world, drone.droneInfo.id, 0, 0);
            }
        }
    }

    public void func_73876_c()
    {
        super.func_73876_c();
        if ((this.player == null) || (this.player.field_70128_L) || (this.player.func_184614_ca() == null))
        {
            this.field_146297_k.func_147108_a(null);
            return;
        }
        EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, this.player.func_184614_ca());
        if (drone != null)
        {
            int droneModeI = drone.getFlyingMode();
            this.recordButton.field_146124_l = ((droneModeI == 3) || (droneModeI == 2));
            this.statusScreenButton.field_146124_l = true;
        }
        else
        {
            this.recordButton.field_146124_l = false;
            this.statusScreenButton.field_146124_l = false;
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneFlyer.png");

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 100, sclH / 2 - 115, 0.0F, 0.0F, 200, 230, 200.0F, 230.0F);

        super.func_73863_a(mouseX, mouseY, partialTicks);

        this.textNewFrequency.func_146194_f();

        ItemStack flyerIS = this.player.func_184614_ca();
        int frequency = DronesMod.droneFlyer.getControllerFreq(flyerIS);
        String frequencyS = TextFormatting.RESET + "Frequency: " + TextFormatting.BOLD + frequency + "GHz";
        int flyModeI = DronesMod.droneFlyer.getFlyMode(flyerIS);
        String flyMode = TextFormatting.RESET + "Controller mode: " + TextFormatting.BOLD;
        if (flyModeI == 0) {
            flyMode = flyMode + "off";
        } else if (flyModeI == 1) {
            flyMode = flyMode + "targeting";
        } else if (flyModeI == 2) {
            flyMode = flyMode + "directing";
        } else if (flyModeI == 3) {
            flyMode = flyMode + "buttons";
        } else {
            flyMode = flyMode + "unknown";
        }
        String flyModeDesc = TextFormatting.ITALIC + "";
        if (flyModeI == 0) {
            flyModeDesc = flyModeDesc + "";
        } else if (flyModeI == 1) {
            flyModeDesc = flyModeDesc + "Drone flies to crosshair";
        } else if (flyModeI == 2) {
            flyModeDesc = flyModeDesc + "Drone flies to looking direction";
        } else if (flyModeI == 3) {
            flyModeDesc = flyModeDesc + "Control with WASD, Jump, Sneak";
        } else {
            flyModeDesc = flyModeDesc + "";
        }
        func_73731_b(this.field_146289_q, frequencyS, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 62, 16777215);
        func_73731_b(this.field_146289_q, flyMode, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 51, 16777215);
        func_73731_b(this.field_146289_q, flyModeDesc, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 - 40, 16777215);

        func_73732_a(this.field_146289_q, TextFormatting.BOLD + "Controller switches", sr.func_78326_a() / 2, sr
                .func_78328_b() / 2 - 17, 16777215);
        func_73732_a(this.field_146289_q, TextFormatting.BOLD + "Drone switches", sr.func_78326_a() / 2, sr
                .func_78328_b() / 2 + 9, 16777215);

        EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(this.world, flyerIS);
        String controllingDrone = "";
        if (drone == null) {
            controllingDrone = TextFormatting.RED + "Control not bound to drone";
        } else {
            controllingDrone = TextFormatting.RESET + "Controlling drone " + drone.droneInfo.getDisplayName();
        }
        func_73731_b(this.field_146289_q, controllingDrone, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 28, 16777215);
        if (drone != null)
        {
            BlockPos pos = new BlockPos(drone);
            String posStr = "At [" + pos.func_177958_n() + " , " + pos.func_177956_o() + " , " + pos.func_177952_p() + "]";
            posStr = posStr + " - " + Math.floor(drone.func_70032_d(this.player) * 10.0D) / 10.0D + "m away";
            String droneMode = "Drone mode: " + drone.getFlyingModeString();
            func_73731_b(this.field_146289_q, posStr, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 39, 16777215);
            func_73731_b(this.field_146289_q, droneMode, sr.func_78326_a() / 2 - 85, sr.func_78328_b() / 2 + 50, 16777215);
        }
    }
}
