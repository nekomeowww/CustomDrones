package com.github.nekomeowww.customdrones.drone.module;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.drone.DroneInfo;
import com.github.nekomeowww.customdrones.entity.EntityDrone;
import com.github.nekomeowww.customdrones.gui.GuiDroneStatus;
import com.github.nekomeowww.customdrones.gui.SlotModule;
import com.github.nekomeowww.customdrones.network.PacketDispatcher;
import com.github.nekomeowww.customdrones.network.server.PacketDroneSwitchMod;
import com.github.nekomeowww.customdrones.network.server.PacketDroneUninstallMod;

public class Module
{
    public static int texturesPerRow = 12;
    public static final List<Module> modules = new ArrayList();
    public static final Module useless1 = new ModulePlaceHolder(1, "Place holder 1").setID("usl1");
    public static final Module useless2 = new ModulePlaceHolder(2, "Place holder 2").setID("usl2");
    public static final Module useless3 = new ModulePlaceHolder(3, "Place holder 3").setID("usl3");
    public static final Module useless4 = new ModulePlaceHolder(4, "Place holder 4").setID("usl4");
    public static final Module itemInventory = new ModuleTransport(1, "Items Inventory").setTexture("itemInventory")
            .setID("tra1");
    public static final Module nplayerTransport = new ModuleTransport(2, "Non-player Transporting")
            .setTexture("nplayerTransport").setID("tra2");
    public static final Module playerTransport = new ModuleTransport(3, "Player Transporting")
            .setTexture("playerTransport").setID("tra3");
    public static final Module multiTransport = new ModuleTransport(4, "Multi Transporting").setID("tra4")
            .setUndergrade(new Module[] { itemInventory, nplayerTransport, playerTransport }).setTexture("multiTransport");
    public static final Module itemCollect = new ModuleCollect(1, "Items Collecting").setTexture("itemCollect")
            .setID("col1");
    public static final Module xpCollect = new ModuleCollect(2, "XP Collecting").setTexture("xpCollect").setID("col2");
    public static final Module multiCollect = new ModuleCollect(3, "Multi Collecting").setID("col3")
            .setUndergrade(new Module[] { itemCollect, xpCollect }).setTexture("multiCollect");
    public static final Module chestDeposit = new ModuleChestDeposit(2, "Chest Depositing").setTexture("chestDeposit")
            .setID("dep1");
    public static final Module mobScan1 = new ModuleScan(1, "Mobs Scanning I").setTexture("mobScan1").setID("sca1");
    public static final Module mobScan2 = new ModuleScan(2, "Mobs Scanning II").setUndergrade(new Module[] { mobScan1 }).setID("sca2")
            .setTexture("mobScan2");
    public static final Module oreScan = new ModuleScan(1, "Ore Scanning").setTexture("oreScan").setID("sca3");
    public static final Module multiScan = new ModuleScan(2, "Multi Scanning").setUndergrade(new Module[] { mobScan2, oreScan })
            .setTexture("multiScan").setID("sca4");
    public static final Module controlMovement = new ModuleMovement(1, "Manual Control Movement")
            .setTexture("controlMovement").setID("mov1");
    public static final Module pathMovement = new ModuleMovement(1, "Preset Path Movement").setTexture("pathMovement")
            .setID("mov2");
    public static final Module followMovement = new ModuleMovement(1, "Follow Movement").setTexture("followMovement")
            .setID("mov3");
    public static final Module multiMovement = new ModuleMovement(2, "Multi Movement").setID("mov4")
            .setUndergrade(new Module[] { pathMovement, followMovement, controlMovement }).setTexture("multiMovement");
    public static final Module camera = new ModuleCamera(3, "Camera").setTexture("camera").setID("cam1");
    public static final Module weapon1 = new ModuleWeapon(1, "Weapon I").setTexture("weapon1").setID("wea1");
    public static final Module weapon2 = new ModuleWeapon(2, "Weapon II").setUndergrade(new Module[] { weapon1 }).setTexture("weapon2")
            .setID("wea2");
    public static final Module weapon3 = new ModuleWeapon(3, "Weapon III").setUndergrade(new Module[] { weapon2 }).setTexture("weapon3")
            .setID("wea3");
    public static final Module weapon4 = new ModuleWeapon(4, "Weapon IV").setUndergrade(new Module[] { weapon3 }).setTexture("weapon4")
            .setID("wea4");
    public static final Module armor1 = new ModuleArmor(1, "Armor I").setTexture("armor1").setID("arm1");
    public static final Module armor2 = new ModuleArmor(2, "Armor II").setUndergrade(new Module[] { armor1 }).setTexture("armor2")
            .setID("arm2");
    public static final Module armor3 = new ModuleArmor(3, "Armor III").setUndergrade(new Module[] { armor2 }).setTexture("armor3")
            .setID("arm3");
    public static final Module armor4 = new ModuleArmor(4, "Armor IV").setUndergrade(new Module[] { armor3 }).setTexture("armor4")
            .setID("arm4");
    public static final Module shooting = new ModuleShooting(1, "Shooting").setTexture("shooting").setID("sho1");
    public static final Module shootingHoming = new ModuleShooting(2, "Shooting Homing").setTexture("shootingHoming")
            .setID("sho2");
    public static final Module batterySave1 = new ModuleBatterySave(1, "Battery Saving I").setTexture("batterySave1")
            .setID("bas1");
    public static final Module batterySave2 = new ModuleBatterySave(2, "Battery Saving II").setUndergrade(new Module[] { batterySave1 })
            .setTexture("batterySave2").setID("bas2");
    public static final Module batterySave3 = new ModuleBatterySave(3, "Battery Saving III").setUndergrade(new Module[] { batterySave2 })
            .setTexture("batterySave3").setID("bas3");
    public static final Module batterySave4 = new ModuleBatterySave(4, "Battery Saving IV").setUndergrade(new Module[] { batterySave3 })
            .setTexture("batterySave4").setID("bas4");
    public static final Module heatPower = new ModuleRecharge(1, "Heat Powered").setTexture("heatPower").setID("pow1");
    public static final Module solarPower = new ModuleRecharge(2, "Solar Powered").setTexture("solarPower")
            .setID("pow2");
    public static final Module multiPower = new ModuleRecharge(2, "Multi Powered").setTexture("multiPower")
            .setID("pow3").setUndergrade(new Module[] { heatPower, solarPower });
    public static final Module autoReturn = new ModuleReturn(2, "Return on Low Battery").setTexture("autoReturn")
            .setID("aur1");
    public static final Module deflect = new ModuleDe(1, "Projectile Deflecting").setTexture("deflect").setID("def1");
    public static final Module deflame = new ModuleDe(2, "Fire Extinguishing").setTexture("deflame").setID("def2");
    public static final Module deexplosion = new ModuleDe(3, "Explosion Negating").setTexture("deexplosion")
            .setID("def3");
    public static final Module multiDe = new ModuleDe(4, "Multi Protection").setID("def4")
            .setUndergrade(new Module[] { deflect, deflame, deexplosion }).setTexture("multiDe");
    public static final Module mine1 = new ModuleMine(1, "Mining I").setTexture("mine1").setID("min1");
    public static final Module mine2 = new ModuleMine(2, "Mining II").setUndergrade(new Module[] { mine1 }).setTexture("mine2")
            .setID("min2");
    public static final Module mine3 = new ModuleMine(3, "Mining III").setUndergrade(new Module[] { mine2 }).setTexture("mine3")
            .setID("min3");
    public static final Module mine4 = new ModuleMine(4, "Mining IV").setUndergrade(new Module[] { mine3 }).setTexture("mine4")
            .setID("min4");
    private String id;
    public int level;
    public String displayName;
    public ModuleType type;
    public List<Module> undergrade = new ArrayList();
    public ResourceLocation texture = new ResourceLocation("drones", "/textures/modules/placeholder.png");

    public Module(int l, ModuleType t, String s)
    {
        this.level = l;
        this.type = t;
        this.displayName = s;
        modules.add(this);
    }

    public Module setID(String s)
    {
        this.id = s;
        return this;
    }

    public Module setUndergrade(Module... ug)
    {
        for (int a = 0; a < ug.length; a++)
        {
            this.undergrade.add(ug[a]);
            this.undergrade.addAll(ug[a].undergrade);
        }
        return this;
    }

    public Module setTexture(String s)
    {
        this.texture = new ResourceLocation("drones", "textures/modules/" + s + ".png");
        return this;
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
        list.add("Rank: " + color + this.level);
        list.add("Category: " + TextFormatting.WHITE + this.type);
        additionalTooltip(list, false);
        return list;
    }

    public void updateModule(EntityDrone drone) {}

    public void overrideDroneMovement(EntityDrone drone) {}

    public boolean collideWithEntity(EntityDrone drone, Entity e)
    {
        return false;
    }

    public boolean collideWithBlock(EntityDrone drone, BlockPos pos, IBlockState state)
    {
        return false;
    }

    public int overridePriority()
    {
        return 0;
    }

    public boolean canOverrideDroneMovement(EntityDrone drone)
    {
        return false;
    }

    public double costBatRawPerSec(EntityDrone drone)
    {
        return this.level;
    }

