package com.github.nekomeowww.customdrones.api.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerNothing
extends Container
{
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }


    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        return null;
    }


    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        return null;
    }


    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        return false;
    }


    protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
    {
        return false;
    }


    public boolean canDragIntoSlot(Slot slotIn)
    {
        return false;
    }

    public void putStackInSlot(int slotID, ItemStack stack) {}

    public void putStacksInSlots(ItemStack[] stack) {}
}
