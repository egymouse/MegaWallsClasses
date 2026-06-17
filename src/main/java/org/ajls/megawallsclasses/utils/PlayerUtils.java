package org.ajls.megawallsclasses.utils;

import org.ajls.megawallsclasses.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;

public class PlayerUtils {

    public static void setSpawnProtection(Player player) {
        PlayerDataManager.getPlayerData(player).setSpawnProtectionTicks(60);
    }
}
