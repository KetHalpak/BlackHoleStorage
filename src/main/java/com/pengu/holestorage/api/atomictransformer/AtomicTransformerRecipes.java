package com.pengu.holestorage.api.atomictransformer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.pengu.hammercore.common.InterItemStack;

public class AtomicTransformerRecipes
{
	private static final Set<SimpleTransformerRecipe> RECIPES = new HashSet<>();
	
	public static void register(SimpleTransformerRecipe recipe)
	{
		RECIPES.add(recipe);
	}
	
	public static Set<SimpleTransformerRecipe> getRecipes()
	{
		return RECIPES;
	}
	
	public static void register(ItemStack in, ItemStack out, BigInteger rf)
	{
		register(new SimpleTransformerRecipe(in, out, rf));
	}
	
	public static void register(ItemStack in, ItemStack out, long rf)
	{
		register(new SimpleTransformerRecipe(in, out, rf));
	}
	
	public static void register(String in, ItemStack out, BigInteger rf)
	{
		register(new SimpleTransformerRecipe(in, out, rf));
	}
	
	public static void register(String in, ItemStack out, long rf)
	{
		register(new SimpleTransformerRecipe(in, out, rf));
	}
	
	public static List<ItemStack> getAllInputs()
	{
		List<ItemStack> ins = new ArrayList<ItemStack>();
		for(SimpleTransformerRecipe r : RECIPES)
			ins.addAll(r.getInputSubtypes());
		return ins;
	}
	
	public static SimpleTransformerRecipe getRecipeFor(ItemStack input)
	{
		if(InterItemStack.isStackNull(input))
			return null;
		for(SimpleTransformerRecipe r : RECIPES)
			if(r.matches(input))
				return r;
		return null;
	}
}