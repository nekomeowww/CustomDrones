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
            Minecraft mc = Minecraft.func_71410_x();
            Entity viewEntity = mc != null ? mc.func_175606_aa() : null;
            if ((evn.phase == TickEvent.Phase.START) && ((viewEntity instanceof EntityDrone)))
            {
                mc.func_147108_a(null);
                mc.field_71474_y.field_74319_N = true;
                mc.field_71474_y.field_74320_O = 0;
                if ((Keyboard.isCreated()) && (Keyboard.isKeyDown(1)))
                {
                    ((EntityDrone)mc.func_175606_aa()).setCameraMode(false);
                    mc.field_71474_y.field_74319_N = ModuleCamera.prevHideGui;
                    mc.func_175607_a(ModuleCamera.prevRenderView);
                }
            }
        }
        if ((evn.type == TickEvent.Type.CLIENT) && (evn.phase == TickEvent.Phase.START))
        {
            Minecraft mc = Minecraft.func_71410_x();
            if ((mc != null) && (mc.field_71462_r == null))
            {
                EntityPlayer p = mc.field_71439_g;
                if ((p != null) && (p.func_184614_ca() != null) &&
                        (DronesMod.droneFlyer.getFlyMode(p.func_184614_ca()) == 3))
                {
                    EntityDrone drone = DronesMod.droneFlyer.getControllingDrone(p.field_70170_p, p.func_184614_ca());
                    if ((drone != null) && (drone.isControllerFlying())) {
                        drone.setControllingPlayer(p);
                    }
                    int u = mc.field_71474_y.field_74351_w.func_151470_d() ? 1 : 0;
                    int d = mc.field_71474_y.field_74368_y.func_151470_d() ? 1 : 0;
                    int l = mc.field_71474_y.field_74370_x.func_151470_d() ? 1 : 0;
                    int r = mc.field_71474_y.field_74366_z.func_151470_d() ? 1 : 0;
                    int j = mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : 0;
                    int s = mc.field_71474_y.field_74311_E.func_151470_d() ? 1 : 0;
                    if ((p.func_184218_aH()) && ((p.func_184187_bx() instanceof EntityDrone)))
                    {
                        j = mc.field_71474_y.field_74351_w.func_151470_d() ? 1 : 0;
                        s = mc.field_71474_y.field_74368_y.func_151470_d() ? 1 : 0;
                        u = mc.field_71474_y.field_74314_A.func_151470_d() ? 1 : 0;
                        d = mc.field_71474_y.field_74311_E.func_151470_d() ? 1 : 0;
                    }
                    int buttonCombination = u | d << 1 | l << 2 | r << 3 | j << 4 | s << 5;
                    if (buttonCombination > 0)
                    {
                        PacketDispatcher.sendToServer(new PacketDroneButtonControl(buttonCombination));
                        if ((p instanceof EntityPlayerSP))
                        {
                            if ((((EntityPlayerSP)p).field_71158_b instanceof MovementInputFromOptions)) {
                                playerMovementInput = (MovementInputFromOptions)((EntityPlayerSP)p).field_71158_b;
                            }
                            ((EntityPlayerSP)p).field_71158_b = new MovementInput();
                        }
                    }
                }
                else if ((p instanceof EntityPlayerSP))
                {
                    if (!(((EntityPlayerSP)p).field_71158_b instanceof MovementInputFromOptions)) {
                        ((EntityPlayerSP)p).field_71158_b = playerMovementInput;
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
            DronesMod.configControl.syncConfig();
            DronesMod.configControl.config.save();
        }
    }
}
