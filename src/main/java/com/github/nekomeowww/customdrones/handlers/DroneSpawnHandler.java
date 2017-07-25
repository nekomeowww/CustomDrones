package com.github.nekomeowww.customdrones.handlers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.github.nekomeowww.customdrones.entity.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Populate;
import net.minecraftforge.event.terraingen.PopulateChunkEvent.Post;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.event.world.WorldEvent.PotentialSpawns;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.nekomeowww.customdrones.CustomDrones;

public class DroneSpawnHandler
{
    public static Map<Biome, List<SpawnDroneEntry>> droneSpawnList = new HashMap();
    public static Biome[] allBiomes = (Biome[])Biome.EXPLORATION_BIOMES_LIST.toArray(new Biome[0]);
    public static SpawnDroneEntry spawnEntryBaby;
    public static SpawnDroneEntry spawnEntryBigBaby;
    public static SpawnDroneEntry spawnEntryWildItem;

    public static void registerSpawns()
    {
        addDroneSpawn(null, 100, 1, 1, allBiomes);
        spawnEntryBaby = addDroneSpawn(EntityDroneBaby.class, 100, 1, 8, allBiomes);
        spawnEntryBigBaby = addDroneSpawn(EntityDroneBabyBig.class, 20, 1, 1, allBiomes);
        spawnEntryWildItem = addDroneSpawn(EntityDroneWildItem.class, 80, 1, 5, allBiomes);
    }

    @SubscribeEvent
    public void potentialSpawn(WorldEvent.PotentialSpawns evn) {}

    @SubscribeEvent
    public void populateChunkEventAnimal(PopulateChunkEvent.Populate evn) {}

    @SubscribeEvent
    public void populateChunkEventPost(PopulateChunkEvent.Post evn)
    {
        int x = evn.getChunkX();
        int z = evn.getChunkZ();
        Biome biome = evn.getWorld().getBiomeForCoordsBody(new BlockPos(x * 16, 0, z * 16));
        spawnDronesInChunk(evn.getWorld(), biome, x * 16, z * 16, 16, 16, evn.getRand());
    }

    public void spawnDronesInChunk(World worldIn, Biome biomeIn, int startX, int startZ, int rangeX, int rangeZ, Random randomIn)
    {
        List<SpawnDroneEntry> list = (List)droneSpawnList.get(biomeIn);
        if ((list != null) && (!list.isEmpty())) {
            while (randomIn.nextFloat() < biomeIn.getSpawningChance())
            {
                SpawnDroneEntry spawnListEntry = (SpawnDroneEntry)WeightedRandom.getRandomItem(worldIn.rand, list);
                if (spawnListEntry.droneClass != null)
                {
                    int groupCount = spawnListEntry.minGroupCount + randomIn.nextInt(1 + spawnListEntry.maxGroupCount - spawnListEntry.minGroupCount);
                    int posX = startX + randomIn.nextInt(rangeX);
                    int posZ = startZ + randomIn.nextInt(rangeZ);
                    int nextPosX = posX;
                    int nextPosZ = posZ;
                    for (int groupCounter = 0; groupCounter < groupCount; groupCounter++)
                    {
                        boolean hasSpawned = false;
                        for (int spawnCounter = 0; (!hasSpawned) && (spawnCounter < 4); spawnCounter++)
                        {
                            BlockPos blockpos = worldIn.getTopSolidOrLiquidBlock(new BlockPos(posX, 0, posZ));
                            if (WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, worldIn, blockpos))
                            {
                                try
                                {
                                    EntityDroneMob droneMob = (EntityDroneMob)spawnListEntry.droneClass.getConstructor(new Class[] { World.class }).newInstance(new Object[] { worldIn });
                                }
                                catch (Exception exception)
                                {
                                    EntityDroneMob droneMob;
                                    exception.printStackTrace();
                                    continue;
                                }
                                EntityDroneMob droneMob = null;
                                droneMob.setLocationAndAngles(posX + 0.5F, blockpos.getY(), posZ + 0.5F, randomIn
                                        .nextFloat() * 360.0F, 0.0F);
                                droneMob.onInitSpawn();
                                worldIn.spawnEntityInWorld(droneMob);
                                hasSpawned = true;
                            }
                            posX += randomIn.nextInt(5) - randomIn.nextInt(5);

                            posZ += randomIn.nextInt(5) - randomIn.nextInt(5);
                            for (; (posX < startX) || (posX >= startX + rangeX) || (posZ < startZ) || (posZ >= startZ + rangeX); posZ = nextPosZ + randomIn.nextInt(5) - randomIn.nextInt(5)) {
                                posX = nextPosX + randomIn.nextInt(5) - randomIn.nextInt(5);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void teleClosestPlayer(World worldIn, Entity e)
    {
        EntityPlayer p = worldIn.getClosestPlayerToEntity(e, 512.0D);
        if ((p != null) && (p.getHeldItemMainhand() != null) && (p.getHeldItemMainhand().getItem() == CustomDrones.droneFlyer)) {
            p.setPositionAndUpdate(e.posX, e.posY + 1.0D, e.posZ);
        }
    }

    public static SpawnDroneEntry addDroneSpawn(Class<? extends EntityDroneMob> entityClass, int weightedProb, int min, int max, Biome... biomes)
    {
        SpawnDroneEntry entry0 = new SpawnDroneEntry(entityClass, weightedProb, min, max);
        for (Biome biome : biomes)
        {
            if (!droneSpawnList.containsKey(biome)) {
                droneSpawnList.put(biome, new ArrayList());
            }
            List<SpawnDroneEntry> spawns = (List)droneSpawnList.get(biome);

            boolean found = false;
            for (SpawnDroneEntry entry : spawns) {
                if (entry.droneClass == entityClass)
                {
                    entry.itemWeight = weightedProb;
                    entry.minGroupCount = min;
                    entry.maxGroupCount = max;
                    found = true;
                    entry0 = entry;
                    break;
                }
            }
            if (!found) {
                spawns.add(entry0);
            }
        }
        return entry0;
    }

    public static class SpawnDroneEntry
            extends Biome.SpawnListEntry
    {
        public Class<? extends EntityDroneMob> droneClass;

        public SpawnDroneEntry(Class<? extends EntityDroneMob> entityclassIn, int weight, int groupCountMin, int groupCountMax)
        {
            super(entityclassIn, weight, groupCountMin, groupCountMax);
            this.droneClass = entityclassIn;
            this.entityClass = entityclassIn;
        }
    }
}
