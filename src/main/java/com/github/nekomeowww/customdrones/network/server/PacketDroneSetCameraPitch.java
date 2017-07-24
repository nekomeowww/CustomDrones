package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneSetCameraPitch
        implements IMessage
{
    int droneID;
    float camera;

    public PacketDroneSetCameraPitch() {}

    public PacketDroneSetCameraPitch(EntityDrone drone, float b)
    {
        this.droneID = drone.getDroneID();
        this.camera = b;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.droneID = buffer.readInt();
        this.camera = buffer.readFloat();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.droneID);
        buffer.writeFloat(this.camera);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneSetCameraPitch>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetCameraPitch message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.getEntityWorld();
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null) {
                        drone.setCameraPitch(message.camera);
                    }
                }
            }
            return null;
        }
    }
}
