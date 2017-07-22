/*     */ package williamle.drones.entity;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.IMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.Biome;
/*     */ import net.minecraft.world.biome.BiomeBeach;
/*     */ import net.minecraft.world.biome.BiomeDesert;
/*     */ import net.minecraft.world.biome.BiomeForest;
/*     */ import net.minecraft.world.biome.BiomeHell;
/*     */ import net.minecraft.world.biome.BiomeHills;
/*     */ import net.minecraft.world.biome.BiomeJungle;
/*     */ import net.minecraft.world.biome.BiomeMesa;
/*     */ import net.minecraft.world.biome.BiomeOcean;
/*     */ import net.minecraft.world.biome.BiomePlains;
/*     */ import net.minecraft.world.biome.BiomeRiver;
/*     */ import net.minecraft.world.biome.BiomeSavanna;
/*     */ import net.minecraft.world.biome.BiomeSnow;
/*     */ import net.minecraft.world.biome.BiomeSwamp;
/*     */ import net.minecraft.world.biome.BiomeTaiga;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.drone.DroneAppearance;
/*     */ import williamle.drones.drone.DroneAppearance.ColorPalette;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.item.ItemDronePart;
/*     */ 
/*     */ public abstract class EntityDroneMob extends EntityDrone implements IMob
/*     */ {
/*  43 */   public int MOBID = 55537;
/*     */   public EntityAITasks droneTasks;
/*     */   public double modelScale;
/*     */   public Entity field_110150_bn;
/*     */   public boolean hostile;
/*     */   public boolean shouldDespawn;
/*     */   
/*     */   public EntityDroneMob(World worldIn)
/*     */   {
/*  52 */     super(worldIn);
/*  53 */     this.modelScale = 1.0D;
/*  54 */     this.droneTasks = new EntityAITasks((worldIn != null) && (worldIn.field_72984_F != null) ? worldIn.field_72984_F : null);
/*  55 */     if ((worldIn != null) && (!worldIn.field_72995_K))
/*     */     {
/*  57 */       initDroneAI();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean func_70692_ba()
/*     */   {
/*  64 */     return this.shouldDespawn;
/*     */   }
/*     */   
/*     */ 
/*     */   public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData livingdata)
/*     */   {
/*  70 */     onInitSpawn();
/*  71 */     return super.func_180482_a(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   public abstract void initDroneAI();
/*     */   
/*     */   public abstract void initDroneAIPostSpawn();
/*     */   
/*     */   public void onInitSpawn()
/*     */   {
/*  80 */     initDroneInfo();
/*  81 */     initDroneAIPostSpawn();
/*     */   }
/*     */   
/*     */   public void initDroneInfo()
/*     */   {
/*  86 */     this.droneInfo.droneFreq = this.MOBID;
/*  87 */     initSpawnSetAppearance();
/*  88 */     initSpawnAddModules();
/*     */   }
/*     */   
/*     */   public void initSpawnSetAppearance()
/*     */   {
/*  93 */     setAppearanceBasedOnBiome();
/*     */   }
/*     */   
/*     */   public abstract void initSpawnAddModules();
/*     */   
/*     */   public void randomizeDroneParts()
/*     */   {
/* 100 */     double log2 = Math.log(2.0D);
/* 101 */     this.droneInfo.casing = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
/* 102 */     this.droneInfo.chip = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
/* 103 */     this.droneInfo.core = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
/* 104 */     this.droneInfo.engine = (4 - (int)Math.floor(Math.log(this.field_70146_Z.nextInt(15) + 1) / log2));
/*     */   }
/*     */   
/*     */   public void setAppearanceBasedOnBiome()
/*     */   {
/* 109 */     Biome biome = this.field_70170_p.getBiomeForCoordsBody(func_180425_c());
/* 110 */     if (((biome instanceof BiomeSnow)) || (this.field_70170_p.canSnowAtBody(func_180425_c(), false)))
/*     */     {
/* 112 */       this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Ice glacier"));
/*     */     }
/* 114 */     else if (((biome instanceof BiomeHell)) || ((biome instanceof BiomeMesa)))
/*     */     {
/* 116 */       this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sunset"));
/*     */     }
/* 118 */     else if (((biome instanceof BiomeBeach)) || ((biome instanceof BiomeDesert)))
/*     */     {
/* 120 */       this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sandy desert"));
/*     */     }
/* 122 */     else if (((biome instanceof BiomeRiver)) || ((biome instanceof BiomeOcean)))
/*     */     {
/* 124 */       this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Sea waves"));
/*     */     }
/* 126 */     else if (((biome instanceof BiomeForest)) || ((biome instanceof BiomeHills)) || ((biome instanceof BiomeJungle)) || ((biome instanceof BiomePlains)) || ((biome instanceof BiomeTaiga)) || ((biome instanceof BiomeSwamp)) || ((biome instanceof BiomeSavanna)))
/*     */     {
/*     */ 
/*     */ 
/* 130 */       this.droneInfo.appearance.palette.setPalette(DroneAppearance.getPalette("Grass roots"));
/*     */     }
/*     */     else
/*     */     {
/* 134 */       setDefaultAppearance();
/*     */     }
/* 136 */     Color c = new Color(0.8D, 0.8D, 0.8D, 1.0D);
/* 137 */     switch (this.droneInfo.core)
/*     */     {
/*     */     case 2: 
/* 140 */       c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
/* 141 */       break;
/*     */     case 3: 
/* 143 */       c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
/* 144 */       break;
/*     */     case 4: 
/* 146 */       c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
/*     */     }
/*     */     
/* 149 */     this.droneInfo.appearance.palette.setPaletteColor("Core", c.copy());
/* 150 */     c = new Color(0.8D, 0.8D, 0.8D, 1.0D);
/* 151 */     switch (this.droneInfo.engine)
/*     */     {
/*     */     case 2: 
/* 154 */       c = new Color(1.0D, 1.0D, 0.5D, 1.0D);
/* 155 */       break;
/*     */     case 3: 
/* 157 */       c = new Color(0.6D, 1.0D, 0.9D, 1.0D);
/* 158 */       break;
/*     */     case 4: 
/* 160 */       c = new Color(0.6D, 1.0D, 0.6D, 1.0D);
/*     */     }
/*     */     
/* 163 */     this.droneInfo.appearance.palette.setPaletteColor("Wing", c.copy());
/*     */   }
/*     */   
/*     */   public void setDefaultAppearance()
/*     */   {
/* 168 */     this.droneInfo.appearance.palette = DroneAppearance.ColorPalette.fastMake("Mob", Integer.valueOf(-10066330));
/*     */   }
/*     */   
/*     */   public int getHerdIndividualWeight()
/*     */   {
/* 173 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean usefulInteraction(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand, boolean hasController)
/*     */   {
/* 179 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70030_z()
/*     */   {
/* 185 */     super.func_70030_z();
/* 186 */     this.droneTasks.func_75774_a();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_70097_a(DamageSource source, float amount)
/*     */   {
/* 192 */     if (source != null) this.field_110150_bn = source.func_76346_g();
/* 193 */     return super.func_70097_a(source, amount);
/*     */   }
/*     */   
/*     */ 
/*     */   public float func_70047_e()
/*     */   {
/* 199 */     return this.field_70131_O / 2.0F;
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFlyingMode()
/*     */   {
/* 205 */     return this.MOBID;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70106_y()
/*     */   {
/* 211 */     super.func_70106_y();
/* 212 */     if ((!this.field_70170_p.field_72995_K) && (this.field_70170_p.func_82736_K().func_82766_b("doMobLoot")))
/*     */     {
/* 214 */       List<ItemStack> itemsToDrop = new ArrayList();
/* 215 */       addDropsOnDeath(itemsToDrop);
/* 216 */       for (ItemStack is : itemsToDrop)
/*     */       {
/* 218 */         func_70099_a(is, this.field_70146_Z.nextFloat() * this.field_70131_O);
/*     */       }
/*     */       
/* 221 */       if (((this.field_110150_bn instanceof EntityPlayer)) || (((this.field_110150_bn instanceof EntityDrone)) && 
/* 222 */         (((EntityDrone)this.field_110150_bn).getControllingPlayer() != null)))
/*     */       {
/* 224 */         int i = getXPOnDeath();
/* 225 */         while (i > 0)
/*     */         {
/* 227 */           int j = EntityXPOrb.func_70527_a(i);
/* 228 */           i -= j;
/* 229 */           this.field_70170_p.func_72838_d(new EntityXPOrb(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, j));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public abstract void addDropsOnDeath(List<ItemStack> paramList);
/*     */   
/*     */   public abstract int getXPOnDeath();
/*     */   
/*     */   public int addUpParts()
/*     */   {
/* 241 */     return this.droneInfo.casing + this.droneInfo.chip + this.droneInfo.core + this.droneInfo.engine;
/*     */   }
/*     */   
/*     */   public static ItemDronePart getPart(String s, int i)
/*     */   {
/* 246 */     if (s.equalsIgnoreCase("casing"))
/*     */     {
/* 248 */       switch (i)
/*     */       {
/*     */       case 1: 
/* 251 */         return DronesMod.case1;
/*     */       case 2: 
/* 253 */         return DronesMod.case2;
/*     */       case 3: 
/* 255 */         return DronesMod.case3;
/*     */       case 4: 
/* 257 */         return DronesMod.case4;
/*     */       }
/*     */       
/* 260 */     } else if (s.equalsIgnoreCase("chip"))
/*     */     {
/* 262 */       switch (i)
/*     */       {
/*     */       case 1: 
/* 265 */         return DronesMod.chip1;
/*     */       case 2: 
/* 267 */         return DronesMod.chip2;
/*     */       case 3: 
/* 269 */         return DronesMod.chip3;
/*     */       case 4: 
/* 271 */         return DronesMod.chip4;
/*     */       }
/*     */       
/* 274 */     } else if (s.equalsIgnoreCase("core"))
/*     */     {
/* 276 */       switch (i)
/*     */       {
/*     */       case 1: 
/* 279 */         return DronesMod.core1;
/*     */       case 2: 
/* 281 */         return DronesMod.core2;
/*     */       case 3: 
/* 283 */         return DronesMod.core3;
/*     */       case 4: 
/* 285 */         return DronesMod.core4;
/*     */       }
/*     */       
/* 288 */     } else if (s.equalsIgnoreCase("engine"))
/*     */     {
/* 290 */       switch (i)
/*     */       {
/*     */       case 1: 
/* 293 */         return DronesMod.engine1;
/*     */       case 2: 
/* 295 */         return DronesMod.engine2;
/*     */       case 3: 
/* 297 */         return DronesMod.engine3;
/*     */       case 4: 
/* 299 */         return DronesMod.engine4;
/*     */       }
/*     */     }
/* 302 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70014_b(NBTTagCompound tagCompound)
/*     */   {
/* 308 */     super.func_70014_b(tagCompound);
/* 309 */     tagCompound.func_74757_a("Hostile", this.hostile);
/* 310 */     tagCompound.func_74757_a("Despawn", this.shouldDespawn);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70037_a(NBTTagCompound tagCompound)
/*     */   {
/* 316 */     super.func_70037_a(tagCompound);
/* 317 */     this.hostile = tagCompound.func_74767_n("Hostile");
/* 318 */     this.shouldDespawn = tagCompound.func_74767_n("Despawn");
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityDroneMob.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */