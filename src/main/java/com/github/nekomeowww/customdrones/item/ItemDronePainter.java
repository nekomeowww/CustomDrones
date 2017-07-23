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

public class ItemDronePainter
        extends Item
        implements IItemInteractDrone
{
    public ItemDronePainter()
    {
        func_77625_d(1);
    }

    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.func_77624_a(stack, playerIn, tooltip, advanced);
        tooltip.add("Used to recolor drones");
    }

    public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
    {
        if (drone.playerHasCorrespondingController(player, false))
        {
            player.openGui(DronesMod.instance, 4, world, drone.droneInfo.id, 0, 0);
            return EnumActionResult.SUCCESS;
        }
        String s = "Not your drone!";
        Random rnd = new Random();
        if (rnd.nextInt(15) == 0) {
            s = "Don't vandalize other people's drone!";
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
