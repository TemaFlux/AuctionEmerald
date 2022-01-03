package me.temaflux.auctionemerald.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import net.md_5.bungee.api.ChatColor;

public class StringUtil {
	public static String color(String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static ConfigurationSection settings() {
		return config().getConfigurationSection("settings");
	}
	
	public static String message(String id) {
		return color(config().getConfigurationSection("messages").getString(id, id));
	}
	
	public static List<String> settingsList(String id) {
		return list("settings", id);
	}
	
	public static List<String> messageList(String id) {
		return list("messages", id);
	}
	
	public static List<String> list(String section, String id) {
		final List<String> lines = config().getConfigurationSection(section).getStringList(id);
		return lines.isEmpty() ? lines : lines.stream().map(StringUtil::color).collect(Collectors.toList());
	}
	
	public static List<String> replaceList(List<String> list, CharSequence target, CharSequence replacement) {
		return list.stream().map(text -> text.replace(target, replacement)).collect(Collectors.toList());
	}
	
	public static List<String> string2List(String list, String regex) {
		return new ArrayList<>(Arrays.asList(list.split(regex)));
	}
	
	public static String list2String(List<String> list) {
		return list.stream().collect(Collectors.joining("\n"));
	}
	
	public static FileConfiguration config() {
		return AuctionEmeraldPlugin.getPlugin().getConfig();
	}
}
