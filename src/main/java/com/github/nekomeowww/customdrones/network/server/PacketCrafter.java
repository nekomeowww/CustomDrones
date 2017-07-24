package com.github.nekomeowww.customdrones.network.server;

import io.netty.buffer.ByteBuf;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;

public class PacketCrafter
        implements IMessage
{
    ItemStack output;
    List<Map.Entry<Byte, Byte>> reduceIndexes;

    public PacketCrafter() {}

    public PacketCrafter(ItemStack output, List<Map.Entry<Byte, Byte>> reduceIndexes)
    {
        this.output = output;
        this.reduceIndexes = reduceIndexes;
    }

    public void fromBytes(ByteBuf buffer)
    {
        this.reduceIndexes = new ArrayList();
        this.output = ByteBufUtils.readItemStack(buffer);
        this.output.stackSize = buffer.readInt();
        int indexesCount = buffer.readInt();
        for (int a = 0; a < indexesCount; a++)
        {
            Byte index = Byte.valueOf(buffer.readByte());
            Byte count = Byte.valueOf(buffer.readByte());
            this.reduceIndexes.add(new AbstractMap.SimpleEntry(index, count));
        }
    }

    public void toBytes(ByteBuf buffer)
    {
        int stackSize = this.output.stackSize;
        this.output.stackSize = 1;
        ByteBufUtils.writeItemStack(buffer, this.output);
        buffer.writeInt(stackSize);
        buffer.writeInt(this.reduceIndexes.size());
        for (int a = 0; a < this.reduceIndexes.size(); a++)
        {
            Map.Entry<Byte, Byte> entry = (Map.Entry)this.reduceIndexes.get(a);
            if (entry != null)
            {
                buffer.writeByte(((Byte)entry.getKey()).byteValue());
                buffer.writeByte(((Byte)entry.getValue()).byteValue());
            }
        }
    }

    public static class Handler
            extends AbstractServerMessageHandler<PacketCrafter>
    {
        public IMessage handleServerMessage(EntityPlayer player, PacketCrafter message, MessageContext ctx)
        {
            InventoryPlayer invp = player.inventory;
            if (!player.capabilities.isCreativeMode) {
                for (Map.Entry<Byte, Byte> entry : message.reduceIndexes) {
                    invp.decrStackSize(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
                }
            }
            ItemStack remain = EntityHelper.addItemStackToInv(invp, message.output);
            if (remain != null)
            {
                EntityItem ei = new EntityItem(player.getEntityWorld(), player.posX, player.posY, player.posZ, remain);
                player.getEntityWorld().spawnEntityInWorld(ei);
            }
            invp.markDirty();
            return null;
        }
    }
}
