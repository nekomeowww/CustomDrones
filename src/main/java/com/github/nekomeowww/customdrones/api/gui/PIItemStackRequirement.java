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
        this.is = item.copy();
        this.is.stackSize = 1;
        this.require = item.stackSize;
    }

    public void updateItem()
    {
        super.updateItem();
        if (this.is != null)
        {
            this.has = 0;
            for (int a = 0; a < this.invp.getSizeInventory(); a++)
            {
                ItemStack is0 = this.invp.getStackInSlot(a);
                if ((ItemStack.areItemsEqual(is0, this.is)) && (ItemStack.areItemStackTagsEqual(is0, this.is))) {
                    this.has += is0.stackSize;
                }
            }
        }
        if ((this.has < this.require) && ((this.invp instanceof InventoryPlayer)) && (((InventoryPlayer)this.invp).player.isCreative())) {
            this.has = this.require;
        }
    }

    public String getDescStringForIS(ItemStack is)
    {
        return (this.has >= this.require ? TextFormatting.GREEN + "" : "") + this.has + "/" + this.require;
    }
}
