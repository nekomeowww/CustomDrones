package com.github.nekomeowww.customdrones;

import com.google.common.collect.Lists;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.AchievementPage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.github.nekomeowww.customdrones.api.helpers.RegHelper;
import com.github.nekomeowww.customdrones.block.BlockCrafter;
import com.github.nekomeowww.customdrones.drone.RecipeDrone;
import com.github.nekomeowww.customdrones.drone.module.Module;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.entity.EntityDroneBaby;
import com.github.nekomeowww.customdrones.entity.EntityDroneBabyBig;
import com.github.nekomeowww.customdrones.entity.EntityDroneWildItem;
import com.github.nekomeowww.customdrones.entity.EntityHomingBox;
import com.github.nekomeowww.customdrones.entity.EntityPlasmaShot;
import com.github.nekomeowww.customdrones.handlers.DroneSpawnHandler;
import com.github.nekomeowww.customdrones.item.IItemWeapon;
import com.github.nekomeowww.customdrones.item.ItemDroneBit;
import com.github.nekomeowww.customdrones.item.ItemDroneFlyer;
import com.github.nekomeowww.customdrones.item.ItemDroneModule;
import com.github.nekomeowww.customdrones.item.ItemDronePainter;
import com.github.nekomeowww.customdrones.item.ItemDronePart;
import com.github.nekomeowww.customdrones.item.ItemDroneScrew;
import com.github.nekomeowww.customdrones.item.ItemDroneSpawn;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade;
import com.github.nekomeowww.customdrones.item.ItemGunUpgrade.GunUpgrade;
import com.github.nekomeowww.customdrones.item.ItemPlasmaGun;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;

@Mod(name="Custom Drones", modid="drones", version="1.10.2-1.5.0", guiFactory="com.github.nekomeowww.customdrones.GuiFactory", acceptedMinecraftVersions="[1.10.2]")
public class CustomDrones
{
  public static final String MODNAME = "Custom Drones";
  public static final String MODID = "drones";
  public static final String VERSION = "1.10.2-1.5.0";
  @Mod.Instance("drones")
  public static CustomDrones instance;
  @SidedProxy(clientSide="com.github.nekomeowww.customdrones.ClientProxy", serverSide="com.github.nekomeowww.customdrones.CommonProxy")
  public static CommonProxy proxy;
  public static ConfigControl configControl;

  @Mod.EventHandler
  public void preinit(FMLPreInitializationEvent event)
  {
    registerEntities();
    registerItemsBlocks();
    new RecipeDrone().registerRecipes();
    registerRecipes();
    registerModuleRecipes();
    registerGunUpgradeRecipes();
    proxy.preLoad(event);
    configControl = new ConfigControl(event.getSuggestedConfigurationFile());
  }

  @Mod.EventHandler
  public void init(FMLInitializationEvent event)
  {
    proxy.load(event);
  }

  @Mod.EventHandler
  public void postinit(FMLPostInitializationEvent event)
  {
    PacketDispatcher.registerPackets();
    DroneSpawnHandler.registerSpawns();
    configControl.syncConfig();
    proxy.registerStuffRenders();
    proxy.registerHandlers();
    proxy.postLoad(event);
    AchievementPage.registerAchievementPage(achievementPage = new AchievementPageDrone());
  }

  public static CreativeTabs droneTab = new CreativeTabs("Drones")
  {

    public Item func_78016_d()
    {
      return CustomDrones.droneSpawn;
    }
  };

  public static AchievementPage achievementPage;

  public static ItemDroneSpawn droneSpawn = (ItemDroneSpawn)new ItemDroneSpawn().func_77637_a(droneTab).func_77655_b("droneSpawn");

  public static ItemDroneFlyer droneFlyer = (ItemDroneFlyer)new ItemDroneFlyer().func_77637_a(droneTab).func_77655_b("droneFlyer");

  public static ItemDronePainter dronePainter = (ItemDronePainter)new ItemDronePainter().func_77637_a(droneTab).func_77655_b("dronePainter");

  public static ItemDroneScrew droneScrew = (ItemDroneScrew)new ItemDroneScrew().func_77637_a(droneTab).func_77655_b("droneScrew");

  public static ItemDroneBit droneBit = (ItemDroneBit)new ItemDroneBit().func_77637_a(droneTab).func_77655_b("droneBit");

  public static ItemDronePart cfPlate1 = (ItemDronePart)new ItemDronePart(56, 1).func_77637_a(droneTab).func_77655_b("cfPlate1");

