package com.github.nekomeowww.customdrones;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionCategoryElement;
import net.minecraftforge.fml.client.IModGuiFactory.RuntimeOptionGuiHandler;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class GuiFactory
        implements IModGuiFactory
{
    public void initialize(Minecraft minecraftInstance) {}

    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return ConfigGui.class;
    }

    public Set<IModGuiFactory.RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    public IModGuiFactory.RuntimeOptionGuiHandler getHandlerFor(IModGuiFactory.RuntimeOptionCategoryElement element)
    {
        return null;
    }

    public static class ConfigGui
            extends GuiConfig
    {
        public ConfigGui(GuiScreen parentScreen)
        {
            super(parentScreen, getConfigElements(parentScreen), "customdrones", ConfigControl.CONFIGID, false, false, "CustomDrones configuration");
        }

        private static List<IConfigElement> getConfigElements(GuiScreen parent)
        {
            List<IConfigElement> list = new ArrayList();

            Set<String> categories = CustomDrones.configControl.config.getCategoryNames();
            for (String s : categories) {
                list.add(new ConfigElement(CustomDrones.configControl.config.getCategory(s)));
            }
            return list;
        }
    }
}
