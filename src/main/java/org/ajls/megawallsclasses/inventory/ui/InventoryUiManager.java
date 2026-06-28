package org.ajls.megawallsclasses.inventory.ui;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryUiManager {

    private final Map<UUID, InventoryUiSession> sessions = new HashMap<>();

    public void setSession(Player player, InventoryUiSession session) {
        sessions.put(player.getUniqueId(), session);
    }

    public InventoryUiSession getSession(Player player) {
        return sessions.get(player.getUniqueId());
    }

    public void clearSession(Player player) {
        sessions.remove(player.getUniqueId());
    }

    public boolean isInUi(Player player, UiType type) {
        InventoryUiSession session = getSession(player);
        return session != null && session.type() == type;
    }
}
