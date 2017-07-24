package com.github.nekomeowww.customdrones.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneInfo
        implements IMessage
{
    public DroneInfo droneInfo;

    public PacketDroneInfo() {}

    public PacketDroneInfo(DroneInfo di)
    {
        this.droneInfo = di;
    }

    public void toBytes(ByteBuf buffer)
    {
        NBTTagCompound di = new NBTTagCompound();
        this.droneInfo.writeToNBT(di);
        ByteBufUtils.writeTag(buffer, di);
    }

    public void fromBytes(ByteBuf buffer)
    {
        NBTTagCompound di = ByteBufUtils.readTag(buffer);
        this.droneInfo = DroneInfo.fromNBT(di);
    }

    public static class Handler
            extends AbstractClientMessageHandler<PacketDroneInfo>
    {
        public IMessage handleClientMessage(EntityPlayer player, PacketDroneInfo message, MessageContext ctx)
        {
            EntityDrone drone = EntityDrone.getDroneFromID(player.getEntityWorld(), message.droneInfo.id); //getEntityWorld() used to be world
            if (drone != null) {
                drone.setDroneInfo(message.droneInfo);
            }
            return null;
        }
    }
}
