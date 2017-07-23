package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.ContainerDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;

public class PacketDroneGuiApplyItem
        implements IMessage
{
    int dim;
    int droneID;

    public PacketDroneGuiApplyItem() {}

    public PacketDroneGuiApplyItem(EntityDrone drone)
    {
        this.dim = drone.field_70170_p.field_73011_w.getDimension();
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
            extends AbstractServerMessageHandler<PacketDroneGuiApplyItem>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneGuiApplyItem message, MessageContext ctx)
        {
            World world = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(message.dim);
            if (world != null)
            {
                EntityDrone drone = EntityDrone.getDroneFromID(world, message.droneID);
                if (drone != null)
                {
                    ContainerDrone c = (ContainerDrone)((player.field_71070_bA instanceof ContainerDrone) ? player.field_71070_bA : null);

                    Slot itemApply = c.func_75147_a(c.module, 0);
                    ItemStack is = itemApply.func_75211_c();
                    if (is != null)
                    {
                        Item i = is.func_77973_b();
                        if (i == DronesMod.droneModule)
                        {
                            drone.droneInfo.applyModule(ItemDroneModule.getModule(is));
                            is.field_77994_a -= 1;
                            if (is.field_77994_a <= 0) {
                                is = null;
                            }
                        }
                        else
                        {
                            is = drone.droneInfo.applyItem(drone, is);
                        }
                    }
                    drone.droneInfo.updateDroneInfoToClient(player);
                    itemApply.func_75215_d(is);
                }
            }
            return null;
        }
    }
}
