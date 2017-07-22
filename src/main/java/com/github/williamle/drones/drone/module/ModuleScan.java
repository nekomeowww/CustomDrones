/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockOre;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.passive.EntityAnimal;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.math.AxisAlignedBB;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.text.TextFormatting;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ModuleScan
/*     */   extends Module
/*     */ {
/*     */   public ModuleScan(int l, String s)
/*     */   {
/*  34 */     super(l, Module.ModuleType.Scout, s);
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/*  41 */     return new ModuleScanGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/*  47 */     super.additionalTooltip(list, forGuiDroneStatus);
/*  48 */     if (this == mobScan1)
/*     */     {
/*  50 */       list.add("Scan nearby animals");
/*     */     }
/*  52 */     else if (this == mobScan2)
/*     */     {
/*  54 */       list.add("Scan all nearby entities");
/*     */     }
/*  56 */     else if (this == oreScan)
/*     */     {
/*  58 */       list.add("Scan nearby ores");
/*     */     }
/*  60 */     else if (this == multiScan)
/*     */     {
/*  62 */       list.add("Scan nearby ores and entities");
/*     */     }
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleScanGui<T extends Module> extends Module.ModuleGui<T> {
/*     */     boolean mob;
/*     */     boolean ore;
/*     */     double range;
/*  71 */     Map<String, Integer> entitiesScan = new HashMap();
/*  72 */     Map<String, Integer> oresScan = new HashMap();
/*     */     
/*     */     public ModuleScanGui(T gui)
/*     */     {
/*  76 */       super(mod);
/*  77 */       this.mob = mod.canFunctionAs(Module.mobScan1);
/*  78 */       this.ore = mod.canFunctionAs(Module.oreScan);
/*  79 */       this.range = ((this.parent.drone.droneInfo.chip + mod.level) * 2);
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/*  85 */       super.func_73866_w_();
/*  86 */       this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 34, this.field_146295_m / 2 + 70, 80, 20, "Change range"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button)
/*     */     {
/*  92 */       super.buttonClickedOnEnabledGui(button);
/*  93 */       if (button.field_146127_k == 1)
/*     */       {
/*  95 */         int maxsteps = 5;
/*  96 */         double maxRange = (this.parent.drone.droneInfo.chip + this.mod.level) * 2;
/*  97 */         int i1 = (int)Math.round(this.range / maxRange * maxsteps);
/*  98 */         int i2 = i1 + 1;
/*  99 */         if (i2 > maxsteps) i2 = 1;
/* 100 */         this.range = (maxRange * i2 / maxsteps);
/* 101 */         this.entitiesScan.clear();
/* 102 */         this.oresScan.clear();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73876_c()
/*     */     {
/* 109 */       super.func_73876_c();
/* 110 */       if (!this.parent.drone.droneInfo.isEnabled(this.mod)) return;
/* 111 */       World world = this.parent.drone.field_70170_p;
/* 112 */       AxisAlignedBB aabb = this.parent.drone.func_174813_aQ().func_72314_b(this.range, this.range, this.range);
/* 113 */       if ((this.mob) && ((this.parent.drone.field_70173_aa % 20 == 0) || (this.entitiesScan.isEmpty())))
/*     */       {
/* 115 */         this.entitiesScan.clear();
/* 116 */         List<Entity> entities = world.func_175647_a(this.mod == Module.mobScan1 ? EntityAnimal.class : Entity.class, aabb, EntitySelectors.field_94557_a);
/*     */         
/* 118 */         entities.remove(this.parent.drone);
/* 119 */         for (int a = 0; a < entities.size(); a++)
/*     */         {
/* 121 */           Entity e = (Entity)entities.get(a);
/* 122 */           String entityName = entityName(e);
/* 123 */           int count = this.entitiesScan.containsKey(entityName) ? ((Integer)this.entitiesScan.get(entityName)).intValue() : 0;
/* 124 */           count++;
/* 125 */           this.entitiesScan.put(entityName, Integer.valueOf(count));
/*     */         }
/*     */       }
/* 128 */       if ((this.ore) && ((this.parent.drone.field_70173_aa % 100 == 0) || (this.oresScan.isEmpty())))
/*     */       {
/* 130 */         this.oresScan.clear();
/* 131 */         for (int x = (int)Math.floor(this.parent.drone.field_70165_t - this.range); x <= this.parent.drone.field_70165_t + this.range; x++)
/*     */         {
/* 133 */           for (int y = (int)Math.floor(this.parent.drone.field_70163_u - this.range); y <= this.parent.drone.field_70163_u + this.range; y++)
/*     */           {
/* 135 */             for (int z = (int)Math.floor(this.parent.drone.field_70161_v - this.range); z <= this.parent.drone.field_70161_v + this.range; z++)
/*     */             {
/* 137 */               BlockPos bp = new BlockPos(x, y, z);
/* 138 */               IBlockState bs = world.func_180495_p(bp);
/* 139 */               Block b = bs.func_177230_c();
/* 140 */               if ((b instanceof BlockOre))
/*     */               {
/* 142 */                 String oreName = oreName(b);
/* 143 */                 int count = this.oresScan.containsKey(oreName) ? ((Integer)this.oresScan.get(oreName)).intValue() : 0;
/* 144 */                 count++;
/* 145 */                 this.oresScan.put(oreName, Integer.valueOf(count));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void addDescText(List<String> l)
/*     */     {
/* 156 */       super.addDescText(l);
/* 157 */       if (!this.parent.drone.droneInfo.isEnabled(this.mod)) return;
/* 158 */       int rangei = (int)Math.round(this.range);
/* 159 */       l.add("Scan range: " + TextFormatting.YELLOW + rangei * 2 + "x" + rangei * 2 + "x" + rangei * 2 + TextFormatting.RESET + " blocks");
/*     */       
/* 161 */       if (this.mob)
/*     */       {
/* 163 */         String s = TextFormatting.GREEN + "" + TextFormatting.BOLD + "Entities: " + TextFormatting.RESET;
/* 164 */         int count; if (this.entitiesScan.isEmpty())
/*     */         {
/* 166 */           s = s + "none";
/*     */         }
/*     */         else
/*     */         {
/* 170 */           count = 0;
/* 171 */           for (Map.Entry<String, Integer> e : this.entitiesScan.entrySet())
/*     */           {
/* 173 */             s = s + (count > 0 ? ", " : "") + (String)e.getKey() + ": " + TextFormatting.GREEN + e.getValue() + TextFormatting.RESET;
/*     */             
/* 175 */             count++;
/*     */           }
/*     */         }
/* 178 */         l.add(s);
/*     */       }
/* 180 */       if (this.ore)
/*     */       {
/* 182 */         String s = TextFormatting.RED + "" + TextFormatting.BOLD + "Ores: " + TextFormatting.RESET;
/* 183 */         int count; if (this.oresScan.isEmpty())
/*     */         {
/* 185 */           s = s + "none";
/*     */         }
/*     */         else
/*     */         {
/* 189 */           count = 0;
/* 190 */           for (Map.Entry<String, Integer> e : this.oresScan.entrySet())
/*     */           {
/* 192 */             s = s + (count > 0 ? ", " : "") + (String)e.getKey() + ": " + TextFormatting.RED + e.getValue() + TextFormatting.RESET;
/*     */             
/* 194 */             count++;
/*     */           }
/*     */         }
/* 197 */         l.add(s);
/*     */       }
/*     */     }
/*     */     
/*     */     public String entityName(Entity e)
/*     */     {
/* 203 */       if ((e instanceof EntityDrone)) return "Other Drone";
/* 204 */       if ((e instanceof EntityPlayer)) return "Player";
/* 205 */       if ((e instanceof EntityItem)) return "Dropped item";
/* 206 */       return e.func_70005_c_();
/*     */     }
/*     */     
/*     */     public String oreName(Block b)
/*     */     {
/* 211 */       String s = b.func_149732_F();
/* 212 */       if ((s.endsWith("Ore")) || (s.endsWith("ore"))) s = s.substring(0, s.length() - 4);
/* 213 */       return s;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleScan.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */