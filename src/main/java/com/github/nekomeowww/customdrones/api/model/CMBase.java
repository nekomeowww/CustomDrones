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
    public static Minecraft mc = ;
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
        translate(this.localTranslate.field_72450_a, this.localTranslate.field_72448_b, this.localTranslate.field_72449_c);
        scale(this.localScale.field_72450_a, this.localScale.field_72448_b, this.localScale.field_72449_c);
        if (!this.centerRots.isEmpty())
        {
            translate(this.localCenter.field_72450_a, this.localCenter.field_72448_b, this.localCenter.field_72449_c);
            for (int a = this.centerRots.size() - 1; a >= 0; a--)
            {
                RotateVec rot = (RotateVec)this.centerRots.get(a);
                rotate(rot.rotation, rot.vec.field_72450_a, rot.vec.field_72448_b, rot.vec.field_72449_c);
            }
            translate(-this.localCenter.field_72450_a, -this.localCenter.field_72448_b, -this.localCenter.field_72449_c);
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
            mc = Minecraft.func_71410_x();
        }
        if ((mc != null) && (location != null))
        {
            TextureManager texturemanager = mc.field_71446_o;
            if (texturemanager != null) {
                texturemanager.func_110577_a(location);
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
            GlStateManager.func_179094_E();
        } else {
            GL11.glPushMatrix();
        }
    }

    public static void pop()
    {
        if (USEGLSM) {
            GlStateManager.func_179121_F();
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
            GlStateManager.func_179131_c((float)r, (float)g, (float)b, (float)a);
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
            GlStateManager.func_179082_a((float)r, (float)g, (float)b, (float)a);
        } else {
            GL11.glClearColor((float)r, (float)g, (float)b, (float)a);
        }
    }

    public static void enableNormalize(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179108_z();
            } else {
                GL11.glEnable(2977);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179133_A();
        } else {
            GL11.glDisable(2977);
        }
    }

    public static void enableLighting(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179145_e();
            } else {
                GL11.glEnable(2896);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179140_f();
        } else {
            GL11.glDisable(2896);
        }
    }

    public static void enableFog(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179127_m();
            } else {
                GL11.glEnable(2912);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179106_n();
        } else {
            GL11.glDisable(2912);
        }
    }

    public static void enableTexture(boolean enable)
    {
        if ((enable) || ((USECOLORSPRITE) && (!enable)))
        {
            if (USEGLSM) {
                GlStateManager.func_179098_w();
            } else {
                GL11.glEnable(3553);
            }
            if (USECOLORSPRITE) {
                bindTexture(COLORSPITE);
            }
        }
        else if (USEGLSM)
        {
            GlStateManager.func_179090_x();
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
                GlStateManager.func_179142_g();
            } else {
                GL11.glEnable(2903);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179119_h();
        } else {
            GL11.glDisable(2903);
        }
    }

    public static void enableAlpha(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179141_d();
            } else {
                GL11.glEnable(3008);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179118_c();
        } else {
            GL11.glDisable(3008);
        }
    }

    public static void enableBlend(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179147_l();
            } else {
                GL11.glEnable(3042);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179084_k();
        } else {
            GL11.glDisable(3042);
        }
    }

    public static void enableCull(boolean enable)
    {
        if (enable)
        {
            if (USEGLSM) {
                GlStateManager.func_179089_o();
            } else {
                GL11.glEnable(2884);
            }
        }
        else if (USEGLSM) {
            GlStateManager.func_179129_p();
        } else {
            GL11.glDisable(2884);
        }
    }

    public static void blendFunc(int i1, int i2)
    {
        if (USEGLSM) {
            GlStateManager.func_179112_b(i1, i2);
        } else {
            GL11.glBlendFunc(i1, i2);
        }
    }

    public static void translate(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.func_179137_b(x, y, z);
        } else {
            GL11.glTranslated(x, y, z);
        }
    }

    public static void scale(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.func_179139_a(x, y, z);
        } else {
            GL11.glScaled(x, y, z);
        }
    }

    public static void rotate(double angle, double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.func_179114_b((float)angle, (float)x, (float)y, (float)z);
        } else {
            GL11.glRotated(angle, x, y, z);
        }
    }

    public static void lineWidth(double w)
    {
        if (USEGLSM) {
            GlStateManager.func_187441_d((float)w);
        } else {
            GL11.glLineWidth((float)w);
        }
    }

    public static void begin(int mode)
    {
        if (USEGLSM) {
            GlStateManager.func_187447_r(mode);
        } else {
            GL11.glBegin(mode);
        }
    }

    public static void enable(int enable)
    {
        if (USEGLSM) {
            GlStateManager.func_187410_q(enable);
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
            GlStateManager.func_187435_e((float)x, (float)y, (float)z);
        } else {
            GL11.glVertex3d(x, y, z);
        }
    }

    public static void texCoord(double u, double v)
    {
        if (USEGLSM) {
            GlStateManager.func_187426_b((float)u, (float)v);
        } else {
            GL11.glTexCoord2d(u, v);
        }
    }

    public static void normal(double x, double y, double z)
    {
        if (USEGLSM) {
            GlStateManager.func_187432_a((float)x, (float)y, (float)z);
        } else {
            GL11.glNormal3d(x, y, z);
        }
    }

    public static void end()
    {
        if (USEGLSM) {
            GlStateManager.func_187437_J();
        } else {
            GL11.glEnd();
        }
    }
}
