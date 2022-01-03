package me.temaflux.auctionemerald.api.event;

import java.util.UUID;

import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import me.temaflux.auctionemerald.api.AuctionItem;

public class AuctionPreItemAddEvent
extends AuctionCancelEvent {
	private UUID owner;
	private AuctionItem item;
	
    public AuctionPreItemAddEvent(UUID owner, AuctionItem item) {
    	this.owner = owner;
    	this.item = item;
    }
    
    public void setOwner(UUID owner) {
		this.owner = owner;
	}
    
    public void setItem(AuctionItem item) {
		this.item = item;
	}
    
    public UUID getOwner() {
		return owner;
	}
    
    public AuctionItem getItem() {
		return item;
	}
}