    public void onDisabled(EntityDrone drone) {}

    public void onReenabled(EntityDrone drone) {}

    public void additionalTooltip(List<String> list, boolean forGuiDroneStatus) {}

    public NBTTagCompound getModNBT(DroneInfo di)
    {
        if ((di != null) && (di.modsNBT != null))
        {
            if (!di.modsNBT.hasKey("MNBT")) {
                di.modsNBT.setTag("MNBT", new NBTTagCompound());
            }
            return di.modsNBT.getCompoundTag("MNBT");
        }
        return null;
    }

    public void setModNBT(DroneInfo di, NBTTagCompound tag)
    {
        if ((di != null) && (di.modsNBT != null)) {
            di.modsNBT.setTag("MNBT", tag);
        }
    }

    public boolean canReplace(Module m)
    {
        return this.undergrade.contains(m);
    }

    public boolean canFunctionAs(Module m)
    {
        return (this == m) || (canReplace(m));
    }

    public String getID()
    {
        return this.id;
    }

    @SideOnly(Side.CLIENT)
    public ModuleGui getModuleGui(GuiDroneStatus gui)
    {
        return new ModuleGui(gui, this);
    }

    public static Module getModuleByID(String id)
    {
        for (Module m : modules) {
            if (m.getID().equals(id)) {
                return m;
            }
        }
        return null;
    }

