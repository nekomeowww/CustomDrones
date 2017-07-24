package com.github.nekomeowww.customdrones;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import com.github.nekomeowww.customdrones.entity.EntityDroneBaby;
import com.github.nekomeowww.customdrones.entity.EntityDroneBabyBig;
import com.github.nekomeowww.customdrones.entity.EntityDroneWildItem;
import com.github.nekomeowww.customdrones.worldgen.WorldGen;

public class ConfigControl
{
    public static Biome[] allBiomes;

    static
    {
        List<Biome> biomes = new ArrayList();
        Iterator<Biome> ite = Biome.REGISTRY.iterator();
        while (ite.hasNext()) {
            biomes.add(ite.next());
        }
        allBiomes = (Biome[])biomes.toArray(new Biome[0]);
    }

    public static String CONFIGID = "MAINCONFIG";
    public Configuration config;

    public ConfigControl(File file)
    {
        this.config = new Configuration(file, true);
        this.config.load();
        syncConfig();
        this.config.save();
    }

    public void syncConfig()
    {
        EnumCreatureType dronesType = EnumCreatureType.CREATURE;
        String cat = "spawn baby drone";
        this.config.addCustomCategoryComment(cat, "Baby drone spawning weights and group size");
        int weight = this.config.getInt("Weight", cat, 10, 1, 2000, "Spawning weight. Higher number = more spawn chance");
        int groupMin = this.config.getInt("Group min", cat, 2, 1, 2000, "Min spawn group size.");
        int groupMax = this.config.getInt("Group max", cat, 8, 1, 2000, "Max spawn group size.");
        editSpawn(EntityDroneBaby.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);

        cat = "spawn big baby drone";
        this.config.addCustomCategoryComment(cat, "Big baby drone spawning weights and group size");
        weight = this.config.getInt("Weight", cat, 1, 1, 2000, "Spawning weight. Higher number = more spawn chance");
        groupMin = this.config.getInt("Group min", cat, 1, 1, 2000, "Min spawn group size.");
        groupMax = this.config.getInt("Group max", cat, 1, 1, 2000, "Max spawn group size.");
        editSpawn(EntityDroneBabyBig.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);

        cat = "spawn wild item drone";
        this.config.addCustomCategoryComment(cat, "Wild item drone spawning weights and group size");
        weight = this.config.getInt("Weight", cat, 10, 1, 2000, "Spawning weight. Higher number = more spawn chance");
        groupMin = this.config.getInt("Group min", cat, 1, 1, 2000, "Min spawn group size.");
        groupMax = this.config.getInt("Group max", cat, 5, 1, 2000, "Max spawn group size.");
        editSpawn(EntityDroneWildItem.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);

        cat = "world gen";
        this.config.addCustomCategoryComment(cat, "World structures generations.");
        WorldGen.syncConfig(this.config, cat);
    }

    public void editSpawn(Class<? extends EntityLiving> clazz, Integer weight, Integer groupMin, Integer groupMax, EnumCreatureType type, Biome... biomes)
    {
        EntityRegistry.addSpawn(clazz, weight.intValue(), groupMin.intValue(), groupMax.intValue(), type, biomes);
    }
}
