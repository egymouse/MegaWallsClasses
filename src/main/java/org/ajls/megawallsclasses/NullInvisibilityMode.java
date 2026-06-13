package org.ajls.megawallsclasses;

import org.ajls.lib.advanced.HashMapInteger;
import org.bukkit.ChatColor;

import java.util.UUID;

public class NullInvisibilityMode {
    public HashMapInteger<UUID> null_invisibility_mode = new HashMapInteger<>(3);
    public NullInvisibilityMode() {}
    public int get(UUID uuid) {
        return null_invisibility_mode.get(uuid);
    }
    public void switchMode(UUID uuid) {
        int mode = null_invisibility_mode.get(uuid);
        if (mode == 0) {
            null_invisibility_mode.put(uuid, 1);
        }
        else{
            null_invisibility_mode.put(uuid, 0);
        }
    }

    public int put(UUID uuid, int mode) {
        return null_invisibility_mode.put(uuid, mode);
    }

    public ChatColor getColor(UUID uuid) {
        int mode = null_invisibility_mode.get(uuid);
        if (mode == 0) {
            return ChatColor.RED;  //cd没好
        }
        else if (mode == 1) {
            return ChatColor.GREEN;  //
        }
        else if (mode == 2) {
            return ChatColor.GOLD;
        }
        else if (mode == 3) {
            return ChatColor.DARK_RED;
        }
        return ChatColor.RED;
    }
}
