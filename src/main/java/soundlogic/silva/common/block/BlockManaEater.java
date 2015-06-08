package soundlogic.silva.common.block;

import java.util.List;
import java.util.Random;

import cpw.mods.fml.common.registry.GameRegistry;
import soundlogic.silva.client.lib.LibRenderIDs;
import soundlogic.silva.common.Silva;
import soundlogic.silva.common.block.tile.TileManaEater;
import soundlogic.silva.common.lexicon.LexiconData;
import soundlogic.silva.common.lib.LibBlockNames;
import vazkii.botania.api.lexicon.ILexiconable;
import vazkii.botania.api.lexicon.LexiconEntry;
import vazkii.botania.api.mana.ILens;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.api.wand.IWireframeAABBProvider;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class BlockManaEater extends BlockContainer implements IWandable, IWandHUD, ILexiconable, IWireframeAABBProvider {

	Random random;

	public BlockManaEater() {
		super(Material.wood);
		setHardness(2.0F);
		setStepSound(soundTypeWood);
		setBlockName(LibBlockNames.MANA_EATER);
		setCreativeTab(Silva.creativeTab);

		random = new Random();
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2, List par3) {
		for(int i = 0; i < 1; i++)
			par3.add(new ItemStack(par1, 1, i));
	}
	
	@Override
	public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		int orientation = BlockPistonBase.determineOrientation(par1World, par2, par3, par4, par5EntityLivingBase);
		TileManaEater eater = (TileManaEater) par1World.getTileEntity(par2, par3, par4);
		par1World.setBlockMetadataWithNotify(par2, par3, par4, par6ItemStack.getItemDamage(), 1 | 2);

		switch(orientation) {
		case 0:
			eater.rotationY = -90F;
			break;
		case 1:
			eater.rotationY = 90F;
			break;
		case 2:
			eater.rotationX = 270F;
			break;
		case 3:
			eater.rotationX = 90F;
			break;
		case 4:
			break;
		default:
			eater.rotationX = 180F;
			break;
		}
	}

	@Override
	public int damageDropped(int par1) {
		return par1;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}
	
	@Override
	public int getRenderType() {
		return LibRenderIDs.idManaEater;
	}

	@Override
	public boolean onBlockActivated(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer, int par6, float par7, float par8, float par9) {
		TileEntity tile = par1World.getTileEntity(par2, par3, par4);
		if(!(tile instanceof TileManaEater))
			return false;

		TileManaEater eater = (TileManaEater) tile;
		ItemStack heldItem = par5EntityPlayer.getCurrentEquippedItem();
		boolean isHeldItemLens = heldItem != null && heldItem.getItem() instanceof ILens;
		boolean wool = heldItem != null && heldItem.getItem() == Item.getItemFromBlock(Blocks.wool);

		if(heldItem != null)
			if(heldItem.getItem() == GameRegistry.findItem("Botania", "twigWand"))
				return false;

		if(isHeldItemLens) {
			par5EntityPlayer.attackEntityFrom(DamageSource.generic, 2);
		}
		else if(wool) {
			par5EntityPlayer.getCurrentEquippedItem().stackSize--;
			par5EntityPlayer.setFire(4);
		}
		
		return true;
	}

	@Override
	public boolean onUsedByWand(EntityPlayer player, ItemStack stack, World world, int x, int y, int z, int side) {
		((TileManaEater) world.getTileEntity(x, y, z)).onWanded(player, stack);
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileManaEater();
	}

	@Override
	public void renderHUD(Minecraft mc, ScaledResolution res, World world, int x, int y, int z) {
		((TileManaEater) world.getTileEntity(x, y, z)).renderHUD(mc, res);
	}

	@Override
	public LexiconEntry getEntry(World world, int x, int y, int z, EntityPlayer player, ItemStack lexicon) {
		return LexiconData.manaEater;
	}

	@Override
	public AxisAlignedBB getWireframeAABB(World world, int x, int y, int z) {
		float f = 1F / 16F;
		return AxisAlignedBB.getBoundingBox(x + f, y + f, z + f, x + 1 - f, y + 1 - f, z + 1 - f);
	}

}