  public static ItemDronePart cfPlate2 = (ItemDronePart)new ItemDronePart(56, 2).func_77637_a(droneTab).func_77655_b("cfPlate2");

  public static ItemDronePart cfPlate3 = (ItemDronePart)new ItemDronePart(56, 3).func_77637_a(droneTab).func_77655_b("cfPlate3");

  public static ItemDronePart cfPlate4 = (ItemDronePart)new ItemDronePart(56, 4).func_77637_a(droneTab).func_77655_b("cfPlate4");

  public static ItemDronePart case1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("case1");

  public static ItemDronePart case2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("case2");

  public static ItemDronePart case3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("case3");

  public static ItemDronePart case4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("case4");

  public static ItemDronePart chip1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("chip1");

  public static ItemDronePart chip2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("chip2");

  public static ItemDronePart chip3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("chip3");

  public static ItemDronePart chip4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("chip4");

  public static ItemDronePart core1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("core1");

  public static ItemDronePart core2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("core2");

  public static ItemDronePart core3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("core3");

  public static ItemDronePart core4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("core4");

  public static ItemDronePart engine1 = (ItemDronePart)new ItemDronePart(1).func_77637_a(droneTab).func_77655_b("engine1");

  public static ItemDronePart engine2 = (ItemDronePart)new ItemDronePart(2).func_77637_a(droneTab).func_77655_b("engine2");

  public static ItemDronePart engine3 = (ItemDronePart)new ItemDronePart(3).func_77637_a(droneTab).func_77655_b("engine3");

  public static ItemDronePart engine4 = (ItemDronePart)new ItemDronePart(4).func_77637_a(droneTab).func_77655_b("engine4");

  public static ItemDroneModule droneModule = (ItemDroneModule)new ItemDroneModule().func_77637_a(droneTab).func_77655_b("droneModule");
  public static Block crafter = new BlockCrafter().func_149647_a(droneTab).func_149663_c("crafter");

  public static Item plasmaGun = new ItemPlasmaGun(false).func_77637_a(droneTab).func_77655_b("plasmaGun");
  public static Item plasmaGunHoming = new ItemPlasmaGun(true).func_77637_a(droneTab)
  .func_77655_b("plasmaGunHoming");
  public static Item gunUpgrade = new ItemGunUpgrade().func_77637_a(droneTab).func_77655_b("gunUpgrade");

  public void registerEntities()
  {
    RegHelper.registerEntity(EntityDrone.class, "drone", 0, this, 256, 1, true, new int[0]);
    RegHelper.registerEntity(EntityPlasmaShot.class, "plasmaShot", 1, this, 256, 1, true, new int[0]);
    RegHelper.registerEntity(EntityDroneBaby.class, "droneBaby", 2, this, 256, 1, true, new int[0]);
    RegHelper.registerEntity(EntityDroneBabyBig.class, "droneBabyBig", 3, this, 256, 1, true, new int[0]);
    RegHelper.registerEntity(EntityHomingBox.class, "homingBox", 4, this, 256, 100, false, new int[0]);
    RegHelper.registerEntity(EntityDroneWildItem.class, "droneWildItem", 5, this, 256, 1, true, new int[0]);
  }

  public void registerItemsBlocks()
  {
    RegHelper.registerItem(droneSpawn);
    RegHelper.registerItem(droneFlyer);
    RegHelper.registerItem(dronePainter);
    RegHelper.registerItem(droneScrew);
    RegHelper.registerItem(droneBit);
    RegHelper.registerItem(cfPlate1);
    RegHelper.registerItem(cfPlate2);
    RegHelper.registerItem(cfPlate3);
    RegHelper.registerItem(cfPlate4);
    RegHelper.registerItem(case1);
    RegHelper.registerItem(case2);
    RegHelper.registerItem(case3);
    RegHelper.registerItem(case4);
    RegHelper.registerItem(chip1);
    RegHelper.registerItem(chip2);
    RegHelper.registerItem(chip3);
    RegHelper.registerItem(chip4);
    RegHelper.registerItem(core1);
    RegHelper.registerItem(core2);
    RegHelper.registerItem(core3);
    RegHelper.registerItem(core4);
    RegHelper.registerItem(engine1);
    RegHelper.registerItem(engine2);
    RegHelper.registerItem(engine3);
    RegHelper.registerItem(engine4);
    RegHelper.registerItem(droneModule);
    RegHelper.registerBlock(crafter);

    RegHelper.registerItem(plasmaGun);
    RegHelper.registerItem(plasmaGunHoming);
    RegHelper.registerItem(gunUpgrade);
  }

