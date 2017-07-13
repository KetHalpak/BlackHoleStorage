package com.pengu.holestorage.api.atomictransformer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.hammercore.common.utils.ItemStackUtil;

public class SimpleTransformerRecipe
{
	protected final ItemStack output;
	protected final Set<ItemStack> subtypes = new HashSet<>();
	protected final String oredict;
	protected final BigInteger rf;
	protected int itemCount;
	
	public SimpleTransformerRecipe(ItemStack in, ItemStack out, long rf)
	{
		this(in, out, BigInteger.valueOf(rf));
	}
	
	public SimpleTransformerRecipe(ItemStack in, ItemStack out, BigInteger rf)
	{
		oredict = null;
		this.subtypes.add(in);
		itemCount = InterItemStack.getStackSize(in);
		this.output = out;
		this.rf = rf;
	}
	
	public SimpleTransformerRecipe(String in, ItemStack out, long rf)
	{
		this(in, out, BigInteger.valueOf(rf));
	}
	
	public SimpleTransformerRecipe(String in, ItemStack out, BigInteger rf)
	{
		this(in, 1, out, rf);
	}
	
	public SimpleTransformerRecipe(String in, int count, ItemStack out, long rf)
	{
		this(in, count, out, BigInteger.valueOf(rf));
	}
	
	public SimpleTransformerRecipe(String in, int count, ItemStack out, BigInteger rf)
	{
		oredict = in;
		itemCount = count;
		this.output = out;
		this.rf = rf;
	}
	
	public boolean matches(ItemStack input)
	{
		for(ItemStack subtype : getInputSubtypes())
			if(ItemStackUtil.itemsEqual(input, subtype) && InterItemStack.getStackSize(input) >= getInputItemCount())
				return true;
		return false;
	}
	
	public ItemStack getOutput(ItemStack input)
	{
		return this.output.copy();
	}
	
	public ItemStack getOutputItem()
	{
		return this.output.copy();
	}
	
	public List<ItemStack> getInputSubtypes()
	{
		List<ItemStack> items = new ArrayList<ItemStack>();
		if(oredict != null)
			for(ItemStack ore : OreDictionary.getOres(oredict))
				if(!InterItemStack.isStackNull(ore))
					items.add(ore.copy());
		for(ItemStack subtype : subtypes)
			if(!InterItemStack.isStackNull(subtype))
				items.add(subtype.copy());
		return items;
	}
	
	public BigInteger getEnergyUsed(ItemStack input)
	{
		return this.rf;
	}
	
	public int getInputItemCount()
	{
		return itemCount;
	}
}