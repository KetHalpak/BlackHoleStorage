package com.pengu.holestorage.intr.hammercore.types;

import java.math.BigInteger;

import com.pengu.hammercore.recipeAPI.iRecipeType;
import com.pengu.hammercore.recipeAPI.iRecipeType.RecipeParseException;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public class AtomicRecipeType implements iRecipeType<SimpleTransformerRecipe>
{
	@Override
	public boolean isJeiSupported(SimpleTransformerRecipe recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(SimpleTransformerRecipe recipe, boolean remove)
	{
		return recipe;
	}
	
	@Override
	public String getTypeId()
	{
		return InfoBHS.MOD_ID + ":atomic_transformer";
	}
	
	@Override
	public SimpleTransformerRecipe createRecipe(NBTTagCompound json) throws RecipeParseException
	{
		SimpleTransformerRecipe recipe = null;
		if(!json.hasKey("cost", NBT.TAG_STRING))
			throw new RecipeParseException("\"cost\" not defined or is not acceptable!");
		if(json.hasKey("input", NBT.TAG_COMPOUND))
		{
			ItemStack input = loadStack(json.getCompoundTag("input"));
			if(json.hasKey("output", NBT.TAG_COMPOUND))
			{
				ItemStack output = loadStack(json.getCompoundTag("output"));
				BigInteger cost = new BigInteger(json.getString("cost"));
				recipe = new SimpleTransformerRecipe(input, output, cost);
			} else
				throw new RecipeParseException("\"output\" not defined!");
		} else if(json.hasKey("input", NBT.TAG_STRING))
		{
			String input = json.getString("input");
			if(json.hasKey("output", NBT.TAG_COMPOUND))
			{
				ItemStack output = loadStack(json.getCompoundTag("output"));
				BigInteger cost = new BigInteger(json.getString("cost"));
				recipe = new SimpleTransformerRecipe(input, output, cost);
			} else
				throw new RecipeParseException("\"output\" not defined!");
		} else
			throw new RecipeParseException("\"input\" not defined or is not acceptable!");
		return recipe;
	}
	
	@Override
	public void addRecipe(SimpleTransformerRecipe recipe)
	{
		AtomicTransformerRecipes.register(recipe);
	}
	
	@Override
	public void removeRecipe(SimpleTransformerRecipe recipe)
	{
		AtomicTransformerRecipes.getRecipes().remove(recipe);
	}
}