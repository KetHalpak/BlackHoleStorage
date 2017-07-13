package com.pengu.holestorage.api.hole;

import java.math.BigInteger;

public interface IBlackHole
{
	public Object receiveContent(BlackHolePacket packet, boolean simulate);
	
	public Object sendContent(BigInteger amount, boolean simulate);
	
	public IBlackHoleStorage getStorage();
	
	public void setStorage(IBlackHoleStorage storage);
}