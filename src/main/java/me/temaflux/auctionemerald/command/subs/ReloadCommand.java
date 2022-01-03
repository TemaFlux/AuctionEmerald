package me.temaflux.auctionemerald.command.subs;

import org.bukkit.command.CommandSender;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.command.SubCommand;
import me.temaflux.auctionemerald.command.SubCommandImpl;
import me.temaflux.auctionemerald.util.StringUtil;

public class ReloadCommand
extends SubCommandImpl {
	private final AuctionEmeraldPlugin plugin;
	
	public ReloadCommand(AuctionEmeraldPlugin plugin) {
		super("reload", false, "auctionemerald.reload");
		this.plugin = plugin;
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		plugin.reloadConfig();
		sender.sendMessage(StringUtil.message("subCommands.reload"));
		return true;
	}
}
