package com.pengu.holestorage.proxy;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import com.pengu.hammercore.HammerCore;
import com.pengu.hammercore.bookAPI.Book;
import com.pengu.hammercore.bookAPI.BookCategory;
import com.pengu.hammercore.bookAPI.BookEntry;
import com.pengu.hammercore.bookAPI.BookPage;
import com.pengu.hammercore.bookAPI.pages.BookPageTextPlain;
import com.pengu.hammercore.client.particle.RecipeRenderer;
import com.pengu.hammercore.client.render.item.ItemRenderingHandler;
import com.pengu.hammercore.gui.book.GuiBookEntry;
import com.pengu.holestorage.Info;
import com.pengu.holestorage.api.atomictransformer.AtomicTransformerRecipes;
import com.pengu.holestorage.api.atomictransformer.SimpleTransformerRecipe;
import com.pengu.holestorage.client.BookPageImage;
import com.pengu.holestorage.client.BookPageTextAndRecipes;
import com.pengu.holestorage.client.Render3D;
import com.pengu.holestorage.client.particle.ParticleEnergyFX;
import com.pengu.holestorage.client.particle.ParticleLiquid;
import com.pengu.holestorage.client.particle.ParticleLiquidTextured;
import com.pengu.holestorage.client.render.item.ItemRenderAntiMatter;
import com.pengu.holestorage.client.tesr.TileRenderAtomicTransformer;
import com.pengu.holestorage.client.tesr.TileRenderBlackHole;
import com.pengu.holestorage.client.tesr.TileRenderBlackHoleFormer;
import com.pengu.holestorage.client.tesr.TileRenderWormhole;
import com.pengu.holestorage.client.tesr.TileRenderWormholeFormer;
import com.pengu.holestorage.init.BlocksBHS;
import com.pengu.holestorage.init.ItemsBHS;
import com.pengu.holestorage.tile.TileAtomicTransformer;
import com.pengu.holestorage.tile.TileBlackHole;
import com.pengu.holestorage.tile.TileBlackHoleFormer;
import com.pengu.holestorage.tile.TileWormhole;
import com.pengu.holestorage.tile.TileWormholeFormer;
import com.pengu.holestorage.vortex.Vortex;

public class ClientProxy extends CommonProxy
{
	public static boolean rendering = false;
	public static Entity renderEntity = null;
	public static Entity backupEntity = null;
	
	public static final Book tome = new Book(Info.MOD_ID + ":tome");
	
	public static Set<Vortex> particleVortex = new HashSet<>();
	
	{
		tome.customBackground = new ResourceLocation(Info.MOD_ID, "textures/gui/tome_gui.png");
		tome.customEntryBackground = new ResourceLocation(Info.MOD_ID, "textures/gui/tome_entry.png");
	}
	
	@Override
	public void init()
	{
		HammerCore.bookProxy.registerBookInstance(tome);
		ItemRenderingHandler.INSTANCE.bindItemRender(ItemsBHS.ANTI_MATTER, new ItemRenderAntiMatter());
		
		createBasicsCategory(new BookCategory(tome, "basics"));
		createBlackHoleCategory(new BookCategory(tome, "black_hole"));
		
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHoleFormer.class, new TileRenderBlackHoleFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileAtomicTransformer.class, new TileRenderAtomicTransformer());
		{
			TileRenderWormhole w;
			ClientRegistry.bindTileEntitySpecialRenderer(TileWormhole.class, w = new TileRenderWormhole());
			MinecraftForge.EVENT_BUS.register(w);
		}
		ClientRegistry.bindTileEntitySpecialRenderer(TileWormholeFormer.class, new TileRenderWormholeFormer());
		ClientRegistry.bindTileEntitySpecialRenderer(TileBlackHole.class, new TileRenderBlackHole());
	}
	
	private void createBasicsCategory(BookCategory cat)
	{
		cat.setIcon(new ItemStack(ItemsBHS.KNOWLEDGE_TOME));
		
		{
			String name = "intro";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(Items.PAPER));
			
			new BookPageTextPlain(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0");
		}
		
		{
			String name = "atomic_transformer";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.ATOMIC_TRANSFORMER)).setRecipePos(48, 84);
		}
		
		{
			String name = "atomic_transformer_recipes";
			BookEntryATRecipes entry = new BookEntryATRecipes(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(ItemsBHS.DARK_MATTER));
			entry.rebakeRecipes();
		}
		
		{
			String name = "anti_matter";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(ItemsBHS.ANTI_MATTER));
			
			new BookPageTextPlain(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0");
		}
	}
	
	private void createBlackHoleCategory(BookCategory cat)
	{
		cat.setIcon(new ItemStack(BlocksBHS.BLACK_HOLE));
		
		{
			String name = "about";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.BLACK_HOLE));
			
			new BookPageTextPlain(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0");
		}
		
		{
			String name = "black_hole_former";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.BLACK_HOLE_FORMER));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.BLACK_HOLE_FORMER)).setRecipePos(48, 84);
		}
		
		{
			String name = "black_hole_stabilizer";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.BLACK_HOLE_STABILIZER));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.BLACK_HOLE_STABILIZER)).setRecipePos(48, 84);
			new BookPageImage(entry, new ResourceLocation(Info.MOD_ID, "textures/screenshots/" + name + ".png"));
		}
		
		{
			String name = "rf_injector";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.RF_INJECTOR));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.RF_INJECTOR)).setRecipePos(48, 84);
			new BookPageImage(entry, new ResourceLocation(Info.MOD_ID, "textures/screenshots/" + name + ".png"));
		}
		
		{
			String name = "rf_ejector";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.RF_EJECTOR));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.RF_EJECTOR)).setRecipePos(48, 84);
			new BookPageImage(entry, new ResourceLocation(Info.MOD_ID, "textures/screenshots/" + name + ".png"));
		}
		
		{
			String name = "fluid_injector";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.FLUID_INJECTOR));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.FLUID_INJECTOR)).setRecipePos(64, 90);
			new BookPageImage(entry, new ResourceLocation(Info.MOD_ID, "textures/screenshots/" + name + ".png"));
		}
		
		{
			String name = "fluid_ejector";
			BookEntry entry = new BookEntry(cat, name, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".title");
			entry.setIcon(new ItemStack(BlocksBHS.FLUID_EJECTOR));
			
			new BookPageTextAndRecipes(entry, "bookapi." + Info.MOD_ID + ":" + cat.categoryId + ":" + name + ".page0", new ItemStack(BlocksBHS.FLUID_EJECTOR)).setRecipePos(48, 84);
			new BookPageImage(entry, new ResourceLocation(Info.MOD_ID, "textures/screenshots/" + name + ".png"));
		}
	}
	
	@SubscribeEvent
	public void guiOpen(GuiOpenEvent evt)
	{
		GuiScreen gui = evt.getGui();
		if(gui instanceof GuiBookEntry)
		{
			GuiBookEntry entry = (GuiBookEntry) gui;
			if(entry.entry instanceof BookEntryATRecipes)
				((BookEntryATRecipes) entry.entry).rebakeRecipes();
		}
	}
	
	@SubscribeEvent
	public void onClientWorldLoad(WorldEvent.Load event)
	{
		if(event.getWorld() instanceof WorldClient)
		{
			TileRenderWormhole.wormholeGlobalRenderer.setWorldAndLoadRenderers((WorldClient) event.getWorld());
		}
	}
	
	@SubscribeEvent
	public void onClientWorldUnload(WorldEvent.Unload event)
	{
		if(event.getWorld() instanceof WorldClient)
		{
			TileRenderWormhole.clearRegisteredWormholes();
		}
	}
	
	@SubscribeEvent
	public void onPrePlayerRender(RenderPlayerEvent.Pre event)
	{
		if(!rendering)
			return;
		
		if(event.getEntityPlayer() == renderEntity)
		{
			this.backupEntity = Minecraft.getMinecraft().getRenderManager().renderViewEntity;
			Minecraft.getMinecraft().getRenderManager().renderViewEntity = renderEntity;
		}
	}
	
	@SubscribeEvent
	public void onPostPlayerRender(RenderPlayerEvent.Post event)
	{
		if(!rendering)
			return;
		
		if(event.getEntityPlayer() == renderEntity)
		{
			Minecraft.getMinecraft().getRenderManager().renderViewEntity = backupEntity;
			renderEntity = null;
		}
	}
	
	@Override
	public void preInit()
	{
		OBJLoader.INSTANCE.addDomain(Info.MOD_ID);
		MinecraftForge.EVENT_BUS.register(new Render3D());
	}
	
	@Override
	public void spawnEnergyFX(World w, double x, double y, double z, double tx, double ty, double tz, int rf)
	{
		if(w == null)
			return;
		if(w.isRemote)
			new ParticleEnergyFX(w, x, y, z, tx, ty, tz, rf).spawn();
		else
			super.spawnEnergyFX(w, x, y, z, tx, ty, tz, rf);
	}
	
	@Override
	public void spawnLiquidFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, int color, float scale, int extend)
	{
		if(w == null)
			return;
		if(w.isRemote)
		{
			ParticleLiquid p = new ParticleLiquid(w, x, y, z, tx, ty, tz, count, color, scale, extend);
			Minecraft.getMinecraft().effectRenderer.addEffect(p);
		} else
			super.spawnLiquidFX(w, x, y, z, tx, ty, tz, count, color, scale, extend);
	}
	
	@Override
	public void spawnLiquidTexturedFX(World w, double x, double y, double z, double tx, double ty, double tz, int count, FluidStack stack, float scale, int extend)
	{
		if(w == null)
			return;
		if(w.isRemote)
		{
			ParticleLiquidTextured p = new ParticleLiquidTextured(w, x, y, z, tx, ty, tz, count, stack, scale, extend);
			Minecraft.getMinecraft().effectRenderer.addEffect(p);
		} else
			super.spawnLiquidTexturedFX(w, x, y, z, tx, ty, tz, count, stack, scale, extend);
	}
	
	@Override
	public void addParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0 || particleVortex.contains(vortex))
			return;
		Set particleVortex = new HashSet<>(this.particleVortex);
		particleVortex.add(vortex);
		this.particleVortex = particleVortex;
	}
	
	@Override
	public void removeParticleVortex(Vortex vortex)
	{
		if(vortex == null || vortex.getVortexStrenght() == 0 || !particleVortex.contains(vortex))
			return;
		Set particleVortex = new HashSet<>(this.particleVortex);
		particleVortex.remove(vortex);
		this.particleVortex = particleVortex;
	}
	
	private static class BookEntryATRecipes extends BookEntry
	{
		public BookEntryATRecipes(BookCategory category, String id, String title)
		{
			super(category, id, title);
		}
		
		public void rebakeRecipes()
		{
			pages.clear();
			for(SimpleTransformerRecipe recipe : AtomicTransformerRecipes.getRecipes())
			{
				pages.add(new BookPage(this)
				{
					@Override
					public void render(int mouseX, int mouseY)
					{
						RenderItem ri = Minecraft.getMinecraft().getRenderItem();
						try
						{
							RenderHelper.enableGUIStandardItemLighting();
							
							ItemStack renderStack = recipe.getInputSubtypes().get(RecipeRenderer.getInRange(500, 0, recipe.getInputSubtypes().size()));
							ri.renderItemAndEffectIntoGUI(renderStack, 16, 64);
							ri.renderItemAndEffectIntoGUI(recipe.getOutput(renderStack), 16, 96);
							
							Minecraft.getMinecraft().fontRenderer.drawString(I18n.translateToLocal("gui." + Info.MOD_ID + ":rf.required") + ": " + String.format("%,d", recipe.getEnergyUsed(renderStack)), 40, 68, 0, false);
							
							String input = I18n.translateToLocal("gui." + Info.MOD_ID + ":input") + ": " + recipe.getInputItemCount() + "x " + renderStack.getDisplayName();
							
							boolean cut = false;
							while(Minecraft.getMinecraft().fontRenderer.getStringWidth(input) > 200)
							{
								input = input.substring(0, input.length() - 1);
								cut = true;
							}
							
							if(cut)
								input += "...";
							
							Minecraft.getMinecraft().fontRenderer.drawString(input, 40, 80, 0, false);
							
							input = I18n.translateToLocal("gui." + Info.MOD_ID + ":output") + ": " + recipe.getOutput(renderStack).getCount() + "x " + recipe.getOutput(renderStack).getDisplayName();
							
							cut = false;
							while(Minecraft.getMinecraft().fontRenderer.getStringWidth(input) > 200)
							{
								input = input.substring(0, input.length() - 1);
								cut = true;
							}
							
							if(cut)
								input += "...";
							
							Minecraft.getMinecraft().fontRenderer.drawString(input, 40, 92, 0, false);
						} catch(Throwable err)
						{
							err.printStackTrace();
						}
					}
					
					public String toString()
					{
						return recipe.getOutputItem().getDisplayName();
					}
				});
			}
			
			for(int i = pages.size() - 1; i >= 0; --i)
				if(i % 2 == 0)
					pages.remove(i);
			
			BookPage[] pgs = pages.toArray(new BookPage[pages.size()]);
			Arrays.sort(pgs, new Comparator<BookPage>()
			{
				@Override
				public int compare(BookPage o1, BookPage o2)
				{
					return o1.toString().compareTo(o2.toString());
				}
			});
			
			pages.clear();
			pages.addAll(Arrays.asList(pgs));
		}
	}
}