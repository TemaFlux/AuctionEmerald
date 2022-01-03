package me.temaflux.auctionemerald.command.subs;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.temaflux.auctionemerald.AuctionEmeraldPlugin;
import me.temaflux.auctionemerald.api.AuctionItem;
import me.temaflux.auctionemerald.command.SubCommand;
import me.temaflux.auctionemerald.command.SubCommandImpl;
import me.temaflux.auctionemerald.database.Database;
import me.temaflux.auctionemerald.manager.AuctionManager;
import me.temaflux.auctionemerald.util.ItemUtil;
import me.temaflux.auctionemerald.util.StringUtil;

public class SellCommand
extends SubCommandImpl {
	public SellCommand() {
		super("sell", true);
	}
	
	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if (args.length < 1) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.noArg"));
			return false;
		}
		
		Integer price;
		
		try { 
			price = Integer.valueOf(args[0]);
		} catch (NumberFormatException e) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.notNumber"));
			return false;
		}
		
		// check price
		
		Integer min = StringUtil.settings().getInt("sell.min", 1);
		
		if (price < min) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.min.price").replace("{price}", min.toString()));
			return false;
		}
		
		Integer max = StringUtil.settings().getInt("sell.max", 1000);
		if (price > max) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.max.price").replace("{price}", max.toString()));
			return false;
		}
		
		// check count
		
		int count = 1;
		
		if (args.length > 1) try { count = Integer.valueOf(args[1]); } catch (NumberFormatException e) {}
		
		if (count < 1) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.min.item"));
			return false;
		}
		
		if (count > 64) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.max.item"));
			return false;
		}
		
		Player player = (Player) sender;
		
		// check placed
		
		final Database database = AuctionEmeraldPlugin.getPlugin().getDatabase();
		if (database.getItems(player).size() > StringUtil.settings().getInt("sell.maxPlace", 3)) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.place"));
			return false;
		}
		
		//
		
		ItemStack original = player.getInventory().getItemInMainHand();
		
		if (original == null || original.getType() == Material.AIR) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.item.air"));
			return false;
		}
		
		if (StringUtil.settings().getStringList("sell.blockedMaterials").contains(original.getType().name())) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.item.blocked"));
			return false;
		}
		
		ItemStack item = original.clone();
		
		if (item.getAmount() < count) {
			sender.sendMessage(StringUtil.message("errors.subCommands.sell.notEnough"));
			return false;
		}
		
		item.setAmount(count);
		
		int value = original.getAmount() - count;
		if (value <= 0) ItemUtil.removeItem(player, original);
		else original.setAmount(value);
		
		AuctionEmeraldPlugin.getPlugin().getDatabase().addItem(player, new AuctionItem(player.getName().toLowerCase(), price, item));
		sender.sendMessage(StringUtil.message("subCommands.sell").replace("{price}", price.toString()));
		return true;
	}
}
