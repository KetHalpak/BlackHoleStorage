package com.pengu.holestorage.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import com.pengu.holestorage.bookmaking.BookMakeHandler;

public class ItemKnowledgeParchment extends Item
{
	public ItemKnowledgeParchment()
	{
		setUnlocalizedName("knowledge_parchment");
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.getBlockState(pos).getBlock() == Blocks.BOOKSHELF && !BookMakeHandler.isCraftingAt(worldIn, pos))
		{
			player.getHeldItem(hand).shrink(1);
			if(!worldIn.isRemote)
				BookMakeHandler.startCrafting(worldIn, pos);
			return EnumActionResult.SUCCESS;
		}
		return EnumActionResult.FAIL;
	}
}