package com.onarandombox.MultiverseScratchWorlds.commands;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.ChunkSnapshot;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;

import com.onarandombox.MultiverseScratchWorlds.MultiverseScratchWorlds;

public class MagicCommand extends ScratchWorldsCommand {
    public MagicCommand(MultiverseScratchWorlds plugin) {
        super(plugin);

        this.setName("Magic");
        this.setCommandUsage("/mvsw magic");
        this.setArgRange(0, 0);
        this.addKey("mvsw magic");
        this.setPermission("multiverse.scratchworlds.magic", "Do magic! OOOoooOOOooo...", PermissionDefault.OP);
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command must be run from in-game.");
            return;
        }

        Player player = (Player)sender;
        Location loc = player.getLocation();
        Chunk chunk = loc.getBlock().getChunk();

        sender.sendMessage("You are in chunk (" + chunk.getX() + "," + chunk.getZ() + ")");

        ChunkSnapshot snapshot = chunk.getChunkSnapshot();
        sender.sendMessage("Chunk snapshot has underlying class " + snapshot.getClass().toString());
    }
}
