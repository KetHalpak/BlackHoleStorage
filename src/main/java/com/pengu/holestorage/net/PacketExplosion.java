package com.pengu.holestorage.net;

import com.pengu.hammercore.net.packetAPI.iPacket;
import com.pengu.hammercore.net.packetAPI.iPacketListener;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.holestorage.client.particle.ParticleExplosion;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketExplosion implements iPacket, iPacketListener<PacketExplosion, iPacket>
{
	public long pos;
	
	public PacketExplosion(long pos)
	{
		this.pos = pos;
	}
	
	public PacketExplosion()
	{
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong("p", pos);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		pos = nbt.getLong("p");
	}
	
	@Override
	public iPacket onArrived(PacketExplosion packet, MessageContext context)
	{
		if(context.side == Side.CLIENT)
			packet.client();
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public void client()
	{
		BlockPos pos = BlockPos.fromLong(this.pos);
		new Thread(() ->
		{
			try
			{
				Thread.sleep(50L);
			} catch(InterruptedException e)
			{
				e.printStackTrace();
			}
			ParticleProxy_Client.queueParticleSpawn(new ParticleExplosion(Minecraft.getMinecraft().world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5));
		}).start();
	}
}