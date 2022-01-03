package me.temaflux.auctionemerald.database;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.security.auth.login.Configuration;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.api.AuctionItem;
import me.temaflux.auctionemerald.api.event.AuctionItemAddEvent;
import me.temaflux.auctionemerald.api.event.AuctionItemRemoveEvent;
import me.temaflux.auctionemerald.api.event.AuctionPreItemAddEvent;
import me.temaflux.auctionemerald.api.event.AuctionPreItemRemoveEvent;
import me.temaflux.auctionemerald.util.AuctionUtil;
import me.temaflux.auctionemerald.util.ItemUtil;

public class YamlDatabase
implements Database {
	private final File file;
	private YamlConfiguration config;
	private Map<UUID, List<AuctionItem>> items = new ConcurrentHashMap<>();
	
	public YamlDatabase(String fileName) {
		file = new File(plugin().getDataFolder(), fileName + ".yml");
	}
	
	@Override
	public void load() {
        create();
        config = new YamlConfiguration();
        
        try {
        	config.load(file);
        } catch (Exception | Error e) {
        	plugin().getLogger().severe("Error on load YamlDatabase:");
            e.printStackTrace();
        }
        
        for (String id : config.getKeys(false)) {
        	AuctionItem[] items;
        	try {
        		items = ItemUtil.deserialize(config.getString(id));
        	} catch (Exception | Error e) {
        		plugin().getLogger().severe("Failed parse items id: " + id + ", reason:");
        		e.printStackTrace();
        		continue;
        	}
        	
        	for (AuctionItem item : items) addItem(UUID.fromString(id), item);
        }
	}
	
	public void create() {
        if (!file.exists()) {
        	file.getParentFile().mkdirs();
            try { plugin().saveResource(file.getName(), false); }
            catch (Exception | Error e) { try { file.createNewFile(); } catch (Exception | Error e1) {} } // ignore and create file on exception
        }
	}

	@Override
	public void unload() {
		save();
	}

	public void save() {
		Collections.unmodifiableMap(items).entrySet().forEach(e -> {
			try {
				config.set(e.getKey().toString(), ItemUtil.serialize(e.getValue()));
			} catch (Exception | Error ex) {
				plugin().getLogger().severe("Failed serialize item on save:");
				ex.printStackTrace();
			}
		});
		
		try {
			config.save(file);
		} catch (IOException e) {
        	plugin().getLogger().severe("Error on save YamlDatabase:");
			e.printStackTrace();
		}
	}

	@Override
	public void addItem(Player owner, AuctionItem item) {
		addItem(AuctionUtil.getOfflineId(owner.getName().toLowerCase()), item);
	}

	@Override
	public void addItem(UUID uniqueId, AuctionItem item) {
		final AuctionPreItemAddEvent event = new AuctionPreItemAddEvent(uniqueId, item);
		plugin().getServer().getPluginManager().callEvent(event);
		uniqueId = event.getOwner();
		item = event.getItem();
		
		final List<AuctionItem> items = this.items.containsKey(uniqueId) ? this.items.get(uniqueId) : new ArrayList<>();
		items.add(item);
		if (this.items.containsKey(uniqueId)) this.items.replace(uniqueId, items);
		else this.items.put(uniqueId, items);
		
		plugin().getServer().getPluginManager().callEvent(new AuctionItemAddEvent(uniqueId, item));
	}

	@Override
	public void removeItem(Player owner, AuctionItem item) {
		removeItem(AuctionUtil.getOfflineId(owner.getName().toLowerCase()), item);
	}

	@Override
	public void removeItem(UUID uniqueId, AuctionItem item) {
		final AuctionPreItemRemoveEvent event = new AuctionPreItemRemoveEvent(uniqueId, item);
		plugin().getServer().getPluginManager().callEvent(event);
		uniqueId = event.getOwner();
		item = event.getItem();
		
		final List<AuctionItem> items = this.items.containsKey(uniqueId) ? this.items.get(uniqueId) : null;
		if (items != null) {
			items.remove(item);
			if (this.items.containsKey(uniqueId)) this.items.replace(uniqueId, items);
			else this.items.put(uniqueId, items);
		}
		
		plugin().getServer().getPluginManager().callEvent(new AuctionItemRemoveEvent(uniqueId, item));
	}

	@Override
	public List<AuctionItem> getItems(Player owner) {
		return getItems(AuctionUtil.getOfflineId(owner.getName().toLowerCase()));
	}

	@Override
	public List<AuctionItem> getItems(UUID uniqueId) {
		return Collections.unmodifiableList(items.getOrDefault(uniqueId, Arrays.asList()));
	}
	
	@Override
	public List<AuctionItem> getItems() {
		return Collections.unmodifiableList(items.values().stream().flatMap(List::stream).collect(Collectors.toList()));
	}
}
