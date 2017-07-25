package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleBatterySave
        extends Module
{
    public ModuleBatterySave(int l, String s)
    {
        super(l, Module.ModuleType.Recover, s);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (!forGuiDroneStatus) {
            list.add("Save " + Math.round((1.0D - consumptionRate(null)) * 100.0D) + "% battery");
        }
    }

    public double costBatRawPerSec(EntityDrone drone)
    {
        return 0.0D;
    }

    public double consumptionRate(EntityDrone drone)
    {
        if ((drone instanceof EntityDroneMob)) {
            return 0.0D;
        }
        return 1.0D - 0.1D * this.level;
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleBatterySaveGui(gui, this);
    }

    @SideOnly(Side.CLIENT)
    public class ModuleBatterySaveGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        double prevConsumed;
        double prevCost;
        double saved;

        public ModuleBatterySaveGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            double rate = this.parent.drone.droneInfo.getBatteryConsumptionRate(this.parent.drone);
            double prevConsumedNew = this.parent.drone.droneInfo.getMovementBatteryConsumption(this.parent.drone);
            double prevCostNew = prevConsumedNew / rate;
            Module mre1 = this.parent.drone.droneInfo.getModuleWithFunctionOf(Module.heatPower);
            Module mre2 = this.parent.drone.droneInfo.getModuleWithFunctionOf(Module.solarPower);
            if ((mre1 != null) && (this.parent.drone.droneInfo.isEnabled(mre1))) {
                prevConsumedNew -= ((ModuleRecharge)mre1).getRechargeAmount(this.parent.drone);
            }
            if ((mre2 != null) && (mre2 != mre1) && (this.parent.drone.droneInfo.isEnabled(mre2))) {
                prevConsumedNew -= ((ModuleRecharge)mre2).getRechargeAmount(this.parent.drone);
            }
            if (prevConsumedNew < 0.0D) {
                prevConsumedNew = 0.0D;
            }
            double savedNew = prevCostNew - prevConsumedNew;
            if ((this.parent.drone.ticksExisted % 7 == 0) || (Math.abs(prevConsumedNew - this.prevConsumed) >= 0.05D)) {
                this.prevConsumed = prevConsumedNew;
            }
            if ((this.parent.drone.ticksExisted % 7 == 0) || (Math.abs(prevCostNew - this.prevCost) >= 0.05D)) {
                this.prevCost = prevCostNew;
            }
            if ((this.parent.drone.ticksExisted % 7 == 0) || (Math.abs(savedNew - this.saved) >= 0.05D)) {
                this.saved = savedNew;
            }
            double pcnDisplay = Math.round(this.prevConsumed * 20.0D * 100.0D) / 100.0D;
            double pcinDisplay = Math.round(this.prevCost * 20.0D * 100.0D) / 100.0D;
            double sDisplay = Math.round(this.saved * 20.0D * 100.0D) / 100.0D;
            l.add("Battery: " + TextFormatting.GREEN + this.parent.drone.droneInfo.getBattery(true));
            l.add("Consumption rate: " + TextFormatting.GREEN + rate * 100.0D + "%");
            l.add("Battery consumption: " + TextFormatting.RED + pcinDisplay + "/sec");
            l.add("Battery saved: " + TextFormatting.YELLOW + sDisplay + "/sec");
            l.add("Actual battery consumed: " + TextFormatting.GREEN + pcnDisplay + "/sec");
        }
    }
}
