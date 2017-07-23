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
            int x = (int)Math.floor(drone.field_70165_t);
            int y = (int)Math.floor(drone.field_70163_u - 1.0D);
            int z = (int)Math.floor(drone.field_70161_v);
            IInventory iinv = null;
            TileEntity tile0 = drone.field_70170_p.func_175625_s(new BlockPos(x, y, z));
            if ((tile0 instanceof IInventory)) {
                iinv = (IInventory)tile0;
            } else if (((tile0 instanceof TileEntityEnderChest)) && (drone.getControllingPlayer() != null)) {
                iinv = drone.getControllingPlayer().func_71005_bN();
            }
            if (iinv != null)
            {
                InventoryDrone droneinv = drone.droneInfo.inventory;
                int stackLimit = iinv.func_70297_j_();
                int invSize = iinv.func_70302_i_();
                for (int a = 0; a < droneinv.func_70302_i_(); a++)
                {
                    ItemStack is0 = droneinv.func_70301_a(a);
                    if (is0 != null) {
                        for (int b = 0; b < invSize; b++)
                        {
                            ItemStack is1 = iinv.func_70301_a(b);
                            if (is1 != null)
                            {
                                int thisStackLimit = Math.min(stackLimit, is1.func_77976_d());
                                if ((ItemStack.func_179545_c(is0, is1)) && (is1.field_77994_a < thisStackLimit))
                                {
                                    int maxAdd = thisStackLimit - is1.field_77994_a;
                                    int canAdd = is0.field_77994_a;
                                    int toAdd = Math.min(maxAdd, canAdd);
                                    is1.field_77994_a += toAdd;
                                    is0.field_77994_a -= toAdd;
                                    iinv.func_70299_a(b, is1);
                                    if (is0.field_77994_a <= 0)
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
                            ItemStack is1 = iinv.func_70301_a(b);
                            if (is1 == null)
                            {
                                is1 = is0.func_77946_l();
                                is0 = null;
                                iinv.func_70299_a(b, is1);
                                droneinv.func_70299_a(a, is0);
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
            super(mod);
        }
    }
}

