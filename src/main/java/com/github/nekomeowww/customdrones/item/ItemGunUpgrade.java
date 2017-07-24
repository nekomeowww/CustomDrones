package com.github.nekomeowww.customdrones.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;

public class ItemGunUpgrade
        extends Item
{
    public static class GunUpgrade
    {
        public static Map<String, GunUpgrade> upgrades = new HashMap();
        public static GunUpgrade explosion = new GunUpgrade("explosion", "Explosion", "Explodes!").setWeaponCosts(10, 1.0D);
        public static GunUpgrade scatter = new GunUpgrade("scatter", "Scatter", "Creates multiple shots on hit.")
                .setWeaponCosts(1, 4.0D);
        public static GunUpgrade healing = new GunUpgrade("heal", "Healing", "Heals target.").setWeaponCosts(2, 1.0D);
        public static GunUpgrade doubleShot = new GunUpgrade("double", "Double Shot", "Twice the bullets.")
                .setWeaponCosts(0, 1.5D);
        public static GunUpgrade tripleShot = new GunUpgrade("triple", "Triple Shot", "Triple the bullets.")
                .setWeaponCosts(0, 2.0D);
        public static GunUpgrade fire = new GunUpgrade("fire", "Fire", "Causes fire, thaw snow & ice.")
                .setWeaponCosts(5, 1.0D);
        public static GunUpgrade ice = new GunUpgrade("ice", "Ice", "Extinguish fire, freeze water, cast snow.")
                .setWeaponCosts(5, 1.0D);
        public String id;
        public String name;
        public String desc;
        public int cost;
        public double costScale = 1.0D;

        public GunUpgrade(String s1, String s2, String s3)
        {
            this.id = s1;
            this.name = s2;
            this.desc = s3;
            upgrades.put(this.id, this);
        }

        public GunUpgrade setWeaponCosts(int i, double d)
        {
            this.cost = i;
            this.costScale = d;
            return this;
        }

        public static String upgradesToString(List<GunUpgrade> list)
        {
            String s = "";
            for (int a = 0; a < list.size(); a++)
            {
                if (a > 0) {
                    s = s + ";";
                }
                s = s + ((GunUpgrade)list.get(a)).id;
            }
            return s;
        }

        public static List<GunUpgrade> getUpgrades(NBTTagCompound tag, String upgradeKey)
        {
            List<GunUpgrade> list = new ArrayList();
            String[] ss = tag.getString(upgradeKey).split(";");
            for (String s : ss)
            {
                GunUpgrade upgrade = (GunUpgrade)upgrades.get(s);
                if (upgrade != null) {
                    list.add(upgrade);
                }
            }
            return list;
        }
    }

    public ItemGunUpgrade()
    {
        setMaxStackSize(16);
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        GunUpgrade gu = getGunUpgrade(stack);
        if (gu != null)
        {
            tooltip.add(TextFormatting.YELLOW + gu.name);
            if ((gu.desc != null) && (!gu.desc.isEmpty())) {
                tooltip.add(TextFormatting.ITALIC + gu.desc);
            }
            tooltip.add(TextFormatting.ITALIC + "" + TextFormatting.RED + "Equip gun in off-hand and right click this upgrade to install.");
            if (advanced)
            {
                String cost = "";
                if (gu.cost != 0) {
                    cost = cost + "Battery cost: " + gu.cost;
                }
                if (gu.costScale != 1.0D) {
                    cost = cost + (cost.isEmpty() ? "Battery cost scale: x" : " - cost scale: x") + gu.costScale;
                }
                if (!cost.isEmpty()) {
                    tooltip.add(cost);
                }
            }
        }
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        ItemStack otherHandIS = playerIn.getHeldItem(otherHand);
        if ((otherHandIS != null) && ((otherHandIS.getItem() instanceof ItemPlasmaGun)))
        {
            GunUpgrade gu = getGunUpgrade(itemStackIn);
            if (gu != null)
            {
                boolean canAdd = ItemPlasmaGun.addUpgrade(otherHandIS, gu);
                if (canAdd) {
                    itemStackIn.stackSize -= 1;
                }
            }
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
    {
        if ((tab == CustomDrones.droneTab) || (tab == null)) {
            for (GunUpgrade gu : GunUpgrade.upgrades.values()) {
                subItems.add(itemGunUpgrade(gu));
            }
        }
    }

    public static void setGunUpgrade(ItemStack stack, GunUpgrade mod)
    {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setString("Gun Upgrade", mod.id);
    }

    public static GunUpgrade getGunUpgrade(ItemStack stack)
    {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        GunUpgrade m = (GunUpgrade)GunUpgrade.upgrades.get(stack.getTagCompound().getString("Gun Upgrade"));
        return m;
    }

    public static ItemStack itemGunUpgrade(GunUpgrade m)
    {
        ItemStack is = new ItemStack(CustomDrones.gunUpgrade);
        setGunUpgrade(is, m);
        return is;
    }
}