  public void registerRecipes()
  {
    addRecipe(new ItemStack(cfPlate1, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151042_j, Character.valueOf('I'), droneBit, Character.valueOf('D'), Items.field_151045_i });

    addRecipe(new ItemStack(cfPlate2, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151043_k, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate1 });
    addRecipe(new ItemStack(cfPlate3, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151045_i, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate2 });
    addRecipe(new ItemStack(cfPlate4, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.field_151166_bC, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate3 });

    addRecipe(new ItemStack(case1), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate1 });
    addRecipe(new ItemStack(case2), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate2 });
    addRecipe(new ItemStack(case3), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate3 });
    addRecipe(new ItemStack(case4), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate4 });
    addRecipe(new ItemStack(chip1), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151137_ax });
    addRecipe(new ItemStack(chip2), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151137_ax });
    addRecipe(new ItemStack(chip3), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151137_ax });
    addRecipe(new ItemStack(chip4), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151137_ax });
    addRecipe(new ItemStack(core1), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151065_br });
    addRecipe(new ItemStack(core2), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151065_br });
    addRecipe(new ItemStack(core3), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151065_br });
    addRecipe(new ItemStack(core4), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151065_br });
    addRecipe(new ItemStack(engine1), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip1 });
    addRecipe(new ItemStack(engine2), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip2 });
    addRecipe(new ItemStack(engine3), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip3 });
    addRecipe(new ItemStack(engine4), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.field_151137_ax, Character.valueOf('C'), chip4 });

    addRecipe(new ItemStack(droneFlyer), new Object[] { " R ", "ECE", "III", Character.valueOf('I'), Items.field_151042_j, Character.valueOf('E'), Items.field_151079_bi, Character.valueOf('R'), Items.field_151137_ax,
    Character.valueOf('C'), chip1 });
    addShapeless(new ItemStack(dronePainter), new Object[] { Items.field_151042_j, Items.field_151137_ax, new ItemStack(Items.field_151100_aR, 1, 1), new ItemStack(Items.field_151100_aR, 1, 2), new ItemStack(Items.field_151100_aR, 1, 4) });

    addShapeless(new ItemStack(droneScrew), new Object[] { Items.field_151042_j, Items.field_151043_k, Items.field_151137_ax, cfPlate1 });
    addShapeless(new ItemStack(crafter), new Object[] { Blocks.field_150462_ai, chip1, case1 });

    addRecipe(new ItemStack(plasmaGun), new Object[] { "COE", "  H", Character.valueOf('C'), case1, Character.valueOf('O'), core1, Character.valueOf('E'), droneBit, Character.valueOf('H'), chip1 });
    addRecipe(new ItemStack(plasmaGunHoming), new Object[] { "COE", "  H", Character.valueOf('C'), case1, Character.valueOf('O'), core1, Character.valueOf('E'), new ItemStack(droneBit, 1, 1),
    Character.valueOf('H'), chip1 });
    addShapeless(new ItemStack(plasmaGunHoming), new Object[] { plasmaGun, new ItemStack(droneBit, 1, 1) });
  }

  public void registerModuleRecipes()
  {
    addRecipe(module(Module.useless1), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate1 });
    addRecipe(module(Module.useless2), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate2 });
    addRecipe(module(Module.useless3), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate3 });
    addRecipe(module(Module.useless4), new Object[] { "PP", "PP", Character.valueOf('P'), cfPlate4 });
    addShapeless(module(Module.itemInventory), new Object[] { module(Module.useless1), Blocks.field_150486_ae });
    addShapeless(module(Module.nplayerTransport), new Object[] { module(Module.useless2), Items.field_151008_G, Items.field_151123_aH });
    addShapeless(module(Module.playerTransport), new Object[] { module(Module.useless3), Items.field_151008_G, Items.field_151123_aH });
    addShapeless(module(Module.multiTransport), new Object[] { module(Module.useless4), module(Module.itemInventory),
      module(Module.nplayerTransport), module(Module.playerTransport) });
    addShapeless(module(Module.itemCollect), new Object[] { module(Module.useless1), Items.field_151112_aM });
    addShapeless(module(Module.xpCollect), new Object[] { module(Module.useless2), Items.field_151112_aM, Items.field_151069_bo });
    addShapeless(module(Module.multiCollect), new Object[] { module(Module.useless3), module(Module.itemCollect),
      module(Module.xpCollect) });
    addShapeless(module(Module.chestDeposit), new Object[] { module(Module.useless2), Items.field_151107_aW, Items.field_151132_bS });
    addShapeless(module(Module.mobScan1), new Object[] { module(Module.useless1), Items.field_151147_al, Items.field_151116_aA, Items.field_151110_aK });
    addShapeless(module(Module.mobScan2), new Object[] { module(Module.useless2), Items.field_151147_al, Items.field_151116_aA, Items.field_151110_aK, Items.field_151078_bh, Items.field_151016_H, Items.field_151103_aS });

    addShapeless(module(Module.oreScan), new Object[] { module(Module.useless1), Items.field_151044_h, Items.field_151137_ax, Items.field_151042_j, Items.field_151043_k, Items.field_151045_i, Items.field_151166_bC, new ItemStack(Items.field_151100_aR, 11) });

    addShapeless(module(Module.multiScan), new Object[] { module(Module.useless2), module(Module.mobScan2),
      module(Module.oreScan) });
    addShapeless(module(Module.controlMovement), new Object[] { module(Module.useless1), cfPlate1 });
    addShapeless(module(Module.pathMovement), new Object[] { module(Module.useless1), cfPlate1, Items.field_151107_aW });
    addShapeless(module(Module.followMovement), new Object[] { module(Module.useless1), cfPlate1, Items.field_151058_ca });
    addShapeless(module(Module.multiMovement), new Object[] { module(Module.useless2), module(Module.controlMovement),
      module(Module.pathMovement), module(Module.followMovement) });
    addShapeless(module(Module.camera), new Object[] { module(Module.useless3), Blocks.field_150359_w, Blocks.field_150429_aA });
    addShapeless(module(Module.weapon1), new Object[] { module(Module.useless1), Items.field_151065_br, Items.field_151042_j });
    addShapeless(module(Module.weapon2), new Object[] { module(Module.useless2), Items.field_151065_br, Items.field_151043_k });
    addShapeless(module(Module.weapon3), new Object[] { module(Module.useless3), Items.field_151065_br, Items.field_151045_i });
    addShapeless(module(Module.weapon4), new Object[] { module(Module.useless4), Items.field_151065_br, Items.field_151166_bC });
    addShapeless(module(Module.armor1), new Object[] { module(Module.useless1), Items.field_151116_aA, Items.field_151042_j });
    addShapeless(module(Module.armor2), new Object[] { module(Module.useless2), Items.field_151116_aA, Items.field_151043_k });
    addShapeless(module(Module.armor3), new Object[] { module(Module.useless3), Items.field_151116_aA, Items.field_151045_i });
    addShapeless(module(Module.armor4), new Object[] { module(Module.useless4), Items.field_151116_aA, Items.field_151166_bC });
    addShapeless(module(Module.shooting), new Object[] { module(Module.useless1), Items.field_151031_f, Items.field_151065_br });
    addShapeless(module(Module.shootingHoming), new Object[] { module(Module.useless2), module(Module.shooting), new ItemStack(droneBit, 1, 1) });

    addShapeless(module(Module.batterySave1), new Object[] { module(Module.useless1), Items.field_151042_j, Items.field_151174_bG });
    addShapeless(module(Module.batterySave2), new Object[] { module(Module.useless2), Items.field_151043_k, Items.field_151174_bG });
    addShapeless(module(Module.batterySave3), new Object[] { module(Module.useless3), Items.field_151045_i, Items.field_151174_bG });
    addShapeless(module(Module.batterySave4), new Object[] { module(Module.useless4), Items.field_151166_bC, Items.field_151174_bG });
    addShapeless(module(Module.heatPower), new Object[] { module(Module.useless1), Items.field_151129_at, Blocks.field_150424_aL });
    addShapeless(module(Module.solarPower), new Object[] { module(Module.useless2), Blocks.field_150410_aZ, Blocks.field_150426_aN });
    addShapeless(module(Module.multiPower), new Object[] { module(Module.useless2), module(Module.heatPower),
      module(Module.solarPower) });
    addShapeless(module(Module.autoReturn), new Object[] { module(Module.useless2), Items.field_151111_aL });
    addShapeless(module(Module.deflect), new Object[] { module(Module.useless1), Items.field_185159_cQ, Items.field_151008_G });
    addShapeless(module(Module.deflame), new Object[] { module(Module.useless2), Items.field_151131_as, Blocks.field_150425_aM, Blocks.field_150354_m });

    addShapeless(module(Module.deexplosion), new Object[] { module(Module.useless3), Items.field_151016_H, Items.field_151061_bv, Blocks.field_150343_Z });

    addShapeless(module(Module.multiDe), new Object[] { module(Module.useless4), module(Module.deflect), module(Module.deflame),
      module(Module.deexplosion) });
    addShapeless(module(Module.mine1), new Object[] { module(Module.useless1), Items.field_151035_b, Items.field_151051_r });
    addShapeless(module(Module.mine2), new Object[] { module(Module.useless2), Items.field_151005_D, Items.field_151011_C });
    addShapeless(module(Module.mine3), new Object[] { module(Module.useless3), Items.field_151046_w, Items.field_151047_v });
    addShapeless(module(Module.mine4), new Object[] { module(Module.useless4), Items.field_151046_w, Items.field_151166_bC, Blocks.field_150343_Z });
  }


  public void registerGunUpgradeRecipes()
  {
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.explosion), new Object[] { chip1, Blocks.field_150335_W });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.scatter), new Object[] { chip1, Items.field_151123_aH, Items.field_151064_bs });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.healing), new Object[] { chip1,
      PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionType.func_185168_a("healing")) });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.doubleShot), new Object[] { chip1, chip2 });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.tripleShot), new Object[] { chip1, chip3 });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.fire), new Object[] { chip1, Items.field_151033_d, Items.field_151065_br });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.ice), new Object[] { chip1, Blocks.field_150432_aD,
      PotionUtils.func_185188_a(new ItemStack(Items.field_151068_bn), PotionType.func_185168_a("slowness")) });
  }

  public static ItemStack gunUpgrade(ItemGunUpgrade.GunUpgrade gu)
  {
    return ItemGunUpgrade.itemGunUpgrade(gu);
  }

  public static ItemStack module(Module m)
  {
    return ItemDroneModule.itemModule(m);
  }

  public static void addRecipe(ItemStack is, Object... os)
  {
    IRecipe recipe = GameRegistry.addShapedRecipe(is, os);
    addRecipeToList(recipe, getRecipeType(is));
  }

  public static void addShapeless(ItemStack is, Object... os)
  {
    List<ItemStack> list = Lists.newArrayList();
    for (Object object : os)
    {
      if ((object instanceof ItemStack))
      {
        list.add(((ItemStack)object).func_77946_l());
      }
      else if ((object instanceof Item))
      {
        list.add(new ItemStack((Item)object));
      }
      else
      {
        if (!(object instanceof Block))
        {

          throw new IllegalArgumentException("Invalid shapeless recipe: unknown type " + object.getClass().getName() + "!");
        }

        list.add(new ItemStack((Block)object));
      }
    }

    Object recipe = new ShapelessRecipes(is, list);
    GameRegistry.addRecipe((IRecipe)recipe);
    addRecipeToList((IRecipe)recipe, getRecipeType(is));
  }

  public static void addRecipeToList(IRecipe recipe, RecipeType type)
  {
    if (!recipes.containsKey(type)) recipes.put(type, new LinkedList());
    LinkedList<IRecipe> list = (LinkedList)recipes.get(type);
    list.add(recipe);
    recipes.put(type, list);
  }

  public static RecipeType getRecipeType(ItemStack is)
  {
    if ((is == null) || (is.func_77973_b() == null)) return RecipeType.None;
    if ((is.func_77973_b() instanceof ItemDronePart)) return RecipeType.Parts;
    if ((is.func_77973_b() instanceof ItemDroneModule)) return RecipeType.Modules;
    if ((is.func_77973_b() instanceof ItemDroneSpawn)) return RecipeType.Drones;
    if ((is.func_77973_b() instanceof IItemWeapon)) return RecipeType.Weapons;
    if ((is.func_77973_b() instanceof ItemDroneFlyer)) return RecipeType.Tools;
    if ((is.func_77973_b() instanceof ItemDronePainter)) return RecipeType.Tools;
    if ((is.func_77973_b() instanceof ItemDroneScrew)) return RecipeType.Tools;
    if ((is.func_77973_b() instanceof ItemGunUpgrade)) return RecipeType.Upgrades;
    if ((is.func_77973_b() instanceof ItemBlock))
    {
      Block b = ((ItemBlock)is.func_77973_b()).func_179223_d();
      if ((b instanceof BlockCrafter)) return RecipeType.Tools;
    }
    return RecipeType.None;
  }

  public static enum RecipeType
  {
    Parts,  Modules,  Drones,  Weapons,  Upgrades,  Tools,  None;

    private RecipeType() {} }
    public static LinkedHashMap<RecipeType, LinkedList<IRecipe>> recipes = new LinkedHashMap();
}
