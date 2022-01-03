package me.temaflux.auctionemerald.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {
	boolean execute(CommandSender sender, String label, String[] args);
	String getName();
	boolean isOnlyPlayer();
	String getPermission();
	boolean hasPermission(CommandSender sender);
}
