package com.pengu.holestorage.gui.inv;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraft.item.ItemStack;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.tile.TileAtomicTransformer;

public class ContainerAtomicTransformer extends Container
{
	public final TileAtomicTransformer tile;
	public final InventoryPlayer inv;
	
	public ContainerAtomicTransformer(TileAtomicTransformer tile, InventoryPlayer inv)
	{
		this.tile = tile;
		this.inv = inv;
		
		addSlotToContainer(new SlotFurnaceOutput(inv.player, tile.inventory, 1, 17, 60));
		addSlotToContainer(new SlotAtomicTransformer(tile.inventory, 0, 17, 10));
		
		for(int i = 0; i < 9; i++)
			addSlotToContainer(new Slot(inv, i, 8 + i * 18, 142));
		
		for(int i = 0; i < 3; i++)
			for(int j = 0; j < 9; j++)
				addSlotToContainer(new Slot(inv, 9 + j + i * 9, 8 + 18 * j, 84 + i * 18));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return tile.isUseableByPlayer(playerIn);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		return InterItemStack.NULL_STACK;
	}
}