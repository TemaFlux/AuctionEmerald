package me.temaflux.auctionemerald.api.event;

import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import me.temaflux.auctionemerald.api.AuctionItem;

public class AuctionItemRemoveEvent
extends AuctionEvent {
	private final UUID owner;
	private final AuctionItem item;
	
    public AuctionItemRemoveEvent(UUID owner, AuctionItem item) {
    	this.owner = owner;
    	this.item = item;
    }
    
    public UUID getOwner() {
		return owner;
	}
    
    public AuctionItem getItem() {
		return item;
	}
}
