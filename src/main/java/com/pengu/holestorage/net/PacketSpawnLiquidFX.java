package com.pengu.holestorage.net;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.holestorage.BlackHoleStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpawnLiquidFX implements iPacket, iPacketListener<PacketSpawnLiquidFX, iPacket>
{
	int dim;
	double x;
	double y;
	double z;
	double tx;
	double ty;
	double tz;
	int count;
	int color;
	float scale;
	int extend;
	
	public PacketSpawnLiquidFX()
	{
	}
	
	public PacketSpawnLiquidFX(int dim, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, int extend)
	{
		this.dim = dim;
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.count = count;
		this.scale = scale;
		this.color = color;
		this.extend = extend;
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
		nbt.setInteger("p8", count);
		nbt.setInteger("p9", color);
		nbt.setFloat("p10", scale);
		nbt.setInteger("p11", extend);
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
		count = nbt.getInteger("p8");
		color = nbt.getInteger("p9");
		scale = nbt.getFloat("p10");
		extend = nbt.getInteger("p11");
	}
	
	@Override
	public iPacket onArrived(PacketSpawnLiquidFX packet, MessageContext context)
	{
		packet.handle(context);
		return null;
	}
	
	private void handle(MessageContext ctx)
	{
		BlackHoleStorage.proxy.spawnLiquidFX(WorldUtil.getWorld(ctx, dim), x, y, z, tx, ty, tz, count, color, scale, extend);
	}
}