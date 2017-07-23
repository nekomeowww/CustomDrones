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
import com.github.nekomeowww.customdrones.drone.module.ModuleMine;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneSetMineLimits
        implements IMessage
{
    int dim;
    int droneID;
    boolean disable;
    int x0;
    int y0;
    int z0;
    int x1;
    int y1;
    int z1;

    public PacketDroneSetMineLimits() {}

    public PacketDroneSetMineLimits(EntityDrone drone, boolean dis, int a, int b, int c, int d, int e, int f)
    {
        this.dim = drone.field_70170_p.field_73011_w.getDimension();
        this.droneID = drone.getDroneID();
        this.disable = dis;
        this.x0 = a;
        this.y0 = b;
        this.z0 = c;
        this.x1 = d;
        this.y1 = e;
        this.z1 = f;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        this.disable = buffer.readBoolean();
        this.x0 = buffer.readInt();
        this.y0 = buffer.readInt();
        this.z0 = buffer.readInt();
        this.x1 = buffer.readInt();
        this.y1 = buffer.readInt();
        this.z1 = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        buffer.writeBoolean(this.disable);
        buffer.writeInt(this.x0);
        buffer.writeInt(this.y0);
        buffer.writeInt(this.z0);
        buffer.writeInt(this.x1);
        buffer.writeInt(this.y1);
        buffer.writeInt(this.z1);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneSetMineLimits>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSetMineLimits message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    Module m = drone.droneInfo.getModuleWithFunctionOf(Module.mine1);
                    if (message.disable) {
                        ((ModuleMine)m).removeLimits(drone.droneInfo);
                    } else {
                        ((ModuleMine)m).setLimits(drone.droneInfo, message.x0, message.y0, message.z0, message.x1, message.y1, message.z1);
                    }
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
