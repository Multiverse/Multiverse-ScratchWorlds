package com.onarandombox.MultiverseScratchWorlds.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.onarandombox.MultiverseScratchWorlds.MultiverseScratchWorlds;

public class ChunkRegenCommand extends ScratchWorldsCommand {
    public ChunkRegenCommand(MultiverseScratchWorlds plugin) {
        super(plugin);

        this.setName("Chunk regenerate");
        this.setCommandUsage("/mvsw chunk regen [world] [x z]");
        this.setArgRange(0, 3);
        this.addKey("mvsw chunk regen");
        this.setPermission("multiverse.scratchworlds.chunkregen", "Regenerate chunk.", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if(!(sender instanceof Player) && (args.size() != 3)) {
            sender.sendMessage(ChatColor.RED + "This command must be run from console.");
            return;
        }
        Player player = (Player)sender;

        Chunk chunk = null;
        World world = null;
        switch(args.size()) {
        case 0:
            world = player.getLocation().getWorld();
            chunk = player.getLocation().getBlock().getChunk();
            break;
        case 1:
            world = this.getPlugin().getServer().getWorld(args.get(0));
            if(world == null) break;
            chunk = player.getLocation().getBlock().getChunk();
            chunk = world.getChunkAt(chunk.getX(), chunk.getZ());
            break;
        case 2:
            world = player.getLocation().getWorld();
            chunk = world.getChunkAt(Integer.parseInt(args.get(0)), Integer.parseInt(args.get(1)));
            break;
        case 3:
            world = this.getPlugin().getServer().getWorld(args.get(0));
            if(world == null) break;
            chunk = world.getChunkAt(Integer.parseInt(args.get(1)), Integer.parseInt(args.get(2)));
            break;
        default:
            sender.sendMessage(ChatColor.RED + "Invalid argument count for chunk regen - this is a bug!");
            return;
        }

        if(chunk == null || world == null) {
            sender.sendMessage(ChatColor.RED + "Couldn't locate chunk to regenerate - this may be a bug!");
            return;
        }

        boolean result = world.regenerateChunk(chunk.getX(), chunk.getZ());
        if(!result) {
            sender.sendMessage(ChatColor.RED + "Couldn't regenerate chunk. Don't know why.");
            return;
        }

        result = world.refreshChunk(chunk.getX(), chunk.getZ());
        if(!result) {
            sender.sendMessage(ChatColor.RED + "Couldn't refresh chunk. Don't know why.");
            return;
        }
    }
}
