package me.temaflux.auctionemerald.api.event;

import java.util.UUID;

import org.bukkit.event.Cancellable;

import me.temaflux.auctionemerald.api.AuctionItem;

public class AuctionItemAddEvent
extends AuctionEvent {
	private final UUID owner;
	private final AuctionItem item;
	
    public AuctionItemAddEvent(UUID owner, AuctionItem item) {
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
