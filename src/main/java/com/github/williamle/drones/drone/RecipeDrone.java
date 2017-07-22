/*     */ package williamle.drones.drone;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.inventory.InventoryCrafting;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.item.crafting.ShapelessRecipes;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.common.ForgeHooks;
/*     */ import net.minecraftforge.fml.common.registry.GameRegistry;
/*     */ import net.minecraftforge.oredict.RecipeSorter;
/*     */ import net.minecraftforge.oredict.RecipeSorter.Category;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.DronesMod.RecipeType;
/*     */ import williamle.drones.item.ItemDronePart;
/*     */ import williamle.drones.item.ItemDroneSpawn;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RecipeDrone
/*     */   implements IRecipe
/*     */ {
/*     */   public void registerRecipes()
/*     */   {
/*  28 */     RecipeSorter.register("drones:drones", RecipeDrone.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
/*     */     
/*  30 */     GameRegistry.addRecipe(this);
/*  31 */     LinkedList<ItemDronePart> cases = new LinkedList();
/*  32 */     cases.addAll(
/*  33 */       Arrays.asList(new ItemDronePart[] { DronesMod.case1, DronesMod.case2, DronesMod.case3, DronesMod.case4 }));
/*  34 */     List<ItemDronePart> chips = new LinkedList();
/*  35 */     chips.addAll(
/*  36 */       Arrays.asList(new ItemDronePart[] { DronesMod.chip1, DronesMod.chip2, DronesMod.chip3, DronesMod.chip4 }));
/*  37 */     List<ItemDronePart> cores = new LinkedList();
/*  38 */     cores.addAll(
/*  39 */       Arrays.asList(new ItemDronePart[] { DronesMod.core1, DronesMod.core2, DronesMod.core3, DronesMod.core4 }));
/*  40 */     List<ItemDronePart> engines = new LinkedList();
/*  41 */     engines.addAll(Arrays.asList(new ItemDronePart[] { DronesMod.engine1, DronesMod.engine2, DronesMod.engine3, DronesMod.engine4 }));
/*     */     
/*  43 */     for (ItemDronePart theCase : cases)
/*     */     {
/*  45 */       iscase = new ItemStack(theCase, 3);
/*  46 */       for (ItemDronePart theChip : chips)
/*     */       {
/*  48 */         ischip = new ItemStack(theChip, 1);
/*  49 */         for (ItemDronePart theCore : cores)
/*     */         {
/*  51 */           iscore = new ItemStack(theCore, 1);
/*  52 */           for (ItemDronePart theEngine : engines)
/*     */           {
/*  54 */             ItemStack isengine = new ItemStack(theEngine, 4);
/*  55 */             LinkedList<ItemStack> shapelessList = new LinkedList();
/*  56 */             shapelessList.add(iscase);
/*  57 */             shapelessList.add(ischip);
/*  58 */             shapelessList.add(iscore);
/*  59 */             shapelessList.add(isengine);
/*  60 */             ItemStack is = new ItemStack(DronesMod.droneSpawn);
/*     */             
/*  62 */             DroneInfo di = new DroneInfo(chipLevel(ischip), coreLevel(iscore), casingLevel(iscase), engineLevel(isengine));
/*  63 */             DronesMod.droneSpawn.setDroneInfo(is, di);
/*  64 */             DronesMod.addRecipeToList(new ShapelessRecipes(is, shapelessList), DronesMod.RecipeType.Drones);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     ItemStack iscase;
/*     */     ItemStack ischip;
/*     */     ItemStack iscore; }
/*     */   
/*  73 */   public int chipLevel(ItemStack is) { if (is != null)
/*     */     {
/*  75 */       if (is.func_77973_b() == DronesMod.chip1) return 1;
/*  76 */       if (is.func_77973_b() == DronesMod.chip2) return 2;
/*  77 */       if (is.func_77973_b() == DronesMod.chip3) return 3;
/*  78 */       if (is.func_77973_b() == DronesMod.chip4) return 4;
/*     */     }
/*  80 */     return -1;
/*     */   }
/*     */   
/*     */   public int coreLevel(ItemStack is)
/*     */   {
/*  85 */     if (is != null)
/*     */     {
/*  87 */       if (is.func_77973_b() == DronesMod.core1) return 1;
/*  88 */       if (is.func_77973_b() == DronesMod.core2) return 2;
/*  89 */       if (is.func_77973_b() == DronesMod.core3) return 3;
/*  90 */       if (is.func_77973_b() == DronesMod.core4) return 4;
/*     */     }
/*  92 */     return -1;
/*     */   }
/*     */   
/*     */   public int casingLevel(ItemStack is)
/*     */   {
/*  97 */     if (is != null)
/*     */     {
/*  99 */       if (is.func_77973_b() == DronesMod.case1) return 1;
/* 100 */       if (is.func_77973_b() == DronesMod.case2) return 2;
/* 101 */       if (is.func_77973_b() == DronesMod.case3) return 3;
/* 102 */       if (is.func_77973_b() == DronesMod.case4) return 4;
/*     */     }
/* 104 */     return -1;
/*     */   }
/*     */   
/*     */   public int engineLevel(ItemStack is)
/*     */   {
/* 109 */     if (is != null)
/*     */     {
/* 111 */       if (is.func_77973_b() == DronesMod.engine1) return 1;
/* 112 */       if (is.func_77973_b() == DronesMod.engine2) return 2;
/* 113 */       if (is.func_77973_b() == DronesMod.engine3) return 3;
/* 114 */       if (is.func_77973_b() == DronesMod.engine4) return 4;
/*     */     }
/* 116 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean func_77569_a(InventoryCrafting inv, World worldIn)
/*     */   {
/* 122 */     if (inv.func_70302_i_() >= 9)
/*     */     {
/* 124 */       if ((chipLevel(inv.func_70301_a(4)) > 0) && (coreLevel(inv.func_70301_a(1)) > 0) && 
/* 125 */         (casingLevel(inv.func_70301_a(3)) > 0) && 
/* 126 */         (casingLevel(inv.func_70301_a(3)) == casingLevel(inv.func_70301_a(5))) && 
/* 127 */         (casingLevel(inv.func_70301_a(5)) == casingLevel(inv.func_70301_a(7))) && 
/* 128 */         (engineLevel(inv.func_70301_a(0)) > 0) && 
/* 129 */         (engineLevel(inv.func_70301_a(0)) == engineLevel(inv.func_70301_a(2))) && 
/* 130 */         (engineLevel(inv.func_70301_a(2)) == engineLevel(inv.func_70301_a(6))) && 
/* 131 */         (engineLevel(inv.func_70301_a(6)) == engineLevel(inv.func_70301_a(8))))
/* 132 */         return true;
/*     */     }
/* 134 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_77572_b(InventoryCrafting inv)
/*     */   {
/* 140 */     ItemStack is = new ItemStack(DronesMod.droneSpawn);
/*     */     
/* 142 */     DroneInfo di = new DroneInfo(chipLevel(inv.func_70301_a(4)), coreLevel(inv.func_70301_a(1)), casingLevel(inv.func_70301_a(3)), engineLevel(inv.func_70301_a(0)));
/* 143 */     DronesMod.droneSpawn.setDroneInfo(is, di);
/* 144 */     return is;
/*     */   }
/*     */   
/*     */ 
/*     */   public int func_77570_a()
/*     */   {
/* 150 */     return 9;
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack func_77571_b()
/*     */   {
/* 156 */     return new ItemStack(DronesMod.droneSpawn);
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemStack[] func_179532_b(InventoryCrafting inv)
/*     */   {
/* 162 */     return ForgeHooks.defaultRecipeGetRemainingItems(inv);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\RecipeDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */