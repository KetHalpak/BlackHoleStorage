package com.pengu.holestorage.bookmaking;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.annotations.MCFBus;
import com.pengu.hammercore.common.utils.WorldUtil;
import com.pengu.hammercore.net.HCNetwork;
import com.pengu.holestorage.BlackHoleStorage;
import com.pengu.holestorage.Info;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.net.PacketClientSpawnBookVortex;
import com.pengu.holestorage.vortex.BookcaseVortex;

@MCFBus
public class BookMakeHandler
{
	private static final Set<BookcaseVortex> active = new HashSet<>();
	
	@SubscribeEvent
	public void worldTick(WorldTickEvent evt)
	{
		Set<BookcaseVortex> removeWaiting = null;
		for(BookcaseVortex vortex : active)
			if(vortex.world.provider.getDimension() == evt.world.provider.getDimension() && !evt.world.isRemote && evt.world.isBlockLoaded(vortex.pos))
			{
				World w = evt.world;
				BlockPos p = vortex.pos;
				
				IBlockState state = w.getBlockState(p);
				
				if(state.getBlock() != Blocks.BOOKSHELF)
				{
					WorldUtil.spawnItemStack(w, p, new ItemStack(ItemsBHS.KNOWLEDGE_PARCHMENT));
					if(removeWaiting == null)
						removeWaiting = new HashSet<>();
					removeWaiting.add(vortex);
					continue;
				}
				
				if(vortex.ticksExisted++ >= 300) // 10 seconds
				{
					w.destroyBlock(p, false);
					WorldUtil.spawnItemStack(w, p, new ItemStack(ItemsBHS.KNOWLEDGE_TOME));
					
					if(removeWaiting == null)
						removeWaiting = new HashSet<>();
					removeWaiting.add(vortex);
					
					HammerCore.audioProxy.playSoundAt(w, Info.MOD_ID + ":crafted", p, .1F, 1F, SoundCategory.BLOCKS);
				}
			}
		if(removeWaiting != null)
			active.removeAll(removeWaiting);
	}
	
	public static boolean isCraftingAt(World world, BlockPos pos)
	{
		for(BookcaseVortex vortex : active)
			if(vortex.world.provider.getDimension() == world.provider.getDimension() && pos.equals(vortex.pos))
				return true;
		return false;
	}
	
	@SideOnly(Side.CLIENT)
	public static void startCrafting_client(BlockPos pos, int dim)
	{
		BlackHoleStorage.proxy.addParticleVortex(new BookcaseVortex(Minecraft.getMinecraft().world, pos));
	}
	
	public static void startCrafting(World world, BlockPos pos)
	{
		if(!world.isRemote)
		{
			active.add(new BookcaseVortex(world, pos));
			HCNetwork.manager.sendToDimension(new PacketClientSpawnBookVortex(pos, world.provider.getDimension()), world.provider.getDimension());
			HammerCore.audioProxy.playSoundAt(world, Info.MOD_ID + ":crafting", pos, .1F, 1F, SoundCategory.BLOCKS);
		}
	}
}