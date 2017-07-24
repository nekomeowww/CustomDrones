package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;

public class PacketDroneButtonControl
        implements IMessage
{
    public int buttonCombination;
    public ItemStack controller;

    public PacketDroneButtonControl() {}

    public PacketDroneButtonControl(int i)
    {
        this.buttonCombination = i;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.buttonCombination = buffer.readInt();
    }

    public void toBytes(ByteBuf buffer)
    {
        buffer.writeInt(this.buttonCombination);
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketDroneButtonControl>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketDroneButtonControl message, MessageContext ctx)
        {
            if (CustomDrones.droneFlyer.getFlyMode(player.getHeldItemMainhand()) == 3) {
                CustomDrones.droneFlyer.flyDroneWithButton(player, message.buttonCombination);
            }
            return null;
        }
    }
}
