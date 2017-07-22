/*    */ package williamle.drones.item;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import net.minecraft.util.EnumActionResult;
/*    */ import net.minecraft.util.EnumHand;
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ import net.minecraft.world.World;
/*    */ import williamle.drones.DronesMod;
/*    */ import williamle.drones.api.helpers.EntityHelper;
/*    */ import williamle.drones.drone.DroneInfo;
/*    */ import williamle.drones.entity.EntityDrone;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemDronePainter
/*    */   extends Item
/*    */   implements IItemInteractDrone
/*    */ {
/*    */   public ItemDronePainter()
/*    */   {
/* 28 */     func_77625_d(1);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*    */   {
/* 34 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/* 35 */     tooltip.add("Used to recolor drones");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
/*    */   {
/* 42 */     if (drone.playerHasCorrespondingController(player, false))
/*    */     {
/* 44 */       player.openGui(DronesMod.instance, 4, world, drone.droneInfo.id, 0, 0);
/* 45 */       return EnumActionResult.SUCCESS;
/*    */     }
/*    */     
/*    */ 
/* 49 */     String s = "Not your drone!";
/* 50 */     Random rnd = new Random();
/* 51 */     if (rnd.nextInt(15) == 0) s = "Don't vandalize other people's drone!";
/* 52 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that nigga!";
/* 53 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that asian!";
/* 54 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that white man!";
/* 55 */     EntityHelper.addChat(player, 1, TextFormatting.RED + s);
/* 56 */     return EnumActionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDronePainter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */