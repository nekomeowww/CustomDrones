package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityTNTPrimed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import williamle.drones.entity.EntityDrone;
import williamle.drones.gui.GuiDroneStatus;
import williamle.drones.network.PacketDispatcher;
import williamle.drones.network.server.PacketDroneDropRider;

public class ModuleTransport
        extends Module
{
    public ModuleTransport(int l, String s)
    {
        super(l, Module.ModuleType.Transport, s);
    }

    public boolean collideWithBlock(EntityDrone drone, BlockPos pos, IBlockState state)
    {
        if ((!drone.field_70170_p.field_72995_K) && (canFunctionAs(nplayerTransport)) && (drone.getRider() == null) &&
                (state.func_177230_c() == Blocks.field_150335_W))
        {
            drone.field_70170_p.func_175698_g(pos);

            EntityTNTPrimed tnt = new EntityTNTPrimed(drone.field_70170_p, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), drone.getControllingPlayer() == null ? drone : drone.getControllingPlayer());
            tnt.func_184220_m(drone);
            drone.field_70170_p.func_72838_d(tnt);
        }
        return super.collideWithBlock(drone, pos, state);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        if ((canFunctionAs(nplayerTransport)) && ((drone.getRider() instanceof EntityTNTPrimed))) {
            ((EntityTNTPrimed)drone.getRider()).func_184534_a(80 - drone.field_70173_aa % 20);
        }
    }

    public double costBatRawPerSec(EntityDrone drone)
    {
        if (drone.getRider() != null) {
            return super.costBatRawPerSec(drone) + drone.getRider().field_70130_N * drone.getRider().field_70131_O;
        }
        return super.costBatRawPerSec(drone);
    }

    public void onDisabled(EntityDrone drone)
    {
        super.onDisabled(drone);
        if ((this == nplayerTransport) || (this == playerTransport) || (this == multiTransport)) {
            drone.dropRider();
        }
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (this == itemInventory) {
            list.add("Install drone inventory");
        } else if (this == nplayerTransport) {
            list.add("Transport non-player entities");
        } else if (this == playerTransport) {
            list.add("Transport players");
        } else if (this == multiTransport) {
            list.add("Install inventory and transport all entities");
        }
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleTransportGui(gui, this);
    }

    @SideOnly(Side.CLIENT)
    public class ModuleTransportGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        boolean transportEntity;

        public ModuleTransportGui(T gui)
        {
            super(mod);
            this.transportEntity = ((mod == Module.nplayerTransport) || (mod == Module.playerTransport) || (mod == Module.multiTransport));
        }

        public void func_73866_w_()
        {
            super.func_73866_w_();
            if (this.transportEntity) {
                this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 24, this.field_146295_m / 2 + 70, 70, 20, "Drop entity"));
            }
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (this.transportEntity)
            {
                String transporting = "None";
                if ((this.parent.drone.getRider() instanceof EntityPlayer)) {
                    transporting = TextFormatting.AQUA + ((EntityPlayer)this.parent.drone.getRider()).func_70005_c_();
                } else if (this.parent.drone.getRider() != null) {
                    transporting = TextFormatting.AQUA + this.parent.drone.getRider().func_70005_c_();
                }
                l.add("Transporting entity: " + transporting);
                if (this.parent.drone.getRider() != null)
                {
                    double cost = this.parent.drone.getRider().field_70130_N * this.parent.drone.getRider().field_70131_O;
                    l.add("Transporting weight cost: " + TextFormatting.RED + Math.round(cost * 100.0D) / 100.0D + " battery/sec");
                }
            }
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if ((button.field_146127_k == 1) && (this.parent.drone.getRider() != null)) {
                PacketDispatcher.sendToServer(new PacketDroneDropRider(this.parent.drone));
            }
        }
    }
}
