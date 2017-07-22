/*    */ package williamle.drones.drone;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.init.Blocks;
/*    */ import net.minecraft.init.Items;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.WeightedRandom.Item;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.drone.module.Module;
/*    */ import williamle.drones.item.ItemDroneModule;
/*    */ import williamle.drones.item.ItemGunUpgrade;
/*    */ import williamle.drones.item.ItemGunUpgrade.GunUpgrade;
/*    */ 
/*    */ public class DroneWeightedLists
/*    */ {
/* 20 */   public static List<WeightedItemStack> bitsList = new ArrayList();
/* 21 */   public static List<WeightedItemStack> matsList = new ArrayList();
/* 22 */   public static List<WeightedItemStack> partsList = new ArrayList();
/* 23 */   public static List<WeightedItemStack> modsList = new ArrayList();
/* 24 */   public static List<WeightedItemStack> gunUpgradesList = new ArrayList();
/* 25 */   public static List<WeightedItemStack> wildHoldingList = new ArrayList();
/*    */   
/*    */   static
/*    */   {
/* 29 */     bitsList.add(new WeightedItemStack(DronesMod.droneBit, 16));
/* 30 */     bitsList.add(new WeightedItemStack(new ItemStack(DronesMod.droneBit, 1, 1), 2));
/* 31 */     matsList.add(new WeightedItemStack(Items.field_151137_ax, 16));
/* 32 */     matsList.add(new WeightedItemStack(Items.field_151042_j, 16));
/* 33 */     matsList.add(new WeightedItemStack(Items.field_151043_k, 8));
/* 34 */     matsList.add(new WeightedItemStack(Items.field_151045_i, 2));
/* 35 */     matsList.add(new WeightedItemStack(Items.field_151166_bC, 2));
/* 36 */     matsList.add(new WeightedItemStack(Items.field_151065_br, 2));
/* 37 */     matsList.add(new WeightedItemStack(Items.field_151079_bi, 2));
/* 38 */     matsList.add(new WeightedItemStack(Items.field_151123_aH, 2));
/* 39 */     partsList.add(new WeightedItemStack(DronesMod.cfPlate1, 4));
/* 40 */     partsList.add(new WeightedItemStack(DronesMod.cfPlate2, 3));
/* 41 */     partsList.add(new WeightedItemStack(DronesMod.cfPlate3, 2));
/* 42 */     partsList.add(new WeightedItemStack(DronesMod.cfPlate4, 1));
/* 43 */     partsList.add(new WeightedItemStack(DronesMod.chip1, 4));
/* 44 */     partsList.add(new WeightedItemStack(DronesMod.chip2, 3));
/* 45 */     partsList.add(new WeightedItemStack(DronesMod.chip3, 2));
/* 46 */     partsList.add(new WeightedItemStack(DronesMod.chip4, 1));
/* 47 */     partsList.add(new WeightedItemStack(DronesMod.case1, 4));
/* 48 */     partsList.add(new WeightedItemStack(DronesMod.case2, 3));
/* 49 */     partsList.add(new WeightedItemStack(DronesMod.case3, 2));
/* 50 */     partsList.add(new WeightedItemStack(DronesMod.case4, 1));
/* 51 */     partsList.add(new WeightedItemStack(DronesMod.chip1, 4));
/* 52 */     partsList.add(new WeightedItemStack(DronesMod.chip2, 3));
/* 53 */     partsList.add(new WeightedItemStack(DronesMod.chip3, 2));
/* 54 */     partsList.add(new WeightedItemStack(DronesMod.chip4, 1));
/* 55 */     partsList.add(new WeightedItemStack(DronesMod.engine1, 4));
/* 56 */     partsList.add(new WeightedItemStack(DronesMod.engine2, 3));
/* 57 */     partsList.add(new WeightedItemStack(DronesMod.engine3, 2));
/* 58 */     partsList.add(new WeightedItemStack(DronesMod.engine4, 1));
/* 59 */     for (Module m : Module.modules)
/*    */     {
/* 61 */       modsList.add(new WeightedItemStack(ItemDroneModule.itemModule(m), 5 - m.level));
/*    */     }
/* 63 */     for (ItemGunUpgrade.GunUpgrade m : ItemGunUpgrade.GunUpgrade.upgrades.values())
/*    */     {
/* 65 */       gunUpgradesList.add(new WeightedItemStack(ItemGunUpgrade.itemGunUpgrade(m), 1));
/*    */     }
/*    */     
/* 68 */     wildHoldingList.add(new WeightedItemStack(Blocks.field_150335_W, 16));
/* 69 */     wildHoldingList.addAll(bitsList);
/* 70 */     wildHoldingList.addAll(matsList);
/* 71 */     wildHoldingList.addAll(partsList);
/* 72 */     wildHoldingList.addAll(modsList);
/* 73 */     wildHoldingList.addAll(gunUpgradesList);
/*    */   }
/*    */   
/*    */   public static class WeightedItemStack extends WeightedRandom.Item
/*    */   {
/*    */     public ItemStack is;
/*    */     
/*    */     public WeightedItemStack(ItemStack i)
/*    */     {
/* 82 */       super();
/* 83 */       this.is = i;
/*    */     }
/*    */     
/*    */     public WeightedItemStack(ItemStack i, int itemWeightIn)
/*    */     {
/* 88 */       super();
/* 89 */       this.is = i;
/*    */     }
/*    */     
/*    */     public WeightedItemStack(Item i, int weight)
/*    */     {
/* 94 */       this(new ItemStack(i), weight);
/*    */     }
/*    */     
/*    */     public WeightedItemStack(Block i, int weight)
/*    */     {
/* 99 */       this(new ItemStack(i), weight);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\DroneWeightedLists.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */