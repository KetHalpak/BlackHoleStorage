package com.pengu.holestorage.items;

import com.pengu.holestorage.Info;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemUnobtainable extends Item
{
	public static final int //
	black_hole_shield = 0, //
	        anti_matter_core = 1, //
	        anti_matter_electron1 = 2, //
	        anti_matter_electron2 = 3, //
	        anti_matter_electron3 = 4 //
	;
	
	public static final String[] names = { Info.MOD_ID + ":black_hole_shield", Info.MOD_ID + ":anti_matter_core", Info.MOD_ID + ":anti_matter_electron1", Info.MOD_ID + ":anti_matter_electron2", Info.MOD_ID + ":anti_matter_electron3" };
	
	public ItemUnobtainable()
	{
		setUnlocalizedName("unobtainable_decoration");
		addPropertyOverride(new ResourceLocation("type"), (stack, world, ent) -> stack.getItemDamage());
		setMaxDamage(names.length);
		setHasSubtypes(true);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> l)
	{
	}
}