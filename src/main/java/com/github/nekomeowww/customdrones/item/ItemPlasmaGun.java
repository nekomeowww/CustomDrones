package com.github.nekomeowww.customdrones.item;

import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CommonProxy;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.Filters.FilterExcepts;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.helpers.WorldHelper;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityHomingBox;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;

public class ItemPlasmaGun
        extends Item
        implements IItemWeapon
{
    public boolean homing;

    public ItemPlasmaGun(boolean b)
    {
        func_77625_d(1);
        func_77656_e(2000);
        this.homing = b;
    }

    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.func_77624_a(stack, playerIn, tooltip, advanced);
        if (this.homing)
        {
            tooltip.add("Max homing range: 64 blocks");
            tooltip.add("Damage: 2.5 hearts");
        }
        else
        {
            tooltip.add("Damage: 1.5 hearts");
        }
        List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(stack);
        if (!upgrades.isEmpty())
        {
            tooltip.add("Gun upgrades");
            for (int a = 0; a < upgrades.size(); a++)
            {
                ItemGunUpgrade.GunUpgrade gu = (ItemGunUpgrade.GunUpgrade)upgrades.get(a);
                String s = a + 1 + ". " + TextFormatting.YELLOW + gu.name + TextFormatting.RESET;
                if ((gu.desc != null) && (!gu.desc.isEmpty())) {
                    s = s + ": " + TextFormatting.ITALIC + gu.desc;
                }
                tooltip.add(s);
            }
        }
        if (advanced)
        {
            tooltip.add("Battery: " + (func_77612_l() - getDamage(stack)) + "/" + func_77612_l());
            int cost = 10;
            Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
            if (target != null) {
                cost = (int)(cost + Math.pow(playerIn.func_70032_d(target), 0.5D));
            }
            int upgradeCost = 0;
            double costScale = 1.0D;
            for (ItemGunUpgrade.GunUpgrade gu : upgrades)
            {
                upgradeCost += gu.cost;
                costScale *= gu.costScale;
            }
            int totalCost = (int)((cost + upgradeCost) * costScale);
            tooltip.add(totalCost + " batteries per shot");
        }
    }

    public static boolean addUpgrade(ItemStack is, ItemGunUpgrade.GunUpgrade upgrade)
    {
        List<ItemGunUpgrade.GunUpgrade> list = getUpgrades(is);
        boolean b = list.contains(upgrade) ? false : list.add(upgrade);
        updateUpgrades(is, list);
        return b;
    }

    public static boolean removeUpgrade(ItemStack is, ItemGunUpgrade.GunUpgrade upgrade)
    {
        List<ItemGunUpgrade.GunUpgrade> list = getUpgrades(is);
        boolean b = list.remove(upgrade);
        updateUpgrades(is, list);
        return b;
    }

    public static void updateUpgrades(ItemStack is, List<ItemGunUpgrade.GunUpgrade> list)
    {
        is.func_179543_a("Gun Upgrade", true).func_74778_a("Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(list));
    }

    public static List<ItemGunUpgrade.GunUpgrade> getUpgrades(ItemStack is)
    {
        return ItemGunUpgrade.GunUpgrade.getUpgrades(is.func_179543_a("Gun Upgrade", true), "Upgrades");
    }

    public ActionResult<ItemStack> func_77659_a(ItemStack is, World worldIn, EntityPlayer player, EnumHand hand)
    {
        List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(is);
        Random rnd = new Random();
        Entity target = this.homing ? getHomingTarget(player, is) : null;

        int shotCount = 1;
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.doubleShot)) {
            shotCount *= 2;
        }
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.tripleShot)) {
            shotCount *= 3;
        }
        double jitter = (shotCount - 1) * 0.1D;
        boolean jitterPos = shotCount == 1;
        if (!worldIn.field_72995_K) {
            for (int a = 0; a < shotCount; a++)
            {
                EntityPlasmaShot shot = new EntityPlasmaShot(worldIn);

                shot.shooter = player;
                shot.setHoming(target);
                Vec3d pos = EntityHelper.getEyeVec(player);
                Vec3d posAdd = player.func_70040_Z().func_186678_a(player.field_70130_N / 2.0F);
                if (jitterPos) {
                    posAdd = VecHelper.jitter(posAdd, 0.7853981633974483D);
                }
                pos = pos.func_178787_e(posAdd);
                shot.func_70107_b(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c);
                Vec3d speed = VecHelper.jitter(player.func_70040_Z().func_186678_a(this.homing ? 2.0D : 2.5D), jitter * 3.141592653589793D / 4.0D);
                shot.field_70159_w = speed.field_72450_a;
                shot.field_70181_x = speed.field_72448_b;
                shot.field_70179_y = speed.field_72449_c;
                shot.damage = (this.homing ? 5.0D : 3.0D);
                shot.color = new Color(1.0D, 0.2D, 0.2D, 1.0D);
                if (upgrades.contains(ItemGunUpgrade.GunUpgrade.healing))
                {
                    shot.damage *= -1.0D;
                    shot.color = new Color(0.0D, 1.0D, 0.1D, 1.0D);
                }
                if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) {
                    shot.color = shot.color.blendNormal(new Color(0.0D, 1.0D, 1.0D, 1.0D), 0.5D);
                }
                if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) {
                    shot.color = shot.color.blendNormal(new Color(1.0D, 0.6D, 0.0D, 1.0D), 0.5D);
                }
                shot.setUpgrades(upgrades);

                worldIn.func_72838_d(shot);
            }
        }
        player.func_184185_a(SoundEvents.field_187853_gC, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
        consumeBatteryAuto(player, is);
        return new ActionResult(EnumActionResult.SUCCESS, is);
    }

    public int getBatteryCost(EntityPlayer playerIn, ItemStack stack)
    {
        int cost = 10;
        Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
        if (target != null) {
            cost = (int)(cost + Math.pow(playerIn.func_70032_d(target), 0.5D));
        }
        int upgradeCost = 0;
        double costScale = 1.0D;
        List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(stack);
        for (ItemGunUpgrade.GunUpgrade gu : upgrades)
        {
            upgradeCost += gu.cost;
            costScale *= gu.costScale;
        }
        int totalCost = (int)((cost + upgradeCost) * costScale);
        return totalCost;
    }

    public void consumeBatteryAuto(EntityPlayer player, ItemStack is)
    {
        int totalCost = getBatteryCost(player, is);
        for (int a = 0; (
                a < player.field_71071_by.func_70302_i_()) && (is.func_77952_i() >= func_77612_l() - totalCost); a++) {
            if ((player.field_71071_by.func_70301_a(a) != null) && (player.field_71071_by.func_70301_a(a).field_77994_a > 0))
            {
                Double i = (Double)DroneInfo.batteryFuel.get(player.field_71071_by.func_70301_a(a).func_77973_b());
                if ((i != null) && (i.doubleValue() > 0.0D)) {
                    while (is.func_77952_i() >= func_77612_l() - totalCost)
                    {
                        is.func_77964_b((int)(is.func_77952_i() - i.doubleValue()));
                        player.field_71071_by.func_70298_a(a, 1);
                    }
                }
            }
        }
        is.func_77972_a(totalCost, player);
    }

    public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.func_77663_a(stack, worldIn, entityIn, itemSlot, isSelected);

        EntityPlayer p = DronesMod.proxy.getClientPlayer();
        if ((this.homing) && (p == entityIn) && (isSelected))
        {
            World world = p.field_70170_p;
            Entity homingTarget = getHomingTarget(p, stack);
            EntityHomingBox box = null;
            List<EntityHomingBox> boxes = world.func_175644_a(EntityHomingBox.class, EntitySelectors.field_180132_d);
            if (boxes.isEmpty())
            {
                box = new EntityHomingBox(world);
                world.func_72838_d(box);
            }
            else
            {
                box = (EntityHomingBox)boxes.get(0);
            }
            if (box != null) {
                if (homingTarget != null)
                {
                    box.target = homingTarget;
                    box.func_70107_b(homingTarget.field_70165_t, homingTarget.field_70163_u, homingTarget.field_70161_v);
                }
                else
                {
                    box.target = null;
                }
            }
        }
    }

    public Entity getHomingTarget(EntityPlayer player, ItemStack stack)
    {
        Filters.FilterExcepts filter = new Filters.FilterExcepts(new Object[] { player, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class, IProjectile.class });

        double range = 64.0D;
        double maxAngle = 15.0D;
        AxisAlignedBB aabb = player.func_174813_aQ().func_186662_g(range);
        Vec3d pLook = player.func_70040_Z();
        Vec3d pEye = EntityHelper.getEyeVec(player);
        return WorldHelper.getEntityBestInAngle(player.field_70170_p, pEye, pLook, aabb, maxAngle, player, filter);
    }

    public EnumRarity func_77613_e(ItemStack stack)
    {
        if (this.homing) {
            return EnumRarity.RARE;
        }
        return super.func_77613_e(stack);
    }
}

