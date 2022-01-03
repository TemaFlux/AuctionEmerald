package me.temaflux.auctionemerald.command.subs;

import org.bukkit.command.CommandSender;

import me.temaflux.auctionemerald.command.SubCommand;
import me.temaflux.auctionemerald.command.SubCommandImpl;
import me.temaflux.auctionemerald.util.StringUtil;

public class HelpCommand
extends SubCommandImpl {
	public HelpCommand() {
		super("help");
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		sendMessage(sender, StringUtil.replaceList(StringUtil.messageList("subCommands.help"), "{cmd}", label));
		return true;
	}
}
