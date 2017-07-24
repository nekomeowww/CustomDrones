package com.github.nekomeowww.customdrones.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneControllingPlayer
        implements IMessage
{
    int droneID;
    String player;

    public PacketDroneControllingPlayer() {}

    public PacketDroneControllingPlayer(EntityDrone drone)
    {
        this.droneID = drone.getDroneID();
        this.player = (drone.getControllingPlayer() == null ? "" : drone.getControllingPlayer().getName());
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.droneID = buffer.readInt();
        this.player = ByteBufUtils.readUTF8String(buffer);
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.droneID);
        ByteBufUtils.writeUTF8String(buffer, this.player);
    }

    public static class Handler
            extends AbstractClientMessageHandler<PacketDroneControllingPlayer>
    {
        public IMessage handleClientMessage(EntityPlayer player, PacketDroneControllingPlayer message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.getEntityWorld(); //getEntityWorld() used to be world
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    EntityPlayer eplayer = world.getPlayerEntityByName(message.player);
                    if (drone != null) {
                        drone.setControllingPlayer(eplayer);
                    }
                }
            }
            return null;
        }
    }
}
