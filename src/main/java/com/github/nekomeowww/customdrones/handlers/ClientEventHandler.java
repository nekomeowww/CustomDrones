package com.github.nekomeowww.customdrones.handlers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovementInput;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;
import com.github.nekomeowww.customdrones.ConfigControl;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.module.ModuleCamera;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneButtonControl;

@SideOnly(Side.CLIENT)
public class ClientEventHandler
{
    public static MovementInputFromOptions playerMovementInput;

    @SubscribeEvent(priority=EventPriority.LOWEST)
    public void tick(TickEvent evn)
    {
        if (evn.type == TickEvent.Type.RENDER)
        {
            Minecraft mc = Minecraft.getMinecraft();
            Entity viewEntity = mc != null ? mc.getRenderViewEntity() : null;
            if ((evn.phase == TickEvent.Phase.START) && ((viewEntity instanceof EntityDrone)))
            {
                mc.displayGuiScreen(null);
                mc.gameSettings.hideGUI = true;
                mc.gameSettings.thirdPersonView = 0;
                if ((Keyboard.isCreated()) && (Keyboard.isKeyDown(1)))
                {
                    ((EntityDrone)mc.getRenderViewEntity()).setCameraMode(false);
                    mc.gameSettings.hideGUI = ModuleCamera.prevHideGui;
                    mc.setRenderViewEntity(ModuleCamera.prevRenderView);
                }
            }
        }
        if ((evn.type == TickEvent.Type.CLIENT) && (evn.phase == TickEvent.Phase.START))
        {
            Minecraft mc = Minecraft.getMinecraft();
            if ((mc != null) && (mc.currentScreen == null))
            {
                EntityPlayer p = mc.thePlayer; //thePlayer used to be player
                if ((p != null) && (p.getHeldItemMainhand() != null) &&
                        (CustomDrones.droneFlyer.getFlyMode(p.getHeldItemMainhand()) == 3))
                {
                    EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(p.getEntityWorld(), p.getHeldItemMainhand());
                    if ((drone != null) && (drone.isControllerFlying())) {
                        drone.setControllingPlayer(p);
                    }
                    int u = mc.gameSettings.keyBindForward.isKeyDown() ? 1 : 0;
                    int d = mc.gameSettings.keyBindBack.isKeyDown() ? 1 : 0;
                    int l = mc.gameSettings.keyBindLeft.isKeyDown() ? 1 : 0;
                    int r = mc.gameSettings.keyBindRight.isKeyDown() ? 1 : 0;
                    int j = mc.gameSettings.keyBindJump.isKeyDown() ? 1 : 0;
                    int s = mc.gameSettings.keyBindSneak.isKeyDown() ? 1 : 0;
                    if ((p.isRiding()) && ((p.getRidingEntity() instanceof EntityDrone)))
                    {
                        j = mc.gameSettings.keyBindForward.isKeyDown() ? 1 : 0;
                        s = mc.gameSettings.keyBindBack.isKeyDown() ? 1 : 0;
                        u = mc.gameSettings.keyBindJump.isKeyDown() ? 1 : 0;
                        d = mc.gameSettings.keyBindSneak.isKeyDown() ? 1 : 0;
                    }
                    int buttonCombination = u | d << 1 | l << 2 | r << 3 | j << 4 | s << 5;
                    if (buttonCombination > 0)
                    {
                        PacketDispatcher.sendToServer(new PacketDroneButtonControl(buttonCombination));
                        if ((p instanceof EntityPlayerSP))
                        {
                            if ((((EntityPlayerSP)p).movementInput instanceof MovementInputFromOptions)) {
                                playerMovementInput = (MovementInputFromOptions)((EntityPlayerSP)p).movementInput;
                            }
                            ((EntityPlayerSP)p).movementInput = new MovementInput();
                        }
                    }
                }
                else if ((p instanceof EntityPlayerSP))
                {
                    if (!(((EntityPlayerSP)p).movementInput instanceof MovementInputFromOptions)) {
                        ((EntityPlayerSP)p).movementInput = playerMovementInput;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void configChanged(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if ((event.getModID() == "drones") && (event.getConfigID() == ConfigControl.CONFIGID))
        {
            CustomDrones.configControl.syncConfig();
            CustomDrones.configControl.config.save();
        }
    }
}
