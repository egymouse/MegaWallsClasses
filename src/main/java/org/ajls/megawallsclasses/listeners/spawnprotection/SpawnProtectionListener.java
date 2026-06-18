package org.ajls.megawallsclasses.listeners.spawnprotection;

import org.ajls.megawallsclasses.listeners.BaseListener;
import org.ajls.megawallsclasses.playerdata.PlayerDataManager;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class SpawnProtectionListener extends BaseListener {

    @EventHandler(priority = EventPriority.LOWEST)  // 这个low其实会和Lib combatO冲突，导致玩家可能伤害并未超出无敌帧攻击。。。不对，反正没检查cancelled，都会触发，无论先后，只要你有战斗意愿就触发，
    //todo 不对，万一lib CombatO先触发，以为被打了好疼，结果是要被取消的 那不就爆炸了，又不能改normal，要不然僵尸受伤害就解决不了了。无法解决，只能通过自己造一个凌驾于插件优先级之上的数字优先级来解决 目前只能通过改成LOWEST来和取消横扫来随机抢占了 但是我目的是希望先横扫裁决再无敌裁决  当然之后才是cps裁决 然后正常攻击
    public void onPlayerDamage(EntityDamageByEntityEvent event) {  //解决剧毒药水还是有用
        // 获取造成伤害的根源实体（攻击者）
        Entity damagerEntity = event.getDamager();
//        Player attacker = null;
        Player attacker = getOwner(damagerEntity);
        Entity victimEntity = event.getEntity();

//        // 1. 情况一：直接物理攻击（拳头、剑等）
//        if (damagerEntity instanceof Player) {
//            attacker = (Player) damagerEntity;
//        }
//        // 2. 情况二：投掷物（箭、雪球、鸡蛋、喷溅药水等）
//        else if (damagerEntity instanceof Projectile) {
//            Projectile projectile = (Projectile) damagerEntity;
//            // 获取投掷者（可能是玩家，也可能是其他生物）
//            if (projectile.getShooter() instanceof Player) {
//                attacker = (Player) projectile.getShooter();
//            }
//        }
//        // 3. 情况三：滞留药水/瞬间伤害药水留下的区域效果云
//        else if (damagerEntity instanceof AreaEffectCloud) {
//            AreaEffectCloud cloud = (AreaEffectCloud) damagerEntity;
//            // 获取药水的拥有者（扔出药水的人）
//            if (cloud.getSource() instanceof Player) {
//                attacker = (Player) cloud.getSource();
//            }
//        }

        // 如果成功提取到了攻击者玩家，且该玩家处于复活保护状态，则取消伤害
        if (attacker != null) {
            // 使用你提供的方法进行判断
            if (PlayerDataManager.getPlayerData(attacker).isInSpawnProtection()) {
                event.setCancelled(true);

                // 可选：给攻击者提示（避免他以为游戏卡了）
                attacker.sendMessage("§c你刚复活，暂时无法造成伤害！");
            }

        }

        if (victimEntity instanceof Player) {  //如果是别人打正在复活保护的人
            Player victim = (Player) victimEntity;
            if (PlayerDataManager.getPlayerData(victim).isInSpawnProtection()) {
                event.setCancelled(true);
                if (attacker != null) {
                    attacker.sendMessage("§c对面刚复活，暂时无法对他造成伤害！");
                }
            }
        }


    }


    private Player getOwner(Entity entity) {
        // 1. 情况一：直接物理攻击（拳头、剑等）
        if (entity instanceof Player) {
            return (Player) entity;
        }
        // 2. 情况二：投掷物（箭、雪球、鸡蛋、喷溅药水等）
        else if (entity instanceof Projectile) {
            Projectile projectile = (Projectile) entity;
            // 获取投掷者（可能是玩家，也可能是其他生物）
            if (projectile.getShooter() instanceof Player) {
                return (Player) projectile.getShooter();
            }
        }
        // 3. 情况三：滞留药水/瞬间伤害药水留下的区域效果云
        else if (entity instanceof AreaEffectCloud) {
            AreaEffectCloud cloud = (AreaEffectCloud) entity;
            // 获取药水的拥有者（扔出药水的人）
            if (cloud.getSource() instanceof Player) {
                return  (Player) cloud.getSource();
            }
        }
        return null;
    }

}
