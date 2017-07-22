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
/*    */ public class ItemDroneScrew
/*    */   extends Item
/*    */   implements IItemInteractDrone
/*    */ {
/*    */   public ItemDroneScrew()
/*    */   {
/* 27 */     func_77625_d(1);
/*    */   }
/*    */   
/*    */ 
/*    */   public void func_77624_a(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced)
/*    */   {
/* 33 */     super.func_77624_a(stack, playerIn, tooltip, advanced);
/* 34 */     tooltip.add("Used to change drone model");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EnumActionResult interactWithDrone(World world, EntityDrone drone, EntityPlayer player, Vec3d vec, ItemStack is, EnumHand hand)
/*    */   {
/* 41 */     if (drone.playerHasCorrespondingController(player, false))
/*    */     {
/* 43 */       player.openGui(DronesMod.instance, 5, world, drone.droneInfo.id, 0, 0);
/* 44 */       return EnumActionResult.SUCCESS;
/*    */     }
/*    */     
/*    */ 
/* 48 */     String s = "Not your drone!";
/* 49 */     Random rnd = new Random();
/* 50 */     if (rnd.nextInt(15) == 0) s = "Don't screw other people's drone!";
/* 51 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that nigga!";
/* 52 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that asian!";
/* 53 */     if (rnd.nextInt(150) == 0) s = "Ya can't do that white man!";
/* 54 */     EntityHelper.addChat(player, 1, TextFormatting.RED + s);
/* 55 */     return EnumActionResult.FAIL;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\item\ItemDroneScrew.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */