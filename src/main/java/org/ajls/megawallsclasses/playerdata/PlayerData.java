package org.ajls.megawallsclasses.playerdata;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.ajls.lib.Lib;
import org.ajls.megawallsclasses.MegaWallsClasses;
import org.ajls.megawallsclasses.customclasses.AbstractCustomClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerData {
    private Player player;
    private UUID uuid;
    private AbstractCustomClass customClass;


    public void setPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void tick() {
        if (spawnProtectionTicks > 0) {
            spawnProtectionTicks--;
//            if (spawnProtectionTicks == 0) {
//                this.spawnProtectionEnd();
//            }
        }
    }

    private int spawnProtectionTimestamp = 0;
    private int spawnProtectionTicks = 0;  //这里记录的是最大的时间，用来给事件查询玩家是否还在保护所以不能射箭
    ScheduledTask spawnProtectionTask; //这里就是复活的schedule task了
    public void setSpawnProtectionTask(ScheduledTask spawnProtectionTask) {
        this.spawnProtectionTask = spawnProtectionTask;
    }
//    public boolean isInSpawnProtectionTicks () {
//        return spawnProtectionTicks > 0;
//    }

    public void activeSpawnProtection() {
        spawnProtectionTimestamp = Bukkit.getCurrentTick() + 100;  //3秒显形2秒给对面反应
    }

    public boolean isInSpawnProtection() {
        return Bukkit.getCurrentTick() < spawnProtectionTimestamp;
    }

//    private void spawnProtectionEnd() {
//
//    }
    public void setSpawnProtectionTicks(int ticks) {
        spawnProtectionTicks = ticks;
    }
    private boolean justDied;
    public void setJustDied(boolean justDied) {
        this.justDied = justDied;
    }


}
