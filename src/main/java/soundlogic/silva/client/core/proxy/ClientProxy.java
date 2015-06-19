package soundlogic.silva.client.core.proxy;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderEnderman;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import soundlogic.silva.client.core.handler.AlfheimPortalGuiHandler;
import soundlogic.silva.client.core.handler.BookConverter;
import soundlogic.silva.client.core.handler.ClientTickHandler;
import soundlogic.silva.client.core.handler.DimensionalExposureRenderHandler;
import soundlogic.silva.client.core.handler.DwarfChainRenderHandler;
import soundlogic.silva.client.core.handler.DwarvenTradeHUDRenderHandler;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.client.model.ModelFenrirEcho;
import soundlogic.silva.client.render.block.RenderBlazeFire;
import soundlogic.silva.client.render.block.RenderBoomMoss;
import soundlogic.silva.client.render.block.RenderDarkElfTrap;
import soundlogic.silva.client.render.block.RenderDwarvenPool;
import soundlogic.silva.client.render.block.RenderManaCrystal;
import soundlogic.silva.client.render.block.RenderManaEater;
import soundlogic.silva.client.render.block.RenderDust;
import soundlogic.silva.client.render.block.RenderPortalCore;
import soundlogic.silva.client.render.block.RenderPortalUpgradeCharge;
import soundlogic.silva.client.render.block.RenderPylon;
import soundlogic.silva.client.render.block.RenderSlingshot;
import soundlogic.silva.client.render.entity.RenderDwarvenChainKnot;
import soundlogic.silva.client.render.entity.RenderEntityStoneHorse;
import soundlogic.silva.client.render.entity.RenderFenrirEcho;
import soundlogic.silva.client.render.entity.RenderNidhogg;
import soundlogic.silva.client.render.multiblock.RenderMultiblock;
import soundlogic.silva.client.render.multiblock.RenderMultiblockCarnilotus;
import soundlogic.silva.client.render.multiblock.RenderMultiblockMysticalGrinder;
import soundlogic.silva.client.render.multiblock.RenderMultiblockPixieFarm;
import soundlogic.silva.client.render.tile.RenderTileBoomMoss;
import soundlogic.silva.client.render.tile.RenderTileDarkElfTrap;
import soundlogic.silva.client.render.tile.RenderTileDwarvenPool;
import soundlogic.silva.client.render.tile.RenderTileDwarvenSign;
import soundlogic.silva.client.render.tile.RenderTileEnchantPlate;
import soundlogic.silva.client.render.tile.RenderTileManaCrystal;
import soundlogic.silva.client.render.tile.RenderTileManaEater;
import soundlogic.silva.client.render.tile.RenderTileMultiblockCore;
import soundlogic.silva.client.render.tile.RenderTilePortalCore;
import soundlogic.silva.client.render.tile.RenderTilePortalUpgradeCharge;
import soundlogic.silva.client.render.tile.RenderTilePylon;
import soundlogic.silva.client.render.tile.RenderTileSlingshot;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileBoomMoss;
import soundlogic.silva.common.block.tile.TileDarkElfTrap;
import soundlogic.silva.common.block.tile.TileDwarvenPool;
import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TileEnchantPlate;
import soundlogic.silva.common.block.tile.TileManaCrystal;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.block.tile.TilePortalUpgradeCharge;
import soundlogic.silva.common.block.tile.TilePylon;
import soundlogic.silva.common.block.tile.TileSlingshot;
import soundlogic.silva.common.block.tile.multiblocks.TileMultiblockCore;
import soundlogic.silva.common.core.proxy.CommonProxy;
import soundlogic.silva.common.entity.EntityDwarvenBarrier;
import soundlogic.silva.common.entity.EntityDwarvenChainKnot;
import soundlogic.silva.common.entity.EntityFenrirEcho;
import soundlogic.silva.common.entity.EntityNidhoggEcho;
import soundlogic.silva.common.entity.EntityPhantomEndermanEcho;
import soundlogic.silva.common.entity.EntityStoneHorse;
import soundlogic.silva.common.item.ModItems;

