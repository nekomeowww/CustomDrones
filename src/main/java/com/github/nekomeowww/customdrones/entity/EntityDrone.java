package com.github.nekomeowww.customdrones.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.path.Node;
import com.github.nekomeowww.customdrones.api.path.Path;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraMode;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSetCameraPitch;

public class EntityDrone
        extends EntityFlying
        implements IEntityAdditionalSpawnData
{
    public static int DroneNextID = 0;
    private static final DataParameter<Boolean> DICHANGED = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187198_h);
    private static final DataParameter<Integer> FLYMODE = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187192_b);
    private static final DataParameter<Boolean> CAMERA = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187198_h);
    private static final DataParameter<Float> CAMERAPITCH = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187193_c);
    private static final DataParameter<String> CONTROLPLAYER = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187194_d);
    public DroneInfo droneInfo;
    public double wingRotate = 0.0D;
    public float acceX;
    public float acceY;
    public float acceZ;
    public double prevMotionX;
    public double prevMotionY;
    public double prevMotionZ;
    public boolean idle = true;
    public Path automatedPath;
    public Path recordingPath;
    private EntityPlayer controllingPlayer;
    public Entity entityToAttack;
    public int attackDelay = 0;
    public int pickupDelay = 0;
    public int hitDelay = 0;

    public EntityDrone(World worldIn)
    {
        super(worldIn);
        func_70105_a(0.8F, 0.3F);
        this.droneInfo = new DroneInfo(this);
        applyDroneAttributes();
    }

    public ITextComponent func_145748_c_()
    {
        return new TextComponentString(this.droneInfo.getDisplayName());
    }

    public int getDroneID()
    {
        return this.droneInfo == null ? -1 : this.droneInfo.id;
    }

    protected boolean func_70692_ba()
    {
        return false;
    }

    protected void func_70088_a()
    {
        super.func_70088_a();
        this.field_70180_af.func_187214_a(DICHANGED, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(FLYMODE, Integer.valueOf(0));
        this.field_70180_af.func_187214_a(CAMERA, Boolean.valueOf(false));
        this.field_70180_af.func_187214_a(CAMERAPITCH, Float.valueOf(0.0F));
        this.field_70180_af.func_187214_a(CONTROLPLAYER, "");
    }

    protected void applyDroneAttributes()
    {
        func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(this.droneInfo.getMaxDamage(this));
    }

    public Vec3d func_70040_Z()
    {
        return func_70676_i(1.0F);
    }

    public Vec3d getHorizontalLookVec()
    {
        return func_174806_f(0.0F, this.field_70177_z);
    }

    public float func_70047_e()
    {
        return 0.15F;
    }

    public boolean func_70067_L()
    {
        return true;
    }

    public boolean func_70104_M()
    {
        return true;
    }

    protected boolean func_70041_e_()
    {
        return true;
    }

    public boolean canRiderInteract()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int func_70070_b(float partialTicks)
    {
        return super.func_70070_b(partialTicks);
    }

    public float func_70013_c(float partialTicks)
    {
        return super.func_70013_c(partialTicks);
    }

    public void func_180430_e(float distance, float damageMultiplier) {}

    public EnumActionResult func_184199_a(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
    {
        ItemStack stack = player.func_184614_ca();
        boolean hasController = playerHasCorrespondingController(player, false);
        if (usefulInteraction(player, vec, stack, hand, hasController))
        {
            if ((stack == null) || (player.func_70093_af()))
            {
                if (hasController)
                {
                    player.openGui(DronesMod.instance, 1, this.field_70170_p, getDroneID(), 0, 0);
                    return EnumActionResult.SUCCESS;
                }
                EntityHelper.addChat(player, 1, TextFormatting.RED + "No corresponding controller");
                return EnumActionResult.FAIL;
            }
            if (stack != null)
            {
                boolean itemHasUse = this.droneInfo.canApplyStack(stack).type != DroneInfo.ApplyType.NONE;
                if (itemHasUse)
                {
                    if (hasController)
                    {
                        if (stack.func_77973_b() == DronesMod.droneModule)
                        {
                            Module mod = ItemDroneModule.getModule(stack);
                            DroneInfo.ApplyResult addEntry = this.droneInfo.canAddModule(mod);
                            if (addEntry.successful) {
                                if (!this.field_70170_p.field_72995_K)
                                {
                                    this.droneInfo.applyModule(mod);
                                    this.droneInfo.updateDroneInfoToClient(player);
                                    player.func_184611_a(EnumHand.MAIN_HAND, null);
                                }
                            }
                            EntityHelper.addChat(player, 1, addEntry.displayString);
                        }
                        else
                        {
                            stack = this.droneInfo.applyItem(this, stack);
                            player.func_184611_a(EnumHand.MAIN_HAND, stack);
                        }
                        return EnumActionResult.SUCCESS;
                    }
                    EntityHelper.addChat(player, 1, TextFormatting.RED + "No corresponding controller");
                    return EnumActionResult.FAIL;
                }
                if ((stack.func_77973_b() instanceof IItemInteractDrone)) {
                    return ((IItemInteractDrone)stack.func_77973_b()).interactWithDrone(this.field_70170_p, this, player, vec, stack, hand);
                }
            }
        }
        return super.func_184199_a(player, vec, is, hand);
    }

    public boolean usefulInteraction(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand, boolean hasController)
    {
        return true;
    }

    public boolean playerHasCorrespondingController(EntityPlayer player, boolean mustInHand)
    {
        if (this.droneInfo.droneFreq == -1) {
            return true;
        }
        if (mustInHand) {
            return DronesMod.droneFlyer.getControllerFreq(player.func_184614_ca()) == this.droneInfo.droneFreq;
        }
        InventoryPlayer inv = player.field_71071_by;
        for (int a = 0; a < inv.func_70302_i_(); a++) {
            if (DronesMod.droneFlyer.getControllerFreq(inv.func_70301_a(a)) == this.droneInfo.droneFreq) {
                return true;
            }
        }
        return false;
    }

    public Entity getRider()
    {
        return func_184179_bs();
    }

    public double func_70042_X()
    {
        if (getRider() != null) {
            return 0.0D - getRiderHeight();
        }
        return 0.25D;
    }

    public double getRiderHeight()
    {
        double h = 0.0D;
        if (getRider() == null) {
            h = 0.0D;
        } else if (((getRider() instanceof EntityPlayer)) || ((getRider() instanceof EntityZombie)) ||
                ((getRider() instanceof EntityPigZombie)) || ((getRider() instanceof EntitySkeleton))) {
            h = getRider().field_70131_O * 0.88D;
        } else {
            h = getRider().field_70131_O * 1.15D;
        }
        return h;
    }

    public void dropRider()
    {
        if (getRider() != null) {
            getRider().func_184210_p();
        }
    }

    public boolean hasEnabled(Module m)
    {
        return this.droneInfo.hasEnabled(m);
    }

    public int getFlyingMode()
    {
        return ((Integer)func_184212_Q().func_187225_a(FLYMODE)).intValue();
    }

    public String getFlyingModeString()
    {
        int droneModeI = getFlyingMode();
        if (droneModeI == 0) {
            return "off";
        }
        if (droneModeI == 1) {
            return "hovering";
        }
        if (droneModeI == 2) {
            return "manual control";
        }
        if (droneModeI == 3) {
            return "preset path";
        }
        if (droneModeI == 4) {
            return "follow controller";
        }
        return "unknown";
    }

    public void setFlyingMode(int i)
    {
        func_184212_Q().func_187227_b(FLYMODE, Integer.valueOf(i));
    }

    public void setNextFlyingMode()
    {
        int mode = getFlyingMode() + 1;
        if (mode == 5) {
            mode = 0;
        }
        setFlyingMode(mode);
        if (!canDroneHaveFlyMode(mode)) {
            setNextFlyingMode();
        }
    }

    public boolean canDroneHaveFlyMode(int mode)
    {
        if ((mode == 0) || (mode == 1)) {
            return true;
        }
        if ((mode == 2) && (hasEnabled(Module.controlMovement))) {
            return true;
        }
        if ((mode == 3) && (hasEnabled(Module.pathMovement))) {
            return true;
        }
        if ((mode == 4) && (hasEnabled(Module.followMovement))) {
            return true;
        }
        return false;
    }

    public boolean getWatchedDIChanged()
    {
        return ((Boolean)this.field_70180_af.func_187225_a(DICHANGED)).booleanValue();
    }

    public void setWatchedDIChanged(boolean b)
    {
        func_184212_Q().func_187227_b(DICHANGED, Boolean.valueOf(b));
    }

    public void setCameraMode(boolean b)
    {
        if (this.field_70170_p.field_72995_K) {
            PacketDispatcher.sendToServer(new PacketDroneSetCameraMode(this, b));
        } else {
            func_184212_Q().func_187227_b(CAMERA, Boolean.valueOf(b));
        }
    }

    public boolean getCameraMode()
    {
        return ((Boolean)func_184212_Q().func_187225_a(CAMERA)).booleanValue();
    }

    public void setCameraPitch(float f)
    {
        if (this.field_70170_p.field_72995_K) {
            PacketDispatcher.sendToServer(new PacketDroneSetCameraPitch(this, f));
        }
        func_184212_Q().func_187227_b(CAMERAPITCH, Float.valueOf(f));
    }

    public float getCameraPitch()
    {
        return ((Float)func_184212_Q().func_187225_a(CAMERAPITCH)).floatValue();
    }

    public void setControllingPlayer(EntityPlayer p)
    {
        this.controllingPlayer = p;
        func_184212_Q().func_187227_b(CONTROLPLAYER, p == null ? "" : p.func_70005_c_());
    }

    public EntityPlayer getControllingPlayer()
    {
        String s = (String)func_184212_Q().func_187225_a(CONTROLPLAYER);
        if ((s.length() == 0) || (s.equals(""))) {
            this.controllingPlayer = null;
        } else if ((this.controllingPlayer == null) || (!this.controllingPlayer.func_70005_c_().equals(s))) {
            this.controllingPlayer = this.field_70170_p.func_72924_a(s);
        }
        return this.controllingPlayer;
    }

    public void func_70030_z()
    {
        super.func_70030_z();
        if (this.hitDelay > 0) {
            this.hitDelay -= 1;
        }
        if (this.attackDelay > 0) {
            this.attackDelay -= 1;
        }
        if ((this.pickupDelay > 0) && (getRider() == null)) {
            this.pickupDelay -= 1;
        }
        if (!this.field_70170_p.field_72995_K) {
            setWatchedDIChanged(this.droneInfo.isChanged);
        }
        if ((getDroneAttackTarget() == null) || (getDroneAttackTarget().field_70128_L)) {
            setDroneAttackTarget(null, true);
        }
        updateFlyingBehavior();
        updateRotation();
        updateWingRotation();
        this.droneInfo.updateDroneInfo(this);
        double speedControl = 1.0D - 0.087D * Math.pow(this.droneInfo.getMaxSpeed(), 0.1D);
        this.field_70159_w *= speedControl;
        this.field_70181_x *= speedControl;
        this.field_70179_y *= speedControl;
        func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
        func_85033_bc();
        func_145771_j(this.field_70165_t, this.field_70163_u, this.field_70161_v);
    }

    public void updateRotation()
    {
        double maxYawChange = 6.0D;
        double nextYaw = (float)(Math.atan2(-this.field_70159_w, this.field_70179_y) / 3.141592653589793D * 180.0D);
        if ((Math.abs(this.field_70159_w) <= 0.001D) && (Math.abs(this.field_70179_y) <= 0.001D)) {
            nextYaw = this.field_70177_z;
        }
        double changingYaw = nextYaw - this.field_70177_z;
        changingYaw %= 360.0D;
        if (360.0D - Math.abs(changingYaw) < Math.abs(changingYaw)) {
            changingYaw *= -1.0D;
        }
        if (changingYaw > 0.0D) {
            changingYaw = Math.min(maxYawChange, changingYaw);
        } else if (changingYaw < 0.0D) {
            changingYaw = -Math.min(maxYawChange, -changingYaw);
        }
        this.field_70177_z = ((float)(this.field_70177_z + changingYaw));
        this.field_70177_z %= 360.0F;
        if ((getCameraMode()) && (getControllingPlayer() != null)) {
            if (this.field_70170_p.field_72995_K) {
                setCameraPitch(getControllingPlayer().field_70125_A);
            }
        }
        double nextPitch = getCameraPitch();
        double maxPitchChange = 3.0D;
        double changingPitch = nextPitch - this.field_70125_A;
        if (changingPitch > 0.0D) {
            changingPitch = Math.min(maxPitchChange, changingPitch);
        } else if (changingPitch < 0.0D) {
            changingPitch = -Math.min(maxPitchChange, -changingPitch);
        }
        this.field_70125_A = ((float)(this.field_70125_A + changingPitch));
    }

    public void updateFlyingBehavior()
    {
        int mode = getFlyingMode();
        boolean overridenByMod = false;
        Module overridenMod = null;
        for (int a = 0; a < this.droneInfo.mods.size(); a++)
        {
            Module m = (Module)this.droneInfo.mods.get(a);
            if ((hasEnabled(m)) && (m.canOverrideDroneMovement(this)))
            {
                overridenByMod = true;
                overridenMod = (overridenMod == null) || (overridenMod.overridePriority() < m.overridePriority()) ? m : overridenMod;
            }
        }
        if (this.droneInfo.getBattery(false) <= 0.0D)
        {
            this.idle = true;
            this.field_70181_x -= 0.08D;
            if (getRider() != null) {
                getRider().func_184210_p();
            }
        }
        else
        {
            this.idle = false;
            if (mode == 0)
            {
                this.idle = true;
                this.field_70181_x -= 0.08D;
                if (getRider() != null) {
                    getRider().func_184210_p();
                }
            }
            else if (mode == 1)
            {
                this.field_70181_x -= 0.08D;
                double belowY = getBelowSurfaceY();
                double distY = this.field_70163_u - belowY;
                double maxDist = 1.5D + getRiderHeight();
                if (distY < maxDist) {
                    this.field_70181_x += 0.08D * Math.pow((maxDist - distY) * 2.0D, 0.5D) * this.droneInfo.getEngineLevel();
                }
                this.idle = true;
            }
            else
            {
                double belowY = getBelowSurfaceY();
                double distY = this.field_70163_u - belowY;
                if (distY > getRiderHeight())
                {
                    Entity rider = getRider();
                    double riderWeight = rider != null ? 0.5D + rider.field_70130_N * rider.field_70131_O : 0.5D;
                    double pullUpPower = this.droneInfo.getEngineLevel() * Math.sqrt(this.droneInfo.getMaxSpeed());
                    if (pullUpPower < riderWeight) {
                        this.field_70181_x -= 0.08D * (riderWeight - pullUpPower) / riderWeight;
                    }
                }
                if (overridenByMod)
                {
                    this.idle = false;
                    overridenMod.overrideDroneMovement(this);
                }
            }
        }
    }

    public void flyTo(Vec3d pos, double minMove, double rate)
    {
        flyNormalAlong(VecHelper.fromTo(func_174791_d(), pos), minMove, rate);
    }

    public void flyNormalAlong(Vec3d vec, double minMove, double rate)
    {
        if ((vec != null) && (vec.func_72433_c() >= minMove))
        {
            Vec3d vec1 = vec.func_72433_c() > 1.0D ? vec.func_72432_b() : vec.func_72441_c(0.0D, 0.0D, 0.0D);
            flyNormalAlong(vec1.field_72450_a, vec1.field_72448_b, vec1.field_72449_c, rate);
        }
    }

    public void flyNormalAlong(double x, double y, double z, double rate)
    {
        double speedMult = getSpeedMultiplication() * rate;
        this.field_70159_w += x * speedMult;
        this.field_70181_x += y * speedMult;
        this.field_70179_y += z * speedMult;
    }

    public EntityPlayer getNearestPlayer(double distance, int hasController)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;
        for (int i = 0; i < this.field_70170_p.field_73010_i.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)this.field_70170_p.field_73010_i.get(i);
            if (EntitySelectors.field_180132_d.apply(entityplayer1)) {
                if (hasController != 0)
                {
                    if (!playerHasCorrespondingController(entityplayer1, hasController == 2)) {}
                }
                else
                {
                    double d1 = entityplayer1.func_70092_e(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                    if (((distance < 0.0D) || (d1 < distance * distance)) && ((d0 == -1.0D) || (d1 < d0)))
                    {
                        d0 = d1;
                        entityplayer = entityplayer1;
                    }
                }
            }
        }
        return entityplayer;
    }

    public boolean isControllerFlying()
    {
        int mode = getFlyingMode();
        return (mode == 2) || ((mode == 3) && (this.recordingPath != null));
    }

    public double getSpeedMultiplication()
    {
        return 0.005D * this.droneInfo.getMaxSpeed() * this.droneInfo.getEngineLevel();
    }

    public void applyRecordPath(boolean loop)
    {
        this.automatedPath = this.recordingPath.simplifiedPath();
        this.recordingPath = null;
        if (!loop)
        {
            List<Node> nodes = new ArrayList();
            for (int a = this.automatedPath.nodes.size() - 1; a > 0; a--) {
                nodes.add(this.automatedPath.nodes.get(a));
            }
            this.automatedPath.pathIndex = (this.automatedPath.nodes.size() - 1);
            this.automatedPath.nodes.addAll(nodes);
        }
    }

    public double getBelowSurfaceY()
    {
        double yMax = -1.0D;
        for (int x = (int)Math.floor(func_174813_aQ().field_72340_a); x <= func_174813_aQ().field_72336_d; x++) {
            for (int z = (int)Math.floor(func_174813_aQ().field_72339_c); z <= func_174813_aQ().field_72334_f; z++) {
                for (int y = (int)Math.floor(this.field_70163_u); y > 0; y--)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState bs = this.field_70170_p.func_180495_p(pos);
                    if ((!bs.func_185904_a().func_76222_j()) || (bs.func_185904_a() != Material.field_151579_a))
                    {
                        double posMax = pos.func_177956_o();
                        if (this.field_70170_p.func_180495_p(pos).func_185890_d(this.field_70170_p, pos) != null) {
                            posMax += this.field_70170_p.func_180495_p(pos).func_185890_d(this.field_70170_p, pos).field_72337_e;
                        }
                        yMax = Math.max(yMax, posMax);
                        break;
                    }
                }
            }
        }
        return yMax;
    }

    public double wingRotationIncrement()
    {
        int mode = getFlyingMode();
        double rate = Math.pow(this.droneInfo.getEngineLevel(), 0.3D);
        if (mode != 0)
        {
            double engine = 100.0D * rate;
            return engine;
        }
        double add = (this.field_70159_w * this.field_70159_w + (this.field_70167_r - this.field_70163_u) * (this.field_70167_r - this.field_70163_u) + this.field_70179_y * this.field_70179_y) * 1000.0D;
        return add;
    }

    public void updateWingRotation()
    {
        double increment = wingRotationIncrement();
        this.wingRotate += increment;
    }

    public double getWingRotation(float partialTick)
    {
        double increment = wingRotationIncrement();
        return this.wingRotate + increment * partialTick;
    }

    public void func_70108_f(Entity e)
    {
        boolean modStopFurtherCollision = false;
        for (Iterator localIterator = this.droneInfo.mods.iterator(); localIterator.hasNext(); modStopFurtherCollision = true)
        {
            Module m = (Module)localIterator.next();
            if ((m == null) || (!m.collideWithEntity(this, e))) {}
        }
        if (modStopFurtherCollision) {
            return;
        }
        if ((!(e instanceof EntityDrone)) || (shouldPushDrone((EntityDrone)e))) {
            super.func_70108_f(e);
        }
        if (getFlyingMode() != 0) {
            if ((getRider() == null) && (canPickUpEntity(e)))
            {
                if (this.pickupDelay == 0)
                {
                    e.func_184220_m(this);
                    func_70107_b(this.field_70165_t, e.field_70163_u + getRiderHeight(), this.field_70161_v);
                    this.pickupDelay = 20;
                }
            }
            else
            {
                if (!this.droneInfo.hasEnabled(Module.weapon1)) {
                    return;
                }
                if (e == getRider()) {
                    return;
                }
                boolean causeDam = true;
                if (((e instanceof EntityDrone)) && (!shouldAttackDrone((EntityDrone)e)))
                {
                    causeDam = false;
                }
                else if ((e instanceof EntityPlayer))
                {
                    EntityPlayer p = (EntityPlayer)e;
                    for (int a = 0; a < p.field_71071_by.func_70302_i_(); a++)
                    {
                        ItemStack is = p.field_71071_by.func_70301_a(a);
                        if ((is != null) && (is.func_77973_b() == DronesMod.droneFlyer) &&
                                (DronesMod.droneFlyer.getControllerFreq(is) == this.droneInfo.droneFreq))
                        {
                            causeDam = false;
                            break;
                        }
                    }
                }
                if (causeDam)
                {
                    double spdSqr = this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y;
                    double spdDmgRate = Math.sqrt(spdSqr) * 20.0D / this.droneInfo.getMaxSpeed();
                    double baseDmg = this.droneInfo.getAttackPower(this);
                    double damage = baseDmg * 0.8D * spdDmgRate + 0.2D * baseDmg;
                    attackEntity(e, getAttackDamageSource(), (float)damage);
                }
            }
        }
    }

    protected void func_85033_bc()
    {
        List<Entity> list = this.field_70170_p.func_72839_b(this,
                func_174813_aQ().func_186662_g(func_70111_Y()));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
                func_70108_f(entity);
            }
        }
    }

    protected void func_145775_I()
    {
        AxisAlignedBB axisalignedbb = func_174813_aQ().func_72317_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);

        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72340_a + 0.001D, axisalignedbb.field_72338_b + 0.001D, axisalignedbb.field_72339_c + 0.001D);

        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72336_d - 0.001D, axisalignedbb.field_72337_e - 0.001D, axisalignedbb.field_72334_f - 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.func_185346_s();
        if (this.field_70170_p.func_175707_a(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
            for (int i = blockpos$pooledmutableblockpos.func_177958_n(); i <= blockpos$pooledmutableblockpos1.func_177958_n(); i++) {
                for (int j = blockpos$pooledmutableblockpos.func_177956_o(); j <= blockpos$pooledmutableblockpos1.func_177956_o(); j++) {
                    for (int k = blockpos$pooledmutableblockpos.func_177952_p(); k <= blockpos$pooledmutableblockpos1.func_177952_p(); k++)
                    {
                        blockpos$pooledmutableblockpos2.func_181079_c(i, j, k);
                        IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos$pooledmutableblockpos2);
                        try
                        {
                            collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.func_85055_a(throwable, "Colliding entity with block");

                            CrashReportCategory crashreportcategory = crashreport.func_85058_a("Block being collided with");
                            CrashReportCategory.func_175750_a(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);

                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.func_185344_t();
        blockpos$pooledmutableblockpos1.func_185344_t();
        blockpos$pooledmutableblockpos2.func_185344_t();
        super.func_145775_I();
    }

    public void collideWithBlock(BlockPos pos, IBlockState state)
    {
        boolean modStopFurtherCollision = false;
        for (Iterator localIterator = this.droneInfo.mods.iterator(); localIterator.hasNext(); modStopFurtherCollision = true)
        {
            Module m = (Module)localIterator.next();
            if ((m == null) || (!m.collideWithBlock(this, pos, state))) {}
        }
        if (modStopFurtherCollision) {}
    }

    public double getBaseAttack()
    {
        return 1.0D;
    }

    public double getBaseHealth()
    {
        return 0.0D;
    }

    public DamageSource getAttackDamageSource()
    {
        return this.controllingPlayer == null ? DamageSource.func_92087_a(this) : DamageSource.func_188403_a(this, this.controllingPlayer);
    }

    public boolean shouldPushDrone(EntityDrone e)
    {
        return e.getClass() != getClass();
    }

    public boolean shouldAttackDrone(EntityDrone e)
    {
        return e.getClass() != getClass();
    }

    public boolean canPickUpEntity(Entity e)
    {
        if ((e instanceof EntityDrone)) {
            return false;
        }
        if (((e instanceof EntityPlayer)) && (canPickUpPlayers())) {
            return true;
        }
        if ((canPickUpNonPlayers()) && (!(e instanceof EntityItem)) && (!(e instanceof EntityXPOrb)) && (!(e instanceof IProjectile)) && (e.field_70130_N * e.field_70131_O < 4 + this.droneInfo.engine)) {
            return true;
        }
        return false;
    }

    public boolean canPickUpPlayers()
    {
        return hasEnabled(Module.playerTransport);
    }

    public boolean canPickUpNonPlayers()
    {
        return hasEnabled(Module.nplayerTransport);
    }

    public Entity func_184179_bs()
    {
        return func_184188_bt().isEmpty() ? null : (Entity)func_184188_bt().get(0);
    }

    public void setDroneAttackTarget(Entity e, boolean forcefully)
    {
        Entity oldTarget = getDroneAttackTarget();
        if ((forcefully) || (oldTarget == null) || (oldTarget.field_70128_L)) {
            this.entityToAttack = e;
        } else if ((func_70068_e(e) < func_70068_e(oldTarget)) || (!func_70685_l(oldTarget))) {
            this.entityToAttack = e;
        }
    }

    public boolean func_70685_l(Entity e)
    {
        return this.field_70170_p.func_147447_a(new Vec3d(this.field_70165_t, this.field_70163_u + func_70047_e(), this.field_70161_v), new Vec3d(e.field_70165_t, e.field_70163_u + e
                .func_70047_e(), e.field_70161_v), false, true, false) == null;
    }

    public boolean canPosBeSeen(Vec3d pos)
    {
        return this.field_70170_p.func_147447_a(new Vec3d(this.field_70165_t, this.field_70163_u + func_70047_e(), this.field_70161_v), pos, false, true, false) == null;
    }

    public Entity getDroneAttackTarget()
    {
        return this.entityToAttack;
    }

    public void attackEntity(Entity e, DamageSource dmg, float dam)
    {
        if (this.attackDelay == 0)
        {
            e.func_70097_a(dmg, dam);
            this.attackDelay = (30 - this.droneInfo.core * 3);
            if (hasEnabled(Module.shooting)) {
                this.attackDelay /= 2;
            }
            this.droneInfo.reduceBattery(dam * this.droneInfo.getBatteryConsumptionRate(this));
        }
    }

    public boolean func_70097_a(DamageSource source, float amount)
    {
        double damage = 0.0D;
        if (((source == DamageSource.field_76372_a) || (source == DamageSource.field_76370_b) || (source == DamageSource.field_76371_c)) &&
                (hasEnabled(Module.heatPower))) {
            this.droneInfo.reduceBattery(-amount);
        } else {
            damage = amount;
        }
        if ((this.hitDelay <= 0) && (damage > 0.0D))
        {
            this.droneInfo.reduceDamage(this, damage * (1.0D - this.droneInfo.getDamageReduction(this)));
            func_70018_K();
            if (this.droneInfo.getDamage(false) <= 0.0D) {
                func_70106_y();
            }
            if (source.func_76346_g() != null) {
                func_70653_a(this, (float)(0.5D * Math.sqrt(damage)), source.func_76346_g().field_70165_t - this.field_70165_t,
                        source.func_76346_g().field_70161_v - this.field_70161_v);
            }
            func_184185_a(func_184601_bQ(), 0.5F, 1.0F + this.field_70146_Z.nextFloat());
        }
        return super.func_70097_a(source, amount);
    }

    public void func_70106_y()
    {
        super.func_70106_y();
        for (int k = 0; k < 20; k++)
        {
            double d2 = this.field_70146_Z.nextGaussian() * 0.02D;
            double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
            double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
            this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, this.field_70165_t + this.field_70146_Z
                    .nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, this.field_70163_u + this.field_70146_Z
                    .nextFloat() * this.field_70131_O, this.field_70161_v + this.field_70146_Z
                    .nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, d2, d0, d1, new int[0]);
        }
        func_184185_a(func_184615_bR(), 0.7F, 1.0F + this.field_70146_Z.nextFloat());
    }

    protected void func_70018_K()
    {
        super.func_70018_K();
        this.hitDelay = 20;
    }

    public void func_70653_a(Entity entityIn, float strenght, double xRatio, double zRatio)
    {
        this.field_70160_al = true;
        float f = MathHelper.func_76133_a(xRatio * xRatio + zRatio * zRatio);
        this.field_70159_w /= 2.0D;
        this.field_70179_y /= 2.0D;
        this.field_70159_w -= xRatio / f * strenght;
        this.field_70179_y -= zRatio / f * strenght;
        if (this.field_70122_E)
        {
            this.field_70181_x /= 2.0D;
            if (this.field_70181_x > 0.4000000059604645D) {
                this.field_70181_x = 0.4000000059604645D;
            }
        }
    }

    @Nullable
    protected SoundEvent func_184601_bQ()
    {
        return SoundEvents.field_187543_bD;
    }

    @Nullable
    protected SoundEvent func_184615_bR()
    {
        return SoundEvents.field_187661_by;
    }

    protected SoundEvent func_184588_d(int heightIn)
    {
        return heightIn > 4 ? SoundEvents.field_187655_bw : SoundEvents.field_187545_bE;
    }

    public void setDroneInfo(DroneInfo di)
    {
        this.droneInfo = di;
    }

    public void func_70037_a(NBTTagCompound tagCompound)
    {
        if (this.droneInfo == null) {
            this.droneInfo = new DroneInfo();
        }
        this.droneInfo.readFromNBT(tagCompound);
        setFlyingMode(tagCompound.func_74762_e("Mode"));
        func_184212_Q().func_187227_b(CONTROLPLAYER, tagCompound.func_74779_i("ConPlayer"));
        if (tagCompound.func_74764_b("Drone Path"))
        {
            NBTTagList automatePath = tagCompound.func_150295_c("Drone Path", 10);
            if (automatePath.func_74745_c() > 0)
            {
                this.automatedPath = new Path();
                for (int a = 0; a < automatePath.func_74745_c(); a++)
                {
                    NBTTagCompound ntag = automatePath.func_150305_b(a);
                    this.automatedPath.addNode(new Node(ntag.func_74769_h("X"), ntag.func_74769_h("Y"), ntag.func_74769_h("Z")));
                }
            }
        }
    }

    public void func_70014_b(NBTTagCompound tagCompound)
    {
        if (this.droneInfo == null) {
            this.droneInfo = new DroneInfo();
        }
        this.droneInfo.writeToNBT(tagCompound);
        tagCompound.func_74768_a("Mode", getFlyingMode());
        tagCompound.func_74778_a("ConPlayer", getControllingPlayer() == null ? "" : getControllingPlayer().func_70005_c_());
        if (this.automatedPath != null)
        {
            NBTTagList automatePath = new NBTTagList();
            for (int a = 0; a < this.automatedPath.nodes.size(); a++)
            {
                Vec3d n = ((Node)this.automatedPath.nodes.get(a)).toVec();
                NBTTagCompound ntag = new NBTTagCompound();
                ntag.func_74780_a("X", n.field_72450_a);
                ntag.func_74780_a("Y", n.field_72448_b);
                ntag.func_74780_a("Z", n.field_72449_c);
                automatePath.func_74742_a(ntag);
            }
            tagCompound.func_74782_a("Drone Path", automatePath);
        }
    }

    public static EntityDrone getDroneFromID(World world, int droneID)
    {
        if ((world == null) || (droneID < 0)) {
            return null;
        }
        List<EntityDrone> drones = getEntities(world, EntityDrone.class, EntitySelectors.field_94557_a);
        for (int a = 0; a < drones.size(); a++)
        {
            EntityDrone e = (EntityDrone)drones.get(a);
            if (e.getDroneID() == droneID) {
                return e;
            }
        }
        return null;
    }

    public static <T extends Entity> List<T> getEntities(World world, Class<? extends T> entityType, Predicate<? super T> filter)
    {
        List<T> list = Lists.newArrayList();
        for (int a = 0; a < world.field_72996_f.size(); a++)
        {
            Entity entity = (Entity)world.field_72996_f.get(a);
            if ((entity != null) && (entityType.isAssignableFrom(entity.getClass())) && ((filter == null) ||
                    (filter.apply(entity)))) {
                list.add(entity);
            }
        }
        return list;
    }

    public void writeSpawnData(ByteBuf buffer)
    {
        NBTTagCompound di = new NBTTagCompound();
        this.droneInfo.writeToNBT(di);
        ByteBufUtils.writeTag(buffer, di);
        buffer.writeInt(getFlyingMode());
    }

    public void readSpawnData(ByteBuf additionalData)
    {
        NBTTagCompound di = ByteBufUtils.readTag(additionalData);
        this.droneInfo = DroneInfo.fromNBT(di);
        setFlyingMode(additionalData.readInt());
    }
}
