package com.onarandombox.MultiverseScratchWorlds;

import org.bukkit.World;

public class SWWorld {
	private World world;
	private boolean shouldReseed = true;
	
	public SWWorld(World world) {
		this.world = world;
	}

	/**
	 * Get the Bukkit world backing this SWWorld
	 * 
	 * @return The World backing this SWWorld
	 */
	public World getWorld() {
		return this.world;
	}
	
	/**
	 * Get the name of this world. References World.getName().
	 * 
	 * @return The name of this world
	 */
	public String getName() {
		return this.world.getName();
	}
	
	/**
	 * Control whether this world should reseed itself on regeneration
	 * 
	 * @param shouldReseed Whether this world should reseed
	 */
	public void setShouldReseed(boolean shouldReseed) {
		this.shouldReseed = shouldReseed;
	}

	/**
	 * @return Whether this world should reseed itself on regeneration
	 */
	public boolean getShouldReseed() {
		return shouldReseed;
	}
}
