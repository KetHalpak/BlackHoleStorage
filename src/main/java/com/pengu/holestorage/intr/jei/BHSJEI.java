package com.pengu.holestorage.intr.jei;

import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import net.minecraft.item.ItemStack;

import com.pengu.holestorage.Info;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.gui.ui.GuiAtomicTransformer;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.intr.jei.atomictransformer.AtomicTransformerCategory;
import com.pengu.holestorage.intr.jei.atomictransformer.AtomicTransformerHandler;

@JEIPlugin
public class BHSJEI implements IModPlugin
{
	public static final String ATOMIC_TRANSFORMER = Info.MOD_ID + ":atomic_transformer";
	
	@Override
	public void register(IModRegistry reg)
	{
		reg.addRecipeCategories(new AtomicTransformerCategory(reg.getJeiHelpers().getGuiHelper()));
		reg.addRecipeHandlers(new AtomicTransformerHandler());
		
		reg.addRecipeCategoryCraftingItem(new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER), ATOMIC_TRANSFORMER);
		
		reg.addRecipeClickArea(GuiAtomicTransformer.class, 17, 30, 16, 26, ATOMIC_TRANSFORMER);
		
		reg.addRecipes(AtomicTransformerRecipes.getRecipes());
		
		reg.addDescription(new ItemStack(ItemsBHS.KNOWLEDGE_TOME), "jei." + Info.MOD_ID + ".desc:knowledge_tome");
	}
	
	@Override
	public void onRuntimeAvailable(IJeiRuntime arg0)
	{
	}
	
	@Override
	public void registerIngredients(IModIngredientRegistration arg0)
	{
	}
	
	@Override
	public void registerItemSubtypes(ISubtypeRegistry arg0)
	{
	}
}