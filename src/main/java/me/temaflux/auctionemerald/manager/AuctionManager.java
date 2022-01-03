package me.temaflux.auctionemerald.manager;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import me.temaflux.auctionemerald.inventory.Auction;

public class AuctionManager {
	private Map<UUID, Auction> viewers = new ConcurrentHashMap<>();
	
	public static void open(Player player) {
		player.openInventory(new Auction().getInventory());
	}
	
	public void addViewer(Player player, Auction auction) {
		addViewer(player.getUniqueId(), auction);
	}
	
	public void addViewer(UUID uniqueId, Auction auction) {
		viewers.put(uniqueId, auction);
	}
	
	public void removeViewer(Player player) {
		removeViewer(player.getUniqueId());
	}
	
	public void removeViewer(UUID uniqueId) {
		viewers.remove(uniqueId);
	}
	
	public void updateAll() {
		getAuctions().forEach(Auction::update);
	}
	
	public Collection<Auction> getAuctions() {
		return Collections.unmodifiableCollection(viewers.values());
	}
}
