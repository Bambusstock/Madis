package me.Bambusstock.Madis;
import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Dispenser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class BlockListener implements Listener{

	/**
	 * Shovels and their blocks
	 */
	final Material[] shovelIDs = {Material.GOLD_SPADE};
	final Material[] shovelBlocks = {	
			Material.AIR, Material.GRASS, Material.DIRT, 
			Material.SAND, Material.GRAVEL, Material.SNOW, 
			Material.SNOW_BLOCK
		};
	
	/**
	 * Pickaxes and their blocks
	 */
	final Material[] pickIDs = {Material.GOLD_PICKAXE};
	final Material[] pickBlocks = {
			Material.STONE, Material.COBBLESTONE, Material.GOLD_ORE,
			Material.IRON_ORE, Material.COAL_ORE, Material.LAPIS_ORE, 
			Material.SANDSTONE, Material.MOSSY_COBBLESTONE, Material.OBSIDIAN, 
			Material.DIAMOND_ORE, Material.REDSTONE_ORE, Material.GLOWING_REDSTONE_ORE
		};
	
	/**
	 * Bucket and blocks.
	 */
	final Material[] bucketID = {Material.BUCKET};
	final Material[] bucketBlocks = {
			Material.LAVA, Material.STATIONARY_LAVA, 
			Material.WATER, Material.STATIONARY_WATER
		};

	/**
	 * Items that could be placed as block. E.g. water bucket as water block.
	 */
	final Material[] specialItems = {
			Material.WATER_BUCKET, Material.LAVA_BUCKET, Material.REDSTONE
		};
	
		
	/**
	 * Check if a material array contains a specific material.
	 * 
	 * @param arr Array containing materials
	 * @param val Specific material
	 * 
	 * @return true if array contains what you're searching for
	 */
	public Boolean arrayContainsMaterial(Material[] arr, Material val) {
		for(int i=0; i < arr.length; i++) {
			if(val.equals(arr[i])) return true;
		}
		return false;
	}
	
	/**
	 * Remove items from a inventory by item stack.
	 * 
	 * @param inventory
	 * @param item Use amount to control amount of items that should be removed .
	 */
	public void rmItemFromInventory(Inventory inventory, ItemStack item) {
	    inventory.removeItem(item);
	}
	
	/**
	 * Taken from Bukkit source file FurnaceAndDispenser.java.
	 * 
	 * @param data
	 * 
	 * @return Block Face
	 */
	public BlockFace getDispenserFacing(MaterialData d) {
		byte data = d.getData();
		switch (data) {
			case 0x2:
				return BlockFace.EAST;
		    case 0x3:
		        return BlockFace.WEST; 
		    case 0x4:
		    	return BlockFace.NORTH;
		    case 0x5:
		    default:
		       	 return BlockFace.SOUTH;
		}
	}
	
	/**
	 * Drop items. Its important to set the amount of the item stack 
	 * else just a 'item picture' is dropped. which couldn't picked up.
	 * 
	 * @param block Block
	 * @param tool Tool
	 */
	public void dropItems(Block block, ItemStack tool) {
		World w = block.getWorld();
		Location l = block.getLocation();
		switch(tool.getType()) {
			case GOLD_PICKAXE:
			    switch (block.getType()) {
				case DIAMOND_ORE:
					ItemStack diamond_ore = new ItemStack(Material.DIAMOND, 1);
					w.dropItem(l, diamond_ore);
					break;
				case IRON_ORE:
					ItemStack iron_ore = new ItemStack(Material.IRON_ORE, 1);
					w.dropItem(l, iron_ore);
					break;
				case GOLD_ORE:
					ItemStack gold_ore = new ItemStack(Material.GOLD_ORE);
					w.dropItem(l, gold_ore);
					break;
				case LAPIS_ORE:
					ItemStack lapis_ore = new ItemStack(Material.LAPIS_BLOCK, 1);
					 w.dropItem(l, lapis_ore);
					break;
				case REDSTONE_ORE:
					ItemStack redstone_ore = new ItemStack(Material.REDSTONE, 4); 
					w.dropItem(l, redstone_ore);
					break;
				default:
					block.breakNaturally(tool);					
				}
				break;
			case BUCKET:
				switch (block.getType()) {
        				case WATER:
        				case STATIONARY_WATER:
        					block.setType(Material.AIR); // remove water
        					ItemStack water_bucket = new ItemStack(Material.WATER_BUCKET, 1);
        					w.dropItem(l, water_bucket);
        					break;
        				case LAVA:
        				case STATIONARY_LAVA:
        					ItemStack lava_bucket = new ItemStack(Material.LAVA_BUCKET, 1);
        					w.dropItem(l, lava_bucket);
        					break;
				}
			case GOLD_SPADE:
			default:
				block.breakNaturally(tool);
		}
	}
	
	/**
	 * Set the new block depending on the dispensed item with the 
	 * ability, e.g. to set a water block instead of dropping a water bucket.
	 * 
	 * @param block to change.
	 * @param Item Item to set.
	 */
	public void setDispensedBlock(Block block, ItemStack item) {
		switch(item.getType()) {
			case WATER_BUCKET:
				block.setType(Material.WATER);
				break;
			case LAVA_BUCKET:
				block.setType(Material.LAVA);
				break;
			case REDSTONE:
				block.setType(Material.REDSTONE_WIRE);
				break;
			default:	
				block.setType(item.getType());
		}
	}
	
	/**
	 * Read maximal 64 blocks or stops at air block.
	 * Blocks save as BlockState-Object, so they can't changed.
	 *  
	 * @param startPos start
	 * @param direction Direction
	 *   
	 * @return List of blocks until the end.
	 */
	public ArrayList<BlockState> readBlocksUntil(Block startPos, BlockFace direction) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // hold blocks
		Block nextBlock = startPos; // dispenser/ start block
		for(int i=0; i < 64; i++) {
			nextBlock = nextBlock.getRelative(direction);
			BlockState nextBlockState = nextBlock.getState();					
			blockStack.add(nextBlockState);
			
			// exit if their is a air block
			if(nextBlock.getType() == Material.AIR) i = 64;
		}
		return blockStack;
	}
	
	/**
	 * Read all blocks until a specified block respectively block type.
	 * Range amount 128 blocks.
	 * 
	 * @param startPos start block
	 * @param direction direction
	 * @param endType material type
	 * 
	 * @return Block stack
	 */
	public ArrayList<BlockState> readBlocksUntil(Block startPos, BlockFace direction, Material[] endType) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // hold blocks
		Block nextBlock = startPos; // dispenser/start block	
		for(int i=0; i < 128; i++) {
			nextBlock = nextBlock.getRelative(direction);
			// Takes one block end break.
			if(nextBlock.getType() != Material.AIR && !this.arrayContainsMaterial(endType, nextBlock.getType())) {
				break;
			}
			else if(nextBlock.getType() != Material.AIR && this.arrayContainsMaterial(endType, nextBlock.getType())) {
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
	 * @param stack Stack to go through.
	 * @param newItem Item that should be appended.
	 *  
	 */
	public void proceedBlockStack(ArrayList<BlockState> stack, ItemStack newItem){
		for(int i=0; i < stack.size(); i++) {
			if(i == 0) {
				BlockState blockState = stack.get(i);
				Block block = blockState.getBlock(); 
				this.setDispensedBlock(block, newItem);
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
	 * @param stack
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
	 * @param tool The tool that mined the blocks
	 */
	public void proceedAndDropBlockStack(ArrayList<BlockState> stack, ItemStack tool) {
		for(int i=0; i < stack.size(); i++) {
			BlockState blockState = stack.get(i);
			Block block = blockState.getBlock();
			if(blockState.getType() != Material.AIR) this.dropItems(block, tool);
			block.setTypeId(0);
		}
	}
	
	@EventHandler
	public void onBlockDispense(BlockDispenseEvent event) {
		// Create Dispenser-Object and get facing
		Dispenser dispenser = (Dispenser) event.getBlock().getState();
		BlockFace dispenserFacing = this.getDispenserFacing(dispenser.getData());
		Block blockBehind = event.getBlock().getRelative(dispenserFacing.getOppositeFace()); // this block is relevant for behavior
		
		// Obsidian or a diamond block enables 'Madis-Features'
		if(blockBehind.getType() == Material.OBSIDIAN || blockBehind.getType() == Material.DIAMOND_BLOCK) {
			event.setCancelled(true); // cancel event, otherwise its dropped			
			
			ItemStack dispensedItem = event.getItem();
			Inventory dispenserInv = (dispenser).getInventory();
			
			// Shovel
			if(this.arrayContainsMaterial(this.shovelIDs, dispensedItem.getType())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), dispenserFacing, this.shovelBlocks);
				ItemStack tool = new ItemStack(dispensedItem.getType());
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			//Pickaxe
			else if(this.arrayContainsMaterial(this.pickIDs, dispensedItem.getType())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), dispenserFacing, this.pickBlocks);
				ItemStack tool = new ItemStack(dispensedItem.getType());
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			// Bucket
			else if(this.arrayContainsMaterial(this.bucketID, dispensedItem.getType())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), dispenserFacing, this.bucketBlocks);
				ItemStack tool = new ItemStack(dispensedItem.getType());
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			// Special items and normal blocks
			else if(dispensedItem.getTypeId() < 96 || this.arrayContainsMaterial(this.specialItems, dispensedItem.getType())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), dispenserFacing);
				this.proceedBlockStack(blockStack, dispensedItem);
				this.rmItemFromInventory(dispenserInv, new ItemStack(dispensedItem.getType(), 1));
			}
			// Items get dropped
			else {
				event.setCancelled(false);
			}
			
		}
	}
}
