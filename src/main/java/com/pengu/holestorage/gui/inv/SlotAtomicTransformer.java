package com.pengu.holestorage.gui.inv;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;

public class SlotAtomicTransformer extends Slot
{
	public SlotAtomicTransformer(IInventory inventoryIn, int index, int xPosition, int yPosition)
	{
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public boolean isItemValid(ItemStack stack)
	{
		return AtomicTransformerRecipes.getRecipeFor(stack) != null;
	}
}