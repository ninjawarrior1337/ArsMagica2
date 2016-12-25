**This is the current list of known bugs, if there are any that have NOT been added to this yet, make an issue post!**
Crashes:

	- Crash on creating new spells(possibly due to server/OS) = Fixed on linux, can't test on windows however
	- Crash with starstrike hitting non-living entities*
	
Bugs:

	- Ender boots do not work when other players are near
	- Random Teleport works once, then doesn't work at all until relog
	- Projectile textures and parts of gui get warped
	- Lectern doesn't render ink sak correctly
	- Warding candle doesn't update until you swap it out of hand
	- Crafting altar missing texture*
	- the /am2 command to set magic level doesn't sync correctly
	- Bosses sometime don't drop anything(there's a check that checks to see if YOU did the last hit, sometimes this check is false when it gets killed)
	- Disarm does not work on mobs
	- Affinity has no effect on mana cost
	- Spell book NEXT/PREV buttons do not work
	- Air Guardian locking servers/clients up with spin attack(it locks the server side)
	- Earth Armor breaks the hand positions
	- Soulbound missing a texture in the Armor Imbuement Table
	- Obelisk rendering is very derpy
	
Performance reports:

	- Mana creepers cause memory leaks
	- Some blocks cause significant performance drops(this is due to the update method being called with unnecessary calls on the client, i plan to restructure them later on to provide an even better performance increase)
	
	
	
	
	
	====================================
	======         LEGEND         ======
	====================================
	* - I can't reproduce it but it has been reported
	** - I can reproduce it but I'm not sure why it happens
