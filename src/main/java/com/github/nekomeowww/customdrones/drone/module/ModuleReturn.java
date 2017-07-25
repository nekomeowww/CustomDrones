package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.InventoryDrone;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetReturnPos;

public class ModuleReturn
        extends Module
{
    public ModuleReturn(int l, String s)
    {
        super(l, Module.ModuleType.Recover, s);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (!forGuiDroneStatus) {
            list.add("Return to designated position on low battery");
        }
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
    }

    public int overridePriority()
    {
        return 100;
    }

    public Vec3d getReturnPos(EntityDrone drone)
    {
        NBTTagCompound tag = getModNBT(drone.droneInfo);
        if ((tag != null) && (tag.hasKey("Return X")) && (tag.hasKey("Return Y")) && (tag.hasKey("Return Z"))) {
            return new Vec3d(tag.getDouble("Return X"), tag.getDouble("Return Y"), tag.getDouble("Return Z"));
        }
        return null;
    }

    public void setReturnPos(EntityDrone drone, Vec3d vec)
    {
        NBTTagCompound tag = getModNBT(drone.droneInfo);
        if (tag != null)
        {
            tag.setDouble("Return X", vec.xCoord);
            tag.setDouble("Return Y", vec.yCoord);
            tag.setDouble("Return Z", vec.zCoord);
        }
    }

    public void overrideDroneMovement(EntityDrone drone)
    {
        super.overrideDroneMovement(drone);
        Vec3d repos = getReturnPos(drone);
        if (repos == null) {
            return;
        }
        double speedMult = drone.getSpeedMultiplication();
        Vec3d dir = VecHelper.fromTo(drone.getPositionVector(), repos);
        if (dir.lengthVector() <= 1.0D)
        {
            int x = (int)Math.floor(drone.posX);
            int y = (int)Math.floor(drone.posY);
            int z = (int)Math.floor(drone.posZ);
            for (int y0 = y; y0 >= y - 1; y0--)
            {
                BlockPos bs = new BlockPos(x, y0, z);
                IInventory iinv = null;
                TileEntity tile0 = drone.getEntityWorld().getTileEntity(bs);
                if ((tile0 instanceof IInventory)) {
                    iinv = (IInventory)tile0;
                } else if (((tile0 instanceof TileEntityEnderChest)) && (drone.getControllingPlayer() != null)) {
                    iinv = drone.getControllingPlayer().getInventoryEnderChest();
                }
                if (iinv != null)
                {
                    for (int a = 0; a < iinv.getSizeInventory(); a++)
                    {
                        ItemStack is1 = iinv.getStackInSlot(a);
                        if (DroneInfo.batteryFuel.containsKey(drone.droneInfo.getItemStackObject(is1)))
                        {
                            is1 = drone.droneInfo.applyItem(drone, is1);
                            if (!drone.droneInfo.hasInventory()) {
                                break;
                            }
                            ItemStack is2 = drone.droneInfo.inventory.addToInv(is1);
                            iinv.setInventorySlotContents(a, is2);
                            break;
                        }
                    }
                    break;
                }
            }
        }
        else
        {
            dir = dir.normalize();
        }
        drone.motionY += dir.yCoord * speedMult;
        drone.motionX += dir.xCoord * speedMult;
        drone.motionZ += dir.zCoord * speedMult;
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        int mode = drone.getFlyingMode();
        double battery = drone.droneInfo.getBattery(false);
        return (battery <= getMinBatteryToReturn(drone)) && (mode != 2);
    }

    public double getMinBatteryToReturn(EntityDrone e)
    {
        Vec3d repos = getReturnPos(e);
        if (repos == null) {
            return 0.0D;
        }
        double dist = e.getDistance(repos.xCoord, repos.yCoord, repos.zCoord);
        double tickLeft = e.droneInfo.getBattery(false) / e.droneInfo.getMovementBatteryConsumption(e);
        double maxSpeed = e.getSpeedMultiplication();
        double tickToFly = dist / maxSpeed;
        double batteryToFly = tickToFly * e.droneInfo.getBattery(false) / tickLeft;
        if ((Double.isInfinite(batteryToFly)) || (Double.isNaN(batteryToFly))) {
            return -1.0D;
        }
        return batteryToFly;
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleReturnGui(gui, this);
    }

    @SideOnly(Side.CLIENT)
    public class ModuleReturnGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleReturnGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void initGui()
        {
            super.initGui();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 24, this.height / 2 + 70, 70, 20, "Return here"));
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if (button.id == 1) {
                PacketDispatcher.sendToServer(new PacketDroneSetReturnPos(this.parent.drone));
            }
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (this.parent.drone.droneInfo.isEnabled(this.mod))
            {
                Vec3d v = ModuleReturn.this.getReturnPos(this.parent.drone);
                if (v != null)
                {
                    double minB = ((ModuleReturn)this.mod).getMinBatteryToReturn(this.parent.drone);
                    if (minB < 0.0D) {
                        l.add(TextFormatting.RED + "Unable to return to [" + (int)v.xCoord + ";" + (int)v.yCoord + ";" + (int)v.zCoord + "]");
                    } else {
                        l.add("Return to " + TextFormatting.GREEN + "[" + (int)v.xCoord + ";" + (int)v.yCoord + ";" + (int)v.zCoord + "]" + TextFormatting.RESET + " on battery less than " + TextFormatting.RED +

                                (int)Math.round(minB * 10.0D) / 10.0D);
                    }
                }
                else
                {
                    l.add(TextFormatting.RED + "" + TextFormatting.ITALIC + "Return coordinates not assigned");
                }
                int x = (int)Math.floor(this.parent.drone.posX);
                int y = (int)Math.floor(this.parent.drone.posY);
                int z = (int)Math.floor(this.parent.drone.posZ);
                for (int y0 = y; y0 >= y - 1; y0--)
                {
                    BlockPos bs = new BlockPos(x, y0, z);
                    IInventory iinv = null;
                    TileEntity tile0 = this.parent.drone.getEntityWorld().getTileEntity(bs);
                    if ((tile0 instanceof IInventory)) {
                        iinv = (IInventory)tile0;
                    } else if (((tile0 instanceof TileEntityEnderChest)) && (this.parent.drone.getControllingPlayer() != null)) {
                        iinv = this.parent.drone.getControllingPlayer().getInventoryEnderChest();
                    }
                    if (iinv != null)
                    {
                        l.add("Drone is on a chest");
                        break;
                    }
                }
                double battery = this.parent.drone.droneInfo.getBattery(true);
                l.add("Current battery: " + TextFormatting.GREEN + battery);
            }
        }
    }
}
