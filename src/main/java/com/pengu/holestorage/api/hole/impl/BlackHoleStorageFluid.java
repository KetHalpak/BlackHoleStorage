package com.pengu.holestorage.api.hole.impl;

import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.pengu.holestorage.api.hole.BlackHolePacket;
import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;
import com.pengu.holestorage.api.hole.IBlackHoleStorage;

public class BlackHoleStorageFluid implements IBlackHoleStorage<FluidStack>
{
	private Fluid fluid = null;
	private BigInteger stored = BigInteger.ZERO;
	private NBTTagCompound tags = null;
	
	@Override
	public boolean canHandle(BlackHolePacket pkt)
	{
		if(pkt == null || pkt.type != EnumBlackHolePacketType.FLUID)
			return false;
		return pkt.stored instanceof FluidStack;
	}
	
	public boolean isFluidSupported(FluidStack stack)
	{
		return stack != null && stack.getFluid() == fluid && (stack.tag + "").equals(tags + "");
	}
	
	@Override
	public FluidStack receive(BlackHolePacket<FluidStack, ?> obj, boolean simulate)
	{
		if(obj == null)
			return null;
		if(isFluidSupported(obj.stored) || getStored() == null)
		{
			if(!simulate)
			{
				if(fluid == null)
				{
					fluid = obj.stored.getFluid();
					tags = obj.stored.tag;
				}
				stored = stored.add(new BigInteger(obj.stored.amount + ""));
			}
			return obj.stored.copy();
		}
		return null;
	}
	
	@Override
	public FluidStack send(BlackHolePacket<FluidStack, ?> obj, boolean simulate)
	{
		if(obj == null)
			return null;
		BigInteger extracted = stored.min(new BigInteger(obj.stored.amount + ""));
		
		FluidStack exported = fluid != null ? new FluidStack(fluid, extracted.intValue(), tags != null ? tags.copy() : null) : null;
		
		if(!simulate)
		{
			stored = stored.subtract(extracted);
			if(isEmpty())
			{
				fluid = null;
				tags = null;
			}
		}
		
		return exported;
	}
	
	@Override
	public FluidStack getStored()
	{
		BlackHolePacket<FluidStack, ?> created = create(stored);
		return created != null ? created.stored : null;
	}
	
	@Override
	public void setStored(FluidStack obj)
	{
		fluid = obj.getFluid();
		stored = new BigInteger("" + obj.amount);
		tags = obj.tag;
	}
	
	@Override
	public BlackHolePacket<FluidStack, ?> create(BigInteger number)
	{
		if(fluid == null)
			return null;
		return new BlackHolePacket<FluidStack, Object>(new FluidStack(fluid, number.bitCount() >= 32 ? Integer.MAX_VALUE : number.intValue(), tags), EnumBlackHolePacketType.RF);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("FluidStored", stored != null ? stored.toString() : "0");
		if(fluid != null)
			nbt.setString("Fluid", FluidRegistry.getFluidName(fluid));
		if(tags != null)
			nbt.setTag("FluidNBT", tags);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		stored = new BigInteger(nbt.getString("FluidStored"));
		if(nbt.hasKey("Fluid"))
			fluid = FluidRegistry.getFluid(nbt.getString("Fluid"));
		if(nbt.hasKey("FluidNBT"))
			tags = nbt.getCompoundTag("FluidNBT");
	}
	
	@Override
	public EnumBlackHolePacketType getType()
	{
		return EnumBlackHolePacketType.FLUID;
	}
	
	@Override
	public boolean isEmpty()
	{
		return fluid == null || stored == null || stored.toString().equals("0") || stored.toString().equals("-0" /* is
																												 * this
																												 * the
																												 * case
																												 * with
																												 * BigInts
																												 * ? */);
	}
}