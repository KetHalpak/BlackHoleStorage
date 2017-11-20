package com.pengu.holestorage.init;

import com.pengu.holestorage.items.ItemUnobtainable;
import com.pengu.holestorage.items.ItemWormholePearl;

import net.minecraft.item.Item;

public class ItemsBHS
{
	public static final Item //
	FORMER_RING = new Item().setUnlocalizedName("former_ring"), //
	        DARK_MATTER = new Item().setUnlocalizedName("dark_matter"), //
	        ANTI_MATTER = new Item().setUnlocalizedName("anti_matter"), //
	        WORMHOLE_PEARL = new ItemWormholePearl(), //
	        BLACK_HOLE_SHIELD = new ItemUnobtainable("black_hole_shield"), //
	        ANTI_MATTER_CORE = new ItemUnobtainable("anti_matter_core"), //
	        ANTI_MATTER_POSITRON_1 = new ItemUnobtainable("anti_matter_positron_1"), //
	        ANTI_MATTER_POSITRON_2 = new ItemUnobtainable("anti_matter_positron_2"), //
	        ANTI_MATTER_POSITRON_3 = new ItemUnobtainable("anti_matter_positron_3");
}