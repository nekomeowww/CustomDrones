package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.drone.module.ModuleReturn;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneSetReturnPos
        implements IMessage
{
    int droneID;

    public PacketDroneSetReturnPos() {}

    public PacketDroneSetReturnPos(EntityDrone drone)
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
            extends AbstractServerMessageHandler<PacketDroneSetReturnPos>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetReturnPos message, MessageContext ctx)
        {
            World world = player.field_70170_p;
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    ((ModuleReturn)drone.droneInfo.getModuleWithFunctionOf(Module.autoReturn)).setReturnPos(drone, drone
                            .func_174791_d());
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
