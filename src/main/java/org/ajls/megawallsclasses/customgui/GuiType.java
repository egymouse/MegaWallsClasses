package org.ajls.megawallsclasses.customgui;

import org.ajls.megawallsclasses.ClassEnum;
import org.ajls.megawallsclasses.states.PlayerState;
import org.ajls.megawallsclasses.states.StateMachine;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public enum GuiType {
    NONE("NONE"),
    SELECT_CLASS("Select Class"),
    REORDER_INVENTORY("Reorder inventory"),
    CLASS_REORDER("ClassReorderInventory"),
    WHICH_CLASS_REORDER("WhichClassReorder"),
    TEAM_SELECTION("TeamSelection"),
    MW_MENU("MW Menu"),
    MW_TOOL_KIT("MW Tool Kit");

    private final String title;
    private final PlayerState playerState;
    private final GuiHandlerI guiHandlerI;

    GuiType(String title) {
        this.title = title;
        this.guiHandlerI = null;
        this.playerState = null;
    }

    GuiType(String title, GuiHandlerI guiHandlerI) {
        this.title = title;
        this.guiHandlerI = guiHandlerI;
        this.playerState = null;
    }

    GuiType(PlayerState playerState, GuiHandlerI guiHandlerI) {
        this.title = null;
        this.guiHandlerI = guiHandlerI;
        this.playerState = playerState;
    }

    public static GuiType fromTitle(String title) {
        for (GuiType type : values()) {
            if (type.title.equals(title)) return type;
        }
        return null;
    }


//    private static final Map<GuiType, GuiHandlerI> HANDLERS = new HashMap<>();  //改成从title查询和PlayerState查询
//
//    static {
//        HANDLERS.put(GuiType.SELECT_CLASS, new ClassSelectionGuiHandler());
////        HANDLERS.put(GuiType.TEAM_SELECTION, new TeamSelectionHandler());
////        HANDLERS.put(GuiType.MW_MENU, new MainMenuHandler());
//        // ...
//    }

    // ===================== 核心：根据ID查找 =====================
    // 1. 静态Map缓存：类加载时就把ID和枚举绑定好，查找速度极快（O(1)）
    private static final Map<PlayerState, GuiType> HANDLERS;
    static {
        HANDLERS = Arrays.stream(values())
                .collect(Collectors.toMap(
                        guiType -> guiType.playerState,   // Key：职业ID
                        guiType -> guiType          // Value：枚举对象
                ));
    }

    private GuiHandlerI getHandler(GuiType type) {
//        return HANDLERS.get(type);
        return type.guiHandlerI;
    }

    public static GuiHandlerI getGuiHandlerByPlayer(Player player) {
        PlayerState playerState = StateMachine.getState(player);
        GuiType guiType = HANDLERS.get(playerState);
        if (guiType == null) return null;
        return guiType.guiHandlerI;

    }

    //todo gui架构写好，同时检测玩家把reorder物品放入自己背包，比如click，drag啊，或者直接关闭物品栏让物品掉出来
}
