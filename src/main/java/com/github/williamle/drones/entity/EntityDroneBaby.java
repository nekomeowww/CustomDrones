/*     */ package williamle.drones.entity;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.ai.DroneAIAttackMelee;
/*     */ import williamle.drones.entity.ai.DroneAITargetPlayer;
/*     */ import williamle.drones.entity.ai.DroneAIWanderHerd;
/*     */ import williamle.drones.render.DroneModels;
/*     */ import williamle.drones.render.DroneModels.ModelProp;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityDroneBaby
/*     */   extends EntityDroneMob
/*     */ {
/*     */   public EntityDroneBaby(World worldIn)
/*     */   {
/*  40 */     super(worldIn);
/*  41 */     func_70105_a(0.3F, 0.4F);
/*  42 */     this.modelScale = 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initDroneAI()
/*     */   {
/*  48 */     this.droneTasks.func_75776_a(7, new DroneAITargetPlayer(this, 12.0D));
/*  49 */     this.droneTasks.func_75776_a(8, new DroneAIAttackMelee(this));
/*  50 */     this.droneTasks.func_75776_a(10, new DroneAIWanderHerd(this, 16.0D, 0.5D, 16.0D, 5, new Class[] { getClass() }));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initDroneAIPostSpawn() {}
/*     */   
/*     */ 
/*     */   public void initDroneInfo()
/*     */   {
/*  60 */     if ((this.droneInfo.casing == 1) && (this.droneInfo.chip == 1) && (this.droneInfo.core == 1) && (this.droneInfo.engine == 1))
/*     */     {
/*  62 */       randomizeDroneParts();
/*     */     }
/*  64 */     super.initDroneInfo();
/*     */   }
/*     */   
/*     */   public void initSpawnSetAppearance()
/*     */   {
/*  69 */     this.droneInfo.appearance.modelID = DroneModels.instance.baby.id;
/*  70 */     super.initSpawnSetAppearance();
/*     */   }
/*     */   
/*     */   public void initSpawnAddModules()
/*     */   {
/*  75 */     this.droneInfo.mods.clear();
/*  76 */     this.droneInfo.applyModule(Module.getModule("Battery Saving " + DroneInfo.greekNumber[this.droneInfo.chip]));
/*  77 */     this.droneInfo.applyModule(Module.getModule("Weapon " + DroneInfo.greekNumber[this.droneInfo.core]));
/*  78 */     this.droneInfo.applyModule(Module.getModule("Armor " + DroneInfo.greekNumber[this.droneInfo.casing]));
/*  79 */     this.droneInfo.applyModule(Module.multiMovement);
/*  80 */     this.droneInfo.switchModule(this, this.droneInfo.getModuleWithFunctionOf(Module.weapon1), false);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_70097_a(DamageSource source, float amount)
/*     */   {
/*  86 */     if (source.func_76346_g() != null)
/*     */     {
/*  88 */       if ((getDroneAttackTarget() == null) || ((!(getDroneAttackTarget() instanceof EntityPlayer)) && 
/*  89 */         ((source.func_76346_g() instanceof EntityPlayer))))
/*     */       {
/*  91 */         setDroneAttackTarget(source.func_76346_g(), true);
/*     */       }
/*     */       else
/*     */       {
/*  95 */         setDroneAttackTarget(source.func_76346_g(), false);
/*     */       }
/*  97 */       callNearbyBabiesToAttack(source.func_76346_g(), 16.0D);
/*     */     }
/*  99 */     return super.func_70097_a(source, amount);
/*     */   }
/*     */   
/*     */   public void callNearbyBabiesToAttack(Entity e, double range)
/*     */   {
/* 104 */     List<EntityDroneBaby> mobs = this.field_70170_p.func_72872_a(EntityDroneBaby.class, 
/* 105 */       func_174813_aQ().func_186662_g(range));
/* 106 */     mobs.remove(this);
/* 107 */     for (EntityDroneBaby mob : mobs)
/*     */     {
/* 109 */       if ((mob.getDroneAttackTarget() == null) || (
/* 110 */         (!(mob.getDroneAttackTarget() instanceof EntityPlayer)) && ((e instanceof EntityPlayer))))
/*     */       {
/* 112 */         mob.setDroneAttackTarget(e, true);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void addDropsOnDeath(List<ItemStack> list)
/*     */   {
/* 120 */     int chanceCount = addUpParts();
/* 121 */     int i = this.field_70146_Z.nextInt(chanceCount * 2);
/* 122 */     if (i > 0) { list.add(new ItemStack(DronesMod.droneBit, i));
/*     */     }
/* 124 */     if (this.field_70146_Z.nextInt(Math.max(100 - chanceCount, 1)) == 0) list.add(new ItemStack(DronesMod.droneBit, 1, 1));
/* 125 */     if (this.field_70146_Z.nextInt(20) == 0) list.add(new ItemStack(getPart("casing", this.droneInfo.casing)));
/* 126 */     if (this.field_70146_Z.nextInt(60) == 0) list.add(new ItemStack(getPart("chip", this.droneInfo.chip)));
/* 127 */     if (this.field_70146_Z.nextInt(60) == 0) list.add(new ItemStack(getPart("core", this.droneInfo.core)));
/* 128 */     if (this.field_70146_Z.nextInt(15) == 0) { list.add(new ItemStack(getPart("engine", this.droneInfo.engine)));
/*     */     }
/*     */   }
/*     */   
/*     */   public int getXPOnDeath()
/*     */   {
/* 134 */     return addUpParts();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean shouldAttackDrone(EntityDrone e)
/*     */   {
/* 140 */     return (super.shouldAttackDrone(e)) && (!(e instanceof EntityDroneBaby));
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70037_a(NBTTagCompound tagCompound)
/*     */   {
/* 146 */     super.func_70037_a(tagCompound);
/* 147 */     this.droneInfo.appearance.modelID = DroneModels.instance.baby.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityDroneBaby.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */