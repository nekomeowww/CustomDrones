package com.github.nekomeowww.customdrones.api.gui;

import java.util.LinkedList;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public class PIItemStackList
        extends PIItemStack
{
    public LinkedList<ItemStack> stacks = new LinkedList();
    int stacksPerRow;
    int rows;

    public PIItemStackList(Panel p, LinkedList<ItemStack> items)
    {
        super(p, (ItemStack)items.getFirst());
        setStacks(items);
    }

    public void setStacks(LinkedList<ItemStack> items)
    {
        this.stacks.addAll(items);
        this.stacksPerRow = ((int)Math.floor((this.xw - this.margin * 2.0D - 6.0D) / 16.0D));
        this.rows = ((int)Math.ceil(this.stacks.size() / this.stacksPerRow));
        this.yw = (this.rows * 16 + this.margin * 2.0D + 6.0D);
    }

    public void mouseOverItem(int mxlocal, int mylocal, boolean drawing)
    {
        super.mouseOverItem(mxlocal, mylocal, drawing);
    }

    public void drawItemContent()
    {
        GL11.glPushMatrix();
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        for (int ry = 0; ry < this.rows; ry++) {
            for (int rx = 0; rx < this.stacksPerRow; rx++)
            {
                int x = (int)(this.margin + 3.0D + rx * 16);
                int y = (int)(this.margin + 3.0D + ry * 16);
                int index = rx + ry * this.stacksPerRow;
                if (index < this.stacks.size())
                {
                    ItemStack stack = (ItemStack)this.stacks.get(index);
                    renderItem(stack, x, y);
                }
            }
        }
        GL11.glColor3d(1.0D, 1.0D, 1.0D);
        GL11.glPopMatrix();
    }
}
