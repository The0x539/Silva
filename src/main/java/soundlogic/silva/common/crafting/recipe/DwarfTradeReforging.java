package soundlogic.silva.common.crafting.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import scala.actors.threadpool.Arrays;
import soundlogic.silva.common.block.tile.TilePortalCore;
import soundlogic.silva.common.core.handler.DwarfForgedHandler;
import soundlogic.silva.common.item.ItemProxy;
import soundlogic.silva.common.item.ModItems;

public class DwarfTradeReforging extends DwarfTrade{

	private final String TAG_EXTRA_COST_SIZE = "extraCostSize";
	private final String TAG_EXTRA_COST = "extraCost";
	private final String TAG_EXTRA_COST_STRING = "extraCostString";
	
	public enum ReforgeType {
		HELMET,
		CHESTPLATE,
		LEGGINGS,
		BOOTS,
		PICKAXE,
		SHOVEL,
		AXE,
		SWORD,
	};
	
	
	
	List<Object> extraCost;
	ReforgeType type;
	int requiredRep;
	int repBoost;
	int maxRep;
	
	public DwarfTradeReforging(ReforgeType type, int requiredRep, int repBoost, int maxRep) {
		this.type = type;

		this.requiredRep=requiredRep;
		this.repBoost=repBoost;
		this.maxRep=maxRep;
	}

	@Override
	public IPortalRecipeTransaction getTransaction(
			ArrayList<ItemStack> inventory, TilePortalCore tilePortalCore) {
		if(!this.canApply(tilePortalCore))
			return null;
		ArrayList<Object> inputsCompressed = new ArrayList(extraCost);
		ArrayList<Object> inputsMissing = new ArrayList();
		for(Object o : inputsCompressed) {
			if(o instanceof String)
				inputsMissing.add(o);
			else {
				ItemStack stack=((ItemStack) o).copy();
				int size=stack.stackSize;
				stack.stackSize=1;
				for(int i=0;i<size;i++) {
					inputsMissing.add(stack);
				}
			}
		}
		ArrayList<ItemStack> stacksToRemove = new ArrayList();
		
		ItemStack target=null;
		
		for(ItemStack stack : inventory) {
			if(stack == null) {
				continue;
			}
			if(inputsMissing.isEmpty() && target!=null)
				break;
			int i=0;
			boolean match=false;
			while(i < inputsMissing.size()) {
				if(stackMatches(stack,inputsMissing.get(i)))
				{
					stacksToRemove.add(stack);
					match=true;
					break;
				}
				i++;
			}
			if(match)
				inputsMissing.remove(i);
			if(isValidReforgeTarget(type,stack))
				target=stack;
		}
		
		ArrayList<ItemStack> result=new ArrayList<ItemStack>();
		if(target!=null) {
			stacksToRemove.add(target);
			result.add(DwarfForgedHandler.dwarfForgeStack(target));
		}
		
		
		if(inputsMissing.isEmpty() && target!=null)
			return new DwarvenTradeTransaction(result,stacksToRemove,this.getRepBoost(),this.getMaxRep());
		
		return null;
	}
	

	private boolean isValidReforgeTarget(ReforgeType type, ItemStack stack) {
		ItemTool tool=null;
		if(stack.getItem() instanceof ItemTool)
			tool=(ItemTool)stack.getItem();
		switch(type) {
		case HELMET:
			return stack.getItem().isValidArmor(stack, 0, null);
		case CHESTPLATE:
			return stack.getItem().isValidArmor(stack, 1, null);
		case LEGGINGS:
			return stack.getItem().isValidArmor(stack, 2, null);
		case BOOTS:
			return stack.getItem().isValidArmor(stack, 3, null);
		case PICKAXE:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "pickaxe") != -1;
		case SHOVEL:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "shovel") != -1;
		case AXE:
			if(tool==null)
				return false;
			return tool.getHarvestLevel(stack, "axe") != -1;
		case SWORD:
			return stack.getItem() instanceof ItemSword;
		}
		return false;
	}

	protected int getMaxRep() {
		return this.maxRep;
	}

	protected int getRepBoost() {
		return this.repBoost;
	}

	private boolean stackMatches(ItemStack stack, Object searchTarget) {
		if(searchTarget instanceof String) {
			List<ItemStack> validStacks = OreDictionary.getOres((String) searchTarget);
			for(ItemStack test : validStacks) {
				ItemStack temp=test.copy();
				if(temp.getItemDamage() == Short.MAX_VALUE)
					temp.setItemDamage(stack.getItemDamage());
				if(stack.isItemEqual(temp) && stack.getItemDamage() == temp.getItemDamage()) {
					return true;
				}
			}
		}
		if(searchTarget instanceof ItemStack) {
			return stack.getItem() == ((ItemStack) searchTarget).getItem() && stack.getItemDamage() == ((ItemStack) searchTarget).getItemDamage();
		}
		return false;
	}

	@Override
	public List<ItemStack> getOutput() {
		ArrayList<ItemStack> result=new ArrayList<ItemStack>();
		result.add(DwarfForgedHandler.dwarfForgeStack(ItemProxy.getStackForReforgeType(type)));
		return result;
	}

	@Override
	public List<Object> getInputs() {
		ArrayList<Object> result=new ArrayList<Object>();
		result.add(ItemProxy.getStackForReforgeType(type));
		result.addAll(extraCost);
		return result;
	}

	@Override
	public boolean isReputationSufficent(int reputation) {
		return reputation>=this.requiredRep;
	}

	@Override
	public void prepRecipe(int reputation) {
		int cost=0;
		switch(type) {
		case HELMET: cost=50; break;
		case CHESTPLATE: cost=100; break;
		case LEGGINGS: cost=75; break;
		case BOOTS: cost=50; break;
		case PICKAXE: cost=100; break;
		case SHOVEL: cost=60; break;
		case AXE: cost=70; break;
		case SWORD: cost=90; break;
		}
		extraCost=getExtraCosts(cost);
	}

	@Override
	public void writeNBTData(NBTTagCompound cmp) {
		cmp.setInteger(TAG_EXTRA_COST_SIZE, extraCost.size());
		for(int i=0;i<extraCost.size();i++) {
			Object o = extraCost.get(i);
			if(o instanceof String) {
				cmp.setBoolean(TAG_EXTRA_COST_STRING+i, true);
				cmp.setString(TAG_EXTRA_COST+i, (String)o);
			}
			else {
				cmp.setBoolean(TAG_EXTRA_COST_STRING+i, false);
				NBTTagCompound stack=new NBTTagCompound();
				((ItemStack)o).writeToNBT(stack);
				cmp.setTag(TAG_EXTRA_COST+i, stack);
			}
		}
	}

	@Override
	public void readNBTData (NBTTagCompound cmp) {
		extraCost = new ArrayList<Object>();
		int size=cmp.getInteger(TAG_EXTRA_COST_SIZE);
		for(int i=0;i<size;i++) {
			boolean str=cmp.getBoolean(TAG_EXTRA_COST_STRING+i);
			if(str)
				extraCost.add(cmp.getString(TAG_EXTRA_COST+i));
			else {
				extraCost.add(ItemStack.loadItemStackFromNBT(cmp.getCompoundTag(TAG_EXTRA_COST+i)));
			}
		}
	}

}
