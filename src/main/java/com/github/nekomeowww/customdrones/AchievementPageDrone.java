package com.github.nekomeowww.customdrones;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementPageDrone
        extends AchievementPage
{
    public AchievementPageDrone()
    {
        super("Drones", allAchievements());
    }

    public static Achievement droneBit = new Achievement("drones.bit", "drones.bit", -2, 0, CustomDrones.droneBit, (Achievement)null)
            .initIndependentStat().registerStat();
    public static Achievement dronePart = new Achievement("drones.part", "drones.part", 0, 0, CustomDrones.cfPlate1, droneBit)
            .setSpecial().registerStat();
    public static Achievement droneSpawn = new Achievement("drones.spawn", "drones.spawn", 0, 2, CustomDrones.droneSpawn, dronePart)
            .registerStat();
    public static Achievement droneFlyer = new Achievement("drones.flyer", "drones.flyer", 2, 2, CustomDrones.droneFlyer, dronePart)
            .registerStat();
    public static Achievement droneCrafter = new Achievement("drones.crafter", "drones.crafter", 4, 2, CustomDrones.crafter, dronePart)
            .registerStat();
    public static Achievement droneScrew = new Achievement("drones.screw", "drones.screw", -2, 2, CustomDrones.droneScrew, dronePart)
            .registerStat();
    public static Achievement dronePaint = new Achievement("drones.paint", "drones.paint", -4, 2, CustomDrones.dronePainter, dronePart)
            .registerStat();
    public static Achievement droneSpawnBest = new Achievement("drones.spawnBest", "drones.spawnBest", 0, 4, CustomDrones.droneSpawn, droneSpawn)
            .setSpecial().registerStat();
    public static Achievement dronePlasmaGun = new Achievement("drones.plasmagun", "drones.plasmagun", -2, -2, CustomDrones.plasmaGun, droneBit)
            .setSpecial().registerStat();

    public static Achievement[] allAchievements()
    {
        List<Achievement> list = new ArrayList();
        list.add(droneBit);
        list.add(dronePart);
        list.add(droneSpawn);
        list.add(droneFlyer);
        list.add(droneCrafter);
        list.add(droneScrew);
        list.add(dronePaint);
        list.add(droneSpawnBest);
        list.add(dronePlasmaGun);
        return (Achievement[])list.toArray(new Achievement[0]);
    }
}
