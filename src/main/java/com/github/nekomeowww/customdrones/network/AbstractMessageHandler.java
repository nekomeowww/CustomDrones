package com.github.nekomeowww.customdrones.network;

import akka.routing.CustomRouterConfig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.CommonProxy;
import com.github.nekomeowww.customdrones.CustomDrones;

public abstract class AbstractMessageHandler<T extends IMessage>
        implements IMessageHandler<T, IMessage>
{
    @SideOnly(Side.CLIENT)
    public abstract IMessage handleClientMessage(EntityPlayer nameEntityPlayer, T nameT, MessageContext nameMessageContext);

    public abstract IMessage handleServerMessage(EntityPlayer nameEntityPlayer, T nameT, MessageContext nameMessageContext);

    public IMessage onMessage(T message, MessageContext ctx)
    {
        if (ctx.side.isClient()) {
            return handleClientMessage(CustomDrones.proxy.getPlayerEntity(ctx), message, ctx);
        }
        return handleServerMessage(CustomDrones.proxy.getPlayerEntity(ctx), message, ctx);
    }
}
