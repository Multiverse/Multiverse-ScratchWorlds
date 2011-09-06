package com.onarandombox.MultiverseScratchWorlds;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MVPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.commands.HelpCommand;
import com.onarandombox.MultiverseScratchWorlds.commands.ChunkRegenCommand;
import com.onarandombox.MultiverseScratchWorlds.commands.MagicCommand;
import com.pneumaticraft.commandhandler.CommandHandler;

public class MultiverseScratchWorlds extends JavaPlugin implements MVPlugin {
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    public static final String LOG_PREFIX = "[Multiverse-ScratchWorlds] ";

    private MultiverseCore core;
    private CommandHandler commandHandler;
    private SWConfiguration swConfig;
    protected Map<String, SWWorld> scratchWorlds = new HashMap<String, SWWorld>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
        if(!this.isEnabled()) {
            return true;
        }

        List<String> allArgs = new ArrayList<String>(Arrays.asList(args));
        allArgs.add(0, command.getName());
        return this.commandHandler.locateAndRunCommand(sender, allArgs);
    }

    @Override
    public void onLoad() {
        this.getDataFolder().mkdirs();
    }

    @Override
    public void onEnable() {
        this.core = (MultiverseCore) this.getServer().getPluginManager().getPlugin("Multiverse-Core");

        // Test if the Core was found, if not we'll disable this plugin.
        if (this.core == null) {
            LOG.info(LOG_PREFIX + "Multiverse-Core not found; disabling Multiverse-ScratchWorlds");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Turn on Logging and register ourselves with Core
        LOG.info(LOG_PREFIX + "- Version " + this.getDescription().getVersion() + " enabled");
        this.core.incrementPluginCount();

        // Read the configuration
        this.readConfig();
        
        // Register our commands
        this.registerCommands();

        // Done
        LOG.info(LOG_PREFIX + "Enabled");
    }

    /**
     * Read the configuration from disk
     */
    private void readConfig() {
        // Read configuration file
        File configFile = new File(this.getDataFolder(), SWConfiguration.CONFIG_FILE_NAME);
        this.swConfig = SWConfiguration.detectConfiguration(configFile);
        while(this.swConfig.getVersion() != SWConfiguration.LATEST_CONFIG_VERSION) {
            SWConfiguration upgraded = this.swConfig.upgrade();
            if(upgraded == null) {
                LOG.warning(LOG_PREFIX + "Unable to upgrade configuration from version " + this.swConfig.getVersion() + ".");
                LOG.warning(LOG_PREFIX + "You may experience some instability. Continuing anyway...");
                break;
            }
            this.swConfig = upgraded;
        }
       
        // Add worlds marked scratch in config
        for(String scratchWorldName : this.swConfig.readScratchWorldNames()) {
            this.addScratchWorld(this.getServer().getWorld(scratchWorldName));
        }
    }

    /**
     * Write the configuration to disk
     */
    private void writeConfig() {
        if(!this.swConfig.write(this)) {
            LOG.warning(LOG_PREFIX + "Failed to save configuration file (version " + this.swConfig.getVersion() + "); continuing anyway...");
        }
    }

    /**
     * Register all commands provided by the plugin.
     */
    private void registerCommands() {
        // Get a common command handler for a unified menu
        this.commandHandler = this.getCore().getCommandHandler();

        // Load the commands implemented by the plugin
        this.commandHandler.registerCommand(new ChunkRegenCommand(this));
        this.commandHandler.registerCommand(new MagicCommand(this));

        // Add the "root" command for this plugin to the help key list
        for(com.pneumaticraft.commandhandler.Command c : this.commandHandler.getAllCommands()) {
            if(c instanceof HelpCommand) {
                c.addKey("mvsw");
            }
        }
    }

    public Map<String, SWWorld> getScratchWorlds() {
        return this.scratchWorlds;
    }

    public Set<String> getScratchWorldNames() {
        return this.scratchWorlds.keySet();
    }

    public void removeScratchWorld(World world) {
        this.scratchWorlds.remove(world.getName());
    }

    public void addScratchWorld(World world) {
        if(world != null) {
            SWWorld scratchWorld = new SWWorld(world);
            String worldName = world.getName(); 
            boolean shouldReseed = this.swConfig.readShouldReseed(worldName);
            scratchWorld.setShouldReseed(shouldReseed);
            this.scratchWorlds.put(worldName, scratchWorld);
        } else {
            LOG.warning(LOG_PREFIX + "Error loading world! Continuing...");
        }
    }

    @Override
    public void onDisable() {
        this.writeConfig();

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

    /**
     * Print the argument to the Minecraft log at INFO level, and return the
     * formatted log message as a String (to be appended to a buffer elsewhere).
     *
     * @param string The message to print, format, and return.
     * @return The formatted log message that was printed.
     */
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
