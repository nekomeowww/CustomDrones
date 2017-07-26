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
    public static IBlockState air = Blocks.AIR.getDefaultState();

    public static void syncConfig(Configuration config, String cat)
    {
        droneTowerRarity = config.getInt("Drone tower rarity", cat, 250, 1, 40000, "Bigger number = rarer.");
    }

    public void generate(Random rnd, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider)
    {
        //Debug
        //System.out.println("Generator is ok");
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
        EntityPlayer p = world.getClosestPlayer(pos.getX(), pos.getY(), pos.getZ(), 512.0D, false);
        if ((p != null) && (p.getHeldItemMainhand() != null) && (p.getHeldItemMainhand().getItem() == CustomDrones.droneFlyer)) {
            p.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        }
    }

    public static BlockPos bp(int x, int y, int z)
    {
        return new BlockPos(x, y, z);
    }

    public static void genChest(World world, BlockPos bp, List<ItemStack> items)
    {
        world.setBlockState(bp, Blocks.CHEST.getDefaultState());
        if ((world.getTileEntity(bp) instanceof TileEntityChest))
        {
            TileEntityChest tile = (TileEntityChest)world.getTileEntity(bp);
            List<Integer> empties = new ArrayList();
            for (int a = 0; a < tile.getSizeInventory(); a++) {
                empties.add(Integer.valueOf(a));
            }
            while ((!items.isEmpty()) && (!empties.isEmpty()))
            {
                Integer emptyIndex = (Integer)empties.get(rnd.nextInt(empties.size()));
                ItemStack is = (ItemStack)items.get(rnd.nextInt(items.size()));
                tile.setInventorySlotContents(emptyIndex.intValue(), is.copy());
                items.remove(is);
                empties.remove(emptyIndex);
            }
        }
    }

    public static void genBox(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs, Predicate filter)
    {
        int minX = Math.min(bp1.getX(), bp2.getX());
        int minY = Math.min(bp1.getY(), bp2.getY());
        int minZ = Math.min(bp1.getZ(), bp2.getZ());
        int maxX = Math.max(bp1.getX(), bp2.getX());
        int maxY = Math.max(bp1.getY(), bp2.getY());
        int maxZ = Math.max(bp1.getZ(), bp2.getZ());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    if ((filter == null) || (filter.apply(state))) {
                        world.setBlockState(pos, ibs);
                    }
                }
            }
        }
    }

    public static void genBoxMargin(World world, BlockPos bp1, BlockPos bp2, IBlockState ibs1, IBlockState ibs2, int xMargin, int yMargin, int zMargin, Predicate filter1, Predicate filter2)
    {
        int minX = Math.min(bp1.getX(), bp2.getX());
        int minY = Math.min(bp1.getY(), bp2.getY());
        int minZ = Math.min(bp1.getZ(), bp2.getZ());
        int maxX = Math.max(bp1.getX(), bp2.getX());
        int maxY = Math.max(bp1.getY(), bp2.getY());
        int maxZ = Math.max(bp1.getZ(), bp2.getZ());
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++)
                {
                    BlockPos pos = new BlockPos(x, y, z);
                    IBlockState state = world.getBlockState(pos);
                    if ((x < minX + xMargin) || (x > maxX - xMargin) || (y < minY + yMargin) || (y > maxY - yMargin) || (z < minZ + zMargin) || (z > maxZ - zMargin))
                    {
                        if ((filter1 == null) || (filter1.apply(state))) {
                            world.setBlockState(pos, ibs1);
                        }
                    }
                    else if ((filter2 == null) || (filter2.apply(state))) {
                        world.setBlockState(pos, ibs2);
                    }
                }
            }
        }
    }

    public static GenBlockEntry blocksForBiome(World world, BlockPos bp)
    {
        Biome biome = world.getBiomeForCoordsBody(bp);
        IBlockState primary = Blocks.STONE.getDefaultState();
        IBlockState secondary = Blocks.DIRT.getDefaultState();
        IBlockState decor1 = Blocks.COBBLESTONE.getDefaultState();
        IBlockState decor2 = Blocks.SANDSTONE.getDefaultState();
        IBlockState plant1 = Blocks.RED_FLOWER.getDefaultState();
        IBlockState plant2 = Blocks.YELLOW_FLOWER.getDefaultState();
        IBlockState liquid = Blocks.WATER.getDefaultState();
        if ((biome instanceof BiomePlains))
        {
            decor1 = Blocks.GRAVEL.getDefaultState();
            plant1 = Blocks.DEADBUSH.getDefaultState();
            plant2 = Blocks.TALLGRASS.getDefaultState();
        }
        if ((biome instanceof BiomeMesa))
        {
            primary = Blocks.RED_SANDSTONE.getDefaultState();
            secondary = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.SILVER);

            decor1 = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.ORANGE);

            decor2 = Blocks.STAINED_HARDENED_CLAY.getDefaultState().withProperty(BlockColored.COLOR, EnumDyeColor.YELLOW);

            plant1 = Blocks.CACTUS.getDefaultState();
            plant2 = Blocks.DEADBUSH.getDefaultState();
        }
        if ((biome instanceof BiomeOcean))
        {
            primary = Blocks.CLAY.getDefaultState();
            secondary = Blocks.RED_SANDSTONE.getDefaultState();
            decor1 = Blocks.PRISMARINE.getDefaultState();
            decor2 = Blocks.SPONGE.getDefaultState();
            plant1 = Blocks.AIR.getDefaultState();
            plant2 = Blocks.AIR.getDefaultState();
        }
        if ((biome instanceof BiomeHell))
        {
            primary = Blocks.NETHERRACK.getDefaultState();
            secondary = Blocks.NETHER_BRICK.getDefaultState();
            decor1 = Blocks.GLOWSTONE.getDefaultState();
            decor2 = Blocks.SOUL_SAND.getDefaultState();
            plant1 = Blocks.NETHER_WART.getDefaultState();
            plant2 = Blocks.NETHER_WART.getDefaultState();
        }
        if (((biome instanceof BiomeDesert)) || ((biome instanceof BiomeBeach)))
        {
            primary = Blocks.SANDSTONE.getDefaultState();
            secondary = Blocks.RED_SANDSTONE.getDefaultState();
            decor1 = Blocks.GLASS.getDefaultState();
            decor2 = Blocks.GRAVEL.getDefaultState();
            plant1 = Blocks.DEADBUSH.getDefaultState();
            plant2 = Blocks.TALLGRASS.getDefaultState();
        }
        if (((biome instanceof BiomeHills)) || ((biome instanceof BiomeStoneBeach)))
        {
            secondary = Blocks.STONEBRICK.getDefaultState();
            decor2 = Blocks.GRAVEL.getDefaultState();
            plant1 = Blocks.DEADBUSH.getDefaultState();
            plant2 = Blocks.TALLGRASS.getDefaultState();
        }
        if (((biome instanceof BiomeForest)) || ((biome instanceof BiomeJungle)) || ((biome instanceof BiomeTaiga)) || ((biome instanceof BiomeSwamp)) || ((biome instanceof BiomeSavanna)))
        {
            BlockPlanks.EnumType woodVariant = woodVariant(biome);
            IBlockState log = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, woodVariant);
            IBlockState plank = Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, woodVariant);

            IBlockState leaf = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, woodVariant).withProperty(BlockOldLeaf.CHECK_DECAY, Boolean.valueOf(false));
            BlockFlower.EnumFlowerType flower1 = biome.pickRandomFlower(rnd, bp);
            BlockFlower.EnumFlowerType flower2 = biome.pickRandomFlower(rnd, bp);
            BlockFlower.EnumFlowerColor flower1Color = flower1.getBlockType();
            BlockFlower.EnumFlowerColor flower2Color = flower2.getBlockType();

            primary = plank;
            secondary = log;
            decor1 = decor2 = leaf;
            plant1 = flower1Color.getBlock().getDefaultState().withProperty(flower1Color.getBlock().getTypeProperty(), flower1);

            plant2 = flower2Color.getBlock().getDefaultState().withProperty(flower2Color.getBlock().getTypeProperty(), flower2);
        }
        if (((biome instanceof BiomeSnow)) || (biome == Biomes.FROZEN_OCEAN) || (biome == Biomes.FROZEN_RIVER) ||
                (biome.getTempCategory() == Biome.TempCategory.COLD))
        {
            primary = Blocks.ICE.getDefaultState();
            secondary = Blocks.SNOW.getDefaultState();
            decor1 = Blocks.GLASS.getDefaultState();
            decor2 = Blocks.FROSTED_ICE.getDefaultState();
            plant1 = plant2 = Blocks.SNOW_LAYER.getDefaultState();
        }
        return new GenBlockEntry(new IBlockState[] { primary, secondary, decor1, decor2, plant1, plant2, liquid });
    }

    public static BlockPlanks.EnumType woodVariant(Biome biome)
    {
        BlockPlanks.EnumType woodVariant = BlockPlanks.EnumType.SPRUCE;
        if (biome.getTemperature() >= 0.5F)
        {
            woodVariant = rnd.nextInt(2) == 0 ? BlockPlanks.EnumType.BIRCH : BlockPlanks.EnumType.OAK;
            if ((biome instanceof BiomeSwamp)) {
                woodVariant = BlockPlanks.EnumType.OAK;
            }
            if (((biome instanceof BiomeForest)) && ((biome.genBigTreeChance(rnd) instanceof WorldGenCanopyTree))) {
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
                this.primary = Blocks.STONE.getDefaultState();
            }
            if (this.secondary == null) {
                this.secondary = Blocks.DIRT.getDefaultState();
            }
            if (this.decor1 == null) {
                this.decor1 = Blocks.SAND.getDefaultState();
            }
            if (this.decor2 == null) {
                this.decor2 = Blocks.SANDSTONE.getDefaultState();
            }
            if (this.plant1 == null) {
                this.plant1 = Blocks.RED_FLOWER.getDefaultState();
            }
            if (this.plant2 == null) {
                this.plant2 = Blocks.YELLOW_FLOWER.getDefaultState();
            }
            if (this.liquid == null) {
                this.liquid = Blocks.WATER.getDefaultState();
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
                    ibss[a] = b.getDefaultState();
                }
            }
            return ibss;
        }
    }
}
