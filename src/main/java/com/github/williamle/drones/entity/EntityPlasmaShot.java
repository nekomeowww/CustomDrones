/*     */ package williamle.drones.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockDynamicLiquid;
/*     */ import net.minecraft.block.BlockFire;
/*     */ import net.minecraft.block.BlockLiquid;
/*     */ import net.minecraft.block.BlockStaticLiquid;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.crash.CrashReport;
/*     */ import net.minecraft.crash.CrashReportCategory;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldProvider;
/*     */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
/*     */ import williamle.drones.api.Filters.FilterExcepts;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.api.helpers.WorldHelper;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.item.ItemGunUpgrade.GunUpgrade;
/*     */ 
/*     */ public class EntityPlasmaShot extends Entity implements IProjectile, IEntityAdditionalSpawnData
/*     */ {
/*     */   public Color color;
/*     */   public Entity shooter;
/*     */   public double damage;
/*  46 */   public boolean homing = false;
/*     */   
/*     */   public Entity homingTarget;
/*  49 */   public NBTTagCompound shotTag = new NBTTagCompound();
/*     */   
/*     */   public EntityPlasmaShot(World worldIn)
/*     */   {
/*  53 */     super(worldIn);
/*  54 */     func_70105_a(0.1F, 0.1F);
/*  55 */     this.color = new Color(1.0D, 1.0D, 1.0D, 1.0D);
/*     */   }
/*     */   
/*     */   public void setHoming(Entity e)
/*     */   {
/*  60 */     if (e == null)
/*     */     {
/*  62 */       this.homing = false;
/*  63 */       this.homingTarget = null;
/*     */     }
/*     */     else
/*     */     {
/*  67 */       this.homing = true;
/*  68 */       this.homingTarget = e;
/*     */     }
/*     */   }
/*     */   
/*     */   public List<ItemGunUpgrade.GunUpgrade> getGunUpgrades()
/*     */   {
/*  74 */     return ItemGunUpgrade.GunUpgrade.getUpgrades(this.shotTag, "Gun Upgrades");
/*     */   }
/*     */   
/*     */   public void addUpgrade(ItemGunUpgrade.GunUpgrade gu)
/*     */   {
/*  79 */     List<ItemGunUpgrade.GunUpgrade> gus = getGunUpgrades();
/*  80 */     if (!gus.contains(gus))
/*     */     {
/*  82 */       gus.add(gu);
/*     */     }
/*  84 */     setUpgrades(gus);
/*     */   }
/*     */   
/*     */   public void removeUpgrade(ItemGunUpgrade.GunUpgrade gu)
/*     */   {
/*  89 */     List<ItemGunUpgrade.GunUpgrade> gus = getGunUpgrades();
/*  90 */     gus.remove(gu);
/*  91 */     setUpgrades(gus);
/*     */   }
/*     */   
/*     */   public void setUpgrades(List<ItemGunUpgrade.GunUpgrade> gus)
/*     */   {
/*  96 */     this.shotTag.func_74778_a("Gun Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(gus));
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_70112_a(double distance)
/*     */   {
/* 102 */     return distance <= 16384.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public AxisAlignedBB func_70046_E()
/*     */   {
/* 108 */     return func_174813_aQ();
/*     */   }
/*     */   
/*     */ 
/*     */   public AxisAlignedBB func_70114_g(Entity entityIn)
/*     */   {
/* 114 */     return func_174813_aQ();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_70067_L()
/*     */   {
/* 120 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_70104_M()
/*     */   {
/* 126 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canRiderInteract()
/*     */   {
/* 132 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_184220_m(Entity entityIn)
/*     */   {
/* 138 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public float func_70111_Y()
/*     */   {
/* 144 */     return 0.1F;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_90999_ad()
/*     */   {
/* 150 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70071_h_()
/*     */   {
/* 156 */     super.func_70071_h_();
/* 157 */     if ((this.homing) && (this.homingTarget != null))
/*     */     {
/* 159 */       double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y);
/* 160 */       Vec3d oldDir = new Vec3d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
/* 161 */       Vec3d dir = VecHelper.fromTo(func_174791_d(), EntityHelper.getCenterVec(this.homingTarget));
/* 162 */       dir = VecHelper.setLength(dir, speed / 2.0D);
/* 163 */       dir = VecHelper.setLength(oldDir.func_178787_e(dir), speed);
/* 164 */       this.field_70159_w = dir.field_72450_a;
/* 165 */       this.field_70181_x = dir.field_72448_b;
/* 166 */       this.field_70179_y = dir.field_72449_c;
/*     */     }
/* 168 */     func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
/* 169 */     collideWithNearbyEntities();
/* 170 */     if (((this.field_70122_E) || (!this.field_70132_H)) || 
/*     */     
/*     */ 
/*     */ 
/* 174 */       (this.field_70173_aa >= 100)) func_70106_y();
/* 175 */     if (this.homing) { this.field_70170_p.func_175688_a(EnumParticleTypes.CRIT_MAGIC, this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void func_70106_y()
/*     */   {
/* 181 */     if (!this.field_70128_L)
/*     */     {
/* 183 */       List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
/* 184 */       if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.explosion)) && (!this.field_70170_p.field_72995_K))
/*     */       {
/* 186 */         this.field_70170_p.func_72876_a(this.shooter != null ? this.shooter : this, this.field_70165_t, this.field_70163_u, this.field_70161_v, 
/* 187 */           (float)Math.abs(this.damage / 3.0D), true);
/*     */       }
/* 189 */       if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.scatter)) && (!this.field_70170_p.field_72995_K))
/*     */       {
/* 191 */         Filters.FilterExcepts filter = new Filters.FilterExcepts(new Object[] { this.shooter, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class, IProjectile.class });
/*     */         
/* 193 */         double range = 32.0D;
/* 194 */         for (int a = 0; a < (this.homing ? 4 : 6); a++)
/*     */         {
/*     */ 
/* 197 */           Vec3d dir = new Vec3d(this.field_70146_Z.nextDouble() - 0.5D, this.field_70146_Z.nextDouble() - 0.5D, this.field_70146_Z.nextDouble() - 0.5D).func_72432_b();
/* 198 */           EntityPlasmaShot babyShot = new EntityPlasmaShot(this.field_70170_p);
/* 199 */           babyShot.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
/* 200 */           babyShot.color = this.color;
/* 201 */           this.damage *= 0.5D;
/* 202 */           babyShot.shotTag = this.shotTag.func_74737_b();
/* 203 */           babyShot.removeUpgrade(ItemGunUpgrade.GunUpgrade.scatter);
/* 204 */           babyShot.shooter = (this.shooter != null ? this.shooter : this);
/* 205 */           babyShot.homing = this.homing;
/* 206 */           if (this.homing)
/*     */           {
/* 208 */             babyShot.homingTarget = WorldHelper.getEntityBestInAngle(this.field_70170_p, func_174791_d(), dir, 
/* 209 */               func_174813_aQ().func_186662_g(range), 90.0D, this.homingTarget, filter);
/*     */           }
/* 211 */           double speed = Math.sqrt(this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y) * 0.5D;
/* 212 */           Vec3d velocity = new Vec3d(dir.field_72450_a, 1.0D, dir.field_72449_c).func_72432_b().func_186678_a(speed);
/* 213 */           babyShot.field_70159_w = velocity.field_72450_a;
/* 214 */           babyShot.field_70181_x = velocity.field_72448_b;
/* 215 */           babyShot.field_70179_y = velocity.field_72449_c;
/* 216 */           this.field_70170_p.func_72838_d(babyShot);
/*     */         }
/*     */       }
/*     */     }
/* 220 */     super.func_70106_y();
/*     */   }
/*     */   
/*     */   public void collideWithNearbyEntities()
/*     */   {
/* 225 */     Filters.FilterExcepts excepts = new Filters.FilterExcepts(new Object[] { this, EntityPlasmaShot.class, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class });
/*     */     
/* 227 */     List<Entity> list = this.field_70170_p.func_175674_a(this, 
/* 228 */       func_174813_aQ().func_186662_g(func_70111_Y()), excepts);
/* 229 */     if (!list.isEmpty())
/*     */     {
/* 231 */       for (Entity entity : list)
/*     */       {
/* 233 */         collideWith(entity);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void collideWith(Entity entityIn)
/*     */   {
/* 240 */     List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
/* 241 */     boolean canHeal = (this.damage < 0.0D) && ((entityIn instanceof EntityLivingBase));
/* 242 */     boolean canHarm = !shouldNotHarmEntity(entityIn);
/* 243 */     if (canHeal) { ((EntityLivingBase)entityIn).func_70691_i((float)this.damage);
/* 244 */     } else if (canHarm) entityIn.func_70097_a(DamageSource.func_92087_a(this.shooter), (float)this.damage);
/* 245 */     if (canHarm)
/*     */     {
/* 247 */       if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire)) entityIn.func_70015_d((int)this.damage);
/* 248 */       if ((upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) && ((entityIn instanceof EntityLivingBase)))
/* 249 */         ((EntityLivingBase)entityIn).func_70690_d(new PotionEffect(
/* 250 */           net.minecraft.potion.Potion.func_180142_b("slowness"), (int)(this.damage * 20.0D)));
/*     */     }
/* 252 */     if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice)) entityIn.func_70066_B();
/* 253 */     if (!this.field_70170_p.field_72995_K) func_70106_y();
/*     */   }
/*     */   
/*     */   public boolean shouldNotHarmEntity(Entity e)
/*     */   {
/* 258 */     if (e.field_70128_L) return true;
/* 259 */     if (((this.shooter instanceof EntityDrone)) && (e == ((EntityDrone)this.shooter).getControllingPlayer())) return true;
/* 260 */     if (e == this.shooter) return true;
/* 261 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_145775_I()
/*     */   {
/* 267 */     super.func_145775_I();
/* 268 */     AxisAlignedBB axisalignedbb = func_174813_aQ().func_186662_g(func_70111_Y());
/*     */     
/* 270 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72340_a + 0.001D, axisalignedbb.field_72338_b + 0.001D, axisalignedbb.field_72339_c + 0.001D);
/*     */     
/* 272 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72336_d - 0.001D, axisalignedbb.field_72337_e - 0.001D, axisalignedbb.field_72334_f - 0.001D);
/* 273 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.func_185346_s();
/*     */     
/* 275 */     if (this.field_70170_p.func_175707_a(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1))
/*     */     {
/* 277 */       for (int i = blockpos$pooledmutableblockpos.func_177958_n(); i <= blockpos$pooledmutableblockpos1.func_177958_n(); i++)
/*     */       {
/* 279 */         for (int j = blockpos$pooledmutableblockpos.func_177956_o(); j <= blockpos$pooledmutableblockpos1.func_177956_o(); j++)
/*     */         {
/* 281 */           for (int k = blockpos$pooledmutableblockpos.func_177952_p(); 
/* 282 */               k <= blockpos$pooledmutableblockpos1.func_177952_p(); k++)
/*     */           {
/* 284 */             blockpos$pooledmutableblockpos2.func_181079_c(i, j, k);
/* 285 */             IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos$pooledmutableblockpos2);
/*     */             
/*     */             try
/*     */             {
/* 289 */               Block b = iblockstate.func_177230_c();
/* 290 */               if (b.func_176209_a(iblockstate, true))
/*     */               {
/* 292 */                 collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
/*     */               }
/*     */             }
/*     */             catch (Throwable throwable)
/*     */             {
/* 297 */               CrashReport crashreport = CrashReport.func_85055_a(throwable, "Colliding entity with block");
/*     */               
/*     */ 
/* 300 */               CrashReportCategory crashreportcategory = crashreport.func_85058_a("Block being collided with");
/* 301 */               CrashReportCategory.func_175750_a(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
/*     */               
/* 303 */               throw new net.minecraft.util.ReportedException(crashreport);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void collideWithBlock(BlockPos pos, IBlockState state)
/*     */   {
/* 313 */     pos = pos.func_185334_h();
/* 314 */     Block block = state.func_177230_c();
/* 315 */     BlockPos posUp = pos.func_177984_a().func_185334_h();
/* 316 */     IBlockState stateUp = this.field_70170_p.func_180495_p(posUp);
/* 317 */     Block blockUp = stateUp.func_177230_c();
/* 318 */     boolean isSolid = (!(block instanceof BlockLiquid)) && (!block.func_176200_f(this.field_70170_p, pos));
/* 319 */     boolean toDie = isSolid;
/* 320 */     List<ItemGunUpgrade.GunUpgrade> upgrades = getGunUpgrades();
/* 321 */     if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice))
/*     */     {
/* 323 */       if ((block == Blocks.field_150355_j) || (block == Blocks.field_150358_i)) {
/* 324 */         this.field_70170_p.func_175656_a(pos, Blocks.field_150432_aD.func_176223_P());
/* 325 */       } else if ((block == Blocks.field_150353_l) || (block == Blocks.field_150356_k)) {
/* 326 */         this.field_70170_p.func_175656_a(pos, Blocks.field_150347_e.func_176223_P());
/* 327 */       } else if (block == Blocks.field_150480_ab) { this.field_70170_p.func_175698_g(pos);
/* 328 */       } else if (blockUp == Blocks.field_150480_ab) { this.field_70170_p.func_175698_g(posUp);
/* 329 */       } else if ((blockUp == Blocks.field_150350_a) && (Blocks.field_150431_aC.func_176196_c(this.field_70170_p, posUp)))
/* 330 */         this.field_70170_p.func_175656_a(posUp, Blocks.field_150431_aC.func_176223_P());
/* 331 */       if ((block instanceof BlockLiquid)) toDie = true;
/*     */     }
/* 333 */     if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire))
/*     */     {
/* 335 */       if (block == Blocks.field_150432_aD)
/*     */       {
/* 337 */         if (this.field_70170_p.field_73011_w.func_177500_n())
/*     */         {
/* 339 */           this.field_70170_p.func_175698_g(pos);
/*     */         }
/*     */         else
/*     */         {
/* 343 */           Material material = this.field_70170_p.func_180495_p(pos.func_177977_b()).func_185904_a();
/* 344 */           if ((material.func_76230_c()) || (material.func_76224_d()))
/*     */           {
/* 346 */             this.field_70170_p.func_175656_a(pos, Blocks.field_150358_i.func_176223_P());
/*     */           }
/*     */           else
/*     */           {
/* 350 */             this.field_70170_p.func_175656_a(pos, Blocks.field_150355_j.func_176223_P());
/* 351 */             this.field_70170_p.func_180496_d(pos, Blocks.field_150355_j);
/*     */           }
/*     */         }
/*     */       }
/* 355 */       else if ((block == Blocks.field_150433_aE) || (block == Blocks.field_150431_aC))
/*     */       {
/* 357 */         this.field_70170_p.func_175698_g(pos);
/*     */       }
/* 359 */       else if ((blockUp == Blocks.field_150350_a) && (Blocks.field_150480_ab.func_176196_c(this.field_70170_p, posUp)))
/* 360 */         this.field_70170_p.func_175656_a(posUp, Blocks.field_150480_ab.func_176223_P());
/*     */     }
/* 362 */     if (toDie) { func_70106_y();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_70186_c(double x, double y, double z, float velocity, float inaccuracy) {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void func_70088_a() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void func_70037_a(NBTTagCompound tagCompound)
/*     */   {
/* 378 */     this.color = new Color(tagCompound.func_74762_e("Color"));
/* 379 */     this.damage = tagCompound.func_74769_h("Damage");
/* 380 */     this.homing = tagCompound.func_74767_n("Homing");
/* 381 */     this.shotTag = tagCompound.func_74775_l("Shot tag");
/* 382 */     if (tagCompound.func_186855_b("Shooter"))
/*     */     {
/* 384 */       UUID shooterUUID = tagCompound.func_186857_a("Shooter");
/* 385 */       this.shooter = WorldHelper.getEntityByPersistentUUID(this.field_70170_p, shooterUUID);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_70014_b(NBTTagCompound tagCompound)
/*     */   {
/* 392 */     tagCompound.func_74768_a("Color", this.color.toInt());
/* 393 */     tagCompound.func_74780_a("Damage", this.damage);
/* 394 */     tagCompound.func_74757_a("Homing", this.homing);
/* 395 */     tagCompound.func_74782_a("Shot tag", this.shotTag);
/* 396 */     if (this.shooter != null) { tagCompound.func_186854_a("Shooter", this.shooter.getPersistentID());
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeSpawnData(ByteBuf buffer)
/*     */   {
/* 402 */     buffer.writeInt(this.color.toInt());
/* 403 */     buffer.writeDouble(this.damage);
/* 404 */     buffer.writeBoolean(this.homing);
/* 405 */     ByteBufUtils.writeTag(buffer, this.shotTag);
/* 406 */     buffer.writeBoolean(this.shooter != null);
/* 407 */     buffer.writeLong(this.shooter != null ? this.shooter.getPersistentID().getMostSignificantBits() : 0L);
/* 408 */     buffer.writeLong(this.shooter != null ? this.shooter.getPersistentID().getLeastSignificantBits() : 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   public void readSpawnData(ByteBuf buffer)
/*     */   {
/* 414 */     this.color = new Color(buffer.readInt());
/* 415 */     this.damage = buffer.readDouble();
/* 416 */     this.homing = buffer.readBoolean();
/* 417 */     this.shotTag = ByteBufUtils.readTag(buffer);
/* 418 */     boolean hasShooter = buffer.readBoolean();
/* 419 */     long most = buffer.readLong();
/* 420 */     long least = buffer.readLong();
/* 421 */     if (hasShooter)
/*     */     {
/* 423 */       UUID shooterUUID = new UUID(most, least);
/* 424 */       this.shooter = WorldHelper.getEntityByPersistentUUID(this.field_70170_p, shooterUUID);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityPlasmaShot.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */