package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.client.PacketDroneControllingPlayer;

public class PacketDroneRequireUpdate
        implements IMessage
{
    int droneID;

    public PacketDroneRequireUpdate() {}

    public PacketDroneRequireUpdate(EntityDrone drone)
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
            extends AbstractServerMessageHandler<PacketDroneRequireUpdate>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneRequireUpdate message, MessageContext ctx)
        {
            World world = player.field_70170_p;
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    drone.droneInfo.updateDroneInfoToClient(player);
                    PacketDispatcher.sendTo(new PacketDroneControllingPlayer(drone), (EntityPlayerMP)player);
                }
            }
            return null;
        }
    }
}
