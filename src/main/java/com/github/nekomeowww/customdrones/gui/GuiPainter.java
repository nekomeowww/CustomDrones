package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.gui.ContainerNothing;
import com.github.nekomeowww.customdrones.api.gui.DrawHelper;
import com.github.nekomeowww.customdrones.api.gui.GuiButtonSelectColor;
import com.github.nekomeowww.customdrones.api.gui.GuiContainerPanel;
import com.github.nekomeowww.customdrones.api.gui.PI;
import com.github.nekomeowww.customdrones.api.gui.PIObjectColor;
import com.github.nekomeowww.customdrones.api.gui.Panel;
import com.github.nekomeowww.customdrones.api.model.CMBase;
import com.github.nekomeowww.customdrones.api.model.Color;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneAppearance.ColorPalette;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDronePaint;
import com.github.nekomeowww.customdrones.render.DroneModels;
import com.github.nekomeowww.customdrones.render.ModelDrone;

public class GuiPainter
        extends GuiContainerPanel
{
    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/painter.png");
    public EntityDrone drone;
    public Panel panelParts;
    public Panel panelPresetPalettes;
    public GuiButton buttonApply;
    public GuiButton buttonReset;
    public GuiButton buttonModel;
    public GuiButtonSelectColor buttonSelectRed;
    public GuiButtonSelectColor buttonSelectGreen;
    public GuiButtonSelectColor buttonSelectBlue;

    public GuiPainter(EntityDrone drone)
    {
        super(new ContainerNothing());
        this.drone = drone;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.add(this.buttonApply = new GuiButton(0, this.field_146294_l / 2 + 95, this.field_146295_m / 2 + 70, 70, 20, "Apply Color"));
        this.field_146292_n.add(this.buttonSelectRed = new GuiButtonSelectColor(1, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 61, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(1.0D, 0.0D, 0.0D)));

        this.field_146292_n.add(this.buttonSelectGreen = new GuiButtonSelectColor(2, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 41, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(0.0D, 1.0D, 0.0D)));

        this.field_146292_n.add(this.buttonSelectBlue = new GuiButtonSelectColor(3, this.field_146294_l / 2 + 5, this.field_146295_m / 2 - 21, 120, 10, new Color(0.0D, 0.0D, 0.0D), new Color(0.0D, 0.0D, 1.0D)));

        this.field_146292_n.add(this.buttonReset = new GuiButton(4, this.field_146294_l / 2 + 35, this.field_146295_m / 2 + 70, 55, 20, "Reset"));
        this.field_146292_n.add(new GuiButton(5, this.field_146294_l / 2 - 167, this.field_146295_m / 2 + 74, 75, 20, "Apply palette"));
        this.field_146292_n.add(this.buttonModel = new GuiButton(6, this.field_146294_l / 2 - 30, this.field_146295_m / 2 + 70, 60, 20, "View model"));

        this.panelParts = new Panel(this, this.field_146294_l / 2 - 102, this.field_146295_m / 2 - 56, 55, 126);
        ModelDrone model = DroneModels.instance.getModelOrDefault(this.drone);
        TreeSet<String> sortedNames = new TreeSet();
        searchPaletteIndexIn(model.models.values(), sortedNames);
        for (String s : sortedNames)
        {
            Color c = this.drone.droneInfo.appearance.palette.getPaletteColorFor(s);
            PIObjectColor pi = new PIObjectColor(this.panelParts, c);
            pi.defaultColor = ((DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(0)).getPaletteColorFor(s);
            pi.yw = 25.0D;
            pi.displayString = s;
            this.panelParts.addItem(pi);
        }
        this.panels.add(this.panelParts);

        this.panelPresetPalettes = new Panel(this, this.field_146294_l / 2 - 167, this.field_146295_m / 2 - 56, 55, 126);
        for (int a = 0; a < DroneAppearance.presetPalettes.size(); a++)
        {
            DroneAppearance.ColorPalette palette = (DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(a);
            PI pi = new PI(this.panelPresetPalettes);
            pi.displayString = palette.paletteName;
            pi.id = a;
            this.panelPresetPalettes.addItem(pi);
        }
        this.panels.add(this.panelPresetPalettes);
    }

    public void searchPaletteIndexIn(Collection<CMBase> cm0, TreeSet<String> sortedNames)
    {
        for (CMBase cm1 : cm0)
        {
            List<String> paletteIndexes = new ArrayList(cm1.getPaletteIndexes());
            if ((paletteIndexes != null) && (!sortedNames.containsAll(paletteIndexes)))
            {
                paletteIndexes.removeAll(sortedNames);
                sortedNames.addAll(paletteIndexes);
            }
            searchPaletteIndexIn(cm1.childModels, sortedNames);
        }
    }

    protected void func_146284_a(GuiButton button)
            throws IOException
    {
        super.func_146284_a(button);
        if (button.field_146127_k == 0) {
            if (this.panelParts.getFirstSelectedItem() != null)
            {
                PIObjectColor pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
                String toChange = pi.displayString;
                Color ored = this.buttonSelectRed.getOutputColor();
                Color ogreen = this.buttonSelectGreen.getOutputColor();
                Color oblue = this.buttonSelectBlue.getOutputColor();
                Color newColor = new Color(ored.red, ogreen.green, oblue.blue);
                pi.color = newColor;
                PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, toChange, newColor));
            }
        }
        PIObjectColor pi;
        if ((button.field_146127_k == 4) || (button.field_146127_k == 5)) {
            if (this.panelParts.getFirstSelectedItem() != null)
            {
                pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
                String toChange = pi.displayString;
                pi.color = pi.defaultColor;
                PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, toChange, null));
            }
        }
        if (button.field_146127_k == 5)
        {
            if (this.panelPresetPalettes.getFirstSelectedItem() != null) {
                for (PI part : this.panelParts.items)
                {
                    List<Map.Entry<String, Color>> entries = new ArrayList();
                    if ((part instanceof PIObjectColor))
                    {
                        String name = part.displayString;
                        Color color = ((PIObjectColor)part).color;
                        entries.add(new AbstractMap.SimpleEntry(name, color));
                    }
                    PacketDispatcher.sendToServer(new PacketDronePaint(this.drone, entries));
                }
            }
            this.panelPresetPalettes.unselectAll();
        }
        if (button.field_146127_k == 6) {
            this.panelParts.unselectAll();
        }
    }

    public void itemSelected(Panel panel, PI pi)
    {
        super.itemSelected(panel, pi);
        if ((panel == this.panelParts) && ((pi instanceof PIObjectColor)))
        {
            Color c = ((PIObjectColor)pi).color;
            if (c != null)
            {
                this.buttonSelectRed.selectedIndex = c.red;
                this.buttonSelectGreen.selectedIndex = c.green;
                this.buttonSelectBlue.selectedIndex = c.blue;
            }
            else
            {
                this.buttonSelectRed.selectedIndex = 0.0D;
                this.buttonSelectGreen.selectedIndex = 0.0D;
                this.buttonSelectBlue.selectedIndex = 0.0D;
            }
        }
        DroneAppearance.ColorPalette palette;
        if (panel == this.panelPresetPalettes)
        {
            int id = pi.id;
            palette = (DroneAppearance.ColorPalette)DroneAppearance.presetPalettes.get(id);
            for (PI part : this.panelParts.items) {
                if ((part instanceof PIObjectColor)) {
                    ((PIObjectColor)part).color = palette.getPaletteColorFor(part.displayString);
                }
            }
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 175, sclH / 2 - 100, 0.0F, 0.0F, 350, 200, 350.0F, 200.0F);

        func_73732_a(this.field_146289_q, "Recolor drone", sclW / 2 - 105, sclH / 2 - 93, 16777215);
        func_73732_a(this.field_146289_q, TextFormatting.BOLD + this.drone.droneInfo.getDisplayName(), sclW / 2 - 105, sclH / 2 - 83, 16777215);

        func_73732_a(this.field_146289_q, "Palettes", sclW / 2 - 140, sclH / 2 - 70, 16777215);
        func_73732_a(this.field_146289_q, "Parts", sclW / 2 - 75, sclH / 2 - 70, 16777215);
        if (this.panelParts.getFirstSelectedItem() != null)
        {
            PIObjectColor pi = (PIObjectColor)this.panelParts.getFirstSelectedItem();
            Color currentColor = pi.color;
            this.buttonModel.field_146125_m = (this.buttonReset.field_146125_m = this.buttonApply.field_146125_m = this.buttonSelectBlue.field_146125_m = this.buttonSelectGreen.field_146125_m = this.buttonSelectRed.field_146125_m = 1);
            func_73732_a(this.field_146289_q, TextFormatting.RESET + "Pick color for " + TextFormatting.BOLD + pi.displayString, sclW / 2 + 65, sclH / 2 - 90, 16777215);
            if (currentColor == null)
            {
                String s = TextFormatting.ITALIC + "(Using default)";
                this.field_146289_q.func_78276_b(s, sclW / 2 + 65 - this.field_146289_q.func_78256_a(s) / 2, sclH / 2 - 78, -5636096);
            }
            func_73732_a(this.field_146289_q, "Red", sclW / 2 - 15, sclH / 2 - 60, 16777215);
            func_73732_a(this.field_146289_q, "Green", sclW / 2 - 15, sclH / 2 - 40, 16777215);
            func_73732_a(this.field_146289_q, "Blue", sclW / 2 - 15, sclH / 2 - 20, 16777215);

            Color ored = this.buttonSelectRed.getOutputColor();
            Color ogreen = this.buttonSelectGreen.getOutputColor();
            Color oblue = this.buttonSelectBlue.getOutputColor();
            Color output = new Color(ored.red, ogreen.green, oblue.blue);
            func_73731_b(this.field_146289_q, "" + (int)Math.round(ored.red * 255.0D), sclW / 2 + 135, sclH / 2 - 60, 16777215);
            func_73731_b(this.field_146289_q, "" + (int)Math.round(ogreen.green * 255.0D), sclW / 2 + 135, sclH / 2 - 40, 16777215);

            func_73731_b(this.field_146289_q, "" + (int)Math.round(oblue.blue * 255.0D), sclW / 2 + 135, sclH / 2 - 20, 16777215);

            func_73732_a(this.field_146289_q, "Old", sclW / 2 + 20, sclH / 2 + 5, 16777215);
            func_73732_a(this.field_146289_q, "->", sclW / 2 + 60, sclH / 2 + 36, 16777215);
            func_73732_a(this.field_146289_q, "New", sclW / 2 + 100, sclH / 2 + 5, 16777215);

            int xo0 = sclW / 2 - 5;
            int xo1 = sclW / 2 + 45;
            int xn0 = sclW / 2 + 75;
            int xn1 = sclW / 2 + 125;
            int y0 = sclH / 2 + 15;
            int y1 = sclH / 2 + 65;
            Color cblack = new Color(0.0D, 0.0D, 0.0D);
            if (currentColor != null)
            {
                func_73734_a(xo0, y0, xo1, y1, (int)currentColor.toLong());
            }
            else
            {
                Color cred = new Color(1.0D, 0.0D, 0.0D);
                DrawHelper.drawLine(xo0 + 1, y0 + 1, xo1 - 1, y1 - 1, cred, 4.0F);
                DrawHelper.drawLine(xo1 - 1, y0 + 1, xo0 + 1, y1 - 1, cred, 4.0F);
            }
            DrawHelper.drawRectMargin(xo0, y0, xo1, y1, 1.0D, (int)cblack.toLong());
            DrawHelper.drawRect(xn0, y0, xn1, y1, (int)output.toLong());
            DrawHelper.drawRectMargin(xn0, y0, xn1, y1, 1.0D, (int)cblack.toLong());
        }
        else
        {
            this.buttonModel.field_146125_m = (this.buttonReset.field_146125_m = this.buttonApply.field_146125_m = this.buttonSelectBlue.field_146125_m = this.buttonSelectGreen.field_146125_m = this.buttonSelectRed.field_146125_m = 0);
            ModelDrone cm = DroneModels.instance.getModelOrDefault(this.drone);
            if (cm != null)
            {
                GL11.glPushMatrix();
                GL11.glTranslated(sclW / 2 + 65, sclH / 2 + 10, 100.0D);
                GL11.glScaled(100.0D, -100.0D, 100.0D);
                GL11.glRotated(15.0D, 1.0D, 0.0D, 0.0D);
                GL11.glRotated(System.currentTimeMillis() / 40L % 14400L, 0.0D, 1.0D, 0.0D);
                cm.doRender(this.drone, 0.0F, 0.0F, new Object[0]);
                GL11.glPopMatrix();
            }
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }
}
