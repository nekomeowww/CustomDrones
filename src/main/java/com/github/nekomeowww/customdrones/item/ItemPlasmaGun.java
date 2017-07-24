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
import com.github.nekomeowww.customdrones.api.Filters;
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
        setMaxStackSize(1);
        setMaxDamage(2000);
        this.homing = b;
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
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
            tooltip.add("Battery: " + (getMaxDamage() - getDamage(stack)) + "/" + getMaxDamage());
            int cost = 10;
            Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
            if (target != null) {
                cost = (int)(cost + Math.pow(playerIn.getDistanceToEntity(target), 0.5D));
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
        is.getSubCompound("Gun Upgrade", true).setString("Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(list));
    }

    public static List<ItemGunUpgrade.GunUpgrade> getUpgrades(ItemStack is)
    {
        return ItemGunUpgrade.GunUpgrade.getUpgrades(is.getSubCompound("Gun Upgrade", true), "Upgrades");
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack is, World worldIn, EntityPlayer player, EnumHand hand)
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
        if (!worldIn.isRemote) {
            for (int a = 0; a < shotCount; a++)
            {
                EntityPlasmaShot shot = new EntityPlasmaShot(worldIn);

                shot.shooter = player;
                shot.setHoming(target);
                Vec3d pos = EntityHelper.getEyeVec(player);
                Vec3d posAdd = player.getLookVec().scale(player.width / 2.0F);
                if (jitterPos) {
                    posAdd = VecHelper.jitter(posAdd, 0.7853981633974483D);
                }
                pos = pos.add(posAdd);
                shot.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
                Vec3d speed = VecHelper.jitter(player.getLookVec().scale(this.homing ? 2.0D : 2.5D), jitter * 3.141592653589793D / 4.0D);
                shot.motionX = speed.xCoord;
                shot.motionY = speed.yCoord;
                shot.motionZ = speed.zCoord;
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

                worldIn.spawnEntityInWorld(shot);
            }
        }
        player.playSound(SoundEvents.ENTITY_WITHER_SHOOT, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
        consumeBatteryAuto(player, is);
        return new ActionResult(EnumActionResult.SUCCESS, is);
    }

    public int getBatteryCost(EntityPlayer playerIn, ItemStack stack)
    {
        int cost = 10;
        Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
        if (target != null) {
            cost = (int)(cost + Math.pow(playerIn.getDistanceToEntity(target), 0.5D));
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
                a < player.inventory.getSizeInventory()) && (is.getItemDamage() >= getMaxDamage() - totalCost); a++) {
            if ((player.inventory.getStackInSlot(a) != null) && (player.inventory.getStackInSlot(a).stackSize > 0))
            {
                Double i = (Double)DroneInfo.batteryFuel.get(player.inventory.getStackInSlot(a).getItem());
                if ((i != null) && (i.doubleValue() > 0.0D)) {
                    while (is.getItemDamage() >= getMaxDamage() - totalCost)
                    {
                        is.setItemDamage((int)(is.getItemDamage() - i.doubleValue()));
                        player.inventory.decrStackSize(a, 1);
                    }
                }
            }
        }
        is.damageItem(totalCost, player);
    }

    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
    {
        super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);

        EntityPlayer p = CustomDrones.proxy.getClientPlayer();
        if ((this.homing) && (p == entityIn) && (isSelected))
        {
            World world = p.getEntityWorld();
            Entity homingTarget = getHomingTarget(p, stack);
            EntityHomingBox box = null;
            List<EntityHomingBox> boxes = world.getEntities(EntityHomingBox.class, EntitySelectors.NOT_SPECTATING);
            if (boxes.isEmpty())
            {
                box = new EntityHomingBox(world);
                world.spawnEntityInWorld(box);
            }
            else
            {
                box = (EntityHomingBox)boxes.get(0);
            }
            if (box != null) {
                if (homingTarget != null)
                {
                    box.target = homingTarget;
                    box.setPosition(homingTarget.posX, homingTarget.posY, homingTarget.posZ);
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
        AxisAlignedBB aabb = player.getEntityBoundingBox().expandXyz(range);
        Vec3d pLook = player.getLookVec();
        Vec3d pEye = EntityHelper.getEyeVec(player);
        return WorldHelper.getEntityBestInAngle(player.getEntityWorld(), pEye, pLook, aabb, maxAngle, player, filter);
    }

    public EnumRarity getRarity(ItemStack stack)
    {
        if (this.homing) {
            return EnumRarity.RARE;
        }
        return super.getRarity(stack);
    }
}

