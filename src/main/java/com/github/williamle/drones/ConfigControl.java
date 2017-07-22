/*    */ package williamle.drones;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.EnumCreatureType;
/*    */ import net.minecraft.util.registry.RegistryNamespaced;
/*    */ import net.minecraft.world.biome.Biome;
/*    */ import net.minecraftforge.common.config.Configuration;
/*    */ import net.minecraftforge.fml.common.registry.EntityRegistry;
/*    */ import williamle.drones.entity.EntityDroneBaby;
/*    */ import williamle.drones.entity.EntityDroneBabyBig;
/*    */ import williamle.drones.entity.EntityDroneWildItem;
/*    */ import williamle.drones.worldgen.WorldGen;
/*    */ 
/*    */ 
/*    */ public class ConfigControl
/*    */ {
/*    */   public static Biome[] allBiomes;
/*    */   
/*    */   static
/*    */   {
/* 25 */     List<Biome> biomes = new ArrayList();
/* 26 */     Iterator<Biome> ite = Biome.field_185377_q.iterator();
/* 27 */     while (ite.hasNext())
/*    */     {
/* 29 */       biomes.add(ite.next());
/*    */     }
/* 31 */     allBiomes = (Biome[])biomes.toArray(new Biome[0]);
/*    */   }
/*    */   
/* 34 */   public static String CONFIGID = "MAINCONFIG";
/*    */   public Configuration config;
/*    */   
/*    */   public ConfigControl(File file)
/*    */   {
/* 39 */     this.config = new Configuration(file, true);
/* 40 */     this.config.load();
/* 41 */     syncConfig();
/* 42 */     this.config.save();
/*    */   }
/*    */   
/*    */   public void syncConfig()
/*    */   {
/* 47 */     EnumCreatureType dronesType = EnumCreatureType.CREATURE;
/* 48 */     String cat = "spawn baby drone";
/* 49 */     this.config.addCustomCategoryComment(cat, "Baby drone spawning weights and group size");
/* 50 */     int weight = this.config.getInt("Weight", cat, 10, 1, 2000, "Spawning weight. Higher number = more spawn chance");
/* 51 */     int groupMin = this.config.getInt("Group min", cat, 2, 1, 2000, "Min spawn group size.");
/* 52 */     int groupMax = this.config.getInt("Group max", cat, 8, 1, 2000, "Max spawn group size.");
/* 53 */     editSpawn(EntityDroneBaby.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);
/*    */     
/* 55 */     cat = "spawn big baby drone";
/* 56 */     this.config.addCustomCategoryComment(cat, "Big baby drone spawning weights and group size");
/* 57 */     weight = this.config.getInt("Weight", cat, 1, 1, 2000, "Spawning weight. Higher number = more spawn chance");
/* 58 */     groupMin = this.config.getInt("Group min", cat, 1, 1, 2000, "Min spawn group size.");
/* 59 */     groupMax = this.config.getInt("Group max", cat, 1, 1, 2000, "Max spawn group size.");
/* 60 */     editSpawn(EntityDroneBabyBig.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);
/*    */     
/* 62 */     cat = "spawn wild item drone";
/* 63 */     this.config.addCustomCategoryComment(cat, "Wild item drone spawning weights and group size");
/* 64 */     weight = this.config.getInt("Weight", cat, 10, 1, 2000, "Spawning weight. Higher number = more spawn chance");
/* 65 */     groupMin = this.config.getInt("Group min", cat, 1, 1, 2000, "Min spawn group size.");
/* 66 */     groupMax = this.config.getInt("Group max", cat, 5, 1, 2000, "Max spawn group size.");
/* 67 */     editSpawn(EntityDroneWildItem.class, Integer.valueOf(weight), Integer.valueOf(groupMin), Integer.valueOf(groupMax), dronesType, allBiomes);
/*    */     
/* 69 */     cat = "world gen";
/* 70 */     this.config.addCustomCategoryComment(cat, "World structures generations.");
/* 71 */     WorldGen.syncConfig(this.config, cat);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void editSpawn(Class<? extends EntityLiving> clazz, Integer weight, Integer groupMin, Integer groupMax, EnumCreatureType type, Biome... biomes)
/*    */   {
/* 78 */     EntityRegistry.addSpawn(clazz, weight.intValue(), groupMin.intValue(), groupMax.intValue(), type, biomes);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\ConfigControl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */