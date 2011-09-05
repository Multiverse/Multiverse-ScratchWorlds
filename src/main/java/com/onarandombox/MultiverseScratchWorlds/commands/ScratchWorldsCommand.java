package com.onarandombox.MultiverseScratchWorlds.commands;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.onarandombox.MultiverseScratchWorlds.MultiverseScratchWorlds;

import com.pneumaticraft.commandhandler.Command;

public abstract class ScratchWorldsCommand extends Command {
    protected MultiverseScratchWorlds plugin;

    public ScratchWorldsCommand(MultiverseScratchWorlds plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public abstract void runCommand(CommandSender sender, List<String> args);
}
