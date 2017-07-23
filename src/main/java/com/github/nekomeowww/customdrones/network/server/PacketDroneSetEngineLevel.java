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

public class PacketDroneSetEngineLevel
        implements IMessage
{
    int dim;
    int droneID;
    double engineLevel;

    public PacketDroneSetEngineLevel() {}

    public PacketDroneSetEngineLevel(EntityDrone drone, double el)
    {
        this.dim = drone.field_70170_p.field_73011_w.getDimension();
        this.droneID = drone.getDroneID();
        this.engineLevel = el;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        this.engineLevel = buffer.readDouble();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        buffer.writeDouble(this.engineLevel);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneSetEngineLevel>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetEngineLevel message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    drone.droneInfo.setEngineLevel(message.engineLevel);
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
