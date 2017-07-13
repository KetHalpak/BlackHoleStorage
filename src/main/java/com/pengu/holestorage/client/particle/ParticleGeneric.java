package com.pengu.holestorage.client.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.holestorage.client.BHSVec3;

public class ParticleGeneric extends SimpleParticle
{
	public TextureAtlasSprite textureSprite;
	
	public ParticleGeneric(World world, double x, double y, double z, double xx, double yy, double zz)
	{
		super(world, x, y, z, xx, yy, zz);
		setSize(0.1F, 0.1F);
		setPosition(x, y, z);
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		particleTextureJitterX = 0.0F;
		particleTextureJitterY = 0.0F;
		motionX = xx;
		motionY = yy;
		motionZ = zz;
		canCollide = false;
	}
	
	int layer = 0;
	
	public void setLayer(int layer)
	{
		this.layer = layer;
	}
	
	public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn)
	{
		super.setRBGColorF(particleRedIn, particleGreenIn, particleBlueIn);
		dr = particleRedIn;
		dg = particleGreenIn;
		db = particleBlueIn;
	}
	
	public void setRBGColorF(float particleRedIn, float particleGreenIn, float particleBlueIn, float r2, float g2, float b2)
	{
		super.setRBGColorF(particleRedIn, particleGreenIn, particleBlueIn);
		dr = r2;
		dg = g2;
		db = b2;
	}
	
	float dr = 0.0F;
	float dg = 0.0F;
	float db = 0.0F;
	
	public int getFXLayer()
	{
		return layer;
	}
	
	boolean loop = false;
	
	public void setLoop(boolean loop)
	{
		this.loop = loop;
	}
	
	protected float particleScaleMod = 0.0F;
	
	public void setScale(float scale)
	{
		particleScale = scale;
	}
	
	public void setScale(float scale, float end)
	{
		particleScale = scale;
		particleScaleMod = ((scale - end) / particleMaxAge);
	}
	
	public void setRotationSpeed(float rot)
	{
		rotationSpeed = rot;
	}
	
	public void setRotationSpeed(float start, float rot)
	{
		rotation = start;
		rotationSpeed = rot;
	}
	
	float rotationSpeed = 0.0F;
	float rotation = 0.0F;
	int delay = 0;
	
	public void setMaxAge(int max, int delay)
	{
		particleMaxAge = max;
		particleMaxAge += delay;
		this.delay = delay;
	}
	
	int startParticle = 0;
	int numParticles = 1;
	int particleInc = 1;
	
	public void setParticles(int startParticle, int numParticles, int particleInc)
	{
		this.startParticle = startParticle;
		this.numParticles = numParticles;
		this.particleInc = particleInc;
		setParticleTextureIndex(startParticle);
	}
	
	float particleAlphaMod = 0.0F;
	
	public void setAlphaF(float a1)
	{
		super.setAlphaF(a1);
		particleAlphaMod = 0.0F;
	}
	
	public void setAlphaF(float a1, float a2)
	{
		super.setAlphaF(a1);
		particleAlphaMod = ((a1 - a2) / particleMaxAge);
	}
	
	double slowDown = 0.9800000190734863D;
	
	public void setSlowDown(double slowDown)
	{
		this.slowDown = slowDown;
	}
	
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if(particleAge++ >= particleMaxAge)
		{
			setExpired();
		}
		rotation += rotationSpeed;
		
		motionY -= 0.04D * particleGravity;
		
		move(motionX, motionY, motionZ);
		
		motionX *= slowDown;
		motionY *= slowDown;
		motionZ *= slowDown;
		
		motionX += windX;
		motionZ += windZ;
		if((onGround) && (slowDown != 1.0D))
		{
			motionX *= 0.699999988079071D;
			motionZ *= 0.699999988079071D;
		}
	}
	
	boolean angled = false;
	float angleYaw;
	float anglePitch;
	
	public void setAngles(float yaw, float pitch)
	{
		angleYaw = yaw;
		anglePitch = pitch;
		angled = true;
	}
	
	public void setGravity(float g)
	{
		particleGravity = g;
	}
	
	public void setParticleTextureIndex(int particleTextureIndex)
	{
		particleTextureIndexX = (particleTextureIndex % gridSize);
		particleTextureIndexY = (particleTextureIndex / gridSize);
	}
	
	int gridSize = 16;
	double windX;
	double windZ;
	
	public void setGridSize(int gridSize)
	{
		this.gridSize = gridSize;
	}
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		if(loop)
			setParticleTextureIndex(startParticle + particleAge / particleInc % numParticles);
		else
		{
			float fs = particleAge / particleMaxAge;
			setParticleTextureIndex((int) (startParticle + Math.min(numParticles * fs, numParticles - 1)));
		}
		if(particleAge < delay)
			return;
		particleAlpha -= particleAlphaMod;
		float t = particleAlpha;
		if((particleAge <= 1) || (particleAge >= particleMaxAge - 1))
			particleAlpha = (t / 2.0F);
		if(particleAlpha < 0.0F)
			particleAlpha = 0.0F;
		if(particleAlpha > 1.0F)
			particleAlpha = 1.0F;
		particleScale -= particleScaleMod;
		if(particleScale < 0.0F)
			particleScale = 0.0F;
		if(depthIgnore)
			GlStateManager.disableDepth();
		draw(Tessellator.getInstance().getBuffer(), partialTicks, rotationX, rotationZ, rotationYZ, rotationXY, rotationXZ);
		if(depthIgnore)
			GlStateManager.enableDepth();
		particleAlpha = t;
	}
	
	public void draw(BufferBuilder wr, float pticks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		float f6 = particleTextureIndexX / gridSize;
		float f7 = f6 + 1.0F / gridSize;
		float f8 = particleTextureIndexY / gridSize;
		float f9 = f8 + 1.0F / gridSize;
		float f10 = 0.1F * particleScale;
		if(textureSprite != null)
		{
			f6 = textureSprite.getMinU();
			f7 = textureSprite.getMaxU();
			f8 = textureSprite.getMinV();
			f9 = textureSprite.getMaxV();
		}
		GL11.glPushMatrix();
		
		float f11 = (float) (prevPosX + (posX - prevPosX) * pticks - interpPosX);
		float f12 = (float) (prevPosY + (posY - prevPosY) * pticks - interpPosY);
		float f13 = (float) (prevPosZ + (posZ - prevPosZ) * pticks - interpPosZ);
		GL11.glTranslated(f11, f12, f13);
		
		GL11.glPushMatrix();
		
		float fs = MathHelper.clamp((particleAge + pticks) / particleMaxAge, 0.0F, 1.0F);
		float pr = particleRed + (dr - particleRed) * fs;
		float pg = particleGreen + (dg - particleGreen) * fs;
		float pb = particleBlue + (db - particleBlue) * fs;
		if(angled)
		{
			GL11.glRotatef(-angleYaw + 90.0F, 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(anglePitch + 90.0F, 1.0F, 0.0F, 0.0F);
		} else
		{
			rotateToPlayer();
		}
		GL11.glRotatef(rotation + pticks * rotationSpeed, 0.0F, 0.0F, 1.0F);
		int i = getBrightnessForRender(pticks);
		int j = i >> 16 & 0xFFFF;
		int k = i & 0xFFFF;
		wr.begin(7, DefaultVertexFormats.PARTICLE_POSITION_TEX_COLOR_LMAP);
		wr.pos(-f10, -f10, 0.0D).tex(f7, f9).color(pr, pg, pb, particleAlpha).lightmap(j, k).endVertex();
		wr.pos(-f10, f10, 0.0D).tex(f7, f8).color(pr, pg, pb, particleAlpha).lightmap(j, k).endVertex();
		wr.pos(f10, f10, 0.0D).tex(f6, f8).color(pr, pg, pb, particleAlpha).lightmap(j, k).endVertex();
		wr.pos(f10, -f10, 0.0D).tex(f6, f9).color(pr, pg, pb, particleAlpha).lightmap(j, k).endVertex();
		Tessellator.getInstance().draw();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
	public void setWind(double d)
	{
		int m = world.getMoonPhase();
		BHSVec3 vsource = BHSVec3.createVectorHelper(0.0D, 0.0D, 0.0D);
		BHSVec3 vtar = BHSVec3.createVectorHelper(0.1D, 0.0D, 0.0D);
		vtar.rotateAroundY(m * (40 + world.rand.nextInt(10)) / 180.0F * 3.1415927F);
		BHSVec3 vres = vsource.addVector(vtar.xCoord, vtar.yCoord, vtar.zCoord);
		windX = (vres.xCoord * d);
		windZ = (vres.zCoord * d);
	}
	
	boolean depthIgnore = false;
	
	public void setDepthIgnore(boolean b)
	{
		depthIgnore = b;
	}
	
	public static void rotateToPlayer()
	{
		GL11.glRotatef(-Minecraft.getMinecraft().getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(Minecraft.getMinecraft().getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
	}
}