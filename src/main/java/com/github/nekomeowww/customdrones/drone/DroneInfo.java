package com.github.nekomeowww.customdrones.drone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.drone.module.ModuleArmor;
import com.github.nekomeowww.customdrones.drone.module.ModuleBatterySave;
import com.github.nekomeowww.customdrones.drone.module.ModulePlaceHolder;
import com.github.nekomeowww.customdrones.drone.module.ModuleRecharge;
import com.github.nekomeowww.customdrones.drone.module.ModuleWeapon;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.client.PacketDroneInfo;

public class DroneInfo
{
    public static final String[] greekNumber = { "Nan", "I", "II", "III", "IV" };
    public static final Map<Object, Double> batteryFuel = new HashMap();
    public static final Map<Object, Double> damageRecover = new HashMap();

    static
    {
        batteryFuel.put(Items.field_151044_h, Double.valueOf(10.0D));
        batteryFuel.put(Items.field_151042_j, Double.valueOf(100.0D));
        batteryFuel.put(Items.field_151043_k, Double.valueOf(200.0D));
        batteryFuel.put(Items.field_151045_i, Double.valueOf(1000.0D));
        batteryFuel.put(Items.field_151166_bC, Double.valueOf(2000.0D));
        damageRecover.put(Blocks.field_150339_S, Double.valueOf(10.0D));
        damageRecover.put(Blocks.field_150340_R, Double.valueOf(20.0D));
        damageRecover.put(Blocks.field_150484_ah, Double.valueOf(40.0D));
        damageRecover.put(Blocks.field_150475_bE, Double.valueOf(60.0D));
    }

    public static int nextID = 1;
    public String name = "#$Drone";
    public int id;
    public int chip = 1;
    public int core = 1;
    public int casing = 1;
    public int engine = 1;
    private double battery;
    private double prevBattery;
    private double damage;
    private double engineLevel;
    public boolean isChanged = false;
    public int droneFreq = -1;
    public List<Module> mods = new ArrayList();
    public List<Module> disabledMods = new ArrayList();
    public NBTTagCompound modsNBT = new NBTTagCompound();
    public InventoryDrone inventory = new InventoryDrone(this);
    public final DroneAppearance appearance = new DroneAppearance();

    public DroneInfo()
    {
        this(null);
    }

    public DroneInfo(EntityDrone e)
    {
        this(e, 1, 1, 1, 1);
    }

    public DroneInfo(int ch, int co, int ca, int en)
    {
        this(null, ch, co, ca, en);
    }

    public DroneInfo(EntityDrone e, int ch, int co, int ca, int en)
    {
        this.chip = ch;
        this.core = co;
        this.casing = ca;
        this.engine = en;
        this.battery = getMaxBattery();
        this.damage = getMaxDamage(e);
        this.engineLevel = 1.0D;
    }

    public DroneInfo newID()
    {
        this.id = (nextID++);
        this.isChanged = true;
        return this;
    }

    public EntityDrone getDrone(World world)
    {
        return EntityDrone.getDroneFromID(world, this.id);
    }

    public void updateDroneInfo(EntityDrone drone)
    {
        this.prevBattery = this.battery;
        if ((this.battery == 0.0D) && (hasInventory())) {
            for (int a = 0; a < this.inventory.func_70302_i_(); a++)
            {
                ItemStack is = this.inventory.func_70301_a(a);
                ApplyResult applyResult = canApplyStack(is);
                if ((applyResult.type == ApplyType.BATTERY) && (applyResult.successful))
                {
                    is.field_77994_a -= 1;
                    is = applyItem(drone, is);
                    this.inventory.func_70299_a(a, is);
                }
            }
        }
        int mode = drone.getFlyingMode();
        if (mode > 0)
        {
            List<Integer> toRemove = new ArrayList();
            for (int a = 0; a < this.mods.size(); a++) {
                if (this.mods.get(a) == null)
                {
                    toRemove.add(Integer.valueOf(a));
                }
                else
                {
                    Module m = (Module)this.mods.get(a);
                    if (isEnabled(m)) {
                        m.updateModule(drone);
                    }
                }
            }
            for (Integer i : toRemove) {
                this.mods.remove(i.intValue());
            }
            double consumption = getMovementBatteryConsumption(drone);
            reduceBattery(consumption);
        }
    }

