package com.pengu.holestorage.net;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.holestorage.BlackHoleStorage;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSpawnLiquidTexturedFX implements iPacket, iPacketListener<PacketSpawnLiquidTexturedFX, iPacket>
{
	int dim;
	double x;
	double y;
	double z;
	double tx;
	double ty;
	double tz;
	int count;
	FluidStack stack;
	float scale;
	int extend;
	
	public PacketSpawnLiquidTexturedFX()
	{
	}
	
	public PacketSpawnLiquidTexturedFX(int dim, double x, double y, double z, double tx, double ty, double tz, int count, FluidStack stack, float scale, int extend)
	{
		this.dim = dim;
		this.x = x;
		this.y = y;
		this.z = z;
		this.tx = tx;
		this.ty = ty;
		this.tz = tz;
		this.count = count;
		this.stack = stack;
		this.scale = scale;
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
		nbt.setTag("p9", stack.writeToNBT(new NBTTagCompound()));
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
		stack = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag("p9"));
		scale = nbt.getFloat("p10");
		extend = nbt.getInteger("p11");
	}
	
	@Override
	public iPacket onArrived(PacketSpawnLiquidTexturedFX packet, MessageContext context)
	{
		packet.handle(context);
		return null;
	}
	
	private void handle(MessageContext ctx)
	{
		BlackHoleStorage.proxy.spawnLiquidTexturedFX(WorldUtil.getWorld(ctx, dim), x, y, z, tx, ty, tz, count, stack, scale, extend);
	}
}