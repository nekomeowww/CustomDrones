package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneRename
        implements IMessage
{
    int dim;
    int droneID;
    String newName;

    public PacketDroneRename() {}

    public PacketDroneRename(EntityDrone drone, String name)
    {
        this.dim = drone.getEntityWorld().provider.getDimension();
        this.droneID = drone.getDroneID();
        this.newName = name;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        this.newName = ByteBufUtils.readUTF8String(buffer);
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        ByteBufUtils.writeUTF8String(buffer, this.newName);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneRename>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneRename message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    drone.droneInfo.setDisplayName(message.newName);
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
