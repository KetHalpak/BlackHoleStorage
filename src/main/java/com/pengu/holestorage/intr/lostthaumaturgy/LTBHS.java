package com.pengu.holestorage.intr.lostthaumaturgy;

import java.lang.reflect.Field;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Loader;

/**
 * @author APengu
 */
public class LTBHS
{
	public static void addRadiation(World world, BlockPos pos, float rad)
	{
		if(!Loader.isModLoaded("lostthaumaturgy"))
			return;
		try
		{
			Object tc = Class.forName("com.pengu.lostthaumaturgy.custom.aura.ThaumosphereManager").getMethod("getAuraChunkFromBlockCoords", World.class, BlockPos.class).invoke(null, world, pos);
			Field frad = tc.getClass().getField("radiation");
			frad.setFloat(tc, frad.getFloat(tc) + rad);	
		} catch(Throwable err)
		{
		}
	}
}