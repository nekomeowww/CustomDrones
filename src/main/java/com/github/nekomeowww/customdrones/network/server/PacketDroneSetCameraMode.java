package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneSetCameraMode
        implements IMessage
{
    int droneID;
    boolean camera;

    public PacketDroneSetCameraMode() {}

    public PacketDroneSetCameraMode(EntityDrone drone, boolean b)
    {
        this.droneID = drone.getDroneID();
        this.camera = b;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.droneID = buffer.readInt();
        this.camera = buffer.readBoolean();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.droneID);
        buffer.writeBoolean(this.camera);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneSetCameraMode>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetCameraMode message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.field_70170_p;
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null) {
                        drone.setCameraMode(message.camera);
                    }
                }
            }
            return null;
        }
    }
}
