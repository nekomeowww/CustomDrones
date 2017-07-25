package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.drone.module.Module;

public class ModuleArmor
        extends Module
{
    public ModuleArmor(int l, String s)
    {
        super(l, Module.ModuleType.Battle, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleArmorGui(gui, this);
    }

    public double getDamageReduction(EntityDrone e)
    {
        return this.level * 0.1D;
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (!forGuiDroneStatus) {
            list.add("Damage reduction: " + TextFormatting.RED + this.level * 10 + "%");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleArmorGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleArmorGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            double damage = this.parent.drone.droneInfo.getDamage(true);
            int heart = (int)Math.round(damage / 2.0D);
            l.add("Health: " + TextFormatting.RED + damage + TextFormatting.RESET + " (" + heart + " heart" + (heart > 1 ? "s" : "") + ")");

            l.add("Damage reduction: " + TextFormatting.RED + this.parent.drone.droneInfo
                    .getDamageReduction(this.parent.drone) * 100.0D + "%");
        }
    }
}
