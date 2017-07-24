package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.path.Path;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.client.PacketDroneControllingPlayer;

public class PacketDroneControllerChange
        implements IMessage
{
    int encoded;

    public PacketDroneControllerChange() {}

    public PacketDroneControllerChange(int i)
    {
        this.encoded = i;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.encoded = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.encoded);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneControllerChange>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneControllerChange message, MessageContext ctx)
        {
            boolean unbindDrone = false;
            EntityDrone drone = CustomDrones.droneFlyer.getControllingDrone(player.worldObj, player.getHeldItemMainhand()); //worldObj used to be world
            if (message.encoded <= 0)
            {
                int newFreq = message.encoded * -1;
                int oldFreq = CustomDrones.droneFlyer.getControllerFreq(player.getHeldItemMainhand());
                if (drone != null)
                {
                    drone.droneInfo.droneFreq = newFreq;
                    drone.droneInfo.isChanged = true;
                }
                CustomDrones.droneFlyer.setControllerFreq(player.getHeldItemMainhand(), newFreq);
            }
            else if (message.encoded == 2)
            {
                CustomDrones.droneFlyer.setNextFlyMode(player.getHeldItemMainhand());
            }
            else if (message.encoded == 3)
            {
                unbindDrone = true;
            }
            else if (message.encoded == 4)
            {
                if (drone != null)
                {
                    drone.setNextFlyingMode();
                    PacketDispatcher.sendTo(new PacketDroneControllingPlayer(drone), (EntityPlayerMP)player);
                }
            }
            else if (message.encoded == 5)
            {
                if (drone != null)
                {
                    drone.recordingPath = new Path();
                    drone.automatedPath = null;
                }
            }
            if (unbindDrone)
            {
                if (drone != null)
                {
                    drone.droneInfo.droneFreq = -1;
                    drone.droneInfo.isChanged = true;
                }
                CustomDrones.droneFlyer.setControllingDrone(player, player.getHeldItemMainhand(), null);
            }
            if ((drone != null) && (drone.droneInfo.isChanged)) {
                drone.droneInfo.updateDroneInfoToClient(player);
            }
            return null;
        }
    }
}
