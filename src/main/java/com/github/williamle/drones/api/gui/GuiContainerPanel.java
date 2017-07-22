/*     */ package williamle.drones.api.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.gui.inventory.GuiContainer;
/*     */ import net.minecraft.inventory.Container;
/*     */ 
/*     */ 
/*     */ public class GuiContainerPanel
/*     */   extends GuiContainer
/*     */ {
/*  13 */   public List<Panel> panels = new ArrayList();
/*     */   
/*     */   public GuiContainerPanel(Container inventorySlotsIn)
/*     */   {
/*  17 */     super(inventorySlotsIn);
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  23 */     super.func_73866_w_();
/*  24 */     this.panels.clear();
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  30 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*  31 */     for (int a = 0; a < this.panels.size(); a++)
/*     */     {
/*  33 */       Panel p = (Panel)this.panels.get(a);
/*  34 */       p.mouseClicked(mouseX, mouseY, mouseButton);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
/*     */   {
/*  41 */     super.func_146273_a(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*  42 */     for (int a = 0; a < this.panels.size(); a++)
/*     */     {
/*  44 */       Panel p = (Panel)this.panels.get(a);
/*  45 */       p.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  52 */     super.func_73869_a(typedChar, keyCode);
/*  53 */     for (int a = 0; a < this.panels.size(); a++)
/*     */     {
/*  55 */       Panel p = (Panel)this.panels.get(a);
/*  56 */       p.keyTyped(typedChar, keyCode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemSelected(Panel panel, PI pi) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemUnselected(Panel panel, PI pi) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemDisabled(Panel panel, PI pi) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void itemEnabled(Panel panel, PI pi) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void func_146276_q_() {}
/*     */   
/*     */ 
/*     */ 
/*     */   protected void func_146976_a(float partialTicks, int mouseX, int mouseY) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_73876_c()
/*     */   {
/*  93 */     super.func_73876_c();
/*  94 */     for (int a = 0; a < this.panels.size(); a++)
/*     */     {
/*  96 */       Panel p = (Panel)this.panels.get(a);
/*  97 */       p.updatePanel();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 104 */     for (int a = 0; a < this.panels.size(); a++)
/*     */     {
/* 106 */       Panel p = (Panel)this.panels.get(a);
/* 107 */       p.drawPanel();
/*     */     }
/* 109 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\GuiContainerPanel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */