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
        super(Material.field_151575_d);
    }

    public boolean func_180639_a(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        playerIn.openGui(DronesMod.instance, 3, worldIn, pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
        return true;
    }
}
