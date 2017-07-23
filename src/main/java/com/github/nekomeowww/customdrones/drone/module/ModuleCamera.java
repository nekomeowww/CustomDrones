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
        public ModuleCameraGui(T gui)
        {
            super(mod);
        }

        public void func_73866_w_()
        {
            super.func_73866_w_();
            this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 - 40, this.field_146295_m / 2 + 40, 80, 20, "Go to camera"));
        }

        public void addDescText(List<String> l)
        {
            super.addDescText(l);
        }

        public void buttonClickedOnEnabledGui(GuiButton button)
        {
            super.buttonClickedOnEnabledGui(button);
            if (button.field_146127_k == 1)
            {
                this.field_146297_k.func_147108_a(null);
                ModuleCamera.prevRenderView = this.field_146297_k.func_175606_aa();
                ModuleCamera.prevHideGui = this.field_146297_k.field_71474_y.field_74319_N;
                this.field_146297_k.func_175607_a(this.parent.drone);
                this.field_146297_k.field_71438_f.func_174979_m();
                this.field_146297_k.field_71460_t.func_175066_a((Entity)null);
                this.parent.drone.setCameraMode(true);
            }
        }
    }
}
