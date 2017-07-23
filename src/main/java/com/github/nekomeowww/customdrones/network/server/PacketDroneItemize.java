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
                World world = player.field_70170_p;
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null)
                    {
                        DroneInfo di = drone.droneInfo;
                        ItemStack is = new ItemStack(DronesMod.droneSpawn);
                        DronesMod.droneSpawn.setDroneInfo(is, di);
                        drone.func_70106_y();
                        EntityItem ei = new EntityItem(drone.field_70170_p, drone.field_70165_t, drone.field_70163_u, drone.field_70161_v, is);
                        drone.field_70170_p.func_72838_d(ei);
                    }
                }
            }
            return null;
        }
    }
}
