package me.temaflux.auctionemerald.util;

import java.util.UUID;

import com.google.common.base.Charsets;

public class AuctionUtil {
	public static UUID getOfflineId(String name) {
		return UUID.nameUUIDFromBytes( ( "OfflinePlayer:" + name ).getBytes( Charsets.UTF_8 ) );
	}
}
