package com.pengu.holestorage.intr.waila;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;

import com.pengu.holestorage.Info;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.tile.TileAtomicTransformer;
import com.pengu.holestorage.tile.TileBlackHoleFormer;
import com.pengu.holestorage.tile.TileRFEjector;

public class BHSWAILA implements IWailaDataProvider
{
	public final DecimalFormat format = new DecimalFormat("#0,00");
	
	public static void registerWAIA(IWailaRegistrar reg)
	{
		BHSWAILA pump = new BHSWAILA();
		reg.registerBodyProvider(pump, Block.class);
	}
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return accessor.getStack();
	}
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		try
		{
			TileEntity te = accessor.getTileEntity();
			
			if(te instanceof TileBlackHoleFormer)
				try
				{
					TileBlackHoleFormer former = (TileBlackHoleFormer) te;
					double progress = former.EnergyStored.divide(former.ABSORBED).doubleValue() * 100D;
					currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":progress") + ": " + String.format("%.002f", progress) + "%");
				} catch(Throwable err)
				{
					err.printStackTrace();
				}
			
			if(te instanceof TileAtomicTransformer)
			{
				TileAtomicTransformer t = (TileAtomicTransformer) te;
				SimpleTransformerRecipe r = t.recipe;
				
				if(r != null)
				{
					currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.required") + ": " + String.format("%,d", r.getEnergyUsed(t.inventory.getStackInSlot(0))) + " RF");
					currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.stored") + ": " + String.format("%,d", t.stored) + " RF");
					currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":input") + ": " + r.getInputItemCount() + "x " + t.getStackInSlot(0).getDisplayName());
					currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":output") + ": " + r.getOutputItem().getDisplayName());
					
					try
					{
						double progress = new BigDecimal(t.stored).divide(new BigDecimal(r.getEnergyUsed(t.inventory.getStackInSlot(0)))).doubleValue() * 100D;
						
						currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":progress") + ": " + format.format(progress * 100D) + "%");
					} catch(Throwable err)
					{
					}
				}
			}
			
			if(te instanceof TileRFEjector)
			{
				TileRFEjector t = (TileRFEjector) te;
				currenttip.add(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.stored") + ":");
				currenttip.add(t.rfStored + " RF");
			}
			
		} catch(Throwable err)
		{
		}
		return currenttip;
	}
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return currenttip;
	}
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)
	{
		return tag;
	}
}