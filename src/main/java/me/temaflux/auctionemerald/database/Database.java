package me.temaflux.auctionemerald.database;

import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.api.AuctionItem;

public interface Database {
	void load();
	void unload();
	
	void addItem(Player owner, AuctionItem item);
	void addItem(UUID uniqueId, AuctionItem item);
	
	void removeItem(Player owner, AuctionItem item);
	void removeItem(UUID uniqueId, AuctionItem item);
	
	List<AuctionItem> getItems(Player owner);
	List<AuctionItem> getItems(UUID owner);
	List<AuctionItem> getItems();
	
	default AuctionEmeraldPlugin plugin() {
		return AuctionEmeraldPlugin.getPlugin();
	}
}
