package me.temaflux.auctionemerald;

import org.bukkit.plugin.java.JavaPlugin;

import me.temaflux.auctionemerald.command.AuctionEmeraldCommand;
import me.temaflux.auctionemerald.database.Database;
import me.temaflux.auctionemerald.database.YamlDatabase;
import me.temaflux.auctionemerald.inventory.Auction;
import me.temaflux.auctionemerald.listeners.AuctionListener;
import me.temaflux.auctionemerald.manager.AuctionManager;

public class AuctionEmeraldPlugin
extends JavaPlugin {
	private static AuctionEmeraldPlugin plugin;
	private AuctionManager manager;
	private Database database;
	
	@Override
	public void onLoad() {
		plugin = this;
		saveDefaultConfig();
		
		manager = new AuctionManager();
		
		database = new YamlDatabase("database");
		database.load();
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new AuctionListener(manager), plugin);
		getCommand("auctionemerald").setExecutor(new AuctionEmeraldCommand(this));
	}
	
	@Override
	public void onDisable() {
		manager.getAuctions().forEach(Auction::destroy);
		database.unload();
		plugin = null;
	}
	
	public static AuctionEmeraldPlugin getPlugin() {
		return plugin;
	}
	
	public AuctionManager getManager() {
		return manager;
	}
	
	public Database getDatabase() {
		return database;
	}
}
