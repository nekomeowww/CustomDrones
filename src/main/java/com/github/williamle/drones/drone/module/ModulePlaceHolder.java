/*    */ package williamle.drones.drone.module;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.text.TextFormatting;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ModulePlaceHolder
/*    */   extends Module
/*    */ {
/*    */   public ModulePlaceHolder(int l, String s)
/*    */   {
/* 17 */     super(l, Module.ModuleType.Misc, s);
/*    */   }
/*    */   
/*    */ 
/*    */   public List<String> getTooltip()
/*    */   {
/* 23 */     List<String> list = new ArrayList();
/* 24 */     TextFormatting color = TextFormatting.WHITE;
/* 25 */     switch (this.level)
/*    */     {
/*    */     case 2: 
/* 28 */       color = TextFormatting.YELLOW;
/* 29 */       break;
/*    */     case 3: 
/* 31 */       color = TextFormatting.AQUA;
/* 32 */       break;
/*    */     case 4: 
/* 34 */       color = TextFormatting.GREEN;
/*    */     }
/*    */     
/* 37 */     list.add(color + this.displayName);
/* 38 */     list.add("Rank " + color + this.level + TextFormatting.GRAY + " module base");
/* 39 */     additionalTooltip(list, false);
/* 40 */     return list;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\module\ModulePlaceHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */