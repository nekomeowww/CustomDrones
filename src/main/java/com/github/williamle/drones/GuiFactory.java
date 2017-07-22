/*    */ package williamle.drones;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.GuiScreen;
/*    */ import net.minecraftforge.common.config.ConfigElement;
/*    */ import net.minecraftforge.common.config.Configuration;
/*    */ import net.minecraftforge.fml.client.IModGuiFactory;
/*    */ import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
/*    */ import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
/*    */ import net.minecraftforge.fml.client.config.GuiConfig;
/*    */ import net.minecraftforge.fml.client.config.IConfigElement;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GuiFactory
/*    */   implements IModGuiFactory
/*    */ {
/*    */   public void initialize(Minecraft minecraftInstance) {}
/*    */   
/*    */   public Class<? extends GuiScreen> mainConfigGuiClass()
/*    */   {
/* 26 */     return ConfigGui.class;
/*    */   }
/*    */   
/*    */ 
/*    */   public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
/*    */   {
/* 32 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element)
/*    */   {
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public static class ConfigGui
/*    */     extends GuiConfig
/*    */   {
/*    */     public ConfigGui(GuiScreen parentScreen)
/*    */     {
/* 46 */       super(getConfigElements(parentScreen), "drones", ConfigControl.CONFIGID, false, false, "CustomDrones configuration");
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */     private static List<IConfigElement> getConfigElements(GuiScreen parent)
/*    */     {
/* 53 */       List<IConfigElement> list = new ArrayList();
/*    */       
/* 55 */       Set<String> categories = DronesMod.configControl.config.getCategoryNames();
/*    */       
/* 57 */       for (String s : categories)
/*    */       {
/* 59 */         list.add(new ConfigElement(DronesMod.configControl.config.getCategory(s)));
/*    */       }
/* 61 */       return list;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\GuiFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */