package com.github.nekomeowww.customdrones.drone;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import net.minecraft.nbt.NBTTagCompound;
import com.github.nekomeowww.customdrones.api.model.Color;

public class DroneAppearance
{
    public static List<ColorPalette> presetPalettes = new ArrayList();

    public static void loadPresetPalettes()
    {
        presetPalettes.clear();
        presetPalettes.add(new ColorPalette().setName("Default"));
        presetPalettes.add(ColorPalette.fastMake("Sunset", Integer.valueOf(63744), Integer.valueOf(50433), Integer.valueOf(50433), Integer.valueOf(63744), Integer.valueOf(-4963827),
                Integer.valueOf(-5961206), Integer.valueOf(-5961206), Integer.valueOf(-5961206)));
        presetPalettes.add(ColorPalette.fastMake("Sea waves", Integer.valueOf(-1774371), Integer.valueOf(-9393742), Integer.valueOf(-14321761), Integer.valueOf(-9393742),
                Integer.valueOf(-14321761), Integer.valueOf(-15256231), Integer.valueOf(-15392463), Integer.valueOf(-15256231)));
        presetPalettes.add(ColorPalette.fastMake("Grass roots", Integer.valueOf(-15600764), Integer.valueOf(-2755681), Integer.valueOf(-13539243), Integer.valueOf(-15600764),
                Integer.valueOf(-2755681), Integer.valueOf(-13539243), Integer.valueOf(-11245519), Integer.valueOf(-11245519)));
        presetPalettes.add(ColorPalette.fastMake("Sandy desert", Integer.valueOf(-592950), Integer.valueOf(-136556), Integer.valueOf(-136017), Integer.valueOf(-592950),
                Integer.valueOf(-136017), Integer.valueOf(-136556), Integer.valueOf(-2764117), Integer.valueOf(-2764117)));
        presetPalettes.add(ColorPalette.fastMake("Ice glacier", Integer.valueOf(-1), Integer.valueOf(-5713184), Integer.valueOf(-4134145), Integer.valueOf(-1967108),
                Integer.valueOf(-5713184), Integer.valueOf(-4134145), Integer.valueOf(-9195050), Integer.valueOf(-5914156)));
        presetPalettes.add(ColorPalette.fastMake("Black & White", Integer.valueOf(-1), Integer.valueOf(-3355444), Integer.valueOf(-10066330), Integer.valueOf(-1),
                Integer.valueOf(-11184811), Integer.valueOf(-15592942), Integer.valueOf(-13355980), Integer.valueOf(-13421773)));
    }

    public static ColorPalette getPalette(String name)
    {
        for (int a = 0; a < presetPalettes.size(); a++) {
            try
            {
                ColorPalette palette = (ColorPalette)presetPalettes.get(a);
                if ((palette != null) && (name.equals(palette.paletteName))) {
                    return palette;
                }
            }
            catch (Exception localException) {}
        }
        return new ColorPalette();
    }

    public static void loadPaletteFile(File file)
    {
        List<String> strings = new ArrayList();
        String fileName = file.getName();
        strings.add(fileName.substring(0, fileName.indexOf(".")));
        try {
            BufferedReader buffer = new BufferedReader(new FileReader(file));
            String s;
            label47:
            //
            for (; (s = buffer.readLine()) != null; strings.add(s)) {
                if ((!s.contains("=")) || (!s.contains(","))) {
                    break label47;
                }
            }
            buffer.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if (strings.size() > 1)
        {
            ColorPalette palette = new ColorPalette();
            palette.readFromStrings(strings);
            boolean exist = false;
            for (ColorPalette cp : presetPalettes) {
                if (cp.paletteName.equals(palette.paletteName))
                {
                    exist = true;
                    cp = palette;
                }
            }
            if (!exist) {
                presetPalettes.add(palette);
            }
        }
    }

    public int modelID = 0;
    public ColorPalette palette = new ColorPalette();

    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("Model ID", this.modelID);

        NBTTagCompound paletteColorsTag = new NBTTagCompound();
        this.palette.writePalette(paletteColorsTag);
        tag.setTag("Palette", paletteColorsTag);
    }

    public void readFromNBT(NBTTagCompound tag)
    {
        this.modelID = tag.getInteger("Model ID");

        NBTTagCompound paletteColorsTag = tag.getCompoundTag("Palette");
        this.palette = new ColorPalette();
        this.palette.readPalette(paletteColorsTag);
    }

    public static DroneAppearance fromNBT(NBTTagCompound tag)
    {
        DroneAppearance da = new DroneAppearance();
        da.readFromNBT(tag);
        return da;
    }

    public static class ColorPalette
    {
        public Map<String, Color> paletteColors = new HashMap();
        public String paletteName;

        public ColorPalette setName(String s)
        {
            this.paletteName = s;
            return this;
        }

        public Color getPaletteColorFor(String s)
        {
            return (Color)this.paletteColors.get(s);
        }

