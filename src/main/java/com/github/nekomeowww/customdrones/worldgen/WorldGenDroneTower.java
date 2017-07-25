package com.github.nekomeowww.customdrones.worldgen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.api.Filters;
import com.github.nekomeowww.customdrones.api.Filters.FilterReplaceable;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;
import com.github.nekomeowww.customdrones.api.helpers.WorldHelper;
import com.github.nekomeowww.customdrones.drone.DroneWeightedLists;
import com.github.nekomeowww.customdrones.drone.DroneWeightedLists.WeightedItemStack;
import com.github.nekomeowww.customdrones.entity.EntityDroneBaby;
import com.github.nekomeowww.customdrones.entity.EntityDroneBabyBig;
import com.github.nekomeowww.customdrones.worldgen.WorldGen;

public class WorldGenDroneTower
        extends WorldGen
{
    public static void genDroneTower(World world, int posX, int posZ)
    {
        BlockPos startPos = WorldHelper.getLowestAir(world, bp(posX, 0, posZ));
        if (startPos.getY() > 12)
        {
            IBlockState torch = Blocks.TORCH.getDefaultState();
            WorldGen.GenBlockEntry blocks = blocksForBiome(world, startPos);
            int surfaceFloors = 3;
            int topDecorHeight = 5;
            int floorHeight = 8;
            int halfWidth = 8;
            int maxBulge = 3;
            int minY = 4;
            int groundY = startPos.getY();
            int maxY = groundY + floorHeight * (surfaceFloors + 1) - (startPos.getY() - minY) % floorHeight;
            int minX = posX - halfWidth;
            int maxX = posX + halfWidth;
            int minZ = posZ - halfWidth;
            int maxZ = posZ + halfWidth;

            int a = maxY - floorHeight;
            for (int floor = 0; a >= minY; a -= floorHeight)
            {
                floor++;
                int floorLevel = Math.max(floor - surfaceFloors, 0);

                int fminY = a;
                int fmaxY = a + floorHeight - 1;
                for (int b = 0; b < floorHeight; b++)
                {
                    double bulgeLinearRate = 1.0D - Math.abs(b - floorHeight / 2.0D) / (floorHeight / 2.0D);
                    int bulge = (int)Math.round(maxBulge * Math.pow(bulgeLinearRate, 2.0D));
                    int tY = fminY + b;
                    int tminX = minX - bulge;
                    int tmaxX = maxX + bulge;
                    int tminZ = minZ - bulge;
                    int tmaxZ = maxZ + bulge;
                    genBoxMargin(world, bp(tminX, tY, tminZ), bp(tmaxX, tY, tmaxZ), blocks.primary, Blocks.AIR
                            .getDefaultState(), 1, 0, 1, null, null);
                    world.setBlockState(bp(tminX, tY, tminZ), blocks.decor1);
                    world.setBlockState(bp(tmaxX, tY, tminZ), blocks.decor1);
                    world.setBlockState(bp(tmaxX, tY, tmaxZ), blocks.decor1);
                    world.setBlockState(bp(tminX, tY, tmaxZ), blocks.decor1);
                    world.setBlockState(bp(tminX, tY, posZ), blocks.decor2);
                    world.setBlockState(bp(tmaxX, tY, posZ), blocks.decor2);
                    world.setBlockState(bp(posX, tY, tminZ), blocks.decor2);
                    world.setBlockState(bp(posX, tY, tmaxZ), blocks.decor2);
                    if (bulge == maxBulge)
                    {
                        world.setBlockState(bp(tminX + 1, tY, tminZ + 1),
                                Blocks.TORCH.getStateForPlacement(world, bp(tminX + 1, tY, tminZ + 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null, null));
                                //Require:                           World,BlockPos,                             EnumFacing,         float,     float,      float,int,   EntityLivingBase,ItemStack
                        world.setBlockState(bp(tmaxX - 1, tY, tminZ + 1),
                                Blocks.TORCH.getStateForPlacement(world, bp(tmaxX - 1, tY, tminZ + 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null, null));
                        world.setBlockState(bp(tmaxX - 1, tY, tmaxZ - 1),
                                Blocks.TORCH.getStateForPlacement(world, bp(tmaxX - 1, tY, tmaxZ - 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null, null));
                        world.setBlockState(bp(tminX + 1, tY, tmaxZ - 1),
                                Blocks.TORCH.getStateForPlacement(world, bp(tminX + 1, tY, tmaxZ - 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null, null));
                    }
                }
                genBox(world, bp(minX, fminY, minZ), bp(maxX, fminY, maxZ), blocks.secondary, new Filters.FilterReplaceable(true));

                boolean bigDrone = genDrones(world, bp(minX + 1, fminY + 1, minZ + 1),
                        bp(maxX - 1, fmaxY - 1, maxZ - 1), floorLevel);
                genChest(world, bp(posX, fminY + 1, posZ), generateLoots(floorLevel + (bigDrone ? 4 : 0)));
                world.setBlockState(bp(posX + 1, fminY + 1, posZ), torch);
                world.setBlockState(bp(posX - 1, fminY + 1, posZ), torch);
                world.setBlockState(bp(posX, fminY + 1, posZ + 1), torch);
                world.setBlockState(bp(posX, fminY + 1, posZ - 1), torch);
            }
            for (int g = 0; g < topDecorHeight; g++) {
                genBoxMargin(world, bp(minX - g, maxY + g, minZ - g), bp(maxX + g, maxY + g, maxZ + g), blocks.secondary, air, 1, 0, 1, null, new Filters.FilterReplaceable(true));
            }
        }
    }

    public static List<ItemStack> generateLoots(int floorLevel)
    {
        List<DroneWeightedLists.WeightedItemStack> loots = new ArrayList();
        loots.addAll(DroneWeightedLists.matsList);
        loots.addAll(DroneWeightedLists.partsList);
        if (floorLevel > 0) {
            loots.addAll(DroneWeightedLists.bitsList);
        }
        if (floorLevel > 1) {
            for (DroneWeightedLists.WeightedItemStack wis : DroneWeightedLists.modsList) {
                if (rnd.nextInt(4) == 0) {
                    loots.add(wis);
                }
            }
        }
        if (floorLevel > 2) {
            for (DroneWeightedLists.WeightedItemStack wis : DroneWeightedLists.gunUpgradesList) {
                if (rnd.nextInt(3) == 0) {
                    loots.add(wis);
                }
            }
        }
        Object list = new ArrayList();
        int count = Math.min(27, 4 + rnd.nextInt(3 + floorLevel));
        for (int a = 0; a < count; a++)
        {
            int randomCount = rnd.nextInt(floorLevel + 1);
            DroneWeightedLists.WeightedItemStack wis = (DroneWeightedLists.WeightedItemStack)WeightedRandom.getRandomItem(rnd, loots);
            for (int b = 0; b < randomCount; b++)
            {
                DroneWeightedLists.WeightedItemStack randomTo = (DroneWeightedLists.WeightedItemStack)WeightedRandom.getRandomItem(rnd, loots);
                if ((wis == null) || (randomTo.itemWeight < wis.itemWeight)) {
                    wis = randomTo;
                }
            }
            if (wis != null)
            {
                ItemStack is = wis.is.copy();
                int minStackSize = 1;
                int maxStackSize = (int)Math.min(minStackSize * (
                        Math.pow(floorLevel, 0.1D) + 1.0D) * Math.pow(wis.itemWeight, 0.5D), is
                        .getMaxStackSize());
                int stackSize = minStackSize + rnd.nextInt(maxStackSize - minStackSize + 1);
                is.stackSize = stackSize;
                ((List)list).add(is);
            }
        }
        return (List<ItemStack>)list;
    }

    public static boolean genDrones(World world, BlockPos bp1, BlockPos bp2, int floorLevel)
    {
        BlockPos pos1 = new BlockPos(Math.min(bp1.getX(), bp2.getX()), Math.min(bp1.getY(), bp2.getY()), Math.min(bp1.getZ(), bp2.getZ()));

        BlockPos pos2 = new BlockPos(Math.max(bp1.getX(), bp2.getX()), Math.max(bp1.getY(), bp2.getY()), Math.max(bp1.getZ(), bp2.getZ()));
        int bigBabyChance = Math.max(5, 200 / (floorLevel + 1));
        Vec3d mid = VecHelper.getMid(new Vec3d[] { new Vec3d(pos1), new Vec3d(pos2) });
        double xDif = pos2.getX() - pos1.getX();
        double yDif = pos2.getY() - pos1.getY();
        double zDif = pos2.getZ() - pos1.getZ();
        if (rnd.nextInt(bigBabyChance) == 0)
        {
            EntityDroneBabyBig bigBaby = new EntityDroneBabyBig(world);
            bigBaby.setPosition(mid.xCoord, mid.yCoord, mid.zCoord);
            bigBaby.minionSpawnRange = Math.min(xDif / 2.0D, Math.min(yDif, zDif / 2.0D));
            bigBaby.hostile = true;
            bigBaby.shouldDespawn = false;
            bigBaby.forceSpawn = true;
            bigBaby.onInitSpawn();
            world.spawnEntityInWorld(bigBaby);
            return true;
        }
        int babyCount = 2 + rnd.nextInt(3 + floorLevel * 2);
        for (int a = 0; a < babyCount; a++)
        {
            EntityDroneBaby baby = new EntityDroneBaby(world);

            Vec3d pos = new Vec3d(pos1.getX() + rnd.nextDouble() * (pos2.getX() - pos1.getX()), pos1.getY() + rnd.nextDouble() * (pos2.getY() - pos1.getY()), pos1.getZ() + rnd.nextDouble() * (pos2.getZ() - pos1.getZ()));
            baby.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
            baby.hostile = true;
            baby.shouldDespawn = false;
            baby.forceSpawn = true;
            baby.onInitSpawn();
            world.spawnEntityInWorld(baby);
        }
        return false;
    }
}
