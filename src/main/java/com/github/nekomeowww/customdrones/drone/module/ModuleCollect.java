package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.InventoryDrone;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneTransferXP;

public class ModuleCollect
        extends Module
{
    public ModuleCollect(int l, String s)
    {
        super(l, Module.ModuleType.Collect, s);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        World world = drone.getEntityWorld();

        AxisAlignedBB aabb = new AxisAlignedBB(drone.posX, drone.posY, drone.posZ, drone.posX, drone.posY, drone.posZ).expand(2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip).offset(0.0D, -drone.droneInfo.chip * 0.5D, 0.0D).addCoord(0.0D, 0.25D, 0.0D);
        if ((drone.droneInfo.hasInventory()) && (canFunctionAs(Module.itemCollect)))
        {
            List<EntityItem> eis = world.getEntitiesWithinAABB(EntityItem.class, aabb);
            for (EntityItem ei : eis)
            {
                ItemStack is = ei.getEntityItem();
                if (drone.droneInfo.inventory.canAddToInv(is, false)) {
                    if ((!ei.cannotPickup()) && (ei.getDistanceSqToEntity(drone) <= 1.0D))
                    {
                        if (is != null)
                        {
                            ItemStack is0 = drone.droneInfo.inventory.addToInv(is);
                            if ((is0 == null) || (is0.stackSize == 0)) {
                                ei.setDead();
                            } else {
                                ei.setEntityItemStack(is0);
                            }
                        }
                    }
                    else
                    {
                        Vec3d vec = VecHelper.fromTo(ei.getPositionVector(), drone.getPositionVector());
                        vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
                        ei.addVelocity(vec.xCoord, vec.yCoord, vec.zCoord);
                    }
                }
            }
        }
        if (canFunctionAs(Module.xpCollect))
        {
            List<EntityXPOrb> xpos = world.getEntitiesWithinAABB(EntityXPOrb.class, aabb);
            for (EntityXPOrb xpo : xpos) {
                if ((xpo.delayBeforeCanPickup == 0) && (xpo.getDistanceSqToEntity(drone) <= 1.0D))
                {
                    setCollectedXP(drone, getCollectedXP(drone) + xpo.getXpValue());
                    xpo.setDead();
                }
                else
                {
                    Vec3d vec = VecHelper.fromTo(xpo.getPositionVector(), drone.getPositionVector());
                    vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
                    xpo.addVelocity(vec.xCoord, vec.yCoord, vec.zCoord);
                }
            }
        }
    }

    public int getCollectedXP(EntityDrone drone)
    {
        return getModNBT(drone.droneInfo).getInteger("Collected XP");
    }

    public void setCollectedXP(EntityDrone drone, int i)
    {
        NBTTagCompound tag = getModNBT(drone.droneInfo);
        if (tag != null) {
            tag.setInteger("Collected XP", i);
        }
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        boolean item = canFunctionAs(Module.itemCollect);
        boolean xp = canFunctionAs(Module.xpCollect);
        list.add("Collect " + (xp ? "XP" : item ? "items" + (xp ? " and XP" : "") : ""));
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleCollectGui(gui, this);
    }

    @SideOnly(Side.CLIENT)
    public class ModuleCollectGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public GuiButton buttonTransfer;

        public ModuleCollectGui(T gui)
        {
            super(mod);
        }

        public void initGui()
        {
            super.initGui();
            boolean xp = ModuleCollect.this.canFunctionAs(Module.xpCollect);
            if (xp) {
                this.buttonList.add(this.buttonTransfer = new GuiButton(1, this.width / 2 - 24, this.height / 2 + 70, 70, 20, "Transfer XP"));
            }
        }

        public void updateScreen()
        {
            super.updateScreen();
            boolean xp = this.mod.canFunctionAs(Module.xpCollect);
            if (xp)
            {
                int range = 4 * this.parent.drone.droneInfo.chip;
                if (this.parent.drone.getDistanceSqToEntity(this.parent.player) <= range * range)
                {
                    this.buttonTransfer.enabled = true;
                    this.buttonTransfer.displayString = "Transfer XP";
                }
                else
                {
                    this.buttonTransfer.enabled = false;
                    this.buttonTransfer.displayString = "Out of range";
                }
            }
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if ((button == this.buttonTransfer) && (ModuleCollect.this.getCollectedXP(this.parent.drone) > 0)) {
                PacketDispatcher.sendToServer(new PacketDroneTransferXP(this.parent.drone));
            }
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            int aoe = this.parent.drone.droneInfo.chip * 4;
            l.add("Area of effect: " + TextFormatting.YELLOW + aoe + "x" + aoe + "x" + aoe + " blocks" + TextFormatting.WHITE + " under drone");
            if (ModuleCollect.this.canFunctionAs(Module.xpCollect))
            {
                l.add("Collected XP: " + TextFormatting.YELLOW + ModuleCollect.this.getCollectedXP(this.parent.drone));
                int range = 4 * this.parent.drone.droneInfo.chip;
                l.add("XP transfer range: " + TextFormatting.YELLOW + range + " blocks");
            }
            if ((ModuleCollect.this.canFunctionAs(Module.itemCollect)) && (!this.parent.drone.droneInfo.hasEnabled(Module.itemInventory))) {
                l.add(TextFormatting.RED + "Need Item inventory mod to work!");
            }
        }
    }
}
