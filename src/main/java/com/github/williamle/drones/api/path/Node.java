/*    */ package williamle.drones.api.path;
/*    */ 
/*    */ import net.minecraft.util.math.Vec3d;
/*    */ 
/*    */ public class Node {
/*    */   public final double x;
/*    */   public final double y;
/*    */   public final double z;
/*    */   
/*    */   public Node(double i, double j, double k) {
/* 11 */     this.x = i;
/* 12 */     this.y = j;
/* 13 */     this.z = k;
/*    */   }
/*    */   
/*    */   public Vec3d toVec()
/*    */   {
/* 18 */     return new Vec3d(this.x, this.y, this.z);
/*    */   }
/*    */   
/*    */   public Vec3d toIVec()
/*    */   {
/* 23 */     return new Vec3d(intX(), intY(), intZ());
/*    */   }
/*    */   
/*    */   public int intX()
/*    */   {
/* 28 */     return (int)Math.floor(this.x);
/*    */   }
/*    */   
/*    */   public int intY()
/*    */   {
/* 33 */     return (int)Math.floor(this.y);
/*    */   }
/*    */   
/*    */   public int intZ()
/*    */   {
/* 38 */     return (int)Math.floor(this.z);
/*    */   }
/*    */   
/*    */   public double distTo(Node p2)
/*    */   {
/* 43 */     return Math.sqrt(distSqrTo(p2));
/*    */   }
/*    */   
/*    */   public double distTo(double i, double j, double k)
/*    */   {
/* 48 */     return Math.sqrt(distSqrTo(i, j, k));
/*    */   }
/*    */   
/*    */   public double distSqrTo(Node p2)
/*    */   {
/* 53 */     return distSqrTo(p2.x, p2.y, p2.z);
/*    */   }
/*    */   
/*    */   public double distSqrTo(double i, double j, double k)
/*    */   {
/* 58 */     return (this.x - i) * (this.x - i) + (this.y - j) * (this.y - j) + (this.z - k) * (this.z - k);
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 64 */     return ((obj instanceof Node)) && (((Node)obj).x == this.x) && (((Node)obj).y == this.y) && (((Node)obj).z == this.z);
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\path\Node.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */