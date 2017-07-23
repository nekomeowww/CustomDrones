package com.github.nekomeowww.customdrones.item;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.module.Module;

public class ItemDroneModule
        extends Item
{
    public ItemDroneModule()
    {
        func_77625_d(16);
        func_77627_a(true);
    }

    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.func_77624_a(stack, playerIn, tooltip, advanced);
        Module mod = getModule(stack);
        if (mod != null)
        {
            String name = (String)tooltip.get(tooltip.size() - 1);
            List<String> moduleTooltips = mod.getTooltip();
            tooltip.set(tooltip.size() - 1, name + " " + (String)moduleTooltips.get(0));
            tooltip.addAll(moduleTooltips.subList(1, moduleTooltips.size()));
        }
    }

    public int getMetadata(ItemStack stack)
    {
        Module mod = getModule(stack);
        if (mod != null) {
            return Math.max(0, mod.level - 1);
        }
        return super.getMetadata(stack);
    }

    public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        super.func_150895_a(itemIn, tab, subItems);
        if ((tab == DronesMod.droneTab) || (tab == null)) {
            for (int a = 1; a < Module.modules.size(); a++) {
                subItems.add(itemModule((Module)Module.modules.get(a)));
            }
        }
    }

    public static void setModule(ItemStack stack, Module mod)
    {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        stack.func_77978_p().func_74778_a("Drone Module", mod.getID());
    }

    public static Module getModule(ItemStack stack)
    {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        Module m = Module.getModuleByID(stack.func_77978_p().func_74779_i("Drone Module"));
        return m == null ? Module.useless1 : m;
    }

    public static ItemStack itemModule(Module m)
    {
        ItemStack is = new ItemStack(DronesMod.droneModule);
        setModule(is, m);
        return is;
    }
}
