package me.temaflux.auctionemerald.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.manager.AuctionManager;
import me.temaflux.auctionemerald.util.AuctionUtil;
import me.temaflux.auctionemerald.util.ItemUtil;
import me.temaflux.auctionemerald.util.StringUtil;

public class AuctionItem {
	private final String owner;
	private final int price;
	private final ItemStack item;
	
	public AuctionItem(String owner, int price, ItemStack item) {
		this.owner = owner;
		this.price = price;
		this.item = item;
	}
	
	public String owner() {
		return owner;
	}
	
	public int price() {
		return price;
	}
	
	public ItemStack item() {
		return item;
	}
	
	public boolean buy(Player player) {
		Collection<? extends ItemStack> emeralds = player.getInventory().all(Material.EMERALD).values();
		int count = 0;
		for (ItemStack i : emeralds) count += i.getAmount();
		
		if (price > count) {
			player.sendMessage(StringUtil.message("errors.buy.notEnough"));
			return false;
		}
		
		ItemUtil.removeMaterial(player, Material.EMERALD, price);
		
		AuctionEmeraldPlugin.getPlugin().getDatabase().removeItem(AuctionUtil.getOfflineId(owner.toLowerCase()), this);
		player.getInventory().addItem(item);
		
		player.sendMessage(StringUtil.message("subCommands.buy").replace("{price}", price + ""));
		return true;
	}
}
