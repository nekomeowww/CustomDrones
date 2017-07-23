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
        func_75146_a(new Slot(this.module = new InventoryBasic("Mod apply", true, 1), 0, 82, 47));
        this.invDrone = invd;
        this.moduleSlotID = (this.field_75151_b.size() - 1);
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                func_75146_a(new Slot(invp, x + y * 9 + 9, 116 + x * 18, 101 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            func_75146_a(new Slot(invp, x, 116 + x * 18, 159));
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 9; x++)
            {
                int index = x + y * 9;
                if (index < invd.func_70302_i_()) {
                    func_75146_a(new Slot(invd, index, 116 + x * 18, 11 + y * 18));
                }
            }
        }
    }

    public ItemStack func_82846_b(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.field_75151_b.get(index);
        if ((slot != null) && (slot.func_75216_d()))
        {
            ItemStack itemstack1 = slot.func_75211_c();
            itemstack = itemstack1.func_77946_l();
            if (index == 0)
            {
                if (!func_75135_a(itemstack1, 1, 36, false)) {
                    return null;
                }
                slot.func_75220_a(itemstack1, itemstack);
            }
            else if (index <= 36)
            {
                if (!func_75135_a(itemstack1, 37, 38 + this.invDrone.func_70302_i_() - 1, false)) {
                    return null;
                }
                slot.func_75220_a(itemstack1, itemstack);
            }
            else if (!func_75135_a(itemstack1, 1, 37, false))
            {
                return null;
            }
            if (itemstack1.field_77994_a == 0) {
                slot.func_75215_d((ItemStack)null);
            } else {
                slot.func_75218_e();
            }
            if (itemstack1.field_77994_a == itemstack.field_77994_a) {
                return null;
            }
            slot.func_82870_a(playerIn, itemstack1);
        }
        return itemstack;
    }

    public boolean func_75145_c(EntityPlayer playerIn)
    {
        return true;
    }

    public void func_75134_a(EntityPlayer playerIn)
    {
        if (func_75147_a(this.module, 0).func_75216_d())
        {
            playerIn.func_71019_a(func_75147_a(this.module, 0).func_75211_c(), false);
            func_75147_a(this.module, 0).func_75215_d(null);
        }
        super.func_75134_a(playerIn);
    }
}
