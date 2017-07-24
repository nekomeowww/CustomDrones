package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class PacketDronePaint
        implements IMessage
{
    int dim;
    int droneID;
    List<Map.Entry<String, Color>> entries = new ArrayList();

    public PacketDronePaint() {}

    public PacketDronePaint(EntityDrone drone, String s, Color c)
    {
        this(drone, toList(s, c));
    }

    public PacketDronePaint(EntityDrone drone, List<Map.Entry<String, Color>> es)
    {
        this.dim = drone.world.provider.getDimension();
        this.droneID = drone.getDroneID();
        this.entries = es;
    }

    public static List<Map.Entry<String, Color>> toList(String s, Color c)
    {
        List<Map.Entry<String, Color>> list = new ArrayList();
        list.add(new AbstractMap.SimpleEntry(s, c));
        return list;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.dim = buffer.readInt();
        this.droneID = buffer.readInt();
        int count = buffer.readInt();
        for (int a = 0; a < count; a++)
        {
            long l = buffer.readLong();
            Color color = l > 0L ? new Color(l) : null;
            String partName = ByteBufUtils.readUTF8String(buffer);
            this.entries.add(new AbstractMap.SimpleEntry(partName, color));
        }
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.dim);
        buffer.writeInt(this.droneID);
        int count = this.entries.size();
        buffer.writeInt(count);
        for (int a = 0; a < count; a++)
        {
            String partName = (String)((Map.Entry)this.entries.get(a)).getKey();
            Color color = (Color)((Map.Entry)this.entries.get(a)).getValue();
            if (color != null) {
                buffer.writeLong(color.toLong());
            } else {
                buffer.writeLong(-1L);
            }
            ByteBufUtils.writeUTF8String(buffer, partName);
        }
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDronePaint>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDronePaint message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().worldServerForDimension(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    for (Map.Entry<String, Color> entry : message.entries) {
                        drone.droneInfo.appearance.palette.setPaletteColor((String)entry.getKey(), (Color)entry.getValue());
                    }
                    drone.droneInfo.updateDroneInfoToClient(player);
                }
            }
            return null;
        }
    }
}
