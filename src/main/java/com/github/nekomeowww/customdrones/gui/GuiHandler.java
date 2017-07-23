package com.github.nekomeowww.customdrones.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import com.github.nekomeowww.customdrones.api.gui.ContainerNothing;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class GuiHandler
        implements IGuiHandler
{
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID != 0) {
            if (ID == 1)
            {
                EntityDrone d = EntityDrone.getDroneFromID(world, x);
                if (d != null)
                {
                    d.droneInfo.updateDroneInfoToClient(player);
                    return new ContainerDrone(player.field_71071_by, d.droneInfo.inventory);
                }
            }
            else if (ID == 2)
            {
                EntityDrone d = EntityDrone.getDroneFromID(world, x);
                if (d != null) {
                    return new ContainerDroneStatus(d.droneInfo.inventory);
                }
            }
            else
            {
                if (ID == 3) {
                    return new ContainerCrafter(player.field_71071_by);
                }
                if (ID == 4) {
                    return new ContainerNothing();
                }
                if (ID == 5) {
                    return new ContainerNothing();
                }
            }
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == 0) {
            return new GuiDroneFlyer(world, player);
        }
        if (ID == 1)
        {
            EntityDrone d = EntityDrone.getDroneFromID(world, x);
            if (d != null) {
                return new GuiDrone(player, d);
            }
        }
        else if (ID == 2)
        {
            EntityDrone d = EntityDrone.getDroneFromID(world, x);
            if (d != null) {
                return new GuiDroneStatus(player, d);
            }
        }
        else
        {
            if (ID == 3) {
                return new GuiCrafter(player);
            }
            if (ID == 4) {
                return new GuiPainter(EntityDrone.getDroneFromID(world, x));
            }
            if (ID == 5) {
                return new GuiScrew(EntityDrone.getDroneFromID(world, x));
            }
        }
        return null;
    }
}
