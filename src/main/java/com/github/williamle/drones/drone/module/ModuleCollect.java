/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
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
/*     */ import williamle.drones.network.server.PacketDroneTransferXP;
/*     */ 
/*     */ 
/*     */ public class ModuleCollect
/*     */   extends Module
/*     */ {
/*     */   public ModuleCollect(int l, String s)
/*     */   {
/*  29 */     super(l, Module.ModuleType.Collect, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  35 */     super.updateModule(drone);
/*  36 */     World world = drone.field_70170_p;
/*     */     
/*     */ 
/*  39 */     AxisAlignedBB aabb = new AxisAlignedBB(drone.field_70165_t, drone.field_70163_u, drone.field_70161_v, drone.field_70165_t, drone.field_70163_u, drone.field_70161_v).func_72314_b(2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip, 2 * drone.droneInfo.chip).func_72317_d(0.0D, -drone.droneInfo.chip * 0.5D, 0.0D).func_72321_a(0.0D, 0.25D, 0.0D);
/*  40 */     if ((drone.droneInfo.hasInventory()) && (canFunctionAs(Module.itemCollect)))
/*     */     {
/*  42 */       List<EntityItem> eis = world.func_72872_a(EntityItem.class, aabb);
/*  43 */       for (EntityItem ei : eis)
/*     */       {
/*  45 */         ItemStack is = ei.func_92059_d();
/*  46 */         if (drone.droneInfo.inventory.canAddToInv(is, false))
/*     */         {
/*  48 */           if ((!ei.func_174874_s()) && (ei.func_70068_e(drone) <= 1.0D))
/*     */           {
/*  50 */             if (is != null)
/*     */             {
/*  52 */               ItemStack is0 = drone.droneInfo.inventory.addToInv(is);
/*  53 */               if ((is0 == null) || (is0.field_77994_a == 0))
/*     */               {
/*  55 */                 ei.func_70106_y();
/*     */               }
/*     */               else
/*     */               {
/*  59 */                 ei.func_92058_a(is0);
/*     */               }
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/*  65 */             Vec3d vec = VecHelper.fromTo(ei.func_174791_d(), drone.func_174791_d());
/*  66 */             vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
/*  67 */             ei.func_70024_g(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*  72 */     if (canFunctionAs(Module.xpCollect))
/*     */     {
/*  74 */       List<EntityXPOrb> xpos = world.func_72872_a(EntityXPOrb.class, aabb);
/*  75 */       for (EntityXPOrb xpo : xpos)
/*     */       {
/*  77 */         if ((xpo.field_70532_c == 0) && (xpo.func_70068_e(drone) <= 1.0D))
/*     */         {
/*  79 */           setCollectedXP(drone, getCollectedXP(drone) + xpo.func_70526_d());
/*  80 */           xpo.func_70106_y();
/*     */         }
/*     */         else
/*     */         {
/*  84 */           Vec3d vec = VecHelper.fromTo(xpo.func_174791_d(), drone.func_174791_d());
/*  85 */           vec = VecHelper.setLength(vec, 0.03D * drone.droneInfo.chip);
/*  86 */           xpo.func_70024_g(vec.field_72450_a, vec.field_72448_b, vec.field_72449_c);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int getCollectedXP(EntityDrone drone)
/*     */   {
/*  94 */     return getModNBT(drone.droneInfo).func_74762_e("Collected XP");
/*     */   }
/*     */   
/*     */   public void setCollectedXP(EntityDrone drone, int i)
/*     */   {
/*  99 */     NBTTagCompound tag = getModNBT(drone.droneInfo);
/* 100 */     if (tag != null) { tag.func_74768_a("Collected XP", i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 106 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 107 */     boolean item = canFunctionAs(Module.itemCollect);
/* 108 */     boolean xp = canFunctionAs(Module.xpCollect);
/* 109 */     list.add("Collect " + (xp ? "XP" : item ? "items" + (xp ? " and XP" : "") : ""));
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 116 */     return new ModuleCollectGui(gui, this);
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleCollectGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     public GuiButton buttonTransfer;
/*     */     
/*     */     public ModuleCollectGui(T gui)
/*     */     {
/* 126 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/* 132 */       super.func_73866_w_();
/* 133 */       boolean xp = ModuleCollect.this.canFunctionAs(Module.xpCollect);
/* 134 */       if (xp)
/*     */       {
/*     */ 
/* 137 */         this.field_146292_n.add(this.buttonTransfer = new GuiButton(1, this.field_146294_l / 2 - 24, this.field_146295_m / 2 + 70, 70, 20, "Transfer XP"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73876_c()
/*     */     {
/* 144 */       super.func_73876_c();
/* 145 */       boolean xp = this.mod.canFunctionAs(Module.xpCollect);
/* 146 */       if (xp)
/*     */       {
/* 148 */         int range = 4 * this.parent.drone.droneInfo.chip;
/* 149 */         if (this.parent.drone.func_70068_e(this.parent.player) <= range * range)
/*     */         {
/* 151 */           this.buttonTransfer.field_146124_l = true;
/* 152 */           this.buttonTransfer.field_146126_j = "Transfer XP";
/*     */         }
/*     */         else
/*     */         {
/* 156 */           this.buttonTransfer.field_146124_l = false;
/* 157 */           this.buttonTransfer.field_146126_j = "Out of range";
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button)
/*     */     {
/* 165 */       super.buttonClickedOnEnabledGui(button);
/* 166 */       if ((button == this.buttonTransfer) && (ModuleCollect.this.getCollectedXP(this.parent.drone) > 0))
/*     */       {
/* 168 */         PacketDispatcher.sendToServer(new PacketDroneTransferXP(this.parent.drone));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 175 */       super.addDescText(l);
/* 176 */       int aoe = this.parent.drone.droneInfo.chip * 4;
/* 177 */       l.add("Area of effect: " + TextFormatting.YELLOW + aoe + "x" + aoe + "x" + aoe + " blocks" + TextFormatting.WHITE + " under drone");
/*     */       
/* 179 */       if (ModuleCollect.this.canFunctionAs(Module.xpCollect))
/*     */       {
/* 181 */         l.add("Collected XP: " + TextFormatting.YELLOW + ModuleCollect.this.getCollectedXP(this.parent.drone));
/* 182 */         int range = 4 * this.parent.drone.droneInfo.chip;
/* 183 */         l.add("XP transfer range: " + TextFormatting.YELLOW + range + " blocks");
/*     */       }
/* 185 */       if ((ModuleCollect.this.canFunctionAs(Module.itemCollect)) && (!this.parent.drone.droneInfo.hasEnabled(Module.itemInventory)))
/*     */       {
/* 187 */         l.add(TextFormatting.RED + "Need Item inventory mod to work!");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleCollect.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */