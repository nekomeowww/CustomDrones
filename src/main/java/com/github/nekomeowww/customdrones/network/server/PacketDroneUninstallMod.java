package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;

public class PacketDroneUninstallMod
        implements IMessage
{
    int droneID;
    Module mod;

    public PacketDroneUninstallMod() {}

    public PacketDroneUninstallMod(EntityDrone drone, Module m)
    {
        this.droneID = drone.getDroneID();
        this.mod = m;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.droneID = buffer.readInt();
        this.mod = Module.getModuleByID(ByteBufUtils.readUTF8String(buffer));
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.droneID);
        ByteBufUtils.writeUTF8String(buffer, this.mod.getID());
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneUninstallMod>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneUninstallMod message, MessageContext ctx)
        {
            if (player != null)
            {
                World world = player.field_70170_p;
                if (world != null)
                {
                    EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                    if (drone != null)
                    {
                        if (drone.droneInfo.modsNBT != null) {
                            drone.droneInfo.modsNBT.func_82580_o("MNBT" + message.mod.getID());
                        }
                        drone.droneInfo.disabledMods.remove(message.mod);
                        drone.droneInfo.mods.remove(message.mod);
                        drone.droneInfo.updateDroneInfoToClient(player);
                        drone.func_70099_a(ItemDroneModule.itemModule(message.mod), 0.0F);
                    }
                }
            }
            return null;
        }
    }
}
