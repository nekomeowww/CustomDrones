package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDroneSwitchMod
        implements IMessage
{
    int dim;
    int droneID;
    Module mod;
    boolean state;

    public PacketDroneSwitchMod() {}

    public PacketDroneSwitchMod(EntityDrone drone, Module m, boolean d)
    {
        this.dim = drone.field_70170_p.field_73011_w.getDimension();
        this.droneID = drone.getDroneID();
        this.mod = m;
        this.state = d;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        this.mod = Module.getModuleByID(ByteBufUtils.readUTF8String(buffer));
        this.state = buffer.readBoolean();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        ByteBufUtils.writeUTF8String(buffer, this.mod.getID());
        buffer.writeBoolean(this.state);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneSwitchMod>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneSwitchMod message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    drone.droneInfo.switchModule(drone, message.mod, message.state);
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
