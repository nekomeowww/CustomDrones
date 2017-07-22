/*     */ package williamle.drones.drone.module;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractMap.SimpleEntry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.math.BlockPos;
/*     */ import net.minecraft.util.math.RayTraceResult;
/*     */ import net.minecraft.util.math.Vec3d;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.client.config.GuiButtonExt;
/*     */ import net.minecraftforge.fml.relauncher.Side;
/*     */ import net.minecraftforge.fml.relauncher.SideOnly;
/*     */ import williamle.drones.api.helpers.EntityHelper;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.gui.GuiDroneStatus;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneSetMineLimits;
/*     */ 
/*     */ public class ModuleMine extends Module
/*     */ {
/*  36 */   public static Map<Block, Integer> blockToWeightMap = new HashMap();
/*  37 */   public static Map<Integer, List<Block>> weightToBlockMap = new HashMap();
/*     */   
/*     */   static
/*     */   {
/*  41 */     addBlockToMap(Blocks.field_150346_d, 1);
/*  42 */     addBlockToMap(Blocks.field_150349_c, 1);
/*  43 */     addBlockToMap(Blocks.field_150351_n, 1);
/*  44 */     addBlockToMap(Blocks.field_150354_m, 1);
/*  45 */     addBlockToMap(Blocks.field_150322_A, 2);
/*  46 */     addBlockToMap(Blocks.field_150348_b, 2);
/*  47 */     addBlockToMap(Blocks.field_150365_q, 3);
/*  48 */     addBlockToMap(Blocks.field_150366_p, 4);
/*  49 */     addBlockToMap(Blocks.field_150424_aL, 5);
/*  50 */     addBlockToMap(Blocks.field_150425_aM, 5);
/*  51 */     addBlockToMap(Blocks.field_150426_aN, 6);
/*  52 */     addBlockToMap(Blocks.field_150369_x, 7);
/*  53 */     addBlockToMap(Blocks.field_150449_bY, 7);
/*  54 */     addBlockToMap(Blocks.field_150450_ax, 8);
/*  55 */     addBlockToMap(Blocks.field_150352_o, 9);
/*  56 */     addBlockToMap(Blocks.field_150343_Z, 10);
/*  57 */     addBlockToMap(Blocks.field_150482_ag, 11);
/*  58 */     addBlockToMap(Blocks.field_150412_bA, 11);
/*     */   }
/*     */   
/*     */   public static void addBlockToMap(Block block, int weight)
/*     */   {
/*  63 */     blockToWeightMap.put(block, Integer.valueOf(weight));
/*  64 */     List<Block> l = new ArrayList();
/*  65 */     if (weightToBlockMap.containsKey(Integer.valueOf(weight))) l = (List)weightToBlockMap.get(Integer.valueOf(weight));
/*  66 */     l.add(block);
/*  67 */     weightToBlockMap.put(Integer.valueOf(weight), l);
/*     */   }
/*     */   
/*     */   public ModuleMine(int l, String s)
/*     */   {
/*  72 */     super(l, Module.ModuleType.Collect, s);
/*     */   }
/*     */   
/*     */ 
/*     */   @SideOnly(Side.CLIENT)
/*     */   public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
/*     */   {
/*  79 */     return new ModuleMineGui(gui, this);
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateModule(EntityDrone drone)
/*     */   {
/*  85 */     super.updateModule(drone);
/*  86 */     List<Map.Entry<BlockPos, IBlockState>> minables = new ArrayList();
/*  87 */     BlockPos bp0 = new BlockPos(drone);
/*  88 */     Integer[] limits = getLimits(drone.droneInfo);
/*  89 */     for (int x = -1; x <= 1; x++)
/*     */     {
/*  91 */       for (int y = -1; y <= 1; y++)
/*     */       {
/*  93 */         for (int z = -1; z <= 1; z++)
/*     */         {
/*  95 */           if (((x == 0) && (y == 0)) || ((x == 0) && (z == 0)) || ((y == 0) && (z == 0)))
/*     */           {
/*  97 */             BlockPos bp = bp0.func_177982_a(x, y, z);
/*  98 */             boolean limited = false;
/*  99 */             if (limits != null)
/*     */             {
/* 101 */               int minx = Math.min(limits[0].intValue(), limits[3].intValue());
/* 102 */               int maxx = Math.max(limits[0].intValue(), limits[3].intValue());
/* 103 */               int miny = Math.min(limits[1].intValue(), limits[4].intValue());
/* 104 */               int maxy = Math.max(limits[1].intValue(), limits[4].intValue());
/* 105 */               int minz = Math.min(limits[2].intValue(), limits[5].intValue());
/* 106 */               int maxz = Math.max(limits[2].intValue(), limits[5].intValue());
/* 107 */               if ((bp.func_177958_n() < minx) || (bp.func_177958_n() > maxx) || (bp.func_177956_o() < miny) || (bp.func_177956_o() > maxy) || 
/* 108 */                 (bp.func_177952_p() < minz) || (bp.func_177952_p() > maxz))
/*     */               {
/* 110 */                 limited = true;
/*     */               }
/*     */             }
/* 113 */             IBlockState bs = drone.field_70170_p.func_180495_p(bp);
/* 114 */             if ((!limited) && (canMine(drone, bs.func_177230_c())))
/*     */             {
/* 116 */               minables.add(new AbstractMap.SimpleEntry(bp, bs));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 122 */     if (minables.isEmpty())
/*     */     {
/* 124 */       getModNBT(drone.droneInfo).func_74757_a("Stay to mine", false);
/*     */     }
/*     */     else
/*     */     {
/* 128 */       getModNBT(drone.droneInfo).func_74757_a("Stay to mine", true);
/* 129 */       for (Map.Entry<BlockPos, IBlockState> entry : minables)
/*     */       {
/* 131 */         BlockPos bpMine = (BlockPos)entry.getKey();
/* 132 */         IBlockState bsMine = (IBlockState)entry.getValue();
/* 133 */         Block b = bsMine.func_177230_c();
/* 134 */         int hardness = (int)Math.ceil(bsMine.func_185887_b(drone.field_70170_p, bpMine)) * 20 + 1;
/* 135 */         if (drone.field_70173_aa % hardness == 0)
/*     */         {
/* 137 */           if (!drone.field_70170_p.field_72995_K)
/*     */           {
/* 139 */             b.func_180637_b(drone.field_70170_p, bpMine, b.getExpDrop(bsMine, drone.field_70170_p, bpMine, 0));
/* 140 */             b.func_176226_b(drone.field_70170_p, bpMine, bsMine, 0);
/* 141 */             drone.field_70170_p.func_175698_g(bpMine);
/*     */           }
/* 143 */           drone.droneInfo.reduceBattery(hardness / 10 * drone.droneInfo.getBatteryConsumptionRate(drone));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean canMine(EntityDrone drone, Block b)
/*     */   {
/* 151 */     if ((blockToWeightMap.containsKey(b)) && (((Integer)blockToWeightMap.get(b)).intValue() <= this.level * 3)) return true;
/* 152 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void overrideDroneMovement(EntityDrone drone)
/*     */   {
/* 158 */     super.overrideDroneMovement(drone);
/* 159 */     moveToNextOre(drone);
/*     */   }
/*     */   
/*     */   public void moveToNextOre(EntityDrone drone)
/*     */   {
/* 164 */     Random rnd = new Random();
/* 165 */     Integer[] limits = getLimits(drone.droneInfo);
/* 166 */     Vec3d mid = EntityHelper.getCenterVec(drone);
/* 167 */     int oreWeight = 0;
/* 168 */     Vec3d orePos = null;
/* 169 */     double d0 = -1.0D;
/* 170 */     int range = getRange(drone);
/* 171 */     for (int ax = 0 - range; ax <= range; ax++)
/*     */     {
/* 173 */       int x = (int)Math.floor(mid.field_72450_a) + ax;
/* 174 */       if ((limits == null) || ((x >= Math.min(limits[0].intValue(), limits[3].intValue())) && (x <= Math.max(limits[0].intValue(), limits[3].intValue()))))
/*     */       {
/* 176 */         for (int az = 0 - range; az <= range; az++)
/*     */         {
/* 178 */           int z = (int)Math.floor(mid.field_72449_c) + az;
/* 179 */           if ((limits == null) || ((z >= Math.min(limits[2].intValue(), limits[5].intValue())) && (z <= Math.max(limits[2].intValue(), limits[5].intValue()))))
/*     */           {
/*     */ 
/* 182 */             for (int ay = 0 - range; ay <= range; ay++)
/*     */             {
/* 184 */               int y = (int)Math.floor(mid.field_72448_b) + ay;
/* 185 */               if ((limits == null) || ((y >= Math.min(limits[1].intValue(), limits[4].intValue())) && (y <= Math.max(limits[1].intValue(), limits[4].intValue()))))
/*     */               {
/* 187 */                 BlockPos bp = new BlockPos(x, y, z);
/* 188 */                 IBlockState bs = drone.field_70170_p.func_180495_p(bp);
/* 189 */                 if (canMine(drone, bs.func_177230_c()))
/*     */                 {
/* 191 */                   int weight = ((Integer)blockToWeightMap.getOrDefault(bs.func_177230_c(), Integer.valueOf(0))).intValue();
/* 192 */                   double d1 = mid.func_186679_c(x + 0.5D, y + 0.5D, z + 0.5D);
/* 193 */                   Vec3d vec = new Vec3d(x, y, z);
/* 194 */                   boolean weightGood = weight > oreWeight;
/*     */                   
/* 196 */                   boolean distGood = (d0 == -1.0D) || (d1 < d0) || ((Math.floor(d1) == Math.floor(d0)) && (rnd.nextInt(range) == 0));
/* 197 */                   RayTraceResult rtr = drone.field_70170_p.func_147447_a(mid, vec.func_72441_c(0.5D, 0.5D, 0.5D), false, true, false);
/*     */                   
/* 199 */                   boolean visible = (rtr == null) || (rtr.func_178782_a().equals(bp));
/* 200 */                   if ((visible) && ((weightGood) || ((weight == oreWeight) && (distGood))))
/*     */                   {
/* 202 */                     d0 = d1;
/* 203 */                     oreWeight = weight;
/* 204 */                     orePos = vec;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             } }
/*     */         } } }
/* 210 */     if ((oreWeight > 0) && (orePos != null))
/*     */     {
/* 212 */       Vec3d dir = orePos.func_72441_c(0.5D, 0.5D, 0.5D).func_178788_d(mid);
/* 213 */       drone.flyNormalAlong(dir, 0.1D, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int overridePriority()
/*     */   {
/* 220 */     return 49;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean canOverrideDroneMovement(EntityDrone drone)
/*     */   {
/* 226 */     Integer[] limits = getLimits(drone.droneInfo);
/* 227 */     if (limits != null)
/*     */     {
/* 229 */       int range = getRange(drone);
/* 230 */       int minx = Math.min(limits[0].intValue(), limits[3].intValue()) - range;
/* 231 */       int maxx = Math.max(limits[0].intValue(), limits[3].intValue()) + range;
/* 232 */       int miny = Math.min(limits[1].intValue(), limits[4].intValue()) - range;
/* 233 */       int maxy = Math.max(limits[1].intValue(), limits[4].intValue()) + range;
/* 234 */       int minz = Math.min(limits[2].intValue(), limits[5].intValue()) - range;
/* 235 */       int maxz = Math.max(limits[2].intValue(), limits[5].intValue()) + range;
/* 236 */       if ((drone.field_70165_t < minx) || (drone.field_70165_t > maxx) || (drone.field_70163_u < miny) || (drone.field_70163_u > maxy) || (drone.field_70161_v < minz) || (drone.field_70161_v > maxz))
/*     */       {
/* 238 */         return false; }
/*     */     }
/* 240 */     return (drone.getFlyingMode() != 2) && (!getModNBT(drone.droneInfo).func_74767_n("Stay to mine"));
/*     */   }
/*     */   
/*     */   public int getRange(EntityDrone drone)
/*     */   {
/* 245 */     return drone.droneInfo.chip * 3;
/*     */   }
/*     */   
/*     */ 
/*     */   public void additionalTooltip(List<String> list, boolean forGuiDroneStatus)
/*     */   {
/* 251 */     super.additionalTooltip(list, forGuiDroneStatus);
/* 252 */     if (!forGuiDroneStatus)
/*     */     {
/* 254 */       list.add("Mine " + net.minecraft.util.text.TextFormatting.AQUA + canMineString(canMine()));
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Block> canMine()
/*     */   {
/* 260 */     List<Block> l = new ArrayList();
/* 261 */     for (int a = 1; a <= this.level * 3; a++)
/*     */     {
/* 263 */       l.addAll((Collection)weightToBlockMap.getOrDefault(Integer.valueOf(a), new ArrayList()));
/*     */     }
/* 265 */     return l;
/*     */   }
/*     */   
/*     */   public String canMineString(List<Block> l)
/*     */   {
/* 270 */     String s = "";
/* 271 */     if (!l.isEmpty())
/*     */     {
/* 273 */       s = s + oreName((Block)l.get(0));
/* 274 */       for (int a = 1; a < l.size(); a++)
/*     */       {
/* 276 */         s = s + ", " + oreName((Block)l.get(a));
/*     */       }
/*     */     }
/* 279 */     return s;
/*     */   }
/*     */   
/*     */   public String oreName(Block b)
/*     */   {
/* 284 */     String s = b.func_149732_F();
/* 285 */     if ((s.endsWith("Ore")) || (s.endsWith("ore"))) s = s.substring(0, s.length() - 4);
/* 286 */     if (s.endsWith("Block")) s = s.substring(0, s.length() - 6);
/* 287 */     return s;
/*     */   }
/*     */   
/*     */   public Integer[] getLimits(DroneInfo di)
/*     */   {
/* 292 */     NBTTagCompound tag = getModNBT(di);
/* 293 */     if (tag != null)
/*     */     {
/* 295 */       Integer x0 = tag.func_74764_b("x0") ? Integer.valueOf(tag.func_74762_e("x0")) : null;
/* 296 */       Integer y0 = tag.func_74764_b("y0") ? Integer.valueOf(tag.func_74762_e("y0")) : null;
/* 297 */       Integer z0 = tag.func_74764_b("z0") ? Integer.valueOf(tag.func_74762_e("z0")) : null;
/* 298 */       Integer x1 = tag.func_74764_b("x1") ? Integer.valueOf(tag.func_74762_e("x1")) : null;
/* 299 */       Integer y1 = tag.func_74764_b("y1") ? Integer.valueOf(tag.func_74762_e("y1")) : null;
/* 300 */       Integer z1 = tag.func_74764_b("z1") ? Integer.valueOf(tag.func_74762_e("z1")) : null;
/* 301 */       if ((x0 != null) && (y0 != null) && (z0 != null) && (x1 != null) && (y1 != null) && (z1 != null))
/*     */       {
/* 303 */         return new Integer[] { x0, y0, z0, x1, y1, z1 };
/*     */       }
/*     */     }
/* 306 */     return null;
/*     */   }
/*     */   
/*     */   public void setLimits(DroneInfo di, int x0, int y0, int z0, int x1, int y1, int z1)
/*     */   {
/* 311 */     NBTTagCompound tag = getModNBT(di);
/* 312 */     if (tag != null)
/*     */     {
/* 314 */       tag.func_74768_a("x0", x0);
/* 315 */       tag.func_74768_a("y0", y0);
/* 316 */       tag.func_74768_a("z0", z0);
/* 317 */       tag.func_74768_a("x1", x1);
/* 318 */       tag.func_74768_a("y1", y1);
/* 319 */       tag.func_74768_a("z1", z1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void removeLimits(DroneInfo di)
/*     */   {
/* 326 */     NBTTagCompound tag = getModNBT(di);
/* 327 */     if (tag != null)
/*     */     {
/* 329 */       tag.func_82580_o("x0");
/* 330 */       tag.func_82580_o("y0");
/* 331 */       tag.func_82580_o("z0");
/* 332 */       tag.func_82580_o("x1");
/* 333 */       tag.func_82580_o("y1");
/* 334 */       tag.func_82580_o("z1");
/*     */     }
/*     */   }
/*     */   
/*     */   @SideOnly(Side.CLIENT)
/*     */   public class ModuleMineGui<T extends ModuleMine>
/*     */     extends Module.ModuleGui<T>
/*     */   {
/* 342 */     GuiTextField[] textFields = new GuiTextField[6];
/*     */     
/*     */     public ModuleMineGui(T gui)
/*     */     {
/* 346 */       super(mod);
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73866_w_()
/*     */     {
/* 352 */       super.func_73866_w_();
/* 353 */       int tx0 = 80;
/* 354 */       int tw = 45;
/* 355 */       int ty0 = 37;
/* 356 */       int ty1 = 52;
/* 357 */       int th = 12;
/* 358 */       this.textFields[0] = new GuiTextField(1, this.field_146289_q, this.field_146294_l / 2 - tx0, this.field_146295_m / 2 + ty0, tw, th);
/* 359 */       this.textFields[1] = new GuiTextField(2, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw + 5, this.field_146295_m / 2 + ty0, tw, th);
/* 360 */       this.textFields[2] = new GuiTextField(3, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw * 2 + 10, this.field_146295_m / 2 + ty0, tw, th);
/*     */       
/* 362 */       this.textFields[3] = new GuiTextField(4, this.field_146289_q, this.field_146294_l / 2 - tx0, this.field_146295_m / 2 + ty1, tw, th);
/* 363 */       this.textFields[4] = new GuiTextField(5, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw + 5, this.field_146295_m / 2 + ty1, tw, th);
/* 364 */       this.textFields[5] = new GuiTextField(6, this.field_146289_q, this.field_146294_l / 2 - tx0 + tw * 2 + 10, this.field_146295_m / 2 + ty1, tw, th);
/*     */       
/* 366 */       for (GuiTextField txtf : this.textFields)
/*     */       {
/* 368 */         txtf.func_146203_f(6);
/*     */       }
/* 370 */       this.field_146292_n.add(new GuiButtonExt(1, this.field_146294_l / 2 + 70, this.field_146295_m / 2 + 35, 60, 30, "Set limits"));
/* 371 */       this.field_146292_n.add(new GuiButtonExt(2, this.field_146294_l / 2 - 35, this.field_146295_m / 2 + 70, 80, 20, "Remove limits"));
/* 372 */       this.field_146292_n.add(new GuiButtonExt(3, this.field_146294_l / 2 - 98, this.field_146295_m / 2 + 70, 60, 20, "Auto limits"));
/* 373 */       setLimitTexts();
/*     */     }
/*     */     
/*     */     public void setLimitTexts()
/*     */     {
/* 378 */       Integer[] ints = ((ModuleMine)this.mod).getLimits(this.parent.drone.droneInfo);
/* 379 */       if (ints != null)
/*     */       {
/* 381 */         for (int a = 0; a < 6; a++)
/*     */         {
/* 383 */           this.textFields[a].func_146180_a(String.valueOf(ints[a]));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */       throws IOException
/*     */     {
/* 391 */       super.func_73864_a(mouseX, mouseY, mouseButton);
/* 392 */       for (GuiTextField txtf : this.textFields)
/*     */       {
/* 394 */         txtf.func_146192_a(mouseX, mouseY, mouseButton);
/*     */       }
/*     */     }
/*     */     
/*     */     protected void func_73869_a(char typedChar, int keyCode)
/*     */       throws IOException
/*     */     {
/* 401 */       super.func_73869_a(typedChar, keyCode);
/* 402 */       if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211) || (keyCode == 12) || (keyCode == 74))
/*     */       {
/* 404 */         for (GuiTextField txtf : this.textFields)
/*     */         {
/* 406 */           if ((txtf != null) && (txtf.func_146206_l()))
/*     */           {
/* 408 */             if (((keyCode != 12) && (keyCode != 74)) || (txtf.func_146198_h() == 0))
/*     */             {
/* 410 */               txtf.func_146201_a(typedChar, keyCode);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void buttonClickedOnEnabledGui(GuiButton button)
/*     */     {
/* 420 */       super.buttonClickedOnEnabledGui(button);
/* 421 */       Integer[] ints; int a; String s; if (button.field_146127_k == 1)
/*     */       {
/* 423 */         ints = new Integer[6];
/* 424 */         for (a = 0; a < 6; a++)
/*     */         {
/* 426 */           s = this.textFields[a].func_146179_b();
/* 427 */           if ((s.length() > 0) && (!s.equals("-")))
/*     */           {
/* 429 */             ints[a] = Integer.valueOf(s);
/*     */           }
/*     */           else
/*     */           {
/* 433 */             ints = null;
/* 434 */             break;
/*     */           }
/*     */         }
/* 437 */         if (ints != null)
/*     */         {
/* 439 */           PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, ints[0].intValue(), ints[1].intValue(), ints[2]
/* 440 */             .intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue()));
/* 441 */           ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, ints[0].intValue(), ints[1].intValue(), ints[2].intValue(), ints[3].intValue(), ints[4].intValue(), ints[5].intValue());
/* 442 */           setLimitTexts();
/*     */         }
/*     */       }
/* 445 */       else if (button.field_146127_k == 2)
/*     */       {
/* 447 */         PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, true, 0, 0, 0, 0, 0, 0));
/* 448 */         ((ModuleMine)this.mod).removeLimits(this.parent.drone.droneInfo);
/* 449 */         ints = this.textFields;a = ints.length; for (s = 0; s < a; s++) { GuiTextField txtf = ints[s];
/*     */           
/* 451 */           txtf.func_146180_a("");
/*     */         }
/*     */       }
/* 454 */       else if (button.field_146127_k == 3)
/*     */       {
/* 456 */         int limitRange = ModuleMine.this.getRange(this.parent.drone);
/* 457 */         int minX = (int)(this.parent.drone.field_70165_t - limitRange);
/* 458 */         int minY = (int)Math.max(this.parent.drone.field_70163_u - limitRange, 0.0D);
/* 459 */         int minZ = (int)(this.parent.drone.field_70161_v - limitRange);
/* 460 */         int maxX = (int)(this.parent.drone.field_70165_t + limitRange);
/* 461 */         int maxY = (int)Math.max(this.parent.drone.field_70163_u, 0.0D);
/* 462 */         int maxZ = (int)(this.parent.drone.field_70161_v + limitRange);
/* 463 */         PacketDispatcher.sendToServer(new PacketDroneSetMineLimits(this.parent.drone, false, minX, minY, minZ, maxX, maxY, maxZ));
/*     */         
/* 465 */         ((ModuleMine)this.mod).setLimits(this.parent.drone.droneInfo, minX, minY, minZ, maxX, maxY, maxZ);
/* 466 */         setLimitTexts();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */     {
/* 473 */       ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 474 */       int sclW = sr.func_78326_a();
/* 475 */       int sclH = sr.func_78328_b();
/* 476 */       for (GuiTextField txtf : this.textFields)
/*     */       {
/* 478 */         txtf.func_146194_f();
/*     */       }
/* 480 */       super.func_73863_a(mouseX, mouseY, partialTicks);
/* 481 */       this.field_146289_q.func_175065_a("X", sclW / 2 - 60, sclH / 2 + 28, 16777215, true);
/* 482 */       this.field_146289_q.func_175065_a("Y", sclW / 2 - 10, sclH / 2 + 28, 16777215, true);
/* 483 */       this.field_146289_q.func_175065_a("Z", sclW / 2 + 40, sclH / 2 + 28, 16777215, true);
/* 484 */       this.field_146289_q.func_175065_a("Corner A:", sclW / 2 - 135, sclH / 2 + 40, 16777215, true);
/* 485 */       this.field_146289_q.func_175065_a("Corner B:", sclW / 2 - 135, sclH / 2 + 55, 16777215, true);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModuleMine.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */