package com.github.nekomeowww.customdrones.drone;

import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;

public class InventoryDrone
        extends InventoryBasic
{
    public DroneInfo drone;

    public InventoryDrone(DroneInfo di)
    {
        super("Drone Inventory", false, 64);
        this.drone = di;
    }

    public boolean canAddToInv(ItemStack stack0, boolean mustAddAll)
    {
        if (stack0 == null) {
            return false;
        }
        ItemStack stack = stack0.copy();
        if (getSizeInventory() <= 0) {
            return false;
        }
        for (int a = 0; a < getSizeInventory(); a++)
        {
            ItemStack is2 = getStackInSlot(a);
            if (is2 == null) {
                return true;
            }
            if (ItemStack.areItemsEqual(stack, is2))
            {
                int maxAdd = is2.getMaxStackSize() - is2.stackSize;
                int canAdd = Math.min(maxAdd, stack.stackSize);
                stack.stackSize -= canAdd;
                if ((!mustAddAll) && (canAdd > 0)) {
                    return true;
                }
                if (stack.stackSize <= 0) {
                    break;
                }
            }
        }
        return stack.stackSize == 0;
    }

    public ItemStack addToInv(ItemStack stack0)
    {
        if ((stack0 == null) || (getSizeInventory() <= 0)) {
            return stack0;
        }
        ItemStack stack = stack0.copy();
        for (int a = 0; a < getSizeInventory(); a++)
        {
            ItemStack is2 = getStackInSlot(a);
            if (ItemStack.areItemsEqual(stack, is2))
            {
                int maxAdd = is2.getMaxStackSize() - is2.stackSize;
                int canAdd = Math.min(maxAdd, stack.stackSize);
                is2.stackSize += canAdd;
                setInventorySlotContents(a, is2);
                stack.stackSize -= canAdd;
                if (stack.stackSize <= 0)
                {
                    stack = null;
                    break;
                }
            }
        }
        if ((stack != null) && (stack.stackSize > 0)) {
            for (int a = 0; a < getSizeInventory(); a++)
            {
                ItemStack is2 = getStackInSlot(a);
                if (is2 == null)
                {
                    setInventorySlotContents(a, stack.copy());
                    stack = null;
                    break;
                }
            }
        }
        this.drone.isChanged = true;
        return stack;
    }

    public void setInventorySlotContents(int index, ItemStack stack)
    {
        if (index >= getSizeInventory()) {
            return;
        }
        this.drone.isChanged = true;
        super.setInventorySlotContents(index, stack);
    }

    public ItemStack getStackInSlot(int index)
    {
        if (index >= getSizeInventory()) {
            return null;
        }
        return super.getStackInSlot(index);
    }

    public ItemStack decrStackSize(int index, int count)
    {
        if (index >= getSizeInventory()) {
            return null;
        }
        this.drone.isChanged = true;
        return super.decrStackSize(index, count);
    }

    public ItemStack removeStackFromSlot(int index)
    {
        if (index >= getSizeInventory()) {
            return null;
        }
        this.drone.isChanged = true;
        return super.removeStackFromSlot(index);
    }

    public String getName()
    {
        return this.drone.name;
    }

    public int getSizeInventory()
    {
        if (!this.drone.hasInventory()) {
            return 0;
        }
        return this.drone.getInvSize();
    }
}
