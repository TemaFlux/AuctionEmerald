package me.temaflux.auctionemerald.command;

import java.util.List;

import org.bukkit.command.CommandSender;

import me.temaflux.auctionemerald.command.exception.NoExecuteException;
import me.temaflux.auctionemerald.util.StringUtil;

public class SubCommandImpl
implements SubCommand {
	private final String name;
	private final boolean onlyPlayer;
	private final String permission;
	
	public SubCommandImpl(String name) {
		this(name, false);
	}
	
	public SubCommandImpl(String name, boolean onlyPlayer) {
		this(name, onlyPlayer, null);
	}
	
	public SubCommandImpl(String name, boolean onlyPlayer, String permission) {
		this.name = name;
		this.onlyPlayer = onlyPlayer;
		this.permission = permission;
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		throw new NoExecuteException("SubCommand (" + name + ") not exist exceute!");
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public boolean isOnlyPlayer() {
		return onlyPlayer;
	}
	
	@Override
	public String getPermission() {
		return permission;
	}
	
	@Override
	public boolean hasPermission(CommandSender sender) {
		return permission == null || sender.hasPermission(permission);
	}
	
	public void sendMessage(CommandSender sender, List<String> lines) {
		lines.forEach(line -> sender.sendMessage(StringUtil.color(line)));
	}
}
