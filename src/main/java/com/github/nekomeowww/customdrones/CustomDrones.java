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

@Mod(name="Custom Drones", modid="drones", version="1.6.1-beta.f3", guiFactory="com.github.nekomeowww.customdrones.GuiFactory", acceptedMinecraftVersions="[1.10.2]")
public class CustomDrones
{
  public static final String MODNAME = "Custom Drones";
  public static final String MODID = "drones";
  public static final String VERSION = "1.6.1-beta.f3";
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

    public Item getTabIconItem()
    {
      return CustomDrones.droneSpawn;
    }
  };

  public static AchievementPage achievementPage;

  public static ItemDroneSpawn droneSpawn = (ItemDroneSpawn)new ItemDroneSpawn().setCreativeTab(droneTab).setUnlocalizedName("droneSpawn");

  public static ItemDroneFlyer droneFlyer = (ItemDroneFlyer)new ItemDroneFlyer().setCreativeTab(droneTab).setUnlocalizedName("droneFlyer");

  public static ItemDronePainter dronePainter = (ItemDronePainter)new ItemDronePainter().setCreativeTab(droneTab).setUnlocalizedName("dronePainter");

  public static ItemDroneScrew droneScrew = (ItemDroneScrew)new ItemDroneScrew().setCreativeTab(droneTab).setUnlocalizedName("droneScrew");

  public static ItemDroneBit droneBit = (ItemDroneBit)new ItemDroneBit().setCreativeTab(droneTab).setUnlocalizedName("droneBit");

  public static ItemDronePart cfPlate1 = (ItemDronePart)new ItemDronePart(56, 1).setCreativeTab(droneTab).setUnlocalizedName("cfPlate1");

  public static ItemDronePart cfPlate2 = (ItemDronePart)new ItemDronePart(56, 2).setCreativeTab(droneTab).setUnlocalizedName("cfPlate2");

  public static ItemDronePart cfPlate3 = (ItemDronePart)new ItemDronePart(56, 3).setCreativeTab(droneTab).setUnlocalizedName("cfPlate3");

  public static ItemDronePart cfPlate4 = (ItemDronePart)new ItemDronePart(56, 4).setCreativeTab(droneTab).setUnlocalizedName("cfPlate4");

  public static ItemDronePart case1 = (ItemDronePart)new ItemDronePart(1).setCreativeTab(droneTab).setUnlocalizedName("case1");

  public static ItemDronePart case2 = (ItemDronePart)new ItemDronePart(2).setCreativeTab(droneTab).setUnlocalizedName("case2");

  public static ItemDronePart case3 = (ItemDronePart)new ItemDronePart(3).setCreativeTab(droneTab).setUnlocalizedName("case3");

  public static ItemDronePart case4 = (ItemDronePart)new ItemDronePart(4).setCreativeTab(droneTab).setUnlocalizedName("case4");

  public static ItemDronePart chip1 = (ItemDronePart)new ItemDronePart(1).setCreativeTab(droneTab).setUnlocalizedName("chip1");

  public static ItemDronePart chip2 = (ItemDronePart)new ItemDronePart(2).setCreativeTab(droneTab).setUnlocalizedName("chip2");

  public static ItemDronePart chip3 = (ItemDronePart)new ItemDronePart(3).setCreativeTab(droneTab).setUnlocalizedName("chip3");

  public static ItemDronePart chip4 = (ItemDronePart)new ItemDronePart(4).setCreativeTab(droneTab).setUnlocalizedName("chip4");

  public static ItemDronePart core1 = (ItemDronePart)new ItemDronePart(1).setCreativeTab(droneTab).setUnlocalizedName("core1");

  public static ItemDronePart core2 = (ItemDronePart)new ItemDronePart(2).setCreativeTab(droneTab).setUnlocalizedName("core2");

  public static ItemDronePart core3 = (ItemDronePart)new ItemDronePart(3).setCreativeTab(droneTab).setUnlocalizedName("core3");

  public static ItemDronePart core4 = (ItemDronePart)new ItemDronePart(4).setCreativeTab(droneTab).setUnlocalizedName("core4");

  public static ItemDronePart engine1 = (ItemDronePart)new ItemDronePart(1).setCreativeTab(droneTab).setUnlocalizedName("engine1");

  public static ItemDronePart engine2 = (ItemDronePart)new ItemDronePart(2).setCreativeTab(droneTab).setUnlocalizedName("engine2");

  public static ItemDronePart engine3 = (ItemDronePart)new ItemDronePart(3).setCreativeTab(droneTab).setUnlocalizedName("engine3");

  public static ItemDronePart engine4 = (ItemDronePart)new ItemDronePart(4).setCreativeTab(droneTab).setUnlocalizedName("engine4");

  public static ItemDroneModule droneModule = (ItemDroneModule)new ItemDroneModule().setCreativeTab(droneTab).setUnlocalizedName("droneModule");
  public static Block crafter = new BlockCrafter().setCreativeTab(droneTab).setUnlocalizedName("crafter");

  public static Item plasmaGun = new ItemPlasmaGun(false).setCreativeTab(droneTab).setUnlocalizedName("plasmaGun");
  public static Item plasmaGunHoming = new ItemPlasmaGun(true).setCreativeTab(droneTab)
    .setUnlocalizedName("plasmaGunHoming");
  public static Item gunUpgrade = new ItemGunUpgrade().setCreativeTab(droneTab).setUnlocalizedName("gunUpgrade");

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
    addRecipe(new ItemStack(cfPlate1, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.IRON_INGOT, Character.valueOf('I'), droneBit, Character.valueOf('D'), Items.DIAMOND });

    addRecipe(new ItemStack(cfPlate2, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.GOLD_INGOT, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate1 });
    addRecipe(new ItemStack(cfPlate3, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.DIAMOND, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate2 });
    addRecipe(new ItemStack(cfPlate4, 7), new Object[] { "IDI", "IEI", "III", Character.valueOf('E'), Items.EMERALD, Character.valueOf('D'), droneBit, Character.valueOf('I'), cfPlate3 });

    addRecipe(new ItemStack(case1), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate1 });
    addRecipe(new ItemStack(case2), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate2 });
    addRecipe(new ItemStack(case3), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate3 });
    addRecipe(new ItemStack(case4), new Object[] { "PPP", "PPP", "PPP", Character.valueOf('P'), cfPlate4 });
    addRecipe(new ItemStack(chip1), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.REDSTONE });
    addRecipe(new ItemStack(chip2), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.REDSTONE });
    addRecipe(new ItemStack(chip3), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.REDSTONE });
    addRecipe(new ItemStack(chip4), new Object[] { "P", "R", "P", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.REDSTONE });
    addRecipe(new ItemStack(core1), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.BLAZE_POWDER });
    addRecipe(new ItemStack(core2), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.BLAZE_POWDER });
    addRecipe(new ItemStack(core3), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.BLAZE_POWDER });
    addRecipe(new ItemStack(core4), new Object[] { " P ", "PRP", " P ", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.BLAZE_POWDER });
    addRecipe(new ItemStack(engine1), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate1, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('C'), chip1 });
    addRecipe(new ItemStack(engine2), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate2, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('C'), chip2 });
    addRecipe(new ItemStack(engine3), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate3, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('C'), chip3 });
    addRecipe(new ItemStack(engine4), new Object[] { "PRP", "RCR", "PRP", Character.valueOf('P'), cfPlate4, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('C'), chip4 });

    addRecipe(new ItemStack(droneFlyer), new Object[] { " R ", "ECE", "III", Character.valueOf('I'), Items.IRON_INGOT, Character.valueOf('E'), Items.ENDER_PEARL, Character.valueOf('R'), Items.REDSTONE,
    Character.valueOf('C'), chip1 });
    addShapeless(new ItemStack(dronePainter), new Object[] { Items.IRON_INGOT, Items.REDSTONE, new ItemStack(Items.DYE, 1, 1), new ItemStack(Items.DYE, 1, 2), new ItemStack(Items.DYE, 1, 4) });

    addShapeless(new ItemStack(droneScrew), new Object[] { Items.IRON_INGOT, Items.GOLD_INGOT, Items.REDSTONE, cfPlate1 });
    addShapeless(new ItemStack(crafter), new Object[] { Blocks.CRAFTING_TABLE, chip1, case1 });

    //Temporarily disabled the Plasma Gun and Gun Upgrade
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
    addShapeless(module(Module.itemInventory), new Object[] { module(Module.useless1), Blocks.CHEST });
    addShapeless(module(Module.nplayerTransport), new Object[] { module(Module.useless2), Items.FEATHER, Items.SLIME_BALL });
    addShapeless(module(Module.playerTransport), new Object[] { module(Module.useless3), Items.FEATHER, Items.SLIME_BALL });
    addShapeless(module(Module.multiTransport), new Object[] { module(Module.useless4), module(Module.itemInventory),
      module(Module.nplayerTransport), module(Module.playerTransport) });
    addShapeless(module(Module.itemCollect), new Object[] { module(Module.useless1), Items.FISHING_ROD });
    addShapeless(module(Module.xpCollect), new Object[] { module(Module.useless2), Items.FISHING_ROD, Items.GLASS_BOTTLE });
    addShapeless(module(Module.multiCollect), new Object[] { module(Module.useless3), module(Module.itemCollect),
      module(Module.xpCollect) });
    addShapeless(module(Module.chestDeposit), new Object[] { module(Module.useless2), Items.REPEATER, Items.COMPARATOR });
    addShapeless(module(Module.mobScan1), new Object[] { module(Module.useless1), Items.PORKCHOP, Items.LEATHER, Items.EGG });
    addShapeless(module(Module.mobScan2), new Object[] { module(Module.useless2), Items.PORKCHOP, Items.LEATHER, Items.EGG, Items.ROTTEN_FLESH, Items.GUNPOWDER, Items.BONE });

    addShapeless(module(Module.oreScan), new Object[] { module(Module.useless1), Items.COAL, Items.REDSTONE, Items.IRON_INGOT, Items.GOLD_INGOT, Items.DIAMOND, Items.EMERALD, new ItemStack(Items.DYE, 11) });

    addShapeless(module(Module.multiScan), new Object[] { module(Module.useless2), module(Module.mobScan2),
      module(Module.oreScan) });
    addShapeless(module(Module.controlMovement), new Object[] { module(Module.useless1), cfPlate1 });
    addShapeless(module(Module.pathMovement), new Object[] { module(Module.useless1), cfPlate1, Items.REPEATER });
    addShapeless(module(Module.followMovement), new Object[] { module(Module.useless1), cfPlate1, Items.LEAD });
    addShapeless(module(Module.multiMovement), new Object[] { module(Module.useless2), module(Module.controlMovement),
      module(Module.pathMovement), module(Module.followMovement) });
    addShapeless(module(Module.camera), new Object[] { module(Module.useless3), Blocks.GLASS, Blocks.REDSTONE_TORCH });
    addShapeless(module(Module.weapon1), new Object[] { module(Module.useless1), Items.BLAZE_POWDER, Items.IRON_INGOT });
    addShapeless(module(Module.weapon2), new Object[] { module(Module.useless2), Items.BLAZE_POWDER, Items.GOLD_INGOT });
    addShapeless(module(Module.weapon3), new Object[] { module(Module.useless3), Items.BLAZE_POWDER, Items.DIAMOND });
    addShapeless(module(Module.weapon4), new Object[] { module(Module.useless4), Items.BLAZE_POWDER, Items.EMERALD });
    addShapeless(module(Module.armor1), new Object[] { module(Module.useless1), Items.LEATHER, Items.IRON_INGOT });
    addShapeless(module(Module.armor2), new Object[] { module(Module.useless2), Items.LEATHER, Items.GOLD_INGOT });
    addShapeless(module(Module.armor3), new Object[] { module(Module.useless3), Items.LEATHER, Items.DIAMOND });
    addShapeless(module(Module.armor4), new Object[] { module(Module.useless4), Items.LEATHER, Items.EMERALD });
    addShapeless(module(Module.shooting), new Object[] { module(Module.useless1), Items.BOW, Items.BLAZE_POWDER });
    addShapeless(module(Module.shootingHoming), new Object[] { module(Module.useless2), module(Module.shooting), new ItemStack(droneBit, 1, 1) });

    addShapeless(module(Module.batterySave1), new Object[] { module(Module.useless1), Items.IRON_INGOT, Items.POTATO });
    addShapeless(module(Module.batterySave2), new Object[] { module(Module.useless2), Items.GOLD_INGOT, Items.POTATO });
    addShapeless(module(Module.batterySave3), new Object[] { module(Module.useless3), Items.DIAMOND, Items.POTATO });
    addShapeless(module(Module.batterySave4), new Object[] { module(Module.useless4), Items.EMERALD, Items.POTATO });
    addShapeless(module(Module.heatPower), new Object[] { module(Module.useless1), Items.LAVA_BUCKET, Blocks.NETHERRACK });
    addShapeless(module(Module.solarPower), new Object[] { module(Module.useless2), Blocks.GLASS_PANE, Blocks.GLOWSTONE });
    addShapeless(module(Module.multiPower), new Object[] { module(Module.useless2), module(Module.heatPower),
      module(Module.solarPower) });
    addShapeless(module(Module.autoReturn), new Object[] { module(Module.useless2), Items.COMPASS });
    addShapeless(module(Module.deflect), new Object[] { module(Module.useless1), Items.SHIELD, Items.FEATHER });
    addShapeless(module(Module.deflame), new Object[] { module(Module.useless2), Items.WATER_BUCKET, Blocks.SOUL_SAND, Blocks.SAND });

    addShapeless(module(Module.deexplosion), new Object[] { module(Module.useless3), Items.GUNPOWDER, Items.ENDER_EYE, Blocks.OBSIDIAN });

    addShapeless(module(Module.multiDe), new Object[] { module(Module.useless4), module(Module.deflect), module(Module.deflame),
      module(Module.deexplosion) });
    addShapeless(module(Module.mine1), new Object[] { module(Module.useless1), Items.IRON_PICKAXE, Items.STONE_SHOVEL });
    addShapeless(module(Module.mine2), new Object[] { module(Module.useless2), Items.GOLDEN_PICKAXE, Items.GOLDEN_SHOVEL });
    addShapeless(module(Module.mine3), new Object[] { module(Module.useless3), Items.DIAMOND_PICKAXE, Items.DIAMOND_SHOVEL });
    addShapeless(module(Module.mine4), new Object[] { module(Module.useless4), Items.DIAMOND_PICKAXE, Items.EMERALD, Blocks.OBSIDIAN });
  }


  public void registerGunUpgradeRecipes()
  {
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.explosion), new Object[] { chip1, Blocks.TNT });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.scatter), new Object[] { chip1, Items.SLIME_BALL, Items.MAGMA_CREAM });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.healing), new Object[] { chip1,
      PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionType.getPotionTypeForName("healing")) });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.doubleShot), new Object[] { chip1, chip2 });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.tripleShot), new Object[] { chip1, chip3 });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.fire), new Object[] { chip1, Items.FLINT_AND_STEEL, Items.BLAZE_POWDER });
    addShapeless(gunUpgrade(ItemGunUpgrade.GunUpgrade.ice), new Object[] { chip1, Blocks.ICE,
      PotionUtils.addPotionToItemStack(new ItemStack(Items.POTIONITEM), PotionType.getPotionTypeForName("slowness")) });
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
        list.add(((ItemStack)object).copy());
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
    if ((is == null) || (is.getItem() == null)) return RecipeType.None;
    if ((is.getItem() instanceof ItemDronePart)) return RecipeType.Parts;
    if ((is.getItem() instanceof ItemDroneModule)) return RecipeType.Modules;
    if ((is.getItem() instanceof ItemDroneSpawn)) return RecipeType.Drones;
    if ((is.getItem() instanceof IItemWeapon)) return RecipeType.Weapons;
    if ((is.getItem() instanceof ItemDroneFlyer)) return RecipeType.Tools;
    if ((is.getItem() instanceof ItemDronePainter)) return RecipeType.Tools;
    if ((is.getItem() instanceof ItemDroneScrew)) return RecipeType.Tools;
    if ((is.getItem() instanceof ItemGunUpgrade)) return RecipeType.Upgrades;
    if ((is.getItem() instanceof ItemBlock))
    {
      Block b = ((ItemBlock)is.getItem()).getBlock();
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
