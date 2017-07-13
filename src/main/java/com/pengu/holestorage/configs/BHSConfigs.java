package com.pengu.holestorage.configs;

import java.math.BigDecimal;

import net.minecraftforge.common.config.Configuration;

import com.pengu.hammercore.cfg.HCModConfigurations;
import com.pengu.hammercore.cfg.IConfigReloadListener;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyBool;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyFloat;
import com.pengu.hammercore.cfg.fields.ModConfigPropertyInt;
import com.pengu.holestorage.Info;
import com.pengu.holestorage.tile.TileBlackHoleFormer;

@HCModConfigurations(modid = Info.MOD_ID)
public class BHSConfigs implements IConfigReloadListener
{
	@ModConfigPropertyBool(name = "Use Shaders", category = "Client", defaultValue = true, comment = "Set this to true to use shaders for rendering black hole's shield.\nIf your PC doesn't support shaders, this option will be ignored and you won't be able to see the shield levels, sorry :(")
	public static boolean client_useShaders = true;
	
	@ModConfigPropertyInt(name = "Seethrought Wormholes", category = "Client", defaultValue = 2, min = 0, max = 256, comment = "Set this to 0 to disable experimental seethrought feature for wormholes. Depending on your PC set this value between 1 and 256.\nThis value will change the framerate for wormhole.\nFor now, this feature doesn't work on wormholes that are far away or in the other dimension. Sorry!")
	public static int client_seeThroughtWormholes = 5;
	
	@ModConfigPropertyInt(name = "Wormhole FOV", category = "Client", defaultValue = 80, min = 10, max = 110, comment = "This property changes FOV of your wormhole. I don't recomend changing it, but feel free to do so if you want.")
	public static int client_wormholeFov = 80;
	
	@ModConfigPropertyInt(name = "Wormhole Quality", category = "Client", defaultValue = 128, min = 8, max = 1024, comment = "This property makes the quality of seethrought feature either more detailed or simplified. Higher values will decrease performance.")
	public static int client_wormholeQuality = 128;
	
	@ModConfigPropertyFloat(name = "Anti-Explosion Multiplier", category = "Black Hole", defaultValue = 1, min = 0, max = 2, comment = "Use this parameter to manipulate black hole's explosion.\nYou can't really fully control explosion power because it's calculated dynamically.\nSet this to 0 to disable explosion.\nSet this to 2 to make the explosion 2 times stronger.")
	public static float blackHole_explosionMultiplier = 1;
	
	@Override
	public void reloadCustom(Configuration cfgs)
	{
		TileBlackHoleFormer.ABSORBED = new BigDecimal(cfgs.get("Energy", "Black Hole Former", 32_000_000_000L, "How much energy do you need to create a black hole?").getDouble());
	}
}