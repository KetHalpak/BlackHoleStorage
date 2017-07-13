package com.pengu.holestorage;

import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.EnumFacing;

public class Info
{
	public static final String MOD_ID = "blackholestorage", MOD_NAME = "Black Hole Storage", MOD_VERSION = "@VERSION@";
	
	public static final String PROXY_BASE = "com.pengu.holestorage.proxy", PROXY_CLIENT = PROXY_BASE + ".ClientProxy", PROXY_SERVER = PROXY_BASE + ".CommonProxy";
	
	public static final PropertyEnum<EnumFacing> FACING_UD = PropertyEnum.create("facing", EnumFacing.class, EnumFacing.UP, EnumFacing.DOWN);
	public static final PropertyEnum<EnumFacing> FACING_UDEWSN = PropertyEnum.create("facing", EnumFacing.class);
}