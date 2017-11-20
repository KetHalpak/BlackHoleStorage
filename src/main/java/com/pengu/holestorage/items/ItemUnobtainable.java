package com.pengu.holestorage.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemUnobtainable extends Item
{
	public ItemUnobtainable(String name)
	{
		setUnlocalizedName(name);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> l)
	{
	}
}