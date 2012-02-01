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
	
	public Madis plugin; // wichitg?
		
	/**
	 * Durchsucht Array nach gg. Integer-Wert.
	 * 
	 * @param arr Array das durchsucht wird.
	 * @param val Wert der enthalten sein soll.
	 * 
	 * @return True wenn enthalten
	 */
	public Boolean arrayIntContains(int[] arr, int val) {
		for(int i=0; i < arr.length; i++) {
			if(arr[i] == val) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Spuckt Items aus.
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
	 * 	Liest maximal 64 Blöcke bzw. bis zu einem Luftblock.
	 *  Die Blöcken werden als BlockState Objekt gespeichert,
	 *  da diese nicht mehr verändert werden können. Siehe
	 *  auch <proceedBlockStack>.
	 *  
	 *  @param startPos Startposition
	 *  @param direction Richtung
	 *   
	 *   @return Liste mit Blöcken bis zum Endpunkt.
	 */
	public ArrayList<BlockState> readBlocks(Block startPos, BlockFace direction) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // Speichert Blöcke vor Dispenser
		Block nextBlock = startPos; // Dispenser auswählen um später in richtige Richtung zu lesen
		for(int i=0; i < 64; i++) {
			// nextBlock = this.getBlockInDirection(nextBlock, direction); // Block lesen
			nextBlock = nextBlock.getRelative(direction);
			BlockState nextBlockState = nextBlock.getState();					
			blockStack.add(nextBlockState);
			
			// Auch aussteigen, wenn Luft gegriffen wird
			if(nextBlock.getTypeId() == 0) i = 64;
		}
		return blockStack;
	}
	
	/**
	 * Liest alle Blöcke bis zu einem gewissen Block bzw. BlockTyp (arg1).
	 * Findet also alle Blöcke bis zu einem Block mit bestimmtem Typ.
	 * Die Reichweite beträgt 128 Blöcke.
	 * 
	 * @param startPos Startposition
	 * @param endType Blocktyp der Endposition
	 * @param direction Richtung
	 * 
	 * @return Stack mit allen Blöcken bis dahin
	 */
	public ArrayList<BlockState> readBlocksUntil(Block startPos, int[] endType, BlockFace direction) {
		ArrayList<BlockState> blockStack = new ArrayList<BlockState>(); // Speichert Blöcke vor Dispenser
		Block nextBlock = startPos; // Dispenser auswählen um später in richtige Richtung zu lesen
		
		for(int i=0; i < 128; i++) {
			//nextBlock = this.getBlockInDirection(nextBlock, direction); // Block lesen
			nextBlock = nextBlock.getRelative(direction);
			// Nimmt auto. nur einen Block auf und beendet sich dann.
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
	 * 	Durchläuft ein ArrayList Objekt und verändert die Blöcke
	 *  vor dem Dispenser. Es ist wichtig, dass es sich bei den
	 *  Objekten innerhalb um BlockState Objekte handelt,
	 *  da ein Block Element als Referenz behandelt werden würde
	 *  und demnach alle nachfolgenden Blöcke mit dem
	 *  dispensedBlock überschrieben werden würden.
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
	 * Entfernt alle Blöcke in einem BlockStack und droppt diese
	 * als Item.
	 * 
	 * @param stack Stack mit den zu bearbeitenden Blöcken
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
	 * Entfernt alle Blöcke in einem BlockStack und droppt diese
	 * als Item mit einem bestimmten Werkzeug.
	 * 
	 * @param stack Stack mit den zu bearbeitenden Blöcken
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
		// Dispenser-Objekt erstellen und Blickrichtung festlegen
		MaterialData d = event.getBlock().getState().getData();
		Dispenser dispenser = (Dispenser) d;	
		BlockFace dispenserFacing = dispenser.getFacing();
		//Block blockBehind = this.getBlockInDirection(event.getBlock(), dispenserFacing.getOppositeFace()); // Block hinter Dispenser zur Verhaltenssteurung ansprechen
		Block blockBehind = event.getBlock().getRelative(dispenserFacing.getOppositeFace()); // Block hinter dispenser zur Verhaltenssteuerung ansprechen
		
		// Obsidian aktivert Madis
		if(blockBehind.getType() == Material.OBSIDIAN || blockBehind.getType() == Material.DIAMOND_BLOCK) {
			ItemStack dispensedItem = event.getItem(); 	// Item abfragen, um es später anzuhängen
			event.setCancelled(true); 					// Abbrechen, da sonst ein Block ausgespuckt wird			
			// Schaufel
			if(this.arrayIntContains(this.shovelIDs, dispensedItem.getTypeId())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), this.shovelBlocks, dispenserFacing);
				ItemStack tool = new ItemStack(284); // Goldene Schaufel
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			//Spitzhacke
			else if(this.arrayIntContains(this.pickIDs, dispensedItem.getTypeId())) {
				ArrayList<BlockState> blockStack = this.readBlocksUntil(event.getBlock(), this.pickBlocks, dispenserFacing);
				ItemStack tool = new ItemStack(285); // Goldene Spitzhacke
				this.proceedAndDropBlockStack(blockStack, tool);
			}
			else if(dispensedItem.getTypeId() < 96){
				ArrayList<BlockState> blockStack = this.readBlocks(event.getBlock(), dispenserFacing);
				this.proceedBlockStack(blockStack, dispensedItem);
			}
		}
	}
}
