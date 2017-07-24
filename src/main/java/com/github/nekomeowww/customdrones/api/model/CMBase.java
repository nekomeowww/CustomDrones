package com.github.nekomeowww.customdrones.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;
import com.github.nekomeowww.customdrones.api.helpers.VecHelper;

public class CMBase
        extends VecHelper
{
    public static Minecraft mc = null;
    public static boolean USEGLSM = true;
    public static boolean USECOLORSPRITE = true;
    public static final int COLORWIDTH = 24;
    public static final ResourceLocation COLORSPITE = new ResourceLocation("drones", "textures/colors.png");
    public static final Vec3d vec0 = vec(0.0D, 0.0D, 0.0D);
    public UniqueName cmName = new UniqueName(String.valueOf(hashCode()));
    public Color color = new Color(-1L);
    private List<String> paletteIndexes = new ArrayList();
    private boolean useTexture = false;
    public ResourceLocation texture;
    public TextureUV textureUV = new TextureUV();
    public Set<CMBase> childModels = new HashSet();
    public int visible = 3;
    public Vec3d localScale = vec(1.0D, 1.0D, 1.0D);
    public Vec3d localTranslate = vec(0.0D, 0.0D, 0.0D);
    public Vec3d localCenter = vec(0.0D, 0.0D, 0.0D);
    public List<RotateVec> centerRots = new ArrayList();

    public CMBase setName(String s)
    {
        this.cmName = new UniqueName(s);
        return this;
    }

    public CMBase addChild(CMBase cm)
    {
        this.childModels.add(cm);
        return this;
    }

    public CMBase setScale(double x, double y, double z)
    {
        this.localScale = vec(x, y, z);
        return this;
    }

    public CMBase setTranslate(double x, double y, double z)
    {
        this.localTranslate = vec(x, y, z);
        return this;
    }

    public CMBase setCenter(double x, double y, double z)
    {
        this.localCenter = vec(x, y, z);
        return this;
    }

    public CMBase resetRotation(double x, double y, double z, double a)
    {
        this.centerRots.clear();
        return setRotation(x, y, z, a);
    }

    public CMBase setRotation(double x, double y, double z, double a)
    {
        this.centerRots.add(new RotateVec(a, x, y, z));
        return this;
    }

    public CMBase setColor(Color c)
    {
        this.color = c;
        this.useTexture = false;
        return this;
    }

    public CMBase setColor(double r, double g, double b, double a)
    {
        this.color = new Color(r, g, b, a);
        this.useTexture = false;
        return this;
    }

    public CMBase setPaletteIndexes(String... s)
    {
        this.paletteIndexes.clear();
        for (int a = 0; a < s.length; a++)
        {
            String pal = s[a];
            addPaletteIndex(pal);
        }
        return this;
    }

    public CMBase addPaletteIndex(String s)
    {
        if (s != null) {
            this.paletteIndexes.add(s);
        } else {
            this.paletteIndexes.add("");
        }
        return this;
    }

    public List<String> getPaletteIndexes()
    {
        return this.paletteIndexes;
    }

    public boolean hasPaletteIndex(String s)
    {
        return getPaletteIndexes().contains(s);
    }

    public void setPaletteIndexColor(String s, Color c, boolean setFull)
    {
        if (hasPaletteIndex(s)) {
            if (setFull) {
                setFullColor(c);
            } else {
                setColor(c);
            }
        }
    }

    public Color getPaletteIndexColor(String s)
    {
        if (hasPaletteIndex(s)) {
            return this.color;
        }
        return null;
    }

    public CMBase setFullColor(Color c)
    {
        setColor(c);
        for (CMBase cm : this.childModels) {
            cm.setFullColor(c);
        }
        return this;
    }

    public CMBase setFullColor(double r, double g, double b, double a)
    {
        setColor(r, g, b, a);
        for (CMBase cm : this.childModels) {
            cm.setFullColor(r, g, b, a);
        }
        return this;
    }

    public CMBase setFullTexture(ResourceLocation text)
    {
        setTexture(text);
        for (CMBase cm : this.childModels) {
            cm.setFullTexture(text);
        }
        return this;
    }

    public void setUseTexture(boolean b)
    {
        this.useTexture = b;
    }

    public CMBase setTexture(ResourceLocation text)
    {
        this.texture = text;
        this.useTexture = (text != null);
        return this;
    }

    public CMBase setTextureUV(TextureUV uv)
    {
        this.textureUV = uv;
        return this;
    }

    public void fullRender()
    {
        push();
        resetColor();
        enableNormalize(true);
        enableBlend(true);
        blendFunc(770, 771);
        applyColorOrTexture(this.texture, this.color);
        translate(this.localTranslate.xCoord, this.localTranslate.yCoord, this.localTranslate.zCoord);
        scale(this.localScale.xCoord, this.localScale.yCoord, this.localScale.zCoord);
        if (!this.centerRots.isEmpty())
        {
            translate(this.localCenter.xCoord, this.localCenter.yCoord, this.localCenter.zCoord);
            for (int a = this.centerRots.size() - 1; a >= 0; a--)
            {
                RotateVec rot = (RotateVec)this.centerRots.get(a);
                rotate(rot.rotation, rot.vec.xCoord, rot.vec.yCoord, rot.vec.zCoord);
            }
            translate(-this.localCenter.xCoord, -this.localCenter.yCoord, -this.localCenter.zCoord);
        }
        if ((this.visible == 2) || (this.visible == 3)) {
            render();
        }
        if ((this.visible == 1) || (this.visible == 3)) {
            renderChilds();
        }
        enableNormalize(false);
        enableTexture(true);
        pop();
    }

    public void render() {}

    public void renderChilds()
    {
        for (CMBase cm : this.childModels) {
            cm.fullRender();
        }
    }

    public void applyColorOrTexture(ResourceLocation text, Color col)
    {
        if (shouldApplyTexture())
        {
            enableTexture(true);
            bindTexture(text);
        }
        else
        {
            enableTexture(false);
            if (col != null) {
                color(col.red, col.green, col.blue, col.alpha);
            }
        }
    }

    public static void bindTexture(ResourceLocation location)
    {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        if ((mc != null) && (location != null))
        {
            TextureManager texturemanager = mc.renderEngine;
            if (texturemanager != null) {
                texturemanager.bindTexture(location);
            }
        }
    }

    public boolean shouldApplyTexture()
    {
        return (this.texture != null) && ((this.useTexture) || (this.color == null));
    }

    public static void push()
    {
        if (USEGLSM) {
            GlStateManager.pushMatrix();
        } else {
            GL11.glPushMatrix();
        }
    }

    public static void pop()
    {
        if (USEGLSM) {
            GlStateManager.popMatrix();
        } else {
            GL11.glPopMatrix();
        }
    }

    public static void color(double r, double g, double b)
    {
        color(r, g, b, 1.0D);
    }

    public static void color(double r, double g, double b, double a)
    {
        if (USECOLORSPRITE)
        {
            double pixelHalfWidth = 8.680555555555555E-4D;
            double x = Math.floor(b * 23.0D) * 24.0D + Math.floor(r * 23.0D);
            double y = Math.floor(a * 23.0D) * 24.0D + Math.floor(g * 23.0D);
            x = x / 24.0D / 24.0D + pixelHalfWidth;
            y = y / 24.0D / 24.0D + pixelHalfWidth;
            texCoord(x, y);
        }
        else if (USEGLSM)
        {
            GlStateManager.color((float)r, (float)g, (float)b, (float)a);
        }
        else
        {
            GL11.glColor4d(r, g, b, a);
        }
    }

    public static void resetColor()
    {
        color(1.0D, 1.0D, 1.0D, 1.0D);
    }

    public static void clearColor(double r, double g, double b, double a)
    {
        if (USEGLSM) {
            GlStateManager.clearColor((float)r, (float)g, (float)b, (float)a);
        } else {
            GL11.glClearColor((float)r, (float)g, (float)b, (float)a);
        }
    }

    public static void enableNormalize(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableNormalize();
            } else {
                GL11.glEnable(2977);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableNormalize();
        } else {
            GL11.glDisable(2977);
        }
    }

    public static void enableLighting(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableLighting();
            } else {
                GL11.glEnable(2896);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableLighting();
        } else {
            GL11.glDisable(2896);
        }
    }

    public static void enableFog(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableFog();
            } else {
                GL11.glEnable(2912);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableFog();
        } else {
            GL11.glDisable(2912);
        }
    }

    public static void enableTexture(boolean enable)
    {
        if ((enable) || ((USECOLORSPRITE) && (!enable)))
        {
            if (USEGLSM) {
                GlStateManager.enableTexture2D();
            } else {
                GL11.glEnable(3553);
            }
            if (USECOLORSPRITE) {
                bindTexture(COLORSPITE);
            }
        }
        else if (USEGLSM)
        {
            GlStateManager.disableTexture2D();
        }
        else
        {
            GL11.glDisable(3553);
        }
    }

    public static void enableColorMaterial(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableColorMaterial();
            } else {
                GL11.glEnable(2903);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableColorMaterial();
        } else {
            GL11.glDisable(2903);
        }
    }

    public static void enableAlpha(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableAlpha();
            } else {
                GL11.glEnable(3008);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableAlpha();
        } else {
            GL11.glDisable(3008);
        }
    }

    public static void enableBlend(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableBlend();
            } else {
                GL11.glEnable(3042);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableBlend();
        } else {
            GL11.glDisable(3042);
        }
    }

    public static void enableCull(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.enableCull();
            } else {
                GL11.glEnable(2884);
            }
        }
        else if (USEGLSM) {
            GlStateManager.disableCull();
        } else {
            GL11.glDisable(2884);
        }
    }

    public static void blendFunc(int i1, int i2)
    {
        if (USEGLSM) {
            GlStateManager.blendFunc(i1, i2);
        } else {
            GL11.glBlendFunc(i1, i2);
        }
    }

    public static void translate(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.translate(x, y, z);
        } else {
            GL11.glTranslated(x, y, z);
        }
    }

    public static void scale(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.scale(x, y, z);
        } else {
            GL11.glScaled(x, y, z);
        }
    }

    public static void rotate(double angle, double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.rotate((float)angle, (float)x, (float)y, (float)z);
        } else {
            GL11.glRotated(angle, x, y, z);
        }
    }

    public static void lineWidth(double w)
    {
        if (USEGLSM) {
            GlStateManager.glLineWidth((float)w);
        } else {
            GL11.glLineWidth((float)w);
        }
    }

    public static void begin(int mode)
    {
        if (USEGLSM) {
            GlStateManager.glBegin(mode);
        } else {
            GL11.glBegin(mode);
        }
    }

    public static void enable(int enable)
    {
        if (USEGLSM) {
            GlStateManager.glEnableClientState(enable);
        } else {
            GL11.glEnable(enable);
        }
    }

    public void vertex(double x, double y, double z, double u, double v)
    {
        if (shouldApplyTexture()) {
            texCoord(u, v);
        }
        if (USEGLSM) {
            GlStateManager.glVertex3f((float)x, (float)y, (float)z);
        } else {
            GL11.glVertex3d(x, y, z);
        }
    }

    public static void texCoord(double u, double v)
    {
        if (USEGLSM) {
            GlStateManager.glTexCoord2f((float)u, (float)v);
        } else {
            GL11.glTexCoord2d(u, v);
        }
    }

    public static void normal(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.glNormal3f((float)x, (float)y, (float)z);
        } else {
            GL11.glNormal3d(x, y, z);
        }
    }

    public static void end()
    {
        if (USEGLSM) {
            GlStateManager.glEnd();
        } else {
            GL11.glEnd();
        }
    }
}
