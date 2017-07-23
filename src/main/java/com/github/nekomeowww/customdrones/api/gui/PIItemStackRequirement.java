package com.github.nekomeowww.customdrones.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;

public class PIItemStackRequirement
        extends PIItemStack
{
    public IInventory invp;
    public int require;
    public int has;

    public PIItemStackRequirement(Panel p, IInventory inv, ItemStack item, int r)
    {
        super(p, item);
        this.invp = inv;
        this.is = item.func_77946_l();
        this.is.field_77994_a = 1;
        this.require = item.field_77994_a;
    }

    public void updateItem()
    {
        super.updateItem();
        if (this.is != null)
        {
            this.has = 0;
            for (int a = 0; a < this.invp.func_70302_i_(); a++)
            {
                ItemStack is0 = this.invp.func_70301_a(a);
                if ((ItemStack.func_179545_c(is0, this.is)) && (ItemStack.func_77970_a(is0, this.is))) {
                    this.has += is0.field_77994_a;
                }
            }
        }
        if ((this.has < this.require) && ((this.invp instanceof InventoryPlayer)) && (((InventoryPlayer)this.invp).field_70458_d.func_184812_l_())) {
            this.has = this.require;
        }
    }

    public String getDescStringForIS(ItemStack is)
    {
        return (this.has >= this.require ? TextFormatting.GREEN + "" : "") + this.has + "/" + this.require;
    }
}
