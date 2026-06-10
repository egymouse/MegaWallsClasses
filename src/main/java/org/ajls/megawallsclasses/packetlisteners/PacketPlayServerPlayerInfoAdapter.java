package org.ajls.megawallsclasses.packetlisteners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import net.kyori.adventure.text.Component;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.displayname.DisplayNameManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PacketPlayServerPlayerInfoAdapter extends PacketAdapter {
    public PacketPlayServerPlayerInfoAdapter() {
        super(MegaWallsClasses.getPlugin(), PacketType.Play.Server.PLAYER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Player viewer = event.getPlayer(); // 看的人
        if (viewer == null) return;

// 只处理添加玩家动作
        if (packet.getPlayerInfoAction().read(0) != EnumWrappers.PlayerInfoAction.ADD_PLAYER)
            return;

        // 读取原有玩家数据列表
        List<PlayerInfoData> oldDataList = packet.getPlayerInfoDataLists().read(0);
        List<PlayerInfoData> newDataList = new ArrayList<>();

        // 遍历重建数据（解决不可变对象问题）
        for (PlayerInfoData data : oldDataList) {
            WrappedGameProfile oldProfile = data.getProfile();
            UUID targetUuid = oldProfile.getUUID();



//            // 无自定义名 → 直接保留原数据
//            if (!customNames.containsKey(targetUuid)) {
//                newDataList.add(data);
//                continue;
//            }


            // ================= 关键修复：新建 Profile（替代 clone/setName） =================
//            String newName = customNames.get(targetUuid);
            String newName = DisplayNameManager.getCustomName(targetUuid).toString();
            WrappedGameProfile newProfile = new WrappedGameProfile(targetUuid, newName);

            // 复制皮肤数据（必须加！否则皮肤会变成史蒂夫）
            for (WrappedSignedProperty property : oldProfile.getProperties().values()) {
                newProfile.getProperties().put(property.getName(), property);
            }

            // 新建 PlayerInfoData（替代 setProfile）
            PlayerInfoData newData = new PlayerInfoData(
                    newProfile,
                    data.getLatency(),
                    data.getGameMode(),
                    data.getDisplayName()
            );

            newDataList.add(newData);
        }

        // 写入新的列表
        packet.getPlayerInfoDataLists().write(0, newDataList);
    }
}
