package com.github.nekomeowww.customdrones.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.nekomeowww.customdrones.CustomDrones;

public class BlockCrafter
        extends Block
{
    public BlockCrafter()
    {
        super(Material.WOOD);
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(CustomDrones.instance, 3, worldIn, pos.getX(), pos.getY(), pos.getZ());
        return true;
    }
}
