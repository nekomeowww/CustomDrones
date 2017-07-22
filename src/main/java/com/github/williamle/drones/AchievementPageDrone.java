/*    */ package williamle.drones;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.stats.Achievement;
/*    */ import net.minecraftforge.common.AchievementPage;
/*    */ 
/*    */ public class AchievementPageDrone
/*    */   extends AchievementPage
/*    */ {
/*    */   public AchievementPageDrone()
/*    */   {
/* 13 */     super("Drones", allAchievements());
/*    */   }
/*    */   
/* 16 */   public static Achievement droneBit = new Achievement("drones.bit", "drones.bit", -2, 0, DronesMod.droneBit, (Achievement)null)
/* 17 */     .func_75966_h().func_75971_g();
/* 18 */   public static Achievement dronePart = new Achievement("drones.part", "drones.part", 0, 0, DronesMod.cfPlate1, droneBit)
/* 19 */     .func_75987_b().func_75971_g();
/* 20 */   public static Achievement droneSpawn = new Achievement("drones.spawn", "drones.spawn", 0, 2, DronesMod.droneSpawn, dronePart)
/* 21 */     .func_75971_g();
/* 22 */   public static Achievement droneFlyer = new Achievement("drones.flyer", "drones.flyer", 2, 2, DronesMod.droneFlyer, dronePart)
/* 23 */     .func_75971_g();
/* 24 */   public static Achievement droneCrafter = new Achievement("drones.crafter", "drones.crafter", 4, 2, DronesMod.crafter, dronePart)
/* 25 */     .func_75971_g();
/* 26 */   public static Achievement droneScrew = new Achievement("drones.screw", "drones.screw", -2, 2, DronesMod.droneScrew, dronePart)
/* 27 */     .func_75971_g();
/* 28 */   public static Achievement dronePaint = new Achievement("drones.paint", "drones.paint", -4, 2, DronesMod.dronePainter, dronePart)
/* 29 */     .func_75971_g();
/* 30 */   public static Achievement droneSpawnBest = new Achievement("drones.spawnBest", "drones.spawnBest", 0, 4, DronesMod.droneSpawn, droneSpawn)
/* 31 */     .func_75987_b().func_75971_g();
/* 32 */   public static Achievement dronePlasmaGun = new Achievement("drones.plasmagun", "drones.plasmagun", -2, -2, DronesMod.plasmaGun, droneBit)
/* 33 */     .func_75987_b().func_75971_g();
/*    */   
/*    */   public static Achievement[] allAchievements()
/*    */   {
/* 37 */     List<Achievement> list = new ArrayList();
/* 38 */     list.add(droneBit);
/* 39 */     list.add(dronePart);
/* 40 */     list.add(droneSpawn);
/* 41 */     list.add(droneFlyer);
/* 42 */     list.add(droneCrafter);
/* 43 */     list.add(droneScrew);
/* 44 */     list.add(dronePaint);
/* 45 */     list.add(droneSpawnBest);
/* 46 */     list.add(dronePlasmaGun);
/* 47 */     return (Achievement[])list.toArray(new Achievement[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\AchievementPageDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */