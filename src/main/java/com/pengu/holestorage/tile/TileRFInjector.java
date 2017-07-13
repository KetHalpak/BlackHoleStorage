package com.pengu.holestorage.tile;

import java.awt.Color;
import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.api.hole.BlackHolePacket;
import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;
import com.pengu.holestorage.api.hole.impl.BlackHoleStorageRF;
import com.pengu.holestorage.blocks.BlockRFInjector;
import com.pengu.holestorage.init.BlocksBHS;

public class TileRFInjector extends TileSyncableTickable implements IEnergyStorage
{
	public BlockPos scan;
	private int lastParticleSize = 0;
	
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
				if(lastParticleSize > 0 && !world.isRemote)
				{
					double xf = (world.rand.nextDouble() - world.rand.nextDouble()) * .5;
					double yf = (world.rand.nextDouble() - world.rand.nextDouble()) * .5;
					double zf = (world.rand.nextDouble() - world.rand.nextDouble()) * .5;
					
					if(world.rand.nextInt(100) < 25)
					{
						BlackHoleStorage.proxy.spawnEnergyFX(world, pos.getX() + .5 + xf, pos.getY() + .5 + yf, pos.getZ() + .5 + zf, tpos.getX() + .5, tpos.getY() + .5, tpos.getZ() + .5, lastParticleSize);
						lastParticleSize = 0;
					}
				} else if(lastParticleSize < 0)
					lastParticleSize = 0;
			}
	}
	
	public boolean isBlackHoleFound()
	{
		return scan != null && world.isAreaLoaded(scan, scan) && world.getTileEntity(scan) instanceof TileBlackHole;
	}
	
	public boolean isValidState()
	{
		return world.getBlockState(pos).getBlock() instanceof BlockRFInjector;
	}
	
	public void nextPos(boolean fxyellow, boolean fxgreen)
	{
		if(isValidState())
		{
			EnumFacing rot = world.getBlockState(pos).getValue(BlockRFInjector.FACING);
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
	
	@Override
	public int getEnergyStored()
	{
		return 0;
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return Integer.MAX_VALUE;
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
		if(scan == null)
			return 0;
		TileEntity tile = world.isAreaLoaded(scan, scan) ? world.getTileEntity(scan) : null;
		if(tile instanceof TileBlackHole)
		{
			TileBlackHole hole = (TileBlackHole) tile;
			EnumBlackHolePacketType pkt = hole.getType();
			if((pkt == EnumBlackHolePacketType.RF && hole.getStorage() instanceof BlackHoleStorageRF) || pkt == null)
			{
				Object ret = hole.receiveContent(new BlackHolePacket<BigInteger, Object>(new BigInteger(maxReceive + ""), EnumBlackHolePacketType.RF), simulate);
				if(ret instanceof BigInteger)
				{
					lastParticleSize += ((BigInteger) ret).intValue();
					return ((BigInteger) ret).intValue();
				}
			}
		}
		return 0;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(world.getBlockState(pos).getBlock() != BlocksBHS.RF_INJECTOR)
			return false;
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(world.getBlockState(pos).getBlock() != BlocksBHS.RF_INJECTOR)
			return null;
		if(capability == CapabilityEnergy.ENERGY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
}