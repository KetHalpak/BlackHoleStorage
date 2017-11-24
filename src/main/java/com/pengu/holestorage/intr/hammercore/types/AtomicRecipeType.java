package com.pengu.holestorage.intr.hammercore.types;

import java.math.BigInteger;

import com.pengu.hammercore.recipeAPI.iRecipeType;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.intr.hammercore.types.AtomicRecipeType.ATRecipeHC;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;

public class AtomicRecipeType implements iRecipeType<ATRecipeHC>
{
	@Override
	public boolean isJeiSupported(ATRecipeHC recipe)
	{
		return true;
	}
	
	@Override
	public Object getJeiRecipeFor(ATRecipeHC recipe, boolean remove)
	{
		return recipe.recipe != null ? recipe.recipe : null;
	}
	
	@Override
	public String getTypeId()
	{
		return InfoBHS.MOD_ID + ":atomic_transformer";
	}
	
	@Override
	public ATRecipeHC createRecipe(NBTTagCompound json) throws RecipeParseException
	{
		boolean remove = json.getBoolean("remove");
		
		if(remove)
		{
			if(json.hasKey("output", NBT.TAG_COMPOUND))
			{
				ItemStack output = loadStack(json.getCompoundTag("output"));
				
				ATRecipeHC at = new ATRecipeHC();
				at.recipe = null;
				at.toRemove = remove;
				at.output = output;
				return at;
			} else
				throw new RecipeParseException("\"output\" not defined!");
		} else
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
			
			ATRecipeHC at = new ATRecipeHC();
			at.recipe = recipe;
			at.toRemove = remove;
			at.output = recipe.getOutputItem();
			return at;
		}
	}
	
	@Override
	public void addRecipe(ATRecipeHC recipe)
	{
		if(recipe.toRemove)
		{
			/* For backwards compatability */
			recipe.recipe = AtomicTransformerRecipes.getRecipeFor(recipe.output);
			AtomicTransformerRecipes.getRecipes().remove(recipe.recipe);
		}
		else
			AtomicTransformerRecipes.register(recipe.recipe);
	}
	
	@Override
	public void removeRecipe(ATRecipeHC recipe)
	{
		if(recipe.toRemove)
			AtomicTransformerRecipes.register(recipe.recipe);
		else
			AtomicTransformerRecipes.getRecipes().remove(recipe.recipe);
	}
	
	public static class ATRecipeHC
	{
		public SimpleTransformerRecipe recipe;
		public ItemStack output;
		public boolean toRemove;
	}
}