package com.github.nekomeowww.customdrones.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import com.github.nekomeowww.customdrones.drone.InventoryDrone;

public class ContainerDroneStatus
        extends Container
{
    public InventoryBasic module;
    public int moduleSlotID;

    public ContainerDroneStatus(InventoryDrone invd)
    {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++)
            {
                int index = x + y * 9;
                if (index < invd.getSizeInventory()) {
                    addSlotToContainer(new Slot(invd, index, 69 + x * 18, -8 + y * 18));
                }
            }
        }
    }

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