    public static Module getModule(String name)
    {
        for (Module m : modules) {
            if (m.displayName.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public static enum ModuleType
    {
        Transport,  Collect,  Scout,  Battle,  Recover,  Misc;

        private ModuleType() {}
    }

    public String toString()
    {
        return "Mod-" + this.displayName;
    }

    @SideOnly(Side.CLIENT)
    public static class ModuleGui<T extends Module>
            extends GuiScreen
    {
        public GuiDroneStatus parent;
        public T mod;
        public GuiButton disableButton;
        public GuiButton uninstallButton;
        public SlotModule modSlot;
        public int scrollHeight;
        public int maxScrollHeight;
        public List<String> splittedString = new ArrayList();

        public ModuleGui(GuiDroneStatus gui, T m)
        {
            this.parent = gui;
            this.mod = m;
        }

        public void initGui()
        {
            super.initGui();
            this.buttonList.add(this.disableButton = new GuiButton(0, this.width / 2 + 48, this.height / 2 + 70, 40, 20, "Disable"));
            this.buttonList.add(this.uninstallButton = new GuiButton(99, this.width / 2 + 90, this.height / 2 + 70, 50, 20, "Uninstall"));
            if (!this.parent.drone.droneInfo.isEnabled(this.mod)) {
                this.disableButton.displayString = "Enable";
            }
        }

        protected void actionPerformed(GuiButton button)
                throws IOException
        {
            super.actionPerformed(button);
            boolean enabled = this.parent.drone.droneInfo.isEnabled(this.mod);
            if (button == this.disableButton)
            {
                PacketDispatcher.sendToServer(new PacketDroneSwitchMod(this.parent.drone, this.mod, !enabled));
                this.disableButton.displayString = (enabled ? "Enable" : "Disable");
            }
            if (button == this.uninstallButton)
            {
                PacketDispatcher.sendToServer(new PacketDroneUninstallMod(this.parent.drone, this.mod));
                this.parent.selectedModSlot.overlayColor = -1;
                this.parent.selectedModSlot = null;
                this.parent.selectedModGui = null;
            }
            else if (!enabled)
            {
                return;
            }
            buttonClickedOnEnabledGui(button);
        }

        public void buttonClickedOnEnabledGui(GuiButton button) {}

        public void drawBackground(int tint) {}

        public void drawDefaultBackground() {}

        public void drawWorldBackground(int tint) {}

        public void updateScreen()
        {
            super.updateScreen();
            if (Mouse.isCreated())
            {
                int mouseD = Mouse.getDWheel();
                this.scrollHeight = ((int)(this.scrollHeight - Math.signum(mouseD) * Math.pow(Math.abs(mouseD / 12.0D), 0.7D)));
            }
            this.scrollHeight = Math.max(0, Math.min(this.scrollHeight, this.maxScrollHeight - 60));
        }

        public List<String> getDescText()
        {
            List<String> l = new ArrayList();
            TextFormatting color = TextFormatting.WHITE;
            switch (this.mod.level)
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
            String init = color + "" + TextFormatting.BOLD + this.mod.displayName + TextFormatting.WHITE + "" + TextFormatting.BOLD + " - " + this.mod.type + " mod rank " + color + "" + TextFormatting.BOLD + this.mod.level;
            if (!this.parent.drone.droneInfo.isEnabled(this.mod)) {
                init = color + "" + TextFormatting.BOLD + this.mod.displayName + TextFormatting.RED + "" + TextFormatting.BOLD + " - disabled";
            }
            l.add(init);
            this.mod.additionalTooltip(l, true);
            addDescText(l);
            return l;
        }

        public void addDescText(List<String> l) {}

        public void drawScreen(int mouseX, int mouseY, float partialTicks)
        {
            List<String> desc = getDescText();
            if (!desc.isEmpty())
            {
                ScaledResolution sr = new ScaledResolution(this.mc);
                int sclh = sr.getScaledHeight();
                int sclw = sr.getScaledWidth();
                this.splittedString.clear();
                for (int a = 0; a < desc.size(); a++) {
                    this.splittedString.addAll(this.fontRendererObj.listFormattedStringToWidth((String)desc.get(a), 280));
                }
                this.maxScrollHeight = (this.splittedString.size() * 10 + 8);
                GL11.glPushMatrix();
                GL11.glEnable(3089);
                GL11.glScissor((sclw / 2 - 143) * this.mc.displayWidth / sclw, (sclh - (sclh / 2 + 91)) * this.mc.displayHeight / sclh, 284 * this.mc.displayWidth / sclw, 80 * this.mc.displayHeight / sclh);

                GL11.glTranslated(this.width / 2 - 140, this.height / 2 + 15 - this.scrollHeight, 0.0D);
                for (int a = 0; a < this.splittedString.size(); a++)
                {
                    String s = (String)this.splittedString.get(a);
                    this.fontRendererObj.drawString(s, 0, a * 10, 16777215);
                }
                GL11.glDisable(3089);
                GL11.glPopMatrix();
            }
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }
}
