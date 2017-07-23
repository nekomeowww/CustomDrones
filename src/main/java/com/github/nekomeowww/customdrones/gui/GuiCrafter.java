package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.ResourceLocation;
import com.github.nekomeowww.customdrones.CustomDrones;
import com.github.nekomeowww.customdrones.CustomDrones.RecipeType;
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
    public LinkedHashMap<ItemStack, LinkedList<ItemStack>> currentRecipes = new LinkedHashMap();
    public InventoryPlayer invp;
    public Panel panelTypes;
    public Panel panelOutputs;
    public Panel panelRecipes;
    public GuiTextField craftCount;

    public GuiCrafter(EntityPlayer p)
    {
        super(new ContainerCrafter(p.field_71071_by));
        this.invp = p.field_71071_by;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 77, this.field_146295_m / 2 - 10, 60, 20, "Craft"));
        this.craftCount = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 + 78, this.field_146295_m / 2 - 35, 58, 20);
        this.craftCount.func_146195_b(true);
        this.craftCount.func_146203_f(6);
        this.craftCount.func_146180_a("0");
        this.panelTypes = new Panel(this, this.field_146294_l / 2 - 133, this.field_146295_m / 2 - 82, 50, 95);
        this.panelOutputs = new Panel(this, this.field_146294_l / 2 - 73, this.field_146295_m / 2 - 82, 70, 95);
        this.panelRecipes = new Panel(this, this.field_146294_l / 2 + 7, this.field_146295_m / 2 - 82, 60, 95);
        this.panels.add(this.panelTypes);
        this.panels.add(this.panelOutputs);
        this.panels.add(this.panelRecipes);

        DronesMod.RecipeType[] rts = DronesMod.RecipeType.values();
        for (DronesMod.RecipeType rt : rts) {
            if (rt != DronesMod.RecipeType.None)
            {
                PI pi = new PI(this.panelTypes);
                pi.yw = 20.0D;
                pi.displayString = rt.name();
                pi.fitHorizontal = true;
                this.panelTypes.addItem(pi);
            }
        }
    }

    protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
            throws IOException
    {
        super.func_73864_a(mouseX, mouseY, mouseButton);
        this.craftCount.func_146192_a(mouseX, mouseY, mouseButton);
    }

    protected void func_73869_a(char typedChar, int keyCode)
            throws IOException
    {
        super.func_73869_a(typedChar, keyCode);
        if (this.craftCount.func_146206_l())
        {
            if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211)) {
                this.craftCount.func_146201_a(typedChar, keyCode);
            }
            int i = this.craftCount.func_146179_b().isEmpty() ? 0 : Integer.parseInt(this.craftCount.func_146179_b());
            this.craftCount.func_146180_a(Math.min(i, maxCanCraft()) + "");
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

    protected void func_146284_a(GuiButton button)
            throws IOException
    {
        super.func_146284_a(button);
        if (button.field_146127_k == 1)
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
            int i = this.craftCount.func_146179_b().isEmpty() ? 0 : Integer.parseInt(this.craftCount.func_146179_b());
            this.craftCount.func_146180_a(Math.min(i, max) + "");
        }
    }

    public void craft()
    {
        int count = Integer.parseInt(this.craftCount.func_146179_b());
        PI selectedOutput = this.panelOutputs.getFirstSelectedItem();
        if ((count > 0) && (selectedOutput != null))
        {
            ItemStack comeout = (selectedOutput instanceof PIItemStack) ? ((PIItemStack)selectedOutput).is.func_77946_l() : null;
            if (comeout != null)
            {
                comeout.field_77994_a *= count;
                List<ItemStack> requirements = new ArrayList();
                for (Iterator localIterator = this.panelRecipes.items.iterator(); localIterator.hasNext();)
                {
                    pi = (PI)localIterator.next();
                    if ((pi instanceof PIItemStackRequirement))
                    {
                        ItemStack isr = ((PIItemStackRequirement)pi).is.func_77946_l();
                        isr.field_77994_a = (((PIItemStackRequirement)pi).require * count);
                        requirements.add(isr);
                    }
                }
                PI pi;
                Object decreaseIndexes = new ArrayList();
                for (ItemStack decrease : requirements)
                {
                    int decreaseLeft = decrease.field_77994_a;
                    for (int a = 0; a < this.invp.func_70302_i_(); a++)
                    {
                        ItemStack invItemStack = this.invp.func_70301_a(a);
                        if ((ItemStack.func_179545_c(invItemStack, decrease)) &&
                                (ItemStack.func_77970_a(invItemStack, decrease)))
                        {
                            int canDecrease = invItemStack.field_77994_a;
                            int hereDecrease = Math.min(decreaseLeft, canDecrease);
                            decreaseLeft -= hereDecrease;
                            ((List)decreaseIndexes)
                                    .add(new AbstractMap.SimpleEntry(Byte.valueOf((byte)a), Byte.valueOf((byte)hereDecrease)));
                            if (decreaseLeft <= 0) {
                                break;
                            }
                        }
                    }
                }
                if (!this.invp.field_70458_d.field_71075_bZ.field_75098_d) {
                    for (Map.Entry<Byte, Byte> entry : (List)decreaseIndexes) {
                        this.invp.func_70298_a(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
                    }
                }
                EntityHelper.addItemStackToInv(this.invp, comeout.func_77946_l());
                PacketDispatcher.sendToServer(new PacketCrafter(comeout, (List)decreaseIndexes));
            }
        }
    }

    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/crafter.png");

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 150, sclH / 2 - 100, 0.0F, 0.0F, 300, 200, 300.0F, 200.0F);

        this.field_146289_q.func_78276_b("Category", sclW / 2 - 131, sclH / 2 - 93, 0);
        this.field_146289_q.func_78276_b("Craft", sclW / 2 - 53, sclH / 2 - 93, 0);
        this.field_146289_q.func_78276_b("Ingredients", sclW / 2 + 9, sclH / 2 - 93, 0);

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
            this.field_146289_q.func_78276_b("Can craft", sclW / 2 + 83, sclH / 2 - 75, 16777215);
            String s1 = max + " time" + (max > 1 ? "s" : "");
            this.field_146289_q.func_78276_b(s1, sclW / 2 + 108 - this.field_146289_q.func_78256_a(s1) / 2, sclH / 2 - 62, 16777215);
        }
        this.craftCount.func_146194_f();
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    public void itemSelected(Panel panel, PI pi)
    {
        super.itemSelected(panel, pi);
        if (panel == this.panelTypes)
        {
            this.craftCount.func_146180_a("0");
            this.currentRecipes.clear();
            this.panelOutputs.items.clear();
            this.panelOutputs.scroll = 0;
            this.panelRecipes.items.clear();
            this.panelRecipes.scroll = 0;
            String type = pi.displayString;
            loadRecipesWithType(DronesMod.RecipeType.valueOf(type));
            addAllRecipes();
        }
        else if ((panel == this.panelOutputs) && ((pi instanceof PIItemStack)))
        {
            this.panelRecipes.items.clear();
            this.panelRecipes.scroll = 0;
            ItemStack is = ((PIItemStack)pi).is;
            LinkedList<ItemStack> recipesList = (LinkedList)this.currentRecipes.get(is);
            for (ItemStack recipeIS : recipesList)
            {
                PIItemStackRequirement piRecipe = new PIItemStackRequirement(this.panelRecipes, this.invp, recipeIS, recipeIS.field_77994_a);

                this.panelRecipes.addItem(piRecipe);
            }
            this.panelRecipes.updatePanel();
            this.craftCount.func_146180_a(Math.min(1, maxCanCraft()) + "");
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

    public void loadRecipesWithType(DronesMod.RecipeType type)
    {
        this.currentRecipes.clear();
        List<IRecipe> recipesList = (List)DronesMod.recipes.get(type);
        for (int a = 0; a < recipesList.size(); a++)
        {
            IRecipe ir = (IRecipe)recipesList.get(a);
            ItemStack is = ir.func_77571_b();
            LinkedList<ItemStack> requireStacks = new LinkedList();
            if ((ir instanceof ShapedRecipes))
            {
                ItemStack[] recipeISArray = ((ShapedRecipes)ir).field_77574_d;
                for (ItemStack recipeIS : recipeISArray) {
                    if (recipeIS != null)
                    {
                        boolean toAdd = true;
                        for (ItemStack addedIS : requireStacks) {
                            if ((ItemStack.func_179545_c(addedIS, recipeIS)) &&
                                    (ItemStack.func_77970_a(addedIS, recipeIS)))
                            {
                                addedIS.field_77994_a += recipeIS.field_77994_a;
                                toAdd = false;
                                break;
                            }
                        }
                        if (toAdd) {
                            requireStacks.add(recipeIS.func_77946_l());
                        }
                    }
                }
            }
            else if ((ir instanceof ShapelessRecipes))
            {
                requireStacks.addAll(((ShapelessRecipes)ir).field_77579_b);
            }
            if (!requireStacks.isEmpty()) {
                this.currentRecipes.put(is, requireStacks);
            }
        }
    }
}
