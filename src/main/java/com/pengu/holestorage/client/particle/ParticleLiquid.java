package com.pengu.holestorage.client.particle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import com.pengu.hammercore.client.particle.api.SimpleParticle;
import com.pengu.hammercore.glelwjgl.CoreGLE;
import com.pengu.holestorage.InfoBHS;
import com.pengu.holestorage.client.Quat;

public class ParticleLiquid extends SimpleParticle
{
	private static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
	
	private double targetX;
	private double targetY;
	private double targetZ;
	private double startX;
	private double startY;
	private double startZ;
	private int count = 0;
	public int length = 20;
	private String key = "";
	private BlockPos startPos = null;
	private BlockPos endPos = null;
	static HashMap<String, ParticleLiquid> pt = new HashMap();
	
	public ParticleLiquid(World w, double par2, double par4, double par6, double tx, double ty, double tz, int count, int color, float scale, int extend)
	{
		super(w, par2, par4, par6, 0.0D, 0.0D, 0.0D);
		
		particleScale = ((float) (scale * (1.0D + rand.nextGaussian() * 0.15000000596046448D)));
		
		length = Math.max(5, extend);
		this.count = count;
		targetX = tx;
		targetY = ty;
		targetZ = tz;
		BlockPos bp1 = new BlockPos(posX, posY, posZ);
		BlockPos bp2 = new BlockPos(targetX, targetY, targetZ);
		
		double dx = tx - posX;
		double dy = ty - posY;
		double dz = tz - posZ;
		int base = (int) (MathHelper.sqrt(dx * dx + dy * dy + dz * dz) * 21.0F);
		if(base < 1)
			base = 1;
		particleMaxAge = base;
		
		String k = bp1.toLong() + "" + bp2.toLong() + "" + color;
		if(pt.containsKey(k))
		{
			ParticleLiquid trail2 = (ParticleLiquid) pt.get(k);
			if(!trail2.isExpired && vecs.size() < length)
			{
				trail2.particleMaxAge += Math.max(extend, 3);
				trail2.length += Math.max(extend, 5);
				int nln = trail2.length;
				trail2.length = Math.min(trail2.length, 100);
				particleMaxAge = 0;
			}
		}
		
		if(particleMaxAge > 0)
		{
			pt.put(k, this);
			key = k;
		}
		
		motionX = (MathHelper.sin(count / 4.0F) * 0.015F);
		motionY = (MathHelper.sin(count / 3.0F) * 0.015F);
		motionZ = (MathHelper.sin(count / 2.0F) * 0.015F);
		
		Color c = new Color(color);
		particleRed = (c.getRed() / 255.0F);
		particleGreen = (c.getGreen() / 255.0F);
		particleBlue = (c.getBlue() / 255.0F);
		
		particleGravity = 0.2F;
		canCollide = false;
		
		vecs.add(new Quat(0.0D, 0.0D, 0.0D, 0.001D));
		vecs.add(new Quat(0.0D, 0.0D, 0.0D, 0.001D));
		
		startX = posX;
		startY = posY;
		startZ = posZ;
		
		startPos = new BlockPos(startX, startY, startZ);
		endPos = bp2;
	}
	
	CoreGLE gle = new CoreGLE();
	private static final ResourceLocation TEX0 = new ResourceLocation(InfoBHS.MOD_ID, "textures/particles/liquid.png");
	
	@Override
	public void doRenderParticle(double x, double y, double z, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ)
	{
		GL11.glPushMatrix();
		double ePX = startX - interpPosX;
		double ePY = startY - interpPosY;
		double ePZ = startZ - interpPosZ;
		GL11.glTranslated(ePX, ePY, ePZ);
		if(points != null && points.length > 2)
		{
			Minecraft.getMinecraft().getTextureManager().bindTexture(TEX0);
			gle.set_POLYCYL_TESS(8);
			gle.set__ROUND_TESS_PIECES(1);
			gle.gleSetJoinStyle(1042);
			gle.glePolyCone(points.length, points, colours, radii, 0.075F, growing < 0 ? 0.0F : 0.075F * (particleAge - growing + partialTicks));
		}
		GL11.glPopMatrix();
	}
	
	int layer = 1;
	double[][] points = {};
	float[][] colours = {};
	double[] radii = {};
	
