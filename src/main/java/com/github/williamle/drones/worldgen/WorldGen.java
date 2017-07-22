/*     */ package williamle.drones.worldgen;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCactus;
/*     */ import net.minecraft.block.BlockChest;
/*     */ import net.minecraft.block.BlockColored;
/*     */ import net.minecraft.block.BlockDeadBush;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerColor;
/*     */ import net.minecraft.block.BlockFlower.EnumFlowerType;
/*     */ import net.minecraft.block.BlockLeaves;
/*     */ import net.minecraft.block.BlockOldLeaf;
/*     */ import net.minecraft.block.BlockOldLog;
/*     */ import net.minecraft.block.BlockPlanks;
/*     */ import net.minecraft.block.BlockPlanks.EnumType;
/*     */ import net.minecraft.block.BlockSand;
/*     */ import net.minecraft.block.BlockStaticLiquid;
/*     */ import net.minecraft.block.BlockTallGrass;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Biomes;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityChest;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.biome.Biome;
/*     */ import net.minecraft.world.biome.Biome.TempCategory;
/*     */ import net.minecraft.world.biome.BiomeBeach;
/*     */ import net.minecraft.world.biome.BiomeDesert;
/*     */ import net.minecraft.world.biome.BiomeForest;
/*     */ import net.minecraft.world.biome.BiomeHell;
/*     */ import net.minecraft.world.biome.BiomeJungle;
/*     */ import net.minecraft.world.biome.BiomeMesa;
/*     */ import net.minecraft.world.biome.BiomeOcean;
/*     */ import net.minecraft.world.biome.BiomeSavanna;
/*     */ import net.minecraft.world.biome.BiomeSnow;
/*     */ import net.minecraft.world.biome.BiomeSwamp;
/*     */ import net.minecraft.world.chunk.IChunkGenerator;
/*     */ import net.minecraft.world.chunk.IChunkProvider;
/*     */ import net.minecraft.world.gen.ChunkProviderHell;
/*     */ import net.minecraft.world.gen.ChunkProviderOverworld;
/*     */ import net.minecraft.world.gen.feature.WorldGenCanopyTree;
/*     */ import net.minecraftforge.common.config.Configuration;
/*     */ import net.minecraftforge.fml.common.IWorldGenerator;
/*     */ 
/*     */ public class WorldGen implements IWorldGenerator
/*     */ {
/*  54 */   public static int droneTowerRarity = 250;
/*  55 */   public static Random rnd = new Random();
/*  56 */   public static IBlockState air = Blocks.field_150350_a.func_176223_P();
/*     */   
/*     */   public static void syncConfig(Configuration config, String cat)
/*     */   {
/*  60 */     droneTowerRarity = config.getInt("Drone tower rarity", cat, 250, 1, 40000, "Bigger number = rarer.");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generate(Random rnd, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
/*     */   {
/*  67 */     int posX = chunkX * 16 + rnd.nextInt(16);
/*  68 */     int posZ = chunkZ * 16 + rnd.nextInt(16);
/*  69 */     if (((chunkGenerator instanceof ChunkProviderOverworld)) || ((chunkGenerator instanceof ChunkProviderHell)))
/*     */     {
/*  71 */       if (rnd.nextInt(droneTowerRarity) == 0)
/*     */       {
/*  73 */         WorldGenDroneTower.genDroneTower(world, posX, posZ);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void telePlayer(World world, BlockPos pos)
/*     */   {
/*  80 */     EntityPlayer p = world.func_184137_a(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), 512.0D, false);
/*  81 */     if ((p != null) && (p.func_184614_ca() != null) && (p.func_184614_ca().func_77973_b() == williamle.drones.DronesMod.droneFlyer))
/*     */     {
/*  83 */       p.func_70634_a(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
/*     */     }
/*     */   }
/*     */   
/*     */   public static BlockPos bp(int x, int y, int z)
/*     */   {
/*  89 */     return new BlockPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static void genChest(World world, BlockPos bp, List<ItemStack> items)
/*     */   {
/*  94 */     world.func_175656_a(bp, Blocks.field_150486_ae.func_176223_P());
/*     */     
/*  96 */     if ((world.func_175625_s(bp) instanceof TileEntityChest))
/*     */     {
/*  98 */       TileEntityChest tile = (TileEntityChest)world.func_175625_s(bp);
/*  99 */       List<Integer> empties = new ArrayList();
/* 100 */       for (int a = 0; a < tile.func_70302_i_(); a++)
/*     */       {
/* 102 */         empties.add(Integer.valueOf(a));
/*     */       }
/* 104 */       while ((!items.isEmpty()) && (!empties.isEmpty()))
/*     */       {
/* 106 */         Integer emptyIndex = (Integer)empties.get(rnd.nextInt(empties.size()));
/* 107 */         ItemStack is = (ItemStack)items.get(rnd.nextInt(items.size()));
/* 108 */         tile.func_70299_a(emptyIndex.intValue(), is.func_77946_l());
/* 109 */         items.remove(is);
/* 110 */         empties.remove(emptyIndex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void genBox(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs, Predicate filter)
/*     */   {
/* 122 */     int minX = Math.min(bp1.func_177958_n(), bp2.func_177958_n());
/* 123 */     int minY = Math.min(bp1.func_177956_o(), bp2.func_177956_o());
/* 124 */     int minZ = Math.min(bp1.func_177952_p(), bp2.func_177952_p());
/* 125 */     int maxX = Math.max(bp1.func_177958_n(), bp2.func_177958_n());
/* 126 */     int maxY = Math.max(bp1.func_177956_o(), bp2.func_177956_o());
/* 127 */     int maxZ = Math.max(bp1.func_177952_p(), bp2.func_177952_p());
/* 128 */     for (int x = minX; x <= maxX; x++)
/*     */     {
/* 130 */       for (int y = minY; y <= maxY; y++)
/*     */       {
/* 132 */         for (int z = minZ; z <= maxZ; z++)
/*     */         {
/* 134 */           BlockPos pos = new BlockPos(x, y, z);
/* 135 */           IBlockState state = world.func_180495_p(pos);
/* 136 */           if ((filter == null) || (filter.apply(state)))
/*     */           {
/* 138 */             world.func_175656_a(pos, ibs);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static void genBoxMargin(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs1, IBlockState ibs2, int xMargin, int yMargin, int zMargin, Predicate filter1, Predicate filter2)
/*     */   {
/* 148 */     int minX = Math.min(bp1.func_177958_n(), bp2.func_177958_n());
/* 149 */     int minY = Math.min(bp1.func_177956_o(), bp2.func_177956_o());
/* 150 */     int minZ = Math.min(bp1.func_177952_p(), bp2.func_177952_p());
/* 151 */     int maxX = Math.max(bp1.func_177958_n(), bp2.func_177958_n());
/* 152 */     int maxY = Math.max(bp1.func_177956_o(), bp2.func_177956_o());
/* 153 */     int maxZ = Math.max(bp1.func_177952_p(), bp2.func_177952_p());
/* 154 */     for (int x = minX; x <= maxX; x++)
/*     */     {
/* 156 */       for (int y = minY; y <= maxY; y++)
/*     */       {
/* 158 */         for (int z = minZ; z <= maxZ; z++)
/*     */         {
/* 160 */           BlockPos pos = new BlockPos(x, y, z);
/* 161 */           IBlockState state = world.func_180495_p(pos);
/* 162 */           if ((x < minX + xMargin) || (x > maxX - xMargin) || (y < minY + yMargin) || (y > maxY - yMargin) || (z < minZ + zMargin) || (z > maxZ - zMargin))
/*     */           {
/*     */ 
/* 165 */             if ((filter1 == null) || (filter1.apply(state)))
/*     */             {
/* 167 */               world.func_175656_a(pos, ibs1);
/*     */             }
/*     */             
/*     */ 
/*     */           }
/* 172 */           else if ((filter2 == null) || (filter2.apply(state)))
/*     */           {
/* 174 */             world.func_175656_a(pos, ibs2);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static GenBlockEntry blocksForBiome(World world, BlockPos bp)
/*     */   {
/* 184 */     Biome biome = world.getBiomeForCoordsBody(bp);
/* 185 */     IBlockState primary = Blocks.field_150348_b.func_176223_P();
/* 186 */     IBlockState secondary = Blocks.field_150346_d.func_176223_P();
/* 187 */     IBlockState decor1 = Blocks.field_150347_e.func_176223_P();
/* 188 */     IBlockState decor2 = Blocks.field_150322_A.func_176223_P();
/* 189 */     IBlockState plant1 = Blocks.field_150328_O.func_176223_P();
/* 190 */     IBlockState plant2 = Blocks.field_150327_N.func_176223_P();
/* 191 */     IBlockState liquid = Blocks.field_150355_j.func_176223_P();
/* 192 */     if ((biome instanceof net.minecraft.world.biome.BiomePlains))
/*     */     {
/* 194 */       decor1 = Blocks.field_150351_n.func_176223_P();
/* 195 */       plant1 = Blocks.field_150330_I.func_176223_P();
/* 196 */       plant2 = Blocks.field_150329_H.func_176223_P();
/*     */     }
/* 198 */     if ((biome instanceof BiomeMesa))
/*     */     {
/* 200 */       primary = Blocks.field_180395_cM.func_176223_P();
/* 201 */       secondary = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.SILVER);
/*     */       
/* 203 */       decor1 = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.ORANGE);
/*     */       
/* 205 */       decor2 = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.YELLOW);
/*     */       
/* 207 */       plant1 = Blocks.field_150434_aF.func_176223_P();
/* 208 */       plant2 = Blocks.field_150330_I.func_176223_P();
/*     */     }
/* 210 */     if ((biome instanceof BiomeOcean))
/*     */     {
/* 212 */       primary = Blocks.field_150435_aG.func_176223_P();
/* 213 */       secondary = Blocks.field_180395_cM.func_176223_P();
/* 214 */       decor1 = Blocks.field_180397_cI.func_176223_P();
/* 215 */       decor2 = Blocks.field_150360_v.func_176223_P();
/* 216 */       plant1 = Blocks.field_150350_a.func_176223_P();
/* 217 */       plant2 = Blocks.field_150350_a.func_176223_P();
/*     */     }
/* 219 */     if ((biome instanceof BiomeHell))
/*     */     {
/* 221 */       primary = Blocks.field_150424_aL.func_176223_P();
/* 222 */       secondary = Blocks.field_150385_bj.func_176223_P();
/* 223 */       decor1 = Blocks.field_150426_aN.func_176223_P();
/* 224 */       decor2 = Blocks.field_150425_aM.func_176223_P();
/* 225 */       plant1 = Blocks.field_150388_bm.func_176223_P();
/* 226 */       plant2 = Blocks.field_150388_bm.func_176223_P();
/*     */     }
/* 228 */     if (((biome instanceof BiomeDesert)) || ((biome instanceof BiomeBeach)))
/*     */     {
/* 230 */       primary = Blocks.field_150322_A.func_176223_P();
/* 231 */       secondary = Blocks.field_180395_cM.func_176223_P();
/* 232 */       decor1 = Blocks.field_150359_w.func_176223_P();
/* 233 */       decor2 = Blocks.field_150351_n.func_176223_P();
/* 234 */       plant1 = Blocks.field_150330_I.func_176223_P();
/* 235 */       plant2 = Blocks.field_150329_H.func_176223_P();
/*     */     }
/* 237 */     if (((biome instanceof net.minecraft.world.biome.BiomeHills)) || ((biome instanceof net.minecraft.world.biome.BiomeStoneBeach)))
/*     */     {
/* 239 */       secondary = Blocks.field_150417_aV.func_176223_P();
/* 240 */       decor2 = Blocks.field_150351_n.func_176223_P();
/* 241 */       plant1 = Blocks.field_150330_I.func_176223_P();
/* 242 */       plant2 = Blocks.field_150329_H.func_176223_P();
/*     */     }
/* 244 */     if (((biome instanceof BiomeForest)) || ((biome instanceof BiomeJungle)) || ((biome instanceof net.minecraft.world.biome.BiomeTaiga)) || ((biome instanceof BiomeSwamp)) || ((biome instanceof BiomeSavanna)))
/*     */     {
/*     */ 
/* 247 */       BlockPlanks.EnumType woodVariant = woodVariant(biome);
/* 248 */       IBlockState log = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, woodVariant);
/* 249 */       IBlockState plank = Blocks.field_150344_f.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, woodVariant);
/*     */       
/* 251 */       IBlockState leaf = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, woodVariant).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(false));
/* 252 */       BlockFlower.EnumFlowerType flower1 = biome.func_180623_a(rnd, bp);
/* 253 */       BlockFlower.EnumFlowerType flower2 = biome.func_180623_a(rnd, bp);
/* 254 */       BlockFlower.EnumFlowerColor flower1Color = flower1.func_176964_a();
/* 255 */       BlockFlower.EnumFlowerColor flower2Color = flower2.func_176964_a();
/*     */       
/* 257 */       primary = plank;
/* 258 */       secondary = log;
/* 259 */       decor1 = decor2 = leaf;
/* 260 */       plant1 = flower1Color.func_180346_a().func_176223_P().func_177226_a(flower1Color.func_180346_a().func_176494_l(), flower1);
/*     */       
/* 262 */       plant2 = flower2Color.func_180346_a().func_176223_P().func_177226_a(flower2Color.func_180346_a().func_176494_l(), flower2);
/*     */     }
/*     */     
/* 265 */     if (((biome instanceof BiomeSnow)) || (biome == Biomes.field_76776_l) || (biome == Biomes.field_76777_m) || 
/* 266 */       (biome.func_150561_m() == Biome.TempCategory.COLD))
/*     */     {
/* 268 */       primary = Blocks.field_150432_aD.func_176223_P();
/* 269 */       secondary = Blocks.field_150433_aE.func_176223_P();
/* 270 */       decor1 = Blocks.field_150359_w.func_176223_P();
/* 271 */       decor2 = Blocks.field_185778_de.func_176223_P();
/* 272 */       plant1 = plant2 = Blocks.field_150431_aC.func_176223_P();
/*     */     }
/* 274 */     return new GenBlockEntry(new IBlockState[] { primary, secondary, decor1, decor2, plant1, plant2, liquid });
/*     */   }
/*     */   
/*     */   public static BlockPlanks.EnumType woodVariant(Biome biome)
/*     */   {
/* 279 */     BlockPlanks.EnumType woodVariant = BlockPlanks.EnumType.SPRUCE;
/* 280 */     if (biome.func_185353_n() >= 0.5F)
/*     */     {
/* 282 */       woodVariant = rnd.nextInt(2) == 0 ? BlockPlanks.EnumType.BIRCH : BlockPlanks.EnumType.OAK;
/* 283 */       if ((biome instanceof BiomeSwamp)) woodVariant = BlockPlanks.EnumType.OAK;
/* 284 */       if (((biome instanceof BiomeForest)) && ((biome.func_150567_a(rnd) instanceof WorldGenCanopyTree)))
/* 285 */         woodVariant = BlockPlanks.EnumType.OAK;
/* 286 */       if ((biome instanceof BiomeJungle)) woodVariant = BlockPlanks.EnumType.JUNGLE;
/* 287 */       if ((biome instanceof BiomeSavanna)) woodVariant = BlockPlanks.EnumType.ACACIA;
/*     */     }
/* 289 */     return woodVariant;
/*     */   }
/*     */   
/*     */   public static class GenBlockEntry {
/*     */     public IBlockState primary;
/*     */     public IBlockState secondary;
/*     */     public IBlockState decor1;
/*     */     public IBlockState decor2;
/*     */     public IBlockState plant1;
/*     */     public IBlockState plant2;
/*     */     public IBlockState liquid;
/*     */     
/* 301 */     public GenBlockEntry(IBlockState... blockStates) { if (blockStates.length > 0) this.primary = blockStates[0];
/* 302 */       if (blockStates.length > 1) this.secondary = blockStates[1];
/* 303 */       if (blockStates.length > 2) this.decor1 = blockStates[2];
/* 304 */       if (blockStates.length > 3) this.decor2 = blockStates[3];
/* 305 */       if (blockStates.length > 4) this.plant1 = blockStates[4];
/* 306 */       if (blockStates.length > 5) this.plant2 = blockStates[5];
/* 307 */       if (blockStates.length > 6) { this.liquid = blockStates[6];
/*     */       }
/* 309 */       if (this.primary == null) this.primary = Blocks.field_150348_b.func_176223_P();
/* 310 */       if (this.secondary == null) this.secondary = Blocks.field_150346_d.func_176223_P();
/* 311 */       if (this.decor1 == null) this.decor1 = Blocks.field_150354_m.func_176223_P();
/* 312 */       if (this.decor2 == null) this.decor2 = Blocks.field_150322_A.func_176223_P();
/* 313 */       if (this.plant1 == null) this.plant1 = Blocks.field_150328_O.func_176223_P();
/* 314 */       if (this.plant2 == null) this.plant2 = Blocks.field_150327_N.func_176223_P();
/* 315 */       if (this.liquid == null) this.liquid = Blocks.field_150355_j.func_176223_P();
/*     */     }
/*     */     
/*     */     public GenBlockEntry(Block... blocks)
/*     */     {
/* 320 */       this(blocksToBlockStates(blocks));
/*     */     }
/*     */     
/*     */     public static IBlockState[] blocksToBlockStates(Block... blocks)
/*     */     {
/* 325 */       IBlockState[] ibss = new IBlockState[blocks.length];
/* 326 */       for (int a = 0; a < blocks.length; a++)
/*     */       {
/* 328 */         Block b = blocks[a];
/* 329 */         if (b != null) ibss[a] = b.func_176223_P();
/*     */       }
/* 331 */       return ibss;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\worldgen\WorldGen.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */