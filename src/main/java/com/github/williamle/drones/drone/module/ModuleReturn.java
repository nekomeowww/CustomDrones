/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.tileentity.TileEntity;
/*     */ import net.minecraft.tileentity.TileEntityEnderChest;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.api.helpers.VecHelper;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.InventoryDrone;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneSetReturnPos;
/*     */ 
/*     */ public class ModuleReturn extends Module
/*     */ {
/*     */   public ModuleReturn(int l, String s)
/*     */   {
/*  30 */     super(l, Module.ModuleType.Recover, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/*  36 */     super.additionalTooltip(list, forGuiDroneStatus);
/*  37 */     if (!forGuiDroneStatus) { list.add("Return to designated position on low battery");
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  43 */     super.updateModule(drone);
/*     */   }
/*     */   
/*     */ 
/*     */   public int overridePriority()
/*     */   {
/*  49 */     return 100;
/*     */   }
/*     */   
/*     */   public Vec3d getReturnPos(EntityDrone drone)
/*     */   {
/*  54 */     NBTTagCompound tag = getModNBT(drone.droneInfo);
/*  55 */     if ((tag != null) && (tag.func_74764_b("Return X")) && (tag.func_74764_b("Return Y")) && (tag.func_74764_b("Return Z")))
/*     */     {
/*  57 */       return new Vec3d(tag.func_74769_h("Return X"), tag.func_74769_h("Return Y"), tag.func_74769_h("Return Z"));
/*     */     }
/*  59 */     return null;
/*     */   }
/*     */   
/*     */   public void setReturnPos(EntityDrone drone, Vec3d vec)
/*     */   {
/*  64 */     NBTTagCompound tag = getModNBT(drone.droneInfo);
/*  65 */     if (tag != null)
/*     */     {
/*  67 */       tag.func_74780_a("Return X", vec.field_72450_a);
/*  68 */       tag.func_74780_a("Return Y", vec.field_72448_b);
/*  69 */       tag.func_74780_a("Return Z", vec.field_72449_c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void overrideDroneMovement(EntityDrone drone)
/*     */   {
/*  76 */     super.overrideDroneMovement(drone);
/*  77 */     Vec3d repos = getReturnPos(drone);
/*  78 */     if (repos == null) return;
/*  79 */     double speedMult = drone.getSpeedMultiplication();
/*  80 */     Vec3d dir = VecHelper.fromTo(drone.func_174791_d(), repos);
/*  81 */     if (dir.func_72433_c() <= 1.0D)
/*     */     {
/*  83 */       int x = (int)Math.floor(drone.field_70165_t);
/*  84 */       int y = (int)Math.floor(drone.field_70163_u);
/*  85 */       int z = (int)Math.floor(drone.field_70161_v);
/*  86 */       for (int y0 = y; y0 >= y - 1; y0--)
/*     */       {
/*  88 */         BlockPos bs = new BlockPos(x, y0, z);
/*  89 */         IInventory iinv = null;
/*  90 */         TileEntity tile0 = drone.field_70170_p.func_175625_s(bs);
/*  91 */         if ((tile0 instanceof IInventory))
/*     */         {
/*  93 */           iinv = (IInventory)tile0;
/*     */         }
/*  95 */         else if (((tile0 instanceof TileEntityEnderChest)) && (drone.getControllingPlayer() != null))
/*     */         {
/*  97 */           iinv = drone.getControllingPlayer().func_71005_bN();
/*     */         }
/*     */         
/* 100 */         if (iinv != null)
/*     */         {
/* 102 */           for (int a = 0; a < iinv.func_70302_i_(); a++)
/*     */           {
/* 104 */             ItemStack is1 = iinv.func_70301_a(a);
/* 105 */             if (DroneInfo.batteryFuel.containsKey(drone.droneInfo.getItemStackObject(is1)))
/*     */             {
/* 107 */               is1 = drone.droneInfo.applyItem(drone, is1);
/* 108 */               if (!drone.droneInfo.hasInventory())
/*     */                 break;
/* 110 */               ItemStack is2 = drone.droneInfo.inventory.addToInv(is1);
/* 111 */               iinv.func_70299_a(a, is2);
/* 112 */               break;
/*     */             }
/*     */           }
/*     */           
/* 116 */           break;
/*     */         }
/*     */       }
/*     */     } else {
/* 120 */       dir = dir.func_72432_b(); }
/* 121 */     drone.field_70181_x += dir.field_72448_b * speedMult;
/* 122 */     drone.field_70159_w += dir.field_72450_a * speedMult;
/* 123 */     drone.field_70179_y += dir.field_72449_c * speedMult;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canOverrideDroneMovement(EntityDrone drone)
/*     */   {
/* 129 */     int mode = drone.getFlyingMode();
/* 130 */     double battery = drone.droneInfo.getBattery(false);
/* 131 */     return (battery <= getMinBatteryToReturn(drone)) && (mode != 2);
/*     */   }
/*     */   
/*     */   public double getMinBatteryToReturn(EntityDrone e)
/*     */   {
/* 136 */     Vec3d repos = getReturnPos(e);
/* 137 */     if (repos == null) return 0.0D;
/* 138 */     double dist = e.func_70011_f(repos.field_72450_a, repos.field_72448_b, repos.field_72449_c);
/* 139 */     double tickLeft = e.droneInfo.getBattery(false) / e.droneInfo.getMovementBatteryConsumption(e);
/* 140 */     double maxSpeed = e.getSpeedMultiplication();
/* 141 */     double tickToFly = dist / maxSpeed;
/* 142 */     double batteryToFly = tickToFly * e.droneInfo.getBattery(false) / tickLeft;
/* 143 */     if ((Double.isInfinite(batteryToFly)) || (Double.isNaN(batteryToFly))) return -1.0D;
/* 144 */     return batteryToFly;
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 151 */     return new ModuleReturnGui(gui, this);
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleReturnGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     public ModuleReturnGui(T gui)
/*     */     {
/* 159 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/* 165 */       super.func_73866_w_();
/* 166 */       this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 24, this.field_146295_m / 2 + 70, 70, 20, "Return here"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button)
/*     */     {
/* 172 */       super.buttonClickedOnEnabledGui(button);
/* 173 */       if (button.field_146127_k == 1)
/*     */       {
/* 175 */         PacketDispatcher.sendToServer(new PacketDroneSetReturnPos(this.parent.drone));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 182 */       super.addDescText(l);
/* 183 */       if (this.parent.drone.droneInfo.isEnabled(this.mod))
/*     */       {
/* 185 */         Vec3d v = ModuleReturn.this.getReturnPos(this.parent.drone);
/* 186 */         if (v != null)
/*     */         {
/* 188 */           double minB = ((ModuleReturn)this.mod).getMinBatteryToReturn(this.parent.drone);
/* 189 */           if (minB < 0.0D)
/*     */           {
/* 191 */             l.add(TextFormatting.RED + "Unable to return to [" + (int)v.field_72450_a + ";" + (int)v.field_72448_b + ";" + (int)v.field_72449_c + "]");
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/* 196 */             l.add("Return to " + TextFormatting.GREEN + "[" + (int)v.field_72450_a + ";" + (int)v.field_72448_b + ";" + (int)v.field_72449_c + "]" + TextFormatting.RESET + " on battery less than " + TextFormatting.RED + 
/*     */             
/* 198 */               (int)Math.round(minB * 10.0D) / 10.0D);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 203 */           l.add(TextFormatting.RED + "" + TextFormatting.ITALIC + "Return coordinates not assigned");
/*     */         }
/* 205 */         int x = (int)Math.floor(this.parent.drone.field_70165_t);
/* 206 */         int y = (int)Math.floor(this.parent.drone.field_70163_u);
/* 207 */         int z = (int)Math.floor(this.parent.drone.field_70161_v);
/* 208 */         for (int y0 = y; y0 >= y - 1; y0--)
/*     */         {
/* 210 */           BlockPos bs = new BlockPos(x, y0, z);
/* 211 */           IInventory iinv = null;
/* 212 */           TileEntity tile0 = this.parent.drone.field_70170_p.func_175625_s(bs);
/* 213 */           if ((tile0 instanceof IInventory))
/*     */           {
/* 215 */             iinv = (IInventory)tile0;
/*     */           }
/* 217 */           else if (((tile0 instanceof TileEntityEnderChest)) && (this.parent.drone.getControllingPlayer() != null))
/*     */           {
/* 219 */             iinv = this.parent.drone.getControllingPlayer().func_71005_bN();
/*     */           }
/*     */           
/* 222 */           if (iinv != null)
/*     */           {
/* 224 */             l.add("Drone is on a chest");
/* 225 */             break;
/*     */           }
/*     */         }
/* 228 */         double battery = this.parent.drone.droneInfo.getBattery(true);
/* 229 */         l.add("Current battery: " + TextFormatting.GREEN + battery);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleReturn.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */