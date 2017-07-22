/*     */ package williamle.drones.item;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ActionResult;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.DronesMod;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ItemGunUpgrade
/*     */   extends Item
/*     */ {
/*     */   public static class GunUpgrade
/*     */   {
/*  29 */     public static Map<String, GunUpgrade> upgrades = new HashMap();
/*     */     
/*  31 */     public static GunUpgrade explosion = new GunUpgrade("explosion", "Explosion", "Explodes!").setWeaponCosts(10, 1.0D);
/*     */     
/*  33 */     public static GunUpgrade scatter = new GunUpgrade("scatter", "Scatter", "Creates multiple shots on hit.")
/*  34 */       .setWeaponCosts(1, 4.0D);
/*  35 */     public static GunUpgrade healing = new GunUpgrade("heal", "Healing", "Heals target.").setWeaponCosts(2, 1.0D);
/*  36 */     public static GunUpgrade doubleShot = new GunUpgrade("double", "Double Shot", "Twice the bullets.")
/*  37 */       .setWeaponCosts(0, 1.5D);
/*  38 */     public static GunUpgrade tripleShot = new GunUpgrade("triple", "Triple Shot", "Triple the bullets.")
/*  39 */       .setWeaponCosts(0, 2.0D);
/*  40 */     public static GunUpgrade fire = new GunUpgrade("fire", "Fire", "Causes fire, thaw snow & ice.")
/*  41 */       .setWeaponCosts(5, 1.0D);
/*  42 */     public static GunUpgrade ice = new GunUpgrade("ice", "Ice", "Extinguish fire, freeze water, cast snow.")
/*  43 */       .setWeaponCosts(5, 1.0D);
/*     */     public String id;
/*     */     public String name;
/*     */     public String desc;
/*     */     public int cost;
/*  48 */     public double costScale = 1.0D;
/*     */     
/*     */     public GunUpgrade(String s1, String s2, String s3)
/*     */     {
/*  52 */       this.id = s1;
/*  53 */       this.name = s2;
/*  54 */       this.desc = s3;
/*  55 */       upgrades.put(this.id, this);
/*     */     }
/*     */     
/*     */     public GunUpgrade setWeaponCosts(int i, double d)
/*     */     {
/*  60 */       this.cost = i;
/*  61 */       this.costScale = d;
/*  62 */       return this;
/*     */     }
/*     */     
/*     */     public static String upgradesToString(List<GunUpgrade> list)
/*     */     {
/*  67 */       String s = "";
/*  68 */       for (int a = 0; a < list.size(); a++)
/*     */       {
/*  70 */         if (a > 0) s = s + ";";
/*  71 */         s = s + ((GunUpgrade)list.get(a)).id;
/*     */       }
/*  73 */       return s;
/*     */     }
/*     */     
/*     */     public static List<GunUpgrade> getUpgrades(NBTTagCompound tag, String upgradeKey)
/*     */     {
/*  78 */       List<GunUpgrade> list = new ArrayList();
/*  79 */       String[] ss = tag.func_74779_i(upgradeKey).split(";");
/*  80 */       for (String s : ss)
/*     */       {
/*  82 */         GunUpgrade upgrade = (GunUpgrade)upgrades.get(s);
/*  83 */         if (upgrade != null) list.add(upgrade);
/*     */       }
/*  85 */       return list;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ItemGunUpgrade()
/*     */   {
/*  92 */     func_77625_d(16);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  98 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/*  99 */     GunUpgrade gu = getGunUpgrade(stack);
/* 100 */     if (gu != null)
/*     */     {
/* 102 */       tooltip.add(TextFormatting.YELLOW + gu.name);
/* 103 */       if ((gu.desc != null) && (!gu.desc.isEmpty())) tooltip.add(TextFormatting.ITALIC + gu.desc);
/* 104 */       tooltip.add(TextFormatting.ITALIC + "" + TextFormatting.RED + "Equip gun in off-hand and right click this upgrade to install.");
/*     */       
/* 106 */       if (advanced)
/*     */       {
/* 108 */         String cost = "";
/* 109 */         if (gu.cost != 0) cost = cost + "Battery cost: " + gu.cost;
/* 110 */         if (gu.costScale != 1.0D)
/* 111 */           cost = cost + (cost.isEmpty() ? "Battery cost scale: x" : " - cost scale: x") + gu.costScale;
/* 112 */         if (!cost.isEmpty()) { tooltip.add(cost);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
/*     */   {
/* 121 */     EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
/* 122 */     ItemStack otherHandIS = playerIn.func_184586_b(otherHand);
/* 123 */     if ((otherHandIS != null) && ((otherHandIS.func_77973_b() instanceof ItemPlasmaGun)))
/*     */     {
/* 125 */       GunUpgrade gu = getGunUpgrade(itemStackIn);
/* 126 */       if (gu != null)
/*     */       {
/* 128 */         boolean canAdd = ItemPlasmaGun.addUpgrade(otherHandIS, gu);
/* 129 */         if (canAdd)
/*     */         {
/* 131 */           itemStackIn.field_77994_a -= 1;
/*     */         }
/*     */       }
/*     */     }
/* 135 */     return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 142 */     if ((tab == DronesMod.droneTab) || (tab == null))
/*     */     {
/* 144 */       for (GunUpgrade gu : GunUpgrade.upgrades.values())
/*     */       {
/* 146 */         subItems.add(itemGunUpgrade(gu));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static void setGunUpgrade(ItemStack stack, GunUpgrade mod)
/*     */   {
/* 153 */     if (!stack.func_77942_o())
/*     */     {
/* 155 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 157 */     stack.func_77978_p().func_74778_a("Gun Upgrade", mod.id);
/*     */   }
/*     */   
/*     */   public static GunUpgrade getGunUpgrade(ItemStack stack)
/*     */   {
/* 162 */     if (!stack.func_77942_o())
/*     */     {
/* 164 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 166 */     GunUpgrade m = (GunUpgrade)GunUpgrade.upgrades.get(stack.func_77978_p().func_74779_i("Gun Upgrade"));
/* 167 */     return m;
/*     */   }
/*     */   
/*     */   public static ItemStack itemGunUpgrade(GunUpgrade m)
/*     */   {
/* 172 */     ItemStack is = new ItemStack(DronesMod.gunUpgrade);
/* 173 */     setGunUpgrade(is, m);
/* 174 */     return is;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemGunUpgrade.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */