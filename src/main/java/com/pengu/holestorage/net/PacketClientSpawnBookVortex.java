package com.pengu.holestorage.net;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.net.packetAPI.IPacket;
import com.pengu.hammercore.net.packetAPI.IPacketListener;
import com.pengu.holestorage.bookmaking.BookMakeHandler;

public class PacketClientSpawnBookVortex implements IPacket, IPacketListener<PacketClientSpawnBookVortex, IPacket>
{
	BlockPos pos;
	int dim;
	
	public PacketClientSpawnBookVortex()
	{
	}
	
	public PacketClientSpawnBookVortex(BlockPos pos, int dim)
	{
		this.pos = pos;
		this.dim = dim;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("d", dim);
		nbt.setLong("p", pos.toLong());
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		dim = nbt.getInteger("d");
		pos = BlockPos.fromLong(nbt.getLong("p"));
	}
	
	public IPacket onArrived(PacketClientSpawnBookVortex packet, net.minecraftforge.fml.common.network.simpleimpl.MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.handle();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	private void handle()
	{
		BookMakeHandler.startCrafting_client(pos, dim);
	}
}