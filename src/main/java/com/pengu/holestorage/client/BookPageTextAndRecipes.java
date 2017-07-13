package com.pengu.holestorage.client;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.bookAPI.pages.BookPageTextPlain;
import com.pengu.hammercore.client.particle.RecipeRenderer;
import com.pengu.hammercore.common.match.item.ItemContainer;
import com.pengu.hammercore.common.match.item.ItemMatchParams;

/**
 * Created by MrDimka on 22.04.2017 at 15:48.
 */
public class BookPageTextAndRecipes extends BookPageTextPlain
{
	public int recipeX, recipeY;
	public ItemStack output;
	public IRecipe recipe;
	
	public BookPageTextAndRecipes(BookEntry entry, String text, ItemStack output)
	{
		super(entry, text);
		this.output = output;
	}
	
	public BookPageTextAndRecipes setRecipePos(int x, int y)
	{
		recipeX = x;
		recipeY = y;
		return this;
	}
	
	@Override
	public void prepare()
	{
		super.prepare();
		
		ItemContainer c = ItemContainer.create(output);
		ItemMatchParams params = new ItemMatchParams().setUseDamage(true);
		
		CraftingManager.REGISTRY.getKeys().stream().filter(t -> 
		{
			IRecipe r = CraftingManager.REGISTRY.getObject(t);
			return !r.getRecipeOutput().isEmpty() && c.matches(r.getRecipeOutput(), params);
		}).forEach(t -> recipe = CraftingManager.REGISTRY.getObject(t));
	}
	
	@Override
	public void render(int mouseX, int mouseY)
	{
		super.render(mouseX, mouseY);
		
		GL11.glPushMatrix();
		GL11.glTranslated(recipeX, recipeY, 0);
//		RecipeRenderer.selectAndRender(recipe);
		Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(recipe.getRecipeOutput(), 56, 4);
		GL11.glPopMatrix();
	}
}