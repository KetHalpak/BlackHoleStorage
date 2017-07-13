package com.pengu.holestorage.client;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.pengu.hammercore.common.utils.Copyable;
import com.pengu.hammercore.math.MathHelper;
import com.pengu.hammercore.vec.Vector3;

public class Quat implements Copyable<Quat>
{
	public double x;
	public double y;
	public double z;
	public double s;
	
	public Quat()
	{
		s = 1.0D;
		x = 0.0D;
		y = 0.0D;
		z = 0.0D;
	}
	
	public Quat(Quat quat)
	{
		this.x = quat.x;
		this.y = quat.y;
		this.z = quat.z;
		this.s = quat.s;
	}
	
	public Quat(double d, double d1, double d2, double d3)
	{
		x = d1;
		y = d2;
		z = d3;
		s = d;
	}
	
	public Quat set(Quat quat)
	{
		this.x = quat.x;
		this.y = quat.y;
		this.z = quat.z;
		this.s = quat.s;
		return this;
	}
	
	public Quat set(double d, double d1, double d2, double d3)
	{
		x = d1;
		y = d2;
		z = d3;
		s = d;
		
		return this;
	}
	
	public static Quat aroundAxis(double ax, double ay, double az, double angle)
	{
		return new Quat().setAroundAxis(ax, ay, az, angle);
	}
	
	public static Quat aroundAxis(Vector3 axis, double angle)
	{
		return aroundAxis(axis.x, axis.y, axis.z, angle);
	}
	
	public Quat setAroundAxis(double ax, double ay, double az, double angle)
	{
		angle *= 0.5D;
		double d4 = MathHelper.sin(angle);
		return set(MathHelper.cos(angle), ax * d4, ay * d4, az * d4);
	}
	
	public Quat setAroundAxis(Vector3 axis, double angle)
	{
		return setAroundAxis(x, y, z, angle);
	}
	
	public Quat multiply(Quat quat)
	{
		double d = s * s - x * x - y * y - z * z;
		double d1 = s * x + x * s - y * z + z * y;
		double d2 = s * y + x * z + y * s - z * x;
		double d3 = s * z - x * y + y * x + z * s;
		s = d;
		x = d1;
		y = d2;
		z = d3;
		
		return this;
	}
	
	public Quat rightMultiply(Quat quat)
	{
		double d = s * s - x * x - y * y - z * z;
		double d1 = s * x + x * s + y * z - z * y;
		double d2 = s * y - x * z + y * s + z * x;
		double d3 = s * z + x * y - y * x + z * s;
		s = d;
		x = d1;
		y = d2;
		z = d3;
		
		return this;
	}
	
	public double mag()
	{
		return Math.sqrt(x * x + y * y + z * z + s * s);
	}
	
	public Quat normalize()
	{
		double d = mag();
		if(d != 0.0D)
		{
			d = 1.0D / d;
			x *= d;
			y *= d;
			z *= d;
			s *= d;
		}
		return this;
	}
	
	public Quat copy()
	{
		return new Quat(this);
	}
	
	public void rotate(Vector3 vec)
	{
		double d = -x * x - y * y - z * z;
		double d1 = s * x + y * z - z * y;
		double d2 = s * y - x * z + z * x;
		double d3 = s * z + x * y - y * x;
		x = (d1 * s - d * x - d2 * z + d3 * y);
		y = (d2 * s - d * y + d1 * z - d3 * x);
		z = (d3 * s - d * z - d1 * y + d2 * x);
	}
	
	public String toString()
	{
		MathContext cont = new MathContext(4, RoundingMode.HALF_UP);
		return "Quat(" + new BigDecimal(s, cont) + ", " + new BigDecimal(x, cont) + ", " + new BigDecimal(y, cont) + ", " + new BigDecimal(z, cont) + ")";
	}
}