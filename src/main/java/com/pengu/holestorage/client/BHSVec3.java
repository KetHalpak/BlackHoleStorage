package com.pengu.holestorage.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BHSVec3
{
	public static final BHSVec3Pool vec3dPool = new BHSVec3Pool(-1, -1);
	public final BHSVec3Pool myVec3LocalPool;
	public double xCoord;
	public double yCoord;
	public double zCoord;
	
	public static BHSVec3 createVectorHelper(double par0, double par2, double par4)
	{
		return new BHSVec3(vec3dPool, par0, par2, par4);
	}
	
	public static BHSVec3 createVectorHelper(BlockPos p)
	{
		return new BHSVec3(vec3dPool, p.getX(), p.getY(), p.getZ());
	}
	
	protected BHSVec3(BHSVec3Pool par1Vec3Pool, double par2, double par4, double par6)
	{
		if(par2 == -0.0D)
		{
			par2 = 0.0D;
		}
		if(par4 == -0.0D)
		{
			par4 = 0.0D;
		}
		if(par6 == -0.0D)
		{
			par6 = 0.0D;
		}
		xCoord = par2;
		yCoord = par4;
		zCoord = par6;
		myVec3LocalPool = par1Vec3Pool;
	}
	
	protected BHSVec3 setComponents(double par1, double par3, double par5)
	{
		xCoord = par1;
		yCoord = par3;
		zCoord = par5;
		return this;
	}
	
	@SideOnly(Side.CLIENT)
	public BHSVec3 subtract(BHSVec3 par1Vec3)
	{
		return myVec3LocalPool.getVecFromPool(xCoord - xCoord, yCoord - yCoord, zCoord - zCoord);
	}
	
	public BHSVec3 normalize()
	{
		double var1 = MathHelper.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
		return var1 < 1.0E-4D ? myVec3LocalPool.getVecFromPool(0.0D, 0.0D, 0.0D) : myVec3LocalPool.getVecFromPool(xCoord / var1, yCoord / var1, zCoord / var1);
	}
	
	public double dotProduct(BHSVec3 par1Vec3)
	{
		return xCoord * xCoord + yCoord * yCoord + zCoord * zCoord;
	}
	
	@SideOnly(Side.CLIENT)
	public BHSVec3 crossProduct(BHSVec3 par1Vec3)
	{
		return myVec3LocalPool.getVecFromPool(yCoord * zCoord - zCoord * yCoord, zCoord * xCoord - xCoord * zCoord, xCoord * yCoord - yCoord * xCoord);
	}
	
	public BHSVec3 addVector(double par1, double par3, double par5)
	{
		return myVec3LocalPool.getVecFromPool(xCoord + par1, yCoord + par3, zCoord + par5);
	}
	
	public double distanceTo(BHSVec3 par1Vec3)
	{
		double var2 = xCoord - xCoord;
		double var4 = yCoord - yCoord;
		double var6 = zCoord - zCoord;
		return MathHelper.sqrt(var2 * var2 + var4 * var4 + var6 * var6);
	}
	
	public double squareDistanceTo(BHSVec3 par1Vec3)
	{
		double var2 = xCoord - xCoord;
		double var4 = yCoord - yCoord;
		double var6 = zCoord - zCoord;
		return var2 * var2 + var4 * var4 + var6 * var6;
	}
	
	public double squareDistanceTo(double par1, double par3, double par5)
	{
		double var7 = par1 - xCoord;
		double var9 = par3 - yCoord;
		double var11 = par5 - zCoord;
		return var7 * var7 + var9 * var9 + var11 * var11;
	}
	
	public double lengthVector()
	{
		return MathHelper.sqrt(xCoord * xCoord + yCoord * yCoord + zCoord * zCoord);
	}
	
	public BHSVec3 getIntermediateWithXValue(BHSVec3 par1Vec3, double par2)
	{
		double var4 = xCoord - xCoord;
		double var6 = yCoord - yCoord;
		double var8 = zCoord - zCoord;
		if(var4 * var4 < 1.0000000116860974E-7D)
		{
			return null;
		}
		double var10 = (par2 - xCoord) / var4;
		return (var10 >= 0.0D) && (var10 <= 1.0D) ? myVec3LocalPool.getVecFromPool(xCoord + var4 * var10, yCoord + var6 * var10, zCoord + var8 * var10) : null;
	}
	
	public BHSVec3 getIntermediateWithYValue(BHSVec3 par1Vec3, double par2)
	{
		double var4 = xCoord - xCoord;
		double var6 = yCoord - yCoord;
		double var8 = zCoord - zCoord;
		if(var6 * var6 < 1.0000000116860974E-7D)
		{
			return null;
		}
		double var10 = (par2 - yCoord) / var6;
		return (var10 >= 0.0D) && (var10 <= 1.0D) ? myVec3LocalPool.getVecFromPool(xCoord + var4 * var10, yCoord + var6 * var10, zCoord + var8 * var10) : null;
	}
	
	public BHSVec3 getIntermediateWithZValue(BHSVec3 par1Vec3, double par2)
	{
		double var4 = xCoord - xCoord;
		double var6 = yCoord - yCoord;
		double var8 = zCoord - zCoord;
		if(var8 * var8 < 1.0000000116860974E-7D)
		{
			return null;
		}
		double var10 = (par2 - zCoord) / var8;
		return (var10 >= 0.0D) && (var10 <= 1.0D) ? myVec3LocalPool.getVecFromPool(xCoord + var4 * var10, yCoord + var6 * var10, zCoord + var8 * var10) : null;
	}
	
	public String toString()
	{
		return "(" + xCoord + ", " + yCoord + ", " + zCoord + ")";
	}
	
	public void rotateAroundX(float par1)
	{
		float var2 = MathHelper.cos(par1);
		float var3 = MathHelper.sin(par1);
		double var4 = xCoord;
		double var6 = yCoord * var2 + zCoord * var3;
		double var8 = zCoord * var2 - yCoord * var3;
		xCoord = var4;
		yCoord = var6;
		zCoord = var8;
	}
	
	public void rotateAroundY(float par1)
	{
		float var2 = MathHelper.cos(par1);
		float var3 = MathHelper.sin(par1);
		double var4 = xCoord * var2 + zCoord * var3;
		double var6 = yCoord;
		double var8 = zCoord * var2 - xCoord * var3;
		xCoord = var4;
		yCoord = var6;
		zCoord = var8;
	}
	
	public void rotateAroundZ(float par1)
	{
		float var2 = MathHelper.cos(par1);
		float var3 = MathHelper.sin(par1);
		double var4 = xCoord * var2 + yCoord * var3;
		double var6 = yCoord * var2 - xCoord * var3;
		double var8 = zCoord;
		xCoord = var4;
		yCoord = var6;
		zCoord = var8;
	}
	
	public static class BHSVec3Pool
	{
		private final int truncateArrayResetThreshold;
		private final int minimumSize;
		private final List vec3Cache = new ArrayList();
		private int nextFreeSpace = 0;
		private int maximumSizeSinceLastTruncation = 0;
		private int resetCount = 0;
		
		public BHSVec3Pool(int par1, int par2)
		{
			truncateArrayResetThreshold = par1;
			minimumSize = par2;
		}
		
		public BHSVec3 getVecFromPool(double par1, double par3, double par5)
		{
			if(func_82589_e())
			{
				return new BHSVec3(this, par1, par3, par5);
			}
			BHSVec3 var7;
			if(nextFreeSpace >= vec3Cache.size())
			{
				var7 = new BHSVec3(this, par1, par3, par5);
				vec3Cache.add(var7);
			} else
			{
				var7 = (BHSVec3) vec3Cache.get(nextFreeSpace);
				var7.setComponents(par1, par3, par5);
			}
			nextFreeSpace += 1;
			return var7;
		}
		
		public void clear()
		{
			if(!func_82589_e())
			{
				if(nextFreeSpace > maximumSizeSinceLastTruncation)
				{
					maximumSizeSinceLastTruncation = nextFreeSpace;
				}
				if(resetCount++ == truncateArrayResetThreshold)
				{
					int var1 = Math.max(maximumSizeSinceLastTruncation, vec3Cache.size() - minimumSize);
					while(vec3Cache.size() > var1)
					{
						vec3Cache.remove(var1);
					}
					maximumSizeSinceLastTruncation = 0;
					resetCount = 0;
				}
				nextFreeSpace = 0;
			}
		}
		
		@SideOnly(Side.CLIENT)
		public void clearAndFreeCache()
		{
			if(!func_82589_e())
			{
				nextFreeSpace = 0;
				vec3Cache.clear();
			}
		}
		
		public int getPoolSize()
		{
			return vec3Cache.size();
		}
		
		public int func_82590_d()
		{
			return nextFreeSpace;
		}
		
		private boolean func_82589_e()
		{
			return (minimumSize < 0) || (truncateArrayResetThreshold < 0);
		}
	}
}
