package com.pengu.holestorage.tile;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.api.explosion.CustomExplosion;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.hole.BlackHolePacket;
import com.pengu.holestorage.api.hole.BlackHolePacket.EnumBlackHolePacketType;
import com.pengu.holestorage.api.hole.IBlackHole;
import com.pengu.holestorage.api.hole.IBlackHoleStorage;
import com.pengu.holestorage.configs.BHSConfigs;
import com.pengu.holestorage.init.DamageSourcesBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.intr.lostthaumaturgy.LTBHS;
import com.pengu.holestorage.net.PacketExplosion;
import com.pengu.holestorage.vortex.BlackHoleVortex;

public class TileBlackHole extends TileSyncableTickable implements IBlackHole
{
	private final BlackHoleVortex vortex = new BlackHoleVortex(this);
	public Random random = new Random();
	public boolean canPull = true;
	public double radius = 16;
	public double instabillity = 1;
	public IBlackHoleStorage storage;
	public BlockPos chosen, lastChosen;
	public int chosenTicks = 0;
	
	/**
	 * This is the minimal distance that any entity can get close to a black
	 * hole, any closer would make them die.
	 */
	public double eventHorizon = 4;
	
	public double additionalMass = 0;
	public int shieldEnergy = 0, shieldEnergyMax = 1_000_000;
	
	public float currentShieldLevel, targetShieldLevel;
	
