package org.ajls.megawallsclasses.playerdata;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    static HashMap<UUID, PlayerData> player_playerData = new HashMap<UUID, PlayerData>();


    public static void register(Player player) {
        if (player_playerData.containsKey(player.getUniqueId())) { return; }
        player_playerData.put(player.getUniqueId(), new PlayerData());
    }


    public static void unregister(Player player) {
        player_playerData.remove(player.getUniqueId());
    }

    public static PlayerData getPlayerData(Player player) {
        return player_playerData.get(player.getUniqueId());
    }
}
