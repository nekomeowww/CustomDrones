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
        World world = drone.field_70170_p;

        AxisAlignedBB aabb = new AxisAlignedBB(drone.field_70165_t, drone.field_70163_u, drone.field_70161_v, drone.field_70165_t, drone.field_70163_u, drone.field_70161_v).func_72314_b(2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip).func_72317_d(0.0D, -drone.droneInfo.chip * 0.5D, 0.0D).func_72321_a(0.0D, 0.25D, 0.0D);
        if ((drone.droneInfo.hasInventory()) && (canFunctionAs(Module.itemCollect)))
        {
            List<EntityItem> eis = world.func_72872_a(EntityItem.class, aabb);
            for (EntityItem ei : eis)
            {
                ItemStack is = ei.func_92059_d();
                if (drone.droneInfo.inventory.canAddToInv(is, false)) {
                    if ((!ei.func_174874_s()) && (ei.func_70068_e(drone) <= 1.0D))
                    {
                        if (is != null)
                        {
                            ItemStack is0 = drone.droneInfo.inventory.addToInv(is);
                            if ((is0 == null) || (is0.field_77994_a == 0)) {
                                ei.func_70106_y();
                            } else {
                                ei.func_92058_a(is0);
                            }
                        }
                    }
                    else
                    {
                        Vec3d vec = VecHelper.fromTo(ei.func_174791_d(), drone.func_174791_d());
                        vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
                        ei.func_70024_g(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
                    }
                }
            }
        }
        if (canFunctionAs(Module.xpCollect))
        {
            List<EntityXPOrb> xpos = world.func_72872_a(EntityXPOrb.class, aabb);
            for (EntityXPOrb xpo : xpos) {
                if ((xpo.field_70532_c == 0) && (xpo.func_70068_e(drone) <= 1.0D))
                {
                    setCollectedXP(drone, getCollectedXP(drone) + xpo.func_70526_d());
                    xpo.func_70106_y();
                }
                else
                {
                    Vec3d vec = VecHelper.fromTo(xpo.func_174791_d(), drone.func_174791_d());
                    vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
                    xpo.func_70024_g(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
                }
            }
        }
    }

    public int getCollectedXP(EntityDrone drone)
    {
        return getModNBT(drone.droneInfo).func_74762_e("Collected XP");
    }

    public void setCollectedXP(EntityDrone drone, int i)
    {
        NBTTagCompound tag = getModNBT(drone.droneInfo);
        if (tag != null) {
            tag.func_74768_a("Collected XP", i);
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

        public void func_73866_w_()
        {
            super.func_73866_w_();
            boolean xp = ModuleCollect.this.canFunctionAs(Module.xpCollect);
            if (xp) {
                this.field_146292_n.add(this.buttonTransfer = new GuiButton(1, this.field_146294_l / 2 - 24, this.field_146295_m / 2 + 70, 70, 20, "Transfer XP"));
            }
        }

        public void func_73876_c()
        {
            super.func_73876_c();
            boolean xp = this.mod.canFunctionAs(Module.xpCollect);
            if (xp)
            {
                int range = 4 * this.parent.drone.droneInfo.chip;
                if (this.parent.drone.func_70068_e(this.parent.player) <= range * range)
                {
                    this.buttonTransfer.field_146124_l = true;
                    this.buttonTransfer.field_146126_j = "Transfer XP";
                }
                else
                {
                    this.buttonTransfer.field_146124_l = false;
                    this.buttonTransfer.field_146126_j = "Out of range";
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
