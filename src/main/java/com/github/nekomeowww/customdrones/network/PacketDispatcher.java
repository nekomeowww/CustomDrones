package com.github.nekomeowww.customdrones.network;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import com.github.nekomeowww.customdrones.network.client.AbstractClientMessageHandler;
import com.github.nekomeowww.customdrones.network.client.PacketDroneControllingPlayer;
import com.github.nekomeowww.customdrones.network.client.PacketDroneControllingPlayer.Handler;
import com.github.nekomeowww.customdrones.network.client.PacketDroneInfo;
import com.github.nekomeowww.customdrones.network.client.PacketDroneInfo.Handler;
import com.github.nekomeowww.customdrones.network.client.PacketDroneModuleNBT;
import com.github.nekomeowww.customdrones.network.client.PacketDroneModuleNBT.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketCrafter;
import com.github.nekomeowww.customdrones.network.server.PacketCrafter.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneButtonControl;
import com.github.nekomeowww.customdrones.network.server.PacketDroneButtonControl.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneControllerChange;
import com.github.nekomeowww.customdrones.network.server.PacketDroneControllerChange.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneDropRider;
import com.github.nekomeowww.customdrones.network.server.PacketDroneDropRider.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneGuiApplyItem;
import com.github.nekomeowww.customdrones.network.server.PacketDroneGuiApplyItem.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneItemize;
import com.github.nekomeowww.customdrones.network.server.PacketDroneItemize.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDronePaint;
import com.github.nekomeowww.customdrones.network.server.PacketDronePaint.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRename;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRename.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRequireUpdate;
import com.github.nekomeowww.customdrones.network.server.PacketDroneRequireUpdate.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneScrew;
import com.github.nekomeowww.customdrones.network.server.PacketDroneScrew.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraMode;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraMode.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraPitch;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraPitch.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetEngineLevel;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetEngineLevel.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetMineLimits;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetMineLimits.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetReturnPos;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetReturnPos.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSwitchMod;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSwitchMod.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneTransferXP;
import com.github.nekomeowww.customdrones.network.server.PacketDroneTransferXP.Handler;
import com.github.nekomeowww.customdrones.network.server.PacketDroneUninstallMod;
import com.github.nekomeowww.customdrones.network.server.PacketDroneUninstallMod.Handler;

public class PacketDispatcher
{
    private static byte packetId = 0;
    private static final SimpleNetworkWrapper dispatcher = NetworkRegistry.INSTANCE.newSimpleChannel("drones");

    public static final void registerPackets()
    {
        registerMessage(PacketDroneButtonControl.Handler.class, PacketDroneButtonControl.class);
        registerMessage(PacketDroneInfo.Handler.class, PacketDroneInfo.class);
        registerMessage(PacketDroneControllerChange.Handler.class, PacketDroneControllerChange.class);
        registerMessage(PacketDroneRename.Handler.class, PacketDroneRename.class);
        registerMessage(PacketDroneSetEngineLevel.Handler.class, PacketDroneSetEngineLevel.class);
        registerMessage(PacketDroneGuiApplyItem.Handler.class, PacketDroneGuiApplyItem.class);
        registerMessage(PacketDroneSwitchMod.Handler.class, PacketDroneSwitchMod.class);
        registerMessage(PacketDroneTransferXP.Handler.class, PacketDroneTransferXP.class);
        registerMessage(PacketDroneRequireUpdate.Handler.class, PacketDroneRequireUpdate.class);
        registerMessage(PacketDroneDropRider.Handler.class, PacketDroneDropRider.class);
        registerMessage(PacketDroneControllingPlayer.Handler.class, PacketDroneControllingPlayer.class);
        registerMessage(PacketDroneSetReturnPos.Handler.class, PacketDroneSetReturnPos.class);
        registerMessage(PacketCrafter.Handler.class, PacketCrafter.class);
        registerMessage(PacketDroneSetCameraMode.Handler.class, PacketDroneSetCameraMode.class);
        registerMessage(PacketDroneItemize.Handler.class, PacketDroneItemize.class);
        registerMessage(PacketDroneUninstallMod.Handler.class, PacketDroneUninstallMod.class);
        registerMessage(PacketDroneSetMineLimits.Handler.class, PacketDroneSetMineLimits.class);
        registerMessage(PacketDroneSetCameraPitch.Handler.class, PacketDroneSetCameraPitch.class);
        registerMessage(PacketDroneModuleNBT.Handler.class, PacketDroneModuleNBT.class);
        registerMessage(PacketDronePaint.Handler.class, PacketDronePaint.class);
        registerMessage(PacketDroneScrew.Handler.class, PacketDroneScrew.class);
    }

    private static final <REQ extends IMessage, REPLY extends IMessage> void registerMessage(Class<? extends IMessageHandler<REQ, REPLY>> handlerClass, Class<REQ> messageClass)
    {
        Side side = AbstractClientMessageHandler.class.isAssignableFrom(handlerClass) ? Side.CLIENT : Side.SERVER;
        dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
    }

    private static final void registerMessage(Class handlerClass, Class messageClass, Side side)
    {
        dispatcher.registerMessage(handlerClass, messageClass, packetId++, side);
    }

    public static final void sendTo(IMessage message, EntityPlayerMP player)
    {
        dispatcher.sendTo(message, player);
    }

    public static final void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
    {
        dispatcher.sendToAllAround(message, point);
    }

    public static final void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range)
    {
        sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
    }

    public static final void sendToAllAround(IMessage message, EntityPlayer player, double range)
    {
        sendToAllAround(message, player.field_70170_p.field_73011_w.getDimension(), player.field_70165_t, player.field_70163_u, player.field_70161_v, range);
    }

    public static final void sendToDimension(IMessage message, int dimensionId)
    {
        dispatcher.sendToDimension(message, dimensionId);
    }

    public static final void sendToServer(IMessage message)
    {
        dispatcher.sendToServer(message);
    }
}
