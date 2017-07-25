package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.InventoryDrone;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleChestDeposit
        extends Module
{
    public ModuleChestDeposit(int l, String s)
    {
        super(l, Module.ModuleType.Collect, s);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        if (drone.droneInfo.hasInventory())
        {
            int x = (int)Math.floor(drone.posX);
            int y = (int)Math.floor(drone.posY - 1.0D);
            int z = (int)Math.floor(drone.posZ);
            IInventory iinv = null;
            TileEntity tile0 = drone.getEntityWorld().getTileEntity(new BlockPos(x, y, z));
            if ((tile0 instanceof IInventory)) {
                iinv = (IInventory)tile0;
            } else if (((tile0 instanceof TileEntityEnderChest)) && (drone.getControllingPlayer() != null)) {
                iinv = drone.getControllingPlayer().getInventoryEnderChest();
            }
            if (iinv != null)
            {
                InventoryDrone droneinv = drone.droneInfo.inventory;
                int stackLimit = iinv.getInventoryStackLimit();
                int invSize = iinv.getSizeInventory();
                for (int a = 0; a < droneinv.getSizeInventory(); a++)
                {
                    ItemStack is0 = droneinv.getStackInSlot(a);
                    if (is0 != null) {
                        for (int b = 0; b < invSize; b++)
                        {
                            ItemStack is1 = iinv.getStackInSlot(b);
                            if (is1 != null)
                            {
                                int thisStackLimit = Math.min(stackLimit, is1.getMaxStackSize());
                                if ((ItemStack.areItemsEqual(is0, is1)) && (is1.stackSize < thisStackLimit))
                                {
                                    int maxAdd = thisStackLimit - is1.stackSize;
                                    int canAdd = is0.stackSize;
                                    int toAdd = Math.min(maxAdd, canAdd);
                                    is1.stackSize += toAdd;
                                    is0.stackSize -= toAdd;
                                    iinv.setInventorySlotContents(b, is1);
                                    if (is0.stackSize <= 0)
                                    {
                                        is0 = null;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (is0 != null) {
                        for (int b = 0; b < invSize; b++)
                        {
                            ItemStack is1 = iinv.getStackInSlot(b);
                            if (is1 == null)
                            {
                                is1 = is0.copy();
                                is0 = null;
                                iinv.setInventorySlotContents(b, is1);
                                droneinv.setInventorySlotContents(a, is0);
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleChestDepositGui(gui, this);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        list.add("Deposit items into chest on collision with");
    }

    @SideOnly(Side.CLIENT)
    public class ModuleChestDepositGui
            extends Module.ModuleGui
    {
        public ModuleChestDepositGui(GuiDroneStatus gui, Module mod)
        {
            super(gui, mod);
        }
    }
}

