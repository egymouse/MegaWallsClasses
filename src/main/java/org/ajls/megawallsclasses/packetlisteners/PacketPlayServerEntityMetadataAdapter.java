package org.ajls.megawallsclasses.packetlisteners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketPlayServerEntityMetadataAdapter extends PacketAdapter {
    // ========== 关键：MC 1.19+ 实体元数据索引（版本不同需修改！）==========
    private static final int INDEX_CUSTOM_NAME = 2;      // 自定义名称
    private static final int INDEX_NAME_VISIBLE = 3;    // 名称可见性

    public PacketPlayServerEntityMetadataAdapter() {
        super(MegaWallsClasses.getPlugin(), PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packetContainer = event.getPacket();
        PacketContainer packet = event.getPacket();
        Player receiver = event.getPlayer();
        int entityID = packetContainer.getIntegers().read(0); // 获取实体ID
        Player targetPlayer = getPlayerByEntityID(entityID);
        if (targetPlayer == null) { return; }
//        List<WrappedDataValue> dataValues = packetContainer.getDataValueCollectionModifier().read(0);
//        String customName = "tesat";
//        WrappedChatComponent nameComponent = WrappedChatComponent.fromText(customName);


        //door包
//        // 1. 设置自定义名称（支持颜色代码）
//        dataValues.add(new WrappedDataValue(INDEX_CUSTOM_NAME, WrappedDataValue.SERIALIZER_CHAT, Optional.of(nameComponent)));
//        // 2. 强制开启名称可见（必须设置，否则不显示）
//        dataValues.add(new WrappedDataValue(INDEX_NAME_VISIBLE, WrappedDataValue.SERIALIZER_BOOLEAN, true));


        //deepseek
//        // 获取 1.21 兼容的 Serializer
//        WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer();
//        WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
//
//        // 移除已存在的同名索引（避免冲突）
//        dataValues.removeIf(v -> v.getIndex() == INDEX_CUSTOM_NAME);
//        dataValues.removeIf(v -> v.getIndex() == INDEX_NAME_VISIBLE);
//
//        // 添加新数据值（注意：WrappedChatComponent 需转为原生 Component）
//        dataValues.add(new WrappedDataValue(INDEX_CUSTOM_NAME, chatSerializer, Optional.of(nameComponent.getHandle())));
//        dataValues.add(new WrappedDataValue(INDEX_NAME_VISIBLE, boolSerializer, true));
//
//
//        // 获取 1.21 兼容的序列化器
//        WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer();
//        WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);


        // 写入修改后的元数据
//        packetContainer.getDataValueCollectionModifier().write(0, dataValues);




        //deepseek2
//        // 获取原始元数据列表（注意：可能为 null）
//        List<WrappedDataValue> originalList = packetContainer.getDataValueCollectionModifier().read(0);
//        // 创建新列表，避免修改不可变集合
//        List<WrappedDataValue> newDataValues = new ArrayList<>();
//
//        String customName = "&a测试名字";  // 绿色名字示例
//        WrappedChatComponent nameComponent = WrappedChatComponent.fromText(
//                org.bukkit.ChatColor.translateAlternateColorCodes('&', customName)
//        );
//
//        // 获取 1.21 兼容的序列化器
//        WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer();
//        WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
//
//        // 复制除了 2 和 3 之外的所有原有元数据
//        if (originalList != null) {
//            for (WrappedDataValue value : originalList) {
//                int index = value.getIndex();
//                if (index != INDEX_CUSTOM_NAME && index != INDEX_NAME_VISIBLE) {
//                    newDataValues.add(value);
//                }
//            }
//        }
//
//        // 添加新的自定义名称（必须用 Optional 包装）
//        newDataValues.add(new WrappedDataValue(
//                INDEX_CUSTOM_NAME,
//                chatSerializer,
//                Optional.of(nameComponent.getHandle())  // 关键：unwrap 原生 Component
//        ));
//
//        // 添加名称可见性（直接 boolean 即可）
//        newDataValues.add(new WrappedDataValue(
//                INDEX_NAME_VISIBLE,
//                boolSerializer,
//                true
//        ));
//
//        // 写回修改后的列表
//        packetContainer.getDataValueCollectionModifier().write(0, newDataValues);


        //deepseek3
//// 获取原始元数据列表（可能为 null 或不可变）
//        List<WrappedDataValue> originalList = packet.getDataValueCollectionModifier().read(0);
//        List<WrappedDataValue> newList = new ArrayList<>();
//
//        // 自定义名称（支持颜色代码）
//        String customName = ChatColor.translateAlternateColorCodes('&', "&a测试名字");
//        WrappedChatComponent nameComponent = WrappedChatComponent.fromText(customName);
//
//        // ========== 关键修正：使用正确的 Optional 序列化器 ==========
//        // 1. 先获取 Component 的基本序列化器
//        WrappedDataWatcher.Serializer componentSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer();
//        // 2. 再获取 Optional<Component> 的序列化器
//        WrappedDataWatcher.Serializer optionalComponentSerializer =
//                WrappedDataWatcher.Registry.getOptionalSerializer(componentSerializer);
//        // 3. 布尔值的序列化器（不变）
//        WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);
//
//        // 复制除了索引 2 和 3 之外的所有原有元数据
//        if (originalList != null) {
//            for (WrappedDataValue value : originalList) {
//                int idx = value.getIndex();
//                if (idx != INDEX_CUSTOM_NAME && idx != INDEX_NAME_VISIBLE) {
//                    newList.add(value);
//                }
//            }
//        }
//
//        // 添加新的自定义名称（用 Optional 包装 Component）
//        newList.add(new WrappedDataValue(
//                INDEX_CUSTOM_NAME,
//                optionalComponentSerializer,    // 使用 Optional 序列化器
//                Optional.of(nameComponent.getHandle())  // 原生 Component
//        ));
//
//        // 添加名称可见性（直接 boolean）
//        newList.add(new WrappedDataValue(
//                INDEX_NAME_VISIBLE,
//                boolSerializer,
//                true
//        ));
//
//        // 写回修改后的数据
//        packet.getDataValueCollectionModifier().write(0, newList);


        // 获取原始元数据列表（可能为 null 或不可变）
        List<WrappedDataValue> originalList = packet.getDataValueCollectionModifier().read(0);
        List<WrappedDataValue> newList = new ArrayList<>();

// 自定义名称（支持颜色代码）
        String customName = ChatColor.translateAlternateColorCodes('&', "&a测试名字");
        WrappedChatComponent nameComponent = WrappedChatComponent.fromText(customName);

// ========== 修改点：使用 getChatComponentSerializer(true) 替代 getOptionalSerializer ==========
// 直接获取支持 Optional<Component> 的序列化器
        WrappedDataWatcher.Serializer optionalComponentSerializer =
                WrappedDataWatcher.Registry.getChatComponentSerializer(true);
// 布尔值的序列化器（不变）
        WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);

// 复制除了索引 2 和 3 之外的所有原有元数据
        if (originalList != null) {
            for (WrappedDataValue value : originalList) {
                int idx = value.getIndex();
                if (idx != INDEX_CUSTOM_NAME && idx != INDEX_NAME_VISIBLE) {
                    newList.add(value);
                }
            }
        }

// 添加新的自定义名称（仍然用 Optional 包装 Component）
        newList.add(new WrappedDataValue(
                INDEX_CUSTOM_NAME,
                optionalComponentSerializer,    // 现在这个序列化器直接接受 Optional<Component>
                Optional.of(nameComponent.getHandle())
        ));

// 添加名称可见性
        newList.add(new WrappedDataValue(
                INDEX_NAME_VISIBLE,
                boolSerializer,
                true
        ));

// 写回修改后的数据
        packet.getDataValueCollectionModifier().write(0, newList);


    }


    private Player getPlayerByEntityID(int entityID) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getEntityId() == entityID) {
                return p;
            }
        } //get sender
        return null;
    }

}
