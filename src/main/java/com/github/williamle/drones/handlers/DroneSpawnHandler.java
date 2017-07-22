/*     */ package williamle.drones.handlers;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving.SpawnPlacementType;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldEntitySpawner;
/*     */ import net.minecraft.world.biome.Biome;
/*     */ import net.minecraft.world.biome.Biome.SpawnListEntry;
/*     */ import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
/*     */ import net.minecraftforge.event.terraingen.PopulateChunkEvent.Post;
/*     */ import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
/*     */ import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.entity.EntityDroneBaby;
/*     */ import williamle.drones.entity.EntityDroneBabyBig;
/*     */ import williamle.drones.entity.EntityDroneMob;
/*     */ import williamle.drones.entity.EntityDroneWildItem;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DroneSpawnHandler
/*     */ {
/*  34 */   public static Map<Biome, List<SpawnDroneEntry>> droneSpawnList = new HashMap();
/*  35 */   public static Biome[] allBiomes = (Biome[])Biome.field_150597_n.toArray(new Biome[0]);
/*     */   
/*     */   public static SpawnDroneEntry spawnEntryBaby;
/*     */   public static SpawnDroneEntry spawnEntryBigBaby;
/*     */   public static SpawnDroneEntry spawnEntryWildItem;
/*     */   
/*     */   public static void registerSpawns()
/*     */   {
/*  43 */     addDroneSpawn(null, 100, 1, 1, allBiomes);
/*  44 */     spawnEntryBaby = addDroneSpawn(EntityDroneBaby.class, 100, 1, 8, allBiomes);
/*  45 */     spawnEntryBigBaby = addDroneSpawn(EntityDroneBabyBig.class, 20, 1, 1, allBiomes);
/*  46 */     spawnEntryWildItem = addDroneSpawn(EntityDroneWildItem.class, 80, 1, 5, allBiomes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @SubscribeEvent
/*     */   public void potentialSpawn(WorldEvent.PotentialSpawns evn) {}
/*     */   
/*     */ 
/*     */   @SubscribeEvent
/*     */   public void populateChunkEventAnimal(PopulateChunkEvent.Populate evn) {}
/*     */   
/*     */ 
/*     */   @SubscribeEvent
/*     */   public void populateChunkEventPost(PopulateChunkEvent.Post evn)
/*     */   {
/*  62 */     int x = evn.getChunkX();
/*  63 */     int z = evn.getChunkZ();
/*  64 */     Biome biome = evn.getWorld().getBiomeForCoordsBody(new BlockPos(x * 16, 0, z * 16));
/*  65 */     spawnDronesInChunk(evn.getWorld(), biome, x * 16, z * 16, 16, 16, evn.getRand());
/*     */   }
/*     */   
/*     */ 
/*     */   public void spawnDronesInChunk(World worldIn, Biome biomeIn, int startX, int startZ, int rangeX, int rangeZ, Random randomIn)
/*     */   {
/*  71 */     List<SpawnDroneEntry> list = (List)droneSpawnList.get(biomeIn);
/*     */     
/*  73 */     if ((list != null) && (!list.isEmpty()))
/*     */     {
/*  75 */       while (randomIn.nextFloat() < biomeIn.func_76741_f())
/*     */       {
/*  77 */         SpawnDroneEntry spawnListEntry = (SpawnDroneEntry)WeightedRandom.func_76271_a(worldIn.field_73012_v, list);
/*  78 */         if (spawnListEntry.droneClass != null)
/*     */         {
/*     */ 
/*  81 */           int groupCount = spawnListEntry.field_76301_c + randomIn.nextInt(1 + spawnListEntry.field_76299_d - spawnListEntry.field_76301_c);
/*  82 */           int posX = startX + randomIn.nextInt(rangeX);
/*  83 */           int posZ = startZ + randomIn.nextInt(rangeZ);
/*  84 */           int nextPosX = posX;
/*  85 */           int nextPosZ = posZ;
/*     */           
/*  87 */           for (int groupCounter = 0; groupCounter < groupCount; groupCounter++)
/*     */           {
/*  89 */             boolean hasSpawned = false;
/*     */             
/*  91 */             for (int spawnCounter = 0; (!hasSpawned) && (spawnCounter < 4); spawnCounter++)
/*     */             {
/*  93 */               BlockPos blockpos = worldIn.func_175672_r(new BlockPos(posX, 0, posZ));
/*     */               
/*  95 */               if (WorldEntitySpawner.func_180267_a(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/*     */                 try
/*     */                 {
/*     */ 
/*     */ 
/* 104 */                   droneMob = (EntityDroneMob)spawnListEntry.droneClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
/*     */                 }
/*     */                 catch (Exception exception) {
/*     */                   EntityDroneMob droneMob;
/* 108 */                   exception.printStackTrace();
/* 109 */                   continue;
/*     */                 }
/*     */                 EntityDroneMob droneMob;
/* 112 */                 droneMob.func_70012_b(posX + 0.5F, blockpos.func_177956_o(), posZ + 0.5F, randomIn
/* 113 */                   .nextFloat() * 360.0F, 0.0F);
/* 114 */                 droneMob.onInitSpawn();
/* 115 */                 worldIn.func_72838_d(droneMob);
/* 116 */                 hasSpawned = true;
/*     */               }
/*     */               
/*     */ 
/*     */ 
/* 121 */               posX += randomIn.nextInt(5) - randomIn.nextInt(5);
/*     */               
/* 123 */               posZ += randomIn.nextInt(5) - randomIn.nextInt(5);
/* 125 */               for (; 
/* 125 */                   (posX < startX) || (posX >= startX + rangeX) || (posZ < startZ) || (posZ >= startZ + rangeX); 
/* 126 */                   posZ = nextPosZ + randomIn.nextInt(5) - randomIn.nextInt(5))
/*     */               {
/* 128 */                 posX = nextPosX + randomIn.nextInt(5) - randomIn.nextInt(5);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void teleClosestPlayer(World worldIn, Entity e)
/*     */   {
/* 139 */     EntityPlayer p = worldIn.func_72890_a(e, 512.0D);
/* 140 */     if ((p != null) && (p.func_184614_ca() != null) && (p.func_184614_ca().func_77973_b() == DronesMod.droneFlyer)) {
/* 141 */       p.func_70634_a(e.field_70165_t, e.field_70163_u + 1.0D, e.field_70161_v);
/*     */     }
/*     */   }
/*     */   
/*     */   public static SpawnDroneEntry addDroneSpawn(Class<? extends EntityDroneMob> entityClass, int weightedProb, int min, int max, Biome... biomes)
/*     */   {
/* 147 */     SpawnDroneEntry entry0 = new SpawnDroneEntry(entityClass, weightedProb, min, max);
/* 148 */     for (Biome biome : biomes)
/*     */     {
/* 150 */       if (!droneSpawnList.containsKey(biome)) droneSpawnList.put(biome, new ArrayList());
/* 151 */       List<SpawnDroneEntry> spawns = (List)droneSpawnList.get(biome);
/*     */       
/* 153 */       boolean found = false;
/* 154 */       for (SpawnDroneEntry entry : spawns)
/*     */       {
/* 156 */         if (entry.droneClass == entityClass)
/*     */         {
/* 158 */           entry.field_76292_a = weightedProb;
/* 159 */           entry.field_76301_c = min;
/* 160 */           entry.field_76299_d = max;
/* 161 */           found = true;
/* 162 */           entry0 = entry;
/* 163 */           break;
/*     */         }
/*     */       }
/*     */       
/* 167 */       if (!found)
/*     */       {
/* 169 */         spawns.add(entry0);
/*     */       }
/*     */     }
/* 172 */     return entry0;
/*     */   }
/*     */   
/*     */   public static class SpawnDroneEntry
/*     */     extends Biome.SpawnListEntry
/*     */   {
/*     */     public Class<? extends EntityDroneMob> droneClass;
/*     */     
/*     */     public SpawnDroneEntry(Class<? extends EntityDroneMob> entityclassIn, int weight, int groupCountMin, int groupCountMax)
/*     */     {
/* 182 */       super(weight, groupCountMin, groupCountMax);
/* 183 */       this.droneClass = entityclassIn;
/* 184 */       this.field_76300_b = entityclassIn;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\handlers\DroneSpawnHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */