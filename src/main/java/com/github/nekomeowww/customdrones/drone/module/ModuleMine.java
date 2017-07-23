package com.github.nekomeowww.customdrones.drone.module;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
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
        addBlockToMap(Blocks.field_150346_d, 1);
        addBlockToMap(Blocks.field_150349_c, 1);
        addBlockToMap(Blocks.field_150351_n, 1);
        addBlockToMap(Blocks.field_150354_m, 1);
        addBlockToMap(Blocks.field_150322_A, 2);
        addBlockToMap(Blocks.field_150348_b, 2);
        addBlockToMap(Blocks.field_150365_q, 3);
        addBlockToMap(Blocks.field_150366_p, 4);
        addBlockToMap(Blocks.field_150424_aL, 5);
        addBlockToMap(Blocks.field_150425_aM, 5);
        addBlockToMap(Blocks.field_150426_aN, 6);
        addBlockToMap(Blocks.field_150369_x, 7);
        addBlockToMap(Blocks.field_150449_bY, 7);
        addBlockToMap(Blocks.field_150450_ax, 8);
        addBlockToMap(Blocks.field_150352_o, 9);
        addBlockToMap(Blocks.field_150343_Z, 10);
        addBlockToMap(Blocks.field_150482_ag, 11);
        addBlockToMap(Blocks.field_150412_bA, 11);
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
                        BlockPos bp = bp0.func_177982_a(x, y, z);
                        boolean limited = false;
                        if (limits != null)
                        {
                            int minx = Math.min(limits[0].intValue(), limits[3].intValue());
                            int maxx = Math.max(limits[0].intValue(), limits[3].intValue());
                            int miny = Math.min(limits[1].intValue(), limits[4].intValue());
                            int maxy = Math.max(limits[1].intValue(), limits[4].intValue());
                            int minz = Math.min(limits[2].intValue(), limits[5].intValue());
                            int maxz = Math.max(limits[2].intValue(), limits[5].intValue());
                            if ((bp.func_177958_n() < minx) || (bp.func_177958_n() > maxx) || (bp.func_177956_o() < miny) || (bp.func_177956_o() > maxy) ||
                                    (bp.func_177952_p() < minz) || (bp.func_177952_p() > maxz)) {
                                limited = true;
                            }
                        }
                        IBlockState bs = drone.field_70170_p.func_180495_p(bp);
                        if ((!limited) && (canMine(drone, bs.func_177230_c()))) {
                            minables.add(new AbstractMap.SimpleEntry(bp, bs));
                        }
                    }
                }
            }
        }
        if (minables.isEmpty())
        {
            getModNBT(drone.droneInfo).func_74757_a("Stay to mine", false);
        }
        else
        {
            getModNBT(drone.droneInfo).func_74757_a("Stay to mine", true);
            for (Map.Entry<BlockPos, IBlockState> entry : minables)
            {
                BlockPos bpMine = (BlockPos)entry.getKey();
                IBlockState bsMine = (IBlockState)entry.getValue();
                Block b = bsMine.func_177230_c();
                int hardness = (int)Math.ceil(bsMine.func_185887_b(drone.field_70170_p, bpMine)) * 20 + 1;
                if (drone.field_70173_aa % hardness == 0)
                {
                    if (!drone.field_70170_p.field_72995_K)
                    {
                        b.func_180637_b(drone.field_70170_p, bpMine, b.getExpDrop(bsMine, drone.field_70170_p, bpMine, 0));
                        b.func_176226_b(drone.field_70170_p, bpMine, bsMine, 0);
                        drone.field_70170_p.func_175698_g(bpMine);
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
            int x = (int)Math.floor(mid.field_72450_a) + ax;
            if ((limits == null) || ((x >= Math.min(limits[0].intValue(), limits[3].intValue())) && (x <= Math.max(limits[0].intValue(), limits[3].intValue())))) {
                for (int az = 0 - range; az <= range; az++)
                {
                    int z = (int)Math.floor(mid.field_72449_c) + az;
                    if ((limits == null) || ((z >= Math.min(limits[2].intValue(), limits[5].intValue())) && (z <= Math.max(limits[2].intValue(), limits[5].intValue())))) {
                        for (int ay = 0 - range; ay <= range; ay++)
                        {
                            int y = (int)Math.floor(mid.field_72448_b) + ay;
                            if ((limits == null) || ((y >= Math.min(limits[1].intValue(), limits[4].intValue())) && (y <= Math.max(limits[1].intValue(), limits[4].intValue()))))
                            {
                                BlockPos bp = new BlockPos(x, y, z);
                                IBlockState bs = drone.field_70170_p.func_180495_p(bp);
                                if (canMine(drone, bs.func_177230_c()))
                                {
                                    int weight = ((Integer)blockToWeightMap.getOrDefault(bs.func_177230_c(), Integer.valueOf(0))).intValue();
                                    double d1 = mid.func_186679_c(x + 0.5D, y + 0.5D, z + 0.5D);
                                    Vec3d vec = new Vec3d(x, y, z);
                                    boolean weightGood = weight > oreWeight;

                                    boolean distGood = (d0 == -1.0D) || (d1 < d0) || ((Math.floor(d1) == Math.floor(d0)) && (rnd.nextInt(range) == 0));
                                    RayTraceResult rtr = drone.field_70170_p.func_147447_a(mid, vec.func_72441_c(0.5D, 0.5D, 0.5D), false, true, false);

                                    boolean visible = (rtr == null) || (rtr.func_178782_a().equals(bp));
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
            Vec3d dir = orePos.func_72441_c(0.5D, 0.5D, 0.5D).func_178788_d(mid);
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
            if ((drone.field_70165_t < minx) || (drone.field_70165_t > maxx) || (drone.field_70163_u < miny) || (drone.field_70163_u > maxy) || (drone.field_70161_v < minz) || (drone.field_70161_v > maxz)) {
                return false;
            }
        }
        return (drone.getFlyingMode() != 2) && (!getModNBT(drone.droneInfo).func_74767_n("Stay to mine"));
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
        String s = b.func_149732_F();
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
            Integer x0 = tag.func_74764_b("x0") ? Integer.valueOf(tag.func_74762_e("x0")) : null;
            Integer y0 = tag.func_74764_b("y0") ? Integer.valueOf(tag.func_74762_e("y0")) : null;
            Integer z0 = tag.func_74764_b("z0") ? Integer.valueOf(tag.func_74762_e("z0")) : null;
            Integer x1 = tag.func_74764_b("x1") ? Integer.valueOf(tag.func_74762_e("x1")) : null;
            Integer y1 = tag.func_74764_b("y1") ? Integer.valueOf(tag.func_74762_e("y1")) : null;
            Integer z1 = tag.func_74764_b("z1") ? Integer.valueOf(tag.func_74762_e("z1")) : null;
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
            tag.func_74768_a("x0", x0);
            tag.func_74768_a("y0", y0);
            tag.func_74768_a("z0", z0);
            tag.func_74768_a("x1", x1);
            tag.func_74768_a("y1", y1);
            tag.func_74768_a("z1", z1);
        }
    }

    public void removeLimits(DroneInfo di)
    {
        NBTTagCompound tag = getModNBT(di);
        if (tag != null)
        {
            tag.func_82580_o("x0");
            tag.func_82580_o("y0");
            tag.func_82580_o("z0");
            tag.func_82580_o("x1");
            tag.func_82580_o("y1");
            tag.func_82580_o("z1");
        }
    }

    @SideOnly(Side.CLIENT)
    public class ModuleMineGui<T extends ModuleMine>
            extends Module.ModuleGui<T>
    {
        GuiTextField[] textFields = new GuiTextField[6];

        public ModuleMineGui(T gui)
        {
            super(mod);
        }

        public void func_73866_w_()
        {
            super.func_73866_w_();
            int tx0 = 80;
            int tw = 45;
            int ty0 = 37;
            int ty1 = 52;
            int th = 12;
            this.textFields[0] = new GuiTextField(1, this.field_146289_q, this.field_146294_l / 2 - tx0, this.field_146295_m / 2 + ty0, tw, th);
            this.textFields[1] = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw + 5, this.field_146295_m / 2 + ty0, tw, th);
            this.textFields[2] = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw * 2 + 10, this.field_146295_m / 2 + ty0, tw, th);

            this.textFields[3] = new GuiTextField(4, this.field_146289_q, this.field_146294_l / 2 - tx0, this.field_146295_m / 2 + ty1, tw, th);
            this.textFields[4] = new GuiTextField(5, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw + 5, this.field_146295_m / 2 + ty1, tw, th);
            this.textFields[5] = new GuiTextField(6, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw * 2 + 10, this.field_146295_m / 2 + ty1, tw, th);
            for (GuiTextField txtf : this.textFields) {
                txtf.func_146203_f(6);
            }
            this.field_146292_n.add(new GuiButtonExt(1, this.field_146294_l / 2 + 70, this.field_146295_m / 2 + 35, 60, 30, "Set limits"));
            this.field_146292_n.add(new GuiButtonExt(2, this.field_146294_l / 2 - 35, this.field_146295_m / 2 + 70, 80, 20, "Remove limits"));
            this.field_146292_n.add(new GuiButtonExt(3, this.field_146294_l / 2 - 98, this.field_146295_m / 2 + 70, 60, 20, "Auto limits"));
            setLimitTexts();
        }

        public void setLimitTexts()
        {
            Integer[] ints = ((ModuleMine)this.mod).getLimits(this.parent.drone.droneInfo);
            if (ints != null) {
                for (int a = 0; a < 6; a++) {
                    this.textFields[a].func_146180_a(String.valueOf(ints[a]));
                }
            }
        }

        protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
                throws IOException
        {
            super.func_73864_a(mouseX, mouseY, mouseButton);
            for (GuiTextField txtf : this.textFields) {
                txtf.func_146192_a(mouseX, mouseY, mouseButton);
            }
        }

        protected void func_73869_a(char typedChar, int keyCode)
                throws IOException
        {
            super.func_73869_a(typedChar, keyCode);
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211) || (keyCode == 12) || (keyCode == 74)) {
                for (GuiTextField txtf : this.textFields) {
                    if ((txtf != null) && (txtf.func_146206_l())) {
                        if (((keyCode != 12) && (keyCode != 74)) || (txtf.func_146198_h() == 0)) {
                            txtf.func_146201_a(typedChar, keyCode);
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
            String s;
            if (button.field_146127_k == 1)
            {
                ints = new Integer[6];
                for (a = 0; a < 6; a++)
                {
                    s = this.textFields[a].func_146179_b();
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
                    PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, ints[0].intValue(), ints[1].intValue(), ints[2]
                            .intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue()));
                    ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, ints[0].intValue(), ints[1].intValue(), ints[2].intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue());
                    setLimitTexts();
                }
            }
            else if (button.field_146127_k == 2)
            {
                PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, true, 0, 0, 0, 0, 0, 0));
                ((ModuleMine)this.mod).removeLimits(this.parent.drone.droneInfo);
                ints = this.textFields;a = ints.length;
                for (s = 0; s < a; s++)
                {
                    GuiTextField txtf = ints[s];

                    txtf.func_146180_a("");
                }
            }
            else if (button.field_146127_k == 3)
            {
                int limitRange = ModuleMine.this.getRange(this.parent.drone);
                int minX = (int)(this.parent.drone.field_70165_t - limitRange);
                int minY = (int)Math.max(this.parent.drone.field_70163_u - limitRange, 0.0D);
                int minZ = (int)(this.parent.drone.field_70161_v - limitRange);
                int maxX = (int)(this.parent.drone.field_70165_t + limitRange);
                int maxY = (int)Math.max(this.parent.drone.field_70163_u, 0.0D);
                int maxZ = (int)(this.parent.drone.field_70161_v + limitRange);
                PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, minX, minY, minZ, maxX, maxY, maxZ));

                ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, minX, minY, minZ, maxX, maxY, maxZ);
                setLimitTexts();
            }
        }

        public void func_73863_a(int mouseX, int mouseY, float partialTicks)
        {
            ScaledResolution sr = new ScaledResolution(this.field_146297_k);
            int sclW = sr.func_78326_a();
            int sclH = sr.func_78328_b();
            for (GuiTextField txtf : this.textFields) {
                txtf.func_146194_f();
            }
            super.func_73863_a(mouseX, mouseY, partialTicks);
            this.field_146289_q.func_175065_a("X", sclW / 2 - 60, sclH / 2 + 28, 16777215, true);
            this.field_146289_q.func_175065_a("Y", sclW / 2 - 10, sclH / 2 + 28, 16777215, true);
            this.field_146289_q.func_175065_a("Z", sclW / 2 + 40, sclH / 2 + 28, 16777215, true);
            this.field_146289_q.func_175065_a("Corner A:", sclW / 2 - 135, sclH / 2 + 40, 16777215, true);
            this.field_146289_q.func_175065_a("Corner B:", sclW / 2 - 135, sclH / 2 + 55, 16777215, true);
        }
    }
}
