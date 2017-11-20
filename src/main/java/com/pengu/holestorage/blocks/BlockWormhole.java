package com.pengu.holestorage.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.tile.TileWormhole;

public class BlockWormhole extends Block implements ITileEntityProvider
{
	public BlockWormhole()
	{
		super(Material.IRON);
		setUnlocalizedName("wormhole");
		setHardness(-1);
		setResistance(Float.MAX_VALUE);
	}
	
	@Override
	public Block setCreativeTab(CreativeTabs tab)
	{
		return this;
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		TileWormhole wormhole = worldIn.getTileEntity(pos) instanceof TileWormhole ? (TileWormhole) worldIn.getTileEntity(pos) : null;
		if(!worldIn.isRemote && playerIn instanceof EntityPlayerMP && wormhole != null && wormhole.isBound())
			return wormhole.teleport((EntityPlayerMP) playerIn);
		return false;
	}
	
	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
	{
		if(entityIn instanceof EntityItem)
		{
			ItemStack stack = ((EntityItem) entityIn).getItem();
			if(stack.getItem() == ItemsBHS.ANTI_MATTER)
			{
				InterItemStack.setStackSize(stack, InterItemStack.getStackSize(stack) - 1);
				TileWormhole wormhole = worldIn.getTileEntity(pos) instanceof TileWormhole ? (TileWormhole) worldIn.getTileEntity(pos) : null;
				if(wormhole != null)
					wormhole.unbind();
			}
			if(stack.isEmpty())
				entityIn.setDead();
		}
	}
	
	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		TileWormhole wormhole = worldIn.getTileEntity(pos) instanceof TileWormhole ? (TileWormhole) worldIn.getTileEntity(pos) : null;
		if(wormhole != null)
			try
			{
				wormhole.unbind();
			} catch(Throwable err)
			{
				err.printStackTrace();
			}
		super.breakBlock(worldIn, pos, state);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileWormhole();
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
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list)
	{
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
	
	public IBlockState getStateFromMeta(int meta)
	{
		return getDefaultState().withProperty(InfoBHS.FACING_UDEWSN, EnumFacing.values()[meta]);
	}
	
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(InfoBHS.FACING_UDEWSN).ordinal();
	}
	
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, InfoBHS.FACING_UDEWSN);
	}
	
	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase ent, ItemStack stack)
	{
		int meta = EnumFacing.getDirectionFromEntityLiving(pos, ent).ordinal();
		world.setBlockState(pos, state = getStateFromMeta(meta));
		world.markAndNotifyBlock(pos, world.getChunkFromBlockCoords(pos), state, state, 3);
	}
	
	public static final AxisAlignedBB UP_BB = new AxisAlignedBB(0, 0, 0, 1, .3, 1), DOWN_BB = new AxisAlignedBB(0, .7, 0, 1, 1, 1), EAST_BB = new AxisAlignedBB(0, 0, 0, .3, 1, 1), WEST_BB = new AxisAlignedBB(.7, 0, 0, 1, 1, 1), SOUTH_BB = new AxisAlignedBB(0, 0, 0, 1, 1, .3), NORTH_BB = new AxisAlignedBB(0, 0, .7, 1, 1, 1);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing rot = state.getValue(InfoBHS.FACING_UDEWSN);
		
		if(rot == EnumFacing.UP)
			return UP_BB;
		if(rot == EnumFacing.DOWN)
			return DOWN_BB;
		if(rot == EnumFacing.EAST)
			return EAST_BB;
		if(rot == EnumFacing.WEST)
			return WEST_BB;
		if(rot == EnumFacing.SOUTH)
			return SOUTH_BB;
		if(rot == EnumFacing.NORTH)
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