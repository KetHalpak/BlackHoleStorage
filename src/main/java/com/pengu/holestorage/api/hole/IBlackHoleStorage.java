package com.pengu.holestorage.api.hole;

import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;

import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;

public interface IBlackHoleStorage<H>
{
	public boolean canHandle(BlackHolePacket pkt);
	
	public H receive(BlackHolePacket<H, ?> obj, boolean simulate);
	
	public H send(BlackHolePacket<H, ?> obj, boolean simulate);
	
	public H getStored();
	
	public void setStored(H obj);
	
	public BlackHolePacket<H, ?> create(BigInteger number);
	
	public EnumBlackHolePacketType getType();
	
	public boolean isEmpty();
	
	public void writeToNBT(NBTTagCompound nbt);
	
	public void readFromNBT(NBTTagCompound nbt);
}