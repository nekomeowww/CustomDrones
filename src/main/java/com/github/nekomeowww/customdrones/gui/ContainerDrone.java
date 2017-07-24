package com.github.nekomeowww.customdrones.gui;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import com.github.nekomeowww.customdrones.drone.InventoryDrone;

public class ContainerDrone
        extends Container
{
    public InventoryBasic module;
    public InventoryDrone invDrone;
    public int moduleSlotID;

    public ContainerDrone(IInventory invp, InventoryDrone invd)
    {
        addSlotToContainer(new Slot(this.module = new InventoryBasic("Mod apply", true, 1), 0, 82, 47));
        this.invDrone = invd;
        this.moduleSlotID = (this.inventorySlots.size() - 1);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                addSlotToContainer(new Slot(invp, x + y * 9 + 9, 116 + x * 18, 101 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            addSlotToContainer(new Slot(invp, x, 116 + x * 18, 159));
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++)
            {
                int index = x + y * 9;
                if (index < invd.getSizeInventory()) {
                    addSlotToContainer(new Slot(invd, index, 116 + x * 18, 11 + y * 18));
                }
            }
        }
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(index);
        if ((slot != null) && (slot.getHasStack()))
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (index == 0)
            {
                if (!mergeItemStack(itemstack1, 1, 36, false)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (index <= 36)
            {
                if (!mergeItemStack(itemstack1, 37, 38 + this.invDrone.getSizeInventory() - 1, false)) {
                    return null;
                }
                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (!mergeItemStack(itemstack1, 1, 37, false))
            {
                return null;
            }
            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack)null);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.stackSize == itemstack.stackSize) {
                return null;
            }
            slot.onPickupFromSlot(playerIn, itemstack1);
        }
        return itemstack;
    }

    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return true;
    }

    public void onContainerClosed(EntityPlayer playerIn)
    {
        if (getSlotFromInventory(this.module, 0).getHasStack())
        {
            playerIn.dropItem(getSlotFromInventory(this.module, 0).getStack(), false);
            getSlotFromInventory(this.module, 0).putStack(null);
        }
        super.onContainerClosed(playerIn);
    }
}
