package com.pengu.holestorage.items;

import com.pengu.hammercore.bookAPI.ItemBook;
import com.pengu.holestorage.Info;

public class ItemKnowledgeTome extends ItemBook
{
	public ItemKnowledgeTome()
	{
		setUnlocalizedName("knowledge_tome");
		setMaxStackSize(1);
	}
	
	@Override
	public String getBookId()
	{
		return Info.MOD_ID + ":tome";
	}
}