	public void setFXLayer(int l)
	{
		layer = l;
	}
	
	public int getFXLayer()
	{
		return layer;
	}
	
	int growing = -1;
	ArrayList<Quat> vecs = new ArrayList();
	
	public BlockPos getPosition()
	{
		return new BlockPos(posX, posY, posZ);
	}
	
	public void onUpdate()
	{
		prevPosX = posX;
		prevPosY = posY;
		prevPosZ = posZ;
		if((particleAge++ >= particleMaxAge) || (length < 1))
		{
			setExpired();
			if((pt.containsKey(key)) && (((ParticleLiquid) pt.get(key)).isExpired))
			{
				pt.remove(key);
			}
			return;
		}
		motionY += 0.01D * particleGravity;
		
		double ds = getDistanceSqToCenter(startPos);
		double de = getDistanceSqToCenter(endPos);
		
		// canCollide = ((ds <= 1.5D) || (de <= 1.5D));
		if(canCollide)
			pushOutOfBlocks(posX, posY, posZ);
		move(motionX, motionY, motionZ);
		
		motionX *= 0.985D;
		motionY *= 0.985D;
		motionZ *= 0.985D;
		
		motionX = MathHelper.clamp((float) motionX, -0.05F, 0.05F);
		motionY = MathHelper.clamp((float) motionY, -0.05F, 0.05F);
		motionZ = MathHelper.clamp((float) motionZ, -0.05F, 0.05F);
		
		double dx = targetX - posX;
		double dy = targetY - posY;
		double dz = targetZ - posZ;
		double d13 = 0.01D;
		double d11 = MathHelper.sqrt(dx * dx + dy * dy + dz * dz);
		
		dx /= d11;
		dy /= d11;
		dz /= d11;
		
		motionX += dx * (d13 / Math.min(1.0D, d11));
		motionY += dy * (d13 / Math.min(1.0D, d11));
		motionZ += dz * (d13 / Math.min(1.0D, d11));
		
		float scale = particleScale * (0.75F + MathHelper.sin((count + particleAge) / 2.0F) * 0.25F);
		if(d11 < 1.0D)
		{
			float f = MathHelper.sin((float) (d11 * 1.5707963267948966D));
			scale *= f;
			particleScale *= f;
		}
		if(particleScale > 0.001D)
		{
			vecs.add(new Quat(scale, posX - startX, posY - startY, posZ - startZ));
		} else
		{
			if(growing < 0)
			{
				growing = particleAge;
			}
			length -= 1;
			essentiaDropFx(targetX + rand.nextGaussian() * 0.07500000298023224D, targetY + rand.nextGaussian() * 0.07500000298023224D, targetZ + rand.nextGaussian() * 0.07500000298023224D, particleRed, particleGreen, particleBlue, 0.5F);
		}
		if(vecs.size() > length)
		{
			vecs.remove(0);
		}
		points = new double[vecs.size()][3];
		colours = new float[vecs.size()][3];
		radii = new double[vecs.size()];
		int c = vecs.size();
		for(Quat v : vecs)
		{
			c--;
			float variance = 1.0F + MathHelper.sin((c + particleAge) / 3.0F) * 0.2F;
			
			float xx = MathHelper.sin((c + particleAge) / 6.0F) * 0.03F;
			float yy = MathHelper.sin((c + particleAge) / 7.0F) * 0.03F;
			float zz = MathHelper.sin((c + particleAge) / 8.0F) * 0.03F;
			
			points[c][0] = (v.x + xx);
			points[c][1] = (v.y + yy);
			points[c][2] = (v.z + zz);
			
			radii[c] = (v.s * variance);
			if(c > vecs.size() - 10)
			{
				radii[c] *= MathHelper.cos((float) ((c - (vecs.size() - 12)) / 10.0F * 1.5707963267948966D));
			}
			if(c == 0)
			{
				radii[c] = 0.0D;
			} else if(c == 1)
			{
				radii[c] = 0.0D;
			} else if(c == 2)
			{
				radii[c] = ((particleScale * 0.5D + radii[c]) / 2.0D);
			} else if(c == 3)
			{
				radii[c] = ((particleScale + radii[c]) / 2.0D);
			} else if(c == 4)
			{
				radii[c] = ((particleScale + radii[c] * 2.0D) / 3.0D);
			}
			float v2 = 1.0F - MathHelper.sin((c + particleAge) / 2.0F) * 0.1F;
			
			colours[c][0] = (particleRed * v2);
			colours[c][1] = (particleGreen * v2);
			colours[c][2] = (particleBlue * v2);
		}
		if((vecs.size() > 2) && (rand.nextBoolean()))
		{
			int q = rand.nextInt(3);
			if(rand.nextBoolean())
			{
				q = vecs.size() - 2;
			}
			essentiaDropFx(((Quat) vecs.get(q)).x + startX, ((Quat) vecs.get(q)).y + startY, ((Quat) vecs.get(q)).z + startZ, particleRed, particleGreen, particleBlue, 0.5F);
		}
	}
	
	public void setGravity(float value)
	{
		particleGravity = value;
	}
	
	public void essentiaDropFx(double x, double y, double z, float r, float g, float b, float alpha)
	{
		if(world.rand.nextInt(100) > 8)
			return;
		ParticleGeneric fb = new ParticleGeneric(world, x, y, z, world.rand.nextGaussian() * 0.004999999888241291D, world.rand.nextGaussian() * 0.004999999888241291D, world.rand.nextGaussian() * 0.004999999888241291D);
		fb.setMaxAge(20 + world.rand.nextInt(10), 0);
		fb.setRBGColorF(r, g, b);
		fb.setAlphaF(alpha);
		fb.setLoop(false);
		fb.setParticles(25, 1, 1);
		fb.setScale(0.4F + world.rand.nextFloat() * 0.2F, 0.2F);
		fb.setLayer(1);
		fb.setGravity(0.01F);
		fb.setRotationSpeed(0.0F);
		Minecraft.getMinecraft().effectRenderer.addEffect(fb);
	}
	
	protected boolean pushOutOfBlocks(double x, double y, double z)
	{
		BlockPos blockpos = new BlockPos(x, y, z);
		double d0 = x - (double) blockpos.getX();
		double d1 = y - (double) blockpos.getY();
		double d2 = z - (double) blockpos.getZ();
		
		if(!world.collidesWithAnyBlock(getBoundingBox()))
		{
			return false;
		} else
		{
			EnumFacing enumfacing = EnumFacing.UP;
			double d3 = Double.MAX_VALUE;
			
			if(!world.isBlockFullCube(blockpos.west()) && d0 < d3)
			{
				d3 = d0;
				enumfacing = EnumFacing.WEST;
			}
			
			if(!world.isBlockFullCube(blockpos.east()) && 1.0D - d0 < d3)
			{
				d3 = 1.0D - d0;
				enumfacing = EnumFacing.EAST;
			}
			
			if(!world.isBlockFullCube(blockpos.north()) && d2 < d3)
			{
				d3 = d2;
				enumfacing = EnumFacing.NORTH;
			}
			
			if(!world.isBlockFullCube(blockpos.south()) && 1.0D - d2 < d3)
			{
				d3 = 1.0D - d2;
				enumfacing = EnumFacing.SOUTH;
			}
			
			if(!world.isBlockFullCube(blockpos.up()) && 1.0D - d1 < d3)
			{
				d3 = 1.0D - d1;
				enumfacing = EnumFacing.UP;
			}
			
			float f = rand.nextFloat() * 0.2F + 0.1F;
			float f1 = (float) enumfacing.getAxisDirection().getOffset();
			
			if(enumfacing.getAxis() == EnumFacing.Axis.X)
			{
				motionX = (double) (f1 * f);
				motionY *= 0.75D;
				motionZ *= 0.75D;
			} else if(enumfacing.getAxis() == EnumFacing.Axis.Y)
			{
				motionX *= 0.75D;
				motionY = (double) (f1 * f);
				motionZ *= 0.75D;
			} else if(enumfacing.getAxis() == EnumFacing.Axis.Z)
			{
				motionX *= 0.75D;
				motionY *= 0.75D;
				motionZ = (double) (f1 * f);
			}
			
			return true;
		}
	}
	
	public double getDistanceSqToCenter(BlockPos pos)
	{
		return pos.distanceSqToCenter(posX, posY, posZ);
	}
}