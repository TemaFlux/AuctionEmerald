package me.temaflux.auctionemerald.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.command.subs.HelpCommand;
import me.temaflux.auctionemerald.command.subs.ReloadCommand;
import me.temaflux.auctionemerald.command.subs.SellCommand;
import me.temaflux.auctionemerald.manager.AuctionManager;
import me.temaflux.auctionemerald.util.StringUtil;

public class AuctionEmeraldCommand
implements CommandExecutor {
	private final AuctionEmeraldPlugin plugin;
	private final Map<String, SubCommand> subCommands;
	
	public AuctionEmeraldCommand(AuctionEmeraldPlugin plugin) {
		this.plugin = plugin;
		
		subCommands = new HashMap<>();
		set(new HelpCommand());
		set(new SellCommand());
		set(new ReloadCommand(plugin));
	}
	
	private void set(SubCommand subCommand) {
		final String name = subCommand.getName().toLowerCase();
		if (subCommands.containsKey(name)) subCommands.replace(name, subCommand);
		else subCommands.put(name, subCommand);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length < 1) {
			if (StringUtil.settings().getBoolean("defaultOpenMenu") && sender instanceof Player) {
				AuctionManager.open((Player) sender);
				return true;
			}
			sender.sendMessage(StringUtil.message("errors.noArgs").replace("{cmd}", label));
			return false;
		}
		
		final SubCommand subCommand = subCommands.getOrDefault(args[0].toLowerCase(), null);
		
		if (subCommand == null) {
			sender.sendMessage(StringUtil.message("errors.subCommands.unknown"));
			return false;
		} else if (subCommand.isOnlyPlayer() && !(sender instanceof Player)) {
			sender.sendMessage(StringUtil.message("errors.onlyPlayer"));
			return false;
		} else if (!subCommand.hasPermission(sender)) {
			sender.sendMessage(StringUtil.message("errors.noPerm"));
			return false;
		}
		
		return subCommand.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
	}
}
