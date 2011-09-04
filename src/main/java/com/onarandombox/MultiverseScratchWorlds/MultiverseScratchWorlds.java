package com.onarandombox.MultiverseScratchWorlds;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MVPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;

public class MultiverseScratchWorlds extends JavaPlugin implements MVPlugin {
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    public static final String LOG_PREFIX = "[Multiverse-ScratchWorlds] ";

    private MultiverseCore core;

    @Override
    public void onLoad() {
        this.getDataFolder().mkdirs();
    }

    @Override
    public void onEnable() {
        LOG.info(LOG_PREFIX + "Enabled");
    }

    @Override
    public void onDisable() {
        LOG.info(LOG_PREFIX + "Disabled");
    }

    @Override
    public void setCore(MultiverseCore multiverseCore) {
        this.core = multiverseCore;
    }

    @Override
    public MultiverseCore getCore() {
        return this.core;
    }

    @Override
    public String dumpVersionInfo(String buffer) {
        buffer += logAndBuffer("Multiverse-ScratchWorlds Version: " + this.getDescription().getVersion());
        buffer += logAndBuffer("Bukkit Version: " + this.getServer().getVersion());
        //buffer += logAndBuffer("Dumping Config Values: (version " + this.getMainConfig().getString("version", "NOT SET") + ")");
        //buffer += logAndBuffer("wand: " + this.getMainConfig().getString("wand", "NOT SET"));
        buffer += logAndBuffer("Special Code: FRN001");
        return buffer;
    }

    private String logAndBuffer(String string) {
        LOG.info(LOG_PREFIX + string);
        return LOG_PREFIX + string + "\n";
    }

    @Override
    public void log(Level level, String msg) {
        if (level == Level.FINE && MultiverseCore.GlobalDebug >= 1) {
            LOG.info(LOG_PREFIX + msg);
            return;
        } else if (level == Level.FINER && MultiverseCore.GlobalDebug >= 2) {
            LOG.info(LOG_PREFIX + msg);
            return;
        } else if (level == Level.FINEST && MultiverseCore.GlobalDebug >= 3) {
            LOG.info(LOG_PREFIX + msg);
            return;
        } else if (level != Level.FINE && level != Level.FINER && level != Level.FINEST) {
            LOG.log(level, LOG_PREFIX + msg);
        }
    }
}