    public void updateDroneInfoToClient(EntityPlayer p)
    {
        if ((p instanceof EntityPlayerMP))
        {
            this.isChanged = false;
            PacketDispatcher.sendTo(new PacketDroneInfo(this), (EntityPlayerMP)p);
        }
        else
        {
            try
            {
                throw new Exception(p + " is not EntityPlayerMP. " + this + " not updated.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void updateDroneModulesToClient(EntityPlayer p)
    {
        if ((p instanceof EntityPlayerMP))
        {
            this.isChanged = false;
            PacketDispatcher.sendTo(new PacketDroneInfo(this), (EntityPlayerMP)p);
        }
        else
        {
            try
            {
                throw new Exception(p + " is not EntityPlayerMP. " + this + " not updated.");
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void setDisplayName(String n)
    {
        this.name = n;
        this.isChanged = true;
    }

    public String getDisplayName()
    {
        if (this.name.equals("#$Drone")) {
            return "#" + this.id;
        }
        return this.name;
    }

    public boolean hasInventory()
    {
        for (Module m : this.mods) {
            if ((m != null) && (m.canFunctionAs(Module.itemInventory))) {
                return true;
            }
        }
        return false;
    }

    public int getInvSize()
    {
        switch (this.casing)
        {
            case 2:
                return 18;
            case 3:
                return 27;
            case 4:
                return 36;
        }
        return 9;
    }

    public Object getItemStackObject(ItemStack is)
    {
        return (is.func_77973_b() instanceof ItemBlock) ? ((ItemBlock)is.func_77973_b()).func_179223_d() : is == null ? null : is.func_77973_b();
    }

    public static class ApplyResult
    {
        public DroneInfo.ApplyType type;
        public boolean successful;
        public int consume;
        public double effect;
        public String displayString;

        public ApplyResult(DroneInfo.ApplyType t, boolean b)
        {
            this(t, b, 1, 0.0D, "");
        }

        public ApplyResult(DroneInfo.ApplyType t, boolean b, String s)
        {
            this(t, b, 1, 0.0D, s);
        }

        public ApplyResult(DroneInfo.ApplyType t, boolean b, double d)
        {
            this(t, b, 1, d, "");
        }

        public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, double d)
        {
            this(t, b, i, d, "");
        }

        public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, String s)
        {
            this(t, b, i, 0.0D, s);
        }

        public ApplyResult(DroneInfo.ApplyType t, boolean b, int i, double d, String s)
        {
            this.type = t;
            this.successful = b;
            this.consume = i;
            this.effect = d;
            this.displayString = s;
        }
    }

    public static enum ApplyType
    {
        NONE,  DAMAGE,  BATTERY,  MODULE;

        private ApplyType() {}
    }

    public ApplyResult canApplyStack(ItemStack is)
    {
        if (is == null) {
            return new ApplyResult(ApplyType.NONE, false);
        }
        Object isO = getItemStackObject(is);
        if (isO == DronesMod.droneModule) {
            return canAddModule(ItemDroneModule.getModule(is));
        }
        if (batteryFuel.containsKey(isO)) {
            return recoverBatteryAnalyze(is);
        }
        if (damageRecover.containsKey(isO)) {
            return recoverDamageAnalyze(is);
        }
        return new ApplyResult(ApplyType.NONE, false);
    }

    public List<Integer> installedModulesToOverride(Module m)
    {
        List<Integer> l = new ArrayList();
        for (int a = 0; a < this.mods.size(); a++) {
            if (m.canReplace((Module)this.mods.get(a))) {
                l.add(Integer.valueOf(a));
            }
        }
        return l;
    }

    public ApplyResult canAddModule(Module m)
    {
        if ((m instanceof ModulePlaceHolder)) {
            return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Module empty");
        }
        if (this.mods.contains(m)) {
            return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + m.displayName + " already installed");
        }
        for (Module m1 : this.mods) {
            if (m1.canReplace(m)) {
                return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Better module (" + m1.displayName + ")" + " already installed");
            }
        }
        if (m.level > getMaxModLevel()) {
            return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "Module level too high");
        }
        if (this.mods.size() - installedModulesToOverride(m).size() + 1 <= getMaxModSlots()) {
            return new ApplyResult(ApplyType.MODULE, true, TextFormatting.GREEN + m.displayName + " installed");
        }
        return new ApplyResult(ApplyType.MODULE, false, TextFormatting.DARK_RED + "No more module slot");
    }

    public void applyModule(Module m)
    {
        if (m == null) {
            return;
        }
        List<Integer> toRemoveIndex = installedModulesToOverride(m);
        List toRemove = new ArrayList();
        for (Integer i : toRemoveIndex) {
            toRemove.add(this.mods.get(i.intValue()));
        }
        this.mods.removeAll(toRemove);
        this.mods.add(m);
        this.isChanged = true;
    }

    public ItemStack applyItem(EntityDrone e, ItemStack is)
    {
        if (batteryFuel.containsKey(getItemStackObject(is)))
        {
            ApplyResult recov = recoverBatteryAnalyze(is);
            if (recov.successful)
            {
                reduceBattery(-recov.effect);
                is.field_77994_a -= recov.consume;
                if (is.field_77994_a == 0) {
                    is = null;
                }
            }
        }
        if (damageRecover.containsKey(getItemStackObject(is)))
        {
            ApplyResult recov = recoverDamageAnalyze(is);
            if (recov.successful)
            {
                reduceDamage(e, -recov.effect);
                is.field_77994_a -= recov.consume;
                if (is.field_77994_a == 0) {
                    is = null;
                }
            }
        }
        return is;
    }

    public ApplyResult recoverBatteryAnalyze(ItemStack is)
    {
        double maxNeedRecover = getMaxBattery() - getBattery(false);
        double eachItemRecover = ((Double)batteryFuel.getOrDefault(getItemStackObject(is), Double.valueOf(0.0D))).doubleValue();
        double maxCanRecover = eachItemRecover * is.field_77994_a;
        double maxToRecover = Math.min(maxNeedRecover, maxCanRecover);
        int stackUse = eachItemRecover > 0.0D ? (int)Math.ceil(maxToRecover / eachItemRecover) : 0;
        return new ApplyResult(ApplyType.BATTERY, stackUse > 0, stackUse, maxToRecover, TextFormatting.GREEN + "Recovered " + (int)maxToRecover + " battery");
    }

    public ApplyResult recoverDamageAnalyze(ItemStack is)
    {
        double maxNeedRecover = getMaxDamage(null) - getDamage(false);
        double eachItemRecover = ((Double)damageRecover.getOrDefault(getItemStackObject(is), Double.valueOf(0.0D))).doubleValue();
        double maxCanRecover = eachItemRecover * is.field_77994_a;
        double maxToRecover = Math.min(maxNeedRecover, maxCanRecover);
        int stackUse = eachItemRecover > 0.0D ? (int)Math.ceil(maxToRecover / eachItemRecover) : 0;
        return new ApplyResult(ApplyType.DAMAGE, stackUse > 0, stackUse, maxToRecover, TextFormatting.GREEN + "Recovered " + (int)maxToRecover + " health");
    }

    public int getMaxModSlots()
    {
        switch (this.chip)
        {
            case 2:
                return 4;
            case 3:
                return 6;
            case 4:
                return 8;
        }
        return 3;
    }

    public int getMaxModLevel()
    {
        return this.chip;
    }

    public boolean hasEnabled(Module m)
    {
        return isEnabled(getModuleWithFunctionOf(m));
    }

    public boolean isEnabled(Module m)
    {
        return (m != null) && ((this.battery > 0.0D) || ((m instanceof ModuleRecharge))) && (!this.disabledMods.contains(m));
    }

    public boolean isEnabled(int modIndex)
    {
        return isEnabled((Module)this.mods.get(modIndex));
    }

    public void switchAllModule(EntityDrone drone, boolean enable)
    {
        if (((enable) && (this.disabledMods.isEmpty())) || ((!enable) && (this.disabledMods.size() == this.mods.size()))) {
            return;
        }
        for (int a = 0; a < this.mods.size(); a++) {
            switchModule(drone, (Module)this.mods.get(a), enable);
        }
    }

    public void switchModule(EntityDrone drone, Module m, boolean enable)
    {
        if (this.mods.contains(m)) {
            if (enable)
            {
                if (this.disabledMods.contains(m))
                {
                    this.disabledMods.remove(m);
                    m.onReenabled(drone);
                }
            }
            else if (!this.disabledMods.contains(m))
            {
                this.disabledMods.add(m);
                m.onDisabled(drone);
            }
        }
    }

    public void switchModule(EntityDrone drone, Integer index, boolean enable)
    {
        Module m = (Module)this.mods.get(index.intValue());
        switchModule(drone, m, enable);
    }

    public Module getModuleWithFunctionOf(Module mod)
    {
        for (int a = 0; a < this.mods.size(); a++)
        {
            Module m = (Module)this.mods.get(a);
            if ((m != null) && (m.canFunctionAs(mod))) {
                return m;
            }
        }
        return null;
    }

    public Module getFirstModuleWithEitherFunctionOf(Module... mods)
    {
        for (Module m : mods)
        {
            Module m0 = getModuleWithFunctionOf(m);
            if (m0 != null) {
                return m0;
            }
        }
        return null;
    }

    public double getBatteryConsumptionRate(EntityDrone e)
    {
        for (Module m : this.mods) {
            if (isEnabled(m)) {
                if ((m instanceof ModuleBatterySave)) {
                    return ((ModuleBatterySave)m).consumptionRate(e);
                }
            }
        }
        return 1.0D;
    }

    public double getMovementBatteryConsumption(EntityDrone e)
    {
        double d = 0.0D;
        if (e.getFlyingMode() > 0)
        {
            d += getFlyingBatteryConsumption(e, e.idle);
            for (Module m : this.mods) {
                if (isEnabled(m)) {
                    d += m.costBatRawPerSec(e) / 20.0D * getBatteryConsumptionRate(e);
                }
            }
        }
        return d;
    }

    public double getFlyingBatteryConsumption(EntityDrone e, boolean idle)
    {
        double d = this.chip * 0.5D + this.engine * this.engineLevel + (this.core + this.casing) * 0.25D;
        for (int a = 0; a < this.inventory.func_70302_i_(); a++)
        {
            ItemStack is = this.inventory.func_70301_a(a);
            if (is != null) {
                d += 0.2D;
            }
        }
        if (idle) {
            d /= 60.0D;
        }
        d = d * getBatteryConsumptionRate(e) / 20.0D;
        return d;
    }

    public double getMaxBattery()
    {
        switch (this.core)
        {
            case 2:
                return 1200.0D;
            case 3:
                return 5200.0D;
            case 4:
                return 13200.0D;
        }
        return 400.0D;
    }

    public double getBattery(boolean forDisplay)
    {
        return forDisplay ? Math.round(this.battery * 100.0D) / 100.0D : this.battery;
    }

    public void setBattery(double battery)
    {
        this.battery = Math.max(Math.min(battery, getMaxBattery()), 0.0D);
        this.isChanged = true;
    }

    public void reduceBattery(double cons)
    {
        setBattery(this.battery - cons);
    }

    public int getEstimatedFlyTimeTick(EntityDrone e)
    {
        if (this.battery == 0.0D) {
            return 0;
        }
        double consumedBattery = this.prevBattery - this.battery;
        if (e != null) {
            consumedBattery = getMovementBatteryConsumption(e);
        }
        if (consumedBattery <= 0.0D) {
            return -1;
        }
        int tickTotal = (int)Math.round(getBattery(false) / consumedBattery);
        return tickTotal;
    }

    public Integer[] getEstimatedFlyTime(EntityDrone e)
    {
        if (this.battery == 0.0D) {
            return new Integer[] { Integer.valueOf(0) };
        }
        List<Integer> times = new ArrayList();
        double consumedBattery = this.prevBattery - this.battery;
        if (e != null) {
            consumedBattery = getMovementBatteryConsumption(e);
        }
        if (consumedBattery <= 0.0D) {
            return new Integer[] { Integer.valueOf(-1) };
        }
        int secTotal = (int)Math.round(getBattery(false) / 20.0D / consumedBattery);
        int sec = secTotal % 60;
        secTotal -= sec;
        times.add(Integer.valueOf(sec));
        if (secTotal > 0)
        {
            int min = secTotal / 60 % 60;
            secTotal -= min * 60;
            times.add(Integer.valueOf(min));
        }
        if (secTotal > 0)
        {
            int hour = secTotal / 3600 % 24;
            secTotal -= hour * 3600;
            times.add(Integer.valueOf(hour));
        }
        if (secTotal > 0)
        {
            int day = secTotal / 86400 % 7;
            secTotal -= day * 86400;
            times.add(Integer.valueOf(day));
        }
        if (secTotal > 0)
        {
            int month = secTotal / 604800;
            times.add(Integer.valueOf(month));
        }
        if (times.isEmpty()) {
            times.add(Integer.valueOf(0));
        }
        Integer[] timesArray = new Integer[times.size()];
        times.toArray(timesArray);
        return timesArray;
    }

    public String getEstimatedFlyTimeString(EntityDrone e)
    {
        String s = "";
        Integer[] times = getEstimatedFlyTime(e);
        if ((times.length == 1) && (times[0].intValue() == -1)) {
            return "infinity";
        }
        for (int a = times.length - 1; a >= 0; a--)
        {
            if (a != times.length - 1) {
                s = s + " ";
            }
            s = s + times[a];
            switch (a)
            {
                case 1:
                    s = s + "m";
                    break;
                case 2:
                    s = s + "h";
                    break;
                case 3:
                    s = s + "d";
                    break;
                case 4:
                    s = s + "w";
                    break;
                default:
                    s = s + "s";
            }
        }
        return s;
    }

    public double getAttackPower(EntityDrone e)
    {
        double atk = this.core + e.getBaseAttack();
        for (Module m : this.mods) {
            if (((m instanceof ModuleWeapon)) && (isEnabled(m))) {
                atk += ((ModuleWeapon)m).getAttackPower(e);
            }
        }
        return atk;
    }

    public double getMaxDamage(EntityDrone e)
    {
        double i = e == null ? 0.0D : e.getBaseHealth();
        switch (this.casing)
        {
            case 2:
                i += 20.0D;
                break;
            case 3:
                i += 40.0D;
                break;
            case 4:
                i += 60.0D;
                break;
            default:
                i += 10.0D;
        }
        return i;
    }

    public double getDamage(boolean forDisplay)
    {
        return forDisplay ? Math.round(this.damage * 100.0D) / 100.0D : this.damage;
    }

    public void setDamage(EntityDrone e, double damage)
    {
        this.damage = Math.max(Math.min(damage, getMaxDamage(e)), 0.0D);
        this.isChanged = true;
        if (e != null) {
            e.func_70606_j((float)damage);
        }
    }

    public void reduceDamage(EntityDrone e, double dam)
    {
        setDamage(e, this.damage - dam);
    }

    public double getDamageReduction(EntityDrone e)
    {
        double rate = 0.0D;
        for (Module m : this.mods) {
            if ((m instanceof ModuleArmor))
            {
                rate = Math.max(((ModuleArmor)m).getDamageReduction(e), rate);
                break;
            }
        }
        return rate;
    }

    public void damageDrone(EntityDrone e, double dam)
    {
        reduceDamage(e, dam * (1.0D - getDamageReduction(e)));
        if (getDamage(false) == 0.0D) {
            setBattery(0.0D);
        }
    }

    public double getEngineLevel()
    {
        if ((getDamage(false) == 0.0D) || (getBattery(false) == 0.0D)) {
            return 0.0D;
        }
        return this.engineLevel;
    }

    public void setEngineLevel(double d)
    {
        this.engineLevel = d;
        this.isChanged = true;
    }

    public double getMaxSpeed()
    {
        switch (this.engine)
        {
            case 2:
                return 10.0D;
            case 3:
                return 20.0D;
            case 4:
                return 30.0D;
        }
        return 5.0D;
    }

    public void writeToNBT(NBTTagCompound tag)
    {
        NBTTagCompound info = new NBTTagCompound();

        info.func_74778_a("Name", this.name);
        info.func_74768_a("ID", this.id);
        info.func_74768_a("Chip", this.chip);
        info.func_74768_a("Core", this.core);
        info.func_74768_a("Casing", this.casing);
        info.func_74768_a("Engine", this.engine);
        info.func_74780_a("Battery", this.battery);
        info.func_74780_a("PrevBattery", this.prevBattery);
        info.func_74780_a("Damage", this.damage);
        info.func_74780_a("EngineLevel", this.engineLevel);
        info.func_74768_a("Controller Frequency", this.droneFreq);

        int count = 0;
        for (int a = 0; a < this.mods.size(); a++)
        {
            Module m = (Module)this.mods.get(a);
            if (m != null)
            {
                count++;
                this.modsNBT.func_74778_a(String.valueOf(a), m.getID());
            }
        }
        this.modsNBT.func_74768_a("Count", count);
        info.func_74782_a("Modules", this.modsNBT);

        NBTTagCompound disabledModTag = new NBTTagCompound();
        count = 0;
        for (int a = 0; a < this.disabledMods.size(); a++)
        {
            Module m = (Module)this.disabledMods.get(a);
            if (m != null)
            {
                count++;
                disabledModTag.func_74778_a(String.valueOf(a), m.getID());
            }
        }
        disabledModTag.func_74768_a("Count", count);
        info.func_74782_a("Disabled Modules", disabledModTag);

        NBTTagCompound invTag = new NBTTagCompound();
        for (int a = 0; a < 36; a++)
        {
            ItemStack is = this.inventory.func_70301_a(a);
            if (is != null)
            {
                NBTTagCompound istag = new NBTTagCompound();
                is.func_77955_b(istag);
                invTag.func_74782_a("IS " + a, istag);
            }
        }
        info.func_74782_a("Inv", invTag);

        NBTTagCompound appearanceTag = new NBTTagCompound();
        this.appearance.writeToNBT(appearanceTag);
        info.func_74782_a("Appearance", appearanceTag);

        tag.func_74782_a("Drone Info", info);
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        this.mods.clear();
        NBTTagCompound info = tag.func_74775_l("Drone Info");

        this.name = (info.func_74764_b("Name") ? info.func_74779_i("Name") : "#$Drone");
        this.id = (info.func_74764_b("ID") ? info.func_74762_e("ID") : 0);
        nextID = Math.max(nextID, this.id + 1);
        this.chip = (info.func_74764_b("Chip") ? info.func_74762_e("Chip") : 1);
        this.core = (info.func_74764_b("Core") ? info.func_74762_e("Core") : 1);
        this.casing = (info.func_74764_b("Casing") ? info.func_74762_e("Casing") : 1);
        this.engine = (info.func_74764_b("Engine") ? info.func_74762_e("Engine") : 1);
        setBattery(info.func_74764_b("Battery") ? info.func_74769_h("Battery") : getMaxBattery());
        this.prevBattery = (info.func_74764_b("PrevBattery") ? info.func_74769_h("PrevBattery") : this.battery);
        setDamage(null, info.func_74764_b("Damage") ? info.func_74769_h("Damage") : getMaxDamage(null));
        this.engineLevel = (info.func_74764_b("EngineLevel") ? info.func_74769_h("EngineLevel") : 1.0D);
        this.droneFreq = (info.func_74764_b("Controller Frequency") ? info.func_74762_e("Controller Frequency") : -1);

        readModulesNBT(info.func_74775_l("Modules"));

        NBTTagCompound disabledModTag = info.func_74775_l("Disabled Modules");
        int disModCount = disabledModTag.func_74762_e("Count");
        for (int a = 0; a < disModCount; a++) {
            this.disabledMods.add(Module.getModuleByID(disabledModTag.func_74779_i(String.valueOf(a))));
        }
        NBTTagCompound invTag = info.func_74775_l("Inv");
        for (int a = 0; a < 36; a++) {
            if (invTag.func_74764_b("IS " + a))
            {
                NBTTagCompound istag = invTag.func_74775_l("IS " + a);
                this.inventory.func_70299_a(a, ItemStack.func_77949_a(istag));
            }
            else
            {
                this.inventory.func_70299_a(a, null);
            }
        }
        this.appearance.readFromNBT(info.func_74775_l("Appearance"));
    }

    public void readModulesNBT(NBTTagCompound tag)
    {
        this.modsNBT = tag;
        int modCount = this.modsNBT.func_74762_e("Count");
        for (int a = 0; a < modCount; a++) {
            this.mods.add(Module.getModuleByID(this.modsNBT.func_74779_i(String.valueOf(a))));
        }
    }

    public DroneInfo copy()
    {
        NBTTagCompound tag = new NBTTagCompound();
        writeToNBT(tag);
        return fromNBT(tag);
    }

    public static DroneInfo fromNBT(NBTTagCompound tag)
    {
        DroneInfo di = new DroneInfo();
        di.readFromNBT(tag);
        return di;
    }
}
