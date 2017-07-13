package com.pengu.holestorage.api.hole.impl;

import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.holestorage.api.hole.BlackHolePacket;
import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;
import com.pengu.holestorage.api.hole.IBlackHoleStorage;

public class BlackHoleStorageRF implements IBlackHoleStorage<BigInteger>
{
	private BigInteger stored = BigInteger.ZERO;
	
	@Override
	public boolean canHandle(BlackHolePacket pkt)
	{
		if(pkt == null || pkt.type != EnumBlackHolePacketType.RF)
			return false;
		return pkt.stored instanceof BigInteger;
	}
	
	@Override
	public BigInteger receive(BlackHolePacket<BigInteger, ?> obj, boolean simulate)
	{
		if(!simulate)
			stored = stored.add(obj.stored);
		return obj.stored;
	}
	
	@Override
	public BigInteger send(BlackHolePacket<BigInteger, ?> obj, boolean simulate)
	{
		BigInteger extracted = stored.min(obj.stored);
		if(!simulate)
			stored = stored.subtract(extracted);
		return extracted;
	}
	
	@Override
	public BigInteger getStored()
	{
		return stored;
	}
	
	@Override
	public void setStored(BigInteger obj)
	{
		stored = obj;
	}
	
	@Override
	public BlackHolePacket<BigInteger, ?> create(BigInteger number)
	{
		return new BlackHolePacket<BigInteger, Object>(number, EnumBlackHolePacketType.RF);
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setString("EnergyStored", stored != null ? stored.toString() : "0");
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		stored = new BigInteger(nbt.getString("EnergyStored"));
	}
	
	@Override
	public EnumBlackHolePacketType getType()
	{
		return EnumBlackHolePacketType.RF;
	}
	
	@Override
	public boolean isEmpty()
	{
		return stored == null || stored.toString().equals("0") || stored.toString().equals("-0" /* is
																								 * this
																								 * the
																								 * case
																								 * with
																								 * BigInts
																								 * ? */);
	}
}