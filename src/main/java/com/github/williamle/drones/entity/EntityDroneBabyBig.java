/*     */ package williamle.drones.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ 
/*     */ 
/*     */ public class EntityDroneBabyBig
/*     */   extends EntityDroneBaby
/*     */ {
/*  16 */   public double minionSpawnRange = 16.0D;
/*     */   
/*     */   public EntityDroneBabyBig(World worldIn)
/*     */   {
/*  20 */     super(worldIn);
/*  21 */     func_70105_a(1.75F, 2.5F);
/*  22 */     this.modelScale = 3.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInitSpawn()
/*     */   {
/*  28 */     super.onInitSpawn();
/*  29 */     spawnMinions(this.minionSpawnRange);
/*     */   }
/*     */   
/*     */   public void spawnMinions(double range)
/*     */   {
/*  34 */     if (range <= 0.0D) return;
/*  35 */     int i = addUpParts();
/*  36 */     int count = 4 + i / 2 + this.field_70146_Z.nextInt(i);
/*  37 */     for (int a = 0; a < count; a++)
/*     */     {
/*  39 */       for (int tryTime = 0; tryTime < 20; tryTime++)
/*     */       {
/*  41 */         double x = this.field_70165_t + (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D * range;
/*  42 */         double y = this.field_70163_u + (this.field_70146_Z.nextDouble() - 0.5D) * range;
/*  43 */         double z = this.field_70161_v + (this.field_70146_Z.nextDouble() - 0.5D) * 2.0D * range;
/*  44 */         Vec3d vec = new Vec3d(x, y, z);
/*  45 */         if (this.field_70170_p.func_147447_a(EntityHelper.getEyeVec(this), vec, false, true, false) == null)
/*     */         {
/*  47 */           EntityDroneBaby baby = new EntityDroneBaby(this.field_70170_p);
/*  48 */           baby.func_70107_b(x, y, z);
/*  49 */           baby.hostile = this.hostile;
/*  50 */           baby.shouldDespawn = this.shouldDespawn;
/*  51 */           baby.onInitSpawn();
/*     */           
/*     */ 
/*  54 */           DroneInfo di = new DroneInfo(baby, this.field_70146_Z.nextInt(this.droneInfo.chip) + 1, this.field_70146_Z.nextInt(this.droneInfo.core) + 1, this.field_70146_Z.nextInt(this.droneInfo.casing) + 1, this.field_70146_Z.nextInt(this.droneInfo.engine) + 1);
/*  55 */           this.field_70170_p.func_72838_d(baby);
/*  56 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double getBaseAttack()
/*     */   {
/*  65 */     return 5.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getBaseHealth()
/*     */   {
/*  71 */     return 100.0D;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getHerdIndividualWeight()
/*     */   {
/*  77 */     return 30;
/*     */   }
/*     */   
/*     */ 
/*     */   public double getSpeedMultiplication()
/*     */   {
/*  83 */     return Math.sqrt(super.getSpeedMultiplication()) * 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addDropsOnDeath(List<ItemStack> list)
/*     */   {
/*  89 */     super.addDropsOnDeath(list);
/*  90 */     int i = addUpParts();
/*  91 */     list.add(new ItemStack(DronesMod.droneBit, i * 2));
/*     */     
/*  93 */     int i2 = 0;
/*  94 */     for (int a = 0; a < i; a++)
/*     */     {
/*  96 */       if (this.field_70146_Z.nextInt(4) == 0) i2++;
/*     */     }
/*  98 */     if (i2 > 0) list.add(new ItemStack(DronesMod.droneBit, i2, 1));
/*  99 */     if (this.field_70146_Z.nextInt(4) == 0) list.add(new ItemStack(getPart("casing", this.droneInfo.casing)));
/* 100 */     if (this.field_70146_Z.nextInt(12) == 0) list.add(new ItemStack(getPart("chip", this.droneInfo.chip)));
/* 101 */     if (this.field_70146_Z.nextInt(12) == 0) list.add(new ItemStack(getPart("core", this.droneInfo.core)));
/* 102 */     if (this.field_70146_Z.nextInt(3) == 0) { list.add(new ItemStack(getPart("engine", this.droneInfo.engine)));
/*     */     }
/*     */   }
/*     */   
/*     */   public int getXPOnDeath()
/*     */   {
/* 108 */     return addUpParts() * addUpParts();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_184222_aU()
/*     */   {
/* 114 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityDroneBabyBig.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */