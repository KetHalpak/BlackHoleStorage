package com.pengu.holestorage.intr.jei.atomictransformer;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.intr.jei.BHSJEI;

public class AtomicTransformerHandler implements IRecipeHandler<SimpleTransformerRecipe>
{
	@Override
	public String getRecipeCategoryUid(SimpleTransformerRecipe arg0)
	{
		return BHSJEI.ATOMIC_TRANSFORMER;
	}
	
	@Override
	public Class<SimpleTransformerRecipe> getRecipeClass()
	{
		return SimpleTransformerRecipe.class;
	}
	
	@Override
	public IRecipeWrapper getRecipeWrapper(SimpleTransformerRecipe rec)
	{
		return new AtomicTransformerWrapper(rec);
	}
	
	@Override
	public boolean isRecipeValid(SimpleTransformerRecipe rec)
	{
		return true;
	}
}