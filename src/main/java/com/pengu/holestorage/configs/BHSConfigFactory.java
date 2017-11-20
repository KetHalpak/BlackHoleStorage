package com.pengu.holestorage.configs;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import com.pengu.hammercore.cfg.gui.HCConfigGui;
import com.pengu.holestorage.InfoBHS;

public class BHSConfigFactory implements IModGuiFactory
{
	@Override
	public void initialize(Minecraft minecraftInstance)
	{
	}
	
	@Override
	public boolean hasConfigGui()
	{
		return false;
	}
	
	@Override
	public GuiScreen createConfigGui(GuiScreen parentScreen)
	{
		return new HCConfigGui(parentScreen, BHSConfigs.cfgs, InfoBHS.MOD_ID);
	}
	
	@Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
	{
		return null;
	}
}