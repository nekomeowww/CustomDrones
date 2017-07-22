/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityTNTPrimed;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneDropRider;
/*     */ 
/*     */ public class ModuleTransport
/*     */   extends Module
/*     */ {
/*     */   public ModuleTransport(int l, String s)
/*     */   {
/*  25 */     super(l, Module.ModuleType.Transport, s);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean collideWithBlock(EntityDrone drone, BlockPos pos, IBlockState state)
/*     */   {
/*  31 */     if ((!drone.field_70170_p.field_72995_K) && (canFunctionAs(nplayerTransport)) && (drone.getRider() == null) && 
/*  32 */       (state.func_177230_c() == Blocks.field_150335_W))
/*     */     {
/*  34 */       drone.field_70170_p.func_175698_g(pos);
/*     */       
/*  36 */       EntityTNTPrimed tnt = new EntityTNTPrimed(drone.field_70170_p, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p(), drone.getControllingPlayer() == null ? drone : drone.getControllingPlayer());
/*  37 */       tnt.func_184220_m(drone);
/*  38 */       drone.field_70170_p.func_72838_d(tnt);
/*     */     }
/*  40 */     return super.collideWithBlock(drone, pos, state);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  46 */     super.updateModule(drone);
/*  47 */     if ((canFunctionAs(nplayerTransport)) && ((drone.getRider() instanceof EntityTNTPrimed)))
/*     */     {
/*  49 */       ((EntityTNTPrimed)drone.getRider()).func_184534_a(80 - drone.field_70173_aa % 20);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public double costBatRawPerSec(EntityDrone drone)
/*     */   {
/*  56 */     if (drone.getRider() != null)
/*     */     {
/*  58 */       return super.costBatRawPerSec(drone) + drone.getRider().field_70130_N * drone.getRider().field_70131_O;
/*     */     }
/*  60 */     return super.costBatRawPerSec(drone);
/*     */   }
/*     */   
/*     */ 
/*     */   public void onDisabled(EntityDrone drone)
/*     */   {
/*  66 */     super.onDisabled(drone);
/*  67 */     if ((this == nplayerTransport) || (this == playerTransport) || (this == multiTransport)) { drone.dropRider();
/*     */     }
/*     */   }
/*     */   
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/*  73 */     super.additionalTooltip(list, forGuiDroneStatus);
/*  74 */     if (this == itemInventory)
/*     */     {
/*  76 */       list.add("Install drone inventory");
/*     */     }
/*  78 */     else if (this == nplayerTransport)
/*     */     {
/*  80 */       list.add("Transport non-player entities");
/*     */     }
/*  82 */     else if (this == playerTransport)
/*     */     {
/*  84 */       list.add("Transport players");
/*     */     }
/*  86 */     else if (this == multiTransport)
/*     */     {
/*  88 */       list.add("Install inventory and transport all entities");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/*  96 */     return new ModuleTransportGui(gui, this);
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleTransportGui<T extends Module> extends Module.ModuleGui<T>
/*     */   {
/*     */     boolean transportEntity;
/*     */     
/*     */     public ModuleTransportGui(T gui)
/*     */     {
/* 106 */       super(mod);
/* 107 */       this.transportEntity = ((mod == Module.nplayerTransport) || (mod == Module.playerTransport) || (mod == Module.multiTransport));
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/* 113 */       super.func_73866_w_();
/* 114 */       if (this.transportEntity)
/*     */       {
/* 116 */         this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 24, this.field_146295_m / 2 + 70, 70, 20, "Drop entity"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 123 */       super.addDescText(l);
/* 124 */       if (this.transportEntity)
/*     */       {
/* 126 */         String transporting = "None";
/* 127 */         if ((this.parent.drone.getRider() instanceof EntityPlayer))
/*     */         {
/* 129 */           transporting = TextFormatting.AQUA + ((EntityPlayer)this.parent.drone.getRider()).func_70005_c_();
/*     */         }
/* 131 */         else if (this.parent.drone.getRider() != null)
/*     */         {
/* 133 */           transporting = TextFormatting.AQUA + this.parent.drone.getRider().func_70005_c_();
/*     */         }
/* 135 */         l.add("Transporting entity: " + transporting);
/* 136 */         if (this.parent.drone.getRider() != null)
/*     */         {
/* 138 */           double cost = this.parent.drone.getRider().field_70130_N * this.parent.drone.getRider().field_70131_O;
/* 139 */           l.add("Transporting weight cost: " + TextFormatting.RED + Math.round(cost * 100.0D) / 100.0D + " battery/sec");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button)
/*     */     {
/* 148 */       super.buttonClickedOnEnabledGui(button);
/* 149 */       if ((button.field_146127_k == 1) && (this.parent.drone.getRider() != null))
/*     */       {
/* 151 */         PacketDispatcher.sendToServer(new PacketDroneDropRider(this.parent.drone));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleTransport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */