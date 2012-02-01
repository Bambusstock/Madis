package me.Bambusstock.Madis;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dispenser; //important!
import org.bukkit.material.MaterialData;
import java.util.ArrayList;

public class BlockListener implements Listener{
	/**
	 * Shovels and their blocks
	 */
	final int[] shovelIDs = {284};
	final int[] shovelBlocks = {0, 2, 3, 12, 13, 78, 80};
	
	/**
	 * Pickaxes and their blocks
	 */
	final int[] pickIDs = {285};
	final int[] pickBlocks = {1, 4, 14, 15, 16, 21, 24, 48, 49, 56, 73, 74};
	
	public Madis plugin; // important? or depracted?
		
	/**
	 * Search for a value inside a array.
	 * 
	 * @param arr Array to go through 
	 * @param val Target value
	 * 
	 * @return true if contained
	 */
	public Boolean arrayIntContains(int[] arr, int val) {
		for(int i=0; i < arr.length; i++) {
			if(arr[i] == val) return true;
		}
		return false;
	}
	
	/**
	 * Drop items
	 * @param block Block
	 * @param tool Tool
	 */
	public void dropItems(Block block, ItemStack tool) {
		if(tool.getTypeId() == 285) {
			World w = block.getWorld();
			Location l = block.getLocation();
			switch (block.getType()) {
				case DIAMOND_ORE:
					ItemStack diamond_ore = new ItemStack(Material.DIAMOND_ORE);
					diamond_ore.setAmount(1);
					w.dropItem(l, diamond_ore);
					break;
				case IRON_ORE:
					ItemStack iron_ore = new ItemStack(Material.IRON_ORE);
					iron_ore.setAmount(1);
					w.dropItem(l, iron_ore);
					break;
				case GOLD_ORE:
					ItemStack gold_ore = new ItemStack(Material.GOLD_ORE);
					gold_ore.setAmount(1);
					w.dropItem(l, gold_ore);
					break;
				case LAPIS_ORE:
					 ItemStack lapis_ore = new ItemStack(Material.LAPIS_ORE);
					 lapis_ore.setAmount(1);
					 w.dropItem(l, lapis_ore);
					break;
				case REDSTONE_ORE:
					ItemStack redstone_ore = new ItemStack(Material.REDSTONE_ORE); 
					redstone_ore.setAmount(1);
					w.dropItem(l, redstone_ore);
					break;
				default:
					block.breakNaturally(tool);					
			}
				
		}
		
	}
	
	/**
	 * 	Read maximal 64 blocks or stops at air block.
	 *  Blocks save as BlockState-Object, so they can't changed.
	 *  
	 *  @param startPos start
	 *  @param direction Direction
	 *   
	 *   @return List of blocks until the end.
	 */
	public ArrayList<BlockState> readBlocks(Block startPos, BlockFace direction) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // hold blocks
		Block nextBlock = startPos; // dispenser/ start block
		for(int i=0; i < 64; i++) {
			nextBlock = nextBlock.getRelative(direction);
			BlockState nextBlockState = nextBlock.getState();					
			blockStack.add(nextBlockState);
			
			// exit if their is a air block
			if(nextBlock.getTypeId() == 0) i = 64;
		}
		return blockStack;
	}
	
	/**
	 * Read all blocks until a specified block resp. block typ.
	 * Range amount 128 blocks.
	 * 
	 * @param startPos start block
	 * @param endType block typ
	 * @param direction direction
	 * 
	 * @return Block stack
	 */
	public ArrayList<BlockState> readBlocksUntil(Block startPos, int[] endType, BlockFace direction) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // hold blocks
		Block nextBlock = startPos; // dispenser/start block	
		for(int i=0; i < 128; i++) {
			nextBlock = nextBlock.getRelative(direction);
			// Takes one block end break.
			if(nextBlock.getTypeId() != 0 && !this.arrayIntContains(endType, nextBlock.getTypeId())) {
				break;
			}
			else if(nextBlock.getTypeId() != 0 && this.arrayIntContains(endType, nextBlock.getTypeId())) {
				BlockState nextBlockState = nextBlock.getState();					
				blockStack.add(nextBlockState);
				break;
			}
		}
		
		return blockStack;
	}
	
	/**
	 * Go through a ArrayList and changes blocks.
	 * 
	 * @param stack Stack to go through
	 * @param newItem Item that should be appended
	 *  
	 */
	public void proceedBlockStack(ArrayList<BlockState> stack, ItemStack newItem){
		for(int i=0; i < stack.size(); i++) {
			if(i == 0) {
				BlockState bState = stack.get(i);
				Block bBlock = bState.getBlock(); 
				bBlock.setTypeId(newItem.getTypeId());
			}
			else {
				BlockState frontBlockState = stack.get(i);
				BlockState backBlockState = stack.get(i-1);
				Block frontBlock = frontBlockState.getBlock();	
				frontBlock.setTypeId(backBlockState.getTypeId());
			}
		}
	}
	
	/**
	 * Remove all blocks and drop them.
	 * 
	 * @param stack Block stack
	 * 
	 */
	public void proceedAndDropBlockStack(ArrayList<BlockState> stack) {
		for(int i=0; i < stack.size(); i++) {
			BlockState blockState = stack.get(i);
			Block block = blockState.getBlock();
			if(blockState.getType() != Material.AIR) block.breakNaturally();
			block.setTypeId(0);
		}
	}
	
	/**
	 * Drop the items like their mined with a tool.
	 * 
	 * @param stack Block stack
	 * 
	 */
	public void proceedAndDropBlockStack(ArrayList<BlockState> stack, ItemStack tool) {
		for(int i=0; i < stack.size(); i++) {
			BlockState blockState = stack.get(i);
			Block block = blockState.getBlock();
			if(blockState.getType() != Material.AIR) {
				this.dropItems(block, tool);
			}
			block.setTypeId(0);
		}
	}
	
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		// Create Dispenser-Object and get facing
		MaterialData d = event.getBlock().getState().getData();
		Dispenser dispenser = (Dispenser) d;	
		BlockFace dispenserFacing = dispenser.getFacing();
		Block blockBehind = event.getBlock().getRelative(dispenserFacing.getOppositeFace()); // this block is relevant for behaivor
		
		// Obsidian or a diamond block enables 'Madis-Features'
		if(blockBehind.getType() == Material.OBSIDIAN || blockBehind.getType() == Material.DIAMOND_BLOCK) {
			ItemStack dispensedItem = event.getItem(); 
			event.setCancelled(true); 					// cancel event, otherwise its dropped			
			// Shovel
			if(this.arrayIntContains(this.shovelIDs, dispensedItem.getTypeId())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), this.shovelBlocks, dispenserFacing);
				ItemStack tool = new ItemStack(284); // golden shovel
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			//Pickaxe
			else if(this.arrayIntContains(this.pickIDs, dispensedItem.getTypeId())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), this.pickBlocks, dispenserFacing);
				ItemStack tool = new ItemStack(285); // gold pickaxe
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			else if(dispensedItem.getTypeId() < 96){
				ArrayList<BlockState> blockStack = this.readBlocks(event.getBlock(), dispenserFacing);
				this.proceedBlockStack(blockStack, dispensedItem);
			}
		}
	}
}
