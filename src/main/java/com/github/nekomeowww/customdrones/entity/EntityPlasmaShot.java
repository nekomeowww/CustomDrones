package com.github.nekomeowww.customdrones.entity;

import io.netty.buffer.ByteBuf;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import com.github.nekomeowww.customdrones.api.Filters.FilterExcepts;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.helpers.WorldHelper;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade;

public class EntityPlasmaShot
        extends Entity
        implements IProjectile, IEntityAdditionalSpawnData
{
    public Color color;
    public Entity shooter;
    public double damage;
    public boolean homing = false;
    public Entity homingTarget;
    public NBTTagCompound shotTag = new NBTTagCompound();

    public EntityPlasmaShot(World worldIn)
    {
        super(worldIn);
        setSize(0.1F, 0.1F);
        this.color = new Color(1.0D, 1.0D, 1.0D, 1.0D);
    }

    public void setHoming(Entity e)
    {
        if (e == null)
        {
            this.homing = false;
            this.homingTarget = null;
        }
        else
        {
            this.homing = true;
            this.homingTarget = e;
        }
    }

    public List<ItemGunUpgrade.GunUpgrade> getGunUpgrades()
    {
        return ItemGunUpgrade.GunUpgrade.getUpgrades(this.shotTag, "Gun Upgrades");
    }

    public void addUpgrade(ItemGunUpgrade.GunUpgrade gu)
    {
        List<ItemGunUpgrade.GunUpgrade> gus = getGunUpgrades();
        if (!gus.contains(gus)) {
            gus.add(gu);
        }
        setUpgrades(gus);
    }

    public void removeUpgrade(ItemGunUpgrade.GunUpgrade gu)
    {
        List<ItemGunUpgrade.GunUpgrade> gus = getGunUpgrades();
        gus.remove(gu);
        setUpgrades(gus);
    }

    public void setUpgrades(List<ItemGunUpgrade.GunUpgrade> gus)
    {
        this.shotTag.setString("Gun Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(gus));
    }

    public boolean isInRangeToRenderDist(double distance)
    {
        return distance <= 16384.0D;
    }

    public AxisAlignedBB getCollisionBoundingBox()
    {
        return getEntityBoundingBox();
    }

    public AxisAlignedBB getCollisionBox(Entity entityIn)
    {
        return getEntityBoundingBox();
    }

    public boolean canBeCollidedWith()
    {
        return true;
    }

    public boolean canBePushed()
    {
        return true;
    }

    public boolean canRiderInteract()
    {
        return true;
    }

    public boolean startRiding(Entity entityIn)
    {
        return false;
    }

    public float getCollisionBorderSize()
    {
        return 0.1F;
    }

    public boolean canRenderOnFire()
    {
        return false;
    }

    public void onUpdate()
    {
        super.onUpdate();
        if ((this.homing) && (this.homingTarget != null))
        {
            double speed = Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
            Vec3d oldDir = new Vec3d(this.motionX, this.motionY, this.motionZ);
            Vec3d dir = VecHelper.fromTo(getPositionVector(), EntityHelper.getCenterVec(this.homingTarget));
            dir = VecHelper.setLength(dir, speed / 2.0D);
            dir = VecHelper.setLength(oldDir.add(dir), speed);
            this.motionX = dir.xCoord;
            this.motionY = dir.yCoord;
            this.motionZ = dir.zCoord;
        }
        move(this.motionX, this.motionY, this.motionZ);
        collideWithNearbyEntities();
        if (((this.onGround) || (!this.isCollided)) ||

                (this.ticksExisted >= 100)) {
            setDead();
        }
        if (this.homing) {
            this.getEntityWorld().spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX, this.posY, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    public void setDead()
    {
        if (!this.isDead)
        {
            List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.explosion)) && (!this.getEntityWorld().isRemote)) {
                this.getEntityWorld().createExplosion(this.shooter != null ? this.shooter : this, this.posX, this.posY, this.posZ,
                        (float)Math.abs(this.damage / 3.0D), true);
            }
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.scatter)) && (!this.world.isRemote))
            {
                Filters.FilterExcepts filter = new Filters.FilterExcepts(new Object[] { this.shooter, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class, IProjectile.class });

                double range = 32.0D;
                for (int a = 0; a < (this.homing ? 4 : 6); a++)
                {
                    Vec3d dir = new Vec3d(this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D, this.rand.nextDouble() - 0.5D).normalize();
                    EntityPlasmaShot babyShot = new EntityPlasmaShot(this.getEntityWorld());
                    babyShot.setPosition(this.posX, this.posY, this.posZ);
                    babyShot.color = this.color;
                    this.damage *= 0.5D;
                    babyShot.shotTag = this.shotTag.copy();
                    babyShot.removeUpgrade(ItemGunUpgrade.GunUpgrade.scatter);
                    babyShot.shooter = (this.shooter != null ? this.shooter : this);
                    babyShot.homing = this.homing;
                    if (this.homing) {
                        babyShot.homingTarget = WorldHelper.getEntityBestInAngle(this.getEntityWorld(), getPositionVector(), dir,
                                getEntityBoundingBox().expandXyz(range), 90.0D, this.homingTarget, filter);
                    }
                    double speed = Math.sqrt(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ) * 0.5D;
                    Vec3d velocity = new Vec3d(dir.xCoord, 1.0D, dir.zCoord).normalize().scale(speed);
                    babyShot.motionX = velocity.xCoord;
                    babyShot.motionY = velocity.yCoord;
                    babyShot.motionZ = velocity.zCoord;
                    this.getEntityWorld().spawnEntity(babyShot);
                }
            }
        }
        super.setDead();
    }

    public void collideWithNearbyEntities()
    {
        Filters.FilterExcepts excepts = new Filters.FilterExcepts(new Object[] { this, EntityPlasmaShot.class, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class });

        List<Entity> list = this.getEntityWorld().getEntitiesInAABBexcluding(this,
                getEntityBoundingBox().expandXyz(getCollisionBorderSize()), excepts);
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                collideWith(entity);
            }
        }
    }

    public void collideWith(Entity entityIn)
    {
        List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
        boolean canHeal = (this.damage < 0.0D) && ((entityIn instanceof EntityLivingBase));
        boolean canHarm = !shouldNotHarmEntity(entityIn);
        if (canHeal) {
            ((EntityLivingBase)entityIn).heal((float)this.damage);
        } else if (canHarm) {
            entityIn.attackEntityFrom(DamageSource.causeThornsDamage(this.shooter), (float)this.damage);
        }
        if (canHarm)
        {
            if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) {
                entityIn.setFire((int)this.damage);
            }
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) && ((entityIn instanceof EntityLivingBase))) {
                ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(
                        Potion.getPotionFromResourceLocation("slowness"), (int)(this.damage * 20.0D)));
            }
        }
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) {
            entityIn.extinguish();
        }
        if (!this.getEntityWorld().isRemote) {
            setDead();
        }
    }

    public boolean shouldNotHarmEntity(Entity e)
    {
        if (e.isDead) {
            return true;
        }
        if (((this.shooter instanceof EntityDrone)) && (e == ((EntityDrone)this.shooter).getControllingPlayer())) {
            return true;
        }
        if (e == this.shooter) {
            return true;
        }
        return false;
    }

    protected void doBlockCollisions()
    {
        super.doBlockCollisions();
        AxisAlignedBB axisalignedbb = getEntityBoundingBox().expandXyz(getCollisionBorderSize());

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
                            Block b = iblockstate.getBlock();
                            if (b.canCollideCheck(iblockstate, true)) {
                                collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
                            }
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
    }

    public void collideWithBlock(BlockPos pos, IBlockState state)
    {
        pos = pos.toImmutable();
        Block block = state.getBlock();
        BlockPos posUp = pos.up().toImmutable();
        IBlockState stateUp = this.getEntityWorld().getBlockState(posUp);
        Block blockUp = stateUp.getBlock();
        boolean isSolid = (!(block instanceof BlockLiquid)) && (!block.isReplaceable(this.getEntityWorld(), pos));
        boolean toDie = isSolid;
        List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice))
        {
            if ((block == Blocks.WATER) || (block == Blocks.FLOWING_WATER)) {
                this.getEntityWorld().setBlockState(pos, Blocks.ICE.getDefaultState());
            } else if ((block == Blocks.LAVA) || (block == Blocks.FLOWING_LAVA)) {
                this.getEntityWorld().setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            } else if (block == Blocks.FIRE) {
                this.getEntityWorld().setBlockToAir(pos);
            } else if (blockUp == Blocks.FIRE) {
                this.getEntityWorld().setBlockToAir(posUp);
            } else if ((blockUp == Blocks.AIR) && (Blocks.SNOW_LAYER.canPlaceBlockAt(this.getEntityWorld(), posUp))) {
                this.getEntityWorld().setBlockState(posUp, Blocks.SNOW_LAYER.getDefaultState());
            }
            if ((block instanceof BlockLiquid)) {
                toDie = true;
            }
        }
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) {
            if (block == Blocks.ICE)
            {
                if (this.getEntityWorld().provider.doesWaterVaporize())
                {
                    this.getEntityWorld().setBlockToAir(pos);
                }
                else
                {
                    Material material = this.getEntityWorld().getBlockState(pos.down()).getMaterial();
                    if ((material.blocksMovement()) || (material.isLiquid()))
                    {
                        this.getEntityWorld().setBlockState(pos, Blocks.FLOWING_WATER.getDefaultState());
                    }
                    else
                    {
                        this.getEntityWorld().setBlockState(pos, Blocks.WATER.getDefaultState());
                        this.getEntityWorld().notifyBlockOfStateChange(pos, Blocks.WATER);
                    }
                }
            }
            else if ((block == Blocks.SNOW) || (block == Blocks.SNOW_LAYER)) {
                this.getEntityWorld().setBlockToAir(pos);
            } else if ((blockUp == Blocks.AIR) && (Blocks.FIRE.canPlaceBlockAt(this.getEntityWorld(), posUp))) {
                this.getEntityWorld().setBlockState(posUp, Blocks.FIRE.getDefaultState());
            }
        }
        if (toDie) {
            setDead();
        }
    }

    public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {}

    protected void entityInit() {}

    protected void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        this.color = new Color(tagCompound.getInteger("Color"));
        this.damage = tagCompound.getDouble("Damage");
        this.homing = tagCompound.getBoolean("Homing");
        this.shotTag = tagCompound.getCompoundTag("Shot tag");
        if (tagCompound.hasUniqueId("Shooter"))
        {
            UUID shooterUUID = tagCompound.getUniqueId("Shooter");
            this.shooter = WorldHelper.getEntityByPersistentUUID(this.getEntityWorld(), shooterUUID);
        }
    }

    protected void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        tagCompound.setInteger("Color", this.color.toInt());
        tagCompound.setDouble("Damage", this.damage);
        tagCompound.setBoolean("Homing", this.homing);
        tagCompound.setTag("Shot tag", this.shotTag);
        if (this.shooter != null) {
            tagCompound.setUniqueId("Shooter", this.shooter.getPersistentID());
        }
    }

    public void writeSpawnData(ByteBuf buffer)
    {
        buffer.writeInt(this.color.toInt());
        buffer.writeDouble(this.damage);
        buffer.writeBoolean(this.homing);
        ByteBufUtils.writeTag(buffer, this.shotTag);
        buffer.writeBoolean(this.shooter != null);
        buffer.writeLong(this.shooter != null ? this.shooter.getPersistentID().getMostSignificantBits() : 0L);
        buffer.writeLong(this.shooter != null ? this.shooter.getPersistentID().getLeastSignificantBits() : 0L);
    }

    public void readSpawnData(ByteBuf buffer)
    {
        this.color = new Color(buffer.readInt());
        this.damage = buffer.readDouble();
        this.homing = buffer.readBoolean();
        this.shotTag = ByteBufUtils.readTag(buffer);
        boolean hasShooter = buffer.readBoolean();
        long most = buffer.readLong();
        long least = buffer.readLong();
        if (hasShooter)
        {
            UUID shooterUUID = new UUID(most, least);
            this.shooter = WorldHelper.getEntityByPersistentUUID(this.getEntityWorld(), shooterUUID);
        }
    }
}
