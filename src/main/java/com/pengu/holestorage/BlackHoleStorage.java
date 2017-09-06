package com.pengu.holestorage;

import java.io.File;

import com.pengu.hammercore.common.SimpleRegistration;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.init.RecipesBHS;
import com.pengu.holestorage.proxy.CommonProxy;
import com.pengu.holestorage.tabs.CreativeTabBlackHoleStorage;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = Info.MOD_ID, version = Info.MOD_VERSION, name = Info.MOD_NAME, dependencies = "required-after:hammercore", guiFactory = "com.pengu.holestorage.configs.BHSConfigFactory")
public class BlackHoleStorage
{
	@SidedProxy(clientSide = Info.PROXY_CLIENT, serverSide = Info.PROXY_SERVER)
	public static CommonProxy proxy;
	
	@Instance
	public static BlackHoleStorage instance;
	
	public static File cfgFolder;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		String cfg = event.getSuggestedConfigurationFile().getAbsolutePath();
		cfg = cfg.substring(0, cfg.lastIndexOf("."));
		cfgFolder = new File(cfg);
		cfgFolder.mkdirs();
		
		proxy.preInit();
		SimpleRegistration.registerFieldBlocksFrom(BlocksBHS.class, Info.MOD_ID, CreativeTabBlackHoleStorage.BLACK_HOLE_STORAGE);
		SimpleRegistration.registerFieldItemsFrom(ItemsBHS.class, Info.MOD_ID, CreativeTabBlackHoleStorage.BLACK_HOLE_STORAGE);
		RecipesBHS.oneTimeInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(proxy);
		proxy.init();
		
		FMLInterModComms.sendMessage("waila", "register", "com.pengu.holestorage.intr.waila.BHSWAILA.registerWAIA");
	}
}