        public void setPalette(ColorPalette palette)
        {
            palette.paletteName += "";
            this.paletteColors.clear();
            this.paletteColors.putAll(palette.paletteColors);
        }

        public void addPalette(ColorPalette palette)
        {
            this.paletteColors.putAll(palette.paletteColors);
        }

        public ColorPalette setPaletteColor(String s, Color c)
        {
            if (c == null) {
                this.paletteColors.remove(s);
            } else {
                this.paletteColors.put(s, c);
            }
            return this;
        }

        public ColorPalette removeColor(String s)
        {
            this.paletteColors.remove(s);
            return this;
        }

        public static ColorPalette fastMake(String name, Integer w, Integer e, Integer a, Integer c, Integer bt, Integer bm, Integer bb, Integer l)
        {
            return fastMake(name, w == null ? null : new Color(w.intValue()), e == null ? null : new Color(e.intValue()), a == null ? null : new Color(a
                    .intValue()), c == null ? null : new Color(c.intValue()), bt == null ? null : new Color(bt.intValue()), bm == null ? null : new Color(bm
                    .intValue()), bb == null ? null : new Color(bb.intValue()), l == null ? null : new Color(l
                    .intValue()));
        }

        public static ColorPalette fastMake(String name, Integer i)
        {
            return fastMake(name, i, i, i, i, i, i, i, i);
        }

        public static ColorPalette fastMake(String name, Color w, Color e, Color a, Color c, Color bt, Color bm, Color bb, Color l)
        {
            ColorPalette cp = new ColorPalette();
            cp.setName(name);
            if (w != null) {
                cp.setPaletteColor("Wing", w);
            }
            if (e != null) {
                cp.setPaletteColor("Engine", e);
            }
            if (a != null) {
                cp.setPaletteColor("Arm", a);
            }
            if (c != null) {
                cp.setPaletteColor("Core", c);
            }
            if (bt != null) {
                cp.setPaletteColor("Body top", bt);
            }
            if (bm != null) {
                cp.setPaletteColor("Body", bm);
            }
            if (bb != null) {
                cp.setPaletteColor("Body bottom", bb);
            }
            if (l != null) {
                cp.setPaletteColor("Leg", l);
            }
            return cp;
        }

        public static ColorPalette fastMake(String name, Color i)
        {
            return fastMake(name, Integer.valueOf(i.toInt()));
        }

        public void savePaletteFile(File file)
        {
            try
            {
                BufferedWriter buffer = new BufferedWriter(new FileWriter(file));
                List<String> strings = saveToStrings();
                for (int a = 1; a < strings.size(); a++)
                {
                    if (a > 1) {
                        buffer.newLine();
                    }
                    buffer.write((String)strings.get(a));
                }
                buffer.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void readFromStrings(List<String> strings)
        {
            if (strings.size() > 0)
            {
                this.paletteName = ((String)strings.get(0));
                for (int i = 1; i < strings.size(); i++)
                {
                    String line = (String)strings.get(i);
                    if (!line.isEmpty())
                    {
                        String name = line.split("=")[0];
                        String[] colorStrings = line.split("=")[1].split(",");
                        int r = colorStrings.length < 1 ? 255 : Integer.parseInt(colorStrings[0]);
                        int g = colorStrings.length < 2 ? 255 : Integer.parseInt(colorStrings[1]);
                        int b = colorStrings.length < 3 ? 255 : Integer.parseInt(colorStrings[2]);
                        int a = colorStrings.length < 4 ? 255 : Integer.parseInt(colorStrings[3]);
                        Color color = new Color(r, g, b, a);
                        this.paletteColors.put(name, color);
                    }
                }
            }
        }

        public List<String> saveToStrings()
        {
            List<String> list = new ArrayList();
            list.add(this.paletteName);
            for (Map.Entry<String, Color> entry : this.paletteColors.entrySet())
            {
                Color c = (Color)entry.getValue();
                String s = (String)entry.getKey() + "=" + c.redI() + "," + c.greenI() + "," + c.blueI() + "," + c.alphaI();
                list.add(s);
            }
            return list;
        }

        public void writePalette(NBTTagCompound tag)
        {
            tag.setString("Name", this.paletteName == null ? "$NAN$" : this.paletteName);
            NBTTagCompound values = new NBTTagCompound();
            for (Map.Entry<String, Color> entry : this.paletteColors.entrySet())
            {
                String s = (String)entry.getKey();
                Color c = (Color)entry.getValue();
                values.setLong(s, c.toLong());
            }
            tag.setTag("Palette", values);
        }

        public void readPalette(NBTTagCompound tag)
        {
            this.paletteName = tag.getString("Name");
            if (this.paletteName.equals("$NAN$")) {
                this.paletteName = null;
            }
            NBTTagCompound values = tag.getCompoundTag("Palette");
            Set<String> valuesString = values.getKeySet();
            for (String s : valuesString)
            {
                Color color = new Color(values.getLong(s));
                this.paletteColors.put(s, color);
            }
        }
    }
}
