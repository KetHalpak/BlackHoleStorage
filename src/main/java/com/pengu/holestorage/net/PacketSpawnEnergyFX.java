package com.pengu.holestorage.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.holestorage.BlackHoleStorage;

public class PacketSpawnEnergyFX implements IPacket, IPacketListener<PacketSpawnEnergyFX, IPacket>
{
	private int dim, rf;
	private double x, y, z, tx, ty, tz;
	
	public PacketSpawnEnergyFX()
	{
	}
	
	public PacketSpawnEnergyFX(int dim, double x, double y, double z, double tx, double ty, double tz, int rf)
	{
		this.dim = dim;
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.rf = rf;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("p1", dim);
		nbt.setDouble("p2", x);
		nbt.setDouble("p3", y);
		nbt.setDouble("p4", z);
		nbt.setDouble("p5", tx);
		nbt.setDouble("p6", ty);
		nbt.setDouble("p7", tz);
		nbt.setInteger("p8", rf);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		dim = nbt.getInteger("p1");
		x = nbt.getDouble("p2");
		y = nbt.getDouble("p3");
		z = nbt.getDouble("p4");
		tx = nbt.getDouble("p5");
		ty = nbt.getDouble("p6");
		tz = nbt.getDouble("p7");
		rf = nbt.getInteger("p8");
	}
	
	@Override
	public IPacket onArrived(PacketSpawnEnergyFX packet, MessageContext context)
	{
		BlackHoleStorage.proxy.spawnEnergyFX(WorldUtil.getWorld(context, dim), packet.x, packet.y, packet.z, packet.tx, packet.ty, packet.tz, packet.rf);
		return null;
	}
}