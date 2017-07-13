package com.pengu.holestorage.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.holestorage.tile.TileAtomicTransformer;

public class BlockAtomicTransformer extends Block implements ITileEntityProvider
{
	public BlockAtomicTransformer()
	{
		super(Material.IRON);
		setHardness(5F);
		setResistance(50F);
		setHarvestLevel("pickaxe", 2);
		setUnlocalizedName("atomic_transformer");
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileAtomicTransformer();
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileAtomicTransformer at = WorldUtil.cast(worldIn.getTileEntity(pos), TileAtomicTransformer.class);
		if(at != null)
			at.tryOpenGui(playerIn, worldIn);
		return true;
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
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
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileAtomicTransformer at = WorldUtil.cast(worldIn.getTileEntity(pos), TileAtomicTransformer.class);
		if(at != null)
			at.inventory.drop(worldIn, pos);
		super.breakBlock(worldIn, pos, state);
	}
}