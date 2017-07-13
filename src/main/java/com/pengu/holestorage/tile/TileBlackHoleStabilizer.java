package com.pengu.holestorage.tile;

import java.awt.Color;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.EnumRotation;
import com.pengu.hammercore.common.capabilities.FEEnergyStorage;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.blocks.BlockBlackHoleStabilizer;

public class TileBlackHoleStabilizer extends TileSyncableTickable implements IEnergyStorage
{
	public FEEnergyStorage storage = new FEEnergyStorage(1_000_000, 100_000, 1_000);
	public BlockPos scan = null;
	
	@Override
	public void tick()
	{
		b:
		{
			scan = null;
			int attempts = 32;
			while(!isBlackHoleFound() && attempts-- > 0)
				nextPos();
			
			if(!isBlackHoleFound())
				break b;
			
			TileEntity tile = world.isAreaLoaded(scan, scan) ? world.getTileEntity(scan) : null;
			
			/** fill black hole's shield */
			if(tile instanceof TileBlackHole)
			{
				TileBlackHole hole = (TileBlackHole) tile;
				int maxFill = hole.shieldEnergyMax - hole.shieldEnergy;
				int fill = Math.min(storage.getEnergyStored(), maxFill);
				fill = storage.extractEnergy(fill, false);
				hole.shieldEnergy += fill;
			}
			
			if(ticksExisted % 20 == 0 && !world.isRemote && isBlackHoleFound())
			{
				EnumFacing rot = BlockBlackHoleStabilizer.convert((EnumRotation) world.getBlockState(pos).getValue(EnumRotation.FACING));
				
				if(storage.getEnergyStored() >= storage.getMaxExtract())
				{
					// HammerCore.particleProxy.spawnZap(world, new
					// Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5),
					// new Vec3d(scan.getX() + .5, scan.getY() + .5, scan.getZ()
					// + .5), Color.RED);
					
					int red = 0;
					int green = 254 + world.rand.nextInt(2);
					int blue = 254 + world.rand.nextInt(2);
					
					BlackHoleStorage.proxy.spawnLiquidFX(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, scan.getX() + .5, scan.getY() + .5, scan.getZ() + .5, 200, (red << 16) | (green << 8) | blue, .1F, 3);
				} else
					HammerCore.particleProxy.spawnZap(world, new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5), new Vec3d(pos.offset(rot).getX() + .5, pos.offset(rot).getY() + .5, pos.offset(rot).getZ() + .5), Color.BLUE.getRGB());
			}
		}
	}
	
	public boolean isBlackHoleFound()
	{
		return scan != null && world.isAreaLoaded(scan, scan) && world.getTileEntity(scan) instanceof TileBlackHole;
	}
	
	public void nextPos()
	{
		EnumFacing rot = BlockBlackHoleStabilizer.convert((EnumRotation) world.getBlockState(pos).getValue(EnumRotation.FACING));
		if(scan == null || scan.getDistance(pos.getX(), pos.getY(), pos.getZ()) >= 32D)
			scan = pos.offset(rot);
		else
			scan = scan.offset(rot);
		
		boolean yellow = scan != null;
		BlockPos s = scan;
		
		if(scan != null && world.isAreaLoaded(scan, scan) && !world.isAirBlock(scan) && !isBlackHoleFound())
			scan = null;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		storage.writeToNBT(nbt);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		storage.readFromNBT(nbt);
	}
	
	@Override
	public int getEnergyStored()
	{
		return storage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return storage.getMaxEnergyStored();
	}
	
	@Override
	public boolean canExtract()
	{
		return false;
	}
	
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		return storage.receiveEnergy(maxReceive, simulate);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
}