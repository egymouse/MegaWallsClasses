package org.ajls.megawallsclasses.displayname;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerPlayerConnection;
import org.ajls.lib.utils.ScoreboardU;
import org.ajls.megawallsclasses.ClassEnum;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DisplayNameManager {

    //todo 到时候搞一个custom class getClassName 返回 黑暗君主 和getClassNameShort 返回黑君

    //todo 搞玩家皮肤对应职业

    public static void refresh(Player player) {
//        player.displayName(Component.text("test"));



//        player.displayName(component);
//        player.bel

//        CraftPlayer craftPlayer = (CraftPlayer) player;
//
//        ServerPlayer serverPlayer = craftPlayer.getHandle();
//        for (Player online : Bukkit.getOnlinePlayers()) {
//            ServerPlayerConnection serverPlayerConnection = serverPlayer.connection;
//            serverPlayerConnection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_DISPLAY_NAME));
//        }
    }

    public static Component getCustomName(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return getCustomName(player);  //todo add not null
    }
    public static Component getCustomName(Player player) {
        String classSimpleName = ClassEnum.getClassByPlayer(player).getClassSimpleName();
        String playerName = player.getName();
        ChatColor teamChatColor = ScoreboardU.getPlayerTeamColor(player, true);
        TextColor teamTextColor = ScoreboardU.getPlayerTeamTextColor(player, true);
        String teamName = ScoreboardU.getPlayerTeamName(player, true);
        Component component = Component.text("[", NamedTextColor.DARK_GRAY)
                .append(Component.text(classSimpleName, NamedTextColor.GRAY))
                .append(Component.text("]", NamedTextColor.DARK_GRAY))
                .append(Component.text("■", teamTextColor))
                .append(Component.text(playerName, NamedTextColor.WHITE));

        return component;
    }
}
