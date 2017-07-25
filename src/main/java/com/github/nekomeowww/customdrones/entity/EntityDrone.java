package com.github.nekomeowww.customdrones.entity;

import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.item.IItemInteractDrone;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.lang.Math;
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
    private static final DataParameter<Boolean> DICHANGED = EntityDataManager.createKey(EntityDrone.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> FLYMODE = EntityDataManager.createKey(EntityDrone.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> CAMERA = EntityDataManager.createKey(EntityDrone.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Float> CAMERAPITCH = EntityDataManager.createKey(EntityDrone.class, DataSerializers.FLOAT);
    private static final DataParameter<String> CONTROLPLAYER = EntityDataManager.createKey(EntityDrone.class, DataSerializers.STRING);
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
        setSize(0.8F, 0.3F);
        this.droneInfo = new DroneInfo(this);
        applyDroneAttributes();
    }

    public ITextComponent getDisplayName()
    {
        return new TextComponentString(this.droneInfo.getDisplayName());
    }

    public int getDroneID()
    {
        return this.droneInfo == null ? -1 : this.droneInfo.id;
    }

    protected boolean canDespawn()
    {
        return false;
    }

    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(DICHANGED, Boolean.valueOf(false));
        this.dataManager.register(FLYMODE, Integer.valueOf(0));
        this.dataManager.register(CAMERA, Boolean.valueOf(false));
        this.dataManager.register(CAMERAPITCH, Float.valueOf(0.0F));
        this.dataManager.register(CONTROLPLAYER, "");
    }

    protected void applyDroneAttributes()
    {
        getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(this.droneInfo.getMaxDamage(this));
    }

    public Vec3d getLookVec()
    {
        return getLook(1.0F);
    }

    public Vec3d getHorizontalLookVec()
    {
        return getVectorForRotation(0.0F, this.rotationYaw);
    }

    public float getEyeHeight()
    {
        return 0.15F;
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean canBePushed()
    {
        return true;
    }

    protected boolean canTriggerWalking()
    {
        return true;
    }

    public boolean canRiderInteract()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public int getBrightnessForRender(float partialTicks)
    {
        return super.getBrightnessForRender(partialTicks);
    }

    public float getBrightness(float partialTicks)
    {
        return super.getBrightness(partialTicks);
    }

    public void fall(float distance, float damageMultiplier) {}

    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
    {
        ItemStack stack = player.getHeldItemMainhand();
        boolean hasController = playerHasCorrespondingController(player, false);
        if (usefulInteraction(player, vec, stack, hand, hasController))
        {
            if ((stack == null) || (player.isSneaking()))
            {
                if (hasController)
                {
                    player.openGui(CustomDrones.instance, 1, this.getEntityWorld(), getDroneID(), 0, 0);
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
                        if (stack.getItem() == CustomDrones.droneModule)
                        {
                            Module mod = ItemDroneModule.getModule(stack);
                            DroneInfo.ApplyResult addEntry = this.droneInfo.canAddModule(mod);
                            if (addEntry.successful) {
                                if (!this.getEntityWorld().isRemote)
                                {
                                    this.droneInfo.applyModule(mod);
                                    this.droneInfo.updateDroneInfoToClient(player);
                                    player.setHeldItem(EnumHand.MAIN_HAND, null);
                                }
                            }
                            EntityHelper.addChat(player, 1, addEntry.displayString);
                        }
                        else
                        {
                            stack = this.droneInfo.applyItem(this, stack);
                            player.setHeldItem(EnumHand.MAIN_HAND, stack);
                        }
                        return EnumActionResult.SUCCESS;
                    }
                    EntityHelper.addChat(player, 1, TextFormatting.RED + "No corresponding controller");
                    return EnumActionResult.FAIL;
                }
                if ((stack.getItem() instanceof IItemInteractDrone)) {
                    return ((IItemInteractDrone)stack.getItem()).interactWithDrone(this.getEntityWorld(), this, player, vec, stack, hand);
                }
            }
        }
        return super.applyPlayerInteraction(player, vec, is, hand);
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
            return CustomDrones.droneFlyer.getControllerFreq(player.getHeldItemMainhand()) == this.droneInfo.droneFreq;
        }
        InventoryPlayer inv = player.inventory;
        for (int a = 0; a < inv.getSizeInventory(); a++) {
            if (CustomDrones.droneFlyer.getControllerFreq(inv.getStackInSlot(a)) == this.droneInfo.droneFreq) {
                return true;
            }
        }
        return false;
    }

    public Entity getRider()
    {
        return getControllingPassenger();
    }

    public double getMountedYOffset()
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
            h = getRider().height * 0.88D;
        } else {
            h = getRider().height * 1.15D;
        }
        return h;
    }

    public void dropRider()
    {
        if (getRider() != null) {
            getRider().dismountRidingEntity();
        }
    }

    public boolean hasEnabled(Module m)
    {
        return this.droneInfo.hasEnabled(m);
    }

    public int getFlyingMode()
    {
        return ((Integer)getDataManager().get(FLYMODE)).intValue();
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
        getDataManager().set(FLYMODE, Integer.valueOf(i));
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
        return ((Boolean)this.dataManager.get(DICHANGED)).booleanValue();
    }

    public void setWatchedDIChanged(boolean b)
    {
        getDataManager().set(DICHANGED, Boolean.valueOf(b));
    }

    public void setCameraMode(boolean b)
    {
        if (this.getEntityWorld().isRemote) {
            PacketDispatcher.sendToServer(new PacketDroneSetCameraMode(this, b));
        } else {
            getDataManager().set(CAMERA, Boolean.valueOf(b));
        }
    }

    public boolean getCameraMode()
    {
        return ((Boolean)getDataManager().get(CAMERA)).booleanValue();
    }

    public void setCameraPitch(float f)
    {
        if (this.getEntityWorld().isRemote) {
            PacketDispatcher.sendToServer(new PacketDroneSetCameraPitch(this, f));
        }
        getDataManager().set(CAMERAPITCH, Float.valueOf(f));
    }

    public float getCameraPitch()
    {
        return ((Float)getDataManager().get(CAMERAPITCH)).floatValue();
    }

    public void setControllingPlayer(EntityPlayer p)
    {
        this.controllingPlayer = p;
        getDataManager().set(CONTROLPLAYER, p == null ? "" : p.getName());
    }

    public EntityPlayer getControllingPlayer()
    {
        String s = (String)getDataManager().get(CONTROLPLAYER);
        if ((s.length() == 0) || (s.equals(""))) {
            this.controllingPlayer = null;
        } else if ((this.controllingPlayer == null) || (!this.controllingPlayer.getName().equals(s))) {
            this.controllingPlayer = this.getEntityWorld().getPlayerEntityByName(s);
        }
        return this.controllingPlayer;
    }

    public void onEntityUpdate()
    {
        super.onEntityUpdate();
        if (this.hitDelay > 0) {
            this.hitDelay -= 1;
        }
        if (this.attackDelay > 0) {
            this.attackDelay -= 1;
        }
        if ((this.pickupDelay > 0) && (getRider() == null)) {
            this.pickupDelay -= 1;
        }
        if (!this.getEntityWorld().isRemote) {
            setWatchedDIChanged(this.droneInfo.isChanged);
        }
        if ((getDroneAttackTarget() == null) || (getDroneAttackTarget().isDead)) {
            setDroneAttackTarget(null, true);
        }
        updateFlyingBehavior();
        updateRotation();
        updateWingRotation();
        this.droneInfo.updateDroneInfo(this);
        double speedControl = 1.0D - 0.087D * Math.pow(this.droneInfo.getMaxSpeed(), 0.1D);
        this.motionX *= speedControl;
        this.motionY *= speedControl;
        this.motionZ *= speedControl;
        move(this.motionX, this.motionY, this.motionZ);
        collideWithNearbyEntities();
        pushOutOfBlocks(this.posX, this.posY, this.posZ);
    }

    private void move(double motionX, double motionY, double motionZ)
    {

    }

    public void updateRotation()
    {
        double maxYawChange = 6.0D;
        double nextYaw = (float)(Math.atan2(-this.motionX, this.motionZ) / 3.141592653589793D * 180.0D);
        if ((Math.abs(this.motionX) <= 0.001D) && (Math.abs(this.motionZ) <= 0.001D)) {
            nextYaw = this.rotationYaw;
        }
        double changingYaw = nextYaw - this.rotationYaw;
        changingYaw %= 360.0D;
        if (360.0D - Math.abs(changingYaw) < Math.abs(changingYaw)) {
            changingYaw *= -1.0D;
        }
        if (changingYaw > 0.0D) {
            changingYaw = Math.min(maxYawChange, changingYaw);
        } else if (changingYaw < 0.0D) {
            changingYaw = -Math.min(maxYawChange, -changingYaw);
        }
        this.rotationYaw = ((float)(this.rotationYaw + changingYaw));
        this.rotationYaw %= 360.0F;
        if ((getCameraMode()) && (getControllingPlayer() != null)) {
            if (this.getEntityWorld().isRemote) {
                setCameraPitch(getControllingPlayer().rotationPitch);
            }
        }
        double nextPitch = getCameraPitch();
        double maxPitchChange = 3.0D;
        double changingPitch = nextPitch - this.rotationPitch;
        if (changingPitch > 0.0D) {
            changingPitch = Math.min(maxPitchChange, changingPitch);
        } else if (changingPitch < 0.0D) {
            changingPitch = -Math.min(maxPitchChange, -changingPitch);
        }
        this.rotationPitch = ((float)(this.rotationPitch + changingPitch));
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
            this.motionY -= 0.08D;
            if (getRider() != null) {
                getRider().dismountRidingEntity();
            }
        }
        else
        {
            this.idle = false;
            if (mode == 0)
            {
                this.idle = true;
                this.motionY -= 0.08D;
                if (getRider() != null) {
                    getRider().dismountRidingEntity();
                }
            }
            else if (mode == 1)
            {
                this.motionY -= 0.08D;
                double belowY = getBelowSurfaceY();
                double distY = this.posY - belowY;
                double maxDist = 1.5D + getRiderHeight();
                if (distY < maxDist) {
                    this.motionY += 0.08D * Math.pow((maxDist - distY) * 2.0D, 0.5D) * this.droneInfo.getEngineLevel();
                }
                this.idle = true;
            }
            else
            {
                double belowY = getBelowSurfaceY();
                double distY = this.posY - belowY;
                if (distY > getRiderHeight())
                {
                    Entity rider = getRider();
                    double riderWeight = rider != null ? 0.5D + rider.width * rider.height : 0.5D;
                    double pullUpPower = this.droneInfo.getEngineLevel() * Math.sqrt(this.droneInfo.getMaxSpeed());
                    if (pullUpPower < riderWeight) {
                        this.motionY -= 0.08D * (riderWeight - pullUpPower) / riderWeight;
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
        flyNormalAlong(VecHelper.fromTo(getPositionVector(), pos), minMove, rate);
    }

    public void flyNormalAlong(Vec3d vec, double minMove, double rate)
    {
        if ((vec != null) && (vec.lengthVector() >= minMove))
        {
            Vec3d vec1 = vec.lengthVector() > 1.0D ? vec.normalize() : vec.addVector(0.0D, 0.0D, 0.0D);
            flyNormalAlong(vec1.xCoord, vec1.yCoord, vec1.zCoord, rate);
        }
    }

    public void flyNormalAlong(double x, double y, double z, double rate)
    {
        double speedMult = getSpeedMultiplication() * rate;
        this.motionX += x * speedMult;
        this.motionY += y * speedMult;
        this.motionZ += z * speedMult;
    }

    public EntityPlayer getNearestPlayer(double distance, int hasController)
    {
        double d0 = -1.0D;
        EntityPlayer entityplayer = null;
        for (int i = 0; i < this.getEntityWorld().playerEntities.size(); i++)
        {
            EntityPlayer entityplayer1 = (EntityPlayer)this.getEntityWorld().playerEntities.get(i);
            if (EntitySelectors.NOT_SPECTATING.apply(entityplayer1)) {
                if (hasController != 0)
                {
                    if (!playerHasCorrespondingController(entityplayer1, hasController == 2)) {}
                }
                else
                {
                    double d1 = entityplayer1.getDistanceSq(this.posX, this.posY, this.posZ);
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
        for (int x = (int)Math.floor(getEntityBoundingBox().minX); x <= getEntityBoundingBox().maxX; x++) {
            for (int z = (int)Math.floor(getEntityBoundingBox().minZ); z <= getEntityBoundingBox().maxZ; z++) {
                for (int y = (int)Math.floor(this.posY); y > 0; y--)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState bs = this.getEntityWorld().getBlockState(pos);
                    if ((!bs.getMaterial().isReplaceable()) || (bs.getMaterial() != Material.AIR))
                    {
                        double posMax = pos.getY();
                        if (this.getEntityWorld().getBlockState(pos).getCollisionBoundingBox(this.getEntityWorld(), pos) != null) {
                            posMax += this.getEntityWorld().getBlockState(pos).getCollisionBoundingBox(this.getEntityWorld(), pos).maxY;
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
        double add = (this.motionX * this.motionX + (this.prevPosY - this.posY) * (this.prevPosY - this.posY) + this.motionZ * this.motionZ) * 1000.0D;
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

    public void applyEntityCollision(Entity e)
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
            super.applyEntityCollision(e);
        }
        if (getFlyingMode() != 0) {
            if ((getRider() == null) && (canPickUpEntity(e)))
            {
                if (this.pickupDelay == 0)
                {
                    e.startRiding(this);
                    setPosition(this.posX, e.posY + getRiderHeight(), this.posZ);
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
                    for (int a = 0; a < p.inventory.getSizeInventory(); a++)
                    {
                        ItemStack is = p.inventory.getStackInSlot(a);
                        if ((is != null) && (is.getItem() == CustomDrones.droneFlyer) &&
                                (CustomDrones.droneFlyer.getControllerFreq(is) == this.droneInfo.droneFreq))
                        {
                            causeDam = false;
                            break;
                        }
                    }
                }
                if (causeDam)
                {
                    double spdSqr = this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ;
                    double spdDmgRate = Math.sqrt(spdSqr) * 20.0D / this.droneInfo.getMaxSpeed();
                    double baseDmg = this.droneInfo.getAttackPower(this);
                    double damage = baseDmg * 0.8D * spdDmgRate + 0.2D * baseDmg;
                    attackEntity(e, getAttackDamageSource(), (float)damage);
                }
            }
        }
    }

    protected void collideWithNearbyEntities()
    {
        List<Entity> list = this.getEntityWorld().getEntitiesWithinAABBExcludingEntity(this,
                getEntityBoundingBox().expandXyz(getCollisionBorderSize()));
        if (!list.isEmpty()) {
            for (int i = 0; i < list.size(); i++)
            {
                Entity entity = (Entity)list.get(i);
                applyEntityCollision(entity);
            }
        }
    }

    protected void doBlockCollisions()
    {
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().offset(this.motionX, this.motionY, this.motionZ);

        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.minX + 0.001D, axisalignedbb.minY + 0.001D, axisalignedbb.minZ + 0.001D);

        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.retain(axisalignedbb.maxX - 0.001D, axisalignedbb.maxY - 0.001D, axisalignedbb.maxZ - 0.001D);
        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.retain();
        if (this.getEntityWorld().isAreaLoaded(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1)) {
            for (int i = blockpos$pooledmutableblockpos.getX(); i <= blockpos$pooledmutableblockpos1.getX(); i++) {
                for (int j = blockpos$pooledmutableblockpos.getY(); j <= blockpos$pooledmutableblockpos1.getY(); j++) {
                    for (int k = blockpos$pooledmutableblockpos.getZ(); k <= blockpos$pooledmutableblockpos1.getZ(); k++)
                    {
                        blockpos$pooledmutableblockpos2.setPos(i, j, k);
                        IBlockState iblockstate = this.getEntityWorld().getBlockState(blockpos$pooledmutableblockpos2);
                        try
                        {
                            collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
                        }
                        catch (Throwable throwable)
                        {
                            CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Colliding entity with block");

                            CrashReportCategory crashreportcategory = crashreport.makeCategory("Block being collided with");
                            CrashReportCategory.addBlockInfo(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);

                            throw new ReportedException(crashreport);
                        }
                    }
                }
            }
        }
        blockpos$pooledmutableblockpos.release();
        blockpos$pooledmutableblockpos1.release();
        blockpos$pooledmutableblockpos2.release();
        super.doBlockCollisions();
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
        return this.controllingPlayer == null ? DamageSource.causeThornsDamage(this) : DamageSource.causeIndirectDamage(this, this.controllingPlayer);
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
        if ((canPickUpNonPlayers()) && (!(e instanceof EntityItem)) && (!(e instanceof EntityXPOrb)) && (!(e instanceof IProjectile)) && (e.width * e.height < 4 + this.droneInfo.engine)) {
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

    public Entity getControllingPassenger()
    {
        return getPassengers().isEmpty() ? null : (Entity)getPassengers().get(0);
    }

    public void setDroneAttackTarget(Entity e, boolean forcefully)
    {
        Entity oldTarget = getDroneAttackTarget();
        if ((forcefully) || (oldTarget == null) || (oldTarget.isDead)) {
            this.entityToAttack = e;
        } else if ((getDistanceSqToEntity(e) < getDistanceSqToEntity(oldTarget)) || (!canEntityBeSeen(oldTarget))) {
            this.entityToAttack = e;
        }
    }

    public boolean canEntityBeSeen(Entity e)
    {
        return this.getEntityWorld().rayTraceBlocks(new Vec3d(this.posX, this.posY + getEyeHeight(), this.posZ), new Vec3d(e.posX, e.posY + e
                .getEyeHeight(), e.posZ), false, true, false) == null;
    }

    public boolean canPosBeSeen(Vec3d pos)
    {
        return this.getEntityWorld().rayTraceBlocks(new Vec3d(this.posX, this.posY + getEyeHeight(), this.posZ), pos, false, true, false) == null;
    }

    public Entity getDroneAttackTarget()
    {
        return this.entityToAttack;
    }

    public void attackEntity(Entity e, DamageSource dmg, float dam)
    {
        if (this.attackDelay == 0)
        {
            e.attackEntityFrom(dmg, dam);
            this.attackDelay = (30 - this.droneInfo.core * 3);
            if (hasEnabled(Module.shooting)) {
                this.attackDelay /= 2;
            }
            this.droneInfo.reduceBattery(dam * this.droneInfo.getBatteryConsumptionRate(this));
        }
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        double damage = 0.0D;
        if (((source == DamageSource.inFire) || (source == DamageSource.onFire) || (source == DamageSource.lava)) &&
                (hasEnabled(Module.heatPower))) {
            this.droneInfo.reduceBattery(-amount);
        } else {
            damage = amount;
        }
        if ((this.hitDelay <= 0) && (damage > 0.0D))
        {
            this.droneInfo.reduceDamage(this, damage * (1.0D - this.droneInfo.getDamageReduction(this)));
            setBeenAttacked();
            if (this.droneInfo.getDamage(false) <= 0.0D) {
                setDead();
            }
            if (source.getEntity() != null) {
                knockBack(this, (float)(0.5D * Math.sqrt(damage)), source.getEntity().posX - this.posX,
                        source.getEntity().posZ - this.posZ);
            }
            playSound(getHurtSound(), 0.5F, 1.0F + this.rand.nextFloat());
        }
        return super.attackEntityFrom(source, amount);
    }

    public void setDead()
    {
        super.setDead();
        for (int k = 0; k < 20; k++)
        {
            double d2 = this.rand.nextGaussian() * 0.02D;
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            this.getEntityWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + this.rand
                    .nextFloat() * this.width * 2.0F - this.width, this.posY + this.rand
                    .nextFloat() * this.height, this.posZ + this.rand
                    .nextFloat() * this.width * 2.0F - this.width, d2, d0, d1, new int[0]);
        }
        playSound(getDeathSound(), 0.7F, 1.0F + this.rand.nextFloat());
    }

    protected void setBeenAttacked()
    {
        super.setBeenAttacked();
        this.hitDelay = 20;
    }

    public void knockBack(Entity entityIn, float strenght, double xRatio, double zRatio)
    {
        float sqrtResult = (float)Math.sqrt(xRatio * xRatio + zRatio * zRatio);
        this.isAirBorne = true;
        float f = sqrtResult;
        this.motionX /= 2.0D;
        this.motionZ /= 2.0D;
        this.motionX -= xRatio / f * strenght;
        this.motionZ -= zRatio / f * strenght;
        if (this.onGround)
        {
            this.motionY /= 2.0D;
            if (this.motionY > 0.4000000059604645D) {
                this.motionY = 0.4000000059604645D;
            }
        }
    }

    @Nullable
    protected SoundEvent getHurtSound()
    {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    protected SoundEvent getFallSound(int heightIn)
    {
        return heightIn > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    public void setDroneInfo(DroneInfo di)
    {
        this.droneInfo = di;
    }

    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        if (this.droneInfo == null) {
            this.droneInfo = new DroneInfo();
        }
        this.droneInfo.readFromNBT(tagCompound);
        setFlyingMode(tagCompound.getInteger("Mode"));
        getDataManager().set(CONTROLPLAYER, tagCompound.getString("ConPlayer"));
        if (tagCompound.hasKey("Drone Path"))
        {
            NBTTagList automatePath = tagCompound.getTagList("Drone Path", 10);
            if (automatePath.tagCount() > 0)
            {
                this.automatedPath = new Path();
                for (int a = 0; a < automatePath.tagCount(); a++)
                {
                    NBTTagCompound ntag = automatePath.getCompoundTagAt(a);
                    this.automatedPath.addNode(new Node(ntag.getDouble("X"), ntag.getDouble("Y"), ntag.getDouble("Z")));
                }
            }
        }
    }

    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        if (this.droneInfo == null) {
            this.droneInfo = new DroneInfo();
        }
        this.droneInfo.writeToNBT(tagCompound);
        tagCompound.setInteger("Mode", getFlyingMode());
        tagCompound.setString("ConPlayer", getControllingPlayer() == null ? "" : getControllingPlayer().getName());
        if (this.automatedPath != null)
        {
            NBTTagList automatePath = new NBTTagList();
            for (int a = 0; a < this.automatedPath.nodes.size(); a++)
            {
                Vec3d n = ((Node)this.automatedPath.nodes.get(a)).toVec();
                NBTTagCompound ntag = new NBTTagCompound();
                ntag.setDouble("X", n.xCoord);
                ntag.setDouble("Y", n.yCoord);
                ntag.setDouble("Z", n.zCoord);
                automatePath.appendTag(ntag);
            }
            tagCompound.setTag("Drone Path", automatePath);
        }
    }

    public static EntityDrone getDroneFromID(World world, int droneID)
    {
        if ((world == null) || (droneID < 0)) {
            return null;
        }
        List<EntityDrone> drones = getEntities(world, EntityDrone.class, EntitySelectors.IS_ALIVE);
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
        for (int a = 0; a < world.loadedEntityList.size(); a++)
        {
            T entity = (T)world.loadedEntityList.get(a);
            if ((entity != null) && (entityType.isAssignableFrom(entity.getClass())) && ((filter == null) || (filter.apply(entity))))
            {
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
