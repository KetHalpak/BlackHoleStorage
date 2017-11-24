package com.pengu.holestorage.init;

import java.util.Arrays;

import com.pengu.hammercore.bookAPI.fancy.ManualCategories;
import com.pengu.hammercore.bookAPI.fancy.ManualEntry;
import com.pengu.hammercore.bookAPI.fancy.ManualEntry.eEntryShape;
import com.pengu.hammercore.bookAPI.fancy.ManualPage;
import com.pengu.hammercore.bookAPI.fancy.ManualPage.PageType;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class ManualBHS
{
	public static final PageType ATOMIC_TRANSFORMER = new PageType("ATOMIC_TRANSFORMER", "com.pengu.holestorage.client.render.recipe.ManualPageRenderAtomic");
	
	public static void load()
	{
		ManualCategories.registerCategory(InfoBHS.MOD_ID, InfoBHS.texture("manual.png"), InfoBHS.texture("gui/manual.png"));
		
		make("intro", 0, -3, InfoBHS.texture("manual.png")).setPages(page("intro")).setShape(eEntryShape.HEX).registerEntry();
		make("atomic_transformer", 0, 0, new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER), "bh.intro").setPages(page("atomic_transformer"), recipe(new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER))).registerEntry();
		make("black_hole_former", 2, 3, new ItemStack(BlocksBHS.BLACK_HOLE_FORMER), "bh.atomic_transformer").setPages(page("black_hole_former"), atransformer(new ItemStack(Blocks.DIAMOND_BLOCK)), recipe(new ItemStack(ItemsBHS.FORMER_RING)), recipe(new ItemStack(BlocksBHS.BLACK_HOLE_FORMER))).registerEntry().setColor(0x9900FF);
		make("anti_matter", -2, -1, new ItemStack(ItemsBHS.ANTI_MATTER), "bh.atomic_transformer").setPages(page("anti_matter"), atransformer(new ItemStack(ItemsBHS.DARK_MATTER))).registerEntry().setColor(0xFF9900);
		make("black_hole", 1, 4, new ItemStack(BlocksBHS.BLACK_HOLE), "bh.black_hole_former").setShape(eEntryShape.ROUND).setPages(page("black_hole.1"), page("black_hole.2")).registerEntry().setColor(0x666666);
		make("black_hole_charger", 2, 5, new ItemStack(BlocksBHS.BLACK_HOLE_CHARGER), "bh.black_hole").setShape(eEntryShape.HEX).setPages(page("black_hole_charger"), recipe(new ItemStack(ItemsBHS.DESTABILIZED_DIAMOND)), atransformer(new ItemStack(ItemsBHS.DESTABILIZED_DIAMOND)), recipe(new ItemStack(BlocksBHS.BLACK_HOLE_CHARGER))).registerEntry().setColor(0xFF0000);
		make("rf_injector", -1, 3, new ItemStack(BlocksBHS.RF_INJECTOR), "bh.black_hole").setPages(page("rf_injector"), recipe(new ItemStack(BlocksBHS.RF_INJECTOR))).registerEntry();
		make("rf_ejector", -1, 2, new ItemStack(BlocksBHS.RF_EJECTOR), "bh.black_hole").setPages(page("rf_ejector"), recipe(new ItemStack(BlocksBHS.RF_EJECTOR))).registerEntry();
		make("fluid_injector", -1, 5, new ItemStack(BlocksBHS.FLUID_INJECTOR), "bh.black_hole").setPages(page("fluid_injector"), recipe(new ItemStack(BlocksBHS.FLUID_INJECTOR))).registerEntry();
		make("fluid_ejector", -1, 6, new ItemStack(BlocksBHS.FLUID_EJECTOR), "bh.black_hole").setPages(page("fluid_ejector"), recipe(new ItemStack(BlocksBHS.FLUID_EJECTOR))).registerEntry();
	}
	
	public static ManualPage recipe(ItemStack... outputs)
	{
		ManualPage r = new ManualPage(outputs, PageType.NORMAL_CRAFTING);
		r.allOutputs.addAll(Arrays.asList(outputs));
		return r;
	}
	
	public static ManualPage atransformer(ItemStack in)
	{
		ManualPage r = new ManualPage(in, ATOMIC_TRANSFORMER);
		SimpleTransformerRecipe s = AtomicTransformerRecipes.getRecipeFor(in);
		if(s != null)
			r.allOutputs.add(s.getOutput(in));
		return r;
	}
	
	public static ManualPage page(String desc)
	{
		return new ManualPage("bhs.manual_desc." + desc);
	}
	
	public static ManualEntry make(String id, int x, int y, ItemStack icon, String... relations)
	{
		return new ManualEntry("bh." + id, InfoBHS.MOD_ID, x, y, icon).setParents(relations);
	}
	
	public static ManualEntry make(String id, int x, int y, ResourceLocation icon, String... relations)
	{
		return new ManualEntry("bh." + id, InfoBHS.MOD_ID, x, y, icon).setParents(relations);
	}
}