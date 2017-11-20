package com.pengu.holestorage.client.particle;

import java.util.function.Function;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.client.utils.RenderUtil;

import net.minecraft.world.World;

public class ParticleExplosion extends SimpleParticle
{
	public static final Function<Integer, Integer> COLORS = i ->
	{
		int j = i % 4;
		return j == 0 ? 0x11FF9900 : j == 1 ? 0x11009900 : j == 2 ? 0x11000099 : 0x11999900;
	};
	
	public ParticleExplosion(World worldIn, double posXIn, double posYIn, double posZIn)
	{
		super(worldIn, posXIn, posYIn, posZIn);
		particleMaxAge = 200;
		particleAge = 0;
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float scale = (particleAge + partialTicks) / particleMaxAge;
		double s = Math.max(-20, Math.sin(scale * 3.5) * 2000);
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glScaled(20 + s, 20 + s, 20 + s);
		RenderUtil.renderColorfulLightRayEffects(0, 0, 0, COLORS, hashCode(), scale, 1, 2, 50, 25);
		GL11.glPopMatrix();
	}
}