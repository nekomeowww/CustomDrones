package com.github.nekomeowww.customdrones.drone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom.Item;
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
        bitsList.add(new WeightedItemStack(DronesMod.droneBit, 16));
        bitsList.add(new WeightedItemStack(new ItemStack(DronesMod.droneBit, 1, 1), 2));
        matsList.add(new WeightedItemStack(Items.field_151137_ax, 16));
        matsList.add(new WeightedItemStack(Items.field_151042_j, 16));
        matsList.add(new WeightedItemStack(Items.field_151043_k, 8));
        matsList.add(new WeightedItemStack(Items.field_151045_i, 2));
        matsList.add(new WeightedItemStack(Items.field_151166_bC, 2));
        matsList.add(new WeightedItemStack(Items.field_151065_br, 2));
        matsList.add(new WeightedItemStack(Items.field_151079_bi, 2));
        matsList.add(new WeightedItemStack(Items.field_151123_aH, 2));
        partsList.add(new WeightedItemStack(DronesMod.cfPlate1, 4));
        partsList.add(new WeightedItemStack(DronesMod.cfPlate2, 3));
        partsList.add(new WeightedItemStack(DronesMod.cfPlate3, 2));
        partsList.add(new WeightedItemStack(DronesMod.cfPlate4, 1));
        partsList.add(new WeightedItemStack(DronesMod.chip1, 4));
        partsList.add(new WeightedItemStack(DronesMod.chip2, 3));
        partsList.add(new WeightedItemStack(DronesMod.chip3, 2));
        partsList.add(new WeightedItemStack(DronesMod.chip4, 1));
        partsList.add(new WeightedItemStack(DronesMod.case1, 4));
        partsList.add(new WeightedItemStack(DronesMod.case2, 3));
        partsList.add(new WeightedItemStack(DronesMod.case3, 2));
        partsList.add(new WeightedItemStack(DronesMod.case4, 1));
        partsList.add(new WeightedItemStack(DronesMod.chip1, 4));
        partsList.add(new WeightedItemStack(DronesMod.chip2, 3));
        partsList.add(new WeightedItemStack(DronesMod.chip3, 2));
        partsList.add(new WeightedItemStack(DronesMod.chip4, 1));
        partsList.add(new WeightedItemStack(DronesMod.engine1, 4));
        partsList.add(new WeightedItemStack(DronesMod.engine2, 3));
        partsList.add(new WeightedItemStack(DronesMod.engine3, 2));
        partsList.add(new WeightedItemStack(DronesMod.engine4, 1));
        for (Module m : Module.modules) {
            modsList.add(new WeightedItemStack(ItemDroneModule.itemModule(m), 5 - m.level));
        }
        for (ItemGunUpgrade.GunUpgrade m : ItemGunUpgrade.GunUpgrade.upgrades.values()) {
            gunUpgradesList.add(new WeightedItemStack(ItemGunUpgrade.itemGunUpgrade(m), 1));
        }
        wildHoldingList.add(new WeightedItemStack(Blocks.field_150335_W, 16));
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
            super();
            this.is = i;
        }

        public WeightedItemStack(ItemStack i, int itemWeightIn)
        {
            super();
            this.is = i;
        }

        public WeightedItemStack(Item i, int weight)
        {
            this(new ItemStack(i), weight);
        }

        public WeightedItemStack(Block i, int weight)
        {
            this(new ItemStack(i), weight);
        }
    }
}
