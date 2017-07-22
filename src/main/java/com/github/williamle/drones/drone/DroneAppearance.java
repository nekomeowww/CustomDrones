/*     */ package williamle.drones.drone;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import williamle.drones.api.model.Color;
/*     */ 
/*     */ 
/*     */ public class DroneAppearance
/*     */ {
/*  20 */   public static List<ColorPalette> presetPalettes = new ArrayList();
/*     */   
/*     */   public static void loadPresetPalettes()
/*     */   {
/*  24 */     presetPalettes.clear();
/*  25 */     presetPalettes.add(new ColorPalette().setName("Default"));
/*  26 */     presetPalettes.add(ColorPalette.fastMake("Sunset", Integer.valueOf(63744), Integer.valueOf(50433), Integer.valueOf(50433), Integer.valueOf(63744), Integer.valueOf(-4963827), 
/*  27 */       Integer.valueOf(-5961206), Integer.valueOf(-5961206), Integer.valueOf(-5961206)));
/*  28 */     presetPalettes.add(ColorPalette.fastMake("Sea waves", Integer.valueOf(-1774371), Integer.valueOf(-9393742), Integer.valueOf(-14321761), Integer.valueOf(-9393742), 
/*  29 */       Integer.valueOf(-14321761), Integer.valueOf(-15256231), Integer.valueOf(-15392463), Integer.valueOf(-15256231)));
/*  30 */     presetPalettes.add(ColorPalette.fastMake("Grass roots", Integer.valueOf(-15600764), Integer.valueOf(-2755681), Integer.valueOf(-13539243), Integer.valueOf(-15600764), 
/*  31 */       Integer.valueOf(-2755681), Integer.valueOf(-13539243), Integer.valueOf(-11245519), Integer.valueOf(-11245519)));
/*  32 */     presetPalettes.add(ColorPalette.fastMake("Sandy desert", Integer.valueOf(-592950), Integer.valueOf(-136556), Integer.valueOf(-136017), Integer.valueOf(-592950), 
/*  33 */       Integer.valueOf(-136017), Integer.valueOf(-136556), Integer.valueOf(-2764117), Integer.valueOf(-2764117)));
/*  34 */     presetPalettes.add(ColorPalette.fastMake("Ice glacier", Integer.valueOf(-1), Integer.valueOf(-5713184), Integer.valueOf(-4134145), Integer.valueOf(-1967108), 
/*  35 */       Integer.valueOf(-5713184), Integer.valueOf(-4134145), Integer.valueOf(-9195050), Integer.valueOf(-5914156)));
/*  36 */     presetPalettes.add(ColorPalette.fastMake("Black & White", Integer.valueOf(-1), Integer.valueOf(-3355444), Integer.valueOf(-10066330), Integer.valueOf(-1), 
/*  37 */       Integer.valueOf(-11184811), Integer.valueOf(-15592942), Integer.valueOf(-13355980), Integer.valueOf(-13421773)));
/*     */   }
/*     */   
/*     */   public static ColorPalette getPalette(String name)
/*     */   {
/*  42 */     for (int a = 0; a < presetPalettes.size(); a++)
/*     */     {
/*     */       try
/*     */       {
/*  46 */         ColorPalette palette = (ColorPalette)presetPalettes.get(a);
/*  47 */         if ((palette != null) && (name.equals(palette.paletteName))) { return palette;
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*  53 */     return new ColorPalette();
/*     */   }
/*     */   
/*     */   public static void loadPaletteFile(File file)
/*     */   {
/*  58 */     List<String> strings = new ArrayList();
/*  59 */     String fileName = file.getName();
/*  60 */     strings.add(fileName.substring(0, fileName.indexOf(".")));
/*     */     try
/*     */     {
/*  63 */       BufferedReader buffer = new BufferedReader(new FileReader(file));
/*     */       label47:
/*  65 */       String s; for (; (s = buffer.readLine()) != null; 
/*     */           
/*  67 */           strings.add(s)) if ((!s.contains("=")) || (!s.contains(",")))
/*     */           break label47;
/*  69 */       buffer.close();
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*  73 */       e.printStackTrace();
/*     */     }
/*  75 */     if (strings.size() > 1)
/*     */     {
/*  77 */       ColorPalette palette = new ColorPalette();
/*  78 */       palette.readFromStrings(strings);
/*  79 */       boolean exist = false;
/*  80 */       for (ColorPalette cp : presetPalettes)
/*     */       {
/*  82 */         if (cp.paletteName.equals(palette.paletteName))
/*     */         {
/*  84 */           exist = true;
/*  85 */           cp = palette;
/*     */         }
/*     */       }
/*  88 */       if (!exist)
/*     */       {
/*  90 */         presetPalettes.add(palette);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*  95 */   public int modelID = 0;
/*  96 */   public ColorPalette palette = new ColorPalette();
/*     */   
/*     */   public void writeToNBT(NBTTagCompound tag)
/*     */   {
/* 100 */     tag.func_74768_a("Model ID", this.modelID);
/*     */     
/*     */ 
/* 103 */     NBTTagCompound paletteColorsTag = new NBTTagCompound();
/* 104 */     this.palette.writePalette(paletteColorsTag);
/* 105 */     tag.func_74782_a("Palette", paletteColorsTag);
/*     */   }
/*     */   
/*     */   public void readFromNBT(NBTTagCompound tag)
/*     */   {
/* 110 */     this.modelID = tag.func_74762_e("Model ID");
/*     */     
/*     */ 
/* 113 */     NBTTagCompound paletteColorsTag = tag.func_74775_l("Palette");
/* 114 */     this.palette = new ColorPalette();
/* 115 */     this.palette.readPalette(paletteColorsTag);
/*     */   }
/*     */   
/*     */   public static DroneAppearance fromNBT(NBTTagCompound tag)
/*     */   {
/* 120 */     DroneAppearance da = new DroneAppearance();
/* 121 */     da.readFromNBT(tag);
/* 122 */     return da;
/*     */   }
/*     */   
/*     */   public static class ColorPalette
/*     */   {
/* 127 */     public Map<String, Color> paletteColors = new HashMap();
/*     */     
/*     */ 
/*     */     public String paletteName;
/*     */     
/*     */ 
/*     */ 
/*     */     public ColorPalette setName(String s)
/*     */     {
/* 136 */       this.paletteName = s;
/* 137 */       return this;
/*     */     }
/*     */     
/*     */     public Color getPaletteColorFor(String s)
/*     */     {
/* 142 */       return (Color)this.paletteColors.get(s);
/*     */     }
/*     */     
/*     */     public void setPalette(ColorPalette palette)
/*     */     {
/* 147 */       palette.paletteName += "";
/* 148 */       this.paletteColors.clear();
/* 149 */       this.paletteColors.putAll(palette.paletteColors);
/*     */     }
/*     */     
/*     */     public void addPalette(ColorPalette palette)
/*     */     {
/* 154 */       this.paletteColors.putAll(palette.paletteColors);
/*     */     }
/*     */     
/*     */     public ColorPalette setPaletteColor(String s, Color c)
/*     */     {
/* 159 */       if (c == null)
/*     */       {
/* 161 */         this.paletteColors.remove(s);
/*     */       }
/*     */       else
/*     */       {
/* 165 */         this.paletteColors.put(s, c);
/*     */       }
/* 167 */       return this;
/*     */     }
/*     */     
/*     */     public ColorPalette removeColor(String s)
/*     */     {
/* 172 */       this.paletteColors.remove(s);
/* 173 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public static ColorPalette fastMake(String name, Integer w, Integer e, Integer a, Integer c, Integer bt, Integer bm, Integer bb, Integer l)
/*     */     {
/* 179 */       return fastMake(name, w == null ? null : new Color(w.intValue()), e == null ? null : new Color(e.intValue()), a == null ? null : new Color(a
/* 180 */         .intValue()), c == null ? null : new Color(c.intValue()), bt == null ? null : new Color(bt.intValue()), bm == null ? null : new Color(bm
/* 181 */         .intValue()), bb == null ? null : new Color(bb.intValue()), l == null ? null : new Color(l
/* 182 */         .intValue()));
/*     */     }
/*     */     
/*     */     public static ColorPalette fastMake(String name, Integer i)
/*     */     {
/* 187 */       return fastMake(name, i, i, i, i, i, i, i, i);
/*     */     }
/*     */     
/*     */ 
/*     */     public static ColorPalette fastMake(String name, Color w, Color e, Color a, Color c, Color bt, Color bm, Color bb, Color l)
/*     */     {
/* 193 */       ColorPalette cp = new ColorPalette();
/* 194 */       cp.setName(name);
/* 195 */       if (w != null) cp.setPaletteColor("Wing", w);
/* 196 */       if (e != null) cp.setPaletteColor("Engine", e);
/* 197 */       if (a != null) cp.setPaletteColor("Arm", a);
/* 198 */       if (c != null) cp.setPaletteColor("Core", c);
/* 199 */       if (bt != null) cp.setPaletteColor("Body top", bt);
/* 200 */       if (bm != null) cp.setPaletteColor("Body", bm);
/* 201 */       if (bb != null) cp.setPaletteColor("Body bottom", bb);
/* 202 */       if (l != null) cp.setPaletteColor("Leg", l);
/* 203 */       return cp;
/*     */     }
/*     */     
/*     */     public static ColorPalette fastMake(String name, Color i)
/*     */     {
/* 208 */       return fastMake(name, Integer.valueOf(i.toInt()));
/*     */     }
/*     */     
/*     */     public void savePaletteFile(File file)
/*     */     {
/*     */       try
/*     */       {
/* 215 */         BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
/* 216 */         List<String> strings = saveToStrings();
/* 217 */         for (int a = 1; a < strings.size(); a++)
/*     */         {
/* 219 */           if (a > 1) buffer.newLine();
/* 220 */           buffer.write((String)strings.get(a));
/*     */         }
/* 222 */         buffer.close();
/*     */       }
/*     */       catch (Exception e)
/*     */       {
/* 226 */         e.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/*     */     public void readFromStrings(List<String> strings)
/*     */     {
/* 232 */       if (strings.size() > 0)
/*     */       {
/* 234 */         this.paletteName = ((String)strings.get(0));
/* 235 */         for (int i = 1; i < strings.size(); i++)
/*     */         {
/* 237 */           String line = (String)strings.get(i);
/* 238 */           if (!line.isEmpty())
/*     */           {
/* 240 */             String name = line.split("=")[0];
/* 241 */             String[] colorStrings = line.split("=")[1].split(",");
/* 242 */             int r = colorStrings.length < 1 ? 255 : Integer.parseInt(colorStrings[0]);
/* 243 */             int g = colorStrings.length < 2 ? 255 : Integer.parseInt(colorStrings[1]);
/* 244 */             int b = colorStrings.length < 3 ? 255 : Integer.parseInt(colorStrings[2]);
/* 245 */             int a = colorStrings.length < 4 ? 255 : Integer.parseInt(colorStrings[3]);
/* 246 */             Color color = new Color(r, g, b, a);
/* 247 */             this.paletteColors.put(name, color);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public List<String> saveToStrings()
/*     */     {
/* 255 */       List<String> list = new ArrayList();
/* 256 */       list.add(this.paletteName);
/* 257 */       for (Map.Entry<String, Color> entry : this.paletteColors.entrySet())
/*     */       {
/* 259 */         Color c = (Color)entry.getValue();
/* 260 */         String s = (String)entry.getKey() + "=" + c.redI() + "," + c.greenI() + "," + c.blueI() + "," + c.alphaI();
/* 261 */         list.add(s);
/*     */       }
/* 263 */       return list;
/*     */     }
/*     */     
/*     */     public void writePalette(NBTTagCompound tag)
/*     */     {
/* 268 */       tag.func_74778_a("Name", this.paletteName == null ? "$NAN$" : this.paletteName);
/* 269 */       NBTTagCompound values = new NBTTagCompound();
/* 270 */       for (Map.Entry<String, Color> entry : this.paletteColors.entrySet())
/*     */       {
/* 272 */         String s = (String)entry.getKey();
/* 273 */         Color c = (Color)entry.getValue();
/* 274 */         values.func_74772_a(s, c.toLong());
/*     */       }
/* 276 */       tag.func_74782_a("Palette", values);
/*     */     }
/*     */     
/*     */     public void readPalette(NBTTagCompound tag)
/*     */     {
/* 281 */       this.paletteName = tag.func_74779_i("Name");
/* 282 */       if (this.paletteName.equals("$NAN$")) this.paletteName = null;
/* 283 */       NBTTagCompound values = tag.func_74775_l("Palette");
/* 284 */       Set<String> valuesString = values.func_150296_c();
/* 285 */       for (String s : valuesString)
/*     */       {
/* 287 */         Color color = new Color(values.func_74763_f(s));
/* 288 */         this.paletteColors.put(s, color);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\drone\DroneAppearance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */