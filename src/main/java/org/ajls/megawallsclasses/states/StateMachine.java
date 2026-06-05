package org.ajls.megawallsclasses.states;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class StateMachine {
    private static Map<UUID, PlayerState> playerStates = new HashMap<>();

    public static void setState(Player player, PlayerState state) {
        if (state == null) {
            playerStates.remove(player.getUniqueId());
        }
        else if (state == PlayerState.NONE) {
            playerStates.remove(player.getUniqueId());
        }
        playerStates.put(player.getUniqueId(), state);
    }

    public static PlayerState getState(Player player) {
        return playerStates.getOrDefault(player.getUniqueId(), PlayerState.NONE);
    }
}
