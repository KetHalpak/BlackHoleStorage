package com.pengu.holestorage.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.tile.TileWormholeFormer;

public class BlockWormholeFormer extends BlockTraceable implements ITileEntityProvider
{
	public BlockWormholeFormer()
	{
		super(Material.IRON);
		setUnlocalizedName("wormhole_former");
		setHardness(4);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public AxisAlignedBB getFullBoundingBox(IBlockAccess world, BlockPos pos, IBlockState state)
	{
		return FULL_BLOCK_AABB;
	}
	
	@Override
	public boolean onBoxActivated(int boxID, Cuboid6 box, World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		TileWormholeFormer former = worldIn.getTileEntity(pos) instanceof TileWormholeFormer ? (TileWormholeFormer) worldIn.getTileEntity(pos) : null;
		
		if(former != null && !worldIn.isRemote)
		{
			int si = 0;
			
			if(boxID == 0)
				si = 0;
			else
				si = 1;
			
			ItemStack slot = former.inventory.getStackInSlot(si);
			if(InterItemStack.isStackNull(slot) && !InterItemStack.isStackNull(stack) && stack.getItem() == ItemsBHS.WORMHOLE_PEARL)
			{
				former.inventory.setInventorySlotContents(si, stack.copy());
				playerIn.setHeldItem(hand, InterItemStack.NULL_STACK);
				
				former.sync();
			} else
			{
				WorldUtil.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, slot);
				former.inventory.setInventorySlotContents(si, InterItemStack.NULL_STACK);
				
				former.sync();
			}
		}
		
		return former != null;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileWormholeFormer();
	}
	
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
	{
		return false;
	}
	
	@Override
	public boolean isBlockNormalCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isNormalCube(IBlockState p_isOpaqueCube_1_)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState p_isFullCube_1_)
	{
		return false;
	}
	
	@Override
	public boolean causesSuffocation(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = playerIn.getHeldItem(hand);
		TileWormholeFormer former = worldIn.getTileEntity(pos) instanceof TileWormholeFormer ? (TileWormholeFormer) worldIn.getTileEntity(pos) : null;
		
		if(former != null && !worldIn.isRemote)
		{
			int si = 0;
			if(hitY > .5)
				si = 0;
			else
				si = 1;
			
			ItemStack slot = former.inventory.getStackInSlot(si);
			if(InterItemStack.isStackNull(slot) && !InterItemStack.isStackNull(stack) && stack.getItem() == ItemsBHS.WORMHOLE_PEARL)
			{
				former.inventory.setInventorySlotContents(si, stack.copy());
				playerIn.setHeldItem(hand, InterItemStack.NULL_STACK);
				
				former.sync();
			} else
			{
				WorldUtil.spawnItemStack(worldIn, playerIn.posX, playerIn.posY, playerIn.posZ, slot);
				former.inventory.setInventorySlotContents(si, InterItemStack.NULL_STACK);
				
				former.sync();
			}
		}
		
		return former != null;
	}
}