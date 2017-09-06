package com.pengu.holestorage.init;

import com.pengu.holestorage.items.ItemKnowledgeParchment;
import com.pengu.holestorage.items.ItemKnowledgeTome;
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
	        KNOWLEDGE_TOME = new ItemKnowledgeTome(), //
	        KNOWLEDGE_PARCHMENT = new ItemKnowledgeParchment(), //
	        ITEM_UNOBTAINABLE = new ItemUnobtainable();
}