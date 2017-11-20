package com.pengu.holestorage.client.shaders;

import org.lwjgl.opengl.ARBShaderObjects;

import com.pengu.hammercore.client.render.shader.HCShaderPipeline;
import com.pengu.hammercore.client.render.shader.ShaderProgram;
import com.pengu.hammercore.client.render.shader.iShaderOperation;
import com.pengu.holestorage.configs.BHSConfigs;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;

public class BHSShaders
{
	public static int blackHoleOpID;
	public static ShaderProgram blackHole;
	public static BlackHoleOperation blackHoleOp;
	
	static
	{
		initBlackHoleShader();
	}
	
	public static void initBlackHoleShader()
	{
		if(blackHole != null)
			blackHole.cleanup();
		blackHoleOpID = HCShaderPipeline.registerOperation();
		blackHole = new ShaderProgram();
		blackHole.attachFrag("/assets/blackholestorage/shaders/black_hole.fsh");
		blackHole.attachVert("/assets/blackholestorage/shaders/black_hole.vsh");
		blackHole.attachShaderOperation(blackHoleOp = new BlackHoleOperation());
		blackHole.validate();
	}
	
	public static class BlackHoleOperation implements iShaderOperation
	{
		@Override
		public boolean load(ShaderProgram program)
		{
			return true;
		}
		
		@Override
		public void operate(ShaderProgram program)
		{
			int loc = program.getUniformLoc("time");
			ARBShaderObjects.glUniform1fARB(loc, (float) (Minecraft.getSystemTime() / 50000D));
		}
		
		@Override
		public int operationID()
		{
			return blackHoleOpID;
		}
	}
	
	public static boolean useShaders()
	{
		return OpenGlHelper.shadersSupported && BHSConfigs.client_useShaders;
	}
}