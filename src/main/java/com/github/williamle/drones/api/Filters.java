/*     */ package williamle.drones.api;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.init.Blocks;
/*     */ 
/*     */ public class Filters
/*     */ {
/*     */   public static class FilterExcept
/*     */     implements Predicate
/*     */   {
/*     */     Object exception;
/*     */     
/*     */     public FilterExcept(Object o)
/*     */     {
/*  21 */       this.exception = o;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/*  27 */       return !input.equals(this.exception);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FilterExcepts implements Predicate
/*     */   {
/*  33 */     List except = new ArrayList();
/*     */     
/*     */     public FilterExcepts(Object... o)
/*     */     {
/*  37 */       for (Object o1 : o)
/*     */       {
/*  39 */         this.except.add(o1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/*  46 */       for (Object o : this.except)
/*     */       {
/*  48 */         if (input == o) return false;
/*  49 */         if ((!(input instanceof Class)) && ((o instanceof Class)) && 
/*  50 */           (((Class)o).isAssignableFrom(input.getClass())))
/*  51 */           return false;
/*     */       }
/*  53 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FilterAccepts implements Predicate
/*     */   {
/*  59 */     List accept = new ArrayList();
/*     */     
/*     */     public FilterAccepts(Object... o)
/*     */     {
/*  63 */       for (Object o1 : o)
/*     */       {
/*  65 */         this.accept.add(o1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/*  72 */       for (Object o : this.accept)
/*     */       {
/*  74 */         if (input == o) return true;
/*  75 */         if ((!(input instanceof Class)) && ((o instanceof Class)) && 
/*  76 */           (((Class)o).isAssignableFrom(input.getClass())))
/*  77 */           return true;
/*     */       }
/*  79 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FilterReplaceable implements Predicate
/*     */   {
/*  85 */     boolean liquid = false;
/*     */     
/*     */     public FilterReplaceable(boolean b)
/*     */     {
/*  89 */       this.liquid = b;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/*  95 */       IBlockState ibs = null;
/*  96 */       Block b = null;
/*  97 */       if ((input instanceof Block))
/*     */       {
/*  99 */         b = (Block)input;
/* 100 */         ibs = b.func_176223_P();
/*     */       }
/* 102 */       else if ((input instanceof IBlockState))
/*     */       {
/* 104 */         ibs = (IBlockState)input;
/* 105 */         b = ibs.func_177230_c();
/*     */       }
/*     */       
/* 108 */       return (b == Blocks.field_150350_a) || (ibs.func_185904_a().func_76222_j()) || ((this.liquid) && (ibs.func_185904_a().func_76224_d()));
/*     */     }
/*     */   }
/*     */   
/* 112 */   public static class FilterExceptsBlock extends Filters.FilterExcepts { public FilterExceptsBlock() { super(); }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/* 117 */       boolean thisApply = super.apply(input);
/* 118 */       boolean blockApply = true;
/* 119 */       boolean stateApply = true;
/* 120 */       if ((input instanceof IBlockState)) blockApply = super.apply(((IBlockState)input).func_177230_c());
/* 121 */       if ((input instanceof Block)) stateApply = super.apply(((Block)input).func_176223_P());
/* 122 */       return (thisApply) && (blockApply) && (stateApply);
/*     */     }
/*     */   }
/*     */   
/* 126 */   public static class FilterAcceptsBlock extends Filters.FilterAccepts { public FilterAcceptsBlock() { super(); }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/* 131 */       boolean thisApply = super.apply(input);
/* 132 */       boolean blockApply = false;
/* 133 */       boolean stateApply = false;
/* 134 */       if ((input instanceof IBlockState)) blockApply = super.apply(((IBlockState)input).func_177230_c());
/* 135 */       if ((input instanceof Block)) stateApply = super.apply(((Block)input).func_176223_P());
/* 136 */       return (thisApply) || (blockApply) || (stateApply);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class FilterChance implements Predicate
/*     */   {
/* 142 */     Random rnd = new Random();
/* 143 */     double chance = 1.0D;
/*     */     
/*     */     public FilterChance(double d)
/*     */     {
/* 147 */       this.chance = d;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean apply(Object input)
/*     */     {
/* 153 */       return this.rnd.nextDouble() <= this.chance;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\fanff\Desktop\CustomDrones-1.5.0-mc1.10.2.jar!\williamle\drones\api\Filters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */