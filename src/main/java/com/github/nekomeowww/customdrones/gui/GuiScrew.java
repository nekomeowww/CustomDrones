package com.github.nekomeowww.customdrones.gui;

import java.io.IOException;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.gui.ContainerNothing;
import com.github.nekomeowww.customdrones.api.gui.DrawHelper;
import com.github.nekomeowww.customdrones.api.gui.GuiContainerPanel;
import com.github.nekomeowww.customdrones.api.gui.PI;
import com.github.nekomeowww.customdrones.api.gui.Panel;
import com.github.nekomeowww.customdrones.drone.DroneAppearance;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneScrew;
import com.github.nekomeowww.customdrones.render.DroneModels;
import com.github.nekomeowww.customdrones.render.DroneModels.ModelProp;
import com.github.nekomeowww.customdrones.render.ModelDrone;

public class GuiScrew
        extends GuiContainerPanel
{
    public static ResourceLocation texture = new ResourceLocation("drones", "textures/guis/screw.png");
    public EntityDrone drone;
    public Panel panelModels;
    public GuiButton buttonApply;
    public GuiButton buttonReset;

    public GuiScrew(EntityDrone drone)
    {
        super(new ContainerNothing());
        this.drone = drone;
    }

    public void func_73866_w_()
    {
        super.func_73866_w_();
        this.field_146292_n.add(this.buttonApply = new GuiButton(0, this.field_146294_l / 2 + 70, this.field_146295_m / 2 + 70, 70, 20, "Apply"));
        this.field_146292_n.add(this.buttonReset = new GuiButton(1, this.field_146294_l / 2 + 10, this.field_146295_m / 2 + 70, 55, 20, "Reset"));

        this.panelModels = new Panel(this, this.field_146294_l / 2 - 137, this.field_146295_m / 2 - 63, 60, 150);
        for (int a = 0; a < DroneModels.instance.models.size(); a++)
        {
            DroneModels.ModelProp mp = (DroneModels.ModelProp)DroneModels.instance.models.get(a);
            if (!mp.isMobModel)
            {
                PI pi = new PI(this.panelModels);
                pi.displayString = mp.model.name;
                pi.id = mp.id;
                this.panelModels.addItem(pi);
            }
        }
        this.panels.add(this.panelModels);
    }

    protected void func_146284_a(GuiButton button)
            throws IOException
    {
        super.func_146284_a(button);
        int newModel = -1;
        if (button.field_146127_k == 0)
        {
            if (this.panelModels.getFirstSelectedItem() != null) {
                newModel = this.panelModels.getFirstSelectedItem().id;
            }
        }
        else if (button.field_146127_k == 1) {
            newModel = 0;
        }
        if ((newModel != -1) && (newModel != this.drone.droneInfo.appearance.modelID))
        {
            this.drone.droneInfo.appearance.modelID = newModel;
            PacketDispatcher.sendToServer(new PacketDroneScrew(this.drone, newModel));
        }
    }

    public void func_73863_a(int mouseX, int mouseY, float partialTicks)
    {
        ScaledResolution sr = new ScaledResolution(this.field_146297_k);
        int sclW = sr.func_78326_a();
        int sclH = sr.func_78328_b();
        this.field_146297_k.func_110434_K().func_110577_a(texture);
        func_146110_a(sclW / 2 - 150, sclH / 2 - 100, 0.0F, 0.0F, 300, 200, 300.0F, 200.0F);

        func_73732_a(this.field_146289_q, TextFormatting.RESET + "Remodel drone " + TextFormatting.BOLD + this.drone.droneInfo
                .getDisplayName(), sclW / 2, sclH / 2 - 90, 16777215);

        func_73732_a(this.field_146289_q, "Models", sclW / 2 - 108, sclH / 2 - 75, 16777215);
        func_73732_a(this.field_146289_q, "Current", sclW / 2 - 30, sclH / 2 - 50, 16777215);
        func_73732_a(this.field_146289_q, "model", sclW / 2 - 30, sclH / 2 - 40, 16777215);
        func_73732_a(this.field_146289_q, "Selected", sclW / 2 - 30, sclH / 2 + 20, 16777215);
        func_73732_a(this.field_146289_q, "model", sclW / 2 - 30, sclH / 2 + 30, 16777215);
        DrawHelper.drawRect(sclW / 2 - 0, sclH / 2 - 70, sclW / 2 + 140, sclH / 2 - 10, -12237499L);
        DrawHelper.drawRect(sclW / 2 - 0, sclH / 2 - 0, sclW / 2 + 140, sclH / 2 + 60, -12237499L);

        ModelDrone cm = DroneModels.instance.getModelOrDefault(this.drone);
        if (cm != null) {
            renderDrone(cm, sclW / 2 + 70, sclH / 2 - 30);
        }
        if (this.panelModels.getFirstSelectedItem() != null)
        {
            ModelDrone cm1 = DroneModels.instance.getModelOrDefault(this.panelModels.getFirstSelectedItem().id);
            if (cm1 != null) {
                renderDrone(cm1, sclW / 2 + 70, sclH / 2 + 40);
            }
        }
        super.func_73863_a(mouseX, mouseY, partialTicks);
    }

    public void renderDrone(ModelDrone cm, double x, double y)
    {
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, 100.0D);
        GL11.glScaled(60.0D, -60.0D, 60.0D);
        GL11.glRotated(15.0D, 1.0D, 0.0D, 0.0D);
        GL11.glRotated(System.currentTimeMillis() / 40L % 14400L, 0.0D, 1.0D, 0.0D);
        cm.doRender(null, 0.0F, 1.0F, new Object[0]);
        GL11.glPopMatrix();
    }
}
