package com.github.nekomeowww.customdrones.worldgen;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChest;
import net.minecraft.block.BlockColored;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockFlower.EnumFlowerColor;
import net.minecraft.block.BlockFlower.EnumFlowerType;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.TempCategory;
import net.minecraft.world.biome.BiomeBeach;
import net.minecraft.world.biome.BiomeDesert;
import net.minecraft.world.biome.BiomeForest;
import net.minecraft.world.biome.BiomeHell;
import net.minecraft.world.biome.BiomeHills;
import net.minecraft.world.biome.BiomeJungle;
import net.minecraft.world.biome.BiomeMesa;
import net.minecraft.world.biome.BiomeOcean;
import net.minecraft.world.biome.BiomePlains;
import net.minecraft.world.biome.BiomeSavanna;
import net.minecraft.world.biome.BiomeSnow;
import net.minecraft.world.biome.BiomeStoneBeach;
import net.minecraft.world.biome.BiomeSwamp;
import net.minecraft.world.biome.BiomeTaiga;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderHell;
import net.minecraft.world.gen.ChunkProviderOverworld;
import net.minecraft.world.gen.feature.WorldGenCanopyTree;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.IWorldGenerator;
import com.github.nekomeowww.customdrones.CustomDrones;

public class WorldGen
        implements IWorldGenerator
{
    public static int droneTowerRarity = 250;
    public static Random rnd = new Random();
    public static IBlockState air = Blocks.field_150350_a.func_176223_P();

    public static void syncConfig(Configuration config, String cat)
    {
        droneTowerRarity = config.getInt("Drone tower rarity", cat, 250, 1, 40000, "Bigger number = rarer.");
    }

    public void generate(Random rnd, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        int posX = chunkX * 16 + rnd.nextInt(16);
        int posZ = chunkZ * 16 + rnd.nextInt(16);
        if (((chunkGenerator instanceof ChunkProviderOverworld)) || ((chunkGenerator instanceof ChunkProviderHell))) {
            if (rnd.nextInt(droneTowerRarity) == 0) {
                WorldGenDroneTower.genDroneTower(world, posX, posZ);
            }
        }
    }

    public static void telePlayer(World world, BlockPos pos)
    {
        EntityPlayer p = world.func_184137_a(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), 512.0D, false);
        if ((p != null) && (p.func_184614_ca() != null) && (p.func_184614_ca().func_77973_b() == DronesMod.droneFlyer)) {
            p.func_70634_a(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
        }
    }

    public static BlockPos bp(int x, int y, int z)
    {
        return new BlockPos(x, y, z);
    }

    public static void genChest(World world, BlockPos bp, List<ItemStack> items)
    {
        world.func_175656_a(bp, Blocks.field_150486_ae.func_176223_P());
        if ((world.func_175625_s(bp) instanceof TileEntityChest))
        {
            TileEntityChest tile = (TileEntityChest)world.func_175625_s(bp);
            List<Integer> empties = new ArrayList();
            for (int a = 0; a < tile.func_70302_i_(); a++) {
                empties.add(Integer.valueOf(a));
            }
            while ((!items.isEmpty()) && (!empties.isEmpty()))
            {
                Integer emptyIndex = (Integer)empties.get(rnd.nextInt(empties.size()));
                ItemStack is = (ItemStack)items.get(rnd.nextInt(items.size()));
                tile.func_70299_a(emptyIndex.intValue(), is.func_77946_l());
                items.remove(is);
                empties.remove(emptyIndex);
            }
        }
    }

    public static void genBox(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs, Predicate filter)
    {
        int minX = Math.min(bp1.func_177958_n(), bp2.func_177958_n());
        int minY = Math.min(bp1.func_177956_o(), bp2.func_177956_o());
        int minZ = Math.min(bp1.func_177952_p(), bp2.func_177952_p());
        int maxX = Math.max(bp1.func_177958_n(), bp2.func_177958_n());
        int maxY = Math.max(bp1.func_177956_o(), bp2.func_177956_o());
        int maxZ = Math.max(bp1.func_177952_p(), bp2.func_177952_p());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.func_180495_p(pos);
                    if ((filter == null) || (filter.apply(state))) {
                        world.func_175656_a(pos, ibs);
                    }
                }
            }
        }
    }

    public static void genBoxMargin(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs1, IBlockState ibs2, int xMargin, int yMargin, int zMargin, Predicate filter1, Predicate filter2)
    {
        int minX = Math.min(bp1.func_177958_n(), bp2.func_177958_n());
        int minY = Math.min(bp1.func_177956_o(), bp2.func_177956_o());
        int minZ = Math.min(bp1.func_177952_p(), bp2.func_177952_p());
        int maxX = Math.max(bp1.func_177958_n(), bp2.func_177958_n());
        int maxY = Math.max(bp1.func_177956_o(), bp2.func_177956_o());
        int maxZ = Math.max(bp1.func_177952_p(), bp2.func_177952_p());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.func_180495_p(pos);
                    if ((x < minX + xMargin) || (x > maxX - xMargin) || (y < minY + yMargin) || (y > maxY - yMargin) || (z < minZ + zMargin) || (z > maxZ - zMargin))
                    {
                        if ((filter1 == null) || (filter1.apply(state))) {
                            world.func_175656_a(pos, ibs1);
                        }
                    }
                    else if ((filter2 == null) || (filter2.apply(state))) {
                        world.func_175656_a(pos, ibs2);
                    }
                }
            }
        }
    }

    public static GenBlockEntry blocksForBiome(World world, BlockPos bp)
    {
        Biome biome = world.getBiomeForCoordsBody(bp);
        IBlockState primary = Blocks.field_150348_b.func_176223_P();
        IBlockState secondary = Blocks.field_150346_d.func_176223_P();
        IBlockState decor1 = Blocks.field_150347_e.func_176223_P();
        IBlockState decor2 = Blocks.field_150322_A.func_176223_P();
        IBlockState plant1 = Blocks.field_150328_O.func_176223_P();
        IBlockState plant2 = Blocks.field_150327_N.func_176223_P();
        IBlockState liquid = Blocks.field_150355_j.func_176223_P();
        if ((biome instanceof BiomePlains))
        {
            decor1 = Blocks.field_150351_n.func_176223_P();
            plant1 = Blocks.field_150330_I.func_176223_P();
            plant2 = Blocks.field_150329_H.func_176223_P();
        }
        if ((biome instanceof BiomeMesa))
        {
            primary = Blocks.field_180395_cM.func_176223_P();
            secondary = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.SILVER);

            decor1 = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.ORANGE);

            decor2 = Blocks.field_150406_ce.func_176223_P().func_177226_a(BlockColored.field_176581_a, EnumDyeColor.YELLOW);

            plant1 = Blocks.field_150434_aF.func_176223_P();
            plant2 = Blocks.field_150330_I.func_176223_P();
        }
        if ((biome instanceof BiomeOcean))
        {
            primary = Blocks.field_150435_aG.func_176223_P();
            secondary = Blocks.field_180395_cM.func_176223_P();
            decor1 = Blocks.field_180397_cI.func_176223_P();
            decor2 = Blocks.field_150360_v.func_176223_P();
            plant1 = Blocks.field_150350_a.func_176223_P();
            plant2 = Blocks.field_150350_a.func_176223_P();
        }
        if ((biome instanceof BiomeHell))
        {
            primary = Blocks.field_150424_aL.func_176223_P();
            secondary = Blocks.field_150385_bj.func_176223_P();
            decor1 = Blocks.field_150426_aN.func_176223_P();
            decor2 = Blocks.field_150425_aM.func_176223_P();
            plant1 = Blocks.field_150388_bm.func_176223_P();
            plant2 = Blocks.field_150388_bm.func_176223_P();
        }
        if (((biome instanceof BiomeDesert)) || ((biome instanceof BiomeBeach)))
        {
            primary = Blocks.field_150322_A.func_176223_P();
            secondary = Blocks.field_180395_cM.func_176223_P();
            decor1 = Blocks.field_150359_w.func_176223_P();
            decor2 = Blocks.field_150351_n.func_176223_P();
            plant1 = Blocks.field_150330_I.func_176223_P();
            plant2 = Blocks.field_150329_H.func_176223_P();
        }
        if (((biome instanceof BiomeHills)) || ((biome instanceof BiomeStoneBeach)))
        {
            secondary = Blocks.field_150417_aV.func_176223_P();
            decor2 = Blocks.field_150351_n.func_176223_P();
            plant1 = Blocks.field_150330_I.func_176223_P();
            plant2 = Blocks.field_150329_H.func_176223_P();
        }
        if (((biome instanceof BiomeForest)) || ((biome instanceof BiomeJungle)) || ((biome instanceof BiomeTaiga)) || ((biome instanceof BiomeSwamp)) || ((biome instanceof BiomeSavanna)))
        {
            BlockPlanks.EnumType woodVariant = woodVariant(biome);
            IBlockState log = Blocks.field_150364_r.func_176223_P().func_177226_a(BlockOldLog.field_176301_b, woodVariant);
            IBlockState plank = Blocks.field_150344_f.func_176223_P().func_177226_a(BlockPlanks.field_176383_a, woodVariant);

            IBlockState leaf = Blocks.field_150362_t.func_176223_P().func_177226_a(BlockOldLeaf.field_176239_P, woodVariant).func_177226_a(BlockOldLeaf.field_176236_b, Boolean.valueOf(false));
            BlockFlower.EnumFlowerType flower1 = biome.func_180623_a(rnd, bp);
            BlockFlower.EnumFlowerType flower2 = biome.func_180623_a(rnd, bp);
            BlockFlower.EnumFlowerColor flower1Color = flower1.func_176964_a();
            BlockFlower.EnumFlowerColor flower2Color = flower2.func_176964_a();

            primary = plank;
            secondary = log;
            decor1 = decor2 = leaf;
            plant1 = flower1Color.func_180346_a().func_176223_P().func_177226_a(flower1Color.func_180346_a().func_176494_l(), flower1);

            plant2 = flower2Color.func_180346_a().func_176223_P().func_177226_a(flower2Color.func_180346_a().func_176494_l(), flower2);
        }
        if (((biome instanceof BiomeSnow)) || (biome == Biomes.field_76776_l) || (biome == Biomes.field_76777_m) ||
                (biome.func_150561_m() == Biome.TempCategory.COLD))
        {
            primary = Blocks.field_150432_aD.func_176223_P();
            secondary = Blocks.field_150433_aE.func_176223_P();
            decor1 = Blocks.field_150359_w.func_176223_P();
            decor2 = Blocks.field_185778_de.func_176223_P();
            plant1 = plant2 = Blocks.field_150431_aC.func_176223_P();
        }
        return new GenBlockEntry(new IBlockState[] { primary, secondary, decor1, decor2, plant1, plant2, liquid });
    }

    public static BlockPlanks.EnumType woodVariant(Biome biome)
    {
        BlockPlanks.EnumType woodVariant = BlockPlanks.EnumType.SPRUCE;
        if (biome.func_185353_n() >= 0.5F)
        {
            woodVariant = rnd.nextInt(2) == 0 ? BlockPlanks.EnumType.BIRCH : BlockPlanks.EnumType.OAK;
            if ((biome instanceof BiomeSwamp)) {
                woodVariant = BlockPlanks.EnumType.OAK;
            }
            if (((biome instanceof BiomeForest)) && ((biome.func_150567_a(rnd) instanceof WorldGenCanopyTree))) {
                woodVariant = BlockPlanks.EnumType.OAK;
            }
            if ((biome instanceof BiomeJungle)) {
                woodVariant = BlockPlanks.EnumType.JUNGLE;
            }
            if ((biome instanceof BiomeSavanna)) {
                woodVariant = BlockPlanks.EnumType.ACACIA;
            }
        }
        return woodVariant;
    }

    public static class GenBlockEntry
    {
        public IBlockState primary;
        public IBlockState secondary;
        public IBlockState decor1;
        public IBlockState decor2;
        public IBlockState plant1;
        public IBlockState plant2;
        public IBlockState liquid;

        public GenBlockEntry(IBlockState... blockStates)
        {
            if (blockStates.length > 0) {
                this.primary = blockStates[0];
            }
            if (blockStates.length > 1) {
                this.secondary = blockStates[1];
            }
            if (blockStates.length > 2) {
                this.decor1 = blockStates[2];
            }
            if (blockStates.length > 3) {
                this.decor2 = blockStates[3];
            }
            if (blockStates.length > 4) {
                this.plant1 = blockStates[4];
            }
            if (blockStates.length > 5) {
                this.plant2 = blockStates[5];
            }
            if (blockStates.length > 6) {
                this.liquid = blockStates[6];
            }
            if (this.primary == null) {
                this.primary = Blocks.field_150348_b.func_176223_P();
            }
            if (this.secondary == null) {
                this.secondary = Blocks.field_150346_d.func_176223_P();
            }
            if (this.decor1 == null) {
                this.decor1 = Blocks.field_150354_m.func_176223_P();
            }
            if (this.decor2 == null) {
                this.decor2 = Blocks.field_150322_A.func_176223_P();
            }
            if (this.plant1 == null) {
                this.plant1 = Blocks.field_150328_O.func_176223_P();
            }
            if (this.plant2 == null) {
                this.plant2 = Blocks.field_150327_N.func_176223_P();
            }
            if (this.liquid == null) {
                this.liquid = Blocks.field_150355_j.func_176223_P();
            }
        }

        public GenBlockEntry(Block... blocks)
        {
            this(blocksToBlockStates(blocks));
        }

        public static IBlockState[] blocksToBlockStates(Block... blocks)
        {
            IBlockState[] ibss = new IBlockState[blocks.length];
            for (int a = 0; a < blocks.length; a++)
            {
                Block b = blocks[a];
                if (b != null) {
                    ibss[a] = b.func_176223_P();
                }
            }
            return ibss;
        }
    }
}
