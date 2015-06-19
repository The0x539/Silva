package soundlogic.silva.common.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import soundlogic.silva.common.block.tile.TileDwarvenSign;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.crafting.recipe.DwarfTradeSigned;
import net.minecraft.nbt.NBTTagCompound;

public class PortalDwarfData {

	
	private static final String TAG_DATA = "dwarfData";
	private static final String TAG_REPUTATION = "reputation";
	private static final String TAG_DWARF_TRADES = "dwarfTrade";
	private static final String TAG_DWARF_TRADES_DATA = "dwarfTradeData";

	int reputation;
	DwarfTradeSigned[] dwarfTrades=new DwarfTradeSigned[9];
	NBTTagCompound[] dwarfTradesData=new NBTTagCompound[9];
	Random random=new Random();
	
	public PortalDwarfData() {
		reputation=0;
	}
	
	public void writeNBT(NBTTagCompound cmp) {
		NBTTagCompound mine=new NBTTagCompound();
		mine.setInteger(TAG_REPUTATION, reputation);
		for(int i=0;i<dwarfTrades.length;i++) {
			if(dwarfTrades[i]==null)
				mine.setString(TAG_DWARF_TRADES+i, "NONE");
			else
				mine.setString(TAG_DWARF_TRADES+i, ModPortalTradeRecipes.dwarfTradeSignsToStrings.get(dwarfTrades[i]));
			if(dwarfTradesData[i]!=null)
				mine.setTag(TAG_DWARF_TRADES_DATA+i, dwarfTradesData[i]);
		}
		
		cmp.setTag(TAG_DATA, mine);
	}

	public void readNBT(NBTTagCompound cmp) {
		NBTTagCompound mine=cmp.getCompoundTag(TAG_DATA);
		for(int i=0;i<dwarfTrades.length;i++) {
			String tradeKey=mine.getString(TAG_DWARF_TRADES+i);
			if(tradeKey.equals("NONE"))
				dwarfTrades[i]=null;
			else
				dwarfTrades[i]=ModPortalTradeRecipes.dwarfStringsToTradeSigns.get(tradeKey);
			if(mine.hasKey(TAG_DWARF_TRADES_DATA+i))
				dwarfTradesData[i]=mine.getCompoundTag(TAG_DWARF_TRADES_DATA+i);
		}
		reputation=mine.getInteger(TAG_REPUTATION);
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int newRep) {
		reputation=newRep;
	}
	
	public DwarfTradeSigned getTrade(int slot) {
		if(dwarfTrades[slot]!=null)
			dwarfTrades[slot].readNBTData(dwarfTradesData[slot]);
		return dwarfTrades[slot];
	}
	
	public void setTrade(int slot, DwarfTradeSigned trade) {
		dwarfTrades[slot]=trade;
	}

	public void updateTrades(int locked, int ticks, TileDwarvenSign[] signs, TilePortalCore core) {
		boolean update=false;
		for(int i=0;i<dwarfTrades.length;i++) {
			if(i != locked && ticks % 60 == 0)
				if(random.nextFloat()<.01F) {
					dwarfTrades[i]=getRandomTrade();
					if(dwarfTrades[i]!=null) {
						dwarfTrades[i].prepRecipe(reputation);
						NBTTagCompound cmp=new NBTTagCompound();
						dwarfTrades[i].writeNBTData(cmp);
						if(cmp.hasNoTags())
							dwarfTradesData[i]=null;
						else
							dwarfTradesData[i]=cmp;
						TileDwarvenSign sign = signs[i];
						if(sign!=null) {
							sign.getWorldObj().markBlockForUpdate(sign.xCoord, sign.yCoord, sign.zCoord);
							update=true;
						}
					}
				}
		}
		if(update)
			core.getWorldObj().markBlockForUpdate(core.xCoord, core.yCoord, core.zCoord);
	}

	private DwarfTradeSigned getRandomTrade() {
		float chanceForNull = 1-((float)reputation) / (4F+1.1F * ((float)reputation));
		if(random.nextFloat()>chanceForNull)
			return ModPortalTradeRecipes.getRandomWeighedTrade(reputation, dwarfTrades);
		return null;
	}
}
