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
import williamle.drones.DronesMod;
import williamle.drones.DronesMod.RecipeType;
import williamle.drones.item.ItemDronePart;
import williamle.drones.item.ItemDroneSpawn;

public class RecipeDrone
        implements IRecipe
{
    public void registerRecipes()
    {
        RecipeSorter.register("drones:drones", RecipeDrone.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        GameRegistry.addRecipe(this);
        LinkedList<ItemDronePart> cases = new LinkedList();
        cases.addAll(
                Arrays.asList(new ItemDronePart[] { DronesMod.case1, DronesMod.case2, DronesMod.case3, DronesMod.case4 }));
        List<ItemDronePart> chips = new LinkedList();
        chips.addAll(
                Arrays.asList(new ItemDronePart[] { DronesMod.chip1, DronesMod.chip2, DronesMod.chip3, DronesMod.chip4 }));
        List<ItemDronePart> cores = new LinkedList();
        cores.addAll(
                Arrays.asList(new ItemDronePart[] { DronesMod.core1, DronesMod.core2, DronesMod.core3, DronesMod.core4 }));
        List<ItemDronePart> engines = new LinkedList();
        engines.addAll(Arrays.asList(new ItemDronePart[] { DronesMod.engine1, DronesMod.engine2, DronesMod.engine3, DronesMod.engine4 }));
        for (ItemDronePart theCase : cases)
        {
            iscase = new ItemStack(theCase, 3);
            for (ItemDronePart theChip : chips)
            {
                ischip = new ItemStack(theChip, 1);
                for (ItemDronePart theCore : cores)
                {
                    iscore = new ItemStack(theCore, 1);
                    for (ItemDronePart theEngine : engines)
                    {
                        ItemStack isengine = new ItemStack(theEngine, 4);
                        LinkedList<ItemStack> shapelessList = new LinkedList();
                        shapelessList.add(iscase);
                        shapelessList.add(ischip);
                        shapelessList.add(iscore);
                        shapelessList.add(isengine);
                        ItemStack is = new ItemStack(DronesMod.droneSpawn);

                        DroneInfo di = new DroneInfo(chipLevel(ischip), coreLevel(iscore), casingLevel(iscase), engineLevel(isengine));
                        DronesMod.droneSpawn.setDroneInfo(is, di);
                        DronesMod.addRecipeToList(new ShapelessRecipes(is, shapelessList), DronesMod.RecipeType.Drones);
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
            if (is.func_77973_b() == DronesMod.chip1) {
                return 1;
            }
            if (is.func_77973_b() == DronesMod.chip2) {
                return 2;
            }
            if (is.func_77973_b() == DronesMod.chip3) {
                return 3;
            }
            if (is.func_77973_b() == DronesMod.chip4) {
                return 4;
            }
        }
        return -1;
    }

    public int coreLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.func_77973_b() == DronesMod.core1) {
                return 1;
            }
            if (is.func_77973_b() == DronesMod.core2) {
                return 2;
            }
            if (is.func_77973_b() == DronesMod.core3) {
                return 3;
            }
            if (is.func_77973_b() == DronesMod.core4) {
                return 4;
            }
        }
        return -1;
    }

    public int casingLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.func_77973_b() == DronesMod.case1) {
                return 1;
            }
            if (is.func_77973_b() == DronesMod.case2) {
                return 2;
            }
            if (is.func_77973_b() == DronesMod.case3) {
                return 3;
            }
            if (is.func_77973_b() == DronesMod.case4) {
                return 4;
            }
        }
        return -1;
    }

    public int engineLevel(ItemStack is)
    {
        if (is != null)
        {
            if (is.func_77973_b() == DronesMod.engine1) {
                return 1;
            }
            if (is.func_77973_b() == DronesMod.engine2) {
                return 2;
            }
            if (is.func_77973_b() == DronesMod.engine3) {
                return 3;
            }
            if (is.func_77973_b() == DronesMod.engine4) {
                return 4;
            }
        }
        return -1;
    }

    public boolean func_77569_a(InventoryCrafting inv, World worldIn)
    {
        if (inv.func_70302_i_() >= 9) {
            if ((chipLevel(inv.func_70301_a(4)) > 0) && (coreLevel(inv.func_70301_a(1)) > 0) &&
                    (casingLevel(inv.func_70301_a(3)) > 0) &&
                    (casingLevel(inv.func_70301_a(3)) == casingLevel(inv.func_70301_a(5))) &&
                    (casingLevel(inv.func_70301_a(5)) == casingLevel(inv.func_70301_a(7))) &&
                    (engineLevel(inv.func_70301_a(0)) > 0) &&
                    (engineLevel(inv.func_70301_a(0)) == engineLevel(inv.func_70301_a(2))) &&
                    (engineLevel(inv.func_70301_a(2)) == engineLevel(inv.func_70301_a(6))) &&
                    (engineLevel(inv.func_70301_a(6)) == engineLevel(inv.func_70301_a(8)))) {
                return true;
            }
        }
        return false;
    }

    public ItemStack func_77572_b(InventoryCrafting inv)
    {
        ItemStack is = new ItemStack(DronesMod.droneSpawn);

        DroneInfo di = new DroneInfo(chipLevel(inv.func_70301_a(4)), coreLevel(inv.func_70301_a(1)), casingLevel(inv.func_70301_a(3)), engineLevel(inv.func_70301_a(0)));
        DronesMod.droneSpawn.setDroneInfo(is, di);
        return is;
    }

    public int func_77570_a()
    {
        return 9;
    }

    public ItemStack func_77571_b()
    {
        return new ItemStack(DronesMod.droneSpawn);
    }

    public ItemStack[] func_179532_b(InventoryCrafting inv)
    {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
