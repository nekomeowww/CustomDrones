package com.github.nekomeowww.customdrones.item;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class ItemDroneScrew
        extends Item
        implements IItemInteractDrone
{
    public ItemDroneScrew()
    {
        setMaxStackSize(1);
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);
        tooltip.add("Used to change drone model");
    }

    public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
    {
        if (drone.playerHasCorrespondingController(player, false))
        {
            player.openGui(CustomDrones.instance, 5, world, drone.droneInfo.id, 0, 0);
            return EnumActionResult.SUCCESS;
        }
        String s = "Not your drone!";
        Random rnd = new Random();
        if (rnd.nextInt(15) == 0) {
            s = "Don't screw other people's drone!";
        }
        if (rnd.nextInt(150) == 0) {
            s = "Ya can't do that nigga!";
        }
        if (rnd.nextInt(150) == 0) {
            s = "Ya can't do that asian!";
        }
        if (rnd.nextInt(150) == 0) {
            s = "Ya can't do that white man!";
        }
        EntityHelper.addChat(player, 1, TextFormatting.RED + s);
        return EnumActionResult.FAIL;
    }
}
