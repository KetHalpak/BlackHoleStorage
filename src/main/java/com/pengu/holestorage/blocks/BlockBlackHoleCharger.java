package com.pengu.holestorage.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.common.EnumRotation;
import com.pengu.holestorage.tile.TileBlackHoleStabilizer;

public class BlockBlackHoleCharger extends Block implements ITileEntityProvider
{
	public BlockBlackHoleCharger()
	{
		super(Material.IRON);
		setUnlocalizedName("black_hole_charger");
		setHardness(4);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileBlackHoleStabilizer();
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
	
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(EnumRotation.FACING, EnumRotation.values()[meta]);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		int meta = ((EnumRotation) state.getValue(EnumRotation.FACING)).ordinal();
		return meta;
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, getProperties());
	}
	
	public IProperty[] getProperties()
	{
		return new IProperty[] { EnumRotation.FACING };
	}
	
	public static EnumFacing convert(EnumRotation rot)
	{
		return rot == null ? null : rot == EnumRotation.EAST ? EnumFacing.EAST : rot == EnumRotation.NORTH ? EnumFacing.NORTH : rot == EnumRotation.SOUTH ? EnumFacing.SOUTH : rot == EnumRotation.WEST ? EnumFacing.WEST : null;
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack)
	{
		int l = MathHelper.floor((double) (ent.rotationYaw * 4F / 360F) + 0.5D) & 3;
		int meta = 0;
		if(l == 0)
			meta = 1;
		if(l == 1)
			meta = 2;
		if(l == 2)
			meta = 0;
		if(l == 3)
			meta = 3;
		world.setBlockState(pos, state = getStateFromMeta(meta));
		world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
	}
	
	public static final AxisAlignedBB EAST_BB = new AxisAlignedBB(0, 0, 0, .5, 1, 1), WEST_BB = new AxisAlignedBB(.5, 0, 0, 1, 1, 1), SOUTH_BB = new AxisAlignedBB(0, 0, 0, 1, 1, .5), NORTH_BB = new AxisAlignedBB(0, 0, .5, 1, 1, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumRotation rot = (EnumRotation) state.getValue(EnumRotation.FACING);
		
		if(rot == EnumRotation.EAST)
			return EAST_BB;
		if(rot == EnumRotation.WEST)
			return WEST_BB;
		if(rot == EnumRotation.SOUTH)
			return SOUTH_BB;
		if(rot == EnumRotation.NORTH)
			return NORTH_BB;
		
		return super.getBoundingBox(state, source, pos);
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
}