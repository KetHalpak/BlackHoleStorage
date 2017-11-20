package com.pengu.holestorage.tile;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.Chunk.EnumCreateEntityType;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.tile.TileSyncable;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.blocks.BlockWormhole;
import com.pengu.holestorage.client.entity.EntityWormhole;

public class TileWormhole extends TileSyncable
{
	private Object boundWormholeEntity = null;
	
	public BlockPos target;
	public int dimension;
	
	public boolean isBound()
	{
		return target != null && !pos.equals(target);
	}
	
	public void unbind()
	{
		if(!world.isRemote && isBound())
		{
			MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();
			WorldServer tw = mc.getWorld(dimension);
			Chunk targetChunk = tw.getChunkProvider().loadChunk(target.getX() >> 4, target.getZ() >> 4);
			TileEntity te = targetChunk.getTileEntity(target, EnumCreateEntityType.CHECK);
			if(te instanceof TileWormhole)
			{
				TileWormhole wormhole = (TileWormhole) te;
				wormhole.dimension = 0;
				wormhole.target = null;
				wormhole.unbind();
				dimension = 0;
				target = null;
			}
		}
		
		world.destroyBlock(pos, false);
	}
	
	public void bind(int dimension, BlockPos target)
	{
		if(!world.isRemote && isBound())
		{
			
			try
			{
				MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();
				WorldServer tw = mc.getWorld(dimension);
				Chunk targetChunk = tw.getChunkProvider().loadChunk(target.getX() >> 4, target.getZ() >> 4);
				TileEntity te = targetChunk.getTileEntity(target, EnumCreateEntityType.CHECK);
				if(te instanceof TileWormhole)
				{
					TileWormhole wormhole = (TileWormhole) te;
					wormhole.dimension = world.provider.getDimension();
					wormhole.target = pos;
					this.dimension = dimension;
					this.target = target;
				}
			} catch(Exception err)
			{
				err.printStackTrace();
			}
		}
	}
	
	public boolean teleport(EntityPlayerMP player)
	{
		if(!isBound() || world.isRemote)
			return false;
		WorldUtil.teleportPlayer(player, dimension, target.getX() + .5, target.getY() + .3, target.getZ() + .5);
		return true;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		if(target != null)
			nbt.setLong("Pos", target.toLong());
		nbt.setInteger("Dim", dimension);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		if(nbt.hasKey("Pos"))
			target = BlockPos.fromLong(nbt.getLong("Pos"));
		dimension = nbt.getInteger("Dim");
	}
	
	@SideOnly(Side.CLIENT)
	public EntityWormhole getWormholeEntity()
	{
		if(boundWormholeEntity == null)
		{
			if(getBlockType() instanceof BlockWormhole)
			{
				EnumFacing facing = world.getBlockState(pos).getValue(InfoBHS.FACING_UDEWSN);
				boundWormholeEntity = new EntityWormhole(world, pos.getX(), pos.getY(), pos.getZ(), facing);
				world.spawnEntity((EntityWormhole) boundWormholeEntity);
			}
		}
		return (EntityWormhole) boundWormholeEntity;
	}
	
	@SideOnly(Side.CLIENT)
	public EntityWormhole getWormholeEntityRemote()
	{
		if(target == null)
			return null;
		TileWormhole te = WorldUtil.cast(world.isBlockLoaded(target) ? world.getTileEntity(target) : null, TileWormhole.class);
		return te != null ? te.getWormholeEntity() : null;
	}
}