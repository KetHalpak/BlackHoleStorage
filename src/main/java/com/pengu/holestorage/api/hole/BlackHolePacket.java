package com.pengu.holestorage.api.hole;

import net.minecraftforge.common.util.EnumHelper;

import com.pengu.holestorage.api.hole.impl.BlackHoleStorageFluid;
import com.pengu.holestorage.api.hole.impl.BlackHoleStorageRF;

public class BlackHolePacket<T, OPT>
{
	public final T stored;
	public final OPT[] optionals;
	public final EnumBlackHolePacketType type;
	
	public BlackHolePacket(T main, EnumBlackHolePacketType type, OPT... opts)
	{
		stored = main;
		optionals = opts;
		this.type = type;
	}
	
	public Class<?> getStoredClass()
	{
		return stored.getClass();
	}
	
	public static enum EnumBlackHolePacketType
	{
		RF(BlackHoleStorageRF.class), FLUID(BlackHoleStorageFluid.class);
		
		private final Class<? extends IBlackHoleStorage> storage;
		
		private EnumBlackHolePacketType(Class<? extends IBlackHoleStorage> storage)
		{
			this.storage = storage;
		}
		
		public IBlackHoleStorage createStorage()
		{
			try
			{
				return storage.newInstance();
			} catch(Throwable err)
			{
				return null;
			}
		}
		
		public static EnumBlackHolePacketType create(String name, Class<? extends IBlackHoleStorage> storage)
		{
			EnumBlackHolePacketType pkt = valueOf(name.toUpperCase());
			if(pkt != null)
				return pkt;
			return EnumHelper.addEnum(EnumBlackHolePacketType.class, name.toUpperCase(), new Class[] { Class.class }, storage);
		}
	}
}