package com.pengu.holestorage.items;

import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

import com.pengu.hammercore.HammerCore;

public class ItemWormholePearl extends Item
{
	public ItemWormholePearl()
	{
		setUnlocalizedName("wormholepearl");
		setMaxStackSize(1);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt == null)
			stack.setTagCompound(nbt = new NBTTagCompound());
		
		if(!player.isSneaking())
		{
			nbt.setInteger("Dim", worldIn.provider.getDimension());
			pos = pos.offset(facing);
			nbt.setInteger("x", pos.getX());
			nbt.setInteger("y", pos.getY());
			nbt.setInteger("z", pos.getZ());
			nbt.setByte("Face", (byte) facing.ordinal());
			
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, SoundEvents.ENTITY_PLAYER_LEVELUP.getRegistryName().toString(), pos, .25F, 1.8F, SoundCategory.PLAYERS);
			
			return EnumActionResult.FAIL;
		}
		
		return EnumActionResult.PASS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if(hasEffect(stack))
		{
			NBTTagCompound nbt = stack.getTagCompound();
			tooltip.add("Dimension: " + nbt.getInteger("Dim"));
			tooltip.add("Facing: " + EnumFacing.values()[nbt.getByte("Face")]);
			tooltip.add("X: " + nbt.getInteger("x") + ", Y: " + nbt.getInteger("y") + ", Z: " + nbt.getInteger("z"));
		}
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack stack = playerIn.getHeldItem(handIn);
		NBTTagCompound nbt = stack.getTagCompound();
		if(playerIn.isSneaking() && nbt != null && nbt.hasKey("Dim", NBT.TAG_INT) && nbt.hasKey("x", NBT.TAG_INT) && nbt.hasKey("y", NBT.TAG_INT) && nbt.hasKey("z", NBT.TAG_INT))
		{
			nbt.removeTag("Dim");
			nbt.removeTag("x");
			nbt.removeTag("y");
			nbt.removeTag("z");
			nbt.removeTag("Face");
			
			if(!worldIn.isRemote)
				HammerCore.audioProxy.playSoundAt(worldIn, SoundEvents.ENTITY_PLAYER_ATTACK_CRIT.getRegistryName().toString(), playerIn.getPosition(), .25F, 1.5F, SoundCategory.PLAYERS);
			
			playerIn.swingArm(handIn);
		}
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}
	
	@Override
	public float getStrVsBlock(ItemStack stack, IBlockState state)
	{
		return 0;
	}
	
	@Override
	public boolean hasEffect(ItemStack stack)
	{
		NBTTagCompound nbt = stack.getTagCompound();
		return nbt != null && nbt.hasKey("Dim", NBT.TAG_INT) && nbt.hasKey("x", NBT.TAG_INT) && nbt.hasKey("y", NBT.TAG_INT) && nbt.hasKey("z", NBT.TAG_INT) && nbt.hasKey("Face", NBT.TAG_BYTE);
	}
}