package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneDropRider
        implements IMessage
{
    int droneID;

    public PacketDroneDropRider() {}

    public PacketDroneDropRider(EntityDrone drone)
    {
        this.droneID = drone.getDroneID();
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.droneID = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.droneID);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneDropRider>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneDropRider message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.worldObj;
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null) {
                        drone.dropRider();
                    }
                }
            }
            return null;
        }
    }
}
