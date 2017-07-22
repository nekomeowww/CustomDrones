/*     */ package williamle.drones.worldgen;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.api.Filters.FilterReplaceable;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.api.helpers.WorldHelper;
/*     */ import williamle.drones.drone.DroneWeightedLists;
/*     */ import williamle.drones.drone.DroneWeightedLists.WeightedItemStack;
/*     */ import williamle.drones.entity.EntityDroneBaby;
/*     */ import williamle.drones.entity.EntityDroneBabyBig;
/*     */ 
/*     */ public class WorldGenDroneTower
/*     */   extends WorldGen
/*     */ {
/*     */   public static void genDroneTower(World world, int posX, int posZ)
/*     */   {
/*  28 */     BlockPos startPos = WorldHelper.getLowestAir(world, bp(posX, 0, posZ));
/*  29 */     if (startPos.func_177956_o() > 12)
/*     */     {
/*  31 */       IBlockState torch = Blocks.field_150478_aa.func_176223_P();
/*  32 */       WorldGen.GenBlockEntry blocks = blocksForBiome(world, startPos);
/*  33 */       int surfaceFloors = 3;
/*  34 */       int topDecorHeight = 5;
/*  35 */       int floorHeight = 8;
/*  36 */       int halfWidth = 8;
/*  37 */       int maxBulge = 3;
/*  38 */       int minY = 4;
/*  39 */       int groundY = startPos.func_177956_o();
/*  40 */       int maxY = groundY + floorHeight * (surfaceFloors + 1) - (startPos.func_177956_o() - minY) % floorHeight;
/*  41 */       int minX = posX - halfWidth;
/*  42 */       int maxX = posX + halfWidth;
/*  43 */       int minZ = posZ - halfWidth;
/*  44 */       int maxZ = posZ + halfWidth;
/*     */       
/*  46 */       int a = maxY - floorHeight; for (int floor = 0; a >= minY; a -= floorHeight)
/*     */       {
/*  48 */         floor++;
/*  49 */         int floorLevel = Math.max(floor - surfaceFloors, 0);
/*     */         
/*  51 */         int fminY = a;
/*  52 */         int fmaxY = a + floorHeight - 1;
/*  53 */         for (int b = 0; b < floorHeight; b++)
/*     */         {
/*  55 */           double bulgeLinearRate = 1.0D - Math.abs(b - floorHeight / 2.0D) / (floorHeight / 2.0D);
/*  56 */           int bulge = (int)Math.round(maxBulge * Math.pow(bulgeLinearRate, 2.0D));
/*  57 */           int tY = fminY + b;
/*  58 */           int tminX = minX - bulge;
/*  59 */           int tmaxX = maxX + bulge;
/*  60 */           int tminZ = minZ - bulge;
/*  61 */           int tmaxZ = maxZ + bulge;
/*  62 */           genBoxMargin(world, bp(tminX, tY, tminZ), bp(tmaxX, tY, tmaxZ), blocks.primary, Blocks.field_150350_a
/*  63 */             .func_176223_P(), 1, 0, 1, null, null);
/*  64 */           world.func_175656_a(bp(tminX, tY, tminZ), blocks.decor1);
/*  65 */           world.func_175656_a(bp(tmaxX, tY, tminZ), blocks.decor1);
/*  66 */           world.func_175656_a(bp(tmaxX, tY, tmaxZ), blocks.decor1);
/*  67 */           world.func_175656_a(bp(tminX, tY, tmaxZ), blocks.decor1);
/*  68 */           world.func_175656_a(bp(tminX, tY, posZ), blocks.decor2);
/*  69 */           world.func_175656_a(bp(tmaxX, tY, posZ), blocks.decor2);
/*  70 */           world.func_175656_a(bp(posX, tY, tminZ), blocks.decor2);
/*  71 */           world.func_175656_a(bp(posX, tY, tmaxZ), blocks.decor2);
/*  72 */           if (bulge == maxBulge)
/*     */           {
/*  74 */             world.func_175656_a(bp(tminX + 1, tY, tminZ + 1), Blocks.field_150478_aa.func_180642_a(world, 
/*  75 */               bp(tminX + 1, tY, tminZ + 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null));
/*  76 */             world.func_175656_a(bp(tmaxX - 1, tY, tminZ + 1), Blocks.field_150478_aa.func_180642_a(world, 
/*  77 */               bp(tmaxX - 1, tY, tminZ + 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null));
/*  78 */             world.func_175656_a(bp(tmaxX - 1, tY, tmaxZ - 1), Blocks.field_150478_aa.func_180642_a(world, 
/*  79 */               bp(tmaxX - 1, tY, tmaxZ - 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null));
/*  80 */             world.func_175656_a(bp(tminX + 1, tY, tmaxZ - 1), Blocks.field_150478_aa.func_180642_a(world, 
/*  81 */               bp(tminX + 1, tY, tmaxZ - 1), EnumFacing.UP, 0.0F, 0.0F, 0.0F, 0, null));
/*     */           }
/*     */         }
/*  84 */         genBox(world, bp(minX, fminY, minZ), bp(maxX, fminY, maxZ), blocks.secondary, new Filters.FilterReplaceable(true));
/*     */         
/*  86 */         boolean bigDrone = genDrones(world, bp(minX + 1, fminY + 1, minZ + 1), 
/*  87 */           bp(maxX - 1, fmaxY - 1, maxZ - 1), floorLevel);
/*  88 */         genChest(world, bp(posX, fminY + 1, posZ), generateLoots(floorLevel + (bigDrone ? 4 : 0)));
/*  89 */         world.func_175656_a(bp(posX + 1, fminY + 1, posZ), torch);
/*  90 */         world.func_175656_a(bp(posX - 1, fminY + 1, posZ), torch);
/*  91 */         world.func_175656_a(bp(posX, fminY + 1, posZ + 1), torch);
/*  92 */         world.func_175656_a(bp(posX, fminY + 1, posZ - 1), torch);
/*     */       }
/*  94 */       for (int a = 0; a < topDecorHeight; a++)
/*     */       {
/*  96 */         genBoxMargin(world, bp(minX - a, maxY + a, minZ - a), bp(maxX + a, maxY + a, maxZ + a), blocks.secondary, air, 1, 0, 1, null, new Filters.FilterReplaceable(true));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List<ItemStack> generateLoots(int floorLevel)
/*     */   {
/* 105 */     List<DroneWeightedLists.WeightedItemStack> loots = new ArrayList();
/* 106 */     loots.addAll(DroneWeightedLists.matsList);
/* 107 */     loots.addAll(DroneWeightedLists.partsList);
/* 108 */     if (floorLevel > 0) loots.addAll(DroneWeightedLists.bitsList);
/* 109 */     if (floorLevel > 1)
/*     */     {
/* 111 */       for (DroneWeightedLists.WeightedItemStack wis : DroneWeightedLists.modsList)
/*     */       {
/* 113 */         if (rnd.nextInt(4) == 0) loots.add(wis);
/*     */       }
/*     */     }
/* 116 */     if (floorLevel > 2)
/*     */     {
/* 118 */       for (DroneWeightedLists.WeightedItemStack wis : DroneWeightedLists.gunUpgradesList)
/*     */       {
/* 120 */         if (rnd.nextInt(3) == 0) { loots.add(wis);
/*     */         }
/*     */       }
/*     */     }
/* 124 */     Object list = new ArrayList();
/* 125 */     int count = Math.min(27, 4 + rnd.nextInt(3 + floorLevel));
/* 126 */     for (int a = 0; a < count; a++)
/*     */     {
/* 128 */       int randomCount = rnd.nextInt(floorLevel + 1);
/* 129 */       DroneWeightedLists.WeightedItemStack wis = (DroneWeightedLists.WeightedItemStack)WeightedRandom.func_76271_a(rnd, loots);
/* 130 */       for (int b = 0; b < randomCount; b++)
/*     */       {
/* 132 */         DroneWeightedLists.WeightedItemStack randomTo = (DroneWeightedLists.WeightedItemStack)WeightedRandom.func_76271_a(rnd, loots);
/* 133 */         if ((wis == null) || (randomTo.field_76292_a < wis.field_76292_a)) wis = randomTo;
/*     */       }
/* 135 */       if (wis != null)
/*     */       {
/* 137 */         ItemStack is = wis.is.func_77946_l();
/* 138 */         int minStackSize = 1;
/* 139 */         int maxStackSize = (int)Math.min(minStackSize * (
/* 140 */           Math.pow(floorLevel, 0.1D) + 1.0D) * Math.pow(wis.field_76292_a, 0.5D), is
/* 141 */           .func_77976_d());
/* 142 */         int stackSize = minStackSize + rnd.nextInt(maxStackSize - minStackSize + 1);
/* 143 */         is.field_77994_a = stackSize;
/* 144 */         ((List)list).add(is);
/*     */       }
/*     */     }
/* 147 */     return (List<ItemStack>)list;
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean genDrones(World world, BlockPos bp1, BlockPos bp2, int floorLevel)
/*     */   {
/* 153 */     BlockPos pos1 = new BlockPos(Math.min(bp1.func_177958_n(), bp2.func_177958_n()), Math.min(bp1.func_177956_o(), bp2.func_177956_o()), Math.min(bp1.func_177952_p(), bp2.func_177952_p()));
/*     */     
/* 155 */     BlockPos pos2 = new BlockPos(Math.max(bp1.func_177958_n(), bp2.func_177958_n()), Math.max(bp1.func_177956_o(), bp2.func_177956_o()), Math.max(bp1.func_177952_p(), bp2.func_177952_p()));
/* 156 */     int bigBabyChance = Math.max(5, 200 / (floorLevel + 1));
/* 157 */     Vec3d mid = VecHelper.getMid(new Vec3d[] { new Vec3d(pos1), new Vec3d(pos2) });
/* 158 */     double xDif = pos2.func_177958_n() - pos1.func_177958_n();
/* 159 */     double yDif = pos2.func_177956_o() - pos1.func_177956_o();
/* 160 */     double zDif = pos2.func_177952_p() - pos1.func_177952_p();
/* 161 */     if (rnd.nextInt(bigBabyChance) == 0)
/*     */     {
/* 163 */       EntityDroneBabyBig bigBaby = new EntityDroneBabyBig(world);
/* 164 */       bigBaby.func_70107_b(mid.field_72450_a, mid.field_72448_b, mid.field_72449_c);
/* 165 */       bigBaby.minionSpawnRange = Math.min(xDif / 2.0D, Math.min(yDif, zDif / 2.0D));
/* 166 */       bigBaby.hostile = true;
/* 167 */       bigBaby.shouldDespawn = false;
/* 168 */       bigBaby.field_98038_p = true;
/* 169 */       bigBaby.onInitSpawn();
/* 170 */       world.func_72838_d(bigBaby);
/* 171 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 175 */     int babyCount = 2 + rnd.nextInt(3 + floorLevel * 2);
/* 176 */     for (int a = 0; a < babyCount; a++)
/*     */     {
/* 178 */       EntityDroneBaby baby = new EntityDroneBaby(world);
/*     */       
/*     */ 
/* 181 */       Vec3d pos = new Vec3d(pos1.func_177958_n() + rnd.nextDouble() * (pos2.func_177958_n() - pos1.func_177958_n()), pos1.func_177956_o() + rnd.nextDouble() * (pos2.func_177956_o() - pos1.func_177956_o()), pos1.func_177952_p() + rnd.nextDouble() * (pos2.func_177952_p() - pos1.func_177952_p()));
/* 182 */       baby.func_70107_b(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c);
/* 183 */       baby.hostile = true;
/* 184 */       baby.shouldDespawn = false;
/* 185 */       baby.field_98038_p = true;
/* 186 */       baby.onInitSpawn();
/* 187 */       world.func_72838_d(baby);
/*     */     }
/* 189 */     return false;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\worldgen\WorldGenDroneTower.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */