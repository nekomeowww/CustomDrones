package com.github.nekomeowww.customdrones.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneModuleNBT
        implements IMessage
{
    public int id;
    public NBTTagCompound tag;

    public PacketDroneModuleNBT() {}

    public PacketDroneModuleNBT(DroneInfo di)
    {
        this.id = di.id;
        this.tag = di.modsNBT;
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.id);
        ByteBufUtils.writeTag(buffer, this.tag);
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.id = buffer.readInt();
        this.tag = ByteBufUtils.readTag(buffer);
    }

    public static class Handler
            extends AbstractClientMessageHandler<PacketDroneModuleNBT>
    {
        public IMessage handleClientMessage(EntityPlayer player, PacketDroneModuleNBT message, MessageContext ctx)
        {
            EntityDrone drone = EntityDrone.getDroneFromID(player.getEntityWorld(), message.id); //getEntityWorld() used to be world
            if (drone != null) {
                drone.droneInfo.readModulesNBT(message.tag);
            }
            return null;
        }
    }
}
