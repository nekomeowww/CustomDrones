package com.github.nekomeowww.customdrones.item;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneBaby;
import com.github.nekomeowww.customdrones.entity.EntityDroneBabyBig;
import com.github.nekomeowww.customdrones.entity.EntityDroneMob;
import com.github.nekomeowww.customdrones.entity.EntityDroneWildItem;

public class ItemDroneSpawn
        extends Item
{
    public ItemDroneSpawn()
    {
        func_77625_d(1);
    }

    public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.field_72995_K)
        {
            DroneInfo di = getDroneInfo(stack).copy();
            boolean allSameHigh = (di.chip == di.core) && (di.core == di.casing) && (di.casing == di.engine) && (di.chip > 1);
            if ((allSameHigh) && (playerIn.field_71075_bZ.field_75098_d) && (playerIn.func_70093_af()))
            {
                EntityDroneMob drone = null;
                if (di.chip == 2) {
                    drone = new EntityDroneBaby(worldIn);
                } else if (di.chip == 3) {
                    drone = new EntityDroneBabyBig(worldIn);
                } else if (di.chip == 4) {
                    drone = new EntityDroneWildItem(worldIn);
                }
                if (drone != null)
                {
                    drone.setDroneInfo(new DroneInfo(drone).newID());
                    drone.func_70107_b(pos.func_177958_n() + 0.5D, pos.func_177956_o() + 1, pos.func_177952_p() + 0.5D);
                    drone.onInitSpawn();
                    worldIn.func_72838_d(drone);
                }
            }
            else
            {
                EntityDrone drone = new EntityDrone(worldIn);
                drone.setDroneInfo(getDroneInfo(stack).newID());
                drone.func_70107_b(pos.func_177958_n() + 0.5D, pos.func_177956_o() + 1, pos.func_177952_p() + 0.5D);
                worldIn.func_72838_d(drone);
                stack.field_77994_a -= 1;
            }
        }
        return EnumActionResult.SUCCESS;
    }

    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.func_77624_a(stack, playerIn, tooltip, advanced);
        DroneInfo di = getDroneInfo(stack);
        if (!di.name.equals("#$Drone"))
        {
            String s = (String)tooltip.get(0) + TextFormatting.AQUA + " " + TextFormatting.BOLD + di.getDisplayName();
            tooltip.set(0, s);
        }
        tooltip.add(TextFormatting.BLUE + "Casing: " + DroneInfo.greekNumber[di.casing] + " / " + "Chip: " + DroneInfo.greekNumber[di.chip] + " / " + "Core: " + DroneInfo.greekNumber[di.core] + " / " + "Engine: " + DroneInfo.greekNumber[di.engine]);

        TextFormatting form = di.mods.size() == di.getMaxModSlots() ? TextFormatting.RED : TextFormatting.GREEN;
        tooltip.add(form + "Installed mods: " + di.mods.size() + "/" + di.getMaxModSlots());
        form = di.getBattery(false) / di.getMaxBattery() <= 0.25D ? TextFormatting.RED : TextFormatting.GREEN;
        tooltip.add(form + "Battery: " + di.getBattery(true) + "/" + di.getMaxBattery());
        form = di.getDamage(false) / di.getMaxDamage(null) <= 0.25D ? TextFormatting.RED : TextFormatting.GREEN;
        tooltip.add(form + "Durabilty: " + di.getDamage(true) + "/" + di.getMaxDamage(null));
        tooltip.add(TextFormatting.GREEN + "Speed: " + di.getMaxSpeed() + "m/s");
    }

    public void setDroneInfo(ItemStack stack, DroneInfo info)
    {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        info.writeToNBT(stack.func_77978_p());
    }

    public DroneInfo getDroneInfo(ItemStack stack)
    {
        if (stack.func_77942_o()) {
            return DroneInfo.fromNBT(stack.func_77978_p());
        }
        stack.func_77982_d(new NBTTagCompound());
        return new DroneInfo();
    }

    public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        super.func_150895_a(itemIn, tab, subItems);
        if ((tab == DronesMod.droneTab) && (FMLClientHandler.instance().getClient().field_71474_y.field_82882_x)) {
            for (int a = 1; a < 5; a++) {
                for (int b = 1; b < 5; b++) {
                    for (int c = 1; c < 5; c++) {
                        for (int d = 1; d < 5; d++) {
                            if ((a != 1) || (b != 1) || (c != 1) || (d != 1))
                            {
                                ItemStack is = new ItemStack(DronesMod.droneSpawn);
                                setDroneInfo(is, new DroneInfo(b, c, a, d));
                                subItems.add(is);
                            }
                        }
                    }
                }
            }
        }
    }
}
