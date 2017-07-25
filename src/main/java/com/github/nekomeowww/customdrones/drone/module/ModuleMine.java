package com.github.nekomeowww.customdrones.drone.module;

import java.io.IOException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.config.GuiButtonExt;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetMineLimits;

public class ModuleMine
        extends Module
{
    public static Map<Block, Integer> blockToWeightMap = new HashMap();
    public static Map<Integer, List<Block>> weightToBlockMap = new HashMap();

    static
    {
        addBlockToMap(Blocks.DIRT, 1);
        addBlockToMap(Blocks.GRASS, 1);
        addBlockToMap(Blocks.GRAVEL, 1);
        addBlockToMap(Blocks.SAND, 1);
        addBlockToMap(Blocks.SANDSTONE, 2);
        addBlockToMap(Blocks.STONE, 2);
        addBlockToMap(Blocks.COAL_ORE, 3);
        addBlockToMap(Blocks.IRON_ORE, 4);
        addBlockToMap(Blocks.NETHERRACK, 5);
        addBlockToMap(Blocks.SOUL_SAND, 5);
        addBlockToMap(Blocks.GLOWSTONE, 6);
        addBlockToMap(Blocks.LAPIS_ORE, 7);
        addBlockToMap(Blocks.QUARTZ_ORE, 7);
        addBlockToMap(Blocks.REDSTONE_ORE, 8);
        addBlockToMap(Blocks.GOLD_ORE, 9);
        addBlockToMap(Blocks.OBSIDIAN, 10);
        addBlockToMap(Blocks.DIAMOND_ORE, 11);
        addBlockToMap(Blocks.EMERALD_ORE, 11);
    }

    public static void addBlockToMap(Block block, int weight)
    {
        blockToWeightMap.put(block, Integer.valueOf(weight));
        List<Block> l = new ArrayList();
        if (weightToBlockMap.containsKey(Integer.valueOf(weight))) {
            l = (List)weightToBlockMap.get(Integer.valueOf(weight));
        }
        l.add(block);
        weightToBlockMap.put(Integer.valueOf(weight), l);
    }

    public ModuleMine(int l, String s)
    {
        super(l, Module.ModuleType.Collect, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleMineGui(gui, this);
    }

    public void updateModule(EntityDrone drone)
    {
        super.updateModule(drone);
        List<Map.Entry<BlockPos, IBlockState>> minables = new ArrayList();
        BlockPos bp0 = new BlockPos(drone);
        Integer[] limits = getLimits(drone.droneInfo);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (((x == 0) && (y == 0)) || ((x == 0) && (z == 0)) || ((y == 0) && (z == 0)))
                    {
                        BlockPos bp = bp0.add(x, y, z);
                        boolean limited = false;
                        if (limits != null)
                        {
                            int minx = Math.min(limits[0].intValue(), limits[3].intValue());
                            int maxx = Math.max(limits[0].intValue(), limits[3].intValue());
                            int miny = Math.min(limits[1].intValue(), limits[4].intValue());
                            int maxy = Math.max(limits[1].intValue(), limits[4].intValue());
                            int minz = Math.min(limits[2].intValue(), limits[5].intValue());
                            int maxz = Math.max(limits[2].intValue(), limits[5].intValue());
                            if ((bp.getX() < minx) || (bp.getX() > maxx) || (bp.getY() < miny) || (bp.getY() > maxy) ||
                                    (bp.getZ() < minz) || (bp.getZ() > maxz)) {
                                limited = true;
                            }
                        }
                        IBlockState bs = drone.getEntityWorld().getBlockState(bp);
                        if ((!limited) && (canMine(drone, bs.getBlock()))) {
                            minables.add(new AbstractMap.SimpleEntry(bp, bs));
                        }
                    }
                }
            }
        }
        if (minables.isEmpty())
        {
            getModNBT(drone.droneInfo).setBoolean("Stay to mine", false);
        }
        else
        {
            getModNBT(drone.droneInfo).setBoolean("Stay to mine", true);
            for (Map.Entry<BlockPos, IBlockState> entry : minables)
            {
                BlockPos bpMine = (BlockPos)entry.getKey();
                IBlockState bsMine = (IBlockState)entry.getValue();
                Block b = bsMine.getBlock();
                int hardness = (int)Math.ceil(bsMine.getBlockHardness(drone.getEntityWorld(), bpMine)) * 20 + 1;
                if (drone.ticksExisted % hardness == 0)
                {
                    if (!drone.getEntityWorld().isRemote)
                    {
                        b.dropXpOnBlockBreak(drone.getEntityWorld(), bpMine, b.getExpDrop(bsMine, drone.getEntityWorld(), bpMine, 0));
                        b.dropBlockAsItem(drone.getEntityWorld(), bpMine, bsMine, 0);
                        drone.getEntityWorld().setBlockToAir(bpMine);
                    }
                    drone.droneInfo.reduceBattery(hardness / 10 * drone.droneInfo.getBatteryConsumptionRate(drone));
                }
            }
        }
    }

    public boolean canMine(EntityDrone drone, Block b)
    {
        if ((blockToWeightMap.containsKey(b)) && (((Integer)blockToWeightMap.get(b)).intValue() <= this.level * 3)) {
            return true;
        }
        return false;
    }

    public void overrideDroneMovement(EntityDrone drone)
    {
        super.overrideDroneMovement(drone);
        moveToNextOre(drone);
    }

    public void moveToNextOre(EntityDrone drone)
    {
        Random rnd = new Random();
        Integer[] limits = getLimits(drone.droneInfo);
        Vec3d mid = EntityHelper.getCenterVec(drone);
        int oreWeight = 0;
        Vec3d orePos = null;
        double d0 = -1.0D;
        int range = getRange(drone);
        for (int ax = 0 - range; ax <= range; ax++)
        {
            int x = (int)Math.floor(mid.xCoord) + ax;
            if ((limits == null) || ((x >= Math.min(limits[0].intValue(), limits[3].intValue())) && (x <= Math.max(limits[0].intValue(), limits[3].intValue())))) {
                for (int az = 0 - range; az <= range; az++)
                {
                    int z = (int)Math.floor(mid.zCoord) + az;
                    if ((limits == null) || ((z >= Math.min(limits[2].intValue(), limits[5].intValue())) && (z <= Math.max(limits[2].intValue(), limits[5].intValue())))) {
                        for (int ay = 0 - range; ay <= range; ay++)
                        {
                            int y = (int)Math.floor(mid.yCoord) + ay;
                            if ((limits == null) || ((y >= Math.min(limits[1].intValue(), limits[4].intValue())) && (y <= Math.max(limits[1].intValue(), limits[4].intValue()))))
                            {
                                BlockPos bp = new BlockPos(x, y, z);
                                IBlockState bs = drone.getEntityWorld().getBlockState(bp);
                                if (canMine(drone, bs.getBlock()))
                                {
                                    int weight = ((Integer)blockToWeightMap.getOrDefault(bs.getBlock(), Integer.valueOf(0))).intValue();
                                    double d1 = mid.squareDistanceTo(x + 0.5D, y + 0.5D, z + 0.5D);
                                    Vec3d vec = new Vec3d(x, y, z);
                                    boolean weightGood = weight > oreWeight;

                                    boolean distGood = (d0 == -1.0D) || (d1 < d0) || ((Math.floor(d1) == Math.floor(d0)) && (rnd.nextInt(range) == 0));
                                    RayTraceResult rtr = drone.getEntityWorld().rayTraceBlocks(mid, vec.addVector(0.5D, 0.5D, 0.5D), false, true, false);

                                    boolean visible = (rtr == null) || (rtr.getBlockPos().equals(bp));
                                    if ((visible) && ((weightGood) || ((weight == oreWeight) && (distGood))))
                                    {
                                        d0 = d1;
                                        oreWeight = weight;
                                        orePos = vec;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if ((oreWeight > 0) && (orePos != null))
        {
            Vec3d dir = orePos.addVector(0.5D, 0.5D, 0.5D).subtract(mid);
            drone.flyNormalAlong(dir, 0.1D, 1.0D);
        }
    }

    public int overridePriority()
    {
        return 49;
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        Integer[] limits = getLimits(drone.droneInfo);
        if (limits != null)
        {
            int range = getRange(drone);
            int minx = Math.min(limits[0].intValue(), limits[3].intValue()) - range;
            int maxx = Math.max(limits[0].intValue(), limits[3].intValue()) + range;
            int miny = Math.min(limits[1].intValue(), limits[4].intValue()) - range;
            int maxy = Math.max(limits[1].intValue(), limits[4].intValue()) + range;
            int minz = Math.min(limits[2].intValue(), limits[5].intValue()) - range;
            int maxz = Math.max(limits[2].intValue(), limits[5].intValue()) + range;
            if ((drone.posX < minx) || (drone.posX > maxx) || (drone.posY < miny) || (drone.posY > maxy) || (drone.posZ < minz) || (drone.posZ > maxz)) {
                return false;
            }
        }
        return (drone.getFlyingMode() != 2) && (!getModNBT(drone.droneInfo).getBoolean("Stay to mine"));
    }

    public int getRange(EntityDrone drone)
    {
        return drone.droneInfo.chip * 3;
    }

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
    {
        super.additionalTooltip(list, forGuiDroneStatus);
        if (!forGuiDroneStatus) {
            list.add("Mine " + TextFormatting.AQUA + canMineString(canMine()));
        }
    }

    public List<Block> canMine()
    {
        List<Block> l = new ArrayList();
        for (int a = 1; a <= this.level * 3; a++) {
            l.addAll((Collection)weightToBlockMap.getOrDefault(Integer.valueOf(a), new ArrayList()));
        }
        return l;
    }

    public String canMineString(List<Block> l)
    {
        String s = "";
        if (!l.isEmpty())
        {
            s = s + oreName((Block)l.get(0));
            for (int a = 1; a < l.size(); a++) {
                s = s + ", " + oreName((Block)l.get(a));
            }
        }
        return s;
    }

    public String oreName(Block b)
    {
        String s = b.getLocalizedName();
        if ((s.endsWith("Ore")) || (s.endsWith("ore"))) {
            s = s.substring(0, s.length() - 4);
        }
        if (s.endsWith("Block")) {
            s = s.substring(0, s.length() - 6);
        }
        return s;
    }

    public Integer[] getLimits(DroneInfo di)
    {
        NBTTagCompound tag = getModNBT(di);
        if (tag != null)
        {
            Integer x0 = tag.hasKey("x0") ? Integer.valueOf(tag.getInteger("x0")) : null;
            Integer y0 = tag.hasKey("y0") ? Integer.valueOf(tag.getInteger("y0")) : null;
            Integer z0 = tag.hasKey("z0") ? Integer.valueOf(tag.getInteger("z0")) : null;
            Integer x1 = tag.hasKey("x1") ? Integer.valueOf(tag.getInteger("x1")) : null;
            Integer y1 = tag.hasKey("y1") ? Integer.valueOf(tag.getInteger("y1")) : null;
            Integer z1 = tag.hasKey("z1") ? Integer.valueOf(tag.getInteger("z1")) : null;
            if ((x0 != null) && (y0 != null) && (z0 != null) && (x1 != null) && (y1 != null) && (z1 != null)) {
                return new Integer[] { x0, y0, z0, x1, y1, z1 };
            }
        }
        return null;
    }

    public void setLimits(DroneInfo di, int x0, int y0, int z0, int x1, int y1, int z1)
    {
        NBTTagCompound tag = getModNBT(di);
        if (tag != null)
        {
            tag.setInteger("x0", x0);
            tag.setInteger("y0", y0);
            tag.setInteger("z0", z0);
            tag.setInteger("x1", x1);
            tag.setInteger("y1", y1);
            tag.setInteger("z1", z1);
        }
    }

    public void removeLimits(DroneInfo di)
    {
        NBTTagCompound tag = getModNBT(di);
        if (tag != null)
        {
            tag.removeTag("x0");
            tag.removeTag("y0");
            tag.removeTag("z0");
            tag.removeTag("x1");
            tag.removeTag("y1");
            tag.removeTag("z1");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleMineGui<T extends ModuleMine>
            extends Module.ModuleGui<T>
    {
        GuiTextField[] textFields = new GuiTextField[6];

        public ModuleMineGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void initGui()
        {
            super.initGui();
            int tx0 = 80;
            int tw = 45;
            int ty0 = 37;
            int ty1 = 52;
            int th = 12;
            this.textFields[0] = new GuiTextField(1, this.fontRendererObj, this.width / 2 - tx0, this.height / 2 + ty0, tw, th);
            this.textFields[1] = new GuiTextField(2, this.fontRendererObj, this.width / 2 - tx0 + tw + 5, this.height / 2 + ty0, tw, th);
            this.textFields[2] = new GuiTextField(3, this.fontRendererObj, this.width / 2 - tx0 + tw * 2 + 10, this.height / 2 + ty0, tw, th);

            this.textFields[3] = new GuiTextField(4, this.fontRendererObj, this.width / 2 - tx0, this.height / 2 + ty1, tw, th);
            this.textFields[4] = new GuiTextField(5, this.fontRendererObj, this.width / 2 - tx0 + tw + 5, this.height / 2 + ty1, tw, th);
            this.textFields[5] = new GuiTextField(6, this.fontRendererObj, this.width / 2 - tx0 + tw * 2 + 10, this.height / 2 + ty1, tw, th);
            for (GuiTextField txtf : this.textFields) {
                txtf.setMaxStringLength(6);
            }
            this.buttonList.add(new GuiButtonExt(1, this.width / 2 + 70, this.height / 2 + 35, 60, 30, "Set limits"));
            this.buttonList.add(new GuiButtonExt(2, this.width / 2 - 35, this.height / 2 + 70, 80, 20, "Remove limits"));
            this.buttonList.add(new GuiButtonExt(3, this.width / 2 - 98, this.height / 2 + 70, 60, 20, "Auto limits"));
            setLimitTexts();
        }

        public void setLimitTexts()
        {
            Integer[] ints = ((ModuleMine)this.mod).getLimits(this.parent.drone.droneInfo);
            if (ints != null) {
                for (int a = 0; a < 6; a++) {
                    this.textFields[a].setText(String.valueOf(ints[a]));
                }
            }
        }

        protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
                throws IOException
        {
            super.mouseClicked(mouseX, mouseY, mouseButton);
            for (GuiTextField txtf : this.textFields) {
                txtf.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }

        protected void keyTyped(char typedChar, int keyCode)
                throws IOException
        {
            super.keyTyped(typedChar, keyCode);
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211) || (keyCode == 12) || (keyCode == 74)) {
                for (GuiTextField txtf : this.textFields) {
                    if ((txtf != null) && (txtf.isFocused())) {
                        if (((keyCode != 12) && (keyCode != 74)) || (txtf.getCursorPosition() == 0)) {
                            txtf.textboxKeyTyped(typedChar, keyCode);
                        }
                    }
                }
            }
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            Integer[] ints;
            int a;
            int b;
            String s;
            GuiTextField[] txtfieldList;
            if (button.id == 1)
            {
                ints = new Integer[6];
                for (a = 0; a < 6; a++)
                {
                    s = this.textFields[a].getText();
                    if ((s.length() > 0) && (!s.equals("-")))
                    {
                        ints[a] = Integer.valueOf(s);
                    }
                    else
                    {
                        ints = null;
                        break;
                    }
                }
                if (ints != null)
                {
                    PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, ints[0].intValue(), ints[1].intValue(), ints[2].intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue()));
                    ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, ints[0].intValue(), ints[1].intValue(), ints[2].intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue());
                    setLimitTexts();
                }
            }
            else if (button.id == 2)
            {
                PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, true, 0, 0, 0, 0, 0, 0));
                ((ModuleMine)this.mod).removeLimits((this.parent.drone.droneInfo));
                txtfieldList = this.textFields; a = txtfieldList.length;
                for (b = 0; b < a; b++)
                {
                    GuiTextField txtf = txtfieldList[b];

                    txtf.setText("");
                }
            }
            /*
            else if (button.id == 2)
            {
                PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, true, 0, 0, 0, 0, 0, 0));
                ((ModuleMine)this.mod).removeLimits(this.parent.drone.droneInfo);
                ints = this.textFields;a = ints.length;
                for (b = 0; b < a; b++)
                {
                    GuiTextField txtf = ints[b];

                    txtf.setText("");
                }
            }
            */
            else if (button.id == 3)
            {
                int limitRange = ModuleMine.this.getRange(this.parent.drone);
                int minX = (int)(this.parent.drone.posX - limitRange);
                int minY = (int)Math.max(this.parent.drone.posY - limitRange, 0.0D);
                int minZ = (int)(this.parent.drone.posZ - limitRange);
                int maxX = (int)(this.parent.drone.posX + limitRange);
                int maxY = (int)Math.max(this.parent.drone.posY, 0.0D);
                int maxZ = (int)(this.parent.drone.posZ + limitRange);
                PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, minX, minY, minZ, maxX, maxY, maxZ));

                ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, minX, minY, minZ, maxX, maxY, maxZ);
                setLimitTexts();
            }
        }

        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            ScaledResolution sr = new ScaledResolution(this.mc);
            int sclW = sr.getScaledWidth();
            int sclH = sr.getScaledHeight();
            for (GuiTextField txtf : this.textFields) {
                txtf.drawTextBox();
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
            this.fontRendererObj.drawString("X", sclW / 2 - 60, sclH / 2 + 28, 16777215, true);
            this.fontRendererObj.drawString("Y", sclW / 2 - 10, sclH / 2 + 28, 16777215, true);
            this.fontRendererObj.drawString("Z", sclW / 2 + 40, sclH / 2 + 28, 16777215, true);
            this.fontRendererObj.drawString("Corner A:", sclW / 2 - 135, sclH / 2 + 40, 16777215, true);
            this.fontRendererObj.drawString("Corner B:", sclW / 2 - 135, sclH / 2 + 55, 16777215, true);
        }
    }
}
