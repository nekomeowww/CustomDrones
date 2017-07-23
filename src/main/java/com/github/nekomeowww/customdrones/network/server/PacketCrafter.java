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
        this.output.field_77994_a = buffer.readInt();
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
        int stackSize = this.output.field_77994_a;
        this.output.field_77994_a = 1;
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
            InventoryPlayer invp = player.field_71071_by;
            if (!player.field_71075_bZ.field_75098_d) {
                for (Map.Entry<Byte, Byte> entry : message.reduceIndexes) {
                    invp.func_70298_a(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
                }
            }
            ItemStack remain = EntityHelper.addItemStackToInv(invp, message.output);
            if (remain != null)
            {
                EntityItem ei = new EntityItem(player.field_70170_p, player.field_70165_t, player.field_70163_u, player.field_70161_v, remain);
                player.field_70170_p.func_72838_d(ei);
            }
            invp.func_70296_d();
            return null;
        }
    }
}
