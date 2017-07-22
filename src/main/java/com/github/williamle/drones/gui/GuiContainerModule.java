/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import net.minecraft.client.renderer.GlStateManager;
/*     */ import net.minecraft.client.renderer.RenderHelper;
/*     */ import net.minecraft.inventory.Container;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import williamle.drones.api.gui.GuiContainerPanel;
/*     */ import williamle.drones.drone.module.Module;
/*     */ import williamle.drones.entity.EntityDrone;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ import williamle.drones.network.server.PacketDroneRequireUpdate;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GuiContainerModule
/*     */   extends GuiContainerPanel
/*     */ {
/*  21 */   public List<SlotModule> moduleSlots = new ArrayList();
/*     */   public EntityDrone drone;
/*     */   
/*     */   public GuiContainerModule(Container inventorySlotsIn, EntityDrone drone)
/*     */   {
/*  26 */     super(inventorySlotsIn);
/*  27 */     this.drone = drone;
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  33 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*  34 */     for (int a = 0; a < this.moduleSlots.size(); a++)
/*     */     {
/*  36 */       SlotModule slot = (SlotModule)this.moduleSlots.get(a);
/*  37 */       if (isMouseOverSlot(slot, mouseX, mouseY))
/*     */       {
/*  39 */         slot.slotClicked(mouseX, mouseY, mouseButton);
/*  40 */         modClicked(slot, mouseX, mouseY, mouseButton);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void modClicked(SlotModule slot, int mX, int mY, int mB) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void func_73876_c()
/*     */   {
/*  53 */     super.func_73876_c();
/*  54 */     if (this.drone.getWatchedDIChanged())
/*     */     {
/*  56 */       PacketDispatcher.sendToServer(new PacketDroneRequireUpdate(this.drone));
/*     */     }
/*  58 */     for (int a = 0; a < this.moduleSlots.size(); a++)
/*     */     {
/*  60 */       SlotModule slot = (SlotModule)this.moduleSlots.get(a);
/*  61 */       slot.updateSlot();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/*  68 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*  69 */     GlStateManager.func_179094_E();
/*  70 */     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
/*  71 */     GL11.glTranslated(this.field_147003_i, this.field_147009_r, 0.0D);
/*  72 */     GlStateManager.func_179091_B();
/*  73 */     for (int i1 = 0; i1 < this.moduleSlots.size(); i1++)
/*     */     {
/*  75 */       SlotModule slot = (SlotModule)this.moduleSlots.get(i1);
/*  76 */       slot.drawModule();
/*     */     }
/*  78 */     GlStateManager.func_179121_F();
/*  79 */     GlStateManager.func_179145_e();
/*  80 */     GlStateManager.func_179126_j();
/*  81 */     RenderHelper.func_74519_b();
/*  82 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*  83 */     for (int i1 = 0; i1 < this.moduleSlots.size(); i1++)
/*     */     {
/*  85 */       SlotModule slot = (SlotModule)this.moduleSlots.get(i1);
/*  86 */       if ((isMouseOverSlot(slot, mouseX, mouseY)) && (slot.module != null))
/*     */       {
/*  88 */         slot.hovering = true;
/*  89 */         renderToolTip(slot.module, mouseX, mouseY);
/*     */       } else {
/*  91 */         slot.hovering = false;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMouseOverSlot(SlotModule slotIn, int mouseX, int mouseY) {
/*  97 */     return func_146978_c(slotIn.posX, slotIn.posY, slotIn.sizeX, slotIn.sizeY, mouseX, mouseY);
/*     */   }
/*     */   
/*     */   protected void renderToolTip(Module mod, int x, int y)
/*     */   {
/* 102 */     drawHoveringText(mod.getTooltip(), x, y, this.field_146289_q);
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiContainerModule.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */