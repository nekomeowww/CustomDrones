/*     */ package williamle.drones.gui;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractMap.SimpleEntry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.FontRenderer;
/*     */ import net.minecraft.client.gui.GuiButton;
/*     */ import net.minecraft.client.gui.GuiTextField;
/*     */ import net.minecraft.client.gui.ScaledResolution;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.InventoryPlayer;
/*     */ import net.minecraft.entity.player.PlayerCapabilities;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.item.crafting.IRecipe;
/*     */ import net.minecraft.item.crafting.ShapedRecipes;
/*     */ import net.minecraft.item.crafting.ShapelessRecipes;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import williamle.drones.DronesMod;
/*     */ import williamle.drones.DronesMod.RecipeType;
/*     */ import williamle.drones.api.gui.GuiContainerPanel;
/*     */ import williamle.drones.api.gui.PI;
/*     */ import williamle.drones.api.gui.PIItemStack;
/*     */ import williamle.drones.api.gui.PIItemStackRequirement;
/*     */ import williamle.drones.api.gui.Panel;
/*     */ import williamle.drones.network.PacketDispatcher;
/*     */ 
/*     */ public class GuiCrafter extends GuiContainerPanel
/*     */ {
/*  35 */   public LinkedHashMap<ItemStack, LinkedList<ItemStack>> currentRecipes = new LinkedHashMap();
/*     */   public InventoryPlayer invp;
/*     */   public Panel panelTypes;
/*     */   public Panel panelOutputs;
/*     */   public Panel panelRecipes;
/*     */   public GuiTextField craftCount;
/*     */   
/*  42 */   public GuiCrafter(EntityPlayer p) { super(new ContainerCrafter(p.field_71071_by));
/*  43 */     this.invp = p.field_71071_by;
/*     */   }
/*     */   
/*     */ 
/*     */   public void func_73866_w_()
/*     */   {
/*  49 */     super.func_73866_w_();
/*  50 */     this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 77, this.field_146295_m / 2 - 10, 60, 20, "Craft"));
/*  51 */     this.craftCount = new GuiTextField(0, this.field_146289_q, this.field_146294_l / 2 + 78, this.field_146295_m / 2 - 35, 58, 20);
/*  52 */     this.craftCount.func_146195_b(true);
/*  53 */     this.craftCount.func_146203_f(6);
/*  54 */     this.craftCount.func_146180_a("0");
/*  55 */     this.panelTypes = new Panel(this, this.field_146294_l / 2 - 133, this.field_146295_m / 2 - 82, 50, 95);
/*  56 */     this.panelOutputs = new Panel(this, this.field_146294_l / 2 - 73, this.field_146295_m / 2 - 82, 70, 95);
/*  57 */     this.panelRecipes = new Panel(this, this.field_146294_l / 2 + 7, this.field_146295_m / 2 - 82, 60, 95);
/*  58 */     this.panels.add(this.panelTypes);
/*  59 */     this.panels.add(this.panelOutputs);
/*  60 */     this.panels.add(this.panelRecipes);
/*     */     
/*  62 */     DronesMod.RecipeType[] rts = DronesMod.RecipeType.values();
/*  63 */     for (DronesMod.RecipeType rt : rts)
/*     */     {
/*  65 */       if (rt != DronesMod.RecipeType.None)
/*     */       {
/*  67 */         PI pi = new PI(this.panelTypes);
/*  68 */         pi.yw = 20.0D;
/*  69 */         pi.displayString = rt.name();
/*  70 */         pi.fitHorizontal = true;
/*  71 */         this.panelTypes.addItem(pi);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void func_73864_a(int mouseX, int mouseY, int mouseButton)
/*     */     throws IOException
/*     */   {
/*  79 */     super.func_73864_a(mouseX, mouseY, mouseButton);
/*  80 */     this.craftCount.func_146192_a(mouseX, mouseY, mouseButton);
/*     */   }
/*     */   
/*     */   protected void func_73869_a(char typedChar, int keyCode)
/*     */     throws IOException
/*     */   {
/*  86 */     super.func_73869_a(typedChar, keyCode);
/*  87 */     if (this.craftCount.func_146206_l())
/*     */     {
/*  89 */       if ((Character.isDigit(typedChar)) || (keyCode == 14) || (keyCode == 211))
/*     */       {
/*  91 */         this.craftCount.func_146201_a(typedChar, keyCode);
/*     */       }
/*  93 */       int i = this.craftCount.func_146179_b().isEmpty() ? 0 : Integer.parseInt(this.craftCount.func_146179_b());
/*  94 */       this.craftCount.func_146180_a(Math.min(i, maxCanCraft()) + "");
/*     */     }
/*     */   }
/*     */   
/*     */   public int maxCanCraft()
/*     */   {
/* 100 */     int max = Integer.MAX_VALUE;
/* 101 */     if (this.panelRecipes.items.isEmpty()) max = 0;
/* 102 */     for (PI pi : this.panelRecipes.items)
/*     */     {
/* 104 */       if ((pi instanceof PIItemStackRequirement))
/*     */       {
/*     */ 
/* 107 */         int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
/* 108 */         max = Math.min(max, can);
/*     */       }
/*     */     }
/* 111 */     return max;
/*     */   }
/*     */   
/*     */   protected void func_146284_a(GuiButton button)
/*     */     throws IOException
/*     */   {
/* 117 */     super.func_146284_a(button);
/* 118 */     if (button.field_146127_k == 1)
/*     */     {
/* 120 */       craft();
/* 121 */       int max = Integer.MAX_VALUE;
/* 122 */       if (this.panelRecipes.items.isEmpty()) max = 0;
/* 123 */       for (PI pi : this.panelRecipes.items)
/*     */       {
/* 125 */         if ((pi instanceof PIItemStackRequirement))
/*     */         {
/*     */ 
/* 128 */           int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
/* 129 */           max = Math.min(max, can);
/*     */         }
/*     */       }
/* 132 */       int i = this.craftCount.func_146179_b().isEmpty() ? 0 : Integer.parseInt(this.craftCount.func_146179_b());
/* 133 */       this.craftCount.func_146180_a(Math.min(i, max) + "");
/*     */     }
/*     */   }
/*     */   
/*     */   public void craft()
/*     */   {
/* 139 */     int count = Integer.parseInt(this.craftCount.func_146179_b());
/* 140 */     PI selectedOutput = this.panelOutputs.getFirstSelectedItem();
/* 141 */     if ((count > 0) && (selectedOutput != null))
/*     */     {
/* 143 */       ItemStack comeout = (selectedOutput instanceof PIItemStack) ? ((PIItemStack)selectedOutput).is.func_77946_l() : null;
/* 144 */       if (comeout != null)
/*     */       {
/* 146 */         comeout.field_77994_a *= count;
/* 147 */         List<ItemStack> requirements = new ArrayList();
/* 148 */         for (Iterator localIterator = this.panelRecipes.items.iterator(); localIterator.hasNext();) { pi = (PI)localIterator.next();
/*     */           
/* 150 */           if ((pi instanceof PIItemStackRequirement))
/*     */           {
/* 152 */             ItemStack isr = ((PIItemStackRequirement)pi).is.func_77946_l();
/* 153 */             isr.field_77994_a = (((PIItemStackRequirement)pi).require * count);
/* 154 */             requirements.add(isr);
/*     */           } }
/*     */         PI pi;
/* 157 */         Object decreaseIndexes = new ArrayList();
/* 158 */         for (ItemStack decrease : requirements)
/*     */         {
/* 160 */           int decreaseLeft = decrease.field_77994_a;
/* 161 */           for (int a = 0; a < this.invp.func_70302_i_(); a++)
/*     */           {
/* 163 */             ItemStack invItemStack = this.invp.func_70301_a(a);
/* 164 */             if ((ItemStack.func_179545_c(invItemStack, decrease)) && 
/* 165 */               (ItemStack.func_77970_a(invItemStack, decrease)))
/*     */             {
/* 167 */               int canDecrease = invItemStack.field_77994_a;
/* 168 */               int hereDecrease = Math.min(decreaseLeft, canDecrease);
/* 169 */               decreaseLeft -= hereDecrease;
/* 170 */               ((List)decreaseIndexes)
/* 171 */                 .add(new AbstractMap.SimpleEntry(Byte.valueOf((byte)a), Byte.valueOf((byte)hereDecrease)));
/* 172 */               if (decreaseLeft <= 0) break;
/*     */             }
/*     */           }
/*     */         }
/* 176 */         if (!this.invp.field_70458_d.field_71075_bZ.field_75098_d)
/*     */         {
/* 178 */           for (Map.Entry<Byte, Byte> entry : (List)decreaseIndexes)
/*     */           {
/* 180 */             this.invp.func_70298_a(((Byte)entry.getKey()).byteValue(), ((Byte)entry.getValue()).byteValue());
/*     */           }
/*     */         }
/* 183 */         williamle.drones.api.helpers.EntityHelper.addItemStackToInv(this.invp, comeout.func_77946_l());
/* 184 */         PacketDispatcher.sendToServer(new williamle.drones.network.server.PacketCrafter(comeout, (List)decreaseIndexes));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/* 190 */   public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/crafter.png");
/*     */   
/*     */ 
/*     */   public void func_73863_a(int mouseX, int mouseY, float partialTicks)
/*     */   {
/* 195 */     ScaledResolution sr = new ScaledResolution(this.field_146297_k);
/* 196 */     int sclW = sr.func_78326_a();
/* 197 */     int sclH = sr.func_78328_b();
/* 198 */     this.field_146297_k.func_110434_K().func_110577_a(texture);
/* 199 */     func_146110_a(sclW / 2 - 150, sclH / 2 - 100, 0.0F, 0.0F, 300, 200, 300.0F, 200.0F);
/*     */     
/* 201 */     this.field_146289_q.func_78276_b("Category", sclW / 2 - 131, sclH / 2 - 93, 0);
/* 202 */     this.field_146289_q.func_78276_b("Craft", sclW / 2 - 53, sclH / 2 - 93, 0);
/* 203 */     this.field_146289_q.func_78276_b("Ingredients", sclW / 2 + 9, sclH / 2 - 93, 0);
/*     */     
/* 205 */     int max = Integer.MAX_VALUE;
/* 206 */     if (this.panelRecipes.items.isEmpty()) max = 0;
/* 207 */     for (PI pi : this.panelRecipes.items)
/*     */     {
/* 209 */       if ((pi instanceof PIItemStackRequirement))
/*     */       {
/*     */ 
/* 212 */         int can = (int)Math.floor(((PIItemStackRequirement)pi).has / ((PIItemStackRequirement)pi).require);
/* 213 */         max = Math.min(max, can);
/*     */       }
/*     */     }
/* 216 */     if (max >= 0)
/*     */     {
/* 218 */       this.field_146289_q.func_78276_b("Can craft", sclW / 2 + 83, sclH / 2 - 75, 16777215);
/* 219 */       String s1 = max + " time" + (max > 1 ? "s" : "");
/* 220 */       this.field_146289_q.func_78276_b(s1, sclW / 2 + 108 - this.field_146289_q.func_78256_a(s1) / 2, sclH / 2 - 62, 16777215);
/*     */     }
/*     */     
/* 223 */     this.craftCount.func_146194_f();
/* 224 */     super.func_73863_a(mouseX, mouseY, partialTicks);
/*     */   }
/*     */   
/*     */ 
/*     */   public void itemSelected(Panel panel, PI pi)
/*     */   {
/* 230 */     super.itemSelected(panel, pi);
/* 231 */     if (panel == this.panelTypes)
/*     */     {
/* 233 */       this.craftCount.func_146180_a("0");
/* 234 */       this.currentRecipes.clear();
/* 235 */       this.panelOutputs.items.clear();
/* 236 */       this.panelOutputs.scroll = 0;
/* 237 */       this.panelRecipes.items.clear();
/* 238 */       this.panelRecipes.scroll = 0;
/* 239 */       String type = pi.displayString;
/* 240 */       loadRecipesWithType(DronesMod.RecipeType.valueOf(type));
/* 241 */       addAllRecipes();
/*     */     }
/* 243 */     else if ((panel == this.panelOutputs) && ((pi instanceof PIItemStack)))
/*     */     {
/* 245 */       this.panelRecipes.items.clear();
/* 246 */       this.panelRecipes.scroll = 0;
/* 247 */       ItemStack is = ((PIItemStack)pi).is;
/* 248 */       LinkedList<ItemStack> recipesList = (LinkedList)this.currentRecipes.get(is);
/* 249 */       for (ItemStack recipeIS : recipesList)
/*     */       {
/* 251 */         PIItemStackRequirement piRecipe = new PIItemStackRequirement(this.panelRecipes, this.invp, recipeIS, recipeIS.field_77994_a);
/*     */         
/* 253 */         this.panelRecipes.addItem(piRecipe);
/*     */       }
/* 255 */       this.panelRecipes.updatePanel();
/* 256 */       this.craftCount.func_146180_a(Math.min(1, maxCanCraft()) + "");
/*     */     }
/*     */   }
/*     */   
/*     */   public void addAllRecipes()
/*     */   {
/* 262 */     for (ItemStack is : this.currentRecipes.keySet())
/*     */     {
/* 264 */       PIItemStack pii = new PIItemStack(this.panelOutputs, is);
/* 265 */       pii.yw = 24.0D;
/* 266 */       this.panelOutputs.addItem(pii);
/*     */     }
/*     */   }
/*     */   
/*     */   public void loadRecipesWithType(DronesMod.RecipeType type)
/*     */   {
/* 272 */     this.currentRecipes.clear();
/* 273 */     List<IRecipe> recipesList = (List)DronesMod.recipes.get(type);
/* 274 */     for (int a = 0; a < recipesList.size(); a++)
/*     */     {
/* 276 */       IRecipe ir = (IRecipe)recipesList.get(a);
/* 277 */       ItemStack is = ir.func_77571_b();
/* 278 */       LinkedList<ItemStack> requireStacks = new LinkedList();
/* 279 */       if ((ir instanceof ShapedRecipes))
/*     */       {
/* 281 */         ItemStack[] recipeISArray = ((ShapedRecipes)ir).field_77574_d;
/* 282 */         for (ItemStack recipeIS : recipeISArray)
/*     */         {
/* 284 */           if (recipeIS != null)
/*     */           {
/* 286 */             boolean toAdd = true;
/* 287 */             for (ItemStack addedIS : requireStacks)
/*     */             {
/* 289 */               if ((ItemStack.func_179545_c(addedIS, recipeIS)) && 
/* 290 */                 (ItemStack.func_77970_a(addedIS, recipeIS)))
/*     */               {
/* 292 */                 addedIS.field_77994_a += recipeIS.field_77994_a;
/* 293 */                 toAdd = false;
/* 294 */                 break;
/*     */               }
/*     */             }
/* 297 */             if (toAdd) requireStacks.add(recipeIS.func_77946_l());
/*     */           }
/*     */         }
/*     */       }
/* 301 */       else if ((ir instanceof ShapelessRecipes))
/*     */       {
/* 303 */         requireStacks.addAll(((ShapelessRecipes)ir).field_77579_b);
/*     */       }
/* 305 */       if (!requireStacks.isEmpty()) this.currentRecipes.put(is, requireStacks);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\gui\GuiCrafter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */