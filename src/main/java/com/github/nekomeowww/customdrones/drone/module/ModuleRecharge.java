package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleRecharge
        extends Module
{
    public ModuleRecharge(int l, String s)
    {
        super(l, Module.ModuleType.Recover, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleRechargeGui(gui, this);
    }

    public double costBatRawPerSec(EntityDrone drone)
    {
        return 0.0D;
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        drone.droneInfo.reduceBattery(-getRechargeAmount(drone));
    }

    public double getRechargeAmount(EntityDrone d)
    {
        if (!d.droneInfo.isEnabled(this)) {
            return 0.0D;
        }
        int range = 2 + d.droneInfo.chip;
        double recharge = 0.0D;
        if ((canFunctionAs(solarPower)) && (d.field_70170_p.func_175710_j(d.func_180425_c())))
        {
            double lightClearness = d.field_70170_p.func_175724_o(d.func_180425_c());
            double sunnyness = d.field_70170_p.func_72820_D() % 24000L;
            if (sunnyness < 6000.0D) {
                sunnyness += 0.0D;
            } else if ((sunnyness > 6000.0D) && (sunnyness <= 18000.0D)) {
                sunnyness = 12000.0D - sunnyness;
            } else if (sunnyness < 24000.0D) {
                sunnyness -= 24000.0D;
            }
            sunnyness += 1000.0D;
            double daytimeLightness = Math.max(0.0D, sunnyness / 7000.0D);
            recharge += lightClearness * daytimeLightness * 0.375D * d.droneInfo.chip;
        }
        if (canFunctionAs(heatPower)) {
            for (int x = (int)(d.field_70165_t - range); x <= d.field_70165_t + range; x++) {
                for (int y = (int)(d.field_70163_u - range); y <= d.field_70163_u + range; y++) {
                    for (int z = (int)(d.field_70161_v - range); z <= d.field_70161_v + range; z++)
                    {
                        double distSqr = d.func_70092_e(x + 0.5D, y + 0.5D, z + 0.5D);
                        if (distSqr <= range * range)
                        {
                            Block b = d.field_70170_p.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
                            double heatPower = 0.0D;
                            if (b == Blocks.field_150480_ab) {
                                heatPower = 1.0D;
                            } else if (b == Blocks.field_150356_k) {
                                heatPower = 2.0D;
                            } else if (b == Blocks.field_150353_l) {
                                heatPower = 3.0D;
                            }
                            if (heatPower > 0.0D)
                            {
                                double distHeatRate = 1.0D - distSqr / range / range;
                                recharge += heatPower * distHeatRate / 120.0D;
                            }
                        }
                    }
                }
            }
        }
        return recharge;
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (this == heatPower) {
            list.add("Recharge battery from heat");
        } else if (this == solarPower) {
            list.add("Recharge battery from sunlight");
        } else if (this == multiPower) {
            list.add("Recharge battery from heat and sunlight");
        }
        if (canFunctionAs(heatPower)) {
            list.add("Drone immune to fire");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleRechargeGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleRechargeGui(T gui)
        {
            super(mod);
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (ModuleRecharge.this.canFunctionAs(Module.heatPower))
            {
                int range = 2 + this.parent.drone.droneInfo.chip;
                l.add("Heat range: " + TextFormatting.YELLOW + range * 2 + "x" + range * 2 + "x" + range * 2 + TextFormatting.RESET + " blocks");
            }
            double recharge = ((ModuleRecharge)this.mod).getRechargeAmount(this.parent.drone);
            l.add("Recharge: " + TextFormatting.GREEN + Math.round(recharge * 20.0D * 100.0D) / 100.0D + "/sec");
        }
    }
}
