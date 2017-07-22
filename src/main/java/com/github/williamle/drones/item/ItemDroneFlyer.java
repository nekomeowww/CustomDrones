/*     */ package williamle.drones.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.ActionResult;
/*     */ import net.minecraft.util.EnumActionResult;
/*     */ import net.minecraft.util.EnumHand;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ 
/*     */ public class ItemDroneFlyer extends Item implements IItemInteractDrone
/*     */ {
/*     */   public ItemDroneFlyer()
/*     */   {
/*  25 */     func_77625_d(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ActionResult<ItemStack> func_77659_a(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
/*     */   {
/*  32 */     EntityDrone drone = getControllingDrone(worldIn, itemStackIn);
/*  33 */     if ((!worldIn.field_72995_K) && (drone != null) && (drone.droneInfo.isChanged))
/*     */     {
/*  35 */       drone.droneInfo.updateDroneInfoToClient(playerIn);
/*     */     }
/*  37 */     if ((drone != null) && (drone.recordingPath != null))
/*     */     {
/*  39 */       drone.applyRecordPath(false);
/*  40 */       EntityHelper.addChat(playerIn, 1, TextFormatting.BOLD + "Path" + TextFormatting.RESET + " set to drone " + TextFormatting.AQUA + drone.droneInfo
/*  41 */         .getDisplayName());
/*     */     } else {
/*  43 */       playerIn.openGui(DronesMod.instance, 0, worldIn, 0, 0, 0);
/*     */     }
/*  45 */     return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer playerIn, Vec3d vec, ItemStack itemStackIn, EnumHand hand)
/*     */   {
/*  52 */     String controllerPrefix = playerIn.getDisplayNameString() + " - slot " + (playerIn.field_71071_by.field_70461_c + 1) + ": ";
/*     */     
/*  54 */     int freq = getControllerFreq(itemStackIn);
/*  55 */     if (drone.droneInfo.droneFreq == -1)
/*     */     {
/*  57 */       drone.droneInfo.droneFreq = freq;
/*  58 */       setControllingDrone(playerIn, itemStackIn, drone);
/*  59 */       EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Set drone freq: " + freq + "GHz.");
/*     */       
/*  61 */       EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Control drone " + drone.droneInfo
/*  62 */         .getDisplayName() + ".");
/*     */     }
/*  64 */     else if (drone.droneInfo.droneFreq != freq)
/*     */     {
/*  66 */       EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.RED + "Drone " + drone.droneInfo
/*  67 */         .getDisplayName() + " is set to another frequency.");
/*     */ 
/*     */ 
/*     */     }
/*  71 */     else if (getControllingDroneID(itemStackIn) == drone.getDroneID())
/*     */     {
/*  73 */       setControllingDrone(playerIn, itemStackIn, null);
/*  74 */       EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.RED + "Disconnect drone " + drone.droneInfo
/*  75 */         .getDisplayName() + ".");
/*     */     }
/*     */     else
/*     */     {
/*  79 */       setControllingDrone(playerIn, itemStackIn, drone);
/*  80 */       EntityHelper.addChat(playerIn, 1, controllerPrefix + TextFormatting.AQUA + "Control drone " + drone.droneInfo
/*  81 */         .getDisplayName() + ".");
/*     */     }
/*     */     
/*  84 */     return EnumActionResult.SUCCESS;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
/*     */   {
/*  90 */     if ((entityLiving instanceof EntityPlayer))
/*     */     {
/*  92 */       EntityDrone drone = getControllingDrone(entityLiving.field_70170_p, stack);
/*  93 */       if ((drone != null) && (drone.recordingPath != null))
/*     */       {
/*  95 */         drone.applyRecordPath(true);
/*  96 */         EntityHelper.addChat((EntityPlayer)entityLiving, 1, TextFormatting.BOLD + "Loop path" + TextFormatting.RESET + " set to drone " + TextFormatting.AQUA + drone.droneInfo
/*     */         
/*  98 */           .getDisplayName());
/*  99 */         return true;
/*     */       }
/*     */     }
/* 102 */     return super.onEntitySwing(entityLiving, stack);
/*     */   }
/*     */   
/*     */   public void flyDroneWithButton(EntityPlayer p, int buttonCombination)
/*     */   {
/* 107 */     ItemStack flyerIS = p.func_184614_ca();
/* 108 */     int ba = buttonCombination >> 5;
/* 109 */     int fo = buttonCombination -= (ba << 5) >> 4;
/* 110 */     int ri = buttonCombination -= (fo << 4) >> 3;
/* 111 */     int le = buttonCombination -= (ri << 3) >> 2;
/* 112 */     int dw = buttonCombination -= (le << 2) >> 1;
/* 113 */     int up = buttonCombination -= (dw << 1);
/*     */     
/* 115 */     EntityDrone drone = getControllingDrone(p.field_70170_p, flyerIS);
/* 116 */     if ((drone != null) && (drone.isControllerFlying()))
/*     */     {
/* 118 */       Vec3d vecJS = p.func_70040_Z();
/* 119 */       Vec3d vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + p.field_70177_z) / 180.0F * 3.141592653589793D);
/*     */       
/* 121 */       Vec3d vecUD = VecHelper.rotateAround(
/* 122 */         VecHelper.rotateAround(new Vec3d(0.0D, 1.0D, 0.0D), new Vec3d(-1.0D, 0.0D, 0.0D), p.field_70125_A / 180.0F * 3.141592653589793D), new Vec3d(0.0D, 1.0D, 0.0D), p.field_70177_z / 180.0F * 3.141592653589793D);
/*     */       
/* 124 */       int i = 0;
/* 125 */       if (drone.getCameraMode())
/*     */       {
/* 127 */         vecJS = new Vec3d(0.0D, 1.0D, 0.0D);
/* 128 */         vecUD = drone.getHorizontalLookVec();
/* 129 */         vecLR = VecHelper.rotateAround(new Vec3d(0.0D, 0.0D, -1.0D), new Vec3d(0.0D, 1.0D, 0.0D), (90.0F + drone.field_70177_z) / 180.0F * 3.141592653589793D);
/*     */ 
/*     */       }
/* 132 */       else if (drone.func_184179_bs() == p)
/*     */       {
/* 134 */         vecUD = new Vec3d(0.0D, 1.0D, 0.0D);
/*     */         
/* 136 */         vecJS = new Vec3d(-Math.sin(p.field_70177_z / 180.0D * 3.141592653589793D), 0.0D, Math.cos(p.field_70177_z / 180.0D * 3.141592653589793D));
/*     */       }
/* 138 */       Vec3d lookMotion = VecHelper.scale(vecJS, fo).func_178787_e(VecHelper.scale(vecJS, -ba));
/* 139 */       Vec3d lrMotion = VecHelper.scale(vecLR, le).func_178787_e(VecHelper.scale(vecLR, -ri));
/* 140 */       Vec3d udMotion = VecHelper.scale(vecUD, up).func_178787_e(VecHelper.scale(vecUD, -dw));
/* 141 */       Vec3d motion = VecHelper.setLength(lookMotion.func_178787_e(lrMotion).func_178787_e(udMotion), drone.getSpeedMultiplication());
/* 142 */       drone.field_70159_w += motion.field_72450_a;
/* 143 */       drone.field_70181_x += motion.field_72448_b;
/* 144 */       drone.field_70179_y += motion.field_72449_c;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*     */   {
/* 151 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/*     */     
/* 153 */     tooltip.add(TextFormatting.AQUA + "Controller frequency: " + getControllerFreq(stack) + "GHz.");
/* 154 */     int controllingID = getControllingDroneID(stack);
/* 155 */     if (controllingID == -2) { tooltip.add(TextFormatting.AQUA + "Universal controller.");
/* 156 */     } else if (controllingID == -1) tooltip.add(TextFormatting.RED + "Not assigned to a drone."); else {
/* 157 */       tooltip.add(TextFormatting.AQUA + "Controlling drone #" + controllingID + ".");
/*     */     }
/*     */   }
/*     */   
/*     */   public void setControllerFreq(ItemStack stack, int i) {
/* 162 */     if (stack == null) return;
/* 163 */     if (!stack.func_77942_o())
/*     */     {
/* 165 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 167 */     stack.func_77978_p().func_74768_a("Controller Frequency", i);
/*     */   }
/*     */   
/*     */   public int getControllerFreq(ItemStack stack)
/*     */   {
/* 172 */     if ((stack == null) || (!(stack.func_77973_b() instanceof ItemDroneFlyer))) return -2;
/* 173 */     if ((!stack.func_77942_o()) || (!stack.func_77978_p().func_74764_b("Controller Frequency")))
/*     */     {
/* 175 */       return 0;
/*     */     }
/* 177 */     return stack.func_77978_p().func_74762_e("Controller Frequency");
/*     */   }
/*     */   
/*     */   public boolean isDroneInFrequency(ItemStack is, EntityDrone d)
/*     */   {
/* 182 */     return (is != null) && (getControllerFreq(is) == d.droneInfo.droneFreq);
/*     */   }
/*     */   
/*     */   public void setUniversalController(ItemStack stack)
/*     */   {
/* 187 */     if (stack == null) return;
/* 188 */     if (!stack.func_77942_o())
/*     */     {
/* 190 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 192 */     stack.func_77978_p().func_74768_a("Drone Controlling", -2);
/*     */   }
/*     */   
/*     */   public void setControllingDrone(EntityPlayer player, ItemStack stack, EntityDrone drone)
/*     */   {
/* 197 */     if (stack == null) return;
/* 198 */     if (!stack.func_77942_o())
/*     */     {
/* 200 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 202 */     if (drone == null)
/*     */     {
/* 204 */       EntityDrone prevDrone = getControllingDrone(player.field_70170_p, stack);
/* 205 */       if (prevDrone != null)
/*     */       {
/* 207 */         prevDrone.setControllingPlayer(null);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 212 */       drone.setControllingPlayer(player);
/*     */     }
/* 214 */     stack.func_77978_p().func_74768_a("Drone Controlling", drone == null ? -1 : drone.getDroneID());
/*     */   }
/*     */   
/*     */   public int getControllingDroneID(ItemStack stack)
/*     */   {
/* 219 */     if (stack == null) return -1;
/* 220 */     if ((!stack.func_77942_o()) || (!stack.func_77978_p().func_74764_b("Drone Controlling")))
/*     */     {
/* 222 */       return -1;
/*     */     }
/* 224 */     return stack.func_77978_p().func_74762_e("Drone Controlling");
/*     */   }
/*     */   
/*     */   public EntityDrone getControllingDrone(World world, ItemStack is)
/*     */   {
/* 229 */     return EntityDrone.getDroneFromID(world, getControllingDroneID(is));
/*     */   }
/*     */   
/*     */   public boolean isControllingDrone(ItemStack stack, EntityDrone ed)
/*     */   {
/* 234 */     if (stack == null) return false;
/* 235 */     int id = getControllingDroneID(stack);
/* 236 */     return (isDroneInFrequency(stack, ed)) && ((id == -2) || (ed.getDroneID() == id));
/*     */   }
/*     */   
/*     */   public void setNextFlyMode(ItemStack stack)
/*     */   {
/* 241 */     int mode = getFlyMode(stack) + 1;
/* 242 */     if (mode == 4) mode = 0;
/* 243 */     setFlyMode(stack, mode);
/*     */   }
/*     */   
/*     */   public void setFlyMode(ItemStack stack, int i)
/*     */   {
/* 248 */     if (!stack.func_77942_o())
/*     */     {
/* 250 */       stack.func_77982_d(new NBTTagCompound());
/*     */     }
/* 252 */     stack.func_77978_p().func_74768_a("Drone Flying Mode", i);
/*     */   }
/*     */   
/*     */   public int getFlyMode(ItemStack stack)
/*     */   {
/* 257 */     if (stack == null) return 0;
/* 258 */     if (stack.func_77942_o())
/*     */     {
/* 260 */       return stack.func_77978_p().func_74762_e("Drone Flying Mode");
/*     */     }
/*     */     
/*     */ 
/* 264 */     return 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDroneFlyer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */