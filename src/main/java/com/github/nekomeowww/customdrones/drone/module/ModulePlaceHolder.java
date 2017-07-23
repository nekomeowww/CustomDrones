package com.github.nekomeowww.customdrones.drone.module;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.text.TextFormatting;

public class ModulePlaceHolder
        extends Module
{
    public ModulePlaceHolder(int l, String s)
    {
        super(l, Module.ModuleType.Misc, s);
    }

    public List<String> getTooltip()
    {
        List<String> list = new ArrayList();
        TextFormatting color = TextFormatting.WHITE;
        switch (this.level)
        {
            case 2:
                color = TextFormatting.YELLOW;
                break;
            case 3:
                color = TextFormatting.AQUA;
                break;
            case 4:
                color = TextFormatting.GREEN;
        }
        list.add(color + this.displayName);
        list.add("Rank " + color + this.level + TextFormatting.GRAY + " module base");
        additionalTooltip(list, false);
        return list;
    }
}
