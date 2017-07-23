package com.github.nekomeowww.customdrones.item;

import java.util.List;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;

public class ItemDroneFlyer
        extends Item
        implements IItemInteractDrone
{
    public ItemDroneFlyer()
    {
        func_77625_d(1);
    }

    public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        EntityDrone drone = getControllingDrone(worldIn, itemStackIn);
        if ((!worldIn.field_72995_K) && (drone != null) && (drone.droneInfo.isChanged)) {
            drone.droneInfo.updateDroneInfoToClient(playerIn);
        }
        if ((drone != null) && (drone.recordingPath != null))
        {
            drone.applyRecordPath(false);
            EntityHelper.addChat(playerIn, 1, TextFormatting.BOLD + "Path" + TextFormatting.RESET + " set to drone " + TextFormatting.AQUA + drone.droneInfo
                    .getDisplayName());
        }
        else
        {
            playerIn.openGui(DronesMod.instance, 0, worldIn, 0, 0, 0);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer playerIn, Vec3d vec, ItemStack itemStackIn, EnumHand hand)
    {
        String controllerPrefix = playerIn.getDisplayNameString() + " - slot " + (playerIn.field_71071_by.field_70461_c + 1) + ": ";

        int freq = getControllerFreq(itemStackIn);
        if (drone.droneInfo.droneFreq == -1)
        {
            drone.droneInfo.droneFreq = freq;
            setControllingDrone(playerIn, itemStackIn, drone);
            EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Set drone freq: " + freq + "GHz.");

            EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Control drone " + drone.droneInfo
                    .getDisplayName() + ".");
        }
        else if (drone.droneInfo.droneFreq != freq)
        {
            EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.RED + "Drone " + drone.droneInfo
                    .getDisplayName() + " is set to another frequency.");
        }
        else if (getControllingDroneID(itemStackIn) == drone.getDroneID())
        {
            setControllingDrone(playerIn, itemStackIn, null);
            EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.RED + "Disconnect drone " + drone.droneInfo
                    .getDisplayName() + ".");
        }
        else
        {
            setControllingDrone(playerIn, itemStackIn, drone);
            EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Control drone " + drone.droneInfo
                    .getDisplayName() + ".");
        }
        return EnumActionResult.SUCCESS;
    }

    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        if ((entityLiving instanceof EntityPlayer))
        {
            EntityDrone drone = getControllingDrone(entityLiving.field_70170_p, stack);
            if ((drone != null) && (drone.recordingPath != null))
            {
                drone.applyRecordPath(true);
                EntityHelper.addChat((EntityPlayer)entityLiving, 1, TextFormatting.BOLD + "Loop path" + TextFormatting.RESET + " set to drone " + TextFormatting.AQUA + drone.droneInfo

                        .getDisplayName());
                return true;
            }
        }
        return super.onEntitySwing(entityLiving, stack);
    }

    public void flyDroneWithButton(EntityPlayer p, int buttonCombination)
    {
        ItemStack flyerIS = p.func_184614_ca();
        int ba = buttonCombination >> 5;
        int fo = buttonCombination -= (ba << 5) >> 4;
        int ri = buttonCombination -= (fo << 4) >> 3;
        int le = buttonCombination -= (ri << 3) >> 2;
        int dw = buttonCombination -= (le << 2) >> 1;
        int up = buttonCombination -= (dw << 1);

        EntityDrone drone = getControllingDrone(p.field_70170_p, flyerIS);
        if ((drone != null) && (drone.isControllerFlying()))
        {
            Vec3d vecJS = p.func_70040_Z();
            Vec3d vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + p.field_70177_z) / 180.0F * 3.141592653589793D);

            Vec3d vecUD = VecHelper.rotateAround(
                    VecHelper.rotateAround(new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 0.0D), p.field_70125_A / 180.0F * 3.141592653589793D), new Vec3d(0.0D, 1.0D, 0.0D), p.field_70177_z / 180.0F * 3.141592653589793D);

            int i = 0;
            if (drone.getCameraMode())
            {
                vecJS = new Vec3d(0.0D, 1.0D, 0.0D);
                vecUD = drone.getHorizontalLookVec();
                vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + drone.field_70177_z) / 180.0F * 3.141592653589793D);
            }
            else if (drone.func_184179_bs() == p)
            {
                vecUD = new Vec3d(0.0D, 1.0D, 0.0D);

                vecJS = new Vec3d(-Math.sin(p.field_70177_z / 180.0D * 3.141592653589793D), 0.0D, Math.cos(p.field_70177_z / 180.0D * 3.141592653589793D));
            }
            Vec3d lookMotion = VecHelper.scale(vecJS, fo).func_178787_e(VecHelper.scale(vecJS, -ba));
            Vec3d lrMotion = VecHelper.scale(vecLR, le).func_178787_e(VecHelper.scale(vecLR, -ri));
            Vec3d udMotion = VecHelper.scale(vecUD, up).func_178787_e(VecHelper.scale(vecUD, -dw));
            Vec3d motion = VecHelper.setLength(lookMotion.func_178787_e(lrMotion).func_178787_e(udMotion), drone.getSpeedMultiplication());
            drone.field_70159_w += motion.field_72450_a;
            drone.field_70181_x += motion.field_72448_b;
            drone.field_70179_y += motion.field_72449_c;
        }
    }

    public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.func_77624_a(stack, playerIn, tooltip, advanced);

        tooltip.add(TextFormatting.AQUA + "Controller frequency: " + getControllerFreq(stack) + "GHz.");
        int controllingID = getControllingDroneID(stack);
        if (controllingID == -2) {
            tooltip.add(TextFormatting.AQUA + "Universal controller.");
        } else if (controllingID == -1) {
            tooltip.add(TextFormatting.RED + "Not assigned to a drone.");
        } else {
            tooltip.add(TextFormatting.AQUA + "Controlling drone #" + controllingID + ".");
        }
    }

    public void setControllerFreq(ItemStack stack, int i)
    {
        if (stack == null) {
            return;
        }
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        stack.func_77978_p().func_74768_a("Controller Frequency", i);
    }

    public int getControllerFreq(ItemStack stack)
    {
        if ((stack == null) || (!(stack.func_77973_b() instanceof ItemDroneFlyer))) {
            return -2;
        }
        if ((!stack.func_77942_o()) || (!stack.func_77978_p().func_74764_b("Controller Frequency"))) {
            return 0;
        }
        return stack.func_77978_p().func_74762_e("Controller Frequency");
    }

    public boolean isDroneInFrequency(ItemStack is, EntityDrone d)
    {
        return (is != null) && (getControllerFreq(is) == d.droneInfo.droneFreq);
    }

    public void setUniversalController(ItemStack stack)
    {
        if (stack == null) {
            return;
        }
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        stack.func_77978_p().func_74768_a("Drone Controlling", -2);
    }

    public void setControllingDrone(EntityPlayer player, ItemStack stack, EntityDrone drone)
    {
        if (stack == null) {
            return;
        }
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        if (drone == null)
        {
            EntityDrone prevDrone = getControllingDrone(player.field_70170_p, stack);
            if (prevDrone != null) {
                prevDrone.setControllingPlayer(null);
            }
        }
        else
        {
            drone.setControllingPlayer(player);
        }
        stack.func_77978_p().func_74768_a("Drone Controlling", drone == null ? -1 : drone.getDroneID());
    }

    public int getControllingDroneID(ItemStack stack)
    {
        if (stack == null) {
            return -1;
        }
        if ((!stack.func_77942_o()) || (!stack.func_77978_p().func_74764_b("Drone Controlling"))) {
            return -1;
        }
        return stack.func_77978_p().func_74762_e("Drone Controlling");
    }

    public EntityDrone getControllingDrone(World world, ItemStack is)
    {
        return EntityDrone.getDroneFromID(world, getControllingDroneID(is));
    }

    public boolean isControllingDrone(ItemStack stack, EntityDrone ed)
    {
        if (stack == null) {
            return false;
        }
        int id = getControllingDroneID(stack);
        return (isDroneInFrequency(stack, ed)) && ((id == -2) || (ed.getDroneID() == id));
    }

    public void setNextFlyMode(ItemStack stack)
    {
        int mode = getFlyMode(stack) + 1;
        if (mode == 4) {
            mode = 0;
        }
        setFlyMode(stack, mode);
    }

    public void setFlyMode(ItemStack stack, int i)
    {
        if (!stack.func_77942_o()) {
            stack.func_77982_d(new NBTTagCompound());
        }
        stack.func_77978_p().func_74768_a("Drone Flying Mode", i);
    }

    public int getFlyMode(ItemStack stack)
    {
        if (stack == null) {
            return 0;
        }
        if (stack.func_77942_o()) {
            return stack.func_77978_p().func_74762_e("Drone Flying Mode");
        }
        return 0;
    }
}
