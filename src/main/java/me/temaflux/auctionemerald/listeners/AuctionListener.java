package me.temaflux.auctionemerald.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.temaflux.auctionemerald.api.event.AuctionItemAddEvent;
import me.temaflux.auctionemerald.api.event.AuctionItemRemoveEvent;
import me.temaflux.auctionemerald.inventory.Auction;
import me.temaflux.auctionemerald.manager.AuctionManager;

public class AuctionListener
implements Listener {
	private final AuctionManager manager;
	
	public AuctionListener(AuctionManager manager) {
		this.manager = manager;
	}
	
	@EventHandler
	public void onOpen(InventoryOpenEvent e) {
		final InventoryHolder holder = e.getInventory().getHolder();
		if (!(holder instanceof Auction)) return;
		manager.addViewer(e.getPlayer().getUniqueId(), (Auction) holder);
	}
	
	@EventHandler
	public void onClose(InventoryCloseEvent e) {
		final InventoryHolder holder = e.getInventory().getHolder();
		if (!(holder instanceof Auction)) return;
		manager.removeViewer(e.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if (e.getSlotType() != SlotType.CONTAINER) return;
		final Inventory i = e.getInventory();
		final InventoryHolder holder = i.getHolder();
		if (!(holder instanceof Auction) || !e.getClickedInventory().equals(i)) return;
		((Auction) holder).action((Player) e.getWhoClicked(), e.getCurrentItem());
		e.setCancelled(true);
	}
	
	@EventHandler
	public void onAdd(AuctionItemAddEvent e) {
		manager.updateAll();
	}
	
	@EventHandler
	public void onRemove(AuctionItemRemoveEvent e) {
		manager.updateAll();
	}
}
