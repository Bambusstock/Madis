Madis - Magic dispenser for Craftbukkit using the Bukkit-API
============================================================

This plugin provide some extra features for your dispenser. Now you can place blocks instead of dropping items. Additionally you have the ability to mine blocks.

How does it work?
-----------------

### Placing blocks

All you need is a obsidian block, a dispenser and a lever, button or something equal. Place the obsidian block behind your dispenser, add the button and put a block into the dispenser. Click the button/lever and watch the magic.
Actually not all blocks and items are supported. In general you could use all blocks including water and lava. Items are a little bit tricky. In any case you could put rails, fences, redstone, water/lava bucket into the dispenser.

__NOTE:__ Keep in mind, that the magic dispenser just place 64 blocks and then stops until a block is removed. You know this behavior from pistons.


### Break blocks

If you like to break blocks with Madis and using the default configuration you put instead of blocks or items a golden pickaxe into your dispenser. Add obsidian behind and a button. If you hit the button a block in front of should be dropped. Additionally to pickaxes you could use golden shovels. The behavior is the same. Just the blocks change. (droppable: Dirt, Grass, Sand, Gravel, Clay, ...)
  
__NOTE:__ This behavior has a range of 128 blocks.

__NOTE:__ Keep in mind that just blocks like stone or cubblestone and ores are dropped. Other blocks stay as they are. If you use Madis 0.3 or newer you
 could define custom shovels and pickaxe and blocks mined by them. See below for more expressive information.

Configuration
-------------

Madis is using the Configuration API of Bukkit and provide some options to customize your plugin.

### General

#### `creativeMode` (Default: false)

If you enable the creative mode, the stacks inside the dispenser will never be decreased. Standard is false.
If you want to activate the `creativeMode` simple switch `false` to `true`:
	
	creativeMode: true

#### `dropSpecialItemsAsBlock` (Default: true)

If this option is enabled then items like water buckets are spawn as water block. If you 'd like to disable this option
simply switch `true` to `false`.
	
	dropSpecialItemsAsBlock: false 

The following items will drop a block:
	
- Lava bucket drops a lava block.
- Redstone drops a redstone wire.

Actually you can't define your own special blocks. See TODO ;).

### Shovels

#### `shovelIDs` (Default: 284)

Define the block or item ids that enable the mining feature for shovelBlocks. Standard is a gold shovel with the id _284_. You can define on or more of this ids. For example you could include the diamond shovel:

	shovelIDs: 
		- 284
		- 277
	
#### `shovelBlocks` (Default: 0, 2, 3, 12, 13, 78, 80)

Define the block ids that can be broken by Madis using the blocks or items defined with `shovelIDs`. You can define more then one. Standard is: grass, dirt, sand, gravel, snow, snow block.

### Pickaxes

#### `pickIDs` (Default: 285)

Define the block or item ids that enable the mining feature for pickBlocks. Standard is a golden pickaxe with the id _285_. You can also define more then one id. For example you could use a diamond pickaxe:
	
	pickIDs: 
		- 285
		- 278
	
### Buckets

#### `bucketIDs` (Default: 325)

Define the block or item used as a bucket. Standard is the bucket ;) with the id _325_. Again you can define more than one id. For example you could use a bowl:

	`bucketIDs`: 
		- 325
		- 281
	
[Check out the MinecraftWiki for DataValues and IDs!](http://www.minecraftwiki.net/wiki/Data_values)

Known Bugs
----------

Because this plugin is in a early development state there are some known bugs:

- Lapis block is directly dropped, instead of group of lapis
- The golden tools (pickaxe, shovel) take no damage inside the dispenser (actually I think this isn't a bug. but why not use options)

TODO:
-----

- Use lists instead of array
- Bug fixing ;)
	- Dropped Buckets need to be destroyed after use
	- damage to used items
- Extend options:
	- provide method to define special items => `specialItems`
	- define other or add blocks to obsidian, diamond block => `activateBlock`
	- items could be destroyed used to break blocks with tools => `damageToItems`
	- self defined ranges (research for redstone and ranges) => `miningRange`, `placingRange`