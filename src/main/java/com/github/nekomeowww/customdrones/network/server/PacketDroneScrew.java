package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneScrew
        implements IMessage
{
    int dim;
    int droneID;
    int id;

    public PacketDroneScrew() {}

    public PacketDroneScrew(EntityDrone drone, int i)
    {
        this.dim = drone.field_70170_p.field_73011_w.getDimension();
        this.droneID = drone.getDroneID();
        this.id = i;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        this.id = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        buffer.writeInt(this.id);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneScrew>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneScrew message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    drone.droneInfo.appearance.modelID = message.id;
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
