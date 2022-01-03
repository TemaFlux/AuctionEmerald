package me.temaflux.auctionemerald.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.api.AuctionItem;
import me.temaflux.auctionemerald.util.ListUtil;
import me.temaflux.auctionemerald.util.StringUtil;

public class Auction
implements InventoryHolder {
	private final Inventory inventory;
	private final Map<ItemStack, AuctionItem> items = new HashMap<>();
	private final Map<ItemStack, Consumer<Player>> actions = new HashMap<>();
	private int currentPage = 0;
	
	public Auction() {
		inventory = Bukkit.createInventory(this, 54, StringUtil.color(StringUtil.settings().getString("menu.title", "")));
		update();
	}
	
	public void update() {
		if (items.size() > 0) inventory.clear();
		int pages = updatePage(currentPage);
		
		if (currentPage > 0) inventory.setItem(45, action(createItem(
			StringUtil.settings().getString("menu.prevItem.name", ""),
			Material.valueOf(StringUtil.settings().getString("menu.prevItem.type", "BARRIER").toUpperCase()),
			StringUtil.list("settings", "menu.prevItem.lore")
		), player -> {
			currentPage--;
			update();
		}));
		
		inventory.setItem(49, action(createItem(
				StringUtil.settings().getString("menu.refreshItem.name", ""),
				Material.valueOf(StringUtil.settings().getString("menu.refreshItem.type", "BARRIER").toUpperCase()),
				StringUtil.list("settings", "menu.refreshItem.lore")
		), player -> update()));
		
		if (pages > currentPage) inventory.setItem(53, action(createItem(
			StringUtil.settings().getString("menu.nextItem.name", ""),
			Material.valueOf(StringUtil.settings().getString("menu.nextItem.type", "BARRIER").toUpperCase()),
			StringUtil.list("settings", "menu.nextItem.lore")
		), player -> {
			currentPage++;
			update();
		}));
	}
	
	public int updatePage(int page) {
		if (currentPage != page) currentPage = page;
		if (actions.size() > 0) actions.clear();
		
		int slot = 0;
		List<List<AuctionItem>> pages = getPages();
		
		if (pages.size() > 0) {
			List<AuctionItem> items = page + 1 > pages.size() ? null : pages.get(currentPage);
			if (items == null || items.size() < 1) return updatePage(currentPage - 1);
			for (AuctionItem item : items) {
				inventory.setItem(slot, action(from(item), player -> item.buy(player)));
				slot++;
			}
		}
		
		return pages.size() - 1;
	}
	
	public void action(Player player, ItemStack item) {
		Consumer<Player> action = actions.get(item);
		if (action != null) action.accept(player);
	}
	
	@Override
	public Inventory getInventory() {
		return inventory;
	}
	
	public AuctionItem getItem(ItemStack item) {
		return items.get(item);
	}
	
	public void destroy() {
		if (inventory == null) return;
		new ArrayList<>(inventory.getViewers()).forEach(HumanEntity::closeInventory);
		inventory.clear();
	}
	
	//
	
	private ItemStack createItem(String name, Material type, List<String> lore) {
		ItemStack item = new ItemStack(type);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName(StringUtil.color(name));
		if (lore != null && !lore.isEmpty()) m.setLore(lore);
		item.setItemMeta(m);
		return item;
	}
	
	private ItemStack action(ItemStack item, Consumer<Player> player) {
		if (item != null && player != null) actions.put(item, player);
		return item;
	}
	
	private ItemStack from(AuctionItem item) {
		final ItemStack n = item.item().clone();
		final ItemMeta m = n.getItemMeta();
		String name = m.getDisplayName();
		if (name == null || name.isEmpty()) name = n.getType().name();
		
		m.setDisplayName(StringUtil.color(StringUtil.settings().getString("menu.item.name", name).replace("{name}", name)));
		
		List<String> lore = m.getLore();
		if (lore == null) lore = Arrays.asList(StringUtil.color(StringUtil.settings().getString("menu.item.noLore", "")));
		
		m.setLore(StringUtil.string2List(StringUtil.list2String(StringUtil.settingsList("menu.item.lore")).replace("{owner}", item.owner()).replace("{price}", item.price() + "").replace("{lore}", StringUtil.list2String(lore)), "\n"));
		m.addItemFlags(ItemFlag.values());
		
		n.setItemMeta(m);

		items.put(n, item);
		return n;
	}
	
	private List<List<AuctionItem>> getPages() {
		return ListUtil.partitionIntegerListBasedOnSize(AuctionEmeraldPlugin.getPlugin().getDatabase().getItems(), 45);
	}
}
