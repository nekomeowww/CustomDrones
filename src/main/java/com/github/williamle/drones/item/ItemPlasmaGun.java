/*     */ package williamle.drones.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.init.SoundEvents;
/*     */ import net.minecraft.item.EnumRarity;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.util.ActionResult;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.CommonProxy;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.Filters.FilterExcepts;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.api.helpers.WorldHelper;
/*     */ import williamle.drones.api.model.Color;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityHomingBox;
/*     */ import williamle.drones.entity.EntityPlasmaShot;
/*     */ 
/*     */ public class ItemPlasmaGun extends Item implements IItemWeapon
/*     */ {
/*     */   public boolean homing;
/*     */   
/*     */   public ItemPlasmaGun(boolean b)
/*     */   {
/*  41 */     func_77625_d(1);
/*  42 */     func_77656_e(2000);
/*  43 */     this.homing = b;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  49 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/*  50 */     if (this.homing)
/*     */     {
/*  52 */       tooltip.add("Max homing range: 64 blocks");
/*  53 */       tooltip.add("Damage: 2.5 hearts");
/*     */     } else {
/*  55 */       tooltip.add("Damage: 1.5 hearts"); }
/*  56 */     List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(stack);
/*  57 */     if (!upgrades.isEmpty())
/*     */     {
/*  59 */       tooltip.add("Gun upgrades");
/*  60 */       for (int a = 0; a < upgrades.size(); a++)
/*     */       {
/*  62 */         ItemGunUpgrade.GunUpgrade gu = (ItemGunUpgrade.GunUpgrade)upgrades.get(a);
/*  63 */         String s = a + 1 + ". " + TextFormatting.YELLOW + gu.name + TextFormatting.RESET;
/*  64 */         if ((gu.desc != null) && (!gu.desc.isEmpty())) s = s + ": " + TextFormatting.ITALIC + gu.desc;
/*  65 */         tooltip.add(s);
/*     */       }
/*     */     }
/*  68 */     if (advanced)
/*     */     {
/*  70 */       tooltip.add("Battery: " + (func_77612_l() - getDamage(stack)) + "/" + func_77612_l());
/*  71 */       int cost = 10;
/*  72 */       Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
/*  73 */       if (target != null) cost = (int)(cost + Math.pow(playerIn.func_70032_d(target), 0.5D));
/*  74 */       int upgradeCost = 0;
/*  75 */       double costScale = 1.0D;
/*  76 */       for (ItemGunUpgrade.GunUpgrade gu : upgrades)
/*     */       {
/*  78 */         upgradeCost += gu.cost;
/*  79 */         costScale *= gu.costScale;
/*     */       }
/*  81 */       int totalCost = (int)((cost + upgradeCost) * costScale);
/*  82 */       tooltip.add(totalCost + " batteries per shot");
/*     */     }
/*     */   }
/*     */   
/*     */   public static boolean addUpgrade(ItemStack is, ItemGunUpgrade.GunUpgrade upgrade)
/*     */   {
/*  88 */     List<ItemGunUpgrade.GunUpgrade> list = getUpgrades(is);
/*  89 */     boolean b = list.contains(upgrade) ? false : list.add(upgrade);
/*  90 */     updateUpgrades(is, list);
/*  91 */     return b;
/*     */   }
/*     */   
/*     */   public static boolean removeUpgrade(ItemStack is, ItemGunUpgrade.GunUpgrade upgrade)
/*     */   {
/*  96 */     List<ItemGunUpgrade.GunUpgrade> list = getUpgrades(is);
/*  97 */     boolean b = list.remove(upgrade);
/*  98 */     updateUpgrades(is, list);
/*  99 */     return b;
/*     */   }
/*     */   
/*     */   public static void updateUpgrades(ItemStack is, List<ItemGunUpgrade.GunUpgrade> list)
/*     */   {
/* 104 */     is.func_179543_a("Gun Upgrade", true).func_74778_a("Upgrades", ItemGunUpgrade.GunUpgrade.upgradesToString(list));
/*     */   }
/*     */   
/*     */   public static List<ItemGunUpgrade.GunUpgrade> getUpgrades(ItemStack is)
/*     */   {
/* 109 */     return ItemGunUpgrade.GunUpgrade.getUpgrades(is.func_179543_a("Gun Upgrade", true), "Upgrades");
/*     */   }
/*     */   
/*     */ 
/*     */   public ActionResult<ItemStack> func_77659_a(ItemStack is, World worldIn, EntityPlayer player, EnumHand hand)
/*     */   {
/* 115 */     List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(is);
/* 116 */     Random rnd = new Random();
/* 117 */     Entity target = this.homing ? getHomingTarget(player, is) : null;
/*     */     
/* 119 */     int shotCount = 1;
/* 120 */     if (upgrades.contains(ItemGunUpgrade.GunUpgrade.doubleShot)) shotCount *= 2;
/* 121 */     if (upgrades.contains(ItemGunUpgrade.GunUpgrade.tripleShot)) shotCount *= 3;
/* 122 */     double jitter = (shotCount - 1) * 0.1D;
/* 123 */     boolean jitterPos = shotCount == 1;
/* 124 */     if (!worldIn.field_72995_K)
/*     */     {
/* 126 */       for (int a = 0; a < shotCount; a++)
/*     */       {
/* 128 */         EntityPlasmaShot shot = new EntityPlasmaShot(worldIn);
/*     */         
/* 130 */         shot.shooter = player;
/* 131 */         shot.setHoming(target);
/* 132 */         Vec3d pos = EntityHelper.getEyeVec(player);
/* 133 */         Vec3d posAdd = player.func_70040_Z().func_186678_a(player.field_70130_N / 2.0F);
/* 134 */         if (jitterPos)
/*     */         {
/* 136 */           posAdd = VecHelper.jitter(posAdd, 0.7853981633974483D);
/*     */         }
/* 138 */         pos = pos.func_178787_e(posAdd);
/* 139 */         shot.func_70107_b(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c);
/* 140 */         Vec3d speed = VecHelper.jitter(player.func_70040_Z().func_186678_a(this.homing ? 2.0D : 2.5D), jitter * 3.141592653589793D / 4.0D);
/* 141 */         shot.field_70159_w = speed.field_72450_a;
/* 142 */         shot.field_70181_x = speed.field_72448_b;
/* 143 */         shot.field_70179_y = speed.field_72449_c;
/* 144 */         shot.damage = (this.homing ? 5.0D : 3.0D);
/* 145 */         shot.color = new Color(1.0D, 0.2D, 0.2D, 1.0D);
/* 146 */         if (upgrades.contains(ItemGunUpgrade.GunUpgrade.healing))
/*     */         {
/* 148 */           shot.damage *= -1.0D;
/* 149 */           shot.color = new Color(0.0D, 1.0D, 0.1D, 1.0D);
/*     */         }
/* 151 */         if (upgrades.contains(ItemGunUpgrade.GunUpgrade.ice))
/* 152 */           shot.color = shot.color.blendNormal(new Color(0.0D, 1.0D, 1.0D, 1.0D), 0.5D);
/* 153 */         if (upgrades.contains(ItemGunUpgrade.GunUpgrade.fire))
/* 154 */           shot.color = shot.color.blendNormal(new Color(1.0D, 0.6D, 0.0D, 1.0D), 0.5D);
/* 155 */         shot.setUpgrades(upgrades);
/*     */         
/* 157 */         worldIn.func_72838_d(shot);
/*     */       }
/*     */     }
/* 160 */     player.func_184185_a(SoundEvents.field_187853_gC, 0.4F, 1.2F + new Random().nextFloat() * 0.8F);
/* 161 */     consumeBatteryAuto(player, is);
/* 162 */     return new ActionResult(EnumActionResult.SUCCESS, is);
/*     */   }
/*     */   
/*     */   public int getBatteryCost(EntityPlayer playerIn, ItemStack stack)
/*     */   {
/* 167 */     int cost = 10;
/* 168 */     Entity target = this.homing ? getHomingTarget(playerIn, stack) : null;
/* 169 */     if (target != null) cost = (int)(cost + Math.pow(playerIn.func_70032_d(target), 0.5D));
/* 170 */     int upgradeCost = 0;
/* 171 */     double costScale = 1.0D;
/* 172 */     List<ItemGunUpgrade.GunUpgrade> upgrades = getUpgrades(stack);
/* 173 */     for (ItemGunUpgrade.GunUpgrade gu : upgrades)
/*     */     {
/* 175 */       upgradeCost += gu.cost;
/* 176 */       costScale *= gu.costScale;
/*     */     }
/* 178 */     int totalCost = (int)((cost + upgradeCost) * costScale);
/* 179 */     return totalCost;
/*     */   }
/*     */   
/*     */   public void consumeBatteryAuto(EntityPlayer player, ItemStack is)
/*     */   {
/* 184 */     int totalCost = getBatteryCost(player, is);
/* 185 */     for (int a = 0; (
/* 186 */           a < player.field_71071_by.func_70302_i_()) && (is.func_77952_i() >= func_77612_l() - totalCost); a++)
/*     */     {
/* 188 */       if ((player.field_71071_by.func_70301_a(a) != null) && (player.field_71071_by.func_70301_a(a).field_77994_a > 0))
/*     */       {
/* 190 */         Double i = (Double)DroneInfo.batteryFuel.get(player.field_71071_by.func_70301_a(a).func_77973_b());
/* 191 */         if ((i != null) && (i.doubleValue() > 0.0D))
/*     */         {
/* 193 */           while (is.func_77952_i() >= func_77612_l() - totalCost)
/*     */           {
/* 195 */             is.func_77964_b((int)(is.func_77952_i() - i.doubleValue()));
/* 196 */             player.field_71071_by.func_70298_a(a, 1);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 201 */     is.func_77972_a(totalCost, player);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_77663_a(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected)
/*     */   {
/* 207 */     super.func_77663_a(stack, worldIn, entityIn, itemSlot, isSelected);
/*     */     
/* 209 */     EntityPlayer p = DronesMod.proxy.getClientPlayer();
/* 210 */     if ((this.homing) && (p == entityIn) && (isSelected))
/*     */     {
/* 212 */       World world = p.field_70170_p;
/* 213 */       Entity homingTarget = getHomingTarget(p, stack);
/* 214 */       EntityHomingBox box = null;
/* 215 */       List<EntityHomingBox> boxes = world.func_175644_a(EntityHomingBox.class, EntitySelectors.field_180132_d);
/* 216 */       if (boxes.isEmpty())
/*     */       {
/* 218 */         box = new EntityHomingBox(world);
/* 219 */         world.func_72838_d(box);
/*     */       } else {
/* 221 */         box = (EntityHomingBox)boxes.get(0); }
/* 222 */       if (box != null)
/*     */       {
/* 224 */         if (homingTarget != null)
/*     */         {
/* 226 */           box.target = homingTarget;
/* 227 */           box.func_70107_b(homingTarget.field_70165_t, homingTarget.field_70163_u, homingTarget.field_70161_v);
/*     */         }
/*     */         else
/*     */         {
/* 231 */           box.target = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Entity getHomingTarget(EntityPlayer player, ItemStack stack)
/*     */   {
/* 239 */     Filters.FilterExcepts filter = new Filters.FilterExcepts(new Object[] { player, EntityXPOrb.class, EntityItem.class, EntityHomingBox.class, IProjectile.class });
/*     */     
/* 241 */     double range = 64.0D;
/* 242 */     double maxAngle = 15.0D;
/* 243 */     AxisAlignedBB aabb = player.func_174813_aQ().func_186662_g(range);
/* 244 */     Vec3d pLook = player.func_70040_Z();
/* 245 */     Vec3d pEye = EntityHelper.getEyeVec(player);
/* 246 */     return WorldHelper.getEntityBestInAngle(player.field_70170_p, pEye, pLook, aabb, maxAngle, player, filter);
/*     */   }
/*     */   
/*     */ 
/*     */   public EnumRarity func_77613_e(ItemStack stack)
/*     */   {
/* 252 */     if (this.homing) return EnumRarity.RARE;
/* 253 */     return super.func_77613_e(stack);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemPlasmaGun.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */