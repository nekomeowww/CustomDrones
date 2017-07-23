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
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade.GunUpgrade;

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
        func_70105_a(0.1F, 0.1F);
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
        this.shotTag.func_74778_a("Gun Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(gus));
    }

    public boolean func_70112_a(double distance)
    {
        return distance <= 16384.0D;
    }

    public AxisAlignedBB func_70046_E()
    {
        return func_174813_aQ();
    }

    public AxisAlignedBB func_70114_g(Entity entityIn)
    {
        return func_174813_aQ();
    }

    public boolean func_70067_L()
    {
        return true;
    }

    public boolean func_70104_M()
    {
        return true;
    }

    public boolean canRiderInteract()
    {
        return true;
    }

    public boolean func_184220_m(Entity entityIn)
    {
        return false;
    }

    public float func_70111_Y()
    {
        return 0.1F;
    }

    public boolean func_90999_ad()
    {
        return false;
    }

    public void func_70071_h_()
    {
        super.func_70071_h_();
        if ((this.homing) && (this.homingTarget != null))
        {
            double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
            Vec3d oldDir = new Vec3d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
            Vec3d dir = VecHelper.fromTo(func_174791_d(), EntityHelper.getCenterVec(this.homingTarget));
            dir = VecHelper.setLength(dir, speed / 2.0D);
            dir = VecHelper.setLength(oldDir.func_178787_e(dir), speed);
            this.field_70159_w = dir.field_72450_a;
            this.field_70181_x = dir.field_72448_b;
            this.field_70179_y = dir.field_72449_c;
        }
        func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
        collideWithNearbyEntities();
        if (((this.field_70122_E) || (!this.field_70132_H)) ||

                (this.field_70173_aa >= 100)) {
            func_70106_y();
        }
        if (this.homing) {
            this.field_70170_p.func_175688_a(EnumParticleTypes.CRIT_MAGIC, this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
        }
    }

    public void func_70106_y()
    {
        if (!this.field_70128_L)
        {
            List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.explosion)) && (!this.field_70170_p.field_72995_K)) {
                this.field_70170_p.func_72876_a(this.shooter != null ? this.shooter : this, this.field_70165_t, this.field_70163_u, this.field_70161_v,
                        (float)Math.abs(this.damage / 3.0D), true);
            }
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.scatter)) && (!this.field_70170_p.field_72995_K))
            {
                Filters.FilterExcepts filter = new Filters.FilterExcepts(new Object[] { this.shooter, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class, IProjectile.class });

                double range = 32.0D;
                for (int a = 0; a < (this.homing ? 4 : 6); a++)
                {
                    Vec3d dir = new Vec3d(this.field_70146_Z.nextDouble() - 0.5D, this.field_70146_Z.nextDouble() - 0.5D, this.field_70146_Z.nextDouble() - 0.5D).func_72432_b();
                    EntityPlasmaShot babyShot = new EntityPlasmaShot(this.field_70170_p);
                    babyShot.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
                    babyShot.color = this.color;
                    this.damage *= 0.5D;
                    babyShot.shotTag = this.shotTag.func_74737_b();
                    babyShot.removeUpgrade(ItemGunUpgrade.GunUpgrade.scatter);
                    babyShot.shooter = (this.shooter != null ? this.shooter : this);
                    babyShot.homing = this.homing;
                    if (this.homing) {
                        babyShot.homingTarget = WorldHelper.getEntityBestInAngle(this.field_70170_p, func_174791_d(), dir,
                                func_174813_aQ().func_186662_g(range), 90.0D, this.homingTarget, filter);
                    }
                    double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y) * 0.5D;
                    Vec3d velocity = new Vec3d(dir.field_72450_a, 1.0D, dir.field_72449_c).func_72432_b().func_186678_a(speed);
                    babyShot.field_70159_w = velocity.field_72450_a;
                    babyShot.field_70181_x = velocity.field_72448_b;
                    babyShot.field_70179_y = velocity.field_72449_c;
                    this.field_70170_p.func_72838_d(babyShot);
                }
            }
        }
        super.func_70106_y();
    }

    public void collideWithNearbyEntities()
    {
        Filters.FilterExcepts excepts = new Filters.FilterExcepts(new Object[] { this, EntityPlasmaShot.class, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class });

        List<Entity> list = this.field_70170_p.func_175674_a(this,
                func_174813_aQ().func_186662_g(func_70111_Y()), excepts);
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
            ((EntityLivingBase)entityIn).func_70691_i((float)this.damage);
        } else if (canHarm) {
            entityIn.func_70097_a(DamageSource.func_92087_a(this.shooter), (float)this.damage);
        }
        if (canHarm)
        {
            if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) {
                entityIn.func_70015_d((int)this.damage);
            }
            if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) && ((entityIn instanceof EntityLivingBase))) {
                ((EntityLivingBase)entityIn).func_70690_d(new PotionEffect(
                        Potion.func_180142_b("slowness"), (int)(this.damage * 20.0D)));
            }
        }
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) {
            entityIn.func_70066_B();
        }
        if (!this.field_70170_p.field_72995_K) {
            func_70106_y();
        }
    }

    public boolean shouldNotHarmEntity(Entity e)
    {
        if (e.field_70128_L) {
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

    protected void func_145775_I()
    {
        super.func_145775_I();
        AxisAlignedBB axisalignedbb = func_174813_aQ().func_186662_g(func_70111_Y());

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
                            Block b = iblockstate.func_177230_c();
                            if (b.func_176209_a(iblockstate, true)) {
                                collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
                            }
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
    }

    public void collideWithBlock(BlockPos pos, IBlockState state)
    {
        pos = pos.func_185334_h();
        Block block = state.func_177230_c();
        BlockPos posUp = pos.func_177984_a().func_185334_h();
        IBlockState stateUp = this.field_70170_p.func_180495_p(posUp);
        Block blockUp = stateUp.func_177230_c();
        boolean isSolid = (!(block instanceof BlockLiquid)) && (!block.func_176200_f(this.field_70170_p, pos));
        boolean toDie = isSolid;
        List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice))
        {
            if ((block == Blocks.field_150355_j) || (block == Blocks.field_150358_i)) {
                this.field_70170_p.func_175656_a(pos, Blocks.field_150432_aD.func_176223_P());
            } else if ((block == Blocks.field_150353_l) || (block == Blocks.field_150356_k)) {
                this.field_70170_p.func_175656_a(pos, Blocks.field_150347_e.func_176223_P());
            } else if (block == Blocks.field_150480_ab) {
                this.field_70170_p.func_175698_g(pos);
            } else if (blockUp == Blocks.field_150480_ab) {
                this.field_70170_p.func_175698_g(posUp);
            } else if ((blockUp == Blocks.field_150350_a) && (Blocks.field_150431_aC.func_176196_c(this.field_70170_p, posUp))) {
                this.field_70170_p.func_175656_a(posUp, Blocks.field_150431_aC.func_176223_P());
            }
            if ((block instanceof BlockLiquid)) {
                toDie = true;
            }
        }
        if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) {
            if (block == Blocks.field_150432_aD)
            {
                if (this.field_70170_p.field_73011_w.func_177500_n())
                {
                    this.field_70170_p.func_175698_g(pos);
                }
                else
                {
                    Material material = this.field_70170_p.func_180495_p(pos.func_177977_b()).func_185904_a();
                    if ((material.func_76230_c()) || (material.func_76224_d()))
                    {
                        this.field_70170_p.func_175656_a(pos, Blocks.field_150358_i.func_176223_P());
                    }
                    else
                    {
                        this.field_70170_p.func_175656_a(pos, Blocks.field_150355_j.func_176223_P());
                        this.field_70170_p.func_180496_d(pos, Blocks.field_150355_j);
                    }
                }
            }
            else if ((block == Blocks.field_150433_aE) || (block == Blocks.field_150431_aC)) {
                this.field_70170_p.func_175698_g(pos);
            } else if ((blockUp == Blocks.field_150350_a) && (Blocks.field_150480_ab.func_176196_c(this.field_70170_p, posUp))) {
                this.field_70170_p.func_175656_a(posUp, Blocks.field_150480_ab.func_176223_P());
            }
        }
        if (toDie) {
            func_70106_y();
        }
    }

    public void func_70186_c(double x, double y, double z, float velocity, float inaccuracy) {}

    protected void func_70088_a() {}

    protected void func_70037_a(NBTTagCompound tagCompound)
    {
        this.color = new Color(tagCompound.func_74762_e("Color"));
        this.damage = tagCompound.func_74769_h("Damage");
        this.homing = tagCompound.func_74767_n("Homing");
        this.shotTag = tagCompound.func_74775_l("Shot tag");
        if (tagCompound.func_186855_b("Shooter"))
        {
            UUID shooterUUID = tagCompound.func_186857_a("Shooter");
            this.shooter = WorldHelper.getEntityByPersistentUUID(this.field_70170_p, shooterUUID);
        }
    }

    protected void func_70014_b(NBTTagCompound tagCompound)
    {
        tagCompound.func_74768_a("Color", this.color.toInt());
        tagCompound.func_74780_a("Damage", this.damage);
        tagCompound.func_74757_a("Homing", this.homing);
        tagCompound.func_74782_a("Shot tag", this.shotTag);
        if (this.shooter != null) {
            tagCompound.func_186854_a("Shooter", this.shooter.getPersistentID());
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
            this.shooter = WorldHelper.getEntityByPersistentUUID(this.field_70170_p, shooterUUID);
        }
    }
}
