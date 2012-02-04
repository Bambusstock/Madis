Madis - Magic dispenser for Craftbukkit using the Bukkit-API
============================================================

This plugin provide some extra features for your dispenser. Now you can place blocks instead of dropping items. Additionally you have the ability to mine blocks.

How does it work?
-----------------

### Placing blocks

All you need is a obsidian block, a dispenser and a lever, button or something equal. Place the obsidian block behind your dispenser, add the button and put a block into the dispenser. Click the button/lever and watch the magic.
Actually not all blocks and items are supported. In general you could use all blocks including water and lava. Items are a little bit tricky. In any case you could put rails, fences, redstone, water/lava bucket into the dispenser.
__Keep in mind, that the dispenser just place 64 blocks and then stops until a block is removed. You know this behaivor from pistons.__


### Mining blocks

If you like to mine with Madis you put instead of blocks or items a golden pickaxe into your dispenser. Add obsidian behind and a button. If you hit the button a block in front of should be dropped. __Keep in mind that just blocks like stone or cubblestone and ores are dropped. Other blocks stay as they are.__ Additionally to pickaxes you could use golden shovels. The behaivor is the same. Just the blocks change. (droppable: Dirt, Grass, Sand, Gravel, Clay, ...) This Behaivor has a range of 128 blocks.

Known Bugs
----------

Because this plugin is in a early development state there are some known bugs:

- Lapis block is directly dropped, instead of group of lapis
- The golden tools (pickaxe, shovel) take no damage inside the dispenser (actually I think this isn't a bug. but why not use options)

TODO:
-----

- Bug fixing ;)
- Make it configurable
- Dropped Buckets need to be destroyed after use