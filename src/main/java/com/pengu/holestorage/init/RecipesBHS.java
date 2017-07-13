package com.pengu.holestorage.init;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import com.pengu.hammercore.init.SimpleRegistration;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;

public class RecipesBHS
{
	public static void oneTimeInit()
	{
	}
	
	static
	{
		OreDictionary.registerOre("matterDark", ItemsBHS.DARK_MATTER);
		OreDictionary.registerOre("matterAnti", ItemsBHS.ANTI_MATTER);
		
		AtomicTransformerRecipes.register("blockDiamond", new ItemStack(ItemsBHS.DARK_MATTER), 1_000_000_000L);
		AtomicTransformerRecipes.register("gemDiamond", new ItemStack(Blocks.DIAMOND_BLOCK), 5_000_000L);
		AtomicTransformerRecipes.register("dustGlowstone", new ItemStack(Blocks.GLOWSTONE), 50_000L);
		AtomicTransformerRecipes.register("dustRedstone", new ItemStack(Blocks.REDSTONE_BLOCK), 10_000L);
		AtomicTransformerRecipes.register("gemQuartz", new ItemStack(Items.PRISMARINE_SHARD), 75_000L);
		AtomicTransformerRecipes.register(new ItemStack(Items.PRISMARINE_SHARD), new ItemStack(Items.PRISMARINE_CRYSTALS), 25000L);
		AtomicTransformerRecipes.register("gemLapis", new ItemStack(Blocks.LAPIS_BLOCK), 500000L);
		AtomicTransformerRecipes.register(new ItemStack(Blocks.QUARTZ_BLOCK), new ItemStack(Items.QUARTZ, 5), 50000L);
		AtomicTransformerRecipes.register("matterDark", new ItemStack(ItemsBHS.ANTI_MATTER), 8_000_000_000L);
	}
}