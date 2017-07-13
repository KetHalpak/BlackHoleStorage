package com.pengu.holestorage.init;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import com.pengu.holestorage.Info;
import com.pengu.holestorage.blocks.BlockAtomicTransformer;
import com.pengu.holestorage.blocks.BlockBlackHole;
import com.pengu.holestorage.blocks.BlockBlackHoleFormer;
import com.pengu.holestorage.blocks.BlockBlackHoleStabilizer;
import com.pengu.holestorage.blocks.BlockFluidEjector;
import com.pengu.holestorage.blocks.BlockFluidInjector;
import com.pengu.holestorage.blocks.BlockRFEjector;
import com.pengu.holestorage.blocks.BlockRFInjector;
import com.pengu.holestorage.blocks.BlockWormhole;
import com.pengu.holestorage.blocks.BlockWormholeFormer;
import com.pengu.holestorage.tabs.CreativeTabBlackHoleStorage;

public class BlocksBHS
{
	public static final Block //
	        BLACK_HOLE = new BlockBlackHole(), //
	        BLACK_HOLE_FORMER = new BlockBlackHoleFormer(), //
	        BLACK_HOLE_STABILIZER = new BlockBlackHoleStabilizer(), //
	        RF_INJECTOR = new BlockRFInjector(), //
	        RF_EJECTOR = new BlockRFEjector(), //
	        FLUID_INJECTOR = new BlockFluidInjector(), //
	        FLUID_EJECTOR = new BlockFluidEjector(), //
	        ATOMIC_TRANSFORMER = new BlockAtomicTransformer(), //
	        WORMHOLE = new BlockWormhole(), //
	        WORMHOLE_FORMER = new BlockWormholeFormer();
}