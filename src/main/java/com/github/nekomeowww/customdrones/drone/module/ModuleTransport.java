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
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneDropRider;

public class ModuleTransport
        extends Module
{
    public ModuleTransport(int l, String s)
    {
        super(l, Module.ModuleType.Transport, s);
    }

    public boolean collideWithBlock(EntityDrone drone, BlockPos pos, IBlockState state)
    {
        if ((!drone.getEntityWorld().isRemote) && (canFunctionAs(nplayerTransport)) && (drone.getRider() == null) &&
                (state.getBlock() == Blocks.TNT))
        {
            drone.getEntityWorld().setBlockToAir(pos);

            EntityTNTPrimed tnt = new EntityTNTPrimed(drone.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ(), drone.getControllingPlayer() == null ? drone : drone.getControllingPlayer());
            tnt.startRiding(drone);
            drone.getEntityWorld().spawnEntityInWorld(tnt);
        }
        return super.collideWithBlock(drone, pos, state);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        if ((canFunctionAs(nplayerTransport)) && ((drone.getRider() instanceof EntityTNTPrimed))) {
            ((EntityTNTPrimed)drone.getRider()).setFuse(80 - drone.ticksExisted % 20);
        }
    }

    public double costBatRawPerSec(EntityDrone drone)
    {
        if (drone.getRider() != null) {
            return super.costBatRawPerSec(drone) + drone.getRider().width * drone.getRider().height;
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

        public void initGui()
        {
            super.initGui();
            if (this.transportEntity) {
                this.buttonList.add(new GuiButton(1, this.width / 2 - 24, this.height / 2 + 70, 70, 20, "Drop entity"));
            }
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (this.transportEntity)
            {
                String transporting = "None";
                if ((this.parent.drone.getRider() instanceof EntityPlayer)) {
                    transporting = TextFormatting.AQUA + ((EntityPlayer)this.parent.drone.getRider()).getName();
                } else if (this.parent.drone.getRider() != null) {
                    transporting = TextFormatting.AQUA + this.parent.drone.getRider().getName();
                }
                l.add("Transporting entity: " + transporting);
                if (this.parent.drone.getRider() != null)
                {
                    double cost = this.parent.drone.getRider().width * this.parent.drone.getRider().height;
                    l.add("Transporting weight cost: " + TextFormatting.RED + Math.round(cost * 100.0D) / 100.0D + " battery/sec");
                }
            }
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if ((button.id == 1) && (this.parent.drone.getRider() != null)) {
                PacketDispatcher.sendToServer(new PacketDroneDropRider(this.parent.drone));
            }
        }
    }
}
