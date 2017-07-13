package com.pengu.holestorage.client;

import net.minecraft.util.ResourceLocation;

import com.pengu.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.bookAPI.BookPage;
import com.pengu.hammercore.client.UV;

/**
 * Created by MrDimka on 22.04.2017 at 15:48.
 */
public class BookPageImage extends BookPage
{
	public ResourceLocation image;
	public UV uv;
	
	public BookPageImage(BookEntry entry, ResourceLocation image)
	{
		super(entry);
		this.image = image;
	}
	
	@Override
	public void prepare()
	{
		uv = new UV(image, 0, 0, 256, 256);
	}
	
	@Override
	public void render(int mouseX, int mouseY)
	{
		uv.render(0, 0, 256, 256);
	}
}