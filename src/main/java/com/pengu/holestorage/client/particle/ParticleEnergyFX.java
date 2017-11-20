package com.pengu.holestorage.client.particle;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.proxy.ParticleProxy_Client;
import com.pengu.holestorage.InfoBHS;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ParticleEnergyFX extends SimpleParticle
{
	public ResourceLocation texture = new ResourceLocation(InfoBHS.MOD_ID, "textures/particles/energy.png");
	
	private double targetX, targetY, targetZ;
	private double omx, omy, omz;
	
	public ParticleEnergyFX(World worldIn, double xIn, double yIn, double zIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int rf)
	{
		super(worldIn, xIn, yIn, zIn);
		motionX = omx = (xSpeedIn - xIn) / 100D;
		motionY = omy = (ySpeedIn - yIn) / 100D;
		motionZ = omz = (zSpeedIn - zIn) / 100D;
		targetX = xSpeedIn;
		targetY = ySpeedIn;
		targetZ = zSpeedIn;
		particleMaxAge = 500;
		particleScale = Math.min(2, rf / 25000F);
		particleBlue = .7F;
		particleRed = .3F;
		canCollide = false;
		particleGreen = 1F;
	}
	
	public ParticleEnergyFX spawn()
	{
		if(Minecraft.getMinecraft().player == null)
			return this;
		if(Minecraft.getMinecraft().player.getDistanceSq(posX, posY, posZ) >= 4096D)
			return this;
		ParticleProxy_Client.queueParticleSpawn(this);
		return this;
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		motionX = omx;
		motionY = omy;
		motionZ = omz;
		
		if(posX >= targetX - .05 && posY >= targetY - .05 && posZ >= targetZ - .05 && posX <= targetX + .24 && posY <= targetY + .24 && posZ <= targetZ + .24)
			setExpired();
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		float f = 0;
		float f1 = 1;
		float f2 = 0;
		float f3 = 1;
		float f4 = 0.1F * particleScale;
		
		if(particleTexture != null)
		{
			f = particleTexture.getMinU();
			f1 = particleTexture.getMaxU();
			f2 = particleTexture.getMinV();
			f3 = particleTexture.getMaxV();
		}
		
		float f5 = (float) (prevPosX + (posX - prevPosX) * (double) partialTicks - interpPosX);
		float f6 = (float) (prevPosY + (posY - prevPosY) * (double) partialTicks - interpPosY);
		float f7 = (float) (prevPosZ + (posZ - prevPosZ) * (double) partialTicks - interpPosZ);
		int i = getBrightnessForRender(partialTicks);
		int j = i >> 16 & 65535;
		int k = i & 65535;
		Vec3d[] avec3d = new Vec3d[] { new Vec3d((double) (-rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (-rotationYZ * f4 - rotationXZ * f4)), new Vec3d((double) (-rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (-rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 + rotationXY * f4), (double) (rotationZ * f4), (double) (rotationYZ * f4 + rotationXZ * f4)), new Vec3d((double) (rotationX * f4 - rotationXY * f4), (double) (-rotationZ * f4), (double) (rotationYZ * f4 - rotationXZ * f4)) };
		
		if(particleAngle != 0.0F)
		{
			float f8 = particleAngle + (particleAngle - prevParticleAngle) * partialTicks;
			float f9 = MathHelper.cos(f8 * 0.5F);
			float f10 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.x;
			float f11 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.y;
			float f12 = MathHelper.sin(f8 * 0.5F) * (float) cameraViewDir.z;
			Vec3d vec3d = new Vec3d((double) f10, (double) f11, (double) f12);
			
			for(int l = 0; l < 4; ++l)
			{
				avec3d[l] = vec3d.scale(2.0D * avec3d[l].dotProduct(vec3d)).add(avec3d[l].scale((double) (f9 * f9) - vec3d.dotProduct(vec3d))).add(vec3d.crossProduct(avec3d[l]).scale((double) (2.0F * f9)));
			}
		}
		
		BufferBuilder buffer = Tessellator.getInstance().getBuffer();
		buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_LMAP_COLOR);
		
		buffer.pos((double) f5 + avec3d[0].x, (double) f6 + avec3d[0].y, (double) f7 + avec3d[0].z).tex((double) f1, (double) f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[1].x, (double) f6 + avec3d[1].y, (double) f7 + avec3d[1].z).tex((double) f1, (double) f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[2].x, (double) f6 + avec3d[2].y, (double) f7 + avec3d[2].z).tex((double) f, (double) f2).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		buffer.pos((double) f5 + avec3d[3].x, (double) f6 + avec3d[3].y, (double) f7 + avec3d[3].z).tex((double) f, (double) f3).color(particleRed, particleGreen, particleBlue, particleAlpha).lightmap(j, k).endVertex();
		
		Tessellator.getInstance().draw();
	}
}