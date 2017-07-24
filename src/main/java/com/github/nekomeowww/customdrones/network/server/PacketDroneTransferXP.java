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
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.drone.module.ModuleCollect;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneTransferXP
        implements IMessage
{
    int dim;
    int droneID;

    public PacketDroneTransferXP() {}

    public PacketDroneTransferXP(EntityDrone drone)
    {
        this.dim = drone.getEntityWorld().provider.getDimension();
        this.droneID = drone.getDroneID();
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneTransferXP>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneTransferXP message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    player.addExperience(((ModuleCollect)drone.droneInfo.getModuleWithFunctionOf(Module.xpCollect))
                            .getCollectedXP(drone));
                    ((ModuleCollect)drone.droneInfo.getModuleWithFunctionOf(Module.xpCollect)).setCollectedXP(drone, 0);

                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
