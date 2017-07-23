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
        ItemStack stack = stack0.func_77946_l();
        if (func_70302_i_() <= 0) {
            return false;
        }
        for (int a = 0; a < func_70302_i_(); a++)
        {
            ItemStack is2 = func_70301_a(a);
            if (is2 == null) {
                return true;
            }
            if (ItemStack.func_179545_c(stack, is2))
            {
                int maxAdd = is2.func_77976_d() - is2.field_77994_a;
                int canAdd = Math.min(maxAdd, stack.field_77994_a);
                stack.field_77994_a -= canAdd;
                if ((!mustAddAll) && (canAdd > 0)) {
                    return true;
                }
                if (stack.field_77994_a <= 0) {
                    break;
                }
            }
        }
        return stack.field_77994_a == 0;
    }

    public ItemStack addToInv(ItemStack stack0)
    {
        if ((stack0 == null) || (func_70302_i_() <= 0)) {
            return stack0;
        }
        ItemStack stack = stack0.func_77946_l();
        for (int a = 0; a < func_70302_i_(); a++)
        {
            ItemStack is2 = func_70301_a(a);
            if (ItemStack.func_179545_c(stack, is2))
            {
                int maxAdd = is2.func_77976_d() - is2.field_77994_a;
                int canAdd = Math.min(maxAdd, stack.field_77994_a);
                is2.field_77994_a += canAdd;
                func_70299_a(a, is2);
                stack.field_77994_a -= canAdd;
                if (stack.field_77994_a <= 0)
                {
                    stack = null;
                    break;
                }
            }
        }
        if ((stack != null) && (stack.field_77994_a > 0)) {
            for (int a = 0; a < func_70302_i_(); a++)
            {
                ItemStack is2 = func_70301_a(a);
                if (is2 == null)
                {
                    func_70299_a(a, stack.func_77946_l());
                    stack = null;
                    break;
                }
            }
        }
        this.drone.isChanged = true;
        return stack;
    }

    public void func_70299_a(int index, ItemStack stack)
    {
        if (index >= func_70302_i_()) {
            return;
        }
        this.drone.isChanged = true;
        super.func_70299_a(index, stack);
    }

    public ItemStack func_70301_a(int index)
    {
        if (index >= func_70302_i_()) {
            return null;
        }
        return super.func_70301_a(index);
    }

    public ItemStack func_70298_a(int index, int count)
    {
        if (index >= func_70302_i_()) {
            return null;
        }
        this.drone.isChanged = true;
        return super.func_70298_a(index, count);
    }

    public ItemStack func_70304_b(int index)
    {
        if (index >= func_70302_i_()) {
            return null;
        }
        this.drone.isChanged = true;
        return super.func_70304_b(index);
    }

    public String func_70005_c_()
    {
        return this.drone.name;
    }

    public int func_70302_i_()
    {
        if (!this.drone.hasInventory()) {
            return 0;
        }
        return this.drone.getInvSize();
    }
}
