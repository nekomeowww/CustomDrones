package com.github.nekomeowww.customdrones.drone.module;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;

public class ModuleCamera
        extends Module
{
    public static Entity prevRenderView;
    public static boolean prevHideGui;

    public ModuleCamera(int l, String s)
    {
        super(l, Module.ModuleType.Scout, s);
    }

    @SideOnly(Side.CLIENT)
    public Module.ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleCameraGui(gui, this);
    }

    @SideOnly(Side.CLIENT)
    public class ModuleCameraGui<T extends Module>
            extends Module.ModuleGui<T>
    {
        public ModuleCameraGui(GuiDroneStatus gui, T mod)
        {
            super(gui, mod);
        }

        public void initGui()
        {
            super.initGui();
            this.buttonList.add(new GuiButton(1, this.width / 2 - 40, this.height / 2 + 40, 80, 20, "Go to camera"));
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if (button.id == 1)
            {
                this.mc.displayGuiScreen(null);
                ModuleCamera.prevRenderView = this.mc.getRenderViewEntity();
                ModuleCamera.prevHideGui = this.mc.gameSettings.hideGUI;
                this.mc.setRenderViewEntity(this.parent.drone);
                this.mc.renderGlobal.setDisplayListEntitiesDirty();
                this.mc.entityRenderer.loadEntityShader((Entity)null);
                this.parent.drone.setCameraMode(true);
            }
        }
    }
}
