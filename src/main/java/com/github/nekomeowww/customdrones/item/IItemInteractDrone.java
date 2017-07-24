package com.github.nekomeowww.customdrones.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public abstract interface IItemInteractDrone
{
    public abstract EnumActionResult interactWithDrone(World nameWorld, EntityDrone nameEntityDrone, EntityPlayer nameEntityPlayer, Vec3d nameVec3d, ItemStack nameItemStack, EnumHand nameEnumHand);
}
