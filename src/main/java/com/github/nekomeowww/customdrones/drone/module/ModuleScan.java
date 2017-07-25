package com.github.nekomeowww.customdrones.drone.module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockOre;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleScan
        extends Module
{
    public ModuleScan(int l, String s)
    {
        super(l, Module.ModuleType.Scout, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleScanGui(gui, this);
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (this == mobScan1) {
            list.add("Scan nearby animals");
        } else if (this == mobScan2) {
            list.add("Scan all nearby entities");
        } else if (this == oreScan) {
            list.add("Scan nearby ores");
        } else if (this == multiScan) {
            list.add("Scan nearby ores and entities");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleScanGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        boolean mob;
        boolean ore;
        double range;
        Map<String, Integer> entitiesScan = new HashMap();
        Map<String, Integer> oresScan = new HashMap();

        public ModuleScanGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
            this.mob = mod.canFunctionAs(Module.mobScan1);
            this.ore = mod.canFunctionAs(Module.oreScan);
            this.range = ((this.parent.drone.droneInfo.chip + mod.level) * 2);
        }

        public void initGui()
        {
            super.initGui();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 34, this.height / 2 + 70, 80, 20, "Change range"));
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if (button.id == 1)
            {
                int maxsteps = 5;
                double maxRange = (this.parent.drone.droneInfo.chip + this.mod.level) * 2;
                int i1 = (int)Math.round(this.range / maxRange * maxsteps);
                int i2 = i1 + 1;
                if (i2 > maxsteps) {
                    i2 = 1;
                }
                this.range = (maxRange * i2 / maxsteps);
                this.entitiesScan.clear();
                this.oresScan.clear();
            }
        }

        public void updateScreen()
        {
            super.updateScreen();
            if (!this.parent.drone.droneInfo.isEnabled(this.mod)) {
                return;
            }
            World world = this.parent.drone.getEntityWorld();
            AxisAlignedBB aabb = this.parent.drone.getEntityBoundingBox().expand(this.range, this.range, this.range);
            if ((this.mob) && ((this.parent.drone.ticksExisted % 20 == 0) || (this.entitiesScan.isEmpty())))
            {
                this.entitiesScan.clear();
                List<Entity> entities = world.getEntitiesWithinAABB(this.mod == Module.mobScan1 ? EntityAnimal.class : Entity.class, aabb, EntitySelectors.IS_ALIVE);

                entities.remove(this.parent.drone);
                for (int a = 0; a < entities.size(); a++)
                {
                    Entity e = (Entity)entities.get(a);
                    String entityName = entityName(e);
                    int count = this.entitiesScan.containsKey(entityName) ? ((Integer)this.entitiesScan.get(entityName)).intValue() : 0;
                    count++;
                    this.entitiesScan.put(entityName, Integer.valueOf(count));
                }
            }
            if ((this.ore) && ((this.parent.drone.ticksExisted % 100 == 0) || (this.oresScan.isEmpty())))
            {
                this.oresScan.clear();
                for (int x = (int)Math.floor(this.parent.drone.posX - this.range); x <= this.parent.drone.posX + this.range; x++) {
                    for (int y = (int)Math.floor(this.parent.drone.posY - this.range); y <= this.parent.drone.posY + this.range; y++) {
                        for (int z = (int)Math.floor(this.parent.drone.posZ - this.range); z <= this.parent.drone.posZ + this.range; z++)
                        {
                            BlockPos bp = new BlockPos(x, y, z);
                            IBlockState bs = world.getBlockState(bp);
                            Block b = bs.getBlock();
                            if ((b instanceof BlockOre))
                            {
                                String oreName = oreName(b);
                                int count = this.oresScan.containsKey(oreName) ? ((Integer)this.oresScan.get(oreName)).intValue() : 0;
                                count++;
                                this.oresScan.put(oreName, Integer.valueOf(count));
                            }
                        }
                    }
                }
            }
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
            if (!this.parent.drone.droneInfo.isEnabled(this.mod)) {
                return;
            }
            int rangei = (int)Math.round(this.range);
            l.add("Scan range: " + TextFormatting.YELLOW + rangei * 2 + "x" + rangei * 2 + "x" + rangei * 2 + TextFormatting.RESET + " blocks");
            if (this.mob)
            {
                String s = TextFormatting.GREEN + "" + TextFormatting.BOLD + "Entities: " + TextFormatting.RESET;
                int count;
                if (this.entitiesScan.isEmpty())
                {
                    s = s + "none";
                }
                else
                {
                    count = 0;
                    for (Map.Entry<String, Integer> e : this.entitiesScan.entrySet())
                    {
                        s = s + (count > 0 ? ", " : "") + (String)e.getKey() + ": " + TextFormatting.GREEN + e.getValue() + TextFormatting.RESET;

                        count++;
                    }
                }
                l.add(s);
            }
            if (this.ore)
            {
                String s = TextFormatting.RED + "" + TextFormatting.BOLD + "Ores: " + TextFormatting.RESET;
                int count;
                if (this.oresScan.isEmpty())
                {
                    s = s + "none";
                }
                else
                {
                    count = 0;
                    for (Map.Entry<String, Integer> e : this.oresScan.entrySet())
                    {
                        s = s + (count > 0 ? ", " : "") + (String)e.getKey() + ": " + TextFormatting.RED + e.getValue() + TextFormatting.RESET;

                        count++;
                    }
                }
                l.add(s);
            }
        }

        public String entityName(Entity e)
        {
            if ((e instanceof EntityDrone)) {
                return "Other Drone";
            }
            if ((e instanceof EntityPlayer)) {
                return "Player";
            }
            if ((e instanceof EntityItem)) {
                return "Dropped item";
            }
            return e.getName();
        }

        public String oreName(Block b)
        {
            String s = b.getLocalizedName();
            if ((s.endsWith("Ore")) || (s.endsWith("ore"))) {
                s = s.substring(0, s.length() - 4);
            }
            return s;
        }
    }
}
