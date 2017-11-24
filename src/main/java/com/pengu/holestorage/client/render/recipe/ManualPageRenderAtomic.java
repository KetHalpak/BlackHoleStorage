package com.pengu.holestorage.client.render.recipe;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.bookAPI.fancy.GuiManualRecipe;
import com.pengu.hammercore.bookAPI.fancy.ManualCategories;
import com.pengu.hammercore.bookAPI.fancy.ManualPage;
import com.pengu.hammercore.bookAPI.fancy.iManualPageRender;
import com.pengu.hammercore.client.utils.UtilsFX;
import com.pengu.hammercore.common.InterItemStack;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.init.BlocksBHS;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag.TooltipFlags;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;

public class ManualPageRenderAtomic implements iManualPageRender<ManualPage>
{
	@Override
	public void render(ManualPage recipe, int side, int x, int y, int mx, int my, GuiManualRecipe gui)
	{
		SimpleTransformerRecipe ar = null;
		ItemStack in = (ItemStack) recipe.getRecipe();
		ItemStack out = ItemStack.EMPTY;
		if(!InterItemStack.isStackNull(in))
			ar = AtomicTransformerRecipes.getRecipeFor(in);
		if(ar != null)
			out = ar.getOutput(in);
		if(!InterItemStack.isStackNull(out))
		{
			FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
			RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();
			
			GL11.glPushMatrix();
			int start = side * 152;
			
			String text = I18n.format("recipe.type." + InfoBHS.MOD_ID + ".atomictransformer");
			int offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y, 5263440);
			
			text = I18n.format("gui.blackholestorage:rf.required") + ":";
			offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y + 29, 5263440);
			
			text = String.format("%,d", ar.getEnergyUsed(in)) + " RF";
			offset = fontRenderer.getStringWidth(text);
			fontRenderer.drawString(text, x + start + 56 - offset / 2, y + 38, 5263440);
			
			UtilsFX.bindTexture(InfoBHS.MOD_ID, "textures/gui/gui_manual_overlay.png");
			GL11.glPushMatrix();
			GL11.glColor4f(1, 1, 1, 1);
			GL11.glEnable(3042);
			GL11.glTranslatef(x + start + 38, y + 54, 0);
			GL11.glScalef(2, 2, 1);
			gui.drawTexturedModalRect(0, 0, 0, 0, 35, 100);
			GL11.glPopMatrix();
			int mposx = mx;
			int mposy = my;
			GL11.glPushMatrix();
			GL11.glTranslated(0, 0, 100);
			GL11.glColor4f(1, 1, 1, 1);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2884);
			gui.renderStack(in, x + start + 41, y + 57, 29, 29, side, true);
			gui.renderStack(new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER), x + (side == 1 ? -8 : 78) + start, y + 164, 48, 48, side, true);
			gui.renderStack(out, x + start + 43, y + 124, 24, 24, side, false);
			RenderHelper.enableGUIStandardItemLighting();
			GL11.glEnable(2896);
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}
}