	@Override
	public void tick()
	{
		BlackHoleStorage.proxy.addParticleVortex(vortex);
		
		if((ticksExisted - 24) % 100 == 0 && !world.isRemote)
			HammerCore.audioProxy.playSoundAt(world, InfoBHS.MOD_ID + ":black_hole_loop", pos, 2F + (float) Math.sqrt(radius), 1F, SoundCategory.BLOCKS);
		
		int maxTake = Math.min(shieldEnergy, (int) (200 + 16 * Math.sqrt(Math.sqrt(additionalMass / 16D))));
		shieldEnergy -= maxTake;
		
		/* calculate instabillity based on shield energy */
		if(shieldEnergy > 16000)
			instabillity = 0;
		else
			instabillity = 1 - shieldEnergy / 16000D;
		
		radius = instabillity * 16 + Math.sqrt(instabillity * (16 * Math.sqrt(additionalMass / 16D)));
		
		canPull = instabillity > 0D && ticksExisted > 20;
		
		currentShieldLevel = (float) shieldEnergy / (float) shieldEnergyMax;
		
		eventHorizon = 4D + Math.sqrt(additionalMass) * 2;
		
		if(world.isRemote)
			return;
		
		if(atTickRate(20))
			sync();
		
		if(canPull)
		{
			if(chosen != lastChosen)
				chosenTicks = 0;
			
			chosen = lastChosen;
			
			if(!canPull(chosen))
			{
				BlockPos newChosen = choosePositionToPull();
				if(canPull(newChosen))
				{
					chosen = newChosen;
					lastChosen = newChosen;
					chosenTicks = 0;
				}
			} else if(chosenTicks >= 1)
			{
				IBlockState state = world.getBlockState(chosen);
				world.setBlockToAir(chosen);
				float hardness = state.getBlock().getBlockHardness(state, world, chosen);
				EntityFallingBlock block = new EntityFallingBlock(world, chosen.getX() + .5, chosen.getY(), chosen.getZ() + .5, state);
				block.fallTime = -100;
				block.setNoGravity(true);
				block.noClip = true;
				world.spawnEntity(block);
				chosen = null;
			}
			
			chosenTicks++;
		}
		
		double push = 1.5 - instabillity;
		push *= radius;
		if(push != 0D && canPull)
		{
			List<Entity> ents = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.getX() - radius, pos.getY() - radius, pos.getZ() - radius, pos.getX() + radius, pos.getY() + radius, pos.getZ() + radius));
			for(Entity e : ents)
			{
				if(e instanceof EntityPlayer && ((EntityPlayer) e).capabilities.isCreativeMode)
					continue;
				if(e.getDistanceSq(pos) < eventHorizon && !e.isDead)
				{
					double volume = 0;
					AxisAlignedBB aabb = e.getEntityBoundingBox();
					volume = (aabb.maxX - aabb.minX) * (aabb.maxY - aabb.minY) * (aabb.maxZ - aabb.minZ);
					
					additionalMass += volume;
					sync();
					
					if(e instanceof EntityLivingBase)
						e.attackEntityFrom(DamageSourcesBHS.BLACK_HOLE, 10000000F);
					else
					{
						if(e instanceof EntityItem)
						{
							EntityItem ei = (EntityItem) e;
							ItemStack stack = ei.getItem();
							if(stack.getItem() == ItemsBHS.ANTI_MATTER)
							{
								world.setBlockToAir(pos);
								HammerCore.audioProxy.playSoundAt(world, InfoBHS.MOD_ID + ":black_hole_implode", pos, 256F, 1F, SoundCategory.BLOCKS);
								CustomExplosion.doExplosionAt(world, pos, (2 + (radius > 16 ? (float) Math.sqrt(radius - 16) * 2F : 0)) * BHSConfigs.blackHole_explosionMultiplier, DamageSourcesBHS.BLACK_HOLE);
								HCNetwork.manager.sendToAllAround(new PacketExplosion(getPos().toLong()), getSyncPoint(256));
								LTBHS.addRadiation(world, pos, .001F * (float) eventHorizon);
							}
						}
						
						e.setDead();
					}
				}
				
				if(e instanceof EntityPlayer)
					;
				else
					e.setNoGravity(true);
				
				e.move(MoverType.PISTON, (pos.getX() - e.posX + .5) / push, (pos.getY() - e.posY) / 2D, (pos.getZ() - e.posZ + .5) / push);
			}
		}
	}
	
	public BlockPos choosePositionToPull()
	{
		if(radius <= 0D)
			return null;
		for(int tries = 0; tries < 8; ++tries)
			try
			// 8 times smarter!
			{
				BlockPos pos = this.pos.add(random.nextInt((int) radius) - random.nextInt((int) radius), random.nextInt((int) radius) - random.nextInt((int) radius), random.nextInt((int) radius) - random.nextInt((int) radius));
				if(canPull(pos))
					return pos;
			} catch(Throwable err)
			{
			}
		return null;
	}
	
	public boolean canPull(BlockPos pos)
	{
		return pos != null && !pos.equals(this.pos) && world.isAreaLoaded(pos, pos) && !world.isAirBlock(pos);
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		NBTTagCompound s = new NBTTagCompound();
		if(storage != null)
		{
			storage.writeToNBT(s);
			s.setString("HoleType", storage.getType().name());
		}
		nbt.setTag("Storage", s);
		nbt.setDouble("Instabillity", instabillity);
		nbt.setDouble("Radius", radius);
		nbt.setBoolean("CanPull", canPull);
		if(chosen != null)
			nbt.setLong("Chosen", chosen.toLong());
		if(lastChosen != null)
			nbt.setLong("LastChosen", lastChosen.toLong());
		nbt.setInteger("ChosenTicks", chosenTicks);
		nbt.setInteger("ShieldEnergy", shieldEnergy);
		nbt.setDouble("AdditionalMass", additionalMass);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		instabillity = nbt.getDouble("Instabillity");
		radius = nbt.getDouble("Radius");
		canPull = nbt.getBoolean("CanPull");
		chosenTicks = nbt.getInteger("ChosenTicks");
		chosen = BlockPos.fromLong(nbt.getLong("Chosen"));
		lastChosen = BlockPos.fromLong(nbt.getLong("LastChosen"));
		shieldEnergy = nbt.getInteger("ShieldEnergy");
		additionalMass = nbt.getDouble("AdditionalMass");
		
		NBTTagCompound s = nbt.getCompoundTag("Storage");
		if(s.hasKey("HoleType"))
		{
			EnumBlackHolePacketType type = EnumBlackHolePacketType.valueOf(s.getString("HoleType"));
			if(type != null)
			{
				storage = type.createStorage();
				if(storage != null)
					storage.readFromNBT(s);
			}
		}
	}
	
	@Override
	public Object receiveContent(BlackHolePacket packet, boolean simulate)
	{
		if(storage == null)
			storage = packet.type.createStorage();
		if(storage != null && storage.canHandle(packet))
			return storage.receive(packet, simulate);
		return null;
	}
	
	@Override
	public Object sendContent(BigInteger amount, boolean simulate)
	{
		if(storage != null)
		{
			BlackHolePacket pkt = storage.create(amount);
			// if(storage.canHandle(pkt)) //Why storage can't handle it's own
			// packet?! If that's the case, well, it's really stupid ._.
			Object obj = storage.send(pkt, simulate);
			if(storage.isEmpty())
				storage = null; // make storage reset if it has been emptied
			return obj;
		}
		return null;
	}
	
	@Override
	public double getMaxRenderDistanceSquared()
	{
		return Double.POSITIVE_INFINITY;
	}
	
	public EnumBlackHolePacketType getType()
	{
		return storage != null ? storage.getType() : null;
	}
	
	@Override
	public IBlackHoleStorage getStorage()
	{
		return storage;
	}
	
	@Override
	public void setStorage(IBlackHoleStorage storage)
	{
		this.storage = storage;
	}
	
	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return INFINITE_EXTENT_AABB;
	}
}