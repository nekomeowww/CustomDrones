/*     */ package williamle.drones.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.settings.GameSettings;
/*     */ import net.minecraft.creativetab.CreativeTabs;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.client.FMLClientHandler;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.entity.EntityDroneBabyBig;
/*     */ import williamle.drones.entity.EntityDroneMob;
/*     */ import williamle.drones.entity.EntityDroneWildItem;
/*     */ 
/*     */ public class ItemDroneSpawn extends Item
/*     */ {
/*     */   public ItemDroneSpawn()
/*     */   {
/*  30 */     func_77625_d(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EnumActionResult func_180614_a(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
/*     */   {
/*  37 */     if (!worldIn.field_72995_K)
/*     */     {
/*  39 */       DroneInfo di = getDroneInfo(stack).copy();
/*  40 */       boolean allSameHigh = (di.chip == di.core) && (di.core == di.casing) && (di.casing == di.engine) && (di.chip > 1);
/*  41 */       if ((allSameHigh) && (playerIn.field_71075_bZ.field_75098_d) && (playerIn.func_70093_af()))
/*     */       {
/*  43 */         EntityDroneMob drone = null;
/*  44 */         if (di.chip == 2) { drone = new williamle.drones.entity.EntityDroneBaby(worldIn);
/*  45 */         } else if (di.chip == 3) { drone = new EntityDroneBabyBig(worldIn);
/*  46 */         } else if (di.chip == 4) drone = new EntityDroneWildItem(worldIn);
/*  47 */         if (drone != null)
/*     */         {
/*  49 */           drone.setDroneInfo(new DroneInfo(drone).newID());
/*  50 */           drone.func_70107_b(pos.func_177958_n() + 0.5D, pos.func_177956_o() + 1, pos.func_177952_p() + 0.5D);
/*  51 */           drone.onInitSpawn();
/*  52 */           worldIn.func_72838_d(drone);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/*  57 */         EntityDrone drone = new EntityDrone(worldIn);
/*  58 */         drone.setDroneInfo(getDroneInfo(stack).newID());
/*  59 */         drone.func_70107_b(pos.func_177958_n() + 0.5D, pos.func_177956_o() + 1, pos.func_177952_p() + 0.5D);
/*  60 */         worldIn.func_72838_d(drone);
/*  61 */         stack.field_77994_a -= 1;
/*     */       }
/*     */     }
/*  64 */     return EnumActionResult.SUCCESS;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/*  70 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/*  71 */     DroneInfo di = getDroneInfo(stack);
/*  72 */     if (!di.name.equals("#$Drone"))
/*     */     {
/*  74 */       String s = (String)tooltip.get(0) + TextFormatting.AQUA + " " + TextFormatting.BOLD + di.getDisplayName();
/*  75 */       tooltip.set(0, s);
/*     */     }
/*  77 */     tooltip.add(TextFormatting.BLUE + "Casing: " + DroneInfo.greekNumber[di.casing] + " / " + "Chip: " + DroneInfo.greekNumber[di.chip] + " / " + "Core: " + DroneInfo.greekNumber[di.core] + " / " + "Engine: " + DroneInfo.greekNumber[di.engine]);
/*     */     
/*     */ 
/*  80 */     TextFormatting form = di.mods.size() == di.getMaxModSlots() ? TextFormatting.RED : TextFormatting.GREEN;
/*  81 */     tooltip.add(form + "Installed mods: " + di.mods.size() + "/" + di.getMaxModSlots());
/*  82 */     form = di.getBattery(false) / di.getMaxBattery() <= 0.25D ? TextFormatting.RED : TextFormatting.GREEN;
/*  83 */     tooltip.add(form + "Battery: " + di.getBattery(true) + "/" + di.getMaxBattery());
/*  84 */     form = di.getDamage(false) / di.getMaxDamage(null) <= 0.25D ? TextFormatting.RED : TextFormatting.GREEN;
/*  85 */     tooltip.add(form + "Durabilty: " + di.getDamage(true) + "/" + di.getMaxDamage(null));
/*  86 */     tooltip.add(TextFormatting.GREEN + "Speed: " + di.getMaxSpeed() + "m/s");
/*     */   }
/*     */   
/*     */   public void setDroneInfo(ItemStack stack, DroneInfo info)
/*     */   {
/*  91 */     if (!stack.func_77942_o())
/*     */     {
/*  93 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/*  95 */     info.writeToNBT(stack.func_77978_p());
/*     */   }
/*     */   
/*     */   public DroneInfo getDroneInfo(ItemStack stack)
/*     */   {
/* 100 */     if (stack.func_77942_o())
/*     */     {
/* 102 */       return DroneInfo.fromNBT(stack.func_77978_p());
/*     */     }
/*     */     
/*     */ 
/* 106 */     stack.func_77982_d(new NBTTagCompound());
/* 107 */     return new DroneInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_150895_a(Item itemIn, CreativeTabs tab, List<ItemStack> subItems)
/*     */   {
/* 114 */     super.func_150895_a(itemIn, tab, subItems);
/* 115 */     if ((tab == DronesMod.droneTab) && (FMLClientHandler.instance().getClient().field_71474_y.field_82882_x))
/*     */     {
/* 117 */       for (int a = 1; a < 5; a++)
/*     */       {
/* 119 */         for (int b = 1; b < 5; b++)
/*     */         {
/* 121 */           for (int c = 1; c < 5; c++)
/*     */           {
/* 123 */             for (int d = 1; d < 5; d++)
/*     */             {
/* 125 */               if ((a != 1) || (b != 1) || (c != 1) || (d != 1))
/*     */               {
/* 127 */                 ItemStack is = new ItemStack(DronesMod.droneSpawn);
/* 128 */                 setDroneInfo(is, new DroneInfo(b, c, a, d));
/* 129 */                 subItems.add(is);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDroneSpawn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */