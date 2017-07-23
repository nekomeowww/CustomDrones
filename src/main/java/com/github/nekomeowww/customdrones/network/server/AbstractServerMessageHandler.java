package com.github.nekomeowww.customdrones.network.server;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.network.AbstractMessageHandler;

public abstract class AbstractServerMessageHandler<T extends IMessage>
        extends AbstractMessageHandler<T>
{
    public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx)
    {
        return null;
    }
}
