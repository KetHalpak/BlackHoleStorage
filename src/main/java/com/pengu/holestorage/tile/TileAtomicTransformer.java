package com.pengu.holestorage.tile;

import java.math.BigInteger;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.inventory.InventoryNonTile;
import com.pengu.hammercore.common.inventory.iInventoryListener;
import com.pengu.hammercore.common.utils.BigIntegerUtils;
import com.pengu.hammercore.common.utils.ItemStackUtil;
import com.pengu.hammercore.tile.TileSyncableTickable;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.gui.inv.ContainerAtomicTransformer;
import com.pengu.holestorage.gui.ui.GuiAtomicTransformer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class TileAtomicTransformer extends TileSyncableTickable implements IEnergyStorage, ISidedInventory, iInventoryListener
{
	public BigInteger stored = BigInteger.ZERO;
	public InventoryNonTile inventory = new InventoryNonTile(2);
	public SimpleTransformerRecipe recipe;
	
	public void slotChange(int slot, ItemStack stack)
	{
		updateRecipe();
	}
	
	public InventoryNonTile getInventory()
	{
		return inventory;
	}
	
	@Override
	public void tick()
	{
		inventory.listener = this;
		
		ItemStack s0 = inventory.getStackInSlot(0);
		ItemStack s1 = inventory.getStackInSlot(1);
		
		if((ticksExisted - 1) % 40 == 0)
			updateRecipe();
		
		if(stored != null && canCraft() && !world.isRemote)
		{
			BigInteger total = recipe.getEnergyUsed(s0);
			BigInteger max = stored.max(total);
			if((max == stored || stored.equals(total)) && canOutput(recipe.getOutput(s0)))
			{
				stored = stored.subtract(total);
				ItemStack stack = inventory.getStackInSlot(0);
				InterItemStack.setStackSize(stack, InterItemStack.getStackSize(stack) - recipe.getInputItemCount());
				output(recipe.getOutput(s0));
				sync();
			}
		}
	}
	
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
	}
	
	public boolean canOutput(ItemStack out)
	{
		ItemStack s = inventory.getStackInSlot(1);
		return InterItemStack.isStackNull(s) || (ItemStackUtil.itemsEqual(s, out) && InterItemStack.getStackSize(s) + InterItemStack.getStackSize(out) <= s.getMaxStackSize());
	}
	
	public void output(ItemStack out)
	{
		if(canOutput(out))
		{
			ItemStack s = inventory.getStackInSlot(1);
			if(InterItemStack.isStackNull(s))
				inventory.setInventorySlotContents(1, out.copy());
			else
				InterItemStack.setStackSize(s, InterItemStack.getStackSize(s) + InterItemStack.getStackSize(out));
		}
	}
	
	public void updateRecipe()
	{
		recipe = AtomicTransformerRecipes.getRecipeFor(inventory.getStackInSlot(0));
	}
	
	public boolean canCraft()
	{
		return recipe != null && recipe.getInputItemCount() <= InterItemStack.getStackSize(inventory.getStackInSlot(0));
	}
	
	@Override
	public void writeNBT(NBTTagCompound nbt)
	{
		nbt.setString("EnergyStored", stored != null ? stored + "" : "0");
		
		NBTTagCompound inv = new NBTTagCompound();
		inventory.writeToNBT(inv);
		nbt.setTag("Inventory", inv);
	}
	
	@Override
	public void readNBT(NBTTagCompound nbt)
	{
		if(nbt.getString("EnergyStored").length() > 0)
			stored = new BigInteger(nbt.getString("EnergyStored"));
		inventory.readFromNBT(nbt.getCompoundTag("Inventory"));
	}
	
	@Override
	public int getEnergyStored()
	{
		return Math.min(BigIntegerUtils.isInt(stored) ? stored.intValue() : Integer.MAX_VALUE, getMaxEnergyStored());
	}
	
	@Override
	public int getMaxEnergyStored()
	{
		if(canCraft() && canOutput(recipe.getOutput(inventory.getStackInSlot(0))))
		{
			BigInteger total = recipe.getEnergyUsed(inventory.getStackInSlot(0));
			return BigIntegerUtils.isInt(total) ? total.intValue() : Integer.MAX_VALUE;
		}
		
		return 0;
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
	public int receiveEnergy(int maxReceive, boolean simulate)
	{
		if(canCraft() && canOutput(recipe.getOutput(inventory.getStackInSlot(0))))
		{
			BigInteger total = recipe.getEnergyUsed(inventory.getStackInSlot(0));
			BigInteger accepted = total.subtract(stored).min(BigInteger.valueOf(maxReceive));
			if(!simulate)
			{
				stored = stored.add(accepted);
				if(!world.isRemote)
					sync();
			}
			return accepted.intValue();
		}
		
		return 0;
	}
	
	@Override
	public int extractEnergy(int maxExtract, boolean simulate)
	{
		return 0;
	}
	
	@Override
	public int getSizeInventory()
	{
		return inventory.getSizeInventory();
	}
	
	@Override
	public boolean isEmpty()
	{
		return inventory.isEmpty();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return inventory.getStackInSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		return inventory.decrStackSize(index, count);
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return inventory.removeStackFromSlot(index);
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		inventory.setInventorySlotContents(index, stack);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return inventory.getInventoryStackLimit();
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player)
	{
		return inventory.isUsableByPlayer(player, pos);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return index == 0 ? AtomicTransformerRecipes.getRecipeFor(stack) != null : false;
	}
	
	@Override
	public int getField(int id)
	{
		return 0;
	}
	
	@Override
	public void setField(int id, int value)
	{
	}
	
	@Override
	public int getFieldCount()
	{
		return 0;
	}
	
	@Override
	public void clear()
	{
		inventory.clear();
	}
	
	@Override
	public String getName()
	{
		return "Atomic Transformer";
	}
	
	@Override
	public boolean hasCustomName()
	{
		return false;
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side)
	{
		return inventory.getAllAvaliableSlots();
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction)
	{
		return index == 0 && AtomicTransformerRecipes.getRecipeFor(itemStackIn) != null;
	}
	
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		return index == 1;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return true;
		return super.hasCapability(capability, facing);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		if(capability == CapabilityEnergy.ENERGY)
			return (T) this;
		return super.getCapability(capability, facing);
	}
	
	@Override
	public boolean hasGui()
	{
		return true;
	}
	
	@Override
	public Object getClientGuiElement(EntityPlayer player)
	{
		return new GuiAtomicTransformer(this, player.inventory);
	}
	
	@Override
	public Object getServerGuiElement(EntityPlayer player)
	{
		return new ContainerAtomicTransformer(this, player.inventory);
	}
}