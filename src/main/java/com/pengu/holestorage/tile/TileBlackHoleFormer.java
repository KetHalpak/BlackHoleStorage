package com.pengu.holestorage.tile;

import java.math.BigDecimal;
import java.util.List;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.common.utils.BigIntegerUtils;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.hammercore.tile.tooltip.iTooltipTile;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.init.BlocksBHS;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileBlackHoleFormer extends TileSyncableTickable implements IEnergyStorage, iTooltipTile
{
	public static BigDecimal ABSORBED = new BigDecimal(32_000_000_000D);
	public BigDecimal EnergyStored = new BigDecimal(0);
	
	@Override
	public void tick()
	{
		if(EnergyStored.max(ABSORBED).equals(EnergyStored) && !world.isRemote)
		{
			world.setBlockState(pos.up(), BlocksBHS.BLACK_HOLE.getDefaultState());
			getLocation().destroyBlock(false);
			getLocation().playSound(InfoBHS.MOD_ID + ":black_hole_form", 4F, 1F, SoundCategory.BLOCKS);
			EnergyStored = BigDecimal.ZERO;
		}
	}
	
	@Override
	public int getEnergyStored()
	{
		return BigIntegerUtils.isInt(EnergyStored.toBigInteger()) ? EnergyStored.intValue() : Integer.MAX_VALUE;
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
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		BigDecimal toConsume = ABSORBED.subtract(EnergyStored).min(new BigDecimal(maxReceive));
		if(!simulate)
		{
			EnergyStored = EnergyStored.add(toConsume);
			sync();
		}
		return toConsume.intValue();
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setString("EnergyStored", EnergyStored.toString());
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		EnergyStored = new BigDecimal(nbt.getString("EnergyStored"));
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

	@Override
	public void getTextTooltip(List<String> list, EntityPlayer player) {
		double progress = EnergyStored.divide(ABSORBED).doubleValue() * 100D;
		list.add(I18n.format("gui." + InfoBHS.MOD_ID + ":progress") + ": " + String.format("%.002f", progress) + "%");
	}
}