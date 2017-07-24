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
        this.world = this.player.getEntityWorld();
    }

    public boolean doesGuiPauseGame()
    {
        return false;
    }

    public void initGui()
    {
        super.initGui();
        EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());

        this.textNewFrequency = new GuiTextField(0, this.fontRendererObj, this.width / 2 - 90, this.height / 2 - 88, 44, 20);
        this.textNewFrequency.setText(String.valueOf(CustomDrones.droneFlyer.getControllerFreq(this.player.getHeldItemMainhand())));
        this.textNewFrequency.setFocused(true);
        this.textNewFrequency.setMaxStringLength(6);
        this.buttonList.add(new GuiButton(1, this.width / 2 - 45, this.height / 2 - 88, 55, 20, "Set freq"));
        this.buttonList.add(new GuiButton(2, this.width / 2 + 16, this.height / 2 - 88, 75, 20, "Switch mode"));

        this.buttonList.add(new GuiButton(3, this.width / 2 - 90, this.height / 2 + 68, 75, 20, "Unbind drone"));
        this.buttonList.add(new GuiButton(4, this.width / 2 - 10, this.height / 2 + 68, 100, 20, "Switch drone mode"));

        this.buttonList.add(this.recordButton = new GuiButton(5, this.width / 2 - 10, this.height / 2 + 90, 100, 20, "Record path"));

        this.buttonList.add(this.statusScreenButton = new GuiButton(6, this.width / 2 + 0, this.height / 2 - 110, 90, 20, "Remote screen"));

        this.buttonList.add(
                this.engineLevelSlider = new GuiSlider(7, this.width / 2 - 90, this.height / 2 + 90, 74, 20, "Engine ", "", 0.0D, 1.0D, drone != null ? Math.round(drone.droneInfo.getEngineLevel() * 100.0D) / 100.0D : 1.0D, true, true));
        this.engineLevelSlider.precision = 2;

        GuiButton b = new GuiButton(8, this.width / 2 - 90, this.height / 2 - 110, 90, 20, "Flyer screen");
        b.enabled = false;
        this.buttonList.add(b);
    }

    protected void keyTyped(char typedChar, int keyCode)
            throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        if (this.textNewFrequency.isFocused()) {
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211)) {
                this.textNewFrequency.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());
        if (drone != null) {
            if (drone.droneInfo.getEngineLevel() != this.engineLevelSlider.getValue()) {
                updateEngineLevel(drone);
            }
        }
    }

    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());
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

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        if ((this.player == null) || (this.player.isDead) || (this.player.getHeldItemMainhand() == null))
        {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.actionPerformed(button);

        int encode = button.id == 1 ? Integer.valueOf(this.textNewFrequency.getText()).intValue() * -1 : button.id;
        PacketDispatcher.sendToServer(new PacketDroneControllerChange(encode));
        if (button == this.recordButton)
        {
            EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());
            if (drone != null)
            {
                drone.recordingPath = new Path();
                drone.automatedPath = null;
            }
            EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Right click" + TextFormatting.RESET + " this controller to add back-and-forth path.");

            EntityHelper.addChat(this.player, 1, TextFormatting.BOLD + "Left click" + TextFormatting.RESET + " to add loop path.");

            this.mc.displayGuiScreen(null);
        }
        else if (button == this.statusScreenButton)
        {
            EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());
            if (drone != null) {
                this.player.openGui(CustomDrones.instance, 2, this.world, drone.droneInfo.id, 0, 0);
            }
        }
    }

    public void updateScreen()
    {
        super.updateScreen();
        if ((this.player == null) || (this.player.isDead) || (this.player.getHeldItemMainhand() == null))
        {
            this.mc.displayGuiScreen(null);
            return;
        }
        EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, this.player.getHeldItemMainhand());
        if (drone != null)
        {
            int droneModeI = drone.getFlyingMode();
            this.recordButton.enabled = ((droneModeI == 3) || (droneModeI == 2));
            this.statusScreenButton.enabled = true;
        }
        else
        {
            this.recordButton.enabled = false;
            this.statusScreenButton.enabled = false;
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/droneFlyer.png");

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int sclW = sr.getScaledWidth();
        int sclH = sr.getScaledHeight();
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(sclW / 2 - 100, sclH / 2 - 115, 0.0F, 0.0F, 200, 230, 200.0F, 230.0F);

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.textNewFrequency.drawTextBox();

        ItemStack flyerIS = this.player.getHeldItemMainhand();
        int frequency = CustomDrones.droneFlyer.getControllerFreq(flyerIS);
        String frequencyS = TextFormatting.RESET + "Frequency: " + TextFormatting.BOLD + frequency + "GHz";
        int flyModeI = CustomDrones.droneFlyer.getFlyMode(flyerIS);
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
        drawString(this.fontRendererObj, frequencyS, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 - 62, 16777215);
        drawString(this.fontRendererObj, flyMode, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 - 51, 16777215);
        drawString(this.fontRendererObj, flyModeDesc, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 - 40, 16777215);

        drawCenteredString(this.fontRendererObj, TextFormatting.BOLD + "Controller switches", sr.getScaledWidth() / 2, sr
                .getScaledHeight() / 2 - 17, 16777215);
        drawCenteredString(this.fontRendererObj, TextFormatting.BOLD + "Drone switches", sr.getScaledWidth() / 2, sr
                .getScaledHeight() / 2 + 9, 16777215);

        EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(this.world, flyerIS);
        String controllingDrone = "";
        if (drone == null) {
            controllingDrone = TextFormatting.RED + "Control not bound to drone";
        } else {
            controllingDrone = TextFormatting.RESET + "Controlling drone " + drone.droneInfo.getDisplayName();
        }
        drawString(this.fontRendererObj, controllingDrone, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 + 28, 16777215);
        if (drone != null)
        {
            BlockPos pos = new BlockPos(drone);
            String posStr = "At [" + pos.getX() + " , " + pos.getY() + " , " + pos.getZ() + "]";
            posStr = posStr + " - " + Math.floor(drone.getDistanceToEntity(this.player) * 10.0D) / 10.0D + "m away";
            String droneMode = "Drone mode: " + drone.getFlyingModeString();
            drawString(this.fontRendererObj, posStr, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 + 39, 16777215);
            drawString(this.fontRendererObj, droneMode, sr.getScaledWidth() / 2 - 85, sr.getScaledHeight() / 2 + 50, 16777215);
        }
    }
}
