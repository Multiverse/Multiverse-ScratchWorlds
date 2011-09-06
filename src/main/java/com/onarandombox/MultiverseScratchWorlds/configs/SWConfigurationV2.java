package com.onarandombox.MultiverseScratchWorlds.configs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.util.config.Configuration;

import com.onarandombox.MultiverseScratchWorlds.SWConfiguration;
import com.onarandombox.MultiverseScratchWorlds.SWWorld;
import com.onarandombox.MultiverseScratchWorlds.MultiverseScratchWorlds;

public class SWConfigurationV2 extends SWConfiguration {

	public SWConfigurationV2(Configuration config) {
		super(config);
		this.version = 2;
	}
	
	public SWConfigurationV2(File file) {
		super(file);
	}

	@Override
	public SWConfiguration upgrade() {
		// Version 2 is currently the most recent config version known
		return null;
	}

	@Override
	public boolean write(MultiverseScratchWorlds plugin) {
        if(plugin.getScratchWorlds().size() == 0) {
            this.configuration.setProperty(SWConfiguration.createPath(WORLD_LIST_KEY), null);
        } else {
		    for(Entry<String,SWWorld> entry : plugin.getScratchWorlds().entrySet()) {
		    	this.configuration.setProperty(SWConfiguration.createPath(WORLD_LIST_KEY, entry.getKey(), RESEED_KEY), entry.getValue().getShouldReseed());
		    }
        }
		
		this.configuration.setProperty(VERSION_KEY, new Integer(2));
		
		return this.configuration.save();
	}
	
	@Override
	public boolean readShouldReseed(String worldName) {
		String path = SWConfiguration.createPath(WORLD_LIST_KEY, worldName, RESEED_KEY);
		return this.configuration.getBoolean(path, true);
	}

	@Override
	public List<String> readScratchWorldNames() {
        List<String> retVal = this.configuration.getKeys(WORLD_LIST_KEY);
		return (retVal == null ? new ArrayList<String>() : retVal);
	}

}
