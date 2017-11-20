package com.pengu.holestorage.init;

import com.pengu.hammercore.bookAPI.fancy.ManualCategories;
import com.pengu.holestorage.InfoBHS;

import net.minecraft.util.ResourceLocation;

public class ManualBHS
{
	public static void load()
	{
		ManualCategories.registerCategory(InfoBHS.MOD_ID, InfoBHS.texture("manual.png"), InfoBHS.texture("gui/manual.png"));
		
		
	}
}