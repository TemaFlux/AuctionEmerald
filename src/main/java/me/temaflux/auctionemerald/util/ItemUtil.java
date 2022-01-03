package me.temaflux.auctionemerald.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import me.temaflux.auctionemerald.api.AuctionItem;

public class ItemUtil {
	public static String serialize(List<AuctionItem> items) throws IOException {
		return items != null && items.size() > 0 ? serialize(items.toArray(new AuctionItem[items.size() - 1])) : "";
	}
	
	public static String serialize(AuctionItem... items) throws IOException {
		if (items == null || items.length < 1) return "";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
        
        dataOutput.writeInt(items.length);
        
        for (int i = 0; i < items.length; i++) {
        	AuctionItem item = items[i];
	        dataOutput.writeUTF(item.owner().toString());
	        dataOutput.writeInt(item.price());
	        dataOutput.writeObject(item.item());
        }
        
        dataOutput.close();
        return Base64Coder.encodeLines(outputStream.toByteArray());
	}
	
	public static AuctionItem[] deserialize(String data) throws IOException {
		if (data == null || data.isEmpty()) return new AuctionItem[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
        BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
        AuctionItem[] items = new AuctionItem[dataInput.readInt()];
        
        for (int i = 0; i < items.length; i++) {
			try {
				items[i] = new AuctionItem(dataInput.readUTF(), dataInput.readInt(), (ItemStack) dataInput.readObject());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
        }
        
        dataInput.close();
        return items;
	}
	
    public static void removeMaterial(Player p, Material m, Integer amount) {
    	for (ItemStack item : p.getInventory().all(m).values()) {
    		if (amount - item.getAmount() >= 0) {
    			removeItem(p, item);
    			amount -= item.getAmount();
    		} else {
    			item.setAmount((item.getAmount() - amount));
    			break;
    		}
        }
    }
    
    public static void removeItem(Player player, ItemStack item) {
        ItemStack[] items = player.getInventory().getStorageContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null || !items[i].equals(item)) continue;
            player.getInventory().clear(i);
            break;
        }
    }
}
