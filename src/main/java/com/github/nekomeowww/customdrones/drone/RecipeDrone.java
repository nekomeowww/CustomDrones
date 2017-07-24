package com.github.nekomeowww.customdrones.drone;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.CustomDrones.RecipeType;
import com.github.nekomeowww.customdrones.item.ItemDronePart;
import com.github.nekomeowww.customdrones.item.ItemDroneSpawn;

public class RecipeDrone
        implements IRecipe
{
    public void registerRecipes()
    {
        RecipeSorter.register("drones:drones", RecipeDrone.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        GameRegistry.addRecipe(this);
        LinkedList<ItemDronePart> cases = new LinkedList();
        cases.addAll(
                Arrays.asList(new ItemDronePart[] { CustomDrones.case1, CustomDrones.case2, CustomDrones.case3, CustomDrones.case4 }));
        List<ItemDronePart> chips = new LinkedList();
        chips.addAll(
                Arrays.asList(new ItemDronePart[] { CustomDrones.chip1, CustomDrones.chip2, CustomDrones.chip3, CustomDrones.chip4 }));
        List<ItemDronePart> cores = new LinkedList();
        cores.addAll(
                Arrays.asList(new ItemDronePart[] { CustomDrones.core1, CustomDrones.core2, CustomDrones.core3, CustomDrones.core4 }));
        List<ItemDronePart> engines = new LinkedList();
        engines.addAll(Arrays.asList(new ItemDronePart[] { CustomDrones.engine1, CustomDrones.engine2, CustomDrones.engine3, CustomDrones.engine4 }));
        for (ItemDronePart theCase : cases)
        {
            ItemStack iscase = new ItemStack(theCase, 3);
            for (ItemDronePart theChip : chips)
            {
                ItemStack ischip = new ItemStack(theChip, 1);
                for (ItemDronePart theCore : cores)
                {
                    ItemStack iscore = new ItemStack(theCore, 1);
                    for (ItemDronePart theEngine : engines)
                    {
                        ItemStack isengine = new ItemStack(theEngine, 4);
                        LinkedList<ItemStack> shapelessList = new LinkedList();
                        shapelessList.add(iscase);
                        shapelessList.add(ischip);
                        shapelessList.add(iscore);
                        shapelessList.add(isengine);
                        ItemStack is = new ItemStack(CustomDrones.droneSpawn);

                        DroneInfo di = new DroneInfo(chipLevel(ischip), coreLevel(iscore), casingLevel(iscase), engineLevel(isengine));
                        CustomDrones.droneSpawn.setDroneInfo(is, di);
                        CustomDrones.addRecipeToList(new ShapelessRecipes(is, shapelessList), CustomDrones.RecipeType.Drones);
                    }
                }
            }
        }
        ItemStack iscase;
        ItemStack ischip;
        ItemStack iscore;
    }

    public int chipLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.getItem() == CustomDrones.chip1) {
                return 1;
            }
            if (is.getItem() == CustomDrones.chip2) {
                return 2;
            }
            if (is.getItem() == CustomDrones.chip3) {
                return 3;
            }
            if (is.getItem() == CustomDrones.chip4) {
                return 4;
            }
        }
        return -1;
    }

    public int coreLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.getItem() == CustomDrones.core1) {
                return 1;
            }
            if (is.getItem() == CustomDrones.core2) {
                return 2;
            }
            if (is.getItem() == CustomDrones.core3) {
                return 3;
            }
            if (is.getItem() == CustomDrones.core4) {
                return 4;
            }
        }
        return -1;
    }

    public int casingLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.getItem() == CustomDrones.case1) {
                return 1;
            }
            if (is.getItem() == CustomDrones.case2) {
                return 2;
            }
            if (is.getItem() == CustomDrones.case3) {
                return 3;
            }
            if (is.getItem() == CustomDrones.case4) {
                return 4;
            }
        }
        return -1;
    }

    public int engineLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.getItem() == CustomDrones.engine1) {
                return 1;
            }
            if (is.getItem() == CustomDrones.engine2) {
                return 2;
            }
            if (is.getItem() == CustomDrones.engine3) {
                return 3;
            }
            if (is.getItem() == CustomDrones.engine4) {
                return 4;
            }
        }
        return -1;
    }

    public boolean matches(InventoryCrafting inv, World worldIn)
    {
        if (inv.getSizeInventory() >= 9) {
            if ((chipLevel(inv.getStackInSlot(4)) > 0) && (coreLevel(inv.getStackInSlot(1)) > 0) &&
                    (casingLevel(inv.getStackInSlot(3)) > 0) &&
                    (casingLevel(inv.getStackInSlot(3)) == casingLevel(inv.getStackInSlot(5))) &&
                    (casingLevel(inv.getStackInSlot(5)) == casingLevel(inv.getStackInSlot(7))) &&
                    (engineLevel(inv.getStackInSlot(0)) > 0) &&
                    (engineLevel(inv.getStackInSlot(0)) == engineLevel(inv.getStackInSlot(2))) &&
                    (engineLevel(inv.getStackInSlot(2)) == engineLevel(inv.getStackInSlot(6))) &&
                    (engineLevel(inv.getStackInSlot(6)) == engineLevel(inv.getStackInSlot(8)))) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getCraftingResult(InventoryCrafting inv)
    {
        ItemStack is = new ItemStack(CustomDrones.droneSpawn);

        DroneInfo di = new DroneInfo(chipLevel(inv.getStackInSlot(4)), coreLevel(inv.getStackInSlot(1)), casingLevel(inv.getStackInSlot(3)), engineLevel(inv.getStackInSlot(0)));
        CustomDrones.droneSpawn.setDroneInfo(is, di);
        return is;
    }

    public int getRecipeSize()
    {
        return 9;
    }

    public ItemStack getRecipeOutput()
    {
        return new ItemStack(CustomDrones.droneSpawn);
    }

    public ItemStack[] getRemainingItems(InventoryCrafting inv)
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