public class ClientProxy extends CommonProxy{

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);

		FMLCommonHandler.instance().bus().register(new ClientTickHandler());
		MinecraftForge.EVENT_BUS.register(new DwarfChainRenderHandler());
		MinecraftForge.EVENT_BUS.register(new AlfheimPortalGuiHandler());
		DimensionalExposureRenderHandler dimensionalExposureRenderHandler = new DimensionalExposureRenderHandler();
		MinecraftForge.EVENT_BUS.register(dimensionalExposureRenderHandler);
		FMLCommonHandler.instance().bus().register(dimensionalExposureRenderHandler);
		FMLCommonHandler.instance().bus().register(new DwarvenTradeHUDRenderHandler());

		DimensionalExposureRenderHandler.defineGUILocation("", 0, 0, "", 0, false);
		DimensionalExposureRenderHandler.defineGUILocation("", 0, 0, "", 0, true);
		
	}

    @Override
    public void registerRenderers() {
    	
    	LibRenderIDs.idPylon = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idManaEater = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idBoomMoss = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDust = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idManaCrystal = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDwarvenPool = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idPortalUpgradeCharge = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idBlazeFire = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idDarkElfTrap = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idSlingshot = RenderingRegistry.getNextAvailableRenderId();
    	LibRenderIDs.idPortalCore = RenderingRegistry.getNextAvailableRenderId();
    	
    	RenderingRegistry.registerBlockHandler(new RenderPylon());
    	RenderingRegistry.registerBlockHandler(new RenderManaEater());
    	RenderingRegistry.registerBlockHandler(new RenderBoomMoss());
    	RenderingRegistry.registerBlockHandler(new RenderDust());
    	RenderingRegistry.registerBlockHandler(new RenderManaCrystal());
    	RenderingRegistry.registerBlockHandler(new RenderDwarvenPool());
    	RenderingRegistry.registerBlockHandler(new RenderPortalUpgradeCharge());
    	RenderingRegistry.registerBlockHandler(new RenderBlazeFire());
    	RenderingRegistry.registerBlockHandler(new RenderDarkElfTrap());
    	RenderingRegistry.registerBlockHandler(new RenderSlingshot());
    	RenderingRegistry.registerBlockHandler(new RenderPortalCore());
    	
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePylon.class, new RenderTilePylon());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileManaEater.class, new RenderTileManaEater());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileBoomMoss.class, new RenderTileBoomMoss());
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePortalCore.class, new RenderTilePortalCore());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDwarvenSign.class, new RenderTileDwarvenSign());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileManaCrystal.class, new RenderTileManaCrystal());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDwarvenPool.class, new RenderTileDwarvenPool());
    	ClientRegistry.bindTileEntitySpecialRenderer(TilePortalUpgradeCharge.class, new RenderTilePortalUpgradeCharge());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileMultiblockCore.class, new RenderTileMultiblockCore());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileEnchantPlate.class, new RenderTileEnchantPlate());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileDarkElfTrap.class, new RenderTileDarkElfTrap());
    	ClientRegistry.bindTileEntitySpecialRenderer(TileSlingshot.class, new RenderTileSlingshot());
    	
    	RenderingRegistry.registerEntityRenderingHandler(EntityStoneHorse.class, new RenderEntityStoneHorse());
    	RenderingRegistry.registerEntityRenderingHandler(EntityDwarvenBarrier.class, new RenderSnowball(ModItems.dwarfBarrier));
    	RenderingRegistry.registerEntityRenderingHandler(EntityDwarvenChainKnot.class, new RenderDwarvenChainKnot());
    	RenderingRegistry.registerEntityRenderingHandler(EntityFenrirEcho.class, new RenderFenrirEcho(new ModelFenrirEcho(), new ModelFenrirEcho(), 0.5F));
    	RenderingRegistry.registerEntityRenderingHandler(EntityPhantomEndermanEcho.class, new RenderEnderman());
    	RenderingRegistry.registerEntityRenderingHandler(EntityNidhoggEcho.class, new RenderNidhogg());
    	
    	RenderMultiblock.registerRenderer(new RenderMultiblockCarnilotus(), "carnilotus");
    	RenderMultiblock.registerRenderer(new RenderMultiblockMysticalGrinder(), "mysticalGrinder");
    	RenderMultiblock.registerRenderer(new RenderMultiblockPixieFarm(), "pixieFarm");
    }
        
    @Override
    public int getTicks() {
    	return ClientTickHandler.ticks;
    }

    @Override
	public void convertBooks() {
		try {
			BookConverter.convertBooks();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
	public void setShader(ResourceLocation loc) {
		Minecraft mc = Minecraft.getMinecraft();
		EntityRenderer rend = mc.entityRenderer;
		if(!OpenGlHelper.shadersSupported)
			return;
		if(loc==null) {
			if(rend.isShaderActive())
				rend.deactivateShader();
			return;
		}
		if(rend.isShaderActive() && rend.getShaderGroup().getShaderGroupName().equals(loc.toString()))
			return;
        try {
			rend.theShaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), loc);
	        rend.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight);
		} catch (JsonException e) {
			e.printStackTrace();
		}
	}
}
