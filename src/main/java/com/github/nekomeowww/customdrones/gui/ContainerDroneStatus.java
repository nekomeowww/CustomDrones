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
                if (index < invd.func_70302_i_()) {
                    func_75146_a(new Slot(invd, index, 69 + x * 18, -8 + y * 18));
                }
            }
        }
    }

    public boolean func_75145_c(EntityPlayer playerIn)
    {
        return true;
    }

    public ItemStack func_82846_b(EntityPlayer playerIn, int index)
    {
        return null;
    }

    public ItemStack func_184996_a(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player)
    {
        return null;
    }

    public boolean func_94530_a(ItemStack stack, Slot p_94530_2_)
    {
        return false;
    }

    protected boolean func_75135_a(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection)
    {
        return false;
    }

    public boolean func_94531_b(Slot p_94531_1_)
    {
        return false;
    }

    public void func_75141_a(int slotID, ItemStack stack) {}

    public void func_75131_a(ItemStack[] p_75131_1_) {}
}
