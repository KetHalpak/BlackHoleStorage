package com.pengu.holestorage.intr.jei.atomictransformer;

import java.util.List;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.Info;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;

public class AtomicTransformerWrapper implements IRecipeWrapper
{
	final SimpleTransformerRecipe recipe;
	
	public AtomicTransformerWrapper(SimpleTransformerRecipe recipe)
	{
		this.recipe = recipe;
	}
	
	@Override
	public void drawInfo(Minecraft mc, int arg1, int arg2, int arg3, int arg4)
	{
		FontRenderer fontRendererObj = mc.fontRenderer;
		GL11.glPushMatrix();
		GL11.glTranslated(44, 9, 0);
		GL11.glScaled(.5, .5, 1);
		fontRendererObj.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.required") + ": " + String.format("%,d", recipe.getEnergyUsed(ItemStack.EMPTY)) + " RF", 0, 36, 0, false);
		fontRendererObj.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":output") + ": " + InterItemStack.getStackSize(recipe.getOutputItem()) + "x " + recipe.getOutputItem().getDisplayName(), 0, 54, 0, false);
		GL11.glPopMatrix();
	}
	
	@Override
	public void getIngredients(IIngredients ing)
	{
		ing.setInputs(ItemStack.class, recipe.getInputSubtypes());
		ing.setOutput(ItemStack.class, recipe.getOutputItem());
	}
	
	@Override
	public List<String> getTooltipStrings(int arg0, int arg1)
	{
		return null;
	}
	
	@Override
	public boolean handleClick(Minecraft arg0, int arg1, int arg2, int arg3)
	{
		return false;
	}
}