/*    */ package williamle.drones.api.helpers;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.item.Item;
/*    */ import net.minecraft.item.ItemBlock;
/*    */ import net.minecraftforge.fml.common.registry.EntityRegistry;
/*    */ import net.minecraftforge.fml.common.registry.GameRegistry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RegHelper
/*    */ {
/*    */   public static void registerBlock(Block block)
/*    */   {
/* 17 */     GameRegistry.register(block.setRegistryName("drones", block.func_149739_a().substring(5)));
/* 18 */     GameRegistry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public static void registerItem(Item item)
/*    */   {
/* 25 */     GameRegistry.register(item.setRegistryName("drones", item.func_77658_a().substring(5)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static void registerEntity(Class entityClass, String entityName, int id, Object mod, int range, int freq, boolean velo, int... egg)
/*    */   {
/* 33 */     EntityRegistry.registerModEntity(entityClass, entityName, id, mod, range, freq, velo);
/* 34 */     if (egg.length >= 2)
/*    */     {
/* 36 */       EntityRegistry.registerEgg(entityClass, egg[0], egg[1]);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\RegHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */