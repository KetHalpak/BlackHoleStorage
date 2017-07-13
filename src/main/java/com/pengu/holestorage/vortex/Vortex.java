package com.pengu.holestorage.vortex;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;

public class Vortex implements ITickable
{
	private final int hashCode;
	protected double x, y, z, vortexStrenght;
	protected double radius = 1;
	protected AxisAlignedBB boundingBox;
	
	public Vortex(double x, double y, double z, double vortexStrenght, boolean includeStrenghtInHash)
	{
		hashCode = (x + "," + y + "," + z + "x" + (includeStrenghtInHash ? vortexStrenght : 0)).hashCode();
		this.vortexStrenght = vortexStrenght;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vortex(double x, double y, double z, double vortexStrenght, double radius, boolean includeStrenghtInHash)
	{
		this(x, y, z, vortexStrenght, includeStrenghtInHash);
		this.radius = radius;
		rebuildBoundingBox();
	}
	
	public AxisAlignedBB rebuildBoundingBox()
	{
		return boundingBox = new AxisAlignedBB(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
	}
	
	public AxisAlignedBB getBoundingBox()
	{
		if(boundingBox == null // If box is null
		        || boundingBox.maxX - boundingBox.minX != radius * 2 // If
		                                                             // radius
		                                                             // changed
		        || boundingBox.maxX - radius != x || boundingBox.maxY - radius != y || boundingBox.maxZ - radius != z) // If
		                                                                                                               // position
		                                                                                                               // changed
			rebuildBoundingBox();
		return boundingBox;
	}
	
	public double getX()
	{
		return x;
	}
	
	public double getY()
	{
		return y;
	}
	
	public double getZ()
	{
		return z;
	}
	
	public double getVortexStrenght()
	{
		return vortexStrenght;
	}
	
	public int getHashCode()
	{
		return hashCode;
	}
	
	@Override
	public void update()
	{
		
	}
	
	@Override
	public int hashCode()
	{
		return getHashCode();
	}
}