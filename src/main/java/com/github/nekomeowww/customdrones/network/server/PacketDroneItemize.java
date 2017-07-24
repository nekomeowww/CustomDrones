package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneSpawn;

public class PacketDroneItemize
        implements IMessage
{
    int droneID;

    public PacketDroneItemize() {}

    public PacketDroneItemize(EntityDrone drone)
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
            extends AbstractServerMessageHandler<PacketDroneItemize>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneItemize message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.getEntityWorld();
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null)
                    {
                        DroneInfo di = drone.droneInfo;
                        ItemStack is = new ItemStack(CustomDrones.droneSpawn);
                        CustomDrones.droneSpawn.setDroneInfo(is, di);
                        drone.setDead();
                        EntityItem ei = new EntityItem(drone.getEntityWorld(), drone.posX, drone.posY, drone.posZ, is);
                        drone.getEntityWorld().spawnEntityInWorld(ei);
                    }
                }
            }
            return null;
        }
    }
}
