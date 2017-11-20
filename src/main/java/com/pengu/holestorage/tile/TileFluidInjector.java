package com.pengu.holestorage.tile;

import java.awt.Color;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.hole.BlackHolePacket;
import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;
import com.pengu.holestorage.api.hole.impl.BlackHoleStorageFluid;
import com.pengu.holestorage.blocks.BlockFluidInjector;

public class TileFluidInjector extends TileSyncableTickable implements IFluidHandler, IFluidTankProperties
{
	public BlockPos scan;
	private FluidStack lastParticleSize;
	
	@Override
	public void tick()
	{
		if(!isValidState())
			world.removeTileEntity(pos);
		else
			b:
			{
				if(!isBlackHoleFound())
				{
					nextPos(false, true);
					if(!isBlackHoleFound())
						break b;
				}
				
				if(ticksExisted % 80 == 0)
					for(int i = 0; i < 32 && !isBlackHoleFound(); ++i)
						nextPos(false, false);
				ticksExisted++;
				
				if(scan == null)
					break b;
				TileBlackHole tile = (TileBlackHole) world.getTileEntity(scan);
				if(tile == null)
					break b;
				
				BlockPos tpos = tile.getPos();
				if(lastParticleSize != null && !world.isRemote && world.rand.nextInt(16) < 1)
				{
					boolean upsideDown = world.getBlockState(pos).getValue(InfoBHS.FACING_UD) == EnumFacing.DOWN;
					
					double fuzzMax = Math.sqrt(tile.eventHorizon) / 6D;
					
					double fx = (world.rand.nextGaussian() - world.rand.nextGaussian()) * fuzzMax;
					double fy = -world.rand.nextGaussian() * fuzzMax;
					double fz = (world.rand.nextGaussian() - world.rand.nextGaussian()) * fuzzMax;
					
					BlackHoleStorage.proxy.spawnLiquidTexturedFX(world, pos.getX() + .5, pos.getY() + (upsideDown ? .75 : .55), pos.getZ() + .5, tpos.getX() + .5 + fx, tpos.getY() + .5 + fy, tpos.getZ() + .5 + fz, 1, lastParticleSize, .1F, (int) MathHelper.clip(Math.sqrt(Math.sqrt(lastParticleSize.amount)) * 2.4, 1, 40));
					lastParticleSize = null;
				}
			}
	}
	
	public boolean isBlackHoleFound()
	{
		return scan != null && world.isAreaLoaded(scan, scan) && world.getTileEntity(scan) instanceof TileBlackHole;
	}
	
	public boolean isValidState()
	{
		return world.getBlockState(pos).getBlock() instanceof BlockFluidInjector;
	}
	
	public void nextPos(boolean fxyellow, boolean fxgreen)
	{
		if(isValidState())
		{
			EnumFacing rot = world.getBlockState(pos).getValue(InfoBHS.FACING_UD);
			if(scan == null || scan.getDistance(pos.getX(), pos.getY(), pos.getZ()) >= 32D)
				scan = pos.offset(rot);
			else
				scan = scan.offset(rot);
			
			boolean yellow = scan != null;
			BlockPos s = scan;
			
			if(scan != null && world.isAreaLoaded(scan, scan) && !world.isAirBlock(scan) && !isBlackHoleFound())
				scan = null;
			
			if(scan != null && isBlackHoleFound() && fxgreen)
				HammerCore.particleProxy.spawnZap(world, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(scan.getX() + .5, scan.getY() + .5, scan.getZ() + .5), Color.GREEN.getRGB());
			else if(yellow && fxyellow)
				HammerCore.particleProxy.spawnZap(world, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(s.getX() + .5, s.getY() + .5, s.getZ() + .5), Color.YELLOW.getRGB());
		}
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		if(scan != null)
			nbt.setLong("Scan", scan.toLong());
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		scan = BlockPos.fromLong(nbt.getLong("Scan"));
	}
	
	protected FluidStack fillFluid(FluidStack insert, boolean simulate)
	{
		if(scan == null)
			return null;
		TileEntity tile = world.isAreaLoaded(scan, scan) ? world.getTileEntity(scan) : null;
		if(tile instanceof TileBlackHole)
		{
			TileBlackHole hole = (TileBlackHole) tile;
			EnumBlackHolePacketType pkt = hole.getType();
			if((pkt == EnumBlackHolePacketType.FLUID && hole.getStorage() instanceof BlackHoleStorageFluid) || pkt == null)
			{
				BlackHoleStorageFluid fs = (BlackHoleStorageFluid) hole.getStorage();
				Object ret = hole.receiveContent(new BlackHolePacket(insert, EnumBlackHolePacketType.FLUID), simulate);
				if(ret instanceof FluidStack)
				{
					FluidStack fs0 = (FluidStack) ret;
					if(!simulate)
					{
						if(lastParticleSize == null)
							lastParticleSize = fs0.copy();
						else
							lastParticleSize.amount += fs0.amount;
					}
					return fs0;
				}
			}
		}
		return null;
	}
	
	@Override
	public FluidStack getContents()
	{
		if(scan == null)
			return null;
		TileEntity tile = world.isAreaLoaded(scan, scan) ? world.getTileEntity(scan) : null;
		if(tile instanceof TileBlackHole)
		{
			TileBlackHole hole = (TileBlackHole) tile;
			EnumBlackHolePacketType pkt = hole.getType();
			if((pkt == EnumBlackHolePacketType.FLUID && hole.getStorage() instanceof BlackHoleStorageFluid) || pkt == null)
			{
				BlackHoleStorageFluid fs = (BlackHoleStorageFluid) hole.getStorage();
				return fs.getStored();
			}
		}
		return null;
	}
	
	@Override
	public int getCapacity()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public boolean canFill()
	{
		return true;
	}
	
	@Override
	public boolean canFillFluidType(FluidStack fluidStack)
	{
		return false;
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		FluidStack fs = fillFluid(resource, !doFill);
		return fs != null ? fs.amount : 0;
	}
	
	@Override
	public IFluidTankProperties[] getTankProperties()
	{
		return new IFluidTankProperties[] { this };
	}
	
	@Override
	public boolean canDrain()
	{
		return false;
	}
	
	@Override
	public boolean canDrainFluidType(FluidStack fluidStack)
	{
		return false;
	}
	
	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		return null;
	}
	
	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		return null;
	}
}