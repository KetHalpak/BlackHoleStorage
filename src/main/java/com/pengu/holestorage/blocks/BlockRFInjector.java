package com.pengu.holestorage.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
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

import com.pengu.holestorage.Info;
import com.pengu.holestorage.tile.TileRFInjector;

public final class BlockRFInjector extends Block implements ITileEntityProvider
{
	public static final PropertyEnum<EnumFacing> FACING = Info.FACING_UD;
	
	public BlockRFInjector()
	{
		super(Material.IRON);
		setUnlocalizedName("rf_injector");
		setHardness(4);
		setHarvestLevel("pickaxe", 2);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileRFInjector();
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
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta]);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		int meta = state.getValue(FACING).ordinal();
		return meta;
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, getProperties());
	}
	
	public IProperty[] getProperties()
	{
		return new IProperty[] { FACING };
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack)
	{
		int l = MathHelper.floor((double) (ent.rotationYaw * 4F / 360F) + 0.5D) & 3;
		int meta = 0;
		
		float yRot = ent.rotationPitch;
		
		if(yRot >= 0)
			meta = 0;
		else
			meta = 1;
		
		world.setBlockState(pos, state = getStateFromMeta(meta));
		world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
	}
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing rot = state.getValue(FACING);
		
		if(rot == EnumFacing.UP)
			return new AxisAlignedBB(0, 0, 0, 1, .7, 1);
		if(rot == EnumFacing.DOWN)
			return new AxisAlignedBB(0, .3, 0, 1, 1, 1);
		
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