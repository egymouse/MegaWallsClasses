package org.ajls.megawallsclasses.packetlisteners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.displayname.DisplayNameManager;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacketPlayerEntityMetadataAdapter extends PacketAdapter {
    public PacketPlayerEntityMetadataAdapter() {
        super(MegaWallsClasses.getPlugin(), PacketType.Play.Server.ENTITY_METADATA);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        /*
        String fakeName = "test";


//        // 设置玩家元数据（名称、皮肤、显示状态）
//        WrappedDataWatcher watcher = new WrappedDataWatcher();
//        // 玩家名称显示（1.13+ 索引）
//        WrappedDataWatcher.WrappedDataWatcherObject nameVisible =
//                new WrappedDataWatcher.WrappedDataWatcherObject(2, WrappedDataWatcher.Registry.get(String.class));
//        watcher.setObject(nameVisible, fakeName);
//        event.getPacket().getDataValueCollectionModifier().write(0, watcher.toDataValues());



        // 1. 创建空的元数据列表
        List<WrappedDataValue> dataValues = new ArrayList<>();

// 2. 手动添加每个元数据项（关键！）
// 格式：new WrappedDataValue(索引, 序列化器, 值)
        dataValues.add(new WrappedDataValue(
                2,  // 名称显示索引（1.13+）
                WrappedDataWatcher.Registry.get(String.class),  // 字符串序列化器
                fakeName  // 假玩家名称
        ));

        dataValues.add(new WrappedDataValue(
                17,  // 皮肤加载索引
                WrappedDataWatcher.Registry.get(Byte.class),  // 字节序列化器
                (byte) 127  // 皮肤加载标记
        ));

// 3. 写入数据包（正确写法）✅
        event.getPacket().getDataValueCollectionModifier().write(0, dataValues);

         */




        // ============== 核心防护：只处理【玩家实体】，否则直接跳过 ==============
//        int entityId = event.getPacket().getIntegers().read(0);
//        Entity entity = event.getPacket().getEntityModifier(event.getPlayer().getWorld()).read(0);
        int entityId = event.getPacket().getIntegers().read(0);
        // ✅ 正确：通过服务器根据ID获取实体，不从数据包读取
        Entity entity = getEntityByEntityID(entityId);

        // 非玩家实体，直接返回，不修改
        if (!(entity instanceof Player)) {
            return;
        }

        String fakeName = "test";

        // ============== 读取原有元数据，不覆盖！避免崩溃 ==============
        List<WrappedDataValue> dataValues = event.getPacket().getDataValueCollectionModifier().read(0);

        // ============== 修改/添加 玩家名称（索引2：自定义名称，索引3：名称可见） ==============
//        // 1. 设置自定义名称
//        setOrAddDataValue(dataValues, 2, WrappedDataWatcher.Registry.get(String.class), fakeName);

        // ==============================================
        // 🔥 核心修复：1.20+ 索引2 必须用 Optional<String>
        // ==============================================
        // 索引2：自定义名称 → 类型 Optional<String>
        setOrAddDataValue(
                dataValues,
                2,
                WrappedDataWatcher.Registry.get(Optional.class), // 关键：getOptional
                Optional.ofNullable(fakeName)
        );

        // 2. 强制显示名称（必须加，否则名字不显示）
        setOrAddDataValue(dataValues, 3, WrappedDataWatcher.Registry.get(Boolean.class), true);


        // ============== 写回修改后的元数据 ==============
        event.getPacket().getDataValueCollectionModifier().write(0, dataValues);

    }


    /**
     * 工具方法：安全修改/添加元数据（不存在则添加，存在则修改）
     */
    private void setOrAddDataValue(List<WrappedDataValue> list, int index, WrappedDataWatcher.Serializer serializer, Object value) {
        // 查找已存在的索引
        Optional<WrappedDataValue> existing = list.stream().filter(v -> v.getIndex() == index).findFirst();
        if (existing.isPresent()) {
            existing.get().setValue(value);
        } else {
            list.add(new WrappedDataValue(index, serializer, value));
        }
    }

    private Player getPlayerByEntityID(int entityID) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getEntityId() == entityID) {
                return p;
            }
        } //get sender
        return null;
    }

    private Entity getEntityByEntityID(int entityID) {
        for (World world: Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.getEntityId() == entityID) {
                    return entity;
                }
            }
        }
        return null;
//        for (Entity e : Bukkit.get) {}
//        for (Player p : Bukkit.getOnlinePlayers()) {
//            if (p.getEntityId() == entityID) {
//                return p;
//            }
//        } //get sender
//        return null;
    }
}
