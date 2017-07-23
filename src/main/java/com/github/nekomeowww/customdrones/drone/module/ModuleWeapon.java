package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import williamle.drones.drone.DroneInfo;
import williamle.drones.entity.EntityDrone;
import williamle.drones.gui.GuiDroneStatus;

public class ModuleWeapon
        extends Module
{
    public ModuleWeapon(int l, String s)
    {
        super(l, Module.ModuleType.Battle, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleWeaponGui(gui, this);
    }

    public double getAttackPower(EntityDrone drone)
    {
        return this.level * 1.5D;
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (!forGuiDroneStatus)
        {
            double damage = this.level * 1.5D;
            double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
            list.add("Additional attack: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleWeaponGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleWeaponGui(T gui)
        {
            super(mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            double damage = this.parent.drone.droneInfo.getAttackPower(this.parent.drone);
            double heart = Math.round(damage / 2.0D * 10.0D) / 10.0D;
            l.add("Attack power: " + TextFormatting.RED + heart + " heart" + (heart > 1.0D ? "s" : ""));
            l.add("Damage entities on colliding with");
        }
    }
}
