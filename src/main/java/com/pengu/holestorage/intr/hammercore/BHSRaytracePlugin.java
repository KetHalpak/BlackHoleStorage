package com.pengu.holestorage.intr.hammercore;

import com.pengu.hammercore.api.mhb.BlockTraceable;
import com.pengu.hammercore.api.mhb.IRayCubeRegistry;
import com.pengu.hammercore.api.mhb.IRayRegistry;
import com.pengu.hammercore.api.mhb.RaytracePlugin;
import com.pengu.hammercore.recipeAPI.IRecipePlugin;
import com.pengu.hammercore.recipeAPI.IRecipeTypeRegistry;
import com.pengu.hammercore.recipeAPI.RecipePlugin;
import com.pengu.hammercore.vec.Cuboid6;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.intr.hammercore.types.AtomicRecipeType;

@RaytracePlugin
@RecipePlugin
public class BHSRaytracePlugin implements IRayRegistry, IRecipePlugin
{
	@Override
	public void registerCubes(IRayCubeRegistry cube)
	{
		cube.bindBlockCube6((BlockTraceable) BlocksBHS.WORMHOLE_FORMER, new Cuboid6(0, 0, 0, 1, .5, 1), new Cuboid6(0, .5, 0, 1, 1, 1));
	}
	
	@Override
	public void registerTypes(IRecipeTypeRegistry reg)
	{
		reg.register(new AtomicRecipeType());
	}
}