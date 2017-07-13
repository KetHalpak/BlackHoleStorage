package com.pengu.holestorage.tile;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.FMLCommonHandler;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.capabilities.CapabilityEJ;
import com.pengu.hammercore.common.capabilities.FEEnergyStorage;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.energy.IPowerStorage;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.init.BlocksBHS;

public class TileWormholeFormer extends TileSyncableTickable implements IEnergyStorage, IPowerStorage
{
	public FEEnergyStorage storage = new FEEnergyStorage(2_048_000_000);
	public InventoryNonTile inventory = new InventoryNonTile(2);
	
	@Override
	public void tick()
	{
		ItemStack a = inventory.getStackInSlot(0);
		ItemStack b = inventory.getStackInSlot(1);
		
		if(InterItemStack.isStackNull(a) || InterItemStack.isStackNull(a))
			return;
		
		if(a.getTagCompound() != null)
		{
			NBTTagCompound nbt = a.getTagCompound();
			if(nbt.hasKey("Dim", NBT.TAG_INT) && nbt.hasKey("x", NBT.TAG_INT) && nbt.hasKey("y", NBT.TAG_INT) && nbt.hasKey("z", NBT.TAG_INT) && nbt.hasKey("Face", NBT.TAG_BYTE))
				;
			else
				return;
		}
		
		if(b.getTagCompound() != null)
		{
			NBTTagCompound nbt = b.getTagCompound();
			if(nbt.hasKey("Dim", NBT.TAG_INT) && nbt.hasKey("x", NBT.TAG_INT) && nbt.hasKey("y", NBT.TAG_INT) && nbt.hasKey("z", NBT.TAG_INT) && nbt.hasKey("Face", NBT.TAG_BYTE))
				;
			else
				return;
		}
		
		if(storage.getEnergyStored() >= storage.getMaxEnergyStored() && !world.isRemote)
		{
			NBTTagCompound nbta = a.getTagCompound();
			NBTTagCompound nbtb = b.getTagCompound();
			
			if(nbta == null || nbtb == null)
				return;
			
			int aw = nbta.getInteger("Dim");
			int bw = nbtb.getInteger("Dim");
			int ameta = nbta.getByte("Face");
			int bmeta = nbtb.getByte("Face");
			BlockPos apos = new BlockPos(nbta.getInteger("x"), nbta.getInteger("y"), nbta.getInteger("z"));
			BlockPos bpos = new BlockPos(nbtb.getInteger("x"), nbtb.getInteger("y"), nbtb.getInteger("z"));
			
			if(aw == bw && apos.distanceSq(bpos) < 8D)
				return;
			
			MinecraftServer mc = FMLCommonHandler.instance().getMinecraftServerInstance();
			
			if(mc != null)
			{
				WorldServer tw = mc.getWorld(aw);
				Chunk targetChunk = tw.getChunkProvider().loadChunk(apos.getX() >> 4, apos.getZ() >> 4);
				targetChunk.setBlockState(apos, BlocksBHS.WORMHOLE.getStateFromMeta(ameta));
				TileWormhole te = new TileWormhole();
				((TileWormhole) te).target = bpos;
				((TileWormhole) te).dimension = bw;
				tw.setTileEntity(apos, te);
			}
			
			if(mc != null)
			{
				WorldServer tw = mc.getWorld(bw);
				Chunk targetChunk = tw.getChunkProvider().loadChunk(bpos.getX() >> 4, bpos.getZ() >> 4);
				targetChunk.setBlockState(bpos, BlocksBHS.WORMHOLE.getStateFromMeta(bmeta));
				TileWormhole te = new TileWormhole();
				((TileWormhole) te).target = apos;
				((TileWormhole) te).dimension = aw;
				tw.setTileEntity(bpos, te);
			}
			
			nbta.removeTag("Dim");
			nbta.removeTag("x");
			nbta.removeTag("y");
			nbta.removeTag("z");
			nbta.removeTag("Face");
			
			nbtb.removeTag("Dim");
			nbtb.removeTag("x");
			nbtb.removeTag("y");
			nbtb.removeTag("z");
			nbtb.removeTag("Face");
			
			storage.readFromNBT(new NBTTagCompound());
			
			if(world.rand.nextBoolean())
				inventory.setInventorySlotContents(0, ItemStack.EMPTY);
			if(world.rand.nextBoolean())
				inventory.setInventorySlotContents(1, ItemStack.EMPTY);
			sync();
		}
	}
	
	@Override
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		int accepted = storage.receiveEnergy(maxReceive, simulate);
		if(!simulate)
			sync();
		return accepted;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public int getEnergyStored()
	{
		return storage.getEnergyStored();
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		return storage.getMaxEnergyStored();
	}
	
	@Override
	public boolean canExtract()
	{
		return false;
	}
	
	@Override
	public boolean canReceive()
	{
		return true;
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		storage.writeToNBT(nbt);
		
		NBTTagCompound inv = new NBTTagCompound();
		inventory.writeToNBT(inv);
		ItemStackHelper.saveAllItems(inv, inventory.inventory);
		nbt.setTag("Inventory", inv);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		storage.readFromNBT(nbt);
		inventory.readFromNBT(nbt.getCompoundTag("Inventory"));
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY || capability == CapabilityEJ.ENERGY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
}