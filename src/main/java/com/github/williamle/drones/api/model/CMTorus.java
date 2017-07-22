/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class CMTorus extends CMPipeLine
/*    */ {
/*    */   public int segments;
/*    */   public double initRot;
/*    */   public double torusRa;
/*    */   public Vec3d origin;
/*    */   public Vec3d normal;
/*    */   
/*    */   public CMTorus(int s, int segs, double ra, double thick, double rot, Vec3d ori, Vec3d nor)
/*    */   {
/* 15 */     this.sides = s;
/* 16 */     this.segments = segs;
/* 17 */     this.torusRa = ra;
/* 18 */     this.radi = filledDouble(thick, segs + 1);
/* 19 */     this.initRot = rot;
/* 20 */     this.origin = (ori == null ? vec(0.0D, 0.0D, 0.0D) : ori);
/* 21 */     this.normal = ((nor == null) || (isZeroVec(nor)) ? vec(0.0D, 1.0D, 0.0D) : nor.func_72432_b());
/* 22 */     fullCalculate();
/*    */   }
/*    */   
/*    */   public void fullCalculate()
/*    */   {
/* 27 */     this.positions = calculatePositions();
/* 28 */     this.endCapsDirection[0] = fromTo(this.positions[0], this.positions[1])
/* 29 */       .func_178787_e(fromTo(this.positions[(this.positions.length - 2)], this.positions[(this.positions.length - 1)]));
/* 30 */     this.endCapsDirection[1] = this.endCapsDirection[0];
/* 31 */     this.pipes = calculatePipes();
/* 32 */     for (CMPipe pipe : this.pipes)
/*    */     {
/* 34 */       pipe.textureHasCaps = false;
/*    */     }
/*    */   }
/*    */   
/*    */   public Vec3d[] calculatePositions()
/*    */   {
/* 40 */     Vec3d[] pos = new Vec3d[this.segments + 1];
/* 41 */     Vec3d baseRot = setLength(rotateAround(
/* 42 */       getPerpendicularVec(this.normal, (this.normal.field_72450_a == 0.0D) && (this.normal.field_72449_c == 0.0D) ? vec(0.0D, 0.0D, 1.0D) : vec(0.0D, 1.0D, 0.0D)), this.normal, this.initRot), this.torusRa);
/*    */     
/* 44 */     double rotAngle = 6.283185307179586D / this.segments;
/* 45 */     for (int a = 0; a < pos.length; a++)
/*    */     {
/* 47 */       pos[a] = rotateAround(baseRot, this.normal, rotAngle * a).func_178787_e(this.origin);
/*    */     }
/* 49 */     return pos;
/*    */   }
/*    */   
/*    */ 
/*    */   public CapType renderCaps(CMPipe pipe, int pipeIndex)
/*    */   {
/* 55 */     return CapType.NONE;
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\CMTorus.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */