/*     */ package williamle.drones.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.ai.EntityAITasks;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.item.ItemBlock;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraftforge.fml.common.network.ByteBufUtils;
/*     */ import williamle.drones.drone.DroneInfo;
/*     */ import williamle.drones.drone.DroneWeightedLists;
/*     */ import williamle.drones.drone.DroneWeightedLists.WeightedItemStack;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.ai.DroneAIFlyToNearest;
/*     */ import williamle.drones.entity.ai.DroneAIWander;
/*     */ import williamle.drones.render.DroneModels;
/*     */ import williamle.drones.render.DroneModels.ModelProp;
/*     */ 
/*     */ public class EntityDroneWildItem extends EntityDroneMob
/*     */ {
/*     */   public ItemStack holding;
/*     */   
/*     */   public EntityDroneWildItem(World worldIn)
/*     */   {
/*  29 */     super(worldIn);
/*  30 */     func_70105_a(0.5F, 0.7F);
/*  31 */     this.modelScale = 0.5D;
/*     */   }
/*     */   
/*     */ 
/*     */   public void onInitSpawn()
/*     */   {
/*  37 */     while (this.holding == null)
/*     */     {
/*  39 */       DroneWeightedLists.WeightedItemStack wis = (DroneWeightedLists.WeightedItemStack)WeightedRandom.func_76271_a(this.field_70146_Z, DroneWeightedLists.wildHoldingList);
/*  40 */       if ((wis != null) && (wis.is != null)) {
/*  41 */         this.holding = wis.is.func_77946_l();
/*  42 */         if (((this.holding.func_77973_b() instanceof ItemBlock)) && (((ItemBlock)this.holding.func_77973_b()).field_150939_a == Blocks.field_150335_W))
/*     */         {
/*  44 */           this.droneTasks.func_75776_a(1, new DroneAIFlyToNearest(this, 16.0D, 0.75D, EntityPlayer.class)); }
/*     */       }
/*     */     }
/*  47 */     super.onInitSpawn();
/*     */   }
/*     */   
/*     */   public void initDroneInfo()
/*     */   {
/*  52 */     if ((this.droneInfo.casing == 1) && (this.droneInfo.chip == 1) && (this.droneInfo.core == 1) && (this.droneInfo.engine == 1))
/*     */     {
/*  54 */       randomizeDroneParts();
/*     */     }
/*  56 */     super.initDroneInfo();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initSpawnSetAppearance()
/*     */   {
/*  62 */     this.droneInfo.appearance.modelID = DroneModels.instance.wildItem.id;
/*  63 */     super.initSpawnSetAppearance();
/*     */   }
/*     */   
/*     */ 
/*     */   public void initSpawnAddModules()
/*     */   {
/*  69 */     this.droneInfo.mods.clear();
/*  70 */     this.droneInfo.applyModule(Module.getModule("Battery Saving " + DroneInfo.greekNumber[this.droneInfo.chip]));
/*  71 */     this.droneInfo.applyModule(Module.getModule("Armor " + DroneInfo.greekNumber[this.droneInfo.casing]));
/*  72 */     this.droneInfo.applyModule(Module.multiMovement);
/*     */   }
/*     */   
/*     */ 
/*     */   public void initDroneAI()
/*     */   {
/*  78 */     this.droneTasks.func_75776_a(10, new DroneAIWander(this, 16.0D, 0.5D));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void initDroneAIPostSpawn() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_70100_b_(EntityPlayer entityIn)
/*     */   {
/*  89 */     super.func_70100_b_(entityIn);
/*  90 */     if ((this.holding != null) && ((this.holding.func_77973_b() instanceof ItemBlock)) && 
/*  91 */       (((ItemBlock)this.holding.func_77973_b()).field_150939_a == Blocks.field_150335_W))
/*     */     {
/*  93 */       this.holding = null;
/*  94 */       this.field_70170_p.func_72876_a(this, this.field_70165_t, this.field_70163_u, this.field_70161_v, 0.5F * this.droneInfo.core, true);
/*  95 */       func_70106_y();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void updateWingRotation()
/*     */   {
/* 102 */     super.updateWingRotation();
/*     */   }
/*     */   
/*     */ 
/*     */   public void addDropsOnDeath(List<ItemStack> list)
/*     */   {
/* 108 */     if (this.holding != null) { list.add(this.holding);
/*     */     }
/*     */   }
/*     */   
/*     */   public int getXPOnDeath()
/*     */   {
/* 114 */     return 2;
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeSpawnData(ByteBuf buffer)
/*     */   {
/* 120 */     super.writeSpawnData(buffer);
/* 121 */     buffer.writeBoolean(this.holding != null);
/* 122 */     if (this.holding != null) { ByteBufUtils.writeItemStack(buffer, this.holding);
/*     */     }
/*     */   }
/*     */   
/*     */   public void readSpawnData(ByteBuf additionalData)
/*     */   {
/* 128 */     super.readSpawnData(additionalData);
/* 129 */     if (additionalData.readBoolean())
/*     */     {
/* 131 */       this.holding = ByteBufUtils.readItemStack(additionalData);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70014_b(NBTTagCompound tag)
/*     */   {
/* 138 */     super.func_70014_b(tag);
/* 139 */     tag.func_74757_a("Holding", this.holding != null);
/* 140 */     if (this.holding != null)
/*     */     {
/* 142 */       NBTTagCompound holdingTag = new NBTTagCompound();
/* 143 */       this.holding.func_77955_b(holdingTag);
/* 144 */       tag.func_74782_a("Holding tag", holdingTag);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_70037_a(NBTTagCompound tag)
/*     */   {
/* 151 */     super.func_70037_a(tag);
/* 152 */     if (tag.func_74767_n("Holding"))
/*     */     {
/* 154 */       NBTTagCompound holdingTag = tag.func_74775_l("Holding tag");
/* 155 */       this.holding = ItemStack.func_77949_a(holdingTag);
/*     */     }
/* 157 */     this.droneInfo.appearance.modelID = DroneModels.instance.wildItem.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\entity\EntityDroneWildItem.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */