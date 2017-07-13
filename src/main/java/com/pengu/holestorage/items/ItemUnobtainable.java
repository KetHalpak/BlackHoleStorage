package com.pengu.holestorage.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

import com.pengu.hammercore.common.items.MultiVariantItem;
import com.pengu.holestorage.Info;

public class ItemUnobtainable extends MultiVariantItem
{
	public static final int //
	        black_hole_shield = 0, //
	        anti_matter_core = 1, //
	        anti_matter_electron1 = 2, //
	        anti_matter_electron2 = 3, //
	        anti_matter_electron3 = 4 //
	        ;
	
	public ItemUnobtainable()
	{
		super("unobtainable_decoration", //
		Info.MOD_ID + ":black_hole_shield", // 0
		Info.MOD_ID + ":anti_matter_core", // 1
		Info.MOD_ID + ":anti_matter_electron1", // 2
		Info.MOD_ID + ":anti_matter_electron2", // 3
		Info.MOD_ID + ":anti_matter_electron3" // 4
		);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> l)
	{
	}
}