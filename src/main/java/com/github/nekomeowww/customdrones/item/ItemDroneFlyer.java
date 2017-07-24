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
        setMaxStackSize(1);
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        EntityDrone drone = getControllingDrone(worldIn, itemStackIn);
        if ((!worldIn.isRemote) && (drone != null) && (drone.droneInfo.isChanged)) {
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
            playerIn.openGui(CustomDrones.instance, 0, worldIn, 0, 0, 0);
        }
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer playerIn, Vec3d vec, ItemStack itemStackIn, EnumHand hand)
    {
        String controllerPrefix = playerIn.getDisplayNameString() + " - slot " + (playerIn.inventory.currentItem + 1) + ": ";

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
            EntityDrone drone = getControllingDrone(entityLiving.getEntityWorld(), stack);
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
        ItemStack flyerIS = p.getHeldItemMainhand();
        int ba = buttonCombination >> 5;
        int fo = buttonCombination -= (ba << 5) >> 4;
        int ri = buttonCombination -= (fo << 4) >> 3;
        int le = buttonCombination -= (ri << 3) >> 2;
        int dw = buttonCombination -= (le << 2) >> 1;
        int up = buttonCombination -= (dw << 1);

        EntityDrone drone = getControllingDrone(p.getEntityWorld(), flyerIS);
        if ((drone != null) && (drone.isControllerFlying()))
        {
            Vec3d vecJS = p.getLookVec();
            Vec3d vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + p.rotationYaw) / 180.0F * 3.141592653589793D);

            Vec3d vecUD = VecHelper.rotateAround(
                    VecHelper.rotateAround(new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 0.0D), p.rotationPitch / 180.0F * 3.141592653589793D), new Vec3d(0.0D, 1.0D, 0.0D), p.rotationYaw / 180.0F * 3.141592653589793D);

            int i = 0;
            if (drone.getCameraMode())
            {
                vecJS = new Vec3d(0.0D, 1.0D, 0.0D);
                vecUD = drone.getHorizontalLookVec();
                vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + drone.rotationYaw) / 180.0F * 3.141592653589793D);
            }
            else if (drone.getControllingPassenger() == p)
            {
                vecUD = new Vec3d(0.0D, 1.0D, 0.0D);

                vecJS = new Vec3d(-Math.sin(p.rotationYaw / 180.0D * 3.141592653589793D), 0.0D, Math.cos(p.rotationYaw / 180.0D * 3.141592653589793D));
            }
            Vec3d lookMotion = VecHelper.scale(vecJS, fo).add(VecHelper.scale(vecJS, -ba));
            Vec3d lrMotion = VecHelper.scale(vecLR, le).add(VecHelper.scale(vecLR, -ri));
            Vec3d udMotion = VecHelper.scale(vecUD, up).add(VecHelper.scale(vecUD, -dw));
            Vec3d motion = VecHelper.setLength(lookMotion.add(lrMotion).add(udMotion), drone.getSpeedMultiplication());
            drone.motionX += motion.xCoord;
            drone.motionY += motion.yCoord;
            drone.motionZ += motion.zCoord;
        }
    }

    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
    {
        super.addInformation(stack, playerIn, tooltip, advanced);

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
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Controller Frequency", i);
    }

    public int getControllerFreq(ItemStack stack)
    {
        if ((stack == null) || (!(stack.getItem() instanceof ItemDroneFlyer))) {
            return -2;
        }
        if ((!stack.hasTagCompound()) || (!stack.getTagCompound().hasKey("Controller Frequency"))) {
            return 0;
        }
        return stack.getTagCompound().getInteger("Controller Frequency");
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
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Drone Controlling", -2);
    }

    public void setControllingDrone(EntityPlayer player, ItemStack stack, EntityDrone drone)
    {
        if (stack == null) {
            return;
        }
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        if (drone == null)
        {
            EntityDrone prevDrone = getControllingDrone(player.getEntityWorld(), stack);
            if (prevDrone != null) {
                prevDrone.setControllingPlayer(null);
            }
        }
        else
        {
            drone.setControllingPlayer(player);
        }
        stack.getTagCompound().setInteger("Drone Controlling", drone == null ? -1 : drone.getDroneID());
    }

    public int getControllingDroneID(ItemStack stack)
    {
        if (stack == null) {
            return -1;
        }
        if ((!stack.hasTagCompound()) || (!stack.getTagCompound().hasKey("Drone Controlling"))) {
            return -1;
        }
        return stack.getTagCompound().getInteger("Drone Controlling");
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
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        stack.getTagCompound().setInteger("Drone Flying Mode", i);
    }

    public int getFlyMode(ItemStack stack)
    {
        if (stack == null) {
            return 0;
        }
        if (stack.hasTagCompound()) {
            return stack.getTagCompound().getInteger("Drone Flying Mode");
        }
        return 0;
    }
}
