/*     */ package williamle.drones;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.item.crafting.ShapelessRecipes;
/*     */ import net.minecraft.potion.PotionType;
/*     */ import net.minecraft.potion.PotionUtils;
/*     */ import net.minecraftforge.common.AchievementPage;
/*     */ import net.minecraftforge.fml.common.Mod;
/*     */ import net.minecraftforge.fml.common.Mod.EventHandler;
/*     */ import net.minecraftforge.fml.common.Mod.Instance;
/*     */ import net.minecraftforge.fml.common.SidedProxy;
/*     */ import net.minecraftforge.fml.common.event.FMLInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
/*     */ import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
/*     */ import net.minecraftforge.fml.common.registry.GameRegistry;
/*     */ import williamle.drones.api.helpers.RegHelper;
/*     */ import williamle.drones.block.BlockCrafter;
/*     */ import williamle.drones.drone.RecipeDrone;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.entity.EntityDroneBaby;
/*     */ import williamle.drones.entity.EntityDroneBabyBig;
/*     */ import williamle.drones.entity.EntityDroneWildItem;
/*     */ import williamle.drones.entity.EntityHomingBox;
/*     */ import williamle.drones.entity.EntityPlasmaShot;
/*     */ import williamle.drones.handlers.DroneSpawnHandler;
/*     */ import williamle.drones.item.IItemWeapon;
/*     */ import williamle.drones.item.ItemDroneBit;
/*     */ import williamle.drones.item.ItemDroneFlyer;
/*     */ import williamle.drones.item.ItemDroneModule;
/*     */ import williamle.drones.item.ItemDronePainter;
/*     */ import williamle.drones.item.ItemDronePart;
/*     */ import williamle.drones.item.ItemDroneScrew;
/*     */ import williamle.drones.item.ItemDroneSpawn;
/*     */ import williamle.drones.item.ItemGunUpgrade;
/*     */ import williamle.drones.item.ItemGunUpgrade.GunUpgrade;
/*     */ import williamle.drones.item.ItemPlasmaGun;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Mod(name="Custom Drones", modid="drones", version="1.10.2-1.5.0", guiFactory="williamle.drones.GuiFactory", acceptedMinecraftVersions="[1.10.2]")
/*     */ public class DronesMod
/*     */ {
/*     */   public static final String MODNAME = "Custom Drones";
/*     */   public static final String MODID = "drones";
/*     */   public static final String VERSION = "1.10.2-1.5.0";
/*     */   @Mod.Instance("drones")
/*     */   public static DronesMod instance;
/*     */   @SidedProxy(clientSide="williamle.drones.ClientProxy", serverSide="williamle.drones.CommonProxy")
/*     */   public static CommonProxy proxy;
/*     */   public static ConfigControl configControl;
/*     */   
/*     */   @Mod.EventHandler
/*     */   public void preinit(FMLPreInitializationEvent event)
/*     */   {
/*  69 */     registerEntities();
/*  70 */     registerItemsBlocks();
/*  71 */     new RecipeDrone().registerRecipes();
/*  72 */     registerRecipes();
/*  73 */     registerModuleRecipes();
/*  74 */     registerGunUpgradeRecipes();
/*  75 */     proxy.preLoad(event);
/*  76 */     configControl = new ConfigControl(event.getSuggestedConfigurationFile());
/*     */   }
/*     */   
/*     */   @Mod.EventHandler
/*     */   public void init(FMLInitializationEvent event)
/*     */   {
/*  82 */     proxy.load(event);
/*     */   }
/*     */   
/*     */   @Mod.EventHandler
/*     */   public void postinit(FMLPostInitializationEvent event)
/*     */   {
/*  88 */     PacketDispatcher.registerPackets();
/*  89 */     DroneSpawnHandler.registerSpawns();
/*  90 */     configControl.syncConfig();
/*  91 */     proxy.registerStuffRenders();
/*  92 */     proxy.registerHandlers();
/*  93 */     proxy.postLoad(event);
/*  94 */     AchievementPage.registerAchievementPage(achievementPage = new AchievementPageDrone());
/*     */   }
/*     */   
/*  97 */   public static CreativeTabs droneTab = new CreativeTabs("Drones")
/*     */   {
/*     */ 
/*     */     public Item func_78016_d()
/*     */     {
/* 102 */       return DronesMod.droneSpawn;
/*     */     }
/*     */   };
/*     */   
/*     */   public static AchievementPage achievementPage;
/*     */   
/* 108 */   public static ItemDroneSpawn droneSpawn = (ItemDroneSpawn)new ItemDroneSpawn().func_77637_a(droneTab).func_77655_b("droneSpawn");
/*     */   
/* 110 */   public static ItemDroneFlyer droneFlyer = (ItemDroneFlyer)new ItemDroneFlyer().func_77637_a(droneTab).func_77655_b("droneFlyer");
/*     */   
/* 112 */   public static ItemDronePainter dronePainter = (ItemDronePainter)new ItemDronePainter().func_77637_a(droneTab).func_77655_b("dronePainter");
/*     */   
/* 114 */   public static ItemDroneScrew droneScrew = (ItemDroneScrew)new ItemDroneScrew().func_77637_a(droneTab).func_77655_b("droneScrew");
/*     */   
/* 116 */   public static ItemDroneBit droneBit = (ItemDroneBit)new ItemDroneBit().func_77637_a(droneTab).func_77655_b("droneBit");
/*     */   
/* 118 */   public static ItemDronePart cfPlate1 = (ItemDronePart)new ItemDronePart(56, 1).func_77637_a(droneTab).func_77655_b("cfPlate1");
/*     */   
/* 120 */   public static ItemDronePart cfPlate2 = (ItemDronePart)new ItemDronePart(56, 2).func_77637_a(droneTab).func_77655_b("cfPlate2");
/*     */   
/* 122 */   public static ItemDronePart cfPlate3 = (ItemDronePart)new ItemDronePart(56, 3).func_77637_a(droneTab).func_77655_b("cfPlate3");
/*     */   
/* 124 */   public static ItemDronePart cfPlate4 = (ItemDronePart)new ItemDronePart(56, 4).func_77637_a(droneTab).func_77655_b("cfPlate4");
/*     */   
/* 126 */   public static ItemDronePart case1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("case1");
/*     */   
/* 128 */   public static ItemDronePart case2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("case2");
/*     */   
/* 130 */   public static ItemDronePart case3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("case3");
/*     */   
/* 132 */   public static ItemDronePart case4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("case4");
/*     */   
/* 134 */   public static ItemDronePart chip1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("chip1");
/*     */   
/* 136 */   public static ItemDronePart chip2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("chip2");
/*     */   
/* 138 */   public static ItemDronePart chip3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("chip3");
/*     */   
/* 140 */   public static ItemDronePart chip4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("chip4");
/*     */   
/* 142 */   public static ItemDronePart core1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("core1");
/*     */   
/* 144 */   public static ItemDronePart core2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("core2");
/*     */   
/* 146 */   public static ItemDronePart core3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("core3");
/*     */   
/* 148 */   public static ItemDronePart core4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("core4");
/*     */   
/* 150 */   public static ItemDronePart engine1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("engine1");
/*     */   
/* 152 */   public static ItemDronePart engine2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("engine2");
/*     */   
/* 154 */   public static ItemDronePart engine3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("engine3");
/*     */   
/* 156 */   public static ItemDronePart engine4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("engine4");
/*     */   
/* 158 */   public static ItemDroneModule droneModule = (ItemDroneModule)new ItemDroneModule().func_77637_a(droneTab).func_77655_b("droneModule");
/* 159 */   public static Block crafter = new BlockCrafter().func_149647_a(droneTab).func_149663_c("crafter");
/*     */   
/* 161 */   public static Item plasmaGun = new ItemPlasmaGun(false).func_77637_a(droneTab).func_77655_b("plasmaGun");
/* 162 */   public static Item plasmaGunHoming = new ItemPlasmaGun(true).func_77637_a(droneTab)
/* 163 */     .func_77655_b("plasmaGunHoming");
/* 164 */   public static Item gunUpgrade = new ItemGunUpgrade().func_77637_a(droneTab).func_77655_b("gunUpgrade");
/*     */   
/*     */   public void registerEntities()
/*     */   {
/* 168 */     RegHelper.registerEntity(EntityDrone.class, "drone", 0, this, 256, 1, true, new int[0]);
/* 169 */     RegHelper.registerEntity(EntityPlasmaShot.class, "plasmaShot", 1, this, 256, 1, true, new int[0]);
/* 170 */     RegHelper.registerEntity(EntityDroneBaby.class, "droneBaby", 2, this, 256, 1, true, new int[0]);
/* 171 */     RegHelper.registerEntity(EntityDroneBabyBig.class, "droneBabyBig", 3, this, 256, 1, true, new int[0]);
/* 172 */     RegHelper.registerEntity(EntityHomingBox.class, "homingBox", 4, this, 256, 100, false, new int[0]);
/* 173 */     RegHelper.registerEntity(EntityDroneWildItem.class, "droneWildItem", 5, this, 256, 1, true, new int[0]);
/*     */   }
/*     */   
/*     */   public void registerItemsBlocks()
/*     */   {
/* 178 */     RegHelper.registerItem(droneSpawn);
/* 179 */     RegHelper.registerItem(droneFlyer);
/* 180 */     RegHelper.registerItem(dronePainter);
/* 181 */     RegHelper.registerItem(droneScrew);
/* 182 */     RegHelper.registerItem(droneBit);
/* 183 */     RegHelper.registerItem(cfPlate1);
/* 184 */     RegHelper.registerItem(cfPlate2);
/* 185 */     RegHelper.registerItem(cfPlate3);
/* 186 */     RegHelper.registerItem(cfPlate4);
/* 187 */     RegHelper.registerItem(case1);
/* 188 */     RegHelper.registerItem(case2);
/* 189 */     RegHelper.registerItem(case3);
/* 190 */     RegHelper.registerItem(case4);
/* 191 */     RegHelper.registerItem(chip1);
/* 192 */     RegHelper.registerItem(chip2);
/* 193 */     RegHelper.registerItem(chip3);
/* 194 */     RegHelper.registerItem(chip4);
/* 195 */     RegHelper.registerItem(core1);
/* 196 */     RegHelper.registerItem(core2);
/* 197 */     RegHelper.registerItem(core3);
/* 198 */     RegHelper.registerItem(core4);
/* 199 */     RegHelper.registerItem(engine1);
/* 200 */     RegHelper.registerItem(engine2);
/* 201 */     RegHelper.registerItem(engine3);
/* 202 */     RegHelper.registerItem(engine4);
/* 203 */     RegHelper.registerItem(droneModule);
/* 204 */     RegHelper.registerBlock(crafter);
/*     */     
/* 206 */     RegHelper.registerItem(plasmaGun);
/* 207 */     RegHelper.registerItem(plasmaGunHoming);
/* 208 */     RegHelper.registerItem(gunUpgrade);
/*     */   }
/*     */   
/*     */   public void registerRecipes()
/*     */   {
/* 213 */     addRecipe(new ItemStack(cfPlate1, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151042_j, Character.valueOf('I'), droneBit, Character.valueOf('D'), Items.field_151045_i });
/*     */     
/* 215 */     addRecipe(new ItemStack(cfPlate2, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151043_k, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate1 });
/* 216 */     addRecipe(new ItemStack(cfPlate3, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151045_i, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate2 });
/* 217 */     addRecipe(new ItemStack(cfPlate4, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151166_bC, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate3 });
/*     */     
/* 219 */     addRecipe(new ItemStack(case1), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate1 });
/* 220 */     addRecipe(new ItemStack(case2), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate2 });
/* 221 */     addRecipe(new ItemStack(case3), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate3 });
/* 222 */     addRecipe(new ItemStack(case4), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate4 });
/* 223 */     addRecipe(new ItemStack(chip1), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151137_ax });
/* 224 */     addRecipe(new ItemStack(chip2), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151137_ax });
/* 225 */     addRecipe(new ItemStack(chip3), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151137_ax });
/* 226 */     addRecipe(new ItemStack(chip4), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151137_ax });
/* 227 */     addRecipe(new ItemStack(core1), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151065_br });
/* 228 */     addRecipe(new ItemStack(core2), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151065_br });
/* 229 */     addRecipe(new ItemStack(core3), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151065_br });
/* 230 */     addRecipe(new ItemStack(core4), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151065_br });
/* 231 */     addRecipe(new ItemStack(engine1), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip1 });
/* 232 */     addRecipe(new ItemStack(engine2), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip2 });
/* 233 */     addRecipe(new ItemStack(engine3), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip3 });
/* 234 */     addRecipe(new ItemStack(engine4), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip4 });
/*     */     
/* 236 */     addRecipe(new ItemStack(droneFlyer), new Object[] { " R ", "ECE", "III", Character.valueOf('I'), Items.field_151042_j, Character.valueOf('E'), Items.field_151079_bi, Character.valueOf('R'), Items.field_151137_ax, 
/* 237 */       Character.valueOf('C'), chip1 });
/* 238 */     addShapeless(new ItemStack(dronePainter), new Object[] { Items.field_151042_j, Items.field_151137_ax, new ItemStack(Items.field_151100_aR, 1, 1), new ItemStack(Items.field_151100_aR, 1, 2), new ItemStack(Items.field_151100_aR, 1, 4) });
/*     */     
/* 240 */     addShapeless(new ItemStack(droneScrew), new Object[] { Items.field_151042_j, Items.field_151043_k, Items.field_151137_ax, cfPlate1 });
/* 241 */     addShapeless(new ItemStack(crafter), new Object[] { Blocks.field_150462_ai, chip1, case1 });
/*     */     
/* 243 */     addRecipe(new ItemStack(plasmaGun), new Object[] { "COE", "  H", Character.valueOf('C'), case1, Character.valueOf('O'), core1, Character.valueOf('E'), droneBit, Character.valueOf('H'), chip1 });
/* 244 */     addRecipe(new ItemStack(plasmaGunHoming), new Object[] { "COE", "  H", Character.valueOf('C'), case1, Character.valueOf('O'), core1, Character.valueOf('E'), new ItemStack(droneBit, 1, 1), 
/* 245 */       Character.valueOf('H'), chip1 });
/* 246 */     addShapeless(new ItemStack(plasmaGunHoming), new Object[] { plasmaGun, new ItemStack(droneBit, 1, 1) });
/*     */   }
/*     */   
/*     */   public void registerModuleRecipes()
/*     */   {
/* 251 */     addRecipe(module(Module.useless1), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate1 });
/* 252 */     addRecipe(module(Module.useless2), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate2 });
/* 253 */     addRecipe(module(Module.useless3), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate3 });
/* 254 */     addRecipe(module(Module.useless4), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate4 });
/* 255 */     addShapeless(module(Module.itemInventory), new Object[] { module(Module.useless1), Blocks.field_150486_ae });
/* 256 */     addShapeless(module(Module.nplayerTransport), new Object[] { module(Module.useless2), Items.field_151008_G, Items.field_151123_aH });
/* 257 */     addShapeless(module(Module.playerTransport), new Object[] { module(Module.useless3), Items.field_151008_G, Items.field_151123_aH });
/* 258 */     addShapeless(module(Module.multiTransport), new Object[] { module(Module.useless4), module(Module.itemInventory), 
/* 259 */       module(Module.nplayerTransport), module(Module.playerTransport) });
/* 260 */     addShapeless(module(Module.itemCollect), new Object[] { module(Module.useless1), Items.field_151112_aM });
/* 261 */     addShapeless(module(Module.xpCollect), new Object[] { module(Module.useless2), Items.field_151112_aM, Items.field_151069_bo });
/* 262 */     addShapeless(module(Module.multiCollect), new Object[] { module(Module.useless3), module(Module.itemCollect), 
/* 263 */       module(Module.xpCollect) });
/* 264 */     addShapeless(module(Module.chestDeposit), new Object[] { module(Module.useless2), Items.field_151107_aW, Items.field_151132_bS });
/* 265 */     addShapeless(module(Module.mobScan1), new Object[] { module(Module.useless1), Items.field_151147_al, Items.field_151116_aA, Items.field_151110_aK });
/* 266 */     addShapeless(module(Module.mobScan2), new Object[] { module(Module.useless2), Items.field_151147_al, Items.field_151116_aA, Items.field_151110_aK, Items.field_151078_bh, Items.field_151016_H, Items.field_151103_aS });
/*     */     
/* 268 */     addShapeless(module(Module.oreScan), new Object[] { module(Module.useless1), Items.field_151044_h, Items.field_151137_ax, Items.field_151042_j, Items.field_151043_k, Items.field_151045_i, Items.field_151166_bC, new ItemStack(Items.field_151100_aR, 11) });
/*     */     
/* 270 */     addShapeless(module(Module.multiScan), new Object[] { module(Module.useless2), module(Module.mobScan2), 
/* 271 */       module(Module.oreScan) });
/* 272 */     addShapeless(module(Module.controlMovement), new Object[] { module(Module.useless1), cfPlate1 });
/* 273 */     addShapeless(module(Module.pathMovement), new Object[] { module(Module.useless1), cfPlate1, Items.field_151107_aW });
/* 274 */     addShapeless(module(Module.followMovement), new Object[] { module(Module.useless1), cfPlate1, Items.field_151058_ca });
/* 275 */     addShapeless(module(Module.multiMovement), new Object[] { module(Module.useless2), module(Module.controlMovement), 
/* 276 */       module(Module.pathMovement), module(Module.followMovement) });
/* 277 */     addShapeless(module(Module.camera), new Object[] { module(Module.useless3), Blocks.field_150359_w, Blocks.field_150429_aA });
/* 278 */     addShapeless(module(Module.weapon1), new Object[] { module(Module.useless1), Items.field_151065_br, Items.field_151042_j });
/* 279 */     addShapeless(module(Module.weapon2), new Object[] { module(Module.useless2), Items.field_151065_br, Items.field_151043_k });
/* 280 */     addShapeless(module(Module.weapon3), new Object[] { module(Module.useless3), Items.field_151065_br, Items.field_151045_i });
/* 281 */     addShapeless(module(Module.weapon4), new Object[] { module(Module.useless4), Items.field_151065_br, Items.field_151166_bC });
/* 282 */     addShapeless(module(Module.armor1), new Object[] { module(Module.useless1), Items.field_151116_aA, Items.field_151042_j });
/* 283 */     addShapeless(module(Module.armor2), new Object[] { module(Module.useless2), Items.field_151116_aA, Items.field_151043_k });
/* 284 */     addShapeless(module(Module.armor3), new Object[] { module(Module.useless3), Items.field_151116_aA, Items.field_151045_i });
/* 285 */     addShapeless(module(Module.armor4), new Object[] { module(Module.useless4), Items.field_151116_aA, Items.field_151166_bC });
/* 286 */     addShapeless(module(Module.shooting), new Object[] { module(Module.useless1), Items.field_151031_f, Items.field_151065_br });
/* 287 */     addShapeless(module(Module.shootingHoming), new Object[] { module(Module.useless2), module(Module.shooting), new ItemStack(droneBit, 1, 1) });
/*     */     
/* 289 */     addShapeless(module(Module.batterySave1), new Object[] { module(Module.useless1), Items.field_151042_j, Items.field_151174_bG });
/* 290 */     addShapeless(module(Module.batterySave2), new Object[] { module(Module.useless2), Items.field_151043_k, Items.field_151174_bG });
/* 291 */     addShapeless(module(Module.batterySave3), new Object[] { module(Module.useless3), Items.field_151045_i, Items.field_151174_bG });
/* 292 */     addShapeless(module(Module.batterySave4), new Object[] { module(Module.useless4), Items.field_151166_bC, Items.field_151174_bG });
/* 293 */     addShapeless(module(Module.heatPower), new Object[] { module(Module.useless1), Items.field_151129_at, Blocks.field_150424_aL });
/* 294 */     addShapeless(module(Module.solarPower), new Object[] { module(Module.useless2), Blocks.field_150410_aZ, Blocks.field_150426_aN });
/* 295 */     addShapeless(module(Module.multiPower), new Object[] { module(Module.useless2), module(Module.heatPower), 
/* 296 */       module(Module.solarPower) });
/* 297 */     addShapeless(module(Module.autoReturn), new Object[] { module(Module.useless2), Items.field_151111_aL });
/* 298 */     addShapeless(module(Module.deflect), new Object[] { module(Module.useless1), Items.field_185159_cQ, Items.field_151008_G });
/* 299 */     addShapeless(module(Module.deflame), new Object[] { module(Module.useless2), Items.field_151131_as, Blocks.field_150425_aM, Blocks.field_150354_m });
/*     */     
/* 301 */     addShapeless(module(Module.deexplosion), new Object[] { module(Module.useless3), Items.field_151016_H, Items.field_151061_bv, Blocks.field_150343_Z });
/*     */     
/* 303 */     addShapeless(module(Module.multiDe), new Object[] { module(Module.useless4), module(Module.deflect), module(Module.deflame), 
/* 304 */       module(Module.deexplosion) });
/* 305 */     addShapeless(module(Module.mine1), new Object[] { module(Module.useless1), Items.field_151035_b, Items.field_151051_r });
/* 306 */     addShapeless(module(Module.mine2), new Object[] { module(Module.useless2), Items.field_151005_D, Items.field_151011_C });
/* 307 */     addShapeless(module(Module.mine3), new Object[] { module(Module.useless3), Items.field_151046_w, Items.field_151047_v });
/* 308 */     addShapeless(module(Module.mine4), new Object[] { module(Module.useless4), Items.field_151046_w, Items.field_151166_bC, Blocks.field_150343_Z });
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerGunUpgradeRecipes()
/*     */   {
/* 314 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.explosion), new Object[] { chip1, Blocks.field_150335_W });
/* 315 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.scatter), new Object[] { chip1, Items.field_151123_aH, Items.field_151064_bs });
/* 316 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.healing), new Object[] { chip1, 
/* 317 */       PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionType.func_185168_a("healing")) });
/* 318 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.doubleShot), new Object[] { chip1, chip2 });
/* 319 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.tripleShot), new Object[] { chip1, chip3 });
/* 320 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.fire), new Object[] { chip1, Items.field_151033_d, Items.field_151065_br });
/* 321 */     addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.ice), new Object[] { chip1, Blocks.field_150432_aD, 
/* 322 */       PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionType.func_185168_a("slowness")) });
/*     */   }
/*     */   
/*     */   public static ItemStack gunUpgrade(ItemGunUpgrade.GunUpgrade gu)
/*     */   {
/* 327 */     return ItemGunUpgrade.itemGunUpgrade(gu);
/*     */   }
/*     */   
/*     */   public static ItemStack module(Module m)
/*     */   {
/* 332 */     return ItemDroneModule.itemModule(m);
/*     */   }
/*     */   
/*     */   public static void addRecipe(ItemStack is, Object... os)
/*     */   {
/* 337 */     IRecipe recipe = GameRegistry.addShapedRecipe(is, os);
/* 338 */     addRecipeToList(recipe, getRecipeType(is));
/*     */   }
/*     */   
/*     */   public static void addShapeless(ItemStack is, Object... os)
/*     */   {
/* 343 */     List<ItemStack> list = Lists.newArrayList();
/* 344 */     for (Object object : os)
/*     */     {
/* 346 */       if ((object instanceof ItemStack))
/*     */       {
/* 348 */         list.add(((ItemStack)object).func_77946_l());
/*     */       }
/* 350 */       else if ((object instanceof Item))
/*     */       {
/* 352 */         list.add(new ItemStack((Item)object));
/*     */       }
/*     */       else
/*     */       {
/* 356 */         if (!(object instanceof Block))
/*     */         {
/*     */ 
/* 359 */           throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
/*     */         }
/*     */         
/* 362 */         list.add(new ItemStack((Block)object));
/*     */       }
/*     */     }
/*     */     
/* 366 */     Object recipe = new ShapelessRecipes(is, list);
/* 367 */     GameRegistry.addRecipe((IRecipe)recipe);
/* 368 */     addRecipeToList((IRecipe)recipe, getRecipeType(is));
/*     */   }
/*     */   
/*     */   public static void addRecipeToList(IRecipe recipe, RecipeType type)
/*     */   {
/* 373 */     if (!recipes.containsKey(type)) recipes.put(type, new LinkedList());
/* 374 */     LinkedList<IRecipe> list = (LinkedList)recipes.get(type);
/* 375 */     list.add(recipe);
/* 376 */     recipes.put(type, list);
/*     */   }
/*     */   
/*     */   public static RecipeType getRecipeType(ItemStack is)
/*     */   {
/* 381 */     if ((is == null) || (is.func_77973_b() == null)) return RecipeType.None;
/* 382 */     if ((is.func_77973_b() instanceof ItemDronePart)) return RecipeType.Parts;
/* 383 */     if ((is.func_77973_b() instanceof ItemDroneModule)) return RecipeType.Modules;
/* 384 */     if ((is.func_77973_b() instanceof ItemDroneSpawn)) return RecipeType.Drones;
/* 385 */     if ((is.func_77973_b() instanceof IItemWeapon)) return RecipeType.Weapons;
/* 386 */     if ((is.func_77973_b() instanceof ItemDroneFlyer)) return RecipeType.Tools;
/* 387 */     if ((is.func_77973_b() instanceof ItemDronePainter)) return RecipeType.Tools;
/* 388 */     if ((is.func_77973_b() instanceof ItemDroneScrew)) return RecipeType.Tools;
/* 389 */     if ((is.func_77973_b() instanceof ItemGunUpgrade)) return RecipeType.Upgrades;
/* 390 */     if ((is.func_77973_b() instanceof ItemBlock))
/*     */     {
/* 392 */       Block b = ((ItemBlock)is.func_77973_b()).func_179223_d();
/* 393 */       if ((b instanceof BlockCrafter)) return RecipeType.Tools;
/*     */     }
/* 395 */     return RecipeType.None;
/*     */   }
/*     */   
/*     */   public static enum RecipeType
/*     */   {
/* 400 */     Parts,  Modules,  Drones,  Weapons,  Upgrades,  Tools,  None;
/*     */     
/*     */     private RecipeType() {} }
/* 403 */   public static LinkedHashMap<RecipeType, LinkedList<IRecipe>> recipes = new LinkedHashMap();
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\DronesMod.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */