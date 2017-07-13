package com.pengu.holestorage.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import com.pengu.holestorage.Info;
import com.pengu.holestorage.init.BlocksBHS;

public class CreativeTabBlackHoleStorage extends CreativeTabs
{
	public static final CreativeTabBlackHoleStorage BLACK_HOLE_STORAGE = new CreativeTabBlackHoleStorage();
	
	public ItemStack icon = new ItemStack(BlocksBHS.BLACK_HOLE_STABILIZER);
	
	private CreativeTabBlackHoleStorage()
	{
		super(Info.MOD_ID);
	}
	
	@Override
	public ItemStack getTabIconItem()
	{
		return new ItemStack(BlocksBHS.BLACK_HOLE_STABILIZER);
	}
}