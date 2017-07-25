package com.github.nekomeowww.customdrones.drone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
//import net.minecraft.util.WeightedRandom.Item;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade.GunUpgrade;

public class DroneWeightedLists
{
    public static List<WeightedItemStack> bitsList = new ArrayList();
    public static List<WeightedItemStack> matsList = new ArrayList();
    public static List<WeightedItemStack> partsList = new ArrayList();
    public static List<WeightedItemStack> modsList = new ArrayList();
    public static List<WeightedItemStack> gunUpgradesList = new ArrayList();
    public static List<WeightedItemStack> wildHoldingList = new ArrayList();

    static
    {
        bitsList.add(new WeightedItemStack(CustomDrones.droneBit, 16));
        bitsList.add(new WeightedItemStack(new ItemStack(CustomDrones.droneBit, 1, 1), 2));
        matsList.add(new WeightedItemStack(Items.REDSTONE, 16));
        matsList.add(new WeightedItemStack(Items.IRON_INGOT, 16));
        matsList.add(new WeightedItemStack(Items.GOLD_INGOT, 8));
        matsList.add(new WeightedItemStack(Items.DIAMOND, 2));
        matsList.add(new WeightedItemStack(Items.EMERALD, 2));
        matsList.add(new WeightedItemStack(Items.BLAZE_POWDER, 2));
        matsList.add(new WeightedItemStack(Items.ENDER_PEARL, 2));
        matsList.add(new WeightedItemStack(Items.SLIME_BALL, 2));
        partsList.add(new WeightedItemStack(CustomDrones.cfPlate1, 4));
        partsList.add(new WeightedItemStack(CustomDrones.cfPlate2, 3));
        partsList.add(new WeightedItemStack(CustomDrones.cfPlate3, 2));
        partsList.add(new WeightedItemStack(CustomDrones.cfPlate4, 1));
        partsList.add(new WeightedItemStack(CustomDrones.chip1, 4));
        partsList.add(new WeightedItemStack(CustomDrones.chip2, 3));
        partsList.add(new WeightedItemStack(CustomDrones.chip3, 2));
        partsList.add(new WeightedItemStack(CustomDrones.chip4, 1));
        partsList.add(new WeightedItemStack(CustomDrones.case1, 4));
        partsList.add(new WeightedItemStack(CustomDrones.case2, 3));
        partsList.add(new WeightedItemStack(CustomDrones.case3, 2));
        partsList.add(new WeightedItemStack(CustomDrones.case4, 1));
        partsList.add(new WeightedItemStack(CustomDrones.chip1, 4));
        partsList.add(new WeightedItemStack(CustomDrones.chip2, 3));
        partsList.add(new WeightedItemStack(CustomDrones.chip3, 2));
        partsList.add(new WeightedItemStack(CustomDrones.chip4, 1));
        partsList.add(new WeightedItemStack(CustomDrones.engine1, 4));
        partsList.add(new WeightedItemStack(CustomDrones.engine2, 3));
        partsList.add(new WeightedItemStack(CustomDrones.engine3, 2));
        partsList.add(new WeightedItemStack(CustomDrones.engine4, 1));
        for (Module m : Module.modules) {
            modsList.add(new WeightedItemStack(ItemDroneModule.itemModule(m), 5 - m.level));
        }
        for (ItemGunUpgrade.GunUpgrade m : ItemGunUpgrade.GunUpgrade.upgrades.values()) {
            gunUpgradesList.add(new WeightedItemStack(ItemGunUpgrade.itemGunUpgrade(m), 1));
        }
        wildHoldingList.add(new WeightedItemStack(Blocks.TNT, 16));
        wildHoldingList.addAll(bitsList);
        wildHoldingList.addAll(matsList);
        wildHoldingList.addAll(partsList);
        wildHoldingList.addAll(modsList);
        wildHoldingList.addAll(gunUpgradesList);
    }

    public static class WeightedItemStack
            extends WeightedRandom.Item
    {
        public ItemStack is;

        public WeightedItemStack(ItemStack i)
        {
            super(0);
            this.is = i;
        }

        public WeightedItemStack(ItemStack i, int itemWeightIn)
        {
            super(itemWeightIn);
            this.is = i;
        }

        public WeightedItemStack(net.minecraft.item.Item i, int weight)
        {
            this(new ItemStack(i), weight);
        }

        public WeightedItemStack(Block i, int weight)
        {
            this(new ItemStack(i), weight);
        }
    }
}
