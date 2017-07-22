/*    */ package williamle.drones.api.model;
/*    */ 
/*    */ public class UniqueName
/*    */ {
/*    */   public String string;
/*    */   
/*    */   public UniqueName(String s)
/*    */   {
/*  9 */     this.string = s;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 15 */     return this.string;
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 21 */     return this.string.hashCode();
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 27 */     return ((obj instanceof UniqueName)) && (((UniqueName)obj).string.equals(this.string));
/*    */   }
/*    */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\model\UniqueName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */