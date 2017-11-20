package com.pengu.holestorage.intr.jei.atomictransformer;

import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

import com.pengu.hammercore.client.utils.RenderUtil;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.intr.jei.BHSJEI;

public class AtomicTransformerCategory implements IRecipeCategory<AtomicTransformerWrapper>
{
	public final IDrawable backgound;
	
	public AtomicTransformerCategory(IGuiHelper gui)
	{
		backgound = gui.createDrawable(new ResourceLocation(InfoBHS.MOD_ID, "textures/gui/atomic_transformer_jei.png"), 0, 0, 168, 72);
	}
	
	@Override
	public void drawExtras(Minecraft mc)
	{
		double progress = System.currentTimeMillis() % 2000L / 2000D;
		RenderUtil.drawTexturedModalRect(11, 23, 168, 0, 16, 27 * progress);
	}
	
	@Override
	public IDrawable getBackground()
	{
		return backgound;
	}
	
	@Override
	public IDrawable getIcon()
	{
		return null;
	}
	
	@Override
	public String getTitle()
	{
		return I18n.translateToLocal("jei." + InfoBHS.MOD_ID + ":atomic_transformer");
	}
	
	@Override
	public String getUid()
	{
		return BHSJEI.ATOMIC_TRANSFORMER;
	}
	
	@Override
	public void setRecipe(IRecipeLayout layout, AtomicTransformerWrapper wrapper, IIngredients ings)
	{
		IGuiItemStackGroup items = layout.getItemStacks();
		
		items.init(0, true, 10, 3);
		items.set(0, wrapper.recipe.getInputSubtypes());
		
		items.init(1, false, 10, 51);
		items.set(1, wrapper.recipe.getOutputItem());
	}

	@Override
    public String getModName()
    {
	    return InfoBHS.MOD_NAME;
    }
}