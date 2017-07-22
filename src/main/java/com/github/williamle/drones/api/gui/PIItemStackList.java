/*    */ package williamle.drones.api.gui;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import net.minecraft.item.ItemStack;
/*    */ import org.lwjgl.opengl.GL11;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PIItemStackList
/*    */   extends PIItemStack
/*    */ {
/* 13 */   public LinkedList<ItemStack> stacks = new LinkedList();
/*    */   int stacksPerRow;
/*    */   int rows;
/*    */   
/*    */   public PIItemStackList(Panel p, LinkedList<ItemStack> items)
/*    */   {
/* 19 */     super(p, (ItemStack)items.getFirst());
/* 20 */     setStacks(items);
/*    */   }
/*    */   
/*    */   public void setStacks(LinkedList<ItemStack> items)
/*    */   {
/* 25 */     this.stacks.addAll(items);
/* 26 */     this.stacksPerRow = ((int)Math.floor((this.xw - this.margin * 2.0D - 6.0D) / 16.0D));
/* 27 */     this.rows = ((int)Math.ceil(this.stacks.size() / this.stacksPerRow));
/* 28 */     this.yw = (this.rows * 16 + this.margin * 2.0D + 6.0D);
/*    */   }
/*    */   
/*    */ 
/*    */   public void mouseOverItem(int mxlocal, int mylocal, boolean drawing)
/*    */   {
/* 34 */     super.mouseOverItem(mxlocal, mylocal, drawing);
/*    */   }
/*    */   
/*    */ 
/*    */   public void drawItemContent()
/*    */   {
/* 40 */     GL11.glPushMatrix();
/* 41 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/* 42 */     for (int ry = 0; ry < this.rows; ry++)
/*    */     {
/* 44 */       for (int rx = 0; rx < this.stacksPerRow; rx++)
/*    */       {
/* 46 */         int x = (int)(this.margin + 3.0D + rx * 16);
/* 47 */         int y = (int)(this.margin + 3.0D + ry * 16);
/* 48 */         int index = rx + ry * this.stacksPerRow;
/* 49 */         if (index < this.stacks.size())
/*    */         {
/* 51 */           ItemStack stack = (ItemStack)this.stacks.get(index);
/* 52 */           renderItem(stack, x, y);
/*    */         }
/*    */       }
/*    */     }
/* 56 */     GL11.glColor3d(1.0D, 1.0D, 1.0D);
/* 57 */     GL11.glPopMatrix();
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\gui\PIItemStackList.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */