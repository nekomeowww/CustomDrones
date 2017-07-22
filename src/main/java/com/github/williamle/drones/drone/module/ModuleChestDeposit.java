/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.inventory.IInventory;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.tileentity.TileEntityEnderChest;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.InventoryDrone;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ 
/*     */ public class ModuleChestDeposit extends Module
/*     */ {
/*     */   public ModuleChestDeposit(int l, String s)
/*     */   {
/*  21 */     super(l, Module.ModuleType.Collect, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  27 */     super.updateModule(drone);
/*  28 */     if (drone.droneInfo.hasInventory())
/*     */     {
/*  30 */       int x = (int)Math.floor(drone.field_70165_t);
/*  31 */       int y = (int)Math.floor(drone.field_70163_u - 1.0D);
/*  32 */       int z = (int)Math.floor(drone.field_70161_v);
/*  33 */       IInventory iinv = null;
/*  34 */       net.minecraft.tileentity.TileEntity tile0 = drone.field_70170_p.func_175625_s(new BlockPos(x, y, z));
/*  35 */       if ((tile0 instanceof IInventory))
/*     */       {
/*  37 */         iinv = (IInventory)tile0;
/*     */       }
/*  39 */       else if (((tile0 instanceof TileEntityEnderChest)) && (drone.getControllingPlayer() != null))
/*     */       {
/*  41 */         iinv = drone.getControllingPlayer().func_71005_bN();
/*     */       }
/*     */       
/*  44 */       if (iinv != null)
/*     */       {
/*  46 */         InventoryDrone droneinv = drone.droneInfo.inventory;
/*  47 */         int stackLimit = iinv.func_70297_j_();
/*  48 */         int invSize = iinv.func_70302_i_();
/*  49 */         for (int a = 0; a < droneinv.func_70302_i_(); a++)
/*     */         {
/*  51 */           ItemStack is0 = droneinv.func_70301_a(a);
/*  52 */           if (is0 != null)
/*     */           {
/*  54 */             for (int b = 0; b < invSize; b++)
/*     */             {
/*  56 */               ItemStack is1 = iinv.func_70301_a(b);
/*  57 */               if (is1 != null)
/*     */               {
/*  59 */                 int thisStackLimit = Math.min(stackLimit, is1.func_77976_d());
/*  60 */                 if ((ItemStack.func_179545_c(is0, is1)) && (is1.field_77994_a < thisStackLimit))
/*     */                 {
/*  62 */                   int maxAdd = thisStackLimit - is1.field_77994_a;
/*  63 */                   int canAdd = is0.field_77994_a;
/*  64 */                   int toAdd = Math.min(maxAdd, canAdd);
/*  65 */                   is1.field_77994_a += toAdd;
/*  66 */                   is0.field_77994_a -= toAdd;
/*  67 */                   iinv.func_70299_a(b, is1);
/*  68 */                   if (is0.field_77994_a <= 0)
/*     */                   {
/*  70 */                     is0 = null;
/*  71 */                     break;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*  77 */           if (is0 != null)
/*     */           {
/*  79 */             for (int b = 0; b < invSize; b++)
/*     */             {
/*  81 */               ItemStack is1 = iinv.func_70301_a(b);
/*  82 */               if (is1 == null)
/*     */               {
/*  84 */                 is1 = is0.func_77946_l();
/*  85 */                 is0 = null;
/*  86 */                 iinv.func_70299_a(b, is1);
/*  87 */                 droneinv.func_70299_a(a, is0);
/*  88 */                 break;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/* 101 */     return new ModuleChestDepositGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 107 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 108 */     list.add("Deposit items into chest on collision with");
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleChestDepositGui extends Module.ModuleGui
/*     */   {
/*     */     public ModuleChestDepositGui(GuiDroneStatus gui, Module mod)
/*     */     {
/* 116 */       super(mod);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleChestDeposit.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */