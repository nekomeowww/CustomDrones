/*      */ package williamle.drones.entity;
/*      */ 
/*      */ import com.google.common.base.Predicate;
/*      */ import com.google.common.collect.Lists;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Random;
/*      */ import javax.annotation.Nullable;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.crash.CrashReport;
/*      */ import net.minecraft.crash.CrashReportCategory;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityFlying;
/*      */ import net.minecraft.entity.IProjectile;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.item.EntityXPOrb;
/*      */ import net.minecraft.entity.monster.EntityPigZombie;
/*      */ import net.minecraft.entity.monster.EntitySkeleton;
/*      */ import net.minecraft.entity.monster.EntityZombie;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.entity.player.InventoryPlayer;
/*      */ import net.minecraft.init.SoundEvents;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.datasync.DataParameter;
/*      */ import net.minecraft.network.datasync.DataSerializers;
/*      */ import net.minecraft.network.datasync.EntityDataManager;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EntitySelectors;
/*      */ import net.minecraft.util.EnumActionResult;
/*      */ import net.minecraft.util.EnumHand;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.ReportedException;
/*      */ import net.minecraft.util.SoundEvent;
/*      */ import net.minecraft.util.math.AxisAlignedBB;
/*      */ import net.minecraft.util.math.BlockPos;
/*      */ import net.minecraft.util.math.BlockPos.PooledMutableBlockPos;
/*      */ import net.minecraft.util.math.MathHelper;
/*      */ import net.minecraft.util.math.Vec3d;
/*      */ import net.minecraft.util.text.ITextComponent;
/*      */ import net.minecraft.util.text.TextFormatting;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*      */ import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
/*      */ import net.minecraftforge.fml.relauncher.Side;
/*      */ import net.minecraftforge.fml.relauncher.SideOnly;
/*      */ import williamle.drones.DronesMod;
/*      */ import williamle.drones.api.helpers.EntityHelper;
/*      */ import williamle.drones.api.helpers.VecHelper;
/*      */ import williamle.drones.api.path.Node;
/*      */ import williamle.drones.api.path.Path;
/*      */ import williamle.drones.drone.DroneInfo;
/*      */ import williamle.drones.drone.DroneInfo.ApplyResult;
/*      */ import williamle.drones.drone.DroneInfo.ApplyType;
/*      */ import williamle.drones.drone.module.Module;
/*      */ import williamle.drones.item.IItemInteractDrone;
/*      */ import williamle.drones.item.ItemDroneFlyer;
/*      */ import williamle.drones.item.ItemDroneModule;
/*      */ import williamle.drones.network.PacketDispatcher;
/*      */ import williamle.drones.network.server.PacketDroneSetCameraMode;
/*      */ import williamle.drones.network.server.PacketDroneSetCameraPitch;
/*      */ 
/*      */ public class EntityDrone extends EntityFlying implements IEntityAdditionalSpawnData
/*      */ {
/*   71 */   public static int DroneNextID = 0;
/*   72 */   private static final DataParameter<Boolean> DICHANGED = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187198_h);
/*      */   
/*   74 */   private static final DataParameter<Integer> FLYMODE = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187192_b);
/*      */   
/*   76 */   private static final DataParameter<Boolean> CAMERA = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187198_h);
/*      */   
/*   78 */   private static final DataParameter<Float> CAMERAPITCH = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187193_c);
/*      */   
/*   80 */   private static final DataParameter<String> CONTROLPLAYER = EntityDataManager.func_187226_a(EntityDrone.class, DataSerializers.field_187194_d);
/*      */   
/*      */   public DroneInfo droneInfo;
/*      */   
/*   84 */   public double wingRotate = 0.0D;
/*      */   public float acceX;
/*      */   public float acceY;
/*   87 */   public float acceZ; public double prevMotionX; public double prevMotionY; public double prevMotionZ; public boolean idle = true;
/*      */   public Path automatedPath;
/*      */   public Path recordingPath;
/*      */   private EntityPlayer controllingPlayer;
/*      */   public Entity entityToAttack;
/*   92 */   public int attackDelay = 0;
/*   93 */   public int pickupDelay = 0;
/*   94 */   public int hitDelay = 0;
/*      */   
/*      */   public EntityDrone(World worldIn)
/*      */   {
/*   98 */     super(worldIn);
/*   99 */     func_70105_a(0.8F, 0.3F);
/*  100 */     this.droneInfo = new DroneInfo(this);
/*  101 */     applyDroneAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */   public ITextComponent func_145748_c_()
/*      */   {
/*  107 */     return new net.minecraft.util.text.TextComponentString(this.droneInfo.getDisplayName());
/*      */   }
/*      */   
/*      */   public int getDroneID()
/*      */   {
/*  112 */     return this.droneInfo == null ? -1 : this.droneInfo.id;
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean func_70692_ba()
/*      */   {
/*  118 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_70088_a()
/*      */   {
/*  124 */     super.func_70088_a();
/*  125 */     this.field_70180_af.func_187214_a(DICHANGED, Boolean.valueOf(false));
/*  126 */     this.field_70180_af.func_187214_a(FLYMODE, Integer.valueOf(0));
/*  127 */     this.field_70180_af.func_187214_a(CAMERA, Boolean.valueOf(false));
/*  128 */     this.field_70180_af.func_187214_a(CAMERAPITCH, Float.valueOf(0.0F));
/*  129 */     this.field_70180_af.func_187214_a(CONTROLPLAYER, "");
/*      */   }
/*      */   
/*      */   protected void applyDroneAttributes()
/*      */   {
/*  134 */     func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a(this.droneInfo.getMaxDamage(this));
/*      */   }
/*      */   
/*      */ 
/*      */   public Vec3d func_70040_Z()
/*      */   {
/*  140 */     return func_70676_i(1.0F);
/*      */   }
/*      */   
/*      */   public Vec3d getHorizontalLookVec()
/*      */   {
/*  145 */     return func_174806_f(0.0F, this.field_70177_z);
/*      */   }
/*      */   
/*      */ 
/*      */   public float func_70047_e()
/*      */   {
/*  151 */     return 0.15F;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_70067_L()
/*      */   {
/*  157 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_70104_M()
/*      */   {
/*  163 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean func_70041_e_()
/*      */   {
/*  169 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean canRiderInteract()
/*      */   {
/*  175 */     return true;
/*      */   }
/*      */   
/*      */   @SideOnly(Side.CLIENT)
/*      */   public int func_70070_b(float partialTicks)
/*      */   {
/*  181 */     return super.func_70070_b(partialTicks);
/*      */   }
/*      */   
/*      */ 
/*      */   public float func_70013_c(float partialTicks)
/*      */   {
/*  187 */     return super.func_70013_c(partialTicks);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void func_180430_e(float distance, float damageMultiplier) {}
/*      */   
/*      */ 
/*      */ 
/*      */   public EnumActionResult func_184199_a(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
/*      */   {
/*  198 */     ItemStack stack = player.func_184614_ca();
/*  199 */     boolean hasController = playerHasCorrespondingController(player, false);
/*  200 */     if (usefulInteraction(player, vec, stack, hand, hasController))
/*      */     {
/*  202 */       if ((stack == null) || (player.func_70093_af()))
/*      */       {
/*  204 */         if (hasController)
/*      */         {
/*  206 */           player.openGui(DronesMod.instance, 1, this.field_70170_p, getDroneID(), 0, 0);
/*  207 */           return EnumActionResult.SUCCESS;
/*      */         }
/*      */         
/*      */ 
/*  211 */         EntityHelper.addChat(player, 1, TextFormatting.RED + "No corresponding controller");
/*  212 */         return EnumActionResult.FAIL;
/*      */       }
/*      */       
/*  215 */       if (stack != null)
/*      */       {
/*  217 */         boolean itemHasUse = this.droneInfo.canApplyStack(stack).type != DroneInfo.ApplyType.NONE;
/*  218 */         if (itemHasUse)
/*      */         {
/*  220 */           if (hasController)
/*      */           {
/*  222 */             if (stack.func_77973_b() == DronesMod.droneModule)
/*      */             {
/*  224 */               Module mod = ItemDroneModule.getModule(stack);
/*  225 */               DroneInfo.ApplyResult addEntry = this.droneInfo.canAddModule(mod);
/*  226 */               if (addEntry.successful)
/*      */               {
/*  228 */                 if (!this.field_70170_p.field_72995_K)
/*      */                 {
/*  230 */                   this.droneInfo.applyModule(mod);
/*  231 */                   this.droneInfo.updateDroneInfoToClient(player);
/*  232 */                   player.func_184611_a(EnumHand.MAIN_HAND, null);
/*      */                 }
/*      */               }
/*  235 */               EntityHelper.addChat(player, 1, addEntry.displayString);
/*      */             }
/*      */             else
/*      */             {
/*  239 */               stack = this.droneInfo.applyItem(this, stack);
/*  240 */               player.func_184611_a(EnumHand.MAIN_HAND, stack);
/*      */             }
/*  242 */             return EnumActionResult.SUCCESS;
/*      */           }
/*      */           
/*      */ 
/*  246 */           EntityHelper.addChat(player, 1, TextFormatting.RED + "No corresponding controller");
/*  247 */           return EnumActionResult.FAIL;
/*      */         }
/*      */         
/*  250 */         if ((stack.func_77973_b() instanceof IItemInteractDrone))
/*      */         {
/*  252 */           return ((IItemInteractDrone)stack.func_77973_b()).interactWithDrone(this.field_70170_p, this, player, vec, stack, hand);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  257 */     return super.func_184199_a(player, vec, is, hand);
/*      */   }
/*      */   
/*      */   public boolean usefulInteraction(EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand, boolean hasController)
/*      */   {
/*  262 */     return true;
/*      */   }
/*      */   
/*      */   public boolean playerHasCorrespondingController(EntityPlayer player, boolean mustInHand)
/*      */   {
/*  267 */     if (this.droneInfo.droneFreq == -1) return true;
/*  268 */     if (mustInHand)
/*      */     {
/*  270 */       return DronesMod.droneFlyer.getControllerFreq(player.func_184614_ca()) == this.droneInfo.droneFreq;
/*      */     }
/*  272 */     InventoryPlayer inv = player.field_71071_by;
/*  273 */     for (int a = 0; a < inv.func_70302_i_(); a++)
/*      */     {
/*  275 */       if (DronesMod.droneFlyer.getControllerFreq(inv.func_70301_a(a)) == this.droneInfo.droneFreq)
/*      */       {
/*  277 */         return true;
/*      */       }
/*      */     }
/*  280 */     return false;
/*      */   }
/*      */   
/*      */   public Entity getRider()
/*      */   {
/*  285 */     return func_184179_bs();
/*      */   }
/*      */   
/*      */ 
/*      */   public double func_70042_X()
/*      */   {
/*  291 */     if (getRider() != null)
/*      */     {
/*  293 */       return 0.0D - getRiderHeight();
/*      */     }
/*  295 */     return 0.25D;
/*      */   }
/*      */   
/*      */   public double getRiderHeight()
/*      */   {
/*  300 */     double h = 0.0D;
/*  301 */     if (getRider() == null) { h = 0.0D;
/*  302 */     } else if (((getRider() instanceof EntityPlayer)) || ((getRider() instanceof EntityZombie)) || 
/*  303 */       ((getRider() instanceof EntityPigZombie)) || ((getRider() instanceof EntitySkeleton)))
/*      */     {
/*  305 */       h = getRider().field_70131_O * 0.88D;
/*      */     }
/*      */     else
/*      */     {
/*  309 */       h = getRider().field_70131_O * 1.15D;
/*      */     }
/*  311 */     return h;
/*      */   }
/*      */   
/*      */   public void dropRider()
/*      */   {
/*  316 */     if (getRider() != null)
/*      */     {
/*  318 */       getRider().func_184210_p();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean hasEnabled(Module m)
/*      */   {
/*  324 */     return this.droneInfo.hasEnabled(m);
/*      */   }
/*      */   
/*      */ 
/*      */   public int getFlyingMode()
/*      */   {
/*  330 */     return ((Integer)func_184212_Q().func_187225_a(FLYMODE)).intValue();
/*      */   }
/*      */   
/*      */   public String getFlyingModeString()
/*      */   {
/*  335 */     int droneModeI = getFlyingMode();
/*  336 */     if (droneModeI == 0) return "off";
/*  337 */     if (droneModeI == 1) return "hovering";
/*  338 */     if (droneModeI == 2) return "manual control";
/*  339 */     if (droneModeI == 3) return "preset path";
/*  340 */     if (droneModeI == 4) return "follow controller";
/*  341 */     return "unknown";
/*      */   }
/*      */   
/*      */ 
/*      */   public void setFlyingMode(int i)
/*      */   {
/*  347 */     func_184212_Q().func_187227_b(FLYMODE, Integer.valueOf(i));
/*      */   }
/*      */   
/*      */   public void setNextFlyingMode()
/*      */   {
/*  352 */     int mode = getFlyingMode() + 1;
/*  353 */     if (mode == 5) mode = 0;
/*  354 */     setFlyingMode(mode);
/*  355 */     if (!canDroneHaveFlyMode(mode))
/*      */     {
/*  357 */       setNextFlyingMode();
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean canDroneHaveFlyMode(int mode)
/*      */   {
/*  363 */     if ((mode == 0) || (mode == 1)) return true;
/*  364 */     if ((mode == 2) && (hasEnabled(Module.controlMovement))) return true;
/*  365 */     if ((mode == 3) && (hasEnabled(Module.pathMovement))) return true;
/*  366 */     if ((mode == 4) && (hasEnabled(Module.followMovement))) return true;
/*  367 */     return false;
/*      */   }
/*      */   
/*      */   public boolean getWatchedDIChanged()
/*      */   {
/*  372 */     return ((Boolean)this.field_70180_af.func_187225_a(DICHANGED)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setWatchedDIChanged(boolean b)
/*      */   {
/*  377 */     func_184212_Q().func_187227_b(DICHANGED, Boolean.valueOf(b));
/*      */   }
/*      */   
/*      */   public void setCameraMode(boolean b)
/*      */   {
/*  382 */     if (this.field_70170_p.field_72995_K)
/*      */     {
/*  384 */       PacketDispatcher.sendToServer(new PacketDroneSetCameraMode(this, b));
/*      */     } else {
/*  386 */       func_184212_Q().func_187227_b(CAMERA, Boolean.valueOf(b));
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean getCameraMode() {
/*  391 */     return ((Boolean)func_184212_Q().func_187225_a(CAMERA)).booleanValue();
/*      */   }
/*      */   
/*      */   public void setCameraPitch(float f)
/*      */   {
/*  396 */     if (this.field_70170_p.field_72995_K)
/*      */     {
/*  398 */       PacketDispatcher.sendToServer(new PacketDroneSetCameraPitch(this, f));
/*      */     }
/*  400 */     func_184212_Q().func_187227_b(CAMERAPITCH, Float.valueOf(f));
/*      */   }
/*      */   
/*      */   public float getCameraPitch()
/*      */   {
/*  405 */     return ((Float)func_184212_Q().func_187225_a(CAMERAPITCH)).floatValue();
/*      */   }
/*      */   
/*      */ 
/*      */   public void setControllingPlayer(EntityPlayer p)
/*      */   {
/*  411 */     this.controllingPlayer = p;
/*  412 */     func_184212_Q().func_187227_b(CONTROLPLAYER, p == null ? "" : p.func_70005_c_());
/*      */   }
/*      */   
/*      */   public EntityPlayer getControllingPlayer()
/*      */   {
/*  417 */     String s = (String)func_184212_Q().func_187225_a(CONTROLPLAYER);
/*  418 */     if ((s.length() == 0) || (s.equals(""))) { this.controllingPlayer = null;
/*  419 */     } else if ((this.controllingPlayer == null) || (!this.controllingPlayer.func_70005_c_().equals(s)))
/*      */     {
/*  421 */       this.controllingPlayer = this.field_70170_p.func_72924_a(s);
/*      */     }
/*  423 */     return this.controllingPlayer;
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70030_z()
/*      */   {
/*  429 */     super.func_70030_z();
/*  430 */     if (this.hitDelay > 0) this.hitDelay -= 1;
/*  431 */     if (this.attackDelay > 0) this.attackDelay -= 1;
/*  432 */     if ((this.pickupDelay > 0) && (getRider() == null)) this.pickupDelay -= 1;
/*  433 */     if (!this.field_70170_p.field_72995_K) setWatchedDIChanged(this.droneInfo.isChanged);
/*  434 */     if ((getDroneAttackTarget() == null) || (getDroneAttackTarget().field_70128_L))
/*      */     {
/*  436 */       setDroneAttackTarget(null, true);
/*      */     }
/*  438 */     updateFlyingBehavior();
/*  439 */     updateRotation();
/*  440 */     updateWingRotation();
/*  441 */     this.droneInfo.updateDroneInfo(this);
/*  442 */     double speedControl = 1.0D - 0.087D * Math.pow(this.droneInfo.getMaxSpeed(), 0.1D);
/*  443 */     this.field_70159_w *= speedControl;
/*  444 */     this.field_70181_x *= speedControl;
/*  445 */     this.field_70179_y *= speedControl;
/*  446 */     func_70091_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
/*  447 */     func_85033_bc();
/*  448 */     func_145771_j(this.field_70165_t, this.field_70163_u, this.field_70161_v);
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateRotation()
/*      */   {
/*  454 */     double maxYawChange = 6.0D;
/*  455 */     double nextYaw = (float)(Math.atan2(-this.field_70159_w, this.field_70179_y) / 3.141592653589793D * 180.0D);
/*  456 */     if ((Math.abs(this.field_70159_w) <= 0.001D) && (Math.abs(this.field_70179_y) <= 0.001D)) { nextYaw = this.field_70177_z;
/*      */     }
/*  458 */     double changingYaw = nextYaw - this.field_70177_z;
/*  459 */     changingYaw %= 360.0D;
/*  460 */     if (360.0D - Math.abs(changingYaw) < Math.abs(changingYaw)) changingYaw *= -1.0D;
/*  461 */     if (changingYaw > 0.0D) { changingYaw = Math.min(maxYawChange, changingYaw);
/*  462 */     } else if (changingYaw < 0.0D) { changingYaw = -Math.min(maxYawChange, -changingYaw);
/*      */     }
/*  464 */     this.field_70177_z = ((float)(this.field_70177_z + changingYaw));
/*  465 */     this.field_70177_z %= 360.0F;
/*      */     
/*  467 */     if ((getCameraMode()) && (getControllingPlayer() != null))
/*      */     {
/*  469 */       if (this.field_70170_p.field_72995_K) setCameraPitch(getControllingPlayer().field_70125_A);
/*      */     }
/*  471 */     double nextPitch = getCameraPitch();
/*  472 */     double maxPitchChange = 3.0D;
/*  473 */     double changingPitch = nextPitch - this.field_70125_A;
/*  474 */     if (changingPitch > 0.0D) { changingPitch = Math.min(maxPitchChange, changingPitch);
/*  475 */     } else if (changingPitch < 0.0D) changingPitch = -Math.min(maxPitchChange, -changingPitch);
/*  476 */     this.field_70125_A = ((float)(this.field_70125_A + changingPitch));
/*      */   }
/*      */   
/*      */   public void updateFlyingBehavior()
/*      */   {
/*  481 */     int mode = getFlyingMode();
/*  482 */     boolean overridenByMod = false;
/*  483 */     Module overridenMod = null;
/*  484 */     for (int a = 0; a < this.droneInfo.mods.size(); a++)
/*      */     {
/*  486 */       Module m = (Module)this.droneInfo.mods.get(a);
/*  487 */       if ((hasEnabled(m)) && (m.canOverrideDroneMovement(this)))
/*      */       {
/*  489 */         overridenByMod = true;
/*  490 */         overridenMod = (overridenMod == null) || (overridenMod.overridePriority() < m.overridePriority()) ? m : overridenMod;
/*      */       }
/*      */     }
/*      */     
/*  494 */     if (this.droneInfo.getBattery(false) <= 0.0D)
/*      */     {
/*  496 */       this.idle = true;
/*  497 */       this.field_70181_x -= 0.08D;
/*  498 */       if (getRider() != null) getRider().func_184210_p();
/*      */     }
/*      */     else
/*      */     {
/*  502 */       this.idle = false;
/*  503 */       if (mode == 0)
/*      */       {
/*  505 */         this.idle = true;
/*  506 */         this.field_70181_x -= 0.08D;
/*  507 */         if (getRider() != null) getRider().func_184210_p();
/*      */       }
/*  509 */       else if (mode == 1)
/*      */       {
/*  511 */         this.field_70181_x -= 0.08D;
/*  512 */         double belowY = getBelowSurfaceY();
/*  513 */         double distY = this.field_70163_u - belowY;
/*  514 */         double maxDist = 1.5D + getRiderHeight();
/*  515 */         if (distY < maxDist)
/*      */         {
/*  517 */           this.field_70181_x += 0.08D * Math.pow((maxDist - distY) * 2.0D, 0.5D) * this.droneInfo.getEngineLevel();
/*      */         }
/*  519 */         this.idle = true;
/*      */       }
/*      */       else
/*      */       {
/*  523 */         double belowY = getBelowSurfaceY();
/*  524 */         double distY = this.field_70163_u - belowY;
/*  525 */         if (distY > getRiderHeight())
/*      */         {
/*  527 */           Entity rider = getRider();
/*  528 */           double riderWeight = rider != null ? 0.5D + rider.field_70130_N * rider.field_70131_O : 0.5D;
/*  529 */           double pullUpPower = this.droneInfo.getEngineLevel() * Math.sqrt(this.droneInfo.getMaxSpeed());
/*  530 */           if (pullUpPower < riderWeight) this.field_70181_x -= 0.08D * (riderWeight - pullUpPower) / riderWeight;
/*      */         }
/*  532 */         if (overridenByMod)
/*      */         {
/*  534 */           this.idle = false;
/*  535 */           overridenMod.overrideDroneMovement(this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void flyTo(Vec3d pos, double minMove, double rate)
/*      */   {
/*  543 */     flyNormalAlong(VecHelper.fromTo(func_174791_d(), pos), minMove, rate);
/*      */   }
/*      */   
/*      */   public void flyNormalAlong(Vec3d vec, double minMove, double rate)
/*      */   {
/*  548 */     if ((vec != null) && (vec.func_72433_c() >= minMove))
/*      */     {
/*  550 */       Vec3d vec1 = vec.func_72433_c() > 1.0D ? vec.func_72432_b() : vec.func_72441_c(0.0D, 0.0D, 0.0D);
/*  551 */       flyNormalAlong(vec1.field_72450_a, vec1.field_72448_b, vec1.field_72449_c, rate);
/*      */     }
/*      */   }
/*      */   
/*      */   public void flyNormalAlong(double x, double y, double z, double rate)
/*      */   {
/*  557 */     double speedMult = getSpeedMultiplication() * rate;
/*  558 */     this.field_70159_w += x * speedMult;
/*  559 */     this.field_70181_x += y * speedMult;
/*  560 */     this.field_70179_y += z * speedMult;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public EntityPlayer getNearestPlayer(double distance, int hasController)
/*      */   {
/*  569 */     double d0 = -1.0D;
/*  570 */     EntityPlayer entityplayer = null;
/*      */     
/*  572 */     for (int i = 0; i < this.field_70170_p.field_73010_i.size(); i++)
/*      */     {
/*  574 */       EntityPlayer entityplayer1 = (EntityPlayer)this.field_70170_p.field_73010_i.get(i);
/*      */       
/*  576 */       if (EntitySelectors.field_180132_d.apply(entityplayer1)) { if (hasController != 0) {
/*  577 */           if (!playerHasCorrespondingController(entityplayer1, hasController == 2)) {}
/*      */         } else {
/*  579 */           double d1 = entityplayer1.func_70092_e(this.field_70165_t, this.field_70163_u, this.field_70161_v);
/*      */           
/*  581 */           if (((distance < 0.0D) || (d1 < distance * distance)) && ((d0 == -1.0D) || (d1 < d0)))
/*      */           {
/*  583 */             d0 = d1;
/*  584 */             entityplayer = entityplayer1;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  589 */     return entityplayer;
/*      */   }
/*      */   
/*      */   public boolean isControllerFlying()
/*      */   {
/*  594 */     int mode = getFlyingMode();
/*  595 */     return (mode == 2) || ((mode == 3) && (this.recordingPath != null));
/*      */   }
/*      */   
/*      */   public double getSpeedMultiplication()
/*      */   {
/*  600 */     return 0.005D * this.droneInfo.getMaxSpeed() * this.droneInfo.getEngineLevel();
/*      */   }
/*      */   
/*      */   public void applyRecordPath(boolean loop)
/*      */   {
/*  605 */     this.automatedPath = this.recordingPath.simplifiedPath();
/*  606 */     this.recordingPath = null;
/*  607 */     if (!loop)
/*      */     {
/*  609 */       List<Node> nodes = new ArrayList();
/*  610 */       for (int a = this.automatedPath.nodes.size() - 1; a > 0; a--)
/*      */       {
/*  612 */         nodes.add(this.automatedPath.nodes.get(a));
/*      */       }
/*  614 */       this.automatedPath.pathIndex = (this.automatedPath.nodes.size() - 1);
/*  615 */       this.automatedPath.nodes.addAll(nodes);
/*      */     }
/*      */   }
/*      */   
/*      */   public double getBelowSurfaceY()
/*      */   {
/*  621 */     double yMax = -1.0D;
/*  622 */     for (int x = (int)Math.floor(func_174813_aQ().field_72340_a); x <= func_174813_aQ().field_72336_d; x++)
/*      */     {
/*  624 */       for (int z = (int)Math.floor(func_174813_aQ().field_72339_c); z <= func_174813_aQ().field_72334_f; z++)
/*      */       {
/*  626 */         for (int y = (int)Math.floor(this.field_70163_u); y > 0; y--)
/*      */         {
/*  628 */           BlockPos pos = new BlockPos(x, y, z);
/*  629 */           IBlockState bs = this.field_70170_p.func_180495_p(pos);
/*  630 */           if ((!bs.func_185904_a().func_76222_j()) || (bs.func_185904_a() != Material.field_151579_a))
/*      */           {
/*  632 */             double posMax = pos.func_177956_o();
/*  633 */             if (this.field_70170_p.func_180495_p(pos).func_185890_d(this.field_70170_p, pos) != null)
/*  634 */               posMax += this.field_70170_p.func_180495_p(pos).func_185890_d(this.field_70170_p, pos).field_72337_e;
/*  635 */             yMax = Math.max(yMax, posMax);
/*  636 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  641 */     return yMax;
/*      */   }
/*      */   
/*      */   public double wingRotationIncrement()
/*      */   {
/*  646 */     int mode = getFlyingMode();
/*  647 */     double rate = Math.pow(this.droneInfo.getEngineLevel(), 0.3D);
/*  648 */     if (mode != 0)
/*      */     {
/*  650 */       double engine = 100.0D * rate;
/*  651 */       return engine;
/*      */     }
/*      */     
/*      */ 
/*  655 */     double add = (this.field_70159_w * this.field_70159_w + (this.field_70167_r - this.field_70163_u) * (this.field_70167_r - this.field_70163_u) + this.field_70179_y * this.field_70179_y) * 1000.0D;
/*  656 */     return add;
/*      */   }
/*      */   
/*      */ 
/*      */   public void updateWingRotation()
/*      */   {
/*  662 */     double increment = wingRotationIncrement();
/*  663 */     this.wingRotate += increment;
/*      */   }
/*      */   
/*      */   public double getWingRotation(float partialTick)
/*      */   {
/*  668 */     double increment = wingRotationIncrement();
/*  669 */     return this.wingRotate + increment * partialTick;
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70108_f(Entity e)
/*      */   {
/*  675 */     boolean modStopFurtherCollision = false;
/*  676 */     for (Iterator localIterator = this.droneInfo.mods.iterator(); localIterator.hasNext(); 
/*      */         
/*  678 */         modStopFurtherCollision = true)
/*      */     {
/*  676 */       Module m = (Module)localIterator.next();
/*      */       
/*  678 */       if ((m == null) || (!m.collideWithEntity(this, e))) {}
/*      */     }
/*  680 */     if (modStopFurtherCollision) { return;
/*      */     }
/*  682 */     if ((!(e instanceof EntityDrone)) || (shouldPushDrone((EntityDrone)e))) super.func_70108_f(e);
/*  683 */     if (getFlyingMode() != 0)
/*      */     {
/*  685 */       if ((getRider() == null) && (canPickUpEntity(e)))
/*      */       {
/*  687 */         if (this.pickupDelay == 0)
/*      */         {
/*  689 */           e.func_184220_m(this);
/*  690 */           func_70107_b(this.field_70165_t, e.field_70163_u + getRiderHeight(), this.field_70161_v);
/*  691 */           this.pickupDelay = 20;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  696 */         if (!this.droneInfo.hasEnabled(Module.weapon1)) return;
/*  697 */         if (e == getRider()) return;
/*  698 */         boolean causeDam = true;
/*  699 */         if (((e instanceof EntityDrone)) && (!shouldAttackDrone((EntityDrone)e)))
/*      */         {
/*  701 */           causeDam = false;
/*      */         }
/*  703 */         else if ((e instanceof EntityPlayer))
/*      */         {
/*  705 */           EntityPlayer p = (EntityPlayer)e;
/*  706 */           for (int a = 0; a < p.field_71071_by.func_70302_i_(); a++)
/*      */           {
/*  708 */             ItemStack is = p.field_71071_by.func_70301_a(a);
/*  709 */             if ((is != null) && (is.func_77973_b() == DronesMod.droneFlyer) && 
/*  710 */               (DronesMod.droneFlyer.getControllerFreq(is) == this.droneInfo.droneFreq))
/*      */             {
/*  712 */               causeDam = false;
/*  713 */               break;
/*      */             }
/*      */           }
/*      */         }
/*  717 */         if (causeDam)
/*      */         {
/*  719 */           double spdSqr = this.field_70159_w * this.field_70159_w + this.field_70181_x * this.field_70181_x + this.field_70179_y * this.field_70179_y;
/*  720 */           double spdDmgRate = Math.sqrt(spdSqr) * 20.0D / this.droneInfo.getMaxSpeed();
/*  721 */           double baseDmg = this.droneInfo.getAttackPower(this);
/*  722 */           double damage = baseDmg * 0.8D * spdDmgRate + 0.2D * baseDmg;
/*  723 */           attackEntity(e, getAttackDamageSource(), (float)damage);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void func_85033_bc()
/*      */   {
/*  731 */     List<Entity> list = this.field_70170_p.func_72839_b(this, 
/*  732 */       func_174813_aQ().func_186662_g(func_70111_Y()));
/*  733 */     if (!list.isEmpty())
/*      */     {
/*  735 */       for (int i = 0; i < list.size(); i++)
/*      */       {
/*  737 */         Entity entity = (Entity)list.get(i);
/*  738 */         func_70108_f(entity);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_145775_I()
/*      */   {
/*  746 */     AxisAlignedBB axisalignedbb = func_174813_aQ().func_72317_d(this.field_70159_w, this.field_70181_x, this.field_70179_y);
/*      */     
/*  748 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72340_a + 0.001D, axisalignedbb.field_72338_b + 0.001D, axisalignedbb.field_72339_c + 0.001D);
/*      */     
/*  750 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos1 = BlockPos.PooledMutableBlockPos.func_185345_c(axisalignedbb.field_72336_d - 0.001D, axisalignedbb.field_72337_e - 0.001D, axisalignedbb.field_72334_f - 0.001D);
/*  751 */     BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos2 = BlockPos.PooledMutableBlockPos.func_185346_s();
/*      */     
/*  753 */     if (this.field_70170_p.func_175707_a(blockpos$pooledmutableblockpos, blockpos$pooledmutableblockpos1))
/*      */     {
/*  755 */       for (int i = blockpos$pooledmutableblockpos.func_177958_n(); i <= blockpos$pooledmutableblockpos1.func_177958_n(); i++)
/*      */       {
/*  757 */         for (int j = blockpos$pooledmutableblockpos.func_177956_o(); j <= blockpos$pooledmutableblockpos1.func_177956_o(); j++)
/*      */         {
/*  759 */           for (int k = blockpos$pooledmutableblockpos.func_177952_p(); 
/*  760 */               k <= blockpos$pooledmutableblockpos1.func_177952_p(); k++)
/*      */           {
/*  762 */             blockpos$pooledmutableblockpos2.func_181079_c(i, j, k);
/*  763 */             IBlockState iblockstate = this.field_70170_p.func_180495_p(blockpos$pooledmutableblockpos2);
/*      */             
/*      */             try
/*      */             {
/*  767 */               collideWithBlock(blockpos$pooledmutableblockpos2, iblockstate);
/*      */             }
/*      */             catch (Throwable throwable)
/*      */             {
/*  771 */               CrashReport crashreport = CrashReport.func_85055_a(throwable, "Colliding entity with block");
/*      */               
/*      */ 
/*  774 */               CrashReportCategory crashreportcategory = crashreport.func_85058_a("Block being collided with");
/*  775 */               CrashReportCategory.func_175750_a(crashreportcategory, blockpos$pooledmutableblockpos2, iblockstate);
/*      */               
/*  777 */               throw new ReportedException(crashreport);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  783 */     blockpos$pooledmutableblockpos.func_185344_t();
/*  784 */     blockpos$pooledmutableblockpos1.func_185344_t();
/*  785 */     blockpos$pooledmutableblockpos2.func_185344_t();
/*  786 */     super.func_145775_I();
/*      */   }
/*      */   
/*      */   public void collideWithBlock(BlockPos pos, IBlockState state)
/*      */   {
/*  791 */     boolean modStopFurtherCollision = false;
/*  792 */     for (Iterator localIterator = this.droneInfo.mods.iterator(); localIterator.hasNext(); 
/*      */         
/*  794 */         modStopFurtherCollision = true)
/*      */     {
/*  792 */       Module m = (Module)localIterator.next();
/*      */       
/*  794 */       if ((m == null) || (!m.collideWithBlock(this, pos, state))) {}
/*      */     }
/*  796 */     if (modStopFurtherCollision) {}
/*      */   }
/*      */   
/*      */   public double getBaseAttack()
/*      */   {
/*  801 */     return 1.0D;
/*      */   }
/*      */   
/*      */   public double getBaseHealth()
/*      */   {
/*  806 */     return 0.0D;
/*      */   }
/*      */   
/*      */ 
/*      */   public DamageSource getAttackDamageSource()
/*      */   {
/*  812 */     return this.controllingPlayer == null ? DamageSource.func_92087_a(this) : DamageSource.func_188403_a(this, this.controllingPlayer);
/*      */   }
/*      */   
/*      */   public boolean shouldPushDrone(EntityDrone e)
/*      */   {
/*  817 */     return e.getClass() != getClass();
/*      */   }
/*      */   
/*      */   public boolean shouldAttackDrone(EntityDrone e)
/*      */   {
/*  822 */     return e.getClass() != getClass();
/*      */   }
/*      */   
/*      */   public boolean canPickUpEntity(Entity e)
/*      */   {
/*  827 */     if ((e instanceof EntityDrone)) return false;
/*  828 */     if (((e instanceof EntityPlayer)) && (canPickUpPlayers())) return true;
/*  829 */     if ((canPickUpNonPlayers()) && (!(e instanceof EntityItem)) && (!(e instanceof EntityXPOrb)) && (!(e instanceof IProjectile)) && (e.field_70130_N * e.field_70131_O < 4 + this.droneInfo.engine))
/*      */     {
/*      */ 
/*  832 */       return true;
/*      */     }
/*  834 */     return false;
/*      */   }
/*      */   
/*      */   public boolean canPickUpPlayers()
/*      */   {
/*  839 */     return hasEnabled(Module.playerTransport);
/*      */   }
/*      */   
/*      */   public boolean canPickUpNonPlayers()
/*      */   {
/*  844 */     return hasEnabled(Module.nplayerTransport);
/*      */   }
/*      */   
/*      */ 
/*      */   public Entity func_184179_bs()
/*      */   {
/*  850 */     return func_184188_bt().isEmpty() ? null : (Entity)func_184188_bt().get(0);
/*      */   }
/*      */   
/*      */   public void setDroneAttackTarget(Entity e, boolean forcefully)
/*      */   {
/*  855 */     Entity oldTarget = getDroneAttackTarget();
/*  856 */     if ((forcefully) || (oldTarget == null) || (oldTarget.field_70128_L)) { this.entityToAttack = e;
/*      */ 
/*      */     }
/*  859 */     else if ((func_70068_e(e) < func_70068_e(oldTarget)) || (!func_70685_l(oldTarget))) {
/*  860 */       this.entityToAttack = e;
/*      */     }
/*      */   }
/*      */   
/*      */   public boolean func_70685_l(Entity e)
/*      */   {
/*  866 */     return this.field_70170_p.func_147447_a(new Vec3d(this.field_70165_t, this.field_70163_u + func_70047_e(), this.field_70161_v), new Vec3d(e.field_70165_t, e.field_70163_u + e
/*  867 */       .func_70047_e(), e.field_70161_v), false, true, false) == null;
/*      */   }
/*      */   
/*      */   public boolean canPosBeSeen(Vec3d pos)
/*      */   {
/*  872 */     return this.field_70170_p.func_147447_a(new Vec3d(this.field_70165_t, this.field_70163_u + func_70047_e(), this.field_70161_v), pos, false, true, false) == null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Entity getDroneAttackTarget()
/*      */   {
/*  878 */     return this.entityToAttack;
/*      */   }
/*      */   
/*      */   public void attackEntity(Entity e, DamageSource dmg, float dam)
/*      */   {
/*  883 */     if (this.attackDelay == 0)
/*      */     {
/*  885 */       e.func_70097_a(dmg, dam);
/*  886 */       this.attackDelay = (30 - this.droneInfo.core * 3);
/*  887 */       if (hasEnabled(Module.shooting)) this.attackDelay /= 2;
/*  888 */       this.droneInfo.reduceBattery(dam * this.droneInfo.getBatteryConsumptionRate(this));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean func_70097_a(DamageSource source, float amount)
/*      */   {
/*  895 */     double damage = 0.0D;
/*  896 */     if (((source == DamageSource.field_76372_a) || (source == DamageSource.field_76370_b) || (source == DamageSource.field_76371_c)) && 
/*  897 */       (hasEnabled(Module.heatPower)))
/*      */     {
/*  899 */       this.droneInfo.reduceBattery(-amount);
/*      */     }
/*      */     else
/*      */     {
/*  903 */       damage = amount;
/*      */     }
/*  905 */     if ((this.hitDelay <= 0) && (damage > 0.0D))
/*      */     {
/*  907 */       this.droneInfo.reduceDamage(this, damage * (1.0D - this.droneInfo.getDamageReduction(this)));
/*  908 */       func_70018_K();
/*  909 */       if (this.droneInfo.getDamage(false) <= 0.0D) func_70106_y();
/*  910 */       if (source.func_76346_g() != null)
/*      */       {
/*  912 */         func_70653_a(this, (float)(0.5D * Math.sqrt(damage)), source.func_76346_g().field_70165_t - this.field_70165_t, 
/*  913 */           source.func_76346_g().field_70161_v - this.field_70161_v);
/*      */       }
/*  915 */       func_184185_a(func_184601_bQ(), 0.5F, 1.0F + this.field_70146_Z.nextFloat());
/*      */     }
/*  917 */     return super.func_70097_a(source, amount);
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70106_y()
/*      */   {
/*  923 */     super.func_70106_y();
/*      */     
/*  925 */     for (int k = 0; k < 20; k++)
/*      */     {
/*  927 */       double d2 = this.field_70146_Z.nextGaussian() * 0.02D;
/*  928 */       double d0 = this.field_70146_Z.nextGaussian() * 0.02D;
/*  929 */       double d1 = this.field_70146_Z.nextGaussian() * 0.02D;
/*  930 */       this.field_70170_p.func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, this.field_70165_t + this.field_70146_Z
/*  931 */         .nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, this.field_70163_u + this.field_70146_Z
/*  932 */         .nextFloat() * this.field_70131_O, this.field_70161_v + this.field_70146_Z
/*  933 */         .nextFloat() * this.field_70130_N * 2.0F - this.field_70130_N, d2, d0, d1, new int[0]);
/*      */     }
/*      */     
/*  936 */     func_184185_a(func_184615_bR(), 0.7F, 1.0F + this.field_70146_Z.nextFloat());
/*      */   }
/*      */   
/*      */ 
/*      */   protected void func_70018_K()
/*      */   {
/*  942 */     super.func_70018_K();
/*  943 */     this.hitDelay = 20;
/*      */   }
/*      */   
/*      */   public void func_70653_a(Entity entityIn, float strenght, double xRatio, double zRatio)
/*      */   {
/*  948 */     this.field_70160_al = true;
/*  949 */     float f = MathHelper.func_76133_a(xRatio * xRatio + zRatio * zRatio);
/*  950 */     this.field_70159_w /= 2.0D;
/*  951 */     this.field_70179_y /= 2.0D;
/*  952 */     this.field_70159_w -= xRatio / f * strenght;
/*  953 */     this.field_70179_y -= zRatio / f * strenght;
/*      */     
/*  955 */     if (this.field_70122_E)
/*      */     {
/*  957 */       this.field_70181_x /= 2.0D;
/*      */       
/*  959 */       if (this.field_70181_x > 0.4000000059604645D)
/*      */       {
/*  961 */         this.field_70181_x = 0.4000000059604645D;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent func_184601_bQ()
/*      */   {
/*  969 */     return SoundEvents.field_187543_bD;
/*      */   }
/*      */   
/*      */   @Nullable
/*      */   protected SoundEvent func_184615_bR()
/*      */   {
/*  975 */     return SoundEvents.field_187661_by;
/*      */   }
/*      */   
/*      */   protected SoundEvent func_184588_d(int heightIn)
/*      */   {
/*  980 */     return heightIn > 4 ? SoundEvents.field_187655_bw : SoundEvents.field_187545_bE;
/*      */   }
/*      */   
/*      */   public void setDroneInfo(DroneInfo di)
/*      */   {
/*  985 */     this.droneInfo = di;
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70037_a(NBTTagCompound tagCompound)
/*      */   {
/*  991 */     if (this.droneInfo == null) this.droneInfo = new DroneInfo();
/*  992 */     this.droneInfo.readFromNBT(tagCompound);
/*  993 */     setFlyingMode(tagCompound.func_74762_e("Mode"));
/*  994 */     func_184212_Q().func_187227_b(CONTROLPLAYER, tagCompound.func_74779_i("ConPlayer"));
/*  995 */     if (tagCompound.func_74764_b("Drone Path"))
/*      */     {
/*  997 */       NBTTagList automatePath = tagCompound.func_150295_c("Drone Path", 10);
/*  998 */       if (automatePath.func_74745_c() > 0)
/*      */       {
/* 1000 */         this.automatedPath = new Path();
/* 1001 */         for (int a = 0; a < automatePath.func_74745_c(); a++)
/*      */         {
/* 1003 */           NBTTagCompound ntag = automatePath.func_150305_b(a);
/* 1004 */           this.automatedPath.addNode(new Node(ntag.func_74769_h("X"), ntag.func_74769_h("Y"), ntag.func_74769_h("Z")));
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void func_70014_b(NBTTagCompound tagCompound)
/*      */   {
/* 1013 */     if (this.droneInfo == null) this.droneInfo = new DroneInfo();
/* 1014 */     this.droneInfo.writeToNBT(tagCompound);
/* 1015 */     tagCompound.func_74768_a("Mode", getFlyingMode());
/* 1016 */     tagCompound.func_74778_a("ConPlayer", getControllingPlayer() == null ? "" : getControllingPlayer().func_70005_c_());
/* 1017 */     if (this.automatedPath != null)
/*      */     {
/* 1019 */       NBTTagList automatePath = new NBTTagList();
/* 1020 */       for (int a = 0; a < this.automatedPath.nodes.size(); a++)
/*      */       {
/* 1022 */         Vec3d n = ((Node)this.automatedPath.nodes.get(a)).toVec();
/* 1023 */         NBTTagCompound ntag = new NBTTagCompound();
/* 1024 */         ntag.func_74780_a("X", n.field_72450_a);
/* 1025 */         ntag.func_74780_a("Y", n.field_72448_b);
/* 1026 */         ntag.func_74780_a("Z", n.field_72449_c);
/* 1027 */         automatePath.func_74742_a(ntag);
/*      */       }
/* 1029 */       tagCompound.func_74782_a("Drone Path", automatePath);
/*      */     }
/*      */   }
/*      */   
/*      */   public static EntityDrone getDroneFromID(World world, int droneID)
/*      */   {
/* 1035 */     if ((world == null) || (droneID < 0)) return null;
/* 1036 */     List<EntityDrone> drones = getEntities(world, EntityDrone.class, EntitySelectors.field_94557_a);
/* 1037 */     for (int a = 0; a < drones.size(); a++)
/*      */     {
/* 1039 */       EntityDrone e = (EntityDrone)drones.get(a);
/* 1040 */       if (e.getDroneID() == droneID)
/*      */       {
/* 1042 */         return e;
/*      */       }
/*      */     }
/* 1045 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public static <T extends Entity> List<T> getEntities(World world, Class<? extends T> entityType, Predicate<? super T> filter)
/*      */   {
/* 1051 */     List<T> list = Lists.newArrayList();
/*      */     
/* 1053 */     for (int a = 0; a < world.field_72996_f.size(); a++)
/*      */     {
/* 1055 */       Entity entity = (Entity)world.field_72996_f.get(a);
/* 1056 */       if ((entity != null) && (entityType.isAssignableFrom(entity.getClass())) && ((filter == null) || 
/* 1057 */         (filter.apply(entity))))
/*      */       {
/* 1059 */         list.add(entity);
/*      */       }
/*      */     }
/*      */     
/* 1063 */     return list;
/*      */   }
/*      */   
/*      */ 
/*      */   public void writeSpawnData(ByteBuf buffer)
/*      */   {
/* 1069 */     NBTTagCompound di = new NBTTagCompound();
/* 1070 */     this.droneInfo.writeToNBT(di);
/* 1071 */     ByteBufUtils.writeTag(buffer, di);
/* 1072 */     buffer.writeInt(getFlyingMode());
/*      */   }
/*      */   
/*      */ 
/*      */   public void readSpawnData(ByteBuf additionalData)
/*      */   {
/* 1078 */     NBTTagCompound di = ByteBufUtils.readTag(additionalData);
/* 1079 */     this.droneInfo = DroneInfo.fromNBT(di);
/* 1080 */     setFlyingMode(additionalData.readInt());
/*      */   }
/*      */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityDrone.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */