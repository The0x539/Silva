package soundlogic.silva.common.item;

import java.util.Map;

import cpw.mods.fml.common.registry.ExistingSubstitutionException;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.Type;
import soundlogic.silva.common.lib.LibItemNames;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ModItems {
	
	public static Item simpleResource;
	public static Item proxyItem;
	public static Item priceProxyItem;
	public static Item pageBundle;
	public static Item fakePageBundle;
	public static Item fakeLexicon;
	public static Item stoneHorse;
	public static Item dwarfMead;
	public static Item dwarfChain;
	public static Item dwarfBarrier;
	public static Item chargedStone;
	public static Item enchantHolder;
	public static Item fatePearl;
	public static Item augments;

	public static void preInit() {
	
		simpleResource= new ItemSimpleResource();
		proxyItem=new ItemProxy(LibItemNames.ITEM_PROXY);
		priceProxyItem=new ItemPriceProxy(LibItemNames.ITEM_PRICE_PROXY);
		pageBundle=new ItemPapers(LibItemNames.PAGE_BUNDLE);
		fakePageBundle=new ItemFakePapers(LibItemNames.FAKE_PAPERS);
		fakeLexicon=new ItemFakeLexicon();
		stoneHorse=new ItemStoneHorse();
		dwarfMead=new ItemDwarvenMead(LibItemNames.DWARF_MEAD);
		dwarfChain=new ItemDwarvenChain(LibItemNames.DWARF_CHAIN);
		dwarfBarrier=new ItemDwarvenBarrier(LibItemNames.DWARF_BARRIER);
		chargedStone=new ItemChargedStone(LibItemNames.CHARGED_STONE);
		enchantHolder=new ItemEnchantHolder(LibItemNames.ENCHANT_HOLDER);
		fatePearl=new ItemFatePearl(LibItemNames.FATE_PEARL);
		augments=new ItemDarkElfAugment(LibItemNames.DARK_ELF_AUGMENT);
	}
	
}
