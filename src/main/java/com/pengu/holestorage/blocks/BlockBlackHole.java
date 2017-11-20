package com.pengu.holestorage.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.holestorage.client.shaders.BHSShaders;
import com.pengu.holestorage.tile.TileBlackHole;

public class BlockBlackHole extends Block implements ITileEntityProvider
{
	public BlockBlackHole()
	{
		super(Material.IRON);
		setUnlocalizedName("black_hole");
		setHardness(-1);
		setResistance(Float.MAX_VALUE);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
	}
	
	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		TileBlackHole tile = WorldUtil.cast(worldIn.getTileEntity(pos), TileBlackHole.class);
		if(tile == null)
			return;
		
		double eventHorizon = Math.sqrt(16 * Math.sqrt(tile.additionalMass / 16D));
		
		double x = pos.getX() + (rand.nextDouble() - rand.nextDouble()) * eventHorizon;
		double y = pos.getY() + (rand.nextDouble() - rand.nextDouble()) * eventHorizon;
		double z = pos.getZ() + (rand.nextDouble() - rand.nextDouble()) * eventHorizon;
		
		double mx = (pos.getX() - x + .5) / 8D;
		double my = (pos.getY() - y + .5) / 8D;
		double mz = (pos.getZ() - z + .5) / 8D;
		
		worldIn.spawnParticle(EnumParticleTypes.END_ROD, x, y, z, mx, my, mz);
	}
	
	@Override
	public EnumBlockRenderType getRenderType(IBlockState state)
	{
		return net.minecraft.client.renderer.OpenGlHelper.shadersSupported && com.pengu.holestorage.configs.BHSConfigs.client_useShaders ? EnumBlockRenderType.ENTITYBLOCK_ANIMATED : EnumBlockRenderType.MODEL;
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileBlackHole();
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
	public boolean isFullBlock(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, Entity entityIn, boolean p_185477_7_)
	{
	}
	
	@Override
	public void onEntityCollidedWithBlock(World world, BlockPos pos, IBlockState state, Entity e)
	{
	}
	
	public static final AxisAlignedBB aabb = new AxisAlignedBB(.5, .5, .5, .5, .5, .5);
	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		return aabb;
	}
}