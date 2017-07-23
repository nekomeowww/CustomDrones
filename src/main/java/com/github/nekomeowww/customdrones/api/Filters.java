package com.github.nekomeowww.customdrones.api;

import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class Filters
{
    public static class FilterExcept
            implements Predicate
    {
        Object exception;

        public FilterExcept(Object o)
        {
            this.exception = o;
        }


        public boolean apply(Object input)
        {
            return !input.equals(this.exception);
        }
    }

    public static class FilterExcepts implements Predicate
    {
        List except = new ArrayList();

        public FilterExcepts(Object... o)
        {
            for (Object o1 : o)
            {
                this.except.add(o1);
            }
        }


        public boolean apply(Object input)
        {
            for (Object o : this.except)
            {
                if (input == o) return false;
                if ((!(input instanceof Class)) && ((o instanceof Class)) &&
                        (((Class)o).isAssignableFrom(input.getClass())))
                    return false;
            }
            return true;
        }
    }

    public static class FilterAccepts implements Predicate
    {
        List accept = new ArrayList();

        public FilterAccepts(Object... o)
        {
            for (Object o1 : o)
            {
                this.accept.add(o1);
            }
        }


        public boolean apply(Object input)
        {
            for (Object o : this.accept)
            {
                if (input == o) return true;
                if ((!(input instanceof Class)) && ((o instanceof Class)) &&
                        (((Class)o).isAssignableFrom(input.getClass())))
                    return true;
            }
            return false;
        }
    }

    public static class FilterReplaceable implements Predicate
    {
        boolean liquid = false;

        public FilterReplaceable(boolean b)
        {
            this.liquid = b;
        }


        public boolean apply(Object input)
        {
            IBlockState ibs = null;
            Block b = null;
            if ((input instanceof Block))
            {
                b = (Block)input;
                ibs = b.func_176223_P();
            }
            else if ((input instanceof IBlockState))
            {
                ibs = (IBlockState)input;
                b = ibs.func_177230_c();
            }

            return (b == Blocks.field_150350_a) || (ibs.func_185904_a().func_76222_j()) || ((this.liquid) && (ibs.func_185904_a().func_76224_d()));
        }
    }

    public static class FilterExceptsBlock extends Filters.FilterExcepts { public FilterExceptsBlock() { super(); }


        public boolean apply(Object input)
        {
            boolean thisApply = super.apply(input);
            boolean blockApply = true;
            boolean stateApply = true;
            if ((input instanceof IBlockState)) blockApply = super.apply(((IBlockState)input).func_177230_c());
            if ((input instanceof Block)) stateApply = super.apply(((Block)input).func_176223_P());
            return (thisApply) && (blockApply) && (stateApply);
        }
    }

    public static class FilterAcceptsBlock extends Filters.FilterAccepts { public FilterAcceptsBlock() { super(); }


        public boolean apply(Object input)
        {
            boolean thisApply = super.apply(input);
            boolean blockApply = false;
            boolean stateApply = false;
            if ((input instanceof IBlockState)) blockApply = super.apply(((IBlockState)input).func_177230_c());
            if ((input instanceof Block)) stateApply = super.apply(((Block)input).func_176223_P());
            return (thisApply) || (blockApply) || (stateApply);
        }
    }

    public static class FilterChance implements Predicate
    {
        Random rnd = new Random();
        double chance = 1.0D;

        public FilterChance(double d)
        {
            this.chance = d;
        }


        public boolean apply(Object input)
        {
            return this.rnd.nextDouble() <= this.chance;
        }
    }
}
