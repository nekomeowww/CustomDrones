/*    */ package williamle.drones.api.helpers;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.renderer.ItemModelMesher;
/*    */ import net.minecraft.client.renderer.RenderItem;
/*    */ import net.minecraft.client.renderer.block.model.ModelResourceLocation;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.item.Item;
/*    */ import williamle.drones.render.RenderMaker;
/*    */ 
/*    */ public class RegHelperClient
/*    */ {
/*    */   public static void registerItemRenderer(Item item)
/*    */   {
/* 16 */     Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(item, 0, new ModelResourceLocation("drones:" + item
/* 17 */       .func_77658_a().substring(5), "inventory"));
/*    */   }
/*    */   
/*    */   public static void registerBlockRenderer(Block block)
/*    */   {
/* 22 */     Item item = Item.func_150898_a(block);
/* 23 */     Minecraft.func_71410_x().func_175599_af().func_175037_a().func_178086_a(item, 0, new ModelResourceLocation("drones:" + item
/* 24 */       .func_77658_a().substring(5), "inventory"));
/*    */   }
/*    */   
/*    */   public static void registerEntityRenderer(Class<? extends Entity> entityClass)
/*    */   {
/* 29 */     net.minecraftforge.fml.client.registry.RenderingRegistry.registerEntityRenderingHandler(entityClass, new RenderMaker(entityClass));
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\helpers\RegHelperClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */