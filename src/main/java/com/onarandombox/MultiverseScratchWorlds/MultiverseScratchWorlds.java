package com.onarandombox.MultiverseScratchWorlds;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.onarandombox.MultiverseCore.MVPlugin;
import com.onarandombox.MultiverseCore.MultiverseCore;
import com.onarandombox.MultiverseCore.commands.HelpCommand;
import com.onarandombox.MultiverseScratchWorlds.commands.ChunkCommand;
import com.onarandombox.MultiverseScratchWorlds.commands.ChunkRegenCommand;
import com.pneumaticraft.commandhandler.CommandHandler;

public class MultiverseScratchWorlds extends JavaPlugin implements MVPlugin {
    
    public static final Logger LOG = Logger.getLogger("Minecraft");
    public static final String LOG_PREFIX = "[Multiverse-ScratchWorlds] ";

    private MultiverseCore core;

    private CommandHandler commandHandler;

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
            LOG.info(LOG_PREFIX + "Multiverse-Core not found, will keep looking.");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Turn on Logging and register ourselves with Core
        LOG.info(LOG_PREFIX + "- Version " + this.getDescription().getVersion() + " Enabled");
        this.core.incrementPluginCount();
        
        // Register our commands
        this.registerCommands();

        // Done
        LOG.info(LOG_PREFIX + "Enabled");

    }

    private void registerCommands() {
        // Get a common command handler for a unified menu
        this.commandHandler = this.getCore().getCommandHandler();

        this.commandHandler.registerCommand(new ChunkCommand(this));
        this.commandHandler.registerCommand(new ChunkRegenCommand(this));

        for(com.pneumaticraft.commandhandler.Command c : this.commandHandler.getAllCommands()) {
            if(c instanceof HelpCommand) {
                c.addKey("mvsw");
            }
        }
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
