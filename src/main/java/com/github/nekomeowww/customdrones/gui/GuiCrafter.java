package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.api.gui.GuiContainerPanel;
import com.github.nekomeowww.customdrones.api.gui.PI;
import com.github.nekomeowww.customdrones.api.gui.PIItemStack;
import com.github.nekomeowww.customdrones.api.gui.PIItemStackRequirement;
import com.github.nekomeowww.customdrones.api.gui.Panel;
import com.github.nekomeowww.customdrones.api.helpers.EntityHelper;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketCrafter;

public class GuiCrafter
        extends GuiContainerPanel
{
    public LinkedHashMap<ItemStack, LinkedList<ItemStack>> currentRecipes = new LinkedHashMap<>();
    public InventoryPlayer invp;
    public Panel panelTypes;
    public Panel panelOutputs;
    public Panel panelRecipes;
    public GuiTextField craftCount;

    public GuiCrafter(EntityPlayer p)
    {
        super(new ContainerCrafter(p.inventory));
        this.invp = p.inventory;
    }

    public void initGui()
    {
        super.initGui();
        this.buttonList.add(new GuiButton(1, this.width / 2 + 77, this.height / 2 - 10, 60, 20, "Craft"));
        this.craftCount = new GuiTextField(0, this.fontRendererObj, this.width / 2 + 78, this.height / 2 - 35, 58, 20);
        this.craftCount.setFocused(true);
        this.craftCount.setMaxStringLength(6);
        this.craftCount.setText("0");
        this.panelTypes = new Panel(this, this.width / 2 - 133, this.height / 2 - 82, 50, 95);
        this.panelOutputs = new Panel(this, this.width / 2 - 73, this.height / 2 - 82, 70, 95);
        this.panelRecipes = new Panel(this, this.width / 2 + 7, this.height / 2 - 82, 60, 95);
        this.panels.add(this.panelTypes);
        this.panels.add(this.panelOutputs);
        this.panels.add(this.panelRecipes);

        CustomDrones.RecipeType[] rts = CustomDrones.RecipeType.values();
        for (CustomDrones.RecipeType rt : rts) {
            if (rt != CustomDrones.RecipeType.None)
            {
                PI pi = new PI(this.panelTypes);
                pi.yw = 20.0D;
                pi.displayString = rt.name();
                pi.fitHorizontal = true;
                this.panelTypes.addItem(pi);
            }
        }
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.craftCount.mouseClicked(mouseX, mouseY, mouseButton);
    }

    protected void keyTyped(char typedChar, int keyCode)
            throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        if (this.craftCount.isFocused())
        {
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211)) {
                this.craftCount.textboxKeyTyped(typedChar, keyCode);
            }
            int i = this.craftCount.getText().isEmpty() ? 0 : Integer.parseInt(this.craftCount.getText());
            this.craftCount.setText(Math.min(i, maxCanCraft()) + "");
        }
    }

    public int maxCanCraft()
    {
        int max = Integer.MAX_VALUE;
        if (this.panelRecipes.items.isEmpty()) {
            max = 0;
        }
        for (PI pi : this.panelRecipes.items) {
            if ((pi instanceof PIItemStackRequirement))
            {
                int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
                max = Math.min(max, can);
            }
        }
        return max;
    }

    protected void actionPerformed(GuiButton button)
            throws IOException
    {
        super.actionPerformed(button);
        if (button.id == 1)
        {
            craft();
            int max = Integer.MAX_VALUE;
            if (this.panelRecipes.items.isEmpty()) {
                max = 0;
            }
            for (PI pi : this.panelRecipes.items) {
                if ((pi instanceof PIItemStackRequirement))
                {
                    int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
                    max = Math.min(max, can);
                }
            }
            int i = this.craftCount.getText().isEmpty() ? 0 : Integer.parseInt(this.craftCount.getText());
            this.craftCount.setText(Math.min(i, max) + "");
        }
    }

    public void craft()
    {
        PI pi;
        int count = Integer.parseInt(this.craftCount.getText());
        PI selectedOutput = this.panelOutputs.getFirstSelectedItem();
        if ((count > 0) && (selectedOutput != null))
        {
            ItemStack comeout = (selectedOutput instanceof PIItemStack) ? ((PIItemStack)selectedOutput).is.copy() : null;
            if (comeout != null)
            {
                comeout.stackSize *= count;
                List<ItemStack> requirements = new ArrayList<>();
                for (Iterator<PI> localIterator = this.panelRecipes.items.iterator(); localIterator.hasNext();)
                {
                    pi = localIterator.next();
                    if ((pi instanceof PIItemStackRequirement))
                    {
                        ItemStack isr = ((PIItemStackRequirement)pi).is.copy();
                        isr.stackSize = (((PIItemStackRequirement)pi).require * count);
                        requirements.add(isr);
                    }
                }
                List<Entry<Byte, Byte>> decreaseIndexes = new ArrayList<Entry<Byte, Byte>>();
                for (ItemStack decrease : requirements)
                {
                    int decreaseLeft = decrease.stackSize;
                    for (int a = 0; a < this.invp.getSizeInventory(); a++)
                    {
                        ItemStack invItemStack = this.invp.getStackInSlot(a);
                        if ((ItemStack.areItemsEqual(invItemStack, decrease)) &&
                                (ItemStack.areItemStackTagsEqual(invItemStack, decrease)))
                        {
                            int canDecrease = invItemStack.stackSize;
                            int hereDecrease = Math.min(decreaseLeft, canDecrease);
                            decreaseLeft -= hereDecrease;
                            (decreaseIndexes)
                                    .add(new AbstractMap.SimpleEntry<>(Byte.valueOf((byte)a), Byte.valueOf((byte)hereDecrease)));
                            if (decreaseLeft <= 0) {
                            }
                        }
                    }
                }
                if (!this.invp.player.capabilities.isCreativeMode) {
                    for (Map.Entry<Byte, Byte> entry : decreaseIndexes) {
                        this.invp.decrStackSize(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
                    }
                }
                EntityHelper.addItemStackToInv(this.invp, comeout.copy());
                PacketDispatcher.sendToServer(new PacketCrafter(comeout, decreaseIndexes));
            }
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/crafter.png");

    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.mc);
        int sclW = sr.getScaledWidth();
        int sclH = sr.getScaledHeight();
        this.mc.getTextureManager().bindTexture(texture);
        drawModalRectWithCustomSizedTexture(sclW / 2 - 150, sclH / 2 - 100, 0.0F, 0.0F, 300, 200, 300.0F, 200.0F);

        this.fontRendererObj.drawString("Category", sclW / 2 - 131, sclH / 2 - 93, 0);
        this.fontRendererObj.drawString("Craft", sclW / 2 - 53, sclH / 2 - 93, 0);
        this.fontRendererObj.drawString("Ingredients", sclW / 2 + 9, sclH / 2 - 93, 0);

        int max = Integer.MAX_VALUE;
        if (this.panelRecipes.items.isEmpty()) {
            max = 0;
        }
        for (PI pi : this.panelRecipes.items) {
            if ((pi instanceof PIItemStackRequirement))
            {
                int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
                max = Math.min(max, can);
            }
        }
        if (max >= 0)
        {
            this.fontRendererObj.drawString("Can craft", sclW / 2 + 83, sclH / 2 - 75, 16777215);
            String s1 = max + " time" + (max > 1 ? "s" : "");
            this.fontRendererObj.drawString(s1, sclW / 2 + 108 - this.fontRendererObj.getStringWidth(s1) / 2, sclH / 2 - 62, 16777215);
        }
        this.craftCount.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void itemSelected(Panel panel, PI pi)
    {
        super.itemSelected(panel, pi);
        if (panel == this.panelTypes)
        {
            this.craftCount.setText("0");
            this.currentRecipes.clear();
            this.panelOutputs.items.clear();
            this.panelOutputs.scroll = 0;
            this.panelRecipes.items.clear();
            this.panelRecipes.scroll = 0;
            String type = pi.displayString;
            loadRecipesWithType(CustomDrones.RecipeType.valueOf(type));
            addAllRecipes();
        }
        else if ((panel == this.panelOutputs) && ((pi instanceof PIItemStack)))
        {
            this.panelRecipes.items.clear();
            this.panelRecipes.scroll = 0;
            ItemStack is = ((PIItemStack)pi).is;
            LinkedList<ItemStack> recipesList = (LinkedList<ItemStack>)this.currentRecipes.get(is);
            for (ItemStack recipeIS : recipesList)
            {
                PIItemStackRequirement piRecipe = new PIItemStackRequirement(this.panelRecipes, this.invp, recipeIS, recipeIS.stackSize);

                this.panelRecipes.addItem(piRecipe);
            }
            this.panelRecipes.updatePanel();
            this.craftCount.setText(Math.min(1, maxCanCraft()) + "");
        }
    }

    public void addAllRecipes()
    {
        for (ItemStack is : this.currentRecipes.keySet())
        {
            PIItemStack pii = new PIItemStack(this.panelOutputs, is);
            pii.yw = 24.0D;
            this.panelOutputs.addItem(pii);
        }
    }

    public void loadRecipesWithType(CustomDrones.RecipeType type)
    {
        this.currentRecipes.clear();
        List<IRecipe> recipesList = (List<IRecipe>)CustomDrones.recipes.get(type);
        for (int a = 0; a < recipesList.size(); a++)
        {
            IRecipe ir = (IRecipe)recipesList.get(a);
            ItemStack is = ir.getRecipeOutput();
            LinkedList<ItemStack> requireStacks = new LinkedList<>();
            if ((ir instanceof ShapedRecipes))
            {
                ItemStack[] recipeISArray = ((ShapedRecipes)ir).recipeItems;
                for (ItemStack recipeIS : recipeISArray) {
                    if (recipeIS != null)
                    {
                        boolean toAdd = true;
                        for (ItemStack addedIS : requireStacks) {
                            if ((ItemStack.areItemsEqual(addedIS, recipeIS)) &&
                                    (ItemStack.areItemStackTagsEqual(addedIS, recipeIS)))
                            {
                                addedIS.stackSize += recipeIS.stackSize;
                                toAdd = false;
                                break;
                            }
                        }
                        if (toAdd) {
                            requireStacks.add(recipeIS.copy());
                        }
                    }
                }
            }
            else if ((ir instanceof ShapelessRecipes))
            {
                requireStacks.addAll(((ShapelessRecipes)ir).recipeItems);
            }
            if (!requireStacks.isEmpty()) {
                this.currentRecipes.put(is, requireStacks);
            }
        }
    }
}
