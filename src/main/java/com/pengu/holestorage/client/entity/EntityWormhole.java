package com.pengu.holestorage.client.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.math.MathHelper;
import com.pengu.holestorage.blocks.BlockWormhole;
import com.pengu.holestorage.client.tesr.TileRenderWormhole;

@SideOnly(Side.CLIENT)
public class EntityWormhole extends Entity
{
	private Minecraft mc = Minecraft.getMinecraft();
	
	private int facing;
	public boolean rendering;
	
	public EntityWormhole(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityWormhole(World worldIn, double x, double y, double z, EnumFacing facing)
	{
		super(worldIn);
		facing = facing.getOpposite();
		this.facing = facing.ordinal();
		this.noClip = true;
		this.height = 0.001F;
		this.width = 0.001F;
		setPostionConsideringRotation(x, y, z, facing.ordinal());
	}
	
	public void setPostionConsideringRotation(double x, double y, double z, int rotation)
	{
		EnumFacing face = EnumFacing.values()[rotation];
		rotation = face.getHorizontalIndex();
		double offset = -0.43;
		switch(rotation)
		{
		case 2:
			z += offset;
		break;
		case 0:
			z -= offset;
		break;
		case 3:
			x -= offset;
		break;
		case 1:
			x += offset;
		break;
		}
		setPosition(x + 0.5D, y + 0.5, z + 0.5D);
	}
	
	@Override
	protected void entityInit()
	{
		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void onUpdate()
	{
		super.onUpdate();
		
		if(rendering)
		{
			double dy = this.posZ - mc.player.posZ;
			double dx = this.posX - mc.player.posX;
			double angleYaw = Math.atan2(dy, dx) * (180D / Math.PI);
			
			EnumFacing face = EnumFacing.values()[this.facing];
			int facing = face.getHorizontalIndex();
			
			if(facing == 1)
			{
				angleYaw -= 90D;
				if(angleYaw <= -180D)
					angleYaw = 360D + angleYaw;
			}
			
			if(facing > 1)
				angleYaw += 360D - (facing * 90D);
			
			if(angleYaw >= 135D)
				angleYaw = 135D;
			
			if(angleYaw <= 45D)
				angleYaw = 45D;
			
			this.rotationYaw = (float) (-90F + facing * 90F - angleYaw);
			
			double distance = getDistanceToEntity(mc.player);
			double height = (mc.player.getEyeHeight() + mc.player.posY) - this.posY;
			double anglePitch = Math.atan2(height, distance) * (180D / Math.PI);
			
			anglePitch = MathHelper.clip(anglePitch, -45, 45);
			
			this.rotationPitch = (float) anglePitch;
			if(face == EnumFacing.DOWN) this.rotationPitch = -90;
			if(face == EnumFacing.UP) this.rotationPitch = 90;
		}
		
		if(!(world.getBlockState(getPosition().down()).getBlock() instanceof BlockWormhole))
		{
			TileRenderWormhole.removeRegisteredWormhole(this);
			this.setDead();
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldRenderInPass(int pass)
	{
		return true;
	}
	
	@Override
	protected void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		
	}
	
	@Override
	protected void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		
	}
}